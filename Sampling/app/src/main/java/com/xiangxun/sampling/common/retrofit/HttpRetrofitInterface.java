package com.xiangxun.sampling.common.retrofit;

import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Author:Created by zhangyh2 on 2016/12/15 at 11:48.
 * Copyright (c) 2016 Organization Rich-Healthcare(D.L.) zhangyh2 All rights reserved.
 * TODO: 服务端接口对应接口实现类。
 */

public interface HttpRetrofitInterface {
    /**
     * @param route
     * @return
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

//
//    /**
//     * @param status
//     * @TODO:是否接收处理订单的接口
//     */
//    @GET("server/workorder/change/status/")
//    Observable<JsonObject> getOrder(@Query("status") String status, @Query("id") String id, @Query("reason") String reason);
//
//    /**
//     * @param args
//     * @TODO:上图片接口
//     */
//    @POST("server/workorder/change/upLoadPicture/")
//    Observable<JsonObject> upLoadImage(@Body RequestBody args);
//
//    /**
//     * @TODO:图片下载接口。
//     */
//    @GET("server/workorder/refer/watchPicture/")
//    Observable<JsonObject> downImage(@Query("id") String id);
//
//
//    /**
//     * @TODO:正常上报和异常上报接口
//     */
//    @GET("server/workorder/change/workorderUp/")
//    Observable<JsonObject> upDataOrder(@Query("status") String status, @Query("id") String id, @Query("reason") String reason);
//
//    /**
//     * @TODO:查找設備的接口
//     */
//    @GET("server/device/refer/searchDevice/")
//    Observable<JsonObject> searchDevice(@Query("type") String type, @Query("pageNo") int pageNo);
//
//    /**
//     * @TODO:巡检页面根据条件查询设备信息。
//     */
//    @GET("server/device/refer/searchOneDevice/")
//    Observable<JsonObject> searchOneDevice(@Query("type") String type, @Query("code") String code, @Query("name") String name);
//
//    /**
//     * @TODO:巡检页面参数完整,提交工单接口.
//     */
//    @POST("server/perambulate/refer/perambulateUp/")
//    Observable<JsonObject> perambulateUp(@Body RequestBody args);
//
//    /**
//     * @TODO:巡检列表页面
//     */
//    @GET("server/perambulate/refer/details/")
//    Observable<JsonObject> details(@Query("pageNo") int pageNo);
//
//    /**
//     * @param version
//     * @TODO:版本更新接口
//     */
//    @GET("server/operations/app/getNewVersion")
//    Observable<JsonObject> getVersion(@Query("version") int version);
//
//    /**
//     * @TODO:测试接口，替换接口。
//     */
//    @GET("server")
//    Observable<JsonObject> test();
//
//
//    /**
//     * @TODO:测试接口，替换接口。
//     */
//    @GET("zyh/json.json")
//    Observable<JsonObject> github();

}
