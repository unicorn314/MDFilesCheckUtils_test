package tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.MyUrl;

/**
 * 检测网址的工具类.
 * 
 * @author SunYichuan
 */
public class HttpLinkChecker {
  // 网址的正则表达式
  private final String httpRex = "(http://|ftp://|https://)"
      + "[^\\s\"\'\\()]*?\\."
      + "(com|net|cn|me|tw|fr|edu)[^\\s\"\'\\()]*";

  /**
   * 判断字符串是否为网址.
   * 
   * @param strLink
   *          字符串
   * @return 返回true或者false
   */
  public boolean isHttpLink(final String strLink) {
    if (strLink.matches(httpRex)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 判断字符串中是否含有网址.
   * 
   * @param strLink
   *          字符串
   * @return 返回true或者false
   */
  public boolean hasHttpLink(final String strLink) {
    Pattern pattern = Pattern.compile(httpRex);
    Matcher matcher = pattern.matcher(strLink);
    if (matcher.find()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 判引入的网页断链接是否有效.
   * 
   * @param strLink
   *          输入链接
   * @return 返回true或者false
   */
  public boolean isValidUrl(final String strLink) {
    URL url;
    try {
      url = new URL(strLink);
      HttpURLConnection connt = (HttpURLConnection) url.openConnection();
      connt.setRequestMethod("HEAD");
      String strMessage = connt.getResponseMessage();
      if (strMessage.compareTo("Not Found") == 0) {
        return false;
      }
      connt.disconnect();
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  /**
   * 在文件中搜索失效链接.
   * 
   * @param file
   *          输入需要检索的文件
   */
  public List<MyUrl> searchBadUrl(final File file) {
    List<MyUrl> badUrlList = new ArrayList<MyUrl>();
    Pattern pattern = Pattern.compile(httpRex);
    // 行读取
    LineNumberReader lineReader = null;
    try {
      lineReader = new LineNumberReader(new FileReader(file));
      String readLine = null;
      while ((readLine = lineReader.readLine()) != null) { // 读取文件内容
        Matcher matcher = pattern.matcher(readLine);
        while (matcher.find()) { // 假如这一行存在可以匹配网址正则表达式的字符串
          if (!isValidUrl(matcher.group(0))) { // 检测网址是否可用
            MyUrl myUrl = new MyUrl();
            myUrl.setFile(file.getParent() + "\\" + file.getName());
            myUrl.setUrl(matcher.group(0));
            // 若网址不可用，将这个网址所属的文件名和网址自身插入到失效链接列表
            badUrlList.add(myUrl);
          }
        }
      }
      return badUrlList;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } finally {
      // 关闭流
      try {
        lineReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
