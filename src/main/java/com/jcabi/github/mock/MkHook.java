/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Hook;
import com.jcabi.github.Repo;
import java.io.IOException;
import javax.json.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mock Github hook.
 *
 * @since 0.8
 */
@Immutable
@Loggable(Loggable.DEBUG)
@ToString
@EqualsAndHashCode(of = { "storage", "self", "coords", "num" })
final class MkHook implements Hook {
    /**
     * Storage.
     */
    private final transient MkStorage storage;

    /**
     * Login of the user logged in.
     */
    private final transient String self;

    /**
     * Repo name.
     */
    private final transient Coordinates coords;

    /**
     * Issue number.
     */
    private final transient int num;

    /**
     * Public ctor.
     * @param stg Storage
     * @param login User to login
     * @param rep Repo
     * @param number Hook number
     * @checkstyle ParameterNumber (5 lines)
     */
    MkHook(
        final MkStorage stg,
        final String login,
        final Coordinates rep,
        final int number) {
        this.storage = stg;
        this.self = login;
        this.coords = rep;
        this.num = number;
    }
    @Override
    public Repo repo() {
        return new MkRepo(this.storage, this.self, this.coords);
    }
    @Override
    public int number() {
        return this.num;
    }
    @Override
    public JsonObject json() throws IOException {
        return new JsonNode(
            this.storage.xml().nodes(
                String.format(
                    "//repo[@coords='%s']/hooks/hook[id='%s']",
                    this.repo().coordinates(), this.number()
                )
            ).get(0)
        ).json();
    }
}
