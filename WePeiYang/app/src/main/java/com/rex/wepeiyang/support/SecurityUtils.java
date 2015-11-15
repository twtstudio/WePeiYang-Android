package com.rex.wepeiyang.support;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class SecurityUtils {

    private static final String SECRET_KEY = "wepeiyang@twtstudio";

    public static String encrypt(String raw) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(SECRET_KEY.getBytes());
            SecretKey secret = SecretKeyFactory.getInstance("DES").generateSecret(dks);

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, random);

            return new String(Base64.encode(cipher.doFinal(raw.getBytes()), 0));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String raw) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec dks = new DESKeySpec(SECRET_KEY.getBytes());
            SecretKey secret = SecretKeyFactory.getInstance("DES").generateSecret(dks);

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, random);

            return new String(cipher.doFinal(Base64.decode(raw, 0)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
