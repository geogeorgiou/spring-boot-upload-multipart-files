package com.bezkoder.spring.files.upload.service;

import com.bezkoder.spring.files.upload.entity.FileDB;
import com.bezkoder.spring.files.upload.repository.FileDBRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {


  // both OS and env variables are goint go be used to save to OS methods
  private static String OS = System.getProperty("os.name").toLowerCase();
  @Autowired
  private Environment env;

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
  public FileDB storeToOS(MultipartFile multipartFile) throws IOException {

    //maybe could use some id here to relate to user

    //also to be changed for saving to particular file path
    // (use commented code AS util and env variables)

    String destination = "C:/Users/geogeorgiou/Desktop/"+multipartFile.getOriginalFilename();

    System.out.println(destination);

//  String templateFileBasePath = OS.indexOf("win") >= 0 ?
//            env.getRequiredProperty("windows.signature.document.original") :
//            env.getRequiredProperty("linux.signature.document.original");

    //file needs to have some kind of english name format
    File file = new File(destination);
//    String destination = "/path2folder/" + id + "/"  + multipartFile.getOriginalFilename();
    multipartFile.transferTo(file);

    return null;
  }

  @Override
  public byte[] loadFromOS(String id) throws IOException {

    //somehow can use file file id to dig into the OS structure and get the file we want

    return Files.readAllBytes(Paths.get("C:/Users/geogeorgiou/Desktop/next-gen-js-summary.pdf"));
  }
}
