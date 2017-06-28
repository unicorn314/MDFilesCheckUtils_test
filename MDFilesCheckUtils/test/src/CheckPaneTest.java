package src;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class CheckPaneTest {
    CheckPane check = new CheckPane();
    /**
     * .
     */
    @Test
    public void testCheck() {
        check.check("E:/OPEN-O");
        assertThat(check.getResultStr().length(), greaterThan(0));
    }
    /**
     * .
     */
    @Test
    public void testCheckCharset_true() {
        try {
            check.cleanAllLists();
            check.checkCharset(new File("E:/OPEN-O/_include/ASCII.md"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertThat(check.getWrongCharsetFile().size(), greaterThan(0));
    }
    /**
     * .
     */
    @Test
    public void testCheckCharset_false() {
        try {
            check.cleanAllLists();
            check.checkCharset(new File("E:/OPEN-O/show-pages.md"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertThat(check.getWrongCharsetFile().size(), comparesEqualTo(0));
    }
    /**
     * .
     */
    @Test
    public void testIsValidPath_ture() {
        String rootPath = "E:/OPEN-O";
        String strPath = "JUnitTest.md";
        assertThat(check.isValidPath(rootPath, strPath),
                is(true));
    }
    /**
     * .
     */
    @Test
    public void testIsValidPath_false() {
        String rootPath = "E:/OPEN-O";
        String strPath = "templates/home.md";
        assertThat(check.isValidPath(rootPath, strPath),
                is(false));
    }
    /**
     * .
     */
    @Test
    public void testSearchBadURL_true() {
        check.searchBadURL(new File("E:/OPEN-O/_include/JUnitTest.md"));
        if (check.getBadURLList().size() == 1) {
            assertThat(check.getBadURLList().get(0)
                    .getUrl(), is("http://www.baidu.edu"));
        }
    }
    /**
     * .
     */
    @Test
    public void testSearchBadURL_false() {
        check.searchBadURL(new File("E:/OPEN-O/_include/home.md"));
        if (check.getBadURLList().size() == 1) {
            assertThat(check.getBadURLList().size(), comparesEqualTo(0));
        }
    }
    /**
     * .
     */
    @Test
    public void testSearchWrongPath_true() {
        check.searchWrongIncludePath(
                new File("E:/OPEN-O/_include/JUnitTest.md"),
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
    public void testSearchWrongPath_false() {
        check.searchWrongIncludePath(
                new File("E:/OPEN-O/_include/home.md"),
                "E:/OPEN-O/");
        if (check.getWrongIncludePathList().size() == 1) {
            assertThat(check.getWrongIncludePathList().size()
                    , comparesEqualTo(0));
        }
    }
    /**
     * .
     */
    @Test
    public void testSerchWrongTitle_true() {
        check.searchWrongTitle(new File("E:\\OPEN-O\\_include\\JUnitTest.md"));
        if (check.getWrongTitleFile().size() == 1) {
            assertThat(check.getWrongTitleFile().get(0)
                    .getFile(),
                    is("E:\\OPEN-O\\_include\\JUnitTest.md"));
        }
    }
    /**
     * .
     */
    @Test
    public void testSerchWrongTitle_false() {
        check.searchWrongTitle(new File("E:\\OPEN-O\\_include\\home.md"));
        if (check.getWrongTitleFile().size() == 1) {
            assertThat(check.getWrongTitleFile().size()
                    , comparesEqualTo(0));
        }
    }
    /**
     * .
     */
    @Test
    public void testSearchWrongIncludePath_true() {
        check.searchWrongIncludePath(new File("E:\\OPEN-O\\_include\\JUnitTest.md")
        , "E:\\OPEN-O");
        assertThat(check.getWrongIncludePathList().size(), greaterThan(0));
    }
    /**
     * .
     */
    @Test
    public void testSearchWrongIncludePath_false() {
        check.searchWrongIncludePath(new File("E:\\OPEN-O\\_include\\home.md")
        , "E:\\OPEN-O");
        assertThat(check.getWrongIncludePathList().size(), comparesEqualTo(0));
    }
}
