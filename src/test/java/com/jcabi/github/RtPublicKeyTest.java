/**
 * Copyright (c) 2013-2025 Yegor Bugayenko
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

import com.jcabi.http.request.FakeRequest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link RtPublicKey}.
 *
 */
public final class RtPublicKeyTest {

    /**
     * RtPublicKey can be described as a JSON object.
     *
     * @throws Exception if a problem occurs.
     */
    @Test
    public void canRepresentAsJson() throws Exception {
        final RtPublicKey key = new RtPublicKey(
            new FakeRequest().withBody("{}"),
            Mockito.mock(User.class),
            1
        );
        MatcherAssert.assertThat(
            key.json(),
            Matchers.notNullValue()
        );
    }

    /**
     * RtPublicKey can obtain its own user.
     *
     */
    @Test
    public void canObtainUser() {
        final User user = Mockito.mock(User.class);
        final RtPublicKey key = new RtPublicKey(new FakeRequest(), user, 2);
        MatcherAssert.assertThat(
            key.user(),
            Matchers.sameInstance(user)
        );
    }

    /**
     * RtPublicKey can obtain its own number.
     *
     */
    @Test
    public void canObtainNumber() {
        final int number = 39;
        final RtPublicKey key = new RtPublicKey(
            new FakeRequest(),
            Mockito.mock(User.class),
            number
        );
        MatcherAssert.assertThat(
            key.number(),
            Matchers.equalTo(number)
        );
    }

}
