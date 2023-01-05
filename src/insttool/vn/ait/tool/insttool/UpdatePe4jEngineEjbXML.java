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

public class UpdatePe4jEngineEjbXML extends Task {

	File toFile;
	File fromFile;

	static final Log log = LogFactory.getLog(UpdatePe4jEngineEjbXML.class);

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

	private Element createElement(Document doc, String tagName) {
		return doc.createElement(tagName);
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

			List<Node> oldEjbNodes = new ArrayList<Node>();
			List<Node> newEjbNodes = new ArrayList<Node>();

			getNewNodes(frmDocument, xpath, toDocument, newEjbNodes, oldEjbNodes);

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

			if (newEjbNodes != null && newEjbNodes.size() > 0) {
				// Get session tag in toDocument
				Node enterpriseBeanTag = (Node) xpath.evaluate("/ejb-jar/enterprise-beans", toDoc, XPathConstants.NODE);
				Node startComment = toDocument.createComment(" Kensetsu START");
				enterpriseBeanTag.appendChild(startComment);
				enterpriseBeanTag.appendChild(toDocument.createTextNode("\n"));

				for (Node node : newEjbNodes) {
					enterpriseBeanTag.appendChild(node);
				}

				Node endComment = toDocument.createComment(" Kensetsu END");
				enterpriseBeanTag.appendChild(endComment);

			}

			if (oldEjbNodes != null && oldEjbNodes.size() > 0) {

				NodeList sessionList = (NodeList) xpath.evaluate("/ejb-jar/enterprise-beans/session", toDoc,
						XPathConstants.NODESET);
				int size = sessionList.getLength();
				String toDisplayName = "";
				String displayName = "";

				for (Node node : oldEjbNodes) {
					// sessionNode =
					// (Node)xpath.evaluate("./ancestor::session/display-name/text()",node,
					// XPathConstants.NODE);
					displayName = Util.toEmpty(xpath.evaluate("./ancestor::session/display-name/text()", node));

					for (int i = 0; i < size; i++) {
						sessionNode = sessionList.item(i);
						toDisplayName = Util.toEmpty(xpath.evaluate("./display-name/text()", sessionNode));
						if (toDisplayName.equalsIgnoreCase(displayName)) {
							sessionNode.appendChild(toDocument.importNode(node, true));
							break;
						}
					}

				}

			}

			Util.writeDocument(toFile, toDocument);
		} catch (Exception e) {
			throw new BuildException(e.getMessage());
		}

	}

	private void getNewNodes(Document frm, XPath xpath, Document toDocument, List<Node> newEjbSession,
			List<Node> oldEjbSession) {

		try {
			Element doc = frm.getDocumentElement();
			Node enterpriseBeanNode = (Node) xpath.evaluate("/ejb-jar/enterprise-beans", doc, XPathConstants.NODE);
			Node sessionNode = (Node) xpath.evaluate("/ejb-jar/enterprise-beans/session", doc, XPathConstants.NODE);

			if (enterpriseBeanNode == null || sessionNode == null) {
				return;
			}

			// an enterprise <-----> session tags <-----> childs
			NodeList sessionList = (NodeList) xpath.evaluate("/ejb-jar/enterprise-beans/session", doc,
					XPathConstants.NODESET);
			int size = 0;

			String displayName = "";

			if (sessionList != null && sessionList.getLength() > 0) {
				size = sessionList.getLength();
				Node currentSessionNode = null;

				for (int i = 0; i < size; i++) {
					currentSessionNode = sessionList.item(i);
					displayName = xpath.evaluate("./display-name/text()", currentSessionNode);

					Node existNode = (Node) xpath.evaluate(
							"/ejb-jar/enterprise-beans/session/display-name[text()=\"" + displayName + "\"]",
							toDocument.getDocumentElement(), XPathConstants.NODE);

					if (existNode != null) {
						// If exists currentSessionNode in the toDoc
						processOldEjbSession(oldEjbSession, currentSessionNode, frm, xpath, toDocument);
					} else {
						// new ejb
						Node tmpNode = toDocument.importNode(currentSessionNode, true);
						newEjbSession.add(tmpNode);
					}
				}

			}

		} catch (XPathExpressionException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private void processOldEjbSession(List<Node> oldEjBSessionList, Node currentSessionNode, Document frm, XPath xpath,
			Document toDocument) {

		if (currentSessionNode == null || currentSessionNode.getChildNodes() == null
				|| currentSessionNode.getChildNodes().getLength() == 0) {
			return;
		}

		// We must add new child tag into toDocument
		int childSize = currentSessionNode.getChildNodes().getLength();
		Node currentChildTag = null;
		String path = "";

		try {
			for (int i = 0; i < childSize; i++) {

				currentChildTag = currentSessionNode.getChildNodes().item(i);
				// tmpNode =(NodeList) xpath.evaluate("./child::*", currentChildTag,
				// XPathConstants.NODESET);
				if (currentChildTag.getNodeType() == Node.ELEMENT_NODE) {
					path = getPathOfNode(currentChildTag, "ejb-jar", xpath);
					Node existNode = (Node) xpath.evaluate(path, toDocument, XPathConstants.NODE);
					if (existNode == null) {
						// oldEjBSessionList.add(toDocument.importNode(currentChildTag, true));
						oldEjBSessionList.add(currentChildTag);// Don't importNode here
					}
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private static String getPathOfNode(Node currentNode, String nodeRootName, XPath xpath) {

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
			throw new RuntimeException(e.getMessage());
		}

		return getPathOfNode(currentNode.getParentNode(), nodeRootName, xpath) + path;
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
