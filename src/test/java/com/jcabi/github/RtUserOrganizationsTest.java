/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Tv;
import com.jcabi.github.mock.MkGithub;
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.request.ApacheRequest;
import java.net.HttpURLConnection;
import javax.json.Json;
import javax.json.JsonObject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test case for {@link RtUserOrganizations}.
 *
 */
public final class RtUserOrganizationsTest {
    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * RtUserOrganizations can iterate organizations for
     * an unauthenticated user.
     *
     * @throws Exception If a problem occurs
     */
    @Test
    public void canIterateOrganizationsForUnauthUser() throws Exception {
        final String username = "octopus";
        final Github github = new MkGithub();
        final User user = github.users().get(username);
        final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                HttpURLConnection.HTTP_OK,
                Json.createArrayBuilder()
                    .add(org(Tv.THREE, "org11"))
                    .add(org(Tv.FOUR, "org12"))
                    .add(org(Tv.FIVE, "org13"))
                    .build().toString()
            )
        ).start(this.resource.port());
        try {
            final UserOrganizations orgs = new RtUserOrganizations(
                github,
                new ApacheRequest(container.home()),
                user
            );
            MatcherAssert.assertThat(
                orgs.iterate(),
                Matchers.<Organization>iterableWithSize(Tv.THREE)
            );
            MatcherAssert.assertThat(
                container.take().uri().toString(),
                Matchers.endsWith(String.format("/users/%s/orgs", username))
            );
        } finally {
            container.stop();
        }
    }

    /**
     * Create and return organization to test.
     * @param number Organization ID
     * @param login Organization login name.
     * @return JsonObject
     */
    private static JsonObject org(final int number, final String login) {
        return Json.createObjectBuilder()
            .add("id", number)
            .add("login", login)
            .build();
    }
}
