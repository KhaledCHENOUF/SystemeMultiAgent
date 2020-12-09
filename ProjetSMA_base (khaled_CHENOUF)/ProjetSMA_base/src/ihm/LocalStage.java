package ihm;

import concept.Vehicle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import params.Params;

public class LocalStage extends Stage {

	private Vehicle watched;

	ImageView imageView = new ImageView();

	public LocalStage() {
		setTitle(Params.localStageTitle);
		setResizable(false);
		initModality(Modality.NONE);
		initStyle(StageStyle.UNIFIED);

		setScene(new Scene(createContent()));
	}

	Parent createContent() {
		Pane root = new Pane(imageView);

		root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		return root;
	}

	public Vehicle watchedVehicle() { return this.watched; }
	public void watch(Vehicle v) { this.watched = v; }

	public void update(Image image) {
		this.imageView.setImage(image);
		this.setWidth(watched.getViewRadius()*2+10);
		this.setHeight(watched.getViewRadius()*2+30);
	}
}
