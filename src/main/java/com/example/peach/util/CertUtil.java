package com.example.peach.util;

import com.example.peach.common.Conts;
import com.example.peach.configuration.Configure;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

/**
 * 加载证书
 */
public class CertUtil {
    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final int SOVCKET_TIME_OUT  = 10000; //链接超时时间10秒

    private static final int CONNECT_TIME_OUT = 30000;//传输超时时间

    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom().setSocketTimeout(SOVCKET_TIME_OUT).setConnectTimeout(CONNECT_TIME_OUT).build();

    private static SSLContext wx_ssl_context = null; //微信支付ssl证书

    static{

        try {
            FileInputStream instream = new FileInputStream(new File("C:/Users/PC/Desktop/Test/pack12/apiclient_cert.p12"));
            KeyStore keystore = KeyStore.getInstance("PKCS12");
//            char[] keyPassword = "1519183381".toCharArray(); //证书密码
            keystore.load(instream, Configure.getMch_id().toCharArray());
            wx_ssl_context = SSLContexts.custom().loadKeyMaterial(keystore, Configure.getMch_id().toCharArray()).build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String posts(String url,Object xmlObj) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = new HttpPost(url);
        String body = null;
        CloseableHttpResponse response = null;
        try {
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    wx_ssl_context,
                    new String[] { "TLSv1" },
                    null,new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }});
            httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)//之前下载的证书
                    .build();
            //解决XStream对出现双下划线的bug
            XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
            xStreamForRequestPostData.alias("xml", xmlObj.getClass());
            //将要提交给API的数据对象转换成XML格式数据Post给API
            String postDataXML = xStreamForRequestPostData.toXML(xmlObj);

            //得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
            StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");

            httpPost.setEntity(postEntity);
            response = httpClient.execute(httpPost);
            body = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return body;
    }

    /**
     * 双向证书请求
     * @param requestXml requestXml
     * @return result
     * @throws Exception Exception
     */
    public static String refund(String requestXml,String url) throws Exception {
        //指定读取证书格式为PKCS12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        //读取本机存放的PKCS12证书文件
        FileInputStream instream = new FileInputStream(new File(Conts.PACK12URL));
        try {
            //指定PKCS12的密码(商户ID)
            keyStore.load(instream, Configure.getMch_id().toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = org.apache.http.conn.ssl.SSLContexts.custom()
                .loadKeyMaterial(keyStore, Configure.getMch_id().toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[]{"TLSv1"}, null,
                new HostnameVerifier() {
                    @Override
                    public boolean verify(String s, SSLSession sslSession) {
                        return true;
                    }});
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        HttpPost post = new HttpPost(url);
        StringEntity entity = new StringEntity(requestXml);
        entity.setContentEncoding("UTF-8");
        entity.setContentType("text/xml");
        post.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(post);
        String result = "";
        try {
            HttpEntity entityResult = response.getEntity();
            if (entityResult != null) {
                result = EntityUtils.toString(entityResult, "UTF-8");
            }
        } finally {
            response.close();
        }
        httpclient.close();
        return result;
    }


}
