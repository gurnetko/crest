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

package org.codegist.crest.interceptor;

import org.codegist.crest.annotate.CRestComponent;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.io.RequestBuilder;

/**
 * <p>RequestInterceptors are called just before building the request and firing it. They can be used to modify the request.</p>
 * <p>RequestInterceptors are CRest Components.</p>
 * @author laurent.gilles@codegist.org
 * @see org.codegist.crest.annotate.CRestComponent
 */
@CRestComponent
public interface RequestInterceptor {

    /**
     * <p>Called before firing the request</p>
     * @param requestBuilder the current request builder
     * @param methodConfig the current method configuration
     * @param args the method arguments passed by the caller
     * @throws Exception Any exception, can be used to cancel the request execution
     */
    void beforeFire(RequestBuilder requestBuilder, MethodConfig methodConfig, Object[] args) throws Exception;

}
