package xogameserver;

import DataBase.DataAccessLayer;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class MainServer extends AnchorPane {

    protected ListView lstOnlineUsers;
    protected Label label;

    protected Button btnStart;
    protected final Label lbIp;
    protected Label lbIpAdd;
    public ServerSocket mySocket;
    public Socket socket;
    PieChart pie = new PieChart();

    public MainServer() {
        try {
            DataAccessLayer.connect();
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        Image back =new Image("Data/back.jpg",700,480,false,true);
       
        BackgroundImage bImg = new BackgroundImage(back, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
       
        Background bGround = new Background(bImg);
       
        getStylesheets().add("/Style/listViewStyle.css"); 
        
        lstOnlineUsers = new ListView();
        label = new Label();
        btnStart = new Button();
        lbIp = new Label();
        lbIpAdd = new Label();

        setId("AnchorPane");
        setPrefHeight(480);
        setPrefWidth(700);

        lstOnlineUsers.setLayoutX(500);
        lstOnlineUsers.setLayoutY(55);
        lstOnlineUsers.setPrefHeight(360);
        lstOnlineUsers.setPrefWidth(170);
        lstOnlineUsers.setBackground(bGround);
        

        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        label.setLayoutX(500);
        label.setLayoutY(20.0);
        label.setPrefHeight(27.0);
        label.setPrefWidth(170);
        label.setText("Online Users");

        pie.setVisible(true);
        

        btnStart.setLayoutX(30);
        btnStart.setLayoutY(400);
        btnStart.setMnemonicParsing(false);
        btnStart.setPrefHeight(39.0);
        btnStart.setPrefWidth(122.0);
        btnStart.setText("Start Services");

        lbIp.setAlignment(javafx.geometry.Pos.CENTER);
        lbIp.setLayoutX(50);
        lbIp.setLayoutY(340);
        lbIp.setPrefHeight(17.0);
        lbIp.setPrefWidth(74.0);
        lbIp.setText("Ip Address");

        lbIpAdd.setAlignment(javafx.geometry.Pos.CENTER);
        lbIpAdd.setLayoutX(20);
        lbIpAdd.setLayoutY(370);
        lbIpAdd.setPrefHeight(27);
        lbIpAdd.setPrefWidth(130);
        lbIpAdd.setText("0.0.0.0");

        try {
            lbIpAdd.setText(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        setBackground(bGround);
        getChildren().add(pie);
        getChildren().add(lstOnlineUsers);
        getChildren().add(label);
        getChildren().add(btnStart);
        getChildren().add(lbIp);
        getChildren().add(lbIpAdd);

     
        try {
            mySocket = new ServerSocket(7001);
        } catch (IOException ex) {
            System.out.println("Address already in use:  Server is on ");
            System.exit(0);
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        socket = new Socket();
        t.start();
        t.suspend();
        onlineUserThread.start();
        onlineUserThread.suspend();

        btnStart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String txt = btnStart.getText();
                if (txt.equals("Start Services")) {
                  
                        socket = new Socket();
                        t.resume();
                        onlineUserThread.resume();
                        btnStart.setText("Stop Services");
                        Handler.clientsVector = new Vector<Handler>();
                   
                } else if (txt.equals("Stop Services")) {
                    try {
                        //mySocket.close();
                       
                        onlineUserThread.suspend();
                        t.suspend();
                        for (Handler h : Handler.clientsVector) {
                            h.threadGame.suspend();
                            h.threadUserOnline.suspend();
                            h.ps.close();
                            h.dis.close();
                            h.mySocket.close();
                          //  Handler.clientsVector.remove(h);
                        }
                        btnStart.setText("Start Services");
                    } catch (IOException ex) {
                        Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });
    }
    Thread t = new Thread() {
        public void run() {
            while (true) {
                try {
                    socket = mySocket.accept();
                    new Handler(socket);
                } catch (IOException ex) {
                    Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    Thread onlineUserThread = new Thread() {
        @Override
        public void run() {
            while (true) {
                ObservableList<String> listUsersOnline = FXCollections.observableArrayList();
                for (Handler h : Handler.clientsVector) {
                    listUsersOnline.add(h.getUsername());

                }
                int count = 0;

                try {
                    count = DataAccessLayer.getCounte();
                } catch (SQLException ex) {
                    Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                }

                int off = count - Handler.clientsVector.size();
                Platform.runLater(() -> {
                    lstOnlineUsers.setItems(listUsersOnline);
                    ObservableList<PieChart.Data> data = FXCollections.observableArrayList(new PieChart.Data("Online", Handler.clientsVector.size()), new PieChart.Data("Offline", off));
                    pie.setData(data);
                });

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    };
}
