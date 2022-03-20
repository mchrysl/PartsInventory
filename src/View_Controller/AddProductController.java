/*
 * Margaret Chrysler #001224428
 * Software I (C482) - Final Task
 */
package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
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
public class AddProductController implements Initializable {
    Inventory inv;
    Stage stage;
    @FXML
    private Label addProductLbl;
    @FXML
    private Button addProductSearchBtn;
    @FXML
    private TextField addProductSearchTxt;
    @FXML
    private Button addProductBtn;
    @FXML
    private Button addProductDeleteBtn;
    @FXML
    private Button addProductCancelBtn;
    @FXML
    private Button addProductSaveBtn;
    @FXML
    private Label addProductIdLbl;
    @FXML
    private TextField idTxt;
    @FXML
    private Label addProductNameLbl;
    @FXML
    private Label addProductInventoryLbl;
    @FXML
    private Label addProductPriceLbl;
    @FXML
    private Label addProductMaxLbl;
    @FXML
    private TextField addProductNameTxt;
    @FXML
    private TextField addProductInvTxt;
    @FXML
    private TextField addProductPriceTxt;
    @FXML
    private TextField addProductMaxTxt;
    @FXML
    private Label addProductMinLbl;
    @FXML
    private TextField addProductMinTxt;
    @FXML
    private Label tempAssocPartLbl;
    @FXML
    private Label TempDisassocPart;
    
    private ObservableList<Part> allPartsTableInventory = FXCollections.observableArrayList();
    private ObservableList<Part> associatedPartsTableInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partSearchList = FXCollections.observableArrayList();
    @FXML
    private TableView<Part> assocPartProductTable;
     @FXML
    private TableColumn<Part,Integer>  partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> inventoryColumn;
    @FXML
    private TableView<Part> disPartProductTable;
    @FXML
    private TableColumn<Part, Integer> assocPartIdCol;
    @FXML
    private TableColumn<Part, String> assocPartNameCol;
    @FXML
    private TableColumn<Part, Integer> assocPartInvCol;
    
