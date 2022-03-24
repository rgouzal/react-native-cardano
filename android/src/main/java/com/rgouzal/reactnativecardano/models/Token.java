package com.rgouzal.reactnativecardano.models;

import com.bloxbean.cardano.client.metadata.Metadata;
import com.bloxbean.cardano.client.transaction.spec.MultiAsset;
import com.bloxbean.cardano.client.transaction.spec.Policy;

public class Token {
    private String assetTokenId;
    private String assetName;
    private Metadata metadata;
    private String policyId;
    private Policy policy;
    private MultiAsset assets = new MultiAsset();

    public String getAssetTokenId() {
        return assetTokenId;
    }

    public void setAssetTokenId(String assetTokenId) {
        this.assetTokenId = assetTokenId;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public MultiAsset getMultiAsset() {
        return assets;
    }

    public void setMultiAsset(MultiAsset assets) {
        this.assets = assets;
    }
}
