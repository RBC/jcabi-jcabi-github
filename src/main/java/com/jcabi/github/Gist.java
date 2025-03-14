/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.json.JsonObject;
import javax.json.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Github gist.
 *
 * @since 0.1
 * @see <a href="https://developer.github.com/v3/gists/">Gists API</a>
 */
@Immutable
@SuppressWarnings("PMD.TooManyMethods")
public interface Gist extends JsonReadable, JsonPatchable {

    /**
     * Github we're in.
     * @return Github
     */
    Github github();

    /**
     * Get gist identifier.
     * @return Gist identifier
     */
    String identifier();

    /**
     * Read file content.
     * @param name Name of it
     * @return File content
     * @throws IOException If there is any I/O problem
     * @see <a href="https://developer.github.com/v3/gists/#get-a-single-gist">Get a Single Gist</a>
     */
    String read(String name) throws IOException;

    /**
     * Write file content.
     * @param name Name of it
     * @param content Content to write
     * @throws IOException If there is any I/O problem
     * @see <a href="https://developer.github.com/v3/gists/#edit-a-gist">Edit a Gist</a>
     */
    void write(String name, String content) throws IOException;

    /**
     * Star a gist.
     * @throws IOException If there is any I/O problem
     */
    void star() throws IOException;

    /**
     * Unstar a gist.
     * @throws IOException If there is any I/O problem
     */
    void unstar() throws IOException;

    /**
     * Checks if Gist is starred.
     * @throws IOException If there is any I/O problem
     * @return True if gist is starred
     */
    boolean starred() throws IOException;

    /**
     * Fork the gist.
     * @return Forked gist
     * @throws IOException If there is any I/O problem
     */
    Gist fork() throws IOException;

    /**
     * Get all comments of the gist.
     * @return GistComments
     * @throws IOException If there is any I/O problem
     * @see <a href="https://developer.github.com/v3/gists/comments/">Gist Comments API</a>
     */
    GistComments comments() throws IOException;

    /**
     * Smart Gist with extra features.
     */
    @Immutable
    @ToString
    @Loggable(Loggable.DEBUG)
    @EqualsAndHashCode(of = "gist")
    final class Smart implements Gist {
        /**
         * Encapsulated gist.
         */
        private final transient Gist gist;
        /**
         * Public ctor.
         * @param gst Gist
         */
        public Smart(final Gist gst) {
            this.gist = gst;
        }

        /**
         * Get gist id.
         * @return Gist id
         */
        @Override
        public String identifier() {
            return this.gist.identifier();
        }

        /**
         * Get a list of all file names in the gist.
         * @return File names
         * @throws IOException If there is any I/O problem
         */
        public Iterable<String> files() throws IOException {
            final JsonObject array = this.gist.json().getJsonObject("files");
            final Collection<String> files =
                new ArrayList<>(array.size());
            for (final JsonValue value : array.values()) {
                files.add(JsonObject.class.cast(value).getString("filename"));
            }
            return files;
        }
        @Override
        public Github github() {
            return this.gist.github();
        }
        @Override
        public String read(final String name) throws IOException {
            return this.gist.read(name);
        }
        @Override
        public void write(final String name, final String content)
            throws IOException {
            this.gist.write(name, content);
        }

        @Override
        public void star() throws IOException {
            this.gist.star();
        }

        @Override
        public void unstar() throws IOException {
            this.gist.unstar();
        }

        @Override
        public boolean starred() throws IOException {
            return this.gist.starred();
        }

        @Override
        public Gist fork() throws IOException {
            return this.gist.fork();
        }

        @Override
        public GistComments comments() throws IOException {
            return this.gist.comments();
        }

        @Override
        public JsonObject json() throws IOException {
            return this.gist.json();
        }

        @Override
        public void patch(final JsonObject json) throws IOException {
            this.gist.patch(json);
        }
    }

}
