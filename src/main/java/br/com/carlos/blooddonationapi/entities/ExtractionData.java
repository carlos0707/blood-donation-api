package br.com.carlos.blooddonationapi.entities;

import org.bson.Document;

public class ExtractionData {

    private Document candidatesByState;

    private Document imcAgeGroup;

    private Document percentObesity;

    private Document averageByBloodType;

    private Document totalDonorsByBloodType;

    public ExtractionData() {}

    public ExtractionData(Document document) {
        this.fromDocument(document);
    }

    private ExtractionData fromDocument(Document document) {
        this.candidatesByState      = document.get("candidatesByState", Document.class);
        this.averageByBloodType     = document.get("averageByBloodType", Document.class);
        this.imcAgeGroup            = document.get("imcAgeGroup", Document.class);
        this.percentObesity         = document.get("percentObesity", Document.class);
        this.totalDonorsByBloodType = document.get("totalDonorsByBloodType", Document.class);

        return this;
    }

    public Document getCandidatesByState() {
        return candidatesByState;
    }

    public void setCandidatesByState(Document candidatesByState) {
        this.candidatesByState = candidatesByState;
    }

    public Document getImcAgeGroup() {
        return imcAgeGroup;
    }

    public void setImcAgeGroup(Document imcAgeGroup) {
        this.imcAgeGroup = imcAgeGroup;
    }

    public Document getPercentObesity() {
        return percentObesity;
    }

    public void setPercentObesity(Document percentObesity) {
        this.percentObesity = percentObesity;
    }

    public Document getAverageByBloodType() {
        return averageByBloodType;
    }

    public void setAverageByBloodType(Document averageByBloodType) {
        this.averageByBloodType = averageByBloodType;
    }

    public Document getTotalDonorsByBloodType() {
        return totalDonorsByBloodType;
    }

    public void setTotalDonorsByBloodType(Document totalDonorsByBloodType) {
        this.totalDonorsByBloodType = totalDonorsByBloodType;
    }
}
