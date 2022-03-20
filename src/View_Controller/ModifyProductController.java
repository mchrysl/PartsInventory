/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import static java.lang.Double.parseDouble;
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
public class ModifyProductController implements Initializable {
    
    Inventory inv;
    Stage stage;

    @FXML
    private Label modifyProductLbl;
    @FXML
    private Button modifyProductSearchBtn;
    @FXML
    private TextField modifyProductSearchTxt;
    @FXML
    private Button modifyProductAddPartBtn;
    @FXML
    private TableColumn<Part, String> assocPartNameColumn;
    @FXML
    private Button modifyProductDeletePartBtn;
    @FXML
    private Button modifyProductSaveBtn;
    @FXML
    private Label modifyProductIdLbl;
    @FXML
    private TextField modifyIdTxt;
    @FXML
    private Label modifyProductNameLbl;
    @FXML
    private Label modifyProductInventoryLbl;
    @FXML
    private Label modifyProductPriceLbl;
    @FXML
    private Label modifyProductMaxLbl;
    @FXML
    private TextField modifyProductNameTxt;
    @FXML
    private TextField modifyProductInvTxt;
    @FXML
    private TextField modifyProductPriceTxt;
    @FXML
    private TextField modifyProductMaxTxt;
    @FXML
    private Label modifyProductMinLbl;
    @FXML
    private TextField modifyProductMinTxt;
    @FXML
    private Label tempAssocPartLbl;
    @FXML
    private Label TempDisassocPart;

    private ObservableList<Part> allPartsTableInventory = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartsTableInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partSearchList = FXCollections.observableArrayList();
    @FXML
    private TableView<Part> assocPartProductTable;
    //@FXML
    //private TableColumn<Part, Integer> assocPartIdColumn;
    @FXML
    private Button modifyProductCancelBtn;
    @FXML
    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;
    @FXML
    private TableView<Part> disPartProductTable;
    @FXML
    private TableColumn<Part, Integer> assocPartInvColumn;
    
