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

public class UpdatePe4jJpaMainPersistenceXML extends Task {

    File toFile;
    File fromFile;

    static final Log log = LogFactory.getLog(UpdatePe4jComponentXML.class);

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

    private List<Node> getNewMappingFiles(Document frmDoc , Document toDoc , XPath xpath) throws Exception{
        List<Node> result = new ArrayList<Node>();
        Element frmRoot = frmDoc.getDocumentElement();

        NodeList list = (NodeList)xpath.evaluate("//node()[local-name()='mapping-file']", frmRoot,XPathConstants.NODESET);

        if(list == null || list.getLength() ==0){
            return result;
        }

        int size = list.getLength();
        Element toRootElement = toDoc.getDocumentElement();
        String nodeValue = "";
        Node tmp = null;

        for (int i = 0; i < size; i++) {
            Node node = list.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){

                nodeValue = xpath.evaluate("./text()",node);
                tmp = ((Node)xpath.evaluate("//node()[local-name()='mapping-file'][text()=\""+ nodeValue+"\"]" , toRootElement,XPathConstants.NODE));
                if(tmp == null){
                    result.add(toDoc.importNode(node, true));
                }
            }
        }

        return result;
    }

    private List<Node> getNewJarFiles(Document frmDoc , Document toDoc , XPath xpath) throws Exception{
        List<Node> result = new ArrayList<Node>();
        Element frmRoot = frmDoc.getDocumentElement();

        NodeList list = (NodeList)xpath.evaluate("//node()[local-name()='jar-file']", frmRoot,XPathConstants.NODESET);

        if(list == null || list.getLength() ==0){
            return result;
        }

        int size = list.getLength();
        Element toRootElement = toDoc.getDocumentElement();
        String nodeValue = "";
        Node tmp = null;

        for (int i = 0; i < size; i++) {
            Node node = list.item(i);

            if(node.getNodeType() == Node.ELEMENT_NODE){
                nodeValue = xpath.evaluate("./text()",node);
                tmp = ((Node)xpath.evaluate("//node()[local-name()='jar-file'][text()=\""+ nodeValue+"\"]" , toRootElement,XPathConstants.NODE));
                if(tmp == null){
                    result.add(toDoc.importNode(node, true));
                }
            }
        }

        return result;
    }

    void updateFile() throws BuildException {

        try {
            Document frmDocument = Util.getDocument(fromFile);
            Document toDocument = Util.getDocument(toFile);
            XPath xpath = XPathFactory.newInstance().newXPath();


            List<Node> newMappingFiles = getNewMappingFiles(frmDocument, toDocument, xpath);
            List<Node> newJarFiles = getNewJarFiles(frmDocument, toDocument, xpath);

            //newMappingFiles.addAll(newJarFiles);
            if(newMappingFiles.size() ==0 && newJarFiles.size() ==0){
                return;
            }

            //Get persistence-unit node
            Node persistenceUnitNode =(Node)xpath.evaluate("//node()[local-name()='persistence-unit' and last()]" ,
                        toDocument.getDocumentElement(), XPathConstants.NODE);

//            Node lastMappingFile = (Node)xpath.evaluate("mapping-file[last()]",persistenceUnitNode,XPathConstants.NODE);
//            Node mappingFileInsertPoint = lastMappingFile.getNextSibling();
//
//            Node startCommentNode =toDocument.createComment("PEのアドオン MAPPING FILES  START" );
//            persistenceUnitNode.insertBefore(startCommentNode, mappingFileInsertPoint) ;
//
//            for (Node node : newMappingFiles) {
//                persistenceUnitNode.insertBefore(node, mappingFileInsertPoint);
//            }
//
//            Node endComment = toDocument.createComment("PEのアドオン MAPPING FILES  END");
//            persistenceUnitNode.insertBefore(endComment, mappingFileInsertPoint);

            addMappingFiles(xpath, persistenceUnitNode, toDocument, newMappingFiles);
            addJarFiles(xpath, persistenceUnitNode, toDocument, newJarFiles);
            Util.writeDocument(toFile, toDocument);
        } catch (Exception e) {
            throw new BuildException(e.getMessage());
        }
    }

    private void addJarFiles(XPath xpath ,  Node persistenceUnitNode, Document toDocument, List<Node> newJarFiles) throws Exception{
        Node lastJarFile = (Node)xpath.evaluate("jar-file[last()]",persistenceUnitNode,XPathConstants.NODE);
        Node jarInsertPoint = lastJarFile.getNextSibling();

        Node startCommentNode =toDocument.createComment("PEのアドオン JAR FILES  START" );
        persistenceUnitNode.insertBefore(startCommentNode, jarInsertPoint) ;

        for (Node node : newJarFiles) {
            persistenceUnitNode.insertBefore(node, jarInsertPoint);
        }

        Node endComment = toDocument.createComment("PEのアドオン JAR FILES  END");
        persistenceUnitNode.insertBefore(endComment, jarInsertPoint);
    }


    private void addMappingFiles(XPath xpath ,  Node persistenceUnitNode, Document toDocument, List<Node> newMappingFiles) throws Exception{
        Node lastMappingFile = (Node)xpath.evaluate("mapping-file[last()]",persistenceUnitNode,XPathConstants.NODE);
        Node mappingFileInsertPoint = lastMappingFile.getNextSibling();

        Node startCommentNode =toDocument.createComment("PEのアドオン MAPPING FILES  START" );
        persistenceUnitNode.insertBefore(startCommentNode, mappingFileInsertPoint) ;

        for (Node node : newMappingFiles) {
            persistenceUnitNode.insertBefore(node, mappingFileInsertPoint);
        }

        Node endComment = toDocument.createComment("PEのアドオン MAPPING FILES  END");
        persistenceUnitNode.insertBefore(endComment, mappingFileInsertPoint);
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

}
