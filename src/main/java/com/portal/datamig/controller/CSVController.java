package com.portal.datamig.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/api")
public class CSVController {
    private static String UPLOADED_FOLDER ="/csvs";
    @GetMapping("/download/{name}")
        public ResponseEntity<Resource> download(@PathVariable("name") String name) throws IOException,FileNotFoundException {
            Resource resource = new ClassPathResource(UPLOADED_FOLDER+"/"+name+"Lookup"+".csv");
            File file = resource.getFile();
            // File file = new File(UPLOADED_FOLDER+"/"+name+".csv");
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource bresource = new ByteArrayResource(Files.readAllBytes(path));
            return ResponseEntity.ok().headers(this.headers(name))
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("aapplication/octed-stream")).body(bresource);
        }
        private HttpHeaders headers(String name) {
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, 
                  "attachment; filename=" + name+".csv");
            header.add("Cache-Control", "no-cache, no-store,"
                  + " must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");
            return header;
         }
    
}
