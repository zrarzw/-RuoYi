package com.ruoyi.workflower.utils;

/**
 * 
 * 查询分页的条件
 * @author shixianjie
 * 下午4:38:53
 */
public class PageParam {
	
	
	protected String order;	// 排序方式

	protected int offset;	// 页码

	protected int limit;		// 每页条目数

	protected int size;		// ?

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	
}
