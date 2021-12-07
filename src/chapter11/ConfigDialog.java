package chapter11;/*
 * @project: NetworkApp-Ading
 * @Created-Time: 2021-11-22 19:12
 * @Author: 刘鼎谦-Ading
 * @file_desc:
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class ConfigDialog {
    private JpcapCaptor jpcapCaptor;  //用于返回给主窗体
    //网卡列表
    private NetworkInterface[] devices = JpcapCaptor.getDeviceList();
    private Stage stage = new Stage();//对话框窗体

    //parentStage表示抓包主程序(PacketCaptureFX)的stage，传值可通过这种构造方法参数的方式
    private String keyData= null;

    public String getKeyData() {
        return keyData;
    }

    private TextField tfKeywords = new TextField();
    public ConfigDialog(Stage parentStage) {
        //设置该对话框的父窗体为调用者的那个窗体
        stage.initOwner(parentStage);
        //设置为模态窗体，即不关闭就不能切换焦点
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setResizable(false);
        stage.setTitle("选择网卡并设置参数");

        //窗体主容器
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 20, 10, 20));

//网卡选择列表，使用组合下拉框控件
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setMaxWidth(800);
        for (int i = 0; i < devices.length; i++) {
            comboBox.getItems().add(i + " :  " + devices[i].description);
        }
        comboBox.getSelectionModel().selectFirst();  //默认选择第一项

        TextField tfFilter = new TextField();  //设置抓包过滤
        TextField tfSize = new TextField("1514");    //设置抓包大小（一般建议在68-1514之间，默认1514）
        CheckBox checkBox = new CheckBox("是否设置为混杂模式");   //是否设置混杂模式
        checkBox.setSelected(true);   //默认选中
        //底部确定和取消按钮

        HBox hBoxBottom = new HBox();
        hBoxBottom.setAlignment(Pos.CENTER_RIGHT);

        Button btnConfirm = new Button("确定");
        Button btnCancel = new Button("取消");
        hBoxBottom.getChildren().addAll(btnConfirm, btnCancel);

        vBox.getChildren().addAll(new Label("请选择网卡："), comboBox,
                new Label("过滤"), tfFilter,
                new Label("设置抓包大小（建议介于68~1514之间）："), tfSize, checkBox,
                new Separator(), hBoxBottom);
        Scene scene = new Scene(vBox);
        stage.setScene(scene);

        btnConfirm.setOnAction(event -> {
            try {
                int index = comboBox.getSelectionModel().getSelectedIndex();
                //选择的网卡接口
                NetworkInterface networkInterface = devices[index];
                //抓包大小
                int snapLen = Integer.parseInt(tfSize.getText().trim());
                //是否混杂模式
                boolean promisc = checkBox.isSelected();
                jpcapCaptor = JpcapCaptor.openDevice(networkInterface, snapLen,
                        promisc, 20);
                jpcapCaptor.setFilter(tfFilter.getText().trim(), true);
                keyData = tfKeywords.getText();
                stage.hide();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        });

        //取消按钮动作事件
        btnCancel.setOnAction(event -> {
            stage.hide();
        });
    }

    //主程序调用，获取设置了参数的JpcapCaptor对象
    public JpcapCaptor getJpcapCaptor() {
        return jpcapCaptor;
    }

    //主程序调用，阻塞式显示界面
    public void showAndWait() {
        stage.showAndWait();
    }
}
