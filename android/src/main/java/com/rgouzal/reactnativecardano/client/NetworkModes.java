package com.rgouzal.reactnativecardano.client;

import com.bloxbean.cardano.client.common.model.Network;
import com.bloxbean.cardano.client.common.model.Networks;

public enum NetworkModes {
    TESTNET("testnet"), MAINNET("mainnet");

    public final String mode;

    NetworkModes(String mode) {
        this.mode = mode;
    }
}
