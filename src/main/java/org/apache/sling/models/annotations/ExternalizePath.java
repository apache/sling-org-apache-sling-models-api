/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.models.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a given Property as Path to be shortened by a Externalized Path Provider.
 *
 * By default the Resource Resolver's map function is used but one can install custom Externalized Path Providers.
 * The registration of the providers is passive and dynamic meaning that the default provider is used until
 * other provider services are bound and then the one with the highest priority is selected
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface ExternalizePath {
    /**
     * @return Name of the method providing the resource. If not provided or this method or the resource field
     * does not provide a resource the 'getResource' is sued as fallback
     */
    public String resourceMethod() default "";

    /**
     * @return Name of the field providing the resource. If not provided or this field or the default method
     * (getResoruce()) is not a providing a resource then 'resource' is used as fallback
     */
    public String resourceField() default "";
}
