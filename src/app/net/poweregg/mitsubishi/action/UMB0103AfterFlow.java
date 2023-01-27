package net.poweregg.mitsubishi.action;

import javax.naming.InitialContext;

import net.poweregg.dataflow.IProcAfterFlow;

/**
 * POWER EGG V2.0
 * 稟議書 事後処理
 */
public class UMB0103AfterFlow implements IProcAfterFlow {

    /**
     * デフォルトコンストラクタ
     */
    public UMB0103AfterFlow()  {
        super();
    }

    /**
     * データフローから呼び出される事後処理のビジネスロジックです。<BR>
     * このメソッドはJTAトランザクションスコープ内で呼び出されますので<BR>
     * トランザクション命令を発行しないでください。
     * @param appRecepNo 申請受付番号
     * @param historyNo 履歴番号
     * @param status 決裁状況（IntAfterFlow インタフェース
     * 							に定数で定義されています）
     */
    public void doProcess(Long appRecepNo, Integer historyNo, String status) {

        try {
            InitialContext icx = new InitialContext();
            ApprovalServiceUmb service = (ApprovalServiceUmb)icx.lookup("java:global/pe4j-ear/wf-mitsubishi/ApprovalServiceUmbBean");
	        service.commitApproval(appRecepNo, status, 3);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
