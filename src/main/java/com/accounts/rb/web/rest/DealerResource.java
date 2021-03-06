package com.accounts.rb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.accounts.rb.domain.Address;
import com.accounts.rb.domain.Dealer;
import com.accounts.rb.repository.AddressRepository;
import com.accounts.rb.repository.DealerRepository;
import com.accounts.rb.web.rest.util.HeaderUtil;
import com.accounts.rb.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Dealer.
 */
@RestController
@RequestMapping("/api")
public class DealerResource {

    private final Logger log = LoggerFactory.getLogger(DealerResource.class);
        
    @Inject
    private DealerRepository dealerRepository;
    
    @Inject
    private AddressRepository addressRepository;
    
    /**
     * POST  /dealers : Create a new dealer.
     *
     * @param dealer the dealer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dealer, or with status 400 (Bad Request) if the dealer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dealers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dealer> createDealer(@Valid @RequestBody Dealer dealer) throws URISyntaxException {
        log.debug("REST request to save Contact : {}", dealer);
        if (dealer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("Contact", "idexists", "A new Contact cannot already have an ID")).body(null);
        }
                
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address savedAddress = addressRepository.save(dealer.getAddress());
        dealer.setAddress(savedAddress);
        dealer.setCreatedBy(user.getUsername());
        dealer.setCreationTime(ZonedDateTime.now());
        Dealer result = dealerRepository.save(dealer);
        return ResponseEntity.created(new URI("/api/dealers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("Contact", result.getFirmName()))
            .body(result);
    }

    /**
     * PUT  /dealers : Updates an existing dealer.
     *
     * @param dealer the dealer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dealer,
     * or with status 400 (Bad Request) if the dealer is not valid,
     * or with status 500 (Internal Server Error) if the dealer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dealers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dealer> updateDealer(@Valid @RequestBody Dealer dealer) throws URISyntaxException {
        log.debug("REST request to update Dealer : {}", dealer);
        if (dealer.getId() == null) {
            return createDealer(dealer);
        }
        dealer.setModificationTime(ZonedDateTime.now());
        Address savedAddress = addressRepository.save(dealer.getAddress());
        dealer.setAddress(savedAddress);
        Dealer result = dealerRepository.save(dealer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("Contact", dealer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dealers : get all the dealers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dealers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/dealers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Dealer>> getAllDealers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Dealers");
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Dealer> page = dealerRepository.findByCreatedBy(pageable, user.getUsername());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dealers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dealers/:id : get the "id" dealer.
     *
     * @param id the id of the dealer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dealer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dealers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dealer> getDealer(@PathVariable Long id) {
        log.debug("REST request to get Contact : {}", id);
        Dealer dealer = dealerRepository.findOne(id);
        return Optional.ofNullable(dealer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dealers/:id : delete the "id" dealer.
     *
     * @param id the id of the dealer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dealers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDealer(@PathVariable Long id) {
        log.debug("REST request to delete Contact : {}", id);
        // TODO check if any invoices exist for this dealer. If yes, don't allow delete
        dealerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("Contact", id.toString())).build();
    }

}
