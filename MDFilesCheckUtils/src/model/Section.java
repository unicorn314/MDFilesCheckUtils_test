package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Section {

  private String title;

  private String path;

  private String workflow;

  @JsonProperty("flow_title")
  private String flowTitle;

  private List<Section> section;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getWorkflow() {
    return workflow;
  }

  public void setWorkflow(String workflow) {
    this.workflow = workflow;
  }

  public String getFlow_title() {
    return flowTitle;
  }

  public void setFlow_title(String flowTitle) {
    this.flowTitle = flowTitle;
  }

  public List<Section> getSection() {
    return section;
  }

  public void setSection(List<Section> section) {
    this.section = section;
  }
}
