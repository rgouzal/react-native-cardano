package com.rgouzal.reactnativecardano.models;

import com.bloxbean.cardano.client.account.Account;

public class Wallet {
    private String privateKey;
    private String baseAddress;
    private String stakeAddress;
    private String enterpriseAddress;
    private String changeAddress;
    private String network;

    public static Wallet fromAccount(Account account, String network) {
        Wallet wallet = new Wallet();
        wallet.setBaseAddress(account.baseAddress());
        wallet.setPrivateKey(account.getBech32PrivateKey());
        wallet.setStakeAddress(account.stakeAddress());
        wallet.setEnterpriseAddress(account.enterpriseAddress());
        wallet.setChangeAddress(account.changeAddress());;
        wallet.setNetwork(network);
        return wallet;
    }

    public static Wallet fromAccount(Account account) {
        Wallet wallet = new Wallet();
        wallet.setBaseAddress(account.baseAddress());
        wallet.setPrivateKey(account.getBech32PrivateKey());
        wallet.setStakeAddress(account.stakeAddress());
        wallet.setEnterpriseAddress(account.enterpriseAddress());
        wallet.setChangeAddress(account.changeAddress());;
        wallet.setNetwork("mainnet");
        return wallet;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }

    public String getStakeAddress() {
        return stakeAddress;
    }

    public void setStakeAddress(String stakeAddress) {
        this.stakeAddress = stakeAddress;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public void setChangeAddress(String changeAddress) {
        this.changeAddress = changeAddress;
    }

    public String getChangeAddress() {
        return changeAddress;
    }
}
