package src;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

/**
 * .
 * @author SunYichuan
 */
public class CheckFilesTest {
	CheckFiles check = new CheckFiles();
    /**
     * .
     */
    /*@Test
    public void testSearchFile() {
        File file = new File("E:/OPEN-O");
        List<File> files = check.searchFile(file);
        assertThat(files, isA(ArrayList<File>.class));
    }*/

    /**
     * .
     */
    @Test
    public void testIsValidPath() {
        String rootPath = "E:/OPEN-O";
        String strPath = "templates/home.md";
        assertThat(check.isValidPath(rootPath, strPath),
                is(false));
    }

    /**
     * .
     */
    @Test
    public void testSearchBadURL() {
//        CheckFiles cf = new CheckFiles();
    	check.searchBadURL(new File("E:/OPEN-O/新建文件夹/JUnitTest.md"));
        if (check.getBadURLList().size() == 1) {
            assertThat(check.getBadURLList().get(0)
                    .getUrl(), is("http://www.baidu.edu"));
        }
    }

    /**
     * .
     */
    @Test
    public void testSearchWrongPath() {
//        CheckFiles cf = new CheckFiles();
    	check.searchWrongIncludePath(
                new File("E:/OPEN-O/新建文件夹/JUnitTest.md"),
                "E:/OPEN-O/");
        if (check.getWrongIncludePathList().size() == 1) {
            assertThat(check.getWrongIncludePathList()
                    .get(0).getUrl(),
                    is("templates/home.md"));
        }
    }

    /**
     * .
     */
    @Test
    public void testSerchWrongTitle() {
//        CheckFiles cf = new CheckFiles();
    	check.searchWrongTitle(new File("E:\\OPEN-O\\新建文件夹\\JUnitTest.md"));
        if (check.getWrongTitleFile().size() == 1) {
            assertThat(check.getWrongTitleFile().get(0)
                    .getFile(),
                    is("E:\\OPEN-O\\新建文件夹\\JUnitTest.md"));
        }
    }

    /**
     * .
     */
//    CheckFiles cf = new CheckFiles();
    @Test
    public void testCheckCharset() {
        try {
        	check.checkCharset(new File("E:/OPEN-O/新建文件夹/ASCII.md"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (check.getWrongCharsetFile().size() == 1) {
            assertThat(check.getWrongCharsetFile()
                    .get(0).getFile(),
                    is("E:\\OPEN-O\\新建文件夹\\ASCII.md"));
        }
    }

    /**
     * .
     */
    @Test
    public void testCheck() {
//        CheckFiles cf = new CheckFiles();
    	check.check("E:/OPEN-O");
        assertThat(check.getResultStr().length(), greaterThan(0));
    }

}
