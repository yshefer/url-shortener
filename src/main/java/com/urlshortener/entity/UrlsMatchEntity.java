package com.urlshortener.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing the mapping between a shortened URL and its corresponding long URL.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "urls_match_table")
public class UrlsMatchEntity {

    @Id
    @Column(name = "short_url_id")
    private String shortUrlId;

    @Column(name = "long_url")
    private String longUrl;

    /**
     * Constructs a new {@code UrlsMatchEntity} with the given short URL ID and long URL.
     *
     * @param shortUrlId the unique identifier for the shortened URL.
     * @param longUrl    the original long URL corresponding to the shortened URL.
     */
    public UrlsMatchEntity(String shortUrlId, String longUrl) {
        this.shortUrlId = shortUrlId;
        this.longUrl = longUrl;
    }
}
