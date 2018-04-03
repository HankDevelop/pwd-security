package com.hankx.security.controller;

import com.alibaba.fastjson.JSON;
import com.hankx.security.AccountContext;
import com.hankx.security.PwdManageApplication;
import com.hankx.security.build.AccountBuilder;
import com.hankx.security.model.AccountData;
import com.hankx.security.model.AccountInfo;
import com.hankx.security.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class MainController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MainController.class);

    /*-------------程序主面板  begin-------------------*/
    @FXML
    public Button save;
    @FXML
    public Button archive;

    @FXML
    public ListView accountList;

    @FXML
    public TextField webSite;
    @FXML
    public ComboBox<String> accountBox;
    @FXML
    public TextField loginPwd;
    @FXML
    public ToggleButton pwdStrengthSetBtn;
    @FXML
    public Button generatePwd;
    @FXML
    public GridPane pwdStrengthSetGridPane;
    @FXML
    public NumberSpinner pwdLength;
    @FXML
    public CheckBox upperChar;
    @FXML
    public CheckBox lowerChar;
    @FXML
    public CheckBox numberic;
    @FXML
    public CheckBox spechars;
    @FXML
    public TextArea remark;
    /*-------------程序主面板  end-------------------*/

    /*-------------accountList  右键菜单 begin-------------------*/
    @FXML
    public MenuItem duplicateCurrentItem;
    @FXML
    public MenuItem editCurrentItem;
    @FXML
    public MenuItem deleteCurrentItem;
    /*-------------accountList  右键菜单 end-------------------*/

    private CopyOnWriteArraySet<String> selectedChars = new CopyOnWriteArraySet<String>();
    private ObservableList<String> accountListItems = null;
    private ConcurrentHashMap<String, AccountInfo> accountInfoMap = null;

    private void handleArchive() {
        AccountData accountData = AccountContext.getInstance().getAccountData();
        accountData.setAccountList(Arrays.asList(accountInfoMap.values().toArray(new AccountInfo[]{})));
        try {
            FileUtils.write(PwdManageApplication.getDataFile(), JSON.toJSONString(accountData), Charset.defaultCharset());
        } catch (IOException e) {
            GuiUtils.informationalAlert("","保存数据文件错误");
            return;
        }
    }


    private void handleSave() {
        try {
            String webDomain = StringUtils.getHostDomain(webSite.getText());
            String account = accountBox.getValue();
            String remarkInfo = remark.getText();
            if(StringUtils.isBlank(account)){
                GuiUtils.informationalAlert("","账户不能为空");
                return;
            }
            final String accountSign = generateAccountSign(webDomain, account);

            if (accountListItems == null){
                accountListItems = accountList.getItems();
            }

            accountInfoMap.put(accountSign, new AccountBuilder().loginSite(webDomain).accountId(account).password(loginPwd.getUserData().toString()).pwdStrengthLevel(selectedChars.toString()).remark(remark.getText()).build());
            if(accountListItems.contains(accountSign)){
                /*GuiUtils.informationalAlert("","账户"+accountSign+"已存在");
                return;*/
                logger.debug("账户：{}更新成功", accountSign);
            } else {
                accountList.setItems(null);
                accountList.setItems(FXCollections.observableArrayList(accountInfoMap.keySet()).sorted());
            }
        } catch (Exception e) {
            GuiUtils.msgAlert(e);
        }
    }

    private String generateAccountSign(String webDomain, String account) {
        if(StringUtils.isNotBlank(webDomain)){
            return account + "@" + webDomain;
        } else {
            return account;
        }
    }

    /**
     * 生成密码按钮事件
     */
    private void handleGeneratePassword() {
        String newPwd = "123456";
        try {
            if(upperChar.isSelected()){
                selectedChars.add(Constant.CHAR_CAPITAL);
            }
            if(lowerChar.isSelected()){
                selectedChars.add(Constant.CHAR_LOWER);
            }
            if(numberic.isSelected()){
                selectedChars.add(Constant.CHAR_NUMBERIC);
            }
            if(spechars.isSelected()){
                selectedChars.add(Constant.CHAR_SPECHARS);
            }
            newPwd = PasswordHelper.generatePassword(pwdLength.getNumber().intValue(), selectedChars.toArray(new String[]{}));
            logger.debug("生成新随机字符串{}", newPwd);
        } catch (Exception e){
            logger.error("生成随机字符串错误：",e);
        }
        loginPwd.setUserData(newPwd);
        loginPwd.setText(StringUtils.getEcho(newPwd));
    }

    /**
     * 密码强度设置面板显示控制
     */
    protected void handleStrengthSet() {
        String userData = pwdStrengthSetBtn.getUserData().toString();
        switch (userData) {
            case "show":
                pwdStrengthSetGridPane.setVisible(true);
                pwdStrengthSetGridPane.setDisable(false);
                pwdStrengthSetBtn.setUserData("hide");
                break;
            case "hide":
                pwdStrengthSetGridPane.setVisible(false);
                pwdStrengthSetBtn.setUserData("show");
                break;
            default:
                pwdStrengthSetGridPane.setVisible(false);
                pwdStrengthSetBtn.setUserData("show");
                break;
        }
    }

    /**
     * 可选字符事件
     */
    private void initPwdStrengthSetPane() {
        pwdStrengthSetGridPane.setVisible(false);
        pwdLength.setNumber(new BigDecimal(6));
        upperChar.selectedProperty().addListener(observable -> {
            if (upperChar.isSelected()) {
                selectedChars.add(Constant.CHAR_CAPITAL);
            } else {
                selectedChars.remove(Constant.CHAR_CAPITAL);
            }
        });
        lowerChar.selectedProperty().addListener(observable -> {
            if (lowerChar.isSelected()) {
                selectedChars.add(Constant.CHAR_LOWER);
            } else {
                selectedChars.remove(Constant.CHAR_LOWER);
            }
        });
        numberic.selectedProperty().addListener(observable -> {
            if (numberic.isSelected()) {
                selectedChars.add(Constant.CHAR_NUMBERIC);
            } else {
                selectedChars.remove(Constant.CHAR_NUMBERIC);
            }
        });
        spechars.selectedProperty().addListener(observable -> {
            if (spechars.isSelected()) {
                selectedChars.add(Constant.CHAR_SPECHARS);
            } else {
                selectedChars.remove(Constant.CHAR_SPECHARS);
            }
        });
    }

    /**
     * 密码输入框事件
     */
    private void initLoginPwdEventListener() {
        loginPwd.anchorProperty().addListener((observable, oldValue, newValue) -> {
            if (loginPwd.getUserData() != null) {
                loginPwd.setText(loginPwd.getUserData().toString());
            }
        });
        loginPwd.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (loginPwd.getUserData() != null) {
                    loginPwd.setText(loginPwd.getUserData().toString());
                }
            } else {
                String echo = "";
                if (loginPwd.getUserData() != null) {
                    echo = StringUtils.getEcho(loginPwd.getUserData().toString());
                }
                loginPwd.setText(echo);
            }
        });
        loginPwd.textProperty().addListener((observable, oldValue, newValue) -> {
            String userData = "";
            if (loginPwd.getUserData() != null) {
                userData = loginPwd.getUserData().toString();
            }
            if (!loginPwd.getText().equals(userData) && !newValue.contains(Constant.PASS_WORD_ECHO)) {
                loginPwd.setUserData(newValue);
                logger.debug("loginPwd set userData: {}", loginPwd.getText());
            }
        });
    }

    // 初始化账户数据信息
    private void initAccountData() {
        // 解析数据文件 初始化accountInfoMap对象
        accountInfoMap = new ConcurrentHashMap<>();

        AccountData accountData = AccountContext.getInstance().getAccountData();
        List<AccountInfo> accountInfoList = accountData.getAccountList();
        if(null != accountInfoList && !accountInfoList.isEmpty()) {
            for (AccountInfo  accountInfo : accountInfoList) {
                accountInfoMap.put(generateAccountSign(accountInfo.getLoginSite(), accountInfo.getAccountId()), accountInfo);
            }
        }

        ObservableList<String> strList = FXCollections.observableArrayList("红色","黄色","绿色");
        strList = FXCollections.observableArrayList(accountInfoMap.keySet()).sorted();
        accountList.setItems(strList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 密码输入框事件
        initLoginPwdEventListener();
        // 密码强度设置面板初始信息
        initPwdStrengthSetPane();
        // 密码强度设置面板显示控制
        pwdStrengthSetBtn.setOnAction(event -> {
            handleStrengthSet();
        });

        //生成密码事件
        generatePwd.setOnAction(event -> {
            handleGeneratePassword();
        });

        accountBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            logger.debug("登录账户数据变更: {} -> {}", oldValue, newValue);
        });

        //保存按钮事件
        save.setOnAction(event -> {
            handleSave();
        });

        // 归档按钮事件
        archive.setOnAction(event -> {
            handleArchive();
        });

        accountList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleAccountSelect();
        });

        duplicateCurrentItem.setOnAction(event -> {
            handleDuplicateCurrentItem();
        });
        editCurrentItem.setOnAction(event -> {
            handleAccountSelect();
        });
        deleteCurrentItem.setOnAction(event -> {
            handleDeleteCurrentItem();
        });

        // 显示页面时加载数据
        initAccountData();
    }

    private void handleDuplicateCurrentItem() {
    }
    private void handleDeleteCurrentItem() {
        logger.debug("select AccountInfo:{} for delete", accountList.getSelectionModel().getSelectedItem());
        accountInfoMap.remove(accountList.getSelectionModel().getSelectedItem());
        accountList.setItems(null);
        accountList.setItems(FXCollections.observableArrayList(accountInfoMap.keySet()).sorted());
    }


    protected void handleAccountSelect() {
        logger.debug("select AccountInfo:{}", accountList.getSelectionModel().getSelectedItem());
        if(null == accountList.getSelectionModel().getSelectedItem()){
            return;
        }
        AccountInfo selectAccount = accountInfoMap.get(accountList.getSelectionModel().getSelectedItem());
        webSite.setText(selectAccount.getLoginSite());
        accountBox.setValue(selectAccount.getAccountId());
        loginPwd.setText(StringUtils.getEcho(selectAccount.getPassword()));
        loginPwd.setUserData(selectAccount.getPassword());
        if(selectAccount.getPwdStrengthLevel().contains(Constant.CHAR_CAPITAL)){
            upperChar.setSelected(true);
        } else {
            upperChar.setSelected(false);
        }
        if(selectAccount.getPwdStrengthLevel().contains(Constant.CHAR_LOWER)){
            lowerChar.setSelected(true);
        } else {
            lowerChar.setSelected(false);
        }
        if(selectAccount.getPwdStrengthLevel().contains(Constant.CHAR_NUMBERIC)){
            numberic.setSelected(true);
        } else {
            numberic.setSelected(false);
        }
        if(selectAccount.getPwdStrengthLevel().contains(Constant.CHAR_SPECHARS)){
            spechars.setSelected(true);
        } else {
            spechars.setSelected(false);
        }
        remark.setText(selectAccount.getRemark());
    }
}
