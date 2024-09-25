package com.finance.tracker.breakout.service.screener;

import com.finance.tracker.breakout.service.dto.AnalysisResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestListService {

    private static final String STOCKLIST_DIRECTORY = "classpath:stocklist/";

    @Autowired
    private RestService restService;

    // Method to read a list of companies from a file and return pros and cons for each company
    public Map<String, AnalysisResult> getProsAndConsForCompanies(String stockGroup) {
        Map<String, AnalysisResult> companyAnalysisMap = new HashMap<>();

        try {
            // Load the file from resources
            File file = ResourceUtils.getFile("classpath:stocklist/" + stockGroup + ".txt");

            // Read the file content
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            reader.close();

            if (line != null) {
                // Split the line by commas to get individual company names
                String[] companyNames = line.split(",");

                // Loop through each company and fetch its pros and cons
                for (String companyName : companyNames) {
                    companyName = companyName.trim(); // Ensure no extra spaces
                    if (!companyName.isEmpty()) {
                        try {
                            AnalysisResult result = restService.getProsAndCons(companyName);
                            companyAnalysisMap.put(companyName, result);
                        } catch (HttpClientErrorException.NotFound e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return companyAnalysisMap;
    }

    // Method to list all files in the stocklist directory
    public List<String> getStockListFiles() {
        List<String> fileNames = new ArrayList<>();

        try {
            // Get the stocklist directory from the resources folder
            File stocklistDir = ResourceUtils.getFile(STOCKLIST_DIRECTORY);

            // Check if it's a valid directory
            if (stocklistDir.isDirectory()) {
                // List all files in the directory
                File[] files = stocklistDir.listFiles();

                if (files != null) {
                    // Loop through the files and add their names to the list
                    for (File file : files) {
                        if (file.isFile()) {
                            fileNames.add(file.getName());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileNames;
    }
}
