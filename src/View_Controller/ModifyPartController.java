/*
 * Margaret Chrysler #001224428
 * Software I (C482) - Final Task
 */
package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Margaret Chrysler #001224428
 */
public class ModifyPartController implements Initializable {

    Inventory inv;
    Stage stage;
    int selectedIndex;
    Part selectedPart;
    
    
    @FXML
    private Label modifyPartLbl;
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private ToggleGroup partSource;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label idLbl;
    @FXML
    private TextField idTxt;
    @FXML
    private Label nameLbl;
    @FXML
    private TextField nameTxt;
    @FXML
    private Label inventoryLbl;
    @FXML
    private TextField inventoryTxt;
    @FXML
    private Label priceLbl;
    @FXML
    private TextField priceTxt;
    @FXML
    private Label maxLbl;
    @FXML
    private TextField maxTxt;
    @FXML
    private Label minLbl;
    @FXML
    private TextField minTxt;
    @FXML
    private Label sourceLbl;
    @FXML
    private TextField sourceTxt;
    @FXML
    private RadioButton outsourcedRadio;
    
    private boolean isRadioBtnChanged = false;
    @FXML
    private Label minMaxWarnLbl;
    @FXML
    private Label inventoryWarnLbl;

    
    public ModifyPartController(Inventory inv, Stage stage, int selectedIndex, Part selectedPart){
        this.inv = inv;
        this.stage = stage;
        this.selectedIndex = selectedIndex;
        this.selectedPart = selectedPart;
        //set up stage exit button override
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            private Object event;
            @Override
            public void handle(WindowEvent e) {
                e.consume();
                try {
                    //Confirm return to Main Screen
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Leave Modify Part Screen");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to return to the Main Screen?"+System.lineSeparator()+"No part information will be updated.");
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
       //fill data fields
       fillOriginalData();
    }    

    
    @FXML
    private void radioButtonChangeSource(ActionEvent event) {        
       if(partSource.getSelectedToggle().equals(inHouseRadio)){
            sourceLbl.setText("Machine ID");
            sourceTxt.clear();
            sourceTxt.setPromptText("machine ID");
        } else if (partSource.getSelectedToggle().equals(outsourcedRadio) ){
            sourceLbl.setText("Company Name");
            sourceTxt.clear();
            sourceTxt.setPromptText("company Name");
        }
       isRadioBtnChanged = (isRadioBtnChanged != true);
       if(!isRadioBtnChanged){
           fillOriginalData();
       }
    }
    
    @FXML
    public void saveButtonClicked() throws Exception {
        //Make sure input fields are not empty
        boolean emptyFlag = false;
        if(((nameTxt.getText().trim().isEmpty()))){
            nameTxt.setPromptText("enter name");
            emptyFlag = true;
        }
        if(((priceTxt.getText().trim().isEmpty()))){
            priceTxt.setPromptText("enter price");
            emptyFlag = true;
        }
        if(((inventoryTxt.getText().trim().isEmpty()))){
            inventoryTxt.setPromptText("enter inventory");
            emptyFlag = true;
        }
        if((minTxt.getText().trim().isEmpty())){
            minTxt.setPromptText("enter min");
            emptyFlag = true;
        }
        if(parseInt(minTxt.getText().trim())<0){
            minTxt.setText("");
            minTxt.setPromptText("no negatives");
            emptyFlag = true;
        }
        if(maxTxt.getText().trim().isEmpty()) {
            maxTxt.setPromptText("enter max");
            emptyFlag = true;
        }
        if(parseInt(maxTxt.getText().trim())<0){
            maxTxt.setText("");
            maxTxt.setPromptText("no negatives");
            emptyFlag = true;
        }
        if(isRadioBtnChanged && sourceTxt.getText().trim().isEmpty()){
            sourceTxt.setPromptText("enter source");
            emptyFlag = true;
        }
        if(!emptyFlag){         //None of the TextFields are empty            
            //Get next ID to add
            ObservableList<Part> tempPart = Inventory.getAllParts();
            int nextIndex = tempPart.size();
            int nextId = ((tempPart.get(nextIndex-1)).getId())+1;
            
            //Load the variables from screen
            String name = nameTxt.getText().trim();
            double price = parseDouble(priceTxt.getText().trim());
            int inventory = parseInt(inventoryTxt.getText().trim());
            int max = parseInt(maxTxt.getText().trim());
            int min = parseInt(minTxt.getText().trim());

            //Determine if input is valid [Exception controls Set2: min, max, & inventory limits]
            if(min>max || inventory<min || inventory>max){
                //Create a warning label to prompt for valid input
                if( min>=max ){
                    minMaxWarnLbl.setVisible(true);
                }else{
                    minMaxWarnLbl.setVisible(false);
                }
                if(inventory<min || inventory>max){
                    inventoryWarnLbl.setVisible(true);
                }else{
                    inventoryWarnLbl.setVisible(false);
                }
            
            }else{      //none of the exceptions are true and nothing is empty - do the update
                if(isRadioBtnChanged){
                    //update requires a new subclass object (this still uses the same index)
                    updateAllFields();
                }else{
                    //Update information for selectedPart
                    if(!(selectedPart.getName().equals(name))){
                        selectedPart.setName(name);
                    }
                    if(!(selectedPart.getPrice() == price)){
                        selectedPart.setPrice(price);
                    }
                    if(!(selectedPart.getStock()==inventory)){
                        selectedPart.setStock(inventory);
                    }
                    if(!(selectedPart.getMin()==min)){
                        selectedPart.setMin(min);
                    }
                    if(!(selectedPart.getMax()==max)){
                        selectedPart.setMax(max);
                    }
                    //update the Part using selectedIndex and selectedPart
                    Inventory.updatePart(selectedIndex, selectedPart);
                }
            //Reload MainScreen
            reloadMainScreen();
            }
        }
    }
    

    @FXML
    public void cancelButtonClicked() throws Exception{
        //Confirm return to Main Screen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Leave Modify Part Screen");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return to the Main Screen?"+System.lineSeparator()+"No part information will be updated.");
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.OK){
        //Go back to the main screen
        reloadMainScreen();
        }
    }
    
