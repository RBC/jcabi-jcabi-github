/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.github.Github;
import com.jcabi.github.Notifications;
import com.jcabi.github.PublicKeys;
import com.jcabi.github.User;
import com.jcabi.github.UserEmails;
import com.jcabi.github.UserOrganizations;
import com.jcabi.xml.XML;
import java.io.IOException;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Github user.
 *
 * @since 0.5
 * @checkstyle ClassDataAbstractionCouplingCheck (8 lines)
 */
@Immutable
@Loggable(Loggable.DEBUG)
@ToString
@EqualsAndHashCode(of = { "storage", "self" })
final class MkUser implements User {

    /**
     * Storage.
     */
    private final transient MkStorage storage;

    /**
     * Login of the user logged in.
     */
    private final transient String self;

    /**
     * Public ctor.
     * @param stg Storage
     * @param login User to login
     */
    MkUser(final MkStorage stg, final String login) {
        this.storage = stg;
        this.self = login;
    }

    @Override
    public Github github() {
        return new MkGithub(this.storage, this.self);
    }

    @Override
    public String login() {
        return this.self;
    }

    @Override
    public UserOrganizations organizations() {
        try {
            return new MkUserOrganizations(this.storage, this.self);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public PublicKeys keys() {
        try {
            return new MkPublicKeys(this.storage, this.self);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public UserEmails emails() {
        try {
            return new MkUserEmails(this.storage, this.self);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Notifications notifications() {
        return new MkNotifications(
            this.storage,
            this.xpath().concat("/notifications/notification")
        );
    }

    @Override
    public void markAsRead(final Date lastread) throws IOException {
        final Iterable<XML> ids = this.storage.xml().nodes(
            this.xpath() + String.format(
                "/notifications/notification[date <= %s]/id",
                lastread.getTime()
            )
        );
        final JsonPatch json = new JsonPatch(this.storage);
        final JsonObject read = Json.createObjectBuilder()
            .add("read", true).build();
        for (final XML nid : ids) {
            json.patch(
                String.format(
                    this.xpath().concat("/notifications/notification[id = %s]"),
                    nid.xpath("text()").get(0)
                ),
                read
            );
        }
    }

    @Override
    public void patch(
        final JsonObject json
    ) throws IOException {
        new JsonPatch(this.storage).patch(this.xpath(), json);
    }

    @Override
    public JsonObject json() throws IOException {
        return new JsonNode(
            this.storage.xml().nodes(this.xpath()).get(0)
        ).json();
    }

    /**
     * XPath of this element in XML tree.
     * @return XPath
     */
    private String xpath() {
        return String.format("/github/users/user[login='%s']", this.self);
    }

}
