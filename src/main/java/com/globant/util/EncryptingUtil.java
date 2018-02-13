package com.globant.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**

 * @modify pedro.aguiar
 */
public class EncryptingUtil {

    private static final byte[] ENCODED_KEY = {0x23, -0x53, 0x42, -0x34, 0x3e, -0x76, 0x64, -0x53, 0x42, -0x61, 0x52, -0x3f, 0x2d,
            -0x64, 0x14, 0x74};
    private static final SecretKey SECRET_KEY = new SecretKeySpec(ENCODED_KEY, 0, ENCODED_KEY.length, "AES");
    private static final String CIPHER_SPLITTER = ";;;";

    public static String encryptString(String plain) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] byteText = plain.getBytes();
            byte[] byteCipherText = cipher.doFinal(byteText);
            return new String(Hex.encodeHex(byteCipherText));
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static String[] decryptString(String encrypt) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

            byte[] byteCipherText = Hex.decodeHex(encrypt.toCharArray());
            byte[] byteText = cipher.doFinal(byteCipherText);

            String text = new String(byteText);
            return text.split(CIPHER_SPLITTER);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static long decryptId(String encrypt) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

            byte[] byteCipherText = Hex.decodeHex(encrypt.toCharArray());
            byte[] byteText = cipher.doFinal(byteCipherText);

            String text = new String(byteText);
            return Long.parseLong(text.split(CIPHER_SPLITTER)[0]);
        } catch (Exception e) {
            e.getMessage();
            return 0;
        }
    }

    public static Stream<Long> decryptStrings(List<String> orderIds) {
        return orderIds.stream().map(EncryptingUtil::decryptId);
    }
}
