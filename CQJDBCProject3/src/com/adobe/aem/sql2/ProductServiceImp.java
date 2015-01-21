package com.adobe.aem.sql2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
  
import java.util.List;
import java.util.ArrayList;
  
import org.w3c.dom.Document;
import org.w3c.dom.Element;
   
import java.io.StringWriter;
import java.util.Iterator;
  
  
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
  
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ProductServiceImp implements ProductService{
	
	//Update Product Method
		@Override
		public void updateProductData(String idProd,String id,String name, String type, String qty, String desc, String price) {
			Connection c2 = null;
		    try {
		        
		        // Create a Connection object
		        c2 =  ConnectionHelper.getConnection();
		           
		         PreparedStatement ps2 = null;  
		         //Set the PK value
	             int pkVal2 = Integer.parseInt(idProd);
	             int pkVal3 = Integer.parseInt(id);
		         //Query for updating a current entry, searches by current ID
		         //however, the id can be changed with this query also
		         String update = "UPDATE Product SET prodId=?, prodName=?, prodType=?, prodDescription=?, prodQty=?, prodPrice=? WHERE prodId=?;";
		         ps2 = c2.prepareStatement(update);
		         ps2.setInt(1, pkVal3);
		         ps2.setString(2, name);
	               ps2.setString(3, type);
	               ps2.setString(4, desc);
	               ps2.setString(5, qty);
	               ps2.setString(6, price);
	               ps2.setInt(7, pkVal2);
		         ps2.execute();

		  }
		  catch (Exception e) {
		    e.printStackTrace();
		   }
		  finally {
			ConnectionHelper.close(c2);
		  }
		}
		
		
		
		//Delete Product Method
		@Override
		public void deleteProductData(String idProd) {
			Connection c = null;
	    try {
	        
	        // Create a Connection object
	        c =  ConnectionHelper.getConnection();
	           
	         PreparedStatement ps = null;  
	         //Set the PK value
             int pkVal = Integer.parseInt(idProd);
	         //Query for updating a current entry, searches by current ID
	         //however, the id can be changed with this query also
	         String delete = "DELETE FROM Product WHERE prodId = ?;";
	         ps = c.prepareStatement(delete);
	         ps.setInt(1, pkVal);
	         ps.execute();

	  }
	  catch (Exception e) {
	    e.printStackTrace();
	   }
	  finally {
	    ConnectionHelper.close(c);
	  }
	}	
	
	@Override
	public int injestProdData(String id, String name, String type, String qty,
			String desc, String price ) {
		Connection c5 = null;
        
        int rowCount2= 0; 
        try {
                                     
              // Create a Connection object
              c5 =  ConnectionHelper.getConnection();
            
               ResultSet rs5 = null;
               Statement s5 = c5.createStatement();
               Statement scount5 = c5.createStatement();
                  
               //Use prepared statements to protected against SQL injection attacks
               PreparedStatement pstmt5 = null;
               PreparedStatement ps5 = null; 
                            
               //Set the query and use a preparedStatement
               String query7 = "Select * FROM Product";
               pstmt5 = c5.prepareStatement(query7); 
               rs5 = pstmt5.executeQuery();
                  
               while (rs5.next()) 
                       rowCount2++;
                               
               //Set the PK value
               int pkVal7 = Integer.parseInt(id);
                  
               String insert = "INSERT INTO Product(prodId,prodName,prodType,prodDescription,prodQty,prodPrice) VALUES(?, ?, ?, ?, ?, ?);";
               ps5 = c5.prepareStatement(insert);
               ps5.setInt(1, pkVal7);
               ps5.setString(2, name);
               ps5.setString(3, type);
               ps5.setString(4, desc);
               ps5.setString(5, qty);
               ps5.setString(6, price);
               ps5.execute();
               return pkVal7;
        }
        catch (Exception e) {
          e.printStackTrace();
         }
        finally {
          ConnectionHelper.close(c5);
    }
        return 0;
	}

	@Override
	public String getProductData(String filter) {
		Connection c = null;
		 int rowCount= 0; 
	        Product prod = null; 
	        String query = "";
	        List<Product> prodList = new ArrayList<Product>();
	        try {
	                                     
	              // Create a Connection object
	              c =  ConnectionHelper.getConnection();
	            
	               ResultSet rs = null;
	               Statement s = c.createStatement();
	               Statement scount = c.createStatement();
	                  
	               //Use prepared statements to protected against SQL injection attacks
	               PreparedStatement pstmt = null;
	               PreparedStatement ps = null; 
	                            
	               //Set the query and use a preparedStatement
	               if (filter.equals("All Products"))
	                   query = "Select prodId,prodName,prodType,prodDescription,prodQty,prodPrice FROM Product";
	               else if (filter.equals("Customizable Products"))
	            	   query = "Select prodId,prodName,prodType,prodDescription,prodQty,prodPrice FROM Product WHERE prodName LIKE '%[CUST]'";
	               else if(filter.equals("Lowstock"))
	            	   query = "SELECT * FROM Product WHERE prodQty < 10";
	               else if(filter.equals("Gnomes"))
	            	   query = "Select prodId,prodName,prodType,prodDescription,prodQty,prodPrice FROM Product WHERE prodType = 'Gnome'";
	               else if(filter.equals("Accessories"))
	            	   query = "Select prodId,prodName,prodType,prodDescription,prodQty,prodPrice FROM Product WHERE prodType = 'Accessories'";
	               else if(filter.equals("Seasonal"))
	            	   query = "Select prodId,prodName,prodType,prodDescription,prodQty,prodPrice FROM Product WHERE prodType = 'Seasonal'";
	               else if(filter.equals("Tools"))
	            	   query = "Select prodId,prodName,prodType,prodDescription,prodQty,prodPrice FROM Product WHERE prodType = 'Garden Tools'";
	                 
	               pstmt = c.prepareStatement(query); 
	               rs = pstmt.executeQuery();
	                  
	               while (rs.next()) 
	               {
	                   //for each pass - create a Produce object
	                   prod = new Product(); 
	                     
	                   //Populate product members with data from MySQL
	                   int prodId = rs.getInt(1);
	                   String id = Integer.toString(prodId);
	                   prod.setProductId(rs.getString(1)); 
	                     
	                   prod.setName(rs.getString(2));
	                   prod.setType(rs.getString(3));
	                  
	                   prod.setDescription(rs.getString(4));
	                   prod.setQty(rs.getString(5));
	                   prod.setPrice(rs.getString(6));
	                     
	                 //Push Customer to the list
	                 prodList.add(prod);
	                      
	               }             
	               //return xml that contains all customer taken from MySQL
	               return convertToString(toXml(prodList));               
	                 
	        }
	    catch(Exception e)
	        {
	         e.printStackTrace();
	        }
	        return null;
	}

	  //Convert Customer data retrieved from the AEM JCR
    //into an XML schema to pass back to client
    private Document toXml(List<Product> prodList) {
    try
    {
        DocumentBuilderFactory factory =     DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
                       
        //Start building the XML to pass back to the AEM client
        Element root = doc.createElement( "Products" );
        doc.appendChild( root );
                    
        //Get the elements from the collection
        int prodCount = prodList.size();
          
        //Iterate through the collection to build up the DOM           
         for ( int index=0; index < prodCount; index++) {
       
             //Get the Customer object from the collection
             Product myProd = (Product)prodList.get(index);
                            
             Element Product = doc.createElement( "Product" );
             root.appendChild( Product );
                             
             //Set qty
             Element id = doc.createElement( "Id" );
             id.appendChild( doc.createTextNode(myProd.getProductId() ) );
             Product.appendChild( id );
             
             //Add rest of data as child elements to customer
             //Set  Name
             Element name = doc.createElement( "Name" );
             name.appendChild( doc.createTextNode(myProd.getName()) );
             Product.appendChild( name );
                                                                
             //Set type
             Element type = doc.createElement( "Type" );
             type.appendChild( doc.createTextNode(myProd.getType() ) );
             Product.appendChild( type );                       
                         
             //Set desc
             Element desc = doc.createElement( "Description" );
             desc.appendChild( doc.createTextNode(myProd.getDescription()) );
             Product.appendChild( desc );
             
             //Set qty
             Element qty = doc.createElement( "Qty" );
             qty.appendChild( doc.createTextNode(myProd.getQty() ) );
             Product.appendChild( qty );
             
             //Set price
             Element price = doc.createElement( "Price" );
             price.appendChild( doc.createTextNode(myProd.getPrice()) );
             Product.appendChild( price );
          }
                  
    return doc;
    }
    catch(Exception e)
    {
        e.printStackTrace();
        }
    return null;
    }
       
       
    private String convertToString(Document xml)
    {
    try {
       Transformer transformer = TransformerFactory.newInstance().newTransformer();
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(xml);
      transformer.transform(source, result);
      return result.getWriter().toString();
    } catch(Exception ex) {
          ex.printStackTrace();
    }
      return null;
     }
    }
