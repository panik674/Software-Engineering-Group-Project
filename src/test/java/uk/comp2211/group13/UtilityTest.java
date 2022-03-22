package uk.comp2211.group13;

import org.junit.Assert;
import org.junit.Test;
import uk.comp2211.group13.enums.Granularity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UtilityTest {

  @Test
  public void testString2Date() throws ParseException {
    Assert.assertEquals(
        new GregorianCalendar(2015, Calendar.JANUARY, 1, 12, 4, 29).getTime(),
        Utility.string2Date("2015-01-01 12:04:29")
    );
    Assert.assertEquals(
        new GregorianCalendar(2015, Calendar.JANUARY, 1, 13, 28, 22).getTime(),
        Utility.string2Date("2015-01-01 13:28:22")
    );
    Assert.assertEquals(
        new GregorianCalendar(2015, Calendar.JANUARY, 13, 13, 29, 51).getTime(),
        Utility.string2Date("2015-01-13 13:29:51")
    );
  }

  @Test
  public void testString2DateInvalidFormat() {
    Assert.assertThrows(
        ParseException.class,
        () -> Utility.string2Date("2015/01/01 12:04:29")
    );
    Assert.assertThrows(
        ParseException.class,
        () -> Utility.string2Date("16:45:23 2016-01-01")
    );
  }

  @Test
  public void testDate2String() {
    Assert.assertEquals(
        "2015-01-01 12:04:29",
        Utility.date2String(new GregorianCalendar(2015, Calendar.JANUARY, 1, 12, 4, 29).getTime())
    );
    Assert.assertEquals(
        "2015-01-01 13:28:22",
        Utility.date2String(new GregorianCalendar(2015, Calendar.JANUARY, 1, 13, 28, 22).getTime())
    );
    Assert.assertEquals(
        "2015-01-13 13:29:51",
        Utility.date2String(new GregorianCalendar(2015, Calendar.JANUARY, 13, 13, 29, 51).getTime())
    );
  }

  @Test
  public void testValidateDate() {
    Assert.assertEquals(
        true,
        Utility.validateDate("2015-01-01 12:04:29")
    );
    Assert.assertEquals(
        true,
        Utility.validateDate("2015-01-01 13:28:22")
    );
    Assert.assertEquals(
        true,
        Utility.validateDate("2015-01-13 13:29:51")
    );

    Assert.assertEquals(
        false,
        Utility.validateDate("2015/01/01 12:04:29")
    );
    Assert.assertEquals(
        false,
        Utility.validateDate("16:45:23 2016-01-01")
    );
  }

  @Test
  public void testValidateGender() {
    try {
      Assert.assertEquals("Male", Utility.validateGender("Male"));
      Assert.assertEquals("Female", Utility.validateGender("Female"));
    } catch (Exception e) {
      Assert.fail();
    }

    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateGender("Invalid")
    );
    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateGender("M")
    );
    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateGender("male")
    );
  }

  @Test
  public void testValidateAge() {
    try {
      Assert.assertEquals("<25", Utility.validateAge("<25"));
      Assert.assertEquals("25-34", Utility.validateAge("25-34"));
      Assert.assertEquals("35-44", Utility.validateAge("35-44"));
      Assert.assertEquals("45-54", Utility.validateAge("45-54"));
      Assert.assertEquals(">54", Utility.validateAge(">54"));
    } catch (Exception e) {
      Assert.fail();
    }

    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateAge("Invalid")
    );
    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateAge("56")
    );
    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateAge("twenty five")
    );
  }

  @Test
  public void testValidateIncome() {
    try {
      Assert.assertEquals("Low", Utility.validateIncome("Low"));
      Assert.assertEquals("Medium", Utility.validateIncome("Medium"));
      Assert.assertEquals("High", Utility.validateIncome("High"));
    } catch (Exception e) {
      Assert.fail();
    }

    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateIncome("Invalid")
    );
    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateIncome("£50,000")
    );
    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateIncome("Huge")
    );
  }

  @Test
  public void testValidateContext() {
    try {
      Assert.assertEquals("News", Utility.validateContext("News"));
      Assert.assertEquals("Shopping", Utility.validateContext("Shopping"));
      Assert.assertEquals("Social Media", Utility.validateContext("Social Media"));
      Assert.assertEquals("Blog", Utility.validateContext("Blog"));
      Assert.assertEquals("Hobbies", Utility.validateContext("Hobbies"));
      Assert.assertEquals("Travel", Utility.validateContext("Travel"));
    } catch (Exception e) {
      Assert.fail();
    }

    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateContext("Invalid")
    );
    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateContext("Racing")
    );
    Assert.assertThrows(
        Exception.class,
        () -> Utility.validateContext("MotoCross")
    );
  }

  @Test
  public void testGetGranularity() {
    Assert.assertEquals(Granularity.None, Utility.getGranularity("None"));
    Assert.assertEquals(Granularity.None, Utility.getGranularity("Invalid"));
    Assert.assertEquals(Granularity.None, Utility.getGranularity("Vague and convoluted time"));

    Assert.assertEquals(Granularity.Second, Utility.getGranularity("Second"));
    Assert.assertEquals(Granularity.Minute, Utility.getGranularity("Minute"));
    Assert.assertEquals(Granularity.Hour, Utility.getGranularity("Hour"));
    Assert.assertEquals(Granularity.Day, Utility.getGranularity("Day"));
    Assert.assertEquals(Granularity.Month, Utility.getGranularity("Month"));
    Assert.assertEquals(Granularity.Year, Utility.getGranularity("Year"));
  }
}
