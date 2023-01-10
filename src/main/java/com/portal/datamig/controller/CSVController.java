package com.portal.datamig.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api")
public class CSVController {
   
    @GetMapping("/download/{name}")
    public String downloadAllFilesByDropdown(@PathVariable("name") String name,HttpServletResponse response) throws IOException {
//        HttpServletResponse response = null;
        String dirName = name.toLowerCase();
        try {
            File f = new File("src/main/resources/csvs/csvs/" + dirName+"/");
            File fileList[] = f.listFiles();
            Arrays.stream(fileList).iterator().forEachRemaining(System.out::println);
            for (File r:fileList){
                 if(r.isDirectory()){
                     Arrays.stream(r.listFiles()).iterator().forEachRemaining(System.out::println);
                 }
            } 
        }catch (Exception e) {
                System.out.println("Directory is empty");
            }
            //test download
            Path zipFile = Path.of("/home/anshika/DMUtil/Client/"+dirName+".zip");
            File f = new File("src/main/resources/csvs/csvs/" + dirName+"/");
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))) {
                if (Files.isDirectory(f.toPath())) {
                    Files.walk(f.toPath()).filter(path -> !Files.isDirectory(path)).forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(f.toPath().relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            if (Files.isRegularFile(path)) {
                                Files.copy(path, zipOutputStream);
                            }
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });
                }
            }
            return "redirect:/api";
    }
        

}
