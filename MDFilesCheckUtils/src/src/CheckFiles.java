package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import model.ListYml;
import model.MyURL;
import model.NodeObj;
import model.R;
import tools.CheckBoxTreeNodeUtils;
import tools.HttpLinkChecker;
import tools.ListYamlReadUtils;
import tools.TitleChecker;
import tools.WrongPathChecker;

/**
 * 低检工具主类.
 * @author SunYichuan
 *
 */
public class CheckFiles {
  /**
   * 存放选项卡的组件.
   */
  private JTabbedPane jTabbedpane = new JTabbedPane();
    /**
     * 存放所有失效的引用链接.
     */
    private List<MyURL> badURLList = new ArrayList<MyURL>();
    /**
     * 存放所有错误的include标签内引用路径.
     */
    private List<MyURL> wrongIncludePathList = new ArrayList<MyURL>();
    /**
     * 存放所有错误的内部引用路径.
     */
    private List<MyURL> wrongInternalPathList = new ArrayList<MyURL>();
    /**
     * 存放所有字符编码不为UTF-8格式的文件.
     */
    private List<MyURL> wrongCharsetFile = new ArrayList<MyURL>();
    /**
     * 存放所有title部分格式不正确的文件.
     */
    private List<MyURL> wrongTitleFile = new ArrayList<MyURL>();
    /**
     * 存放最终输出结果的字符串.
     */
    private String resultStr = "";
    /**
     * 存放配置文件中读取的页面列表对象集合
     */
    private List<ListYml> ymlList = new ArrayList<ListYml>();
    /**
     * 存放树形图中已勾选对象的集合
     */
    private List<NodeObj> nodeObjList = new ArrayList<NodeObj>();
    /**
     * 树形图复选框勾选模式
     */
    private String checkType;

    public List<NodeObj> getNodeObjList() {
        return nodeObjList;
    }

    public void setNodeObjList(List<NodeObj> nodeObjList) {
        this.nodeObjList = nodeObjList;
    }

    /**
     * 默认构造方法，所有窗口组件初始化
     */
    public CheckFiles() {
    	init();
    }

    /**
     * 失效的引用链接.
     * @return badURLList
     */
    public List<MyURL> getBadURLList() {
        return badURLList;
    }
    /**
     * 错误的include标签内引用路径.
     * @return wrongIncludePathList
     */
    public List<MyURL> getWrongIncludePathList() {
        return wrongIncludePathList;
    }
    /**
     * 错误的内部引用路径.
     * @return wrongInternalPathList
     */
    public List<MyURL> getWrongInternalPathList() {
        return wrongInternalPathList;
    }
    /**
     * 字符编码不为UTF-8格式的文件.
     * @return wrongCharsetFile
     */
    public List<MyURL> getWrongCharsetFile() {
        return wrongCharsetFile;
    }
    /**
     * title部分格式不正确的文件.
     * @return wrongTitleFile
     */
    public List<MyURL> getWrongTitleFile() {
        return wrongTitleFile;
    }
    /**
     * 结果字符串.
     * @return resultStr
     */
    public String getResultStr() {
        return resultStr;
    }

