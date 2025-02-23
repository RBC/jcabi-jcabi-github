/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.github.mock.MkGithub;
import com.jcabi.http.Request;
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.request.ApacheRequest;
import java.net.HttpURLConnection;
import javax.json.Json;
import javax.json.JsonObject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

/**
 * Testcase for RtCommits.
 *
 * @checkstyle MultipleStringLiterals (500 lines)
 */
public class RtCommitsTest {

    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * Tests creating a Commit.
     *
     * @throws Exception when an error occurs
     */
    @Test
    public final void createsCommit() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_CREATED,
                    "{\"sha\":\"0abcd89jcabitest\"}"
                )
            ).start(this.resource.port())) {
            final Commits commits = new RtCommits(
                new ApacheRequest(container.home()),
                new MkGithub().randomRepo()
            );
            final JsonObject author = Json.createObjectBuilder()
                .add("name", "Scott").add("email", "scott@gmail.com")
                .add("date", "2011-06-17T14:53:35-07:00").build();
            final JsonObject input = Json.createObjectBuilder()
                .add("message", "initial version")
                .add("author", author).build();
            final Commit newCommit = commits.create(input);
            MatcherAssert.assertThat(
                newCommit,
                Matchers.instanceOf(Commit.class)
            );
            MatcherAssert.assertThat(
                container.take().method(),
                Matchers.equalTo(Request.POST)
            );
            MatcherAssert.assertThat(
                newCommit.sha(),
                Matchers.equalTo("0abcd89jcabitest")
            );
        }
    }
}
