package com.urlshortener.repository;

import com.urlshortener.entity.UrlsMatchEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UrlShortenerRepository extends CrudRepository<UrlsMatchEntity, String> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO urls_match_table (short_url_id, long_url) VALUES (:shortUrlId, :longUrl)", nativeQuery = true)
    void insert(@Param("shortUrlId") String shortUrlId, @Param("longUrl") String longUrl);

    Optional<UrlsMatchEntity> findByLongUrl(String longUrl);
}
