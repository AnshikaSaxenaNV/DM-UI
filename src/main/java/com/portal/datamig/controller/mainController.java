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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
    private static String selectedValueValidate;

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
    public String save(@RequestParam Map<String, String> data , Model model,RedirectAttributes attributes)throws IOException {
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
        attributes.addFlashAttribute("globalmessage", "Global Lookup values successfully updated !!");
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
    public String transform(Model model) throws JsonMappingException, JsonProcessingException{
        String staticDataString=null;
        try {
            ClassPathResource staticDataResource = new ClassPathResource("/json/entities.json");
            staticDataString = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DataNotFoundException("Json file not found with name "+"entities");
        }
        model.addAttribute("entities", objectMapper.readValue(staticDataString, Object.class));
       
        return "transform";
    }
    
    @PostMapping("/add")
    public String selectEntity(@RequestParam Map<String, String> ent, Model model)
    throws IOException, Exception {
        System.out.println(
        ent.keySet().toString().replaceAll("\\[", "").replaceAll("\\]", "") + "HH"
        );
        selectedValue =
        ent.keySet().toString().replaceAll("\\[", "").replaceAll("\\]", "");
        return "redirect:/api";
  }
    
    @PostMapping("/write")
    public String writeLookup(@RequestParam(required = false)Map<String, String> csvdata,Model model, RedirectAttributes attributes) throws IOException, Exception{
        System.out.println("nlk/n/n/n/n/n/"+csvdata);
        System.out.println(selectedValue);
        read.saveLookup(csvdata, selectedValue);
        attributes.addFlashAttribute("Updatemessage", "Primary Lookup values successfully updated !!");

        return "redirect:/api";
    }

    @GetMapping("/validate")
    public String validate(Model model) throws JsonMappingException, JsonProcessingException{
        String staticDataString=null;
        try {
            ClassPathResource staticDataResource = new ClassPathResource("/json/entities.json");
            staticDataString = IOUtils.toString(staticDataResource.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DataNotFoundException("Json file not found with name "+"entities");
        }
        model.addAttribute("entities", objectMapper.readValue(staticDataString, Object.class));
        return "validate";
    }
    @PostMapping("/validate")
    public String listEntity(@RequestParam Map<String, String> ent, Model model)
    throws IOException, Exception {
        System.out.println(
        ent.keySet().toString().replaceAll("\\[", "").replaceAll("\\]", "") + "HH"
        );
        selectedValueValidate =
        ent.keySet().toString().replaceAll("\\[", "").replaceAll("\\]", "");
        return "redirect:/api/validate";
  }
  @PostMapping("/validate/upload")
  public String uploadFile(RedirectAttributes attributes) {
    System.out.println("sdfxgcvjhbx" + selectedValueValidate);
    try {
      read.copyCSVFilesP(selectedValueValidate);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // return success response
    attributes.addFlashAttribute(
      "messageP",
      "You successfully uploaded " + selectedValueValidate + '!'
    );
    return "redirect:/api/validate";
  }
  @PostMapping("/validate/uploadSec")
  public String uploadFileS(RedirectAttributes attributes) {
    System.out.println("sdfxgcvjhbx" + selectedValueValidate);
    try {
      read.copyCSVFilesS(selectedValueValidate);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // return success response
    attributes.addFlashAttribute(
      "messageS",
      "You successfully uploaded " + selectedValueValidate + '!'
    );
    return "redirect:/api/validate";
  }

    
}