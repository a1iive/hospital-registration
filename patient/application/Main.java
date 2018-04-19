package application;
	
import java.net.URL;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			URL location = getClass().getResource("login.fxml");
	        FXMLLoader fxmlLoader = new FXMLLoader();
	        fxmlLoader.setLocation(location);
	        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
	        Parent anotherRoot = fxmlLoader.load();
        	//Parent anotherRoot=FXMLLoader.load(getClass().getResource("shiyan2.fxml"));
        	Stage anotherStage = new Stage();
        	Scene scene=new Scene(anotherRoot);
            anotherStage.setTitle("guahao");
            anotherStage.setScene(scene);
            loginController controller = fxmlLoader.getController();   //获取Controller的实例对象                                               
            controller.init();
            anotherStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
