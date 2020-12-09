package ihm;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage globalStage;
	private Stage localStage;
	private static Rectangle2D screen = Screen.getPrimary().getVisualBounds();

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(Stage stage) {
		localStage = new LocalStage();
		globalStage = new GlobalStage(localStage);

        globalStage.show();

        globalStage.setX(screen.getMinX() + 150);
        globalStage.setY(screen.getHeight() / 2.0 - (globalStage.getHeight() / 2.0));

        localStage.show();

        localStage.setX(Main.getScreen().getWidth() - localStage.getWidth() - 150);
		localStage.setY(Main.getScreen().getHeight() / 2.0 - (localStage.getHeight() / 2.0));

		globalStage.requestFocus();
    }

	public static Rectangle2D getScreen() { return screen; }
}