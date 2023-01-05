package net.poweregg.mitsubishi.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;

import net.poweregg.annotations.PEIntercepter;
import net.poweregg.annotations.Single;
import net.poweregg.common.ClassificationService;
import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.mitsubishi.dto.UTSR01SearchDto;
import net.poweregg.mitsubishi.dto.UTSR01SearchDto.SORTITEM;
import net.poweregg.mitsubishi.dto.UTSR01SearchDto.SORTORDER;
import net.poweregg.mitsubishi.service.UTSRCommonService;
import net.poweregg.seam.faces.FacesMessages;
import net.poweregg.util.JSFUtil;
import net.poweregg.util.ui.PageBean;

/**
 * 
 * UTSR01： (01)検索画面 (02): 一覧画面
 * 
 * @author diennv
 */

@Named("UTSR01Bean")
@ConversationScoped
@PEIntercepter
public class UTSR01Bean implements Serializable {
	private static final long serialVersionUID = 1L;

	@EJB
	ClassificationService classificationService;

	@Inject
	private transient FacesMessages facesMessages;

	/**
	 * 実行履歴一覧のDataModel
	 */
	// @In(required = false, scope = ScopeType.CONVERSATION)
	// @Out(required = false)
	private DataModel<UTSR01SearchDto> tsr01ListDataModel = new ListDataModel<>();

	/**
	 * リテラル定義
	 */
	@Inject
	@Single
	private transient Map<String, String> messages;

	/**
	 * 改ページ制御用クラス
	 */
	@Inject
	@Single
	private PageBean pageBean;

	@EJB
	private UTSRCommonService utsrService;

	/**
	 * 検索条件
	 */
	// @In(required = false)
	// @Out
	@Inject
	private UTSR01SearchDto utsrSearchCond;

	private List<UTSR01SearchDto> tsrSearchResult;

