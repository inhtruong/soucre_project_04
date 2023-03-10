package net.poweregg.mitsubishi.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.poweregg.annotations.Login;
import net.poweregg.annotations.Single;
import net.poweregg.common.ClassificationService;
import net.poweregg.common.entity.ClassInfo;
import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.mitsubishi.constant.MitsubishiConst.COMMON_NO;
import net.poweregg.mitsubishi.csv.utils.ExportCsvUtils;
import net.poweregg.mitsubishi.dto.UMB01MasterDto;
import net.poweregg.mitsubishi.dto.Umb01Dto;
import net.poweregg.mitsubishi.webdb.utils.ConvertUtils;
import net.poweregg.mitsubishi.webdb.utils.LogUtils;
import net.poweregg.mitsubishi.webdb.utils.Operand;
import net.poweregg.mitsubishi.webdb.utils.QueryConj;
import net.poweregg.mitsubishi.webdb.utils.WebDbConstant;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;
import net.poweregg.security.CertificationService;
import net.poweregg.util.DBUtil;
import net.poweregg.util.DateUtils;
import net.poweregg.util.PESystemProperties;
import net.poweregg.util.StringUtils;
import net.poweregg.web.engine.navigation.LoginUser;
@Stateless
public class MitsubishiServiceBean implements MitsubishiService {

	@EJB
	ClassificationService classificationService;

	@EJB
	CertificationService certificationService;

	@PersistenceContext(unitName = "pe4jPU")
	private EntityManager em;
	
	@Inject
	@Login
	private LoginUser loginUser;
	
	@Inject
	@Single
	private transient Map<String, String> messages;

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
		JSONArray rsJson = findDataUmbByCondition(webdbUtils, MitsubishiConst.DATA_NO, dataNo,MitsubishiConst.MANAGER_NO);

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

