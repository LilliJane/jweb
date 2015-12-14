package eu.epitech.jweb.web.rest;

import eu.epitech.jweb.Application;
import eu.epitech.jweb.domain.News;
import eu.epitech.jweb.repository.NewsRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the NewsResource REST controller.
 *
 * @see NewsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NewsResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private NewsRepository newsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNewsMockMvc;

    private News news;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NewsResource newsResource = new NewsResource();
        ReflectionTestUtils.setField(newsResource, "newsRepository", newsRepository);
        this.restNewsMockMvc = MockMvcBuilders.standaloneSetup(newsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        news = new News();
        news.setTitle(DEFAULT_TITLE);
        news.setContent(DEFAULT_CONTENT);
        news.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createNews() throws Exception {
        int databaseSizeBeforeCreate = newsRepository.findAll().size();

        // Create the News

        restNewsMockMvc.perform(post("/api/newss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(news)))
                .andExpect(status().isCreated());

        // Validate the News in the database
        List<News> newss = newsRepository.findAll();
        assertThat(newss).hasSize(databaseSizeBeforeCreate + 1);
        News testNews = newss.get(newss.size() - 1);
        assertThat(testNews.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNews.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testNews.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsRepository.findAll().size();
        // set the field null
        news.setTitle(null);

        // Create the News, which fails.

        restNewsMockMvc.perform(post("/api/newss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(news)))
                .andExpect(status().isBadRequest());

        List<News> newss = newsRepository.findAll();
        assertThat(newss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = newsRepository.findAll().size();
        // set the field null
        news.setContent(null);

        // Create the News, which fails.

        restNewsMockMvc.perform(post("/api/newss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(news)))
                .andExpect(status().isBadRequest());

        List<News> newss = newsRepository.findAll();
        assertThat(newss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNewss() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get all the newss
        restNewsMockMvc.perform(get("/api/newss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(news.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

        // Get the news
        restNewsMockMvc.perform(get("/api/newss/{id}", news.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(news.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNews() throws Exception {
        // Get the news
        restNewsMockMvc.perform(get("/api/newss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

		int databaseSizeBeforeUpdate = newsRepository.findAll().size();

        // Update the news
        news.setTitle(UPDATED_TITLE);
        news.setContent(UPDATED_CONTENT);
        news.setDate(UPDATED_DATE);

        restNewsMockMvc.perform(put("/api/newss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(news)))
                .andExpect(status().isOk());

        // Validate the News in the database
        List<News> newss = newsRepository.findAll();
        assertThat(newss).hasSize(databaseSizeBeforeUpdate);
        News testNews = newss.get(newss.size() - 1);
        assertThat(testNews.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNews.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testNews.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void deleteNews() throws Exception {
        // Initialize the database
        newsRepository.saveAndFlush(news);

		int databaseSizeBeforeDelete = newsRepository.findAll().size();

        // Get the news
        restNewsMockMvc.perform(delete("/api/newss/{id}", news.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<News> newss = newsRepository.findAll();
        assertThat(newss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
