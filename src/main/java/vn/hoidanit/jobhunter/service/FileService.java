package vn.hoidanit.jobhunter.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vn.hoidanit.jobhunter.util.error.StorageException;

@Service
public class FileService {

    @Value("${hoidanit.upload-file.base-path}")
    private String basePath;

    public String saveFile(MultipartFile file, String folder) throws URISyntaxException, IOException, StorageException {
        this.createUploadFolder(basePath + folder);
        return this.store(file, folder);
    }

    private void createUploadFolder(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (!tmpDir.isDirectory()) {
            try {
                Files.createDirectory(tmpDir.toPath());
                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    public String store(MultipartFile file, String folder) throws URISyntaxException,
            IOException, StorageException {

        if (file == null || file.isEmpty()) {
            throw new StorageException("File upload is not exists !!!");
        }

        List<String> allowedMimeTypes = Arrays.asList(
                "application/pdf",
                "image/jpeg",
                "image/png",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        if (!allowedMimeTypes.contains(file.getContentType())) {
            throw new StorageException("File upload is not match Content-Type !!!");
        }
        // create unique filename
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        URI uri = new URI(basePath + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }
        return finalName;
    }

    public long getLengthFile(String fileName, String folder) throws URISyntaxException {
        URI uri = new URI(basePath + folder + "/" + fileName);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (tmpDir.isDirectory() || !tmpDir.exists()) {
            return 0;
        }
        return tmpDir.length();
    }

    public InputStreamResource getFile(String fileName, String folder)
            throws URISyntaxException, StorageException, FileNotFoundException {
        URI uri = new URI(basePath + folder + "/" + fileName);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if (tmpDir.isDirectory() || !tmpDir.exists()) {
            throw new StorageException(" file is not exists !!!");
        }
        return new InputStreamResource(new FileInputStream(tmpDir));
    }

}
