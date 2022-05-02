package uk.comp2211.group13.data;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * This Class is responsible for capturing the screenshot of a user-specified Scene.
 * The screenshot is saved in the Downloads folder of the user's computer.
 **/
public class Screenshot {
    public static void screenshot(Scene scene){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        // replacing "/" and ":" from the date format with "-" as these are illegal in filenames
        String name = (dtf.format(now)).replace(":", "-").replace("/", "-");
        var workdir = Paths.get("").toAbsolutePath();
        String home = System.getProperty("user.home");
        home = home + "\\Downloads\\"+name+".png";
        File file = new File(home);
        try {
            //Pad the capture area
            WritableImage writableImage = new WritableImage((int)scene.getWidth(),
                    (int)scene.getHeight());
            scene.snapshot( writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            //Write the snapshot to the chosen file
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException ex) { ex.printStackTrace(); }
    }
}
