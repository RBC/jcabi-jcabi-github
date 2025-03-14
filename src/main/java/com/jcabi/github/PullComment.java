/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import java.io.IOException;
import java.util.Collection;
import javax.json.Json;
import javax.json.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Github pull comment.
 *
 * <p>PullComment implements {@link JsonReadable},
 * that's how you can get its full details in JSON format.
 * For example, to get its id, you get the entire JSON and
 * then gets its element:
 *
 * <pre>String id = comment.jsn().getString("id");</pre>
 *
 * <p>However, it's better to use a supplementary "smart" decorator, which
 * automates most of these operations:
 *
 * <pre>String id = new PullComment.Smart(comment).identifier();</pre>
 *
 * @since 0.8
 * @see <a href="https://developer.github.com/v3/pulls/comments/">Pull Comments API</a>
 */
@Immutable
@SuppressWarnings("PMD.TooManyMethods")
public interface PullComment extends JsonReadable, JsonPatchable,
    Comparable<PullComment> {

    /**
     * Pull we're in.
     * @return Pull
     */
    Pull pull();

    /**
     * Get its number.
     * @return Pull comment number
     */
    int number();

    /**
     * Adds the reaction to the pull comment.
     * @param reaction Reaction to be added.
     */
    void react(Reaction reaction);

    /**
     * List the reactions of the pull comment.
     * @return Comment reactions.
     */
    Collection<Reaction> reactions();

    /**
     * Smart PullComment with extra features.
     */
    @Immutable
    @ToString
    @Loggable(Loggable.DEBUG)
    @EqualsAndHashCode(of = { "cmnt", "jsn" })
    final class Smart implements PullComment {

        /**
         * Id field's name in JSON.
         */
        private static final String ID = "id";

        /**
         * Commit id field's name in JSON.
         */
        private static final String COMMIT_ID = "commit_id";

        /**
         * Url field's name in JSON.
         */
        private static final String URL = "url";

        /**
         * Body field's name in JSON.
         */
        private static final String BODY = "body";

        /**
         * Encapsulated pull comment.
         */
        private final transient PullComment cmnt;

        /**
         * SmartJson object for convenient JSON parsing.
         */
        private final transient SmartJson jsn;

        /**
         * Public ctor.
         * @param pcomment Pull comment
         */
        public Smart(
            final PullComment pcomment
        ) {
            this.cmnt = pcomment;
            this.jsn = new SmartJson(pcomment);
        }

        /**
         * Get its id value.
         * @return Id of pull comment
         * @throws IOException If there is any I/O problem
         */
        public String identifier() throws IOException {
            return this.jsn.text(ID);
        }

        /**
         * Change its id value.
         * @param value Id of pull comment
         * @throws IOException If there is any I/O problem
         */
        public void identifier(
            final String value
        ) throws IOException {
            this.cmnt.patch(
                Json.createObjectBuilder().add(ID, value).build()
            );
        }

        /**
         * Get its commit id value.
         * @return Commit id of pull comment
         * @throws IOException If there is any I/O problem
         */
        public String commitId() throws IOException {
            return this.jsn.text(COMMIT_ID);
        }

        /**
         * Change its commit id value.
         * @param value Commit id of pull comment
         * @throws IOException If there is any I/O problem
         */
        public void commitId(
            final String value
        ) throws IOException {
            this.cmnt.patch(
                Json.createObjectBuilder().add(COMMIT_ID, value).build()
            );
        }

        /**
         * Get its url value.
         * @return Url of pull comment
         * @throws IOException If there is any I/O problem
         */
        public String url() throws IOException {
            return this.jsn.text(URL);
        }

        /**
         * Get its reply id value.
         * @return Reply id of pull comment
         * @throws IOException If there is any I/O problem
         */
        public int reply() throws IOException {
            return this.jsn.number("in_reply_to");
        }

        /**
         * Change its url value.
         * @param value Url of pull comment
         * @throws IOException If there is any I/O problem
         */
        public void url(
            final String value
        ) throws IOException {
            this.cmnt.patch(
                Json.createObjectBuilder().add(URL, value).build()
            );
        }

        /**
         * Get its body value.
         * @return Url of pull comment
         * @throws IOException If there is any I/O problem
         */
        public String body() throws IOException {
            return this.jsn.text(BODY);
        }

        /**
         * Change its body value.
         * @param value Url of pull comment
         * @throws IOException If there is any I/O problem
         */
        public void body(
            final String value
        ) throws IOException {
            this.cmnt.patch(
                Json.createObjectBuilder().add(BODY, value).build()
            );
        }

        @Override
        public Pull pull() {
            return this.cmnt.pull();
        }

        @Override
        public int number() {
            return this.cmnt.number();
        }

        @Override
        public void react(final Reaction reaction) {
            throw new UnsupportedOperationException("React not implemented");
        }

        @Override
        public Collection<Reaction> reactions() {
            throw new UnsupportedOperationException(
                "reactions() not implemented"
            );
        }

        @Override
        public int compareTo(
            final PullComment comment
        ) {
            return this.cmnt.compareTo(comment);
        }

        @Override
        public void patch(
            final JsonObject json
        ) throws IOException {
            this.cmnt.patch(json);
        }

        @Override
        public JsonObject json() throws IOException {
            return this.cmnt.json();
        }

        /**
         * Get its author.
         * @return Pull comment author
         * @throws IOException If there is any I/O problem
         */
        public String author() throws IOException {
            return this.json().getJsonObject("user")
                .getString("login");
        }
    }
}
