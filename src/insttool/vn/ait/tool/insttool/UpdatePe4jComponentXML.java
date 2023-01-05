package vn.ait.tool.insttool;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
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

public class UpdatePe4jComponentXML  extends Task  {

    File toFile;
    File fromFile;

    boolean includePageFlow = true;

    static final Log log = LogFactory.getLog(UpdatePe4jComponentXML.class);
    static final String PWS99_PAGE_FLOW = "PWS99PageFlow.jpdl.xml";
    static final String PWSA1P01_PAGE_FLOW = "PWSA1P01PageFlow.jpdl.xml";
    static final String PWSA2P01_PAGE_FLOW = "PWSA2P01PageFlow.jpdl.xml";
    
    static Node pageFlowRoot = null;
    static  Node coreBundleNode = null;
    static Node pws99PageFlowNode = null;

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

    private NamespaceContext getNamespaceContext(){
        // We map the prefixes to URIs

        NamespaceContext ctx = new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                String uri;
                if (prefix.equals("ns1")){
                    uri = "http://jboss.com/products/seam/core";
                }else if (prefix.equals("ns2")){
                    uri = "http://jboss.com/products/seam/bpm";
                }else if("security".equalsIgnoreCase(prefix )){
                    uri = "http://jboss.com/products/seam/components";
                }else if("security".equalsIgnoreCase(prefix)){
                    uri ="http://jboss.com/products/seam/security";
                }else if("web".equalsIgnoreCase(prefix)){
                    uri ="http://jboss.com/products/seam/web";
                }else{
                    uri = "http://jboss.com/products/seam/components";
                }
                return uri;
            }

            // Dummy implementation - not used!
            public Iterator getPrefixes(String val) {
                return null;
            }

            // Dummy implemenation - not used!
            public String getPrefix(String uri) {
                return null;
            }
        };

        return ctx;
    }


    /**
     * ファイルの更新を実行する
     *
     * @throws BuildException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    void updateFile() throws BuildException{
        try {
            Document frmDocument = Util.getDocument(fromFile);
            Document toDocument = Util.getDocument(toFile);
          
            log.debug("From File " + fromFile.getPath());
            log.debug("To File " + toFile.getPath());

            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(getNamespaceContext());

            log.debug(" IsIncludePageFlow =" + isIncludePageFlow());

            List<String> newPropertyFiles = getNewTags(frmDocument, toDocument, xpath);

            if(newPropertyFiles == null || newPropertyFiles.size() ==0){
                log.debug(" Not Exist Data ");
                return;
            }

            boolean hasNewBundle  = (coreBundleNode != null) ? true : false;
            boolean  hasNewPageFlow = (pageFlowRoot != null) ? true : false;

            log.debug(" hasNewPageFlow =" +hasNewPageFlow);
            if(hasNewBundle){
                coreBundleNode.appendChild(toDocument.createComment("PEのアドオン START RESOURCE"));
                coreBundleNode.appendChild(toDocument.createTextNode("\n"));
            }

            if(hasNewPageFlow){
                pageFlowRoot.appendChild(toDocument.createComment("PEのアドオン START PAGEFLOW"));
                pageFlowRoot.appendChild(toDocument.createComment("\n"));
            }

            Node node = null;
            Node textNode = null;
            for (String value : newPropertyFiles) {
                node = toDocument.createElement("value");
                textNode = toDocument.createTextNode(value);
                node.appendChild(textNode);
                if(value.contains(".jpdl.xml") && hasNewPageFlow){
                	if (null != pws99PageFlowNode 
                			&& (PWSA1P01_PAGE_FLOW.equals(value) || PWSA2P01_PAGE_FLOW.equals(value))) {
                		pageFlowRoot.insertBefore(node, pws99PageFlowNode);
                	} else {
                		pageFlowRoot.appendChild(node);
                	}
                }else if(hasNewBundle){
                    coreBundleNode.appendChild(node);
                }
            }

            if(hasNewBundle){
                coreBundleNode.appendChild(toDocument.createComment("PEのアドオン END RESOURCE"));
            }

            if(hasNewPageFlow){
                pageFlowRoot.appendChild(toDocument.createComment("PEのアドオン PAGEFLOW"));
            }

            Util.writeDocument(toFile, toDocument);

        } catch (Exception e) {
            throw new BuildException(e.getMessage());
        }
   }
    
    private  List<String> convertToListContent(NodeList list, XPath xpath, boolean setRoot) throws Exception{

        List<String> result = new ArrayList<String>();

        if(list == null || list.getLength()==0){
            log.debug(" Not Exist Data setRoot = " +setRoot);
            return result;
        }

        int size = list.getLength();

        String value = "";
        boolean isPageFlowFile = false;

        if (setRoot) {
            for (int i = 0; i < size; i++) {
                value = xpath.evaluate("./text()",list.item(i));

//                if(isIncludePageFlow() && isPageFlowFile  && pageFlowRoot == null){
//                    pageFlowRoot = list.item(i).getParentNode();
//                }else if(coreBundleNode == null){
//                        coreBundleNode = list.item(i).getParentNode();
//                }

                if(!Util.isEmpty(value)){
                    isPageFlowFile = value.contains(".jpdl.xml") ;

                    if(isPageFlowFile && isIncludePageFlow()){
                        if(pageFlowRoot == null){
                            pageFlowRoot = list.item(i).getParentNode();
                        }
                        if (PWS99_PAGE_FLOW.equals(value)) {
                        	pws99PageFlowNode = list.item(i);
                        }
                        result.add(value);
                    }else{
                        if(coreBundleNode == null){
                            coreBundleNode = list.item(i).getParentNode();
                        }
                        result.add(value);
                    }

                }
            }
        }else{
            for (int i = 0; i < size; i++) {
                value = xpath.evaluate("./text()",list.item(i));
                if(!Util.isEmpty(value)){
                    isPageFlowFile = value.contains(".jpdl.xml") ;

                    if (isPageFlowFile && !isIncludePageFlow()) {
                        log.debug("Skip PageFlow File :" + value);
                        continue;
                    }

                    result.add(value);
                }
            }
        }


        return result;
    }

    private  List<String> getNewTags(Document frmDoc , Document toDoc , XPath xpath) throws Exception{

        Element frmRoot = frmDoc.getDocumentElement();
        Element toRoot = toDoc.getDocumentElement();

        NodeList frmList = frmDoc.getElementsByTagName("value");
        NodeList toList = toRoot.getElementsByTagName("value");

        List<String> frmProperties = convertToListContent(frmList, xpath, false);
        List<String> toProperties = convertToListContent(toList, xpath ,true);

        List<String> duplicateValue = new ArrayList<String>();
        for (String value : frmProperties) {

            if(toProperties.contains(value)){
                duplicateValue.add(value);
            }
        }

       frmProperties.removeAll(duplicateValue);

        return frmProperties;
    }

    public boolean isIncludePageFlow() {
        return includePageFlow;
    }

    public void setIncludePageFlow(boolean includePageFlow) {
        this.includePageFlow = includePageFlow;
    }

    public void setIncludePageFlow(String includePageFlow) {
        if(Util.nullOrBlank(includePageFlow)){
            this.includePageFlow = true;
        }
        this.includePageFlow = Boolean.valueOf(includePageFlow);
    }
}
