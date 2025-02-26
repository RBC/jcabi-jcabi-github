/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */

package com.jcabi.github;

import com.jcabi.aspects.Immutable;
import java.io.IOException;

/**
 * Github repository collaborators.
 * @since 0.8
 */
@Immutable
public interface Collaborators {
    /**
     * Permission levels a user can be granted in an organization repository.
     *
     * @see <a href="https://developer.github.com/v3/repos/collaborators/#parameters-1">Add user with permissions</a>
     */
    enum Permission { PULL, PUSH, ADMIN, MAINTAIN, TRIAGE };

    /**
     * Owner of them.
     * @return Repo
     */
    Repo repo();

    /**
     * Check if a user is collaborator.
     *
     * @param user User
     * @return True is a user is a collaborator, otherwise returns false
     * @throws IOException If there is any I/O problem
     * @see <a href="https://developer.github.com/v3/repos/collaborators/#get">
     *  Check if a user is collaborator</a>
     */
    boolean isCollaborator(String user) throws IOException;

    /**
     * Add user as a collaborator.
     *
     * @param user User
     * @throws IOException If there is any I/O problem
     * @see <a href="https://developer.github.com/v3/repos/collaborators/#add-collaborator">Add user as a collaborator</a>
     */
    void add(String user) throws IOException;

    /**
     * Add user with permissions. Only works on an organization repository
     *
     * @param user User to add
     * @param permission Permission level to grant
     * @throws IOException if there is an I/O problem
     * @see <a href=https://developer.github.com/v3/repos/collaborators/#add-user-as-a-collaborator">Add user as a collaborator</a>
     */
    void addWithPermission(String user,
        Collaborators.Permission permission) throws IOException;

    /**
     * Get user permission in this repo.
     *
     * @param user User to check
     * @return Permission level granted, incl. "admin", "write",
     *  "read", or "none"
     * @throws IOException if there is an I/O problem
     * @see <a href=https://docs.github.com/en/rest/collaborators/collaborators#get-repository-permissions-for-a-user">Get repository permissions for a user</a>
     */
    String permission(String user) throws IOException;

    /**
     * Remove user as a collaborator.
     *
     * @param user User
     * @throws IOException If there is any I/O problem
     * @see <a href="https://developer.github.com/v3/repos/collaborators/#remove-collaborator">Remove user as a collaborator</a>
     */
    void remove(String user) throws IOException;

    /**
     * Iterates over repo collaborators.
     * @return Iterator on repo collaborators.
     */
    Iterable<User> iterate();
}
