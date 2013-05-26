package me.zxr.weixr.bean;

public class TokenInfoBean {
	public String uid;
	public String appkey;
	public String scope;
	public String create_at;
	public String expire_in;
	
	
	@Override
	public String toString() {
		return "TokenInfoBean [uid=" + uid + ", appkey=" + appkey + ", scope="
				+ scope + ", create_at=" + create_at + ", expire_in="
				+ expire_in + "]";
	}
	
	
}
