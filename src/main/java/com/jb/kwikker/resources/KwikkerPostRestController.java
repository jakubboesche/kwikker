package com.jb.kwikker.resources;

import com.jb.kwikker.api.KwikkerPostDto;
import com.jb.kwikker.api.KwikkerPostUploadDto;
import com.jb.kwikker.model.KwikkerPost;
import com.jb.kwikker.repositories.KwikkerPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
public class KwikkerPostRestController {
    private static final int MAX_POST_LENGTH = 140;
    @Autowired
    private KwikkerPostRepository kwikkerPostRepository;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Resource> postUpload(@RequestBody KwikkerPostUploadDto kwikkerPost) {
        if (kwikkerPost.getMessage().length() > MAX_POST_LENGTH) {
            return ResponseEntity.badRequest().build();
        }
        String id = kwikkerPostRepository.savePost(kwikkerPost.getAuthor(), kwikkerPost.getMessage());
        return ResponseEntity.created(URI.create(id)).build();
    }

    @GetMapping(path = "wall/{user}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<KwikkerPostDto>> getWall(@PathVariable("user") String user) {
        return ResponseEntity.of(
                Optional.of(kwikkerPostRepository.getPosts(user)
                        .stream().map(KwikkerPost::toDto).collect(toList())));
    }

    @GetMapping(path = "timeline/{user}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<KwikkerPostDto>> getTimeline(@PathVariable("user") String user) {
        return ResponseEntity.of(
                Optional.of(kwikkerPostRepository.getTimeline(user)
                        .stream().map(KwikkerPost::toDto).collect(toList())));
    }
}
