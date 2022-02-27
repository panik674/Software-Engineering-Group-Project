package uk.comp2211.group13;

// Log4j (Test may remove)
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 *
 */
public class App {
  private static final Logger logger = LogManager.getLogger(App.class);

  public static void main( String[] args )
  {
    logger.info("Hello Logging World!");

    System.out.println( "Hello World!" );
  }
}
