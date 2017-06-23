package model;

import java.util.List;
/**
 * 文件选择树形图的节点.
 * 若为可选择文件节点，则包含有fileTitle中的file和title信息，
 * fileTitleNodes为空，
 * 若为下拉菜单节点，则包含fileTitle中的title信息及fileTitleNodes，
 * fileTitle中的file为空。
 * @author SunYichuan
 */
public class FileTitleNode {
	/**
	 * 文件节点.
	 */
	private FileTitle fileTitle;
	/**
	 * 树形图分支.
	 */
	private List<FileTitleNode> fileTitleNodes;

	public FileTitle getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(FileTitle fileTitle) {
		this.fileTitle = fileTitle;
	}

	public List<FileTitleNode> getFileTitleNodes() {
		return fileTitleNodes;
	}

	public void setFileTitleNodes(List<FileTitleNode> fileTitleNodes) {
		this.fileTitleNodes = fileTitleNodes;
	}
}
