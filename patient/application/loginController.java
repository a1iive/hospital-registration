package application;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.sql.*;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.JavaFXBuilderFactory;

public class loginController {
	@FXML
	private Font x1;
	@FXML
	private TextField name;
	@FXML
	private PasswordField passward;
	@FXML
	private Button button1;
	@FXML
	private Font x2;
	@FXML
	private Button button2;
	@FXML
	private Button button3;
	public void init() {
		name.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event)
			{
				if(event.getCode() == KeyCode.ENTER) 
				{
					passward.requestFocus();
				}
			}
		});
		name.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event)
			{
				if(event.getCode() == KeyCode.F1) 
				{
					passward.requestFocus();
				}
			}
		});
		passward.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event)
			{
				if(event.getCode() == KeyCode.ENTER) 
				{
					button1.requestFocus();
				}
			}
		});
		button1.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event)
			{
				if(event.getCode() == KeyCode.ENTER) 
				{
					eventbutton1();
				}
			}
		});
	}
	@FXML
	public void eventbutton1(){
		
		int flag=0;
		String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=db_hos";
	    String USER="sa";
	    String PASSWORD="l.y53543";
	    Connection con=null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM T_BRXX WHERE BRBH='"+name.getText()+"'");  
            
            if(rs.next()) {
            	//String str1=rs.getString(1).trim();
            	java.math.BigDecimal ycfy=rs.getBigDecimal("YCJE");
            	String str3=rs.getString("DLKL").trim();            	          	
            	if(passward.getText().equals(str3)) {
            		 //校准系统时间与数据库时间
                    PreparedStatement pstmt=con.prepareStatement("SELECT GETDATE() as serverTime");
                	ResultSet time=pstmt.executeQuery();
                	time.next();
                	Runtime run=Runtime.getRuntime();  
                    try{  
                        run.exec("cmd /c time "+time.getTime("serverTime"));  
                        System.out.println("设置时间成功");   
                    }catch(IOException e){  
                        System.out.println(e.getMessage());  
                    }
            		flag=1;
            		System.out.println("right pass");
            		String sql="UPDATE T_BRXX SET DLRQ = GetDate() WHERE BRBH = "+name.getText().trim();       	
            		int ret= stmt.executeUpdate(sql);  
            		System.out.println("update "+ret+" success");
            		//挂号界面
            		try {	
            			URL location = getClass().getResource("shiyan2.fxml");
            	        FXMLLoader fxmlLoader = new FXMLLoader();
            	        fxmlLoader.setLocation(location);
            	        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            	        Parent anotherRoot = fxmlLoader.load();
                    	//Parent anotherRoot=FXMLLoader.load(getClass().getResource("shiyan2.fxml"));
                    	Stage anotherStage = new Stage();
                    	Scene scene=new Scene(anotherRoot);
                        anotherStage.setTitle("guahao");
                        anotherStage.setScene(scene);
                        ghController controller = fxmlLoader.getController();   //获取Controller的实例对象                                               
                        if(ycfy.compareTo(new java.math.BigDecimal("0"))>0) {
                        	controller.jkje_visitable();                 
                        }
                        controller.get_brbh(name.getText());
                        scene.focusOwnerProperty().addListener(
                                (prop, oldNode, newNode) -> placeMarker(controller));                     
                        anotherStage.show();
                        button1.getScene().getWindow().hide();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
            	}
            	else {
            		flag=1;
            		System.out.println("miss pass");
            		Alert alert = new Alert(AlertType.INFORMATION);
            		alert.setTitle("Information Dialog");
            		alert.setHeaderText(null);
            		alert.setContentText("密码错误!");
            		alert.showAndWait();
            	}
            }
            if(flag==0) {
            	Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Information Dialog");
        		alert.setHeaderText(null);
        		alert.setContentText("用户名错误!");
        		alert.showAndWait();
            }
            else flag=0;
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	public void placeMarker(ghController controller) {
		//Controller中写的初始化方法
        controller.init();
	}
	
	
	@FXML
	public void eventbutton2(){
        name.clear();
        passward.clear();
    }
	@FXML
	public void eventbutton3(){
		Stage stage=(Stage)button3.getScene().getWindow();
		stage.close();
    }

}
