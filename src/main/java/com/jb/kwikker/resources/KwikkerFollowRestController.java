package com.jb.kwikker.resources;

import com.jb.kwikker.repositories.KwikkerFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/follow/{follower}")
public class KwikkerFollowRestController {
    @Autowired
    private KwikkerFollowRepository kwikkerFollowRepository;

    @PutMapping(path = "/{author}")
    public ResponseEntity<Set<String>> follow(@PathVariable("follower") String follower, @PathVariable("author") String author) {
        return ResponseEntity.of(Optional.of(kwikkerFollowRepository.follow(follower, author)));
    }

    @DeleteMapping(path = "/{author}")
    public ResponseEntity<Set<String>> unfollow(@PathVariable("follower") String follower, @PathVariable("author") String author) {
        return ResponseEntity.of(Optional.of(kwikkerFollowRepository.unfollow(follower, author)));
    }

    @GetMapping()
    public ResponseEntity<Set<String>> getFollowed(@PathVariable("follower") String follower) {
        return ResponseEntity.of(Optional.of(kwikkerFollowRepository.getFollowedAuthors(follower)));
    }
}
