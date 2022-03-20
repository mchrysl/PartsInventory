/*
 * Margaret Chrysler #001224428
 * Software I (C482) - Final Task
 */
package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Margaret Chrysler #001224428
 */
public class MainScreenController implements Initializable {
    
    Inventory inv;
    Stage stage;

    @FXML
    private Label MainScreenLbl;
    @FXML
    private TextField searchPartsText;
    @FXML
    private Button searchPartsButton;
    @FXML
    private Button deletePartsButton;
    @FXML
    private Button modifyPartsButton;
    @FXML
    private Button addPartsButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button searchProductsButton;
    @FXML
    private Button deleteProductsButton;
    @FXML
    private Button modifyProductsButton;
    @FXML
    private Button addProductsButton;
    @FXML
    private TableView<Part> partsMainTable;
    @FXML
    private TableView<Product> productsMainTable;
    private ObservableList<Part> partsTableInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productsTableInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partSearchList = FXCollections.observableArrayList();
    private ObservableList<Product> productSearchList  = FXCollections.observableArrayList();
    
    @FXML
    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;

    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;
    @FXML
    private TextField searchProductsText;
  
 
    public MainScreenController(Inventory inv, Stage stage){
        this.inv = inv;
        this.stage = stage;
        //set up stage exit button override
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        private Object event;
        @Override
        public void handle(WindowEvent e) {
            e.consume();
            try {
                exitButtonClicked();
            } catch (Exception ex) {
                Logger.getLogger(ModifyPartController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialize parts & product tables
        generatePartsTable();
        generateProductsTable();
    }    
    
    private void generatePartsTable(){
        partsTableInventory.setAll(Inventory.getAllParts());
        TableColumn<Part,Double> partPriceCol = formatPrice();
        partsMainTable.getColumns().addAll(partPriceCol);
        
        partsMainTable.setItems(partsTableInventory);
        partsMainTable.refresh();
    }
    
    private void generateProductsTable(){
        productsTableInventory.setAll(Inventory.getAllProducts());
        TableColumn<Product,Double> productPriceCol = formatPrice();
        productsMainTable.getColumns().addAll(productPriceCol);
        
        productsMainTable.setItems(productsTableInventory);
        productsMainTable.refresh();
    }
    
    
    @FXML
    public void addPartButtonClicked() throws IOException {
        //pulling information to pass for index of next part
        int nextIndex = (partsTableInventory.size());
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddPart.fxml"));
        View_Controller.AddPartController controller = new View_Controller.AddPartController(inv, stage);   
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();

    }
     
    @FXML
    public void modifyPartButtonClicked() throws IOException {

        //Gather selected item info to pass to Modify Part screen
        int selectedIndex = partsMainTable.getSelectionModel().getSelectedIndex();
        Part selectedPart = partsMainTable.getSelectionModel().getSelectedItem();
        if(selectedIndex >= 0){
            //Open Modify Part screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyPart.fxml"));
            View_Controller.ModifyPartController controller = new View_Controller.ModifyPartController(inv, stage, selectedIndex, selectedPart);   
            loader.setController(controller);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
         }
    }
    
    
    @FXML
    public void addProductButtonClicked() throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/AddProduct.fxml"));
        View_Controller.AddProductController controller = new View_Controller.AddProductController(inv, stage);   
        loader.setController(controller);
        
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();

    }
    
    @FXML
    public void modifyProductButtonClicked() throws IOException {
        
        int selectedIndex = productsMainTable.getSelectionModel().getSelectedIndex();
        Product selectedProduct = productsMainTable.getSelectionModel().getSelectedItem();
        
        if(selectedIndex >= 0){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/ModifyProduct.fxml"));
            View_Controller.ModifyProductController Acontroller = new View_Controller.ModifyProductController(inv, stage, selectedIndex, selectedProduct);   
            loader.setController(Acontroller);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        }
    }
    
    
    @FXML
    public void searchPartButtonClicked(){

        //get string that is the searchRequest
        String searchInput;
        searchInput=searchPartsText.getText();
        
        //Make sure searchRequest is not an empty string
        if(searchInput.isEmpty()){
            searchPartsText.setPromptText("enter part ID or name");
            
            //Reset to full tableView
            partsTableInventory.setAll(Inventory.getAllParts());  
            partsMainTable.setItems(partsTableInventory);
            partsMainTable.refresh();
        }   //empty string works
        else{
            if((integer_check(searchInput))==true){ 
                //is search input valid? (in the table?)
                ObservableList<Part> searchPartsList = Inventory.getAllParts();
                partsTableInventory.clear();
                for(Part searchPart: searchPartsList){
                    if((searchPart.getId())==(parseInt(searchInput))){
                        partsTableInventory.setAll(Inventory.lookupPart(parseInt(searchInput)));
                    }
                }    
            }
            else{
                partSearchList = Inventory.lookupPart(searchInput); //string lookup call
                partsTableInventory.setAll(partSearchList);
            }
            
            partsMainTable.setItems(partsTableInventory);
            partsMainTable.refresh();
        }  //end outer if-else          
    }
    
    public void searchProductButtonClicked(){

        //get string that is the searchRequest
        String searchInput;
        searchInput=searchProductsText.getText();

        //Make sure searchRequest is not an empty string
        if(searchInput.isEmpty()){
            searchProductsText.setPromptText("enter product ID or name");
            
            //Reset to full tableView
            productsTableInventory.setAll(Inventory.getAllProducts());  
            productsMainTable.setItems(productsTableInventory);
            productsMainTable.refresh();
        }   //empty string works
        else{
            //Is it an integer?
            if((integer_check(searchInput))==true){     //this line works
                //System.out.println("input is a number");
                //is search input valid? (is it in Table?)
                ObservableList<Product> searchProductsList = Inventory.getAllProducts();
                productsTableInventory.clear();
                for(Product searchProduct: searchProductsList){
                    if((searchProduct.getId())==(parseInt(searchInput))){
                        productsTableInventory.setAll(Inventory.lookupProduct(parseInt(searchInput)));  
                    }
                }
            }    
            else{  //Is it string input?
                productSearchList = Inventory.lookupProduct(searchInput); //string lookup call
                productsTableInventory.setAll(productSearchList);
            }
            productsMainTable.setItems(productsTableInventory);
            productsMainTable.refresh();
        }  //end outer if-else    
    
    }//method end
    
    
    
    @FXML
    public void deletePartButtonClicked(){
        //get Part selected in the tableView (partsMainTable)
        //  AND
        //call deletePart in Inventory inv [inv.deletePart(selectedPart);
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Part");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete "+partsMainTable.getSelectionModel().getSelectedItem().getName()+"?");
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.OK){
            if(Inventory.deletePart(partsMainTable.getSelectionModel().getSelectedItem())){
                //update the tableView
                partsTableInventory.setAll(Inventory.getAllParts());
                partsMainTable.setItems(partsTableInventory);
                partsMainTable.refresh();
            }
        }
    }
    
