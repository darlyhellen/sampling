/**
 * 
 */
package com.xiangxun.sampling.bean;

import java.util.ArrayList;

/**
 * 登录用户监控信息
 */
public class LoginInfo {
	public static boolean isLogined = false;
	public static boolean isOffLine = false;
	public static boolean isNeedUpdateAddressbook = false;
	public static String sessionId = "";
	public static int addressbookVersion = 0;
	public static int messageType = 0;
	public static boolean isViodataUpLoaded = false;
	public static ArrayList<String> permissions = new ArrayList<String>();
}
