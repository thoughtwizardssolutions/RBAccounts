package com.accounts.rb.web.rest;

import com.accounts.rb.RbaccountsApp;
import com.accounts.rb.domain.UserSequence;
import com.accounts.rb.repository.UserSequenceRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UserSequenceResource REST controller.
 *
 * @see UserSequenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RbaccountsApp.class)
@WebAppConfiguration
@IntegrationTest
public class UserSequenceResourceIntTest {

    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";
    private static final String DEFAULT_PREFIX = "AAAAA";
    private static final String UPDATED_PREFIX = "BBBBB";

    private static final Integer DEFAULT_CURRENT_SEQUENCE = 1;
    private static final Integer UPDATED_CURRENT_SEQUENCE = 2;

    @Inject
    private UserSequenceRepository userSequenceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserSequenceMockMvc;

    private UserSequence userSequence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserSequenceResource userSequenceResource = new UserSequenceResource();
        ReflectionTestUtils.setField(userSequenceResource, "userSequenceRepository", userSequenceRepository);
        this.restUserSequenceMockMvc = MockMvcBuilders.standaloneSetup(userSequenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userSequence = new UserSequence();
        userSequence.setCreatedBy(DEFAULT_CREATED_BY);
        userSequence.setPrefix(DEFAULT_PREFIX);
        userSequence.setCurrentSequence(DEFAULT_CURRENT_SEQUENCE);
    }

    @Test
    @Transactional
    public void createUserSequence() throws Exception {
        int databaseSizeBeforeCreate = userSequenceRepository.findAll().size();

        // Create the UserSequence

        restUserSequenceMockMvc.perform(post("/api/user-sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userSequence)))
                .andExpect(status().isCreated());

        // Validate the UserSequence in the database
        List<UserSequence> userSequences = userSequenceRepository.findAll();
        assertThat(userSequences).hasSize(databaseSizeBeforeCreate + 1);
        UserSequence testUserSequence = userSequences.get(userSequences.size() - 1);
        assertThat(testUserSequence.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testUserSequence.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testUserSequence.getCurrentSequence()).isEqualTo(DEFAULT_CURRENT_SEQUENCE);
    }

    @Test
    @Transactional
    public void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSequenceRepository.findAll().size();
        // set the field null
        userSequence.setCreatedBy(null);

        // Create the UserSequence, which fails.

        restUserSequenceMockMvc.perform(post("/api/user-sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userSequence)))
                .andExpect(status().isBadRequest());

        List<UserSequence> userSequences = userSequenceRepository.findAll();
        assertThat(userSequences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrefixIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSequenceRepository.findAll().size();
        // set the field null
        userSequence.setPrefix(null);

        // Create the UserSequence, which fails.

        restUserSequenceMockMvc.perform(post("/api/user-sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userSequence)))
                .andExpect(status().isBadRequest());

        List<UserSequence> userSequences = userSequenceRepository.findAll();
        assertThat(userSequences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCurrentSequenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = userSequenceRepository.findAll().size();
        // set the field null
        userSequence.setCurrentSequence(null);

        // Create the UserSequence, which fails.

        restUserSequenceMockMvc.perform(post("/api/user-sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userSequence)))
                .andExpect(status().isBadRequest());

        List<UserSequence> userSequences = userSequenceRepository.findAll();
        assertThat(userSequences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserSequences() throws Exception {
        // Initialize the database
        userSequenceRepository.saveAndFlush(userSequence);

        // Get all the userSequences
        restUserSequenceMockMvc.perform(get("/api/user-sequences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userSequence.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX.toString())))
                .andExpect(jsonPath("$.[*].currentSequence").value(hasItem(DEFAULT_CURRENT_SEQUENCE)));
    }

    @Test
    @Transactional
    public void getUserSequence() throws Exception {
        // Initialize the database
        userSequenceRepository.saveAndFlush(userSequence);

        // Get the userSequence
        restUserSequenceMockMvc.perform(get("/api/user-sequences/{id}", userSequence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userSequence.getId().intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX.toString()))
            .andExpect(jsonPath("$.currentSequence").value(DEFAULT_CURRENT_SEQUENCE));
    }

    @Test
    @Transactional
    public void getNonExistingUserSequence() throws Exception {
        // Get the userSequence
        restUserSequenceMockMvc.perform(get("/api/user-sequences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserSequence() throws Exception {
        // Initialize the database
        userSequenceRepository.saveAndFlush(userSequence);
        int databaseSizeBeforeUpdate = userSequenceRepository.findAll().size();

        // Update the userSequence
        UserSequence updatedUserSequence = new UserSequence();
        updatedUserSequence.setId(userSequence.getId());
        updatedUserSequence.setCreatedBy(UPDATED_CREATED_BY);
        updatedUserSequence.setPrefix(UPDATED_PREFIX);
        updatedUserSequence.setCurrentSequence(UPDATED_CURRENT_SEQUENCE);

        restUserSequenceMockMvc.perform(put("/api/user-sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserSequence)))
                .andExpect(status().isOk());

        // Validate the UserSequence in the database
        List<UserSequence> userSequences = userSequenceRepository.findAll();
        assertThat(userSequences).hasSize(databaseSizeBeforeUpdate);
        UserSequence testUserSequence = userSequences.get(userSequences.size() - 1);
        assertThat(testUserSequence.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserSequence.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testUserSequence.getCurrentSequence()).isEqualTo(UPDATED_CURRENT_SEQUENCE);
    }

    @Test
    @Transactional
    public void deleteUserSequence() throws Exception {
        // Initialize the database
        userSequenceRepository.saveAndFlush(userSequence);
        int databaseSizeBeforeDelete = userSequenceRepository.findAll().size();

        // Get the userSequence
        restUserSequenceMockMvc.perform(delete("/api/user-sequences/{id}", userSequence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserSequence> userSequences = userSequenceRepository.findAll();
        assertThat(userSequences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
