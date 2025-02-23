/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.github.Issue;
import com.jcabi.github.Pull;
import com.jcabi.github.Repo;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test case for {@link MkPulls}.
 * @since 1.0
 * @checkstyle MultipleStringLiteralsCheck (100 lines)
 */
public final class MkPullsTest {

    /**
     * MkPulls can create a pull.
     * It should create an issue first, and then pull with the same number
     * @throws Exception if some problem inside
     */
    @Test
    public void canCreateAPull() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final Pull pull = repo.pulls().create(
            "hello",
            "head-branch",
            "base-branch"
        );
        final Issue.Smart issue = new Issue.Smart(
            repo.issues().get(pull.number())
        );
        MatcherAssert.assertThat(
            issue.title(),
            Matchers.is("hello")
        );
    }

    /**
     * MkPulls can fetch empty list of pulls.
     */
    @Test
    @Ignore
    public void canFetchEmptyListOfPulls() {
        // to be implemented
    }

    /**
     * MkPulls can fetch single pull.
     */
    @Test
    @Ignore
    public void canFetchSinglePull() {
        // to be implemented
    }
}
