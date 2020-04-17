package com.example.bookingwallet.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Link {
    private String rel;
    private String link;

    public Link(String rel, String link) {
        this.rel = rel;
        this.link = link;
    }
}
