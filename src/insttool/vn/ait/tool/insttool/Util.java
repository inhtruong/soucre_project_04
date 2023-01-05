package vn.ait.tool.insttool;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildException;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Util {

    private static final Log log = LogFactory.getLog(Util.class);
    private static final String COMMENT_START = "*** PEのアドオン START ***";
    private static final String COMMENT_END = "*** PEのアドオン END ***";

    static Document getDocument(File file) throws BuildException {
        try {
            DocumentBuilderFactory factory
                = DocumentBuilderFactory.newInstance();
            //ネットワークに繋がっていない時に、パースに失敗
            //するため、妥当性検証をOFFにする。
            if (!factory.isValidating()) {
                log.info("パーサーの妥当性検証をOFFにします.");
                factory.setValidating(false);
            }
            return factory.newDocumentBuilder().parse(file);
        } catch (IOException ioe) {
            throw new BuildException("対象ファイルの読み込み時にエラーが発生しました");
        } catch (SAXException saxe) {
            throw new BuildException("対象ファイルの読み込み時に構文解析エラーが発生しました");
        } catch (Exception e) {
            throw new BuildException(e.getMessage());
        }
    }

    /**
     * 引数で与えられたノードの次のノードが空白のテキストノード（空行）またはEX経費が追加したコメントノード
     * か判定する.
     *
     * @param node ノード（このノードの次のノードが判定に利用される)
     * @return 空白のテキストまたはEX経費のコメントの場合はtrue, それ以外はfalse
     */
    static boolean isNextSiblingIsWhiteSpaceOrMyComment(Node node) {
        Node next = node.getNextSibling();
        if (next != null && next.getNodeType()==Node.TEXT_NODE && !next.getNodeValue().matches("[a-zA-Z].*")) {
            return true;
        } else {
            if (next != null && next.getNodeType()==Node.COMMENT_NODE
                    && (COMMENT_START.equals(next.getNodeValue()) || COMMENT_END.equals(next.getNodeValue()))) {
                return true;
            }
            return false;
        }
    }

    /**
     * EX経費が追加する要素の開始コメントを生成します.
     * @param document
     * @return
     */
    static Node createStartComment(Document document) {
        return document.createComment(COMMENT_START);
    }

    /**
     * EX経費が追加する要素の終了コメントを生成します.
     * @param document
     * @return
     */
    static Node createEndComment(Document document) {
        return document.createComment(COMMENT_END);
    }

    /**
     * 引数で指定されたファイルにドキュメントを書き込みます.
     *
     * ファイルは、xml形式でインデント付きで出力されます.
     *
     * @param targetFile 出力先のファイル
     * @param document 出力するドキュメント
     */
    static void writeDocument(File targetFile, Document document) {
        try {
            Transformer transformer = TransformerFactory.newInstance()
                                        .newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "2");
            DocumentType doctype = document.getDoctype();
            if (doctype != null) {
                String doctype_public = doctype.getPublicId();
                if (doctype_public != null && !doctype_public.equals("")) {
                    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype_public);
                }
                String doctype_system = doctype.getSystemId();
                if (doctype_system != null && !doctype_system.equals("")) {
                    transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype_system);
                }
            }
            DOMSource domsource = new DOMSource(document.getDocumentElement());
            StreamResult streamresult = new StreamResult(targetFile);
            transformer.transform(domsource,streamresult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toEmpty(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    public static boolean nullOrBlank(Object obj){
        if(obj == null)
            return true;
        return "".equals(obj.toString().trim());
    }

    public static boolean isEmpty(String value){
        if(value == null){
            return true;
        }

        return "".equals(value.trim());
    }
}
