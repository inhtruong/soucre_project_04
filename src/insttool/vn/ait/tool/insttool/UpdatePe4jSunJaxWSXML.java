package vn.ait.tool.insttool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdatePe4jSunJaxWSXML extends Task {

	File toFile;
	File fromFile;

	static final Log log = LogFactory.getLog(UpdatePe4jSunJaxWSXML.class);

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
			// Get all page nodes
			NodeList endPointNodes = (NodeList) xpath.evaluate(
					"./child::node()[name()='endpoint']",
					frmDocument.getDocumentElement(), XPathConstants.NODESET);

			if (endPointNodes == null) {
				return;
			}

			int size = endPointNodes.getLength();
			Element toDocRoot =toDocument.getDocumentElement();
			String endPointName = "";
			List<Node> newNodes = new ArrayList<Node>();
			
			for (int i = 0; i < size; i++) {
				Node endPoint = endPointNodes.item(i);
				endPointName = xpath.evaluate("./@Named", endPoint);
				//Check exist ?
				Node existEndPoint = (Node)xpath.evaluate(
						"./child::node()[name()='endpoint'][@Named=\""+ endPointName +"\"]",
						toDocRoot, XPathConstants.NODE);
				
				if(existEndPoint == null){
					//this endPoint node is new node
					newNodes.add(toDocument.importNode(endPoint, true));
				}
			}
			
			if(newNodes.size() >0){
				//Find the last endPoint node
//				Node lastEndPointNode = (Node) xpath.evaluate(
//						"./child::node()[name()='endpoint'][last()]", toDocRoot,
//						XPathConstants.NODE);
//				Node startComment = toDocument.createComment("END POINT OF PEのアドオン (START) ");
//				toDocRoot.insertBefore(startComment,lastEndPointNode.getNextSibling());
//				
//				Node endComment = toDocument.createComment("PAGES OF PEのアドオン (END)");
//				toDocRoot.insertBefore(endComment,startComment.getNextSibling());
//				
				
				for (Node node : newNodes) {
					toDocRoot.appendChild(node);
			//		toDocRoot.insertBefore(node, endComment);
				}
				
				Util.writeDocument(toFile, toDocument);
			}
		} catch (Exception e) {
			throw new BuildException(e.getMessage());
		}

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
