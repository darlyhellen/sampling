package com.xiangxun.sampling.common.retrofit;


import android.text.TextUtils;

import com.xiangxun.sampling.BuildConfig;
import com.xiangxun.sampling.common.dlog.DLog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author:Created by zhangyh2 on 2016/12/8 at 10:33.
 * Copyright (c) 2016 Organization Rich-Healthcare(D.L.) zhangyh2 All rights reserved.
 * TODO:
 */

public class RxjavaRetrofitRequestUtil {
    private volatile static RxjavaRetrofitRequestUtil instance;

    private static final int REQUEST_TIME = 3;
    private int interSize = 0;

    private OkHttpClient.Builder builder;
    private Api api;

    private RxjavaRetrofitRequestUtil() {
        initClient();
    }

    private void initClient() {
        api = new Api();
        builder = new OkHttpClient.Builder();
        //设置请求超时时间
        builder.readTimeout(REQUEST_TIME, TimeUnit.SECONDS);//设置读取超时时间
        builder.writeTimeout(REQUEST_TIME, TimeUnit.SECONDS);//设置写的超时时间
        builder.connectTimeout(REQUEST_TIME, TimeUnit.SECONDS);//设置连接超时时间
        //设置请求日志
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor log = new HttpLoggingInterceptor();
            log.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(log);
        }
        interSize = builder.interceptors().size();
        builder.addInterceptor(RxInterceptor.getInstance());

    }


    /**
     * @return 启动单例模式，加载进JVM时不进行初始化，调用getInstance（）初始化请求类。
     */
    public static RxjavaRetrofitRequestUtil getInstance() {
        if (instance == null) {
            synchronized (RxjavaRetrofitRequestUtil.class) {
                if (instance == null) {
                    instance = new RxjavaRetrofitRequestUtil();
                }
            }
        }
        return instance;
    }


    public HttpRetrofitInterface get() {
        //清理多余的头文件。
        while (builder.interceptors().size() > 0 && builder.interceptors().size() != interSize) {
            builder.interceptors().remove(builder.interceptors().size() - 1);
        }
        //清理多余的头文件。
        builder.addInterceptor(RxInterceptor.getInstance());
        DLog.i(builder.interceptors().size());
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(api.urlHost)
                .build();
        return retrofit.create(HttpRetrofitInterface.class);
    }


    public HttpRetrofitInterface post() {
        //清理多余的头文件。
        while (builder.interceptors().size() > 0 && builder.interceptors().size() != interSize) {
            builder.interceptors().remove(builder.interceptors().size() - 1);
        }
        //清理多余的头文件。
        builder.addInterceptor(RxInterceptor.getInstance());
        DLog.i(builder.interceptors().size());
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(api.urlHost)
                .build();
        return retrofit.create(HttpRetrofitInterface.class);
    }

    /**
     * @param m
     * @param paramsEncoding
     * @param <T>
     * @return 将对象参数集合转换为String参数
     */
    public static <T> String getParamers(T m, String paramsEncoding) {
        Map<String, String> params = changeTtoMap(m);
        if (TextUtils.isEmpty(paramsEncoding)) {
            paramsEncoding = "UTF-8";
        }
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                if (!TextUtils.isEmpty(entry.getValue())) {
                    encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                }
                encodedParams.append('&');
            }
            encodedParams.deleteCharAt(encodedParams.length() - 1);
            DLog.i(encodedParams.toString());
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }

    }

    private static <T> Map<String, String> changeTtoMap(T m) {
        HashMap<String, String> map = new HashMap<String, String>();
        // 获取实体类的所有属性
        Field[] field = m.getClass().getDeclaredFields();
        // 遍历所有属性
        for (int j = 0; j < field.length; j++) {
            // 获取属性的名字
            String name = field[j].getName();

            String value = (String) getFieldValueObj(m, name);
            if (value != null && !value.equals("")) {
                map.put(name, value);
            }
        }
        return map;
    }

    /**
     * 获取对应的属性值
     *
     * @param target 对象
     * @param fname  Filed
     * @return
     */
    private static Object getFieldValueObj(Object target, String fname) { // 获取字段值
        // 如:username 字段,getUsername()
        if (target == null || fname == null || "".equals(fname)) {// 如果类型不匹配，直接退出
            return "";
        }
        Class clazz = target.getClass();
        try { // 先通过getXxx()方法设置类属性值
            String methodname = "get" + Character.toUpperCase(fname.charAt(0))
                    + fname.substring(1);
            Method method = clazz.getDeclaredMethod(methodname); // 获取定义的方法
            if (!Modifier.isPublic(method.getModifiers())) { // 设置非共有方法权限
                method.setAccessible(true);
            }
            return (Object) method.invoke(target); // 执行方法回调
        } catch (Exception me) {// 如果get方法不存在，则直接设置类属性值
            try {
                Field field = clazz.getDeclaredField(fname); // 获取定义的类属性
                if (!Modifier.isPublic(field.getModifiers())) { // 设置非共有类属性权限
                    field.setAccessible(true);
                }
                return (Object) field.get(target); // 获取类属性值
            } catch (Exception fe) {
            }
        }
        return "";
    }

}
