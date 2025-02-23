/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.github.mock.MkGithub;
import com.jcabi.http.request.FakeRequest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link RtBranch}.
 *
 */
public final class RtBranchTest {
    /**
     * Test branch name.
     */
    private static final String BRANCH_NAME = "topic";
    /**
     * Commit SHA for test branch.
     * @checkstyle LineLengthCheck (2 lines)
     */
    private static final String SHA = "b9b0b8a357bbf70f7c9f8ef17160ee31feb508a9";

    /**
     * RtBranch can fetch its commit.
     * @throws Exception if a problem occurs.
     */
    @Test
    public void fetchesCommit() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final Commit commit = RtBranchTest.newBranch(repo).commit();
        MatcherAssert.assertThat(commit.sha(), Matchers.equalTo(SHA));
        final Coordinates coords = commit.repo().coordinates();
        MatcherAssert.assertThat(
            coords.user(),
            Matchers.equalTo(repo.coordinates().user())
        );
        MatcherAssert.assertThat(
            coords.repo(),
            Matchers.equalTo(repo.coordinates().repo())
        );
    }

    /**
     * RtBranch can fetch its branch name.
     * @throws Exception if a problem occurs.
     */
    @Test
    public void fetchesName() throws Exception {
        MatcherAssert.assertThat(
            RtBranchTest.newBranch(new MkGithub().randomRepo()).name(),
            Matchers.equalTo(BRANCH_NAME)
        );
    }

    /**
     * RtBranch can fetch its repo.
     * @throws Exception if a problem occurs.
     */
    @Test
    public void fetchesRepo() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final Coordinates coords = RtBranchTest.newBranch(repo)
            .repo().coordinates();
        MatcherAssert.assertThat(
            coords.user(),
            Matchers.equalTo(repo.coordinates().user())
        );
        MatcherAssert.assertThat(
            coords.repo(),
            Matchers.equalTo(repo.coordinates().repo())
        );
    }

    /**
     * RtBranch for testing.
     * @param repo Repository to create the branch in
     * @return The RtBranch.
     */
    private static Branch newBranch(final Repo repo) {
        return new RtBranch(
            new FakeRequest(),
            repo,
            BRANCH_NAME,
            SHA
        );
    }
}
