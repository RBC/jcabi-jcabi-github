/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import com.jcabi.github.OAuthScope.Scope;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration case for {@link RtGitignores}.
 *
 * @see <a href="https://developer.github.com/v3/gitignore/">Gitignore API</a>
 *
 */
@Immutable
@OAuthScope(Scope.REPO)
public final class RtGitignoresITCase {

    /**
     * RtGitignores can iterate template names.
     * @throws Exception if there is any error
     */
    @Test
    public void iterateTemplateNames() throws Exception {
        final Gitignores gitignores = RtGitignoresITCase.gitignores();
        MatcherAssert.assertThat(
            gitignores.iterate(),
            Matchers.hasItem("C++")
        );
    }

    /**
     * RtGitignores can get raw template by name.
     * @throws Exception if there is any error
     */
    @Test
    public void getRawTemplateByName() throws Exception {
        final Gitignores gitignores = RtGitignoresITCase.gitignores();
        MatcherAssert.assertThat(
            gitignores.template("C"),
            Matchers.containsString("#")
        );
    }

    /**
     * Create and return gitignores object to test.
     * @return Gitignores
     */
    private static Gitignores gitignores() {
        return new RtGitignores(new GithubIT().connect());
    }
}
