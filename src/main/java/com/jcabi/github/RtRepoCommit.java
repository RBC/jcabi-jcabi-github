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
 * Github repo commit.
 *
 * @since 0.8
 */
@Immutable
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = { "request", "owner", "hash" })
final class RtRepoCommit implements RepoCommit {

    /**
     * RESTful request.
     */
    private final transient Request request;

    /**
     * Repo we're in.
     */
    private final transient Repo owner;

    /**
     * Commit SHA hash.
     */
    private final transient String hash;

    /**
     * Public ctor.
     * @param req RESTful request
     * @param repo Owner of this commit
     * @param sha Number of the get
     */
    RtRepoCommit(final Request req, final Repo repo, final String sha) {
        final Coordinates coords = repo.coordinates();
        this.request = req.uri()
            .path("/repos")
            .path(coords.user())
            .path(coords.repo())
            .path("/commits")
            .path(sha)
            .back();
        this.owner = repo;
        this.hash = sha;
    }

    @Override
    public String toString() {
        return this.request.uri().get().toString();
    }

    @Override
    public Repo repo() {
        return this.owner;
    }

    @Override
    public String sha() {
        return this.hash;
    }

    @Override
    public JsonObject json() throws IOException {
        return new RtJson(this.request).fetch();
    }

    @Override
    public int compareTo(
        final RepoCommit commit
    ) {
        return this.sha().compareTo(commit.sha());
    }
}
