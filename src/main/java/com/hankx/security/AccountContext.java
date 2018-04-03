package com.hankx.security;

import com.hankx.security.model.AccountData;
import com.hankx.security.model.AccountInfo;

import java.util.List;

public class AccountContext {

    private static final AccountContext INSTANCE = new AccountContext();

    private AccountContext (){}
    public static final AccountContext getInstance() {
        return INSTANCE;
    }

    private AccountData accountData;

    public AccountData getAccountData() {
        return accountData;
    }

    public void setAccountData(AccountData accountData) {
        this.accountData = accountData;
    }

    public String getVerifyInfo(){
        if(accountData == null){
            throw new IllegalArgumentException("数据文件加载错误");
        }
        return accountData.getVerifyInfo();
    }
}
