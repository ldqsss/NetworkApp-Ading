package chapter01;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleFX extends Application {

    private Button btnExit =new Button("退出");
    private Button btnSend = new Button("发送");
    private Button btnOpen  = new Button("加载");
    private Button btnSave =new Button("保存");

    private TextField tfSend = new TextField();
    private TextArea taDisplay = new TextArea();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setSpacing(10);//各控件之间的间隔
        //VBox面板中的内容距离四周的留空区域
        vBox.setPadding(new Insets(10,20,10,20));
        vBox.getChildren().addAll(new Label("信息显示区："),
                taDisplay,new Label("信息输入区："), tfSend);

        VBox.setVgrow(taDisplay, Priority.ALWAYS);
        mainPane.setCenter(vBox);
        //底部按钮区域
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnSend,btnSave,btnOpen,btnExit);
        mainPane.setBottom(hBox);
        Scene scene = new Scene(mainPane,700,400);

        primaryStage.setScene(scene);
        primaryStage.show();
        btnExit.setOnAction(event -> {System.exit(0);});
        btnSend.setOnAction(event -> {
            String msg = tfSend.getText();
            taDisplay.appendText(msg+'\n');
            tfSend.clear();
        });
        tfSend.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
//                if (event.getCode() == KeyCode.SHIFT,KeyCode.ENTER){

//                }
                if (event.getCode()== KeyCode.ENTER)
                {
                    String msg = tfSend.getText();
                    if (event.isShiftDown()) {
                        taDisplay.appendText("echo:"+msg+'\n');
                    }
//                    else if (event.isControlDown()){  //todo control+enter tfSend area 换行
//                        tfSend.
//                    }
                    else {
                        taDisplay.appendText(msg + '\n');
                    }
                    tfSend.clear();
                }
            }
        });
    }
}
