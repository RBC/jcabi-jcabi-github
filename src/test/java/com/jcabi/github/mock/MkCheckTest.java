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
package com.jcabi.github.mock;

import com.jcabi.github.Check;
import com.jcabi.github.Pull;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link MkCheck}.
 *
 * @since 1.6.1
 */
public final class MkCheckTest {

    /**
     * Pull request.
     */
    private transient Pull pull;

    /**
     * Set up.
     * @throws java.io.IOException If some problem with I/O.
     */
    @Before
    public void setUp() throws IOException {
        this.pull = new MkGithub()
            .randomRepo()
            .pulls()
            .create("Test PR", "abcdea8", "abcdea9");
    }

    /**
     * MkChecks can create successful check.
     * @throws IOException If some problem with I/O.
     */
    @Test
    public void createsSuccessfulCheck() throws IOException {
        MatcherAssert.assertThat(
            ((MkChecks) this.pull.checks())
                .create(Check.Status.COMPLETED, Check.Conclusion.SUCCESS)
                .successful(),
            Matchers.is(true)
        );
    }

    /**
     * MkChecks can create failed check.
     * @throws IOException If some problem with I/O.
     */
    @Test
    public void createsFailedCheck() throws IOException {
        MatcherAssert.assertThat(
            ((MkChecks) this.pull.checks())
                .create(
                    Check.Status.COMPLETED,
                    Check.Conclusion.FAILURE
                ).successful(),
            Matchers.is(false)
        );
    }
}
