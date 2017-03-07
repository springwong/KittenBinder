package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import kittenbinder.BindTest;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
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
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations(){
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindTest.class);
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
        Map<Object, Element> actions = generateActionMap(env);
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
    private Map<Object, Element> generateActionMap(RoundEnvironment env){
        Map<Object, Element> builderMap = new LinkedHashMap<>();

        //a list of action map
        for(Element element : env.getElementsAnnotatedWith(BindTest.class)){
            if(isSubtypeOfType(element.asType(), VIEW_TYPE)){
                createBindingClassName(element);
                builderMap.put(element.getAnnotation(BindTest.class), element);
            }else{
                //todo : some warning
            }
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
    private TypeSpec createType(int sdk, ClassName bindingClassName, Map<Object, Element> maps){
        TypeSpec.Builder result = TypeSpec.classBuilder(bindingClassName.simpleName() + "_ViewDecorator");
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
                .addParameter(bindingClassName.box(), "target")
                .addParameter(ClassName.get("android.content", "Context"), "context")
                ;
        for(Map.Entry<Object, Element> entry : maps.entrySet()){
            if(entry.getKey() instanceof BindTest){
                methodBuilder.addCode("target.$L = new $T(context);\n", entry.getValue().getSimpleName(), entry.getValue().asType());
            }
        }
        result.addMethod(methodBuilder.build());

//                .addCode(variableElement.asType().toString() + " " + variableElement.getSimpleName() + " = new " + variableElement.asType().toString() + "(context);\n")
//                .build());
        return result.build();
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
