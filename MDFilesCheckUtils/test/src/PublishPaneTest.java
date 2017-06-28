package src;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import model.R;

import org.junit.Test;

public class PublishPaneTest {
    PublishPane p = new PublishPane();

    /**
     * .
     */
    @Test
    public void testGetCheckType_() {
        p.getCheckType();
        p.chckbxSelectParent.setSelected(false);
        p.chckbxSelectChildren.setSelected(false);
        assertThat(p.getCheckType(), is(""));
    }
    /**
     * .
     */
    @Test
    public void testGetCheckType_p() {
        p.getCheckType();
        p.chckbxSelectParent.setSelected(true);
        p.chckbxSelectChildren.setSelected(false);
        assertThat(p.getCheckType(), is(R.PARENT));
    }
    /**
     * .
     */
    @Test
    public void testGetCheckType_c() {
        p.getCheckType();
        p.chckbxSelectParent.setSelected(false);
        p.chckbxSelectChildren.setSelected(true);
        assertThat(p.getCheckType(), is(R.SUBNODES));
    }
    /**
     * .
     */
    @Test
    public void testGetCheckType_pc() {
        p.getCheckType();
        p.chckbxSelectParent.setSelected(true);
        p.chckbxSelectChildren.setSelected(true);
        assertThat(p.getCheckType(), is(R.SUBNODES_AND_PARENT));
    }
}
