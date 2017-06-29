package tools;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import src.Main;

/**
 * cmd命令后台任务.
 * 
 * @author ywx474563 2017年6月28日
 */
public class CmdProcesser {
  
  /**
   * 测试.
   * @param args 参数
   */
  public static void main(final String[] args) { 
  }

  protected List<String> exec(String cmd, File dir)
      throws IOException, InterruptedException {
    List<String> result = new ArrayList<String>();
    Process process = null;
    BufferedReader bufrIn = null;
    BufferedReader bufrError = null;
    try {
      // 执行命令, 返回一个子进程对象（命令在子进程中执行）
      process = Runtime.getRuntime().exec(cmd, null, dir);
      // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
      bufrIn = new BufferedReader(
          new InputStreamReader(process.getInputStream(), "GBK"));
      bufrError = new BufferedReader(
          new InputStreamReader(process.getErrorStream(), "GBK"));
      // 读取输出
      String line = null;
      while ((line = bufrIn.readLine()) != null) {
        result.add(line);
      }
      while ((line = bufrError.readLine()) != null) {
        result.add(line);
      }
      // 方法阻塞, 等待命令执行完成（成功会返回0）
      process.waitFor();
    } finally {
      closeStream(bufrIn);
      closeStream(bufrError);
      // 销毁子进程
      if (process != null) {
        process.destroy();
      }
    }
    // 返回执行结果
    return result;
  }

  private static void closeStream(Closeable stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (Exception e) {
        // nothing
      }
    }
  }

}
