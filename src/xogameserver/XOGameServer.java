/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xogameserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class XOGameServer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = new MainServer();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
       System.exit(0);
    }
    

    
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
