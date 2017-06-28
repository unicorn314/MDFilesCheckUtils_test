package src;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import model.R;

import org.junit.Test;

public class PublishPaneTest {
  PublishPane publishPane = new PublishPane();

  /**
   * .
   */
  @Test
  public void testGetCheckType_() {
    publishPane.getCheckType();
    publishPane.chckbxSelectParent.setSelected(false);
    publishPane.chckbxSelectChildren.setSelected(false);
    assertThat(publishPane.getCheckType(), is(""));
  }

  /**
   * .
   */
  @Test
  public void testGetCheckType_p() {
    publishPane.getCheckType();
    publishPane.chckbxSelectParent.setSelected(true);
    publishPane.chckbxSelectChildren.setSelected(false);
    assertThat(publishPane.getCheckType(), is(R.PARENT));
  }

  /**
   * .
   */
  @Test
  public void testGetCheckType_c() {
    publishPane.getCheckType();
    publishPane.chckbxSelectParent.setSelected(false);
    publishPane.chckbxSelectChildren.setSelected(true);
    assertThat(publishPane.getCheckType(), is(R.SUBNODES));
  }

  /**
   * .
   */
  @Test
  public void testGetCheckType_pc() {
    publishPane.getCheckType();
    publishPane.chckbxSelectParent.setSelected(true);
    publishPane.chckbxSelectChildren.setSelected(true);
    assertThat(publishPane.getCheckType(), is(R.SUBNODES_AND_PARENT));
  }
}
