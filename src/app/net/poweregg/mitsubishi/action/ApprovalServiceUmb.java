package net.poweregg.mitsubishi.action;

import javax.ejb.Local;

/**
 * 
 * @author diennv
 */
@Local
public interface ApprovalServiceUmb {

	// ビルメンテ稟議事後処理
	public void commitApproval(Long appRecepNo, String status);
}
