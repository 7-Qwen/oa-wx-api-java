package com.wen.oawxapi.common.utils;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 7wen
 * @Date: 2023-05-18 19:08
 * @description: 服务器统一回复 实例方法不能调用静态方法
 */
public class R extends HashMap<String, Object> {

    public R() {
        put("code", HttpStatus.SC_OK);
        put("msg", "success");
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }


    /**
     * 实例变量继续调用补充
     */
    public R putMap(Map map) {
        this.putAll(map);
        return this;
    }


    /**
     * 以下为静态方法调用
     */
    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }


    public static R ok() {
        return new R();
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error() {
        return error("服务器出现异常,请联系管理员");
    }

}
