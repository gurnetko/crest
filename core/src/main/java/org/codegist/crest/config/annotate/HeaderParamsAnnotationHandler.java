/*
 * Copyright 2011 CodeGist.org
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

import org.codegist.crest.CRestConfig;
import org.codegist.crest.annotate.HeaderParam;
import org.codegist.crest.annotate.HeaderParams;
import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;

/**
 * @author laurent.gilles@codegist.org
 */
class HeaderParamsAnnotationHandler extends NoOpAnnotationHandler<HeaderParams> {

    private final HeaderParamAnnotationHandler handler;

    public HeaderParamsAnnotationHandler(HeaderParamAnnotationHandler handler) {
        this.handler = handler;
    }

    public HeaderParamsAnnotationHandler(CRestConfig crestConfig) {
        this(new HeaderParamAnnotationHandler(crestConfig));
    }

    @Override
    public void handleInterfaceAnnotation(HeaderParams annotation, InterfaceConfigBuilder builder) {
        for(HeaderParam paramAnnotation : annotation.value()){
            handler.handleInterfaceAnnotation(paramAnnotation, builder);
        }
    }

    @Override
    public void handleMethodAnnotation(HeaderParams annotation, MethodConfigBuilder builder) {
        for(HeaderParam paramAnnotation : annotation.value()){
            handler.handleMethodAnnotation(paramAnnotation, builder);
        }
    }
    
}
