package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;

import javafx.scene.control.PasswordField;

import javafx.scene.text.Font;
import javafx.stage.Stage;

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

	// Event Listener on Button[#button1].onAction
	@FXML
	public void eventbutton1(ActionEvent event) {
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM T_KSYS WHERE YSBH='"+name.getText()+"'");  
         
            if(rs.next()) {
            	//String str1=rs.getString("YSBH").trim();
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
            		String sql="UPDATE T_KSYS SET DLRQ = GetDate() WHERE YSBH = "+name.getText().trim();       	
            		int ret= stmt.executeUpdate(sql);  
            		System.out.println("update "+ret+" success");
            		//挂号界面
            		try {	
            			URL location = getClass().getResource("listshow.fxml");
            	        FXMLLoader fxmlLoader = new FXMLLoader();
            	        fxmlLoader.setLocation(location);
            	        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            	        Parent anotherRoot = fxmlLoader.load();
                    	//Parent anotherRoot=FXMLLoader.load(getClass().getResource("shiyan2.fxml"));
                    	Stage anotherStage = new Stage();
                    	Scene scene=new Scene(anotherRoot);
                        anotherStage.setTitle("Doc Query");
                        anotherStage.setScene(scene);
                        listshowController controller = fxmlLoader.getController();   //获取Controller的实例对象                                                                 
                        System.out.println(name.getText());
                        controller.init(name.getText());
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
	// Event Listener on Button[#button2].onAction
	@FXML
	public void eventbutton2(ActionEvent event) {
		name.clear();
        passward.clear();
	}
	// Event Listener on Button[#button3].onAction
	@FXML
	public void eventbutton3(ActionEvent event) {
		Stage stage=(Stage)button3.getScene().getWindow();
		stage.close();
	}
}
