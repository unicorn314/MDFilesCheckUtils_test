package src;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

/**
 * 在视图层上，CheckBoxTree的每个结点比JTree的结点多显示一个复选框.
 * 通过继承DefaultMutableTreeNode定义CheckBoxTreeNode解决.
 *
 */
public class CheckBoxTreeCellRenderer extends JPanel
    implements TreeCellRenderer {
  protected JCheckBox check;
  protected CheckBoxTreeLabel label;

  /**
   * 树加载.
   */
  public CheckBoxTreeCellRenderer() {
    setLayout(null);
    add(check = new JCheckBox());
    add(label = new CheckBoxTreeLabel());
    check.setBackground(UIManager.getColor("Tree.textBackground"));
    label.setForeground(UIManager.getColor("Tree.textForeground"));
  }

  /**
   * 返回的是一个<code>JPanel</code>对象，该对象中包含一个<code>JCheckBox</code>对象.
   * 和一个<code>JLabel</code>对象。并且根据每个结点是否被选中来决定<code>JCheckBox</code> 是否被选中.
   */
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean selected, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {
    final String stringValue = tree.convertValueToText(value, selected,
        expanded, leaf, row, hasFocus);
    setEnabled(tree.isEnabled());
    check.setSelected(((CheckBoxTreeNode) value).isSelected());
    label.setFont(tree.getFont());
    label.setText(stringValue);
    label.setSelected(selected);
    label.setFocus(hasFocus);
    if (leaf) {
      label.setIcon(UIManager.getIcon("Tree.leafIcon"));
    } else if (expanded) {
      label.setIcon(UIManager.getIcon("Tree.openIcon"));
    } else {
      label.setIcon(UIManager.getIcon("Tree.closedIcon"));
    }

    return this;
  }

  @Override
  public Dimension getPreferredSize() {
    Dimension dimensionCheck = check.getPreferredSize();
    Dimension dimensionLabel = label.getPreferredSize();
    return new Dimension(dimensionCheck.width + dimensionLabel.width,
        dimensionCheck.height < dimensionLabel.height ? dimensionLabel.height
            : dimensionCheck.height);
  }

  @Override
  public void doLayout() {
    Dimension dimensionCheck = check.getPreferredSize();
    Dimension dimensionLabel = label.getPreferredSize();
    int yaxisCheck = 0;
    int yaxisLabel = 0;
    if (dimensionCheck.height < dimensionLabel.height) {
      yaxisCheck = (dimensionLabel.height - dimensionCheck.height) / 2;
    } else {
      yaxisLabel = (dimensionCheck.height - dimensionLabel.height) / 2;
    }
    check.setLocation(0, yaxisCheck);
    check.setBounds(0, yaxisCheck, dimensionCheck.width, dimensionCheck.height);
    label.setLocation(dimensionCheck.width, yaxisLabel);
    label.setBounds(dimensionCheck.width, yaxisLabel, dimensionLabel.width, dimensionLabel.height);
  }

  @Override
  public void setBackground(Color color) {
    if (color instanceof ColorUIResource) {
      color = null;
    }
    super.setBackground(color);
  }
}
