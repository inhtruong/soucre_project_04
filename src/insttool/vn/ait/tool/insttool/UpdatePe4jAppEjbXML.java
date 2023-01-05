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

public class UpdatePe4jAppEjbXML extends Task {

	File toFile;
	File fromFile;

	static final Log log = LogFactory.getLog(UpdatePe4jAppEjbXML.class);

	@Override
	public void execute() {
		try {
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

			// Are there <enterprise-beans> tag and <session>tag in
			// pe4j-ear/pe4j-app.jar/META-INF/ejb-jar.xml file ?
			Element toDoc = toDocument.getDocumentElement();

			Node enterpriseBeanNode = (Node) xpath.evaluate("/ejb-jar/enterprise-beans", toDoc, XPathConstants.NODE);
			boolean existEnterpriseBeanNode = false;
			boolean existSessionNode = false;

			if (enterpriseBeanNode != null) {
				existEnterpriseBeanNode = true;
			}

			Node sessionNode = (Node) xpath.evaluate("/ejb-jar/enterprise-beans/session", toDoc, XPathConstants.NODE);

			if (sessionNode != null) {
				existSessionNode = true;
			}

			List<Node> newNodes = getNewNodes(frmDocument, xpath, toDocument);

			// In case of : there aren't <enterprise-beans> tag and <session>tag in
			// pe4j-ear/pe4j-app.jar/META-INF/ejb-jar.xml file ?
			if (!existEnterpriseBeanNode) {
				enterpriseBeanNode = createElement(toDocument, "enterprise-beans");

				if (!existSessionNode) {
					sessionNode = createElement(toDocument, "session");
				}

				enterpriseBeanNode.appendChild(sessionNode);
				toDoc.appendChild(enterpriseBeanNode);
			}

			if (newNodes != null && newNodes.size() > 0) {
				// Get session tag in toDocument
				Node sessionTag = (Node) xpath.evaluate("/ejb-jar/enterprise-beans/session", toDoc,
						XPathConstants.NODE);
				Node startComment = toDocument.createComment(" Kensetsu START");
				sessionTag.appendChild(startComment);
				sessionTag.appendChild(toDocument.createTextNode("\n"));

				for (Node node : newNodes) {
					sessionTag.appendChild(node);
				}

				Node endComment = toDocument.createComment(" Kensetsu END");
				sessionTag.appendChild(endComment);

				Util.writeDocument(toFile, toDocument);
			}

		} catch (Exception e) {
			throw new BuildException(e.getMessage());
		}

	}

	private List<Node> getNewNodes(Document frm, XPath xpath, Document toDocument) {

		List<Node> result = new ArrayList<Node>();
		try {
			Element doc = frm.getDocumentElement();
			Node enterpriseBeanNode = (Node) xpath.evaluate("/ejb-jar/enterprise-beans", doc, XPathConstants.NODE);
			Node sessionNode = (Node) xpath.evaluate("/ejb-jar/enterprise-beans/session", doc, XPathConstants.NODE);

			if (enterpriseBeanNode == null || sessionNode == null) {
				return result;
			}

			NodeList list = (NodeList) xpath.evaluate("/ejb-jar/enterprise-beans/session/child::node()", doc,
					XPathConstants.NODESET);

			if (list != null && list.getLength() > 0) {
				int size = list.getLength();
				String path = "";
				Element toRootElement = toDocument.getDocumentElement();
				Node node = null;
				for (int i = 0; i < size; i++) {

					node = list.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {

						path = getPathOfNode(node, "ejb-jar", xpath);

						Node existsNode = (Node) xpath.evaluate(path, toRootElement, XPathConstants.NODE);
						if (existsNode == null) {
							result.add(toDocument.importNode(list.item(i), true));
						}
					}
				}
			}

		} catch (XPathExpressionException e) {
			throw new BuildException(e.getMessage());
		}

		return result;
	}

	private String getPathOfNode(Node currentNode, String nodeRootName, XPath xpath) {

		if (currentNode == null) {
			return "";
		}

		String path = "";
		try {
			if (nodeRootName.equalsIgnoreCase(currentNode.getNodeName())) {
				return "/" + currentNode.getNodeName();
			}

			path = "/" + currentNode.getNodeName();
			String contextValue = null;

			Node childNode = (Node) xpath.evaluate("./child::*", currentNode, XPathConstants.NODE);

			if (childNode == null) {
				contextValue = xpath.evaluate("./text()", currentNode);

			} else if ("ejb-local-ref".equalsIgnoreCase(currentNode.getNodeName())) {
				contextValue = xpath.evaluate("./ejb-ref-name/text()", currentNode);
			}

			if (!Util.nullOrBlank(contextValue)) {

				if (!"ejb-local-ref".equalsIgnoreCase(currentNode.getNodeName())) {
					path += "[text()=\"" + contextValue + "\"]";
				} else {
					path += "/ejb-ref-name[text()=\"" + contextValue + "\"]";
				}

			}

		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}

		return getPathOfNode(currentNode.getParentNode(), nodeRootName, xpath) + path;
	}

	private Element createElement(Document doc, String tagName) {
		return doc.createElement(tagName);
	}

	/**
	 * タスク属性のチェックを行う
	 * 
	 * @throws BuildException
	 */
	void validateAttributes() throws BuildException {
		if (toFile == null) {
			throw new BuildException("toFile属性は必須です");
		}
		if (!toFile.exists()) {
			throw new BuildException("toFileが存在しません");
		}
		if (toFile.exists() && toFile.isDirectory()) {
			throw new BuildException("toFile属性にディレクトリは指定できません");
		}
		if (fromFile == null) {
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
