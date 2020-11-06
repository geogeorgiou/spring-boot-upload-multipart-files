package com.bezkoder.spring.files.upload.service;

import com.bezkoder.spring.files.upload.entity.FileDB;
import com.bezkoder.spring.files.upload.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {

  @Autowired
  private FileDBRepository fileDBRepository;

  public FileDB store(MultipartFile file) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    FileDB FileDB = new FileDB(fileName, file.getContentType(), file.getBytes());

    return fileDBRepository.save(FileDB);
  }

  public FileDB getFile(String id) {
    return fileDBRepository.findById(id).get();
  }

  public Stream<FileDB> getAllFiles() {
    return fileDBRepository.findAll().stream();
  }

  @Override
  public FileDB storeToOS(MultipartFile file) {
    return null;
  }

  @Override
  public FileDB loadFromOS(String id) {
    return null;
  }
}
