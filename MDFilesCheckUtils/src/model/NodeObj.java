package model;

public class NodeObj {
  private String name;
  private String path;

  public NodeObj(String name, String path) {
    this.name = name;
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  // 重点在toString，节点的显示文本就是toString
  public String toString() {
    return name;
  }

}
