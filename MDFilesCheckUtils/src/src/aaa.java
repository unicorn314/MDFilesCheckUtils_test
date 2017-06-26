package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import model.ListYml;
import model.NodeObj;
import tools.CheckBoxTreeNodeUtils;
import tools.ListYamlReadUtils;
import javax.swing.JCheckBox;

public class aaa {

//    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    aaa window = new aaa();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public aaa() {
        initialize();
    }

    final int tWidth = 0;
    final int tHeight = 0;
    final int fWidth = 920;
    final int fHeight = 570;
    final int fontSize = 20;
    // 新建主窗口
    JFrame frame = new JFrame();
    final JLabel label = new JLabel();
    final JProgressBar jpb = new JProgressBar();
    JTextArea textArea = new JTextArea(tWidth, tHeight);
//    private final JTree tree = new JTree();
    private final JButton btnHtmlToPdf = new JButton("html to pdf");

    // 文件选择按钮
    final JButton selectFileButton = new JButton("select file");
    // 确认按钮
    final JButton createTreeButton = new JButton("create tree");
    // 开始检测按钮
    final JButton checkButton = new JButton("start check");

    JPanel centerPanel = new JPanel();
    JTree tree = new JTree();
    
    // 下拉框当前选择的第几个选项
    int comboBoxSelectIndex = 0;
    private final JPanel panel = new JPanel();
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        
        
        
        
        
        frame.setSize(fWidth, fHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("低检工具");
        frame.getContentPane().setLayout(new BorderLayout());

        // 主窗口center部分（中间的下拉框和树形图）
//      textArea.setFont(new Font("黑体", Font.BOLD, fontSize));
//      textArea.setLineWrap(true);
//      scrollPane.setColumnHeaderView(comboBox);

        
        // 文件夹路径显示框
        final TextField rootFolderTextField = new TextField(50);
        rootFolderTextField.setFont(new Font("黑体", Font.LAYOUT_LEFT_TO_RIGHT,
                fontSize));

        // 弹出文件选择窗口
        final JFileChooser chooser = new JFileChooser();
        chooser.setApproveButtonText("确定");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 设置只选择目录
        // "select file"按钮添加点击弹出选择文件窗口事件
        
        // 主窗口north部分按钮功能（顶部"select file"按钮及文件夹路径显示框）
        JPanel northPanle = new JPanel();
        rootFolderTextField.setEditable(false);
        createTreeButton.setMaximumSize(new Dimension(90, 30));
        selectFileButton.setMaximumSize(new Dimension(90, 30));
        northPanle.add(createTreeButton);
        northPanle.add(selectFileButton);
        northPanle.add(rootFolderTextField);
        frame.getContentPane().add(northPanle, BorderLayout.NORTH);

        // 主窗口south部分（底部按钮及进度条）
        JPanel southPanel = new JPanel();
        
        southPanel.add(btnHtmlToPdf);
        
        checkButton.setMaximumSize(new Dimension(90, 30));
        southPanel.add(checkButton);
        southPanel.add(jpb);
        southPanel.add(label);
        frame.getContentPane().add(southPanel, BorderLayout.SOUTH);
        
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);
        
        JComboBox comboBox = new JComboBox();
        comboBox.setBounds(149, 13, 590, 24);
        panel.add(comboBox);
        
        JCheckBox chckbxSelectAll = new JCheckBox("select all");
        chckbxSelectAll.setBounds(143, 46, 133, 27);
        panel.add(chckbxSelectAll);
        
        JCheckBox chckbxSelectParent = new JCheckBox("select parent");
        chckbxSelectParent.setBounds(274, 46, 156, 27);
        panel.add(chckbxSelectParent);
        
        JCheckBox chckbxSelectChildren = new JCheckBox("select children");
        chckbxSelectChildren.setBounds(436, 46, 163, 27);
        panel.add(chckbxSelectChildren);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(153, 81, 590, 350);
        panel.add(scrollPane);
        
        JTree tree_1 = new JTree();
        scrollPane.setViewportView(tree_1);
        frame.setVisible(true);
        
        

        
    }
}
