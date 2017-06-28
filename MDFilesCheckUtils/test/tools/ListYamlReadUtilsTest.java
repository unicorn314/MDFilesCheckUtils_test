package tools;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.ListYml;
import model.Section;

import org.junit.Test;

public class ListYamlReadUtilsTest {
  ListYamlReadUtils utils = new ListYamlReadUtils();

  /**
   * .
   */
  @Test
  public void testYmlToObject_more() {
    ListYml listYml = new ListYml();
    listYml = utils.ymlToObject(
        new File("E:\\Github\\pnpdjie.github.io\\_data\\guides.yml"));
    assertThat(listYml.getToc().size(), is(6));
  }

  /**
   * .
   */
  @Test
  public void testYmlToObject_true() {
    ListYml listYml = new ListYml();
    listYml = utils.ymlToObject(new File("E:\\OPEN-O\\utest\\test.yml"));
    assertThat(listYml.getToc().size(), is(1));
  }

  /**
   * .
   */
  @Test
  public void testFillToc() {
    List<Section> toc = new ArrayList<Section>();
    Section section = new Section();
    section.setPath("docs/guides/jekyll/install-jekyll-in-linux.md");
    toc.add(section);
    String rootPath = "E:\\Github\\pnpdjie.github.io\\";
    try {
      utils.fillToc(rootPath, toc);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    assertThat(toc.get(0).getTitle(), is("Linux安装Jekyll"));
  }
}
