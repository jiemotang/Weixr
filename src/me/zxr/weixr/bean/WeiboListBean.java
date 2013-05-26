package me.zxr.weixr.bean;

import java.util.ArrayList;
import java.util.List;

public class WeiboListBean {
	private List<WeiboBean> statuses = new ArrayList<WeiboBean>();

	public List<WeiboBean> getStatuses() {
		return statuses;
	}

	public void setStatuses(List<WeiboBean> statuses) {
		this.statuses = statuses;
	}
	
	
}
