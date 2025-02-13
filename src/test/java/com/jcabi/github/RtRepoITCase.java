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

import com.jcabi.aspects.Tv;
import com.jcabi.github.OAuthScope.Scope;
import javax.json.Json;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Integration case for {@link Github}.
 * @checkstyle ClassDataAbstractionCoupling (500 lines)
 *  See https://developer.github.com/v3/repos/#list-languages for API details
 */
@OAuthScope(Scope.REPO)
public final class RtRepoITCase {
    /**
     * Test repos.
     */
    private static Repos repos;

    /**
     * Test repo.
     */
    private static Repo repo;

    /**
     * Set up test fixtures.
     * @throws Exception If some errors occurred.
     */
    @BeforeClass
    public static void setUp() throws Exception {
        final Github github = new GithubIT().connect();
        repos = github.repos();
        repo = repos.create(
            new Repos.RepoCreate(
                RandomStringUtils.randomAlphanumeric(Tv.TEN),
                false
            ).withAutoInit(true)
        );
        repo.contents().create(
            Json.createObjectBuilder()
                .add("path", "test.java")
                .add("message", "Test file for language test")
                .add(
                    "content", Base64.encodeBase64String(
                        "some content".getBytes()
                    )
                )
                .add("ref", "master")
                .build()
        );
    }

    /**
     * Tear down test fixtures.
     * @throws Exception If some errors occurred.
     */
    @AfterClass
    public static void tearDown() throws Exception {
        if (repos != null && repo != null) {
            repos.remove(repo.coordinates());
        }
    }

    /**
     * RtRepo can identify itself.
     */
    @Test
    public void identifiesItself() {
        MatcherAssert.assertThat(
            repo.coordinates(),
            Matchers.notNullValue()
        );
    }

    /**
     * RtRepo can fetch events.
     * @throws Exception If some problem inside
     */
    @Test
    public void iteratesEvents() throws Exception {
        final Issue issue = repo.issues().create("Test", "This is a bug");
        new Issue.Smart(issue).close();
        MatcherAssert.assertThat(
            repo.issueEvents().iterate(),
            Matchers.not(Matchers.emptyIterable())
        );
    }

    /**
     * RtRepo can tell if it exists.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void exists() throws Exception {
        MatcherAssert.assertThat(
            new Repo.Smart(repo).exists(), Matchers.is(Boolean.TRUE)
        );
    }

    /**
     * RtRepo can fetch its commits.
     */
    @Test
    public void fetchCommits() {
        MatcherAssert.assertThat(repo.commits(), Matchers.notNullValue());
    }

    /**
     * RtRepo can fetch assignees.
     */
    @Test
    public void iteratesAssignees() {
        MatcherAssert.assertThat(
            repo.assignees().iterate(),
            Matchers.not(Matchers.emptyIterable())
        );
    }

    /**
     * RtRepo can fetch languages.
     * @throws Exception If some problem inside
     */
    @Test
    public void fetchLanguages() throws Exception {
        MatcherAssert.assertThat(repo.languages(), Matchers.notNullValue());
    }

    /**
     * RtRepo can iterate languages. This test is ignored because of bug
     * https://github.com/jcabi/jcabi-github/issues/1007 .
     * @throws Exception If some problem inside
     */
    @Test
    @Ignore
    public void iteratesLanguages() throws Exception {
        MatcherAssert.assertThat(
            repo.languages(),
            Matchers.not(Matchers.emptyIterable())
        );
    }
}
