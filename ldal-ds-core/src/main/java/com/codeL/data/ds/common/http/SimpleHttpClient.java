package com.codeL.data.ds.common.http;

import com.codeL.data.ds.common.http.exception.HTTPException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.Map;

public class SimpleHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);
    private final static int CONNECTION_TIMEOUT = 10000;
    private final static int READ_TIMEOUT = 60000;
    private final static String DEFAULT_CHARSET_NAME = "UTF-8";

    public static HttpSendResult sendPost(String url, Map<String, String> data) {
        return sendPost(url, data, DEFAULT_CHARSET_NAME);
    }

    public static HttpSendResult sendPost(String url, Map<String, String> data, String encoding) {
        try {
            if (StringUtils.isBlank(encoding)) {
                encoding = DEFAULT_CHARSET_NAME;
            }
            HttpURLConnection httpURLConnection = createPostConnection(url, encoding);
            String sendData = getRequestParamString(data, encoding);
            requestServer(httpURLConnection, sendData, encoding);
            String result = response(httpURLConnection, encoding);
            return new HttpSendResult(httpURLConnection.getResponseCode(), result);
        } catch (HTTPException e) {
            throw e;
        } catch (Exception e) {
            throw new HTTPException("Http POST request failed, detail message：" + e.getMessage(), e);
        }
    }


    private static void requestServer(final URLConnection connection, String message,
                                      String encoder) throws Exception {
        PrintStream out = null;
        try {
            connection.connect();
            out = new PrintStream(connection.getOutputStream(), false, encoder);
            out.print(message);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    private static String response(final HttpURLConnection connection, String encoding)
            throws URISyntaxException, IOException, Exception {
        InputStream in = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            if (200 == connection.getResponseCode()) {
                in = connection.getInputStream();
                sb.append(new String(read(in), encoding));
            } else {
                in = connection.getErrorStream();
                sb.append(new String(read(in), encoding));
            }
            //logger.info("HTTP Return Status-Code:[" + connection.getResponseCode() + "]");
            return sb.toString();
        } catch (Exception e) {
            throw new HTTPException(connection.getResponseCode(), e.getMessage(), e);
        } finally {
            if (null != br) {
                br.close();
            }
            if (null != in) {
                in.close();
            }
            if (null != connection) {
                connection.disconnect();
            }
        }
    }

    public static byte[] read(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        int length = 0;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        while ((length = in.read(buf, 0, buf.length)) > 0) {
            bout.write(buf, 0, length);
        }
        bout.flush();
        return bout.toByteArray();
    }

    private static HttpURLConnection createPostConnection(String url, String encoding)
            throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
        httpURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        httpURLConnection.setReadTimeout(READ_TIMEOUT);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=" + encoding);
        httpURLConnection.setRequestMethod("POST");
        return httpURLConnection;
    }


    private static String getRequestParamString(Map<String, String> requestParam,
                                                String coder) throws UnsupportedEncodingException {
        StringBuffer sf = new StringBuffer("");
        String reqstr = "";
        if (null != requestParam && 0 != requestParam.size()) {
            for (Map.Entry<String, String> en : requestParam.entrySet()) {
                sf.append(en.getKey()
                        + "="
                        + (null == en.getValue() || "".equals(en.getValue()) ? ""
                        : URLEncoder.encode(en.getValue(), coder))
                        + "&");
            }
            reqstr = sf.substring(0, sf.length() - 1);
        }
        return reqstr;
    }

    public static class HttpSendResult {

        private int httpCode = 200;
        private String response;

        public HttpSendResult(int httpCode, String response) {
            this.httpCode = httpCode;
            this.response = response;
        }

        public boolean isOk() {
            return httpCode == 200;
        }

        public int getHttpCode() {
            return httpCode;
        }

        public String getResponse() {
            return response;
        }

        @Override
        public String toString() {
            return "HttpSendResult{" +
                    "httpCode=" + httpCode +
                    ", response='" + response + '\'' +
                    '}';
        }
    }

}
