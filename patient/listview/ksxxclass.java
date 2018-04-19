package listview;

public class ksxxclass {
	private String ksbh=new String();
	private String ksmc=new String();
	private String pyzs=new String();
	public ksxxclass(String ksbh,String ksmc,String pyzs) {
		setksbh(ksbh);
		setksmc(ksmc);
		setpyzs(pyzs);
	}
	public void setksbh(String ksbh) {
		this.ksbh=ksbh;
	}
	public String getksbh() {
		return this.ksbh;
	}
	public void setksmc(String ksmc) {
		this.ksmc=ksmc;
	}
	public String getksmc() {
		return this.ksmc;
	}
	public void setpyzs(String pyzs) {
		this.pyzs=pyzs;
	}
	public String getpyzs() {
		return this.pyzs;
	}
}

