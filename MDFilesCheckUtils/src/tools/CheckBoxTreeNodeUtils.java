package tools;

import java.util.List;

import src.CheckBoxTreeNode;
import model.ListYml;
import model.NodeObj;
import model.Section;

/**
 * 树形复选框相关工具
 * @author SunYichuan
 */
public class CheckBoxTreeNodeUtils {
    /**
     * 根据ListYml生成树形复选框列表
     * @param ymlList 传入的ListYml对象
     * @param treeNodeList 用来存放结果的集合
     */
    public void createTreeNodes (List<ListYml> ymlList, List<CheckBoxTreeNode> treeNodeList) {
        for (ListYml yml : ymlList) {
            CheckBoxTreeNode rootNode;
            String name;
            if (yml.getBigheader() != null) {
                name = yml.getBigheader();
            } else {
                name = null;
            }
            rootNode = new CheckBoxTreeNode(
                    new NodeObj(name, null));
            createChildTreeNodes(rootNode, yml.getToc());
            treeNodeList.add(rootNode);
        }

    }
    /**
     * 递归生成树形复选框的子级菜单
     * @param sections 传入Section集合
     * @param rootNode 菜单根节点对象
     * @return 树形复选框
     */
    public void createChildTreeNodes (CheckBoxTreeNode rootNode, List<Section> sections) {
        for (Section section : sections) {
            CheckBoxTreeNode treeNode;
            String name;
            String path;
            if (section.getTitle() != null) {
                name = section.getTitle();
            } else {
                name = null;
            }
            if (section.getPath() != null) {
                path = section.getPath();
            } else {
                path = null;
            }
            treeNode = new CheckBoxTreeNode(
                    new NodeObj(name, path));
            rootNode.add(treeNode);
            // 如果节点还有子级菜单，递归生成并加入
            if (section.getSection() != null) {
                createChildTreeNodes(treeNode, section.getSection());
            }
        }
    }
}
