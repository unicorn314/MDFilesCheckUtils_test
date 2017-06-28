package src;

import javax.swing.tree.DefaultMutableTreeNode;

import model.R;

/**
 * 在模型层上，CheckBoxTree的每个结点需要一个成员来保存其是否被选中，但是JTree的结点则不需要.
 * 通过继承DefaultMutableTreeNode定义CheckBoxTreeNode解决.
 *
 */
public class CheckBoxTreeNode extends DefaultMutableTreeNode {
  protected boolean isSelected;

  public CheckBoxTreeNode() {
    this(null);
  }

  public CheckBoxTreeNode(Object userObject) {
    this(userObject, true, false);
  }

  public CheckBoxTreeNode(Object userObject, boolean allowsChildren,
      boolean isSelected) {
    super(userObject, allowsChildren);
    this.isSelected = isSelected;
  }

  public boolean isSelected() {
    return isSelected;
  }

  /**
   * 复选框自动勾选方法.
   * 
   * @param selected
   *          复选框是否被选择
   * @param type
   *          复选框的自动勾选方式
   */
  public void setSelected(boolean selected, String type) {
    this.isSelected = selected;
    // 根据选中条件添加判断逻辑,如果都没选，就只勾选当前节点
    if (type.equals(R.SUBNODES)) {
      // 只选中Auto Select Subnodes，自动勾选所有子集节点
      subnodesSelect(selected, type);
    } else if (type.equals(R.PARENT)) {
      // 只选中Auto Select Parent，自动勾选父级节点即使子集节点未全部勾选
      parentSelect(selected, type);
    } else if (type.equals(R.SUBNODES_AND_PARENT)) {
      // 选中Auto Select Subnodes 和 Auto Select
      // Parent，自动勾选所有子集节点并勾选父级节点即使子集节点未全部勾选
      subnodesAndParentSelect(selected, type);
    }
  }

  /*
   * 选中Auto Select Subnodes 和 Auto Select Parent，自动勾选所有子集节点并勾选父级节点即使子集节点未全部勾选
   */
  private void subnodesAndParentSelect(boolean selected, String type) {
    subnodesSelect(selected, R.SUBNODES);
    parentSelect(selected, R.PARENT);
  }

  /*
   * 只选中Auto Select Parent，自动勾选父级节点即使子集节点未全部勾选
   */
  private void parentSelect(boolean selected, String type) {
    // 向上检查，如果父结点的子结点被选中，那么将父结点也选中
    CheckBoxTreeNode parentNode = (CheckBoxTreeNode) parent;
    // if (_isSelected) {
    // 开始检查pNode的子节点是否被选中
    if (parentNode != null) {
      int index = 0;
      for (; index < parentNode.children.size(); ++index) {
        CheckBoxTreeNode parentChildNode = (CheckBoxTreeNode) parentNode.children
            .get(index);
        // if (pChildNode.isSelected()) {
        // 递归
        parentNode.setSelected(selected, type);
        break;
        // }
      }
    }
    // }else{
    // //取消节点 对应父节点全部取消
    // if (pNode != null) {
    //
    // }
    // }
  }

  /*
   * 只选中Auto Select Subnodes，自动勾选所有子集节点
   */
  private void subnodesSelect(boolean selected, String type) {
    if (selected) {
      // 如果选中，则将其所有的子结点都选中
      if (children != null) {
        for (Object obj : children) {
          CheckBoxTreeNode node = (CheckBoxTreeNode) obj;
          if (selected != node.isSelected()) {
            node.setSelected(selected, type);
          }
        }
      }
    } else {
      if (children != null) {
        // int index = 0;
        // for (; index < children.size(); ++index) {
        // CheckBoxTreeNode childNode = (CheckBoxTreeNode) children.get(index);
        // if (!childNode.isSelected())
        // break;
        // }
        // 从上向下取消的时候
        // if (index == children.size()) {
        for (int i = 0; i < children.size(); ++i) {
          CheckBoxTreeNode node = (CheckBoxTreeNode) children.get(i);
          if (node.isSelected() != selected) {
            node.setSelected(selected, type);
          }
        }
        // }
      }
    }
  }
}
