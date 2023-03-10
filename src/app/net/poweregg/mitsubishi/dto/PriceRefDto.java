/**
 * 
 */
package net.poweregg.mitsubishi.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

/**
 * 価格マスタより参照Dto
 * 
 * @author dattdd
 *
 */
@Named(value = "PriceRefDto")
@ConversationScoped
public class PriceRefDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public PriceRefDto() {
		super();
	}
	
	/**
	 * 警告
	 */
	private String warning;
	
	/**
	 * ロット数量
	 */
	private String lotQuantity;

	/**
	 * 末端価格
	 */
	private BigDecimal retailPrice = new BigDecimal("0");

	/**
	 * 小口配送単価
	 */
	private BigDecimal unitPriceSmallParcel = new BigDecimal("0");
	
	/**
	 * 小口着色単価
	 */
	private BigDecimal unitPriceForeheadColor = new BigDecimal("0");
	
	
	/**
	 * 一次店口銭金額
	 */
	private BigDecimal primaryStoreCommissionAmount = new BigDecimal("0");
	
	/**
	 * 一次店口銭率
	 */
	private BigDecimal primaryStoreOpenRate = new BigDecimal("0");
	
	/**
	 * 一次店口銭(金額)
	 */
	private BigDecimal primaryStoreOpenAmount = new BigDecimal("0");
	
	/**
	 * 改定前単価
	 */
	private BigDecimal unitPriceBefRevision;
	
	/**
	 * データ更新区分CD
	 */
	private String dataUpdateCategoryCD;
	
	/**
	 * データ更新区分
	 */
	private String dataUpdateCategory;
	
	/**
	 * 適用開始日
	 */
	private Date applicationStartDate;
	
	/**
	 * 適用終了日
	 */
	private Date applicationEndDate;
	
	/**
	 * 契約番号
	 */
	private String contractNumber;
	
	/**
	 * 伺い理由
	 */
	private String reasonInquiry;
	
	/**
	 * 遡及区分
	 */
	private String retroactiveClassification;
	
	/**
	 * 状態CD
	 */
	private String statusCD;
	
	/**
	 * 申請受付番号
	 */
	private String appRecepNo;
	
	/**
	 * Cancel申請受付番号
	 */
	private String appRecepNoCancel;

	/**
	 * @return the warning
	 */
	public String getWarning() {
		return warning;
	}

	/**
	 * @param warning the warning to set
	 */
	public void setWarning(String warning) {
		this.warning = warning;
	}

	/**
	 * @return the lotQuantity
	 */
	public String getLotQuantity() {
		return lotQuantity;
	}

	/**
	 * @param lotQuantity the lotQuantity to set
	 */
	public void setLotQuantity(String lotQuantity) {
		this.lotQuantity = lotQuantity;
	}

	/**
	 * @return the retailPrice
	 */
	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	/**
	 * @param retailPrice the retailPrice to set
	 */
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	/**
	 * @return the unitPriceSmallParcel
	 */
	public BigDecimal getUnitPriceSmallParcel() {
		return unitPriceSmallParcel;
	}

	/**
	 * @param unitPriceSmallParcel the unitPriceSmallParcel to set
	 */
	public void setUnitPriceSmallParcel(BigDecimal unitPriceSmallParcel) {
		this.unitPriceSmallParcel = unitPriceSmallParcel;
	}

	/**
	 * @return the unitPriceForeheadColor
	 */
	public BigDecimal getUnitPriceForeheadColor() {
		return unitPriceForeheadColor;
	}

	/**
	 * @param unitPriceForeheadColor the unitPriceForeheadColor to set
	 */
	public void setUnitPriceForeheadColor(BigDecimal unitPriceForeheadColor) {
		this.unitPriceForeheadColor = unitPriceForeheadColor;
	}

	/**
	 * @return the primaryStoreCommissionAmount
	 */
	public BigDecimal getPrimaryStoreCommissionAmount() {
		return primaryStoreCommissionAmount;
	}

	/**
	 * @param primaryStoreCommissionAmount the primaryStoreCommissionAmount to set
	 */
	public void setPrimaryStoreCommissionAmount(BigDecimal primaryStoreCommissionAmount) {
		this.primaryStoreCommissionAmount = primaryStoreCommissionAmount;
	}

	/**
	 * @return the primaryStoreOpenRate
	 */
	public BigDecimal getPrimaryStoreOpenRate() {
		return primaryStoreOpenRate;
	}

	/**
	 * @param primaryStoreOpenRate the primaryStoreOpenRate to set
	 */
	public void setPrimaryStoreOpenRate(BigDecimal primaryStoreOpenRate) {
		this.primaryStoreOpenRate = primaryStoreOpenRate;
	}

	/**
	 * @return the dataUpdateCategoryCD
	 */
	public String getDataUpdateCategoryCD() {
		return dataUpdateCategoryCD;
	}

	/**
	 * @param dataUpdateCategoryCD the dataUpdateCategoryCD to set
	 */
	public void setDataUpdateCategoryCD(String dataUpdateCategoryCD) {
		this.dataUpdateCategoryCD = dataUpdateCategoryCD;
	}

	/**
	 * @return the dataUpdateCategory
	 */
	public String getDataUpdateCategory() {
		return dataUpdateCategory;
	}

	/**
	 * @param dataUpdateCategory the dataUpdateCategory to set
	 */
	public void setDataUpdateCategory(String dataUpdateCategory) {
		this.dataUpdateCategory = dataUpdateCategory;
	}

	/**
	 * @return the applicationStartDate
	 */
	public Date getApplicationStartDate() {
		return applicationStartDate;
	}

	/**
	 * @param applicationStartDate the applicationStartDate to set
	 */
	public void setApplicationStartDate(Date applicationStartDate) {
		this.applicationStartDate = applicationStartDate;
	}

	/**
	 * @return the applicationEndDate
	 */
	public Date getApplicationEndDate() {
		return applicationEndDate;
	}

	/**
	 * @param applicationEndDate the applicationEndDate to set
	 */
	public void setApplicationEndDate(Date applicationEndDate) {
		this.applicationEndDate = applicationEndDate;
	}

	/**
	 * @return the contractNumber
	 */
	public String getContractNumber() {
		return contractNumber;
	}

	/**
	 * @param contractNumber the contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
	 * @return the reasonInquiry
	 */
	public String getReasonInquiry() {
		return reasonInquiry;
	}

	/**
	 * @param reasonInquiry the reasonInquiry to set
	 */
	public void setReasonInquiry(String reasonInquiry) {
		this.reasonInquiry = reasonInquiry;
	}

	/**
	 * @return the retroactiveClassification
	 */
	public String getRetroactiveClassification() {
		return retroactiveClassification;
	}

	/**
	 * @param retroactiveClassification the retroactiveClassification to set
	 */
	public void setRetroactiveClassification(String retroactiveClassification) {
		this.retroactiveClassification = retroactiveClassification;
	}

	/**
	 * @return the unitPriceBefRevision
	 */
	public BigDecimal getUnitPriceBefRevision() {
		return unitPriceBefRevision;
	}

	/**
	 * @param unitPriceBefRevision the unitPriceBefRevision to set
	 */
	public void setUnitPriceBefRevision(BigDecimal unitPriceBefRevision) {
		this.unitPriceBefRevision = unitPriceBefRevision;
	}

	/**
	 * @return the statusCD
	 */
	public String getStatusCD() {
		return statusCD;
	}

	/**
	 * @param statusCD the statusCD to set
	 */
	public void setStatusCD(String statusCD) {
		this.statusCD = statusCD;
	}

	/**
	 * @return the appRecepNo
	 */
	public String getAppRecepNo() {
		return appRecepNo;
	}

	/**
	 * @param appRecepNo the appRecepNo to set
	 */
	public void setAppRecepNo(String appRecepNo) {
		this.appRecepNo = appRecepNo;
	}

	/**
	 * @return the appRecepNoCancel
	 */
	public String getAppRecepNoCancel() {
		return appRecepNoCancel;
	}

	/**
	 * @param appRecepNoCancel the appRecepNoCancel to set
	 */
	public void setAppRecepNoCancel(String appRecepNoCancel) {
		this.appRecepNoCancel = appRecepNoCancel;
	}

	/**
	 * @return the primaryStoreOpenAmount
	 */
	public BigDecimal getPrimaryStoreOpenAmount() {
		return primaryStoreOpenAmount;
	}

	/**
	 * @param primaryStoreOpenAmount the primaryStoreOpenAmount to set
	 */
	public void setPrimaryStoreOpenAmount(BigDecimal primaryStoreOpenAmount) {
		this.primaryStoreOpenAmount = primaryStoreOpenAmount;
	}
}
