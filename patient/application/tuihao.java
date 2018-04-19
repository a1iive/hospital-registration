package application;

import javafx.beans.property.SimpleStringProperty;

public class tuihao {
	private SimpleStringProperty ghbh = new SimpleStringProperty();

    private SimpleStringProperty hzbh = new SimpleStringProperty();

    private SimpleStringProperty ysbh = new SimpleStringProperty();

    public tuihao(String ghbh, String hzbh, String ysbh)
    {
    	setghbh(ghbh);
        sethzbh(hzbh);
        setysbh(ysbh);
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

    public String gethzbh()
    {
        return hzbh.get();
    }

    public SimpleStringProperty hzbhProperty()
    {
        return hzbh;
    }

    public void sethzbh(String hzbh)
    {
        this.hzbh.set(hzbh);
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

}