    int selectedIndex;
    Product selectedProduct;
    private boolean firstGenAssocParts = true;
    @FXML
    private TableColumn<Part, Integer> assocPartIdColumn;
    @FXML
    private Label mustHavePartLbl;
    @FXML
    private Label mustEnterDataLbl;
    @FXML
    private Label minMaxWarnLbl;
    @FXML
    private Label inventoryWarnLbl;
    @FXML
    private Label priceWarnLbl;
    
    
    public ModifyProductController(Inventory inv, Stage stage, int selectedIndex, Product selectedProduct){
        this.inv = inv;
        this.stage = stage;
        this.selectedIndex = selectedIndex;
        this.selectedProduct = selectedProduct;
                //set up stage exit button override
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            private Object event;
            @Override
            public void handle(WindowEvent e) {
                e.consume();
                try {//Confirm return to Main Screen
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Leave Modify Product Screen");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to return to the Main Screen?"+System.lineSeparator()+"No products will be modified.");
                alert.showAndWait();

                if(alert.getResult() == ButtonType.OK){
                    reloadMainScreen();
                }
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
        // Generate tables
        generateAllPartsTable();
        generateAssocPartsTable();
         //Fill in TextField boxes with selectedPart information
       modifyIdTxt.setText(String.valueOf(selectedProduct.getId()));
       modifyProductNameTxt.setText(selectedProduct.getName());
       modifyProductInvTxt.setText(String.valueOf(selectedProduct.getStock()));
       modifyProductPriceTxt.setText(String.format("%.2f", selectedProduct.getPrice()));
       modifyProductMaxTxt.setText(String.valueOf(selectedProduct.getMax()));
       modifyProductMinTxt.setText(String.valueOf(selectedProduct.getMin()));
    }    
    
    private void generateAllPartsTable(){
        allPartsTableInventory.setAll(Inventory.getAllParts());
        TableColumn<Part,Double> partPriceCol = formatPrice();
        assocPartProductTable.getColumns().addAll(partPriceCol);
        
        assocPartProductTable.setItems(allPartsTableInventory);
        assocPartProductTable.refresh();
        
    }
    
    private void generateAssocPartsTable(){
        //ObservableList<Part> tempPartList = selectedProduct.getAllAssociateParts();
        assocPartsTableInventory.setAll(selectedProduct.getAllAssociateParts());
        if(firstGenAssocParts){
            TableColumn<Part,Double> partPriceCol = formatPrice();
            disPartProductTable.getColumns().addAll(partPriceCol);
            firstGenAssocParts = false;
        }
        
        //Determine warning about need at least one part?
        if(selectedProduct.getAllAssociateParts().isEmpty()){
            mustHavePartLbl.setVisible(true);
        }else{
            mustHavePartLbl.setVisible(false);
        }
        //Determine warning about Product price must be more than sum of associated Parts
        priceMoreThanCost();
        
        disPartProductTable.setItems(assocPartsTableInventory);
        disPartProductTable.refresh();
        

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
    
    @FXML
    public void searchPartButtonClicked(){
        //**source button determines whether Parts or Products

        //get string that is the searchRequest
        String searchInput;
        searchInput=modifyProductSearchTxt.getText();
        
        //Make sure searchRequest is not an empty string
        if(searchInput.isEmpty()){
            modifyProductSearchTxt.setPromptText("enter part ID or name");
            
            //Reset to full tableView
            allPartsTableInventory.setAll(Inventory.getAllParts());  
            assocPartProductTable.setItems(allPartsTableInventory);
            assocPartProductTable.refresh();
        }   //empty string works
        else{
            //Input not empty - determine type and validity of input
            if((integer_check(searchInput))==true){     //this line works
                //System.out.println("input is a number");
                ObservableList<Part> searchPartsList = Inventory.getAllParts();
                allPartsTableInventory.clear();
                for(Part searchPart: searchPartsList){
                    if((searchPart.getId())==(parseInt(searchInput))){
                        allPartsTableInventory.setAll(Inventory.lookupPart(parseInt(searchInput))); 
                    }
                }
                allPartsTableInventory.setAll(Inventory.lookupPart(parseInt(searchInput)));
            }
            else{
                partSearchList = Inventory.lookupPart(searchInput); //string lookup call
                allPartsTableInventory.setAll(partSearchList);
            }
            assocPartProductTable.setItems(allPartsTableInventory);
            assocPartProductTable.refresh();
        }  //end outer if-else          
    }
    
    
    @FXML
    public void addButtonClicked(){
        int addIndex = assocPartProductTable.getSelectionModel().getSelectedIndex();
        if(addIndex >= 0){
            //Add selected Part to selectedProduct
            selectedProduct.addAssociatedPart(assocPartProductTable.getSelectionModel().getSelectedItem());

            //(re)load to potential delete/disassociate table
            generateAssocPartsTable();

        }
    }
    
    @FXML
    public void deleteButtonClicked(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Part from Product");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete "+disPartProductTable.getSelectionModel().getSelectedItem().getName()+
                " from "+selectedProduct.getName()+"?");
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.OK){
            //Remove selected Part
            selectedProduct.deleteAssociatedPart(disPartProductTable.getSelectionModel().getSelectedItem());

            //(re)load to potention delete/disassociate table
            generateAssocPartsTable();

        }
    }

