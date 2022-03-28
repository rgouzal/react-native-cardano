package com.rgouzal.reactnativecardano.client;

import com.bloxbean.cardano.client.common.model.Network;
import com.bloxbean.cardano.client.common.model.Networks;

public enum NetworkModes {
    TESTNET("testnet", "https://cardano-testnet.blockfrost.io/api/v0/"),
    MAINNET("mainnet", "https://cardano-mainnet.blockfrost.io/api/v0/");

    public final String mode;
    public final String url;

    NetworkModes(String mode, String url) {
        this.mode = mode;
        this.url = url;
    }

    public static NetworkModes getByValue(String val) {
        return val.equalsIgnoreCase(NetworkModes.TESTNET.mode)?
                NetworkModes.TESTNET : NetworkModes.MAINNET;
    }
}
