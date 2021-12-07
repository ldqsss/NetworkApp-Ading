package chapter11;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-22 19:14
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jpcap.JpcapCaptor;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;

import java.io.UnsupportedEncodingException;

public class PacketCaptureFX extends Application {
    private TextArea taDisplay = new TextArea();

    private Button btnStart = new Button("开始抓包");
    private Button btnStop = new Button("停止抓包");
    private Button btnClear = new Button("清空");
    private Button btnSetting = new Button("设置");
    private Button btnExit = new Button("退出");

    private ConfigDialog configDialog;
    private JpcapCaptor jpcapCaptor;


    private void interrupt(String threadName){
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        for(int i = 0 ;i < noThreads; i++){
            if(lstThreads[i].getName().equals(threadName)){
                lstThreads[i].interrupt();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();

        VBox display = new VBox();
        display.setSpacing(10);
        display.setPadding(new Insets(10,20,10,20));
        taDisplay.setWrapText(true);
        taDisplay.setEditable(false);
        taDisplay.setPrefHeight(250.9);
        display.getChildren().addAll(new Label("抓包INFO: "), taDisplay);

        VBox.setVgrow(taDisplay, Priority.ALWAYS);
        mainPane.setCenter(display);

        btnSetting.setOnAction(event -> {
            if (configDialog == null) {
                configDialog = new ConfigDialog(primaryStage);
            }
            configDialog.showAndWait();
            jpcapCaptor = configDialog.getJpcapCaptor();
        });

        btnStart.setOnAction(e->{
            if(jpcapCaptor==null){
                if(configDialog==null){
                    configDialog = new ConfigDialog(primaryStage);
                }
                configDialog.showAndWait();
                jpcapCaptor=configDialog.getJpcapCaptor();
            }
            interrupt("captureThread");
            new Thread(()->{
               while (true){
                   if(Thread.currentThread().isInterrupted()){
                       break;
                   }
                   jpcapCaptor.processPacket(1, new PacketHandler());
               }
            }).start();
        });

        btnStop.setOnAction(event -> {
            interrupt("captureThread");
        });

        btnClear.setOnAction(event -> {
            taDisplay.clear();
        });

        btnExit.setOnAction(event -> {
            exit();
        });

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(10, 20, 10, 20));
        buttons.getChildren().addAll(btnStart, btnStop, btnClear, btnSetting, btnExit);
        mainPane.setBottom(buttons);

        Scene scene = new Scene(mainPane, 700, 400);
        primaryStage.setOnCloseRequest(event -> {
            exit();
        });

        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void exit() {
        interrupt("captureThread");
        System.exit(0);
    }
    class PacketHandler implements PacketReceiver {
        @Override
        public void receivePacket(Packet packet) {
            String keyData = null;
            String packetData;
            //分析包中是否包含需要显示的数据
            try {
                packetData = new String(packet.data,0,packet.data.length,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (keyData == null || keyData.trim().equalsIgnoreCase(""))
                return;

//将keyData按空格切分出包含多个关键词的字符串数组
//提示，split方法也可以使用正则表达式，\s+表示匹配一个或多个空白
                String[] keyList = keyData.split("s+");
                try {
                    String msg = new String(packet.data, 0, packet.data.length, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                taDisplay.appendText(packet.toString() + "\n");
            });
        }
    }
}
