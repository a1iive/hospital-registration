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
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import listview.*;
import ysmcview.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.net.URL;

import hzlbview.*;
import hzmcview.hzmctext;

public class ghController {
	@FXML
	private Font x1;
	@FXML
	private Button button1;
	@FXML
	private Font x2;
	@FXML
	private Button button2;
	@FXML
	private Button button3;
	@FXML
	private Button button4;
	@FXML
	private TextField KSMC;
	@FXML
	private TextField YSXM;
	@FXML
	private TextField HZLB;
	@FXML
	private TextField HZMC;
	@FXML
	private TextField JKJE;
	@FXML
	private TextField YJJE;
	@FXML
	private TextField ZLJE;
	@FXML
	private TextField GHHM;
	private String brbh;
    public void jkje_visitable() {
    	JKJE.setVisible(false);
    	//System.out.println(brbh);
    }
    public void get_brbh(String brbh) {
    	this.brbh=brbh;
    }
	public void init() {
				
		if(KSMC.isFocused()&&(KSMC.getText()==null||(KSMC.getText()!=null&&KSMC.getText().isEmpty()))) {
		textlistview auto=new textlistview(KSMC);
		auto.init();
		}
		if(YSXM.isFocused()) {
			//System.out.println(KSMC.getText());
			if(KSMC.getText()==null) {
				//System.out.println("error");
				ysmctext ys_text=new ysmctext(YSXM,"");
				ys_text.init();
			}
			else {
				ysmctext ys_text=new ysmctext(YSXM,KSMC.getText());
				ys_text.init();
			}
		}
		if(HZLB.isFocused()) {
			if(YSXM.getText()==null) {
				hzlbtext hz_text=new hzlbtext(HZLB,"");
				hz_text.init();
			}
			else {
			hzlbtext hz_text=new hzlbtext(HZLB,YSXM.getText());
			hz_text.init();
			}
		}
		if(HZMC.isFocused()) {
			if(HZLB.getText()==null||KSMC.getText()==null){
				hzmctext hzmc_text=new hzmctext(HZMC,"","");
				hzmc_text.init();
			}
			else {
			hzmctext hzmc_text=new hzmctext(HZMC,KSMC.getText(),HZLB.getText());
			hzmc_text.init();
			}
		}
		if(YJJE.isFocused()&&HZMC.getText()!=null) {
			String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=db_hos";
		    String USER="sa";
		    String PASSWORD="l.y53543";
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				Connection con = DriverManager.getConnection(URL,USER,PASSWORD);
	            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	                    ResultSet.CONCUR_READ_ONLY);
	            ResultSet rs = stmt.executeQuery("SELECT * FROM T_HZXX WHERE HZMC='"+HZMC.getText()+"'");                   
	            //System.out.println("open success");
	            if(rs.next()) {
	            	BigDecimal ghfy=rs.getBigDecimal("GHFY");              	          	
	            	YJJE.setText(ghfy.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
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
	}
	
	// Event Listener on Button[#button1].onAction
	@FXML
	public void eventbutton1(ActionEvent event) {
		if(KSMC.getText()!=null&&YSXM.getText()!=null&&HZLB.getText()!=null&&HZMC.getText()!=null) {
		StringBuffer str = new StringBuffer("empty");
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
            ResultSet rs = stmt.executeQuery("SELECT TOP 1 * FROM T_GHXX ORDER BY GHBH DESC");                   
            //System.out.println("open success");
            //取挂号编号最后一个编号
            if(rs.next()) {
            	//System.out.println("cc");
            	str=new StringBuffer(rs.getString("GHBH").trim());            	        	          	
            	}          
            ResultSet ksxx_rs=stmt.executeQuery("SELECT * FROM T_KSXX WHERE KSMC= '"+KSMC.getText()+"'");
            if(ksxx_rs.next()) {//判断所输入的科室名称
            	PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_KSYS WHERE YSMC= '"+YSXM.getText()+"'");
	            ResultSet ksys_rs=pstmt1.executeQuery();          
	            if(ksys_rs.next()) {//判断所输入的医生名称
	            	PreparedStatement pstmt2=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZMC= '"+HZMC.getText()+"'");
	            	ResultSet hzxx_rs=pstmt2.executeQuery();
	                if(hzxx_rs.next()) {//判断所输入的号种名称
	                	PreparedStatement pstmt3=con.prepareStatement("SELECT * FROM T_BRXX WHERE BRBH='"+brbh+"'");
	            		System.out.println(brbh);
	                	ResultSet brxx_rs=pstmt3.executeQuery();
	                    if(brxx_rs.next()) {   	
	                    	if(brxx_rs.getBigDecimal("YCJE").compareTo(new java.math.BigDecimal("0"))>0&&brxx_rs.getBigDecimal("YCJE").compareTo(hzxx_rs.getBigDecimal("GHFY"))>=0) {//判断金额是否足够                    		
	                    		//预存金额大于0并且比挂号费用多
	                    		if(str.toString().equals("empty")) {
	                    			//如果挂号信息表为空
		                			PreparedStatement pstmt=con.prepareStatement("INSERT INTO T_GHXX VALUES (?,?,?,?,?,?,?,GetDate())");
		                			pstmt.setString(1,"000001");
		                			pstmt.setString(2, hzxx_rs.getString("HZBH"));
		                			pstmt.setString(3, ksys_rs.getString("YSBH"));
		                			pstmt.setString(4, brbh);
		                			pstmt.setInt(5, 1);
		                			pstmt.setBoolean(6, false);
		                			pstmt.setBigDecimal(7, hzxx_rs.getBigDecimal("GHFY"));
		                			pstmt.execute();
		                			
		                			
		                			pstmt.close();
		                			JKJE.setVisible(true);
		                			Alert alert = new Alert(AlertType.INFORMATION);
	                        		alert.setTitle("Information Dialog");
	                        		alert.setHeaderText(null);
	                        		alert.setContentText("挂号成功！挂号编号为000001,预存余额"+brxx_rs.getBigDecimal("YCJE").subtract(hzxx_rs.getBigDecimal("GHFY")).doubleValue());
	                        		PreparedStatement pstmt0=con.prepareStatement("UPDATE T_BRXX SET YCJE=? WHERE BRBH= '"+brbh+"'");
	                        		pstmt0.setBigDecimal(1,brxx_rs.getBigDecimal("YCJE").subtract(hzxx_rs.getBigDecimal("GHFY")));
	                        		pstmt0.execute();
	                        		pstmt0.close();
	                        		alert.showAndWait();
		                			}	
		                		else {
		                			int ret=0;
		                			ResultSet rs0=stmt.executeQuery("SELECT * FROM T_GHXX WHERE HZBH='"+hzxx_rs.getString("HZBH")+"'");
		                			if(rs0.next()) {
		                				if(rs0.getInt("GHRC")<hzxx_rs.getInt("GHRS"))ret=1;
		                				else ret=0;
		                			}
		                			else ret=1;
		                			//int ret=stmt.executeUpdate("UPDATE T_HZXX SET GHRC=GHRC+1 WHERE HZBH='"+hzxx_rs.getString("HZBH")+"' AND GHRC<GHRS");
		                			System.out.println(""+ret+" "+hzxx_rs.getString("HZBH")+"号");
		                			if(ret>0) {//如果挂号人次小于挂号人数，即该号种挂号未满
		                				
		                			String ghbh=String.format("%06d",Integer.parseInt(str.toString())+1);//挂号编号为最大编号加1
		                			PreparedStatement pst=con.prepareStatement("SELECT * FROM T_GHXX WHERE HZBH= '"+hzxx_rs.getString("HZBH")+"' AND BRBH= '"+brbh+"' AND THBZ=0");
		                			ResultSet br_gh=pst.executeQuery();
		                			if(br_gh.next()) {
		                				Alert alert = new Alert(AlertType.INFORMATION);
		                        		alert.setTitle("Information Dialog");
		                        		alert.setHeaderText(null);
		                        		alert.setContentText("该号种您已挂过!");
		                        		alert.showAndWait();
		                			}
		                			else {
		                			PreparedStatement pstmt4=con.prepareStatement("SELECT * FROM T_GHXX WHERE HZBH= '"+hzxx_rs.getString("HZBH")+"'");
		                			ResultSet ghxx_rs=pstmt4.executeQuery();		   
		                			if(ghxx_rs.next()) {//如果该号种已经有人挂过，修改GHRC
		                			PreparedStatement pstmt=con.prepareStatement("INSERT INTO T_GHXX VALUES (?,?,?,?,?,?,?,GetDate())");
		                			pstmt.setString(1,ghbh);
		                			pstmt.setString(2, hzxx_rs.getString("HZBH"));
		                			pstmt.setString(3, ksys_rs.getString("YSBH"));
		                			pstmt.setString(4, brbh);
		                			pstmt.setInt(5, ghxx_rs.getInt("GHRC"));
		                			pstmt.setBoolean(6, false);
		                			pstmt.setBigDecimal(7, hzxx_rs.getBigDecimal("GHFY"));
		                			pstmt.executeUpdate();
		                			PreparedStatement pstmt0=con.prepareStatement("UPDATE T_GHXX SET GHRC=GHRC+1 WHERE HZBH='"+hzxx_rs.getString("HZBH")+"'");
		                			pstmt0.execute();
		                			pstmt0.close();
		                			}
		                			else {		  
		                				PreparedStatement pstmt=con.prepareStatement("INSERT INTO T_GHXX VALUES (?,?,?,?,?,?,?,GetDate())");
			                			pstmt.setString(1,ghbh);
			                			pstmt.setString(2, hzxx_rs.getString("HZBH"));
			                			pstmt.setString(3, ksys_rs.getString("YSBH"));
			                			pstmt.setString(4, brbh);
			                			pstmt.setInt(5, 1);
			                			pstmt.setBoolean(6, false);
			                			pstmt.setBigDecimal(7, hzxx_rs.getBigDecimal("GHFY"));
			                			pstmt.executeUpdate();			                					                			

		                			}
		                			ghxx_rs.close();
		                			pstmt4.close();
		                			
		                			Alert alert = new Alert(AlertType.INFORMATION);
	                        		alert.setTitle("Information Dialog");
	                        		alert.setHeaderText(null);
	                        		alert.setContentText("挂号成功！挂号编号为"+ghbh+",预存余额"+brxx_rs.getBigDecimal("YCJE").subtract(hzxx_rs.getBigDecimal("GHFY")).doubleValue());
	                        		PreparedStatement pstmt0=con.prepareStatement("UPDATE T_BRXX SET YCJE=? WHERE BRBH= '"+brbh+"'");
	                        		pstmt0.setBigDecimal(1,brxx_rs.getBigDecimal("YCJE").subtract(hzxx_rs.getBigDecimal("GHFY")));
	                        		pstmt0.execute();
	                        		pstmt0.close();
	                        		alert.showAndWait();
		                			}
		                			br_gh.close();
		                			pst.close();
		                			}
		                			else {
		                				Alert alert = new Alert(AlertType.INFORMATION);
		                        		alert.setTitle("Information Dialog");
		                        		alert.setHeaderText(null);
		                        		alert.setContentText("当前该号种挂号已满!");
		                        		alert.showAndWait();
		                			}		             			
		                			}
		                			
	                    	}
	                    	else if(brxx_rs.getBigDecimal("YCJE").compareTo(new java.math.BigDecimal("0"))>0&&brxx_rs.getBigDecimal("YCJE").compareTo(hzxx_rs.getBigDecimal("GHFY"))<0) {
	                    		//预存金额大于0但是比挂号费用少
	                    		Alert alert = new Alert(AlertType.INFORMATION);
	                    		alert.setTitle("Information Dialog");
	                    		alert.setHeaderText(null);
	                    		alert.setContentText("预存金额不足，退回全部金额共"+brxx_rs.getBigDecimal("YCJE")+"元，请重新挂号！");
	                    		stmt.executeUpdate("UPDATE T_BRXX SET YCJE=0 WHERE BRBH='"+brbh+"'");
	                    		JKJE.setVisible(true);
	                    		alert.showAndWait();
	                    	}
	                    	else if(brxx_rs.getBigDecimal("YCJE").compareTo(new java.math.BigDecimal("0"))<=0) {
	                    		BigDecimal jf=new BigDecimal(JKJE.getText());
	                    		if(jf.compareTo(hzxx_rs.getBigDecimal("GHFY"))>=0) {//如果交款金额大于挂号费用
	                    			if(str.toString().equals("empty")) {
		                    			//如果挂号信息表为空
			                			PreparedStatement pstmt=con.prepareStatement("INSERT INTO T_GHXX VALUES (?,?,?,?,?,?,?,GetDate())");
			                			pstmt.setString(1,"000001");
			                			pstmt.setString(2, hzxx_rs.getString("HZBH"));
			                			pstmt.setString(3, ksys_rs.getString("YSBH"));
			                			pstmt.setString(4, brbh);
			                			pstmt.setInt(5, 1);
			                			pstmt.setBoolean(6, false);
			                			pstmt.setBigDecimal(7, hzxx_rs.getBigDecimal("GHFY"));
			                			pstmt.execute();
			                			pstmt.close();
			                			JKJE.setVisible(true);
			                			Alert alert = new Alert(AlertType.INFORMATION);
		                        		alert.setTitle("Information Dialog");
		                        		alert.setHeaderText(null);
		                        		alert.setContentText("挂号成功！挂号编号为000001,找零"+jf.subtract(hzxx_rs.getBigDecimal("GHFY")).doubleValue()+"");	
		                        		alert.showAndWait();
			                			}	
			                		else {
			                			int ret=0;
			                			ResultSet rs0=stmt.executeQuery("SELECT * FROM T_GHXX WHERE HZBH='"+hzxx_rs.getString("HZBH")+"'");
			                			if(rs0.next()) {
			                				if(rs0.getInt("GHRC")<hzxx_rs.getInt("GHRS"))ret=1;
			                				else ret=0;
			                			}
			                			else ret=1;
			                			//int ret=stmt.executeUpdate("UPDATE T_HZXX SET GHRC=GHRC+1 WHERE HZBH='"+hzxx_rs.getString("HZBH")+"' AND GHRC<GHRS");
			                			System.out.println(""+ret+" "+hzxx_rs.getString("HZBH")+"号");
			                			if(ret>0) {//如果挂号人次小于挂号人数，即该号种挂号未满
			                				
			                			String ghbh=String.format("%06d",Integer.parseInt(str.toString())+1);//挂号编号为最大编号加1
			                			PreparedStatement pst=con.prepareStatement("SELECT * FROM T_GHXX WHERE HZBH= '"+hzxx_rs.getString("HZBH")+"' AND BRBH= '"+brbh+"' AND THBZ=0");
			                			ResultSet br_gh=pst.executeQuery();
			                			if(br_gh.next()) {
			                				Alert alert = new Alert(AlertType.INFORMATION);
			                        		alert.setTitle("Information Dialog");
			                        		alert.setHeaderText(null);
			                        		alert.setContentText("该号种您已挂过!");
			                        		alert.showAndWait();
			                			}
			                			else {
			                			PreparedStatement pstmt4=con.prepareStatement("SELECT * FROM T_GHXX WHERE HZBH= '"+hzxx_rs.getString("HZBH")+"'");
			                			ResultSet ghxx_rs=pstmt4.executeQuery();		   
			                			if(ghxx_rs.next()) {//如果该号种已经有人挂过，修改GHRC
			                			PreparedStatement pstmt=con.prepareStatement("INSERT INTO T_GHXX VALUES (?,?,?,?,?,?,?,GetDate())");
			                			pstmt.setString(1,ghbh);
			                			pstmt.setString(2, hzxx_rs.getString("HZBH"));
			                			pstmt.setString(3, ksys_rs.getString("YSBH"));
			                			pstmt.setString(4, brbh);
			                			pstmt.setInt(5, ghxx_rs.getInt("GHRC"));
			                			pstmt.setBoolean(6, false);
			                			pstmt.setBigDecimal(7, hzxx_rs.getBigDecimal("GHFY"));
			                			pstmt.executeUpdate();
			                			pstmt.close();
			                			stmt.executeUpdate("UPDATE T_GHXX SET GHRC=GHRC+1 WHERE HZBH='"+hzxx_rs.getString("HZBH")+"'");
			                			}
			                			else {		  
			                				PreparedStatement pstmt=con.prepareStatement("INSERT INTO T_GHXX VALUES (?,?,?,?,?,?,?,GetDate())");
				                			pstmt.setString(1,ghbh);
				                			pstmt.setString(2, hzxx_rs.getString("HZBH"));
				                			pstmt.setString(3, ksys_rs.getString("YSBH"));
				                			pstmt.setString(4, brbh);
				                			pstmt.setInt(5, 1);
				                			pstmt.setBoolean(6, false);
				                			pstmt.setBigDecimal(7, hzxx_rs.getBigDecimal("GHFY"));
				                			pstmt.executeUpdate();
				                			pstmt.close();

			                			}
			                			ghxx_rs.close();
			                			pstmt4.close();
			                			JKJE.setVisible(true);
			                			
			                			Alert alert = new Alert(AlertType.INFORMATION);
		                        		alert.setTitle("Information Dialog");
		                        		alert.setHeaderText(null);
		                        		alert.setContentText("挂号成功！挂号编号为"+ghbh+",找零"+jf.subtract(hzxx_rs.getBigDecimal("GHFY")).doubleValue());
		                        		alert.showAndWait();
			                			}
			                			br_gh.close();
			                			pst.close();
			                			}
			                			else {
			                				Alert alert = new Alert(AlertType.INFORMATION);
			                        		alert.setTitle("Information Dialog");
			                        		alert.setHeaderText(null);
			                        		alert.setContentText("当前该号种挂号已满!");
			                        		alert.showAndWait();
			                			}
			                			
			                			}
	                    		}
	                    		else {
	                    			Alert alert = new Alert(AlertType.INFORMATION);
	                        		alert.setTitle("Information Dialog");
	                        		alert.setHeaderText(null);
	                        		alert.setContentText("缴费金额不足!");
	                        		alert.showAndWait();
	                    		}
	                    	}
	                	}
	                    else System.out.println("病人信息出错");
	                    brxx_rs.close();
	                    }
	                hzxx_rs.close();
	            }
            else {
            	Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Information Dialog");
        		alert.setHeaderText(null);
        		alert.setContentText("医生名称不存在!");
        		alert.showAndWait();
            }
            ksys_rs.close();                   
            }
            else {
            	Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Information Dialog");
        		alert.setHeaderText(null);
        		alert.setContentText("科室名称不存在!");
        		alert.showAndWait();
            }
            con.commit();
			con.setAutoCommit(true);
            ksxx_rs.close();
            rs.close();
            stmt.close();
            con.close();
		}
		catch (SQLException e) {
			try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }
		catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		}
	}
	// Event Listener on Button[#button2].onAction
	@FXML
	public void eventbutton2(ActionEvent event) {
		HZLB.clear();
		HZMC.clear();
		YJJE.clear();
		JKJE.clear();
		ZLJE.clear();
		GHHM.clear();
		KSMC.clear();
		YSXM.clear();
	}
	// Event Listener on Button[#button3].onAction
	@FXML
	public void eventbutton3(ActionEvent event) {
		
		try {	
			URL location = getClass().getResource("tuihao.fxml");
	        FXMLLoader fxmlLoader = new FXMLLoader();
	        fxmlLoader.setLocation(location);
	        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
	        Parent anotherRoot = fxmlLoader.load();
        	Stage anotherStage = new Stage();
        	Scene scene=new Scene(anotherRoot);
            anotherStage.setTitle("guahao");
            anotherStage.setScene(scene);
            tuihaoController controller = fxmlLoader.getController();   //获取Controller的实例对象                                               
            controller.init(brbh);   
            
            anotherStage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
	}

	// Event Listener on Button[#button4].onAction
	@FXML
	public void eventbutton4(ActionEvent event) {
		Stage stage=(Stage)button4.getScene().getWindow();
		stage.close();
	}
}
