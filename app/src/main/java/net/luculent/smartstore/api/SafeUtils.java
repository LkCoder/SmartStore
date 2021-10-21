package net.luculent.smartstore.api;

import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class SafeUtils {

    /* renamed from: SK */
    public static final String f6395SK = "1Lu2cu3le4nt5!@#";
    private static BaseInfo info = new BaseInfo();

    static class BaseInfo {
        private String HmacSecret = "HmacSecret";
        private String aesKey = "aesKey";
        private String httpSession = "httpSession";
        private String langId = "langId";
        private String orgNo = "orgNo";
        private String sessionId = "sessionId";
        private String skin = "skin";
        private String userId = "userId";

        BaseInfo() {
        }

        public String getHmacSecret() {
            return this.HmacSecret;
        }

        public void setHmacSecret(String hmacSecret) {
            this.HmacSecret = hmacSecret;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId2) {
            this.userId = userId2;
        }

        public String getSessionId() {
            return this.sessionId;
        }

        public void setSessionId(String sessionId2) {
            this.sessionId = sessionId2;
        }

        public String getLangId() {
            return this.langId;
        }

        public void setLangId(String langId2) {
            this.langId = langId2;
        }

        public String getOrgNo() {
            return this.orgNo;
        }

        public void setOrgNo(String orgNo2) {
            this.orgNo = orgNo2;
        }

        public String getSkin() {
            return this.skin;
        }

        public void setSkin(String skin2) {
            this.skin = skin2;
        }

        public String getHttpSession() {
            return this.httpSession;
        }

        public void setHttpSession(String httpSession2) {
            this.httpSession = httpSession2;
        }

        public String getAesKey() {
            return this.aesKey;
        }

        public void setAesKey(String aesKey2) {
            this.aesKey = aesKey2;
        }
    }

    public static void setSaltAndPass(String usrId, String salt, String pass) {
        info.setUserId(usrId);
        info.setHmacSecret(pbkdf2(md5(pass), salt, 1000, 256));
        info.setSessionId("");
    }

    public static void setNewHmacSecret(String key, String sessionId, String userId, String orgNo) {
        byte[] data = AESDecrypt(key, md5(info.getHmacSecret()).substring(8, 24));
        info.setHmacSecret(new String(data));
        info.setSessionId(sessionId);
        info.setUserId(userId);
        info.setOrgNo(orgNo);
        info.setAesKey(md5(new String(data)).substring(8, 24));
    }

    public static void setHttpSession(String id) {
        info.setHttpSession(id);
    }

    public static String getHttpSession() {
        return info.getHttpSession();
    }

    public static byte[] encryPass(String pass) {
        return AESEncrypt(pass, info.getAesKey());
    }

    public static String getLoginUsrCode(String userId) {
        return Base64.encodeToString(AESEncrypt(userId, new String(Base64.decode(f6395SK.getBytes(), 2))), 2);
    }

    public static String getLoginDecry(String message) {
        return new String(AESDecrypt(message, new String(Base64.decode(f6395SK.getBytes(), 2))));
    }


    public static String aesSimpleEncrypt(String userId) {
        String revKey = new String(Base64.decode(f6395SK.getBytes(), 2));
        String enc = Base64.encodeToString(AESEncrypt(userId, revKey), 2);
        return Base64.encodeToString(AESEncrypt((new Date().getTime() + 300000) + "_" + enc + "_" + getHttpSession(), revKey), 2);
    }

    public static String Base64StrAesEncrypt(String message) {
        if (TextUtils.isEmpty(message)) {
            message = "";
        }
        return Base64.encodeToString(AESEncrypt(message, f6395SK), 2);
    }

    public static String getJwt() {
        HashMap hashMap = new HashMap();
        long jti = new Date().getTime();
        long iat = jti / 1000;
        long exp = 300000 + jti;
        try {
            hashMap.put("sub", info.getUserId());
            hashMap.put("exp", Long.valueOf(exp));
            hashMap.put("iat", Long.valueOf(iat));
            hashMap.put("iss", "MB00001");
            hashMap.put("jti", Long.valueOf(((long) ((int) (Math.random() * 1000.0d))) + jti));
            hashMap.put("sessionId", TextUtils.isEmpty(info.getSessionId()) ? Long.valueOf(jti) : info.getSessionId());
            hashMap.put("langId", info.getLangId());
            hashMap.put("orgNo", info.getOrgNo());
            hashMap.put("skin", info.getSkin());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jwt = "";
        try {
            JSONObject headJson = new JSONObject();
            headJson.put("alg", "HS256");
            JSONObject payloadJson = new JSONObject(hashMap);
            String base64UrlEncodedHeader = base64UrlEncode(headJson.toString().getBytes());
            String base64UrlEncodedBody = base64UrlEncode(payloadJson.toString().getBytes());
            String jwt1 = base64UrlEncodedHeader + '.' + base64UrlEncodedBody;
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            JSONObject jSONObject = headJson;
            SecretKeySpec secretKeySpec = new SecretKeySpec(info.getHmacSecret().getBytes(), "HS256");
            sha256_HMAC.init(secretKeySpec);
            String base64UrlSignature = base64UrlEncode(sha256_HMAC.doFinal(jwt1.getBytes()));
            SecretKeySpec secretKeySpec2 = secretKeySpec;
            StringBuilder sb = new StringBuilder();
            sb.append(jwt1);
            HashMap hashMap2 = hashMap;
            try {
                sb.append(".");
                sb.append(base64UrlSignature);
                return sb.toString();
            } catch (Exception e2) {
                e2.printStackTrace();
                return jwt;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            return jwt;
        }
    }

    private static String md5(String string) {
        try {
            return computeMD5(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh,UTF-8 should be supported?", e);
        }
    }

    private static String computeMD5(byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input, 0, input.length);
            return byte2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] AESEncrypt(String messageStr, String secretStr) {
        try {
            byte[] message = messageStr.getBytes("UTF-8");
            byte[] secret = secretStr.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, new SecretKeySpec(secret, "AES"));
            return cipher.doFinal(message);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] AESDecrypt(String messageStr, String secretStr) {
        try {
            byte[] message = Base64.decode(messageStr, 2);
            byte[] secret = secretStr.getBytes("UTF-8");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, new SecretKeySpec(secret, "AES"));
            byte[] hash = cipher.doFinal(message);
            new String(hash);
            return hash;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String pbkdf2(String password, String salt, int iterationCount, int keyLength) {
        try {
            return byte2Hex(SecretKeyFactory.getInstance("PBKDF2withHmacSHA1").generateSecret(new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterationCount, keyLength)).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String base64UrlEncode(byte[] data) {
        Charset US_ASCII = Charset.forName("US-ASCII");
        byte[] bytes = removePadding(Base64.encodeToString(data, 2).getBytes(US_ASCII));
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == 43) {
                bytes[i] = 45;
            } else if (bytes[i] == 47) {
                bytes[i] = 95;
            }
        }
        return new String(bytes, US_ASCII);
    }

    private static byte[] removePadding(byte[] bytes) {
        byte[] result = bytes;
        int paddingCount = 0;
        int i = bytes.length - 1;
        while (i > 0 && bytes[i] == 61) {
            paddingCount++;
            i--;
        }
        if (paddingCount <= 0) {
            return result;
        }
        byte[] result2 = new byte[(bytes.length - paddingCount)];
        System.arraycopy(bytes, 0, result2, 0, bytes.length - paddingCount);
        return result2;
    }

    public static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            String stmp = Integer.toHexString(b & 0xFF);
            if (stmp.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(stmp);
        }
        return stringBuilder.toString().toLowerCase();
    }

    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[(hex.length() / 2)];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(i * 2, (i * 2) + 2), 16);
        }
        return binary;
    }
}