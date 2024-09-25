package com.finance.tracker.breakout.web.rest;

import com.finance.tracker.breakout.service.dto.AnalysisResult;
import com.finance.tracker.breakout.service.screener.RestListService;
import com.finance.tracker.breakout.service.screener.RestService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AnalysisController {

    @Autowired
    private RestService restService;

    @Autowired
    private RestListService restListService;

    @GetMapping("/analysis/{companyName}")
    public AnalysisResult getCompanyAnalysis(@PathVariable String companyName) {
        return restService.getProsAndCons(companyName);
    }

    @GetMapping("/analysis/stockgroups")
    public List<String> getStockListFiles() {
        return restListService.getStockListFiles();
    }

    @GetMapping("/analysis/companies/{stockGroup}")
    public Map<String, AnalysisResult> getCompanyAnalysisForAll(@PathVariable String stockGroup) {
        return restListService.getProsAndConsForCompanies(stockGroup);
    }
}
