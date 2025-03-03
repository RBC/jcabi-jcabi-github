/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.github.mock.MkGithub;
import com.jcabi.github.mock.MkOrganization;
import com.jcabi.github.mock.MkStorage;
import com.jcabi.http.Request;
import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.mock.MkQuery;
import com.jcabi.http.request.ApacheRequest;
import com.jcabi.http.request.FakeRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test case for {@link RtPublicMembers}.
 *
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class RtPublicMembersTest {
    /**
     * Test organization.
     */
    private static final String ORG = "starfleet";

    /**
     * Test username.
     */
    private static final String USERNAME = "wesley";

    /**
     * Public members URL for test org.
     */
    private static final String MEMBERS_URL = String.format(
        "/orgs/%s/public_members",
        ORG
    );

    /**
     * Public member URL for test user in test org.
     */
    private static final String MEMBER_URL = String.format(
        "%s/%s",
        MEMBERS_URL,
        USERNAME
    );

    /**
     * Rule for checking thrown exception.
     * @checkstyle VisibilityModifier (3 lines)
     */
    @Rule
    @SuppressWarnings("deprecation")
    public transient ExpectedException thrown = ExpectedException.none();

    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * RtPublicMembers can fetch its organization.
     * @throws IOException If there is an I/O problem
     */
    @Test
    public void fetchesOrg() throws IOException {
        final Organization org = organization();
        MatcherAssert.assertThat(
            new RtPublicMembers(new FakeRequest(), org).org(),
            Matchers.equalTo(org)
        );
    }

    /**
     * RtPublicMembers can conceal a user's membership in the organization.
     * @throws IOException If there is an I/O problem
     */
    @Test
    public void concealsMembers() throws IOException {
        try (
            final MkContainer container = new MkGrizzlyContainer()
            .next(new MkAnswer.Simple(HttpURLConnection.HTTP_NO_CONTENT))
            .next(new MkAnswer.Simple(HttpURLConnection.HTTP_INTERNAL_ERROR))
            .start(this.resource.port())
        ) {
            final RtPublicMembers members = new RtPublicMembers(
                new ApacheRequest(container.home()),
                organization()
            );
            members.conceal(user());
            final MkQuery req = container.take();
            MatcherAssert.assertThat(
                req.method(),
                Matchers.equalTo(Request.DELETE)
            );
            MatcherAssert.assertThat(
                req.body(),
                Matchers.is(Matchers.emptyOrNullString())
            );
            MatcherAssert.assertThat(
                req.uri().toString(),
                Matchers.endsWith(MEMBER_URL)
            );
            this.thrown.expect(AssertionError.class);
            members.conceal(user());
            container.stop();
        }
    }

    /**
     * RtPublicMembers can publicize the membership of
     * a user in the organization.
     * @throws IOException If there is an I/O problem
     */
    @Test
    public void publicizesMembers() throws IOException {
        try (MkContainer container = new MkGrizzlyContainer()
            .next(new MkAnswer.Simple(HttpURLConnection.HTTP_NO_CONTENT))
            .next(new MkAnswer.Simple(HttpURLConnection.HTTP_INTERNAL_ERROR))
            .start(this.resource.port())
        ) {
            final RtPublicMembers members = new RtPublicMembers(
                new ApacheRequest(container.home()),
                organization()
            );
            members.publicize(user());
            final MkQuery req = container.take();
            MatcherAssert.assertThat(
                req.method(),
                Matchers.equalTo(Request.PUT)
            );
            MatcherAssert.assertThat(
                req.uri().toString(),
                Matchers.endsWith(MEMBER_URL)
            );
            this.thrown.expect(AssertionError.class);
            members.publicize(user());
            container.stop();
        }
    }

    /**
     * RtPublicMembers can check whether a user
     * is a public member of the organization.
     * @throws IOException If there is an I/O problem
     */
    @Test
    public void checkPublicMembership() throws IOException {
        try (
            final MkContainer container = new MkGrizzlyContainer()
                .next(new MkAnswer.Simple(HttpURLConnection.HTTP_NOT_FOUND))
                .next(new MkAnswer.Simple(HttpURLConnection.HTTP_NOT_FOUND))
                .next(new MkAnswer.Simple(HttpURLConnection.HTTP_NO_CONTENT))
                .next(
                    new MkAnswer.Simple(HttpURLConnection.HTTP_INTERNAL_ERROR)
                )
                .start(this.resource.port())
        ) {
            final RtPublicMembers members = new RtPublicMembers(
                new ApacheRequest(container.home()),
                organization()
            );
            members.contains(user());
            final MkQuery req = container.take();
            MatcherAssert.assertThat(
                req.method(),
                Matchers.equalTo(Request.GET)
            );
            MatcherAssert.assertThat(
                req.uri().toString(),
                Matchers.endsWith(MEMBER_URL)
            );
            MatcherAssert.assertThat(
                "404 is interpreted as the user not being a public member",
                !members.contains(user())
            );
            MatcherAssert.assertThat(
                "204 is interpreted as the user being a public member",
                members.contains(user())
            );
            this.thrown.expect(AssertionError.class);
            members.contains(user());
            container.stop();
        }
    }

    /**
     * RtPublicMembers can list the public members of the organization.
     * @throws IOException If there is an I/O problem
     */
    @Test
    public void iteratesPublicMembers() throws IOException {
        try (
            final MkContainer container = new MkGrizzlyContainer()
                .next(
                    new MkAnswer.Simple(
                        HttpURLConnection.HTTP_OK,
                        "[{\"login\":\"octobat\"}]"
                    )
            )
            .next(new MkAnswer.Simple(HttpURLConnection.HTTP_INTERNAL_ERROR))
            .start(this.resource.port())
        ) {
            final RtPublicMembers members = new RtPublicMembers(
                new ApacheRequest(container.home()),
                organization()
            );
            members.iterate().iterator().next();
            final MkQuery req = container.take();
            MatcherAssert.assertThat(
                req.method(),
                Matchers.equalTo(Request.GET)
            );
            MatcherAssert.assertThat(
                req.uri().toString(),
                Matchers.endsWith(MEMBERS_URL)
            );
            this.thrown.expect(AssertionError.class);
            members.iterate().iterator().next();
            container.stop();
        }
    }

    /**
     * Get test organization.
     * @return Organization
     * @throws IOException If there is an I/O problem
     */
    private static Organization organization() throws IOException {
        return new MkOrganization(new MkStorage.InFile(), ORG);
    }

    /**
     * Get test user.
     * @return User
     * @throws IOException If there is an I/O problem
     */
    private static User user() throws IOException {
        return new MkGithub().users().get(USERNAME);
    }
}
