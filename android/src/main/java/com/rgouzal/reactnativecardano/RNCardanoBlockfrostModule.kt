package com.rgouzal.reactnativecardano

import android.net.Network
import android.widget.Toast
import com.bloxbean.cardano.client.account.Account
import com.bloxbean.cardano.client.crypto.bip39.Words
import com.facebook.react.bridge.*
import com.google.gson.JsonObject
import com.rgouzal.reactnativecardano.client.CardanoClient
import com.rgouzal.reactnativecardano.client.NetworkModes
import com.rgouzal.reactnativecardano.models.Wallet
import com.rgouzal.reactnativecardano.utils.CryptoUtils

import com.rgouzal.reactnativecardano.utils.RNMapUtils;
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.HashMap


class RNCardanoBlockfrostModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {


    var cardanoClient: CardanoClient? = null;
    var networkMode: String = ""

    override fun getName() = "RNCardanoBlockfrostModule"

    override fun getConstants(): MutableMap<String, Any> {
        return hashMapOf("count" to 1)
    }

    @ReactMethod
    fun initClient(networkMode: String, projectId: String) {
//        Toast.makeText(reactApplicationContext, "network passed: $networkMode", Toast.LENGTH_SHORT).show()
//        var url = NetworkModes.getByValue(networkMode).url;
//        Toast.makeText(reactApplicationContext, "project id: $projectId", Toast.LENGTH_LONG).show()
//        Toast.makeText(reactApplicationContext, "supposed url: $url", Toast.LENGTH_LONG).show()
        val blockchainNetwork: NetworkModes = NetworkModes.getByValue(networkMode)
        val blockfrostUrl = blockchainNetwork.url
        cardanoClient = CardanoClient.getInstance(blockchainNetwork, blockfrostUrl, projectId);
    }

    @ReactMethod
    fun canConnect(promise: Promise) {
//        Toast.makeText(this.reactApplicationContext, "Cardano Blockchain: $isOk", Toast.LENGTH_SHORT).show()
        try {
            val isOk = cardanoClient?.isHealthOk
            promise.resolve(isOk)
        } catch (e: java.lang.Exception) {
            promise.resolve(false)
        }
    }

    @ReactMethod
    fun createAccountByMnemonic(networkMode: String, mnemonic: String, promise: Promise) {
        this.networkMode = networkMode
        var map: WritableMap = WritableNativeMap()
        val account: Account? =
                cardanoClient?.createAccountByMnemonic(NetworkModes.getByValue(networkMode), mnemonic)
        if (account != null) {
              val wallet: Wallet = Wallet.fromAccount(account, networkMode)
              val accountJson: JsonObject = RNMapUtils.toJsonObject(wallet);
              map = RNMapUtils.fromJsonObjectToMap(accountJson);
        }
        promise.resolve(map)
    }

    @ReactMethod
    fun createAccountByPrivKey(networkMode: String, passphrase: String, promise: Promise) {
        var map: WritableMap = WritableNativeMap()
        val key = CryptoUtils.passphraseToPrivateKey(passphrase, "IcanIWlkakjiJmpu82Etc9EIP122MXrpNnBtcciADAuwIcanIWlkakjiJmpu82Etc9EIP122MXrpNnBtcciADAuwIcanIWlkakjiJmpu82Etc9EIP122MXrpNnBtcciADAuwIcanIWlkakjiJmpu82Etc9EIP122MXrpNnBtcciADAuw")
        val account: Account? =
                cardanoClient?.createAccountByPrivKey(NetworkModes.getByValue(networkMode), key)
        if (account != null) {
            val wallet: Wallet = Wallet.fromAccount(account, networkMode)
            val accountJson: JsonObject = RNMapUtils.toJsonObject(wallet);
            map = RNMapUtils.fromJsonObjectToMap(accountJson);
        }
        promise.resolve(map)
    }

    @ReactMethod
    fun generateMnemonic(promise: Promise) {
        val mnemonic = cardanoClient?.generateMnemonic()
        promise.resolve(mnemonic)
    }

//    fun fetchWordList(): String {
//        val ins: InputStream = reactApplicationContext.resources.openRawResource(R.raw.en_wordlist)
//        val br: BufferedReader = BufferedReader(InputStreamReader(ins))
//        var raw: StringBuilder = StringBuilder("")
//        var continueRead: Boolean = true
//        return try {
//            while(continueRead) {
//                val currentLine = br.readLine()
//                raw.append(currentLine)
//                continueRead = currentLine != null
//            }
//            raw.toString()
//        } catch (ex: java.lang.Exception) {
//            ""
//        }
//    }
}
