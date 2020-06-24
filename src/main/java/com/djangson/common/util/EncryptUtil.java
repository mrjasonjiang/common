package com.djangson.common.util;

import com.djangson.common.constant.Constants;
import com.djangson.common.exception.BusinessException;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * @description: 加解密工具类
 * @author: wangqinjun@vichain.com
 * @create: 2020/06/18 13:21
 */
public class EncryptUtil {

    public static final String SHA1 = "SHA1", DES = "DES", AES = "AES", MD5 = "MD5", RSA = "RSA";
    private static final String[] CHAR_POOL = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    /**
     * 根据明文密钥生成加密密钥，默认128位
     * @param key       明文密钥
     * @param algorithm 加密算法
     * @return
     */
    public static String getSecretKey(String key, String algorithm) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(128, new SecureRandom(key.getBytes(Charset.defaultCharset())));
            return Base64Encode(keyGenerator.generateKey().getEncoded());
        } catch (Exception e) {
            throw new BusinessException("获取加密密钥失败！", e);
        }
    }

    /**
     * 生成密钥对：密钥对中包含公钥和私钥
     * @return 包含 RSA 公钥与私钥的 KeyPair
     * @throws Exception
     */
    public static KeyPair getKeyPair(String key) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        SecureRandom secureRandom = new SecureRandom(key.getBytes(Charset.defaultCharset()));
        keyPairGenerator.initialize(1024, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 获取公钥
     * @param keyPair
     * @return
     */
    public static String getPublicKey(KeyPair keyPair) {
        return Base64Encode(keyPair.getPublic().getEncoded());
    }

    /**
     * 获取私钥
     * @param keyPair
     * @return
     */
    public static String getPrivateKey(KeyPair keyPair) {
        return Base64Encode(keyPair.getPrivate().getEncoded());
    }

    /**
     * BASE64编码
     * @param src
     * @return
     */
    public static String Base64Encode(String src) {
        return Base64.getEncoder().encodeToString(src.getBytes());
    }

    /**
     * BASE64编码
     * @param src
     * @return
     */
    public static String Base64Encode(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    /**
     * BASE64解码
     * @param src
     * @return
     */
    public static String Base64Decode(String src) {
        return new String(Base64.getDecoder().decode(src));
    }

    /**
     * 使用SHA1加密算法进行加密（不可解密）
     * @param src 需要加密的原文
     * @return
     */
    public static String SHA1Encode(String src) {
        return messageDigest(src, SHA1);
    }

    /**
     * 使用RSA加密算法进行加密（可解密）
     * @param src       需要加密的明文
     * @param publicKey 公钥
     * @return
     */
    public static String RSAEncode(String src, String publicKey) {
        return encryptors(src, publicKey, RSA, true);
    }

    /**
     * 使用AES算法进行解密
     * @param src        需要解密的密文
     * @param privateKey 秘钥
     * @return
     */
    public static String RSADecode(String src, String privateKey) {
        return encryptors(src, privateKey, RSA, false);
    }

    /**
     * 使用AES加密算法进行加密（可解密）
     * @param src 需要加密的密文
     * @param key 秘钥
     * @return
     */
    public static String AESEncode(String src, String key) {
        return encryptors(src, key, AES, true);
    }

    /**
     * 使用AES算法进行解密
     * @param src 需要解密的密文
     * @param key 秘钥
     * @return
     */
    public static String AESDecode(String src, String key) {
        return encryptors(src, key, AES, false);
    }

    /**
     * 使用DES加密算法进行加密（可逆）
     * @param src 需要加密的原文
     * @param key 秘钥
     * @return
     */
    public static String DESEncode(String src, String key) {
        return encryptors(src, key, DES, true);
    }

    /**
     * 对使用DES加密算法的密文进行解密（可逆）
     * @param src 需要解密的密文
     * @param key 秘钥
     * @return
     */
    public static String DESDecode(String src, String key) {
        return encryptors(src, key, DES, false);
    }

    /**
     * MD5加密
     * @param src
     * @return
     */
    public static String MD5Encode(String src) {

        try {

            // 获取密文
            byte[] md5Bytes = messageDigestByte(src, MD5);

            // 将密文转换成十六进制的字符串形式
            StringBuilder hexValue = new StringBuilder();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = md5Bytes[i] & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            throw new BusinessException("数据加密失败！", e);
        }
    }

    /**
     * MD5 加密，加盐
     * @param password
     * @return
     */
    public static String MD5EncodeWithSalt(String password) {

        // 随机生成盐值，默认16位
        int capacity = 16;
        Random random = new Random();
        StringBuilder saltBuilder = new StringBuilder(capacity);
        for (int i = 0; i < capacity; i++) {
            saltBuilder.append(CHAR_POOL[random.nextInt(CHAR_POOL.length)]);
        }

        // 生成加盐密码
        String newPassword = messageDigest(password + saltBuilder, MD5);

        // 将盐值隐藏在生成的加密密码中
        char[] newPasswordArr = new char[48];
        for (int i = 0; i < 48; i += 3) {
            newPasswordArr[i] = newPassword.charAt(i / 3 * 2);
            newPasswordArr[i + 1] = saltBuilder.charAt(i / 3);
            newPasswordArr[i + 2] = newPassword.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(newPasswordArr);
    }

    /**
     * 校验加盐后是否和原文一致
     * @param password   校验密码
     * @param cipherText 原始密码
     * @return
     */
    public static boolean MD5Verify(String password, String cipherText) {

        // 参数校验
        if (StringUtils.isBlank(cipherText) || cipherText.length() != 48) {
            return false;
        }

        // 初始化原MD5数组、盐值数组
        char[] passwordArr = new char[32];
        char[] saltArr = new char[16];

        // 从48位的原加密密码中剥离MD5和盐值
        for (int i = 0; i < 48; i += 3) {
            passwordArr[i / 3 * 2] = cipherText.charAt(i);
            passwordArr[i / 3 * 2 + 1] = cipherText.charAt(i + 2);
            saltArr[i / 3] = cipherText.charAt(i + 1);
        }
        return messageDigest(password + String.valueOf(saltArr), MD5).equals(String.valueOf(passwordArr));
    }

    /**
     * 根据算法要求获取十六进制字符串形式的摘要
     * @param src
     * @param algorithm
     * @return
     */
    private static String messageDigest(String src, String algorithm) {
        try {
            return Hex.encode(messageDigestByte(src, algorithm));
        } catch (Exception e) {
            throw new BusinessException("数据加密失败！", e);
        }
    }

    /**
     * 根据算法要求获取十六进制字符串形式的摘要
     * @param src
     * @param algorithm
     * @return
     */
    private static byte[] messageDigestByte(String src, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            return messageDigest.digest(src.getBytes());
        } catch (Exception e) {
            throw new BusinessException("数据加密失败！", e);
        }
    }

    /**
     * RSA, AES, DES 统一加解密运算器
     * @param src       加密的原文
     * @param key       加密的秘钥
     * @param algorithm 加密使用的算法名称
     * @param isEncode
     * @return
     */
    private static String encryptors(String src, String key, String algorithm, boolean isEncode) {
        try {

            // 将序列化后的密钥反向序列化
            Key secretKey = getSecretKeyByAlgorithm(key, algorithm, isEncode);

            // 获取加解密实例，对数据加解密
            Cipher cipher = Cipher.getInstance(algorithm);
            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] resBytes = src.getBytes(Charset.defaultCharset());
                return Hex.encode(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return new String(cipher.doFinal(Hex.decode(src)));
            }
        } catch (Exception e) {
            throw new BusinessException("数据加解密失败！", e);
        }
    }

    /**
     * 根据密钥字符串反向序列化为密钥对象
     * @param key
     * @param algorithm
     * @param isEncode
     * @return
     */
    private static Key getSecretKeyByAlgorithm(String key, String algorithm, boolean isEncode) throws Exception {

        // 1. 先解码密钥
        byte[] keyByte = Base64.getDecoder().decode(key);

        // 1. 若是RSA方式：若是加密，则默认为公钥，若是解密，则默认为私钥
        if (algorithm.equals(RSA)) {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return isEncode ? keyFactory.generatePublic(new X509EncodedKeySpec(keyByte)) : keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyByte));
        }

        // 2. 其他方式：将序列化后的密钥反向序列化
        return new SecretKeySpec(keyByte, algorithm);
    }

    /**
     * 内部类，为org.springframework.security.crypto.codec.HEX副本
     */
    private static final class Hex {

        private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        /**
         * 二进制转换为十六进制
         * @param bytes
         * @return
         */
        private static String encode(byte[] bytes) {
            final int nBytes = bytes.length;
            char[] result = new char[2 * nBytes];
            for (int i = 0, j = 0; i < nBytes; i++) {
                result[j++] = HEX[(0xF0 & bytes[i]) >>> 4];
                result[j++] = HEX[(0x0F & bytes[i])];
            }
            return new String(result);
        }

        /**
         * 十六进制转换为二进制
         * @param s
         * @return
         */
        private static byte[] decode(CharSequence s) {
            int nChars = s.length();
            if (nChars % 2 != 0) {
                throw new IllegalArgumentException("Hex-encoded string must have an even number of characters");
            }

            byte[] result = new byte[nChars / 2];
            for (int i = 0; i < nChars; i += 2) {
                int msb = Character.digit(s.charAt(i), 16);
                int lsb = Character.digit(s.charAt(i + 1), 16);
                if (msb < 0 || lsb < 0) {
                    throw new IllegalArgumentException("Detected a Non-hex character at " + (i + 1) + " or " + (i + 2) + " position");
                }
                result[i / 2] = (byte) ((msb << 4) | lsb);
            }
            return result;
        }
    }
}
