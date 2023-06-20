package br.com.carlos.blooddonationapi.entities;

import org.bson.Document;

import java.util.List;


public class Response {

    private Long total;

    private Integer currentPage;

    private Integer totalPages;

    private Integer limit;

    private List<?> result;

    private Boolean success;

    private String errorMessage;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    @Override
    public String toString() {
    	
		Document doc = new Document();
    	doc.put("success", this.success);
    	doc.put("errorMessage", this.errorMessage);
    	
    	return doc.toJson();
    }
}
