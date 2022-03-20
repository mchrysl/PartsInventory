/*
 * Margaret Chrysler #001224428
 * Software I (C482) - Final Task
 */
package Model;

/**
 *
 * @author Margaret Chrysler
 * Created from the following UML information:
 *  Outsourced (class extended from Part)
 * 
 *      private companyName:String
 * 
 *      *constructor* Outsourced(id:int, name:String, price:double, stock:int, min:int, max, int, companyName:String)
 *      public setCompanyName(companyName:String):void
 *      public getCompanyName():String
 * 
 */
public class Outsourced extends Part {
    
    // Class Variable
    private String companyName;
    
    // Class Methods
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }
    
    public final void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    public String getCompanyName(){
        return companyName;
    }
}
