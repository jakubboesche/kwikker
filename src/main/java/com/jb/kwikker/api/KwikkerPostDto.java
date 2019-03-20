package com.jb.kwikker.api;

import java.time.OffsetDateTime;

public class KwikkerPostDto {
    private String message;
    private String author;
    private OffsetDateTime posted;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public OffsetDateTime getPosted() {
        return posted;
    }

    public void setPosted(OffsetDateTime posted) {
        this.posted = posted;
    }
}
