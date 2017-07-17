package com.xiangxun.sampling.common.http;

import android.content.Context;

import com.xiangxun.sampling.common.EncodeTools;
import com.xiangxun.sampling.common.Tools;


/**
 * @package: com.xiangxun.request
 * @ClassName: ApiUrl.java
 * @Description: 所有请求方法url拼接
 * @author: HanGJ
 * @date: 2015-7-30 下午4:59:58
 */
public class ApiUrl {
	private static Api api;

	private static void init(Context context) {
		api = new Api();
	}

	// 登陆
	public static String login(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadlogin).append("/login").toString()));
	}
	// 采样计划获取方案接口
	public static String planning(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadlogin).append("/server/land/sheme/queryByFinish").toString()));
	}
	// 采样计划获取点位接口
	public static String point(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadlogin).append("/server/land/point/queryBySchemeId").toString()));
	}
	// 采样点位新增接口
	public static String addPoint(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadlogin).append("/server/land/point/doAdd").toString()));
	}
	// 采样点位修改接口
	public static String changePoint(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadlogin).append("/server/land/point/doUpdate").toString()));
	}


	public static String updateVersion(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/app/getNewVersion").toString()));
	}

	public static String getInitData(Context context){
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/dic/query").toString()));
	}

	public static String getCompanyList(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/enterprise/simpleAll/").toString()));
	}

	public static String getCompanyDetail(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/enterprise/detail").toString()));
	}

	public static String changePassword(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/account/updateP/").toString()));
	}

	public static String getEmergencyOrgUrl(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/eme/org/query").toString()));
	}

	public static String getEmergencyMaterialUrl(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/eme/materials/query").toString()));
	}

	public static String AddAccident(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/record/doAdd/").toString()));
	}

	public static String AddDanger(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/danger/doAdd/").toString()));
	}

	public static String AddIllegal(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/info/doAdd/").toString()));
	}

	public static String getCaseList(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/rectify/queryByCode/").toString()));
	}

	public static String AddCaseInfo(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/rectify/doAdd").toString()));
	}

	public static String searchCaseList(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/info/query/").toString()));
	}

	public static String searchFileList(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/file/query/").toString()));
	}

	public static String searchFileDetail(Context context) {
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/file/detail/").toString()));
	}

	public static String getContactList(Context context){
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/device/queryAll").toString()));
	}

	public static String getFileCaseList(Context context){
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/file/queryByCode/").toString()));
	}

	public static String addFile(Context context){
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/file/doAdd/").toString()));
	}

	public static String updateFile(Context context){
		init(context);
		return EncodeTools.Utf8URLencode(EncodeTools.getEnUrl(Tools.getSB().append(api.urlHeadMobile).append("/server/les/file/doUpdate/").toString()));
	}
}
