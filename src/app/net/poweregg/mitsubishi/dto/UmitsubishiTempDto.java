package net.poweregg.mitsubishi.dto;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import net.poweregg.ui.param.WebDBParam;

/**
 * UMB01 Temp Condition 仮単価情報の受信
 * 
 * @author diennv
 *
 */
@Named(value = "UMB01TempDto")
@ConversationScoped
public class UmitsubishiTempDto implements Serializable {

	private static final long serialVersionUID = 1L;

	public UmitsubishiTempDto() {
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
	 * 送信元レコード作成日時
	 */
	private String srcCreateDate;

	/**
	 * 会社CD
	 */
	private String companyCD;

	/**
	 * 取引CD
	 */
	private String transactionCD;

	/**
	 * 売上部門CD
	 */
	private String salesDepartmentCD;

	/**
	 * 上位部門CD
	 */
	private String upperCategoryCD;

	/**
	 * 会計部門CD
	 */
	private String accountDepartmentCD;

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
	 * 品名分類CD1
	 */
	private String productNameClassCD1;

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
	 * 納品先CD
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
	private String deliveryDate;

	/**
	 * 品目分類CD1
	 */
	private String commodityClassCD1;

	/**
	 * 受注日
	 */
	private String orderDate;

	/**
	 * 登録担当者
	 */
	private String registrar;

	/**
	 * 売上担当者CD
	 */
	private String salespersonCD;

	/**
	 * 売上担当者名
	 */
	private String salespersonName;

	/**
	 * 用途参照
	 */
	private String usageRef;

	/**
	 * @return the productNameClassCD1
	 */
	public String getProductNameClassCD1() {
		return productNameClassCD1;
	}

	/**
	 * @param productNameClassCD1 the productNameClassCD1 to set
	 */
	public void setProductNameClassCD1(String productNameClassCD1) {
		this.productNameClassCD1 = productNameClassCD1;
	}

	/**
	 * @return the companyCD
	 */
	public String getCompanyCD() {
		return companyCD;
	}

	/**
	 * @param companyCD the companyCD to set
	 */
	public void setCompanyCD(String companyCD) {
		this.companyCD = companyCD;
	}

	/**
	 * @return the transactionCD
	 */
	public String getTransactionCD() {
		return transactionCD;
	}

	/**
	 * @param transactionCD the transactionCD to set
	 */
	public void setTransactionCD(String transactionCD) {
		this.transactionCD = transactionCD;
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
	 * @return the upperCategoryCD
	 */
	public String getUpperCategoryCD() {
		return upperCategoryCD;
	}

	/**
	 * @param upperCategoryCD the upperCategoryCD to set
	 */
	public void setUpperCategoryCD(String upperCategoryCD) {
		this.upperCategoryCD = upperCategoryCD;
	}

	/**
	 * @return the accountDepartmentCD
	 */
	public String getAccountDepartmentCD() {
		return accountDepartmentCD;
	}

	/**
	 * @param accountDepartmentCD the accountDepartmentCD to set
	 */
	public void setAccountDepartmentCD(String accountDepartmentCD) {
		this.accountDepartmentCD = accountDepartmentCD;
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
	 * @return the clientBranchNumber
	 */
	public String getClientBranchNumber() {
		return clientBranchNumber;
	}

	/**
	 * @param clientBranchNumber the clientBranchNumber to set
	 */
	public void setClientBranchNumber(String clientBranchNumber) {
		this.clientBranchNumber = clientBranchNumber;
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
	 * @return the commodityClassCD1
	 */
	public String getCommodityClassCD1() {
		return commodityClassCD1;
	}

	/**
	 * @param commodityClassCD1 the commodityClassCD1 to set
	 */
	public void setCommodityClassCD1(String commodityClassCD1) {
		this.commodityClassCD1 = commodityClassCD1;
	}

	/**
	 * @return the registrar
	 */
	public String getRegistrar() {
		return registrar;
	}

	/**
	 * @param registrar the registrar to set
	 */
	public void setRegistrar(String registrar) {
		this.registrar = registrar;
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
	 * @return the usageRef
	 */
	public String getUsageRef() {
		return usageRef;
	}

	/**
	 * @param usageRef the usageRef to set
	 */
	public void setUsageRef(String usageRef) {
		this.usageRef = usageRef;
	}

	/**
	 * @return the srcCreateDate
	 */
	public String getSrcCreateDate() {
		return srcCreateDate;
	}

	/**
	 * @param srcCreateDate the srcCreateDate to set
	 */
	public void setSrcCreateDate(String srcCreateDate) {
		this.srcCreateDate = srcCreateDate;
	}

	/**
	 * @return the deliveryDate
	 */
	public String getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

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

}
