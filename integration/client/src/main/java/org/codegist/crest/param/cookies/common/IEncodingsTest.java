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

package org.codegist.crest.param.cookies.common;

import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.EnumSet;

import static org.codegist.crest.param.common.IEncodingsTest.Tests.Encoded;
import static org.junit.Assert.assertEquals;

/**
 * @author laurent.gilles@codegist.org
 */
public class IEncodingsTest<T extends IEncodingsTest.IEncodings> extends org.codegist.crest.param.common.IEncodingsTest<T> {

    public IEncodingsTest(CRestHolder crest, Class<T> clazz) {
        super(crest, clazz);
    }
    
    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServicesForHeaders());
    }

    // comma/semi-colons are considered special charactere in cookies value and are not escapable (true ?)

    @Override
    public void assertDefault(String p1, String p21, String p22, String actual) throws UnsupportedEncodingException {
        assertEquals(
                encodeHeader("default(cookies(count:2):[p1=£\"(')? &£d&f{/p3=}:,;£\"(')? &£d&f{/pp3=},p2=£\"(')? &£d&f{/p3=}:,;£\"(')? &£d&f{/pp3=},p2=£\"(')? &£d&f{/p3=}:,;£\"(')? &£d&f{/pp3=}]) p1=£\"(')? &£d&f{/p3=}: p2=£\"(')? &£d&f{/p3=}:"),  actual);
    }


    @Override
    public EnumSet<Tests> ignores() {
        return EnumSet.of(Encoded); // @Encoded does not applies to cookie param
    }

}