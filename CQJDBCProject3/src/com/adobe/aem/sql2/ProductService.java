package com.adobe.aem.sql2;

public interface ProductService {

	//Adds a new product record in the Product table
    public int injestProdData( String id,String name, String type, String qty, String desc, String price);
    
    public String getProductData(String filter); 
    
    public void updateProductData(String prodId, String id,String name, String type, String qty, String desc, String price);
    
    public void deleteProductData(String prodId);
}
