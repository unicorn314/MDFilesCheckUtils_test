package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import model.ListYml;
import model.NodeObj;
import model.R;
import tools.CheckBoxTreeNodeUtils;
import tools.CmdWorker;
import tools.ListYamlReadUtils;

/**
 * 离线发布界面.
 * 
 * @author ywx474563 2017年6月26日
 */
public class PublishPane extends JPanel {

  /**
   * 选择的文件夹路径.
   */
  private String rootFolderPath;
  private JComboBox comboBox = new JComboBox();
  // private final JTree tree = new JTree();
  private final JButton btnHtmlToPdf = new JButton("html to pdf");

  // 确认按钮
  final JButton createTreeButton = new JButton("create tree");

  // 启动Jekyll按钮
  final JButton jekyllServeButton = new JButton("发布html");

  JTree tree = new JTree();

  JScrollPane scrollPane = new JScrollPane();
  JPanel centerPanel = new JPanel();
  // 状态复选框
  JCheckBox chckbxSelectAll = new JCheckBox("select all");
  JCheckBox chckbxSelectParent = new JCheckBox("select parent");
  JCheckBox chckbxSelectChildren = new JCheckBox("select children");

  JTextArea logArea = new JTextArea();

  // 下拉框当前选择的第几个选项
  int comboBoxSelectIndex = 0;

  /**
   * 存放配置文件中读取的页面列表对象集合.
   */
  private List<ListYml> ymlList = new ArrayList<ListYml>();
  /**
   * 存放树形图中已勾选对象的集合.
   */
  private List<NodeObj> nodeObjList = new ArrayList<NodeObj>();

  public List<NodeObj> getNodeObjList() {
    return nodeObjList;
  }

  public void setNodeObjList(List<NodeObj> nodeObjList) {
    this.nodeObjList = nodeObjList;
  }

  /**
   * 树形图复选框勾选模式.
   */
  private String checkType;
  final int frameWidth = 920;
  final int frameHeight = 570;

  public void setRootFolderPath(String rootFolderPath) {
    this.rootFolderPath = rootFolderPath;
  }

