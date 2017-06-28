package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * 主界面.
 * 
 * @author ywx474563 2017年6月26日
 */
public class Main {
  /**
   * 存放选项卡的组件.
   */
  private JTabbedPane tabPane = new JTabbedPane();

  final int frameWidth = 920;
  final int frameHeight = 570;
  final int fontSize = 20;
  // 新建主窗口
  JFrame frame = new JFrame();
  JScrollPane scrollPane = new JScrollPane();
  JPanel centerPanel = new JPanel();

  // 文件选择按钮
  final JButton selectFileButton = new JButton("select file");

  private CheckPane checkPane;

  private PublishPane publishPane;

  /**
   * 默认构造方法，所有窗口组件初始化.
   */
  public Main() {
    init();
  }

  /**
   * 窗口组件初始化.
   */
  private void init() {

    frame.setSize(frameWidth, frameHeight);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("发布工具");
    frame.setLayout(new BorderLayout());

    // 加入低检选项卡
    checkPane = new CheckPane();
    tabPane.addTab("低检", null, checkPane, "check");

    // 加入离线发布选项卡
    publishPane = new PublishPane();
    tabPane.addTab("离线发布", null, publishPane, "publish");

    frame.add(tabPane, BorderLayout.CENTER);

    // 文件夹路径显示框
    final TextField rootFolderTextField = new TextField(50);
    rootFolderTextField.setFont(new Font("黑体", Font.LAYOUT_LEFT_TO_RIGHT, fontSize));

    // 弹出文件选择窗口
    final JFileChooser chooser = new JFileChooser();
    chooser.setApproveButtonText("确定");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 设置只选择目录
    // "select file"按钮添加点击弹出选择文件窗口事件
    selectFileButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        int returnVal = chooser.showOpenDialog(chooser);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          // 选择文件夹后，将所选文件夹路径显示到主窗口顶部文本框中
          rootFolderTextField.setText(chooser.getSelectedFile().getPath());
          rootFolderTextField.validate();
          checkPane.setRootFolderPath(chooser.getSelectedFile().getPath());
          publishPane.setRootFolderPath(chooser.getSelectedFile().getPath());
        }
      }
    });

    // 主窗口north部分按钮功能（顶部"select file"按钮及文件夹路径显示框）

    JPanel northPanle = new JPanel();
    northPanle.setLayout(new FlowLayout(FlowLayout.LEFT));
    rootFolderTextField.setEditable(false);
    selectFileButton.setMaximumSize(new Dimension(90, 30));
    northPanle.add(new JLabel("路径："));
    northPanle.add(rootFolderTextField);
    northPanle.add(selectFileButton);
    frame.add(northPanle, BorderLayout.NORTH);

    frame.setVisible(true);

  }

  /**
   * 生成文件选择窗口和结果显示窗口.
   * 
   * @param args
   *          ...
   */
  public static void main(final String[] args) { // java程序的主入口处
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new Main();
      }
    });
  }
}
