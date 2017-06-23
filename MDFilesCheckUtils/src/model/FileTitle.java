package model;

/**
 * 存储文件及文件title的实体类.
 * @author SunYichuan
 */
public class FileTitle {
	/**
	 * 网络链接地址
	 */
	private String link;
	/**
	 * 本地文件路径.
	 */
	private String path;
	/**
	 * title内容.
	 */
	private String title;
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * title = ""：文件没有title部分；title=null：title格式错误.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * title = ""：文件没有title部分；title=null：title格式错误.
	 * @param title 文件标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
