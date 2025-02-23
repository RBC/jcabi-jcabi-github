/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.github.Github;
import com.jcabi.github.Limit;
import com.jcabi.github.Limits;
import javax.json.Json;
import javax.json.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Mock Github Rate Limit API.
 *
 * @since 0.6
 */
@Immutable
@Loggable(Loggable.DEBUG)
@ToString
@EqualsAndHashCode(of = { "storage", "himself" })
final class MkLimits implements Limits {

    /**
     * Storage.
     */
    private final transient MkStorage storage;

    /**
     * Login of the user logged in.
     */
    private final transient String himself;

    /**
     * Public ctor.
     * @param stg Storage
     * @param login User to login
     */
    MkLimits(final MkStorage stg,
        final String login
    ) {
        this.storage = stg;
        this.himself = login;
    }

    @Override
    public Github github() {
        return new MkGithub(this.storage, this.himself);
    }

    @Override
    public Limit get(final String resource) {
        // @checkstyle AnonInnerLength (50 lines)
        return new Limit() {
            @Override
            public Github github() {
                return MkLimits.this.github();
            }
            @Override
            public JsonObject json() {
                return Json.createObjectBuilder()
                    // @checkstyle MagicNumber (2 lines)
                    .add("limit", 5000)
                    .add("remaining", 4999)
                    .add("reset", System.currentTimeMillis())
                    .build();
            }
        };
    }
}
