package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.train.router.model.Route;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/train")
public class TrainController {

    /**
     * Adds a new route to the train with the provided trainID
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = "application/json")
    public void addRoute(@PathVariable UUID id, @RequestBody Route route) {



    }
}
