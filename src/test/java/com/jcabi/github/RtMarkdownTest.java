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
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.request.ApacheRequest;
import java.net.HttpURLConnection;
import javax.json.Json;
import javax.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test case for {@link RtMarkdown}.
 *
 * @checkstyle MultipleStringLiteralsCheck (100 lines)
 */
public final class RtMarkdownTest {

    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * RtMarkdown should be able to return JSON output.
     *
     * @throws Exception If a problem occurs.
     */
    @Test
    public void returnsJsonOutput() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(HttpURLConnection.HTTP_OK, "{\"a\":\"b\"}")
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML)
            ).start(this.resource.port())
        ) {
            final RtMarkdown markdown = new RtMarkdown(
                new MkGithub(),
                new ApacheRequest(container.home())
            );
            MatcherAssert.assertThat(
                markdown.render(
                    Json.createObjectBuilder().add("hello", "world").build()
                ),
                Matchers.equalTo("{\"a\":\"b\"}")
            );
            MatcherAssert.assertThat(
                container.take().body(),
                Matchers.equalTo("{\"hello\":\"world\"}")
            );
            container.stop();
        }
    }

    /**
     * RtMarkdown should be able to return raw output.
     *
     * @throws Exception If a problem occurs.
     */
    @Test
    public void returnsRawOutput() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(HttpURLConnection.HTTP_OK, "Test Output")
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML)
            ).start(this.resource.port())
        ) {
            final RtMarkdown markdown = new RtMarkdown(
                new MkGithub(),
                new ApacheRequest(container.home())
            );
            MatcherAssert.assertThat(
                markdown.raw("Hello World!"),
                Matchers.equalTo("Test Output")
            );
            MatcherAssert.assertThat(
                container.take().body(),
                Matchers.equalTo("Hello World!")
            );
            container.stop();
        }
    }

}
