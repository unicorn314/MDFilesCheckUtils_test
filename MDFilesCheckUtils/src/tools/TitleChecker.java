package tools;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import model.MyUrl;

/**
 * 检查文件title部分的工具类.
 * 
 * @author SunYichuan
 */
public class TitleChecker {

  private List<MyUrl> myUrlList;

  private String title;

  /**
   * 检查title格式是否正确.
   * 
   * @param file
   *          需要检查的文件
   * @return 错误信息，若无错误则返回一个长度为0的list。
   */
  public List<MyUrl> searchWrongTitle(final File file) {
    /*
     * 检查内容： 1.title标识线是否为3个"-" 2.title内容里面key和value中间的冒号后面是否跟有空格
     */
    myUrlList = new ArrayList<MyUrl>();
    LineNumberReader lineReader = null;
    int titleLineNum = 0; // 已读取到的title标识线"---"行的数量
    int titleContentNum = 0; // 已读取到的title内容"key: value"行的数量
    try {
      lineReader = new LineNumberReader(new FileReader(file));
      String readLine = null;
      while ((readLine = lineReader.readLine()) != null) {
        // 从文件第一行开始读取，直到最后一行
        if (readLine.matches("\\s*")) { // 假如是空行，则继续读取下一行
          continue;
        } else if (titleLineNum == 0 && !readLine.matches("^[\\-]+$")) {
          // 如果读到的第一行不是若干个"-"组成的，说明此文件没有加title，跳过此文件
          break;
        } else if (titleLineNum == 0 && !readLine.matches("^[\\-]{3}$")) {
          // 如果第一行"-"数量不为3个，则记入title错误文件的list
          MyUrl myUrl = new MyUrl();
          myUrl.setFile(file.getParent() + file.getName());
          myUrlList.add(myUrl);
          break;
        } else if (titleLineNum == 0 && readLine.matches("^[\\-]{3}$")) {
          // 如果开头的"-"数量为3个，则继续读取文件，检查title内容
          titleLineNum++; // title标识线数量+1，表明已经找到title的开头
          continue;
        } else if (titleLineNum == 1) {
          // 如果已经找到上半截title标识线，检测title内容是否符合格式
          if (readLine.matches(".*[\\:]{1}[\\s]{1}.*")) {
            // 如果该行内容符合title内容格式
            if (readLine.matches("[\\s]*title\\:\\s[\\s\\S]*")) {
              // 假如key为"title"，则提取出title内容
              title = readLine.replace("title: ", "");
            }
            // 修改title内容行数，继续读取下一行内容
            titleContentNum++;
            continue;
          } else if (readLine.matches("\\s*")) {
            // 如果该title内容行为空行，继续读取下一行内容
            continue;
          } else if (titleContentNum > 0 && readLine.matches("^[\\-]{3}$")) {
            // 如果title内容行数不为0的情况下读取到了title的结束标识线，
            // 则表示title部分读取完毕，跳过该文件剩余部分
            break;
          } else {
            // 如果为其他情况,则记入title错误文件的list
            MyUrl myUrl = new MyUrl();
            myUrl.setFile(file.getParent() + "\\" + file.getName());
            myUrlList.add(myUrl);
            break;
          }
        }
      }
      return myUrlList;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      // 关闭流
      close(lineReader);
    }
  }

  /**
   * 获得文件的title内容.
   * 
   * @param file
   *          文件
   * @return 文件title，如果无title，返回""，否则返回title
   */
  public String getTitle(File file) {
    searchWrongTitle(file);
    return title;
  }

  /**
   * 关闭流.
   * 
   * @param able
   *          需要关闭的流
   */
  private void close(final Closeable able) {
    if (able != null) {
      try {
        able.close();
      } catch (IOException e) {
        e.printStackTrace();
        // able = null;
      }
    }
  }
}
