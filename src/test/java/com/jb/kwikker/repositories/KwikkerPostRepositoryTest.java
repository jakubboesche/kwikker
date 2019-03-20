package com.jb.kwikker.repositories;

import com.jb.kwikker.model.KwikkerPost;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.util.List;

import static java.util.Comparator.reverseOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class KwikkerPostRepositoryTest {
    private static final String AUTHOR = "test author";
    private static final String TEST_MESSAGE = "test message";
    private static final String TEST_MESSAGE_2 = "test message 2";
    private KwikkerPostRepository kwikkerPostRepository = new KwikkerPostRepository();

    @Test
    public void shouldAcceptPost() {
        assertThat(kwikkerPostRepository.savePost(AUTHOR, TEST_MESSAGE)).isNotBlank();
    }

    @Test
    public void shouldReadSavedPostById() {
        String postId = kwikkerPostRepository.savePost(AUTHOR, TEST_MESSAGE);

        KwikkerPost post = kwikkerPostRepository.getPost(postId);
        assertThat(post.getAuthor()).isEqualTo(AUTHOR);
        assertThat(post.getMessage()).isEqualTo(TEST_MESSAGE);
        assertThat(post.getPosted()).isBefore(OffsetDateTime.now());
    }

    @Test
    public void shouldReadNoPostByInvalidId() {
        KwikkerPost post = kwikkerPostRepository.getPost("invalid_id");
        assertThat(post).isNull();
    }

    @Test
    public void shouldReadSavedPostsByAuthor() throws InterruptedException {
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
}