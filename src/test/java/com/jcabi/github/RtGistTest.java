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

import com.jcabi.github.mock.MkGithub;
import com.jcabi.http.Request;
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.mock.MkQuery;
import com.jcabi.http.request.ApacheRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import javax.json.Json;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test case for {@link RtGist}.
 *
 */
public final class RtGistTest {
    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * RtGist should be able to do reads.
     *
     * @throws Exception if there is a problem.
     * @checkstyle MultipleStringLiteralsCheck (20 lines)
     */
    @Test
    public void readsFileWithContents() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "{\"files\":{\"hello\":{\"raw_url\":\"world\"}}}"
                )
            ).next(new MkAnswer.Simple(HttpURLConnection.HTTP_OK, "success!"))
                .start(this.resource.port())) {
            final RtGist gist = new RtGist(
                new MkGithub(),
                new ApacheRequest(container.home()),
                "test"
            );
            MatcherAssert.assertThat(
                gist.read("hello"),
                Matchers.equalTo("success!")
            );
            container.stop();
        }
    }

    /**
     * RtGist should be able to do writes.
     * @throws Exception if there is a problem.
     */
    @Test
    public void writesFileContents() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(HttpURLConnection.HTTP_OK, "testFileWrite")
            ).start(this.resource.port())
        ) {
            final RtGist gist = new RtGist(
                new MkGithub(),
                new ApacheRequest(container.home()),
                "testWrite"
            );
            gist.write("testFile", "testContent");
            MatcherAssert.assertThat(
                container.take().body(),
                Matchers.containsString(
                    "\"testFile\":{\"content\":\"testContent\"}"
                )
            );
            container.stop();
        }
    }

    /**
     * RtGist can fork itself.
     *
     * @throws IOException If there is a problem.
     */
    @Test
    public void fork() throws IOException {
        final String fileContent = "success";
        try (final MkContainer container = new MkGrizzlyContainer()) {
            container.next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "{\"files\":{\"hello\":{\"raw_url\":\"world\"}}}"
                )
            );
            container.next(
                new MkAnswer.Simple(HttpURLConnection.HTTP_OK, fileContent)
            );
            container.next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_CREATED,
                    "{\"id\": \"forked\"}"
                )
            );
            container.next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "{\"files\":{\"hello\":{\"raw_url\":\"world\"}}}"
                )
            );
            container.next(
                new MkAnswer.Simple(HttpURLConnection.HTTP_OK, fileContent)
            );
            container.start(this.resource.port());
            final Gist gist = new RtGist(
                new MkGithub(),
                new ApacheRequest(container.home()),
                "test"
            );
            final String content = gist.read("hello");
            final Gist forkedGist = gist.fork();
            MatcherAssert.assertThat(
                forkedGist.read("hello"),
                Matchers.equalTo(content)
            );
            container.stop();
        }
    }

    /**
     * Gist.Smart can iterate through its files.
     *
     * @throws Exception if something goes wrong.
     */
    @Test
    public void canIterateFiles() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "{\"files\":{\"something\":{\"filename\":\"not null\"}}}"
                )
            ).start(this.resource.port())
        ) {
            final Gist.Smart smart = new Gist.Smart(
                new RtGist(
                    new MkGithub(),
                    new ApacheRequest(container.home()),
                    "testGetFiles"
                )
            );
            MatcherAssert.assertThat(
                smart.files(),
                Matchers.notNullValue()
            );
            MatcherAssert.assertThat(
                container.take().uri().toString(),
                Matchers.endsWith("/gists/testGetFiles")
            );
            container.stop();
        }
    }

    /**
     * RtGist can return a String representation correctly reflecting its URI.
     *
     * @throws Exception If something goes wrong.
     */
    @Test
    public void canRepresentAsString() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer()
            .start(this.resource.port())
        ) {
            final RtGist gist = new RtGist(
                new MkGithub(),
                new ApacheRequest(container.home()),
                "testToString"
            );
            MatcherAssert.assertThat(
                gist.toString(),
                Matchers.endsWith("/gists/testToString")
            );
            container.stop();
        }
    }

    /**
     * RtGist can unstar a starred Gist.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void canUnstarAGist() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(HttpURLConnection.HTTP_NO_CONTENT, "")
            ).start(this.resource.port())
        ) {
            final RtGist gist = new RtGist(
                new MkGithub(),
                new ApacheRequest(container.home()),
                "unstar"
            );
            gist.unstar();
            final MkQuery query = container.take();
            MatcherAssert.assertThat(
                query.method(),
                Matchers.equalTo(Request.DELETE)
            );
            MatcherAssert.assertThat(
                query.body(),
                Matchers.is(Matchers.emptyOrNullString())
            );
            container.stop();
        }
    }

    /**
     * RtGist can execute PATCH request.
     *
     * @throws Exception if there is any problem
     */
    @Test
    public void executePatchRequest() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK, "{\"msg\":\"hi\"}"
                )
            ).start(this.resource.port())
        ) {
            final RtGist gist = new RtGist(
                new MkGithub(),
                new ApacheRequest(container.home()),
                "patch"
            );
            gist.patch(
                Json.createObjectBuilder()
                    .add("content", "hi you!")
                    .build()
            );
            MatcherAssert.assertThat(
                container.take().method(),
                Matchers.equalTo(Request.PATCH)
            );
            container.stop();
        }
    }
}
