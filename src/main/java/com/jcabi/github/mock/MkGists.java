/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.aspects.Immutable;
import com.jcabi.aspects.Loggable;
import com.jcabi.github.Gist;
import com.jcabi.github.Gists;
import com.jcabi.github.Github;
import java.io.IOException;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.xembly.Directives;

/**
 * Mock Github gists.
 *
 * @since 0.5
 */
@Immutable
@Loggable(Loggable.DEBUG)
@ToString
@EqualsAndHashCode(of = { "storage", "self" })
final class MkGists implements Gists {

    /**
     * Storage.
     */
    private final transient MkStorage storage;

    /**
     * Login of the user logged in.
     */
    private final transient String self;

    /**
     * Public ctor.
     * @param stg Storage
     * @param login User to login
     * @throws IOException If there is any I/O problem
     */
    MkGists(
        final MkStorage stg,
        final String login
    ) throws IOException {
        this.storage = stg;
        this.self = login;
        this.storage.apply(
            new Directives().xpath("/github").addIf("gists")
        );
    }

    @Override
    public Github github() {
        return new MkGithub(this.storage, this.self);
    }

    @Override
    public Gist create(
        final Map<String, String> files, final boolean visible
    ) throws IOException {
        this.storage.lock();
        final String number;
        try {
            number = Integer.toString(
                1 + this.storage.xml().xpath(
                    String.format("%s/gist/id/text()", this.xpath())
                ).size()
            );
            final Directives dirs = new Directives().xpath(this.xpath())
                .add("gist")
                .add("id").set(number).up()
                .add("public").set(String.valueOf(visible)).up()
                .add("files");
            for (final Map.Entry<String, String> file : files.entrySet()) {
                dirs.add("file")
                    .add("filename").set(file.getKey()).up()
                    .add("raw_content").set(file.getValue()).up().up();
            }
            this.storage.apply(dirs);
        } finally {
            this.storage.unlock();
        }
        return this.get(number);
    }

    @Override
    public Gist get(final String name
    ) {
        return new MkGist(this.storage, this.self, name);
    }

    @Override
    public Iterable<Gist> iterate() {
        return new MkIterable<>(
            this.storage,
            String.format("%s/gist", this.xpath()),
            xml -> this.get(xml.xpath("id/text()").get(0))
        );
    }

    @Override
    public void remove(final String identifier
    ) throws IOException {
        this.storage.apply(
            new Directives().xpath(
                String.format("%s/gist[id='%s']", this.xpath(), identifier)
            ).remove()
        );
    }

    /**
     * XPath of this element in XML tree.
     * @return XPath
     */
    private String xpath() {
        return "/github/gists";
    }

}
