/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Tv;
import com.jcabi.github.mock.MkGithub;
import com.jcabi.http.request.FakeRequest;
import java.net.HttpURLConnection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link RtReleaseAssets}.
 * @since 0.8
 */
public final class RtReleaseAssetsTest {

    /**
     * RtRelease can list assets for a release.
     *
     * @throws Exception If something goes wrong.
     */
    @Test
    public void listReleaseAssets() throws Exception {
        final ReleaseAssets assets = new RtReleaseAssets(
            new FakeRequest().withStatus(HttpURLConnection.HTTP_OK)
                .withBody("[{\"id\":1},{\"id\":2}]"), release()
        );
        MatcherAssert.assertThat(
            assets.iterate(),
            Matchers.<ReleaseAsset>iterableWithSize(2)
        );
    }

    /**
     * RtRelease can upload a release asset.
     *
     * @throws Exception If something goes wrong
     */
    @Test
    public void uploadReleaseAsset() throws Exception {
        final String body = "{\"id\":1}";
        final ReleaseAssets assets = new RtReleaseAssets(
            new FakeRequest().withStatus(HttpURLConnection.HTTP_CREATED)
                .withBody(body),
            release()
        );
        MatcherAssert.assertThat(
            assets.upload(body.getBytes(), "text/plain", "hello.txt")
                .number(),
            Matchers.is(1)
        );
    }

    /**
     * RtRelease can get a single release asset.
     *
     * @throws Exception if something goes wrong.
     */
    @Test
    public void getReleaseAsset() throws Exception {
        final ReleaseAssets assets = new RtReleaseAssets(
            new FakeRequest().withStatus(HttpURLConnection.HTTP_OK)
                .withBody("{\"id\":3}"),
                release()
        );
        MatcherAssert.assertThat(
            assets.get(Tv.THREE).number(),
            Matchers.is(Tv.THREE)
        );
    }

    /**
     * This method returns a Release for testing.
     * @return Release to be used for test.
     * @throws Exception - if anything goes wrong.
     */
    private static Release release() throws Exception {
        final Release release = Mockito.mock(Release.class);
        final Repo repo = new MkGithub("john").randomRepo();
        Mockito.doReturn(repo).when(release).repo();
        Mockito.doReturn(1).when(release).number();
        return release;
    }
}
