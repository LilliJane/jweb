package eu.epitech.jweb.web.rest;

import com.codahale.metrics.annotation.Timed;
import eu.epitech.jweb.domain.News;
import eu.epitech.jweb.repository.NewsRepository;
import eu.epitech.jweb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing News.
 */
@RestController
@RequestMapping("/api")
public class NewsResource {

    private final Logger log = LoggerFactory.getLogger(NewsResource.class);
        
    @Inject
    private NewsRepository newsRepository;
    
    /**
     * POST  /newss -> Create a new news.
     */
    @RequestMapping(value = "/newss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<News> createNews(@Valid @RequestBody News news) throws URISyntaxException {
        log.debug("REST request to save News : {}", news);
        if (news.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("news", "idexists", "A new news cannot already have an ID")).body(null);
        }
        News result = newsRepository.save(news);
        return ResponseEntity.created(new URI("/api/newss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("news", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /newss -> Updates an existing news.
     */
    @RequestMapping(value = "/newss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<News> updateNews(@Valid @RequestBody News news) throws URISyntaxException {
        log.debug("REST request to update News : {}", news);
        if (news.getId() == null) {
            return createNews(news);
        }
        News result = newsRepository.save(news);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("news", news.getId().toString()))
            .body(result);
    }

    /**
     * GET  /newss -> get all the newss.
     */
    @RequestMapping(value = "/newss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<News> getAllNewss() {
        log.debug("REST request to get all Newss");
        return newsRepository.findAll();
            }

    /**
     * GET  /newss/:id -> get the "id" news.
     */
    @RequestMapping(value = "/newss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<News> getNews(@PathVariable Long id) {
        log.debug("REST request to get News : {}", id);
        News news = newsRepository.findOne(id);
        return Optional.ofNullable(news)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /newss/:id -> delete the "id" news.
     */
    @RequestMapping(value = "/newss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        log.debug("REST request to delete News : {}", id);
        newsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("news", id.toString())).build();
    }
}
