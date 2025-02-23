/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.github.mock.MkGithub;
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.request.JdkRequest;
import java.net.HttpURLConnection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test case for RtCommit.
 *
 * @checkstyle MultipleStringLiterals (500 lines)
 * @since 0.18.2
 */
public class RtCommitTest {

    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * Commit.Smart can read message.
     * @throws Exception when an error occurs
     */
    @Test
    public final void readsMessage() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "{\"sha\":\"a0b1c3\",\"commit\":{\"message\":\"hello\"}}"
                )
            ).start(this.resource.port())) {
            final Commit.Smart commit = new Commit.Smart(
                new RtCommit(
                    new JdkRequest(container.home()),
                    new MkGithub().randomRepo(),
                    "sha"
                )
            );
            MatcherAssert.assertThat(
                commit.message(),
                Matchers.equalTo("hello")
            );
        }
    }
}