    private void fillOriginalData(){
        //Fill in TextField boxes with selectedPart information
       idTxt.setText(String.valueOf(selectedPart.getId()));
       nameTxt.setText(selectedPart.getName());
       inventoryTxt.setText(String.valueOf(selectedPart.getStock()));
       priceTxt.setText(String.format(String.format("%.2f", selectedPart.getPrice())));
       maxTxt.setText(String.valueOf(selectedPart.getMax()));
       minTxt.setText(String.valueOf(selectedPart.getMin()));
       
       if(selectedPart instanceof InHouse){
           partSource.selectToggle(inHouseRadio);
           sourceLbl.setText("Machine ID");
           InHouse fillTextParti = (InHouse)selectedPart;
           sourceTxt.setText(String.valueOf(fillTextParti.getMachineId()));
       }else if(selectedPart instanceof Outsourced){
           partSource.selectToggle(outsourcedRadio);
           sourceLbl.setText("Company Name");
           Outsourced fillTextParto = (Outsourced)selectedPart;
           sourceTxt.setText(String.valueOf(fillTextParto.getCompanyName()));
       }
    }
    
    private void updateAllFields(){
        //Create a new subclass as needed
        if((selectedPart instanceof Outsourced) && isRadioBtnChanged){
            InHouse tempPartI = new InHouse((parseInt(idTxt.getText().trim())), nameTxt.getText().trim(),
                    (parseDouble(priceTxt.getText().trim())), (parseInt(inventoryTxt.getText().trim())), 
                    (parseInt(minTxt.getText().trim())), (parseInt(maxTxt.getText().trim())), (parseInt(sourceTxt.getText().trim())));
            Inventory.updatePart(selectedIndex, tempPartI);
        }else if((selectedPart instanceof InHouse) && isRadioBtnChanged){
            Outsourced tempPartO = new Outsourced((parseInt(idTxt.getText().trim())), nameTxt.getText().trim(),
                    (parseDouble(priceTxt.getText().trim())), (parseInt(inventoryTxt.getText().trim())), 
                    (parseInt(minTxt.getText().trim())), (parseInt(maxTxt.getText().trim())), (sourceTxt.getText().trim().trim()));
            Inventory.updatePart(selectedIndex, tempPartO);
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
