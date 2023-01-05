package vn.ait.tool.insttool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
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
import org.xml.sax.SAXException;

public class UpdatePe4jApplicationXML {

    File fromFile;
    File toFile;

    static final Log log = LogFactory.getLog(UpdatePe4jApplicationXML.class);
    /**
     * タスク実行メソッド
     * @see org.apache.tools.ant.Task#execute()
     */
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
     * ファイルの更新を実行する
     *
     * @throws BuildException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    void updateFile() throws BuildException{

        String value = "";
        Node nodeTmp = null;
        List<Node> newNodes = new ArrayList<Node>();
        int numOfChild= 0;
        Node firstChild = null;
        String nodeName = "";
        String queryString="";

        try {
            Document frmDocument = Util.getDocument(fromFile);
            Document toDocument = Util.getDocument(toFile);

            log.debug("From File " + fromFile.getPath());
            log.debug("To File " + toFile.getPath());

            Element root = frmDocument.getDocumentElement();
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList frmModuleList =  root.getElementsByTagName("module");

            if(frmModuleList == null || frmModuleList.getLength() ==0){
                return;
            }

            int size = frmModuleList.getLength();

            //Filter
            for (int i = 0; i < size ; i++) {
                Node node = frmModuleList.item(i);
                NodeList childs =  node.getChildNodes();
                numOfChild = childs.getLength();

                for (int j = 0; j <numOfChild ; j++) {

                    if(childs.item(j).getNodeType() == Node.ELEMENT_NODE){
                        nodeName = childs.item(j).getNodeName();
                        if("ejb".equalsIgnoreCase(nodeName) ||
                                "web".equalsIgnoreCase(nodeName)){
                            firstChild = childs.item(j);
                            break;
                        }
                    }
                }

                if(firstChild == null){
                    continue;
                }

                nodeName = firstChild.getNodeName();
                if("ejb".equalsIgnoreCase(nodeName)){
                    value = xpath.evaluate("./text()", firstChild);
                    queryString = "//module/ejb[text()=\""+value+"\"]" ;
               }else if ("web".equalsIgnoreCase(nodeName)){
                    value  = xpath.evaluate("./web-uri/text()", firstChild);
                    queryString = "//module/web/web-uri[text()=\""+value+"\"]" ;
                }

                nodeTmp = (Node)xpath.evaluate(queryString , toDocument.getDocumentElement(), XPathConstants.NODE);
                if(nodeTmp == null){
                    //new node
                    newNodes.add(node);
                }
            }

            if(newNodes.size() ==0){
                return;
            }

            Element toRoot = toDocument.getDocumentElement();
            toRoot.appendChild(toDocument.createComment("PEのアドオン START"));
            toRoot.appendChild(toDocument.createTextNode("\n"));

            for (Node node : newNodes) {
                toRoot.appendChild(toDocument.importNode(node, true));
            }
            toRoot.appendChild(toDocument.createComment("PEのアドオン END"));

           Util. writeDocument(toFile,toDocument);

        } catch (Exception e) {
            throw new BuildException(e.getMessage());
        }
   }

    /**
     * タスク属性のチェックを行う
     * @throws BuildException
     */
    void validateAttributes() throws BuildException {
        if (fromFile == null ) {
            throw new BuildException("fromFile属性は必須です");
        }
        if (!fromFile.exists()) {
            throw new BuildException("fromFileが存在しません");
        }
        if (fromFile.exists() && fromFile.isDirectory()) {
            throw new BuildException("fromFile属性にディレクトリは指定できません");
        }
        if (toFile == null ) {
            throw new BuildException("toFile属性は必須です");
        }
        if (!toFile.exists()) {
            throw new BuildException("toFileが存在しません");
        }
        if (toFile.exists() && toFile.isDirectory()) {
            throw new BuildException("toFile属性にディレクトリは指定できません");
        }
    }

    public File getFromFile() {
        return fromFile;
    }

    public void setFromFile(File fromFile) {
        this.fromFile = fromFile;
    }

    public File getToFile() {
        return toFile;
    }

    public void setToFile(File toFile) {
        this.toFile = toFile;
    }

}
