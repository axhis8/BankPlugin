package com.axhislmc.bankPlugin.utils;

public enum BankPermission {
    BANK_ADMIN("bank.admin"),
    BANK_USE("bank.use"),
    BANK_PAY("bank.pay"),
    ;

    private final String permission;

    BankPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }
}