package com.accounts.rb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.accounts.rb.domain.UserSequence;
import com.accounts.rb.repository.UserSequenceRepository;
import com.accounts.rb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing UserSequence.
 */
@RestController
@RequestMapping("/api")
public class UserSequenceResource {

    private final Logger log = LoggerFactory.getLogger(UserSequenceResource.class);
        
    @Inject
    private UserSequenceRepository userSequenceRepository;
    
    /**
     * POST  /user-sequences : Create a new userSequence.
     *
     * @param userSequence the userSequence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new userSequence, or with status 400 (Bad Request) if the userSequence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-sequences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserSequence> createUserSequence(@Valid @RequestBody UserSequence userSequence) throws URISyntaxException {
        log.debug("REST request to save UserSequence : {}", userSequence);
        if (userSequence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userSequence", "idexists", "A new userSequence cannot already have an ID")).body(null);
        }
        UserSequence result = userSequenceRepository.save(userSequence);
        return ResponseEntity.created(new URI("/api/user-sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userSequence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /user-sequences : Updates an existing userSequence.
     *
     * @param userSequence the userSequence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated userSequence,
     * or with status 400 (Bad Request) if the userSequence is not valid,
     * or with status 500 (Internal Server Error) if the userSequence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/user-sequences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserSequence> updateUserSequence(@Valid @RequestBody UserSequence userSequence) throws URISyntaxException {
        log.debug("REST request to update UserSequence : {}", userSequence);
        if (userSequence.getId() == null) {
            return createUserSequence(userSequence);
        }
        UserSequence result = userSequenceRepository.save(userSequence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userSequence", userSequence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /user-sequences : get all the userSequences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userSequences in body
     */
    @RequestMapping(value = "/user-sequences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserSequence> getAllUserSequences() {
        log.debug("REST request to get all UserSequences");
        List<UserSequence> userSequences = userSequenceRepository.findAll();
        return userSequences;
    }

    /**
     * GET  /user-sequences/:id : get the "id" userSequence.
     *
     * @param id the id of the userSequence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the userSequence, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/user-sequences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserSequence> getUserSequence(@PathVariable Long id) {
        log.debug("REST request to get UserSequence : {}", id);
        UserSequence userSequence = userSequenceRepository.findOne(id);
        return Optional.ofNullable(userSequence)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @RequestMapping(value = "/{user}/user-sequences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserSequence> getMyUserSequence(@PathVariable String user) {
        log.debug("REST request to get UserSequence : {}", user);
        List<UserSequence> userSequence = userSequenceRepository.findByCreatedBy(user);
        if(userSequence.isEmpty()) {
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<UserSequence>(userSequence.get(0), HttpStatus.OK);
    }

    /**
     * DELETE  /user-sequences/:id : delete the "id" userSequence.
     *
     * @param id the id of the userSequence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/user-sequences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserSequence(@PathVariable Long id) {
        log.debug("REST request to delete UserSequence : {}", id);
        userSequenceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userSequence", id.toString())).build();
    }

}
