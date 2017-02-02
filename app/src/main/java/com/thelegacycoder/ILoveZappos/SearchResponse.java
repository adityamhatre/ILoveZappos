package com.thelegacycoder.ILoveZappos;

import java.util.List;

public class SearchResponse{
	private String originalTerm;
	private int currentResultCount;
	private int totalResultCount;
	private String term;
	private List<ResultsItem> results;
	private String statusCode;

	public void setOriginalTerm(String originalTerm){
		this.originalTerm = originalTerm;
	}

	public String getOriginalTerm(){
		return originalTerm;
	}

	public void setCurrentResultCount(int currentResultCount){
		this.currentResultCount = currentResultCount;
	}

	public int getCurrentResultCount(){
		return currentResultCount;
	}

	public void setTotalResultCount(int totalResultCount){
		this.totalResultCount = totalResultCount;
	}

	public int getTotalResultCount(){
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

	@Override
	public String toString() {
		return "SearchResponse{" +
				"originalTerm='" + originalTerm + '\'' +
				", currentResultCount='" + currentResultCount + '\'' +
				", totalResultCount='" + totalResultCount + '\'' +
				", term='" + term + '\'' +
				", results=" + results +
				", statusCode='" + statusCode + '\'' +
				'}';
	}
}