    @FXML
    public void saveButtonClicked() throws Exception {
        
        //Make sure information is there so parsing can happen [Exception Set 2:name, price, & inventory level must be populated]
        boolean emptyFlag = false;   //start out presuming people know to fill in textfields
        if(((modifyProductNameTxt.getText().trim().isEmpty()))){
            modifyProductNameTxt.setText("enter name");
            emptyFlag = true;
        }
        if(((modifyProductPriceTxt.getText().trim().isEmpty()))){
            modifyProductPriceTxt.setPromptText("enter price");
            emptyFlag = true;
        }
        if(((modifyProductInvTxt.getText().trim().isEmpty()))){
            modifyProductInvTxt.setPromptText("enter inventory");
            emptyFlag = true;
        }
        if((modifyProductMinTxt.getText().trim().isEmpty())){
            modifyProductMinTxt.setPromptText("enter min");
            emptyFlag = true;
        }
        if(parseInt(modifyProductMinTxt.getText().trim())<0){
            modifyProductMinTxt.setText("");
            modifyProductMinTxt.setPromptText("no negatives");
            emptyFlag = true;
        }        
        if(modifyProductMaxTxt.getText().trim().isEmpty()) {
            modifyProductMaxTxt.setPromptText("enter max");
            emptyFlag = true;
        }
        if(parseInt(modifyProductMaxTxt.getText().trim())<0){
            modifyProductMaxTxt.setText("");
            modifyProductMaxTxt.setPromptText("no negatives");
            emptyFlag = true;
        }
        
        //once everything is filled in, grab info from screen
        if(!emptyFlag){
            mustEnterDataLbl.setText("");

            //Load the information from screen
            String name = modifyProductNameTxt.getText().trim();
            double price = parseDouble(modifyProductPriceTxt.getText().trim());
            int inventory = parseInt(modifyProductInvTxt.getText().trim());
            int max = parseInt(modifyProductMaxTxt.getText().trim());
            int min = parseInt(modifyProductMinTxt.getText().trim());
            
            //Determine if input is valid [Exception controls Set1: min, max, & inventory limits]
            boolean isValidMinMax;
            boolean isValidInventory;
            //Create a warning label to prompt for valid input
            if( min>=max ){
                minMaxWarnLbl.setVisible(true);
                isValidMinMax = false;
            }else{
                minMaxWarnLbl.setVisible(false);
                isValidMinMax = true;
            }
            if(inventory<min || inventory>max){
                inventoryWarnLbl.setVisible(true);
                isValidInventory = false;
            }else{
                inventoryWarnLbl.setVisible(false);
                isValidInventory = true;
            }
                
            
            //If none of the exceptions are true and nothing is empty, 
            //Update selectedProduct and enable the part tableview
            if(!emptyFlag && isValidMinMax && isValidInventory){
                //Update selectedProduct if needed
                if(!(selectedProduct.getName().equals(name))){
                    selectedProduct.setName(name);
                }
                if(!(selectedProduct.getPrice()==price)){
                    selectedProduct.setPrice(price);
                }
                if(!(selectedProduct.getStock()==inventory)){
                    selectedProduct.setStock(inventory);
                }
                if(!(selectedProduct.getMin()==min)){
                    selectedProduct.setMin(min);
                }
                if(!(selectedProduct.getMax()==max)){
                    selectedProduct.setMax(max);
                }
                
                
                //Make sure there are parts associated [Exception Set 1]
                if(!selectedProduct.getAllAssociateParts().isEmpty()){
                    mustHavePartLbl.setVisible(false);
                    //check that price is greater than cost of parts
                    if(priceMoreThanCost()){
                        //add the product to inventory and go to main screen
                        Inventory.updateProduct(selectedIndex, selectedProduct);
                        //Reload MainScreen         
                        reloadMainScreen();
                    }else{
                        priceWarnLbl.setVisible(true);
                    }
                }
            }
        }else{  //Some data fields are empty
            mustEnterDataLbl.setText("All data fields must be filled out");
            minMaxWarnLbl.setVisible(false);
            inventoryWarnLbl.setVisible(false);
            priceWarnLbl.setVisible(false);
        }       
    }   
    
    
    @FXML
    public void cancelButtonClicked() throws Exception{
        //Confirm return to Main Screen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Leave Modify Product Screen");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return to the Main Screen?"+System.lineSeparator()+"No products will be modified.");
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.OK){
        //Go back to the main screen
            reloadMainScreen();
        }
    }
    
    private boolean integer_check(String inputString){
        return (inputString.matches("\\d+"));   
    }  
    
    private boolean priceMoreThanCost(){
        //Determine cost of all parts
        double productCost = 0.00;
        ObservableList<Part> searchCost = selectedProduct.getAllAssociateParts();
        for(Part findProductCost: searchCost){
            productCost += findProductCost.getPrice();
        }

        if(selectedProduct.getPrice()>productCost){
            priceWarnLbl.setVisible(false);
            return true;
        }else{
            priceWarnLbl.setVisible(true);
            return false;
        }
    }
    
    private void reloadMainScreen() throws Exception{
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View_Controller/MainScreen.fxml"));
        View_Controller.MainScreenController controller = new View_Controller.MainScreenController(inv, stage);   
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
       
    }
}
