package com.accounts.rb.web.rest;

import com.accounts.rb.RbaccountsApp;
import com.accounts.rb.domain.Profile;
import com.accounts.rb.repository.ProfileRepository;

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
 * Test class for the ProfileResource REST controller.
 *
 * @see ProfileResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RbaccountsApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProfileResourceIntTest {

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
    private static final String DEFAULT_USER = "AAAAA";
    private static final String UPDATED_USER = "BBBBB";
    private static final String DEFAULT_TERMS_AND_CONDITIONS = "AAAAA";
    private static final String UPDATED_TERMS_AND_CONDITIONS = "BBBBB";

    @Inject
    private ProfileRepository profileRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProfileMockMvc;

    private Profile profile;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProfileResource profileResource = new ProfileResource();
        ReflectionTestUtils.setField(profileResource, "profileRepository", profileRepository);
        this.restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        profile = new Profile();
        profile.setCreationTime(DEFAULT_CREATION_TIME);
        profile.setModificationTime(DEFAULT_MODIFICATION_TIME);
        profile.setFirmName(DEFAULT_FIRM_NAME);
        profile.setOwnerName(DEFAULT_OWNER_NAME);
        profile.setTin(DEFAULT_TIN);
        profile.setUser(DEFAULT_USER);
        profile.setTermsAndConditions(DEFAULT_TERMS_AND_CONDITIONS);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile

        restProfileMockMvc.perform(post("/api/profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profile)))
                .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profiles.get(profiles.size() - 1);
        assertThat(testProfile.getCreationTime()).isEqualTo(DEFAULT_CREATION_TIME);
        assertThat(testProfile.getModificationTime()).isEqualTo(DEFAULT_MODIFICATION_TIME);
        assertThat(testProfile.getFirmName()).isEqualTo(DEFAULT_FIRM_NAME);
        assertThat(testProfile.getOwnerName()).isEqualTo(DEFAULT_OWNER_NAME);
        assertThat(testProfile.getTin()).isEqualTo(DEFAULT_TIN);
        assertThat(testProfile.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testProfile.getTermsAndConditions()).isEqualTo(DEFAULT_TERMS_AND_CONDITIONS);
    }

    @Test
    @Transactional
    public void checkCreationTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setCreationTime(null);

        // Create the Profile, which fails.

        restProfileMockMvc.perform(post("/api/profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profile)))
                .andExpect(status().isBadRequest());

        List<Profile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirmNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setFirmName(null);

        // Create the Profile, which fails.

        restProfileMockMvc.perform(post("/api/profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profile)))
                .andExpect(status().isBadRequest());

        List<Profile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setUser(null);

        // Create the Profile, which fails.

        restProfileMockMvc.perform(post("/api/profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(profile)))
                .andExpect(status().isBadRequest());

        List<Profile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profiles
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
                .andExpect(jsonPath("$.[*].creationTime").value(hasItem(DEFAULT_CREATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].modificationTime").value(hasItem(DEFAULT_MODIFICATION_TIME_STR)))
                .andExpect(jsonPath("$.[*].firmName").value(hasItem(DEFAULT_FIRM_NAME.toString())))
                .andExpect(jsonPath("$.[*].ownerName").value(hasItem(DEFAULT_OWNER_NAME.toString())))
                .andExpect(jsonPath("$.[*].tin").value(hasItem(DEFAULT_TIN.toString())))
                .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER.toString())))
                .andExpect(jsonPath("$.[*].termsAndConditions").value(hasItem(DEFAULT_TERMS_AND_CONDITIONS.toString())));
    }

    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.creationTime").value(DEFAULT_CREATION_TIME_STR))
            .andExpect(jsonPath("$.modificationTime").value(DEFAULT_MODIFICATION_TIME_STR))
            .andExpect(jsonPath("$.firmName").value(DEFAULT_FIRM_NAME.toString()))
            .andExpect(jsonPath("$.ownerName").value(DEFAULT_OWNER_NAME.toString()))
            .andExpect(jsonPath("$.tin").value(DEFAULT_TIN.toString()))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER.toString()))
            .andExpect(jsonPath("$.termsAndConditions").value(DEFAULT_TERMS_AND_CONDITIONS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = new Profile();
        updatedProfile.setId(profile.getId());
        updatedProfile.setCreationTime(UPDATED_CREATION_TIME);
        updatedProfile.setModificationTime(UPDATED_MODIFICATION_TIME);
        updatedProfile.setFirmName(UPDATED_FIRM_NAME);
        updatedProfile.setOwnerName(UPDATED_OWNER_NAME);
        updatedProfile.setTin(UPDATED_TIN);
        updatedProfile.setUser(UPDATED_USER);
        updatedProfile.setTermsAndConditions(UPDATED_TERMS_AND_CONDITIONS);

        restProfileMockMvc.perform(put("/api/profiles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProfile)))
                .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profiles.get(profiles.size() - 1);
        assertThat(testProfile.getCreationTime()).isEqualTo(UPDATED_CREATION_TIME);
        assertThat(testProfile.getModificationTime()).isEqualTo(UPDATED_MODIFICATION_TIME);
        assertThat(testProfile.getFirmName()).isEqualTo(UPDATED_FIRM_NAME);
        assertThat(testProfile.getOwnerName()).isEqualTo(UPDATED_OWNER_NAME);
        assertThat(testProfile.getTin()).isEqualTo(UPDATED_TIN);
        assertThat(testProfile.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testProfile.getTermsAndConditions()).isEqualTo(UPDATED_TERMS_AND_CONDITIONS);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Get the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Profile> profiles = profileRepository.findAll();
        assertThat(profiles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
