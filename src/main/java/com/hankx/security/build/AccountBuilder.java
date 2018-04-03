package com.hankx.security.build;

import com.hankx.security.model.AccountInfo;

public class AccountBuilder {

        private AccountInfo targetAccount;

        public AccountBuilder(){
            this.targetAccount = new AccountInfo();
        }

    public AccountInfo build() {
        return targetAccount;
    }

    public AccountBuilder loginSite(String loginSite) {
            targetAccount.setLoginSite(loginSite);
            return this;
        }

        public AccountBuilder accountId(String accountId) {
            targetAccount.setAccountId(accountId);
            return this;
        }

        public AccountBuilder password(String password) {
            targetAccount.setPassword(password);
            return this;
        }

        public AccountBuilder pwdStrengthLevel(String pwdStrengthLevel) {
            targetAccount.setPwdStrengthLevel(pwdStrengthLevel);
            return this;
        }

        public AccountBuilder remark(String remark) {
            targetAccount.setRemark(remark);
            return this;
        }
    }