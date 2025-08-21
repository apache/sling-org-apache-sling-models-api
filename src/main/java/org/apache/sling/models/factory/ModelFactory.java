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
package org.apache.sling.models.factory;

import java.util.Map;

import org.apache.sling.api.SlingJakartaHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

/**
 * The ModelFactory instantiates Sling Model classes similar to #adaptTo but will throw an exception in case
 * instantiation fails for some reason.
 */
@ProviderType
public interface ModelFactory {

    /**
     * Instantiates the given Sling Model class from the given adaptable.
     * @param adaptable the adaptable to use to instantiate the Sling Model Class
     * @param type the class to instantiate
     * @param <ModelType> Model type
     * @return a new instance for the required model (never {@code null})
     * @throws MissingElementsException in case no injector was able to inject some required values with the given types
     * @throws InvalidAdaptableException in case the given class cannot be instantiated from the given adaptable (different adaptable on the model annotation)
     * @throws ModelClassException in case the model could not be instantiated because model annotation was missing, reflection failed, no valid constructor was found, model was not registered as adapter factory yet, or post-construct could not be called
     * @throws PostConstructException in case the post-construct method has thrown an exception itself
     * @throws ValidationException in case validation could not be performed for some reason (e.g. no validation information available)
     * @throws InvalidModelException in case the given model type could not be validated through the model validation
     */
    public @NotNull <ModelType> ModelType createModel(@NotNull Object adaptable, @NotNull Class<ModelType> type)
            throws MissingElementsException, InvalidAdaptableException, ModelClassException, PostConstructException,
                    ValidationException, InvalidModelException;

    /**
     * Create a wrapped request object with the specified resource and instantiates the given Sling Model class from that wrapped request. The wrapped request
     * object will have a fresh set of script bindings so that any injected bindings references have the correct context.
     *
     * @param request the current request
     * @param resource the resource to set as the wrapped request's resource
     * @param targetClass the class to instantiate
     * @param <T> Model type
     * @return a new instance for the required model (never {@code null})
     * @throws MissingElementsException in case no injector was able to inject some required values with the given types
     * @throws InvalidAdaptableException in case the given class cannot be instantiated from the given adaptable (different adaptable on the model annotation)
     * @throws ModelClassException in case the model could not be instantiated because model annotation was missing, reflection failed, no valid constructor was found, model was not registered as adapter factory yet, or post-construct could not be called
     * @throws PostConstructException in case the post-construct method has thrown an exception itself
     * @throws ValidationException in case validation could not be performed for some reason (e.g. no validation information available)
     * @throws InvalidModelException in case the given model type could not be validated through the model validation
     * @since 1.5.0 (Models API Bundle 1.4.4)
     * @see #getModelFromWrappedRequest(SlingHttpServletRequest, Resource, Class)
     * @deprecated use {@link #createModelFromWrappedRequest(SlingJakartaHttpServletRequest, Resource, Class)} instead
     */
    @Deprecated(since = "1.6.0")
    public @NotNull <T> T createModelFromWrappedRequest(
            @NotNull org.apache.sling.api.SlingHttpServletRequest request,
            @NotNull Resource resource,
            @NotNull Class<T> targetClass);

    /**
     * Create a wrapped request object with the specified resource and instantiates the given Sling Model class from that wrapped request. The wrapped request
     * object will have a fresh set of script bindings so that any injected bindings references have the correct context.
     *
     * @param request the current request
     * @param resource the resource to set as the wrapped request's resource
     * @param targetClass the class to instantiate
     * @param <T> Model type
     * @return a new instance for the required model (never {@code null})
     * @throws MissingElementsException in case no injector was able to inject some required values with the given types
     * @throws InvalidAdaptableException in case the given class cannot be instantiated from the given adaptable (different adaptable on the model annotation)
     * @throws ModelClassException in case the model could not be instantiated because model annotation was missing, reflection failed, no valid constructor was found, model was not registered as adapter factory yet, or post-construct could not be called
     * @throws PostConstructException in case the post-construct method has thrown an exception itself
     * @throws ValidationException in case validation could not be performed for some reason (e.g. no validation information available)
     * @throws InvalidModelException in case the given model type could not be validated through the model validation
     * @since 1.5.0 (Models API Bundle 1.4.4)
     * @see #getModelFromWrappedRequest(SlingJakartaHttpServletRequest, Resource, Class)
     */
    public @NotNull <T> T createModelFromWrappedRequest(
            @NotNull SlingJakartaHttpServletRequest request, @NotNull Resource resource, @NotNull Class<T> targetClass);

