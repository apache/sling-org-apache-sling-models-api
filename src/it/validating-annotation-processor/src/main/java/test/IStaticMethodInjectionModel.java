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
package test;

import javax.inject.Inject;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;

@Model(adaptables = { Resource.class })
public interface IStaticMethodInjectionModel {

    @Inject
    static void inject(Resource resource) {

    }

    @ChildResource
    static void childResource(Resource resource) {

    }

    @OSGiService
    static void osgiService(ResourceResolverFactory resourceResolverFactory){

    }

    @RequestAttribute
    static void requestAttribute(Object object) {

    }

    @ResourcePath
    static void resourcePath(String path) {

    }

    @ScriptVariable
    static void scriptVariable(String variable) {

    }

    @Self
    static void self(Resource resource) {

    }

    @SlingObject
    static void slingObject(Resource resource) {

    }

    @ValueMapValue
    static void valueMapValue(Object value) {

    }

}