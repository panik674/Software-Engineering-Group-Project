package uk.comp2211.group13.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

public class ValueCanvas extends Canvas {

  private final double width;
  private final double height;

  public ValueCanvas() {
    width = 190;
    height = 120;

    //A canvas needs a fixed width and height
    setWidth(width);
    setHeight(height);

    //Do an initial paint
    paint();

    //When the value property is updated, call the internal updateValue method
    //value.addListener(this::updateValue);
  }

  private void paint() {
    var gc = getGraphicsContext2D();

    //Shadow effect
    InnerShadow innerShadow = new InnerShadow();
    innerShadow.setColor(Color.rgb(1, 1, 1, 0.5));
    innerShadow.setOffsetX(2);
    innerShadow.setOffsetY(2);

    //Clear
    gc.clearRect(0, 0, width, height);

    //Colour fill
    gc.setEffect(innerShadow);
    gc.setFill(Color.CYAN);
    gc.fillRect(0, 0, width, height);

    //Border
    gc.setStroke(Color.BLACK);

  }

}
