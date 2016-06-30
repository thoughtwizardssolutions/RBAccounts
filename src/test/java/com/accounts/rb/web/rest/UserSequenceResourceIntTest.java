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

    private static final Integer DEFAULT_SALES_SEQUENCE = 1;
    private static final Integer UPDATED_SALES_SEQUENCE = 2;

    private static final Integer DEFAULT_SAMPLE_INVOICE_SEQUENCE = 1;
    private static final Integer UPDATED_SAMPLE_INVOICE_SEQUENCE = 2;
    private static final String DEFAULT_PREFIX_TAX = "AAAAA";
    private static final String UPDATED_PREFIX_TAX = "BBBBB";
    private static final String DEFAULT_PREFIX_SALES = "AAAAA";
    private static final String UPDATED_PREFIX_SALES = "BBBBB";
    private static final String DEFAULT_PREFIX_SAMPLE = "AAAAA";
    private static final String UPDATED_PREFIX_SAMPLE = "BBBBB";

    private static final Integer DEFAULT_TAX_SEQUENCE = 1;
    private static final Integer UPDATED_TAX_SEQUENCE = 2;

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
        userSequence.setSalesSequence(DEFAULT_SALES_SEQUENCE);
        userSequence.setSampleInvoiceSequence(DEFAULT_SAMPLE_INVOICE_SEQUENCE);
        userSequence.setPrefixTax(DEFAULT_PREFIX_TAX);
        userSequence.setPrefixSales(DEFAULT_PREFIX_SALES);
        userSequence.setPrefixSample(DEFAULT_PREFIX_SAMPLE);
        userSequence.setTaxSequence(DEFAULT_TAX_SEQUENCE);
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
        assertThat(testUserSequence.getSalesSequence()).isEqualTo(DEFAULT_SALES_SEQUENCE);
        assertThat(testUserSequence.getSampleInvoiceSequence()).isEqualTo(DEFAULT_SAMPLE_INVOICE_SEQUENCE);
        assertThat(testUserSequence.getPrefixTax()).isEqualTo(DEFAULT_PREFIX_TAX);
        assertThat(testUserSequence.getPrefixSales()).isEqualTo(DEFAULT_PREFIX_SALES);
        assertThat(testUserSequence.getPrefixSample()).isEqualTo(DEFAULT_PREFIX_SAMPLE);
        assertThat(testUserSequence.getTaxSequence()).isEqualTo(DEFAULT_TAX_SEQUENCE);
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
    public void getAllUserSequences() throws Exception {
        // Initialize the database
        userSequenceRepository.saveAndFlush(userSequence);

        // Get all the userSequences
        restUserSequenceMockMvc.perform(get("/api/user-sequences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userSequence.getId().intValue())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].salesSequence").value(hasItem(DEFAULT_SALES_SEQUENCE)))
                .andExpect(jsonPath("$.[*].sampleInvoiceSequence").value(hasItem(DEFAULT_SAMPLE_INVOICE_SEQUENCE)))
                .andExpect(jsonPath("$.[*].prefix_tax").value(hasItem(DEFAULT_PREFIX_TAX.toString())))
                .andExpect(jsonPath("$.[*].prefix_sales").value(hasItem(DEFAULT_PREFIX_SALES.toString())))
                .andExpect(jsonPath("$.[*].prefix_sample").value(hasItem(DEFAULT_PREFIX_SAMPLE.toString())))
                .andExpect(jsonPath("$.[*].taxSequence").value(hasItem(DEFAULT_TAX_SEQUENCE)));
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
            .andExpect(jsonPath("$.salesSequence").value(DEFAULT_SALES_SEQUENCE))
            .andExpect(jsonPath("$.sampleInvoiceSequence").value(DEFAULT_SAMPLE_INVOICE_SEQUENCE))
            .andExpect(jsonPath("$.prefix_tax").value(DEFAULT_PREFIX_TAX.toString()))
            .andExpect(jsonPath("$.prefix_sales").value(DEFAULT_PREFIX_SALES.toString()))
            .andExpect(jsonPath("$.prefix_sample").value(DEFAULT_PREFIX_SAMPLE.toString()))
            .andExpect(jsonPath("$.taxSequence").value(DEFAULT_TAX_SEQUENCE));
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
        updatedUserSequence.setSalesSequence(UPDATED_SALES_SEQUENCE);
        updatedUserSequence.setSampleInvoiceSequence(UPDATED_SAMPLE_INVOICE_SEQUENCE);
        updatedUserSequence.setPrefixTax(UPDATED_PREFIX_TAX);
        updatedUserSequence.setPrefixSales(UPDATED_PREFIX_SALES);
        updatedUserSequence.setPrefixSample(UPDATED_PREFIX_SAMPLE);
        updatedUserSequence.setTaxSequence(UPDATED_TAX_SEQUENCE);

        restUserSequenceMockMvc.perform(put("/api/user-sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUserSequence)))
                .andExpect(status().isOk());

        // Validate the UserSequence in the database
        List<UserSequence> userSequences = userSequenceRepository.findAll();
        assertThat(userSequences).hasSize(databaseSizeBeforeUpdate);
        UserSequence testUserSequence = userSequences.get(userSequences.size() - 1);
        assertThat(testUserSequence.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testUserSequence.getSalesSequence()).isEqualTo(UPDATED_SALES_SEQUENCE);
        assertThat(testUserSequence.getSampleInvoiceSequence()).isEqualTo(UPDATED_SAMPLE_INVOICE_SEQUENCE);
        assertThat(testUserSequence.getPrefixTax()).isEqualTo(UPDATED_PREFIX_TAX);
        assertThat(testUserSequence.getPrefixSales()).isEqualTo(UPDATED_PREFIX_SALES);
        assertThat(testUserSequence.getPrefixSample()).isEqualTo(UPDATED_PREFIX_SAMPLE);
        assertThat(testUserSequence.getTaxSequence()).isEqualTo(UPDATED_TAX_SEQUENCE);
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
