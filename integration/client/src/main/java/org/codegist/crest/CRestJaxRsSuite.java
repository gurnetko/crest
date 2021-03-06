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

package org.codegist.crest;

import org.codegist.crest.deserialization.DeserializationsJaxRsSuite;
import org.codegist.crest.handler.ErrorHandlersTest;
import org.codegist.crest.param.ParamsBaseSuite;
import org.codegist.crest.param.ParamsJaxRsSuite;
import org.codegist.crest.request.RequestsJaxRsSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ParamsBaseSuite.class,
        ParamsJaxRsSuite.class,
        RequestsJaxRsSuite.class,
        DeserializationsJaxRsSuite.class,
        ErrorHandlersTest.class
})
public class CRestJaxRsSuite {
}
