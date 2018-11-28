package com.ruoyi.workflower.utils;

import com.github.pagehelper.Page;

import java.util.List;

public class LeavePage<T>  extends PageParam{

	private long total;	// 总条目数

	private List<T> rows;	// 数据


	public LeavePage(Page<T> page) {
		this.rows = page;
		super.offset = page.getPageNum();
		super.limit = page.getPageSize();
		this.total = page.getTotal();
	}

	public LeavePage() {
		
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long l) {
		this.total = l;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	
}
