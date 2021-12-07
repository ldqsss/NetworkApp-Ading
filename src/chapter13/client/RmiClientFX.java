package chapter13.client;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-12-05 16:10
 * @Author: 刘鼎谦-Ading
 * @file_desc:
 *//*
@author: Ading
@file: RmiClientFX
@time: 2021-12-05 16:10
@myBlog:blog.csdn.net/m0_46156900
*/

import chapter13.rmi.ClientService;
import chapter13.rmi.ServerService;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClientFX extends Application {
    // buttons
    private Button btnExit = new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnLogin = new Button("登录");

    // textField
    private TextField tfNO = new TextField("2019100");
    private TextField tfName = new TextField("刘");
    private TextField tfMsg = new TextField("Hi! I'am Ading");

    // textArea
    private TextArea taDisplay = new TextArea();

    //serverIF and clientIF
    private ServerService serverService;
    private ClientService clientService;
    private String client; //学号-姓名的格式

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initComponents(primaryStage);
        new Thread(()->{
            initRmi();
        }).start();
        initEvent(primaryStage);

    }
    private void initComponents(Stage primaryStage){
        HBox hBoxLogin = new HBox();
        hBoxLogin.setSpacing(10);
        hBoxLogin.setPadding(new Insets(10,20,10,20));
        hBoxLogin.getChildren().addAll(new Label("StuNO: "),
                tfNO, new Label("StuName: "), tfName, btnLogin);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.getChildren().addAll(btnSend, btnExit);


        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.getChildren().addAll(hBoxLogin, new Label("INFO Display: "),
                taDisplay,new Label("INFO Input: "), tfMsg, hBox);


        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //初始化rmi操作
    private void initRmi()
    {
        try
        {
//            String ip = "202.116.195.71";
////为了不和上一讲端口冲突，临时修改为8008，一般不做特别说明，是使用1099
//            int port = 8008;
            String ip = "192.168.235.14";
            int port = 8008;
            //获取RMI注册器
            Registry registry = LocateRegistry.getRegistry(ip, port);
            for (String name : registry.list())
            {
                System.out.println(name);
            }

            //客户端(调用端)到注册器中使用助记符寻找并创建远程服务对象的客户端(调用端)stub,
            // 之后本地调用serverService的方法，实质就是调用了远程同名接口下的同名方法
            serverService = (ServerService) registry.lookup("ServerService");
            //实例化本地客户端的远程对象
            clientService = new ClientServiceImpl(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void appendMsg(String msg){
        taDisplay.appendText(msg.trim() + "\n");
    }

    private void initEvent(Stage primaryStage){
        // login , join the online groups ,The format is student ID - name
        btnLogin.setOnAction(event -> {
            try {
                {
                    String NO = tfNO.getText().trim();
                    String name = tfName.getText().trim();
                    if (!NO.equals("") && !name.equals("")){
                        client = NO +'-'+name;
                        String retStr = serverService.addClientToOnlineGroup(
                                client, clientService);
                        taDisplay.appendText(retStr + '\n');
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        // send
        btnSend.setOnAction(event -> {
            String sendMsg = tfMsg.getText();

            // invoke 服务端的远程服务，群发消息
            try {
                serverService.sendPublicMsgToServer(client, sendMsg);
                appendMsg(sendMsg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        btnExit.setOnAction(event -> {
            exit();
        });
        primaryStage.setOnCloseRequest(event ->
        {
            //调用退出方法
            exit();
        });
    }
    private void exit(){
        try{
            serverService.removeClientFromOnlineGroup(client, clientService);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        finally {
            System.exit(0);
        }
    }
}
