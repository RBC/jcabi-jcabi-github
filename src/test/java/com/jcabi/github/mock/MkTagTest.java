/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */

package com.jcabi.github.mock;

import com.jcabi.github.Tag;
import javax.json.Json;
import javax.json.JsonObject;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Testcase for MkTag.
 * @checkstyle MultipleStringLiterals (500 lines)
 */
public final class MkTagTest {

    /**
     * MkTag should return its json.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void fetchesContent() throws Exception {
        MatcherAssert.assertThat(
            this.tag().json().getString("message"),
            Matchers.is("\"test tag\"")
        );
    }

    /**
     * Return a Tag for testing.
     * @return Tag
     * @throws Exception If something goes wrong.
     */
    private Tag tag() throws Exception {
        final JsonObject json = Json.createObjectBuilder()
            .add("sha", "abcsha12").add("message", "test tag")
            .add("name", "v.0.1").build();
        return new MkGithub().randomRepo().git().tags().create(json);
    }

}
