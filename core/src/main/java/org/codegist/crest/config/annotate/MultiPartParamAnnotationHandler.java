/*
 * Copyright 2010 CodeGist.org
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ===================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.config.annotate;

import org.codegist.crest.annotate.MultiPartParam;
import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;
import org.codegist.crest.config.ParamConfigBuilder;
import org.codegist.crest.config.ParamType;

import static org.codegist.crest.config.ParamType.FORM;
import static org.codegist.crest.config.ParamType.HEADER;
import static org.codegist.crest.util.MultiParts.toMetaDatas;

/**
 * @author laurent.gilles@codegist.org
 */
class MultiPartParamAnnotationHandler extends NoOpAnnotationHandler<MultiPartParam> {

    @Override
    public void handleInterfaceAnnotation(MultiPartParam annotation, InterfaceConfigBuilder builder) {
        builder.addMethodsExtraMultiPartParam(annotation.value(), annotation.defaultValue(), annotation.contentType(), annotation.fileName());
    }

    @Override
    public void handleMethodAnnotation(MultiPartParam annotation, MethodConfigBuilder builder) {
        builder.addExtraMultiPartParam(annotation.value(), annotation.defaultValue(), annotation.contentType(), annotation.fileName());
    }
                  
    @Override
    public void handleParameterAnnotation(MultiPartParam annotation, ParamConfigBuilder builder) {
        builder.setType(FORM)
                .setName(annotation.value())
                .setDefaultValue(annotation.defaultValue())
                .setMetaDatas(toMetaDatas(annotation.contentType(), annotation.fileName()));
    }
    
}