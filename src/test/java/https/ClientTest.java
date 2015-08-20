package https;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.junit.Test;

import javax.net.ssl.*;

/**
 * Created by hongcheng on 7/11/15.
 */
public class ClientTest {


    public void test() throws Exception{
        HttpClient http = new HttpClient();
        String url = "https://192.168.0.108/";

        Protocol myhttps = new Protocol("https", new DefaultSSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);

        GetMethod get = new GetMethod(url);
        http.executeMethod(get);
        System.out.println(get.getResponseBodyAsString());
    }


    @Test
    public void test2() {

        try {
            SslTest st = new SslTest();
            String a = st.getRequest("https://192.168.0.108/", 3000);
            System.out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
