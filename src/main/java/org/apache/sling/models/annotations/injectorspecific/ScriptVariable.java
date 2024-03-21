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
package org.apache.sling.models.annotations.injectorspecific;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.spi.injectorspecific.InjectAnnotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to be used on either methods, fields or constructor parameters to let Sling Models inject a
 * script variable (from the org.apache.sling.api.scripting.SlingBindings)
 *
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@InjectAnnotation
@Source("script-bindings")
public @interface ScriptVariable {
    /**
     * Specifies the name of the script variable.
     * If empty or not set, then the name is derived from the method or field.
     * @return Name
     */
    public String name() default "";

    /**
     * If set to true, the model can be instantiated even if there is no
     * scripting value with the specified name.
     * Default = false.
     * @return Optional
     * @deprecated Use {@link #injectionStrategy} instead.
     */
    @Deprecated
    public boolean optional() default false;

    /**
     * Specifies the injection strategy applied to an annotated element:
     * <ul>
     * <li>If set to {@link InjectionStrategy#REQUIRED}, injection is mandatory.</li>
     * <li>If set to {@link InjectionStrategy#OPTIONAL}, injection is optional.</li>
     * <li>If set to {@link InjectionStrategy#DEFAULT} (default), the default injection strategy defined on the
     * {@link org.apache.sling.models.annotations.Model} applies.</li>
     * </ul>
     * WARNING: Injection strategy is ignored if either {@link org.apache.sling.models.annotations.Optional}
     * or {@link org.apache.sling.models.annotations.Required} is applied on the same element.
     * @return Injection strategy
     */
    public InjectionStrategy injectionStrategy() default InjectionStrategy.DEFAULT;
}
