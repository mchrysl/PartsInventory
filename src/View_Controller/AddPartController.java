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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;





/**
 * FXML Controller class
 *
 * @author Margaret Chryser #001224428
 */
public class AddPartController implements Initializable {

    Inventory inv;
    Stage stage;
    
    @FXML
    private TextField idTxt;
    @FXML
    private ToggleGroup addPartSourceTog;
    @FXML
    private RadioButton inHouseRadio;
    private RadioButton outsourceRadio;
    @FXML
    private Label sourceLbl;
    @FXML
    private Label idLbl;
    @FXML
    private Label addPartLbl;
    @FXML
    private TextField sourceTxt;
    @FXML
    private RadioButton outsourcedRadio;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField inventoryTxt;
    @FXML
    private TextField priceTxt;
    @FXML
    private TextField maxTxt;
    @FXML
    private TextField minTxt;
    @FXML
    private Label minMaxWarnLbl;
    @FXML
    private Label inventoryWarnLbl;

    

public AddPartController(Inventory inv, Stage stage){
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
            alert.setTitle("Confirm Leave Add Part Screen");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to return to the Main Screen?"+System.lineSeparator()+"No parts will be added.");
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

@Override
public void initialize(URL url, ResourceBundle rb) {
    //Nothing to do in this one
} 


 /**
 * When a part source radio button is selected, change what appears
 * as the last entry label and textfield
 */
    @FXML
     private void radioButtonChangeSource(ActionEvent event) {
        sourceLbl.setTextFill(Color.BLACK); 
        if(addPartSourceTog.getSelectedToggle().equals(inHouseRadio)){
            sourceLbl.setText("Machine ID");
            sourceTxt.setPromptText("Mach ID");
        } else if (addPartSourceTog.getSelectedToggle().equals(outsourcedRadio)){
            sourceLbl.setText("Company Name");
            sourceTxt.setPromptText("Comp Nm");
        }
        sourceTxt.setDisable(false);
        sourceTxt.setVisible(true);        
    }
    
    /**
     *
     * @throws Exception
     */
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
        if(sourceTxt.getText().trim().isEmpty()){
            sourceTxt.setPromptText("enter source");
            emptyFlag = true;
        }
        if(sourceTxt.isDisabled()){
            sourceLbl.setTextFill(Color.RED);
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
            
            }else{      //none of the exceptions are true and nothing is empty
            //Determine subclass and add the part to the inventory
            if(inHouseRadio.isSelected()==true){
                if(!(sourceTxt.getText().trim().isEmpty())){
                int machineId = parseInt(sourceTxt.getText().trim());
                Inventory.addPart(new InHouse(nextId, name, price, inventory, min, max, machineId));
                }
            }else if(outsourcedRadio.isSelected()==true){
                String companyName = sourceTxt.getText().trim();
                Inventory.addPart(new Outsourced(nextId, name, price, inventory, min, max, companyName));
            }
            //Reload MainScreen
            reloadMainScreen();
            }
        }
        
    }   

    
    @FXML
    public void cancelButtonClicked() throws Exception {
        //Confirm return to Main Screen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Leave Add Part Screen");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to return to the Main Screen?"+System.lineSeparator()+"No parts will be added.");
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
 
}
