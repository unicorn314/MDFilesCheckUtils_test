package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.MyUrl;

/**
 * 检测include标签.
 * @author SunYichuan
 */
public class IncludePathChecker {
  /**
   * 在文件中搜索include标签中错误的引用文件路径.
   * 
   * @param file
   *          需要检测的文件
   * @param rootPath
   *          项目根路径
   */
  public List<MyUrl> searchWrongIncludePath(final File file, final String rootPath) {
    List<MyUrl> wrongList = new ArrayList<MyUrl>();
    // include正则判断
    String rex = "\\{%[\\s]+include[\\s]+[/]?" + "[a-zA-z0-9\\/\\.]*[\\s]+%\\}";
    Pattern pattern = Pattern.compile(rex);

    LineNumberReader lineReader = null;
    try {
      lineReader = new LineNumberReader(new FileReader(file));
      String readLine = null;

      // 读取文件内容
      while ((readLine = lineReader.readLine()) != null) {
        Matcher matcher = pattern.matcher(readLine);
        while (matcher.find()) { // 假如这一行存在可以匹配include标签正则表达式的字符串
          // 将路径字符串从include标签字符串中切割出来
          String includePath = matcher.group(0)
              .replaceAll("\\{%[\\s]+include[\\s]+", "")
              .replaceAll("[\\s]+%\\}", "");

          // 检测路径是否可用
          if (!isValidPath(rootPath, includePath.trim())) {
            MyUrl myUrl = new MyUrl();
            myUrl.setFile(file.getParent() + "\\" + file.getName());
            myUrl.setUrl(includePath);
            // 若路径不可用，将这个网址所属的文件名和网址字符串插入到错误路径list
            wrongList.add(myUrl);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // 关闭流
      try {
        lineReader.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return wrongList;
  }

  /**
   * 判断include标签中的引用路径是否有效.
   * 
   * @param rootPath
   *          项目根路径
   * @param strPath
   *          引用路径
   * @return 返回true或者false
   */
  public boolean isValidPath(final String rootPath, final String strPath) {
    String include;
    Pattern pattern = Pattern.compile("/$");
    Matcher matcher = pattern.matcher(rootPath);
    if (matcher.find()) {
      include = "_include/";
    } else {
      include = "/_include/";
    }
    File folder = new File(rootPath + include + strPath);
    if (folder.exists()) {
      return true;
    }
    return false;
  }
}
