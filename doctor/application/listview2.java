package application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
public class listview2 {
	private SimpleStringProperty ksmc = new SimpleStringProperty();
	private SimpleStringProperty ysbh = new SimpleStringProperty();
	private SimpleStringProperty ysmc = new SimpleStringProperty();
	private SimpleStringProperty hzlb = new SimpleStringProperty();
	private SimpleIntegerProperty ghrc = new SimpleIntegerProperty();
	private SimpleIntegerProperty srhj = new SimpleIntegerProperty();
	public listview2(String ksmc, String ysbh, String ysmc,String hzlb,Integer ghrc,Integer srhj)
    {
    	setksmc(ksmc);
    	setysbh(ysbh);
    	setysmc(ysmc);
    	sethzlb(hzlb);
    	setghrc(ghrc);
    	setsrhj(srhj);
    }
	public String getksmc()
    {
        return ksmc.get();
    }

    public SimpleStringProperty ksmcProperty()
    {
        return ksmc;
    }
    
    public void setksmc(String ksmc)
    {
        this.ksmc.set(ksmc);
    }
    
    public String getysbh()
    {
        return ysbh.get();
    }

    public SimpleStringProperty ysbhProperty()
    {
        return ysbh;
    }
    
    public void setysbh(String ysbh)
    {
        this.ysbh.set(ysbh);
    }
    
    public String getysmc()
    {
        return ysmc.get();
    }

    public SimpleStringProperty ysmcProperty()
    {
        return ysmc;
    }
    
    public void setysmc(String ysmc)
    {
        this.ysmc.set(ysmc);
    }
    
    public String gethzlb()
    {
        return hzlb.get();
    }

    public SimpleStringProperty hzlbProperty()
    {
        return hzlb;
    }
    
    public void sethzlb(String hzlb)
    {
        this.hzlb.set(hzlb);
    }
    
    public Integer getghrc()
    {
        return ghrc.get();
    }

    public SimpleIntegerProperty ghrcProperty()
    {
        return ghrc;
    }
    
    public void setghrc(Integer ghrc)
    {
        this.ghrc.set(ghrc);
    }
    
    public Integer getsrhj()
    {
        return srhj.get();
    }

    public SimpleIntegerProperty srhjProperty()
    {
        return srhj;
    }
    
    public void setsrhj(Integer srhj)
    {
        this.srhj.set(srhj);
    }
}
