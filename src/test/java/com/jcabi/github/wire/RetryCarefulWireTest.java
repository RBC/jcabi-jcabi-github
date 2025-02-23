/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.wire;

import com.jcabi.github.RandomPort;
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.request.FakeRequest;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.RestResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test case for {@link RetryCarefulWire}.
 * Just combines the RetryWire and CarefulWire test cases.
 */
public final class RetryCarefulWireTest {
    /**
     * HTTP 200 status reason.
     */
    private static final String OK = "OK";
    /**
     * Name of GitHub's number-of-requests-remaining rate limit header.
     */
    private static final String REMAINING_HEADER = "X-RateLimit-Remaining";
    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * RetryCarefulWire can make a few requests before giving up and
     * can wait until the rate limit resets.
     * @throws Exception If something goes wrong inside
     */
    @Test
    public void makesMultipleRequestsAndWaitUntilReset() throws Exception {
        final int threshold = 10;
        // @checkstyle MagicNumber (2 lines)
        final long reset = TimeUnit.MILLISECONDS
            .toSeconds(System.currentTimeMillis()) + 5L;
        final MkContainer container = new MkGrizzlyContainer()
            .next(new MkAnswer.Simple(HttpURLConnection.HTTP_INTERNAL_ERROR))
            .next(new MkAnswer.Simple(HttpURLConnection.HTTP_INTERNAL_ERROR))
            .next(new MkAnswer.Simple(HttpURLConnection.HTTP_OK)
                .withHeader(REMAINING_HEADER, "9")
                .withHeader("X-RateLimit-Reset", String.valueOf(reset))
            )
            .start(this.resource.port());
        new JdkRequest(container.home())
            .through(RetryCarefulWire.class, threshold)
            .fetch()
            .as(RestResponse.class)
            .assertStatus(HttpURLConnection.HTTP_OK);
        final long now = TimeUnit.MILLISECONDS
            .toSeconds(System.currentTimeMillis());
        MatcherAssert.assertThat(now, Matchers.greaterThanOrEqualTo(reset));
        container.stop();
    }

    /**
     * RetryCarefulWire can tolerate the lack the X-RateLimit-Remaining header.
     * @throws IOException If some problem inside
     */
    @Test
    public void tolerateMissingRateLimitRemainingHeader() throws IOException {
        final int threshold = 11;
        // @checkstyle MagicNumber (1 lines)
        new FakeRequest()
            .withStatus(HttpURLConnection.HTTP_OK)
            .withReason(OK)
            .through(RetryCarefulWire.class, threshold)
            .fetch();
        MatcherAssert.assertThat(
            "Did not crash when X-RateLimit-Remaining header was absent",
            true
        );
    }

    /**
     * RetryCarefulWire can tolerate the lack the X-RateLimit-Reset header.
     * @throws IOException If some problem inside
     */
    @Test
    public void tolerateMissingRateLimitResetHeader() throws IOException {
        final int threshold = 8;
        // @checkstyle MagicNumber (1 lines)
        new FakeRequest()
            .withStatus(HttpURLConnection.HTTP_OK)
            .withReason(OK)
            .withHeader(REMAINING_HEADER, "7")
            .through(RetryCarefulWire.class, threshold)
            .fetch();
        MatcherAssert.assertThat(
            "Did not crash when X-RateLimit-Reset header was absent",
            true
        );
    }
}
