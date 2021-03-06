package com.weiziplus.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64工具类
 *
 * @author wanglongwei
 * @date 2020/05/27 14/50
 */
public class Base64Utils {

    /**
     * 字符串加密
     *
     * @param text
     * @return
     */
    public static String encode(String text) {
        if (null == text) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(bytes);
    }

    /**
     * 字符串解密
     *
     * @param text
     * @return
     */
    public static String decode(String text) {
        if (null == text) {
            return null;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decode = decoder.decode(text);
        return new String(decode, StandardCharsets.UTF_8);
    }

}
