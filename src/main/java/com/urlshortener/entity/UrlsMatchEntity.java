package com.urlshortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "urls_match_table", indexes = {
        @Index(name = "idx_long_url", columnList = "long_url")
})
public class UrlsMatchEntity {

    @Id
    @Column(name = "short_url_id")
    private String shortUrlId;

    @Column(name = "long_url")
    private String longUrl;

    public UrlsMatchEntity(String shortUrlId, String longUrl) {
        this.shortUrlId = shortUrlId;
        this.longUrl = longUrl;
    }
}