    /**
     *
     * @param adaptable the adaptable to check
     * @param type the class to check
     * @return {@code true} in case the given class can be created from the given adaptable, otherwise {@code false}
     */
    public boolean canCreateFromAdaptable(@NotNull Object adaptable, @NotNull Class<?> type);

    /**
     *
     * @param adaptable the adaptable to check
     * @param type the class to check
     * @return false in case no class with the Model annotation adapts to the requested type
     *
     * @see org.apache.sling.models.annotations.Model
     * @deprecated Use {@link #isModelClass(Class)} instead!
     */
    @Deprecated(since = "1.1.0")
    public boolean isModelClass(@NotNull Object adaptable, @NotNull Class<?> type);

    /**
     * Checks if a given type can be instantiated though Sling Models. This checks that
     * <ul>
     * <li>there is a class annotated with <code>Model</code> which adapts to the given type</li>
     * <li>this class is registered as Sling Model (i.e. the package is listed in the "Sling-Model-Packages" header from the bundles manifest and has been picked up already by the bundle listener)</li>
     * </ul>
     * Only if both conditions are fulfilled this method will return {@code true}.
     * @param type the class to check
     * @return {@code true} in case the given type can be instantiated though Sling Models.
     *
     */
    public boolean isModelClass(@NotNull Class<?> type);

    /**
     * Determine is a model class is available for the resource's resource type.
     *
     * @param resource a resource
     * @return {@code true} if a model class is mapped to the resource type
     */
    public boolean isModelAvailableForResource(@NotNull Resource resource);

    /**
     * Determine is a model class is available for the request's resource's resource type.
     *
     * @param request a request
     * @return {@code true} if a model class is mapped to the resource type
     * @deprecated use {@link #isModelAvailableForRequest(SlingJakartaHttpServletRequest)} instead
     */
    @Deprecated(since = "1.6.0")
    public boolean isModelAvailableForRequest(@NotNull org.apache.sling.api.SlingHttpServletRequest request);

    /**
     * Determine is a model class is available for the request's resource's resource type.
     *
     * @param request a request
     * @return {@code true} if a model class is mapped to the resource type
     */
    public boolean isModelAvailableForRequest(@NotNull SlingJakartaHttpServletRequest request);

    /**
     * Obtain an adapted model class based on the resource type of the provided resource.
     *
     * @param resource a resource
     * @return an adapted model object
     * @throws MissingElementsException in case no injector was able to inject some required values with the given types
     * @throws InvalidAdaptableException in case the given class cannot be instantiated from the given adaptable (different adaptable on the model annotation)
     * @throws ModelClassException in case the model could not be instantiated because model annotation was missing, reflection failed, no valid constructor was found, model was not registered as adapter factory yet, or post-construct could not be called
     * @throws PostConstructException in case the post-construct method has thrown an exception itself
     * @throws ValidationException in case validation could not be performed for some reason (e.g. no validation information available)
     * @throws InvalidModelException in case the given model type could not be validated through the model validation
     */
    public @NotNull Object getModelFromResource(@NotNull Resource resource)
            throws MissingElementsException, InvalidAdaptableException, ModelClassException, PostConstructException,
                    ValidationException, InvalidModelException;

