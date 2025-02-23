/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.request.ApacheRequest;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.json.JsonValue;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link RtHook}.
 *
 * @checkstyle ClassDataAbstractionCouplingCheck (2 lines)
 */
public final class RtHookTest {
    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * RtHook should perform a JSON request to "/repos/:owner/:repo/hooks/:id".
     *
     * @throws Exception If a problem occurs.
     */
    @Test
    public void performsValidRequest() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "{\"test\":\"hook\"}"
                )
            ).start(this.resource.port())
        ) {
            final Hook hook = new RtHook(
                new ApacheRequest(container.home()),
                repo(),
                1
            );
            MatcherAssert.assertThat(
                hook.json(),
                Matchers.notNullValue()
            );
            MatcherAssert.assertThat(
                container.take().uri().toString(),
                Matchers.endsWith("/repos/test/repo/hooks/1")
            );
            container.stop();
        }
    }

    /**
     * RtHook.json() should return a json array with the hook's events.
     *
     * @throws Exception If a problem occurs.
     */
    @Test
    public void returnsEvents() throws Exception {
        try (
            final MkContainer container = new MkGrizzlyContainer().next(
                new MkAnswer.Simple(
                    HttpURLConnection.HTTP_OK,
                    "{ \"id\": 1, \"events\": [ \"push\", \"pull_request\" ] }"
                )
            ).start(this.resource.port())
        ) {
            MatcherAssert.assertThat(
                new RtHook(
                    new ApacheRequest(container.home()),
                    repo(),
                    1
                ).json().getJsonArray("events")
                    .stream()
                    .map(JsonValue::toString)
                    .map(event -> event.replace("\"", ""))
                    .collect(Collectors.toList()),
                new IsIterableContainingInAnyOrder<>(
                    Arrays.asList(
                        new IsEqual<>("push"),
                        new IsEqual<>("pull_request")
                    )
                )
            );
            container.stop();
        }
    }

    /**
     * Create and return repo for testing.
     * @return Repo
     */
    private static Repo repo() {
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.doReturn(new Coordinates.Simple("test", "repo"))
            .when(repo).coordinates();
        return repo;
    }

}
