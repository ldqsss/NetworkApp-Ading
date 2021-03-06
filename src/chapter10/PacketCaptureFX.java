package chapter10;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/11/16 16:12
 * @Author: Ading
 * @file_des:
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

public class PacketCaptureFX extends Application {

    private TextArea taDisplay = new TextArea();

    private Button btnStart = new Button("开始抓包");
    private Button btnStop = new Button("停止抓包");
    private Button btnClear = new Button("清空");
    private Button btnSetting = new Button("设置");
    private Button btnExit = new Button("退出");

    private ConfigDialog configDialog;
    private JpcapCaptor jpcapCaptor;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = new BorderPane();

        VBox display = new VBox();
        display.setSpacing(10);
        display.setPadding(new Insets(10, 20, 10, 20));
        // 自动换行
        taDisplay.setWrapText(true);
        // 只读
        taDisplay.setEditable(false);
        taDisplay.setPrefHeight(250);
        display.getChildren().addAll(new Label("抓包信息："), taDisplay);
        VBox.setVgrow(taDisplay, Priority.ALWAYS);
        mainPane.setCenter(display);

        btnSetting.setOnAction(event -> {
            if (configDialog == null) {
                configDialog = new ConfigDialog(primaryStage);
            }
            configDialog.showAndWait();
            jpcapCaptor = configDialog.getJpcapCaptor();
        });

        btnStart.setOnAction(event -> {
            if (jpcapCaptor == null) {
                if (configDialog == null) {
                    configDialog = new ConfigDialog(primaryStage);
                }
                configDialog.showAndWait();
                jpcapCaptor = configDialog.getJpcapCaptor();
            }
            interrupt("captureThread"); //如果已经存在抓包的线程，则先停止该线程；
            new Thread(() -> {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) { //如果声明了本线程被中断，则退出循环
                        break;
                    }

                    //每次抓一个包，交给内部类PacketHandler的实例处理
                    // PacketHandler为接口PacketReceiver的实现类
                    jpcapCaptor.processPacket(1, new PacketHandler());
                }
            }, "captureThread").start();   //开启一个命名为"captureThread"的新线程来进行抓包的操作；
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

    class PacketHandler implements PacketReceiver {
        @Override
        public void receivePacket(Packet packet) {
            Platform.runLater(() -> {
                taDisplay.appendText(packet.toString() + "\n");
            });
        }
    }

    private void interrupt(String threadName) {
        /**
         循环遍历指定线程名的线程列表，并声明关闭，需要关闭的线程需要在构造时指定线程名，作为参数传入
         */
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup(); //获取当前线程的线程组及其子线程组中活动线程数量
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);  //将活动线程复制到线程数组
        for (int i = 0; i < noThreads; i++) { //遍历这些活动线程，符合指定线程名的则声明关闭
            if (lstThreads[i].getName().equals(threadName)) {
                lstThreads[i].interrupt();
            }
        }
    }

    private void exit() {
        interrupt("captureThread");
        System.exit(0);
    }
}
