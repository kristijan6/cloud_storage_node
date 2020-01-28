package com.univie.cc.cloudStorageNode.controller;

import com.univie.cc.cloudStorageNode.hash.HashHolder;
import com.univie.cc.cloudStorageNode.model.KeyValueDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunicationController {

    @PostMapping("/save")
    public ResponseEntity save(@RequestBody KeyValueDTO keyValueDTO) {
        System.out.println("Hash from " + keyValueDTO.getKey() + " is: " + keyValueDTO.getHash(HashHolder.getHashingAlgorithm()));

        return new ResponseEntity(HttpStatus.OK);
    }
}
