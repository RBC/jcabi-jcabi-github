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
package com.jcabi.github.mock;

import com.jcabi.github.Collaborators;
import com.jcabi.github.User;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link MkCollaborators}.
 */
public final class MkCollaboratorsTest {

    /**
     * MkCollaborators can add, remove and iterate collaborators.
     * @throws Exception If some problem inside
     */
    @Test
    public void addAndRemove() throws Exception {
        final Collaborators collaborators = this.collaborators();
        final String login = "some_user";
        collaborators.add(login);
        MatcherAssert.assertThat(
            collaborators.iterate(),
            Matchers.<User>iterableWithSize(1)
        );
        MatcherAssert.assertThat(
            collaborators.iterate().iterator().next().login(),
            Matchers.equalTo(login)
        );
        collaborators.remove(login);
        MatcherAssert.assertThat(
            collaborators.iterate(),
            Matchers.<User>iterableWithSize(0)
        );
    }

    /**
     * MkCollaborators can check whether  user is collaborator or not.
     * @throws Exception If some problem inside
     */
    @Test
    public void isCollaborator() throws Exception {
        final Collaborators collaborators = this.collaborators();
        final String collaborator = "collaborator";
        final String stranger = "stranger";
        collaborators.add(collaborator);
        MatcherAssert.assertThat(
            collaborators.isCollaborator(collaborator),
            Matchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            collaborators.isCollaborator(stranger),
            Matchers.equalTo(false)
        );
    }

    /**
     * Create a collaborators to work with.
     * @return Collaborators just created
     * @throws Exception If some problem inside
     */
    private Collaborators collaborators() throws Exception {
        return new MkGithub().randomRepo().collaborators();
    }
}
