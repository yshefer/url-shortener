package com.urlshortener.repository;

import com.urlshortener.entity.UrlsMatchEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for managing URL mappings in the database.
 */
public interface UrlShortenerRepository extends CrudRepository<UrlsMatchEntity, String> {

    /**
     * Inserts a new URL mapping into the database.
     * <p>
     * This method executes a custom SQL query to directly insert a short URL ID
     * and its corresponding long URL into the database table.
     * </p>
     *
     * @param shortUrlId the unique identifier for the shortened URL.
     * @param longUrl    the original long URL to be associated with the short URL ID.
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO urls_match_table (short_url_id, long_url) VALUES (:shortUrlId, :longUrl)", nativeQuery = true)
    void insert(@Param("shortUrlId") String shortUrlId, @Param("longUrl") String longUrl);

    /**
     * Finds a URL mapping entity by its long URL.
     *
     * @param longUrl the original long URL to search for.
     * @return an {@link Optional} containing the matching {@link UrlsMatchEntity},
     * or an empty {@code Optional} if no match is found.
     */
    Optional<UrlsMatchEntity> findByLongUrl(String longUrl);
}
