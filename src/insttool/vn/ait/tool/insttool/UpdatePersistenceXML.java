package vn.ait.tool.insttool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import org.w3c.dom.NodeList;

/**
 * POWER EGG標準のpersistence.xmlにEX経費の定義を追加する.
 *
 * @author matsushita
 *
 */
public class UpdatePersistenceXML extends Task {
    static final Log log = LogFactory.getLog(UpdatePersistenceXML.class);
    /** 変更対象のpersistance.xml */
    File targetFile;
    File fromFile;

    /**
     * タスク実行メソッド
     * @see org.apache.tools.ant.Task#execute()
     */
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
    /**
     * タスク属性のチェックを行う
     * @throws BuildException
     */
    void validateAttributes() throws BuildException {
        if (targetFile == null ) {
            throw new BuildException("targetFile属性は必須です");
        }
        if (!targetFile.exists()) {
            throw new BuildException("targetFileが存在しません");
        }
        if (targetFile.exists() && targetFile.isDirectory()) {
            throw new BuildException("targetFile属性にディレクトリは指定できません");
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
    /**
     * ファイルの更新を実行する
     *
     * @throws BuildException
     */
    void updateFile() throws BuildException {
        Document targetDocument = Util.getDocument(targetFile);
        Document fromDocument = Util.getDocument(fromFile);
        XPath xpath = XPathFactory.newInstance().newXPath();

        addMappingFile(targetDocument, fromDocument, xpath);
        addJarFile(targetDocument, fromDocument, xpath);

        //追加した結果を書き込む
        Util.writeDocument(targetFile, targetDocument);

    }

    private void addMappingFile(Document targetDocument, Document fromDocument,
            XPath xpath) {

        Element targetRoot = targetDocument.getDocumentElement();
        Element fromRoot = fromDocument.getDocumentElement();
        Element persistenceUnitElem = getPersistenceUnitElement(targetRoot);


        NodeList mappingFileNodes = fromRoot.getElementsByTagName("mapping-file");
        List<Node> importNodes = new ArrayList<Node>();
        for (int i=0; i<mappingFileNodes.getLength(); i++) {
            Node node = mappingFileNodes.item(i);
            try {
                //既存ファイルに既に定義がある場合は除去する.
                Node exsistNode = (Node)xpath.evaluate("mapping-file[text()=\"" + (String)node.getFirstChild().getNodeValue() + "\"]",
                        persistenceUnitElem, XPathConstants.NODE);
                if (exsistNode !=null) {
                    persistenceUnitElem.removeChild(exsistNode);
                }
                //追加対象のリストに加える.
                importNodes.add(targetDocument.importNode(node, true));
            } catch (XPathExpressionException e) {
                throw new BuildException(e);
            }
        }
        if (mappingFileNodes.getLength() > 0) {
            try {
                //追加位置を特定(最後のmapping-files要素の後)
                Node lastMappingFile = (Node)xpath.evaluate("mapping-file[last()]", persistenceUnitElem, XPathConstants.NODE);
                Node insertPoint = lastMappingFile.getNextSibling();
                Node inserted = null;
                //コメントノードを作成して追加
                Node startComment = Util.createStartComment(targetDocument);
                persistenceUnitElem.insertBefore(startComment, insertPoint);
                for (Node node : importNodes) {
                    inserted = persistenceUnitElem.insertBefore(node, insertPoint);
                }
                Node endComment = Util.createEndComment(targetDocument);
                inserted = persistenceUnitElem.insertBefore(endComment, insertPoint);

                while (Util.isNextSiblingIsWhiteSpaceOrMyComment(inserted)) {
                    persistenceUnitElem.removeChild(inserted.getNextSibling());
                }

            } catch (XPathExpressionException e) {
                throw new BuildException(e);
            }
        }
    }

    private void addJarFile(Document targetDocument, Document fromDocument,
            XPath xpath) {

        Element targetRoot = targetDocument.getDocumentElement();
        Element fromRoot = fromDocument.getDocumentElement();
        Element persistenceUnitElem = getPersistenceUnitElement(targetRoot);


        NodeList mappingFileNodes = fromRoot.getElementsByTagName("jar-file");
        List<Node> importNodes = new ArrayList<Node>();
        for (int i=0; i<mappingFileNodes.getLength(); i++) {
            Node node = mappingFileNodes.item(i);
            try {
                //既存ファイルに既に定義がある場合は除去する.
                Node exsistNode = (Node)xpath.evaluate("jar-file[text()=\"" + (String)node.getFirstChild().getNodeValue() + "\"]",
                        persistenceUnitElem, XPathConstants.NODE);
                if (exsistNode !=null) {
                    persistenceUnitElem.removeChild(exsistNode);
                }
                //追加対象のリストに加える.
                importNodes.add(targetDocument.importNode(node, true));
            } catch (XPathExpressionException e) {
                throw new BuildException(e);
            }
        }
        if (mappingFileNodes.getLength() > 0) {
            try {
                //追加位置を特定(最後のjar-file要素の後)
                Node lastMappingFile = (Node)xpath.evaluate("jar-file[last()]", persistenceUnitElem, XPathConstants.NODE);
                Node insertPoint = lastMappingFile.getNextSibling();
                Node inserted = null;
                //コメントノードを作成して追加
                Node startComment = Util.createStartComment(targetDocument);
                persistenceUnitElem.insertBefore(startComment, insertPoint);
                for (Node node : importNodes) {
                    inserted = persistenceUnitElem.insertBefore(node, insertPoint);
                }
                Node endComment = Util.createEndComment(targetDocument);
                inserted = persistenceUnitElem.insertBefore(endComment, insertPoint);

                while (Util.isNextSiblingIsWhiteSpaceOrMyComment(inserted)) {
                    persistenceUnitElem.removeChild(inserted.getNextSibling());
                }

            } catch (XPathExpressionException e) {
                throw new BuildException(e);
            }
        }
    }

    Element getPersistenceUnitElement(Element root) {
        NodeList nodeList = root.getElementsByTagName("persistence-unit");
        if (nodeList.getLength() == 0) {
            throw new BuildException("persistence-unit not found.");
        }
        if (nodeList.getLength() > 1) {
            throw new BuildException("persistence-unit element found multi.");
        }
        return (Element)nodeList.item(0);
    }

    /**
     * @return the targetFile
     */
    public File getTargetFile() {
        return targetFile;
    }
    /**
     * @param targetFile the targetFile to set
     */
    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }
    /**
     * @return the fromFile
     */
    public File getFromFile() {
        return fromFile;
    }
    /**
     * @param fromFile the fromFile to set
     */
    public void setFromFile(File fromFile) {
        this.fromFile = fromFile;
    }
}
