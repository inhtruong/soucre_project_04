package vn.ait.tool.insttool;

import java.io.File;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class UpdatePe4jWebXMLNavigation extends Task {

	 File fromFolder;
	 File toFile;
	 
	 static final Log log = LogFactory.getLog(UpdatePe4jWebXMLNavigation.class);


    @Override
    public void execute() {
        try{
            validateAttributes();
            updateFile();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    void updateFile() throws BuildException, XPathExpressionException {
    	File[] navigationFiles = fromFolder.listFiles();
    	Document toDocument = Util.getDocument(toFile);
        XPath xpath = XPathFactory.newInstance().newXPath();

        Node navigationNode = ((Node)xpath.evaluate("//context-param/param-name[text()='javax.faces.CONFIG_FILES']",
        		 toDocument.getDocumentElement(),XPathConstants.NODE));

        if(navigationNode == null){
        	return;
        }
        
        Node paramValueNode = findValueNode(navigationNode.getNextSibling());
        
        if(paramValueNode != null){
        	String nodeContent = paramValueNode.getTextContent().trim();
        	if(nodeContent.endsWith(",")){
        		nodeContent = nodeContent.substring(0,nodeContent.length()-1);
        	}
        	StringBuilder navigationContext = new StringBuilder(nodeContent);
        	
        	String format = ",/WEB-INF/navigations/";
        	for (File file : navigationFiles) {
        		if(file.getName().contains(".xml")){
        			navigationContext.append(format+file.getName());	
        		}
    		}
        	paramValueNode.setTextContent(navigationContext.toString());
        	Util.writeDocument(new File(toFile.getPath()), toDocument);
        }
        
    }
    
    private Node findValueNode(Node sibling){
    	while (!(sibling instanceof Element) && sibling != null) {
       	  sibling = sibling.getNextSibling();
    	}
    	return sibling;
    }
    
    /**
     * タスク属性のチェックを行う
     * @throws BuildException
     */
    void validateAttributes() throws BuildException {
        if (toFile == null ) {
            throw new BuildException("toFile属性は必須です");
        }
        if (!toFile.exists()) {
            throw new BuildException("toFileが存在しません");
        }
        if (toFile.exists() && toFile.isDirectory()) {
            throw new BuildException("toFile属性にディレクトリは指定できません");
        }
        if (fromFolder == null ) {
            throw new BuildException("fromFolder属性は必須です");
        }
        if (!fromFolder.exists()) {
            throw new BuildException("fromFolderが存在しません");
        }
        if (!fromFolder.isDirectory()) {
            throw new BuildException("fromFolder属性にディレクトリは指定できません");
        }
    }

    public File getToFile() {
        return toFile;
    }

    public void setToFile(File toFile) {
        this.toFile = toFile;
    }

	public File getFromFolder() {
		return fromFolder;
	}

	public void setFromFolder(File fromFolder) {
		this.fromFolder = fromFolder;
	}

}
