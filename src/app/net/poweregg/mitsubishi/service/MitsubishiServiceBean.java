package net.poweregg.mitsubishi.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.poweregg.common.ClassificationService;
import net.poweregg.common.entity.ClassInfo;
import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.mitsubishi.constant.MitsubishiConst.COMMON_NO;
import net.poweregg.mitsubishi.dto.UMB01MasterDto;
import net.poweregg.mitsubishi.dto.Umb01Dto;
import net.poweregg.mitsubishi.webdb.utils.LogUtils;
import net.poweregg.mitsubishi.webdb.utils.Operand;
import net.poweregg.mitsubishi.webdb.utils.QueryConj;
import net.poweregg.mitsubishi.webdb.utils.WebDbConstant;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;
import net.poweregg.security.CertificationService;
import net.poweregg.util.DateUtils;
import net.poweregg.util.StringUtils;

@Stateless
public class MitsubishiServiceBean implements MitsubishiService {

	@EJB
	ClassificationService classificationService;

	@EJB
	CertificationService certificationService;

	@PersistenceContext(unitName = "pe4jPU")
	private EntityManager em;

	private Long offset = 0L;
	private Long limit = null;

	/**
	 * 
	 * @param dataNo
	 * @param dbType
	 * @return
	 * @throws Exception
	 */
	@Override
	public Umb01Dto getDataMitsubishi(String dataNo, int dbType) throws Exception {
		WebDbUtils webdbUtils = new WebDbUtils(getInfoWebDb(), 0, dbType);
		JSONArray rsJson = findDataUmbByCondition(webdbUtils, MitsubishiConst.DATA_NO, dataNo);

		// Khong tim duoc thi return
		if (rsJson == null || rsJson.length() == 0) {
			return null;
		}

		Umb01Dto umb01Dto = new Umb01Dto();
		// get data Umb01Dto form JSON
		parseJSONtoUMB01Temp(umb01Dto, rsJson.getJSONObject(0), dbType);

		if (2 == dbType) {
			parseJSONtoUMB01Master(umb01Dto, rsJson.getJSONObject(0), dbType);
		}

		return umb01Dto;
	}

