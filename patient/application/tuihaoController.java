package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;

public class tuihaoController {
	@FXML
	private Button button1;
	@FXML
	private Font x1;
	@FXML
	private Button button2;
	@FXML
	private Button button3;
	@FXML
	private TableView<tuihao> TableView;
	@FXML
	private TableColumn<tuihao,String> GHBH;
	@FXML
	private TableColumn<tuihao,String> HZBH;
	@FXML
	private TableColumn<tuihao,String> YSBH;
    private String brbh;
	private final ObservableList<tuihao> data
    = FXCollections.observableArrayList();
	private tuihao cache=null;
	public void init(String brbh) {
		data.clear();
		this.brbh=brbh;
		GHBH.setCellValueFactory(new PropertyValueFactory<tuihao, String>("ghbh"));
		HZBH.setCellValueFactory(new PropertyValueFactory<tuihao, String>("hzbh"));
		YSBH.setCellValueFactory(new PropertyValueFactory<tuihao, String>("ysbh"));
		TableView.setItems(data);
		String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=db_hos";
	    String USER="sa";
	    String PASSWORD="l.y53543";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection con = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM T_GHXX WHERE BRBH= '"+this.brbh+"' AND THBZ=0");                   
            //System.out.println("open success");
            if(rs.next()) {
            	String str1=rs.getString("GHBH").trim();
                String str2=rs.getString("HZBH").trim();
            	String str3=rs.getString("YSBH").trim();            	          	
            	data.add(new tuihao(str1,str2,str3));
            	while(rs.next()) {
            		str1=rs.getString("GHBH").trim();
                    str2=rs.getString("HZBH").trim();
                	str3=rs.getString("YSBH").trim();            	          	
                	data.add(new tuihao(str1,str2,str3));
            	}
            	}
            else {
            	Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Information Dialog");
        		alert.setHeaderText(null);
        		alert.setContentText("您并未挂号!");
        		alert.showAndWait();
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		conflistener();
		
	}
	//事件监听
	public void conflistener() {
		TableView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				selectedItem();
			}
		});
		TableView.setOnKeyPressed(new EventHandler<KeyEvent>()
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
	//取选中项
	public void selectedItem() {
		cache=TableView.getSelectionModel().getSelectedItem();
		System.out.println(cache.getghbh());
	}
	// Event Listener on Button[#button1].onAction
	@FXML
	public void eventbutton1(ActionEvent event) {
		if(cache!=null) {
		String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=db_hos";
	    String USER="sa";
	    String PASSWORD="l.y53543";
	    Connection con=null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(URL,USER,PASSWORD);
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);//保证不读脏，可重复读，不可幻读，事务隔离级别最高
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            con.setAutoCommit(false);
            stmt.executeUpdate("UPDATE T_GHXX SET THBZ=1 WHERE GHBH='"+cache.getghbh()+"' AND HZBH='"+cache.gethzbh()+"' AND YSBH='"+cache.getysbh()+"'");
            PreparedStatement pstmt=con.prepareStatement("UPDATE T_GHXX SET GHRC=GHRC-1 WHERE HZBH='"+cache.gethzbh()+"'");
            pstmt.execute();
            con.commit();
            con.setAutoCommit(true);
            pstmt.close();
            stmt.close();
            con.close();
            Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("退号成功!");
    		alert.showAndWait();
        } catch (SQLException e) {
        	try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Information Dialog");
    		alert.setHeaderText(null);
    		alert.setContentText("请选中退号项!");
    		alert.showAndWait();
		}
	}
	// Event Listener on Button[#button2].onAction
	@FXML
	public void eventbutton2(ActionEvent event) {
		Stage stage=(Stage)button2.getScene().getWindow();
		stage.close();
	}
	@FXML
	public void eventbutton3(ActionEvent event) {
		init(brbh);
	}
}
