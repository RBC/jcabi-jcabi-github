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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Github deploy key.
 *
 * @since 0.8
 * @see <a href="https://developer.github.com/v3/repos/keys/">Deploy Keys API</a>
 */
@Immutable
@SuppressWarnings("PMD.TooManyMethods")
public interface DeployKey extends JsonReadable, JsonPatchable {

    /**
     * Get id of a deploy key.
     * @return Id
     */
    int number();

    /**
     * Delete a deploy key.
     * @throws java.io.IOException If there is any I/O problem
     * @see <a href="https://developer.github.com/v3/repos/keys/#delete">Remove a deploy key</a>
     */
    void remove() throws IOException;

    /**
     * Smart DeployKey with extra features.
     * @checkstyle MultipleStringLiterals (500 lines)
     */
    @Immutable
    @ToString
    @Loggable(Loggable.DEBUG)
    @EqualsAndHashCode(of = { "key", "jsn" })
    final class Smart implements DeployKey {

        /**
         * Encapsulated deploy key.
         */
        private final transient DeployKey key;

        /**
         * SmartJson object for convenient JSON parsing.
         */
        private final transient SmartJson jsn;

        /**
         * Public ctor.
         * @param dkey Deploy key
         */
        public Smart(final DeployKey dkey) {
            this.key = dkey;
            this.jsn = new SmartJson(dkey);
        }

        /**
         * Get its key value.
         * @return Value of deploy key
         * @throws IOException If there is any I/O problem
         */
        public String key() throws IOException {
            return this.jsn.text("key");
        }

        /**
         * Change its value.
         * @param value Title of deploy key
         * @throws IOException If there is any I/O problem
         */
        public void key(final String value) throws IOException {
            this.key.patch(
                Json.createObjectBuilder().add("key", value).build()
            );
        }

        /**
         * Get its URL.
         * @return URL of deploy key
         * @throws IOException If there is any I/O problem
         */
        public URL url() throws IOException {
            try {
                return new URI(this.jsn.text("url")).toURL();
            } catch (final URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }

        /**
         * Get its title.
         * @return Title of deploy key
         * @throws IOException If there is any I/O problem
         */
        public String title() throws IOException {
            return this.jsn.text("title");
        }

        /**
         * Change its title.
         * @param text Title of deploy key
         * @throws IOException If there is any I/O problem
         */
        public void title(final String text) throws IOException {
            this.key.patch(
                Json.createObjectBuilder().add("title", text).build()
            );
        }

        @Override
        public JsonObject json() throws IOException {
            return this.key.json();
        }

        @Override
        public void patch(final JsonObject json) throws IOException {
            this.key.patch(json);
        }

        @Override
        public int number() {
            return this.key.number();
        }

        @Override
        public void remove() throws IOException {
            this.key.remove();
        }
    }

}