    /**
     * 递归查找指定路径下所有的.md和.yml文件.
     * @param folder 指定搜索的文件夹
     * @return 所有的文件数组
     */
    public List<File> searchFile(final File folder) {
        try {
            File[] subFolders = folder.listFiles();
            List<File> result = new ArrayList<File>(); // 声明一个集合
            for (int i = 0; i < subFolders.length; i++) { // 循环显示文件夹或文件
                if (subFolders[i].isFile()) { // 如果是文件,加入列表
                    result.add(subFolders[i]);
                } else { // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                    List<File> foldResult = searchFile(subFolders[i]);
                    // 循环显示文件
                    for (int j = 0; j < foldResult.size(); j++) {
                        result.add(foldResult.get(j)); // 文件保存到集合中
                    }
                }
            }
            return result;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断include标签中的引用路径是否有效.
     * @param rootPath 项目根路径
     * @param strPath 引用路径
     * @return 返回true或者false 　　
     */
    public boolean isValidPath(final String rootPath,
            final String strPath) {
        String include;
        Pattern pattern = Pattern.compile("/$");
        Matcher matcher = pattern.matcher(rootPath);
        if (matcher.find()) {
            include = "_include/";
        } else {
            include = "/_include/";
        }
        File folder = new File(rootPath + include + strPath);
        if (folder.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 在文件中搜索失效链接.
     * @param file 输入需要检索的文件
     */
    public void searchBadURL(final File file) {
    	HttpLinkChecker checker = new HttpLinkChecker();
    	badURLList.addAll(checker.searchBadURL(file));
    }

    /**
     * 在文件中搜索include标签中错误的引用文件路径.
     * @param file 需要检测的文件
     * @param rootPath 项目根路径
     */
    public void searchWrongIncludePath(final File file, final String rootPath) {
        // include正则判断
        String rex = "\\{%[\\s]+include[\\s]+[/]?"
                + "[a-zA-z0-9\\/\\.]*[\\s]+%\\}";
        Pattern pattern = Pattern.compile(rex);

        LineNumberReader lineReader = null;
        try {
            lineReader = new LineNumberReader(new FileReader(file));
            String readLine = null;

            // 读取文件内容
            while ((readLine = lineReader.readLine()) != null) {
                Matcher matcher = pattern.matcher(readLine);
                while (matcher.find()) { // 假如这一行存在可以匹配include标签正则表达式的字符串
                    // 将路径字符串从include标签字符串中切割出来
                    String includePath = matcher.group(0).replaceAll(
                            "\\{%[\\s]+include[\\s]+", "")
                            .replaceAll("[\\s]+%\\}", "");

                    // 检测路径是否可用
                    if (!isValidPath(rootPath, includePath.trim())) {
                        MyURL myURL = new MyURL();
                        myURL.setFile(file.getParent()
                                + "\\" + file.getName());
                        myURL.setUrl(includePath);
                        // 若路径不可用，将这个网址所属的文件名和网址字符串插入到错误路径list
                        wrongIncludePathList.add(myURL);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭流
            close(lineReader);
        }
        }

    /**
     * 检查title部分内容是否符合格式.
     * @param file 需要检测的文件
     */
    public void searchWrongTitle(final File file) {
    	TitleChecker checker = new TitleChecker();
    	if (checker.searchWrongTitle(file) != null) {    		
    		wrongTitleFile.addAll(checker.searchWrongTitle(file)) ;
    	}
    }

    /**
     * 检测文件编码是否为UTF-8.
     * @param file 输入文件
     * @throws FileNotFoundException 文件不存在
     * @throws IOException IO错误
     */
    public void checkCharset(final File file)
            throws FileNotFoundException, IOException {
        String charset
            = new FileCharsetDetector().guessFileEncoding(file);
        if (!charset.equals("UTF-8")) {
            MyURL myURL = new MyURL();
            myURL.setFile(file.getParent() + "\\" + file.getName());
            myURL.setUrl(charset);
            // 将编码不为UTF-8的文件路径和编码格式记入对应list
            wrongCharsetFile.add(myURL);
        }
    }

    /**
     * 关闭流.
     * @param able 需要关闭的流
     */
    private void close(final Closeable able) {
        if (able != null) {
            try {
                able.close();
            } catch (IOException e) {
                e.printStackTrace();
//                able = null;
            }
        }
    }

    /**
     * 启动线程，开始检测文件.
     * @param rootPath 项目根路径
     */
    public void check(final String rootPath) {
        File folder = new File(rootPath); // 指定项目根目录
        if (!folder.exists()) { // 如果文件夹不存在
            resultStr = resultStr + "目录不存在："
                + folder.getAbsolutePath() + "\r\n";
            return;
        }
        List<File> result = searchFile(folder); // 调用方法获得文件数组
        resultStr = resultStr + "在 " + folder
                + " 以及所有子文件时查找对象.md文件及.yml文件" + "\r\n";
        ProGressWork work = new ProGressWork(result,rootPath);
        work.execute();
    }
    /**
     * 生成窗口主页面中的树形图
     * @param index 下拉框的被选中项
     */
    public void createTree (int index) {
        centerPanel.removeAll();
        scrollPane.remove(tree);
        CheckBoxTreeNodeUtils nodeUtils = new CheckBoxTreeNodeUtils();
        List<CheckBoxTreeNode> treeNodeList = new ArrayList<CheckBoxTreeNode>();
        nodeUtils.createTreeNodes(ymlList, treeNodeList);
        
        tree = new JTree();
        tree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
        DefaultTreeModel model = new DefaultTreeModel(treeNodeList.get(index));
        tree.setModel(model);
        tree.setCellRenderer(new CheckBoxTreeCellRenderer());
        
        chckbxSelectAll.addMouseListener(new CheckBoxSelectAllListener());
        
        // 生成窗口中间部分
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(null);
        
        // 生成下拉框
        comboBox.setBounds(149, 13, 590, 24);
        centerPanel.add(comboBox);
        
        // 生成多选框
        chckbxSelectAll.setBounds(143, 46, 133, 27);
        centerPanel.add(chckbxSelectAll);
        
        chckbxSelectParent.setBounds(274, 46, 156, 27);
        centerPanel.add(chckbxSelectParent);
        
        chckbxSelectChildren.setBounds(436, 46, 163, 27);
        centerPanel.add(chckbxSelectChildren);
        
        scrollPane.setBounds(153, 82, 590, 349);
        centerPanel.add(scrollPane);
        
        // 生成树形图
        scrollPane.setViewportView(tree);
        scrollPane.validate();
        frame.setVisible(true);
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
	JPanel centerPanel = new JPanel();
	
	private JComboBox comboBox = new JComboBox();
//    private final JTree tree = new JTree();
    private final JButton btnHtmlToPdf = new JButton("html to pdf");

	// 文件选择按钮
	final JButton selectFileButton = new JButton("select file");
	// 确认按钮
	final JButton createTreeButton = new JButton("create tree");
	// 开始检测按钮
	final JButton checkButton = new JButton("start check");

	JScrollPane scrollPane = new JScrollPane();
	JTree tree = new JTree();
	
	// 下拉框当前选择的第几个选项
	int comboBoxSelectIndex = 0;
	// 状态复选框
	JCheckBox chckbxSelectAll = new JCheckBox("select all");
	JCheckBox chckbxSelectParent = new JCheckBox("select parent");
	JCheckBox chckbxSelectChildren = new JCheckBox("select children");
	/**
	 * 窗口组件初始化
	 */
	private void init() {
	    comboBox.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                if( e.getStateChange() == ItemEvent.SELECTED ) {
                    comboBoxSelectIndex = comboBox.getSelectedIndex();
                 // 生成树形图
                    createTree(comboBoxSelectIndex);
                    System.out.println(comboBoxSelectIndex);
                }
            }
        });
	    
	    DefaultTreeModel model = new DefaultTreeModel(new CheckBoxTreeNode());
	    tree.setModel(model);
	    
		frame.setSize(fWidth, fHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("低检工具");
		frame.setLayout(new BorderLayout());

		// 主窗口center部分（中间的下拉框和树形图）
//		textArea.setFont(new Font("黑体", Font.BOLD, fontSize));
//		textArea.setLineWrap(true);
//		scrollPane.setColumnHeaderView(comboBox);
		frame.add(scrollPane, BorderLayout.CENTER);

        
		// 文件夹路径显示框
		final TextField rootFolderTextField = new TextField(50);
		rootFolderTextField.setFont(new Font("黑体", Font.LAYOUT_LEFT_TO_RIGHT,
				fontSize));

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
					rootFolderTextField.setText(chooser.getSelectedFile()
							.getPath());
					rootFolderTextField.validate();
				}
			}
		});
		// "start check"按钮添加点击开始扫描文件夹事件
		checkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rootFolderTextField.getText() != null
						&& !"".equals(rootFolderTextField.getText())) {
					// 如果已经选择过文件夹，开始检测
					check(rootFolderTextField.getText());
					// 清空所有错误列表和结果显示界面
					cleanAllLists();
					textArea.setText("");
					checkButton.setEnabled(false);
				} else {
					// 如果还没有选择过文件夹，弹出提示窗口
					JOptionPane.showMessageDialog(null, "请选择项目根路径!");
				}
			}
		});

		// "create tree"按钮添加点击生成页面列表事件
		createTreeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rootFolderTextField.getText() != null
                        && !"".equals(rootFolderTextField.getText())) {
                    // 如果已经选择过文件夹，开始生成页面列表
                    ListYamlReadUtils util = new ListYamlReadUtils();
                    List<File> fileList = searchFile(new File(rootFolderTextField.getText()+"\\_data"));
                    ymlList = new ArrayList<ListYml>();
                    for (File f : fileList) {
                        if (util.ymlToObject(f)!=null
                                && util.ymlToObject(f).getBigheader() != null){
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
		// "html to pdf"按钮添加点击事件
		btnHtmlToPdf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rootFolderTextField.getText() != null
                        && !"".equals(rootFolderTextField.getText())) {
                    try {
                        List<NodeObj> list = new ArrayList<NodeObj>();
                        CheckBoxTreeNode root = (CheckBoxTreeNode) tree
                                .getPathForRow(tree.getRowForLocation(0, 0)).getLastPathComponent();
                        // 获得所有已勾选节点
                        getSelectedNodes(root, list);
                        String createPdf = "wkhtmltopdf toc ";
                        for (NodeObj nodeobj : list) {
                            if (nodeobj.getPath() != null && !nodeobj.equals("")){
                                String str = nodeobj.getPath()
                                        .replace(rootFolderTextField.getText(), "")
                                        .replace("\\", "/")
                                        .replace(".md", ".html");
                                str = "localhost:4000" + str;
                                createPdf = createPdf + str + " ";
                            }
                        }
                        // pdf文件名加上时间戳
                        createPdf = createPdf + " " + rootFolderTextField.getText()
                                + "\\htmlToPdf" + System.currentTimeMillis() + ".pdf";
                        // 将转换命令保存到txt文件中
                        PrintStream mytxt = new PrintStream("./htmlToPdf.txt");
                        PrintStream out = System.out;
                        System.setOut(mytxt);
                        System.out.println(createPdf);
                        System.setOut(out);
                        mytxt.close();
                        out.close();
                    } catch (FileNotFoundException exception) {
                        exception.printStackTrace();
                    }
                    // 弹出窗口提示检查完成，并提示日志位置
                    JOptionPane.showMessageDialog(null, "生成pdf命令已保存在本地目录下的htmlToPdf.txt中。");
                } else {
                    // 如果还没有选择过文件夹，弹出提示窗口
                    JOptionPane.showMessageDialog(null, "请选择项目根路径!");
                }
            }
        });
		// 主窗口north部分按钮功能（顶部"select file"按钮及文件夹路径显示框）
		JPanel northPanle = new JPanel();
		rootFolderTextField.setEditable(false);
		createTreeButton.setMaximumSize(new Dimension(90, 30));
		selectFileButton.setMaximumSize(new Dimension(90, 30));
		northPanle.add(createTreeButton);
		northPanle.add(selectFileButton);
		northPanle.add(rootFolderTextField);
		frame.add(northPanle, BorderLayout.NORTH);

		// 主窗口south部分（底部按钮及进度条）
		JPanel southPanel = new JPanel();
		
		southPanel.add(btnHtmlToPdf);
		
		checkButton.setMaximumSize(new Dimension(90, 30));
		southPanel.add(checkButton);
		southPanel.add(jpb);
		southPanel.add(label);
		frame.add(southPanel, BorderLayout.SOUTH);
		frame.setVisible(true);

		// 将结果写入页面中间文本框，并刷新窗口显示内容