	/**
	 * get table from table master of price
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<UMB01MasterDto> getDataPriceMaster() throws Exception {
		Long offset = 0L;
		Long limit = null;
		// get classInfo by commonNo: UMB01
		List<ClassInfo> webDBClassInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
				COMMON_NO.COMMON_NO_UMB01.getValue());
		List<UMB01MasterDto> umbResults = new ArrayList<>();

		if (webDBClassInfos == null) {
			return null;
		}

		WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0, 1);

		// call API
		JSONObject jsonObj = webdbUtils.getDataFormAPI(null, null, offset, limit);
		// get list json records
		JSONArray resultJson = webdbUtils.getJsonObjectWebDB(jsonObj, offset, limit);

		// Khong tim duoc thi record no return luon
		if (resultJson == null || resultJson.length() == 0) {
			return new ArrayList<>();
		}

		// parse json to dto
		for (int i = 0; i < resultJson.length(); i++) {
			UMB01MasterDto umbTempDto = new UMB01MasterDto();
			JSONObject recordObj = resultJson.getJSONObject(i);
			for (int j = 0; j < recordObj.length(); j++) {
				/** データ行NO */
				umbTempDto.setDataLineNo(WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_LINE_NO));
				/** データNO */
				umbTempDto.setDataNo(WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_NO));
				/** 送信元レコード作成日時 */
				umbTempDto.setSrcCreateDate(
						WebDbUtils.getValue(recordObj, MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME));
				/** データ更新区分 */
				umbTempDto.setUpdateCategory(WebDbUtils.getValue(recordObj, MitsubishiConst.DATE_UPDATE_CATEGORY));
				/** 得意先CD */
				umbTempDto.setCustomerCD(WebDbUtils.getValue(recordObj, MitsubishiConst.CUSTOMER_CD));
				/** 仕向先CD1 */
				umbTempDto.setDestinationCD1(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD1));
				/** 仕向先CD2 */
				umbTempDto.setDestinationCD2(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD2));
				/** 品名略号 */
				umbTempDto.setProductNameAbbreviation(
						WebDbUtils.getValue(recordObj, MitsubishiConst.PRODUCT_NAME_ABBREVIATION));
				/** カラーNo */
				umbTempDto.setColorNo(WebDbUtils.getValue(recordObj, MitsubishiConst.COLOR_NO));
				/** グレード1 */
				umbTempDto.setGrade1(WebDbUtils.getValue(recordObj, MitsubishiConst.GRADE_1));
				/** 適用開始日 */
				umbTempDto.setStartDateApplication(
						WebDbUtils.getValue(recordObj, MitsubishiConst.APPLICATION_START_DATE));
				/** ロット数量 */
				umbTempDto.setLotQuantity(WebDbUtils.getValue(recordObj, MitsubishiConst.LOT_QUANTITY));
				/** 通貨CD */
				umbTempDto.setUpdateCategory(WebDbUtils.getValue(recordObj, MitsubishiConst.DATE_UPDATE_CATEGORY));
				/** 取引先枝番 */
				umbTempDto.setClientBranchNumber(WebDbUtils.getValue(recordObj, MitsubishiConst.CLIENT_BRANCH_NUMBER));
				/** 価格形態 */
				umbTempDto.setPriceForm(WebDbUtils.getValue(recordObj, MitsubishiConst.PRICE_FORM));
				/** 仕切単価 */
				umbTempDto.setUnitPricePartition(WebDbUtils.getValue(recordObj, MitsubishiConst.PARTITION_UNIT_PRICE));
				/** 改定前単価 */
				umbTempDto.setUnitPriceBefRevision(
						WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
				/** 小口配送単価 */
				umbTempDto.setUnitPriceSmallParcel(
						WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
				/** 小口着色単価 */
				umbTempDto.setUnitPriceForeheadColor(
						WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
				/** 末端価格 */
				umbTempDto.setRetailPrice(WebDbUtils.getValue(recordObj, MitsubishiConst.PARTITION_UNIT_PRICE));
				/** 契約番号 */
				umbTempDto.setContractNumber(WebDbUtils.getValue(recordObj, MitsubishiConst.CONTRACT_NUMBER));
				/** 用途参照 */
				umbTempDto.setUsageRef(WebDbUtils.getValue(recordObj, MitsubishiConst.USAGE_REF));

			}
			umbResults.add(umbTempDto);
		}
		return umbResults;
	}

	private void parseJSONtoUMB01Temp(Umb01Dto umb01Dto, JSONObject resultJson, int dbType) throws JSONException {
		// id
		umb01Dto.setId(WebDbUtils.getValue(resultJson, "No"));
		// データ移行NO
		umb01Dto.getPriceUnitRefDto()
				.setDataMigrationNo(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.DATA_LINE_NO));
		// データNO
		umb01Dto.getPriceUnitRefDto().setDataNo(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.DATA_NO));
		// 送信元レコード作成日時
		umb01Dto.getPriceUnitRefDto()
				.setSrcCreateDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME));
		// 会社CD
		umb01Dto.getPriceUnitRefDto().setCompanyCD(WebDbUtils.getValue(resultJson, MitsubishiConst.COMPANY_CD));
		// 取引CD
		umb01Dto.getPriceUnitRefDto().setTransactionCD(WebDbUtils.getValue(resultJson, MitsubishiConst.TRANSACTION_CD));
		// 売上部門CD
		umb01Dto.getPriceUnitRefDto()
				.setSalesDepartmentCD(WebDbUtils.getValue(resultJson, MitsubishiConst.SALES_DEPARTMENT_CD));
		// 上位部門CD
		umb01Dto.getPriceUnitRefDto()
				.setUpperCategoryCD(WebDbUtils.getValue(resultJson, MitsubishiConst.UPPER_CATEGORY_CD));
		// 会計部門CD
		umb01Dto.getPriceUnitRefDto()
				.setAccountDepartmentCD(WebDbUtils.getValue(resultJson, MitsubishiConst.ACCOUNT_DEPARTMENT_CD));
		// 受注NO
		umb01Dto.getPriceUnitRefDto().setOrderNo(WebDbUtils.getValue(resultJson, MitsubishiConst.ORDER_NO));
		// 受注明細NO
		umb01Dto.getPriceUnitRefDto().setSalesOrderNo(WebDbUtils.getValue(resultJson, MitsubishiConst.SALES_ORDER_NO));
		// 得意先CD
		umb01Dto.getPriceUnitRefDto().setCustomerCD(WebDbUtils.getValue(resultJson, MitsubishiConst.CUSTOMER_CD));
		// 得意先名
		umb01Dto.getPriceUnitRefDto().setCustomerName(WebDbUtils.getValue(resultJson, MitsubishiConst.CUSTOMER_NAME));
		// 仕向先CD1
		umb01Dto.getPriceUnitRefDto()
				.setDestinationCD1(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_CD1));
		// 仕向先名1
		umb01Dto.getPriceUnitRefDto()
				.setDestinationName1(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_NAME1));
		// 仕向先CD2
		umb01Dto.getPriceUnitRefDto()
				.setDestinationCD2(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_CD2));
		// 仕向先名2
		umb01Dto.getPriceUnitRefDto()
				.setDestinationName2(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_NAME2));
		// 納品先CD
		umb01Dto.getPriceUnitRefDto()
				.setDeliveryDestinationCD(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_CD));
		// 納品先名
		umb01Dto.getPriceUnitRefDto()
				.setDeliveryDestinationName(WebDbUtils.getValue(resultJson, MitsubishiConst.DELIVERY_DESTINATION_NAME));
		// 品名略号
		umb01Dto.getPriceUnitRefDto()
				.setProductNameAbbreviation(WebDbUtils.getValue(resultJson, MitsubishiConst.PRODUCT_NAME_ABBREVIATION));
		// カラーNO
		umb01Dto.getPriceUnitRefDto().setColorNo(WebDbUtils.getValue(resultJson, MitsubishiConst.COLOR_NO));
		// グレード1
		umb01Dto.getPriceUnitRefDto().setGrade1(WebDbUtils.getValue(resultJson, MitsubishiConst.GRADE_1));
		// ユーザー品目
		umb01Dto.getPriceUnitRefDto().setUserItem(WebDbUtils.getValue(resultJson, MitsubishiConst.USER_ITEM));
		// 通貨CD
		umb01Dto.getPriceUnitRefDto().setCurrencyCD(WebDbUtils.getValue(resultJson, MitsubishiConst.CURRENCY_CD));
		// 取引単位CD
		umb01Dto.getPriceUnitRefDto()
				.setTransactionUnitCD(WebDbUtils.getValue(resultJson, MitsubishiConst.TRANSACTION_UNIT_CD));
		// 荷姿
		umb01Dto.getPriceUnitRefDto().setPacking(WebDbUtils.getValue(resultJson, MitsubishiConst.PACKING));
		// 取引先枝番
		umb01Dto.getPriceUnitRefDto()
				.setClientBranchNumber(WebDbUtils.getValue(resultJson, MitsubishiConst.CLIENT_BRANCH_NUMBER));
		// 価格形態
		umb01Dto.getPriceUnitRefDto().setPriceForm(WebDbUtils.getValue(resultJson, MitsubishiConst.PRICE_FORM));
		// 用途参照
		umb01Dto.getPriceUnitRefDto().setUsageCD(WebDbUtils.getValue(resultJson, MitsubishiConst.USAGE_REF));
		// 納品予定日時
		umb01Dto.getPriceUnitRefDto()
				.setDeliveryDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.SCHEDULED_DELIVERY_DATE));
		// 品名分類CD1
		umb01Dto.getPriceUnitRefDto()
				.setCommodityClassificationCD1(WebDbUtils.getValue(resultJson, MitsubishiConst.PRODUCT_NAME_CLASS_CD1));
		// 受注日
		umb01Dto.getPriceUnitRefDto().setOrderDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.ORDER_DATE));
		// 登録担当者
		umb01Dto.getPriceUnitRefDto().setRegistrar(WebDbUtils.getValue(resultJson, MitsubishiConst.REGISTRAR));

		if (1 == dbType) {
			umb01Dto.getPriceUnitRefDto().setAppRecepNo(WebDbUtils.getValue(resultJson, MitsubishiConst.APPLICATION_REC_NO));
			umb01Dto.getPriceUnitRefDto().setStatusCD(WebDbUtils.getValue(resultJson, MitsubishiConst.STATUS_CD));
		}

	}

	private void parseJSONtoUMB01Master(Umb01Dto umb01Dto, JSONObject resultJson, int dbType) throws JSONException {
		// 警告
		umb01Dto.getPriceRefDto().setWarning(WebDbUtils.getValue(resultJson, MitsubishiConst.WARNING));
		// ロット数量
		umb01Dto.getPriceRefDto()
				.setLotQuantity(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.LOT_QUANTITY));
		// ロット数量
		umb01Dto.getPriceRefDto()
				.setRetailPrice(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.RETAIL_PRICE));
		// 小口配送単価
		umb01Dto.getPriceRefDto().setUnitPriceSmallParcel(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
		// 小口着色単価
		umb01Dto.getPriceRefDto().setUnitPriceForeheadColor(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
		// 一次店口銭金額
		umb01Dto.getPriceRefDto().setPrimaryStoreCommissionAmount(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.PRIMARY_STORE_COMMISSION_AMOUNT));
		// 一次店口銭率
		umb01Dto.getPriceRefDto().setPrimaryStoreOpenRate(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.PRIMARY_STORE_OPEN_RATE));
		// 改定前単価
		umb01Dto.getPriceRefDto().setUnitPriceBefRevision(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
		// データ更新区分CD
		umb01Dto.getPriceRefDto()
				.setDataUpdateCategoryCD(WebDbUtils.getValue(resultJson, MitsubishiConst.DATA_UPDATE_CATEGORY_CD));
		// データ更新区分
		umb01Dto.getPriceRefDto()
				.setDataUpdateCategory(WebDbUtils.getValue(resultJson, MitsubishiConst.DATE_UPDATE_CATEGORY));
		// 適用開始日
		umb01Dto.getPriceRefDto()
				.setApplicationStartDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.APPLICATION_START_DATE));
		// 適用終了日
		umb01Dto.getPriceRefDto()
				.setApplicationEndDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.APPLICATION_END_DATE));
		// 契約番号
		umb01Dto.getPriceRefDto().setContractNumber(WebDbUtils.getValue(resultJson, MitsubishiConst.CONTRACT_NUMBER));
		// 伺い理由
		umb01Dto.getPriceRefDto().setReasonInquiry(WebDbUtils.getValue(resultJson, MitsubishiConst.REASON_INQUIRY));
		// 遡及区分
		umb01Dto.getPriceRefDto().setRetroactiveClassification(
				WebDbUtils.getValue(resultJson, MitsubishiConst.RETROACTIVE_CLASSIFICATION));
		
		umb01Dto.getPriceRefDto().setAppRecepNo(WebDbUtils.getValue(resultJson, MitsubishiConst.APPLICATION_REC_NO));
		
		umb01Dto.getPriceRefDto().setAppRecepNoCancel(WebDbUtils.getValue(resultJson, MitsubishiConst.CANCEL_APPLICATION_REC_NO));

		if (2 == dbType) {
			umb01Dto.getPriceRefDto().setStatusCD(WebDbUtils.getValue(resultJson, MitsubishiConst.STATUS_CD));
		}
	}

	/**
	 * create XML table price
	 * 
	 * @return
	 */
	@Override
	public String createXMLTablePrice(Umb01Dto param) {
		// XML作成部分
		String xmlString = new String();

		xmlString = "";
		String CR_LF = System.getProperties().getProperty("line.separator");
		xmlString += "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + CR_LF;
		xmlString += "<U_MITSUBISHI>" + CR_LF;
		xmlString += "<TB_DEFAULT>" + CR_LF;

		xmlString += "<UNITPRICEDATAREF>"
//				+ StringUtils.toEmpty(param.getUnitPriceDataRef())
				+ "UNITPRICEDATAREF" + "</UNITPRICEDATAREF>" + CR_LF;

		xmlString += "<PRICEDATAREF>"
//				+ StringUtils.toEmpty(param.getPriceDataRef())
				+ "PRICEDATAREF" + "</PRICEDATAREF>" + CR_LF;

		xmlString += "<DATAMIGRATIONNO>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getDataMigrationNo())
				+ "</DATAMIGRATIONNO>" + CR_LF;

		xmlString += "<DATANO>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getDataNo()) + "</DATANO>" + CR_LF;

		xmlString += "<SRCCREATEDATE>" + StringUtils.formatDate(param.getPriceUnitRefDto().getSrcCreateDate())
				+ "</SRCCREATEDATE>" + CR_LF;

		xmlString += "<COMPANYCD>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getCompanyCD()) + "</COMPANYCD>"
				+ CR_LF;

		xmlString += "<TRANSACTIONCD>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getTransactionCD())
				+ "</TRANSACTIONCD>" + CR_LF;

		xmlString += "<SALESDEPARTMENTCD>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getSalesDepartmentCD())
				+ "</SALESDEPARTMENTCD>" + CR_LF;

		xmlString += "<UPPERCATEGORYCD>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getUpperCategoryCD())
				+ "</UPPERCATEGORYCD>" + CR_LF;

		xmlString += "<ACCOUNTDEPARTMENTCD>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getAccountDepartmentCD())
				+ "</ACCOUNTDEPARTMENTCD>" + CR_LF;

		xmlString += "<ORDERNO>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getOrderNo()) + "</ORDERNO>" + CR_LF;

		xmlString += "<SALESORDERNO>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getSalesOrderNo())
				+ "</SALESORDERNO>" + CR_LF;

		xmlString += "<CUSTOMERCD>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getCustomerCD()) + "</CUSTOMERCD>"
				+ CR_LF;

		xmlString += "<CUSTOMERNAME>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getCustomerName())
				+ "</CUSTOMERNAME>" + CR_LF;

		xmlString += "<DESTINATIONCD1>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getDestinationCD1())
				+ "</DESTINATIONCD1>" + CR_LF;

		xmlString += "<DESTINATIONNAME1>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getDestinationName1())
				+ "</DESTINATIONNAME1>" + CR_LF;

		xmlString += "<DESTINATIONCD2>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getDestinationCD2())
				+ "</DESTINATIONCD2>" + CR_LF;

		xmlString += "<DESTINATIONNAME2>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getDestinationName2())
				+ "</DESTINATIONNAME2>" + CR_LF;

		xmlString += "<DELIVERYDESTINATIONCD>"
				+ StringUtils.toEmpty(param.getPriceUnitRefDto().getDeliveryDestinationCD())
				+ "</DELIVERYDESTINATIONCD>" + CR_LF;

		xmlString += "<DELIVERYDESTINATIONNAME>"
				+ StringUtils.toEmpty(param.getPriceUnitRefDto().getDeliveryDestinationName())
				+ "</DELIVERYDESTINATIONNAME>" + CR_LF;

		xmlString += "<PRODUCTNAMEABBREVIATION>"
				+ StringUtils.toEmpty(param.getPriceUnitRefDto().getProductNameAbbreviation())
				+ "</PRODUCTNAMEABBREVIATION>" + CR_LF;

		xmlString += "<COLORNO>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getColorNo()) + "</COLORNO>" + CR_LF;

		xmlString += "<GRADE1>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getGrade1()) + "</GRADE1>" + CR_LF;

		xmlString += "<USERITEM>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getUserItem()) + "</USERITEM>"
				+ CR_LF;

		xmlString += "<CURRENCYCD>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getCurrencyCD()) + "</CURRENCYCD>"
				+ CR_LF;

		xmlString += "<TRANSACTIONUNITCD>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getTransactionUnitCD())
				+ "</TRANSACTIONUNITCD>" + CR_LF;

		xmlString += "<PACKING>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getPacking()) + "</PACKING>" + CR_LF;

		xmlString += "<CLIENTBRANCHNUMBER>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getClientBranchNumber())
				+ "</CLIENTBRANCHNUMBER>" + CR_LF;

		xmlString += "<PRICEFORM>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getPriceForm()) + "</PRICEFORM>"
				+ CR_LF;

		xmlString += "<USAGECD>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getUsageCD()) + "</USAGECD>" + CR_LF;

		xmlString += "<USAGEREF>"
//				+ StringUtils.toEmpty(param.getPriceUnitRefDto().getUsageRef())
				+ "USAGEREF" + "</USAGEREF>" + CR_LF;

		xmlString += "<DELIVERYDATE>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getDeliveryDate())
				+ "</DELIVERYDATE>" + CR_LF;

		xmlString += "<COMMODITYCLASSIFICATIONCD1>"
				+ StringUtils.toEmpty(param.getPriceUnitRefDto().getCommodityClassificationCD1())
				+ "</COMMODITYCLASSIFICATIONCD1>" + CR_LF;

		xmlString += "<ORDERDATE>" + StringUtils.formatDate(param.getPriceUnitRefDto().getOrderDate()) + "</ORDERDATE>"
				+ CR_LF;

		xmlString += "<REGISTRAR>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getRegistrar()) + "</REGISTRAR>"
				+ CR_LF;

		xmlString += "<REGISTRAR>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getRegistrar()) + "</REGISTRAR>"
				+ CR_LF;

		xmlString += "<WARNING>" + StringUtils.toEmpty(param.getPriceRefDto().getWarning()) + "</WARNING>" + CR_LF;

		xmlString += "<LOTQUANTITY>" + StringUtils.toEmpty(param.getPriceRefDto().getLotQuantity()) + "</LOTQUANTITY>"
				+ CR_LF;

		xmlString += "<RETAILPRICE>" + StringUtils.toEmpty(param.getPriceRefDto().getRetailPrice()) + "</RETAILPRICE>"
				+ CR_LF;

		xmlString += "<UNITPRICESMALLPARCEL>" + StringUtils.toEmpty(param.getPriceRefDto().getUnitPriceSmallParcel())
				+ "</UNITPRICESMALLPARCEL>" + CR_LF;

		xmlString += "<UNITPRICEFOREHEADCOLOR>"
				+ StringUtils.toEmpty(param.getPriceRefDto().getUnitPriceForeheadColor()) + "</UNITPRICEFOREHEADCOLOR>"
				+ CR_LF;

		xmlString += "<PRIMARYSTORECOMMISSIONAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceRefDto().getPrimaryStoreCommissionAmount())
				+ "</PRIMARYSTORECOMMISSIONAMOUNT>" + CR_LF;

		xmlString += "<PRIMARYSTOREOPENRATE>" + StringUtils.toEmpty(param.getPriceRefDto().getPrimaryStoreOpenRate())
				+ "</PRIMARYSTOREOPENRATE>" + CR_LF;

		xmlString += "<UNITPRICEBEFREVISION>" + StringUtils.toEmpty(param.getPriceRefDto().getUnitPriceBefRevision())
				+ "</UNITPRICEBEFREVISION>" + CR_LF;

		xmlString += "<DATAUPDATECATEGORYCD>" + StringUtils.toEmpty(param.getPriceRefDto().getDataUpdateCategoryCD())
				+ "</DATAUPDATECATEGORYCD>" + CR_LF;

		xmlString += "<DATAUPDATECATEGORY>" + StringUtils.toEmpty(param.getPriceRefDto().getDataUpdateCategory())
				+ "</DATAUPDATECATEGORY>" + CR_LF;

		xmlString += "<APPLICATIONSTARTDATE>" + StringUtils.formatDate(param.getPriceRefDto().getApplicationStartDate())
				+ "</APPLICATIONSTARTDATE>" + CR_LF;

		xmlString += "<APPLICATIONENDDATE>" + StringUtils.formatDate(param.getPriceRefDto().getApplicationEndDate())
				+ "</APPLICATIONENDDATE>" + CR_LF;

		xmlString += "<CONTRACTNUMBER>" + StringUtils.toEmpty(param.getPriceRefDto().getContractNumber())
				+ "</CONTRACTNUMBER>" + CR_LF;

		xmlString += "<REASONINQUIRY>" + StringUtils.toEmpty(param.getPriceRefDto().getReasonInquiry())
				+ "</REASONINQUIRY>" + CR_LF;

		xmlString += "<RETROACTIVECLASSIFICATION>"
				+ StringUtils.toEmpty(param.getPriceRefDto().getRetroactiveClassification())
				+ "</RETROACTIVECLASSIFICATION>" + CR_LF;

		xmlString += "<NOPRERETAILPRICE1>" + StringUtils.toEmpty(param.getPriceCalParam().getNoPreRetailPrice1())
				+ "</NOPRERETAILPRICE1>" + CR_LF;

		xmlString += "<NOPRERETAILPRICE2>" + StringUtils.toEmpty(param.getPriceCalParam().getNoPreRetailPrice2())
				+ "</NOPRERETAILPRICE2>" + CR_LF;

		xmlString += "<DELIRETAILPRICE1>" + StringUtils.toEmpty(param.getPriceCalParam().getDeliRetailPrice1())
				+ "</DELIRETAILPRICE1>" + CR_LF;

		xmlString += "<DELIRETAILPRICE2>" + StringUtils.toEmpty(param.getPriceCalParam().getNoPreRetailPrice2())
				+ "</DELIRETAILPRICE2>" + CR_LF;

		xmlString += "<LARGERETAILPRICE1>" + StringUtils.toEmpty(param.getPriceCalParam().getLargeRetailPrice1())
				+ "</LARGERETAILPRICE1>" + CR_LF;

		xmlString += "<LARGERETAILPRICE2>" + StringUtils.toEmpty(param.getPriceCalParam().getLargeRetailPrice2())
				+ "</LARGERETAILPRICE2>" + CR_LF;

		xmlString += "<SMALLRETAILPRICE1>" + StringUtils.toEmpty(param.getPriceCalParam().getSmallRetailPrice1())
				+ "</SMALLRETAILPRICE1>" + CR_LF;

		xmlString += "<SMALLRETAILPRICE1>" + StringUtils.toEmpty(param.getPriceCalParam().getLargeRetailPrice2())
				+ "</SMALLRETAILPRICE1>" + CR_LF;

		xmlString += "<DELIUNITPRICEPARCEL1>" + StringUtils.toEmpty(param.getPriceCalParam().getDeliUnitPriceParcel1())
				+ "</DELIUNITPRICEPARCEL1>" + CR_LF;

		xmlString += "<DELIUNITPRICEPARCEL2>" + StringUtils.toEmpty(param.getPriceCalParam().getDeliUnitPriceParcel2())
				+ "</DELIUNITPRICEPARCEL2>" + CR_LF;

		xmlString += "<SMALLUNITPRICEPARCEL1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallUnitPriceParcel1()) + "</SMALLUNITPRICEPARCEL1>"
				+ CR_LF;

		xmlString += "<SMALLUNITPRICEPARCEL2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallUnitPriceParcel2()) + "</SMALLUNITPRICEPARCEL2>"
				+ CR_LF;

		xmlString += "<LARGEUNITPRICEFOREHEAD1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargeUnitPriceForehead1())
				+ "</LARGEUNITPRICEFOREHEAD1>" + CR_LF;

		xmlString += "<LARGEUNITPRICEFOREHEAD2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargeUnitPriceForehead2())
				+ "</LARGEUNITPRICEFOREHEAD2>" + CR_LF;

		xmlString += "<SMALLUNITPRICEFOREHEAD1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallUnitPriceForehead1())
				+ "</SMALLUNITPRICEFOREHEAD1>" + CR_LF;

		xmlString += "<SMALLUNITPRICEFOREHEAD2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallUnitPriceForehead2())
				+ "</SMALLUNITPRICEFOREHEAD2>" + CR_LF;

		xmlString += "<NOPRETOTALRETAILPRICE1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getNoPreTotalRetailPrice1())
				+ "</NOPRETOTALRETAILPRICE1>" + CR_LF;

		xmlString += "<NOPRETOTALRETAILPRICE2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getNoPreTotalRetailPrice2())
				+ "</NOPRETOTALRETAILPRICE2>" + CR_LF;

		xmlString += "<DELITOTALRETAILPRICE1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getDeliTotalRetailPrice1()) + "</DELITOTALRETAILPRICE1>"
				+ CR_LF;

		xmlString += "<DELITOTALRETAILPRICE2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getDeliTotalRetailPrice2()) + "</DELITOTALRETAILPRICE2>"
				+ CR_LF;

		xmlString += "<LARGETOTALRETAILPRICE1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargeTotalRetailPrice1())
				+ "</LARGETOTALRETAILPRICE1>" + CR_LF;

		xmlString += "<LARGETOTALRETAILPRICE2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargeTotalRetailPrice1())
				+ "</LARGETOTALRETAILPRICE2>" + CR_LF;

		xmlString += "<SMALLTOTALRETAILPRICE1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallTotalRetailPrice1())
				+ "</SMALLTOTALRETAILPRICE1>" + CR_LF;

		xmlString += "<SMALLTOTALRETAILPRICE2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallTotalRetailPrice2())
				+ "</SMALLTOTALRETAILPRICE2>" + CR_LF;

		xmlString += "<NOPREPRIMARYOPENAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getNoPrePrimaryOpenAmount())
				+ "</NOPREPRIMARYOPENAMOUNT>" + CR_LF;

		xmlString += "<DELIPRIMARYOPENAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getDeliPrimaryOpenAmount()) + "</DELIPRIMARYOPENAMOUNT>"
				+ CR_LF;

		xmlString += "<LARGEPRIMARYOPENAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargePrimaryOpenAmount())
				+ "</LARGEPRIMARYOPENAMOUNT>" + CR_LF;

		xmlString += "<SMALLPRIMARYOPENAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallPrimaryOpenAmount())
				+ "</SMALLPRIMARYOPENAMOUNT>" + CR_LF;

		xmlString += "<NOPREPRIMARYOPENRATE>" + StringUtils.toEmpty(param.getPriceCalParam().getNoPrePrimaryOpenRate())
				+ "</NOPREPRIMARYOPENRATE>" + CR_LF;

		xmlString += "<DELIPRIMARYOPENRATE>" + StringUtils.toEmpty(param.getPriceCalParam().getDeliPrimaryOpenRate())
				+ "</DELIPRIMARYOPENRATE>" + CR_LF;

		xmlString += "<LARGEPRIMARYOPENRATE>" + StringUtils.toEmpty(param.getPriceCalParam().getLargePrimaryOpenRate())
				+ "</LARGEPRIMARYOPENRATE>" + CR_LF;

		xmlString += "<SMALLPRIMARYOPENRATE>" + StringUtils.toEmpty(param.getPriceCalParam().getSmallPrimaryOpenRate())
				+ "</SMALLPRIMARYOPENRATE>" + CR_LF;

		xmlString += "<NOPREPRIMARYCOMMISAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getNoPrePrimaryCommisAmount())
				+ "</NOPREPRIMARYCOMMISAMOUNT>" + CR_LF;

		xmlString += "<DELIPRIMARYCOMMISAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getDeliPrimaryCommisAmount())
				+ "</DELIPRIMARYCOMMISAMOUNT>" + CR_LF;

		xmlString += "<LARGEPRIMARYCOMMISAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargePrimaryCommisAmount())
				+ "</LARGEPRIMARYCOMMISAMOUNT>" + CR_LF;

		xmlString += "<SMALLPRIMARYCOMMISAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallPrimaryCommisAmount())
				+ "</SMALLPRIMARYCOMMISAMOUNT>" + CR_LF;

		xmlString += "<SECONDSTOREOPENRATE>" + StringUtils.toEmpty(param.getSecondStoreOpenRate())
				+ "</SECONDSTOREOPENRATE>" + CR_LF;

		xmlString += "<SECONDSTOREOPENAMOUNT>" + StringUtils.toEmpty(param.getSecondStoreOpenAmount())
				+ "</SECONDSTOREOPENAMOUNT>" + CR_LF;

		xmlString += "<NOPREPARTITIONUNITPRICE1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getNoPrePartitionUnitPrice1())
				+ "</NOPREPARTITIONUNITPRICE1>" + CR_LF;

		xmlString += "<NOPREPARTITIONUNITPRICE2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getNoPrePartitionUnitPrice2())
				+ "</NOPREPARTITIONUNITPRICE2>" + CR_LF;

		xmlString += "<DELIPARTITIONUNITPRICE1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getDeliPartitionUnitPrice1())
				+ "</DELIPARTITIONUNITPRICE1>" + CR_LF;

		xmlString += "<DELIPARTITIONUNITPRICE2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getDeliPartitionUnitPrice2())
				+ "</DELIPARTITIONUNITPRICE2>" + CR_LF;

		xmlString += "<LARGEPARTITIONUNITPRICE1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargePartitionUnitPrice1())
				+ "</LARGEPARTITIONUNITPRICE1>" + CR_LF;

		xmlString += "<LARGEPARTITIONUNITPRICE2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargePartitionUnitPrice2())
				+ "</LARGEPARTITIONUNITPRICE2>" + CR_LF;

		xmlString += "<SMALLPARTITIONUNITPRICE1>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallPartitionUnitPrice1())
				+ "</SMALLPARTITIONUNITPRICE1>" + CR_LF;

		xmlString += "<SMALLPARTITIONUNITPRICE2>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallPartitionUnitPrice2())
				+ "</SMALLPARTITIONUNITPRICE2>" + CR_LF;

		xmlString += "</TB_DEFAULT>" + CR_LF;
		xmlString += "</U_MITSUBISHI>" + CR_LF;

		return xmlString;

	}

	/**
	 * Update Record DB Temp
	 * 
	 * @param logFileFullPath
	 * @param appRecepNo
	 * @param statusCd
	 * @return
	 * @throws Exception
	 */
	@Override
	public void updateRecordDbPrice(String logFileFullPath, Umb01Dto umb01Dto, int dbType, String mode)
			throws Exception {
		WebDbUtils webdbUtils = new WebDbUtils(getInfoWebDb(), 0, dbType);
		String dataNo = StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDataNo());
		JSONArray rsJson = findDataUmbByCondition(webdbUtils, MitsubishiConst.DATA_NO, dataNo);

		// Khong tim duoc thi record no return luon
		if (rsJson == null || rsJson.length() == 0) {
			LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "Error:" + dataNo + "は存在しません");
			throw new Exception("Error: " + MitsubishiConst.DATA_NO + " " + dataNo + " は存在しません");
		}

		// chuan bi du lieu jsonobject
		JSONObject queryBlocks = new JSONObject();
		queryBlocks = createJsonQuery(umb01Dto, mode);
		
		// update thông qua rest api
		webdbUtils.putJsonObject(queryBlocks, umb01Dto.getId(), false, false, false);
	}

	/**
	 * Find Data UMB By Condition
	 * 
	 * @param webdbUtils
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONArray findDataUmbByCondition(WebDbUtils webdbUtils, String field, String value) throws Exception {

		JSONArray condOr = new JSONArray();
		condOr.put(WebDbUtils.createConditionQuery(field, Operand.EQUALS, value));
		JSONArray queryBlocks = new JSONArray();
		queryBlocks.put(WebDbUtils.createConditionBlock(QueryConj.AND, QueryConj.AND, condOr));

		// call API
		JSONObject tempJson = webdbUtils.getDataFormAPI(queryBlocks.toString(), null, offset, limit);
		JSONArray rsJson = webdbUtils.getJsonObjectWebDB(tempJson, offset, limit);

		return rsJson;
	}

	/**
	 * Get Info Web DB
	 * 
	 * @return
	 */
	@Override
	public List<ClassInfo> getInfoWebDb() {
		// get classInfo by commonNo: UMB01
		List<ClassInfo> webDBClassInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
				MitsubishiConst.COMMON_NO.COMMON_NO_UMB01.getValue());

		return webDBClassInfos;
	}
	
	/**
	 * create object query
	 * 
	 * @param recordObj
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	private JSONObject createJsonQuery(Umb01Dto umb01Dto, String mode)
			throws JSONException, Exception {

		JSONObject queryBlocks = new JSONObject();
		// mode new
		if (MitsubishiConst.MODE_NEW.equals(mode)) {
			queryBlocks.put(MitsubishiConst.APPLICATION_REC_NO, WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getAppRecepNo()));
			queryBlocks.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getStatusCD()));
		}
		// mode edit
		if (MitsubishiConst.MODE_EDIT.equals(mode)) {
			queryBlocks.put(MitsubishiConst.APPLICATION_REC_NO, WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getAppRecepNo()));
			queryBlocks.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getStatusCD()));
		}
		// mode edit
		if (MitsubishiConst.MODE_CANCEL.equals(mode)) {
			queryBlocks.put(MitsubishiConst.APPLICATION_REC_NO, WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getAppRecepNoCancel()));
			queryBlocks.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getStatusCD()));
		}
		/** 送信元レコード作成日時 */
		queryBlocks.put(MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME,
				WebDbUtils.createRecordItem(DateUtils.convertDateTime(umb01Dto.getPriceUnitRefDto().getSrcCreateDate(), "yyyyMMdd")));
		/** 会社CD */
		queryBlocks.put(MitsubishiConst.COMPANY_CD,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getCompanyCD()));
		/** 取引CD */
		queryBlocks.put(MitsubishiConst.TRANSACTION_CD,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getTransactionCD()));
		/** 売上部門CD */
		queryBlocks.put(MitsubishiConst.SALES_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getSalesDepartmentCD()));
		/** 上位部門CD */
		queryBlocks.put(MitsubishiConst.UPPER_CATEGORY_CD,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getUpperCategoryCD()));
		/** 会計部門CD */
		queryBlocks.put(MitsubishiConst.ACCOUNT_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getAccountDepartmentCD()));
		/** 受注NO */
		queryBlocks.put(MitsubishiConst.ORDER_NO,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getOrderNo()));
		/** 受注明細NO */
		queryBlocks.put(MitsubishiConst.SALES_ORDER_NO,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getSalesOrderNo()));
		/** 得意先CD */
		queryBlocks.put(MitsubishiConst.CUSTOMER_CD,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getCustomerCD()));
		/** 得意先名 */
		queryBlocks.put(MitsubishiConst.CUSTOMER_NAME,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getCustomerName()));
		/** 仕向先CD1 */
		queryBlocks.put(MitsubishiConst.DESTINATION_CD1,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getDestinationCD1()));
		/** 仕向先名1 */
		queryBlocks.put(MitsubishiConst.DESTINATION_NAME1,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getDestinationName1()));
		/** 仕向先CD2 */
		queryBlocks.put(MitsubishiConst.DESTINATION_CD2,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getDestinationCD2()));
		/** 仕向先名2 */
		queryBlocks.put(MitsubishiConst.DESTINATION_NAME2,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getDestinationName2()));
		/** 納品先CD */
		queryBlocks.put(MitsubishiConst.DESTINATION_CD,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getDeliveryDestinationCD()));
		/** 納品先名 */
		queryBlocks.put(MitsubishiConst.DELIVERY_DESTINATION_NAME,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getDeliveryDestinationName()));
		/** 品名略号 */
		queryBlocks.put(MitsubishiConst.PRODUCT_NAME_ABBREVIATION,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getProductNameAbbreviation()));
		/** カラーNO */
		queryBlocks.put(MitsubishiConst.COLOR_NO,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getColorNo()));
		/** グレード1 */
		queryBlocks.put(MitsubishiConst.GRADE_1,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getGrade1()));
