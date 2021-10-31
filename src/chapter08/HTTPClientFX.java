package chapter08;
/*
@author: Ading
@file: HTTPClientFX
@time: 2021-10-30 23:45
@myBlog:blog.csdn.net/m0_46156900
*/

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class HTTPClientFX extends Application {
    private TextField tfWebAddr = new TextField("www.baidu.com");
    private TextField tfPort = new TextField("80");
    private TextArea taRespInfo = new TextArea();
    private Button btnConn = new Button("连接");
    private Button btnWebRequ = new Button("请求网页");
    private Button btnClear = new Button("清空");
    private Button btnExit = new Button("退出");
    private HTTPClient httpClient;
    Thread receiveThread;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 布局
        BorderPane mainBorder = new BorderPane();
        HBox hbAddrPort = new HBox();
        HBox hbReqClearExit = new HBox();
        hbAddrPort.getChildren().addAll(new Label("Website:"),tfWebAddr,new Label("Port:"),tfPort, btnConn);
        hbAddrPort.setAlignment(Pos.CENTER);
        hbAddrPort.setSpacing(10);
        hbAddrPort.setPadding(new Insets(15));

        hbReqClearExit.getChildren().addAll(btnWebRequ,btnClear,btnExit);
        hbReqClearExit.setAlignment(Pos.CENTER_RIGHT);
        hbReqClearExit.setSpacing(10);
        hbReqClearExit.setPadding(new Insets(10));

        VBox vBox = new VBox();
        vBox.getChildren().addAll(new Label("Response INFO:"), taRespInfo);
        vBox.setSpacing(5);
        VBox.setVgrow(taRespInfo, Priority.ALWAYS);
        VBox.setMargin(taRespInfo,new Insets(0,10,10,15));
        taRespInfo.setWrapText(true);
        taRespInfo.setEditable(false);
        mainBorder.setTop(hbAddrPort);
        mainBorder.setBottom(hbReqClearExit);
        mainBorder.setCenter(vBox);

        Scene scene = new Scene(mainBorder,800,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("HTTP Client FX");
        primaryStage.show();

        btnWebRequ.setDisable(true);

        btnConn.setOnAction(event -> {
            try {
                httpClient = new HTTPClient(tfWebAddr.getText().trim(),tfPort.getText().trim());
                btnConn.setDisable(true);
                btnWebRequ.setDisable(false);
                taRespInfo.setText("Server Connected");
                receiveThread = new Thread(()->{
                   String msg;
                   while ((msg = httpClient.receive())!=null){
                       String tmp=msg;
                       Platform.runLater(()->{
                           taRespInfo.appendText(tmp+"\n");
                       });
                   }
                   Platform.runLater(()->{
                       taRespInfo.appendText("Connection closed");
                       btnConn.setDisable(false);
                   });
                },"myThread");
                receiveThread.start();
            } catch (IOException e) {
                taRespInfo.setText("Failed to connect to the server");
            }

        });
        btnWebRequ.setOnAction(e->{
            String requMsg = "GET / HTTP/1.1\r\n" +
                    "HOST: "+tfWebAddr.getText().trim()+"\r\n"+
                    "Accept: */*\r\n" +
                    "Accept-Language: zh-cn\r\n" +
                    "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.54 Safari/537.36\r\n" +
                    "Upgrade-Insecure-Requests: 1\r\n"+
                    "Connection: Keep-Alive \r\n\r\n";
            taRespInfo.setText(requMsg);
            btnWebRequ.setDisable(true);
            httpClient.send(requMsg);
            //btnWebRequ.setDisable(false);
        });
        btnClear.setOnAction(event->{
            taRespInfo.clear();
        });
        btnExit.setOnAction(event -> {
            if(httpClient!=null){
                httpClient.send("Connection:close" +"\r\n");
                httpClient.close();
                taRespInfo.appendText("Closing the tcp connection...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                primaryStage.close();

            }
            else {
                primaryStage.close();
            }
        });
    }
}
