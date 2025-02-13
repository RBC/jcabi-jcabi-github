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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.xembly.Directives;

/**
 * Test case for {@link MkStorage}.
 * @checkstyle MultipleStringLiteralsCheck (200 lines)
 */
@SuppressWarnings("PMD.DoNotUseThreads")
public final class MkStorageTest {

    /**
     * MkStorage can text and write.
     * @throws Exception If some problem inside
     */
    @Test
    public void readsAndWrites() throws Exception {
        final MkStorage storage = new MkStorage.InFile();
        storage.lock();
        try {
            storage.apply(
                new Directives().xpath("/github").add("test")
                    .set("hello, world")
            );
            MatcherAssert.assertThat(
                storage.xml().xpath("/github/test/text()").get(0),
                Matchers.endsWith(", world")
            );
        } finally {
            storage.unlock();
        }
    }

    /**
     * MkStorage can lock and unlock files.
     * @throws Exception If some problem inside
     */
    @Test
    public void locksAndUnlocks() throws Exception {
        final MkStorage storage = new MkStorage.Synced(new MkStorage.InFile());
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Runnable second = () -> storage.lock();
        storage.lock();
        Future<?> future = executor.submit(second);
        try {
            future.get(1L, TimeUnit.SECONDS);
            MatcherAssert.assertThat("timeout SHOULD happen", false);
        } catch (final TimeoutException ex) {
            future.cancel(true);
        } finally {
            storage.unlock();
        }
        future = executor.submit(second);
        try {
            future.get(1L, TimeUnit.SECONDS);
        } catch (final TimeoutException ex) {
            MatcherAssert.assertThat("timeout SHOULD NOT happen", false);
            future.cancel(true);
        }
        executor.shutdown();
    }

}
