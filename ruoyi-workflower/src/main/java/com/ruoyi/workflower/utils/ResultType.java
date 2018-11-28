package com.ruoyi.workflower.utils;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zoushaohua
 * @createTime 创建时间：2016年2月23日 下午1:49:26
 * 类说明 接口返回的对象
 */
public class ResultType implements Serializable {

	private static final long serialVersionUID = -3178655848915565716L;

	/** 返回状态码,使用系统定义的常量 */
	public int status;

	/** 提示信息 */
	public String msg;

	/** 返回数据集，可以是各种类型 */
	public Object data;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ResultType() {
	}

	public ResultType(int status, String msg, Object data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public ResultType(int status, String msg) {
		this.status = status;
		this.msg = msg;
		this.data = null;
	}

	/** */
	public static ResultType getError(String msg) {
		return new ResultType(SysStatus.ERROR.getStatus(), msg);
	}

	/** 操作成功 */
	public static ResultType getSuccess() {
		return new ResultType(SysStatus.SUCCESS.getStatus(),SysStatus.SUCCESS.getMsg());
	}
	
	public static ResultType getSuccess(String msg) {
		return new ResultType(SysStatus.SUCCESS.getStatus(),msg);
	}
	
	public static ResultType getSuccess(Object data) {
		return new ResultType(SysStatus.SUCCESS.getStatus(), SysStatus.SUCCESS.getMsg(), data);
	}
	
	public static ResultType getSuccess(String msg, Object data) {
		return new ResultType(SysStatus.SUCCESS.getStatus(), msg, data);
	}
	
	public static ResultType getNoDataSuccess(String msg) {
		return new ResultType(SysStatus.SUCCESS.getStatus(), msg);
	}
	
	/** 系统异常 */
	public static ResultType getException() {
		return new ResultType(SysStatus.EXCEPTION.getStatus(),SysStatus.EXCEPTION.getMsg());
	}
	
	public static ResultType getException(String msg) {
		return new ResultType(SysStatus.EXCEPTION.getStatus(),msg);
	}

	/** 操作失败 */
	public static ResultType getFail() {
		return new ResultType(SysStatus.FAIL.getStatus(),SysStatus.FAIL.getMsg());
	}

	public static ResultType getFail(String msg) {
		return new ResultType(SysStatus.FAIL.getStatus(), msg);
	}

	/** 权限不足 */
	public static ResultType getNoAuth(){
		return new ResultType(SysStatus.NO_AUTH.getStatus(), SysStatus.NO_AUTH.getMsg());
	}
	
	public static ResultType getNoAuth(String msg){
		return new ResultType(SysStatus.NO_AUTH.getStatus(), msg);
	}
	
	/** 参数不合法 */
	public static ResultType getParamIllegal(){
		return new ResultType(SysStatus.PARAM_ILLEGAL.getStatus(), SysStatus.PARAM_ILLEGAL.getMsg());
	}
	
	public static ResultType getParamIllegal(String msg){
		return new ResultType(SysStatus.PARAM_ILLEGAL.getStatus(), msg);
	}

	public static ResultType getChatName(String chatName) {
		Map<String, String> map = new HashMap<>();
		map.put("chatName", chatName);
		return new ResultType(SysStatus.SUCCESS.getStatus(), "会话名称", map);
	}

	/** 未查询到数据 */
	public static ResultType getNoData() {
		return new ResultType(SysStatus.NO_DATA.getStatus(),SysStatus.NO_DATA.getMsg());
	}

}