	@Override
	public Umb01Dto getDataUpdateStatus(int dbType, String customerCD, String destinationCD1, String destinationCD2,
			String productNameAbbreviation, String colorNo, String currencyCD, String clientBranchNumber,
			String priceForm, String managerNo) throws Exception {
		Umb01Dto umb01Dto = new Umb01Dto();

		WebDbUtils webdbUtils = new WebDbUtils(getInfoWebDb(), 0, dbType);
		JSONArray rsJson = findDataUpdateStatusByCondition(webdbUtils, customerCD, destinationCD1, destinationCD2,
				productNameAbbreviation, colorNo, currencyCD, clientBranchNumber, priceForm, managerNo);

		// Khong tim duoc thi return
		if (rsJson == null || rsJson.length() == 0) {
			return null;
		}

		Umb01Dto umb01Dto1 = new Umb01Dto();
		// get data Umb01Dto form JSON
		parseJSONtoUMB01Temp(umb01Dto1, rsJson.getJSONObject(0), dbType);

		if (2 == dbType) {
			parseJSONtoUMB01Master(umb01Dto1, rsJson.getJSONObject(0), dbType);
		}

		return umb01Dto1;
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

		if (webDBClassInfos == null || webDBClassInfos.size() ==0 ) {
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
				/** ????????????NO */
				umbTempDto.setDataLineNo(WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_LINE_NO));
				/** ?????????NO */
				umbTempDto.setDataNo(WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_NO));
				/** ????????????????????????????????? */
				umbTempDto.setSrcCreateDate(
						WebDbUtils.getValue(recordObj, MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME));
				/** ????????????????????? */
				umbTempDto.setUpdateCategory(WebDbUtils.getValue(recordObj, MitsubishiConst.DATE_UPDATE_CATEGORY));
				/** ?????????CD */
				umbTempDto.setCustomerCD(WebDbUtils.getValue(recordObj, MitsubishiConst.CUSTOMER_CD));
				/** ?????????CD1 */
				umbTempDto.setDestinationCD1(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD1));
				/** ?????????CD2 */
				umbTempDto.setDestinationCD2(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD2));
				/** ???????????? */
				umbTempDto.setProductNameAbbreviation(
						WebDbUtils.getValue(recordObj, MitsubishiConst.PRODUCT_NAME_ABBREVIATION));
				/** ?????????No */
				umbTempDto.setColorNo(WebDbUtils.getValue(recordObj, MitsubishiConst.COLOR_NO));
				/** ????????????1 */
				umbTempDto.setGrade1(WebDbUtils.getValue(recordObj, MitsubishiConst.GRADE_1));
				/** ??????????????? */
				umbTempDto.setStartDateApplication(
						WebDbUtils.getValue(recordObj, MitsubishiConst.APPLICATION_START_DATE));
				/** ??????????????? */
				umbTempDto.setLotQuantity(WebDbUtils.getValue(recordObj, MitsubishiConst.LOT_QUANTITY));
				/** ??????CD */
				umbTempDto.setUpdateCategory(WebDbUtils.getValue(recordObj, MitsubishiConst.DATE_UPDATE_CATEGORY));
				/** ??????????????? */
				umbTempDto.setClientBranchNumber(WebDbUtils.getValue(recordObj, MitsubishiConst.CLIENT_BRANCH_NUMBER));
				/** ???????????? */
				umbTempDto.setPriceForm(WebDbUtils.getValue(recordObj, MitsubishiConst.PRICE_FORM));
				/** ???????????? */
				umbTempDto.setUnitPricePartition(WebDbUtils.getValue(recordObj, MitsubishiConst.PARTITION_UNIT_PRICE));
				/** ??????????????? */
				umbTempDto.setUnitPriceBefRevision(
						WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
				/** ?????????????????? */
				umbTempDto.setUnitPriceSmallParcel(
						WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
				/** ?????????????????? */
				umbTempDto.setUnitPriceForeheadColor(
						WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
				/** ???????????? */
				umbTempDto.setRetailPrice(WebDbUtils.getValue(recordObj, MitsubishiConst.PARTITION_UNIT_PRICE));
				/** ???????????? */
				umbTempDto.setContractNumber(WebDbUtils.getValue(recordObj, MitsubishiConst.CONTRACT_NUMBER));
				/** ???????????? */
				umbTempDto.setUsageRef(WebDbUtils.getValue(recordObj, MitsubishiConst.USAGE_REF));

			}
			umbResults.add(umbTempDto);
		}
		return umbResults;
	}

	private void parseJSONtoUMB01Temp(Umb01Dto umb01Dto, JSONObject resultJson, int dbType) throws JSONException {
		// id
		umb01Dto.setId(WebDbUtils.getValue(resultJson, "No"));
		// id
		umb01Dto.setManagerNo(WebDbUtils.getValue(resultJson, MitsubishiConst.MANAGER_NO));
		// ???????????????NO
		umb01Dto.getPriceUnitRefDto()
				.setDataMigrationNo(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.DATA_LINE_NO));
		// ?????????NO
		umb01Dto.getPriceUnitRefDto().setDataNo(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.DATA_NO));
		// ?????????????????????????????????
		umb01Dto.getPriceUnitRefDto()
				.setSrcCreateDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME));
		// ??????CD
		umb01Dto.getPriceUnitRefDto().setCompanyCD(WebDbUtils.getValue(resultJson, MitsubishiConst.COMPANY_CD));
		// ??????CD
		umb01Dto.getPriceUnitRefDto().setTransactionCD(WebDbUtils.getValue(resultJson, MitsubishiConst.TRANSACTION_CD));
		// ????????????CD
		umb01Dto.getPriceUnitRefDto()
				.setSalesDepartmentCD(WebDbUtils.getValue(resultJson, MitsubishiConst.SALES_DEPARTMENT_CD));
		// ????????????CD
		umb01Dto.getPriceUnitRefDto()
				.setUpperCategoryCD(WebDbUtils.getValue(resultJson, MitsubishiConst.UPPER_CATEGORY_CD));
		// ????????????CD
		umb01Dto.getPriceUnitRefDto()
				.setAccountDepartmentCD(WebDbUtils.getValue(resultJson, MitsubishiConst.ACCOUNT_DEPARTMENT_CD));
		// ??????NO
		umb01Dto.getPriceUnitRefDto().setOrderNo(WebDbUtils.getValue(resultJson, MitsubishiConst.ORDER_NO));
		// ????????????NO
		umb01Dto.getPriceUnitRefDto().setSalesOrderNo(WebDbUtils.getValue(resultJson, MitsubishiConst.SALES_ORDER_NO));
		// ?????????CD
		umb01Dto.getPriceUnitRefDto().setCustomerCD(WebDbUtils.getValue(resultJson, MitsubishiConst.CUSTOMER_CD));
		// ????????????
		umb01Dto.getPriceUnitRefDto().setCustomerName(WebDbUtils.getValue(resultJson, MitsubishiConst.CUSTOMER_NAME));
		// ?????????CD1
		umb01Dto.getPriceUnitRefDto()
				.setDestinationCD1(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_CD1));
		// ????????????1
		umb01Dto.getPriceUnitRefDto()
				.setDestinationName1(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_NAME1));
		// ?????????CD2
		umb01Dto.getPriceUnitRefDto()
				.setDestinationCD2(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_CD2));
		// ????????????2
		umb01Dto.getPriceUnitRefDto()
				.setDestinationName2(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_NAME2));
		// ?????????CD
		umb01Dto.getPriceUnitRefDto()
				.setDeliveryDestinationCD(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_CD));
		// ????????????
		umb01Dto.getPriceUnitRefDto()
				.setDeliveryDestinationName(WebDbUtils.getValue(resultJson, MitsubishiConst.DELIVERY_DESTINATION_NAME));
		// ????????????
		umb01Dto.getPriceUnitRefDto()
				.setProductNameAbbreviation(WebDbUtils.getValue(resultJson, MitsubishiConst.PRODUCT_NAME_ABBREVIATION));
		// ?????????NO
		umb01Dto.getPriceUnitRefDto().setColorNo(WebDbUtils.getValue(resultJson, MitsubishiConst.COLOR_NO));
		// ????????????1
		umb01Dto.getPriceUnitRefDto().setGrade1(WebDbUtils.getValue(resultJson, MitsubishiConst.GRADE_1));
		// ??????????????????
		umb01Dto.getPriceUnitRefDto().setUserItem(WebDbUtils.getValue(resultJson, MitsubishiConst.USER_ITEM));
		// ??????CD
		umb01Dto.getPriceUnitRefDto().setCurrencyCD(WebDbUtils.getValue(resultJson, MitsubishiConst.CURRENCY_CD));
		// ????????????CD
		umb01Dto.getPriceUnitRefDto()
				.setTransactionUnitCD(WebDbUtils.getValue(resultJson, MitsubishiConst.TRANSACTION_UNIT_CD));
		// ??????
		umb01Dto.getPriceUnitRefDto().setPacking(WebDbUtils.getValue(resultJson, MitsubishiConst.PACKING));
		// ???????????????
		umb01Dto.getPriceUnitRefDto()
				.setClientBranchNumber(WebDbUtils.getValue(resultJson, MitsubishiConst.CLIENT_BRANCH_NUMBER));
		// ????????????
		umb01Dto.getPriceUnitRefDto().setPriceForm(WebDbUtils.getValue(resultJson, MitsubishiConst.PRICE_FORM));
		// ????????????
		umb01Dto.getPriceUnitRefDto().setUsageCD(WebDbUtils.getValue(resultJson, MitsubishiConst.USAGE_REF));
		// ??????????????????
		umb01Dto.getPriceUnitRefDto()
				.setDeliveryDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.SCHEDULED_DELIVERY_DATE));
		// ????????????CD1
		umb01Dto.getPriceUnitRefDto()
				.setCommodityClassificationCD1(WebDbUtils.getValue(resultJson, MitsubishiConst.PRODUCT_NAME_CLASS_CD1));
		// ?????????
		umb01Dto.getPriceUnitRefDto().setOrderDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.ORDER_DATE));
		// ???????????????
		umb01Dto.getPriceUnitRefDto().setRegistrar(WebDbUtils.getValue(resultJson, MitsubishiConst.REGISTRAR));
		// ???????????????
		umb01Dto.getPriceRefDto().setLotQuantity(WebDbUtils.getValue(resultJson, MitsubishiConst.LOT_QUANTITY));
		// ????????????
		umb01Dto.getPriceRefDto()
				.setRetailPrice(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.RETAIL_PRICE));
		// ??????????????????
		umb01Dto.getPriceRefDto().setUnitPriceSmallParcel(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
		// ??????????????????
		umb01Dto.getPriceRefDto().setUnitPriceForeheadColor(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
		// ?????????????????????
		umb01Dto.getPriceRefDto().setPrimaryStoreCommissionAmount(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.PRIMARY_STORE_COMMISSION_AMOUNT));
		// ??????????????????
		umb01Dto.getPriceRefDto().setPrimaryStoreOpenRate(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.PRIMARY_STORE_OPENING_RATE));
		// ???????????????(??????)
		umb01Dto.getPriceRefDto().setPrimaryStoreOpenAmount(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.PRIMARY_STORE_OPEN_AMOUNT));
		// ??????????????????
		umb01Dto.setSecondStoreOpenRate(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.SECOND_STORE_OPEN_RATE));
		// ??????????????????
		umb01Dto.setSecondStoreOpenAmount(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.SECOND_STORE_OPEN_AMOUNT));
		// ????????????(?????????)
		umb01Dto.setPartitionUnitPrice(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.PARTITION_UNIT_PRICE));
		// ??????????????????
		umb01Dto.setTotalRetailPrice(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.TOTAL_RETAIL_PRICE));

		if (1 == dbType) {
			umb01Dto.getPriceUnitRefDto()
					.setAppRecepNo(WebDbUtils.getValue(resultJson, MitsubishiConst.APPLICATION_REC_NO));
			umb01Dto.getPriceUnitRefDto().setStatusCD(WebDbUtils.getValue(resultJson, MitsubishiConst.STATUS_CD));
		}

	}

	private void parseJSONtoUMB01Master(Umb01Dto umb01Dto, JSONObject resultJson, int dbType) throws JSONException {
		// ??????
		umb01Dto.getPriceRefDto().setWarning(WebDbUtils.getValue(resultJson, MitsubishiConst.WARNING));
		// ???????????????
		umb01Dto.getPriceRefDto().setUnitPriceBefRevision(
				WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
		// ?????????????????????CD
		umb01Dto.getPriceRefDto()
				.setDataUpdateCategoryCD(WebDbUtils.getValue(resultJson, MitsubishiConst.DATA_UPDATE_CATEGORY_CD));
		// ?????????????????????
		umb01Dto.getPriceRefDto()
				.setDataUpdateCategory(WebDbUtils.getValue(resultJson, MitsubishiConst.DATE_UPDATE_CATEGORY));
		// ???????????????
		umb01Dto.getPriceRefDto()
				.setApplicationStartDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.APPLICATION_START_DATE));
		// ???????????????
		umb01Dto.getPriceRefDto()
				.setApplicationEndDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.APPLICATION_END_DATE));
		// ????????????
		umb01Dto.getPriceRefDto().setContractNumber(WebDbUtils.getValue(resultJson, MitsubishiConst.CONTRACT_NUMBER));
		// ????????????
		umb01Dto.getPriceRefDto().setReasonInquiry(WebDbUtils.getValue(resultJson, MitsubishiConst.REASON_INQUIRY));
		// ????????????
		umb01Dto.getPriceRefDto().setRetroactiveClassification(
				WebDbUtils.getValue(resultJson, MitsubishiConst.RETROACTIVE_CLASSIFICATION));

		umb01Dto.getPriceRefDto().setAppRecepNo(WebDbUtils.getValue(resultJson, MitsubishiConst.APPLICATION_REC_NO));

		umb01Dto.getPriceRefDto()
				.setAppRecepNoCancel(WebDbUtils.getValue(resultJson, MitsubishiConst.CANCEL_APPRECP_NO));

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
		// XML????????????
		String xmlString = new String();

		xmlString = "";
		String CR_LF = System.getProperties().getProperty("line.separator");
		xmlString += "<?xml version=\"1.0\" encoding=\"utf-8\" ?>" + CR_LF;
		xmlString += "<U_MITSUBISHI>" + CR_LF;
		xmlString += "<TB_DEFAULT>" + CR_LF;

		xmlString += "<PATTERN>" + StringUtils.toEmpty(param.getPriceCalParam().getPattern()) + "</PATTERN>" + CR_LF;

		xmlString += "<MANAGERNO>" + StringUtils.toEmpty(param.getManagerNo()) + "</MANAGERNO>" + CR_LF;

		xmlString += "<UNITPRICEDATAREF>"
