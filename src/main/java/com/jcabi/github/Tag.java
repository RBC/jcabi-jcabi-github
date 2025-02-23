/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;

/**
 * Github Git Data Tag.
 *
 * @since 0.8
 */
@Immutable
public interface Tag extends JsonReadable {

    /**
     * Return owner repo.
     * @return Repo
     */
    Repo repo();

    /**
     * Return its sha.
     * @return String
     */
    String key();

}
