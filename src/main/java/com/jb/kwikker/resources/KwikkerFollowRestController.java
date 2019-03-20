package com.jb.kwikker.resources;

import com.jb.kwikker.repositories.KwikkerFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

@RestController
public class KwikkerFollowRestController {
    @Autowired
    private KwikkerFollowRepository kwikkerFollowRepository;

    @PutMapping(path = "follow/{follower}/{author}")
    public ResponseEntity<Set<String>> follow(@PathVariable("follower") String follower, @PathVariable("author") String author) {
        return ResponseEntity.of(Optional.of(kwikkerFollowRepository.follow(follower, author)));
    }

    @DeleteMapping(path = "follow/{follower}/{author}")
    public ResponseEntity<Set<String>> unfollow(@PathVariable("follower") String follower, @PathVariable("author") String author) {
        return ResponseEntity.of(Optional.of(kwikkerFollowRepository.unfollow(follower, author)));
    }
}
