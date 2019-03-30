package com.jb.kwikker.repositories;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KwikkerFollowRepositoryTest {
    private final KwikkerFollowRepository kwikkerFollowRepository = new KwikkerFollowRepository();

    @Test
    public void shouldFollow() {
        assertThat(kwikkerFollowRepository.follow("john", "peter"))
                .containsOnly("peter");
    }

    @Test
    public void shouldUnfollowWhenNotFollowedBefore() {
        assertThat(kwikkerFollowRepository.unfollow("john", "peter"))
                .isEmpty();
    }

    @Test
    public void shouldUnfollowWhenFollowedBefore() {
        kwikkerFollowRepository.follow("john", "peter");
        kwikkerFollowRepository.follow("john", "michael");

        assertThat(kwikkerFollowRepository.unfollow("john", "peter"))
                .containsOnly("michael");
    }

    @Test
    public void shouldReturnFollowedAuthorsList() {
        kwikkerFollowRepository.follow("john", "peter");
        kwikkerFollowRepository.follow("john", "michael");

        assertThat(kwikkerFollowRepository.getFollowedAuthors("john"))
                .containsExactlyInAnyOrder("peter", "michael");
    }
}