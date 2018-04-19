package hzlbview;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.Window;

public class hzlbtext {
	private TextField textField;
	private String ysmc;
	private final static int LIST_MAX_SIZE = 6;
	private final static int LIST_CELL_HEIGHT = 24;
	/** 用来存储显示 出来的信息列表 */
	private ObservableList<String> showCacheDataList = FXCollections.<String> observableArrayList();
	private ObservableList<String> CacheDataList1 = FXCollections.<String> observableArrayList();
	private ObservableList<String> CacheDataList2 = FXCollections.<String> observableArrayList();

	/** 监听输入框的内容 */
	private SimpleStringProperty inputContent = new SimpleStringProperty();

	/** 输入内容后显示的pop */
	private Popup popShowList = new Popup();

	/** 输入内容后显示的提示信息列表 */
	private ListView<String> autoTipList = new ListView<String>();
	
	public hzlbtext(TextField textField,String ysmc)
	{
		this.textField = textField;
		this.ysmc=ysmc;
		this.CacheDataList1.addAll("专家号","普通号");
		this.CacheDataList2.addAll("普通号");
	}
	
	public void init() {
		setCacheDataList();
		configure();
		showTipPop();
		confListener();
	}
	
	public void setCacheDataList()
	{
		String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=db_hos";
	    String USER="sa";
	    String PASSWORD="l.y53543";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection con = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM T_KSYS WHERE YSMC ='"+ysmc.trim()+"'");
            if(rs.next()) {      
            	if(rs.getBoolean("SFZJ")) {
            		showCacheDataList.clear();
            		showCacheDataList.addAll(CacheDataList1);
            	}
            	else {
            		showCacheDataList.clear();
            		showCacheDataList.addAll(CacheDataList2);
            	}
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		
	}
	/**
	 * 
	 * <p>
	 * 配置组建
	 * </p>
	 * 
	 */
	private void configure()
	{
		
		popShowList.setAutoHide(true);
		popShowList.getContent().add(autoTipList);
  		autoTipList.setItems(showCacheDataList);
	}
	/**
	 * 
	 * <p>
	 * 添加监听事件
	 * </p>
	 * 
	 */
	private void confListener()
	{
		textField.textProperty().bindBidirectional(inputContent);

		inputContent.addListener(new ChangeListener<String>()
		{

			@Override
			public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue)
			{
				configureListContext(newValue);    //当文本框中内容发生变化时会触发此事件，对文本框中内容进行匹配
			}
		});

		autoTipList.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				selectedItem();
			}
		});

		autoTipList.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				if(event.getCode() == KeyCode.ENTER) 
				{
					selectedItem();
				}
			}
		});
	}
	/**
	 * 
	 * <p>
	 * 获取选中的list内容到输入框
	 * </p>
	 */
	private void selectedItem() {
		if(autoTipList.getSelectionModel().getSelectedItem()!=null)
		inputContent.set(autoTipList.getSelectionModel().getSelectedItem());
		else inputContent.set("");
		textField.end();
		popShowList.hide();
	}
	
	/**
	 * 
	 * <p>
	 * 根据输入的内容来配置提示信息
	 * </p>
	 * 
	 */
	private void configureListContext(String tipContent)
	{
		if(tipContent!=null) {
		this.setCacheDataList();
		if(!showCacheDataList.isEmpty()) {
			showTipPop();
		} else {
			popShowList.hide();
		}
		}
	}
	/**
	 * 
	 * <p>
	 * 显示pop
	 * </p>
	 */
	public final void showTipPop()
	{
		autoTipList.setPrefWidth(textField.getWidth() - 3);
		if(showCacheDataList.size() < LIST_MAX_SIZE) {
			autoTipList.setPrefHeight(showCacheDataList.size() * LIST_CELL_HEIGHT + 3);
		} else {
			autoTipList.setPrefHeight(LIST_MAX_SIZE * LIST_CELL_HEIGHT + 3);
		}
		Window window = textField.getScene().getWindow();
		Scene scene = textField.getScene();
		Point2D fieldPosition = textField.localToScene(0, 0);
		popShowList.show(window, window.getX() + fieldPosition.getX() + scene.getX(), window.getY() + fieldPosition.getY() + scene.getY() + textField.getHeight());
		autoTipList.getSelectionModel().clearSelection();
		autoTipList.getFocusModel().focus(-1);
	}
}
