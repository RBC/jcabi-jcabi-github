/**
 * SPDX-FileCopyrightText: Copyright (c) 2013-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
 */
package com.jcabi.github.mock;

import com.jcabi.github.Event;
import com.jcabi.github.Issue;
import com.jcabi.github.IssueLabels;
import com.jcabi.github.Label;
import com.jcabi.github.Repo;
import java.util.Collections;
import java.util.Iterator;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case for {@link MkIssueLabels}.
 */
public final class MkIssueLabelsTest {
    /**
     * Username of actor.
     */
    private static final String USER = "jeff";

    /**
     * MkIssueLabels can list labels.
     * @throws Exception If some problem inside
     */
    @Test
    public void iteratesIssues() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final String name = "bug";
        repo.labels().create(name, "c0c0c0");
        final Issue issue = repo.issues().create("title", "body");
        issue.labels().add(Collections.singletonList(name));
        MatcherAssert.assertThat(
            issue.labels().iterate(),
            Matchers.<Label>iterableWithSize(1)
        );
    }

    /**
     * MkIssueLabels can create labels through Smart decorator.
     * @throws Exception If some problem inside
     */
    @Test
    public void createsLabelsThroughDecorator() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final Issue issue = repo.issues().create("how are you?", "");
        final String name = "task";
        new IssueLabels.Smart(issue.labels()).addIfAbsent(name, "f0f0f0");
        MatcherAssert.assertThat(
            issue.labels().iterate(),
            Matchers.<Label>iterableWithSize(1)
        );
    }

    /**
     * MkIssueLabels creates a "labeled" event when a label is added.
     * @throws Exception If some problem inside
     */
    @Test
    public void addingLabelGeneratesEvent() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final String name = "confirmed";
        repo.labels().create(name, "663399");
        final Issue issue = repo.issues().create("Titular", "Corpus");
        issue.labels().add(Collections.singletonList(name));
        MatcherAssert.assertThat(
            issue.events(),
            Matchers.<Event>iterableWithSize(1)
        );
        final Event.Smart labeled = new Event.Smart(
            issue.events().iterator().next()
        );
        MatcherAssert.assertThat(
            labeled.type(),
            Matchers.equalTo(Event.LABELED)
        );
        MatcherAssert.assertThat(
            labeled.author().login(),
            Matchers.equalTo(USER)
        );
        MatcherAssert.assertThat(
            labeled.repo(),
            Matchers.equalTo(repo)
        );
        MatcherAssert.assertThat(
            labeled.label().get().name(),
            Matchers.equalTo(name)
        );
    }

    /**
     * MkIssueLabels creates an "unlabeled" event when a label is removed.
     * @throws Exception If some problem inside
     */
    @Test
    public void removingLabelGeneratesEvent() throws Exception {
        final Repo repo = new MkGithub().randomRepo();
        final String name = "invalid";
        repo.labels().create(name, "ee82ee");
        final Issue issue = repo.issues().create("Rewrite", "Sound good?");
        issue.labels().add(Collections.singletonList(name));
        issue.labels().remove(name);
        MatcherAssert.assertThat(
            issue.events(),
            Matchers.<Event>iterableWithSize(2)
        );
        final Iterator<Event> events = issue.events().iterator();
        events.next();
        final Event.Smart unlabeled = new Event.Smart(events.next());
        MatcherAssert.assertThat(
            unlabeled.type(),
            Matchers.equalTo(Event.UNLABELED)
        );
        MatcherAssert.assertThat(
            unlabeled.author().login(),
            Matchers.equalTo(USER)
        );
        MatcherAssert.assertThat(
            unlabeled.repo(),
            Matchers.equalTo(repo)
        );
        MatcherAssert.assertThat(
            unlabeled.label().get().name(),
            Matchers.equalTo(name)
        );
    }
}
