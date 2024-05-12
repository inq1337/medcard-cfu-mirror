package org.cfuv.medcard.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.cfuv.medcard.api.service.ImageService;
import org.cfuv.medcard.config.MinioProperties;
import org.cfuv.medcard.dto.ImageFileDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    // TODO: create exception
    @Override
    public String upload(ImageFileDTO imageFileDTO) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
        if (!"jpeg".equalsIgnoreCase(getExtension(imageFileDTO.image())) && !"jpg".equalsIgnoreCase(getExtension(imageFileDTO.image()))) {
            throw new RuntimeException("Image upload failed: only jpeg is supported");
        }
        MultipartFile file = imageFileDTO.image();
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new RuntimeException("Image must have name.");
        }
        String fileName = generateFileName(file);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
        saveImage(inputStream, fileName);
        return fileName;
    }

    @SneakyThrows
    @Override
    public byte[] get(String fileName) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(fileName)
                        .build())) {
            return stream.readAllBytes();
        }
    }

    @SneakyThrows
    @Override
    public void delete(String fileName) {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build()
        );
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFileName(MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(MultipartFile file) {
        return file.getOriginalFilename()
                .substring(file.getOriginalFilename()
                        .lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(InputStream inputStream, String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}
