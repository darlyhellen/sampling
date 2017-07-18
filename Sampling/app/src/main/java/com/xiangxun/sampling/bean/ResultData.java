package com.xiangxun.sampling.bean;

import java.io.Serializable;
import java.util.List;

public class ResultData {

	public static class ImageBean {
		public String file_id;
		public String prefix;
		public String url;
	}

	public static class UserInfos {
		public String id;		//用户ID
		public String account;	//账号
		public String name;		//用户名
		public String deptId;	//部门ID
		public String deptName;	//部门名称
		public String imei;		//设备串号
	}

	public static class LoginData {
		public UserInfos result;
		public int resCode;
		public String resDesc;
	}

	public static class DataTypeList {
		public List<Type> lesDanger;		//隐患类型
		public List<Type> lesType;			//违法类型
		public List<Type> lesAccidentType;	//事故类型
		public List<Type> lesAccidentLevel;// 事故等级
	}

	public static class InitData {
		public DataTypeList result;	//黑名单类型
		public String resCode;
		public String resDesc;
	}


	public static class Company {
		public String enterCode;		//企业编码
		public String enterName;		//企业名称
	}

	public static class CompanyList {
		public List<Company> result;	//企业列表
		public String resCode;
		public String resDesc;
	}

	public static class ImageFile implements Serializable {
		public String businessType;		//lesinfo
		public String fileName;			//500_wKgH4VhOHb6ABjrrAAP76RLV3FM610.jpg
		public String filePath;			//http://193.169.100.205:8080/epms/upload/files/lesinfo/20170228/170228103657245691e93b62714c8d07.jpg
	}

	public static class EnvCase implements Serializable {
		public String id;
		public String enterCode;		//企业编码
		public String enterName;		//企业名称
		public String lesCode;			//违法案件编码
		public String typeCode;			//违法类型
		public String place;			//违法地址
		public String times;			//违法时间
		public String explain;			//违法简介
		public String createId;			//创建人
		public String createTime;		//创建时间
		public List<ImageFile> files;
	}

	public static class CaseList {
		public List<EnvCase> result;	//违法隐患列表
		public String resCode;
		public String resDesc;
	}

	public static class FileAll {
		public List<ImageFile> lesfile_FCJD;
		public List<ImageFile> lesfile_SDHZ;
		public List<ImageFile> lesfile_NBSP;
		public List<ImageFile> lesfile_XWBL;
		public List<ImageFile> lesfile_XCZP;
		public List<ImageFile> lesfile_LASP;
		public List<ImageFile> lesfile_HYJY;
		public List<ImageFile> lesfile_JKHZ;
		public List<ImageFile> lesfile_JA;
	}

	public static class FileDetail {
		public EnvFile result;
		public String resCode;
		public String resDesc;
	}

}
