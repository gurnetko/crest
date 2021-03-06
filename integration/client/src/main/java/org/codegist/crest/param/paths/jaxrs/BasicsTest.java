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

package org.codegist.crest.param.paths.jaxrs;

import org.codegist.crest.annotate.EndPoint;
import org.codegist.crest.param.paths.common.IBasicsTest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author laurent.gilles@codegist.org
 */
public class BasicsTest extends IBasicsTest<BasicsTest.Basics> {

    public BasicsTest(CRestHolder crest) {
        super(crest, Basics.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/path/basic")
    public static interface Basics extends IBasicsTest.IBasics {

        @GET
        String send();

        @GET
        @Path("{p1}/{p2}")
        String send(
                @PathParam("p1") String p1,
                @PathParam("p2") int p2);

    }
}
