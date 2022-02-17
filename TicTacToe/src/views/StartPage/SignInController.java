/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views.StartPage;
import controllers.Server;
import views.*;
import models.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import controllers.Database;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import views.MainRoom.MainRoom;
import views.MainRoom.MainRoomController;

/**
 *
 * @author Hossam
 */
public class SignInController   {
    Database db;
    Person p;
    @FXML
    private Button loginbtn;
    @FXML
    private Button singupbtn;
    @FXML
    private TextField txtusername;
    @FXML
    private TextField txtpassword;
    @FXML
    private Label txtalert ;
    
    
    
    
    @FXML
    private void SignNHandle(ActionEvent event) throws SQLException, IOException{
        db = Server.getDatabase();
          
        String regex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(txtusername.getText());
        String userName = txtusername.getText().trim();
       
        String password = txtpassword.getText().trim();
         
        System.out.print((userName+" "+ " "+" "+password));

        if (userName.isEmpty() || password.isEmpty()) {
            Platform.runLater(()-> {
                txtalert.setText("Empty Fields is Required");
            });

        } else if(txtusername.getText().equals("")){
                 Platform.runLater(()-> {
                txtalert.setText("Please enter a valid username");
            });
                
            }else if(!matcher.matches()){
                Platform.runLater(()->{
                  txtalert.setText("Please enter a valid username");
                 }); 
            } else if(txtpassword.getText().equals("")){
                Platform.runLater(()-> {
                txtalert.setText("Please enter password");
            });
            } else {
           
           if(db.logIn(userName, password)){ 
               Server.db.updatePlayerStatus(userName, "online");
               p = db.getPlayer(userName);
               Server.updateOnlinePlayersVector(p);
               finshSignIn(event);
          }
       }  
      
    }
    public void finshSignIn(ActionEvent event) throws IOException{
         
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("views/MainRoom/MainRoom.fxml"));
        Parent View = loader.load();
        
        Scene ViewScene = new Scene(View);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(ViewScene);
        MainRoomController controller = loader.getController();
        controller.logPlayer(p);
        controller.initSockets();
        window.show();
    }
    public void SwitchtoSignUp(ActionEvent event) throws IOException
    {
        Parent signUpView =  FXMLLoader.load(getClass().getClassLoader().getResource("views/StartPage/SingUp.fxml"));
        Scene signUpViewScene = new Scene(signUpView);
        
        //This line gets the Stage information
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(signUpViewScene);
        window.show();
    }
    
    
    
}
