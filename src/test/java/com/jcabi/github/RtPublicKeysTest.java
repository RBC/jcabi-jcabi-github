/**
 * Copyright (c) 2012-2013, JCabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.github;

import com.rexsl.test.Request;
import com.rexsl.test.mock.MkAnswer;
import com.rexsl.test.mock.MkContainer;
import com.rexsl.test.mock.MkGrizzlyContainer;
import com.rexsl.test.mock.MkQuery;
import com.rexsl.test.request.ApacheRequest;
import java.net.HttpURLConnection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link RtPublicKeys}.
 *
 * @author Carlos Miranda (miranda.cma@gmail.com)
 * @version $Id$
 */
public final class RtPublicKeysTest {

    /**
     * RtPublicKeys should be able to iterate its keys.
     *
     * @throws Exception if a problem occurs.
     */
    @Test
    @Ignore
    public void retrievesKeys() throws Exception {
        //to be implemented.
    }

    /**
     * RtPublicKeys should be able to obtain a single key.
     *
     * @throws Exception if a problem occurs.
     */
    @Test
    public void canFetchSingleKey() throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                HttpURLConnection.HTTP_OK,
                ""
            )
        ).start();
        final RtPublicKeys keys = new RtPublicKeys(
            new ApacheRequest(container.home()),
            Mockito.mock(User.class)
        );
        try {
            MatcherAssert.assertThat(
                keys.get(1),
                Matchers.notNullValue()
            );
        } finally {
            container.stop();
        }
    }

    /**
     * RtPublicKeys should be able to remove a key.
     *
     * @throws Exception if a problem occurs.
     */
    @Test
    public void canRemoveKey() throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                HttpURLConnection.HTTP_NO_CONTENT,
                ""
            )
        ).start();
        final RtPublicKeys keys = new RtPublicKeys(
            new ApacheRequest(container.home()),
            Mockito.mock(User.class)
        );
        try {
            keys.remove(1);
            final MkQuery query = container.take();
            MatcherAssert.assertThat(
                query.uri().toString(),
                Matchers.endsWith("/user/keys/1")
            );
            MatcherAssert.assertThat(
                query.method(),
                Matchers.equalTo(Request.DELETE)
            );
        } finally {
            container.stop();
        }
    }

}