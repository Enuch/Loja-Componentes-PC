package com.tads.hardware.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


@Service
public class FileStorageService {
    
    private final Path root = Paths.get("src/main/webapp/WEB-INF/images");

    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar a pasta para fazer o upload");
        }
    }

    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar a pasta para fazer o upload" + e.getMessage());

        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
             Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Não consegui ler o arquivo");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro: " + e.getMessage());
        }
    }

    public void deledeAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possivel carregar a imagem");
        }
    }
}


