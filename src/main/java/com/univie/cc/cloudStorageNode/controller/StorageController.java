package com.univie.cc.cloudStorageNode.controller;

import com.univie.cc.cloudStorageNode.exception.StorageException;
import com.univie.cc.cloudStorageNode.model.KeyValueDTO;
import com.univie.cc.cloudStorageNode.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @PostMapping("/put")
    public ResponseEntity put(@RequestBody KeyValueDTO keyValueDTO) {
        try {
            String filePath = storageService.put(keyValueDTO);
            return new ResponseEntity(filePath, HttpStatus.OK);
        } catch (StorageException ex) {
            return new ResponseEntity(ex.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get/{key}")
    public ResponseEntity get(@PathVariable String key) {
        try {
            KeyValueDTO keyValueDTO = new KeyValueDTO(key, storageService.get(key));
            return new ResponseEntity(keyValueDTO, HttpStatus.OK);
        } catch (StorageException ex) {
            return new ResponseEntity(ex.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getWithPath")
    public ResponseEntity getWithPath(@RequestParam String path) {
        try {
            KeyValueDTO keyValueDTO = storageService.getWithPath(path);
            return new ResponseEntity(keyValueDTO, HttpStatus.OK);
        } catch (StorageException ex) {
            return new ResponseEntity(ex.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/delete/{key}")
    public ResponseEntity delete(@PathVariable String key) {
        try {
            return new ResponseEntity(storageService.delete(key), HttpStatus.OK);
        } catch (StorageException ex) {
            return new ResponseEntity(ex.getCause(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/recreateStorage")
    public ResponseEntity recreateStorage() {
        try {
            return new ResponseEntity(storageService.recreateStorage(), HttpStatus.OK);
        } catch (StorageException ex) {
            return new ResponseEntity(ex.getCause(), HttpStatus.BAD_REQUEST);
        }
    }
}
