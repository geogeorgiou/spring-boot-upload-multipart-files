package com.bezkoder.spring.files.upload.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.bezkoder.spring.files.upload.entity.FileDB;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  public FileDB store(MultipartFile file) throws IOException;

  public FileDB storeToOS(MultipartFile file) throws IOException;

  public FileDB getFile(String id);

  public byte[] loadFromOS(String id) throws IOException;

  public Stream<FileDB> getAllFiles();
}
