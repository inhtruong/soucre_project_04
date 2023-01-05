package net.poweregg.mitsubishi.dto;

import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import net.poweregg.ui.param.WebDBParam;

/**
 * UMB01 Condition
 * 
 * @author diennv
 *
 */
@Named(value = "UMB01Dto")
@ConversationScoped
public class UmitsubishiDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public UmitsubishiDto() {
		super();
	}

	/**
	 * データ行NO
	 */
	private String dataLineNo;

	/**
	 * データNO
	 */
	private String dataNo;

	/**
	 * 状態
	 */
	private String status;

	/**
	 * 売上部門CD
	 */
	private String salesDepartmentCD;

	/**
	 * 受注明細NO
	 */
	private String salesOrderNo;

	/**
	 * 受注NO
	 */
	private String orderNo;

	/**
	 * 得意先CD
	 */
	private String customerCD;

	/**
	 * 得意先名
	 */
	private String customerName;

	/**
	 * 仕向先CD1
	 */
	private String destinationCD1;

	/**
	 * 仕向先名1
	 */
	private String destinationName1;

	/**
	 * 仕向先CD2
	 */
	private String destinationCD2;

	/**
	 * 仕向先名2
	 */
	private String destinationName2;

	/**
	 * 仕向先CD
	 */
	private String destinationCD;

	/**
	 * 納品先名
	 */
	private String deliveryDestinationName;

	/**
	 * 品名略号
	 */
	private String productNameAbbreviation;

	/**
	 * カラーNo
	 */
	private String colorNo;

	/**
	 * グレード1
	 */
	private String grade1;

	/**
	 * ユーザー品目
	 */
	private String userItem;

	/**
	 * 通貨CD
	 */
	private String currencyCD;

	/**
	 * 取引単位CD
	 */
	private String transactionUnitCD;

	/**
	 * 荷姿
	 */
	private String packing;

	/**
	 * 取引先枝番
	 */
	private String clientBranchNumber;

	/**
	 * 価格形態
	 */
	private String priceForm;

	/**
	 * 用途CD
	 */
	private WebDBParam usageCD;

	/**
	 * 納品予定日時
	 */
	private Date deliveryDate;

	/**
	 * 品目分類CD1
	 */
	private String commodityClassificationCD1;

	/**
	 * 受注日
	 */
	private Date orderDate;

	/**
	 * 売上担当者CD
	 */
	private String salespersonCD;

	/**
	 * 売上担当者名
	 */
	private String salespersonName;

	/**
	 * ロット数量
	 */
	private String lotQuantity;

	/**
	 * 末端価格
	 */
	private String retailPrice;

	/**
	 * 小口配送単価
	 */
	private String unitPriceSmallParcel;

	/**
	 * 小口着色単価
	 */
	private String unitPriceForeheadColor;

	/**
	 * 一次店口銭率
	 */
	private String primaryStoreOpenRate;

	/**
	 * 一次店口銭額
	 */
	private String primaryStoreOpenAmount;

	/**
	 * 二次店口銭率
	 */
	private String secondStoreOpenRate;

	/**
	 * 二次店口銭額
	 */
	private String secondStoreOpenAmount;

	/**
	 * 仕切単価(決定値)
	 */
	private String unitPricePartition;

	/**
	 * 改定前単価
	 */
	private String unitPriceBefRevision;

	/**
	 * データ更新区分
	 */
	private String updateCategory;

	/**
	 * 適用開始日
	 */
	private String startDateApplication;

	/**
	 * 契約番号
	 */
	private String contractNumber;

	/**
	 * 伺い理由
	 */
	private String inquiryReason;

	/**
	 * 顧客要求事項確認
	 */
	private String requiredConfirm;

	/**
	 * 新規申請URL
	 */
	private String newApplicationUrl;

	/**
	 * 編集申請URL
	 */
	private String editRequestUrl;

	/**
	 * 廃止申請URL
	 */
	private String deprecationRequestUrl;

	/**
	 * 管理No
	 */
	private String managerNo;

	/**
	 * @return the dataLineNo
	 */
	public String getDataLineNo() {
		return dataLineNo;
	}

	/**
	 * @param dataLineNo the dataLineNo to set
	 */
	public void setDataLineNo(String dataLineNo) {
		this.dataLineNo = dataLineNo;
	}

	/**
	 * @return the dataNo
	 */
	public String getDataNo() {
		return dataNo;
	}

	/**
	 * @param dataNo the dataNo to set
	 */
	public void setDataNo(String dataNo) {
		this.dataNo = dataNo;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the salesDepartmentCD
	 */
	public String getSalesDepartmentCD() {
		return salesDepartmentCD;
	}

	/**
	 * @param salesDepartmentCD the salesDepartmentCD to set
	 */
	public void setSalesDepartmentCD(String salesDepartmentCD) {
		this.salesDepartmentCD = salesDepartmentCD;
	}

	/**
	 * @return the salesOrderNo
	 */
	public String getSalesOrderNo() {
		return salesOrderNo;
	}

	/**
	 * @param salesOrderNo the salesOrderNo to set
	 */
	public void setSalesOrderNo(String salesOrderNo) {
		this.salesOrderNo = salesOrderNo;
	}

	/**
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the customerCD
	 */
	public String getCustomerCD() {
		return customerCD;
	}

	/**
	 * @param customerCD the customerCD to set
	 */
	public void setCustomerCD(String customerCD) {
		this.customerCD = customerCD;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the destinationCD1
	 */
	public String getDestinationCD1() {
		return destinationCD1;
	}

	/**
	 * @param destinationCD1 the destinationCD1 to set
	 */
	public void setDestinationCD1(String destinationCD1) {
		this.destinationCD1 = destinationCD1;
	}

	/**
	 * @return the destinationName1
	 */
	public String getDestinationName1() {
		return destinationName1;
	}

	/**
	 * @param destinationName1 the destinationName1 to set
	 */
	public void setDestinationName1(String destinationName1) {
		this.destinationName1 = destinationName1;
	}

	/**
	 * @return the destinationCD2
	 */
	public String getDestinationCD2() {
		return destinationCD2;
	}

	/**
	 * @param destinationCD2 the destinationCD2 to set
	 */
	public void setDestinationCD2(String destinationCD2) {
		this.destinationCD2 = destinationCD2;
	}

	/**
	 * @return the destinationName2
	 */
	public String getDestinationName2() {
		return destinationName2;
	}

	/**
	 * @param destinationName2 the destinationName2 to set
	 */
	public void setDestinationName2(String destinationName2) {
		this.destinationName2 = destinationName2;
	}

	/**
	 * @return the destinationCD
	 */
	public String getDestinationCD() {
		return destinationCD;
	}

	/**
	 * @param destinationCD the destinationCD to set
	 */
	public void setDestinationCD(String destinationCD) {
		this.destinationCD = destinationCD;
	}

	/**
	 * @return the deliveryDestinationName
	 */
	public String getDeliveryDestinationName() {
		return deliveryDestinationName;
	}

	/**
	 * @param deliveryDestinationName the deliveryDestinationName to set
	 */
	public void setDeliveryDestinationName(String deliveryDestinationName) {
		this.deliveryDestinationName = deliveryDestinationName;
	}

	/**
	 * @return the productNameAbbreviation
	 */
	public String getProductNameAbbreviation() {
		return productNameAbbreviation;
	}

	/**
	 * @param productNameAbbreviation the productNameAbbreviation to set
	 */
	public void setProductNameAbbreviation(String productNameAbbreviation) {
		this.productNameAbbreviation = productNameAbbreviation;
	}

	/**
	 * @return the colorNo
	 */
	public String getColorNo() {
		return colorNo;
	}

	/**
	 * @param colorNo the colorNo to set
	 */
	public void setColorNo(String colorNo) {
		this.colorNo = colorNo;
	}

	/**
	 * @return the grade1
	 */
	public String getGrade1() {
		return grade1;
	}

	/**
	 * @param grade1 the grade1 to set
	 */
	public void setGrade1(String grade1) {
		this.grade1 = grade1;
	}

	/**
	 * @return the userItem
	 */
	public String getUserItem() {
		return userItem;
	}

	/**
	 * @param userItem the userItem to set
	 */
	public void setUserItem(String userItem) {
		this.userItem = userItem;
	}

	/**
	 * @return the currencyCD
	 */
	public String getCurrencyCD() {
		return currencyCD;
	}

	/**
	 * @param currencyCD the currencyCD to set
	 */
	public void setCurrencyCD(String currencyCD) {
		this.currencyCD = currencyCD;
	}

	/**
	 * @return the transactionUnitCD
	 */
	public String getTransactionUnitCD() {
		return transactionUnitCD;
	}

	/**
	 * @param transactionUnitCD the transactionUnitCD to set
	 */
	public void setTransactionUnitCD(String transactionUnitCD) {
		this.transactionUnitCD = transactionUnitCD;
	}

	/**
	 * @return the packing
	 */
	public String getPacking() {
		return packing;
	}

	/**
	 * @param packing the packing to set
	 */
	public void setPacking(String packing) {
		this.packing = packing;
	}

	/**
	 * @return the priceForm
	 */
	public String getPriceForm() {
		return priceForm;
	}

	/**
	 * @param priceForm the priceForm to set
	 */
	public void setPriceForm(String priceForm) {
		this.priceForm = priceForm;
	}

	/**
	 * @return the usageCD
	 */
	public WebDBParam getUsageCD() {
		return usageCD;
	}

	/**
	 * @param usageCD the usageCD to set
	 */
	public void setUsageCD(WebDBParam usageCD) {
		this.usageCD = usageCD;
	}

	/**
	 * @return the deliveryDate
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the commodityClassificationCD1
	 */
	public String getCommodityClassificationCD1() {
		return commodityClassificationCD1;
	}

	/**
	 * @param commodityClassificationCD1 the commodityClassificationCD1 to set
	 */
	public void setCommodityClassificationCD1(String commodityClassificationCD1) {
		this.commodityClassificationCD1 = commodityClassificationCD1;
	}

	/**
	 * @return the orderDate
	 */
	public Date getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the salespersonCD
	 */
	public String getSalespersonCD() {
		return salespersonCD;
	}

	/**
	 * @param salespersonCD the salespersonCD to set
	 */
	public void setSalespersonCD(String salespersonCD) {
		this.salespersonCD = salespersonCD;
	}

	/**
	 * @return the salespersonName
	 */
	public String getSalespersonName() {
		return salespersonName;
	}

	/**
	 * @param salespersonName the salespersonName to set
	 */
	public void setSalespersonName(String salespersonName) {
		this.salespersonName = salespersonName;
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
	public String getRetailPrice() {
		return retailPrice;
	}

	/**
	 * @param retailPrice the retailPrice to set
	 */
	public void setRetailPrice(String retailPrice) {
		this.retailPrice = retailPrice;
	}

	/**
	 * @return the unitPriceSmallParcel
	 */
	public String getUnitPriceSmallParcel() {
		return unitPriceSmallParcel;
	}

	/**
	 * @param unitPriceSmallParcel the unitPriceSmallParcel to set
	 */
	public void setUnitPriceSmallParcel(String unitPriceSmallParcel) {
		this.unitPriceSmallParcel = unitPriceSmallParcel;
	}

	/**
	 * @return the unitPriceForeheadColor
	 */
	public String getUnitPriceForeheadColor() {
		return unitPriceForeheadColor;
	}

	/**
	 * @param unitPriceForeheadColor the unitPriceForeheadColor to set
	 */
	public void setUnitPriceForeheadColor(String unitPriceForeheadColor) {
		this.unitPriceForeheadColor = unitPriceForeheadColor;
	}

	/**
	 * @return the primaryStoreOpenRate
	 */
	public String getPrimaryStoreOpenRate() {
		return primaryStoreOpenRate;
	}

	/**
	 * @param primaryStoreOpenRate the primaryStoreOpenRate to set
	 */
	public void setPrimaryStoreOpenRate(String primaryStoreOpenRate) {
		this.primaryStoreOpenRate = primaryStoreOpenRate;
	}

	/**
	 * @return the primaryStoreOpenAmount
	 */
	public String getPrimaryStoreOpenAmount() {
		return primaryStoreOpenAmount;
	}

	/**
	 * @param primaryStoreOpenAmount the primaryStoreOpenAmount to set
	 */
	public void setPrimaryStoreOpenAmount(String primaryStoreOpenAmount) {
		this.primaryStoreOpenAmount = primaryStoreOpenAmount;
	}

	/**
	 * @return the secondStoreOpenRate
	 */
	public String getSecondStoreOpenRate() {
		return secondStoreOpenRate;
	}

	/**
	 * @param secondStoreOpenRate the secondStoreOpenRate to set
	 */
	public void setSecondStoreOpenRate(String secondStoreOpenRate) {
		this.secondStoreOpenRate = secondStoreOpenRate;
	}

	/**
	 * @return the secondStoreOpenAmount
	 */
	public String getSecondStoreOpenAmount() {
		return secondStoreOpenAmount;
	}

	/**
	 * @param secondStoreOpenAmount the secondStoreOpenAmount to set
	 */
	public void setSecondStoreOpenAmount(String secondStoreOpenAmount) {
		this.secondStoreOpenAmount = secondStoreOpenAmount;
	}

	/**
	 * @return the unitPricePartition
	 */
	public String getUnitPricePartition() {
		return unitPricePartition;
	}

	/**
	 * @param unitPricePartition the unitPricePartition to set
	 */
	public void setUnitPricePartition(String unitPricePartition) {
		this.unitPricePartition = unitPricePartition;
	}

	/**
	 * @return the unitPriceBefRevision
	 */
	public String getUnitPriceBefRevision() {
		return unitPriceBefRevision;
	}

	/**
	 * @param unitPriceBefRevision the unitPriceBefRevision to set
	 */
	public void setUnitPriceBefRevision(String unitPriceBefRevision) {
		this.unitPriceBefRevision = unitPriceBefRevision;
	}

	/**
	 * @return the updateCategory
	 */
	public String getUpdateCategory() {
		return updateCategory;
	}

	/**
	 * @param updateCategory the updateCategory to set
	 */
	public void setUpdateCategory(String updateCategory) {
		this.updateCategory = updateCategory;
	}

	/**
	 * @return the startDateApplication
	 */
	public String getStartDateApplication() {
		return startDateApplication;
	}

	/**
	 * @param startDateApplication the startDateApplication to set
	 */
	public void setStartDateApplication(String startDateApplication) {
		this.startDateApplication = startDateApplication;
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
	 * @return the inquiryReason
	 */
	public String getInquiryReason() {
		return inquiryReason;
	}

	/**
	 * @param inquiryReason the inquiryReason to set
	 */
	public void setInquiryReason(String inquiryReason) {
		this.inquiryReason = inquiryReason;
	}

	/**
	 * @return the requiredConfirm
	 */
	public String getRequiredConfirm() {
		return requiredConfirm;
	}

	/**
	 * @param requiredConfirm the requiredConfirm to set
	 */
	public void setRequiredConfirm(String requiredConfirm) {
		this.requiredConfirm = requiredConfirm;
	}

	/**
	 * @return the newApplicationUrl
	 */
	public String getNewApplicationUrl() {
		return newApplicationUrl;
	}

	/**
	 * @param newApplicationUrl the newApplicationUrl to set
	 */
	public void setNewApplicationUrl(String newApplicationUrl) {
		this.newApplicationUrl = newApplicationUrl;
	}

	/**
	 * @return the editRequestUrl
	 */
	public String getEditRequestUrl() {
		return editRequestUrl;
	}

	/**
	 * @param editRequestUrl the editRequestUrl to set
	 */
	public void setEditRequestUrl(String editRequestUrl) {
		this.editRequestUrl = editRequestUrl;
	}

	/**
	 * @return the deprecationRequestUrl
	 */
	public String getDeprecationRequestUrl() {
		return deprecationRequestUrl;
	}

	/**
	 * @param deprecationRequestUrl the deprecationRequestUrl to set
	 */
	public void setDeprecationRequestUrl(String deprecationRequestUrl) {
		this.deprecationRequestUrl = deprecationRequestUrl;
	}

	/**
	 * @return the managerNo
	 */
	public String getManagerNo() {
		return managerNo;
	}

	/**
	 * @param managerNo the managerNo to set
	 */
	public void setManagerNo(String managerNo) {
		this.managerNo = managerNo;
	}

}
