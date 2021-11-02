package chapter07;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021/9/14 15:38
 * @Author: 刘鼎谦-Ading
 * @Homework_des:
 */

import javafx.application.Application;
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

public class TCPMailClientFX1 extends Application {
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");

    private TextArea tfSend = new TextArea("test");
    private TextField tfStatus = new TextField();
    private TextField tfSmtpIP = new TextField("smtp.qq.com");
    private TextField tfSmtpPort = new TextField("25");
    private TextField mailSender = new TextField("734200940@qq.com");
    private TextField mailReceiver = new TextField("734200940@qq.com");
    private TextField mailSubj = new TextField("java mail");

    private TCPMailClient tcpMailClient;

    Thread receiveThread; //定义成员变量，读取服务器信息的线程

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        HBox mailServerHbox = new HBox();
        mailServerHbox.setAlignment(Pos.CENTER);
        mailServerHbox.setSpacing(10);
        mailServerHbox.getChildren().addAll(new Label("邮件服务器IP: "), tfSmtpIP, new Label("邮件服务器Port: "), tfSmtpPort);
        mainPane.setTop(mailServerHbox);

        HBox mailerHbox = new HBox();
        mailerHbox.setAlignment(Pos.CENTER);
        mailerHbox.setSpacing(10);
        mailerHbox.getChildren().addAll(new Label("邮件发送者邮箱:"), mailSender,new Label("邮件接收者邮箱: "),mailReceiver );

        HBox subjHbox = new HBox();
        subjHbox.setSpacing(10);
        subjHbox.setAlignment(Pos.CENTER_LEFT);
        subjHbox.getChildren().addAll(new Label("邮件主题: "), mailSubj);

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 20, 10, 20));

        VBox.setVgrow(tfSend,Priority.ALWAYS);
        vBox.getChildren().addAll(mailerHbox, subjHbox,new Label("信息输入区："), tfSend);
        mainPane.setCenter(vBox);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 20, 10, 20));
        hBox.setAlignment(Pos.CENTER_RIGHT);

        hBox.getChildren().addAll(new Label("邮件发送状态:"),tfStatus,btnSend, btnExit);
        mainPane.setBottom(hBox);
        // 编辑区,按键绑定 ——方式1
        btnSend.setDisable(false);
        tfStatus.setDisable(true);
        btnExit.setOnAction(event -> {
            if(tcpMailClient != null){
                //向服务器发送关闭连接的约定信息
                tcpMailClient.send("bye");
                tcpMailClient.close();
            }
            System.exit(0);
        });

        btnSend.setOnAction(event -> {
            String smtpIP = tfSmtpIP.getText().trim();
            String smtpPort = tfSmtpPort.getText().trim();
            String sendMsg = tfSend.getText();

            try {
                TCPMailClient tcpMailClient = new TCPMailClient(smtpIP,smtpPort);
                tcpMailClient.send("HELO It's Ading");
                tcpMailClient.send("AUTH LOGIN");
                String EncodeUsername = BASE64.encode("734200940@qq.com");
                tcpMailClient.send(EncodeUsername);
                String EncodeAuthCode = BASE64.encode("wxuhjbiaippcbbac");
                tcpMailClient.send(EncodeAuthCode);
                String msg = "MAIL FROM:<"+mailSender.getText().trim()+">";
                tcpMailClient.send(msg);
                msg = "RCPT TO:<" + mailReceiver.getText().trim() + ">";
                tcpMailClient.send(msg);
                msg = "DATA \n";
                tcpMailClient.send(msg);
                msg = "FROM:" + mailSender.getText().trim();
                tcpMailClient.send(msg);
                msg = "Subject:"+ mailSubj.getText().trim();
                tcpMailClient.send(msg);
                msg = "To:"+mailReceiver.getText().trim();
                tcpMailClient.send(msg);
                tcpMailClient.send("\n");
                msg = tfSend.getText();
                tcpMailClient.send(msg);
                tcpMailClient.send(".");
                tcpMailClient.send("QUIT");

            } catch (IOException e) {
                e.printStackTrace();
            }
            tfStatus.setText("发送成功");


        });


        // 响应窗体关闭
        primaryStage.setOnCloseRequest(event -> {
            if(tcpMailClient != null){
                //向服务器发送关闭连接的约定信息
                tcpMailClient.send("bye");
                tcpMailClient.close();
            }
            System.exit(0);
        });
        Scene scene = new Scene(mainPane, 700, 400);



        primaryStage.setScene(scene);
        primaryStage.show();
    }
}