package com.jb.kwikker.repositories;

import com.jb.kwikker.model.KwikkerPost;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KwikkerPostRepositoryTest {
    private static final String AUTHOR = "test author";
    private static final String JOHN_MESSAGE = "test message";
    private static final String JOHN_MESSAGE_2 = "test message 2";
    private static final String PETER_MESSAGE_3 = "other message 3";
    private static final String EVA_MESSAGE_4 = "other message 4";
    private static final String TEST_MESSAGE = "test message";
    private static final String TEST_MESSAGE_2 = "test message 2";

    private KwikkerFollowRepository kwikkerFollowRepositoryMock = mock(KwikkerFollowRepository.class);
    private KwikkerPostRepository kwikkerPostRepository = new KwikkerPostRepository(kwikkerFollowRepositoryMock);

    @Test
    public void shouldAcceptPost() {
        assertThat(kwikkerPostRepository.savePost(AUTHOR, TEST_MESSAGE)).isNotBlank();
    }

    @Test
    public void shouldReadSavedPostById() {
        String postId = kwikkerPostRepository.savePost(AUTHOR, TEST_MESSAGE);

        KwikkerPost post = kwikkerPostRepository.getPostById(postId);
        assertThat(post.getAuthor()).isEqualTo(AUTHOR);
        assertThat(post.getMessage()).isEqualTo(TEST_MESSAGE);
        assertThat(post.getPosted()).isBefore(OffsetDateTime.now());
    }

    @Test
    public void shouldReadNoPostByInvalidId() {
        KwikkerPost post = kwikkerPostRepository.getPostById("invalid_id");
        assertThat(post).isNull();
    }

    @Test
    public void shouldReadSavedPostsByUser() throws InterruptedException {
        kwikkerPostRepository.savePost(AUTHOR, TEST_MESSAGE);
        Thread.sleep(20);
        kwikkerPostRepository.savePost(AUTHOR, TEST_MESSAGE_2);
        kwikkerPostRepository.savePost("other author", "other message");

        List<KwikkerPost> posts = kwikkerPostRepository.getPosts(AUTHOR);
        assertThat(posts)
                .extracting(KwikkerPost::getAuthor, KwikkerPost::getMessage)
                .containsExactly(
                        tuple(AUTHOR, TEST_MESSAGE_2),
                        tuple(AUTHOR, TEST_MESSAGE));
        assertThat(posts)
                .extracting(KwikkerPost::getPosted)
                .isSortedAccordingTo(reverseOrder());
    }

    @Test
    public void shouldReadNoSavedPostsByAuthor() {
        List<KwikkerPost> posts = kwikkerPostRepository.getPosts(AUTHOR);
        assertThat(posts)
                .isEmpty();
    }

    @Test
    public void shouldReadTimelineByUser() throws InterruptedException {
        kwikkerPostRepository.savePost("john", JOHN_MESSAGE);
        Thread.sleep(20);
        kwikkerPostRepository.savePost("john", JOHN_MESSAGE_2);
        Thread.sleep(20);
        kwikkerPostRepository.savePost("peter", PETER_MESSAGE_3);
        Thread.sleep(20);
        kwikkerPostRepository.savePost("eva", EVA_MESSAGE_4);
        when(kwikkerFollowRepositoryMock.getFollowedAuthors("tom"))
                .thenReturn(new HashSet<>(asList("john", "eva")));

        List<KwikkerPost> posts = kwikkerPostRepository.getTimeline("tom");
        assertThat(posts)
                .extracting(KwikkerPost::getAuthor, KwikkerPost::getMessage)
                .containsExactly(tuple("eva", EVA_MESSAGE_4),
                        tuple("john", JOHN_MESSAGE_2),
                        tuple("john", JOHN_MESSAGE));
        assertThat(posts)
                .extracting(KwikkerPost::getPosted)
                .isSortedAccordingTo(reverseOrder());
    }

    @Test
    public void shouldReadEmptyTimelineByUser() {
        kwikkerPostRepository.savePost("john", JOHN_MESSAGE);

        List<KwikkerPost> posts = kwikkerPostRepository.getTimeline(AUTHOR);
        assertThat(posts)
                .isEmpty();
    }

    @Test
    public void shouldReadEmptyTimelineByUserWhenNoSubscription() {
        List<KwikkerPost> posts = kwikkerPostRepository.getTimeline(AUTHOR);
        assertThat(posts)
                .isEmpty();
    }
}