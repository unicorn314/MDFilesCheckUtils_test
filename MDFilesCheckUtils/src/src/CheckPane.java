package src;

import java.awt.BorderLayout;
import java.awt.Desktop;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import model.MyUrl;
import tools.HttpLinkChecker;
import tools.TitleChecker;
import tools.WrongPathChecker;

/**
 * 低检界面.
 * 
 * @author ywx474563 2017年6月26日
 */
public class CheckPane extends JPanel {

  /**
   * 选择的文件夹路径.
   */
  private String rootFolderPath;
  // 开始检测按钮
  final JButton checkButton = new JButton("start checking");
  // 浏览日志按钮
  final JButton logButton = new JButton("browse log");

  final int frameWidth = 920;
  final int frameHeight = 570;
  final int fontSize = 20;

  JScrollPane scrollPane = new JScrollPane();
  final JLabel label = new JLabel();
  final JProgressBar jpb = new JProgressBar();
  JTextArea textArea = new JTextArea();
  JPanel centerPanel = new JPanel();

  /**
   * 存放所有失效的引用链接.
   */
  private List<MyUrl> badUrlList = new ArrayList<MyUrl>();
  /**
   * 存放所有错误的include标签内引用路径.
   */
  private List<MyUrl> wrongIncludePathList = new ArrayList<MyUrl>();
  /**
   * 存放所有错误的内部引用路径.
   */
  private List<MyUrl> wrongInternalPathList = new ArrayList<MyUrl>();
  /**
   * 存放所有字符编码不为UTF-8格式的文件.
   */
  private List<MyUrl> wrongCharsetFile = new ArrayList<MyUrl>();
  /**
   * 存放所有title部分格式不正确的文件.
   */
  private List<MyUrl> wrongTitleFile = new ArrayList<MyUrl>();
  /**
   * 存放最终输出结果的字符串.
   */
  private StringBuilder resultStr = new StringBuilder();

  /**
   * 日志文件路径.
   */
  private String logPath;

  public void setRootFolderPath(String rootFolderPath) {
    this.rootFolderPath = rootFolderPath;
  }

