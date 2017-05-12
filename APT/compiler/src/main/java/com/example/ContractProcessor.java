package com.example;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by wing on 5/9/17.
 */
@AutoService(Processor.class) public class ContractProcessor extends AbstractProcessor {
  private Elements elementUtils;

  @Override
  public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    for (Element element : roundEnvironment.getElementsAnnotatedWith(Contract.class)) {

      String mainName = element.getSimpleName().toString().replace("Activity", "");
      TypeSpec contractType = TypeSpec.classBuilder(mainName + "Contract")
          .addType(TypeSpec.interfaceBuilder("Presenter").addModifiers(Modifier.PUBLIC).build())
          .addType(TypeSpec.interfaceBuilder("Model").addModifiers(Modifier.PUBLIC).build())
          .addType(TypeSpec.interfaceBuilder("View").addModifiers(Modifier.PUBLIC).build())
          .addModifiers(Modifier.PUBLIC)
          .build();

      TypeSpec presenterType = TypeSpec.classBuilder(mainName + "Presenter")
          .addSuperinterface(ClassName.get(elementUtils.getPackageOf(element).toString(),
              mainName + "Contract.Presenter"))
          .build();

      TypeSpec modelType = TypeSpec.classBuilder(mainName + "Model")
          .addSuperinterface(ClassName.get(elementUtils.getPackageOf(element).toString(),
              mainName + "Contract.Model"))
          .build();

      JavaFile contractFile =
          JavaFile.builder(elementUtils.getPackageOf(element).toString(), contractType).build();

      JavaFile presenterFile =
          JavaFile.builder(elementUtils.getPackageOf(element).toString(), presenterType).build();

      JavaFile modelFile =
          JavaFile.builder(elementUtils.getPackageOf(element).toString(), modelType).build();

      try {
        contractFile.writeTo(processingEnv.getFiler());

        presenterFile.writeTo(processingEnv.getFiler());

        modelFile.writeTo(processingEnv.getFiler());
      } catch (IOException e) {
        e.printStackTrace();
      }

      //生成injector
      List<? extends Element> members = elementUtils.getAllMembers((TypeElement) element);

      String fieldName = null;
      for (Element item : members) {
        Inject inject = item.getAnnotation(Inject.class);
        if (inject == null) {
          continue;
        }

        //这是注解了@Inject的属性名称
        fieldName = item.getSimpleName().toString();
        System.out.println("========>" + item.getSimpleName());
      }

      MethodSpec inject = MethodSpec.methodBuilder("inject")
          .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
          .addParameter(ClassName.get(element.asType()), "activity")
          .addCode("activity." + fieldName + "= new " + mainName + "Presenter();")
          .build();

      TypeSpec injector = TypeSpec.classBuilder(mainName + "Injector").addMethod(inject).build();

      JavaFile injectorFile =
          JavaFile.builder(elementUtils.getPackageOf(element).toString(), injector).build();

      try {
        injectorFile.writeTo(processingEnv.getFiler());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return true;
  }

  @Override public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(Contract.class.getCanonicalName());
  }

  @Override public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    elementUtils = processingEnv.getElementUtils();
  }
}