//		/** ユーザー品目 */
//		queryBlocks.put(MitsubishiConst.USER_PRODUCT_NAME,
//				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getUserItem()));
		/** 通貨CD */
		queryBlocks.put(MitsubishiConst.CURRENCY_CD,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getCurrencyCD()));
		/** 取引単位CD */
		queryBlocks.put(MitsubishiConst.TRANSACTION_UNIT_CD,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getTransactionUnitCD()));
		/** 荷姿 */
		queryBlocks.put(MitsubishiConst.PACKING,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getPacking()));
		/** 取引先枝番 */
		queryBlocks.put(MitsubishiConst.CLIENT_BRANCH_NUMBER,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getClientBranchNumber()));
		/** 価格形態 */
		queryBlocks.put(MitsubishiConst.PRICE_FORM,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getPriceForm()));
//		/** 用途参照 */
//		queryBlocks.put(MitsubishiConst.USAGE_REF,
//				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getUsageRef())));
		/** 納品予定日時 */
		queryBlocks.put(MitsubishiConst.SCHEDULED_DELIVERY_DATE,
				WebDbUtils.createRecordItem(DateUtils.convertDateTime(umb01Dto.getPriceUnitRefDto().getDeliveryDate(), "yyyyMMdd")));
		/** 受注日 */
		queryBlocks.put(MitsubishiConst.ORDER_DATE,
				WebDbUtils.createRecordItem(DateUtils.convertDateTime(umb01Dto.getPriceUnitRefDto().getOrderDate(), "yyyyMMdd")));
		/** 登録担当者 */
		queryBlocks.put(MitsubishiConst.REGISTRAR,
				WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getRegistrar()));
		/** データ移行NO */
		queryBlocks.put(MitsubishiConst.DATA_LINE_NO,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDataMigrationNo())));
		/** データNO */
		queryBlocks.put(MitsubishiConst.DATA_NO,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDataNo())));
		
		return queryBlocks;
	}
}
