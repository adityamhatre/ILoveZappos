package com.thelegacycoder.ILoveZappos.Models;

import java.util.List;

public class ProductAPIResponse {
    private List<DetailedProductItem> product;
    private String statusCode;

    public void setProduct(List<DetailedProductItem> product) {
        this.product = product;
    }

    public List<DetailedProductItem> getProduct() {
        return product;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}