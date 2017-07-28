package com.xiangxun.sampling.common.retrofit;

import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Author:Created by zhangyh2 on 2016/12/15 at 11:48.
 * Copyright (c) 2016 Organization Rich-Healthcare(D.L.) zhangyh2 All rights reserved.
 * TODO: 服务端接口对应接口实现类。
 */

public interface HttpRetrofitInterface {
    /**
     * @TODO:用户登录POST接口，传递参数为<b>route</b>
     */
    //@FormUrlEncoded//添加这行注解，否则参数报错。Post请求
    @POST("/login")
    Observable<JsonObject> postlogin(@Body RequestBody route);

    /**
     * @return
     * @TODO:退出登录接口
     */
    @GET("/logout")
    Observable<JsonObject> getloginout();

    /**
     * @return
     * @TODO:修改密码接口
     */
    @POST("/server/account/updateP")
    Observable<JsonObject> postPass(@Body RequestBody route);

    /**
     * @return
     * @TODO:获取计划列表接口
     */
    @GET("/server/land/sheme/queryByFinish")
    Observable<JsonObject> planning(@Query("resTime") String resTime);

    /**
     * @return
     * @TODO:获取点位列表信息
     */
    @POST("/server/land/point/queryBySchemeId")
    Observable<JsonObject> point(@Body RequestBody body);

    /**
     * @TODO:新增点位
     */
    @POST("/server/land/point/doAdd")
    Observable<JsonObject> addPoint(@Body RequestBody body);

    /**
     * @TODO:新增点位
     */
    @POST("/server/land/point/doUpdate")
    Observable<JsonObject> updataPoint(@Body RequestBody body);

    /**
     * @TODO:现场采集页面状态修改接口
     */
    @POST("/server/land/reg/collect")
    Observable<JsonObject> senceSamply(@Body RequestBody body);

    /**
     * @TODO:现场采集页面土壤类型接口
     */
    @GET("/server/land/type/queryAll")
    Observable<JsonObject> landType();

    /**
     * @TODO:现场采集页面地区类型接口
     */
    @GET("/server/region/queryAll")
    Observable<JsonObject> region();

    /**
     * @TODO:指标查询接口
     */
    @POST("/server/land/analysis/query")
    Observable<JsonObject> analysis(@Body RequestBody body);

    /**
     * @TODO:地块异常新增功能接口
     */
    @POST("/server/land/error/doAdd")
    Observable<JsonObject> addexc(@Body RequestBody body);

    /**
     * @TODO:地块异常列表展示
     */
    @POST("/server/land/error/queryAll")
    Observable<JsonObject> hisexc(@Body RequestBody body);

    /**
     * @TODO:地块异常列表点击查看详情信息
     */
    @POST("/server/land/error/showView")
    Observable<JsonObject> hisshow(@Body RequestBody body);

    /**
     * @TODO:地块分组接口
     */
    @POST("/server/block/query")
    Observable<JsonObject> block(@Body RequestBody body);

    /**
     * @TODO:历史采样列表接口
     */
    @POST("/server/land/sheme/query")
    Observable<JsonObject> hisence(@Body RequestBody body);

    /**
     * @TODO:历史采样现场采样列表接口
     */
    @POST("/server/land/reg/select")
    Observable<JsonObject> hispoint(@Body RequestBody body);

    /**
     * @TODO:历史采样现场采样页面接口
     */
    @POST("/server/land/reg/showView")
    Observable<JsonObject> sencehispage(@Body RequestBody body);

    /**
     * @TODO:现场采样后续图片和视频上传接口
     */
    @POST("/server/land/reg/uploud")
    Observable<JsonObject> allupload(@Body RequestBody body);


    /**
     * @param version
     * @TODO:版本更新接口
     */
    @GET("server/operations/app/getNewVersion")
    Observable<JsonObject> getVersion(@Query("version") int version);

}
