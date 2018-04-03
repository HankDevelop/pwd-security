package com.hankx.security.controller;

import com.hankx.security.AccountContext;
import com.hankx.security.PwdManageApplication;
import com.hankx.security.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    /*-------------登录面板  begin-------------------*/
    @FXML
    public TextField inputPwd;
    @FXML
    public Button verifyPwd;
    @FXML
    public Label errorMsg;
    /*-------------登录面板  end-------------------*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 登录页 文本输入变更事件监控
        inputPwd.textProperty().addListener(observable -> {
            handleLoginMsgTip();
        });

        verifyPwd.setOnAction(event -> {
            handleLogin(event);
        });
    }

    /**
     * 登录页错误消息显示控制
     */
    protected void handleLoginMsgTip() {
        if (errorMsg.isVisible()) {
            errorMsg.setVisible(false);
        }
    }

    /**
     * 登录控制
     *
     * @param event
     */
    protected void handleLogin(ActionEvent event) {
        String iptPwd = inputPwd.getText().trim();
        if (StringUtils.equals(AccountContext.getInstance().getVerifyInfo().trim(), iptPwd)) {
            PwdManageApplication.instance.gotomain();
        } else {
            // 重新输入口令
            errorMsg.setText("口令错误，请重新输入！");
            errorMsg.setVisible(true);
        }
    }

}
