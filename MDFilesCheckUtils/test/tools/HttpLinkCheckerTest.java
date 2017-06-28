package tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.MyUrl;

import org.junit.Test;

public class HttpLinkCheckerTest {
  HttpLinkChecker checker = new HttpLinkChecker();

  /**
   * .
   */
  @Test
  public void testIsHttpLink_true() {
    String link = "http://github.com";
    checker.isHttpLink(link);
    assertThat(checker.isHttpLink(link), is(true));
  }

  /**
   * .
   */
  @Test
  public void testIsHttpLink_false() {
    String link = "abc://www.github.coam";
    checker.isHttpLink(link);
    assertThat(checker.isHttpLink(link), is(false));
  }

  /**
   * .
   */
  @Test
  public void testHasHttpLink_true() {
    String link = "包含有超链接的字符串[github](https://www.github.com \"click here\")";
    checker.hasHttpLink(link);
    assertThat(checker.hasHttpLink(link), is(true));
  }

  /**
   * .
   */
  @Test
  public void testHasHttpLink_false() {
    String link = "没有超链接的字符串www.baidu.com没有http";
    checker.hasHttpLink(link);
    assertThat(checker.hasHttpLink(link), is(false));
  }

  /**
   * .
   */
  @Test
  public void testIsValidUrlTrue() {
    String link = "https://github.com";
    checker.isValidUrl(link);
    assertThat(checker.isValidUrl(link), is(true));
  }

  /**
   * .
   */
  @Test
  public void testIsValidUrlFalse() {
    String link = "https://www.baidu.edu";
    checker.isValidUrl(link);
    assertThat(checker.isValidUrl(link), is(false));
  }

  /**
   * .
   */
  @Test
  public void testSearchBadUrlTrue() {
    List<MyUrl> badUrList = new ArrayList<MyUrl>();
    File file = new File("E:\\OPEN-O\\_include\\JUnitTest.md");
    badUrList = checker.searchBadUrl(file);
    assertThat(badUrList.size(), is(1));
  }
}
