package com.rgouzal.reactnativecardano.utils;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.bloxbean.cardano.client.crypto.bip39.Words;
import com.rgouzal.reactnativecardano.R;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CryptoUtils {

      private static final String RANDOM_ALGORITHM = "SHA1PRNG";
      private static final int HASH_ITERATIONS = 4096;
      private static final int HASH_OUTPUT_SIZE = 128; //128 bytes since we're using HmacSHA256 underneath
      private static final int SALT_LENGTH = 20;
      private static final int KEY_SIZE  = 768;

      public static byte[] randomPrivateKey() throws NoSuchAlgorithmException {
          KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
          keyGenerator.init(128);
          SecretKey secretKey = keyGenerator.generateKey();
          byte[] encodedKey = secretKey.getEncoded();
//          String keyText = String.format("%032x", new BigInteger(+1, encodedKey));
//          return keyText;
          return encodedKey;
      }

      public static byte[] passphraseToPrivateKey(String passphrase, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] encodedHash = messageDigest.digest(passphrase.getBytes(StandardCharsets.UTF_8));
            return Arrays.copyOf(encodedHash, 128);
      }

      @RequiresApi(api = Build.VERSION_CODES.N)
      public static byte[] mnemonicToPrivateKey(String mnemonic) throws Exception {
          List<String> words = Arrays.asList(mnemonic.split(" "));
          String mnemonicFiltered = words.stream().collect(Collectors.joining(""));
          String randomSalt = generateSalt();
          PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
          gen.init("".getBytes(), mnemonicFiltered.getBytes(StandardCharsets.UTF_8), HASH_ITERATIONS);
          byte[] key = ((KeyParameter) gen.generateDerivedParameters(KEY_SIZE)).getKey();
          return key;
      }

    public static String generateSalt() throws Exception {
        try {
            SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            String saltHex = bytesToHexString(salt);
            return saltHex;
        } catch (Exception e) {
            throw new Exception("Unable to generate salt", e);
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

}
