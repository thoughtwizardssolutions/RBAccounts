package com.accounts.rb.web.rest;

import com.accounts.rb.RbaccountsApp;
import com.accounts.rb.domain.Imei;
import com.accounts.rb.repository.ImeiRepository;

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
 * Test class for the ImeiResource REST controller.
 *
 * @see ImeiResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RbaccountsApp.class)
@WebAppConfiguration
@IntegrationTest
public class ImeiResourceIntTest {

    private static final String DEFAULT_IMEI_1 = "AAAAA";
    private static final String UPDATED_IMEI_1 = "BBBBB";
    private static final String DEFAULT_IMEI_2 = "AAAAA";
    private static final String UPDATED_IMEI_2 = "BBBBB";

    @Inject
    private ImeiRepository imeiRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restImeiMockMvc;

    private Imei imei;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImeiResource imeiResource = new ImeiResource();
        ReflectionTestUtils.setField(imeiResource, "imeiRepository", imeiRepository);
        this.restImeiMockMvc = MockMvcBuilders.standaloneSetup(imeiResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        imei = new Imei();
        imei.setImei1(DEFAULT_IMEI_1);
        imei.setImei2(DEFAULT_IMEI_2);
    }

    @Test
    @Transactional
    public void createImei() throws Exception {
        int databaseSizeBeforeCreate = imeiRepository.findAll().size();

        // Create the Imei

        restImeiMockMvc.perform(post("/api/imeis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imei)))
                .andExpect(status().isCreated());

        // Validate the Imei in the database
        List<Imei> imeis = imeiRepository.findAll();
        assertThat(imeis).hasSize(databaseSizeBeforeCreate + 1);
        Imei testImei = imeis.get(imeis.size() - 1);
        assertThat(testImei.getImei1()).isEqualTo(DEFAULT_IMEI_1);
        assertThat(testImei.getImei2()).isEqualTo(DEFAULT_IMEI_2);
    }

    @Test
    @Transactional
    public void getAllImeis() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeis
        restImeiMockMvc.perform(get("/api/imeis?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(imei.getId().intValue())))
                .andExpect(jsonPath("$.[*].imei1").value(hasItem(DEFAULT_IMEI_1.toString())))
                .andExpect(jsonPath("$.[*].imei2").value(hasItem(DEFAULT_IMEI_2.toString())));
    }

    @Test
    @Transactional
    public void getImei() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get the imei
        restImeiMockMvc.perform(get("/api/imeis/{id}", imei.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(imei.getId().intValue()))
            .andExpect(jsonPath("$.imei1").value(DEFAULT_IMEI_1.toString()))
            .andExpect(jsonPath("$.imei2").value(DEFAULT_IMEI_2.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingImei() throws Exception {
        // Get the imei
        restImeiMockMvc.perform(get("/api/imeis/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImei() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);
        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();

        // Update the imei
        Imei updatedImei = new Imei();
        updatedImei.setId(imei.getId());
        updatedImei.setImei1(UPDATED_IMEI_1);
        updatedImei.setImei2(UPDATED_IMEI_2);

        restImeiMockMvc.perform(put("/api/imeis")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedImei)))
                .andExpect(status().isOk());

        // Validate the Imei in the database
        List<Imei> imeis = imeiRepository.findAll();
        assertThat(imeis).hasSize(databaseSizeBeforeUpdate);
        Imei testImei = imeis.get(imeis.size() - 1);
        assertThat(testImei.getImei1()).isEqualTo(UPDATED_IMEI_1);
        assertThat(testImei.getImei2()).isEqualTo(UPDATED_IMEI_2);
    }

    @Test
    @Transactional
    public void deleteImei() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);
        int databaseSizeBeforeDelete = imeiRepository.findAll().size();

        // Get the imei
        restImeiMockMvc.perform(delete("/api/imeis/{id}", imei.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Imei> imeis = imeiRepository.findAll();
        assertThat(imeis).hasSize(databaseSizeBeforeDelete - 1);
    }
}
