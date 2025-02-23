/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.github.CommitsComparison;
import com.jcabi.github.Coordinates;
import com.jcabi.github.RepoCommit;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link MkCommitsComparison).
 */
public final class MkCommitsComparisonTest {

    /**
     * MkCommitsComparison can get a repo.
     * @throws IOException if some problem inside
     */
    @Test
    public void getRepo() throws IOException {
        final String user = "test_user";
        MatcherAssert.assertThat(
            new MkCommitsComparison(
                new MkStorage.InFile(), user,
                new Coordinates.Simple(user, "test_repo")
            ).repo(), Matchers.notNullValue()
        );
    }
    /**
     * MkCommitsComparison can get a JSON.
     * @throws Exception if some problem inside
     */
    @Test
    public void canGetJson() throws Exception {
        MatcherAssert.assertThat(
            new MkCommitsComparison(
                new MkStorage.InFile(), "test1", new Coordinates.Simple(
                    "test_user1", "test_repo1"
                )
            ).json().getString("status"),
            Matchers.notNullValue()
        );
        MatcherAssert.assertThat(
            new MkCommitsComparison(
                new MkStorage.InFile(), "test2", new Coordinates.Simple(
                    "test_user2", "test_repo2"
                )
            ).json().getInt("ahead_by"),
            Matchers.notNullValue()
        );
    }

    /**
     * MkCommitsComparison can get a JSON with commits.
     * @throws Exception if some problem inside
     */
    @Test
    public void canGetJsonWithCommits() throws Exception {
        final CommitsComparison cmp = new MkCommitsComparison(
            new MkStorage.InFile(), "test-9",
            new Coordinates.Simple("test_user_A", "test_repo_B")
        );
        MatcherAssert.assertThat(
            new CommitsComparison.Smart(cmp).commits(),
            Matchers.<RepoCommit>iterableWithSize(0)
        );
        MatcherAssert.assertThat(
            cmp.json().getJsonArray("commits"),
            Matchers.notNullValue()
        );
    }
}