  /**
   * 窗口组件初始化.
   */
  public CheckPane() {
    super();

    setSize(frameWidth, frameHeight);
    setLayout(new BorderLayout());
    textArea.setLineWrap(true); // 激活自动换行功能
    textArea.setWrapStyleWord(true); // 激活断行不断字功能
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

    // 查看日志
    logButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
          if (logPath != null && !"".equals(logPath)) {

            if (!Desktop.isDesktopSupported()) {
              // 测试当前平台是否支持此类
              JOptionPane.showMessageDialog(null,
                  "浏览器设置不支持，请手动打开链接：" + logPath);
              return;
            }
            // 用来打开系统默认浏览器浏览指定的URL
            Desktop desktop = Desktop.getDesktop();
            // 创建URI统一资源标识符
            URI uri = new URI("file:///" + logPath.replace("\\", "/"));
            // 使用默认浏览器打开超链接
            desktop.browse(uri);
          } else {
            JOptionPane.showMessageDialog(null, "请先执行低检!");
          }
        } catch (URISyntaxException | IOException ex) {
          // TODO: handle exception
        }
      }
    });

    // 主窗口south部分（底部按钮及进度条）
    JPanel southPanel = new JPanel(new FlowLayout());
    southPanel.add(new JLabel("进度"));
    southPanel.add(jpb);
    southPanel.add(label);
    add(southPanel, BorderLayout.SOUTH);

    // 主窗口south部分（底部按钮及进度条）
    JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    checkButton.setMaximumSize(new Dimension(90, 30));
    northPanel.add(checkButton);
    logButton.setMaximumSize(new Dimension(90, 30));
    logButton.setEnabled(false);
    northPanel.add(logButton);
    add(northPanel, BorderLayout.NORTH);
  }

  /**
   * 失效的引用链接.
   * 
   * @return badURLList
   */
  public List<MyUrl> getBadUrlList() {
    return badUrlList;
  }

  /**
   * 错误的include标签内引用路径.
   * 
   * @return wrongIncludePathList
   */
  public List<MyUrl> getWrongIncludePathList() {
    return wrongIncludePathList;
  }

  /**
   * 错误的内部引用路径.
   * 
   * @return wrongInternalPathList
   */
  public List<MyUrl> getWrongInternalPathList() {
    return wrongInternalPathList;
  }

  /**
   * 字符编码不为UTF-8格式的文件.
   * 
   * @return wrongCharsetFile
   */
  public List<MyUrl> getWrongCharsetFile() {
    return wrongCharsetFile;
  }

  /**
   * title部分格式不正确的文件.
   * 
   * @return wrongTitleFile
   */
  public List<MyUrl> getWrongTitleFile() {
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
  public void searchBadUrl(final File file) {
    HttpLinkChecker checker = new HttpLinkChecker();
    badUrlList.addAll(checker.searchBadUrl(file));
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
          String includePath = matcher.group(0)
              .replaceAll("\\{%[\\s]+include[\\s]+", "")
              .replaceAll("[\\s]+%\\}", "");

          // 检测路径是否可用
          if (!isValidPath(rootPath, includePath.trim())) {
            MyUrl myUrl = new MyUrl();
            myUrl.setFile(file.getParent() + "\\" + file.getName());
            myUrl.setUrl(includePath);
            // 若路径不可用，将这个网址所属的文件名和网址字符串插入到错误路径list
            wrongIncludePathList.add(myUrl);
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
  public void checkCharset(final File file)
      throws FileNotFoundException, IOException {
    String charset = new FileCharsetDetector().guessFileEncoding(file);
    if (!charset.equals("UTF-8")) {
      MyUrl myUrl = new MyUrl();
      myUrl.setFile(file.getParent() + "\\" + file.getName());
      myUrl.setUrl(charset);
      // 将编码不为UTF-8的文件路径和编码格式记入对应list
      wrongCharsetFile.add(myUrl);
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

  // private void writeMsg(String msg) {
  // resultStr.append(msg);
  // textArea.append(msg);
  // }

  private void htmlFormat(String msg, String tab) {
    resultStr.append("<" + tab + ">" + msg + "</" + tab + ">");
  }

  private void htmlFormat(String msg) {
    resultStr.append(msg);
  }

  private void htmlWrapper(String content) {
    resultStr = new StringBuilder();
    resultStr.append("<!DOCTYPE HTML>");
    resultStr.append("<html>");
    resultStr.append("<head>");
    resultStr.append(
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
    resultStr.append("</head>");
    resultStr.append("<body>");
    resultStr.append(content);
    resultStr.append("</body>");
    resultStr.append("</html>");
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
      String msg = "目录不存在：" + folder.getAbsolutePath();
      htmlFormat(msg, "p");
      textArea.append(msg + "\r\n");
      return;
    }
    List<File> result = FileUtils.searchFile(folder); // 调用方法获得文件数组
    String msg = "在 " + folder + " 以及所有子文件时查找对象.md文件及.yml文件";
    htmlFormat(msg, "p");
    textArea.append(msg + "\r\n");
    ProGressWork work = new ProGressWork(result, rootPath);
    work.execute();
  }

  /**
   * 清空所有list.
   */
  public void cleanAllLists() {
    badUrlList = new ArrayList<MyUrl>();
    wrongIncludePathList = new ArrayList<MyUrl>();
    wrongInternalPathList = new ArrayList<MyUrl>();
    wrongCharsetFile = new ArrayList<MyUrl>();
    wrongTitleFile = new ArrayList<MyUrl>();
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
          searchBadUrl(file);
          searchWrongIncludePath(file, rootPath);
          checkCharset(file);
          searchWrongTitle(file);
          List<MyUrl> wrongIntercalPath = wrongPathChecker
              .searchWrongIntercalPath(file);
          wrongInternalPathList.addAll(wrongIntercalPath);
          jpb.setValue(100 * (i + 1) / result.size());
          // 用于监听器取值
          // setProgress(100 * (i+1) / result.size());
          label.setText(100 * (i + 1) / result.size() + "%");
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
      String msg = "";
      // 所有检查结果都已存入各自的list，开始将最终结果转换为字符串
      msg = "失效链接总数：" + badUrlList.size();
      htmlFormat(msg, "h2");
      textArea.append(msg + "\r\n");
      msg = "处理建议：请检查本地网络是否通畅，链接是否输入正确，若链接被删除，请删除或改用其他网页链接替代";
      htmlFormat(msg, "h4");
      textArea.append(msg + "\r\n");

      for (MyUrl myUrl : badUrlList) {
        msg = "文件路径：" + myUrl.getFile() + " 链接地址：" + myUrl.getUrl();
        htmlFormat(msg, "p");
      }
      htmlFormat("<br />");
      textArea.append("\r\n");

      msg = "错误include标签引用路径总数：" + wrongIncludePathList.size();
      htmlFormat(msg, "h2");
      textArea.append(msg + "\r\n");
      msg = "处理建议：请检查项目相对路径是否输入正确，或者原文件是否被删除";
      htmlFormat(msg, "h4");
      textArea.append(msg + "\r\n");

      for (MyUrl myUrl : wrongIncludePathList) {
        String url = myUrl.getUrl();
        try {
          url = new String(url.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        msg = "文件路径：" + myUrl.getFile() + "        引用路径：" + url;
        htmlFormat(msg, "p");
      }
      htmlFormat("<br />");
      textArea.append("\r\n");

      msg = "错误内部引用路径总数：" + wrongInternalPathList.size();
      htmlFormat(msg, "h2");
      textArea.append(msg + "\r\n");
      msg = "处理建议：请检查项目相对路径是否输入正确，或者原文件是否被删除";
      htmlFormat(msg, "h4");
      textArea.append(msg + "\r\n");

      for (MyUrl myUrl : wrongInternalPathList) {
        String url = myUrl.getUrl();
        try {
          url = new String(url.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        msg = "文件路径：" + myUrl.getFile() + "        引用路径：" + url;
        htmlFormat(msg, "p");
      }
      htmlFormat("<br />");
      textArea.append("\r\n");

      msg = "字符编码不正确的文件总数：" + wrongCharsetFile.size();
      htmlFormat(msg, "h2");
      textArea.append(msg + "\r\n");
      msg = "处理建议：请用编辑工具将文件的字符编码转换为合适的编码格式，建议为UTF-8格式，否则可能出现乱码";
      htmlFormat(msg, "h4");
      textArea.append(msg + "\r\n");

      for (MyUrl myUrl : wrongCharsetFile) {
        String url = myUrl.getUrl();
        try {
          url = new String(url.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
        }
        msg = "文件路径：" + myUrl.getFile() + "        编码格式：" + url;
        htmlFormat(msg, "p");
      }
      htmlFormat("<br />");
      textArea.append("\r\n");

      msg = "title部分格式不正确的文件总数：" + wrongTitleFile.size();
      htmlFormat(msg, "h2");
      textArea.append(msg + "\r\n");
      msg = "处理建议：请修改为正确的格式。格式要求如下：\r\n" + "1.起始和结束的短横线都应为3个，不能多不能少，也不能有空格。\r\n"
          + "2.中间内容部分应为键值对格式，key和value中间用英文格式冒号后紧跟一个英文空格隔开。\r\n" + "正确实例如下：\r\n"
          + "---\r\n" + "title: this is title\r\n" + "---\r\n";
      textArea.append(msg + "\r\n");
      htmlFormat(msg.replace("\r\n", "<br />"), "h4");

      for (MyUrl myUrl : wrongTitleFile) {
        msg = "文件路径：" + myUrl.getFile();
        htmlFormat(msg, "p");
      }
      htmlFormat("<br />");
      textArea.append("\r\n");

      htmlWrapper(resultStr.toString());

      // 将结果保存到日志文件中
      try {
        SimpleDateFormat formatter = new SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss");
        String logRelativePath = "MDFilesCheckLog"
            + formatter.format(new Date()) + ".html";

        File logFile = new File(logRelativePath);
        PrintStream mytxt = new PrintStream(logFile, "UTF-8");
        PrintStream out = System.out;
        System.setOut(mytxt);
        String logContent = new String(resultStr.toString());
        System.out.println(logContent);
        System.setOut(out);
        System.out.println("日志保存完毕。");
        mytxt.close();
        out.close();

        textArea.append("日志保存路径：" + logFile.getAbsolutePath());
        logPath = logFile.getAbsolutePath();
        logButton.setEnabled(true);

      } catch (IOException e) {
        e.printStackTrace();
      }
      // 弹出窗口提示检查完成，并提示日志位置
      // JOptionPane.showMessageDialog(null,
      // "检查已完成。\n结果保存在当前目录下的日志文件MDFilesCheckLog.log中。");
      // 在文本框中显示结果
      // writeMsg("日志文件MDFilesCheckLog.log已生成，保存在当前目录下");
      checkButton.setEnabled(true);
    }
  }

}
