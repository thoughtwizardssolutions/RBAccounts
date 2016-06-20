package com.accounts.rb.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.accounts.rb.domain.User;
import com.accounts.rb.repository.UserRepository;
import com.accounts.rb.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing User.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);
        
    @Inject
    private UserRepository userRepository;
    
    /**
     * POST  /users : Create a new user.
     *
     * @param user the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> createUser(@RequestBody User user) throws URISyntaxException {
        log.debug("REST request to save User : {}", user);
        if (user.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user", "idexists", "A new user cannot already have an ID")).body(null);
        }
        User result = userRepository.save(user);
        return ResponseEntity.created(new URI("/api/users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("user", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /users : Updates an existing user.
     *
     * @param user the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the user is not valid,
     * or with status 500 (Internal Server Error) if the user couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> updateUser(@RequestBody User user) throws URISyntaxException {
        log.debug("REST request to update User : {}", user);
        if (user.getId() == null) {
            return createUser(user);
        }
        User result = userRepository.save(user);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("user", user.getId().toString()))
            .body(result);
    }

    /**
     * GET  /users : get all the users.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of users in body
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getAllUsers() {
        log.debug("REST request to get all Users");
        List<User> users = userRepository.findAll();
        return users;
    }

    /**
     * GET  /users/:id : get the "id" user.
     *
     * @param id the id of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/users/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        User user = userRepository.findOne(id);
        return Optional.ofNullable(user)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /users/:id : delete the "id" user.
     *
     * @param id the id of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/users/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete User : {}", id);
        userRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("user", id.toString())).build();
    }

}
