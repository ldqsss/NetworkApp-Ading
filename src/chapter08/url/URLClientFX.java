package chapter08.url;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-10-31 11:06
 * @Author: 刘鼎谦-Ading
 * @file_desc:
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class URLClientFX extends Application {
    private TextArea taRespInfo = new TextArea();
    private TextField urlInput = new TextField();
    private Button btnWebRequ = new Button("请求网页");
    private Button btnExit = new Button("退出");
    private URL url;
    Thread receiveThread;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 布局
        BorderPane mainBorder = new BorderPane();
        HBox hbReqExit = new HBox();
        hbReqExit.getChildren().addAll(btnWebRequ,btnExit);
        hbReqExit.setAlignment(Pos.CENTER_RIGHT);
        hbReqExit.setSpacing(10);
        hbReqExit.setPadding(new Insets(10));
        VBox vBox = new VBox();
        vBox.getChildren().addAll(new Label("Response INFO:"), taRespInfo, new Label("Input URL"),urlInput);
        vBox.setSpacing(5);
        VBox.setVgrow(taRespInfo, Priority.ALWAYS);
        VBox.setMargin(taRespInfo,new Insets(0,10,10,15));
        taRespInfo.setWrapText(true);
        taRespInfo.setEditable(false);
        mainBorder.setBottom(hbReqExit);
        mainBorder.setCenter(vBox);

        Scene scene = new Scene(mainBorder,800,600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("URL Client ");
        primaryStage.show();


        btnWebRequ.setOnAction(e-> {
            String addr = urlInput.getText().trim();
            if (isValidAddr(addr)){
                try {
                    taRespInfo.clear();
                    url = new URL(addr);
                    showURL(url);
                    InputStream ins = url.openStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(ins, "utf-8"));
                    receiveThread = new Thread(() -> {
                        try {
                            String msg;
                            while ((msg = br.readLine()) != null) {
                                String tmp = msg;
                                Platform.runLater(() -> {
                                    taRespInfo.appendText(tmp + '\n');
                                });
                            }
                        } catch (IOException ioException) {
                            System.out.println("hh");
                        }

                    });
                    receiveThread.start();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else{
                taRespInfo.setText("URL 格式错误");
            }

        });

        btnExit.setOnAction(event -> {
            taRespInfo.setText("Closing the connection...");
            if(url !=null){
                receiveThread.stop();
            }
            primaryStage.close();
        });
    }
    public boolean isValidAddr(String addr){
        if (addr.matches("^(https?|ftp|file)://.*"))
            return true;
        return false;
    }

    public void showURL(URL url){
        System.out.println("URL 为：" + url.toString());
        System.out.println("协议为：" + url.getProtocol());
        System.out.println("验证信息：" + url.getAuthority());
        System.out.println("文件名及请求参数：" + url.getFile());
        System.out.println("主机名：" + url.getHost());
        System.out.println("路径：" + url.getPath());
        System.out.println("端口：" + url.getPort());
        System.out.println("默认端口：" + url.getDefaultPort());
        System.out.println("请求参数：" + url.getQuery());
        System.out.println("定位位置：" + url.getRef());
    }
}
