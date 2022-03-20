/*
 * Margaret Chrysler #001224428
 * Software I (C482) - Final Task
 */
package Model;

/**
 *
 * @author Margaret Chrysler #001224428
 * Created from the following UML information:
 *  Part (Abstract Class)
 * 
 *      private id:int
 *      private name:String
 *      private price:double
 *      private stock:int
 *      private min:int
 *      private max:int
 * 
 *      *constructor* Part(id:int, name:String, price:double, stock:int, min:int, max:int)
 *      public setId(id:int):void
 *      public setName(name:Stirng):void
 *      public setPrice(price:double):void
 *      public setStock(stock:int):void
 *      public setMin(min:int):void
 *      public setMax(max:int):void
 *      public getId():int
 *      public getName():String
 *      public getPrice():double
 *      public getStock():int
 *      public getMin():int
 *      public getMax():int
 * 
 */
public abstract class Part {
    
    // Class Variables
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    
    // Class Methods
    Part(int id, String name, double price, int stock, int min, int max){
        //Assign Part Variables
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }
    
    //Setters
    public final void setId(int id){
        this.id = id;
    }
    public final void setName(String name){
        this.name = name;
    }
    public final void setPrice(double price){
        this.price = price;
    }
    public final void setStock(int stock){
        this.stock = stock;
    }
    public final void setMin(int min){
        this.min = min;
    }
    public final void setMax(int max){
        this.max = max;
    }
    
    //Getters
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
    public int getStock(){
        return stock;
    }
    public int getMin(){
        return min;
    }
    public int getMax(){
        return max;
    }
   
}
