/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.github.OAuthScope.Scope;
import java.util.Collections;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Integration test for {@link RtGistComments}.
 * @see <a href="https://developer.github.com/v3/gists/comments/">Gist Comments API</a>
 * @since 0.8
 */
@OAuthScope(Scope.GIST)
public final class RtGistCommentsITCase {
    /**
     * RtGistComments can create a comment.
     * @throws Exception if some problem inside
     */
    @Test
    public void createComment() throws Exception {
        final Gist gist = RtGistCommentsITCase.gist();
        final GistComments comments = gist.comments();
        final GistComment comment = comments.post("gist comment");
        MatcherAssert.assertThat(
            new GistComment.Smart(comment).body(),
            Matchers.startsWith("gist")
        );
        comment.remove();
        gist.github().gists().remove(gist.identifier());
    }

    /**
     * RtGistComments can get a comment.
     * @throws Exception if some problem inside
     */
    @Test
    public void getComment() throws Exception {
        final Gist gist = RtGistCommentsITCase.gist();
        final GistComments comments = gist.comments();
        final GistComment comment = comments.post("test comment");
        MatcherAssert.assertThat(
            comments.get(comment.number()),
            Matchers.equalTo(comment)
        );
        comment.remove();
        gist.github().gists().remove(gist.identifier());
    }

    /**
     * RtGistComments can iterate all gist comments.
     * @throws Exception if some problem inside
     */
    @Test
    public void iterateComments() throws Exception {
        final Gist gist = RtGistCommentsITCase.gist();
        final GistComments comments = gist.comments();
        final GistComment comment = comments.post("comment");
        MatcherAssert.assertThat(
            comments.iterate(),
            Matchers.hasItem(comment)
        );
        comment.remove();
        gist.github().gists().remove(gist.identifier());
    }

    /**
     * Return gist to test.
     * @return Gist
     * @throws Exception If some problem inside
     */
    private static Gist gist() throws Exception {
        return new GithubIT()
            .connect()
            .gists()
            .create(
                Collections.singletonMap("file.txt",  "file content"), false
            );
    }
}
