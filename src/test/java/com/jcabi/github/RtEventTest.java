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

import com.jcabi.http.mock.MkAnswer;
import com.jcabi.http.mock.MkContainer;
import com.jcabi.http.mock.MkGrizzlyContainer;
import com.jcabi.http.request.ApacheRequest;
import com.jcabi.http.request.FakeRequest;
import java.net.HttpURLConnection;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test case for {@link RtEvent}.
 *
 * @checkstyle MultipleStringLiteralsCheck (100 lines)
 */
public final class RtEventTest {

    /**
     * The rule for skipping test if there's BindException.
     * @checkstyle VisibilityModifierCheck (3 lines)
     */
    @Rule
    public final transient RandomPort resource = new RandomPort();

    /**
     * RtEvent can retrieve its own repo.
     *
     */
    @Test
    public void canRetrieveOwnRepo() {
        final Repo repo = this.repo();
        final RtEvent event = new RtEvent(new FakeRequest(), repo, 1);
        MatcherAssert.assertThat(
            event.repo(),
            Matchers.sameInstance(repo)
        );
    }

    /**
     * RtEvent can retrieve its own number.
     *
     */
    @Test
    public void canRetrieveOwnNumber() {
        final Repo repo = this.repo();
        final RtEvent event = new RtEvent(new FakeRequest(), repo, 2);
        MatcherAssert.assertThat(
            event.number(),
            Matchers.equalTo(2)
        );
    }

    /**
     * RtEvent can be retrieved in JSON form.
     *
     * @throws Exception If a problem occurs.
     */
    @Test
    public void retrieveEventAsJson() throws Exception {
        try (final MkContainer container = new MkGrizzlyContainer().next(
            new MkAnswer.Simple(
                HttpURLConnection.HTTP_OK,
                "{\"test\":\"events\"}"
            )
        ).start(this.resource.port())) {
            final RtEvent event = new RtEvent(
                new ApacheRequest(container.home()),
                this.repo(),
                3
            );
            MatcherAssert.assertThat(
                event.json().getString("test"),
                Matchers.equalTo("events")
            );
        }
    }

    /**
     * RtEvent should be able to compare different instances.
     *
     */
    @Test
    public void canCompareInstances() {
        final RtEvent less = new RtEvent(new FakeRequest(), this.repo(), 1);
        final RtEvent greater = new RtEvent(new FakeRequest(), this.repo(), 2);
        MatcherAssert.assertThat(
            less.compareTo(greater), Matchers.lessThan(0)
        );
        MatcherAssert.assertThat(
            greater.compareTo(less), Matchers.greaterThan(0)
        );
    }

    /**
     * Create and return repo for testing.
     * @return Repo
     */
    private Repo repo() {
        final Repo repo = Mockito.mock(Repo.class);
        Mockito.doReturn(new Coordinates.Simple("test", "event"))
            .when(repo).coordinates();
        return repo;
    }

}
