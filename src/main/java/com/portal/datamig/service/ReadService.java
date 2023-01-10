package com.portal.datamig.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

@Service
public class ReadService {

    private static String lookup = "Field_Name,Field_Value";

    // display primary and Secondary files
    public Map<String, Map<String, String>> readCSVFiles(String fname) {
        Map<String, Map<String, String>> listMap = new HashMap();
        String dirName = fname.toLowerCase();
        String filename = fname.replaceAll("_", "");
        try {
            Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/");
            File f = resource.getFile();
            System.out.println("file name" + f);
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    int flag = 0;
                    // We want to find only .csv files
                    return name.endsWith(".csv") && (!name.equals(filename + "Lookup" + ".csv"));
                }
            };
            // Note that this time we are using a File class as an array,
            // instead of String
            File[] files = f.listFiles(filter);
            // Get the names of the files by using the .getName() method
            for (int i = 0; i < files.length; i++) {
                System.out.println("file name" + files[i].getName());
                listMap.put(files[i].getName(), printCSVFile(files[i].getName(), dirName));
            }
            // for (Map<String, String> map : listMap) {
            // for (Map.Entry<String, String> entry : map.entrySet()) {
            // String key = entry.getKey();
            // String value = entry.getValue();
            // System.out.println(key + " " + value);
            // }
            // }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listMap;
    }

    public Map<String, String> readCSVFile(String name) throws IOException, Exception {
        String dirName = name.toLowerCase();
        name = name.replaceAll("_", "");
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



    // For primary lookup update
    public Map<String, String> saveLookup(Map<String, String> data, String fname) throws IOException {
        System.out.println(data.entrySet());
        String dirName = fname.toLowerCase();
        fname = fname.replaceAll("_", "");
        // Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/" +
        // fname + "Lookup" + ".csv");
        File file = new File("src/main/resources/csvs/csvs/" + dirName + "/" + fname + "Lookup" + ".csv");
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
        } catch (IOException e) {

            e.printStackTrace();
        }
        return data;
    }
 
    public Map<String, String> saveLookups(Map<String, String> data, String fname) throws IOException {
        System.out.println(data.entrySet());
        String dirName = fname.toLowerCase();
        fname = fname.replaceAll("_", "");
        String fileName = data.keySet().stream().findFirst().get();
        // Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/" +
        // fname + "Lookup" + ".csv");
        File file = new File("src/main/resources/csvs/csvs/" + dirName + "/" + fileName);
        System.out.println("src/main/resources/csvs/csvs/" + dirName + "/" + fileName);
        String eol = System.getProperty("line.separator");

        int k = 0, i = 1;
        try (Writer writer = new FileWriter(file)) {
            writer.append(lookup)
                    .append(eol);
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (k == 0) {
                    k++;
                    continue;
                }

                if ((i % 2) != 0) {
                    writer.append(entry.getValue())
                            .append(',');
                } else {
                    writer.append(entry.getValue())
                            .append(eol);
                }

                i++;

            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return data;
    }

    // upload/copy for validate page
    public void copyCSVFilesP(String loc) throws IOException {
        String dir = loc.toLowerCase();

        Path src = Paths.get("src/main/resources/client/" + dir + "/");
        Path dest = Paths.get("src/main/resources/dmutil/input/" + dir + "/");
        System.out.println(src.toString());
        File f = src.toFile();
        // int size = src.toFile().list().length;
        // File src = new File("/home/data/src");
        // File dest = new File("/home/data/dest");
        Arrays.stream(f.listFiles()).filter(p -> !p.isDirectory()).forEach(p -> {
            try {
                FileUtils.copyFileToDirectory(p, dest.toFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void copyCSVFilesS(String loc) throws IOException {
        String dir = loc.toLowerCase();

        Path src = Paths.get("src/main/resources/client/" + dir + "/");
        Path dest = Paths.get("src/main/resources/dmutil/input/" + dir + "/");
        System.out.println(src.toString());
        File f = src.toFile();
        // int size = src.toFile().list().length;
        // File src = new File("/home/data/src");
        // File dest = new File("/home/data/dest");
        Arrays.stream(f.listFiles()).filter(p -> p.isDirectory()).forEach(p -> {
            try {
                FileUtils.copyDirectoryToDirectory(p, dest.toFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public Map<String, String> entityList() throws IOException {
        Resource resource = new ClassPathResource("csvs/entities/EntityList.csv");
        File file = resource.getFile();
        FileReader filereader = new FileReader(file);
        BufferedReader br = new BufferedReader(filereader);
        Map<String,String> map = new LinkedHashMap<>();
        String[] groupArray = new String[1000];
        String line;
        int i = 0,k=0;
        while((line = br.readLine()) != null) {
            String[] split = line.split(",");
            if(k==0){
                k++;
                continue;
            }
            groupArray[i] = split[0].trim();
            groupArray[i+1] = (split.length > 1) ? split[1].trim() : "";
            i += 2;
        }
        String keyname, keyvalue;
        for(int j = 0; j < i; j += 2) {
            keyname = groupArray[j];
            keyvalue = groupArray[j+1];
            map.put(keyname, keyvalue);
        }
        return map;
    }
    public Map<String, String> entityListSecondary(String n) throws IOException {
        Path src = Paths.get("/home/anshika/DMUtil/Input/"+n+ "/");
            Map<String,String> secondaryList = new LinkedHashMap<>();
            File f = src.toFile();
            Arrays.stream(f.listFiles()).filter(p -> p.isDirectory()).forEach(p -> {
                System.out.println(p);
                secondaryList.put(p.getName(),"");
            });
            return secondaryList;
        }


}
