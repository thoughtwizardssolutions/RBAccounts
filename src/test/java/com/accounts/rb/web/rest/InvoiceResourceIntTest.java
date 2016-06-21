package com.accounts.rb.web.rest;

import com.accounts.rb.RbaccountsApp;
import com.accounts.rb.domain.Invoice;
import com.accounts.rb.repository.InvoiceRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the InvoiceResource REST controller.
 *
 * @see InvoiceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RbaccountsApp.class)
@WebAppConfiguration
@IntegrationTest
public class InvoiceResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_CREATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATION_TIME);

    private static final ZonedDateTime DEFAULT_MODIFICATION_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_MODIFICATION_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_MODIFICATION_TIME_STR = dateTimeFormatter.format(DEFAULT_MODIFICATION_TIME);
    private static final String DEFAULT_INVOICE_NUMBER = "AAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBB";
    private static final String DEFAULT_ORDER_NUMBER = "AAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBB";
    private static final String DEFAULT_SALES_PERSON_NAME = "AAAAA";
    private static final String UPDATED_SALES_PERSON_NAME = "BBBBB";

    private static final BigDecimal DEFAULT_SHIPPING_CHARGES = new BigDecimal(1);
    private static final BigDecimal UPDATED_SHIPPING_CHARGES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_SUBTOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUBTOTAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAXES = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXES = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ADJUSTMENTS = new BigDecimal(1);
    private static final BigDecimal UPDATED_ADJUSTMENTS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    private static final Long DEFAULT_DEALER_ID = 1L;
    private static final Long UPDATED_DEALER_ID = 2L;

    @Inject
    private InvoiceRepository invoiceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InvoiceResource invoiceResource = new InvoiceResource();
        ReflectionTestUtils.setField(invoiceResource, "invoiceRepository", invoiceRepository);
        this.restInvoiceMockMvc = MockMvcBuilders.standaloneSetup(invoiceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        invoice = new Invoice();
        invoice.setCreationTime(DEFAULT_CREATION_TIME);
        invoice.setModificationTime(DEFAULT_MODIFICATION_TIME);
        invoice.setInvoiceNumber(DEFAULT_INVOICE_NUMBER);
        invoice.setOrderNumber(DEFAULT_ORDER_NUMBER);
        invoice.setSalesPersonName(DEFAULT_SALES_PERSON_NAME);
        invoice.setShippingCharges(DEFAULT_SHIPPING_CHARGES);
        invoice.setSubtotal(DEFAULT_SUBTOTAL);
        invoice.setTaxes(DEFAULT_TAXES);
        invoice.setAdjustments(DEFAULT_ADJUSTMENTS);
        invoice.setTotalAmount(DEFAULT_TOTAL_AMOUNT);
        invoice.setCreatedBy(DEFAULT_CREATED_BY);
        invoice.setDealerId(DEFAULT_DEALER_ID);
    }

    @Test
    @Transactional
    public void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // Create the Invoice

        restInvoiceMockMvc.perform(post("/api/invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invoice)))
                .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoices = invoiceRepository.findAll();
        assertThat(invoices).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoices.get(invoices.size() - 1);
        assertThat(testInvoice.getCreationTime()).isEqualTo(DEFAULT_CREATION_TIME);
        assertThat(testInvoice.getModificationTime()).isEqualTo(DEFAULT_MODIFICATION_TIME);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
        assertThat(testInvoice.getOrderNumber()).isEqualTo(DEFAULT_ORDER_NUMBER);
        assertThat(testInvoice.getSalesPersonName()).isEqualTo(DEFAULT_SALES_PERSON_NAME);
        assertThat(testInvoice.getShippingCharges()).isEqualTo(DEFAULT_SHIPPING_CHARGES);
        assertThat(testInvoice.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testInvoice.getTaxes()).isEqualTo(DEFAULT_TAXES);
        assertThat(testInvoice.getAdjustments()).isEqualTo(DEFAULT_ADJUSTMENTS);
        assertThat(testInvoice.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testInvoice.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testInvoice.getDealerId()).isEqualTo(DEFAULT_DEALER_ID);
    }

    @Test
    @Transactional
    public void checkInvoiceNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setInvoiceNumber(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invoice)))
                .andExpect(status().isBadRequest());

        List<Invoice> invoices = invoiceRepository.findAll();
        assertThat(invoices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubtotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setSubtotal(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invoice)))
                .andExpect(status().isBadRequest());

        List<Invoice> invoices = invoiceRepository.findAll();
        assertThat(invoices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setTotalAmount(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invoice)))
                .andExpect(status().isBadRequest());

        List<Invoice> invoices = invoiceRepository.findAll();
        assertThat(invoices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDealerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = invoiceRepository.findAll().size();
        // set the field null
        invoice.setDealerId(null);

        // Create the Invoice, which fails.

        restInvoiceMockMvc.perform(post("/api/invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(invoice)))
                .andExpect(status().isBadRequest());

        List<Invoice> invoices = invoiceRepository.findAll();
        assertThat(invoices).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInvoices() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get all the invoices
        restInvoiceMockMvc.perform(get("/api/invoices?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId().intValue())))
                .andExpect(jsonPath("$.[*].creationTime").value(hasItem(DEFAULT_CREATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].modificationTime").value(hasItem(DEFAULT_MODIFICATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].invoiceNumber").value(hasItem(DEFAULT_INVOICE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].salesPersonName").value(hasItem(DEFAULT_SALES_PERSON_NAME.toString())))
                .andExpect(jsonPath("$.[*].shippingCharges").value(hasItem(DEFAULT_SHIPPING_CHARGES.intValue())))
                .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.intValue())))
                .andExpect(jsonPath("$.[*].taxes").value(hasItem(DEFAULT_TAXES.intValue())))
                .andExpect(jsonPath("$.[*].adjustments").value(hasItem(DEFAULT_ADJUSTMENTS.intValue())))
                .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.intValue())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].dealerId").value(hasItem(DEFAULT_DEALER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);

        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
            .andExpect(jsonPath("$.creationTime").value(DEFAULT_CREATION_TIME_STR))
            .andExpect(jsonPath("$.modificationTime").value(DEFAULT_MODIFICATION_TIME_STR))
            .andExpect(jsonPath("$.invoiceNumber").value(DEFAULT_INVOICE_NUMBER.toString()))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER.toString()))
            .andExpect(jsonPath("$.salesPersonName").value(DEFAULT_SALES_PERSON_NAME.toString()))
            .andExpect(jsonPath("$.shippingCharges").value(DEFAULT_SHIPPING_CHARGES.intValue()))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL.intValue()))
            .andExpect(jsonPath("$.taxes").value(DEFAULT_TAXES.intValue()))
            .andExpect(jsonPath("$.adjustments").value(DEFAULT_ADJUSTMENTS.intValue()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.intValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.dealerId").value(DEFAULT_DEALER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get("/api/invoices/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setId(invoice.getId());
        updatedInvoice.setCreationTime(UPDATED_CREATION_TIME);
        updatedInvoice.setModificationTime(UPDATED_MODIFICATION_TIME);
        updatedInvoice.setInvoiceNumber(UPDATED_INVOICE_NUMBER);
        updatedInvoice.setOrderNumber(UPDATED_ORDER_NUMBER);
        updatedInvoice.setSalesPersonName(UPDATED_SALES_PERSON_NAME);
        updatedInvoice.setShippingCharges(UPDATED_SHIPPING_CHARGES);
        updatedInvoice.setSubtotal(UPDATED_SUBTOTAL);
        updatedInvoice.setTaxes(UPDATED_TAXES);
        updatedInvoice.setAdjustments(UPDATED_ADJUSTMENTS);
        updatedInvoice.setTotalAmount(UPDATED_TOTAL_AMOUNT);
        updatedInvoice.setCreatedBy(UPDATED_CREATED_BY);
        updatedInvoice.setDealerId(UPDATED_DEALER_ID);

        restInvoiceMockMvc.perform(put("/api/invoices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInvoice)))
                .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoices = invoiceRepository.findAll();
        assertThat(invoices).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoices.get(invoices.size() - 1);
        assertThat(testInvoice.getCreationTime()).isEqualTo(UPDATED_CREATION_TIME);
        assertThat(testInvoice.getModificationTime()).isEqualTo(UPDATED_MODIFICATION_TIME);
        assertThat(testInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
        assertThat(testInvoice.getOrderNumber()).isEqualTo(UPDATED_ORDER_NUMBER);
        assertThat(testInvoice.getSalesPersonName()).isEqualTo(UPDATED_SALES_PERSON_NAME);
        assertThat(testInvoice.getShippingCharges()).isEqualTo(UPDATED_SHIPPING_CHARGES);
        assertThat(testInvoice.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testInvoice.getTaxes()).isEqualTo(UPDATED_TAXES);
        assertThat(testInvoice.getAdjustments()).isEqualTo(UPDATED_ADJUSTMENTS);
        assertThat(testInvoice.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testInvoice.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testInvoice.getDealerId()).isEqualTo(UPDATED_DEALER_ID);
    }

    @Test
    @Transactional
    public void deleteInvoice() throws Exception {
        // Initialize the database
        invoiceRepository.saveAndFlush(invoice);
        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Get the invoice
        restInvoiceMockMvc.perform(delete("/api/invoices/{id}", invoice.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Invoice> invoices = invoiceRepository.findAll();
        assertThat(invoices).hasSize(databaseSizeBeforeDelete - 1);
    }
}
