package tools;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.ListYml;
import model.NodeObj;
import model.Section;
import net.sf.json.JSONObject;

import org.yaml.snakeyaml.Yaml;

public class ListYamlReadUtils {
  /**
   * 递归查找指定路径下所有的文件.
   * 
   * @param folder
   *          指定搜索的文件夹
   * @return 所有的文件数组
   */
  public List<File> searchFile(final File folder) {
    try {
      File[] subFolders = folder.listFiles();
      List<File> result = new ArrayList<File>(); // 声明一个集合
      for (int i = 0; i < subFolders.length; i++) { // 循环显示文件夹或文件
        if (subFolders[i].isFile()) { // 如果是文件,加入列表
          result.add(subFolders[i]);
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

  /**
   * 将配置下拉菜单信息的yml文件解析为java对象.
   * 
   * @param file
   *          解析的yml文件
   * @return ListYml对象
   */
  public ListYml ymlToObject(File file) {
    ListYml listYml = new ListYml();
    String rootPath = new File(file.getParent()).getParent();
    if (!rootPath.endsWith("\\")) {
      rootPath += "\\";
    }
    try {
      // 读取yml文件内容
      final String encoding = "UTF-8";
      Long filelength = file.length();
      byte[] filecontent = new byte[filelength.intValue()];
      FileInputStream in = new FileInputStream(file);
      in.read(filecontent);
      in.close();
      String res = new String(filecontent, encoding);
      Yaml yaml = new Yaml();
      Object result = yaml.load(res);
      // 将yml文件解析封装为ListYml对象
      JSONObject jsonObject = JSONObject.fromObject(result);
      listYml = (ListYml) JSONObject.toBean(jsonObject, ListYml.class);
      // 将ListYml对象中的toc属性从JSONObject转换为Toc对象
      List<Section> toc = jsonObjectToToc(listYml.getToc());
      // 填充toc对象
      fillToc(rootPath, toc);
      listYml.setToc(toc);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return listYml;
  }

  /**
   * 递归将传入的jsonObject中的所有对象转换为Section集合类型.
   * 
   * @param jsonObjectList
   *          传入的jsonObject
   * @return Section集合
   */
  public List<Section> jsonObjectToToc(List jsonObjectList)
      throws JsonParseException, JsonMappingException, IOException {
    List<Section> sections = new ArrayList<Section>();
    // 遍历集合中所有的Object，全部转为Section
    for (Object obj : jsonObjectList) {
      JSONObject jsonObject = JSONObject.fromObject(obj);
      Section section = (Section) JSONObject.toBean(jsonObject, Section.class);
      // 如果转换为Section后，内部的section属性不为空，则说明还有子级菜单
      if (section.getSection() != null) {
        // 递归调用此方法，将所有子级菜单也转换为Section对象
        List<Section> innerSections = jsonObjectToToc(section.getSection());
        section.setSection(innerSections);
      }
      sections.add(section);
    }
    return sections;
  }

  /**
   * 填充Toc对象. 将地址中的相对路径转换为绝对路径， 根据path的绝对路径找到对应文件，
   * 从对应的文件title部分读取title内容并填充到title属性中
   * 
   * @param rootPath
   *          根路径
   * @param toc
   *          需要填充的 Toc对象
   */
  public void fillToc(String rootPath, List<Section> toc)
      throws FileNotFoundException, IOException {
    TitleChecker titleChecker = new TitleChecker();
    HttpLinkChecker httpLinkChecker = new HttpLinkChecker();
    // 遍历Section集合
    for (Section section : toc) {
      if (section.getPath() != null) {
        if (!httpLinkChecker.isHttpLink(section.getPath())) {
          // 如果有path，将相对路径改为绝对路径
          String abPath = rootPath + section.getPath().replace("/", "\\");
          section.setPath(abPath);
          if (section.getTitle() == null) {
            // 如果有path而没有title，从对应文件中读取title
            section.setTitle(titleChecker.getTitle(new File(abPath)));
          }
        } else {
          String abPath = section.getPath();
          section.setPath(abPath);
          if (section.getTitle() == null) {
            // 如果有path而没有title，从对应文件中读取title
            section.setTitle(titleChecker.getTitle(new File(abPath)));
          }
        }
      }
      if (section.getSection() != null) {
        // 如果section不为空，说明还有子级菜单，递归继续填充
        fillToc(rootPath, section.getSection());
      }
    }
  }
}
