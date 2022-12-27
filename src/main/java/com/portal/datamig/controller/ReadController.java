// package com.portal.datamig.controller;

// import org.springframework.core.io.ClassPathResource;
// import org.springframework.core.io.Resource;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import java.io.*;
// import java.util.*;

// @RestController
// @RequestMapping("/api")
// public class ReadController {
//     @GetMapping("/readlist/{name}")
//     public List<Map<String, String>> readCSVFiles(@PathVariable("name") String fname) {
//         List<Map<String, String>> listMap = new ArrayList<>();
//         String dirName = fname.toLowerCase();
//         try {
//             Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/");
//             File f = resource.getFile();
//             System.out.println("file name" + f);
//             FilenameFilter filter = new FilenameFilter() {
//                 @Override
//                 public boolean accept(File f, String name) {
//                     int flag = 0;
//                     // We want to find only .csv files
//                     return name.endsWith(".csv") && (!name.equals(fname + "Lookup" + ".csv"));
//                 }
//             };
//             // Note that this time we are using a File class as an array,
//             // instead of String
//             File[] files = f.listFiles(filter);
//             // Get the names of the files by using the .getName() method
//             for (int i = 0; i < files.length; i++) {
//                 System.out.println("file name" + files[i].getName());
//                 listMap.add(printCSVFile(files[i].getName(), dirName));
//             }
//             for (Map<String, String> map : listMap) {
//                 for (Map.Entry<String, String> entry : map.entrySet()) {
//                     String key = entry.getKey();
//                     String value = entry.getValue();
//                     System.out.println(key + " " + value);
//                 }
//             }
//         } catch (Exception e) {
//             System.err.println(e.getMessage());
//         }
//         return listMap;
//     }

//     @GetMapping("/read/{name}")
//     public Map<String, String> readCSVFile(@PathVariable("name") String name) throws IOException, Exception {
//         String dirName = name.toLowerCase();
//         Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/" + name + "Lookup" + ".csv");
//         File file = resource.getFile();
//         FileReader filereader = new FileReader(file);
//         BufferedReader br = new BufferedReader(filereader);
//         Map<String, String> map = new LinkedHashMap<>();
//         String[] groupArray = new String[1000];
//         String line;
//         int i = 0, k = 0;
//         while ((line = br.readLine()) != null) {
//             String[] split = line.split(",");
//             if (k == 0) {
//                 k++;
//                 continue;
//             }
//             groupArray[i] = split[0].trim();
//             groupArray[i + 1] = (split.length > 1) ? split[1].trim() : "";
//             i += 2;
//         }
//         String keyname, keyvalue;
//         for (int j = 0; j < i; j += 2) {
//             keyname = groupArray[j];
//             keyvalue = groupArray[j + 1];
//             map.put(keyname, keyvalue);
//         }
//         // for (String key : map.keySet()) {
//         // System.out.println(key + " " + map.get(key));
//         // }
//         // model.addAttribute("csvdata",map);
//         return map;
//     }

//     public Map<String, String> printCSVFile(String name, String dirName) throws IOException {
//         Resource resource = new ClassPathResource("/csvs/csvs/" + dirName + "/" + name);
//         File file = resource.getFile();
//         FileReader filereader = new FileReader(file);
//         BufferedReader br = new BufferedReader(filereader);
//         Map<String, String> map = new HashMap<String, String>();
//         String[] groupArray = new String[1000];
//         String line;
//         int i = 0, k = 0;
//         while ((line = br.readLine()) != null) {
//             String[] split = line.split(",");
//             if (k == 0) {
//                 k++;
//                 continue;
//             }
//             groupArray[i] = split[0].trim();
//             groupArray[i + 1] = (split.length > 1) ? split[1].trim() : "";
//             i += 2;
//         }
//         String keyname, keyvalue;
//         for (int j = 0; j < i; j += 2) {
//             keyname = groupArray[j];
//             keyvalue = groupArray[j + 1];
//             map.put(keyname, keyvalue);
//         }
//         for (String key : map.keySet()) {
//             System.out.println(key + " " + map.get(key));
//         }
//         return map;
//     }
// }
