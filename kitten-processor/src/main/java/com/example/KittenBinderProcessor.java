package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import kittenbinder.DecoBackground;
import kittenbinder.BindContext;
import kittenbinder.DecoEditText;
import kittenbinder.DecoImageView;
import kittenbinder.DecoLinearLayout;
import kittenbinder.DecoPadding;
import kittenbinder.DecoStyle;
import kittenbinder.DecoTextAppearance;
import kittenbinder.DecoTextView;
import kittenbinder.DecoVisibility;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.google.auto.common.MoreElements.getPackage;

@AutoService(Processor.class)
public class KittenBinderProcessor extends AbstractProcessor{
    private Filer filer;
    static final String VIEW_TYPE = "android.view.View";
    static final String IMAGE_VIEW_TYPE = "android.widget.ImageView";
    static final String TEXT_VIEW_TYPE = "android.widget.TextView";
    static final String Linear_LAYOUT_TYPE = "android.widget.LinearLayout";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations(){
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindContext.class);
        annotations.add(DecoStyle.class);
        annotations.add(DecoTextAppearance.class);
        annotations.add(DecoPadding.class);
        annotations.add(DecoBackground.class);
        annotations.add(DecoVisibility.class);
        annotations.add(DecoTextView.class);
        annotations.add(DecoEditText.class);
        annotations.add(DecoImageView.class);
        annotations.add(DecoLinearLayout.class);
        return annotations;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    enum BindingAction{
        Context
    }
    ClassName bindingClassName = null;
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Map<TypeElement,  Map<Class<? extends Annotation>, Map<Element, Object>>> actions = generateActionMap(env);
        if(actions.size() == 0){
            return false;
        }
        for (Map.Entry<TypeElement, Map<Class<? extends Annotation>, Map<Element, Object>>> entry : actions.entrySet()) {
            ClassName className = createClassName(entry.getKey());
            if(className != null) {
                JavaFile javaFile = JavaFile.builder(className.packageName(), createType(0, className, entry.getValue()))
                        .addStaticImport(ClassName.get("com.spring.kittenbinder.binding", "KittenBind"), "setStyle")
                        .build();
                try {
                    javaFile.writeTo(filer);
                } catch (IOException e) {
//            error(typeElement, "Unable to write binding for type %s: %s", typeElement, e.getMessage());
                }
            }
        }
        return false;
    }
    private Map<TypeElement,  Map<Class<? extends Annotation>, Map<Element, Object>>> generateActionMap(RoundEnvironment env){
        Map<TypeElement,  Map<Class<? extends Annotation>, Map<Element, Object>>> typeAnnotationMap = new LinkedHashMap<>();

        for (Class<? extends  Annotation> classZ : getSupportedAnnotations()){
            for(Element element : env.getElementsAnnotatedWith(classZ)){
                if(isSubtypeOfType(element.asType(), VIEW_TYPE)){
                    Map<Class<? extends Annotation>, Map<Element, Object>> annotationElementMap = null;
                    if(!typeAnnotationMap.containsKey(element.getEnclosingElement())){
                        annotationElementMap = new LinkedHashMap<>();
                        typeAnnotationMap.put((TypeElement)element.getEnclosingElement(), annotationElementMap);
                    }else{
                        annotationElementMap = typeAnnotationMap.get(element.getEnclosingElement());
                    }
                    Map<Element, Object> elementAnnotationObjectMap = null;
                    if(!annotationElementMap.containsKey(classZ)){
                        elementAnnotationObjectMap = new LinkedHashMap<>();
                        annotationElementMap.put(classZ, elementAnnotationObjectMap);
                    }else{
                        elementAnnotationObjectMap = annotationElementMap.get(classZ);
                    }
                    elementAnnotationObjectMap.put(element, element.getAnnotation(classZ));
                }else{
                    //todo : some warning
                }
            }
        }

        return typeAnnotationMap;
    }
    private ClassName createClassName(TypeElement element){
        String packageName = getPackage((Element)element).getQualifiedName().toString();
        String className = element.getQualifiedName().toString().substring(
                packageName.length() + 1).replace('.', '$');
        return ClassName.get(packageName, className);
    }
    private void createBindingClassName(Element element){
        if(bindingClassName == null){
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String packageName = getPackage((Element)enclosingElement).getQualifiedName().toString();
            String className = enclosingElement.getQualifiedName().toString().substring(
                    packageName.length() + 1).replace('.', '$');
            bindingClassName = ClassName.get(packageName, className);
        }
    }
    private TypeSpec createType(int sdk, ClassName bindingClassName, Map<Class<? extends Annotation>, Map<Element, Object>> maps){
        TypeSpec.Builder result = TypeSpec.classBuilder(bindingClassName.simpleName() + "_ViewDecorator");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
                .addParameter(bindingClassName.box(), "target")
                .addParameter(ClassName.get("android.content", "Context"), "context")
                ;
        for(Map.Entry<Class<? extends Annotation>, Map<Element, Object>> entry : maps.entrySet()){
            methodBuilder.addComment("Decorate Annotation : $L", entry.getKey().getSimpleName());
            if(entry.getKey() == BindContext.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()){
                    //subEntry.getValue() would be used when parameter exist
                    bindContext(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == DecoStyle.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()){
                    //subEntry.getValue() would be used when parameter exist
                    DecoStyle(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == DecoTextAppearance.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()){
                    //subEntry.getValue() would be used when parameter exist
                    bindTextAppearance(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == DecoVisibility.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()){
                    //subEntry.getValue() would be used when parameter exist
                    bindVisibility(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == DecoPadding.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindPadding(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == DecoBackground.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindBackground(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == DecoImageView.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindImageView(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == DecoTextView.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindTextView(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == DecoEditText.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindEditText(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == DecoLinearLayout.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindLinearLayout(methodBuilder, subEntry);
                }
            }

        }
        result.addMethod(methodBuilder.build());

        return result.build();
    }

    private void DecoStyle(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry) {
        DecoStyle bind = (DecoStyle) subEntry.getValue();
        if(bind.value() != -1){
            methodBuilder.addStatement("setStyle(target.$L, $L, context)", subEntry.getKey().getSimpleName(), bind.value());
        }
    }
    private void bindLinearLayout(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry) {
        if (isSubtypeOfType(subEntry.getKey().asType(), Linear_LAYOUT_TYPE)){
            DecoLinearLayout bind = (DecoLinearLayout) subEntry.getValue();
            methodBuilder.addStatement("target.$L.setOrientation($L)", subEntry.getKey().getSimpleName(), bind.value());
        }
    }
    private void bindTextAppearance(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry) {
        if (isSubtypeOfType(subEntry.getKey().asType(), TEXT_VIEW_TYPE)) {
            DecoTextAppearance bind = (DecoTextAppearance) subEntry.getValue();
            if(bind.value() != -1){
                methodBuilder.addStatement("target.$L.setTextAppearance(context, $L)", subEntry.getKey().getSimpleName(), bind.value());
            }
        }
    }
    private void bindEditText(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry) {
        if (isSubtypeOfType(subEntry.getKey().asType(), TEXT_VIEW_TYPE)) {
            DecoEditText bind = (DecoEditText) subEntry.getValue();
            if(bind.hint() != -1){
                methodBuilder.addStatement("target.$L.setHint($L)", subEntry.getKey().getSimpleName(), bind.hint());
            }
            methodBuilder.addStatement("target.$L.setSingleLine($L)", subEntry.getKey().getSimpleName(), bind.isSingleLine());
        }
    }
    private void bindTextView(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry) {
        if(isSubtypeOfType(subEntry.getKey().asType(), TEXT_VIEW_TYPE)){
            DecoTextView bind = (DecoTextView) subEntry.getValue();
            if(bind.value() != -1){
                methodBuilder.addStatement("target.$L.setText($L)", subEntry.getKey().getSimpleName(), bind.value());
            }
            if(bind.textSize() != -1){
                methodBuilder.addStatement("target.$L.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension($L))", subEntry.getKey().getSimpleName(), bind.textSize());
            }
            if(bind.textColor() != -1){
                methodBuilder.addStatement("target.$L.setTextColor(context.getResources().getColor($L, context.getTheme()))", subEntry.getKey().getSimpleName(), bind.textColor());
            }
            if(bind.lines()!=-1){
                methodBuilder.addStatement("target.$L.setLines($L)", subEntry.getKey().getSimpleName(), bind.lines());
            }
            if(bind.minLines()!=-1){
                methodBuilder.addStatement("target.$L.setMinLines($L)", subEntry.getKey().getSimpleName(), bind.minLines());
            }
            if(bind.maxLines()!=-1){
                methodBuilder.addStatement("target.$L.setMaxLines($L)", subEntry.getKey().getSimpleName(), bind.maxLines());
            }
            if(bind.gravity() != -1){
                methodBuilder.addStatement("target.$L.setGravity($L)", subEntry.getKey().getSimpleName(), bind.gravity());
            }
        }
    }
    private void bindImageView(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry) {
        if(isSubtypeOfType(subEntry.getKey().asType(), IMAGE_VIEW_TYPE)){
            DecoImageView bind = (DecoImageView) subEntry.getValue();
            if(bind.value() != -1){
                methodBuilder.addStatement("target.$L.setImageResource($L)", subEntry.getKey().getSimpleName(), bind.value());
            }
            methodBuilder.addStatement("target.$L.setAdjustViewBounds($L)", subEntry.getKey().getSimpleName(), bind.adjustViewBounds());
            methodBuilder.addStatement("target.$L.setScaleType(ImageView.ScaleType.$L)", subEntry.getKey().getSimpleName(), bind.scaleType());
        }
    }
    private void bindBackground(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry){
        DecoBackground bind = (DecoBackground) subEntry.getValue();
        if(bind.value() != -1){
            methodBuilder.addStatement("target.$L.setBackgroundResource($L)", subEntry.getKey().getSimpleName(), bind.value());
        }
    }
    private void bindContext(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry){
        methodBuilder.addStatement("target.$L = new $T(context)", subEntry.getKey().getSimpleName(), subEntry.getKey().asType());
    }
    private void bindVisibility(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry){
        DecoVisibility bind = (DecoVisibility) subEntry.getValue();
        switch (bind.value()){
            case 0:
                methodBuilder.addStatement("target.$L.setVisibility($L)", subEntry.getKey().getSimpleName(), "android.view.View.VISIBLE");
                break;
            case 4:
                methodBuilder.addStatement("target.$L.setVisibility($L)", subEntry.getKey().getSimpleName(), "android.view.View.INVISIBLE");
                break;
            case 8:
                methodBuilder.addStatement("target.$L.setVisibility($L)", subEntry.getKey().getSimpleName(), "android.view.View.GONE");
                break;
        }
    }
    private void bindPadding(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry){
        DecoPadding bind = (DecoPadding) subEntry.getValue();
        if(bind.value() != Integer.MIN_VALUE
                || bind.left() != Integer.MIN_VALUE
                || bind.top() != Integer.MIN_VALUE
                || bind.right() != Integer.MIN_VALUE
                || bind.bottom() != Integer.MIN_VALUE){
            methodBuilder.addCode("{\n");
            methodBuilder.addStatement("int left = target.$L.getLeft()", subEntry.getKey().getSimpleName());
            methodBuilder.addStatement("int top = target.$L.getTop()", subEntry.getKey().getSimpleName());
            methodBuilder.addStatement("int right = target.$L.getRight()", subEntry.getKey().getSimpleName());
            methodBuilder.addStatement("int bottom = target.$L.getBottom()", subEntry.getKey().getSimpleName());
            if(bind.value() != Integer.MIN_VALUE){
                methodBuilder.addStatement("int padding = (int)android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, $L, context.getResources().getDisplayMetrics())", bind.value());
                methodBuilder.addStatement("left = padding");
                methodBuilder.addStatement("top = padding");
                methodBuilder.addStatement("right = padding");
                methodBuilder.addStatement("bottom = padding");
            }
            if(bind.left() != Integer.MIN_VALUE){
                methodBuilder.addStatement("left = (int)android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, $L, context.getResources().getDisplayMetrics()" , bind.left());
            }
            if(bind.right() != Integer.MIN_VALUE){
                methodBuilder.addStatement("right = (int)android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, $L, context.getResources().getDisplayMetrics()" , bind.right());
            }
            if(bind.top() != Integer.MIN_VALUE){
                methodBuilder.addStatement("top = (int)android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, $L, context.getResources().getDisplayMetrics()" , bind.top());
            }
            if(bind.bottom() != Integer.MIN_VALUE) {
                methodBuilder.addStatement("bottom = (int)android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, $L, context.getResources().getDisplayMetrics()", bind.bottom());
            }
            methodBuilder.addStatement("target.$L.setPadding(left, top, right, bottom)", subEntry.getKey().getSimpleName());

            methodBuilder.addCode("}\n");
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    static boolean isSubtypeOfType(TypeMirror typeMirror, String otherType) {
        if (isTypeEqual(typeMirror, otherType)) {
            return true;
        }
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return false;
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() > 0) {
            StringBuilder typeString = new StringBuilder(declaredType.asElement().toString());
            typeString.append('<');
            for (int i = 0; i < typeArguments.size(); i++) {
                if (i > 0) {
                    typeString.append(',');
                }
                typeString.append('?');
            }
            typeString.append('>');
            if (typeString.toString().equals(otherType)) {
                return true;
            }
        }
        Element element = declaredType.asElement();
        if (!(element instanceof TypeElement)) {
            return false;
        }
        TypeElement typeElement = (TypeElement) element;
        TypeMirror superType = typeElement.getSuperclass();
        if (isSubtypeOfType(superType, otherType)) {
            return true;
        }
        for (TypeMirror interfaceType : typeElement.getInterfaces()) {
            if (isSubtypeOfType(interfaceType, otherType)) {
                return true;
            }
        }
        return false;
    }
    private static boolean isTypeEqual(TypeMirror typeMirror, String otherType) {
        return otherType.equals(typeMirror.toString());
    }
}
