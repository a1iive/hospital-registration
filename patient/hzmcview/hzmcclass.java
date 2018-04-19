package hzmcview;

public class hzmcclass {
	private String hzbh=new String();
	private String hzmc=new String();
	private String pyzs=new String();
	public hzmcclass(String hzbh,String hzmc,String pyzs) {
		sethzbh(hzbh);
		sethzmc(hzmc);
		setpyzs(pyzs);
	}
	public void sethzbh(String hzbh) {
		this.hzbh=hzbh;
	}
	public String gethzbh() {
		return this.hzbh;
	}
	public void sethzmc(String hzmc) {
		this.hzmc=hzmc;
	}
	public String gethzmc() {
		return this.hzmc;
	}
	public void setpyzs(String pyzs) {
		this.pyzs=pyzs;
	}
	public String getpyzs() {
		return this.pyzs;
	}
}
