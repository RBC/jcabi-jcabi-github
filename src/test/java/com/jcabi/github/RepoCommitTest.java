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

import java.io.IOException;
import java.net.URI;
import javax.json.Json;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link RepoCommit}.
 * @checkstyle MultipleStringLiterals (500 lines)
 */
public class RepoCommitTest {

    /**
     * RepoCommit.Smart can fetch url property from RepoCommit.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesUrl() throws Exception {
        final RepoCommit commit = Mockito.mock(RepoCommit.class);
        // @checkstyle LineLength (1 line)
        final String prop = "https://api.github.com/repos/pengwynn/octokit/contents/README.md";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("url", prop)
                .build()
        ).when(commit).json();
        MatcherAssert.assertThat(
            new RepoCommit.Smart(commit).url(),
            Matchers.is(new URI(prop).toURL())
        );
    }

    /**
     * RepoCommit.Smart can fetch message property from RepoCommit.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesMessage() throws Exception {
        final RepoCommit commit = Mockito.mock(RepoCommit.class);
        Mockito.doReturn(
            Json.createObjectBuilder().add(
                "commit",
                Json.createObjectBuilder().add("message", "hello, world!")
            ).build()
        ).when(commit).json();
        MatcherAssert.assertThat(
            new RepoCommit.Smart(commit).message(),
            Matchers.startsWith("hello, ")
        );
    }

    /**
     * RtRepoCommit can verify status.
     * @throws IOException If fails
     */
    @Test
    public final void verifiesStatus() throws IOException {
        final RepoCommit commit = Mockito.mock(RepoCommit.class);
        Mockito.doReturn(
            Json.createObjectBuilder().add(
                "commit",
                Json.createObjectBuilder().add(
                    "verification",
                    Json.createObjectBuilder().add("verified", true)
                ).build()
            ).build()
        ).when(commit).json();
        MatcherAssert.assertThat(
            new RepoCommit.Smart(commit).isVerified(),
            Matchers.is(true)
        );
    }

    /**
     * RtRepoCommit can read author's login.
     * @throws IOException If fails
     */
    @Test
    public final void readsAuthorLogin() throws IOException {
        final RepoCommit commit = Mockito.mock(RepoCommit.class);
        final String login = "jeff";
        Mockito.doReturn(
            Json.createObjectBuilder().add(
                "commit",
                Json.createObjectBuilder().add(
                    "author",
                    Json.createObjectBuilder().add("name", login)
                ).build()
            ).build()
        ).when(commit).json();
        MatcherAssert.assertThat(
            new RepoCommit.Smart(commit).author(),
            Matchers.equalTo(login)
        );
    }
}
