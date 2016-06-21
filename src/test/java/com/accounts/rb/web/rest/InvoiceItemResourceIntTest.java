package com.accounts.rb.web.rest;

import com.accounts.rb.RbaccountsApp;
import com.accounts.rb.domain.InvoiceItem;
import com.accounts.rb.repository.InvoiceItemRepository;
import com.accounts.rb.service.InvoiceItemService;

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
 * Test class for the InvoiceItemResource REST controller.
 *
 * @see InvoiceItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RbaccountsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InvoiceItemResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final BigDecimal DEFAULT_MRP = new BigDecimal(1);
    private static final BigDecimal UPDATED_MRP = new BigDecimal(2);
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final String DEFAULT_TAX_TYPE = "AAAAA";
    private static final String UPDATED_TAX_TYPE = "BBBBB";

    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_RATE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DISCOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final ZonedDateTime DEFAULT_CREATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATION_TIME);

    private static final ZonedDateTime DEFAULT_MODIFICATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFICATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFICATION_TIME_STR = dateTimeFormatter.format(DEFAULT_MODIFICATION_TIME);
    private static final String DEFAULT_COLOR = "AAAAA";
    private static final String UPDATED_COLOR = "BBBBB";
    private static final String DEFAULT_PRODUCT_NAME = "AAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBB";

    @Inject
    private InvoiceItemRepository invoiceItemRepository;

    @Inject
    private InvoiceItemService invoiceItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInvoiceItemMockMvc;

    private InvoiceItem invoiceItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InvoiceItemResource invoiceItemResource = new InvoiceItemResource();
        ReflectionTestUtils.setField(invoiceItemResource, "invoiceItemService", invoiceItemService);
        this.restInvoiceItemMockMvc = MockMvcBuilders.standaloneSetup(invoiceItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        invoiceItem = new InvoiceItem();
        invoiceItem.setMrp(DEFAULT_MRP);
        invoiceItem.setDescription(DEFAULT_DESCRIPTION);
        invoiceItem.setAmount(DEFAULT_AMOUNT);
        invoiceItem.setTaxType(DEFAULT_TAX_TYPE);
        invoiceItem.setTaxRate(DEFAULT_TAX_RATE);
        invoiceItem.setDiscount(DEFAULT_DISCOUNT);
        invoiceItem.setQuantity(DEFAULT_QUANTITY);
        invoiceItem.setCreationTime(DEFAULT_CREATION_TIME);
        invoiceItem.setModificationTime(DEFAULT_MODIFICATION_TIME);
        invoiceItem.setColor(DEFAULT_COLOR);
        invoiceItem.setProductName(DEFAULT_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void createInvoiceItem() throws Exception {
        int databaseSizeBeforeCreate = invoiceItemRepository.findAll().size();

        // Create the InvoiceItem

        restInvoiceItemMockMvc.perform(post("/api/invoice-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invoiceItem)))
                .andExpect(status().isCreated());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findAll();
        assertThat(invoiceItems).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceItem testInvoiceItem = invoiceItems.get(invoiceItems.size() - 1);
        assertThat(testInvoiceItem.getMrp()).isEqualTo(DEFAULT_MRP);
        assertThat(testInvoiceItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInvoiceItem.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testInvoiceItem.getTaxType()).isEqualTo(DEFAULT_TAX_TYPE);
        assertThat(testInvoiceItem.getTaxRate()).isEqualTo(DEFAULT_TAX_RATE);
        assertThat(testInvoiceItem.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
        assertThat(testInvoiceItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testInvoiceItem.getCreationTime()).isEqualTo(DEFAULT_CREATION_TIME);
        assertThat(testInvoiceItem.getModificationTime()).isEqualTo(DEFAULT_MODIFICATION_TIME);
        assertThat(testInvoiceItem.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testInvoiceItem.getProductName()).isEqualTo(DEFAULT_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void getAllInvoiceItems() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get all the invoiceItems
        restInvoiceItemMockMvc.perform(get("/api/invoice-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].mrp").value(hasItem(DEFAULT_MRP.intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].taxType").value(hasItem(DEFAULT_TAX_TYPE.toString())))
                .andExpect(jsonPath("$.[*].taxRate").value(hasItem(DEFAULT_TAX_RATE.intValue())))
                .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].creationTime").value(hasItem(DEFAULT_CREATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].modificationTime").value(hasItem(DEFAULT_MODIFICATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
                .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemRepository.saveAndFlush(invoiceItem);

        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(get("/api/invoice-items/{id}", invoiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(invoiceItem.getId().intValue()))
            .andExpect(jsonPath("$.mrp").value(DEFAULT_MRP.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.taxType").value(DEFAULT_TAX_TYPE.toString()))
            .andExpect(jsonPath("$.taxRate").value(DEFAULT_TAX_RATE.intValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.creationTime").value(DEFAULT_CREATION_TIME_STR))
            .andExpect(jsonPath("$.modificationTime").value(DEFAULT_MODIFICATION_TIME_STR))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoiceItem() throws Exception {
        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(get("/api/invoice-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemService.save(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem
        InvoiceItem updatedInvoiceItem = new InvoiceItem();
        updatedInvoiceItem.setId(invoiceItem.getId());
        updatedInvoiceItem.setMrp(UPDATED_MRP);
        updatedInvoiceItem.setDescription(UPDATED_DESCRIPTION);
        updatedInvoiceItem.setAmount(UPDATED_AMOUNT);
        updatedInvoiceItem.setTaxType(UPDATED_TAX_TYPE);
        updatedInvoiceItem.setTaxRate(UPDATED_TAX_RATE);
        updatedInvoiceItem.setDiscount(UPDATED_DISCOUNT);
        updatedInvoiceItem.setQuantity(UPDATED_QUANTITY);
        updatedInvoiceItem.setCreationTime(UPDATED_CREATION_TIME);
        updatedInvoiceItem.setModificationTime(UPDATED_MODIFICATION_TIME);
        updatedInvoiceItem.setColor(UPDATED_COLOR);
        updatedInvoiceItem.setProductName(UPDATED_PRODUCT_NAME);

        restInvoiceItemMockMvc.perform(put("/api/invoice-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInvoiceItem)))
                .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findAll();
        assertThat(invoiceItems).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItems.get(invoiceItems.size() - 1);
        assertThat(testInvoiceItem.getMrp()).isEqualTo(UPDATED_MRP);
        assertThat(testInvoiceItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInvoiceItem.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testInvoiceItem.getTaxType()).isEqualTo(UPDATED_TAX_TYPE);
        assertThat(testInvoiceItem.getTaxRate()).isEqualTo(UPDATED_TAX_RATE);
        assertThat(testInvoiceItem.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testInvoiceItem.getCreationTime()).isEqualTo(UPDATED_CREATION_TIME);
        assertThat(testInvoiceItem.getModificationTime()).isEqualTo(UPDATED_MODIFICATION_TIME);
        assertThat(testInvoiceItem.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testInvoiceItem.getProductName()).isEqualTo(UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    public void deleteInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItemService.save(invoiceItem);

        int databaseSizeBeforeDelete = invoiceItemRepository.findAll().size();

        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(delete("/api/invoice-items/{id}", invoiceItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InvoiceItem> invoiceItems = invoiceItemRepository.findAll();
        assertThat(invoiceItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
