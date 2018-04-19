package ysmcview;

public class ysmcclass {
	private String ysbh=new String();
	private String ysmc=new String();
	private String pyzs=new String();
	public ysmcclass(String ysbh,String ysmc,String pyzs) {
		setysbh(ysbh);
		setysmc(ysmc);
		setpyzs(pyzs);
	}
	public void setysbh(String ysbh) {
		this.ysbh=ysbh;
	}
	public String getysbh() {
		return this.ysbh;
	}
	public void setysmc(String ysmc) {
		this.ysmc=ysmc;
	}
	public String getysmc() {
		return this.ysmc;
	}
	public void setpyzs(String pyzs) {
		this.pyzs=pyzs;
	}
	public String getpyzs() {
		return this.pyzs;
	}
}
