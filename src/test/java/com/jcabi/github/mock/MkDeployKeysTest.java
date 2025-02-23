/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.github.DeployKey;
import com.jcabi.github.DeployKeys;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link MkDeployKeys}.
 * @since 0.8
 */
public final class MkDeployKeysTest {
    /**
     * MkDeployKeys can fetch empty list of deploy keys.
     * @throws Exception if some problem inside
     */
    @Test
    public void canFetchEmptyListOfDeployKeys() throws Exception {
        final DeployKeys deployKeys = new MkGithub().randomRepo().keys();
        MatcherAssert.assertThat(
            deployKeys.iterate(),
            Matchers.emptyIterable()
        );
    }

    /**
     * MkDeployKeys can fetch a single deploy key.
     * @throws Exception If some problem inside
     */
    @Test
    public void canFetchSingleDeployKey() throws Exception {
        final DeployKeys keys = new MkGithub().randomRepo().keys();
        final DeployKey key = keys.create("Title", "Key");
        MatcherAssert.assertThat(keys.get(key.number()), Matchers.equalTo(key));
    }

    /**
     * MkDeployKeys can create a deploy key.
     * @throws Exception If some problem inside.
     */
    @Test
    public void canCreateDeployKey() throws Exception {
        final DeployKeys keys = new MkGithub().randomRepo().keys();
        final DeployKey key = keys.create("Title1", "Key1");
        MatcherAssert.assertThat(key, Matchers.equalTo(keys.get(key.number())));
    }

    /**
     * MkDeployKeys can create distinct deploy keys.
     * Reproduces bug described in issue #346.
     * @throws Exception If some problem inside.
     */
    @Test
    public void canCreateDistinctDeployKeys() throws Exception {
        final DeployKeys keys = new MkGithub().randomRepo().keys();
        final DeployKey first = keys.create("Title2", "Key2");
        final DeployKey second = keys.create("Title3", "Key3");
        MatcherAssert.assertThat(
            first,
            Matchers.not(Matchers.is(second))
        );
        MatcherAssert.assertThat(
            first.number(),
            Matchers.not(Matchers.is(second.number()))
        );
    }

    /**
     * MkDeployKeys can be represented in JSON format.
     * Reproduces bug described in issue #346.
     * @throws Exception If some problem inside.
     */
    @Test
    public void canRepresentAsJson() throws Exception {
        final DeployKeys keys = new MkGithub().randomRepo().keys();
        final DeployKey first = keys.create("Title4", "Key4");
        MatcherAssert.assertThat(
            first.json().toString(),
            Matchers.allOf(
                Matchers.containsString("\"title\":\"Title4\""),
                Matchers.containsString("\"key\":\"Key4\"")
            )
        );
    }
}
