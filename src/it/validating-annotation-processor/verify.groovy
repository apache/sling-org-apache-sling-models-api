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
def buildLog = new File(basedir, 'build.log')

assert buildLog.exists()

def lines = buildLog.readLines()
def errors = lines.findAll({ it.contains('[ERROR]') })

def expectErrors = { classes, expectedMessages ->
    classes.forEach({ cls ->
        def messages = expectedMessages.clone()
        messages.removeAll({ message -> errors.findAll({ it.contains("/${cls}.java") && it.contains(message) }) })
        assert messages.empty: "did not find the following errors for class '$cls': $messages"
    })
}

def expectNoErrors = { classes ->
    classes.forEach({ cls ->
        assert !errors.findAll({ it.contains("/${cls}.java") }): "found errors for class '$cls'"
    })
}

expectErrors(
        ['StaticFieldInjectionModel', 'IStaticFieldInjectionModel'],
        [
                'Annotation javax.inject.Inject may not be used on static or final fields',
                'Annotation org.apache.sling.models.annotations.injectorspecific.ChildResource may not be used on static or final fields',
                'Annotation org.apache.sling.models.annotations.injectorspecific.OSGiService may not be used on static or final fields',
                'Annotation org.apache.sling.models.annotations.injectorspecific.RequestAttribute may not be used on static or final fields',
                'Annotation org.apache.sling.models.annotations.injectorspecific.ResourcePath may not be used on static or final fields',
                'Annotation org.apache.sling.models.annotations.injectorspecific.ScriptVariable may not be used on static or final fields',
                'Annotation org.apache.sling.models.annotations.injectorspecific.Self may not be used on static or final fields',
                'Annotation org.apache.sling.models.annotations.injectorspecific.SlingObject may not be used on static or final fields',
                'Annotation org.apache.sling.models.annotations.injectorspecific.ValueMapValue may not be used on static or final fields',
        ]
)

expectErrors(
        ['StaticMethodInjectionModel', 'IStaticMethodInjectionModel'],
        [
                'Annotation javax.inject.Inject may not be used on static methods',
                'Annotation org.apache.sling.models.annotations.injectorspecific.ChildResource may not be used on static methods',
                'Annotation org.apache.sling.models.annotations.injectorspecific.OSGiService may not be used on static methods',
                'Annotation org.apache.sling.models.annotations.injectorspecific.RequestAttribute may not be used on static methods',
                'Annotation org.apache.sling.models.annotations.injectorspecific.ResourcePath may not be used on static methods',
                'Annotation org.apache.sling.models.annotations.injectorspecific.ScriptVariable may not be used on static methods',
                'Annotation org.apache.sling.models.annotations.injectorspecific.Self may not be used on static methods',
                'Annotation org.apache.sling.models.annotations.injectorspecific.SlingObject may not be used on static methods',
                'Annotation org.apache.sling.models.annotations.injectorspecific.ValueMapValue may not be used on static methods',
        ]
)

expectErrors(
        ['FinalMemberInjectionModel'],
        ['Annotation javax.inject.Inject may not be used on static or final fields']
)

expectNoErrors(['MemberInjectionModel', 'NoModel'])
