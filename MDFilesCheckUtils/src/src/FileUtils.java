package src;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件帮助类.
 * 
 * @author ywx474563 2017年6月26日
 */
public class FileUtils {
  /**
   * 递归查找指定路径下所有的.md和.yml文件.
   * 
   * @param folder
   *          指定搜索的文件夹
   * @return 所有的文件数组
   */
  public static List<File> searchFile(final File folder) {
    try {
      File[] subFolders = folder.listFiles();
      List<File> result = new ArrayList<File>(); // 声明一个集合
      for (int i = 0; i < subFolders.length; i++) { // 循环显示文件夹或文件
        if (subFolders[i].isFile()) { // 如果是文件,加入列表
          if (subFolders[i].getName().toLowerCase().endsWith(".md")
              || subFolders[i].getName().toLowerCase().endsWith(".yml")) {
            // 如果是.md或者.yml文件，则将结果加入列表，否则不做处理
            result.add(subFolders[i]);
          }
        } else { // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
          List<File> foldResult = searchFile(subFolders[i]);
          // 循环显示文件
          for (int j = 0; j < foldResult.size(); j++) {
            result.add(foldResult.get(j)); // 文件保存到集合中
          }
        }
      }
      return result;
    } catch (NullPointerException e) {
      e.printStackTrace();
      return null;
    }
  }

}
