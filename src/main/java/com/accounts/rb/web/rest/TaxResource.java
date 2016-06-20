package com.accounts.rb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.accounts.rb.domain.Tax;
import com.accounts.rb.repository.TaxRepository;
import com.accounts.rb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tax.
 */
@RestController
@RequestMapping("/api")
public class TaxResource {

    private final Logger log = LoggerFactory.getLogger(TaxResource.class);
        
    @Inject
    private TaxRepository taxRepository;
    
    /**
     * POST  /taxes : Create a new tax.
     *
     * @param tax the tax to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tax, or with status 400 (Bad Request) if the tax has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/taxes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tax> createTax(@Valid @RequestBody Tax tax) throws URISyntaxException {
        log.debug("REST request to save Tax : {}", tax);
        if (tax.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tax", "idexists", "A new tax cannot already have an ID")).body(null);
        }
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tax.setCreatedBy(user.getUsername());
        Tax result = taxRepository.save(tax);
        return ResponseEntity.created(new URI("/api/taxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tax", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /taxes : Updates an existing tax.
     *
     * @param tax the tax to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tax,
     * or with status 400 (Bad Request) if the tax is not valid,
     * or with status 500 (Internal Server Error) if the tax couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/taxes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tax> updateTax(@Valid @RequestBody Tax tax) throws URISyntaxException {
        log.debug("REST request to update Tax : {}", tax);
        if (tax.getId() == null) {
            return createTax(tax);
        }
        Tax result = taxRepository.save(tax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tax", tax.getId().toString()))
            .body(result);
    }

    /**
     * GET  /taxes : get all the taxes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of taxes in body
     */
    @RequestMapping(value = "/taxes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tax> getAllTaxes() {
        log.debug("REST request to get all Taxes");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Tax> taxes = taxRepository.findByCreatedBy(user.getUsername());
        return taxes;
    }

    /**
     * GET  /taxes/:id : get the "id" tax.
     *
     * @param id the id of the tax to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tax, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/taxes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tax> getTax(@PathVariable Long id) {
        log.debug("REST request to get Tax : {}", id);
        Tax tax = taxRepository.findOne(id);
        return Optional.ofNullable(tax)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /taxes/:id : delete the "id" tax.
     *
     * @param id the id of the tax to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/taxes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTax(@PathVariable Long id) {
        log.debug("REST request to delete Tax : {}", id);
        taxRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tax", id.toString())).build();
    }

}
