package com.realmbuilder.app.web.rest;

import com.realmbuilder.app.domain.Attributes;
import com.realmbuilder.app.repository.AttributesRepository;
import com.realmbuilder.app.service.AttributesService;
import com.realmbuilder.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.realmbuilder.app.domain.Attributes}.
 */
@RestController
@RequestMapping("/api/character")
@Transactional
public class AttributesResource {

    private final Logger log = LoggerFactory.getLogger(AttributesResource.class);

    private static final String ENTITY_NAME = "attributes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttributesService attributesService;

    private final AttributesRepository attributesRepository;

    public AttributesResource(AttributesService attributesService, AttributesRepository attributesRepository) {
        this.attributesService = attributesService;
        this.attributesRepository = attributesRepository;
    }

    /**
     * {@code POST  /attributes} : Create a new attributes.
     *
     * @param attributes the attributes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attributes, or with status {@code 400 (Bad Request)} if the attributes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attributes")
    public ResponseEntity<Attributes> createAttributes(@Valid @RequestBody Attributes attributes) throws URISyntaxException {
        log.debug("REST request to save Attributes : {}", attributes);
        if (attributes.getId() != null) {
            throw new BadRequestAlertException("A new attributes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Attributes result = attributesService.save(attributes);
        return ResponseEntity
            .created(new URI("/api/attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attributes/:id} : Updates an existing attributes.
     *
     * @param id the id of the attributes to save.
     * @param attributes the attributes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributes,
     * or with status {@code 400 (Bad Request)} if the attributes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attributes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attributes/{id}")
    public ResponseEntity<Attributes> updateAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Attributes attributes
    ) throws URISyntaxException {
        log.debug("REST request to update Attributes : {}, {}", id, attributes);
        if (attributes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Attributes result = attributesService.update(attributes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attributes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /attributes/:id} : Partial updates given fields of an existing attributes, field will ignore if it is null
     *
     * @param id the id of the attributes to save.
     * @param attributes the attributes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attributes,
     * or with status {@code 400 (Bad Request)} if the attributes is not valid,
     * or with status {@code 404 (Not Found)} if the attributes is not found,
     * or with status {@code 500 (Internal Server Error)} if the attributes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attributes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Attributes> partialUpdateAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Attributes attributes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Attributes partially : {}, {}", id, attributes);
        if (attributes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attributes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Attributes> result = attributesService.partialUpdate(attributes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attributes.getId().toString())
        );
    }

    /**
     * {@code GET  /attributes} : get all the attributes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attributes in body.
     */
    @GetMapping("/attributes")
    public List<Attributes> getAllAttributes() {
        log.debug("REST request to get all Attributes");
        return attributesService.findAll();
    }

    /**
     * {@code GET  /attributes/:id} : get the "id" attributes.
     *
     * @param id the id of the attributes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attributes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attributes/{id}")
    public ResponseEntity<Attributes> getAttributes(@PathVariable Long id) {
        log.debug("REST request to get Attributes : {}", id);
        Optional<Attributes> attributes = attributesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attributes);
    }

    /**
     * {@code DELETE  /attributes/:id} : delete the "id" attributes.
     *
     * @param id the id of the attributes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attributes/{id}")
    public ResponseEntity<Void> deleteAttributes(@PathVariable Long id) {
        log.debug("REST request to delete Attributes : {}", id);
        attributesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
