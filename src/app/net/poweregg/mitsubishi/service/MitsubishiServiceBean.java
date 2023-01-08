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
import net.poweregg.mitsubishi.dto.Umb01Dto;
import net.poweregg.mitsubishi.webdb.utils.Operand;
import net.poweregg.mitsubishi.webdb.utils.QueryConj;
import net.poweregg.mitsubishi.webdb.utils.WebDbConstant;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;
import net.poweregg.security.CertificationService;

@Stateless
public class MitsubishiServiceBean implements MitsubishiService {

	@EJB
	ClassificationService classificationService;

	@EJB
	CertificationService certificationService;

	@PersistenceContext(unitName = "pe4jPU")
	private EntityManager em;

	private static final String DATA_NO = "データNO";

	@Override
	public Umb01Dto getDataMitsubishi(String dataNo) throws Exception {
		Long offset = 0L;
		Long limit = null;
		// get classInfo by commonNo: UMB01
		List<ClassInfo> webDBClassInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
				COMMON_NO.COMMON_NO_UMB01.getValue());
		
		if (webDBClassInfos == null) {
			return null;
		}
		
		WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0);

		JSONArray condList = new JSONArray();
		JSONArray query = new JSONArray();

		condList.put(WebDbUtils.createConditionQuery(DATA_NO, Operand.EQUALS, dataNo));
		query.put(WebDbUtils.createConditionBlock(QueryConj.AND, QueryConj.AND, condList));

		//call API
		JSONObject resultJson = webdbUtils.getDataFormAPI(query.toString(), null, offset, limit);
		
		// Khong tim duoc thi return
		if (resultJson == null || resultJson.length() == 0) {
			return null;
		}
		
		Umb01Dto umb01Dto = new Umb01Dto();
		// get data Umb01Dto form JSON 
		parseJSONtoUMB01(umb01Dto, resultJson);
		
		
		
		return umb01Dto;
	}
	
	private void parseJSONtoUMB01(Umb01Dto umb01Dto, JSONObject resultJson) throws JSONException {
		// id
		umb01Dto.setId(WebDbUtils.getValue(resultJson, "No"));
		// データ移行NO
		umb01Dto.getUnitPriceRefDto().setDataMigrationNo(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.DATA_LINE_NO));
		// データNO
		umb01Dto.getUnitPriceRefDto().setDataNo(WebDbUtils.getBigDecimalValue(resultJson, MitsubishiConst.DATA_NO));
		// 送信元レコード作成日時
		umb01Dto.getUnitPriceRefDto().setSrcCreateDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME));
		// 会社CD
		umb01Dto.getUnitPriceRefDto().setCompanyCD(WebDbUtils.getValue(resultJson, MitsubishiConst.COMPANY_CD));
		// 取引CD
		umb01Dto.getUnitPriceRefDto().setTransactionCD(WebDbUtils.getValue(resultJson, MitsubishiConst.TRANSACTION_CD));
		// 売上部門CD
		umb01Dto.getUnitPriceRefDto().setSalesDepartmentCD(WebDbUtils.getValue(resultJson, MitsubishiConst.SALES_DEPARTMENT_CD));
		// 上位部門CD
		umb01Dto.getUnitPriceRefDto().setUpperCategoryCD(WebDbUtils.getValue(resultJson, MitsubishiConst.UPPER_CATEGORY_CD));
		// 会計部門CD
		umb01Dto.getUnitPriceRefDto().setAccountDepartmentCD(WebDbUtils.getValue(resultJson, MitsubishiConst.ACCOUNT_DEPARTMENT_CD));
		// 受注NO
		umb01Dto.getUnitPriceRefDto().setOrderNo(WebDbUtils.getValue(resultJson, MitsubishiConst.ORDER_NO));
		// 受注明細NO
		umb01Dto.getUnitPriceRefDto().setSalesOrderNo(WebDbUtils.getValue(resultJson, MitsubishiConst.SALES_ORDER_NO));
		// 得意先CD
		umb01Dto.getUnitPriceRefDto().setCustomerCD(WebDbUtils.getValue(resultJson, MitsubishiConst.CUSTOMER_CD));
		// 得意先名
		umb01Dto.getUnitPriceRefDto().setCustomerName(WebDbUtils.getValue(resultJson, MitsubishiConst.CUSTOMER_NAME));
		// 仕向先CD1
		umb01Dto.getUnitPriceRefDto().setDestinationCD1(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_CD1));
		// 仕向先名1
		umb01Dto.getUnitPriceRefDto().setDestinationName1(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_NAME1));
		// 仕向先CD2
		umb01Dto.getUnitPriceRefDto().setDestinationCD2(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_CD2));
		// 仕向先名2
		umb01Dto.getUnitPriceRefDto().setDestinationName2(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_NAME2));
		// 納品先CD
		umb01Dto.getUnitPriceRefDto().setDeliveryDestinationCD(WebDbUtils.getValue(resultJson, MitsubishiConst.DESTINATION_CD));
		// 納品先名
		umb01Dto.getUnitPriceRefDto().setDeliveryDestinationName(WebDbUtils.getValue(resultJson, MitsubishiConst.DELIVERY_DESTINATION_NAME));
		// 品名略号
		umb01Dto.getUnitPriceRefDto().setProductNameAbbreviation(WebDbUtils.getValue(resultJson, MitsubishiConst.PRODUCT_NAME_ABBREVIATION));
		// カラーNO
		umb01Dto.getUnitPriceRefDto().setColorNo(WebDbUtils.getValue(resultJson, MitsubishiConst.COLOR_NO));
		// グレード1
		umb01Dto.getUnitPriceRefDto().setGrade1(WebDbUtils.getValue(resultJson, MitsubishiConst.GRADE_1));
		// ユーザー品目
		umb01Dto.getUnitPriceRefDto().setUserItem(WebDbUtils.getValue(resultJson, MitsubishiConst.USER_ITEM));
		// 通貨CD
		umb01Dto.getUnitPriceRefDto().setCurrencyCD(WebDbUtils.getValue(resultJson, MitsubishiConst.CURRENCY_CD));
		// 取引単位CD
		umb01Dto.getUnitPriceRefDto().setTransactionUnitCD(WebDbUtils.getValue(resultJson, MitsubishiConst.TRANSACTION_UNIT_CD));
		// 荷姿
		umb01Dto.getUnitPriceRefDto().setPacking(WebDbUtils.getValue(resultJson, MitsubishiConst.PACKING));
		// 取引先枝番
		umb01Dto.getUnitPriceRefDto().setClientBranchNumber(WebDbUtils.getValue(resultJson, MitsubishiConst.CLIENT_BRANCH_NUMBER));
		// 価格形態
		umb01Dto.getUnitPriceRefDto().setPriceForm(WebDbUtils.getValue(resultJson, MitsubishiConst.PRICE_FORM));
		// 用途参照
		umb01Dto.getUnitPriceRefDto().setUsageRef(WebDbUtils.getValue(resultJson, MitsubishiConst.USAGE_REF));
		// 納品予定日時
		umb01Dto.getUnitPriceRefDto().setDeliveryDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.SCHEDULED_DELIVERY_DATE));
		// 品名分類CD1
		umb01Dto.getUnitPriceRefDto().setCommodityClassificationCD1(WebDbUtils.getValue(resultJson, MitsubishiConst.PRODUCT_NAME_CLASS_CD1));
		// 受注日
		umb01Dto.getUnitPriceRefDto().setOrderDate(WebDbUtils.getDateValue(resultJson, MitsubishiConst.ORDER_DATE));
		// 登録担当者
		umb01Dto.getUnitPriceRefDto().setRegistrar(WebDbUtils.getValue(resultJson, MitsubishiConst.REGISTRAR));
	}
}