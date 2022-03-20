/*
 * Margaret Chrysler #001224428
 * Software I (C482) - Final Task
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Margaret Chrysler #001224428
 * Created from the following UML information:
 * Inventory Class
 *      private allParts:ObservableList<Part>
 *      private allProducts:ObservableList<Product>
 * 
 *      public addPart(newPart:Part):void
 *      public addProduct(newProduct:Product):void
 *      public lookupPart(PartId:int):Part
 *      public lookupProduct(ProductId:int):Product
 *      public lookupPart(PartName:String):ObservableList<Part>
 *      public lookupProduct(productName:String):ObservableList<Product>
 *      public updatePart(index:int, selectedPart:Part):void
 *      public updateProduct(index:int, newProduct:Product):void
 *      public deletePart(selectedPart:Part):boolean
 *      public deleteProduct(selectedProduct:Product):boolean
 *      public getAllParts():ObservableList<Part>
 *      public getAllProducts():ObservableLIst<Product>
 * 
 **/
public class Inventory {
    
    // Class Variables
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    
    // Class Methods
  
    public static void addPart(Part newPart){
        //Add the part to allParts
        allParts.add(newPart);

    }
    public static void addProduct(Product newProduct){
        //Add the product to allProducts
        allProducts.add(newProduct);
    }
    
    public static Part lookupPart(int partId){
        //Search using a given part ID

        //find index from partId
        int lookupIndex = -1;
        for(Part lookupPart: allParts){
            if(lookupPart.getId()==partId){
                lookupIndex = allParts.indexOf(lookupPart);
            }
        }
        
        return allParts.get(lookupIndex);
    }
    
    public static Product lookupProduct(int productId){
        //Search using a given product ID
        //find index from partId
        int lookupIndex = -1;
        for(Product lookupProduct: allProducts){
            if((lookupProduct.getId())==productId){
                lookupIndex = allProducts.indexOf(lookupProduct);
            }
        }
        
        return (allProducts.get(lookupIndex));
    }
    
    public static ObservableList<Part> lookupPart(String partName){
        //Search for all items with the same part name
        ObservableList<Part> returnList = allParts.filtered(name -> name.getName().contains(partName));
        return returnList;
    }
    
    public static ObservableList<Product> lookupProduct(String productName){
        //Search for all items with the same product name 
        ObservableList<Product> returnList = allProducts.filtered(name -> name.getName().contains(productName));
        return returnList;
    }
    
    public static boolean deletePart(Part selectedPart){
        //Remove a selected part
        return allParts.remove(selectedPart);
    }
    
    public static boolean deleteProduct(Product selectedProduct){
        //Remove a selected product
        return allProducts.remove(selectedProduct);
    }
    
    
    
    public static void updatePart(int index, Part selectedPart){
        //update(overwrite) that part's new information
        allParts.set(index, selectedPart);
    }
    
    
    public static void updateProduct(int index, Product selectedProduct){
        //Search for a product using the index given and update that product's information
        allProducts.set(index, selectedProduct);
    }


    public static ObservableList<Part>getAllParts(){
        //Return a list of all of the parts
        return allParts;
    }
    
    public static ObservableList<Product>getAllProducts(){
        //Return a list of all of the products 
        return allProducts;
    }
   
}
