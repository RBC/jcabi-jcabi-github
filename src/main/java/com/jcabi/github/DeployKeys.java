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
import java.io.IOException;

/**
 * Github deploy keys.
 *
 * @since 0.8
 * @see <a href="https://developer.github.com/v3/repos/keys/">Deploy Keys API</a>
 */
@Immutable
public interface DeployKeys {

    /**
     * Owner of them.
     * @return Repo
     */
    Repo repo();

    /**
     * Iterate them all.
     * @return Iterator of deploy keys
     * @see <a href="https://developer.github.com/v3/repos/keys/#list">List</a>
     */
    Iterable<DeployKey> iterate();

    /**
     * Get a single deploy key.
     * @param number Id of a deploy key
     * @return Deploy key
     * @see <a href="https://developer.github.com/v3/repos/keys/#get">Get a deploy key</a>
     */
    DeployKey get(int number);

    /**
     * Create a deploy key.
     * @param title Title
     * @param key Key
     * @return A new deploy key
     * @throws IOException If there is any I/O problem
     * @see <a href="https://developer.github.com/v3/repos/keys/#create">Add a new deploy key</a>
     */
    DeployKey create(String title, String key) throws IOException;

}