  /**
   * 窗口组件初始化.
   */
  public PublishPane() {
    super();
    comboBox.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          comboBoxSelectIndex = comboBox.getSelectedIndex();
          // 生成树形图
          createTree(comboBoxSelectIndex);
          System.out.println(comboBoxSelectIndex);
        }
      }
    });

    DefaultTreeModel model = new DefaultTreeModel(new CheckBoxTreeNode());
    tree.setModel(model);

    setSize(frameWidth, frameHeight);
    setLayout(new BorderLayout());

    // 主窗口center部分（中间的下拉框和树形图）
    // textArea.setFont(new Font("黑体", Font.BOLD, fontSize));
    // textArea.setLineWrap(true);
    // scrollPane.setColumnHeaderView(comboBox);

    // "create tree"按钮添加点击生成页面列表事件
    createTreeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (rootFolderPath != null && !"".equals(rootFolderPath)) {
          // 如果已经选择过文件夹，开始生成页面列表
          ListYamlReadUtils util = new ListYamlReadUtils();
          List<File> fileList = FileUtils.searchFile(new File(rootFolderPath + "\\_data"));
          ymlList = new ArrayList<ListYml>();
          for (File f : fileList) {
            if (util.ymlToObject(f) != null && util.ymlToObject(f).getBigheader() != null) {
              ymlList.add(util.ymlToObject(f));
            }
          }
          // 填充下拉框
          comboBox.removeAllItems();
          String[] comboBoxTitle = new String[ymlList.size()];
          for (int i = 0; i < ymlList.size(); i++) {
            comboBox.addItem(ymlList.get(i).getBigheader());
          }

        } else {
          // 如果还没有选择过文件夹，弹出提示窗口
          JOptionPane.showMessageDialog(null, "请选择项目根路径!");
        }
      }
    });

    jekyllServeButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (rootFolderPath == null || "".equals(rootFolderPath)) {
          // 如果还没有选择过文件夹，弹出提示窗口
          JOptionPane.showMessageDialog(null, "请选择项目根路径!");
          return;
        }

        try {
          CmdWorker jekyllWorker = new CmdWorker("cmd /c bundle exec jekyll serve",
              new File(rootFolderPath), logArea);
          jekyllWorker.execute();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });

    // "html to pdf"按钮添加点击事件
    btnHtmlToPdf.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (rootFolderPath != null && !"".equals(rootFolderPath)) {
          try {
            List<NodeObj> list = new ArrayList<NodeObj>();
            CheckBoxTreeNode root = (CheckBoxTreeNode) tree
                .getPathForRow(tree.getRowForLocation(0, 0)).getLastPathComponent();
            // 获得所有已勾选节点
            getSelectedNodes(root, list);
            String createPdf = "wkhtmltopdf toc ";
            for (NodeObj nodeobj : list) {
              if (nodeobj.getPath() != null && !nodeobj.toString().equals("")) {
                String str = nodeobj.getPath().replace(rootFolderPath, "").replace("\\", "/")
                    .replace(".md", ".html");
                str = "localhost:4000" + str;
                createPdf = createPdf + str + " ";
              }
            }
            // pdf文件名加上时间戳
            String savePath = System.getProperty("user.dir") + "\\htmlToPdf"
                + System.currentTimeMillis() + ".pdf";
            createPdf = createPdf + " " + savePath;

            System.out.println(createPdf);

            logArea.append("------------------------生成pdf------------------------\n");
            logArea.append("执行生成pdf命令：" + createPdf + "\n");
            logArea.append("文件路径：" + savePath + "\n");
            CmdWorker pdfWorker = new CmdWorker("cmd /c " + createPdf,
                null, logArea);
            pdfWorker.execute();
          } catch (Exception exception) {
            exception.printStackTrace();
          }
        } else {
          // 如果还没有选择过文件夹，弹出提示窗口
          JOptionPane.showMessageDialog(null, "请选择项目根路径!");
        }
      }
    });

    // 主窗口north部分按钮功能（顶部"select file"按钮及文件夹路径显示框）
    JPanel northPanle = new JPanel(new FlowLayout(FlowLayout.LEFT));
    createTreeButton.setMaximumSize(new Dimension(90, 30));
    jekyllServeButton.setMaximumSize(new Dimension(90, 30));
    northPanle.add(createTreeButton);
    northPanle.add(jekyllServeButton);
    northPanle.add(btnHtmlToPdf);
    add(northPanle, BorderLayout.NORTH);

    JPanel panelContainer = new JPanel();

    // centerPanel.removeAll();
    // scrollPane.remove(tree);
    // CheckBoxTreeNodeUtils nodeUtils = new CheckBoxTreeNodeUtils();
    // List<CheckBoxTreeNode> treeNodeList = new ArrayList<CheckBoxTreeNode>();
    // nodeUtils.createTreeNodes(ymlList, treeNodeList);

    // tree = new JTree();
    // tree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
    // DefaultTreeModel model = new DefaultTreeModel(treeNodeList.get(index));
    // tree.setModel(model);
    // tree.setCellRenderer(new CheckBoxTreeCellRenderer());

    chckbxSelectAll.addMouseListener(new CheckBoxSelectAllListener());

    // 生成窗口中间部分
    add(panelContainer, BorderLayout.CENTER);
    centerPanel.setLayout(new BorderLayout());

    JPanel comboBoxPanel = new JPanel();
    comboBoxPanel.setLayout(new BoxLayout(comboBoxPanel, BoxLayout.Y_AXIS));

    // 生成下拉框
    // comboBox.setBounds(5, 13, 590, 24);
    comboBoxPanel.add(comboBox);

    JPanel checkboxPanel = new JPanel();
    checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.X_AXIS));

    // 生成多选框
    checkboxPanel.add(chckbxSelectAll);

    checkboxPanel.add(chckbxSelectParent);

    checkboxPanel.add(chckbxSelectChildren);

    comboBoxPanel.add(checkboxPanel);
    centerPanel.add(comboBoxPanel, BorderLayout.NORTH);

    centerPanel.add(scrollPane, BorderLayout.CENTER);

    // 生成树形图
    scrollPane.setViewportView(tree);
    scrollPane.validate();

    // panelContainer 的布局为 GridBagLayout
    panelContainer.setLayout(new GridBagLayout());

    GridBagConstraints c1 = new GridBagConstraints();
    c1.gridx = 0;
    c1.gridy = 0;
    c1.weightx = 0;
    c1.weighty = 0;
    c1.fill = GridBagConstraints.BOTH;
    panelContainer.add(centerPanel, c1);

    JScrollPane middlePanel = new JScrollPane();
    logArea.setLineWrap(true); // 激活自动换行功能
    logArea.setWrapStyleWord(true); // 激活断行不断字功能
    middlePanel.add(logArea);
    middlePanel.setViewportView(logArea);
    middlePanel.validate();
    GridBagConstraints c2 = new GridBagConstraints();
    c2.gridx = 1;
    c2.gridy = 0;
    c2.weightx = 1;
    c2.weighty = 1;
    c2.fill = GridBagConstraints.BOTH;
    panelContainer.add(middlePanel, c2);

    panelContainer.setOpaque(true);
  }

  /**
   * 生成窗口主页面中的树形图.
   * 
   * @param index
   *          下拉框的被选中项
   */
  public void createTree(int index) {
    logArea.setText("");

    scrollPane.remove(tree);
    CheckBoxTreeNodeUtils nodeUtils = new CheckBoxTreeNodeUtils();
    List<CheckBoxTreeNode> treeNodeList = new ArrayList<CheckBoxTreeNode>();
    nodeUtils.createTreeNodes(ymlList, treeNodeList);

    tree = new JTree();
    tree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
    DefaultTreeModel model = new DefaultTreeModel(treeNodeList.get(index));
    tree.setModel(model);
    tree.setCellRenderer(new CheckBoxTreeCellRenderer());

    // 生成树形图
    scrollPane.setViewportView(tree);
    scrollPane.validate();

  }

  /**
   * 根据根节点遍历获取所有被选择节点.
   * 
   * @param root
   *          根节点
   * @param lst
   *          存放所有被选择节点的集合
   */
  public void getSelectedNodes(TreeNode root, List lst) {
    if (root.getChildCount() > 0) {
      for (Enumeration e = root.children(); e.hasMoreElements();) {
        CheckBoxTreeNode n = (CheckBoxTreeNode) e.nextElement();
        if (n.getUserObject() instanceof NodeObj && n.isSelected()) {
          lst.add(((NodeObj) n.getUserObject()));
        }
        getSelectedNodes(n, lst);
      }
    }
  }

  /**
   * 树形图鼠标监听器.
   * 
   * @author SunYichuan
   */
  class CheckBoxTreeNodeSelectionListener extends MouseAdapter {
    List<NodeObj> list;

    /**
     * 鼠标点击监听器.
     */
    @Override
    public void mouseClicked(MouseEvent event) {
      getCheckType();
      System.out.println("type=" + checkType);
      JTree tree = (JTree) event.getSource();
      int x = event.getX();
      int y = event.getY();
      int row = tree.getRowForLocation(x, y);
      TreePath path = tree.getPathForRow(row);
      // 点击后改变复选框状态
      if (path != null) {
        CheckBoxTreeNode node = (CheckBoxTreeNode) path.getLastPathComponent();
        if (node != null) {
          // String type =R.SUBNODES;
          node.setSelected(!node.isSelected(), checkType);
          ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(node);
        }
      }
      list = new ArrayList<NodeObj>();
      // 获得根节点
      CheckBoxTreeNode root = (CheckBoxTreeNode) tree.getPathForRow(tree.getRowForLocation(0, 0))
          .getLastPathComponent();
      // 遍历所有节点，并获得已勾选节点
      getSelectedNodes(root, list);
      show(list);
    }

    private void show(List<NodeObj> l) {
      System.out.println("--------------list-----------------");
      for (NodeObj o : l) {
        System.out.print("<" + o.getName() + "," + o.getPath() + ">");
      }
      System.out.println();
    }
  }

  /**
   * 从状态复选框中获得目前的复选框勾选状态.
   */
  public String getCheckType() {
    if (!chckbxSelectParent.isSelected() && chckbxSelectChildren.isSelected()) {
      // 如果只勾选规则“选择子节点”
      checkType = R.SUBNODES;
    } else if (chckbxSelectParent.isSelected() && !chckbxSelectChildren.isSelected()) {
      // 如果只勾选规则“选择父节点”
      checkType = R.PARENT;
    } else if (!chckbxSelectParent.isSelected() && !chckbxSelectChildren.isSelected()) {
      // 如果规则“选择父节点”和“选择子节点”都没勾选
      checkType = "";
    } else if (chckbxSelectParent.isSelected() && chckbxSelectChildren.isSelected()) {
      // 如果规则“选择父节点”和“选择子节点”都被勾选
      checkType = R.SUBNODES_AND_PARENT;
    }
    return checkType;
  }

  class CheckBoxSelectAllListener extends MouseAdapter {
    List<NodeObj> list;

    /**
     * 鼠标点击监听器.
     */
    @Override
    public void mouseClicked(MouseEvent event) {
      // JTree tree = (JTree) event.getSource();
      // 点击后改变复选框状态
      list = new ArrayList<NodeObj>();
      // 获得根节点
      CheckBoxTreeNode root = (CheckBoxTreeNode) tree.getPathForRow(tree.getRowForLocation(0, 0))
          .getLastPathComponent();
      // 遍历所有节点，并获得已勾选节点
      if (root != null) {

        root.setSelected(!root.isSelected, R.SUBNODES_AND_PARENT);
        ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(root);
      }
      // 遍历所有节点，并获得已勾选节点
      getSelectedNodes(root, list);
    }
  }
}
