package net.poweregg.mitsubishi.action;

import javax.ejb.Local;

/**
 * 
 * @author diennv
 */
@Local
public interface ApprovalServiceUmb {

	/**
	 * 
	 * @param appRecepNo
	 * @param status
	 * @param mode 0-new, 1-edit, 2-cancel
	 */
	public void commitApproval(Long appRecepNo, String status, int mode);
}
