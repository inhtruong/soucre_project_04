package vn.ait.tool.insttool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdatePe4jPagesXML extends Task {

    File toFile;
    File fromFile;

    static final Log log = LogFactory.getLog(UpdatePe4jPagesXML.class);


    @Override
    public void execute() {
        try{
            validateAttributes();
            updateFile();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    void updateFile() throws BuildException {

        try {
            Document frmDocument = Util.getDocument(fromFile);
            Document toDocument = Util.getDocument(toFile);
            XPath xpath = XPathFactory.newInstance().newXPath();
            //Get all page nodes
            NodeList pageNodes =(NodeList)xpath.evaluate("./child::node()[name()='page']",
                        frmDocument.getDocumentElement() ,XPathConstants.NODESET);

            if(pageNodes == null){
                return;
            }

            int size = pageNodes.getLength();

            String viewId = "";
            Element toDocRoot =toDocument.getDocumentElement();
            List<Node> newPages = new ArrayList<Node>();
            for (int i = 0; i < size; i++) {
                Node page = pageNodes.item(i);
                viewId = xpath.evaluate("./@view-id", page);
                Node existPage = (Node)xpath.evaluate("./child::node()[name()='page'][@view-id=\""+viewId+"\"]",
                        toDocRoot, XPathConstants.NODE);

                if(existPage == null){
                    newPages.add(toDocument.importNode(page, true) );
                }
            }

            //write to toFile
            if(newPages.size()>0){

                //Find the last page node
                Node lastPageNode =(Node)xpath.evaluate("./child::node()[name()='page'][last()]", toDocRoot, XPathConstants.NODE);

                Node startComment = toDocument.createComment("PAGES OF PEのアドオン (START) ");
                toDocRoot.insertBefore(startComment, lastPageNode.getNextSibling());
                Node endComment = toDocument.createComment("PAGES OF PEのアドオン (END)");
                toDocRoot.insertBefore(endComment, startComment.getNextSibling());
                for (Node node : newPages) {
                    toDocRoot.insertBefore(node, endComment);
                }
               Util.writeDocument(toFile, toDocument);
            }
        } catch (Exception e) {
            throw new BuildException(e.getMessage());
        }

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
        if (fromFile == null ) {
            throw new BuildException("fromFile属性は必須です");
        }
        if (!fromFile.exists()) {
            throw new BuildException("fromFileが存在しません");
        }
        if (fromFile.exists() && fromFile.isDirectory()) {
            throw new BuildException("fromFile属性にディレクトリは指定できません");
        }
    }

    public File getToFile() {
        return toFile;
    }

    public void setToFile(File toFile) {
        this.toFile = toFile;
    }

    public File getFromFile() {
        return fromFile;
    }

    public void setFromFile(File fromFile) {
        this.fromFile = fromFile;
    }




}