    /**
     * Obtain an adapted model class based on the resource type of the request's resource.
     *
     * @param request a request
     * @return an adapted model object
     * @throws MissingElementsException in case no injector was able to inject some required values with the given types
     * @throws InvalidAdaptableException in case the given class cannot be instantiated from the given adaptable (different adaptable on the model annotation)
     * @throws ModelClassException in case the model could not be instantiated because model annotation was missing, reflection failed, no valid constructor was found, model was not registered as adapter factory yet, or post-construct could not be called
     * @throws PostConstructException in case the post-construct method has thrown an exception itself
     * @throws ValidationException in case validation could not be performed for some reason (e.g. no validation information available)
     * @throws InvalidModelException in case the given model type could not be validated through the model validation
     * @deprecated use {@link #getModelFromRequest(SlingJakartaHttpServletRequest)} instead
     */
    @Deprecated(since = "1.6.0")
    public @NotNull Object getModelFromRequest(@NotNull org.apache.sling.api.SlingHttpServletRequest request)
            throws MissingElementsException, InvalidAdaptableException, ModelClassException, PostConstructException,
                    ValidationException, InvalidModelException;

    /**
     * Obtain an adapted model class based on the resource type of the request's resource.
     *
     * @param request a request
     * @return an adapted model object
     * @throws MissingElementsException in case no injector was able to inject some required values with the given types
     * @throws InvalidAdaptableException in case the given class cannot be instantiated from the given adaptable (different adaptable on the model annotation)
     * @throws ModelClassException in case the model could not be instantiated because model annotation was missing, reflection failed, no valid constructor was found, model was not registered as adapter factory yet, or post-construct could not be called
     * @throws PostConstructException in case the post-construct method has thrown an exception itself
     * @throws ValidationException in case validation could not be performed for some reason (e.g. no validation information available)
     * @throws InvalidModelException in case the given model type could not be validated through the model validation
     */
    public @NotNull Object getModelFromRequest(@NotNull SlingJakartaHttpServletRequest request)
            throws MissingElementsException, InvalidAdaptableException, ModelClassException, PostConstructException,
                    ValidationException, InvalidModelException;

    /**
     * Export the model object using the defined target class using the named exporter.
     *
     * @param model the model object
     * @param exporterName the exporter name
     * @param targetClass the target class
     * @param options any exporter options
     * @param <T> the target class
     * @return an instance of the target class
     * @throws ExportException if the export fails
     * @throws MissingExporterException if the named exporter can't be found
     */
    public @NotNull <T> T exportModel(
            @NotNull Object model,
            @NotNull String exporterName,
            @NotNull Class<T> targetClass,
            @NotNull Map<String, String> options)
            throws ExportException, MissingExporterException;

    /**
     * Export the model object registered to the resource's type using the defined target class using the named exporter.
     *
     * @param resource the resource
     * @param exporterName the exporter name
     * @param targetClass the target class
     * @param options any exporter options
     * @param <T> the target class
     * @return an instance of the target class
     * @throws MissingElementsException in case no injector was able to inject some required values with the given types
     * @throws InvalidAdaptableException in case the given class cannot be instantiated from the given adaptable (different adaptable on the model annotation)
     * @throws ModelClassException in case the model could not be instantiated because model annotation was missing, reflection failed, no valid constructor was found, model was not registered as adapter factory yet, or post-construct could not be called
     * @throws PostConstructException in case the post-construct method has thrown an exception itself
     * @throws ValidationException in case validation could not be performed for some reason (e.g. no validation information available)
     * @throws InvalidModelException in case the given model type could not be validated through the model validation
     * @throws ExportException if the export fails
     * @throws MissingExporterException if the named exporter can't be found
     */
    public @NotNull <T> T exportModelForResource(
            @NotNull Resource resource,
            @NotNull String exporterName,
            @NotNull Class<T> targetClass,
            @NotNull Map<String, String> options)
            throws MissingElementsException, InvalidAdaptableException, ModelClassException, PostConstructException,
                    ValidationException, InvalidModelException, ExportException, MissingExporterException;