	/*
	 * init page UTSR0101e
	 */
	public void initPageSearch() {
		if (utsrSearchCond == null) {
			utsrSearchCond = new UTSR01SearchDto();
		} else {
			// ページ情報を更新
			pageBean.setCurrentPageIndex(0);
			utsrSearchCond.setSortItem(SORTITEM.COMPANYCODE);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
	}

	/*
	 * init page UTSR0102l
	 */
	public void initPageList() {
		if (utsrSearchCond != null) {
			getUTSRSearchList();
		}
	}

	/**
	 * action for button search
	 * 
	 * @return searchListWebDB Page
	 */
	public String toSearchListWebDB() {

		if (checkSearchCondition()) {
			return MitsubishiConst.BLANK;
		}
		return MitsubishiConst.SEARCH_LIST_WEBDB;
	}

	/**
	 * check エラーメッセージ
	 * 
	 * @return true if error
	 */
	private boolean checkSearchCondition() {
		boolean errorFlg = false;
		if (100 < utsrSearchCond.getCompanyNameKanji().length()) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, MessageFormat
					.format(messages.get("utsr_check_number_degits"), messages.get("utsr_kanji_company_name"), "100"));
			errorFlg = true;
		}
		if (60 < utsrSearchCond.getCompanyNameKana().length()) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, MessageFormat
					.format(messages.get("utsr_check_number_degits"), messages.get("utsr_kana_company_name"), "60"));
			errorFlg = true;
		}
		if (120 < utsrSearchCond.getLocationText().length()) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, MessageFormat
					.format(messages.get("utsr_check_number_degits"), messages.get("utsr_location"), "120"));
			errorFlg = true;
		}
		if (23 < utsrSearchCond.getTelNumberText().length()) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, MessageFormat
					.format(messages.get("utsr_check_number_degits"), messages.get("utsr_corp_phone_number"), "23"));
			errorFlg = true;
		}
		if (120 < utsrSearchCond.getRepresentativeKanji().length()) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, MessageFormat.format(
					messages.get("utsr_check_number_degits"), messages.get("utsr_kanji_representative"), "120"));
			errorFlg = true;
		}
		if (60 < utsrSearchCond.getRepresentativeKana().length()) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, MessageFormat
					.format(messages.get("utsr_check_number_degits"), messages.get("utsr_kana_representative"), "60"));
			errorFlg = true;
		}
		if (60 < utsrSearchCond.getTsrCompanyCode().length()) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, MessageFormat
					.format(messages.get("utsr_check_number_degits"), messages.get("utsr_company_code"), "60"));
			errorFlg = true;
		}
		// 企業コードが半角英数字でない場合
		if (!JSFUtil.isHalfSize(utsrSearchCond.getTsrCompanyCode(), null)) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR,
					MessageFormat.format(messages.get("utsr_check_halfsize"), messages.get("utsr_company_code")));
			errorFlg = true;
		}
		// 企業電話番号が半角英数字でない場合
		if (!JSFUtil.isHalfSize(utsrSearchCond.getTelNumberText(), null)) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR,
					MessageFormat.format(messages.get("utsr_check_halfsize"), messages.get("utsr_corp_phone_number")));
			errorFlg = true;
		}
		// カナ代表者がカタカナ以外の場合
		if (!isAllFullwidthKatakana(utsrSearchCond.getCompanyNameKana())) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR,
					MessageFormat.format(messages.get("utsr_check_katakana"), messages.get("utsr_kana_company_name")));
			errorFlg = true;
		}
		// カナ商号がカタカナ以外の場合
		if (!isAllFullwidthKatakana(utsrSearchCond.getRepresentativeKana())) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, MessageFormat.format(messages.get("utsr_check_katakana"),
					messages.get("utsr_kana_representative")));
			errorFlg = true;
		}
		return errorFlg;
	}

	/**
	 * 戻るBT押下
	 * 
	 * @return
	 */
	public String actionBack() {

		return MitsubishiConst.BACK_TO_SEARCH;
	}

	/**
	 * check full width of katakana
	 * 
	 * @param input
	 * @return true if katakana is full width
	 */
	private static boolean isAllFullwidthKatakana(String input) {
		for (int i = 0, max = input.length(); i < max; i++) {
			char c = input.charAt(i);
			Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
			if (!Character.UnicodeBlock.KATAKANA.equals(block))
				return false;
		}
		return true;
	}

	/**
	 * Sort Company Code
	 * 
	 * @return null
	 */
	public String sortByCompanyCode() {
		if (SORTITEM.COMPANYCODE.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.COMPANYCODE);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * Sort Company Name Kanji
	 * 
	 * @return null
	 */
	public String sortByCompanyNameKanji() {
		if (SORTITEM.COMPANYNAMEKANJI.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.COMPANYNAMEKANJI);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * Sort Company Name Kanji
	 * 
	 * @return null
	 */
	public String sortByTelPhoneNumber() {
		if (SORTITEM.PHONENUMBER.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.PHONENUMBER);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * Sort Founded
	 * 
	 * @return null
	 */
	public String sortByFounded() {
		if (SORTITEM.FOUNDED.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.FOUNDED);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * Sort Latest grade
	 * 
	 * @return null
	 */
	public String sortByLatestGrade() {
		if (SORTITEM.LATESTGRADE.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.LATESTGRADE);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * Sort date establish
	 * 
	 * @return null
	 */
	public String sortByDateEstablish() {
		if (SORTITEM.DATEESTABLISH.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.DATEESTABLISH);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * Sort classification
	 * 
	 * @return null
	 */
	public String sortByClassification() {
		if (SORTITEM.CLASSIFICATION.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.CLASSIFICATION);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * Sort CAPITAL
	 * 
	 * @return null
	 */
	public String sortByCapital() {
		if (SORTITEM.CAPITAL.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.CAPITAL);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * Sorts Sales amount
	 * 
	 * @return null
	 */
	public String sortBySalesAmount() {
		if (SORTITEM.SALESAMOUNT.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.SALESAMOUNT);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * Sorts Employees Count
	 * 
	 * @return null
	 */
	public String sortByEmployeesCount() {
		if (SORTITEM.EMPLOYEESCOUNT.equals(utsrSearchCond.getSortItem())) {
			if (utsrSearchCond.getSortOrder().compareTo(SORTORDER.ASC) == 0) {
				utsrSearchCond.setSortOrder(SORTORDER.DESC);
			} else {
				utsrSearchCond.setSortOrder(SORTORDER.ASC);
			}
		} else {
			utsrSearchCond.setSortItem(SORTITEM.EMPLOYEESCOUNT);
			utsrSearchCond.setSortOrder(SORTORDER.ASC);
		}
		getUTSRSearchList();
		return null;
	}

	/**
	 * get list field web DB
	 */
	private void getUTSRSearchList() {
		try {

			long maxPage = pageBean.getNumRowOfPage();
			// 表示開始位置を取得する
			long start = pageBean.getCurrentPageIndex() * maxPage;

			if (start > -1) {
				tsrSearchResult = utsrService.searchData(utsrSearchCond, start, maxPage);
				tsr01ListDataModel.setWrappedData(tsrSearchResult);
				// 取得レコード数をPage管理Beanにセットする
				pageBean.setTotalRecCount(utsrService.countTotalData(utsrSearchCond, start, maxPage).intValue());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * move first page
	 */
	public void firstPage() {

		// 表示条件初期値で検索
		pageBean.setCurrentPageIndex(0);
		getUTSRSearchList();
	}

	/**
	 * move last page
	 * 
	 * @throws Exception
	 */
	public void lastPage() throws Exception {

		// 表示条件初期値で検索
		checkTotalRecCount();
		int currentPage = (pageBean.getTotalRecCount() - 1) / pageBean.getNumRowOfPage();
		pageBean.setCurrentPageIndex(currentPage);
		getUTSRSearchList();
	}

	/**
	 * move next page
	 * 
	 * @throws Exception
	 */
	public void nextPage() throws Exception {

		// 表示条件初期値で検索
		int currentPage = pageBean.getCurrentPageIndex();
		if (checkTotalRecCount()) {
			currentPage++;
		}
		pageBean.setCurrentPageIndex(currentPage);
		getUTSRSearchList();
	}

	/**
	 * move previous page
	 */
	public void prevPage() {

		// 表示条件初期値で検索
		int currentPage = pageBean.getCurrentPageIndex() - 1;
		pageBean.setCurrentPageIndex(currentPage);
		getUTSRSearchList();
	}

	/**
	 * check total record count when add and delete record, then there will be a
	 * difference in value NumRowOfPage if difference return false else return true
	 * 
	 * @throws Exception
	 */
	private boolean checkTotalRecCount() throws Exception {

		// get old total record count
		int oldTotalRecCount = pageBean.getTotalRecCount();
		// get all data and get new total record count
		int newTotalRecCount = utsrService.countTotalData(utsrSearchCond, 0, pageBean.getNumRowOfPage()).intValue();

		if (oldTotalRecCount != newTotalRecCount) {
			pageBean.setTotalRecCount(newTotalRecCount);
			return false;
		}

		return true;
	}

	@PreDestroy
	public void destroy() {
	}

	/**
	 * @return the utsrSearchCond
	 */
	public UTSR01SearchDto getUtsrSearchCond() {
		return utsrSearchCond;
	}

	/**
	 * @param utsrSearchCond the utsrSearchCond to set
	 */
	public void setUtsrSearchCond(UTSR01SearchDto utsrSearchCond) {
		this.utsrSearchCond = utsrSearchCond;
	}

	/**
	 * @return the tsrSearchResult
	 */
	public List<UTSR01SearchDto> getTsrSearchResult() {
		return tsrSearchResult;
	}

	/**
	 * @param tsrSearchResult the tsrSearchResult to set
	 */
	public void setTsrSearchResult(List<UTSR01SearchDto> tsrSearchResult) {
		this.tsrSearchResult = tsrSearchResult;
	}

	/**
	 * @return the tsr01ListDataModel
	 */
	public DataModel<UTSR01SearchDto> getTsr01ListDataModel() {
		return tsr01ListDataModel;
	}

	/**
	 * @param tsr01ListDataModel the tsr01ListDataModel to set
	 */
	public void setTsr01ListDataModel(DataModel<UTSR01SearchDto> tsr01ListDataModel) {
		this.tsr01ListDataModel = tsr01ListDataModel;
	}

}
