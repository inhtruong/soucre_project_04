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

public class UpdatePe4jWebXML  extends Task {

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
            Document frmDocument =Util.getDocument(fromFile);
            Document toDocument = Util.getDocument(toFile);
            XPath xpath = XPathFactory.newInstance().newXPath();
            List<Node> newContextParamNodes = getNewContextParamNodes(frmDocument, toDocument, xpath);
            List<Node> newFilterNodes =getNewFilterNodes(frmDocument, toDocument, xpath);
            List<Node> newFilterMappingNodes =getNewFilterMappingNodes(frmDocument, toDocument, xpath);
            List<Node> newListenerNodes =getListenerNodes(frmDocument, toDocument, xpath);
            List<Node> newServletNodes =getNewServletNodes(frmDocument, toDocument, xpath);
            List<Node> newServletMappingNodes =getNewServletMappingNodes(frmDocument, toDocument, xpath);
            List<Node> newEjbLocalRefNodes =getNewEjbLocalRefNodes(frmDocument, toDocument, xpath);

            int count = newContextParamNodes.size()+ newFilterNodes.size()+newFilterMappingNodes.size()+
                            newListenerNodes.size()+newServletNodes.size()+
                            newServletMappingNodes.size()+ newEjbLocalRefNodes.size();

            if(count >0){
                Element toRootDoc = toDocument.getDocumentElement();
                toRootDoc.appendChild( toDocument.createComment("PEのアドオン START "));
                toRootDoc.appendChild(toDocument.createTextNode("\n"));

                insertNode(newContextParamNodes, toRootDoc);
                insertNode(newFilterNodes, toRootDoc);
                insertNode(newFilterMappingNodes, toRootDoc);
                insertNode(newListenerNodes, toRootDoc);
                insertNode(newServletNodes, toRootDoc);
                insertNode(newServletMappingNodes, toRootDoc);
                insertNode(newEjbLocalRefNodes, toRootDoc);
                toRootDoc.appendChild(toDocument.createComment("PEのアドオン END "));
                //Duyenctn 20201203 start
                NodeList filterToDocList = (NodeList)xpath.evaluate("./child::node()[name() ='filter']", toDocument.getDocumentElement(),
                		XPathConstants.NODESET);
                int sizeTo = 0;
                if (filterToDocList != null && filterToDocList.getLength() > 0) {
                	sizeTo = filterToDocList.getLength();
                }
				Node filterNode = null;
				int countFilter = 0;
				for (int j = 0; j < sizeTo; j++) {
					Node filterTo = filterToDocList.item(j);
					String filterNameTo = xpath.evaluate("./child::node()[name()='filter-name']/text()", filterTo);
					if (filterNameTo.equals("URLDirectAccessFilter")) {
						countFilter++;
						if (countFilter == 1) {
							filterNode = filterTo;
						}
					}
				}
				if (filterNode != null && countFilter == 2) {
					toRootDoc.removeChild(filterNode);
				}
                //Duyenctn 20201203 end
                Util.writeDocument(toFile, toDocument);
            }
        } catch (Exception e) {
            throw new BuildException(e.getMessage());
        }


    }


    private  void insertNode(List<Node> newNode, Element toRootDoc){
        if(newNode == null || newNode.size() ==0){
            return;
        }

        for (Node node : newNode) {
            toRootDoc.appendChild(node);
        }
    }

    /**
     *<ejb-local-ref>
     *        <ejb-ref-name>pe4j-ear/PWAD3Bean/local</ejb-ref-name>
     *        <ejb-ref-type>Session</ejb-ref-type>
     *        <local>net.poweregg.exkeihi.pwa.PWAD3Local</local>
     *        <ejb-link>PWAD3Bean</ejb-link>
     *</ejb-local-ref>
     * @param frmDoc
     * @param toDoc
     * @param xpath
     * @return
     * @throws Exception
     */
    private List<Node> getNewEjbLocalRefNodes(Document frmDoc , Document toDoc , XPath xpath) throws Exception{
        List<Node> result = new ArrayList<Node>();
        NodeList ejbLocalRefList = (NodeList)xpath.evaluate("./child::node()[name() ='ejb-local-ref']",
                frmDoc.getDocumentElement(), XPathConstants.NODESET);

        if(ejbLocalRefList == null || ejbLocalRefList.getLength() ==0){
            return result ;
        }

        int size = ejbLocalRefList.getLength();
        Element toRootDoc = toDoc.getDocumentElement();
        String ejbRefName = "";
        for (int i = 0; i < size ; i++) {
            Node ejbLocalRef = ejbLocalRefList.item(i);
            ejbRefName = xpath.evaluate("./child::node()[name()='ejb-ref-name']/text()", ejbLocalRef);
            Node existsNode = (Node)xpath.evaluate("./child::node()[name() ='ejb-local-ref']/child::node()[name()='ejb-ref-name'][text()=\""+ejbRefName+"\"]",
                    toRootDoc, XPathConstants.NODE);

            if(existsNode == null){
                result.add(toDoc.importNode(ejbLocalRef, true));
            }
        }
        return result ;
    }


    /**
     * <servlet-mapping>
     *         <servlet-name>JnlpDownloadServlet</servlet-name>
     *         <url-pattern>/reminder/*</url-pattern>
     * </servlet-mapping>
     * @param frmDoc
     * @param toDoc
     * @param xpath
     * @return
     * @throws Exception
     */
    private List<Node> getNewServletMappingNodes(Document frmDoc , Document toDoc , XPath xpath) throws Exception{
        List<Node> result = new ArrayList<Node>();
        NodeList servletMappingList = (NodeList)xpath.evaluate("./child::node()[name() ='servlet-mapping']",
                frmDoc.getDocumentElement(), XPathConstants.NODESET);

        if(servletMappingList == null || servletMappingList.getLength() ==0){
            return result ;
        }

        int size = servletMappingList.getLength();
        Element toRootDoc = toDoc.getDocumentElement();
        String servletName = "";
        for (int i = 0; i < size ; i++) {
            Node servletMapping = servletMappingList.item(i);
            servletName = xpath.evaluate("./child::node()[name()='servlet-name']/text()", servletMapping);
            Node existsNode = (Node)xpath.evaluate("./child::node()[name() ='servlet-mapping']/child::node()[name()='servlet-name'][text()=\""+servletName+"\"]",
                    toRootDoc, XPathConstants.NODE);

            if(existsNode == null){
                result.add(toDoc.importNode(servletMapping, true));
            }
        }


        return result ;

    }

    /**
     * <servlet>
     *         <description>JAX-WS endpoint</description>
     *         <display-name>webservice</display-name>
     *         <servlet-name>webservice</servlet-name>
     *         <servlet-class>com.sun.xml.ws.transport.http.servlet.WSServlet</servlet-class>
     *         <load-on-startup>1</load-on-startup>
     * </servlet>
     * @param frmDoc
     * @param toDoc
     * @param xpath
     * @return
     * @throws Exception
     */
    private List<Node> getNewServletNodes(Document frmDoc , Document toDoc , XPath xpath) throws Exception{
        List<Node> result = new ArrayList<Node>();
        NodeList servletList = (NodeList)xpath.evaluate("./child::node()[name() ='servlet']",
                frmDoc.getDocumentElement(), XPathConstants.NODESET);

        if(servletList == null || servletList.getLength() == 0){
            return result ;
        }

        int size = servletList.getLength();
        Element toRootDoc = toDoc.getDocumentElement();
        String servletClass = "";
        for (int i = 0; i < size ; i++) {
            Node servlet = servletList.item(i);
            servletClass = xpath.evaluate("./child::node()[name()='servlet-name']/text()", servlet);
            Node existsNode = (Node)xpath.evaluate("./child::node()[name() ='servlet']/child::node()[name()='servlet-name'][text()=\""+servletClass+"\"]",
                    toRootDoc, XPathConstants.NODE);

            if(existsNode == null){
                result.add(toDoc.importNode(servlet, true));
            }
        }


        return result ;
    }

    /**
     * <listener>
     *         <listener-class>com.sun.faces.config.ConfigureListener</listener-class>
     *</listener>
     * @param frmDoc
     * @param toDoc
     * @param xpath
     * @return
     * @throws Exception
     */
    private  List<Node> getListenerNodes(Document frmDoc , Document toDoc , XPath xpath) throws Exception{
        List<Node> result = new ArrayList<Node>();
        NodeList listenerList = (NodeList)xpath.evaluate("./child::node()[name() ='listener']",
                frmDoc.getDocumentElement(), XPathConstants.NODESET);

        if(listenerList == null || listenerList.getLength()== 0){
            return result ;
        }

        int size = listenerList.getLength();
        Element toRootDoc = toDoc.getDocumentElement();
        String listenerClass = "";
        for (int i = 0; i < size ; i++) {
            Node listener = listenerList.item(i);
            listenerClass = xpath.evaluate("./child::node()[name()='listener-class']/text()", listener);
            Node existsNode = (Node)xpath.evaluate("./child::node()[name() ='listener']/child::node()[name()='listener-class'][text()=\""+listenerClass+"\"]",
                    toRootDoc, XPathConstants.NODE);

            if(existsNode == null){
                result.add(toDoc.importNode(listener, true));
            }
        }


        return result ;
    }

    /**
     * <filter-mapping>
     *         <filter-name>NTLMFilter</filter-name>
     *         <url-pattern>/winsso/*</url-pattern>
     * </filter-mapping>
     * @param frmDoc
     * @param toDoc
     * @param xpath
     * @return
     * @throws Exception
     */
    private List<Node> getNewFilterMappingNodes(Document frmDoc , Document toDoc , XPath xpath) throws Exception{
        List<Node> result = new ArrayList<Node>();

        NodeList filterMappingList = (NodeList)xpath.evaluate("./child::node()[name() ='filter-mapping']",
                frmDoc.getDocumentElement(), XPathConstants.NODESET);

        if(filterMappingList == null || filterMappingList.getLength() == 0){
            return result ;
        }

        int size = filterMappingList.getLength();
        Element toRootDoc = toDoc.getDocumentElement();
        String filterName = "";
        for (int i = 0; i < size ; i++) {
            Node filter = filterMappingList.item(i);
            filterName = xpath.evaluate("./child::node()[name()='filter-name']/text()", filter);
            Node existsNode = (Node)xpath.evaluate("./child::node()[name() ='filter-mapping']/child::node()[name()='filter-name'][text()=\""+filterName+"\"]",
                    toRootDoc, XPathConstants.NODE);

            if(existsNode == null){
                result.add(toDoc.importNode(filter, true));
            }
        }

        return result;
    }


    private List<Node> getNewFilterNodes(Document frmDoc , Document toDoc , XPath xpath) throws Exception{

        List<Node> result = new ArrayList<Node>();

        //Get all context-param nodes in frmDoc
        NodeList filterFrmDocList = (NodeList)xpath.evaluate("./child::node()[name() ='filter']", frmDoc.getDocumentElement(),
                XPathConstants.NODESET);

        if(filterFrmDocList == null || filterFrmDocList.getLength() ==0){
            return result ;
        }

        int size = filterFrmDocList.getLength();
        Element toRootDoc = toDoc.getDocumentElement();
        String filterName = "";
        for (int i = 0; i < size ; i++) {
            Node filter = filterFrmDocList.item(i);
            filterName = xpath.evaluate("./child::node()[name()='filter-name']/text()", filter);
            Node existsNode = (Node)xpath.evaluate("./child::node()[name() ='filter']/child::node()[name()='filter-name'][text()=\""+filterName+"\"]",
                    toRootDoc, XPathConstants.NODE);
            //20201203 Duyenctn start
			if (filterName.equals("URLDirectAccessFilter") && existsNode != null) {
				result.add(toDoc.importNode(filter, true));
			}
			//20201203 Duyenctn end
            if(existsNode == null){
                result.add(toDoc.importNode(filter, true));
            }
        }

        return result;
    }


    /**
     * <context-param>
     *   <description>Absolute path for webapp(V1.x compatible)</description>
     *   <param-name>applbase</param-name>
     *   <param-value>/pe4j/</param-value>
     *  </context-param>
     * @param frmDoc
     * @param toDoc
     * @param xpath
     * @return
     * @throws Exception
     */
    private  List<Node> getNewContextParamNodes(Document frmDoc , Document toDoc , XPath xpath) throws Exception{

        List<Node> result = new ArrayList<Node>();

        //Get all context-param nodes in frmDoc
        NodeList contextParamFrmDocList = (NodeList)xpath.evaluate("./child::node()[name() ='context-param']",
                    frmDoc.getDocumentElement(), XPathConstants.NODESET);

        if(contextParamFrmDocList == null || contextParamFrmDocList.getLength() == 0){
            return result ;
        }

        int size = contextParamFrmDocList.getLength();
        Element toRootDoc = toDoc.getDocumentElement();
        String paramName = "";
        for (int i = 0; i < size ; i++) {
            Node contextParam = contextParamFrmDocList.item(i);
            paramName = xpath.evaluate("./child::node()[name()='param-name']/text()", contextParam);
            Node existsNode = (Node)xpath.evaluate("./child::node()[name() ='context-param']/child::node()[name()='param-name'][text()=\""+paramName+"\"]",
                    toRootDoc, XPathConstants.NODE);

            if(existsNode == null){
                result.add(toDoc.importNode(contextParam, true));
            }
        }

        return result;
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
