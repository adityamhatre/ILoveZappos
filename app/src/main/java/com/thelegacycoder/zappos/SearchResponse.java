package com.thelegacycoder.zappos;

import java.util.List;

public class SearchResponse{
	private String originalTerm;
	private String currentResultCount;
	private String totalResultCount;
	private String term;
	private List<ResultsItem> results;
	private String statusCode;

	public void setOriginalTerm(String originalTerm){
		this.originalTerm = originalTerm;
	}

	public String getOriginalTerm(){
		return originalTerm;
	}

	public void setCurrentResultCount(String currentResultCount){
		this.currentResultCount = currentResultCount;
	}

	public String getCurrentResultCount(){
		return currentResultCount;
	}

	public void setTotalResultCount(String totalResultCount){
		this.totalResultCount = totalResultCount;
	}

	public String getTotalResultCount(){
		return totalResultCount;
	}

	public void setTerm(String term){
		this.term = term;
	}

	public String getTerm(){
		return term;
	}

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public void setStatusCode(String statusCode){
		this.statusCode = statusCode;
	}

	public String getStatusCode(){
		return statusCode;
	}
}