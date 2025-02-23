/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.github.Content;
import com.jcabi.github.Contents;
import com.jcabi.github.Repo;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.json.Json;
import javax.json.JsonObject;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link MkContent}.
 * @since 0.8
 */
public final class MkContentTest {

    /**
     * MkContent should be able to fetch its own repo.
     *
     * @throws Exception if some problem inside
     */
    @Test
    public void canGetOwnRepo() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final Contents contents = repo.contents();
        final Content content = contents.create(
            jsonContent("repo.txt", "for repo", "json repo")
        );
        MatcherAssert.assertThat(
            content.repo(),
            Matchers.is(repo)
        );
    }

    /**
     * MkContent should be able to fetch its own path.
     *
     * @throws Exception if some problem inside
     */
    @Test
    public void canGetOwnPath() throws Exception {
        final Contents contents = new MkGithub().randomRepo().contents();
        final String path = "dummy.txt";
        final Content content = contents.create(
            jsonContent(path, "for path", "path test")
        );
        MatcherAssert.assertThat(
            content.path(),
            Matchers.is(path)
        );
    }

    /**
     * MkContent should be able to fetch its JSON representation.
     *
     * @throws Exception if some problem inside
     */
    @Test
    public void fetchesJsonRepresentation() throws Exception {
        final Contents contents = new MkGithub().randomRepo().contents();
        final String path = "fake.txt";
        final Content content = contents.create(
            jsonContent(path, "for json", "json test")
        );
        MatcherAssert.assertThat(
            // @checkstyle MultipleStringLiterals (1 line)
            content.json().getString("name"),
            Matchers.is(path)
        );
    }

    /**
     * MkContent should be able to fetch its raw representation.
     *
     * @throws Exception if some problem inside
     */
    @Test
    public void fetchesRawRepresentation() throws Exception {
        final Contents contents = new MkGithub().randomRepo().contents();
        final String raw = "raw test \u20ac\u0000";
        final InputStream stream = contents.create(
            jsonContent("raw.txt", "for raw", raw)
        ).raw();
        try {
            MatcherAssert.assertThat(
                IOUtils.toString(stream, StandardCharsets.UTF_8),
                Matchers.is(raw)
            );
        } finally {
            stream.close();
        }
    }

    /**
     * Get a JSON object for content creation.
     * @param path The path of the file
     * @param message Commit message
     * @param content File content
     * @return JSON representation of content attributes
     */
    private static JsonObject jsonContent(
        final String path,
        final String message,
        final String content
    ) {
        return Json.createObjectBuilder()
            .add("path", path)
            .add("message", message)
            .add(
                "content",
                DatatypeConverter.printBase64Binary(
                    content.getBytes(StandardCharsets.UTF_8)
                )
            ).build();
    }
}
