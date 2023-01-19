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
import net.poweregg.mitsubishi.constant.MitsubishiConst.CLASS_NO;
import net.poweregg.mitsubishi.service.MitsubishiService;
import net.poweregg.mitsubishi.webdb.utils.ConvertUtils;
import net.poweregg.mitsubishi.webdb.utils.LogUtils;
import net.poweregg.mitsubishi.webdb.utils.WebDbConstant;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;
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
		WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0, 0);
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);

		LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), MitsubishiConst.LOG_BEGIN);
		// get list json records and find data of temp from appRecepNo
		JSONArray resultJson = new JSONArray();

		try {

			// find record data at webDB price
			resultJson = mitsubishiService.findDataUmbByCondition(webdbUtils, MitsubishiConst.APPLICATION_REC_NO,
					StringUtils.toEmpty(appRecepNo));
			JSONObject recordObj = resultJson.getJSONObject(0);

			// withdraw
			if (ConstStatus.STATUS_BACKED_AWAY.equals(status)) {

				// mode new
				if (0 == mode) {
					updateRecordDb(webDBClassInfos, StringUtils.toEmpty(appRecepNo), status,
							WebDbUtils.getValue(recordObj, MitsubishiConst.NO), 0);
				}
				// mode edit
				if (1 == mode) {
					// update 申請受付番号 in temp webDB: apply
					updateRecordDb(webDBClassInfos, StringUtils.toEmpty(appRecepNo), status,
							WebDbUtils.getValue(recordObj, MitsubishiConst.NO), 0);
					// update 申請受付番号 in master webDB: withraw
					updateRecordDb(webDBClassInfos, StringUtils.toEmpty(appRecepNo), ConstStatus.STATUS_BACKED_AWAY,
							WebDbUtils.getValue(recordObj, MitsubishiConst.NO), 1);
				}
			}

			// approval
			if (ConstStatus.STATUS_APPROVED.equals(status)) {

				JSONObject queryBlocks = createJsonQuery(webDBClassInfos, recordObj, mode, status);
				// insert at master webDB
				webdbUtils = new WebDbUtils(webDBClassInfos, 0, 1);

				// mode edit
				if (1 == mode) {

					// delete old record
					webdbUtils.delJsonObject(WebDbUtils.getValue(recordObj, MitsubishiConst.NO));
					// insert at history webDB
					webdbUtils = new WebDbUtils(webDBClassInfos, 0, 2);

				}
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
	 * get url by mode new,edit,cancel
	 * 
	 * @param webDBClassInfos
	 * @param recordObj
	 * @param mode
	 * @return
	 * @throws JSONException
	 */
	private String getUrlStringByMode(List<ClassInfo> webDBClassInfos, JSONObject recordObj, String modeConst)
			throws JSONException {

		StringBuilder url = new StringBuilder(
				WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_1.getValue()));

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
	 * create object query
	 * 
	 * @param recordObj
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	private JSONObject createJsonQuery(List<ClassInfo> webDBClassInfos, JSONObject recordObj, int mode, String status)
			throws JSONException, Exception {

		JSONObject queryBlocks = new JSONObject();
		// mode new
		if (0 == mode) {
			/** 改定前単価 */
			queryBlocks.put(MitsubishiConst.UNIT_PRICE_BEFORE_REVISION, WebDbUtils
					.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION)));
			/** 管理No */
			queryBlocks.put(MitsubishiConst.MANAGER_NO,
					WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.MANAGER_NO)));
			/** 新規申請URL */
			queryBlocks.put(MitsubishiConst.NEW_APPLICATION_URL, WebDbUtils
					.createRecordURL(getUrlStringByMode(webDBClassInfos, recordObj, MitsubishiConst.MODE_NEW)));
		}
		// mode edit
		if (1 == mode) {
			/** 改定前単価 */
			queryBlocks.put(MitsubishiConst.UNIT_PRICE_BEFORE_REVISION,
					WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PARTITION_UNIT_PRICE)));
			// Thực hiện dữ liệu tham khảo +1 cho 「管理No」
			int managerNO = Integer.parseInt(WebDbUtils.getValue(recordObj, MitsubishiConst.MANAGER_NO)) + 1;
			queryBlocks.put(MitsubishiConst.MANAGER_NO, WebDbUtils.createRecordItem(StringUtils.toEmpty(managerNO)));
			/** 申請受付番号 */
			queryBlocks.put(MitsubishiConst.APPLICATION_REC_NO,
					WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, status)));
			/** 廃止申請URL */
			queryBlocks.put(MitsubishiConst.CANCEL_REQUEST_URL, WebDbUtils
					.createRecordURL(getUrlStringByMode(webDBClassInfos, recordObj, MitsubishiConst.MODE_CANCEL)));
		}

		/** 編集申請URL */
		queryBlocks.put(MitsubishiConst.EDIT_REQUEST_URL,
				WebDbUtils.createRecordURL(getUrlStringByMode(webDBClassInfos, recordObj, MitsubishiConst.MODE_EDIT)));
		/** 送信元レコード作成日時 */
		queryBlocks.put(MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME)));
		/** 会社CD */
		queryBlocks.put(MitsubishiConst.COMPANY_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.COMPANY_CD)));
		/** 取引CD */
		queryBlocks.put(MitsubishiConst.TRANSACTION_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.TRANSACTION_CD)));
		/** 売上部門CD */
		queryBlocks.put(MitsubishiConst.SALES_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SALES_DEPARTMENT_CD)));
		/** 上位部門CD */
		queryBlocks.put(MitsubishiConst.UPPER_CATEGORY_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.UPPER_CATEGORY_CD)));
		/** 会計部門CD */
		queryBlocks.put(MitsubishiConst.ACCOUNT_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.ACCOUNT_DEPARTMENT_CD)));
		/** 受注NO */
		queryBlocks.put(MitsubishiConst.ORDER_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.ORDER_NO)));
		/** 受注明細NO */
		queryBlocks.put(MitsubishiConst.SALES_ORDER_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SALES_ORDER_NO)));
		/** 得意先CD */
		queryBlocks.put(MitsubishiConst.TRANSACTION_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.TRANSACTION_CD)));
		/** 得意先名 */
		queryBlocks.put(MitsubishiConst.CUSTOMER_NAME,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CUSTOMER_NAME)));
		/** 仕向先CD1 */
		queryBlocks.put(MitsubishiConst.DESTINATION_CD1,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD1)));
		/** 仕向先名1 */
		queryBlocks.put(MitsubishiConst.DESTINATION_NAME1,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_NAME1)));
		/** 仕向先CD2 */
		queryBlocks.put(MitsubishiConst.DESTINATION_CD2,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD2)));
		/** 仕向先名2 */
		queryBlocks.put(MitsubishiConst.DESTINATION_NAME2,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_NAME2)));
		/** 納品先CD */
		queryBlocks.put(MitsubishiConst.DESTINATION_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD)));
		/** 納品先名 */
		queryBlocks.put(MitsubishiConst.DELIVERY_DESTINATION_NAME,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DELIVERY_DESTINATION_NAME)));
		/** 品名略号 */
		queryBlocks.put(MitsubishiConst.PRODUCT_NAME_ABBREVIATION,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PRODUCT_NAME_ABBREVIATION)));
		/** カラーNO */
		queryBlocks.put(MitsubishiConst.COLOR_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.COLOR_NO)));
		/** グレード1 */
		queryBlocks.put(MitsubishiConst.GRADE_1,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.GRADE_1)));
		/** ユーザー品目 */
		queryBlocks.put(MitsubishiConst.USER_PRODUCT_NAME,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.USER_ITEM)));
		/** 通貨CD */
		queryBlocks.put(MitsubishiConst.CURRENCY_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CURRENCY_CD)));
		/** 取引単位CD */
		queryBlocks.put(MitsubishiConst.TRANSACTION_UNIT_CD,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.TRANSACTION_UNIT_CD)));
		/** 荷姿 */
		queryBlocks.put(MitsubishiConst.PACKING,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PACKING)));
		/** 取引先枝番 */
		queryBlocks.put(MitsubishiConst.CLIENT_BRANCH_NUMBER,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CLIENT_BRANCH_NUMBER)));
		/** 価格形態 */
		queryBlocks.put(MitsubishiConst.PRICE_FORM,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PRICE_FORM)));
		/** 用途参照 */
		queryBlocks.put(MitsubishiConst.USAGE_REF,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.USAGE_REF)));
		/** 納品予定日時 */
		queryBlocks.put(MitsubishiConst.SCHEDULED_DELIVERY_DATE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.SCHEDULED_DELIVERY_DATE)));
		/** 受注日 */
		queryBlocks.put(MitsubishiConst.ORDER_DATE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.ORDER_DATE)));
		/** 登録担当者 */
		queryBlocks.put(MitsubishiConst.REGISTRAR,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.REGISTRAR)));
		/** 警告 */
		queryBlocks.put(MitsubishiConst.WARNING,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.WARNING)));
		/** ロット数量 */
		queryBlocks.put(MitsubishiConst.LOT_QUANTITY,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.LOT_QUANTITY)));
		/** 末端価格 */
		queryBlocks.put(MitsubishiConst.RETAIL_PRICE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.RETAIL_PRICE)));
		/** 小口配送単価 */
		queryBlocks.put(MitsubishiConst.UNIT_PRICE_SMALL_PARCEL,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL)));
		/** 小口着色単価 */
		queryBlocks.put(MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR)));
		/** 一次店口銭金額 */
		queryBlocks.put(MitsubishiConst.PRIMARY_STORE_COMMISSION_AMOUNT, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PRIMARY_STORE_COMMISSION_AMOUNT)));
		/** 一次店口銭率 */
		queryBlocks.put(MitsubishiConst.PRIMARY_STORE_OPENING_RATE, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PRIMARY_STORE_OPENING_RATE)));
		/** 仕切単価（決定値） */
		queryBlocks.put(MitsubishiConst.PARTITION_UNIT_PRICE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.PARTITION_UNIT_PRICE)));
		/** データ更新区分 */
		queryBlocks.put(MitsubishiConst.DATE_UPDATE_CATEGORY,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DATE_UPDATE_CATEGORY)));
		/** 適用開始日 */
		queryBlocks.put(MitsubishiConst.APPLICATION_START_DATE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.APPLICATION_START_DATE)));
		/** 適用終了日 */
		queryBlocks.put(MitsubishiConst.APPLICATION_END_DATE,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.APPLICATION_END_DATE)));
		/** 契約番号 */
		queryBlocks.put(MitsubishiConst.CONTRACT_NUMBER,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CONTRACT_NUMBER)));
		/** 伺い理由 */
		queryBlocks.put(MitsubishiConst.REASON_FOR_INQUIRY,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.REASON_FOR_INQUIRY)));
		/** 顧客要求事項確認 */
		queryBlocks.put(MitsubishiConst.CONFIRM_OF_CUSTOMER_REQUIREMENTS, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.CONFIRM_OF_CUSTOMER_REQUIREMENTS)));
		/** 改定前単価 */
		queryBlocks.put(MitsubishiConst.UNIT_PRICE_BEFORE_REVISION, WebDbUtils
				.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION)));
		/** 状態CD */
		queryBlocks.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(ConstStatus.STATUS_BEFORE_APPLY));
		/** データ移行NO */
		queryBlocks.put(MitsubishiConst.DATA_LINE_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_LINE_NO)));
		/** データNO */
		queryBlocks.put(MitsubishiConst.DATA_NO,
				WebDbUtils.createRecordItem(WebDbUtils.getValue(recordObj, MitsubishiConst.DATA_NO)));

		return queryBlocks;
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
		// chuan bi du lieu jsonobject dể
		JSONObject updateRecord = new JSONObject();
		updateRecord.put(MitsubishiConst.APPLICATION_REC_NO, WebDbUtils.createRecordItem(appRecepNo));
		updateRecord.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(statusCd));
		// update thông qua rest api
		webdbUtils.putJsonObject(updateRecord, recordNo, false, false, false);
	}
}
