package uk.comp2211.group13.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ValueBlock extends StackPane {

  private static final Logger logger = LogManager.getLogger(ValueBlock.class);

  private final String valueName;
  private String value;

  private VBox vBox;
  private Text valueText;

  public ValueBlock(String valueName, String value) { // TODO: Probably the values display need to be done with binding
    logger.info("Building " + this.getClass().getName());

    this.valueName = valueName;
    this.value = value;

    //Do an initial paint
    build();

    //When the value property is updated, call the internal updateValue method
    //value.addListener(this::updateValue);
  }

  private void build() {
    ValueCanvas valueCanvas = new ValueCanvas();
    getChildren().add(valueCanvas);

    vBox = new VBox();
    getChildren().add(vBox);
    vBox.setAlignment(Pos.CENTER);
    vBox.setPadding(new Insets(10, 10, 10, 10));
    vBox.setSpacing(10);

    Text nameText = new Text(valueName);
    vBox.getChildren().add(nameText);

    valueText = new Text(value); //TODO: Add binding
    vBox.getChildren().add(valueText);
  }

  public VBox getVBox() {
    return vBox;
  }

  public void setValue(String string) {
    this.value = string;
    valueText.setText(value);
  }
}
