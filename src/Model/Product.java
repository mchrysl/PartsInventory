/*
 * Margaret Chrysler #001224428
 * Software I (C482) - Final Task
 */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Margaret Chrysler#001224428
 * Created from the following UML information:
 *  Product
 * 
 *      private associateParts:ObservableList<Part>
 *      private id:int
 *      private name:String
 *      private price:double
 *      private stock:int
 *      private min:int
 *      private max:int
 * 
 *      *constructor* Product(id:int, name:String, price:double, stock:int, min:int, max:int)
 *      public setId(id:int):void
 *      public setName(name:String):void
 *      public setPrice(price:double):void
 *      public setStock(stock:int):void
 *      public setMin(min:int):void
 *      public setMax(max:int):void
 *      public setPrice(max:int):void
 *      public getId():int
 *      public getName():String
 *      public getPrice():double
 *      public getStock():int
 *      public getMin():int
 *      public getMax():int
 *      public addAssociatedPart(part:Part):void
 *      public deleteAssociatePart(selectedAsPart:Part):boolean
 *      public getAllAssociateParts():ObservableList<Part>
 * 
 */
public class Product {
    
    // Class Variables
     private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
     private int id;
     private String name;
     private double price;
     private int stock;
     private int min;
     private int max;
     
     //private int lastProductId;
     
     //Class Methods
    public Product(int id, String name, double price, int stock, int min, int max){  //Constructor
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }
    //Setters
     public void setId(int id){
         this.id = id;
    }
     public void setName(String name){
         this.name = name;
     }
     public void setPrice(double price){
         this.price = price;
    }
     public void setStock(int stock){
         this.stock = stock;
    }
     public void setMin(int min){
         this.min = min;
    }
     public void setMax(int max){
         this.max = max;
    }
     
    //Getters
     public int getId(){
         return this.id;
    }
     public String getName(){
         return this.name;
     }
     public double getPrice(){
         return this.price;
     }
     public int getStock(){
         return this.stock;
     }
     public int getMin(){
         return this.min;
     }
     public int getMax(){
         return this.max;
     }
     
     //public void addAssociatedPart(part:Part){}
     //public boolean deleteAssociatePart(selectedAsPart:Part){}
     
     public ObservableList<Part> getAllAssociateParts(){
        //return associatedParts for a particular Product
        return associatedParts;
     }

    public void addAssociatedPart(Part part) {
        //associate a part using Observable list named associatedParts
        associatedParts.addAll(part);
    }
    
    public boolean deleteAssociatedPart(Part selectedPart){
        return associatedParts.removeAll(selectedPart);
    }
}
