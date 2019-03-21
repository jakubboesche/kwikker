package com.jb.kwikker.repositories;

import com.jb.kwikker.model.KwikkerPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Repository
public class KwikkerPostRepository {
    private Map<String, KwikkerPost> postById = new HashMap<>();
    private Map<String, List<KwikkerPost>> postsByAuthor = new HashMap<>();
    private KwikkerFollowRepository kwikkerFollowRepository;

    @Autowired
    public KwikkerPostRepository(KwikkerFollowRepository kwikkerFollowRepository) {
        this.kwikkerFollowRepository = kwikkerFollowRepository;
        init();
    }

    public void init() {
        postById = new HashMap<>();
        postsByAuthor = new HashMap<>();
    }

    public String savePost(String author, String message) {
        String uuid = UUID.randomUUID().toString();
        KwikkerPost post = new KwikkerPost(author, message, OffsetDateTime.now());
        postById.put(uuid, post);
        postsByAuthor.computeIfAbsent(author, s -> new ArrayList<>()).add(post);
        return uuid;
    }

    public KwikkerPost getPostById(String postId) {
        return postById.getOrDefault(postId, null);
    }

    public List<KwikkerPost> getPosts(String user) {
        return getPostsByAuthor(user).stream()
                .sorted(comparing(KwikkerPost::getPosted).reversed())
                .collect(toList());
    }

    public List<KwikkerPost> getTimeline(String user) {
        return kwikkerFollowRepository.getFollowedAuthors(user).stream()
                .flatMap(author -> getPostsByAuthor(author).stream())
                .sorted(comparing(KwikkerPost::getPosted).reversed())
                .collect(toList());
    }

    private List<KwikkerPost> getPostsByAuthor(String author) {
        return postsByAuthor.getOrDefault(author, emptyList());
    }
}
