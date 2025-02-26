/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Tv;
import javax.json.Json;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link Fork}.
 */
public class ForkTest {
    /**
     * Fork.Smart can fetch name property from Fork.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesName() throws Exception {
        final Fork fork = Mockito.mock(Fork.class);
        final String name = "this is some name";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("name", name)
                .build()
        ).when(fork).json();
        MatcherAssert.assertThat(
            new Fork.Smart(fork).name(),
            Matchers.is(name)
        );
    }

    /**
     * Fork.Smart can fetch full name property from Fork.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesFullName() throws Exception {
        final Fork fork = Mockito.mock(Fork.class);
        final String name = "test full name";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("full_name", name)
                .build()
        ).when(fork).json();
        MatcherAssert.assertThat(
            new Fork.Smart(fork).fullName(),
            Matchers.is(name)
        );
    }

    /**
     * Fork.Smart can description property from Fork.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesDescription() throws Exception {
        final Fork fork = Mockito.mock(Fork.class);
        final String description = "test description";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("description", description)
                .build()
        ).when(fork).json();
        MatcherAssert.assertThat(
            new Fork.Smart(fork).description(),
            Matchers.is(description)
        );
    }
    /**
     * Fork.Smart can fetch size property from Fork.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesSize() throws Exception {
        final Fork fork = Mockito.mock(Fork.class);
        final int prop = Tv.HUNDRED;
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("size", prop)
                .build()
        ).when(fork).json();
        MatcherAssert.assertThat(
            new Fork.Smart(fork).size(),
            Matchers.is(prop)
        );
    }

    /**
     * Fork.Smart can fetch the Fork URLs.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesUrls() throws Exception {
        final Fork fork = Mockito.mock(Fork.class);
        final String url = "https://api.github.com/repos/octocat/Hello-World";
        final String html = "https://github.com/octocat/Hello-World";
        final String clone = "https://github.com/octocat/Hello-World.git";
        final String git = "git://github.com/octocat/Hello-World.git";
        final String ssh = "git@github.com:octocat/Hello-World.git";
        final String svn = "https://svn.github.com/octocat/Hello-World";
        final String mirror = "git://git.example.com/octocat/Hello-World";
        final String homepage = "https://github.com";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("url", url)
                .add("html_url", html)
                .add("clone_url", clone)
                .add("git_url", git)
                .add("ssh_url", ssh)
                .add("svn_url", svn)
                .add("mirror_url", mirror)
                .add("homepage", homepage)
                .build()
        ).when(fork).json();
        final Fork.Smart smart = new Fork.Smart(fork);
        MatcherAssert.assertThat(
            smart.url().toString(),
            Matchers.is(url)
        );
        MatcherAssert.assertThat(
            smart.htmlUrl().toString(),
            Matchers.is(html)
        );
        MatcherAssert.assertThat(
            smart.cloneUrl().toString(),
            Matchers.is(clone)
        );
        MatcherAssert.assertThat(
            smart.gitUrl().toString(),
            Matchers.is(git)
        );
        MatcherAssert.assertThat(
            smart.sshUrl().toString(),
            Matchers.is(ssh)
        );
        MatcherAssert.assertThat(
            smart.svnUrl().toString(),
            Matchers.is(svn)
        );
        MatcherAssert.assertThat(
            smart.mirrorUrl().toString(),
            Matchers.is(mirror)
        );
        MatcherAssert.assertThat(
            smart.homeUrl().toString(),
            Matchers.is(homepage)
        );
    }
    /**
     * Fork.Smart can fetch the number of forks, stargazers, and watchers
     * from Fork.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesCounts() throws Exception {
        final Fork fork = Mockito.mock(Fork.class);
        final int forks = Tv.TEN;
        final int stargazers = Tv.TWENTY;
        final int watchers = Tv.THIRTY;
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("forks_count", forks)
                .add("stargazers_count", stargazers)
                .add("watchers_count", watchers)
                .build()
        ).when(fork).json();
        final Fork.Smart smart = new Fork.Smart(fork);
        MatcherAssert.assertThat(
            smart.forks(),
            Matchers.is(forks)
        );
        MatcherAssert.assertThat(
            smart.stargazers(),
            Matchers.is(stargazers)
        );
        MatcherAssert.assertThat(
            smart.watchers(),
            Matchers.is(watchers)
        );
    }

    /**
     * Fork.Smart can fetch the number of open issues from Fork.
     * @throws Exception If some problem inside
     */
    @Test
    public final void openIssues() throws Exception {
        final Fork fork = Mockito.mock(Fork.class);
        final int openIssues = Tv.TEN;
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("open_issues_count", openIssues)
                .build()
        ).when(fork).json();
        final Fork.Smart smart = new Fork.Smart(fork);
        MatcherAssert.assertThat(
            smart.openIssues(),
            Matchers.is(openIssues)
        );
    }

    /**
     * Fork.Smart can fetch the default branch from Fork.
     * @throws Exception If some problem inside
     */
    @Test
    public final void fetchesDefaultBranches() throws Exception {
        final Fork fork = Mockito.mock(Fork.class);
        final String master = "master";
        Mockito.doReturn(
            Json.createObjectBuilder()
                .add("default_branch", master)
                .build()
        ).when(fork).json();
        final Fork.Smart smart = new Fork.Smart(fork);
        MatcherAssert.assertThat(
            smart.defaultBranch(),
            Matchers.is(master)
        );
    }
}
