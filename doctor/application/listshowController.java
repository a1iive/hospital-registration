package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class listshowController {
	@FXML
	private Button button0;
	@FXML
	private TableView<listview1> tableview1;
	@FXML
	private TableColumn<listview1,String> GHBH1;
	@FXML
	private TableColumn<listview1,String> BRMC1;
	@FXML
	private TableColumn<listview1,String> GHRQ1;
	@FXML
	private TableColumn<listview1,String> HZLB1;
	@FXML
	private Button button1;
	@FXML
	private Font x1;
	@FXML
	private Font x2;
	@FXML
	private TableView<listview2> tableview2;
	@FXML
	private TableColumn<listview2,String> KSMC2;
	@FXML
	private TableColumn<listview2,String> YSBH2;
	@FXML
	private TableColumn<listview2,String> YSMC2;
	@FXML
	private TableColumn<listview2,String> HZLB2;
	@FXML
	private TableColumn<listview2,Integer> GHRC2;
	@FXML
	private TableColumn<listview2,Integer> SRHJ2;
	@FXML
	private TextField textfield;
	@FXML
	private Button button2;
	@FXML
	private Button button3;
	@FXML
	private TextField deadline;
	
	private String ysbh;
	private final ObservableList<listview1> data1
	= FXCollections.observableArrayList();
	private final ObservableList<listview2> data2
	= FXCollections.observableArrayList();

	public void init(String ysbh) {
		this.ysbh=ysbh;
		data1.clear();
		data2.clear();
		GHBH1.setCellValueFactory(new PropertyValueFactory<listview1, String>("ghbh"));
		BRMC1.setCellValueFactory(new PropertyValueFactory<listview1, String>("brmc"));
		GHRQ1.setCellValueFactory(new PropertyValueFactory<listview1, String>("ghrq"));
		HZLB1.setCellValueFactory(new PropertyValueFactory<listview1, String>("hzlb"));
		tableview1.setItems(data1);
		
		KSMC2.setCellValueFactory(new PropertyValueFactory<listview2, String>("ksmc"));
		YSBH2.setCellValueFactory(new PropertyValueFactory<listview2, String>("ysbh"));
		YSMC2.setCellValueFactory(new PropertyValueFactory<listview2, String>("ysmc"));
		HZLB2.setCellValueFactory(new PropertyValueFactory<listview2, String>("hzlb"));
		GHRC2.setCellValueFactory(new PropertyValueFactory<listview2, Integer>("ghrc"));
		SRHJ2.setCellValueFactory(new PropertyValueFactory<listview2, Integer>("srhj"));
		tableview2.setItems(data2);
		String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=db_hos";
	    String USER="sa";
	    String PASSWORD="l.y53543";
	    Connection con=null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);//保证不读脏，可重复读，不可幻读，事务隔离级别最高
            con.setAutoCommit(false);
            ResultSet rs = stmt.executeQuery("SELECT * FROM T_GHXX WHERE YSBH= '"+this.ysbh+"' AND THBZ=0");                   
            //System.out.println("open success");
            //病人列表初始化
            while(rs.next()) {
            	String str1=rs.getString("GHBH").trim();
            	java.sql.Timestamp date=rs.getTimestamp("RQSJ");
            	String str2=rs.getString("BRBH").trim();  
            	String str3=rs.getString("HZBH").trim();
            	String str4=null;
            	PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_BRXX WHERE BRBH='"+str2+"'");
            	ResultSet rs1=pstmt1.executeQuery();
            	rs1.next();
            	PreparedStatement pstmt2=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str3+"'");
            	ResultSet rs2=pstmt2.executeQuery();
            	rs2.next();
            	if(rs2.getBoolean("SFZJ")) {
            		str4="专家号";
            	}
            	else str4="普通号";
            	
            	data1.add(new listview1(str1,rs1.getString("BRMC"),date.toString(),str4));
                rs2.close();
                pstmt2.close();
                rs1.close();
                pstmt1.close();
            	}
            //test-demo
            /*PreparedStatement pstmt=con.prepareStatement("SELECT GETDATE() as serverTime");
        	ResultSet time=pstmt.executeQuery();
        	time.next();
        	System.out.println(time.getDate("serverTime").toString()+" 00:00:00");
        	PreparedStatement pstmt0=con.prepareStatement("SELECT * FROM T_BRXX WHERE DLRQ BETWEEN '2018-04-13 00:00:00' AND GetDate()");
            ResultSet rs0=pstmt0.executeQuery();
            while(rs0.next()) {
            	System.out.println(rs0.getString("BRMC"));
            }*/
            con.commit();
        	//收入列表初始化
            PreparedStatement pstmt=con.prepareStatement("SELECT GETDATE() as serverTime");
        	ResultSet time=pstmt.executeQuery();
        	time.next();
        	String low_t=time.getDate("serverTime").toString()+" 00:00:00";
        	String high_t=time.getDate("serverTime").toString()+" "+time.getTime("serverTime").toString();
        	textfield.setText(low_t);
        	deadline.setText(high_t);
            PreparedStatement pstmt0=con.prepareStatement("SELECT * FROM T_GHXX  WHERE (RQSJ BETWEEN ? AND ?) AND THBZ=0 ORDER BY YSBH");
            pstmt0.setString(1,low_t);
            pstmt0.setString(2,high_t);
            ResultSet rs0=pstmt0.executeQuery();
            String str1=null;
            int zjrc=0,zjsr=0,ptrc=0,ptsr=0;
            while(rs0.next()) { 
            	String temp=rs0.getString("YSBH");
            	String str2=rs0.getString("HZBH"); 	
            	if(str1==null) {
            		str1=temp;
            		PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str2+"'");
            		ResultSet rs1=pstmt1.executeQuery();
            		rs1.next();
            		if(rs1.getBoolean("SFZJ")) {//如果是专家号
            			zjrc++;
            			zjsr+=rs0.getBigDecimal("GHFY").intValue();
            		}
            		else {//如果是普通号
            			ptrc++;
            			ptsr+=rs0.getBigDecimal("GHFY").intValue();
            		}
            		rs1.close();
            		pstmt1.close();
            	}
            	else if(str1.equals(temp)) {
            		PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str2+"'");
            		ResultSet rs1=pstmt1.executeQuery();
            		rs1.next();
            		if(rs1.getBoolean("SFZJ")) {//如果是专家号
            			zjrc++;
            			zjsr+=rs0.getBigDecimal("GHFY").intValue();
            		}
            		else {//如果是普通号
            			ptrc++;
            			ptsr+=rs0.getBigDecimal("GHFY").intValue();
            		}
            		rs1.close();
            		pstmt1.close();
            	}
            	else if(!str1.equals(temp)) {
            		PreparedStatement pstmt2=con.prepareStatement("SELECT * FROM T_KSYS WHERE YSBH='"+str1+"'");
                	ResultSet rs2=pstmt2.executeQuery();
                	rs2.next();
            		PreparedStatement pstmt3=con.prepareStatement("SELECT * FROM T_KSXX WHERE KSBH='"+rs2.getString("KSBH")+"'");
                	ResultSet rs3=pstmt3.executeQuery();
                	rs3.next();
                	if(zjrc>0) 
            		data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"专家号",zjrc,zjsr));
                	if(ptrc>0)
                	data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"普通号",ptrc,ptsr));
                	zjrc=0;
            		zjsr=0;
            		ptrc=0;
            		ptsr=0;
            		str1=temp;
            		PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str2+"'");
            		ResultSet rs1=pstmt1.executeQuery();
            		rs1.next();
            		if(rs1.getBoolean("SFZJ")) {//如果是专家号
            			zjrc++;
            			zjsr+=rs0.getBigDecimal("GHFY").intValue();
            		}
            		else {//如果是普通号
            			ptrc++;
            			ptsr+=rs0.getBigDecimal("GHFY").intValue();
            		}
            		rs1.close();
            		pstmt1.close();
            		rs3.close();
            		pstmt3.close();
            		rs2.close();
            		pstmt2.close();           		
            	} 	
            }
            if(str1!=null) {
            	PreparedStatement pstmt2=con.prepareStatement("SELECT * FROM T_KSYS WHERE YSBH='"+str1+"'");
            	ResultSet rs2=pstmt2.executeQuery();
            	rs2.next();
        		PreparedStatement pstmt3=con.prepareStatement("SELECT * FROM T_KSXX WHERE KSBH='"+rs2.getString("KSBH")+"'");
            	ResultSet rs3=pstmt3.executeQuery();
            	rs3.next();
            	if(zjrc>0) 	
        		data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"专家号",zjrc,zjsr));
        		if(ptrc>0) 
            	data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"普通号",ptrc,ptsr));	
            	rs3.close();
        		pstmt3.close();
        		rs2.close();
        		pstmt2.close();
            }
            con.commit();
            con.setAutoCommit(true);
            time.close();
            pstmt.close();
            rs0.close();
            pstmt0.close();
			rs.close();
            stmt.close();
        } catch (SQLException e) {
        	try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
				try {
		            con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
	}
	// Event Listener on Button[#button0].onAction
	@FXML
	public void eventbutton0(ActionEvent event) {
		Stage stage=(Stage)button0.getScene().getWindow();
		stage.close();
	}
	// Event Listener on Button[#button1].onAction
	@FXML
	public void eventbutton1(ActionEvent event) {
		data1.clear();
		String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=db_hos";
	    String USER="sa";
	    String PASSWORD="l.y53543";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection con = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery("SELECT * FROM T_GHXX WHERE YSBH= '"+this.ysbh+"' AND THBZ=0");                   
            //System.out.println("open success");
            while(rs.next()) {
            	String str1=rs.getString("GHBH").trim();
            	java.sql.Timestamp date=rs.getTimestamp("RQSJ");
            	String str2=rs.getString("BRBH").trim();  
            	String str3=rs.getString("HZBH").trim();
            	String str4=null;
            	PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_BRXX WHERE BRBH='"+str2+"'");
            	ResultSet rs1=pstmt1.executeQuery();
            	rs1.next();
            	PreparedStatement pstmt2=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str3+"'");
            	ResultSet rs2=pstmt2.executeQuery();
            	rs2.next();
            	if(rs2.getBoolean("SFZJ")) {
            		str4="专家号";
            	}
            	else str4="普通号";
            	
            	data1.add(new listview1(str1,rs1.getString("BRMC"),date.toString(),str4));
                rs2.close();
                pstmt2.close();
                rs1.close();
                pstmt1.close();
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
	// Event Listener on Button[#button2].onAction
	@FXML
	public void eventbutton2(ActionEvent event) {
		data2.clear();
		String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=db_hos";
	    String USER="sa";
	    String PASSWORD="l.y53543";
	    Connection con=null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);//保证不读脏，可重复读，不可幻读，事务隔离级别最高
            con.setAutoCommit(false);
	    	ResultSet time=stmt.executeQuery("SELECT GETDATE() as serverTime");
	    	time.next();
	    	String low_t=textfield.getText();
	    	String high_t=deadline.getText();
	        PreparedStatement pstmt0=con.prepareStatement("SELECT * FROM T_GHXX  WHERE (RQSJ BETWEEN ? AND ?) AND THBZ=0 ORDER BY YSBH");
	        pstmt0.setString(1,low_t);
	        pstmt0.setString(2,high_t);
	        ResultSet rs0=pstmt0.executeQuery();
	        String str1=null;
	        int zjrc=0,zjsr=0,ptrc=0,ptsr=0;
	        while(rs0.next()) { 
	        	String temp=rs0.getString("YSBH");
	        	String str2=rs0.getString("HZBH"); 	
	        	if(str1==null) {
	        		str1=temp;
	        		PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str2+"'");
	        		ResultSet rs1=pstmt1.executeQuery();
	        		rs1.next();
	        		if(rs1.getBoolean("SFZJ")) {//如果是专家号
	        			zjrc++;
	        			zjsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		else {//如果是普通号
	        			ptrc++;
	        			ptsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		rs1.close();
	        		pstmt1.close();
	        	}
	        	else if(str1.equals(temp)) {
	        		PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str2+"'");
	        		ResultSet rs1=pstmt1.executeQuery();
	        		rs1.next();
	        		if(rs1.getBoolean("SFZJ")) {//如果是专家号
	        			zjrc++;
	        			zjsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		else {//如果是普通号
	        			ptrc++;
	        			ptsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		rs1.close();
	        		pstmt1.close();
	        	}
	        	else if(!str1.equals(temp)) {
	        		PreparedStatement pstmt2=con.prepareStatement("SELECT * FROM T_KSYS WHERE YSBH='"+str1+"'");
	            	ResultSet rs2=pstmt2.executeQuery();
	            	rs2.next();
	        		PreparedStatement pstmt3=con.prepareStatement("SELECT * FROM T_KSXX WHERE KSBH='"+rs2.getString("KSBH")+"'");
	            	ResultSet rs3=pstmt3.executeQuery();
	            	rs3.next();
	            	if(zjrc>0) 
	        		data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"专家号",zjrc,zjsr));
	            	if(ptrc>0)
	            	data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"普通号",ptrc,ptsr));
	            	zjrc=0;
	        		zjsr=0;
	        		ptrc=0;
	        		ptsr=0;
	        		str1=temp;
	        		PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str2+"'");
	        		ResultSet rs1=pstmt1.executeQuery();
	        		rs1.next();
	        		if(rs1.getBoolean("SFZJ")) {//如果是专家号
	        			zjrc++;
	        			zjsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		else {//如果是普通号
	        			ptrc++;
	        			ptsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		rs1.close();
	        		pstmt1.close();
	        		rs3.close();
	        		pstmt3.close();
	        		rs2.close();
	        		pstmt2.close();           		
	        	} 	
	        }
	        if(str1!=null) {
	        	PreparedStatement pstmt2=con.prepareStatement("SELECT * FROM T_KSYS WHERE YSBH='"+str1+"'");
	        	ResultSet rs2=pstmt2.executeQuery();
	        	rs2.next();
	    		PreparedStatement pstmt3=con.prepareStatement("SELECT * FROM T_KSXX WHERE KSBH='"+rs2.getString("KSBH")+"'");
	        	ResultSet rs3=pstmt3.executeQuery();
	        	rs3.next();
	        	if(zjrc>0) 	
	    		data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"专家号",zjrc,zjsr));
	    		if(ptrc>0) 
	        	data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"普通号",ptrc,ptsr));	
	        	rs3.close();
	    		pstmt3.close();
	    		rs2.close();
	    		pstmt2.close();
	        }
	        con.commit();
            con.setAutoCommit(true);
            rs0.close();
            pstmt0.close();
            time.close();
            stmt.close();
		}catch (SQLException e) {
        	try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
				try {
		            con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
		
	}

	@FXML
	public void eventbutton3(ActionEvent event) {
		data2.clear();
		String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=db_hos";
	    String USER="sa";
	    String PASSWORD="l.y53543";
	    Connection con=null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(URL,USER,PASSWORD);
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);//保证不读脏，可重复读，不可幻读，事务隔离级别最高
            con.setAutoCommit(false);
	    	ResultSet time=stmt.executeQuery("SELECT GETDATE() as serverTime");
	    	time.next();
	    	String low_t=textfield.getText();
	    	String high_t=time.getDate("serverTime").toString()+" "+time.getTime("serverTime").toString();
        	deadline.setText(high_t);
	        PreparedStatement pstmt0=con.prepareStatement("SELECT * FROM T_GHXX  WHERE (RQSJ BETWEEN ? AND ?) AND THBZ=0 ORDER BY YSBH");
	        pstmt0.setString(1,low_t);
	        pstmt0.setString(2,high_t);
	        ResultSet rs0=pstmt0.executeQuery();
	        String str1=null;
	        int zjrc=0,zjsr=0,ptrc=0,ptsr=0;
	        while(rs0.next()) { 
	        	String temp=rs0.getString("YSBH");
	        	String str2=rs0.getString("HZBH"); 	
	        	if(str1==null) {
	        		str1=temp;
	        		PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str2+"'");
	        		ResultSet rs1=pstmt1.executeQuery();
	        		rs1.next();
	        		if(rs1.getBoolean("SFZJ")) {//如果是专家号
	        			zjrc++;
	        			zjsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		else {//如果是普通号
	        			ptrc++;
	        			ptsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		rs1.close();
	        		pstmt1.close();
	        	}
	        	else if(str1.equals(temp)) {
	        		PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str2+"'");
	        		ResultSet rs1=pstmt1.executeQuery();
	        		rs1.next();
	        		if(rs1.getBoolean("SFZJ")) {//如果是专家号
	        			zjrc++;
	        			zjsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		else {//如果是普通号
	        			ptrc++;
	        			ptsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		rs1.close();
	        		pstmt1.close();
	        	}
	        	else if(!str1.equals(temp)) {
	        		PreparedStatement pstmt2=con.prepareStatement("SELECT * FROM T_KSYS WHERE YSBH='"+str1+"'");
	            	ResultSet rs2=pstmt2.executeQuery();
	            	rs2.next();
	        		PreparedStatement pstmt3=con.prepareStatement("SELECT * FROM T_KSXX WHERE KSBH='"+rs2.getString("KSBH")+"'");
	            	ResultSet rs3=pstmt3.executeQuery();
	            	rs3.next();
	            	if(zjrc>0) 
	        		data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"专家号",zjrc,zjsr));
	            	if(ptrc>0)
	            	data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"普通号",ptrc,ptsr));
	            	zjrc=0;
	        		zjsr=0;
	        		ptrc=0;
	        		ptsr=0;
	        		str1=temp;
	        		PreparedStatement pstmt1=con.prepareStatement("SELECT * FROM T_HZXX WHERE HZBH='"+str2+"'");
	        		ResultSet rs1=pstmt1.executeQuery();
	        		rs1.next();
	        		if(rs1.getBoolean("SFZJ")) {//如果是专家号
	        			zjrc++;
	        			zjsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		else {//如果是普通号
	        			ptrc++;
	        			ptsr+=rs0.getBigDecimal("GHFY").intValue();
	        		}
	        		rs1.close();
	        		pstmt1.close();
	        		rs3.close();
	        		pstmt3.close();
	        		rs2.close();
	        		pstmt2.close();           		
	        	} 	
	        }
	        if(str1!=null) {
	        	PreparedStatement pstmt2=con.prepareStatement("SELECT * FROM T_KSYS WHERE YSBH='"+str1+"'");
	        	ResultSet rs2=pstmt2.executeQuery();
	        	rs2.next();
	    		PreparedStatement pstmt3=con.prepareStatement("SELECT * FROM T_KSXX WHERE KSBH='"+rs2.getString("KSBH")+"'");
	        	ResultSet rs3=pstmt3.executeQuery();
	        	rs3.next();
	        	if(zjrc>0) 	
	    		data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"专家号",zjrc,zjsr));
	    		if(ptrc>0) 
	        	data2.add(new listview2(rs3.getString("KSMC"),str1,rs2.getString("YSMC"),"普通号",ptrc,ptsr));	
	        	rs3.close();
	    		pstmt3.close();
	    		rs2.close();
	    		pstmt2.close();
	        }
	        con.commit();
            con.setAutoCommit(true);
            rs0.close();
            pstmt0.close();
            time.close();
            stmt.close();
		}catch (SQLException e) {
        	try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
				try {
		            con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
		
	}
}
