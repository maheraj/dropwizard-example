package com.example.bookingwallet.api.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Link {
    private String rel;
    private String link;

    public Link(String rel, String link) {
        this.rel = rel;
        this.link = link;
    }
}
