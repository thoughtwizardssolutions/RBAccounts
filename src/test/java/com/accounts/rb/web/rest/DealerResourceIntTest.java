package com.accounts.rb.web.rest;

import com.accounts.rb.RbaccountsApp;
import com.accounts.rb.domain.Dealer;
import com.accounts.rb.repository.DealerRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DealerResource REST controller.
 *
 * @see DealerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RbaccountsApp.class)
@WebAppConfiguration
@IntegrationTest
public class DealerResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATION_TIME);

    private static final ZonedDateTime DEFAULT_MODIFICATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFICATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFICATION_TIME_STR = dateTimeFormatter.format(DEFAULT_MODIFICATION_TIME);
    private static final String DEFAULT_FIRM_NAME = "AAAAA";
    private static final String UPDATED_FIRM_NAME = "BBBBB";
    private static final String DEFAULT_OWNER_NAME = "AAAAA";
    private static final String UPDATED_OWNER_NAME = "BBBBB";
    private static final String DEFAULT_TIN = "AAAAA";
    private static final String UPDATED_TIN = "BBBBB";

    private static final BigDecimal DEFAULT_OPENING_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_OPENING_BALANCE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CURRENT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_BALANCE = new BigDecimal(2);

    @Inject
    private DealerRepository dealerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDealerMockMvc;

    private Dealer dealer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DealerResource dealerResource = new DealerResource();
        ReflectionTestUtils.setField(dealerResource, "dealerRepository", dealerRepository);
        this.restDealerMockMvc = MockMvcBuilders.standaloneSetup(dealerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dealer = new Dealer();
        dealer.setCreationTime(DEFAULT_CREATION_TIME);
        dealer.setModificationTime(DEFAULT_MODIFICATION_TIME);
        dealer.setFirmName(DEFAULT_FIRM_NAME);
        dealer.setOwnerName(DEFAULT_OWNER_NAME);
        dealer.setTin(DEFAULT_TIN);
        dealer.setOpeningBalance(DEFAULT_OPENING_BALANCE);
        dealer.setCurrentBalance(DEFAULT_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void createDealer() throws Exception {
        int databaseSizeBeforeCreate = dealerRepository.findAll().size();

        // Create the Dealer

        restDealerMockMvc.perform(post("/api/dealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dealer)))
                .andExpect(status().isCreated());

        // Validate the Dealer in the database
        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeCreate + 1);
        Dealer testDealer = dealers.get(dealers.size() - 1);
        assertThat(testDealer.getCreationTime()).isEqualTo(DEFAULT_CREATION_TIME);
        assertThat(testDealer.getModificationTime()).isEqualTo(DEFAULT_MODIFICATION_TIME);
        assertThat(testDealer.getFirmName()).isEqualTo(DEFAULT_FIRM_NAME);
        assertThat(testDealer.getOwnerName()).isEqualTo(DEFAULT_OWNER_NAME);
        assertThat(testDealer.getTin()).isEqualTo(DEFAULT_TIN);
        assertThat(testDealer.getOpeningBalance()).isEqualTo(DEFAULT_OPENING_BALANCE);
        assertThat(testDealer.getCurrentBalance()).isEqualTo(DEFAULT_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void checkFirmNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealerRepository.findAll().size();
        // set the field null
        dealer.setFirmName(null);

        // Create the Dealer, which fails.

        restDealerMockMvc.perform(post("/api/dealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dealer)))
                .andExpect(status().isBadRequest());

        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDealers() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealers
        restDealerMockMvc.perform(get("/api/dealers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
                .andExpect(jsonPath("$.[*].creationTime").value(hasItem(DEFAULT_CREATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].modificationTime").value(hasItem(DEFAULT_MODIFICATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].firmName").value(hasItem(DEFAULT_FIRM_NAME.toString())))
                .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME.toString())))
                .andExpect(jsonPath("$.[*].tin").value(hasItem(DEFAULT_TIN.toString())))
                .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(DEFAULT_OPENING_BALANCE.intValue())))
                .andExpect(jsonPath("$.[*].currentBalance").value(hasItem(DEFAULT_CURRENT_BALANCE.intValue())));
    }

    @Test
    @Transactional
    public void getDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get the dealer
        restDealerMockMvc.perform(get("/api/dealers/{id}", dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dealer.getId().intValue()))
            .andExpect(jsonPath("$.creationTime").value(DEFAULT_CREATION_TIME_STR))
            .andExpect(jsonPath("$.modificationTime").value(DEFAULT_MODIFICATION_TIME_STR))
            .andExpect(jsonPath("$.firmName").value(DEFAULT_FIRM_NAME.toString()))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME.toString()))
            .andExpect(jsonPath("$.tin").value(DEFAULT_TIN.toString()))
            .andExpect(jsonPath("$.openingBalance").value(DEFAULT_OPENING_BALANCE.intValue()))
            .andExpect(jsonPath("$.currentBalance").value(DEFAULT_CURRENT_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDealer() throws Exception {
        // Get the dealer
        restDealerMockMvc.perform(get("/api/dealers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();

        // Update the dealer
        Dealer updatedDealer = new Dealer();
        updatedDealer.setId(dealer.getId());
        updatedDealer.setCreationTime(UPDATED_CREATION_TIME);
        updatedDealer.setModificationTime(UPDATED_MODIFICATION_TIME);
        updatedDealer.setFirmName(UPDATED_FIRM_NAME);
        updatedDealer.setOwnerName(UPDATED_OWNER_NAME);
        updatedDealer.setTin(UPDATED_TIN);
        updatedDealer.setOpeningBalance(UPDATED_OPENING_BALANCE);
        updatedDealer.setCurrentBalance(UPDATED_CURRENT_BALANCE);

        restDealerMockMvc.perform(put("/api/dealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDealer)))
                .andExpect(status().isOk());

        // Validate the Dealer in the database
        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeUpdate);
        Dealer testDealer = dealers.get(dealers.size() - 1);
        assertThat(testDealer.getCreationTime()).isEqualTo(UPDATED_CREATION_TIME);
        assertThat(testDealer.getModificationTime()).isEqualTo(UPDATED_MODIFICATION_TIME);
        assertThat(testDealer.getFirmName()).isEqualTo(UPDATED_FIRM_NAME);
        assertThat(testDealer.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testDealer.getTin()).isEqualTo(UPDATED_TIN);
        assertThat(testDealer.getOpeningBalance()).isEqualTo(UPDATED_OPENING_BALANCE);
        assertThat(testDealer.getCurrentBalance()).isEqualTo(UPDATED_CURRENT_BALANCE);
    }

    @Test
    @Transactional
    public void deleteDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        int databaseSizeBeforeDelete = dealerRepository.findAll().size();

        // Get the dealer
        restDealerMockMvc.perform(delete("/api/dealers/{id}", dealer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
