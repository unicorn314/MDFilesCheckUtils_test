package src;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.mozilla.intl.chardet.nsDetector;
import org.mozilla.intl.chardet.nsICharsetDetectionObserver;

/**
 * 检测文件编码.
 * 
 * @author SunYichuan
 */
public class FileCharsetDetector {
  /**
   * 是否以检查到文件编码.
   */
  private boolean found = false;
  /**
   * 字符编码.
   */
  private String encoding = null;

  /**
   * main.
   * 
   * @param argv
   *          ..
   * @throws Exception
   *           ..
   */
  public static void main(final String[] argv) throws Exception {
    File file1 = new File("E:\\OPEN-O\\ASCII.txt");

    System.out
        .println("文件编码:" + new FileCharsetDetector().guessFileEncoding(file1));
  }

  /**
   * 传入一个文件(File)对象，检查文件编码.
   * 
   * @param file
   *          File对象实例
   * @return 文件编码，若无，则返回null
   * @throws FileNotFoundException
   *           ..
   * @throws IOException
   *           ..
   */
  public String guessFileEncoding(final File file)
      throws FileNotFoundException, IOException {
    return guessFileEncoding(file, new nsDetector());
  }

  /**
   * <pre>
   * 获取文件的编码.
   * &#64;param file
   *            File对象实例
   * &#64;param languageHint
   *            语言提示区域代码 @see #nsPSMDetector ,取值如下：
   *             1 : Japanese
   *             2 : Chinese
   *             3 : Simplified Chinese
   *             4 : Traditional Chinese
   *             5 : Korean
   *             6 : Dont know(default)
   * </pre>
   * 
   * @return 文件编码，eg：UTF-8,GBK,GB2312形式(不确定的时候，返回可能的字符编码序列)；若无，则返回null
   * @throws FileNotFoundException
   *           ..
   * @throws IOException
   *           .
   */
  public String guessFileEncoding(final File file, final int languageHint)
      throws FileNotFoundException, IOException {
    return guessFileEncoding(file, new nsDetector(languageHint));
  }

  /**
   * 获取文件的编码.
   * 
   * @param file
   *          .
   * @param det
   *          .
   * @return .
   * @throws FileNotFoundException
   *           .
   * @throws IOException
   *           .
   */
  private String guessFileEncoding(final File file, final nsDetector det)
      throws FileNotFoundException, IOException {
    // Set an observer...
    // The Notify() will be called when a matching charset is found.
    det.Init(new nsICharsetDetectionObserver() {
      @Override
      public void Notify(final String charset) {
        encoding = charset;
        found = true;
      }
    });

    BufferedInputStream imp = new BufferedInputStream(
        new FileInputStream(file));
    final int str = 1024;
    byte[] buf = new byte[str];
    int len;
    boolean done = false;
    boolean isAscii = false;

    while ((len = imp.read(buf, 0, buf.length)) != -1) {
      // Check if the stream is only ascii.
      isAscii = det.isAscii(buf, len);
      if (isAscii) {
        break;
      }
      // DoIt if non-ascii and not done yet.
      done = det.DoIt(buf, len, false);
      if (done) {
        break;
      }
    }
    imp.close();
    det.DataEnd();

    if (isAscii) {
      encoding = "ASCII";
      found = true;
    }

    if (!found) {
      String[] prob = det.getProbableCharsets();
      // 这里将可能的字符集组合起来返回
      for (int i = 0; i < prob.length; i++) {
        if (i == 0) {
          encoding = prob[i];
        } else {
          encoding += "," + prob[i];
        }
      }

      if (prob.length > 0) {
        // 在没有发现情况下,也可以只取第一个可能的编码,这里返回的是一个可能的序列
        return encoding;
      } else {
        return null;
      }
    }
    return encoding;
  }
}
