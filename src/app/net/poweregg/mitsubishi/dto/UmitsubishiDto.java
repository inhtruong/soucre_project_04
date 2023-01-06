package net.poweregg.mitsubishi.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import net.poweregg.webdb.dto.WebDBRefDto;

/**
 * UMB01 Condition 価格情報の送信
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
	private BigDecimal dataLineNo;

	/**
	 * データNO
	 */
	private BigDecimal dataNo;

	/**
	 * 送信元レコード作成日時
	 */
	private Date srcCreateDate;

	/**
	 * データ更新区分
	 */
	private String updateCategory;

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
	 * 仕向先CD1
	 */
	private String destinationCD1;

	/**
	 * 仕向先CD2
	 */
	private String destinationCD2;

	/**
	 * 品名略号
	 */
	private String productNameAbbreviation;

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
	 * カラーNo
	 */
	private String colorNo;

	/**
	 * グレード1
	 */
	private String grade1;

	/**
	 * 適用開始日
	 */
	private String startDateApplication;

	/**
	 * 通貨CD
	 */
	private String currencyCD;

	/**
	 * 取引先枝番
	 */
	private String clientBranchNumber;

	/**
	 * 価格形態
	 */
	private String priceForm;

	/**
	 * 用途参照
	 */
	private WebDBRefDto usageRef;

	/**
	 * 仕切単価(決定値)
	 */
	private BigDecimal unitPricePartition;

	/**
	 * 改定前単価
	 */
	private BigDecimal unitPriceBefRevision;

	/**
	 * 小口配送単価
	 */
	private BigDecimal unitPriceSmallParcel;

	/**
	 * 小口着色単価
	 */
	private BigDecimal unitPriceForeheadColor;

	/**
	 * 末端価格
	 */
	private BigDecimal retailPrice;

	/**
	 * 契約番号
	 */
	private String contractNumber;

	/**
	 * @return the dataLineNo
	 */
	public BigDecimal getDataLineNo() {
		return dataLineNo;
	}

	/**
	 * @param dataLineNo the dataLineNo to set
	 */
	public void setDataLineNo(BigDecimal dataLineNo) {
		this.dataLineNo = dataLineNo;
	}

	/**
	 * @return the dataNo
	 */
	public BigDecimal getDataNo() {
		return dataNo;
	}

	/**
	 * @param dataNo the dataNo to set
	 */
	public void setDataNo(BigDecimal dataNo) {
		this.dataNo = dataNo;
	}

	/**
	 * @return the srcCreateDate
	 */
	public Date getSrcCreateDate() {
		return srcCreateDate;
	}

	/**
	 * @param srcCreateDate the srcCreateDate to set
	 */
	public void setSrcCreateDate(Date srcCreateDate) {
		this.srcCreateDate = srcCreateDate;
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
	 * @return the usageRef
	 */
	public WebDBRefDto getUsageRef() {
		return usageRef;
	}

	/**
	 * @param usageRef the usageRef to set
	 */
	public void setUsageRef(WebDBRefDto usageRef) {
		this.usageRef = usageRef;
	}

	/**
	 * @return the unitPricePartition
	 */
	public BigDecimal getUnitPricePartition() {
		return unitPricePartition;
	}

	/**
	 * @param unitPricePartition the unitPricePartition to set
	 */
	public void setUnitPricePartition(BigDecimal unitPricePartition) {
		this.unitPricePartition = unitPricePartition;
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

}
