package com.bezkoder.spring.files.upload.controller;

import com.bezkoder.spring.files.upload.entity.FileDB;
import com.bezkoder.spring.files.upload.message.ResponseFile;
import com.bezkoder.spring.files.upload.message.ResponseMessage;
import com.bezkoder.spring.files.upload.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("http://localhost:8081")
public class FileController {

  @Autowired
  private FileStorageService storageService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      storageService.store(file);

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/files")
  public ResponseEntity<List<ResponseFile>> getListFiles() {
    List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
      String fileDownloadUri = ServletUriComponentsBuilder
              .fromCurrentContextPath()
              .path("/files/")
              .path(dbFile.getId())
              .toUriString();

      return new ResponseFile(
              dbFile.getName(),
              fileDownloadUri,
              dbFile.getType(),
              dbFile.getData().length);
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(files);
  }

  @GetMapping("/files/{id}")
  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
    FileDB fileDB = storageService.getFile(id);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
            .body(fileDB.getData());
  }

  @PostMapping("/uploadToOs")
  public ResponseEntity<?> saveFileToOs(@RequestParam("file") MultipartFile multiFile) throws IOException {

    storageService.storeToOS(multiFile);
    return ResponseEntity.ok("Successfully saved");

  }

  @GetMapping("/downloadFromOs")
  public ResponseEntity<byte[]> downloadFile() throws IOException {
    //could use uuid here to relate to file

    //watch out CONTENT DISPOSITION HEADER DEMANDS an extension to work properly

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "whatevername.pdf" + "\"")
            .body(storageService.loadFromOS("1"));
  }

}
