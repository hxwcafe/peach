package com.example.peach.util;

import com.example.peach.configuration.Configure;
import org.apache.commons.codec.digest.DigestUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class PayUtil {
    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        //加密
        System.out.println(text);
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));

    }

    /**
     * 获取客户端真实IP地址
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
    /**
     * @param text
     * @param sign          签名结果
     * @param key
     * @param input_charset
     * @return
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 按照指定的字符编码
     *
     * @param content
     * @param charset
     * @return
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }

    }

    private static boolean isValidChar(char ch) {
        if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))
            return true;
        if ((ch >= 0x4e00 && ch <= 0x7fff) || (ch >= 0x8000 && ch <= 0x952f))
            return true;// 简体中文汉字编码
        return false;
    }

    /**
     * 除去数组中的空值和签名值
     *
     * @param filmap 签名参数组
     * @return 去掉空值与签名参数的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> filmap) {
        Map<String, String> result = new HashMap<String, String>();
        if (filmap == null || filmap.size() <= 0) {
            return result;
        }
        for (String key : filmap.keySet()) {
            String value = filmap.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param strxml
     * @return
     * @throws Exception
     */
    public static Map doXMLParse(String strxml)throws Exception{
        if(null==strxml||"".equals(strxml)){
            return null;
        }
        Map map = new HashMap();
        InputStream in = String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        //SAXBuilder是一个JDOM解析器 能将路径中的XML文件解析为Document对象
        Document document = builder.build(in);
        //得到根节点
        Element element = document.getRootElement();
//        List list = element.getChildren();
//        Iterator iterator = list.iterator();
//        while(iterator.hasNext()){
//            Element e = (Element) iterator.next();
//            String key = e.getName();
//            String value = "";
//            List children = e.getChildren();
//            if(children.isEmpty()){
//                value = e.getTextNormalize();
//            }else{
//                value = getChildrenText(children);
//            }
//            map(key,value);
//        }
        List<Element> list =element.getChildren();
        for (Element e : list) {
            map.put(e.getName(), e.getText());
        }
        in.close();
        return map;
    }

    /**
     * 获取子节点的xml
     * @param children
     * @return
     */
    public static String getChildrenText(List children){
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    /**
     * 字符串转输出流
     * @param str
     * @return
     */
    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * 微信退款
     * @param out_trade_no
     * @return
     */
    public static String wxPayRefund(String out_trade_no,String total_fee,String out_refun_no) {
        StringBuffer xml = new StringBuffer();
        String data = null;
        Map<String,String> parameters = new TreeMap<String,String>();
        try {
            String nonceStr = null;
            if (out_refun_no!=null){
                nonceStr = out_refun_no;
            }else {
                nonceStr = RandomStringGenerator.getRandomStringtime(32);
            }
            xml.append("</xml>");
            parameters.put("appid", Configure.getAppID());
            parameters.put("mch_id", Configure.getMch_id());
            parameters.put("nonce_str",  RandomStringGenerator.getRandomStringByLength(32));
            parameters.put("out_trade_no", out_trade_no);
//            parameters.put("transaction_id", transaction_id);
            parameters.put("out_refund_no", nonceStr);//退款单号
//            parameters.put("fee_type", "CNY");
            parameters.put("total_fee", total_fee);
            parameters.put("refund_fee", total_fee);
            Map<String, String> tonullmap = PayUtil.paraFilter(parameters);//回调验签时需要去除sign和空值参数
//            String vailder = PayUtil.createLinkString(tonullmap);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
//            String sign = PayUtil.sign(vailder, Configure.getKey(), "utf-8").toUpperCase();
            String stringA = Signature.formatUrlMap(tonullmap,false,false);
            String sign = MD5.MD5Encode(stringA+"&key="+ Configure.getKey()).toUpperCase();
            parameters.put("sign", sign);
            data = MaptoXml(parameters);//map转xml
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        return data;
    }

    /**
     *  map转换xml格式
    * @param params
     * @return
     */
    public static String MaptoXml(Map<String,String> params) {

        StringBuilder sb = new StringBuilder();
        Set es = params.entrySet();
        Iterator it = es.iterator();
        sb.append("<xml>\n");
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            sb.append("<"+k+">");
            sb.append(v);
            sb.append("</"+k+">\n");
        }
        sb.append("</xml>");
        return sb.toString();
    }


}
