/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.http.Request;
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.mock.MkQuery;
import com.jcabi.http.request.JdkRequest;
import java.net.HttpURLConnection;
import javax.json.Json;
import javax.json.JsonObject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link RtLabels}.
 *
 */
public final class RtLabelsTest {
    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * RtLabels can create a label.
     * @throws Exception if some problem inside
     */
    @Test
    public void createLabel() throws Exception {
        final String name = "API";
        final String color = "FFFFFF";
        final String body = label(name, color).toString();
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(HttpURLConnection.HTTP_CREATED, body)
            ).next(new MkAnswer.Simple(HttpURLConnection.HTTP_OK, body))
                .start(this.resource.port())
        ) {
            final RtLabels labels = new RtLabels(
                new JdkRequest(container.home()),
                repo()
            );
            final Label label = labels.create(name, color);
            MatcherAssert.assertThat(
                container.take().method(),
                Matchers.equalTo(Request.POST)
            );
            MatcherAssert.assertThat(
                new Label.Smart(label).name(),
                Matchers.equalTo(name)
            );
            MatcherAssert.assertThat(
                new Label.Smart(label).color(),
                Matchers.equalTo(color)
            );
            container.stop();
        }
    }

    /**
     * RtLabels can get a single label.
     *
     * @throws Exception if some problem inside
     */
    @Test
    public void getSingleLabel() throws Exception {
        final String name = "bug";
        final String color = "f29513";
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    label(name, color).toString()
                )
            ).start(this.resource.port())
        ) {
            final RtLabels issues = new RtLabels(
                new JdkRequest(container.home()),
                repo()
            );
            final Label label = issues.get(name);
            MatcherAssert.assertThat(
                new Label.Smart(label).color(),
                Matchers.equalTo(color)
            );
            container.stop();
        }
    }

    /**
     * RtLabels can delete a label.
     * @throws Exception if some problem inside
     */
    @Test
    public void deleteLabel() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(HttpURLConnection.HTTP_NO_CONTENT, "")
            ).start(this.resource.port())
        ) {
            final RtLabels issues = new RtLabels(
                new JdkRequest(container.home()),
                repo()
            );
            issues.delete("issue");
            final MkQuery query = container.take();
            MatcherAssert.assertThat(
                query.method(),
                Matchers.equalTo(Request.DELETE)
            );
            MatcherAssert.assertThat(
                query.body(),
                Matchers.is(Matchers.emptyOrNullString())
            );
            container.stop();
        }
    }

    /**
     * RtLabels can iterate labels.
     * @throws Exception if there is any error
     */
    @Test
    public void iterateLabels() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    Json.createArrayBuilder()
                        .add(label("new issue", "f29512"))
                        .add(label("new bug", "f29522"))
                        .build().toString()
                )
            ).start(this.resource.port())
        ) {
            final RtLabels labels = new RtLabels(
                new JdkRequest(container.home()),
                repo()
            );
            MatcherAssert.assertThat(
                labels.iterate(),
                Matchers.<Label>iterableWithSize(2)
            );
            container.stop();
        }
    }

    /**
     * Create and return JsonObject to test.
     * @param name The name of the label
     * @param color A 6 character hex code, identifying the color
     * @return JsonObject
     */
    private static JsonObject label(
        final String name, final String color) {
        return Json.createObjectBuilder()
            .add("name", name)
            .add("color", color)
            .build();
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
