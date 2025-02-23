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
 * Github Blob.
 *
 */
@Immutable
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = {"request", "hash" })
final class RtBlob implements Blob {

    /**
     * RESTful request.
     */
    private final transient Request request;

    /**
     * Blob SHA hash.
     */
    private final transient String hash;

    /**
     * Public ctor.
     * @param req Request
     * @param repo Repository
     * @param sha Number of the get
     */
    RtBlob(
        final Request req,
        final Repo repo,
        final String sha) {
        final Coordinates coords = repo.coordinates();
        this.request = req.uri()
            .path("/repos")
            .path(coords.user())
            .path(coords.repo())
            .path("/git")
            .path("/blobs")
            .path(sha)
            .back();
        this.hash = sha;
    }

    @Override
    public String sha() {
        return this.hash;
    }

    @Override
    public JsonObject json() throws IOException {
        return new RtJson(this.request).fetch();
    }
}
