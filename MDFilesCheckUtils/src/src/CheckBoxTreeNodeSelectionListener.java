/*package src;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultTreeModel;

import model.NodeObj;
import model.R;

*//**
 * 添加一个响应用户鼠标事件的监听器
 *
 *//*
public class CheckBoxTreeNodeSelectionListener extends MouseAdapter {
    List<NodeObj> list;

    String type;

    *//**
     * 鼠标点击监听器
     *//*
    @Override
    public void mouseClicked(MouseEvent event) {
        JTree tree = (JTree) event.getSource();
        int x = event.getX();
        int y = event.getY();
        int row = tree.getRowForLocation(x, y);
        TreePath path = tree.getPathForRow(row);
        // 点击后改变复选框状态
        if (path != null) {
            CheckBoxTreeNode node = (CheckBoxTreeNode) path.getLastPathComponent();
            if (node != null) {
//                String type =R.SUBNODES;
                node.setSelected(!node.isSelected(),type);
                ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
            }
        }
        list = new ArrayList<NodeObj>();
        // 获得根节点
        CheckBoxTreeNode root = (CheckBoxTreeNode) tree
                .getPathForRow(tree.getRowForLocation(0, 0)).getLastPathComponent();
        // 遍历所有节点，并获得已勾选节点
        getSelectedNodes(root, list);
        show(list);
    }

    *//**
     * 根据根节点遍历获取所有被选择节点.
     * @param root 根节点
     * @param lst 存放所有被选择节点的集合
     *//*
    public void getSelectedNodes (TreeNode root,List lst) {  
        if(root.getChildCount() > 0){  
              for (Enumeration e = root.children(); e.hasMoreElements();) {  
                  CheckBoxTreeNode n = (CheckBoxTreeNode) e.nextElement();  
                  if (n.getUserObject() instanceof NodeObj
                          && n.isSelected()) {
                      lst.add(((NodeObj)n.getUserObject()));  
                  }  
                  getSelectedNodes(n,lst);  
              }   
          }  
      }
    private void show(List<NodeObj> l) {
        System.out.println("--------------list-----------------");
        for(NodeObj o:l){
            System.out.print("<"+o.getName()+","+o.getPath()+">");
        }
        System.out.println();
        
    }
    
}
*/