    @FXML
    public void deleteProductButtonClicked(){
        //get Part selected in the tableView (partsMainTable)
        //  AND
        //call deletePart in Inventory inv [inv.deletePart(selectedPart);
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Product");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete "+productsMainTable.getSelectionModel().getSelectedItem().getName()+"?");
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.OK){
            if(Inventory.deleteProduct(productsMainTable.getSelectionModel().getSelectedItem())){
                //update the tableView
                productsTableInventory.setAll(Inventory.getAllProducts());
                productsMainTable.setItems(productsTableInventory);
                productsMainTable.refresh();
            }
        }
    }
    
    
    
    @FXML
    public void exitButtonClicked(){
        //Confirm Exit Program
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm End Program");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to end the program?");
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.OK){
            System.exit(0);
        }
    }
    
    
    private<T>TableColumn<T, Double>formatPrice(){
        //set up the price column to display
        TableColumn<T, Double> costColumn = new TableColumn("Price");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        
        //currency formatting
        costColumn.setCellFactory((TableColumn<T, Double>column) ->{
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty){
                    if(!empty){
                        setText("$"+String.format("%10.2f", item));
                    }
                }
            };
        });
        return costColumn;
    }
    
    private boolean integer_check(String inputString){
        return (inputString.matches("\\d+"));
        
    }
}
