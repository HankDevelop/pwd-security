package com.hankx.security;

import com.alibaba.fastjson.JSON;
import com.hankx.security.model.AccountData;
import com.hankx.security.utils.GuiUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

public class PwdManageApplication extends Application {

    private static final Logger logger = LogManager.getLogger(PwdManageApplication.class);

    public static final String APP_NAME = "密码安全管家";
    public Stage mainWindow;
    public static PwdManageApplication instance;

    private static File dataFile = null;
    private static final String defaultFilePath = "data/accountInfo.data";

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            realStart(primaryStage);
        } catch (Throwable e) {
            GuiUtils.msgAlert(e);
            throw e;
        }
    }

    private void realStart(Stage mainWindow) throws IOException {
        this.mainWindow = mainWindow;
        instance = this;
        // Show the dialog for any exceptions that we don't handle and that hit the main loop.
        GuiUtils.handleExceptionsOnThisThread();
        gotologin();

        mainWindow.centerOnScreen();
        mainWindow.show();
    }

    public void gotologin() {
        try {
            if(dataFile == null || !dataFile.exists()){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("打开账户信息数据文件");
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files (*.*)", "*.*");
                fileChooser.getExtensionFilters().add(extFilter);
                dataFile = fileChooser.showOpenDialog(PwdManageApplication.instance.mainWindow);
            }
            if(dataFile != null) {
                AccountData accountData = JSON.parseObject(FileUtils.readFileToString(dataFile, Charset.defaultCharset()), AccountData.class);
                AccountContext.getInstance().setAccountData(accountData);
            } else {
                throw new IllegalArgumentException("账户信息数据文件无法加载");
            }
            replaceSceneContent("pwd-login.fxml");
        } catch (Exception e) {
            GuiUtils.msgAlert(e);
            logger.debug("加载登录页面失败", e);
        }
    }

    public void gotomain() {
        try {
            replaceSceneContent("pwd-safe.fxml");
        } catch (Exception e) {
            gotologin();
            GuiUtils.msgAlert(e);
            logger.debug("加载应用主页面失败", e);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = PwdManageApplication.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(PwdManageApplication.class.getResource(fxml));
        AnchorPane page;
        try {
            page = loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page, 600, 350);
        mainWindow.setScene(scene);
        mainWindow.setTitle(APP_NAME);
        mainWindow.sizeToScene();
        return (Initializable) loader.getController();
    }

    public static File getDataFile() {
        return dataFile;
    }

    public static void main(String[] args) {
        dataFile = new File(defaultFilePath);
        launch(args);
        Properties props = System.getProperties();
        for (String prop : props.stringPropertyNames() ) {
            System.out.println(prop+"\t" + props.get(prop));
        }

    }
}