//		textArea.setText(resultStr);
//		textArea.validate();
		
		
	}

	/**
	 * 清空所有list.
	 */
	public void cleanAllLists() {
		badURLList = new ArrayList<MyURL>();
		wrongIncludePathList = new ArrayList<MyURL>();
		wrongInternalPathList = new ArrayList<MyURL>();
		wrongCharsetFile = new ArrayList<MyURL>();
		wrongTitleFile = new ArrayList<MyURL>();
		resultStr = "";
	}

	/**
	 * 生成文件选择窗口和结果显示窗口.
	 * 
	 * @param args ...
	 */
	public static void main(final String[] args) { // java程序的主入口处
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new CheckFiles();
			}
		});
	}

	/**
	 * 线程类.
	 * 用来在检查文件的同时动态生成进度条.
	 * @author SunYichuan
	 */
	class ProGressWork extends SwingWorker<List<File>, File> {
		private List<File> result;
		private String rootPath;

		public ProGressWork(List<File> files, String path) {
			super();
			result = files;
			rootPath = path;
		}

		/**
		 * 启动该线程，开始检测所有文件，并同步刷新进度条.
		 */
		@Override
		protected List<File> doInBackground() throws Exception {
			/*
			 * 检查项目包含： 
			 * 1.所有.md文件及.yml文件中的网页链接是否有效 
			 * 2.所有include标签中的引用文件路径是否有效
			 * 3.所有文件的编码格式是否为UTF-8编码 
			 * 4.文件头title的区域标识线是否为3个减号，有没有多写或者少写
			 * 5.文件头title的内容部分，键值对之间的冒号后是否有加上空格
			 * 6.所有引入的内部路径是否正确
			 */
			WrongPathChecker wrongPathChecker = new WrongPathChecker();
			for (int i = 0; i < result.size(); i++) { // 循环显示文件
				File file = result.get(i);
				try {
					searchBadURL(file);
					searchWrongIncludePath(file, rootPath);
					checkCharset(file);
					searchWrongTitle(file);
					List<MyURL> wrongIntercalPath = wrongPathChecker
							.searchWrongIntercalPath(file);
					wrongInternalPathList.addAll(wrongIntercalPath);
					jpb.setValue(100 * (i + 1) / result.size());
					// 用于监听器取值
					// setProgress(100 * (i+1) / result.size());
					label.setText((i + 1) + " / " + result.size());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return result;
		}

		/**
		 * 将所有错误列表中的检测结果拼成字符串，用于页面展示.
		 */
		@Override
		protected void done() {
			// 所有检查结果都已存入各自的list，开始将最终结果转换为字符串
			if (badURLList.size() > 0) {
				resultStr = resultStr + "\r\n以下为失效链接：" + "\r\n";
				for (MyURL myURL : badURLList) {
					resultStr = resultStr + "文件路径：" + myURL.getFile() + "        链接地址："
							+ myURL.getUrl() + "\r\n";
				}
			}

			if (wrongIncludePathList.size() > 0) {
				resultStr = resultStr + "\r\n以下为错误include标签引用路径：" + "\r\n";
				for (MyURL myURL : wrongIncludePathList) {
					String url = myURL.getUrl();
					try {
						url = new String(url.getBytes(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					resultStr = resultStr + "文件路径：" + myURL.getFile() + "        引用路径："
							+ url + "\r\n";
				}
			}

			if (wrongInternalPathList.size() > 0) {
				resultStr = resultStr + "\r\n以下为错误内部引用路径：" + "\r\n";
				for (MyURL myURL : wrongInternalPathList) {
					String url = myURL.getUrl();
					try {
						url = new String(url.getBytes(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					resultStr = resultStr + "文件路径：" + myURL.getFile() + "        引用路径："
							+ url + "\r\n";
				}
			}

			if (wrongCharsetFile.size() > 0) {
				resultStr = resultStr + "\r\n以下为字符编码不正确的文件：" + "\r\n";
				for (MyURL myURL : wrongCharsetFile) {
					String url = myURL.getUrl();
					try {
						url = new String(url.getBytes(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					resultStr = resultStr + "文件路径：" + myURL.getFile() + "        编码格式："
							+ url + "\r\n";
				}
			}

			if (wrongTitleFile.size() > 0) {
				resultStr = resultStr + "\r\n以下为title部分格式不正确的文件：" + "\r\n";
				for (MyURL myURL : wrongTitleFile) {
					resultStr = resultStr + "文件路径：" + myURL.getFile() + "\r\n";
				}
			}
			// 将结果保存到日志文件中
			try {
				PrintStream mytxt = new PrintStream("./MDFilesCheckLog.log");
				PrintStream out = System.out;
				System.setOut(mytxt);
				System.out.println(resultStr);
				System.setOut(out);
				System.out.println("日志保存完毕。");
				mytxt.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// 弹出窗口提示检查完成，并提示日志位置
			JOptionPane.showMessageDialog(null, "检查已完成。\n结果保存在当前目录下的日志文件MDFilesCheckLog.log中。");
			// 在文本框中显示结果
//			resultStr += "日志文件MDFilesCheckLog.log已生成，保存在当前目录下";
//			textArea.setText(resultStr);
			checkButton.setEnabled(true);
		}
	}
	/**
	 * 从状态复选框中获得目前的复选框勾选状态.
	 */
	private void getCheckType() {
	    if (!chckbxSelectParent.isSelected() 
                && chckbxSelectChildren.isSelected()) {
	        // 如果只勾选规则“选择子节点”
	        checkType = R.SUBNODES;
	    } else if (chckbxSelectParent.isSelected() 
                && !chckbxSelectChildren.isSelected()) {
	        // 如果只勾选规则“选择父节点”
	        checkType = R.PARENT;
	    } else if (!chckbxSelectParent.isSelected() 
                && !chckbxSelectChildren.isSelected()) {
	        // 如果规则“选择父节点”和“选择子节点”都没勾选
	        checkType = "";
	    } else if (chckbxSelectParent.isSelected() 
                && chckbxSelectChildren.isSelected()) {
	        // 如果规则“选择父节点”和“选择子节点”都被勾选
	        checkType = R.SUBNODES_AND_PARENT;
	    }
	}
	/**
	 * 树形图鼠标监听器
	 * @author SunYichuan
	 */
	class CheckBoxTreeNodeSelectionListener extends MouseAdapter {
	    List<NodeObj> list;

	    /**
	     * 鼠标点击监听器
	     */
	    @Override
	    public void mouseClicked(MouseEvent event) {
	        getCheckType();
	        System.out.println("type="+checkType);
	        JTree tree = (JTree) event.getSource();
	        int x = event.getX();
	        int y = event.getY();
	        int row = tree.getRowForLocation(x, y);
	        TreePath path = tree.getPathForRow(row);
	        // 点击后改变复选框状态
	        if (path != null) {
	            CheckBoxTreeNode node = (CheckBoxTreeNode) path.getLastPathComponent();
	            if (node != null) {
//	                String type =R.SUBNODES;
	                node.setSelected(!node.isSelected(),checkType);
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

	    private void show(List<NodeObj> l) {
	        System.out.println("--------------list-----------------");
	        for(NodeObj o:l){
	            System.out.print("<"+o.getName()+","+o.getPath()+">");
	        }
	        System.out.println();
	    }
	}
	/**
	 * 根据根节点遍历获取所有被选择节点.
	 * @param root 根节点
	 * @param lst 存放所有被选择节点的集合
	 */
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
	class CheckBoxSelectAllListener extends MouseAdapter {
        List<NodeObj> list;

        /**
         * 鼠标点击监听器
         */
        @Override
        public void mouseClicked(MouseEvent event) {
//            JTree tree = (JTree) event.getSource();
            // 点击后改变复选框状态
            list = new ArrayList<NodeObj>();
            // 获得根节点
            CheckBoxTreeNode root = (CheckBoxTreeNode) tree
                    .getPathForRow(tree.getRowForLocation(0, 0)).getLastPathComponent();
            // 遍历所有节点，并获得已勾选节点
            if (root != null) {
                
                root.setSelected(chckbxSelectAll.isSelected(),R.SUBNODES_AND_PARENT);
                ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(root);
            }
            // 遍历所有节点，并获得已勾选节点
            getSelectedNodes(root, list);
        }
    }
}
