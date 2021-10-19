package chapter06.client;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/9/14 15:38
 * @Author: 刘鼎谦-Ading
 * @Homework_des:
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UDPClientFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");

    private TextField tfSend = new TextField();
    private TextArea taDisplay = new TextArea();

    private TextField tfIP = new TextField("127.0.0.1");
    private TextField tfPort = new TextField("8008");
    private Button btnInitialize = new Button("初始化");   // 代码是从TCPClientFX复制来的，原来叫做btnConnect 但是，udp是无连接的

    private UDPClient udpClient;

    Thread receiveThread; //定义成员变量，读取服务器信息的线程


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();

        HBox connHbox = new HBox();
        connHbox.setAlignment(Pos.CENTER);
        connHbox.setSpacing(10);
        connHbox.getChildren().addAll(new Label("IP地址："), tfIP, new Label("端口："), tfPort, btnInitialize);
        mainPane.setTop(connHbox);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 20, 10, 20));
        // 设置发送信息的文本框
        // 自动换行
        taDisplay.setWrapText(true);
        // 只读
        taDisplay.setEditable(false);
        vBox.getChildren().addAll(new Label("信息显示区： "), taDisplay, new Label("信息输入区："), tfSend);
        VBox.setVgrow(taDisplay, Priority.ALWAYS);
        mainPane.setCenter(vBox);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 20, 10, 20));
        hBox.setAlignment(Pos.CENTER_RIGHT);

        // 按钮事件绑定
        btnInitialize.setOnAction(event -> {
            String ip = tfIP.getText().trim();
            String port = tfPort.getText().trim();

            try {
                udpClient = new UDPClient(ip,port);   // 初始化，创建udp对象
                // 启用发送按钮
                btnSend.setDisable(false);
                // 停用连接按钮
                btnInitialize.setDisable(true);
                receiveThread = new Thread(()->{     // 创建线程，接受信息
                    String msg = null;
                    //不知道服务器有多少回传信息，就持续不断接收
                    //由于在另外一个线程，不会阻塞主线程的正常运行
                    while ((msg = udpClient.receive()) != null) {
                        //runLater中的lambda表达式不能直接访问外部非final类型局部变量
                        //所以这里使用了一个临时变量
                        String msgTemp = msg;
                        Platform.runLater(()->{
                            taDisplay.appendText( msgTemp + "\n");
                        });
                    }
//跳出了循环，说明服务器已关闭，读取为null，提示对话关闭
                    Platform.runLater(()->{
                        taDisplay.appendText("对话已关闭！\n" );
                    });
                },"my_reveiveThread");
                receiveThread.start(); //启动线程
            } catch (Exception e) {
                taDisplay.appendText("UDP客户端初始化失败！" + e.getMessage() + "\n");
            }

        });
        // 编辑区,按键绑定 ——方式1
        tfSend.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String msg = tfSend.getText();
                if (event.isShiftDown()) {
                    taDisplay.appendText("UDP客户端发送：echo:" +  msg + "\n");
                    udpClient.send("echo:" + msg + '\n');//向服务器发送一串字符
                } else {
                    taDisplay.appendText("UDP客户端发送：" +  msg + "\n");
                    udpClient.send(msg + '\n');
                }
                tfSend.clear();

//                String receiveMsg = udpClient.receive();   // udp 无连接，没有所谓的等待接受信息
//                taDisplay.appendText(receiveMsg + "\n");
//                if (msg.equals("bye")) {
//                    btnInitialize.setDisable(false);
//                    btnSend.setDisable(true);
//                }
            }
        });
        btnExit.setOnAction(event -> {
            if(udpClient != null){
                //向服务器发送关闭连接的约定信息
                udpClient.send("Good bye, although I know we were not connected ever");
            }
            System.exit(0);
        });
        btnSend.setOnAction(event -> {
            String sendMsg = tfSend.getText();
            udpClient.send(sendMsg);//向服务器发送一串字符
            taDisplay.appendText("UDP客户端发送：" + sendMsg + "\n");
//            String receiveMsg = udpClient.receive(); // udp 无连接，没有所谓的等待接受信息
//            taDisplay.appendText(receiveMsg + "\n");
            tfSend.clear();
            // 发送bye后重新启用连接按钮，禁用发送按钮
//            if (sendMsg.equals("bye")) {
//                btnInitialize.setDisable(false);
//                btnSend.setDisable(true);
//            }

        });

        // 未连接时禁用发送按钮
        btnSend.setDisable(true);
        hBox.getChildren().addAll(btnSend, btnExit);
        mainPane.setBottom(hBox);
        Scene scene = new Scene(mainPane, 700, 400);

        // 响应窗体关闭
        primaryStage.setOnCloseRequest(event -> {
            if(udpClient != null){
                //向服务器发送关闭连接的约定信息
                udpClient.send("Good bye, although I know we were not connected ever");
            }
            System.exit(0);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}