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
package org.apache.sling.models.annotations.injectorspecific;

import org.jetbrains.annotations.NotNull;

public interface ExternalizedPathProvider {

    int FALLBACK_PRIORITY = -1;

    /** @return The priority used to select a provider when multiple are available. The one with the highest priority is selected
     *          In order for a custom Provider to be selected the priroty must be higher than the Fallback Priority
     **/
    int getPriority();

    /**
     * Externalizes a given Path
     * @param sourcePath The path to be externalized. If this value is null then null is returned
     * @param adaptable The adaptable source
     * @return The externalized path if there is a mapping or otherwise the same
     */
    String externalize(@NotNull Object adaptable, String sourcePath);
}