//				+ StringUtils.toEmpty(param.getUnitPriceDataRef())
				+ "</UNITPRICEDATAREF>" + CR_LF;

		xmlString += "<PRICEDATAREF>"
//				+ StringUtils.toEmpty(param.getPriceDataRef())
				+ "</PRICEDATAREF>" + CR_LF;

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
		
		if(param.getPriceUnitRefDto().getUsageRef() != null && param.getPriceUnitRefDto().getUsageRef().getTargetFieldName()!=null) {
			xmlString += "<USAGEREF>" +
						StringUtils.toEmpty(param.getPriceUnitRefDto().getUsageRef().getTargetFieldName())
			+ "</USAGEREF>" + CR_LF;
		}else {
			xmlString += "<USAGEREF>"+ "" + "</USAGEREF>" + CR_LF;
		}

		xmlString += "<DELIVERYDATE>" + StringUtils.toEmpty(param.getPriceUnitRefDto().getDeliveryDate())
				+ "</DELIVERYDATE>" + CR_LF;

		xmlString += "<COMMODITYCLASSIFICATIONCD1>"
				+ StringUtils.toEmpty(param.getPriceUnitRefDto().getCommodityClassificationCD1())
				+ "</COMMODITYCLASSIFICATIONCD1>" + CR_LF;

		xmlString += "<ORDERDATE>" + StringUtils.formatDate(param.getPriceUnitRefDto().getOrderDate()) + "</ORDERDATE>"
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

		// ????????????
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

		// ??????????????????
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

		// ??????????????????
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

		// ???????????? ??????
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

		// ???????????????
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

		// ???????????????
		xmlString += "<NOPRESECONDARYOPENAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getNoPreSecondaryOpenAmount())
				+ "</NOPRESECONDARYOPENAMOUNT>" + CR_LF;

		xmlString += "<DELISECONDARYOPENAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getDeliSecondaryOpenAmount())
				+ "</DELISECONDARYOPENAMOUNT>" + CR_LF;

		xmlString += "<LARGESECONDARYOPENAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargeSecondaryOpenAmount())
				+ "</LARGESECONDARYOPENAMOUNT>" + CR_LF;

		xmlString += "<SMALLSECONDARYOPENAMOUNT>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallSecondaryOpenAmount())
				+ "</SMALLSECONDARYOPENAMOUNT>" + CR_LF;

		xmlString += "<NOPRESECONDARYOPENRATE>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getNoPreSecondaryOpenRate())
				+ "</NOPRESECONDARYOPENRATE>" + CR_LF;

		xmlString += "<DELISECONDARYOPENRATE>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getDeliSecondaryOpenRate()) + "</DELISECONDARYOPENRATE>"
				+ CR_LF;

		xmlString += "<LARGESECONDARYOPENRATE>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getLargeSecondaryOpenRate())
				+ "</LARGESECONDARYOPENRATE>" + CR_LF;

		xmlString += "<SMALLSECONDARYOPENRATE>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getSmallSecondaryOpenRate())
				+ "</SMALLSECONDARYOPENRATE>" + CR_LF;

		// ????????????
		xmlString += "<NOPRETOTALOPENAMOUNT>" + StringUtils.toEmpty(param.getPriceCalParam().getNoPreTotalOpenAmount())
				+ "</NOPRETOTALOPENAMOUNT>" + CR_LF;

		xmlString += "<DELITOTALOPENAMOUNT>" + StringUtils.toEmpty(param.getPriceCalParam().getDeliTotalOpenAmount())
				+ "</DELITOTALOPENAMOUNT>" + CR_LF;

		xmlString += "<LARGETOTALOPENAMOUNT>" + StringUtils.toEmpty(param.getPriceCalParam().getLargeTotalOpenAmount())
				+ "</LARGETOTALOPENAMOUNT>" + CR_LF;

		xmlString += "<SMALLTOTALOPENAMOUNT>" + StringUtils.toEmpty(param.getPriceCalParam().getSmallTotalOpenAmount())
				+ "</SMALLTOTALOPENAMOUNT>" + CR_LF;

		xmlString += "<NOPRETOTALOPENRATE>" + StringUtils.toEmpty(param.getPriceCalParam().getNoPreTotalOpenRate())
				+ "</NOPRETOTALOPENRATE>" + CR_LF;

		xmlString += "<DELITOTALOPENRATE>" + StringUtils.toEmpty(param.getPriceCalParam().getDeliTotalOpenRate())
				+ "</DELITOTALOPENRATE>" + CR_LF;

		xmlString += "<LARGETOTALOPENRATE>" + StringUtils.toEmpty(param.getPriceCalParam().getLargeTotalOpenRate())
				+ "</LARGETOTALOPENRATE>" + CR_LF;

		xmlString += "<SMALLTOTALOPENRATE>" + StringUtils.toEmpty(param.getPriceCalParam().getSmallTotalOpenRate())
				+ "</SMALLTOTALOPENRATE>" + CR_LF;

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

		// ????????????(?????????)
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

		// ??????????????????(?????????)
		xmlString += "<CALSMALLUNITPRICEPARCEL>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getCalSmallUnitPriceParcel())
				+ "</CALSMALLUNITPRICEPARCEL>" + CR_LF;
		xmlString += "<CALLARGEUNITPRICEPARCEL>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getCalLargeUnitPriceParcel())
				+ "</CALLARGEUNITPRICEPARCEL>" + CR_LF;
		// ??????????????????(?????????)
		xmlString += "<CALSMALLUNITPRICEFOREHEAD>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getCalSmallUnitPriceForehead())
				+ "</CALSMALLUNITPRICEFOREHEAD>" + CR_LF;
		xmlString += "<CALLARGEUNITPRICEFOREHEAD>"
				+ StringUtils.toEmpty(param.getPriceCalParam().getCalLargeUnitPriceForehead())
				+ "</CALLARGEUNITPRICEFOREHEAD>" + CR_LF;

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
		JSONArray rsJson = findDataUmbByCondition(webdbUtils, MitsubishiConst.DATA_NO, dataNo,MitsubishiConst.MANAGER_NO);

		// Khong tim duoc thi record no return luon
		if (rsJson == null || rsJson.length() == 0) {
			LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "Error:" + dataNo + "?????????????????????");
			throw new Exception("Error: " + MitsubishiConst.DATA_NO + " " + dataNo + " ?????????????????????");
		}

		// chuan bi du lieu jsonobject
		JSONObject queryBlocks = new JSONObject();
		if (StringUtils.nullOrBlank(mode)) {
			queryBlocks.put(MitsubishiConst.STATUS_CD,
					WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getStatusCD()));
		} else {
			queryBlocks = createJsonQuery(umb01Dto, mode);
		}

		// update th??ng qua rest api/
		webdbUtils.putJsonObject(queryBlocks, umb01Dto.getId(), false, false, false);
		
		// update 
		if ("1".equals(mode)) {
			/**Update ???????????? */
			String contracNumber = WebDbUtils.getValue(queryBlocks, MitsubishiConst.CONTRACT_NUMBER);
			String companyId = loginUser.getCorpID();
			if(contracNumber!= null && !"".equals(contracNumber)) {
				 String[] contracNumberArray = new String[]{};
				 contracNumberArray = contracNumber.split(companyId);
				if (contracNumberArray.length>0) {
					int i = contracNumberArray.length-1;
					updateDivisionNumber(Long.valueOf(contracNumberArray[i])+1);
				}
			}
		}

		
	}

	@Override
	public void registerRecordDbPrice(String logFileFullPath, Umb01Dto umb01Dto) throws Exception {
		WebDbUtils webdbUtils = new WebDbUtils(getInfoWebDb(), 0, 2);

		// chuan bi du lieu jsonobject
		JSONObject queryBlocks = new JSONObject();
		queryBlocks = createJsonQuery(umb01Dto, null);

		// register th??ng qua rest api
		webdbUtils.registJsonObject(queryBlocks, true);
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
	public JSONArray findDataUmbByCondition(WebDbUtils webdbUtils, String field, String value,String order) throws Exception {

		JSONArray condOr = new JSONArray();
		condOr.put(WebDbUtils.createConditionQuery(field, Operand.EQUALS, value));
		JSONArray queryBlocks = new JSONArray();
		queryBlocks.put(WebDbUtils.createConditionBlock(QueryConj.AND, QueryConj.AND, condOr));
		if ( order!= null && "".equals(order)) {
			queryBlocks.put(WebDbUtils.createOrderQuery(order, true));
		}
		// call API
		JSONObject tempJson = webdbUtils.getDataFormAPI(queryBlocks.toString(), null, offset, limit);
		JSONArray rsJson = webdbUtils.getJsonObjectWebDB(tempJson, offset, limit);

		return rsJson;
	}

	@Override
	public JSONArray findDataUpdateStatusByCondition(WebDbUtils webdbUtils, String customerCD, String destinationCD1,
			String destinationCD2, String productNameAbbreviation, String colorNo, String currencyCD,
			String clientBranchNumber, String priceForm, String managerNo) throws Exception {

		JSONArray condOr = new JSONArray();
		condOr.put(WebDbUtils.createConditionQuery(MitsubishiConst.CUSTOMER_CD, Operand.EQUALS, customerCD));
		condOr.put(WebDbUtils.createConditionQuery(MitsubishiConst.DESTINATION_CD1, Operand.EQUALS, destinationCD1));
		condOr.put(WebDbUtils.createConditionQuery(MitsubishiConst.DESTINATION_CD2, Operand.EQUALS, destinationCD2));
		condOr.put(WebDbUtils.createConditionQuery(MitsubishiConst.PRODUCT_NAME_ABBREVIATION, Operand.EQUALS,
				productNameAbbreviation));
		condOr.put(WebDbUtils.createConditionQuery(MitsubishiConst.COLOR_NO, Operand.EQUALS, colorNo));
		condOr.put(WebDbUtils.createConditionQuery(MitsubishiConst.CURRENCY_CD, Operand.EQUALS, currencyCD));
		condOr.put(WebDbUtils.createConditionQuery(MitsubishiConst.CLIENT_BRANCH_NUMBER, Operand.EQUALS,
				clientBranchNumber));
		condOr.put(WebDbUtils.createConditionQuery(MitsubishiConst.PRICE_FORM, Operand.EQUALS, priceForm));
		if (!StringUtils.nullOrBlank(managerNo)) {
			condOr.put(WebDbUtils.createConditionQuery(MitsubishiConst.MANAGER_NO, Operand.EQUALS, managerNo));
		}
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
	private JSONObject createJsonQuery(Umb01Dto umb01Dto, String mode) throws JSONException, Exception {

		JSONObject queryBlocks = new JSONObject();
		// mode new
		if (MitsubishiConst.MODE_NEW.equals(mode)) {
			queryBlocks.put(MitsubishiConst.APPLICATION_REC_NO,
					WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getAppRecepNo()));
			queryBlocks.put(MitsubishiConst.STATUS_CD,
					WebDbUtils.createRecordItem(umb01Dto.getPriceUnitRefDto().getStatusCD()));
			/** ???????????? */
			queryBlocks.put(MitsubishiConst.CONTRACT_NUMBER,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getContractNumber())));
		}
		// mode edit
		if (MitsubishiConst.MODE_EDIT.equals(mode)) {
			queryBlocks.put(MitsubishiConst.APPLICATION_REC_NO,
					WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getAppRecepNo()));
			queryBlocks.put(MitsubishiConst.STATUS_CD,
					WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getStatusCD()));
			/** ??????????????? */
			queryBlocks.put(MitsubishiConst.UNIT_PRICE_BEFORE_REVISION, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getUnitPriceBefRevision())));
		}
		// mode cancel
		if (MitsubishiConst.MODE_CANCEL.equals(mode)) {
			queryBlocks.put(MitsubishiConst.CANCEL_APPRECP_NO,
					WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getAppRecepNoCancel()));
			queryBlocks.put(MitsubishiConst.STATUS_CD,
					WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getStatusCD()));
			/** ??????????????? */
			queryBlocks.put(MitsubishiConst.UNIT_PRICE_BEFORE_REVISION, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getUnitPriceBefRevision())));
		}

		if (StringUtils.nullOrBlank(mode)) {
			queryBlocks.put(MitsubishiConst.STATUS_CD,
					WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getStatusCD()));
			queryBlocks.put(MitsubishiConst.APPLICATION_REC_NO,
					WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getAppRecepNo()));
			queryBlocks.put(MitsubishiConst.CANCEL_APPRECP_NO,
					WebDbUtils.createRecordItem(umb01Dto.getPriceRefDto().getAppRecepNoCancel()));
			/** ???????????? */
			queryBlocks.put(MitsubishiConst.CONTRACT_NUMBER,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getContractNumber())));
			/** ??????????????? */
			queryBlocks.put(MitsubishiConst.UNIT_PRICE_BEFORE_REVISION, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getUnitPriceBefRevision())));
			/** ???????????????NO */
			queryBlocks.put(MitsubishiConst.DATA_LINE_NO, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDataMigrationNo())));
			/** ?????????NO */
			queryBlocks.put(MitsubishiConst.DATA_NO,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDataNo())));
			/** ????????????????????????????????? */
			queryBlocks.put(MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME, WebDbUtils.createRecordItem(
					DateUtils.convertDateTime(umb01Dto.getPriceUnitRefDto().getSrcCreateDate(), "yyyyMMdd")));
			/** ??????CD */
			queryBlocks.put(MitsubishiConst.COMPANY_CD,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getCompanyCD())));
			/** ??????CD */
			queryBlocks.put(MitsubishiConst.TRANSACTION_CD,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getTransactionCD())));
			/** ????????????CD */
			queryBlocks.put(MitsubishiConst.SALES_DEPARTMENT_CD, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getSalesDepartmentCD())));
			/** ????????????CD */
			queryBlocks.put(MitsubishiConst.UPPER_CATEGORY_CD, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getUpperCategoryCD())));
			/** ????????????CD */
			queryBlocks.put(MitsubishiConst.ACCOUNT_DEPARTMENT_CD, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getAccountDepartmentCD())));
			/** ??????NO */
			queryBlocks.put(MitsubishiConst.ORDER_NO,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getOrderNo())));
			/** ????????????NO */
			queryBlocks.put(MitsubishiConst.SALES_ORDER_NO,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getSalesOrderNo())));
			/** ?????????CD */
			queryBlocks.put(MitsubishiConst.CUSTOMER_CD,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getCustomerCD())));
			/** ???????????? */
			queryBlocks.put(MitsubishiConst.CUSTOMER_NAME,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getCustomerName())));
			/** ?????????CD1 */
			queryBlocks.put(MitsubishiConst.DESTINATION_CD1, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDestinationCD1())));
			/** ????????????1 */
			queryBlocks.put(MitsubishiConst.DESTINATION_NAME1, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDestinationName1())));
			/** ?????????CD2 */
			queryBlocks.put(MitsubishiConst.DESTINATION_CD2, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDestinationCD2())));
			/** ????????????2 */
			queryBlocks.put(MitsubishiConst.DESTINATION_NAME2, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDestinationName2())));
			/** ?????????CD */
			queryBlocks.put(MitsubishiConst.DESTINATION_CD, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDeliveryDestinationCD())));
			/** ???????????? */
			queryBlocks.put(MitsubishiConst.DELIVERY_DESTINATION_NAME, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDeliveryDestinationName())));
			/** ???????????? */
			queryBlocks.put(MitsubishiConst.PRODUCT_NAME_ABBREVIATION, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getProductNameAbbreviation())));
			/** ?????????NO */
			queryBlocks.put(MitsubishiConst.COLOR_NO,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getColorNo())));
			/** ????????????1 */
			queryBlocks.put(MitsubishiConst.GRADE_1,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getGrade1())));
			/** ?????????????????? */
			queryBlocks.put(MitsubishiConst.USER_PRODUCT_NAME,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getUserItem())));
			/** ??????CD */
			queryBlocks.put(MitsubishiConst.CURRENCY_CD,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getCurrencyCD())));
			/** ????????????CD */
			queryBlocks.put(MitsubishiConst.TRANSACTION_UNIT_CD, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getTransactionUnitCD())));
			/** ?????? */
			queryBlocks.put(MitsubishiConst.PACKING,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getPacking())));
			/** ??????????????? */
			queryBlocks.put(MitsubishiConst.CLIENT_BRANCH_NUMBER, WebDbUtils
					.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getClientBranchNumber())));
			/** ???????????? */
			queryBlocks.put(MitsubishiConst.PRICE_FORM,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getPriceForm())));
			/** ?????????????????? */
			queryBlocks.put(MitsubishiConst.SCHEDULED_DELIVERY_DATE, WebDbUtils.createRecordItem(
					DateUtils.convertDateTime(umb01Dto.getPriceUnitRefDto().getDeliveryDate(), "yyyyMMdd")));
			/** ????????? */
			queryBlocks.put(MitsubishiConst.ORDER_DATE, WebDbUtils.createRecordItem(
					DateUtils.convertDateTime(umb01Dto.getPriceUnitRefDto().getOrderDate(), "yyyyMMdd")));
			/** ??????????????? */
			queryBlocks.put(MitsubishiConst.REGISTRAR,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getRegistrar())));
			/** ?????? */
			queryBlocks.put(MitsubishiConst.WARNING,
					WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getWarning())));
			/** ????????????URL */
			queryBlocks.put(MitsubishiConst.NEW_APPLICATION_URL,
					WebDbUtils.createRecordURL(getUrlStringByMode(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDataNo()), MitsubishiConst.MODE_NEW)));
			/** ????????????URL */
			queryBlocks.put(MitsubishiConst.EDIT_REQUEST_URL,
					WebDbUtils.createRecordURL(getUrlStringByMode(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDataNo()), MitsubishiConst.MODE_EDIT)));
			/** ????????????URL */
			queryBlocks.put(MitsubishiConst.CANCEL_REQUEST_URL,
					WebDbUtils.createRecordURL(getUrlStringByMode(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getDataNo()), MitsubishiConst.MODE_CANCEL)));
		}
		/** ????????????1 */
		queryBlocks.put(MitsubishiConst.GRADE_1,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getGrade1())));
		/** ???????????? */
		if (umb01Dto.getPriceUnitRefDto().getUsageRef()!= null && umb01Dto.getPriceUnitRefDto().getUsageRef().getTargetFieldID() != null && !"".equals(umb01Dto.getPriceUnitRefDto().getUsageRef().getTargetFieldID())){
			queryBlocks.put(MitsubishiConst.USAGE_REF, WebDbUtils
				.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceUnitRefDto().getUsageRef().getTargetFieldID())));
		}else {
			queryBlocks.put(MitsubishiConst.USAGE_REF, WebDbUtils.createRecordItem(""));
		}
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.RETAIL_PRICE,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getRetailPrice())));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.UNIT_PRICE_SMALL_PARCEL,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getUnitPriceSmallParcel())));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR, WebDbUtils
				.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getUnitPriceForeheadColor())));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.PRIMARY_STORE_OPEN_AMOUNT, WebDbUtils
				.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getPrimaryStoreOpenAmount())));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.PRIMARY_STORE_OPENING_RATE,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getPrimaryStoreOpenRate())));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.SECOND_STORE_OPEN_AMOUNT,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getSecondStoreOpenAmount())));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.SECOND_STORE_OPEN_RATE,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getSecondStoreOpenRate())));
		/** ??????????????????????????? */
		queryBlocks.put(MitsubishiConst.PARTITION_UNIT_PRICE,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPartitionUnitPrice())));
		/** ??????????????? */
		queryBlocks.put(MitsubishiConst.LOT_QUANTITY,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getLotQuantity())));
		/** ??????????????? */
		queryBlocks.put(MitsubishiConst.APPLICATION_START_DATE, WebDbUtils.createRecordItem(
				DateUtils.convertDateTime(umb01Dto.getPriceRefDto().getApplicationStartDate(), "yyyyMMdd")));
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.REASON_INQUIRY,
				WebDbUtils.createRecordItem(StringUtils.toEmpty(umb01Dto.getPriceRefDto().getReasonInquiry())));

		return queryBlocks;
	}
	
	
	public String createFileName(String ext) {
		String tenPuDir = PESystemProperties.getInstance().getProperty(MitsubishiConst.TENPU_DIR);
		List<ClassInfo> webDBClassInfos = getInfoWebDb();
		if (ConvertUtils.isNullOrEmptyOrBlank(tenPuDir)) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(tenPuDir);
		buffer.append(MitsubishiConst.SEPARATOR);
		buffer.append(LogUtils.getCharData1(webDBClassInfos, MitsubishiConst.CLASS_NO.CLASSNO_10.getValue()));
		buffer.append(ext);
		return buffer.toString();
	}
	
	@Override
	public String exportCsvBtnStatusUMB01(Umb01Dto umb01Dto) throws JSONException, Exception {
		try {
			String filePath = createFileName(MitsubishiConst.CSV_EXTENSION);
			JSONObject recordObj= createJsonQuery(umb01Dto, null);
			return ExportCsvUtils.exportCsvUMB01(recordObj, filePath);
			
		}catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	/*
	 * update {??????} + 1 before update webdb
	 */
	public int updateDivisionNumber(Long serial){
		int cnt =0;
		StringBuffer queryStr = new StringBuffer();
		queryStr.append("UPDATE ZT2_CLASS SET ");
		queryStr.append("NUMDATA1 =" + serial);
		queryStr.append(" WHERE CORPID ='" + WebDbConstant.ALL_CORP + "' AND COMMONNO = 'UMB01' AND CLASSNAME = '" + MitsubishiConst.CLASS_NO.CLASSNO_20.getValue() +"'");				
		Query query = this.em.createNativeQuery(queryStr.toString());

		cnt  = query.executeUpdate();
		System.out.println("Update ?????? record: " + cnt);
		return cnt;
	}
	
	/**
	 * Create url by mode new,edit,cancel
	 * 
	 * @param webDBClassInfos
	 * @param recordObj
	 * @param mode
	 * @return
	 * @throws JSONException
	 */
	private String getUrlStringByMode( String dataNo, String modeConst) throws JSONException {

		StringBuilder url = new StringBuilder(messages.get(MitsubishiConst.PE4J_PROPERTIES));

		if (MitsubishiConst.MODE_NEW.equals(modeConst)) {
			url.append(MessageFormat.format(messages.get(MitsubishiConst.URL_PROPERTIES),
					dataNo, MitsubishiConst.MODE_NEW));
		}
		if (MitsubishiConst.MODE_EDIT.equals(modeConst)) {
			url.append(MessageFormat.format(messages.get(MitsubishiConst.URL_PROPERTIES),
					dataNo, MitsubishiConst.MODE_EDIT));
		}
		if (MitsubishiConst.MODE_CANCEL.equals(modeConst)) {
			url.append(MessageFormat.format(messages.get(MitsubishiConst.URL_PROPERTIES),
					dataNo, MitsubishiConst.MODE_CANCEL));
		}

		return url.toString();
	}
}
