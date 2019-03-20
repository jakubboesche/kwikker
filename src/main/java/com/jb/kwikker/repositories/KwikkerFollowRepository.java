package com.jb.kwikker.repositories;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class KwikkerFollowRepository {
    private Map<String, Set<String>> followedByFollower = new HashMap<>();

    public Set<String> getFollowedAuthors(String follower) {
        return initByFollower(follower);
    }

    public Set<String> follow(String follower, String author) {
        initByFollower(follower).add(author);
        return followedByFollower.get(follower);
    }

    public Set<String> unfollow(String follower, String author) {
        initByFollower(follower).remove(author);
        return followedByFollower.get(follower);
    }

    private Set<String> initByFollower(String follower) {
        return followedByFollower.computeIfAbsent(follower, s -> new HashSet<>());
    }
}
