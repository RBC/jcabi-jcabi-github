/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.http.Request;
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.request.ApacheRequest;
import com.jcabi.http.request.FakeRequest;
import java.net.HttpURLConnection;
import javax.json.Json;
import javax.json.JsonObject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link RtTrees}.
 * @checkstyle MultipleStringLiteralsCheck (100 lines)
 */
public final class RtTreesTest {

    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * RtTrees can create a tree.
     * @throws Exception - If something goes wrong.
     */
    @Test
    public void createsTree() throws Exception {
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                HttpURLConnection.HTTP_CREATED,
                "{\"sha\":\"0abcd89jcabitest\",\"url\":\"http://localhost/1\"}"
            )
        ).start(this.resource.port());
        final Trees trees = new RtTrees(
            new ApacheRequest(container.home()),
            repo()
        );
        final JsonObject tree = Json.createObjectBuilder()
            .add("path", "/path").add("mode", "100644 ")
            .add("type", "blob").add("sha", "sha1")
            .add("content", "content1").build();
        final JsonObject input = Json.createObjectBuilder()
            .add("tree", tree).add("base_tree", "SHA1")
            .build();
        try {
            final Tree tri = trees.create(input);
            MatcherAssert.assertThat(
                tri,
                Matchers.instanceOf(Tree.class)
            );
            MatcherAssert.assertThat(
                trees.get(tri.sha()),
                Matchers.equalTo(tri)
            );
            MatcherAssert.assertThat(
                container.take().method(),
                Matchers.equalTo(Request.POST)
            );
        } finally {
            container.stop();
        }
    }

    /**
     * RtTrees can get tree.
     *
     */
    @Test
    public void getTree() {
        final String sha = "0abcd89jcabitest";
        final Trees trees = new RtTrees(
            new FakeRequest().withBody(
                Json.createObjectBuilder()
                    .add("sha", sha)
                    .build()
                    .toString()
            ),
            repo()
        );
        MatcherAssert.assertThat(
            trees.get(sha).sha(), Matchers.equalTo(sha)
        );
    }

    /**
     * RtTrees can get tree recursively.
     *
     */
    @Test
    public void getTreeRec() {
        final String sha = "0abcd89jcabitest";
        final Trees trees = new RtTrees(
            new FakeRequest().withBody(
                Json.createObjectBuilder()
                    .add("sha", sha)
                    .build()
                    .toString()
            ),
            repo()
        );
        MatcherAssert.assertThat(
            trees.getRec(sha).sha(), Matchers.equalTo(sha)
        );
    }

    /**
     * Create and return repo to test.
     * @return Repo
     */
    private static Repo repo() {
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.doReturn(new Coordinates.Simple("mark", "test"))
            .when(repo).coordinates();
        return repo;
    }

}
