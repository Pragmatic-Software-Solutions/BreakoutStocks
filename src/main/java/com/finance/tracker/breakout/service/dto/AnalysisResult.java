package com.finance.tracker.breakout.service.dto;

import java.util.List;

public class AnalysisResult {

    private List<String> pros;
    private List<String> cons;

    public AnalysisResult(List<String> pros, List<String> cons) {
        this.pros = pros;
        this.cons = cons;
    }

    // Getters and Setters
    public List<String> getPros() {
        return pros;
    }

    public void setPros(List<String> pros) {
        this.pros = pros;
    }

    public List<String> getCons() {
        return cons;
    }

    public void setCons(List<String> cons) {
        this.cons = cons;
    }

    @Override
    public String toString() {
        return "AnalysisResult{" + "pros=" + pros + ", cons=" + cons + '}';
    }
}
