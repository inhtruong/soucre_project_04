/**
 * 
 */
package net.poweregg.mitsubishi.dto;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

/**
 * UTSR01 Search Condition
 * 
 * @author diennv
 *
 */
@Named(value = "tsr01Search")
@ConversationScoped
public class UTSR01SearchDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクター
	 */
	public UTSR01SearchDto() {
	}

	/**
	 * 漢字商号
	 */
	private String companyNameKanji;

	/**
	 * カナ商号
	 */
	private String companyNameKana;

	/**
	 * 所在地
	 */
	private String locationText;

	/**
	 * 企業電話番号 / 電話番号（編集済）
	 */
	private String telNumberText;

	/**
	 * 漢字代表者
	 */
	private String representativeKanji;

	/**
	 * カナ代表者
	 */
	private String representativeKana;

	/**
	 * 企業コード / TSR企業コード
	 */
	private String tsrCompanyCode;

	/**
	 * id
	 */
	private String id;

	/**
	 * 評点最新
	 */
	private String grade;

	/**
	 * 創業年月
	 */
	private String founded;

	/**
	 * 設立年月
	 */
	private String dateEstablishment;

	/**
	 * 大分類
	 */
	private String classification;

	/**
	 * 売上高
	 */
	private String amountSales;

	/**
	 * 資本金
	 */
	private String capital;

	/**
	 * 従業員数
	 */
	private String numberEmp;

	/**
	 * 詳細表示
	 */
	private String detailView;

	private SORTITEM sortItem = SORTITEM.COMPANYCODE;

	private SORTORDER sortOrder = SORTORDER.ASC;

	/** 並び替え可能フィールドの列挙 */
	public enum SORTITEM {
		COMPANYCODE, COMPANYNAMEKANJI,
		PHONENUMBER, LATESTGRADE,
		FOUNDED, DATEESTABLISH,
		CLASSIFICATION, SALESAMOUNT,
		CAPITAL, EMPLOYEESCOUNT
	};

	/** 並び替え方法の列挙 */
	public enum SORTORDER {
		ASC, DESC
	};

	/**
	 * @return the companyNameKanji
	 */
	public String getCompanyNameKanji() {
		return companyNameKanji;
	}

	/**
	 * @param companyNameKanji the companyNameKanji to set
	 */
	public void setCompanyNameKanji(String companyNameKanji) {
		this.companyNameKanji = companyNameKanji;
	}

	/**
	 * @return the companyNameKana
	 */
	public String getCompanyNameKana() {
		return companyNameKana;
	}

	/**
	 * @param companyNameKana the companyNameKana to set
	 */
	public void setCompanyNameKana(String companyNameKana) {
		this.companyNameKana = companyNameKana;
	}

	/**
	 * @return the locationText
	 */
	public String getLocationText() {
		return locationText;
	}

	/**
	 * @param locationText the locationText to set
	 */
	public void setLocationText(String locationText) {
		this.locationText = locationText;
	}

	/**
	 * @return the telNumberText
	 */
	public String getTelNumberText() {
		return telNumberText;
	}

	/**
	 * @param telNumberText the telNumberText to set
	 */
	public void setTelNumberText(String telNumberText) {
		this.telNumberText = telNumberText;
	}

	/**
	 * @return the representativeKanji
	 */
	public String getRepresentativeKanji() {
		return representativeKanji;
	}

	/**
	 * @param representativeKanji the representativeKanji to set
	 */
	public void setRepresentativeKanji(String representativeKanji) {
		this.representativeKanji = representativeKanji;
	}

	/**
	 * @return the representativeKana
	 */
	public String getRepresentativeKana() {
		return representativeKana;
	}

	/**
	 * @param representativeKana the representativeKana to set
	 */
	public void setRepresentativeKana(String representativeKana) {
		this.representativeKana = representativeKana;
	}

	/**
	 * @return the tsrCompanyCode
	 */
	public String getTsrCompanyCode() {
		return tsrCompanyCode;
	}

	/**
	 * @param tsrCompanyCode the tsrCompanyCode to set
	 */
	public void setTsrCompanyCode(String tsrCompanyCode) {
		this.tsrCompanyCode = tsrCompanyCode;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the grade
	 */
	public String getGrade() {
		return grade;
	}

	/**
	 * @param grade the grade to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}

	/**
	 * @return the founded
	 */
	public String getFounded() {
		return founded;
	}

	/**
	 * @param founded the founded to set
	 */
	public void setFounded(String founded) {
		this.founded = founded;
	}

	/**
	 * @return the dateEstablishment
	 */
	public String getDateEstablishment() {
		return dateEstablishment;
	}

	/**
	 * @param dateEstablishment the dateEstablishment to set
	 */
	public void setDateEstablishment(String dateEstablishment) {
		this.dateEstablishment = dateEstablishment;
	}

	/**
	 * @return the classification
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * @param classification the classification to set
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @return the amountSales
	 */
	public String getAmountSales() {
		return amountSales;
	}

	/**
	 * @param amountSales the amountSales to set
	 */
	public void setAmountSales(String amountSales) {
		this.amountSales = amountSales;
	}

	/**
	 * @return the capital
	 */
	public String getCapital() {
		return capital;
	}

	/**
	 * @param capital the capital to set
	 */
	public void setCapital(String capital) {
		this.capital = capital;
	}

	/**
	 * @return the numberEmp
	 */
	public String getNumberEmp() {
		return numberEmp;
	}

	/**
	 * @param numberEmp the numberEmp to set
	 */
	public void setNumberEmp(String numberEmp) {
		this.numberEmp = numberEmp;
	}

	/**
	 * @return the detailView
	 */
	public String getDetailView() {
		return detailView;
	}

	/**
	 * @param detailView the detailView to set
	 */
	public void setDetailView(String detailView) {
		this.detailView = detailView;
	}

	/**
	 * @return the sortItem
	 */
	public SORTITEM getSortItem() {
		return sortItem;
	}

	/**
	 * @param sortItem the sortItem to set
	 */
	public void setSortItem(SORTITEM sortItem) {
		this.sortItem = sortItem;
	}

	/**
	 * @return the sortOrder
	 */
	public SORTORDER getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(SORTORDER sortOrder) {
		this.sortOrder = sortOrder;
	}

}
