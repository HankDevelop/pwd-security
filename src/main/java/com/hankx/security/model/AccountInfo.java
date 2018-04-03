package com.hankx.security.model;

import com.google.common.base.Objects;

public class AccountInfo {

    private String loginSite;
    private String accountId;
    private String password;
    private String pwdStrengthLevel;
    private String remark;

    public String getLoginSite() {
        return loginSite;
    }

    public void setLoginSite(String loginSite) {
        this.loginSite = loginSite;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPwdStrengthLevel() {
        return pwdStrengthLevel;
    }

    public void setPwdStrengthLevel(String pwdStrengthLevel) {
        this.pwdStrengthLevel = pwdStrengthLevel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("loginSite", loginSite)
                .add("accountId", accountId)
                .add("password", password)
                .add("pwdStrengthLevel", pwdStrengthLevel)
                .add("remark", remark)
                .toString();
    }

}
