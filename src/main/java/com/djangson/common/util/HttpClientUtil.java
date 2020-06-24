package com.djangson.common.util;

import com.djangson.common.base.domain.Result;
import com.djangson.common.constant.Constants;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: wangqinjun@vichain.com
 * @Date: 2019/3/25 20:37
 */
public class HttpClientUtil {

    /**
     * 用于GET 请求
     * @param url
     * @param param
     * @param headers
     * @return
     */
    public static String executeGetRequest(String url, Object param, Map<String, String> headers) {
        return executeRequest(HttpMethod.GET, url, param, headers, null);
    }

    /**
     * 用于POST 请求
     * @param url
     * @param param
     * @param headers
     * @return
     */
    public static String executePostRequest(String url, Object param, Map<String, String> headers) {
        return executeRequest(HttpMethod.POST, url, param, headers, null);
    }

    /**
     * 用于文件下载请求
     * @param url
     * @param param
     * @param headers
     * @return
     */
    public static String executeFileDownloadRequest(String url, Object param, Map<String, String> headers) {
        headers = headers == null ? new HashMap<>() : headers;
        headers.put(Constants.FEIGN_TOKEN_HEADER, Constants.FEIGN_TOKEN_VALUE);
        return executeGetRequest(url, param, headers);
    }

    /**
     * 用于文件上传请求
     * @param url
     * @param param
     * @param headers
     * @return
     */
    public static String executeFileUploadRequest(String url, Object param, Map<String, String> headers, File uploadFile) {
        headers = headers == null ? new HashMap<>() : headers;
        headers.put(Constants.FEIGN_TOKEN_HEADER, Constants.FEIGN_TOKEN_VALUE);
        return executeRequest(HttpMethod.POST, url, param, headers, uploadFile);
    }

    /**
     * 统一请求方法，支持文件上传、下载
     * @param method
     * @param url
     * @param param
     * @param headers
     * @return
     */
    public static String executeRequest(HttpMethod method, String url, Object param, Map<String, String> headers, File uploadFile) {

        // 若是GET请求，需要格式化URL，设置参数
        url = method == HttpMethod.GET ? RequestParamFormatUtil.formatGetRequestParam(url, param) : url;

        // 初始化请求对象
        HttpRequest httpRequest = method == HttpMethod.GET ? new HttpGet(url) : new HttpPost(url);

        // 设置请求头
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpRequest.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // 对于POST 请求，设置请求体，目前仅支持Payload
        if (method == HttpMethod.POST) {
            if (uploadFile == null) {
                StringEntity entity = new StringEntity(JsonUtil.toJsonString(param), "UTF-8");
                entity.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ((HttpPost) httpRequest).setEntity(entity);
            } else {
                HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", new FileBody(uploadFile)).build();
                ((HttpPost) httpRequest).setEntity(reqEntity);
            }
        }

        try {

            // 设置HTTP 请求客户端
            HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(30000).build()).build();

            // 开始请求并获取响应
            HttpResponse response = httpClient.execute((HttpUriRequest) httpRequest);

            // 解析响应状态码
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                LoggerUtil.getLogger().error("响应状态码异常！具体如下：{}", statusLine);
                ExceptionUtil.rollback(StringUtil.concat("响应状态码异常！具体如下：", statusLine));
            }

            // 获取响应头的content-type
            Header[] contentTypeHeader = response.getHeaders(HttpHeaders.CONTENT_TYPE);
            String contentType = ArrayUtils.isEmpty(contentTypeHeader) ? null : contentTypeHeader[0].getValue();

            // 若content-type不存在，或是JSON，则一律按照JSON格式处理
            if (StringUtils.isBlank(contentType) || contentType.toUpperCase().contains("APPLICATION/JSON")) {
                return EntityUtils.toString(response.getEntity());
            }

            // 其他格式，则按照文件处理
            return writeFile(response);

        } catch (Exception e) {
            ExceptionUtil.rollback("请求处理异常！", e);
        } finally {
            ((HttpRequestBase) httpRequest).releaseConnection();
        }
        return null;
    }

    /**
     * 写出文件
     * @param response
     */
    private static String writeFile(HttpResponse response) throws Exception {

        // 获取响应实体
        HttpEntity httpEntity = response.getEntity();
        if (httpEntity.getContentLength() <= 0) {
            ExceptionUtil.rollback("您访问的文件不存在！");
        }
        if (httpEntity.getContentLength() > 10 * 1024 * 1024) {
            ExceptionUtil.rollback("文件过大，无法解析！");
        }

        // 获取文件响应头
        Header[] contentDispositionHeader = response.getHeaders("Content-Disposition");
        String contentDisposition = ArrayUtils.isEmpty(contentDispositionHeader) ? null : contentDispositionHeader[0].getValue();

        // 校验文件名对象
        String originalFileName = StringUtils.isBlank(contentDisposition) ? null : contentDisposition.substring(contentDisposition.indexOf("=") + 1);
        originalFileName = StringUtils.isBlank(originalFileName) ? "unknown" : originalFileName;

        // 检查临时文件目录
        File storagePath = new File(StringUtil.concat(FileUtil.getRootPath(), "/vichain_file_download"));
        if (!storagePath.exists()) {
            storagePath.mkdirs();
        }

        // 创建文件对象
        int index = originalFileName.lastIndexOf(".");
        String extend = index >= 0 ? originalFileName.substring(index) : "";
        File file = new File(StringUtil.concat(storagePath, Constants.PATH_SEPARATOR, KeyGeneratorUtil.createSerialNumber(), extend));
        if (!file.exists()) {
            file.createNewFile();
        }

        // 开始进行文件写出
        try (InputStream inputStream = httpEntity.getContent(); FileOutputStream outputStream = new FileOutputStream(file)) {
            int readLength = 0;
            byte[] reader = new byte[4096];
            while ((readLength = inputStream.read(reader)) > 0) {
                outputStream.write(reader, 0, readLength);
            }
            outputStream.flush();
        } catch (Exception e) {
            ExceptionUtil.rollback("文件下载异常！", e);
        }

        // 文件写出完成，返回响应结果
        Map<String, String> resMap = new HashMap<>();
        resMap.put("originalFileName", originalFileName);
        resMap.put("filePath", file.getPath());
        return JsonUtil.toJsonString(Result.createWithModel(null, resMap));
    }
}
