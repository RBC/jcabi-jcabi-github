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

import com.jcabi.github.OAuthScope.Scope;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test case for {@link RtBlobs}.
 * @checkstyle MultipleStringLiteralsCheck (100 lines)
 */
@OAuthScope(Scope.REPO)
public final class RtBlobsITCase {

    /**
     * Test repos.
     */
    private static Repos repos;

    /**
     * Test repo.
     */
    private static Repo repo;

    /**
     * RepoRule.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    private static RepoRule rule = new RepoRule();

    /**
     * Set up test fixtures.
     * @throws Exception If some errors occurred.
     */
    @BeforeClass
    public static void setUp() throws Exception {
        final Github github = new GithubIT().connect();
        repos = github.repos();
        repo = rule.repo(repos);
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
     * RtBlobs can create a blob.
     * @throws Exception If something goes wrong
     */
    @Test
    public void createsBlob() throws Exception {
        final Blobs blobs = repo.git().blobs();
        final Blob blob = blobs.create(
            "Test Content", "utf-8"
        );
        MatcherAssert.assertThat(
            blob.sha(),
            Matchers.equalTo(blob.json().getString("sha"))
        );
    }

    /**
     * RtBlobs can get a blob.
     * @throws Exception If something goes wrong
     */
    @Test
    public void getsBlob() throws Exception {
        final Blobs blobs = repo.git().blobs();
        final String content = "Content of the blob";
        final String encoding = "base64";
        final Blob blob = blobs.create(
            content, encoding
        );
        MatcherAssert.assertThat(
            blobs.get(blob.sha()).json().getString("sha"),
            Matchers.equalTo(blob.sha())
        );
        MatcherAssert.assertThat(
            blobs.get(blob.sha()).json().getString("encoding"),
            Matchers.equalTo(encoding)
        );
    }
}
