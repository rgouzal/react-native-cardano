package com.rgouzal.reactnativecardano.client;

import com.bloxbean.cardano.client.account.Account;
import com.bloxbean.cardano.client.address.util.AddressUtil;
import com.bloxbean.cardano.client.backend.api.AddressService;
import com.bloxbean.cardano.client.backend.api.AssetService;
import com.bloxbean.cardano.client.backend.api.BackendService;
import com.bloxbean.cardano.client.backend.api.BlockService;
import com.bloxbean.cardano.client.backend.api.MetadataService;
import com.bloxbean.cardano.client.backend.api.NetworkInfoService;
import com.bloxbean.cardano.client.backend.api.TransactionService;
import com.bloxbean.cardano.client.backend.api.UtxoService;
import com.bloxbean.cardano.client.backend.api.helper.FeeCalculationService;
import com.bloxbean.cardano.client.backend.api.helper.TransactionHelperService;
import com.bloxbean.cardano.client.backend.api.helper.model.TransactionResult;
import com.bloxbean.cardano.client.backend.exception.ApiException;
import com.bloxbean.cardano.client.backend.factory.BackendFactory;
import com.bloxbean.cardano.client.backend.model.AddressContent;
//import com.bloxbean.cardano.client.backend.model.Asset;
import com.bloxbean.cardano.client.backend.model.Block;
import com.bloxbean.cardano.client.backend.model.Genesis;
import com.bloxbean.cardano.client.backend.model.Result;
import com.bloxbean.cardano.client.backend.model.TxContentOutputAmount;
import com.bloxbean.cardano.client.backend.model.TxOutputAmount;
import com.bloxbean.cardano.client.backend.model.metadata.MetadataCBORContent;
import com.bloxbean.cardano.client.common.CardanoConstants;
import com.bloxbean.cardano.client.common.model.Network;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.exception.AddressExcepion;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.metadata.Metadata;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadata;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadataList;
import com.bloxbean.cardano.client.metadata.cbor.CBORMetadataMap;
import com.bloxbean.cardano.client.metadata.cbor.MetadataHelper;
import com.bloxbean.cardano.client.transaction.model.MintTransaction;
import com.bloxbean.cardano.client.transaction.model.PaymentTransaction;
import com.bloxbean.cardano.client.transaction.model.TransactionDetailsParams;
import com.bloxbean.cardano.client.transaction.spec.Asset;
import com.bloxbean.cardano.client.transaction.spec.MultiAsset;
import com.bloxbean.cardano.client.transaction.spec.Policy;
import com.bloxbean.cardano.client.transaction.spec.script.ScriptAtLeast;
import com.bloxbean.cardano.client.transaction.spec.script.ScriptPubkey;
import com.bloxbean.cardano.client.util.Tuple;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.rgouzal.reactnativecardano.models.Token;
import com.rgouzal.reactnativecardano.models.TxResult;
import com.rgouzal.reactnativecardano.models.WalletBalance;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CardanoClient {

//    private String blockfrostUrl;
//    private String projectId;
    private BackendService backendService;
    private NetworkInfoService networkInfoService;
    private BlockService blockService;
    private MetadataService metadataService;
    private AddressService addressService;
    private TransactionService transactionService;
    private AssetService assetService;
    private TransactionHelperService transactionHelperService;
    private UtxoService utxoService;
    private FeeCalculationService feeCalculationService;

    public CardanoClient(String blockfrostUrl, String projectId) {
//        this.blockfrostUrl = blockfrostUrl;
//        this.projectId = projectId;
        initServices(blockfrostUrl, projectId);
    }

    public static CardanoClient getInstance(String blockfrostUrl, String projectId) {
        CardanoClient cardanoClient = new CardanoClient(blockfrostUrl, projectId);
        return cardanoClient;
    }

    private void initServices(String blockfrostUrl, String projectId) {
        this.backendService = BackendFactory.getBlockfrostBackendService(blockfrostUrl, projectId);
        this.networkInfoService = this.backendService.getNetworkInfoService();
        this.blockService = this.backendService.getBlockService();
        this.metadataService = this.backendService.getMetadataService();
        this.transactionService = this.backendService.getTransactionService();
        this.transactionHelperService = this.backendService.getTransactionHelperService();
        this.utxoService = this.backendService.getUtxoService();
        this.assetService = this.backendService.getAssetService();
        this.addressService = this.backendService.getAddressService();
        this.feeCalculationService = this.backendService.getFeeCalculationService();
    }

    public Result<Genesis> getNetworkInformation() throws ApiException {
        return networkInfoService.getNetworkInfo();
    }

    /**
     * Gets last block details
     * @return
     * @throws ApiException
     */
    public Result<Block> getLatestBlockDetails() throws ApiException{
        return this.blockService.getLastestBlock();
    }

    /**
     * Gets latest time to live, which is required for fee calculation.
     * @return
     * @throws ApiException
     */
    public long getTTL() throws ApiException {
        return this.blockService.getLastestBlock().getValue().getSlot() + 1000;
    }

    /**
     * In order for us to submit a transaction we need to calculate fees for transaction
     * , so this function calculates transaction fees to be added to transaction.
     * @param tx
     * @param detailsParams
     * @return
     * @throws ApiException
     * @throws AddressExcepion
     * @throws CborSerializationException
     */
    public BigInteger calcTxFee(PaymentTransaction tx,
                                TransactionDetailsParams detailsParams)
            throws ApiException, AddressExcepion, CborSerializationException {
        return feeCalculationService.calculateFee(tx, detailsParams, null);
    }
    /**
     * In order for us to submit a mint transaction we need to calculate fees for
     * minted transaction along  with its metadata, so this function calculates
     * transaction fees to be added to transaction.
     * @param mintTx
     * @param detailsParams
     * @return
     * @throws ApiException
     * @throws AddressExcepion
     * @throws CborSerializationException
     */
    public BigInteger calcTxFee(MintTransaction mintTx,
                                TransactionDetailsParams detailsParams, Metadata metadata)
            throws ApiException, AddressExcepion, CborSerializationException {
        return feeCalculationService.calculateFee(mintTx, detailsParams, metadata);
    }

    /**
     * Create account or wallet for user, returns new wallet address and its information.
     * When account is created, account returned should be stored and specially private key
     * since we can only get account using private key or mnemonic. Private key can be
     * used to store using any state management library like Redux, Mobx, etc ...
     * Make sure to store private key in encrypted format for security purposes.
     * @param mnemonic
     * @param mode
     * @return account
     */
    public Account createAccountByMnemonic(NetworkModes mode, String mnemonic) {
        Account account = new Account(toNetwork(mode), mnemonic);
        return account;
    }

    public Account createAccountByPrivKey(String privKey, NetworkModes mode) {
        Account account = new Account(toNetwork(mode), privKey.getBytes(StandardCharsets.UTF_8));
        return account;
    }

    /**
     * Gets wallet address balance available.
     * @param address
     * @return
     * @throws ApiException
     */
    public WalletBalance getWalletBalance(String address) throws ApiException {
        TxContentOutputAmount outputAmount = getWalletInfo(address).getAmount().get(0);
        return new WalletBalance(outputAmount.getQuantity(), outputAmount.getUnit());
    }

    /**
     * Checks if wallet is valid and available in network.
     * @param address
     * @return
     */
    public boolean isValidAddress(String address) {
        return AddressUtil.isValidAddress(address);
    }

    /**
     * Gets all info related to wallet address balance amount, stake address.
     * @param walletAddress
     * @return
     * @throws ApiException
     */
    // TODO: Fetch more info like txs
    public AddressContent getWalletInfo(String walletAddress) throws ApiException {
        AddressContent content = addressService.getAddressInfo(walletAddress).getValue();
        return content;
    }

    /**
     * Creates a payment transaction to be sent from current user (sender)
     * to be received by receiver address.
     * @param fromStoredPrivateKey
     * @param to
     * @param amount
     * @param networkMode
     * @return
     * @throws ApiException
     */
    public PaymentTransaction createPaymentTx(
            String fromStoredPrivateKey, String to,
            long amount, NetworkModes networkMode, String unit
    ) throws ApiException {
        Account sender = createAccountByPrivKey(fromStoredPrivateKey, networkMode);
        PaymentTransaction tx = PaymentTransaction.builder().sender(sender).receiver(to)
                .amount(BigInteger.valueOf(amount)).unit(CardanoConstants.LOVELACE).build();
        return tx;
    }


    /**
     * Creates transaction details for current ttl, which is required for fee calculation.
     * @param timeToLive
     * @return
     */
    public TransactionDetailsParams createTxDetailsForTTL(long timeToLive) {
        return TransactionDetailsParams.builder().ttl(timeToLive).build();
    }

    /**
     * Transfers payment tx specified by the user.
     * @param tx
     * @param txParams
     * @return
     * @throws ApiException
     */
    public TxResult transferTx(PaymentTransaction tx, TransactionDetailsParams txParams)
            throws ApiException, AddressExcepion, CborSerializationException {
        Result<TransactionResult> txResult = transactionHelperService.transfer(tx, txParams);
        return (TxResult) txResult.getValue();
    }

    public Token createAssetOrNFTMetadata(
            String assetName, String description, List<String> tags, String assetUrlOrIpfsUrl,
            String thumbnailUrlOrIpfsUrl, String type
    )
            throws CborSerializationException {
        String POLICY_NAME_KEY = "pol_";
        Tuple<ScriptPubkey, Keys> key = ScriptPubkey.createWithNewKey();
        ScriptAtLeast scriptAtLeast = new ScriptAtLeast(1).addScript(key._1);
        String keyHash = key._1.getKeyHash();
        String policyId = scriptAtLeast.getPolicyId();
        String assetId = generateAssetId(policyId, assetName);
        Policy policy = new Policy();
        policy.setName(POLICY_NAME_KEY + assetName);
        List<SecretKey> secretKeys = new ArrayList<>();
        secretKeys.add(key._2.getSkey());
        policy.setPolicyKeys(secretKeys);
        policy.setPolicyScript(key._1);
        Token tokenAsset = new Token();
        tokenAsset.setAssetName(assetName);
        tokenAsset.setAssetTokenId(assetId);
        tokenAsset.setPolicy(policy);
        CBORMetadataMap assetMeta = new CBORMetadataMap()
                .put(MetadataKeys.ASSET_NAME, assetName)
                .put(MetadataKeys.ASSET_DESCRIPTION, description)
                .put(MetadataKeys.ASSET_IMAGE, assetUrlOrIpfsUrl)
                .put(MetadataKeys.ASSET_THUMBNAIL, thumbnailUrlOrIpfsUrl)
                .put(MetadataKeys.ASSET_TYPE, type);
        CBORMetadata nftMetadataMap = new CBORMetadata()
                .put(BigInteger.valueOf(MetadataKeys.NFT_ASSET), assetMeta);
        if(tags.size() > 0) {
            CBORMetadataList tagsList = new CBORMetadataList();
            for(String tag : tags)
                tagsList.add(tag);
            nftMetadataMap.put(BigInteger.valueOf(MetadataKeys.NFT_TAGS), tagsList);
        }
        Metadata nftMetadata = (Metadata) nftMetadataMap;
        tokenAsset.setMetadata(nftMetadata);
        return tokenAsset;
    }

    public Token createAssetOrNFT(
            String assetName, String description,
            List<String> tags, String assetUrlOrIpfsUrl,
            String thumbnailUrlOrIpfsUrl, String type, long qty
    ) throws CborSerializationException {
        Token token = createAssetOrNFTMetadata(assetName, description,
                tags, assetUrlOrIpfsUrl, thumbnailUrlOrIpfsUrl, type);
        Asset asset = Asset.builder().name(assetName).value(BigInteger.valueOf(qty)).build();
        MultiAsset multiAsset = token.getMultiAsset();
        multiAsset.getAssets().add(asset);
        token.setMultiAsset(multiAsset);
        return token;
    }

    public TxResult createAndSubmitToken(
            String addressPrivKey, NetworkModes mode, String assetName, String description, List<String> tags,
            String assetUrlOrIpfsUrl, String thumbnailUrlOrIpfsUrl, String type, long qty
    ) throws CborSerializationException, ApiException, AddressExcepion {
        Token token = createAssetOrNFT(
                assetName, description, tags, assetUrlOrIpfsUrl, thumbnailUrlOrIpfsUrl, type, qty
        );
        MintTransaction mintedToken = mintToken(addressPrivKey, mode, token);
        TransactionResult result = submitMint(mintedToken, token.getMetadata());
        return (TxResult) result;
    }

    public MintTransaction mintToken(
            String addressPrivateKey, NetworkModes mode, Token token
    ) throws ApiException {
        Account account = createAccountByPrivKey(addressPrivateKey, mode);
        Policy policy = token.getPolicy();
        MintTransaction mintTx = MintTransaction.builder().sender(account)
                .mintAssets(Arrays.asList(token.getMultiAsset())).policy(policy).build();
        return mintTx;
    }

    public TxResult submitMint(MintTransaction mintTx, Metadata metadata)
            throws ApiException, AddressExcepion, CborSerializationException {
        long timeToLive = getTTL();
        TransactionDetailsParams txParams = createTxDetailsForTTL(timeToLive);
        BigInteger fee = calcTxFee(mintTx, txParams, metadata);
        mintTx.setFee(fee);
        Result<TransactionResult> mintedTxId =
                transactionHelperService.mintToken(mintTx, txParams, metadata);
        return (TxResult) mintedTxId.getValue();
    }

    private String generateAssetId(String policyId, String assetName) {
        return new StringBuilder(policyId).append(assetName).toString();
    }

    /**
     * According to: https://blockfrost.dev/docs/sdks-java#release-resources-when-program-exits
     * This removes any threads or resources when user exists program this helps with
     * performance.
     */
    public void wipeResources() {
//        NetworkHelper.getInstance().shutdown();
    }

    private Network toNetwork(NetworkModes networkMode) {
        if(networkMode == NetworkModes.MAINNET)
            return Networks.mainnet();
        else
            return Networks.testnet();
    }
}
