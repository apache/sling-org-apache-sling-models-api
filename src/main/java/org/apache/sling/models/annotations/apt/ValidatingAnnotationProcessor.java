/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.models.annotations.apt;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import java.util.Set;

import org.apache.sling.models.annotations.Model;

/**
 * Annotation processor that implements compile-time validation for Sling Models
 */
@SupportedAnnotationTypes({
    "javax.inject.Inject",
    "org.apache.sling.models.annotations.injectorspecific.*",
})
public class ValidatingAnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(annotation)) {
                if (!isSlingModel(annotatedElement)) {
                    // skip any class that is not a Sling Model
                    continue;
                }

                if (isStaticOrFinalField(annotatedElement)) {
                    processingEnv
                            .getMessager()
                            .printMessage(
                                    Kind.ERROR,
                                    "Annotation " + annotation + " may not be used on static or final fields: "
                                            + getSymbolName(annotatedElement),
                                    annotatedElement);
                } else if (isStaticMethod(annotatedElement)) {
                    processingEnv
                            .getMessager()
                            .printMessage(
                                    Kind.ERROR,
                                    "Annotation " + annotation + " may not be used on static methods: "
                                            + getSymbolName(annotatedElement),
                                    annotatedElement);
                }
            }
        }

        return true;
    }

    private String getSymbolName(Element annotatedElement) {
        String name = annotatedElement.getEnclosingElement().getSimpleName().toString() + "#"
                + annotatedElement.getSimpleName().toString();
        if (annotatedElement.getKind() == ElementKind.METHOD) {
            name += "()";
        }
        return name;
    }

    private boolean isStaticOrFinalField(Element annotatedElement) {
        return (annotatedElement.getModifiers().contains(Modifier.STATIC)
                        || annotatedElement.getModifiers().contains(Modifier.FINAL))
                && annotatedElement.getKind() == ElementKind.FIELD;
    }

    private boolean isStaticMethod(Element annotatedElement) {
        return annotatedElement.getModifiers().contains(Modifier.STATIC)
                && annotatedElement.getKind() == ElementKind.METHOD;
    }

    private boolean isSlingModel(Element annotatedElement) {
        Element enclosingElement = annotatedElement.getEnclosingElement();

        // skip any occurrence where the enclosing element is not a class or interface, should not happen
        return (enclosingElement.getKind() == ElementKind.CLASS || enclosingElement.getKind() == ElementKind.INTERFACE)
                && enclosingElement.getAnnotation(Model.class) != null;
    }
}
