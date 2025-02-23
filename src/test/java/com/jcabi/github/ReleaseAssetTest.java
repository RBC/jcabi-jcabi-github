/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import java.net.URI;
import javax.json.Json;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link ReleaseAsset}.
 * @checkstyle MultipleStringLiterals (150 lines)
 */
@SuppressWarnings("PMD.TooManyMethods")
public class ReleaseAssetTest {

    /**
     * ReleaseAsset.Smart can fetch url property from ReleaseAsset.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesUrl() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        // @checkstyle LineLength (1 line)
        final String prop = "https://api.github.com/repos/octo/Hello/releases/assets/1";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("url", prop)
                .build()
        ).when(releaseAsset).json();
        MatcherAssert.assertThat(
            new ReleaseAsset.Smart(releaseAsset).url(),
            Matchers.is(new URI(prop).toURL())
        );
    }

    /**
     * ReleaseAsset.Smart can fetch name property from ReleaseAsset.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesName() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final String prop = "assetname.ext";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("name", prop)
                .build()
        ).when(releaseAsset).json();
        MatcherAssert.assertThat(
            new ReleaseAsset.Smart(releaseAsset).name(),
            Matchers.is(prop)
        );
    }

    /**
     * ReleaseAsset.Smart can fetch label property from ReleaseAsset.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesLabel() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final String prop = "short description";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("label", prop)
                .build()
        ).when(releaseAsset).json();
        MatcherAssert.assertThat(
            new ReleaseAsset.Smart(releaseAsset).label(),
            Matchers.is(prop)
        );
    }

    /**
     * ReleaseAsset.Smart can fetch state property from ReleaseAsset.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesState() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final String prop = "uploaded";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("state", prop)
                .build()
        ).when(releaseAsset).json();
        MatcherAssert.assertThat(
            new ReleaseAsset.Smart(releaseAsset).state(),
            Matchers.is(prop)
        );
    }

    /**
     * ReleaseAsset.Smart can fetch content_type property from ReleaseAsset.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesContentType() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final String prop = "application/zip";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("content_type", prop)
                .build()
        ).when(releaseAsset).json();
        MatcherAssert.assertThat(
            new ReleaseAsset.Smart(releaseAsset).contentType(),
            Matchers.is(prop)
        );
    }

    /**
     * ReleaseAsset.Smart can fetch size property from ReleaseAsset.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesSize() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final int prop = 1024;
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("size", prop)
                .build()
        ).when(releaseAsset).json();
        MatcherAssert.assertThat(
            new ReleaseAsset.Smart(releaseAsset).size(),
            Matchers.is(prop)
        );
    }

    /**
     * ReleaseAsset.Smart can fetch download_count property from ReleaseAsset.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesDownloadCount() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final int prop = 42;
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("download_count", prop)
                .build()
        ).when(releaseAsset).json();
        MatcherAssert.assertThat(
            new ReleaseAsset.Smart(releaseAsset).downloadCount(),
            Matchers.is(prop)
        );
    }

    /**
     * ReleaseAsset.Smart can fetch created_at property from ReleaseAsset.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesCreatedAt() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final String prop = "2013-02-27T19:35:32Z";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("created_at", prop)
                .build()
        ).when(releaseAsset).json();
        MatcherAssert.assertThat(
            new ReleaseAsset.Smart(releaseAsset).createdAt(),
            Matchers.equalTo(new Github.Time(prop).date())
        );
    }

    /**
     * ReleaseAsset.Smart can fetch updated_at property from ReleaseAsset.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesUpdatedAt() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final String prop = "2013-02-27T19:35:32Z";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("updated_at", prop)
                .build()
        ).when(releaseAsset).json();
        MatcherAssert.assertThat(
            new ReleaseAsset.Smart(releaseAsset).updatedAt(),
            Matchers.equalTo(new Github.Time(prop).date())
        );
    }

    /**
     * ReleaseAsset.Smart can update the name property of ReleaseAsset.
     * @throws Exception If a problem occurs.
     */
    @Test
    public final void updatesName() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final String prop = "new_name";
        new ReleaseAsset.Smart(releaseAsset).name(prop);
        Mockito.verify(releaseAsset).patch(
            Json.createObjectBuilder().add("name", prop).build()
        );
    }

    /**
     * ReleaseAsset.Smart can update the label property of ReleaseAsset.
     * @throws Exception If a problem occurs.
     */
    @Test
    public final void updatesLabel() throws Exception {
        final ReleaseAsset releaseAsset = Mockito.mock(ReleaseAsset.class);
        final String prop = "new_label";
        new ReleaseAsset.Smart(releaseAsset).label(prop);
        Mockito.verify(releaseAsset).patch(
            Json.createObjectBuilder().add("label", prop).build()
        );
    }
}
