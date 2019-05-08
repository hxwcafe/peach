package com.example.peach.util;


import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.example.peach.configuration.Configure;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;


/**
 * AES加密
 * Created by Administrator on 2018/11/15.
 */
@Service
public class AesCbcUtil {
    private static final String AES ="AES";
    private static final String AES_CBC_PKCS7 ="AES/CBC/PKCS5Padding";
    /**
     * AES解密
     *
     * @param content 密文
     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchProviderException
     */
    public byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws InvalidAlgorithmParameterException {
            try {
//                byte[] data = Base64.decode(content);
//                byte[] aseKey = Base64.decode(keyByte);
//                byte[] ivData = Base64.decode(ivByte);
                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

                Cipher cipher = Cipher.getInstance(AES_CBC_PKCS7);
                Key sKeySpec = new SecretKeySpec(keyByte, AES);
                cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化

                byte[] result = cipher.doFinal(content);
                return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    // 生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }

    /**
     * 根据code获取openid,session_key
     * @param code code
     * @return json
     */
    public static JSONObject ResponseopenidSessionKey(String code){
        Map<String, String> paremap = new TreeMap<String, String>();
        paremap.put("appid", Configure.getAppID());
        paremap.put("secret", Configure.getSecret());
        paremap.put("js_code", code);
        paremap.put("grant_type", "authorization_code");
        String param = Signature.formatUrlMap(paremap, false, false);
        String sr = HttpRequest.sendGet(Configure.Code_Session, param);
        JSONObject json = JSONObject.fromObject(sr);
        if (json.get("errmsg") != null) {
            System.out.println("错误:" + json.get("errmsg"));
        }
        return json;
    }


}
