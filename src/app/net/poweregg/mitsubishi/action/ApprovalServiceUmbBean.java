/**
 * 
 */
package net.poweregg.mitsubishi.action;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.poweregg.annotations.Single;
import net.poweregg.common.ClassificationService;
import net.poweregg.common.entity.ClassInfo;
import net.poweregg.dataflow.ConstStatus;
import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.mitsubishi.csv.utils.ExportCsvUtils;
import net.poweregg.mitsubishi.service.MitsubishiService;
import net.poweregg.mitsubishi.webdb.utils.ConvertUtils;
import net.poweregg.mitsubishi.webdb.utils.LogUtils;
import net.poweregg.mitsubishi.webdb.utils.WebDbConstant;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;
import net.poweregg.util.PESystemProperties;
import net.poweregg.util.StringUtils;

/**
 * @author diennv
 *
 */
@Stateless
public class ApprovalServiceUmbBean implements ApprovalServiceUmb {

	@EJB
	private ClassificationService classificationService;

	@EJB
	private MitsubishiService mitsubishiService;

	@Inject
	@Single
	private transient Map<String, String> messages;

	/**
	 * apply after flow from temp_matsubishi to master_matsubishi
	 */
	@Override
	public void commitApproval(Long appRecepNo, String status, int mode) {

		List<ClassInfo> webDBClassInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
				MitsubishiConst.COMMON_NO.COMMON_NO_UMB01.getValue());
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);
		LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), MitsubishiConst.LOG_BEGIN);
		int dbType = 2;
		WebDbUtils webdbUtils = new WebDbUtils();
		WebDbUtils webdbUtilsBD1 = new WebDbUtils();
		JSONArray rsJson = new JSONArray();
		JSONObject recordObj = new JSONObject();
		try {
			// find record data at webDB price
			webdbUtilsBD1 = new WebDbUtils(webDBClassInfos, 0, dbType);
			if(mode ==3) {
				rsJson = mitsubishiService.findDataUmbByCondition(webdbUtilsBD1,
						MitsubishiConst.CANCEL_APPRECP_NO, StringUtils.toEmpty(appRecepNo),MitsubishiConst.MANAGER_NO);
			}else {
				rsJson = mitsubishiService.findDataUmbByCondition(webdbUtilsBD1,
						MitsubishiConst.APPLICATION_REC_NO, StringUtils.toEmpty(appRecepNo),MitsubishiConst.MANAGER_NO);
			}
			if (rsJson == null || rsJson.length() == 0) {
				if (1 == mode) {
					webdbUtilsBD1 = new WebDbUtils(webDBClassInfos, 0, mode);
					rsJson = mitsubishiService.findDataUmbByCondition(webdbUtilsBD1,
							MitsubishiConst.APPLICATION_REC_NO, StringUtils.toEmpty(appRecepNo),MitsubishiConst.MANAGER_NO);
					if (rsJson == null || rsJson.length() == 0) {
						throw new Exception("Error: ?????????????????????" + appRecepNo + "????????????????????????");
					}else {
						dbType = mode;
						recordObj = rsJson.getJSONObject(0);
						webdbUtils = webdbUtilsBD1;
					}
				}else {
					throw new Exception("Error: ?????????????????????" + appRecepNo + "????????????????????????");
				}
			}else {
				recordObj = rsJson.getJSONObject(0);
				webdbUtils = webdbUtilsBD1;
			}

			// withdraw or sentback
			if (ConstStatus.STATUS_BACKED_AWAY.equals(status) || ConstStatus.STATUS_SENT_BACK.equals(status)) {
				// mode new
				if (1 == mode) {
					// insert at master webDB
					webdbUtils = new WebDbUtils(webDBClassInfos, 0, dbType);
					updateRecordDb(webDBClassInfos, StringUtils.toEmpty(appRecepNo), status,
							WebDbUtils.getValue(recordObj, MitsubishiConst.NO), dbType);
				}
				// mode edit & cancel
				if (2 == mode || 3 == mode) {
					if (2 == mode && dbType == 1) {
						// update ?????????????????? in temp webDB: apply
						updateRecordDb(webDBClassInfos, StringUtils.toEmpty(appRecepNo), status,
								WebDbUtils.getValue(recordObj, MitsubishiConst.NO), 1);
					}else {
						// update ?????????????????? in master webDB: withdraw
						updateRecordDb(webDBClassInfos, StringUtils.toEmpty(appRecepNo), status,
								WebDbUtils.getValue(recordObj, MitsubishiConst.NO), 2);
					}
				}
			}

			// approval
			if (ConstStatus.STATUS_APPROVED.equals(status)) {
				JSONObject queryBlocks = createJsonQuery(recordObj, mode, dbType);
				// mode new
				if (1 == mode) {
					// insert at master webDB
					webdbUtils = new WebDbUtils(webDBClassInfos, 0, 2);
					if (mode== dbType) {
						updateRecordDb(webDBClassInfos, StringUtils.toEmpty(appRecepNo), status,
								WebDbUtils.getValue(recordObj, MitsubishiConst.NO), 1);
					}else {
						updateRecordDb(webDBClassInfos, StringUtils.toEmpty(appRecepNo), status,
								WebDbUtils.getValue(recordObj, MitsubishiConst.NO), 2);
						queryBlocks.put(MitsubishiConst.MANAGER_NO,
								WebDbUtils.createRecordItem(StringUtils.toEmpty(1)));
					}
				}
				// mode edit
				if (2 == mode || 3 == mode) {
					// copy data from master to history webDB
					webdbUtils = new WebDbUtils(webDBClassInfos, 0, 3);
					webdbUtils.registJsonObject(queryBlocks, true);

					// delete old record in master webDB
					webdbUtils = new WebDbUtils(webDBClassInfos, 0, 2);
					webdbUtils.delJsonObject(webDBClassInfos, WebDbUtils.getValue(recordObj, MitsubishiConst.NO));
					//  ?????????No??? = ??????NO+1
					int managerNO = Integer.parseInt(WebDbUtils.getValue(recordObj, MitsubishiConst.MANAGER_NO)) + 1;
					/** ??????NO */
					queryBlocks.put(MitsubishiConst.MANAGER_NO,
							WebDbUtils.createRecordItem(StringUtils.toEmpty(managerNO)));
					
					if (3 == mode) {
						/** ?????????????????? */
						queryBlocks.put(MitsubishiConst.CANCEL_APPRECP_NO,
								WebDbUtils.createRecordItem(StringUtils.toEmpty(appRecepNo)));
						/** ??????CD */
						queryBlocks.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(ConstStatus.STATUS_CANCELED));
					}else {
						/** ??????CD */
						queryBlocks.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(status));
					}
					//export csv
					String filePath = createFileName(MitsubishiConst.CSV_EXTENSION);
					ExportCsvUtils.exportCsvUMB01(recordObj, filePath);
				}
				
				//Import data to webDB
				String result = webdbUtils.registJsonObject(queryBlocks, true);
				if (!ConvertUtils.isNullOrEmpty(result)) {
					System.out.println(result);
					LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), e.getMessage());

		} finally {
			LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
					MitsubishiConst.LOG_FINISH);
		}
	}

	/**
	 * create object query
	 * 
	 * @param recordObj
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	private JSONObject createJsonQuery(JSONObject recordObj, int mode, int dbType) throws JSONException, Exception {

		JSONObject queryBlocks = new JSONObject();
		// mode new
		if (1 == mode) {
			/** ??????No */
			queryBlocks.put(MitsubishiConst.MANAGER_NO,WebDbUtils.createRecordItem("1"));
		}
		if (dbType == 1) {
			/** ??????????????? */
			queryBlocks.put(MitsubishiConst.UNIT_PRICE_BEFORE_REVISION, WebDbUtils
					.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PARTITION_UNIT_PRICE)));
			/** ??????CD */
			queryBlocks.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(ConstStatus.STATUS_BEFORE_APPLY));
		}else {
			/** ??????????????? */
			queryBlocks.put(MitsubishiConst.UNIT_PRICE_BEFORE_REVISION,
					WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION)));
			if (dbType == 3) {
				queryBlocks.put(MitsubishiConst.MANAGER_NO,WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.MANAGER_NO)));
			}
		}
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.APPLICATION_REC_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.APPLICATION_REC_NO)));
		//Pending 
		/** ????????????URL */
		queryBlocks.put(MitsubishiConst.NEW_APPLICATION_URL,
				WebDbUtils.createRecordURL(getUrlStringByMode(recordObj, MitsubishiConst.MODE_NEW)));
		if (mode ==3) {
			/** ????????????URL */
			queryBlocks.put(MitsubishiConst.EDIT_REQUEST_URL,"");
			/** ????????????URL */
			queryBlocks.put(MitsubishiConst.CANCEL_REQUEST_URL,"");
		}else {
			/** ????????????URL */
			queryBlocks.put(MitsubishiConst.EDIT_REQUEST_URL,
					WebDbUtils.createRecordURL(getUrlStringByMode(recordObj, MitsubishiConst.MODE_EDIT)));
			/** ????????????URL */
			queryBlocks.put(MitsubishiConst.CANCEL_REQUEST_URL,
					WebDbUtils.createRecordURL(getUrlStringByMode(recordObj, MitsubishiConst.MODE_CANCEL)));
		}
		
		/** ????????????????????????????????? */
		queryBlocks.put(MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME)));
		/** ??????CD */
		queryBlocks.put(MitsubishiConst.COMPANY_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.COMPANY_CD)));
		/** ??????CD */
		queryBlocks.put(MitsubishiConst.TRANSACTION_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.TRANSACTION_CD)));
		/** ????????????CD */
		queryBlocks.put(MitsubishiConst.SALES_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SALES_DEPARTMENT_CD)));
		/** ????????????CD */
		queryBlocks.put(MitsubishiConst.UPPER_CATEGORY_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.UPPER_CATEGORY_CD)));
		/** ????????????CD */
		queryBlocks.put(MitsubishiConst.ACCOUNT_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.ACCOUNT_DEPARTMENT_CD)));
		/** ??????NO */
		queryBlocks.put(MitsubishiConst.ORDER_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.ORDER_NO)));
		/** ????????????NO */
		queryBlocks.put(MitsubishiConst.SALES_ORDER_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SALES_ORDER_NO)));
		/** ?????????CD */
		queryBlocks.put(MitsubishiConst.CUSTOMER_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CUSTOMER_CD)));
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.CUSTOMER_NAME,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CUSTOMER_NAME)));
		/** ?????????CD1 */
		queryBlocks.put(MitsubishiConst.DESTINATION_CD1,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD1)));
		/** ????????????1 */
		queryBlocks.put(MitsubishiConst.DESTINATION_NAME1,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_NAME1)));
		/** ?????????CD2 */
		queryBlocks.put(MitsubishiConst.DESTINATION_CD2,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD2)));
		/** ????????????2 */
		queryBlocks.put(MitsubishiConst.DESTINATION_NAME2,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_NAME2)));
		/** ?????????CD */
		queryBlocks.put(MitsubishiConst.DESTINATION_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD)));
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.DELIVERY_DESTINATION_NAME,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DELIVERY_DESTINATION_NAME)));
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.PRODUCT_NAME_ABBREVIATION,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PRODUCT_NAME_ABBREVIATION)));
		/** ?????????NO */
		queryBlocks.put(MitsubishiConst.COLOR_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.COLOR_NO)));
		/** ????????????1 */
		queryBlocks.put(MitsubishiConst.GRADE_1,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.GRADE_1)));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.USER_PRODUCT_NAME,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.USER_ITEM)));
		/** ??????CD */
		queryBlocks.put(MitsubishiConst.CURRENCY_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CURRENCY_CD)));
		/** ????????????CD */
		queryBlocks.put(MitsubishiConst.TRANSACTION_UNIT_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.TRANSACTION_UNIT_CD)));
		/** ?????? */
		queryBlocks.put(MitsubishiConst.PACKING,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PACKING)));
		/** ??????????????? */
		queryBlocks.put(MitsubishiConst.CLIENT_BRANCH_NUMBER,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CLIENT_BRANCH_NUMBER)));
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.PRICE_FORM,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PRICE_FORM)));
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.USAGE_REF,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.USAGE_REF)));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.SCHEDULED_DELIVERY_DATE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SCHEDULED_DELIVERY_DATE)));
		/** ????????? */
		queryBlocks.put(MitsubishiConst.ORDER_DATE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.ORDER_DATE)));
		/** ??????????????? */
		queryBlocks.put(MitsubishiConst.REGISTRAR,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.REGISTRAR)));
		/** ?????? */
		queryBlocks.put(MitsubishiConst.WARNING,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.WARNING)));
		/** ??????????????? */
		queryBlocks.put(MitsubishiConst.LOT_QUANTITY,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.LOT_QUANTITY)));
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.RETAIL_PRICE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.RETAIL_PRICE)));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.UNIT_PRICE_SMALL_PARCEL,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL)));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR)));
		/** ???????????????(??????)*/
		queryBlocks.put(MitsubishiConst.PRIMARY_STORE_OPEN_AMOUNT, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PRIMARY_STORE_OPEN_AMOUNT)));
		/** ????????????????????? */
		queryBlocks.put(MitsubishiConst.PRIMARY_STORE_COMMISSION_AMOUNT, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PRIMARY_STORE_COMMISSION_AMOUNT)));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.PRIMARY_STORE_OPENING_RATE, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PRIMARY_STORE_OPENING_RATE)));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.SECOND_STORE_OPEN_AMOUNT, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SECOND_STORE_OPEN_AMOUNT)));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.SECOND_STORE_OPEN_RATE, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SECOND_STORE_OPEN_RATE)));
		/** ?????????????????? */
		queryBlocks.put(MitsubishiConst.TOTAL_RETAIL_PRICE, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.TOTAL_RETAIL_PRICE)));
		/** ??????????????????????????? */
		queryBlocks.put(MitsubishiConst.PARTITION_UNIT_PRICE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PARTITION_UNIT_PRICE)));
		/** ????????????????????? */
		queryBlocks.put(MitsubishiConst.DATE_UPDATE_CATEGORY,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DATE_UPDATE_CATEGORY)));
		/** ??????????????? */
		queryBlocks.put(MitsubishiConst.APPLICATION_START_DATE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.APPLICATION_START_DATE)));
		/** ??????????????? */
		queryBlocks.put(MitsubishiConst.APPLICATION_END_DATE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.APPLICATION_END_DATE)));
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.CONTRACT_NUMBER,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CONTRACT_NUMBER)));
		/** ???????????? */
		queryBlocks.put(MitsubishiConst.REASON_FOR_INQUIRY,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.REASON_FOR_INQUIRY)));
		/** ???????????????????????? */
		// queryBlocks.put(MitsubishiConst.CONFIRM_OF_CUSTOMER_REQUIREMENTS, WebDbUtils
		// .createRecordItem(WebDbUtils.getValue(recordObj,
		// MitsubishiConst.CONFIRM_OF_CUSTOMER_REQUIREMENTS)));
		/** ???????????????NO */
		queryBlocks.put(MitsubishiConst.DATA_LINE_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_LINE_NO)));
		/** ?????????NO */
		queryBlocks.put(MitsubishiConst.DATA_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_NO)));
		return queryBlocks;
	}

	/**
	 * get url by mode new,edit,cancel
	 * 
	 * @param webDBClassInfos
	 * @param recordObj
	 * @param mode
	 * @return
	 * @throws JSONException
	 */
	private String getUrlStringByMode(JSONObject recordObj, String modeConst) throws JSONException {

		StringBuilder url = new StringBuilder(messages.get(MitsubishiConst.PE4J_PROPERTIES));

		if (MitsubishiConst.MODE_NEW.equals(modeConst)) {
			url.append(MessageFormat.format(messages.get(MitsubishiConst.URL_PROPERTIES),
					WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_NO), MitsubishiConst.MODE_NEW));
		}
		if (MitsubishiConst.MODE_EDIT.equals(modeConst)) {
			url.append(MessageFormat.format(messages.get(MitsubishiConst.URL_PROPERTIES),
					WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_NO), MitsubishiConst.MODE_EDIT));
		}
		if (MitsubishiConst.MODE_CANCEL.equals(modeConst)) {
			url.append(MessageFormat.format(messages.get(MitsubishiConst.URL_PROPERTIES),
					WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_NO), MitsubishiConst.MODE_CANCEL));
		}

		return url.toString();
	}

	/**
	 * update record by mode
	 * 
	 * @param webDBClassInfos
	 * @param appRecepNo
	 * @param statusCd
	 * @param recordNo
	 * @param mode
	 * @throws Exception
	 */
	private void updateRecordDb(List<ClassInfo> webDBClassInfos, String appRecepNo, String statusCd, String recordNo,
			int mode) throws Exception {
		WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0, mode);
		// chuan bi du lieu jsonobject
		JSONObject updateRecord = new JSONObject();
		updateRecord.put(MitsubishiConst.APPLICATION_REC_NO, WebDbUtils.createRecordItem(appRecepNo));
		updateRecord.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(statusCd));
		// update th??ng qua rest api
		webdbUtils.putJsonObject(updateRecord, recordNo, false, false, false);
	}

	/**
	 * @param ext
	 */
	private String createFileName(String ext) {
		String tenPuDir = PESystemProperties.getInstance().getProperty(MitsubishiConst.TENPU_DIR);
		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		if (ConvertUtils.isNullOrEmptyOrBlank(tenPuDir)) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(tenPuDir);
		buffer.append(MitsubishiConst.SEPARATOR);
		buffer.append(LogUtils.getCharData1(webDBClassInfos, MitsubishiConst.CLASS_NO.CLASSNO_10.getValue()));
		buffer.append(ext);
		return buffer.toString();
	}
}
