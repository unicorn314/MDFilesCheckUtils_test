package tools;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;

import model.CmdResult;

/**
 * 执行cmd命令.
 * @author Administrator
 *
 */
public class CmdProcesser {
  public static void main(String[] args) throws Exception {
    CmdResult res = execCmd("cmd /c jekyll serve", new File("D:\\git\\pnpdjie.github.io"));
    System.out.println(res.getMsg());
    

//    String  result = execCmd("cmd /c reg query HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\wkhtmltopdf /v DisplayName", new File("D:\\git\\pnpdjie.github.io"));
//    System.out.println(result);
  }
  
  public static CmdResult startJekyll(String jekyllPath) throws Exception{
    return execCmd("cmd /c jekyll serve", new File(jekyllPath));
  }

  /**
   * 执行系统命令, 返回执行结果
   *
   * @param cmd
   *          需要执行的命令
   * @param dir
   *          执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
   */
  public static CmdResult execCmd(String cmd, File dir) throws Exception {
    StringBuilder result = new StringBuilder();
    boolean isSuccess = true;
    Process process = null;
    BufferedReader bufrIn = null;
    BufferedReader bufrError = null;
    try {
      // 执行命令, 返回一个子进程对象（命令在子进程中执行）
      process = Runtime.getRuntime().exec(cmd, null, dir);
      // 方法阻塞, 等待命令执行完成（成功会返回0）
      process.waitFor();
      // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
      bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
      bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
      // 读取输出
      String line = null;
      while ((line = bufrIn.readLine()) != null) {
        result.append(line).append('\n');
        System.out.println(line);
      }
      while ((line = bufrError.readLine()) != null) {
        isSuccess = false;
        result.append(line).append('\n');
        System.out.println(line);
      }
    } finally {
      closeStream(bufrIn);
      closeStream(bufrError);
      // 销毁子进程
      if (process != null) {
        process.destroy();
      }
    }
    // 返回执行结果
    return new CmdResult(isSuccess, result.toString()) ;
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