package eu.epitech.jweb.repository;

import eu.epitech.jweb.domain.News;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the News entity.
 */
public interface NewsRepository extends JpaRepository<News,Long> {

}
