package com.portal.datamig.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.datamig.exception.DataNotFoundException;
import com.portal.datamig.service.ReadService;


@Controller
@RequestMapping("/api")
public class mainController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ReadService read;

    private static String selectedValue;

    private static String lookup="Field_Name,Field_Value";

    private final String UPLOAD_DIR = "src/main/resources/entity/";
    

    @GetMapping("")
    public String main(Model model) throws IOException {
        // Resource resource = new ClassPathResource("/csvs/Global_Lookup.csv");
        File file = new File("src/main/resources/csvs/Global_Lookup.csv");
        // FileReader filereader = new FileReader(file);
            BufferedReader br = new BufferedReader(new FileReader(file));
            lookup =br.readLine();
            String line;
            String staticDataString=null;
           
            List<String[]> allLines = new ArrayList<>();
            
            while ((line = br.readLine()) != null) {
                String[] splited = line.split("\\s*,\\s*");
                allLines.add(splited);                
            }

             Map<String, String> map = new LinkedHashMap<>();
            for(String[] row : allLines){
                String key = row[0];
                String value = row[1];
                map.put(key, value);
                
               
            }
            try {
                ClassPathResource staticDataResource = new ClassPathResource("/json/entities.json");
                staticDataString = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new DataNotFoundException("Json file not found with name "+"entities");
            }
            model.addAttribute("entities", objectMapper.readValue(staticDataString, Object.class));    
            model.addAttribute("data", map);

            try {
                model.addAttribute("csvfile",read.readCSVFile(selectedValue));
                model.addAttribute("csvfiles",read.readCSVFiles(selectedValue));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "admin";

        }
        
        @PostMapping(value = "")
    public String save(@RequestParam Map<String, String> data , Model model)throws IOException {
        System.out.println(data.entrySet());
        String staticDataString=null;
        File file = new File("src/main/resources/csvs/Global_Lookup.csv");
        String eol = System.getProperty("line.separator");
        
        try (Writer writer = new FileWriter(file)) {
            writer.append(lookup)
            .append(eol);
            for (Map.Entry<String, String> entry : data.entrySet()) {
              writer.append(entry.getKey())
                    .append(',')
                    .append(entry.getValue())
                    .append(eol);
            }
        } catch (
        IOException e)
        {
            
            e.printStackTrace();
        }
         try {
                ClassPathResource staticDataResource = new ClassPathResource("/json/entities.json");
                staticDataString = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new DataNotFoundException("Json file not found with name "+"entities");
            }
            model.addAttribute("entities", objectMapper.readValue(staticDataString, Object.class));
        model.addAttribute("data", data);
        return "redirect:/api";
    }
  
    @GetMapping("/load")
    public String load(){
        return "load";
    }
    @GetMapping("/report")
    public String report(){
        return "report";
    }
    @GetMapping("/transform")
    public String transform(){
        return "transform";
    }
    @GetMapping("/validate")
    public String validate(){
        return "validate";
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {

        
        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/";
        }

        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // save the file on the local file system
        try {
            Path path = Paths.get(UPLOAD_DIR+"Account/" + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

        return "redirect:/api/validate";
    }

    @PostMapping("/upload/s")
    public String uploadSFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {

        
        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("messageS", "Please select a file to upload.");
            return "redirect:/";
        }

        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // save the file on the local file system
        try {
            Path path = Paths.get(UPLOAD_DIR+"Account/" + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return success response
        attributes.addFlashAttribute("messageS", "You successfully uploaded " + fileName + '!');

        return "redirect:/api/validate";
    }
    @PostMapping("/add")
    public String ent(@RequestParam Map<String, String> ent,Model model) throws IOException, Exception{
        System.out.println(ent.keySet().toString().replaceAll("\\[", "").replaceAll("\\]","")+"HH");
        selectedValue=ent.keySet().toString().replaceAll("\\[", "").replaceAll("\\]","");
        model.addAttribute("csvfile",read.readCSVFile(selectedValue));
        model.addAttribute("csvfiles",read.readCSVFiles(selectedValue));
        return "redirect:/api";
    }
    
}

