package com.jb.kwikker.model;

import com.jb.kwikker.api.KwikkerPostDto;
import org.springframework.beans.BeanUtils;

import java.time.OffsetDateTime;

public class KwikkerPost {
    private final String author;
    private final String message;
    private final OffsetDateTime posted;

    public KwikkerPost(String author, String message, OffsetDateTime posted) {
        this.author = author;
        this.message = message;
        this.posted = posted;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getPosted() {
        return posted;
    }

    public KwikkerPostDto toDto() {
        KwikkerPostDto kwikkerPostDto = new KwikkerPostDto();
        BeanUtils.copyProperties(this, kwikkerPostDto);
        return kwikkerPostDto;
    }
}
