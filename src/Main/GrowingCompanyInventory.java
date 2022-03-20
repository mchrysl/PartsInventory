/*
 * Margaret Chrysler #001224428
 * Software I (C482) - Final Task
 */
package Main;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import Model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 *
 * @author Margaret Chrysler
 */
public class GrowingCompanyInventory extends Application {
    
      /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Inventory inv = new Inventory();
        fillTestData(inv);  
        
        //Declare loader and controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
        View_Controller.MainScreenController controller = new View_Controller.MainScreenController(inv, stage);   
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        //Set the stage with the scene and then show;
        stage.setScene(scene);
        stage.show();
    }

    void fillTestData(Inventory inv){
        //Add InHouse Parts Data (5 pieces)
        Part iPart1 = new InHouse(1, "iPart 1", 0.99, 10, 5, 100, 101);
        Inventory.addPart(iPart1);
        Part iPart2 = new InHouse(2, "iPart 2", 1.99, 20, 10, 110, 102);
        Inventory.addPart(iPart2);
        Part iPart3 = new InHouse(3, "iPart 3", 2.99, 30, 15, 115, 103);
        Inventory.addPart(iPart3);
        Inventory.addPart(new InHouse(4, "iPart 4", 3.99, 40, 20, 120, 104));
        Inventory.addPart(new InHouse(5, "iPart 5", 4.99, 50, 30, 125, 105));
                
        //Add Outsourced Parts Data (5 pieces)
        Part oPart1 = new Outsourced(6, "oPart 1", 1.50, 15, 10, 50, "Company 1");
        Part oPart2 = new Outsourced(7, "oPart 2", 2.50, 20, 20, 60, "Company 1");
        Part oPart3 = new Outsourced(8, "oPart 3", 3.50, 25, 30, 70, "Company 2");
        Inventory.addPart(oPart1);
        Inventory.addPart(oPart2);
        Inventory.addPart(oPart3);
        Inventory.addPart(new Outsourced(9, "oPart 4", 4.50, 30, 40, 80, "Company 2"));
        Inventory.addPart(new Outsourced(10, "oPart 5", 5.50, 35, 50, 90, "Company 3"));
        
        //Add Product Data
        Product product1 = new Product(1001, "Product 1001", 10.00, 101, 11, 1000);
        product1.addAssociatedPart(iPart1);
        product1.addAssociatedPart(oPart1);
        Inventory.addProduct(product1);
        Product product2 = new Product(1002, "Product 1002", 20.00, 102, 12, 1000);
        product2.addAssociatedPart(iPart2);
        product2.addAssociatedPart(oPart2);
        Inventory.addProduct(product2);
        Product product3 = new Product(1003, "Product 1003", 30.00, 103, 13, 1000);
        product3.addAssociatedPart(iPart3);
        product3.addAssociatedPart(oPart3);
        Inventory.addProduct(product3);
        Inventory.addProduct(new Product(1004, "Product 1004", 40.00, 104, 14, 1000));
        Inventory.addProduct(new Product(1005, "Product 1005", 50.00, 105, 15, 1000));
    }
    
}