    Product newProduct;
    boolean isNewProductCreated;
    boolean firstGenerateTable;
    @FXML
    private Label mustHavePartLbl;
    @FXML
    private Label minMaxWarnLbl;
    @FXML
    private Label inventoryWarnLbl;
    @FXML
    private Label priceWarnLbl;
    @FXML
    private Label mustEnterDataLbl;


    
    
    
    public AddProductController(Inventory inv, Stage stage){
            this.inv = inv;
            this.stage = stage;
         //set up stage exit button override
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            private Object event;
            @Override
            public void handle(WindowEvent e) {
                e.consume();
                try {
                    //Confirm return to Main Screen
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Leave Add Product Screen");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to return to the Main Screen?"+System.lineSeparator()+"No products will be added.");
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
        //load table of parts to add
        generateAllPartsTable();
        //disable table of parts to delete/disassociate until something is added
        assocPartProductTable.setDisable(true);
        addProductBtn.setDisable(true);
        disPartProductTable.setDisable(true);
        addProductDeleteBtn.setDisable(true);
        isNewProductCreated = false;
        firstGenerateTable = true;
    }    
    
    private void generateAllPartsTable(){
        allPartsTableInventory.setAll(Inventory.getAllParts());
        TableColumn<Part,Double> partPriceCol = formatPrice();
        assocPartProductTable.getColumns().addAll(partPriceCol);
        
        assocPartProductTable.setItems(allPartsTableInventory);
        assocPartProductTable.refresh();
     
    }
    
        private void generateRemovePartsTable(){
            //build table of associated parts
            
            associatedPartsTableInventory.setAll(newProduct.getAllAssociateParts());
            if(firstGenerateTable){
                TableColumn<Part,Double> partPriceCol = formatPrice();
                disPartProductTable.getColumns().addAll(partPriceCol);
                firstGenerateTable = false;
            }
            
            //Determine warning about need at least one part?
            if(newProduct.getAllAssociateParts().isEmpty()){
                mustHavePartLbl.setVisible(true);
            }else{
                mustHavePartLbl.setVisible(false);
            }

            //Warn if the product price is less than the cost
            priceMoreThanCost();
            
            disPartProductTable.setItems(associatedPartsTableInventory);
            disPartProductTable.refresh();
    }
    

    @FXML
    public void searchPartButtonClicked(){
        //**source button determines whether Parts or Products

        //get string that is the searchRequest
        String searchInput;
        searchInput=addProductSearchTxt.getText();

        //Make sure searchRequest is not an empty string
        if(searchInput.isEmpty()){
            addProductSearchTxt.setPromptText("enter part ID or name");
            
            //Reset to full tableView
            allPartsTableInventory.setAll(Inventory.getAllParts());  
            assocPartProductTable.setItems(allPartsTableInventory);
            assocPartProductTable.refresh();
        }   //empty string works
        else{
            //Input not empty - determine type and validity of input
            if((integer_check(searchInput))==true){     //this line works
                //System.out.println("input is a number");
                //is search input valid? (is it in Table?)
                ObservableList<Part> searchPartsList = Inventory.getAllParts();
                allPartsTableInventory.clear();
                for(Part searchPart: searchPartsList){
                    if((searchPart.getId())==(parseInt(searchInput))){
                        allPartsTableInventory.setAll(Inventory.lookupPart(parseInt(searchInput)));   
                    }
                allPartsTableInventory.setAll(Inventory.lookupPart(parseInt(searchInput)));
                }
            }else{
                partSearchList = Inventory.lookupPart(searchInput); //string lookup call
                allPartsTableInventory.setAll(partSearchList);
            }
            
            assocPartProductTable.setItems(allPartsTableInventory);
            assocPartProductTable.refresh();
        }  //end outer if-else          
    }
    
    
    @FXML
    public void saveButtonClicked() throws Exception {
        
        //Get information for next ID to add
        ObservableList<Product> tempProduct = Inventory.getAllProducts();
        int nextIndex = tempProduct.size();
        int nextId = ((tempProduct.get(nextIndex-1)).getId())+1;
        
        //Make sure information is there so parsing can happen [Exception Set 2:name, price, & inventory level must be populated]
        boolean emptyFlag = false;   //start out presuming people know to fill in textfields
        if(((addProductNameTxt.getText().trim().isEmpty()))){
            addProductNameTxt.setPromptText("enter name");
            emptyFlag = true;
        }
        if(((addProductPriceTxt.getText().trim().isEmpty()))){
            addProductPriceTxt.setPromptText("enter price");
            emptyFlag = true;
        }
        if(((addProductInvTxt.getText().trim().isEmpty()))){
            addProductInvTxt.setPromptText("enter inventory");
            emptyFlag = true;
        }
        if((addProductMinTxt.getText().trim().isEmpty())){
            addProductMinTxt.setPromptText("enter min");
            emptyFlag = true;
        }
        if(parseInt(addProductMinTxt.getText().trim())<0){
            addProductMinTxt.setText("");
            addProductMinTxt.setPromptText("no negatives");
            emptyFlag = true;
        }
        if(addProductMaxTxt.getText().trim().isEmpty()) {
            addProductMaxTxt.setPromptText("enter max");
            emptyFlag = true;
        }
        if(parseInt(addProductMaxTxt.getText().trim())<0){
            addProductMaxTxt.setText("");
            addProductMaxTxt.setPromptText("no negatives");
            emptyFlag = true;
        }

            
        if(!emptyFlag){    //once everything is filled in, grab info and create newProduct
            mustEnterDataLbl.setText("");
            
            //Load the information from screen
            String name = addProductNameTxt.getText();
            double price = parseDouble(addProductPriceTxt.getText());
            int inventory = parseInt(addProductInvTxt.getText());
            int max = parseInt(addProductMaxTxt.getText());
            int min = parseInt(addProductMinTxt.getText());
            
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
            //Create newProduct and enable the part tableview
            if(!emptyFlag && isValidMinMax && isValidInventory){
                //if newProduct already exists, update it
                if(isNewProductCreated){
                    if(!(newProduct.getName().equals(name))){
                        newProduct.setName(name);
                    }
                    if(!(newProduct.getPrice()==price)){
                        newProduct.setPrice(price);
                    }
                    if(!(newProduct.getStock()==inventory)){
                        newProduct.setStock(inventory);
                    }
                    if(!(newProduct.getMin()==min)){
                        newProduct.setMin(min);
                    }
                    if(!(newProduct.getMax()==max)){
                        newProduct.setMax(max);
                    }
                }else{
                    //create newProduct, update new product flag, and enable parts list
                    newProduct = new Product(nextId, name, price, inventory, min, max);
                    isNewProductCreated = true;
                    assocPartProductTable.setDisable(false);
                    addProductBtn.setDisable(false);
                }
            }
            
            //Make sure there is a product 
            if(isNewProductCreated){
                //Make sure there are parts associated
                if(!newProduct.getAllAssociateParts().isEmpty()){
                    
                    //check that price is greater than cost of parts
                    if(priceMoreThanCost()){
                        //add the product to inventory and go to main screen
                        Inventory.addProduct(newProduct);
                        //Reload MainScreen         
                        reloadMainScreen();
                    }else{
                        priceWarnLbl.setVisible(true);
                    }
                } else{
                    mustHavePartLbl.setVisible(true);
                }
            }
        }else{
            mustEnterDataLbl.setText("All data fields must be filled out");
            minMaxWarnLbl.setVisible(false);
            inventoryWarnLbl.setVisible(false);
            priceWarnLbl.setVisible(false);
        }
    }   
    
    @FXML
    public void addButtonClicked(){
        //create new product if not created yet
        if(!isNewProductCreated){
            createNewProduct();
        }
        
        int selectedIndex = assocPartProductTable.getSelectionModel().getSelectedIndex();
        if((isNewProductCreated) && (selectedIndex >= 0)){
            //Add part association
            newProduct.addAssociatedPart(assocPartProductTable.getSelectionModel().getSelectedItem());

            //(re)load to potential delete/disassociate table
            generateRemovePartsTable();
            
            //enable delete/disassociate table and button
            if(disPartProductTable.isDisabled()){
                disPartProductTable.setDisable(false);
                addProductDeleteBtn.setDisable(false);
            }
        }       
    }
    
    @FXML
    public void deleteButtonClicked(){
        //Confirm return to Main Screen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete Part from Product");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete "+disPartProductTable.getSelectionModel().getSelectedItem().getName()+
                " from "+newProduct.getName()+"?");
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.OK){
            //Remove selected part
            newProduct.deleteAssociatedPart(disPartProductTable.getSelectionModel().getSelectedItem());

            //reload table of associated parts
            generateRemovePartsTable();
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
    
    @FXML
    public void cancelButtonClicked() throws Exception{
        //Confirm return to Main Screen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Leave Add Product Screen");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return to the Main Screen?"+System.lineSeparator()+"No products will be added.");
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.OK){
        //Go back to the main screen
        reloadMainScreen();
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
    
    private boolean createNewProduct(){ 
        return isNewProductCreated;
    }
    
    private boolean priceMoreThanCost(){
        //Determine cost of all parts
        double productCost = 0.00;
        ObservableList<Part> searchCost = newProduct.getAllAssociateParts();
        for(Part findProductCost: searchCost){
            productCost += findProductCost.getPrice();
        }
        if(newProduct.getPrice()>productCost){
            priceWarnLbl.setVisible(false);
            return true;
        }else{
            priceWarnLbl.setVisible(true);
            return false;
        }
    }
}
