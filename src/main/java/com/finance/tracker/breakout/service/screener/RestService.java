package com.finance.tracker.breakout.service.screener;

import com.finance.tracker.breakout.domain.Cons;
import com.finance.tracker.breakout.domain.Pros;
import com.finance.tracker.breakout.repository.ConsRepository;
import com.finance.tracker.breakout.repository.ProsRepository;
import com.finance.tracker.breakout.service.dto.AnalysisResult;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    private static final Logger log = LoggerFactory.getLogger(RestService.class);
    private static final String URL_TEMPLATE = "https://www.screener.in/company/{companyName}/";
    private static final String STOCKLIST_DIRECTORY = "classpath:stocklist/";

    @Autowired
    private ProsRepository prosRepository;

    @Autowired
    private ConsRepository consRepository;

    @Cacheable(value = "companyAnalysis", key = "#companyName", unless = "#result == null")
    public AnalysisResult getProsAndCons(String companyName) {
        log.info("Getting pros and cons for {}", companyName);
        return fetchProsAndCons(companyName);
    }

    private AnalysisResult parseHtmlForAnalysis(String html) {
        try {
            // Parse the HTML
            Document doc = Jsoup.parse(html);

            // XPath equivalent in Jsoup for pros: //*[@id="analysis"]/div/div[1]/ul
            Elements prosElements = doc.select("#analysis > div > div:nth-child(1) > ul > li");

            // XPath equivalent in Jsoup for cons: //*[@id="analysis"]/div/div[2]/ul
            Elements consElements = doc.select("#analysis > div > div:nth-child(2) > ul > li");

            // Extract the text for each list item (li)
            List<String> prosList = new ArrayList<>();
            prosElements.forEach(pro -> prosList.add(pro.text()));

            List<String> consList = new ArrayList<>();
            consElements.forEach(con -> consList.add(con.text()));

            // Return the result as an AnalysisResult object
            return new AnalysisResult(prosList, consList);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException();
        } catch (Exception e) {
            e.printStackTrace();
            return new AnalysisResult(new ArrayList<>(), new ArrayList<>());
        }
    }

    private void saveProsAndCons(String companyName, List<String> prosList, List<String> consList) {
        // Save Pros
        for (String pro : prosList) {
            Pros pros = new Pros(companyName, pro);
            prosRepository.save(pros);
        }

        // Save Cons
        for (String con : consList) {
            Cons cons = new Cons(companyName, con);
            consRepository.save(cons);
        }
    }

    private AnalysisResult fetchProsAndCons(String companyName) {
        AnalysisResult result = null;
        List<Pros> pros = prosRepository.findByCompanyName(companyName);
        List<Cons> cons = consRepository.findByCompanyName(companyName);

        if (!CollectionUtils.isEmpty(pros) && !CollectionUtils.isEmpty(cons)) {
            return new AnalysisResult(pros.stream().map(Pros::getPro).toList(), cons.stream().map(Cons::getCon).toList());
        }
        String url = URL_TEMPLATE.replace("{companyName}", companyName);

        // Call the endpoint and get the HTML as a string
        RestTemplate restTemplate = new RestTemplate();
        String responseHtml = restTemplate.getForObject(url, String.class);

        // Parse the HTML using Jsoup
        AnalysisResult analysisResult = parseHtmlForAnalysis(responseHtml);
        saveProsAndCons(companyName, analysisResult.getPros(), analysisResult.getCons());
        return analysisResult;
    }
}
