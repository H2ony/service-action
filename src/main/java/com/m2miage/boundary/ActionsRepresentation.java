package com.m2miage.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.m2miage.entity.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping(value="/actions", produces=MediaType.APPLICATION_JSON_VALUE)
@ExposesResourceFor(Action.class)
public class ActionsRepresentation {
    private final ActionRessource ir;
    
    @Autowired 
    public ActionsRepresentation(ActionRessource ir) {
        this.ir = ir;
    }
    
    // GET all
    @GetMapping
    public ResponseEntity<?> getAllActions() {
        Iterable<Action> allActions = ir.findAll();
        return new ResponseEntity<>(actions2Resource(allActions), HttpStatus.OK);
    }
    
    
    // GET one
    @GetMapping(value="/{actionId}")
    public ResponseEntity<?> getOneAction(@PathVariable("actionId") String id) {
        return Optional.ofNullable(ir.findOne(id))
                .map(i -> new ResponseEntity<>(actionToResource(i,true), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // POST
    @PostMapping
    public ResponseEntity<?> saveAction(@RequestBody Action action) {
        action.setId(UUID.randomUUID().toString());
        Action saved = ir.save(action);
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.setLocation(linkTo(ActionsRepresentation.class).slash(saved.getId()).toUri());
        return new ResponseEntity<>(null, responseHeader, HttpStatus.CREATED);
    }
    
    // DELETE
    @DeleteMapping(value="/{actionId}")
    public ResponseEntity<?> deleteAction(@PathVariable("actionId") String id) {
        ir.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    //PUT
    @PutMapping(value="{actionId}")
    public ResponseEntity<?> updateAction(@PathVariable("actionId") String id,
            @RequestBody Action action) {
        if (!ir.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Action> body = Optional.ofNullable(action);
        if (!body.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        action.setId(id);
        Action resultat = ir.save(action);
        return new ResponseEntity<>(HttpStatus.OK); // NO_CONTENT
    }
    
   // gestion de HATEOAS
    private Resources<Resource<Action>> actions2Resource(Iterable<Action> actions) {
        Link selfLink = linkTo(methodOn(ActionsRepresentation.class).getAllActions())
                .withSelfRel();
        List<Resource<Action>> actionsResources = new ArrayList();
        actions.forEach(action -> 
                actionsResources.add(actionToResource(action, false)
                ));
        return new Resources<>(actionsResources, selfLink);
    }
    
    private Resource<Action> actionToResource(Action action, Boolean collection) {
        Link selfLink = linkTo(ActionsRepresentation.class)
                .slash(action.getId())
                .withSelfRel();
        if (collection) {
            Link collectionLink = linkTo(methodOn(ActionsRepresentation.class).getAllActions())
                    .withRel("collection");
            return new Resource<>(action, selfLink, collectionLink);
        } else {
            return new Resource<>(action, selfLink);
        }
    }

}
