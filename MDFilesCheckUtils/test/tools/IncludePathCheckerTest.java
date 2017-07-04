package tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class IncludePathCheckerTest {
  IncludePathChecker checker = new IncludePathChecker();
  
  /**
   * .
   */
  @Test
  public void testIsValidPath_ture() {
    String rootPath = "utestfiles";
    String strPath = "JUnitTest.md";
    assertThat(checker.isValidPath(rootPath, strPath), is(true));
  }

  /**
   * .
   */
  @Test
  public void testIsValidPath_false() {
    String rootPath = "utestfiles";
    String strPath = "templates/home.md";
    assertThat(checker.isValidPath(rootPath, strPath), is(false));
  }
}
