package src;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import model.ListYml;
import tools.CheckBoxTreeNodeUtils;
import tools.ListYamlReadUtils;

public class Test {

	
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
//		System.out.println("- /doc/home.md".matches("\\-\\s[\\S]*"));
//		System.out.println("../"+"  path: https://github.com/guodongxiaren/README".replace("  path: ", ""));
    	/*ListYamlReadUtils util = new ListYamlReadUtils();
    	List<ListYml> ymlList = new ArrayList<ListYml>();
    	ymlList.add(util.ymlToObject(new File("E:\\Github\\pnpdjie.github.io\\_data\\guides.yml")));
    	ymlList.add(util.ymlToObject(new File("E:\\Github\\pnpdjie.github.io\\_data\\docs-home.yml")));

    	CheckBoxTreeNodeUtils nodeUtils = new CheckBoxTreeNodeUtils();
    	List<CheckBoxTreeNode> treeNodeList = new ArrayList<CheckBoxTreeNode>();
    	
    	nodeUtils.createTreeNodes(ymlList, treeNodeList);
    	
    	JFrame frame = new JFrame("CheckBoxTreeDemo");
        frame.setBounds(200, 200, 400, 400);
        JTree tree = new JTree();
        tree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
        DefaultTreeModel model = new DefaultTreeModel(treeNodeList.get(0));
        tree.setModel(model);
        tree.setCellRenderer(new CheckBoxTreeCellRenderer());
        JScrollPane scroll = new JScrollPane(tree);
        scroll.setBounds(0, 0, 300, 320);
        //show select info
        JButton btnPdf = new JButton("pdf");
        btnPdf.addMouseListener(new GetSelectedInfo());
        
        frame.getContentPane().add(btnPdf, BorderLayout.SOUTH);
        frame.getContentPane().add(scroll);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);*/
    	
    	/*try {
    		String encoding = "UTF-8";  
            File file = new File("E:\\Github\\pnpdjie.github.io\\_data\\guides.yml");
            System.out.println(new File(file.getParent()).getParent());
            
            Long filelength = file.length();  
            byte[] filecontent = new byte[filelength.intValue()]; 
    		FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close(); 
            String res = new String(filecontent, encoding);
            Yaml yaml = new Yaml();
            Object result = yaml.load(res);
            JSONObject jsonObject = JSONObject.fromObject(result);
            listYml=(ListYml)JSONObject.toBean(jsonObject, ListYml.class);
//			listYml = mapper.readValue(res, ListYml.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	/*
    	System.out.println(listYml.getToc().size());
    	System.out.println(listYml.getBigheader());
    	System.out.println(listYml.getToc().get(0));
    	
    	// 无path属性二级目录的section，先转换为Toc，再getSection
    	JSONObject j1 = JSONObject.fromObject(listYml.getToc().get(2));
    	Section toc0 = (Section)JSONObject.toBean(j1, Section.class);
    	System.out.println("--------");
    	System.out.println(toc0.getSection().get(0) instanceof Section);
    	
    	// 转list
//    	List<String> strlist = (ArrayList)JSONObject.toBean(j1, Toc.class);
//    	System.out.println(strlist.get(0));

    	System.out.println("--------");
    	
    	// 二级目录转换为对象
    	JSONObject j2 = JSONObject.fromObject(listYml.getToc().get(4));
    	Section toc = (Section)JSONObject.toBean(j2, Section.class);
    	System.out.println(toc.getTitle());
//    	System.out.println(toc.getSection().get(0).getPath());
//    	System.out.println(toc.getSection().get(0).getClass());
    	
    	// 三级目录转换为对象
    	JSONObject j3 = JSONObject.fromObject(toc.getSection().get(0));
    	Section section = (Section)JSONObject.toBean(j3, Section.class);
    	System.out.println(section.getPath());
    	 */
	}
}
