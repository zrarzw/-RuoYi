package com.ruoyi.workflower.utils;
/** 
 * @author zoushaohua
 * @version 创建时间：2016年2月23日 下午1:51:18 
 * 类说明 系统状态返回编码
 */
public enum SysStatus {
	
	/** 没有数据 */
	NO_DATA(10000,"未查询到数据！"),
	
	/** 操作成功 */
	SUCCESS(10001,"操作成功！"),
	
	/** 操作失败 */
	FAIL(10002,"操作失败！"),
	
	/** 服务器错误 */
	ERROR(10003,"服务器错误！"),
	
	/** 服务器异常 */
	EXCEPTION(10004,"系统异常！"),
	
	/** 参数不全 */
	PARAM_INCOMPLETE(10005,"参数不全！"),
	
	/** 参数不合法 */
	PARAM_ILLEGAL(10006,"参数不合法！"),
	
	/** 重复插入数据 */
	REPEAT_DATA(10007,"重复插入数据！"),
	
	/** 警告提示 */
	WARN_NOTICE(10008,"警告提示！"),
	
	/** session错误 */
	SESSION_ERROR(10009,"session错误!"),
	
	/** 频率限制 */
	FREQ_LIMIT(10416,"频率限制！"),
	
	/** 权限不足 */
	NO_AUTH(10405,"权限不足！"),
	
	/** Token过期 */
	TOKEN_EXPIRE(10406,"Token过期！"),
	
	/** 请求超时 */
	REQUEST_TIMEOUT(10408,"请求超时！"),
	
	/** 未知异常 */
	OTHER_ERROR(19999,"未知异常！");
	
	/** 状态编码 end */
	
	/** 返回状态码*/
	private int status ;
	
	/** 返回消息*/
	private String msg ;
	
	public int getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	private SysStatus(int status, String msg){
		this.status = status ;
		this.msg = msg ;
	}

}
