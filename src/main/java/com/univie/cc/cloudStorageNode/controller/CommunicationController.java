package com.univie.cc.cloudStorageNode.controller;

import com.univie.cc.cloudStorageNode.exception.StorageException;
import com.univie.cc.cloudStorageNode.model.KeyValueDTO;
import com.univie.cc.cloudStorageNode.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommunicationController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody KeyValueDTO keyValueDTO) {
        try {
            storageService.save(keyValueDTO);
            return new ResponseEntity(HttpStatus.OK);
        } catch (StorageException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{key}")
    public ResponseEntity get(@PathVariable String key) {
        try {
            KeyValueDTO keyValueDTO = new KeyValueDTO(key, storageService.get(key));
            return new ResponseEntity(keyValueDTO, HttpStatus.OK);
        } catch (StorageException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
