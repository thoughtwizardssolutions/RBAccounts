package com.accounts.rb.web.rest;

import com.accounts.rb.RbaccountsApp;
import com.accounts.rb.domain.ProductTransaction;
import com.accounts.rb.repository.ProductTransactionRepository;
import com.accounts.rb.service.ProductTransactionService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ProductTransactionResource REST controller.
 *
 * @see ProductTransactionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RbaccountsApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProductTransactionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATION_TIME);

    private static final ZonedDateTime DEFAULT_MODIFICATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFICATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFICATION_TIME_STR = dateTimeFormatter.format(DEFAULT_MODIFICATION_TIME);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Boolean DEFAULT_NEW_STOCK = false;
    private static final Boolean UPDATED_NEW_STOCK = true;

    private static final Boolean DEFAULT_RETURN_STOCK = false;
    private static final Boolean UPDATED_RETURN_STOCK = true;

    private static final Boolean DEFAULT_INVOICED_STOCK = false;
    private static final Boolean UPDATED_INVOICED_STOCK = true;

    @Inject
    private ProductTransactionRepository productTransactionRepository;

    @Inject
    private ProductTransactionService productTransactionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductTransactionMockMvc;

    private ProductTransaction productTransaction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductTransactionResource productTransactionResource = new ProductTransactionResource();
        ReflectionTestUtils.setField(productTransactionResource, "productTransactionService", productTransactionService);
        this.restProductTransactionMockMvc = MockMvcBuilders.standaloneSetup(productTransactionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productTransaction = new ProductTransaction();
        productTransaction.setCreationTime(DEFAULT_CREATION_TIME);
        productTransaction.setModificationTime(DEFAULT_MODIFICATION_TIME);
        productTransaction.setQuantity(DEFAULT_QUANTITY);
        productTransaction.setNewStock(DEFAULT_NEW_STOCK);
        productTransaction.setReturnStock(DEFAULT_RETURN_STOCK);
        productTransaction.setInvoicedStock(DEFAULT_INVOICED_STOCK);
    }

    @Test
    @Transactional
    public void createProductTransaction() throws Exception {
        int databaseSizeBeforeCreate = productTransactionRepository.findAll().size();

        // Create the ProductTransaction

        restProductTransactionMockMvc.perform(post("/api/product-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productTransaction)))
                .andExpect(status().isCreated());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactions = productTransactionRepository.findAll();
        assertThat(productTransactions).hasSize(databaseSizeBeforeCreate + 1);
        ProductTransaction testProductTransaction = productTransactions.get(productTransactions.size() - 1);
        assertThat(testProductTransaction.getCreationTime()).isEqualTo(DEFAULT_CREATION_TIME);
        assertThat(testProductTransaction.getModificationTime()).isEqualTo(DEFAULT_MODIFICATION_TIME);
        assertThat(testProductTransaction.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testProductTransaction.isNewStock()).isEqualTo(DEFAULT_NEW_STOCK);
        assertThat(testProductTransaction.isReturnStock()).isEqualTo(DEFAULT_RETURN_STOCK);
        assertThat(testProductTransaction.isInvoicedStock()).isEqualTo(DEFAULT_INVOICED_STOCK);
    }

    @Test
    @Transactional
    public void checkCreationTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTransactionRepository.findAll().size();
        // set the field null
        productTransaction.setCreationTime(null);

        // Create the ProductTransaction, which fails.

        restProductTransactionMockMvc.perform(post("/api/product-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productTransaction)))
                .andExpect(status().isBadRequest());

        List<ProductTransaction> productTransactions = productTransactionRepository.findAll();
        assertThat(productTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = productTransactionRepository.findAll().size();
        // set the field null
        productTransaction.setQuantity(null);

        // Create the ProductTransaction, which fails.

        restProductTransactionMockMvc.perform(post("/api/product-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productTransaction)))
                .andExpect(status().isBadRequest());

        List<ProductTransaction> productTransactions = productTransactionRepository.findAll();
        assertThat(productTransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductTransactions() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get all the productTransactions
        restProductTransactionMockMvc.perform(get("/api/product-transactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productTransaction.getId().intValue())))
                .andExpect(jsonPath("$.[*].creationTime").value(hasItem(DEFAULT_CREATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].modificationTime").value(hasItem(DEFAULT_MODIFICATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].newStock").value(hasItem(DEFAULT_NEW_STOCK.booleanValue())))
                .andExpect(jsonPath("$.[*].returnStock").value(hasItem(DEFAULT_RETURN_STOCK.booleanValue())))
                .andExpect(jsonPath("$.[*].invoicedStock").value(hasItem(DEFAULT_INVOICED_STOCK.booleanValue())));
    }

    @Test
    @Transactional
    public void getProductTransaction() throws Exception {
        // Initialize the database
        productTransactionRepository.saveAndFlush(productTransaction);

        // Get the productTransaction
        restProductTransactionMockMvc.perform(get("/api/product-transactions/{id}", productTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productTransaction.getId().intValue()))
            .andExpect(jsonPath("$.creationTime").value(DEFAULT_CREATION_TIME_STR))
            .andExpect(jsonPath("$.modificationTime").value(DEFAULT_MODIFICATION_TIME_STR))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.newStock").value(DEFAULT_NEW_STOCK.booleanValue()))
            .andExpect(jsonPath("$.returnStock").value(DEFAULT_RETURN_STOCK.booleanValue()))
            .andExpect(jsonPath("$.invoicedStock").value(DEFAULT_INVOICED_STOCK.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProductTransaction() throws Exception {
        // Get the productTransaction
        restProductTransactionMockMvc.perform(get("/api/product-transactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductTransaction() throws Exception {
        // Initialize the database
        productTransactionService.save(productTransaction);

        int databaseSizeBeforeUpdate = productTransactionRepository.findAll().size();

        // Update the productTransaction
        ProductTransaction updatedProductTransaction = new ProductTransaction();
        updatedProductTransaction.setId(productTransaction.getId());
        updatedProductTransaction.setCreationTime(UPDATED_CREATION_TIME);
        updatedProductTransaction.setModificationTime(UPDATED_MODIFICATION_TIME);
        updatedProductTransaction.setQuantity(UPDATED_QUANTITY);
        updatedProductTransaction.setNewStock(UPDATED_NEW_STOCK);
        updatedProductTransaction.setReturnStock(UPDATED_RETURN_STOCK);
        updatedProductTransaction.setInvoicedStock(UPDATED_INVOICED_STOCK);

        restProductTransactionMockMvc.perform(put("/api/product-transactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProductTransaction)))
                .andExpect(status().isOk());

        // Validate the ProductTransaction in the database
        List<ProductTransaction> productTransactions = productTransactionRepository.findAll();
        assertThat(productTransactions).hasSize(databaseSizeBeforeUpdate);
        ProductTransaction testProductTransaction = productTransactions.get(productTransactions.size() - 1);
        assertThat(testProductTransaction.getCreationTime()).isEqualTo(UPDATED_CREATION_TIME);
        assertThat(testProductTransaction.getModificationTime()).isEqualTo(UPDATED_MODIFICATION_TIME);
        assertThat(testProductTransaction.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testProductTransaction.isNewStock()).isEqualTo(UPDATED_NEW_STOCK);
        assertThat(testProductTransaction.isReturnStock()).isEqualTo(UPDATED_RETURN_STOCK);
        assertThat(testProductTransaction.isInvoicedStock()).isEqualTo(UPDATED_INVOICED_STOCK);
    }

    @Test
    @Transactional
    public void deleteProductTransaction() throws Exception {
        // Initialize the database
        productTransactionService.save(productTransaction);

        int databaseSizeBeforeDelete = productTransactionRepository.findAll().size();

        // Get the productTransaction
        restProductTransactionMockMvc.perform(delete("/api/product-transactions/{id}", productTransaction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductTransaction> productTransactions = productTransactionRepository.findAll();
        assertThat(productTransactions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
