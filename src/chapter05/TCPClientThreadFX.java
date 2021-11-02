package chapter05;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/9/14 15:38
 * @Author: 刘鼎谦-Ading
 * @Homework_des:
 */

import chapter03.TCPClient;
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
import sun.awt.windows.ThemeReader;

public class TCPClientThreadFX extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");

    private TextField tfSend = new TextField();
    private TextArea taDisplay = new TextArea();

    private TextField tfIP = new TextField("202.116.195.71");
    private TextField tfPort = new TextField();
    private Button btnConnect = new Button("连接");

    private TCPClient tcpClient;

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
        connHbox.getChildren().addAll(new Label("IP地址："), tfIP, new Label("端口："), tfPort, btnConnect);
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
        btnConnect.setOnAction(event -> {
            String ip = tfIP.getText().trim();
            String port = tfPort.getText().trim();

            try {
                //tcpClient不是局部变量，是本程序定义的一个TCPClient类型的成员变量
                tcpClient = new TCPClient(ip,port);
                //成功连接服务器，接收服务器发来的第一条欢迎信息
//                String firstMsg = tcpClient.receive();
//                taDisplay.appendText(firstMsg + "\n");
                // 启用发送按钮
                btnSend.setDisable(false);
                // 停用连接按钮
                btnConnect.setDisable(true);

//以下代码位于btnConnect.setOnActon方法中的合适位置
//用于接收服务器信息的单独线程
                receiveThread = new Thread(()->{
                    String msg = null;
                    //不知道服务器有多少回传信息，就持续不断接收
                    //由于在另外一个线程，不会阻塞主线程的正常运行
                    while ((msg = tcpClient.receive()) != null) {
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
                taDisplay.appendText("服务器连接失败！" + e.getMessage() + "\n");
            }

        });
        // 编辑区,按键绑定 ——方式1
        tfSend.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String msg = tfSend.getText();
                if (event.isShiftDown()) {
                    taDisplay.appendText("客户端发送：echo:" +  msg + "\n");
                    tcpClient.send("echo:" + msg + '\n');//向服务器发送一串字符
                } else {
                    taDisplay.appendText("客户端发送：" +  msg + "\n");
                    tcpClient.send(msg + '\n');
                }
//                String receiveMsg = tcpClient.receive();//从服务器接收一行字符
//                taDisplay.appendText(receiveMsg + "\n");
//                tfSend.clear();
                if (msg.equals("bye")) {
                    btnConnect.setDisable(false);
                    btnSend.setDisable(true);
                }
            }
        });
        btnExit.setOnAction(event -> {
            if(tcpClient != null){
                //向服务器发送关闭连接的约定信息
                tcpClient.send("bye");
                tcpClient.close();
            }
            System.exit(0);
        });

        btnSend.setOnAction(event -> {
            String sendMsg = tfSend.getText();
            tcpClient.send(sendMsg);//向服务器发送一串字符
            taDisplay.appendText("客户端发送：" + sendMsg + "\n");
//            String receiveMsg = tcpClient.receive();//从服务器接收一行字符
//            taDisplay.appendText(receiveMsg + "\n");
//            tfSend.clear();
            // 发送bye后重新启用连接按钮，禁用发送按钮
            if (sendMsg.equals("bye")) {
                btnConnect.setDisable(false);
                btnSend.setDisable(true);
            }
        });

        // 未连接时禁用发送按钮
        btnSend.setDisable(true);
        hBox.getChildren().addAll(btnSend, btnExit);
        mainPane.setBottom(hBox);
        // 响应窗体关闭
        primaryStage.setOnCloseRequest(event -> {
            if(tcpClient != null){
                //向服务器发送关闭连接的约定信息
                tcpClient.send("bye");
                tcpClient.close();
            }
            System.exit(0);
        });
        Scene scene = new Scene(mainPane, 700, 400);



        primaryStage.setScene(scene);
        primaryStage.show();
    }
}