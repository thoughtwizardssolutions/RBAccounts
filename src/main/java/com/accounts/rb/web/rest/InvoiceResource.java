package com.accounts.rb.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accounts.rb.domain.Invoice;
import com.accounts.rb.domain.InvoiceReport;
import com.accounts.rb.domain.InvoiceSearchCommand;
import com.accounts.rb.domain.Profile;
import com.accounts.rb.repository.DealerRepository;
import com.accounts.rb.repository.ProfileRepository;
import com.accounts.rb.security.AuthoritiesConstants;
import com.accounts.rb.service.InvoiceService;
import com.accounts.rb.service.MailService;
import com.accounts.rb.service.pdf.PdfService;
import com.accounts.rb.web.rest.util.HeaderUtil;
import com.accounts.rb.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Invoice.
 */
@RestController
@RequestMapping("/api")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);
        
    @Inject
    private InvoiceService invoiceService;
    
    @Inject
    PdfService pdfService;
    
    @Inject
    private ProfileRepository profileRepository;
    
    @Inject
    MailService mailService;
    
    /**
     * POST  /invoices : Create a new invoice.
     *
     * @param invoice the invoice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new invoice, or with status 400 (Bad Request) if the invoice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invoices",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to save Invoice : {}", invoice);
        if (invoice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invoice", "idexists", "A new invoice cannot already have an ID")).body(null);
        }
        User user = getCurrentUser();
        invoice.setCreationTime(ZonedDateTime.now());
        invoice.setCreatedBy(user.getUsername());
        if(profileRepository.findByUser(user.getUsername()).isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invoice", "profilenotexists", "Please set up your profile before creating an Invoice")).body(null);
        }
        Invoice result = invoiceService.save(invoice, user);
        
        return ResponseEntity.created(new URI("/api/invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("invoice", result.getInvoiceNumber()))
            .body(result);
    }


    /**
     * POST  /invoices/pdf : Create a new invoice PDF.
     *
     * @param invoice the invoice to create PDF for
     * @return 
     * @return attachment
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/pdf",
        method = RequestMethod.POST,
        produces = "application/pdf")
    @Timed
	public ResponseEntity<InputStreamResource> createInvoicePdf(@Valid @RequestBody Invoice invoice,
			HttpServletRequest request, HttpServletResponse response)
			throws URISyntaxException, FileNotFoundException, IOException {
		log.debug("REST request to create pdf Invoice : {}", invoice);
		User user = getCurrentUser();
		List<Profile> profiles = profileRepository.findByUser(user.getUsername());
		if(profiles.isEmpty()) {
		  return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("invoice", "profilenotexists", "Please set up your profile before creating an Invoice.")).body(null);
		}
		// generate pdf
		File pdfFile = pdfService.createPdfInvoice(invoice, profiles.get(0));
		// set content type
		response.setContentType("application/pdf");
		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		return ResponseEntity.ok().headers(headers).contentLength(pdfFile.length())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(new InputStreamResource(new FileInputStream(pdfFile)));
	}

    
    /**
     * PUT  /invoices : Updates an existing invoice.
     *
     * @param invoice the invoice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated invoice,
     * or with status 400 (Bad Request) if the invoice is not valid,
     * or with status 500 (Internal Server Error) if the invoice couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/invoices",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invoice> updateInvoice(@Valid @RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to update Invoice : {}", invoice);
        if (invoice.getId() == null) {
            return createInvoice(invoice);
        }
        Invoice result = invoiceService.update(invoice, getCurrentUser());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("invoice", invoice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /invoices : get all the invoices.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of invoices in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/invoices",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Invoice>> getAllInvoices(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Invoices");
        User user = getCurrentUser();
        Page<Invoice> page = invoiceService.findByCreatedBy(pageable, user.getUsername());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invoices");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

  /**
   * GET /reports : get all the invoices.
   *
   * @param 
   * @return the ResponseEntity with status 200 (OK) and the list of invoices in body
   * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
   */
  @RequestMapping(value = "/reports", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public List<InvoiceReport> getReport(InvoiceSearchCommand criteria) {
    
    log.debug("REST request to create report for criteria : " + criteria);
    User user = getCurrentUser();
    Iterator<GrantedAuthority> iter = user.getAuthorities().iterator();
    while(iter.hasNext()) {
      GrantedAuthority auth = iter.next();
      if(auth.getAuthority().equals(AuthoritiesConstants.ORG_ADMIN) || auth.getAuthority().equals(AuthoritiesConstants.ADMIN)) { 
        return invoiceService.findAllByCriteria(criteria);
      }
      if(auth.getAuthority().equals(AuthoritiesConstants.USER)) {
        criteria.setCreatedBy(user.getUsername());
        return invoiceService.findAllByCriteria(criteria);
      }
    }
    return new ArrayList<InvoiceReport>();
  }
    
    /**
     * GET  /invoices/:id : get the "id" invoice.
     *
     * @param id the id of the invoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoice, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/invoices/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Invoice> getInvoice(@PathVariable Long id) {
        log.debug("REST request to get Invoice : {}", id);
        Invoice invoice = invoiceService.findOne(id);
        return Optional.ofNullable(invoice)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /invoices/:id : delete the "id" invoice.
     *
     * @param id the id of the invoice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/invoices/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        log.debug("REST request to delete Invoice : {}", id);
        invoiceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("invoice", id.toString())).build();
    }
    
    /**
     * GET  /invoices/send/:id : get the "id" invoice.
     *
     * @param id the id of the invoice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the invoice, or with status 404 (Not Found)
     * @throws IOException 
     */
    @RequestMapping(value = "/invoices/send",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void sendInvoiceViaMail(Long id, String email) throws IOException {
        log.debug("REST request to send Invoice : {}", id);
        Invoice invoice = invoiceService.findOne(id);
        List<Profile> profiles = profileRepository.findByUser(getCurrentUser().getUsername());
        mailService.sendInvoiceViaMail(invoice, profiles.get(0), email);
    }
    
    /**
     * 
     * @return current logged in User
     */
    private User getCurrentUser() {
      User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      return user;
    }
}
