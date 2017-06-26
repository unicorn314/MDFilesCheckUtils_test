package src;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import model.MyURL;
import tools.HttpLinkChecker;
import tools.TitleChecker;
import tools.WrongPathChecker;

/**
 * 
 * 
 * @author ywx474563 2017年6月26日
 */
public class CheckPane extends JPanel {

  /**
   * 选择的文件夹路径.
   */
  private String rootFolderPath;
  // 开始检测按钮
  final JButton checkButton = new JButton("start check");

  final int tWidth = 0;
  final int tHeight = 0;
  final int fWidth = 920;
  final int fHeight = 570;
  final int fontSize = 20;

  JScrollPane scrollPane = new JScrollPane();
  final JLabel label = new JLabel();
  final JProgressBar jpb = new JProgressBar();
  JTextArea textArea = new JTextArea();
  JPanel centerPanel = new JPanel();

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
  private StringBuilder resultStr = new StringBuilder();

  public void setRootFolderPath(String rootFolderPath) {
    this.rootFolderPath = rootFolderPath;
  }

  /**
   * 窗口组件初始化
   */
  public CheckPane() {
    super();

    setSize(fWidth, fHeight);
    setLayout(new BorderLayout());
    textArea.setLineWrap(true);        //激活自动换行功能 
    textArea.setWrapStyleWord(true);            // 激活断行不断字功能
    scrollPane.setViewportView(textArea);
    scrollPane.validate();
    add(scrollPane, BorderLayout.CENTER);

    // "start check"按钮添加点击开始扫描文件夹事件
    checkButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (rootFolderPath != null && !"".equals(rootFolderPath)) {
          // 清空所有错误列表和结果显示界面
          cleanAllLists();
          // 如果已经选择过文件夹，开始检测
          check(rootFolderPath);
          checkButton.setEnabled(false);
        } else {
          // 如果还没有选择过文件夹，弹出提示窗口
          JOptionPane.showMessageDialog(null, "请选择项目根路径!");
        }
      }
    });

    // 主窗口south部分（底部按钮及进度条）
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    checkButton.setMaximumSize(new Dimension(90, 30));
    southPanel.add(checkButton);
    southPanel.add(jpb);
    southPanel.add(label);
    add(southPanel, BorderLayout.NORTH);
  }

  /**
   * 失效的引用链接.
   * 
   * @return badURLList
   */
  public List<MyURL> getBadURLList() {
    return badURLList;
  }

  /**
   * 错误的include标签内引用路径.
   * 
   * @return wrongIncludePathList
   */
  public List<MyURL> getWrongIncludePathList() {
    return wrongIncludePathList;
  }

  /**
   * 错误的内部引用路径.
   * 
   * @return wrongInternalPathList
   */
  public List<MyURL> getWrongInternalPathList() {
    return wrongInternalPathList;
  }

  /**
   * 字符编码不为UTF-8格式的文件.
   * 
   * @return wrongCharsetFile
   */
  public List<MyURL> getWrongCharsetFile() {
    return wrongCharsetFile;
  }

  /**
   * title部分格式不正确的文件.
   * 
   * @return wrongTitleFile
   */
  public List<MyURL> getWrongTitleFile() {
    return wrongTitleFile;
  }

  /**
   * 结果字符串.
   * 
   * @return resultStr
   */
  public String getResultStr() {
    return resultStr.toString();
  }

  /**
   * 判断include标签中的引用路径是否有效.
   * 
   * @param rootPath
   *          项目根路径
   * @param strPath
   *          引用路径
   * @return 返回true或者false
   */
  public boolean isValidPath(final String rootPath, final String strPath) {
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
   * 
   * @param file
   *          输入需要检索的文件
   */
  public void searchBadURL(final File file) {
    HttpLinkChecker checker = new HttpLinkChecker();
    badURLList.addAll(checker.searchBadURL(file));
  }

  /**
   * 在文件中搜索include标签中错误的引用文件路径.
   * 
   * @param file
   *          需要检测的文件
   * @param rootPath
   *          项目根路径
   */
  public void searchWrongIncludePath(final File file, final String rootPath) {
    // include正则判断
    String rex = "\\{%[\\s]+include[\\s]+[/]?" + "[a-zA-z0-9\\/\\.]*[\\s]+%\\}";
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
          String includePath = matcher.group(0).replaceAll("\\{%[\\s]+include[\\s]+", "")
              .replaceAll("[\\s]+%\\}", "");

          // 检测路径是否可用
          if (!isValidPath(rootPath, includePath.trim())) {
            MyURL myURL = new MyURL();
            myURL.setFile(file.getParent() + "\\" + file.getName());
            myURL.setUrl(includePath);
            // 若路径不可用，将这个网址所属的文件名和网址字符串插入到错误路径list
            wrongIncludePathList.add(myURL);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      // 关闭流
      close(lineReader);
    }
  }

  /**
   * 检查title部分内容是否符合格式.
   * 
   * @param file
   *          需要检测的文件
   */
  public void searchWrongTitle(final File file) {
    TitleChecker checker = new TitleChecker();
    if (checker.searchWrongTitle(file) != null) {
      wrongTitleFile.addAll(checker.searchWrongTitle(file));
    }
  }

  /**
   * 检测文件编码是否为UTF-8.
   * 
   * @param file
   *          输入文件
   * @throws FileNotFoundException
   *           文件不存在
   * @throws IOException
   *           IO错误
   */
  public void checkCharset(final File file) throws FileNotFoundException, IOException {
    String charset = new FileCharsetDetector().guessFileEncoding(file);
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
   * 
   * @param able
   *          需要关闭的流
   */
  private void close(final Closeable able) {
    if (able != null) {
      try {
        able.close();
      } catch (IOException e) {
        e.printStackTrace();
        // able = null;
      }
    }
  }

  private void writeMsg(String msg) {
    resultStr.append(msg);
    textArea.append(msg);
  }

  /**
   * 启动线程，开始检测文件.
   * 
   * @param rootPath
   *          项目根路径
   */
  public void check(final String rootPath) {
    File folder = new File(rootPath); // 指定项目根目录
    if (!folder.exists()) { // 如果文件夹不存在
      writeMsg("目录不存在：" + folder.getAbsolutePath() + "\r\n");
      return;
    }
    List<File> result = FileUtils.searchFile(folder); // 调用方法获得文件数组
    writeMsg("在 " + folder + " 以及所有子文件时查找对象.md文件及.yml文件" + "\r\n");
    ProGressWork work = new ProGressWork(result, rootPath);
    work.execute();
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
    resultStr = new StringBuilder();
    textArea.setText("");
  }

  /**
   * 线程类. 用来在检查文件的同时动态生成进度条.
   * 
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
       * 检查项目包含： 1.所有.md文件及.yml文件中的网页链接是否有效 2.所有include标签中的引用文件路径是否有效
       * 3.所有文件的编码格式是否为UTF-8编码 4.文件头title的区域标识线是否为3个减号，有没有多写或者少写
       * 5.文件头title的内容部分，键值对之间的冒号后是否有加上空格 6.所有引入的内部路径是否正确
       */
      WrongPathChecker wrongPathChecker = new WrongPathChecker();
      for (int i = 0; i < result.size(); i++) { // 循环显示文件
        File file = result.get(i);
        try {
          searchBadURL(file);
          searchWrongIncludePath(file, rootPath);
          checkCharset(file);
          searchWrongTitle(file);
          List<MyURL> wrongIntercalPath = wrongPathChecker.searchWrongIntercalPath(file);
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
        writeMsg("\r\n以下为失效链接：" + "\r\n");
        for (MyURL myURL : badURLList) {
          writeMsg("文件路径：" + myURL.getFile() + "        链接地址：" + myURL.getUrl() + "\r\n");
        }
      }

      if (wrongIncludePathList.size() > 0) {
        writeMsg("\r\n以下为错误include标签引用路径：" + "\r\n");
        for (MyURL myURL : wrongIncludePathList) {
          String url = myURL.getUrl();
          try {
            url = new String(url.getBytes(), "utf-8");
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
          writeMsg("文件路径：" + myURL.getFile() + "        引用路径：" + url + "\r\n");
        }
      }

      if (wrongInternalPathList.size() > 0) {
        writeMsg("\r\n以下为错误内部引用路径：" + "\r\n");
        for (MyURL myURL : wrongInternalPathList) {
          String url = myURL.getUrl();
          try {
            url = new String(url.getBytes(), "utf-8");
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
          writeMsg("文件路径：" + myURL.getFile() + "        引用路径：" + url + "\r\n");
        }
      }

      if (wrongCharsetFile.size() > 0) {
        writeMsg("\r\n以下为字符编码不正确的文件：" + "\r\n");
        for (MyURL myURL : wrongCharsetFile) {
          String url = myURL.getUrl();
          try {
            url = new String(url.getBytes(), "utf-8");
          } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
          }
          writeMsg("文件路径：" + myURL.getFile() + "        编码格式：" + url + "\r\n");
        }
      }

      if (wrongTitleFile.size() > 0) {
        writeMsg("\r\n以下为title部分格式不正确的文件：" + "\r\n");
        for (MyURL myURL : wrongTitleFile) {
          writeMsg("文件路径：" + myURL.getFile() + "\r\n");
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
//      writeMsg("日志文件MDFilesCheckLog.log已生成，保存在当前目录下");
      checkButton.setEnabled(true);
    }
  }

}
