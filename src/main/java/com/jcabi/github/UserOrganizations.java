/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import java.io.IOException;

/**
 * Organizations of a Github user.
 * @see <a href="https://developer.github.com/v3/orgs/">Organizations API</a>
 * @since 0.24
 */
@Immutable
public interface UserOrganizations {

    /**
     * Github we're in.
     * @return Github
     */
    Github github();

    /**
     * Get its owner.
     * @return User
     */
    User user();

    /**
     * Iterate organizations of particular user.
     * All public organizations for an unauthenticated user or
     * private and public organizations for authenticated users
     * @return Iterator of Organizations
     * @throws IOException If there is an I/O problem
     * @see <a href="https://developer.github.com/v3/orgs/#list-user-organizations">List User Organizations</a>
     * @since 0.24
     */
    Iterable<Organization> iterate() throws IOException;
}
