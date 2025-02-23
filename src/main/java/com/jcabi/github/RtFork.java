/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.http.Request;
import java.io.IOException;
import javax.json.JsonObject;
import lombok.EqualsAndHashCode;

/**
 * Github fork.
 *
 * @since 0.8
 */
@Immutable
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = {"request", "num" })
final class RtFork implements Fork {

    /**
     * RESTful request.
     */
    private final transient Request request;

    /**
     * Fork number.
     */
    private final transient int num;

    /**
     * Public ctor.
     * @param req Request
     * @param repo Repository
     * @param number Number of the get
     */
    RtFork(final Request req, final Repo repo, final int number) {
        final Coordinates coords = repo.coordinates();
        this.request = req.uri()
            .path("/repos")
            .path(coords.user())
            .path(coords.repo())
            .path("/forks")
            .path(Integer.toString(number))
            .back();
        this.num = number;
    }

    @Override
    public int number() {
        return this.num;
    }

    @Override
    public void patch(
        final JsonObject json)
        throws IOException {
        new RtJson(this.request).patch(json);
    }

    @Override
    public JsonObject json() throws IOException {
        return new RtJson(this.request).fetch();
    }
}
