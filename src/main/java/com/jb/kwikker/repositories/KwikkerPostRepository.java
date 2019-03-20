package com.jb.kwikker.repositories;

import com.jb.kwikker.model.KwikkerPost;
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

    public String savePost(String author, String message) {
        String uuid = UUID.randomUUID().toString();
        KwikkerPost post = new KwikkerPost(author, message, OffsetDateTime.now());
        postById.put(uuid, post);
        postsByAuthor.computeIfAbsent(author, s -> new ArrayList<>()).add(post);
        return uuid;
    }

    public KwikkerPost getPost(String postId) {
        return postById.getOrDefault(postId, null);
    }

    public List<KwikkerPost> getPosts(String author) {
        return postsByAuthor.getOrDefault(author, emptyList()).stream()
                .sorted(comparing(KwikkerPost::getPosted).reversed())
                .collect(toList());
    }
}
