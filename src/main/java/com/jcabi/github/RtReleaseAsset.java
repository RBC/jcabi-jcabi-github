/**
 * Copyright (c) 2013-2025 Yegor Bugayenko
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.http.Request;
import com.jcabi.http.response.RestResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import javax.json.JsonObject;
import javax.ws.rs.core.HttpHeaders;
import lombok.EqualsAndHashCode;

/**
 * Github release asset.
 *
 */
@Immutable
@Loggable(Loggable.DEBUG)
@EqualsAndHashCode(of = { "request", "owner", "num" })
final class RtReleaseAsset implements ReleaseAsset {

    /**
     * RESTful request.
     */
    private final transient Request request;

    /**
     * Release we're in.
     */
    private final transient Release owner;

    /**
     * Release Asset number.
     */
    private final transient int num;

    /**
     * Public ctor.
     * @param req RESTful Request
     * @param release Release
     * @param number Number of the release asset.
     */
    RtReleaseAsset(
        final Request req,
        final Release release,
        final int number
    ) {
        final Coordinates coords = release.repo().coordinates();
        this.request = req.uri()
            .path("/repos")
            .path(coords.user())
            .path(coords.repo())
            .path("/releases")
            .path("/assets")
            .path(Integer.toString(number))
            .back();
        this.owner = release;
        this.num = number;
    }

    @Override
    public String toString() {
        return this.request.uri().get().toString();
    }

    @Override
    public Release release() {
        return this.owner;
    }

    @Override
    public int number() {
        return this.num;
    }

    @Override
    public JsonObject json() throws IOException {
        return new RtJson(this.request).fetch();
    }

    @Override
    public void patch(
        final JsonObject json
    ) throws IOException {
        new RtJson(this.request).patch(json);
    }

    @Override
    public void remove() throws IOException {
        this.request.method(Request.DELETE).fetch()
            .as(RestResponse.class)
            .assertStatus(HttpURLConnection.HTTP_NO_CONTENT);
    }

    /**
     * Get raw release asset content.
     *
     * @see <a href="https://developer.github.com/v3/repos/releases/">Releases API</a>
     * @return Stream with content
     * @throws IOException If some problem inside.
     */
    @Override
    public InputStream raw() throws IOException {
        return new ByteArrayInputStream(
            this.request.method(Request.GET)
                .reset(HttpHeaders.ACCEPT).header(
                    HttpHeaders.ACCEPT,
                    "application/vnd.github.v3.raw"
                )
                .fetch()
                .as(RestResponse.class)
                .assertStatus(HttpURLConnection.HTTP_OK)
                .binary()
        );
    }

}
