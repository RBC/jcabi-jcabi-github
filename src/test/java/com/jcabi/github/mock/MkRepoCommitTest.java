/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.github.Coordinates;
import com.jcabi.github.Repo;
import java.io.IOException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.xembly.Directives;

/**
 * Test case for {@link MkRepoCommit).
 */
public final class MkRepoCommitTest {

    /**
     * The fist test key.
     */
    private static final String SHA1 =
        "6dcb09b5b57875f334f61aebed695e2e4193db5e";
    /**
     * The second test key.
     */
    private static final String SHA2 =
        "51cabb8e759852a6a40a7a2a76ef0afd4beef96d";

    /**
     * MkRepoCommit can return repository.
     * @throws IOException If some problem inside
     */
    @Test
    public void getRepo() throws IOException {
        final MkStorage storage = new MkStorage.InFile();
        final Repo repo = this.repo(storage);
        MatcherAssert.assertThat(
            new MkRepoCommit(
                storage, repo, SHA1
            ).repo(), Matchers.equalTo(repo)
        );
    }

    /**
     * MkRepoCommit can return sha.
     * @throws IOException If some problem inside
     */
    @Test
    public void getSha() throws IOException {
        final MkStorage storage = new MkStorage.InFile();
        MatcherAssert.assertThat(
            new MkRepoCommit(storage, this.repo(storage), SHA2).sha(),
            Matchers.equalTo(SHA2)
        );
    }

    /**
     * MkRepoCommit should be able to compare different instances.
     *
     * @throws Exception when a problem occurs.
     */
    @Test
    public void canCompareInstances() throws Exception {
        final MkStorage storage = new MkStorage.InFile();
        final Repo repoa = new MkRepo(
            storage, "login1",
            new Coordinates.Simple("test_login1", "test_repo1")
        );
        final Repo repob = new MkRepo(
            storage, "login2",
            new Coordinates.Simple("test_login2", "test_repo2")
        );
        final MkRepoCommit less =  new MkRepoCommit(
            storage, repoa, SHA1
        );
        final MkRepoCommit greater =  new MkRepoCommit(
            storage, repob, SHA2
        );
        MatcherAssert.assertThat(
            less.compareTo(greater),
            Matchers.lessThan(0)
        );
        MatcherAssert.assertThat(
            greater.compareTo(less),
            Matchers.greaterThan(0)
        );
    }
    /**
     * MkRepoCommit can get a JSON.
     * @throws Exception if some problem inside
     */
    @Test
    public void canGetJson() throws Exception {
        final MkStorage storage = new MkStorage.InFile();
        storage.apply(
            new Directives().xpath("/github").add("repos")
                .add("repo").attr("coords", "test_login/test_repo")
                .add("commits").add("commit").add("sha").set(SHA1)
        );
        final MkRepoCommit repoCommit = new MkRepoCommit(
            storage, this.repo(storage), SHA1
        );
        MatcherAssert.assertThat(
            repoCommit.json(), Matchers.notNullValue()
        );
    }

    /**
     * MkRepoCommit can compare equal commits.
     * @throws Exception If some problem inside
     */
    @Test
    public void compareEqual() throws Exception {
        final String sha = "c2c53d66948214258a26ca9ca845d7ac0c17f8e7";
        final MkStorage storage = new MkStorage.InFile();
        final Repo repo = this.repo(storage);
        final MkRepoCommit commit = new MkRepoCommit(storage, repo, sha);
        final MkRepoCommit other = new MkRepoCommit(storage, repo, sha);
        MatcherAssert.assertThat(
            commit.compareTo(other), Matchers.equalTo(0)
        );
        MatcherAssert.assertThat(
            other.compareTo(commit), Matchers.equalTo(0)
        );
    }

    /**
     * MkRepoCommit can compare different commits.
     * @throws Exception If some problem inside
     */
    @Test
    public void compareDifferent() throws Exception {
        final MkStorage storage = new MkStorage.InFile();
        final Repo repo = this.repo(storage);
        final MkRepoCommit commit = new MkRepoCommit(
            storage, repo, "6dcd4ce23d88e2ee9568ba546c007c63d9131c1b"
        );
        final MkRepoCommit other = new MkRepoCommit(
            storage, repo, "e9d71f5ee7c92d6dc9e92ffdad17b8bd49418f98"
        );
        MatcherAssert.assertThat(
            commit.compareTo(other), Matchers.not(0)
        );
        MatcherAssert.assertThat(
            other.compareTo(commit), Matchers.not(0)
        );
    }

    /**
     * Create repository for test.
     * @param storage The storage
     * @return Repo
     */
    private Repo repo(final MkStorage storage) {
        final String login = "test_login";
        return new MkRepo(
            storage,
            login,
            new Coordinates.Simple(login, "test_repo")
        );
    }
}
