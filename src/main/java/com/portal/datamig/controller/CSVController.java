package com.portal.datamig.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    public void downloadAllFilesByDropdown(@PathVariable("name") String name,HttpServletResponse response) throws IOException {
//        HttpServletResponse response = null;
        String dirName = name.toLowerCase();
        try {
            Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/");
            File f = resource.getFile();
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    
                    // We want to find only .csv files
                    return name.endsWith(".csv");
                }
            };
            // Note that this time we are using a File class as an array,
            // instead of String
            File[] files = f.listFiles(filter);
            List<String> n = new ArrayList<>();
            // Get the names of the files by using the .getName() method
            for (int i = 0; i < files.length; i++) {
                System.out.println("file name" + files[i].getName());
                n.add(files[i].getName());
            }
                ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
                for (String fileName : n) {
//                FileSystemResource fresource = new FileSystemResource("/csvs/" + dirName + "/" + fileName);
                    Resource fresource = new ClassPathResource("/csvs/csvs/" + dirName + "/" + fileName);
                    ZipEntry zipEntry = new ZipEntry(fresource.getFilename());
                    zipEntry.setSize(fresource.contentLength());
                    zipOut.putNextEntry(zipEntry);
                    StreamUtils.copy(fresource.getInputStream(), zipOut);
                    zipOut.closeEntry();
                }
            zipOut.finish();
            zipOut.close();
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "zipFileName" + "\"");
        } catch (Exception e) {
            System.out.println("Directory is empty");
        }
    }
    
}
