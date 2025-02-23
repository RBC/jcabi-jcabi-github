/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import java.io.IOException;
import javax.json.JsonObject;

/**
 * Github Git Data Reference.
 *
 */
@Immutable
public interface Reference {
    /**
     * Return its owner repo.
     * @return Repo
     */
    Repo repo();

    /**
     * Return its name.
     * @return String
     */
    String ref();

    /**
     * Return its Json.
     * @return JsonObject
     * @throws IOException - If something goes wrong.
     */
    JsonObject json() throws IOException;

    /**
     * Patch using this JSON object.
     * @param json JSON object
     * @throws IOException If there is any I/O problem
     */
    void patch(JsonObject json)
        throws IOException;

}
