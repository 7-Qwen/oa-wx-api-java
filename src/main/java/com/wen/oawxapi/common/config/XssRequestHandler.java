package com.wen.oawxapi.common.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: 7wen
 * @Date: 2023-05-19 14:38
 * @description: 处理xss脚本攻击对请求参数进行转义处理
 */

public class XssRequestHandler extends HttpServletRequestWrapper {

    public XssRequestHandler(HttpServletRequest request) {
        super(request);
    }


    /**
     * 转义header
     */
    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        if (StrUtil.isNotEmpty(header)) {
            header = HtmlUtil.filter(header);
        }
        return header;
    }


    /**
     * 转义输入流
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        ServletInputStream inputStream = super.getInputStream();
        InputStreamReader ir = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(ir);
        //声明字符拼接
        StringBuffer sb = new StringBuffer();
        String readLine = br.readLine();
        while (StrUtil.isNotEmpty(readLine)) {
            //转义
            HtmlUtil.filter(readLine);
            sb.append(readLine);
            readLine = br.readLine();
        }
        //关闭流
        br.close();
        ir.close();
        inputStream.close();
        //将获取到新的流数据转换为json数据以及map容器
        Map<String, Object> map = JSONUtil.parseObj(sb.toString());
        Map<String, Object> filterMap = new HashMap<>(map.size());

        for (String key : map.keySet()) {
            if (map.get(key) instanceof String) {
                filterMap.put(key, HtmlUtil.filter((String) map.get(key)));
            } else {
                filterMap.put(key, map.get(key));
            }
        }
        //将转义后的map转换为json
        String filterStr = JSONUtil.toJsonStr(filterMap);
        //将新的字符串序列转换为流
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(filterStr.getBytes(StandardCharsets.UTF_8));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }


    /**
     * 转义参数
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StrUtil.isNotEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }


    /**
     * 转义参数2
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = super.getParameterMap();
        //声明转义后的容器
        Map<String, String[]> filterMap = new LinkedHashMap<>();
        for (String key : parameterMap.keySet()) {
            String[] strings = parameterMap.get(key);
            for (int i = 0; i < strings.length; i++) {
                if (StrUtil.isNotEmpty(strings[i])) {
                    strings[i] = HtmlUtil.filter(strings[i]);
                }
            }
            filterMap.put(key, strings);
        }
        return filterMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (StrUtil.isNotEmpty(values[i])) {
                    values[i] = HtmlUtil.filter(values[i]);
                }
            }
        }
        return values;
    }
}
