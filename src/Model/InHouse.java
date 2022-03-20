/*
 * Margaret Chrysler #001224428
 * Software I (C482) - Final Task
 */
package Model;

/**
 *
 * @author Margaret Chrysler #001224428
 * Created from the following UML information:
 *  InHouse (extended class of Part)  
 *
 *      private machineId:int
 * 
 *      *constructor* InHouse(id:int, name:String, price:double, stock:int, min:int, max:int, machineId:int)
 *      public setMachineId(machineId:int):void
 *      public getMachineId():int
 *      
 */
public class InHouse extends Part{
    
    // Class Variable
    private int machineId;
    
    //Class Methods

    /**
     *
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineId
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        
        setMachineId(machineId);
    }
    
    public final void setMachineId(int machineId){
        this.machineId = machineId;
    }
    
    public int getMachineId(){
        return machineId;
    }
}
