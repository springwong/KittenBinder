package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import kittenbinder.BindTest;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
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
import javax.lang.model.util.ElementFilter;

import static com.google.auto.common.MoreElements.getPackage;

@AutoService(Processor.class)
public class KittenBinderProcessor extends AbstractProcessor{
    private Filer filer;

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
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        Collection<? extends Element> annotatedElements =  env.getElementsAnnotatedWith(BindTest.class);
        List<VariableElement> fields = ElementFilter.fieldsIn(annotatedElements);

        ClassName bindingClassName = null;
        for(VariableElement element : fields){
//            element
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            String packageName = getPackage((Element)enclosingElement).getQualifiedName().toString();
            String className = enclosingElement.getQualifiedName().toString().substring(
                    packageName.length() + 1).replace('.', '$');
            bindingClassName = ClassName.get(packageName, className + "_ViewDecorator");
        }
        JavaFile javaFile = JavaFile.builder(bindingClassName.packageName(), createType(0, bindingClassName)).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
//            error(typeElement, "Unable to write binding for type %s: %s", typeElement, e.getMessage());
        }
        return false;
    }

    private TypeSpec createType(int sdk, ClassName bindingClassName){
        TypeSpec.Builder result = TypeSpec.classBuilder(bindingClassName.simpleName());


        return result.build();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    //    static final String VIEW_TYPE = "android.view.View";
//    private void handleEnclosedElement(TypeElement enclosingElement){
//        TypeMirror typeMirror = enclosingElement.asType();
//    }
}
