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
	 * @param mode 1-new, 2-edit, 3-cancel
	 */
	public void commitApproval(Long appRecepNo, String status, int mode);
}
