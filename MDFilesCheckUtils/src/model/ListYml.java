package model;

import java.util.List;

/**
 * 下拉菜单Yml文件的实体类.
 * 
 * @author SunYichuan
 */
public class ListYml {

  private String bigheader;

  private List<Section> toc;

  public String getBigheader() {
    return bigheader;
  }

  public void setBigheader(String bigheader) {
    this.bigheader = bigheader;
  }

  public void setToc(List<Section> toc) {
    this.toc = toc;
  }

  public List<Section> getToc() {
    return toc;
  }

}
