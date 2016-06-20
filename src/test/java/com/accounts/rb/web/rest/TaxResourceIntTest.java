package com.accounts.rb.web.rest;

import com.accounts.rb.RbaccountsApp;
import com.accounts.rb.domain.Tax;
import com.accounts.rb.repository.TaxRepository;

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
import java.math.BigDecimal;;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TaxResource REST controller.
 *
 * @see TaxResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RbaccountsApp.class)
@WebAppConfiguration
@IntegrationTest
public class TaxResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final BigDecimal DEFAULT_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RATE = new BigDecimal(2);
    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    @Inject
    private TaxRepository taxRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTaxMockMvc;

    private Tax tax;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TaxResource taxResource = new TaxResource();
        ReflectionTestUtils.setField(taxResource, "taxRepository", taxRepository);
        this.restTaxMockMvc = MockMvcBuilders.standaloneSetup(taxResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tax = new Tax();
        tax.setName(DEFAULT_NAME);
        tax.setRate(DEFAULT_RATE);
        tax.setCreatedBy(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void createTax() throws Exception {
        int databaseSizeBeforeCreate = taxRepository.findAll().size();

        // Create the Tax

        restTaxMockMvc.perform(post("/api/taxes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tax)))
                .andExpect(status().isCreated());

        // Validate the Tax in the database
        List<Tax> taxes = taxRepository.findAll();
        assertThat(taxes).hasSize(databaseSizeBeforeCreate + 1);
        Tax testTax = taxes.get(taxes.size() - 1);
        assertThat(testTax.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTax.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testTax.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRepository.findAll().size();
        // set the field null
        tax.setName(null);

        // Create the Tax, which fails.

        restTaxMockMvc.perform(post("/api/taxes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tax)))
                .andExpect(status().isBadRequest());

        List<Tax> taxes = taxRepository.findAll();
        assertThat(taxes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRateIsRequired() throws Exception {
        int databaseSizeBeforeTest = taxRepository.findAll().size();
        // set the field null
        tax.setRate(null);

        // Create the Tax, which fails.

        restTaxMockMvc.perform(post("/api/taxes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tax)))
                .andExpect(status().isBadRequest());

        List<Tax> taxes = taxRepository.findAll();
        assertThat(taxes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTaxes() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get all the taxes
        restTaxMockMvc.perform(get("/api/taxes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tax.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);

        // Get the tax
        restTaxMockMvc.perform(get("/api/taxes/{id}", tax.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tax.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTax() throws Exception {
        // Get the tax
        restTaxMockMvc.perform(get("/api/taxes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);
        int databaseSizeBeforeUpdate = taxRepository.findAll().size();

        // Update the tax
        Tax updatedTax = new Tax();
        updatedTax.setId(tax.getId());
        updatedTax.setName(UPDATED_NAME);
        updatedTax.setRate(UPDATED_RATE);
        updatedTax.setCreatedBy(UPDATED_CREATED_BY);

        restTaxMockMvc.perform(put("/api/taxes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTax)))
                .andExpect(status().isOk());

        // Validate the Tax in the database
        List<Tax> taxes = taxRepository.findAll();
        assertThat(taxes).hasSize(databaseSizeBeforeUpdate);
        Tax testTax = taxes.get(taxes.size() - 1);
        assertThat(testTax.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTax.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testTax.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    public void deleteTax() throws Exception {
        // Initialize the database
        taxRepository.saveAndFlush(tax);
        int databaseSizeBeforeDelete = taxRepository.findAll().size();

        // Get the tax
        restTaxMockMvc.perform(delete("/api/taxes/{id}", tax.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tax> taxes = taxRepository.findAll();
        assertThat(taxes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
