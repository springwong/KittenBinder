package com.example;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import kittenbinder.BindBackground;
import kittenbinder.BindContext;
import kittenbinder.BindEditText;
import kittenbinder.BindImageView;
import kittenbinder.BindLinearLayout;
import kittenbinder.BindPadding;
import kittenbinder.BindStyle;
import kittenbinder.BindTest;
import kittenbinder.BindTextAppearance;
import kittenbinder.BindTextView;
import kittenbinder.BindVisibility;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Dictionary;
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
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import static com.google.auto.common.MoreElements.getPackage;

@AutoService(Processor.class)
public class KittenBinderProcessor extends AbstractProcessor{
    private Filer filer;
    static final String VIEW_TYPE = "android.view.View";
    static final String IMAGE_VIEW_TYPE = "android.widget.ImageView";
    static final String TEXT_VIEW_TYPE = "android.widget.TextView";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations(){
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindContext.class);
        annotations.add(BindStyle.class);
        annotations.add(BindTextAppearance.class);
        annotations.add(BindPadding.class);
        annotations.add(BindBackground.class);
        annotations.add(BindVisibility.class);
        annotations.add(BindTextView.class);
        annotations.add(BindEditText.class);
        annotations.add(BindImageView.class);
        annotations.add(BindLinearLayout.class);
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
        Map<Class<? extends Annotation>, Map<Element, Object>> actions = generateActionMap(env);
        if(actions.size() == 0){
            return false;
        }
        JavaFile javaFile = JavaFile.builder(bindingClassName.packageName(), createType(0, bindingClassName, actions)).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
//            error(typeElement, "Unable to write binding for type %s: %s", typeElement, e.getMessage());
        }
        return false;
    }
    private Map<Class<? extends Annotation>, Map<Element, Object>> generateActionMap(RoundEnvironment env){
        Map<Class<? extends Annotation>, Map<Element, Object>> builderMap = new LinkedHashMap<>();

        for (Class<? extends  Annotation> classZ : getSupportedAnnotations()){
            Map<Element, Object> annotationMap = new LinkedHashMap<>();
            for(Element element : env.getElementsAnnotatedWith(classZ)){
                if(isSubtypeOfType(element.asType(), VIEW_TYPE)){
                    createBindingClassName(element);
                    annotationMap.put(element, element.getAnnotation(classZ));
                }else{
                    //todo : some warning
                }
            }
            builderMap.put(classZ, annotationMap);
        }

        return builderMap;
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
            if(entry.getKey() == BindVisibility.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()){
                    //subEntry.getValue() would be used when parameter exist
                    bindVisibility(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == BindPadding.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindPadding(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == BindBackground.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindBackground(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == BindImageView.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindImageView(methodBuilder, subEntry);
                }
            }
            if(entry.getKey() == BindTextView.class){
                for(Map.Entry<Element, Object> subEntry : entry.getValue().entrySet()) {
                    bindTextView(methodBuilder, subEntry);
                }
            }

        }
        result.addMethod(methodBuilder.build());

        return result.build();
    }
    private void bindEditText(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry) {

    }
    private void bindTextView(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry) {
        if(isSubtypeOfType(subEntry.getKey().asType(), TEXT_VIEW_TYPE)){
            BindTextView bind = (BindTextView) subEntry.getValue();
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
            BindImageView bind = (BindImageView) subEntry.getValue();
            if(bind.value() != -1){
                methodBuilder.addStatement("target.$L.setImageResource($L)", subEntry.getKey().getSimpleName(), bind.value());
            }
            methodBuilder.addStatement("target.$L.setAdjustViewBounds($L)", subEntry.getKey().getSimpleName(), bind.adjustViewBounds());
            methodBuilder.addStatement("target.$L.setScaleType(ImageView.ScaleType.$L)", subEntry.getKey().getSimpleName(), bind.scaleType());
        }
    }
    private void bindBackground(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry){
        BindBackground bind = (BindBackground) subEntry.getValue();
        if(bind.value() != -1){
            methodBuilder.addStatement("target.$L.setBackgroundResource($L)", subEntry.getKey().getSimpleName(), bind.value());
        }
    }
    private void bindContext(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry){
        methodBuilder.addStatement("target.$L = new $T(context)", subEntry.getKey().getSimpleName(), subEntry.getKey().asType());
    }
    private void bindVisibility(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry){
        BindVisibility bind = (BindVisibility) subEntry.getValue();
        switch (bind.value()){
            case View.VISIBLE:
                methodBuilder.addStatement("target.$L.setVisibility($L)", subEntry.getKey().getSimpleName(), "android.view.View.VISIBLE");
                break;
            case View.INVISIBLE:
                methodBuilder.addStatement("target.$L.setVisibility($L)", subEntry.getKey().getSimpleName(), "android.view.View.INVISIBLE");
                break;
            case View.GONE:
                methodBuilder.addStatement("target.$L.setVisibility($L)", subEntry.getKey().getSimpleName(), "android.view.View.GONE");
                break;
        }
    }
    private void bindPadding(MethodSpec.Builder methodBuilder, Map.Entry<Element, Object> subEntry){
        BindPadding bind = (BindPadding) subEntry.getValue();
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
