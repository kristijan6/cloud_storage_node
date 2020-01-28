package com.univie.cc.cloudStorageNode.service;

import com.univie.cc.cloudStorageNode.exception.StorageException;
import com.univie.cc.cloudStorageNode.hash.HashHolder;
import com.univie.cc.cloudStorageNode.model.KeyValueDTO;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

@Service
public class StorageService {

    private final String path = "./src/main/resources/storage/";

    public void save(KeyValueDTO keyValueDTO) {
        try {
            String fileName = keyValueDTO.getHash(HashHolder.getHashingAlgorithm());
            Path filePath = Paths.get(path + fileName);

            // If the file with same key appears, old one will be replaced with new one
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
            writer.write(keyValueDTO.getValue());

            writer.close();
        } catch (IOException ex) {
            throw new StorageException(ex.getMessage());
        }
    }

    public String get(String key) {
        try {
            String fileName = HashHolder.getHashingAlgorithm().hash(key);
            Path filePath = Paths.get(path + fileName);

            if (!Files.exists(filePath)) throw new StorageException("Given key does not exists in storage.");

            StringJoiner result = new StringJoiner(System.lineSeparator());
            for (String s : Files.readAllLines(filePath)) {
                result.add(s);
            }

            return result.toString();
        } catch (IOException ex) {
            throw new StorageException(ex.getMessage());
        }
    }

    // On startup create storage dir if does not exist
    @EventListener(ApplicationReadyEvent.class)
    public void createStorageDir() {
        if (!Files.exists(Paths.get(path))) {
            try {
                Files.createDirectory(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
