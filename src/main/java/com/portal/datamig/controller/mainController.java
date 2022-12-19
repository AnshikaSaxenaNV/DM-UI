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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.datamig.exception.DataNotFoundException;


@Controller
@RequestMapping("/api")
public class mainController {

    @Autowired
    ObjectMapper objectMapper;

    private static String lookup="Field_Name,Field_Value";
    

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
            System.out.println(lookup+" get");
            model.addAttribute("entities", objectMapper.readValue(staticDataString, Object.class));    
            model.addAttribute("data", map);

            return "index";

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
        System.out.println(lookup);
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

    
}
