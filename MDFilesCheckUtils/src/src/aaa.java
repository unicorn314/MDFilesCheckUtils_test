package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class aaa {

    private JFrame frame;

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
    final JLabel label = new JLabel();
    final JProgressBar jpb = new JProgressBar();
    JTextArea textArea = new JTextArea(tWidth, tHeight);

    // 文件选择按钮
    final JButton selectFileButton = new JButton("select file");
    // 开始检测按钮
    final JButton checkButton = new JButton("start check");
    private final JComboBox comboBox = new JComboBox();
    private final JTree tree = new JTree();
    private final JButton btnHtmlToPdf = new JButton("html to pdf");
    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(fWidth, fHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("低检工具");
        frame.getContentPane().setLayout(null);
//        frame.getContentPane().setLayout(new BorderLayout());

        // 主窗口center部分（中间结果显示文本域）
        textArea.setFont(new Font("黑体", Font.BOLD, fontSize));
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(0, 42, 902, 444);
        frame.getContentPane().add(scrollPane);
        
        scrollPane.setColumnHeaderView(comboBox);
        tree.setModel(new DefaultTreeModel(
            new DefaultMutableTreeNode("JTree") {
                {
                    DefaultMutableTreeNode node_1;
                    node_1 = new DefaultMutableTreeNode("colors");
                        node_1.add(new DefaultMutableTreeNode("blue"));
                        node_1.add(new DefaultMutableTreeNode("violet"));
                        node_1.add(new DefaultMutableTreeNode("red"));
                        node_1.add(new DefaultMutableTreeNode("yellow"));
                    add(node_1);
                    node_1 = new DefaultMutableTreeNode("sports");
                        node_1.add(new DefaultMutableTreeNode("basketball"));
                        node_1.add(new DefaultMutableTreeNode("soccer"));
                        node_1.add(new DefaultMutableTreeNode("football"));
                        node_1.add(new DefaultMutableTreeNode("hockey"));
                    add(node_1);
                    node_1 = new DefaultMutableTreeNode("food");
                        node_1.add(new DefaultMutableTreeNode("hot dogs"));
                        node_1.add(new DefaultMutableTreeNode("pizza"));
                        node_1.add(new DefaultMutableTreeNode("ravioli"));
                        node_1.add(new DefaultMutableTreeNode("bananas"));
                    add(node_1);
                }
            }
        ));
        
        scrollPane.setRowHeaderView(tree);

        // 文件夹路径显示框
        final TextField rootFolderTextField = new TextField(50);
        rootFolderTextField.setFont(new Font("黑体", Font.LAYOUT_LEFT_TO_RIGHT,
                fontSize));

        // 弹出文件选择窗口
        final JFileChooser chooser = new JFileChooser();
        chooser.setApproveButtonText("确定");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        
        JPanel northPanle = new JPanel();
        northPanle.setBounds(0, 0, 902, 42);
        rootFolderTextField.setEditable(false);
        selectFileButton.setMaximumSize(new Dimension(90, 30));
        northPanle.add(selectFileButton);
        northPanle.add(rootFolderTextField);
        frame.getContentPane().add(northPanle);

        // 主窗口south部分（底部按钮及进度条）
        JPanel southPanel = new JPanel();
        southPanel.setBounds(0, 486, 902, 37);
        
        southPanel.add(btnHtmlToPdf);
        checkButton.setMaximumSize(new Dimension(90, 30));
        southPanel.add(checkButton);
        southPanel.add(jpb);
        southPanel.add(label);
        frame.getContentPane().add(southPanel);
        frame.setVisible(true);
    }

}
