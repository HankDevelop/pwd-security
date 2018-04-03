package com.hankx.security.model;

import java.util.List;

public class AccountData {

    private String verifyInfo;

    private List<AccountInfo> accountList;

    public String getVerifyInfo() {
        return verifyInfo;
    }

    public void setVerifyInfo(String verifyInfo) {
        this.verifyInfo = verifyInfo;
    }

    public List<AccountInfo> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountInfo> accountList) {
        this.accountList = accountList;
    }
}
