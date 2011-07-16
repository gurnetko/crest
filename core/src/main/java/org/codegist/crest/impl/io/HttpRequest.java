/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest.impl.io;

import org.codegist.common.lang.ToStringBuilder;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.io.Request;
import org.codegist.crest.param.Param;

import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HttpRequest implements Request {

    private final MethodConfig methodConfig;
    private final List<Param> headerParams;
    private final List<Param> matrixParams;
    private final List<Param> queryParams;
    private final List<Param> pathParams;
    private final List<Param> cookieParams;
    private final List<Param> formParams;

    public HttpRequest(MethodConfig methodConfig, List<Param> headerParams, List<Param> matrixParams, List<Param> queryParams, List<Param> pathParams, List<Param> cookieParams, List<Param> formParams) {
        this.methodConfig = methodConfig;
        this.headerParams = unmodifiableList(headerParams);
        this.matrixParams = unmodifiableList(matrixParams);
        this.queryParams = unmodifiableList(queryParams);
        this.pathParams = unmodifiableList(pathParams);
        this.cookieParams = unmodifiableList(cookieParams);
        this.formParams = unmodifiableList(formParams);
    }

    public MethodConfig getMethodConfig() {
        return methodConfig;
    }

    public List<Param> getParams(String type) {
        switch(HttpParamType.valueOf(type)){
            case COOKIE: return cookieParams;
            case FORM: return formParams;
            case HEADER: return headerParams;
            case MATRIX: return matrixParams;
            case PATH: return pathParams;
            case QUERY: return queryParams;
            default: throw new IllegalArgumentException("Unsupported param type:" + type);
        }
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("methodConfig", methodConfig)
                .append("headerParams", headerParams)
                .append("matrixParams", matrixParams)
                .append("queryParams", queryParams)
                .append("pathParams", pathParams)
                .append("cookieParams", cookieParams)
                .append("formParams", formParams)
                .toString();
    }

}
