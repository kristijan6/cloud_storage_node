package com.univie.cc.cloudStorageNode.service;

import com.univie.cc.cloudStorageNode.exception.StorageException;
import com.univie.cc.cloudStorageNode.hash.HashHolder;
import com.univie.cc.cloudStorageNode.model.KeyValueDTO;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

@Service
public class StorageService {

    private final String path = "./storage/";

    public String put(KeyValueDTO keyValueDTO) {
        try {
            String fileName = keyValueDTO.getHash(HashHolder.getHashingAlgorithm());
            Path filePath = Paths.get(path + fileName);

            // If the file with same key appears, old one will be replaced with new one
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);

            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
            writer.write(keyValueDTO.getValue());

            writer.close();

            return filePath.toString();
        } catch (IOException ex) {
            throw new StorageException(ex.getMessage(), ex.getCause());
        }
    }

    public String get(String key) {
        try {
            String fileName = HashHolder.getHashingAlgorithm().hash(key);
            Path filePath = Paths.get(path + fileName);

            return readValue(filePath, "Given key does not exists in storage.");
        } catch (IOException ex) {
            throw new StorageException(ex.getMessage(), ex.getCause());
        }
    }

    public KeyValueDTO getWithPath(String path) {
        try {
            Path filePath = Paths.get(path);
            String value = readValue(filePath, "Given path does not exists in storage.");

            String[] pathSplitted = path.split("[/]");
            String key = pathSplitted[pathSplitted.length-1];

            return new KeyValueDTO(key, value);
        } catch (IOException ex) {
            throw new StorageException(ex.getMessage(), ex.getCause());
        }
    }

    public boolean delete(String key) {
        String fileName = HashHolder.getHashingAlgorithm().hash(key);
        Path filePath = Paths.get(path + fileName);

        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new StorageException(ex.getMessage(), ex.getCause());
        }
    }

    public boolean recreateStorage() {
        try {
            deleteDirRecursively(Paths.get(path).toFile());
            Files.createDirectory(Paths.get(path));

            return true;
        } catch (IOException ex) {
            throw new StorageException(ex.getMessage(), ex.getCause());
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

    private String readValue(Path filePath, String errorMsg) throws IOException {
        if (!Files.exists(filePath)) throw new StorageException("Given key does not exists in storage.");

        StringJoiner result = new StringJoiner(System.lineSeparator());
        for (String s : Files.readAllLines(filePath)) {
            result.add(s);
        }

        return result.toString();
    }

    private void deleteDirRecursively(File dir) throws IOException {
        File[] allContents = dir.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                Files.deleteIfExists(file.toPath());
            }
        }

        Files.deleteIfExists(dir.toPath());
    }
}
