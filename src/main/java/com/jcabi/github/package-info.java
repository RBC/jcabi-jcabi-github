/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */

/**
 * Object Oriented Github API.
 *
 * <p>The only dependency you need is (check our latest version available
 * at <a href="http://github.jcabi.com">github.jcabi.com</a>):
 *
 * <pre>&lt;depedency&gt;
 *   &lt;groupId&gt;com.jcabi&lt;/groupId&gt;
 *   &lt;artifactId&gt;jcabi-github&lt;/artifactId&gt;
 * &lt;/dependency&gt;</pre>
 *
 * <p>There are some design conventions in this library, which is important
 * to keep in mind.
 *
 * <p>Sometimes we use {@link javax.json.JsonObject}
 * as an input argument for a method
 * (for example, in {@link Repos}),
 * somewhere else we use {@link java.util.Map}
 * (see {@link Issues#iterate(Map)}),
 * in other case we use just a few plain Java types
 * (see {@link DeployKeys#create(String,String)}),
 * and sometimes we combine them
 * (see {@link Hooks}).
 * This is not a bug, it's done intentionally.
 * The logic is simple.
 * Class {@link javax.json.JsonObject} is used when Github API is
 * expecting a JSON object as HTTP request body.
 * {@link java.util.Map} is used when Github API expects HTTP query parameters,
 * and some of them are optional, and there are more than two of them.
 * In all other situations we're using plain Java types.
 *
 * @since 0.1
 * @see <a href="http://github.jcabi.com/">project website</a>
 */
package com.jcabi.github;