    /**
     * Export the model object registered to the request's resource's type using the defined target class using the named exporter.
     *
     * @param request the request
     * @param exporterName the exporter name
     * @param targetClass the target class
     * @param options any exporter options
     * @param <T> the target class
     * @return an instance of the target class
     * @throws MissingElementsException in case no injector was able to inject some required values with the given types
     * @throws InvalidAdaptableException in case the given class cannot be instantiated from the given adaptable (different adaptable on the model annotation)
     * @throws ModelClassException in case the model could not be instantiated because model annotation was missing, reflection failed, no valid constructor was found, model was not registered as adapter factory yet, or post-construct could not be called
     * @throws PostConstructException in case the post-construct method has thrown an exception itself
     * @throws ValidationException in case validation could not be performed for some reason (e.g. no validation information available)
     * @throws InvalidModelException in case the given model type could not be validated through the model validation
     * @throws ExportException if the export fails
     * @throws MissingExporterException if the named exporter can't be found
     * @deprecated use {@link #exportModelForRequest(SlingJakartaHttpServletRequest, String, Class, Map)} instead
     */
    @Deprecated(since = "1.6.0")
    public @NotNull <T> T exportModelForRequest(
            @NotNull org.apache.sling.api.SlingHttpServletRequest request,
            @NotNull String exporterName,
            @NotNull Class<T> targetClass,
            @NotNull Map<String, String> options)
            throws MissingElementsException, InvalidAdaptableException, ModelClassException, PostConstructException,
                    ValidationException, InvalidModelException, ExportException, MissingExporterException;

    /**
     * Export the model object registered to the request's resource's type using the defined target class using the named exporter.
     *
     * @param request the request
     * @param exporterName the exporter name
     * @param targetClass the target class
     * @param options any exporter options
     * @param <T> the target class
     * @return an instance of the target class
     * @throws MissingElementsException in case no injector was able to inject some required values with the given types
     * @throws InvalidAdaptableException in case the given class cannot be instantiated from the given adaptable (different adaptable on the model annotation)
     * @throws ModelClassException in case the model could not be instantiated because model annotation was missing, reflection failed, no valid constructor was found, model was not registered as adapter factory yet, or post-construct could not be called
     * @throws PostConstructException in case the post-construct method has thrown an exception itself
     * @throws ValidationException in case validation could not be performed for some reason (e.g. no validation information available)
     * @throws InvalidModelException in case the given model type could not be validated through the model validation
     * @throws ExportException if the export fails
     * @throws MissingExporterException if the named exporter can't be found
     */
    public @NotNull <T> T exportModelForRequest(
            @NotNull SlingJakartaHttpServletRequest request,
            @NotNull String exporterName,
            @NotNull Class<T> targetClass,
            @NotNull Map<String, String> options)
            throws MissingElementsException, InvalidAdaptableException, ModelClassException, PostConstructException,
                    ValidationException, InvalidModelException, ExportException, MissingExporterException;

    /**
     * Create a wrapped request object with the specified resource and (try to) adapt the request object into the specified class. The wrapped request
     * object will have a fresh set of script bindings so that any injected bindings references have the correct context.
     * Consider using {@link #createModelFromWrappedRequest(SlingHttpServletRequest, Resource, Class)} instead to get exceptions propagated which may occur when trying to create the model.
     *
     * @param request the current request
     * @param resource the resource to set as the wrapped request's resource
     * @param targetClass the target adapter class
     * @param <T> the target adapter class
     * @return an instance of the target class or null if the adaptation could not be done
     * @since 1.4.0 (Models API Bundle 1.3.6)
     * @deprecated use {@link #getModelFromWrappedRequest(SlingJakartaHttpServletRequest, Resource, Class)} instead
     */
    @Deprecated(since = "1.6.0")
    public @Nullable <T> T getModelFromWrappedRequest(
            @NotNull org.apache.sling.api.SlingHttpServletRequest request,
            @NotNull Resource resource,
            @NotNull Class<T> targetClass);

    /**
     * Create a wrapped request object with the specified resource and (try to) adapt the request object into the specified class. The wrapped request
     * object will have a fresh set of script bindings so that any injected bindings references have the correct context.
     * Consider using {@link #createModelFromWrappedRequest(SlingHttpServletRequest, Resource, Class)} instead to get exceptions propagated which may occur when trying to create the model.
     *
     * @param request the current request
     * @param resource the resource to set as the wrapped request's resource
     * @param targetClass the target adapter class
     * @param <T> the target adapter class
     * @return an instance of the target class or null if the adaptation could not be done
     * @since 1.4.0 (Models API Bundle 1.3.6)
     *
     */
    public @Nullable <T> T getModelFromWrappedRequest(
            @NotNull SlingJakartaHttpServletRequest request, @NotNull Resource resource, @NotNull Class<T> targetClass);
}
