package application;
import javafx.beans.property.SimpleStringProperty;

public class listview1 {
	private SimpleStringProperty ghbh = new SimpleStringProperty();
	private SimpleStringProperty brmc = new SimpleStringProperty();
	private SimpleStringProperty ghrq = new SimpleStringProperty();
	private SimpleStringProperty hzlb = new SimpleStringProperty();
	
	public listview1(String ghbh, String brmc, String ghrq,String hzlb)
    {
    	setghbh(ghbh);
    	setbrmc(brmc);
    	setghrq(ghrq);
    	sethzlb(hzlb);
    }

    public String getghbh()
    {
        return ghbh.get();
    }

    public SimpleStringProperty ghbhProperty()
    {
        return ghbh;
    }
    
    public void setghbh(String ghbh)
    {
        this.ghbh.set(ghbh);
    }

    public String getbrmc()
    {
        return brmc.get();
    }

    public SimpleStringProperty brmcProperty()
    {
        return brmc;
    }
    
    public void setbrmc(String brmc)
    {
        this.brmc.set(brmc);
    }
    
    public String getghrq()
    {
        return ghrq.get();
    }

    public SimpleStringProperty ghrqProperty()
    {
        return ghrq;
    }
    
    public void setghrq(String ghrq)
    {
        this.ghrq.set(ghrq);
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
    
}
