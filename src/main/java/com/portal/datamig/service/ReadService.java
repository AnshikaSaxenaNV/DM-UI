package com.portal.datamig.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ReadService {
    public List<Map<String, String>> readCSVFiles(String fname) {
        List<Map<String, String>> listMap = new ArrayList<>();
        String dirName = fname.toLowerCase();
        try {
            Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/");
            File f = resource.getFile();
            System.out.println("file name" + f);
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    int flag = 0;
                    // We want to find only .csv files
                    return name.endsWith(".csv") && (!name.equals(fname + "Lookup" + ".csv"));
                }
            };
            // Note that this time we are using a File class as an array,
            // instead of String
            File[] files = f.listFiles(filter);
            // Get the names of the files by using the .getName() method
            for (int i = 0; i < files.length; i++) {
                System.out.println("file name" + files[i].getName());
                listMap.add(printCSVFile(files[i].getName(), dirName));
            }
            for (Map<String, String> map : listMap) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    System.out.println(key + " " + value);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listMap;
    }

   
    public Map<String, String> readCSVFile(String name) throws IOException, Exception {
        String dirName = name.toLowerCase();
        Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/" + name + "Lookup" + ".csv");
        File file = resource.getFile();
        FileReader filereader = new FileReader(file);
        BufferedReader br = new BufferedReader(filereader);
        Map<String, String> map = new LinkedHashMap<>();
        String[] groupArray = new String[1000];
        String line;
        int i = 0, k = 0;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",");
            if (k == 0) {
                k++;
                continue;
            }
            groupArray[i] = split[0].trim();
            groupArray[i + 1] = (split.length > 1) ? split[1].trim() : "";
            i += 2;
        }
        String keyname, keyvalue;
        for (int j = 0; j < i; j += 2) {
            keyname = groupArray[j];
            keyvalue = groupArray[j + 1];
            map.put(keyname, keyvalue);
        }
        System.out.println(map.entrySet());
        // for (String key : map.keySet()) {
        // System.out.println(key + " " + map.get(key));
        // }
        // model.addAttribute("csvdata",map);
        
        return map;
    }
    public Map<String, String> printCSVFile(String name, String dirName) throws IOException {
        Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/" + name);
        File file = resource.getFile();
        FileReader filereader = new FileReader(file);
        BufferedReader br = new BufferedReader(filereader);
        Map<String, String> map = new LinkedHashMap<String, String>();
        String[] groupArray = new String[1000];
        String line;
        int i = 0, k = 0;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",");
            if (k == 0) {
                k++;
                continue;
            }
            groupArray[i] = split[0].trim();
            groupArray[i + 1] = (split.length > 1) ? split[1].trim() : "";
            i += 2;
        }
        String keyname, keyvalue;
        for (int j = 0; j < i; j += 2) {
            keyname = groupArray[j];
            keyvalue = groupArray[j + 1];
            map.put(keyname, keyvalue);
        }
        for (String key : map.keySet()) {
            System.out.println(key + " " + map.get(key));
        }
        return map;
    }
    
}
