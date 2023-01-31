package net.poweregg.mitsubishi.action;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
import javax.xml.transform.TransformerException;

import org.json.JSONException;
import org.json.JSONObject;

import net.poweregg.annotations.Login;
import net.poweregg.annotations.PEIntercepter;
import net.poweregg.annotations.RequestParameter;
import net.poweregg.annotations.Single;
import net.poweregg.common.ClassificationService;
import net.poweregg.common.entity.ClassInfo;
import net.poweregg.dataflow.ApplyException;
import net.poweregg.dataflow.ConstStatus;
import net.poweregg.dataflow.DataFlowService;
import net.poweregg.dataflow.DataFlowUtil;
import net.poweregg.dataflow.DataflowHelperBean;
import net.poweregg.dataflow.entity.ApplicationForm;
import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.mitsubishi.constant.MitsubishiConst.CLASS_NO;
import net.poweregg.mitsubishi.constant.MitsubishiConst.COMMON_NO;
import net.poweregg.mitsubishi.dto.UMB01TempDto;
import net.poweregg.mitsubishi.dto.Umb01Dto;
import net.poweregg.mitsubishi.service.MitsubishiService;
import net.poweregg.mitsubishi.webdb.utils.CSVUtils;
import net.poweregg.mitsubishi.webdb.utils.ConvertUtils;
import net.poweregg.mitsubishi.webdb.utils.LogUtils;
import net.poweregg.mitsubishi.webdb.utils.WebDbConstant;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;
import net.poweregg.organization.entity.Employee;
import net.poweregg.seam.faces.FacesMessages;
import net.poweregg.ui.param.AttachFile;
import net.poweregg.ui.param.DateRange;
import net.poweregg.util.DateUtils;
import net.poweregg.util.FacesContextUtils;
import net.poweregg.util.NumberUtils;
import net.poweregg.util.StringUtils;
import net.poweregg.web.engine.navigation.LoginUser;
import net.poweregg.webdb.util.ArrayCollectionUtil;

@Named("UMB01Bean")
@ConversationScoped
@PEIntercepter
public class UMB01Bean implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String PRIORITY_USUAL = "0001";
	private static final String FILE_XML = "umb01.xsl";

	@EJB
	private ClassificationService classificationService;

	@EJB
	private MitsubishiService mitsubishiService;

	@EJB
	private DataFlowService flowService;

	private Integer returnCode;

	private String csvFilePath;

	private Umb01Dto umb01Dto;

	@RequestParameter(value = "datano")
	private String dataNo;
	
	@RequestParameter(value = "mode")
	private String mode;

	@Inject
	@Single
	private transient Map<String, String> messages;

	@Inject
	@Login
	private LoginUser loginUser;

	@Inject
	private DataflowHelperBean dataflowHelper;

	@Inject
	private transient FacesMessages facesMessages;

	// 申請受付番号
	private Long appRecepNo;

	private String fileUrlPath;

	private String selectEmp = "";

	private Employee emp;

	/** 申請日 */
	private Date applyDate;
	/** 件名 */
	private String titleApply;
	/** 優先度 */
	private String priority;

	private String paperDocument;
	private List<AttachFile> attachFileList;

	private List<SelectItem> transactionList;
	private List<SelectItem> dataUpdateCategoryList;
	private List<SelectItem> reasonInquiryList;
	private List<SelectItem> retroactiveClassificationList;
	private List<SelectItem> customerReqConfirmList;

	private String unitPriceDataRef;
	private String priceDataRef;
	private String usageRef;

	private String outputHtml;
	private DateRange dateRange;
	private String currentMode;

	public String initUMB0102e() throws Exception {
		
		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);
		
		if (loginUser == null) {
			return "login";
		}
		
		selectEmp = "0";
		emp = loginUser.getCurrentLoginInfo().getEmployee();
		applyDate = new Date();
		titleApply = "";
		priority = PRIORITY_USUAL;
		paperDocument = "";
		attachFileList = null;
		unitPriceDataRef = "";
		priceDataRef = "";
		usageRef = "";
		currentMode = mode;

		if (MitsubishiConst.MODE_NEW.equals(mode)) {
			initApplyScreenByDbType(1);
			
			if (checkStatusCdApplyed(umb01Dto.getStatusCD())) {
				LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), " Error: This data has been applied");
				facesMessages.add(FacesMessage.SEVERITY_ERROR, "申請しました。", "");
				return StringUtils.EMPTY;
			}
			
			return StringUtils.EMPTY;
		}
		if (MitsubishiConst.MODE_EDIT.equals(mode) || MitsubishiConst.MODE_CANCEL.equals(mode)) {
			initApplyScreenByDbType(2);
			
			if (checkStatusCdApplyed(umb01Dto.getStatusCD()) || checkStatusCdApplyed(umb01Dto.getStatusCD())) {
				LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), " Error: This data has been applied");
				facesMessages.add(FacesMessage.SEVERITY_ERROR, "申請しました。", "");
				return StringUtils.EMPTY;
			}
			
			return StringUtils.EMPTY;
		}
		
		LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "This mode doesn't exist");
		throw new Exception("This mode doesn't exist");
	}
	
	private void initApplyScreenByDbType(int dbType) throws Exception {
		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);

		// get value from WebDB
		umb01Dto = mitsubishiService.getDataMitsubishi(dataNo, dbType);
		if (umb01Dto == null) {
			LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "Error:" + dataNo + "not exist");
			throw new Exception("Error: " + MitsubishiConst.DATA_NO + " " + dataNo + " not exist");
		}

		Date nowDate = new Date();
		dateRange = new DateRange();
		dateRange.setStartDate(DateUtils.addDate(nowDate, "MONTH", -1));
		dateRange.setEndDate(nowDate);

		if (umb01Dto.getPriceRefDto().getApplicationStartDate() != null) {
			dateRange.setStartDate(umb01Dto.getPriceRefDto().getApplicationStartDate());
		}
		if (umb01Dto.getPriceRefDto().getApplicationEndDate() != null) {
			dateRange.setStartDate(umb01Dto.getPriceRefDto().getApplicationEndDate());
		}

		// make XML table price
		outputHtml = new DataFlowUtil().transformXML2HTML(mitsubishiService.createXMLTablePrice(umb01Dto), FILE_XML);
	}

	/**
	 * 
	 * 
	 * @return "confirm"
	 */
	public String toConfirm() {
		umb01Dto.getPriceRefDto().setApplicationStartDate(dateRange.getStartDate());
		umb01Dto.getPriceRefDto().setApplicationEndDate(dateRange.getEndDate());
		
		// ワークフローパラメタ編集
		dataflowHelper.setApplyDate(getToday());
		dataflowHelper.setTitle(titleApply);
		if (MitsubishiConst.MODE_NEW.equals(currentMode)) {
			dataflowHelper.setBaseForm("U901");
			dataflowHelper.setXslFileName("umb02.xsl");
		}
		if (MitsubishiConst.MODE_EDIT.equals(currentMode)) {
			dataflowHelper.setBaseForm("U902");
			dataflowHelper.setXslFileName("umb03.xsl");
		}
		if (MitsubishiConst.MODE_CANCEL.equals(currentMode)) {
			dataflowHelper.setBaseForm("U903");
			dataflowHelper.setXslFileName("umb03.xsl");
		}
		dataflowHelper.setApplicant(loginUser.getCorpID(), loginUser.getDeptID(), loginUser.getEmpID());
		dataflowHelper.setApplyCondition("a");
		dataflowHelper.setRouteEdit(true); // ルート変更可能に設定
		dataflowHelper.setApplyContent(mitsubishiService.createXMLTablePrice(umb01Dto));

		try {
			appRecepNo = dataflowHelper.prepareApply();
			facesMessages.add("申請してよろしいですか？");
			return "confirm";
		} catch (ApplyException e) {
			return StringUtils.EMPTY;
		}
	}

	/**
	 * 
	 * 
	 * @return "apply"
	 * @throws Exception 
	 **/
	public String apply() throws Exception {
		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);
		
		if (checkStatusCdApplyed(umb01Dto.getStatusCD())) {
			LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), " Error: This data has been applied");
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "申請しました。", "");
			return StringUtils.EMPTY;
		}
		
		try {
			// 申請確定
			dataflowHelper.apply();
			// 自分のデータを保存する.
			ApplicationForm appForm = flowService.findApplicationFormByRecpNo(appRecepNo);
			String recordNo = umb01Dto.getId();
			
			if (MitsubishiConst.MODE_NEW.equals(currentMode)) {
				mitsubishiService.updateRecordDbPrice(recordNo, appRecepNo.toString(), appForm.getStatus(), 1);
			}
			if (MitsubishiConst.MODE_EDIT.equals(currentMode) || MitsubishiConst.MODE_CANCEL.equals(currentMode)) {
				mitsubishiService.updateRecordDbPrice(recordNo, appRecepNo.toString(), appForm.getStatus(), 2);
			}

			facesMessages.add(FacesMessage.SEVERITY_ERROR, "申請しました。", "");
			dataflowHelper.reset();

			return "apply";
		} catch (ApplyException ex) {
			return StringUtils.EMPTY;
		}
	}

	/**
	 * 
	 * 
	 * @return "return"
	 **/
	public String page0102eReturn() {
		FacesContextUtils.putToFlash("return", true);
		return "return";
	}

	/**
	 * 
	 * 
	 * @return "return"
	 **/
	public String page0102cReturn() {
		FacesContextUtils.putToFlash("fromScreen", "UMB0102c");
		FacesContextUtils.putToFlash("return", true);
		return "return";

	}

	/**
	 * 現在時間取得
	 * 
	 * @return
	 */
	private Date getToday() {
		GregorianCalendar calelder = new GregorianCalendar();
		calelder.setTime(new Date());
		calelder.set(Calendar.HOUR_OF_DAY, 0);
		calelder.set(Calendar.MINUTE, 0);
		calelder.set(Calendar.SECOND, 0);
		calelder.set(Calendar.MILLISECOND, 0);
		return calelder.getTime();
	}

	public void executeBatch() throws Exception {
		String logFileFullPath = "";
		try {

			List<ClassInfo> webDBClassInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
					MitsubishiConst.COMMON_NO.COMMON_NO_UMB01.getValue());
			logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);

			LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
					MitsubishiConst.LOG_BEGIN);

			String[] args = getBatchParam();
			csvFilePath = args[0];
			File importFile = new File(csvFilePath);
			if (importFile != null && importFile.length() == 0) {
				System.out.println(MitsubishiConst.ERROR_EMPTY_FILE_CSV);
			}
			try {
				// get content in file CSV
				List<String[]> contentFile = CSVUtils.readAllToListString(importFile, MitsubishiConst.COMMA,
						MitsubishiConst.MS_932, true);
				InitialContext icx = new InitialContext();
				UserTransaction utx = (UserTransaction) icx.lookup("java:comp/UserTransaction");
				List<UMB01TempDto> dataList = new ArrayList<>();

				if (ArrayCollectionUtil.isCollectionNotNullOrEmpty(contentFile)) {
					for (int i = 0; i < contentFile.size(); i++) {
						String[] content = contentFile.get(i);
						UMB01TempDto umitsubishiTemp = createUmitsubishiTemp(content);
						dataList.add(umitsubishiTemp);
					}
				}

				for (int i = 0; i < dataList.size(); i++) {
					UMB01TempDto recordData = dataList.get(i);
					// b.1. Insert 前払勧奨情報 get classInfo by commonNo: UMB01
					WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0, 1);
					String result = webdbUtils.registJsonObject(
							putDataUmitsubishiTemp(webDBClassInfos, recordData, logFileFullPath), true);
					if (!ConvertUtils.isNullOrEmpty(result)) {
						System.out.println(result);
						LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), result);
					}
				}
				utx.commit();
				setReturnCode(MitsubishiConst.RESULT_OK);
			} catch (Exception e) {
				this.setReturnCode(MitsubishiConst.RESULT_EXCEPTION);
				e.printStackTrace();
				throw e;
			}
		} catch (IOException e) {
			e.printStackTrace();
			setReturnCode(MitsubishiConst.RESULT_EXCEPTION);
			LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			setReturnCode(MitsubishiConst.RESULT_EXCEPTION);
			LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), e.getMessage());
		} finally {
			LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
					MitsubishiConst.LOG_FINISH);
		}
	}

	/**
	 * put data to json for insert data UmitsubishiTempDto
	 * 
	 * @param umbItem
	 * @return regDataJson
	 * @throws Exception
	 * @throws JSONException
	 */
	private JSONObject putDataUmitsubishiTemp(List<ClassInfo> webDBClassInfos, UMB01TempDto umbItem,
			String logFileFullPath) throws JSONException, Exception {

		JSONObject regDataJson = new JSONObject();

		StringBuilder getLog = new StringBuilder();
		getLog.append(MitsubishiConst.DATA_NO).append(MitsubishiConst.COLON);

		// log: start data no
		LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
				getLog.append(umbItem.getDataNo()).append(MitsubishiConst.HYPHEN).append(MitsubishiConst.START_LOG)
						.toString());

		/** データ行NO */
		regDataJson.put(MitsubishiConst.DATA_LINE_NO,
				WebDbUtils.createRecordItemNumber(NumberUtils.toLong(umbItem.getDataLineNo())));
		/** データNO */
		regDataJson.put(MitsubishiConst.DATA_NO,
				WebDbUtils.createRecordItemNumber(NumberUtils.toLong(umbItem.getDataNo())));
		/** 管理NO */
		regDataJson.put(MitsubishiConst.MANAGER_NO,
				WebDbUtils.createRecordItemNumber(NumberUtils.toLong(umbItem.getManagerNo())));
		/** 送信元レコード作成日時 */
		regDataJson.put(MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME,
				WebDbUtils.createRecordItem(umbItem.getSrcCreateDate()));
		/** 会社CD */
		regDataJson.put(MitsubishiConst.COMPANY_CD, WebDbUtils.createRecordItem(umbItem.getCompanyCD()));
		/** 取引CD */
		regDataJson.put(MitsubishiConst.TRANSACTION_CD, WebDbUtils.createRecordItem(umbItem.getTransactionCD()));
		/** 売上部門CD */
		regDataJson.put(MitsubishiConst.SALES_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(umbItem.getSalesDepartmentCD()));
		/** 上位部門CD */
		regDataJson.put(MitsubishiConst.UPPER_CATEGORY_CD, WebDbUtils.createRecordItem(umbItem.getUpperCategoryCD()));
		/** 会計部門CD */
		regDataJson.put(MitsubishiConst.ACCOUNT_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(umbItem.getAccountDepartmentCD()));
		/** 受注NO */
		regDataJson.put(MitsubishiConst.ORDER_NO, WebDbUtils.createRecordItem(umbItem.getOrderNo()));
		/** 受注明細NO */
		regDataJson.put(MitsubishiConst.SALES_ORDER_NO, WebDbUtils.createRecordItem(umbItem.getSalesOrderNo()));
		/** 得意先CD */
		regDataJson.put(MitsubishiConst.TRANSACTION_CD, WebDbUtils.createRecordItem(umbItem.getCustomerCD()));
		/** 得意先名 */
		regDataJson.put(MitsubishiConst.CUSTOMER_NAME, WebDbUtils.createRecordItem(umbItem.getCustomerName()));
		/** 仕向先CD1 */
		regDataJson.put(MitsubishiConst.DESTINATION_CD1, WebDbUtils.createRecordItem(umbItem.getDestinationCD1()));
		/** 仕向先名1 */
		regDataJson.put(MitsubishiConst.DESTINATION_NAME1, WebDbUtils.createRecordItem(umbItem.getDestinationName1()));
		/** 仕向先CD2 */
		regDataJson.put(MitsubishiConst.DESTINATION_CD2, WebDbUtils.createRecordItem(umbItem.getDestinationCD2()));
		/** 仕向先名2 */
		regDataJson.put(MitsubishiConst.DESTINATION_NAME2, WebDbUtils.createRecordItem(umbItem.getDestinationName2()));
		/** 納品先CD */
		regDataJson.put(MitsubishiConst.DESTINATION_CD, WebDbUtils.createRecordItem(umbItem.getDestinationCD()));
		/** 納品先名 */
		regDataJson.put(MitsubishiConst.DELIVERY_DESTINATION_NAME,
				WebDbUtils.createRecordItem(umbItem.getDeliveryDestinationName()));
		/** 品名略号 */
		regDataJson.put(MitsubishiConst.PRODUCT_NAME_ABBREVIATION,
				WebDbUtils.createRecordItem(umbItem.getProductNameAbbreviation()));
		/** カラーNO */
		regDataJson.put(MitsubishiConst.COLOR_NO, WebDbUtils.createRecordItem(umbItem.getColorNo()));
		/** グレード1 */
		regDataJson.put(MitsubishiConst.GRADE_1, WebDbUtils.createRecordItem(umbItem.getGrade1()));
		/** ユーザー品目 */
		regDataJson.put(MitsubishiConst.USER_ITEM, WebDbUtils.createRecordItem(umbItem.getUserItem()));
		/** 通貨CD */
		regDataJson.put(MitsubishiConst.CURRENCY_CD, WebDbUtils.createRecordItem(umbItem.getCurrencyCD()));
		/** 取引単位CD */
		regDataJson.put(MitsubishiConst.TRANSACTION_UNIT_CD,
				WebDbUtils.createRecordItem(umbItem.getTransactionUnitCD()));
		/** 荷姿 */
		regDataJson.put(MitsubishiConst.PACKING, WebDbUtils.createRecordItem(umbItem.getPacking()));
		/** 取引先枝番 */
		regDataJson.put(MitsubishiConst.CLIENT_BRANCH_NUMBER,
				WebDbUtils.createRecordItem(umbItem.getClientBranchNumber()));
		/** 価格形態 */
		regDataJson.put(MitsubishiConst.PRICE_FORM, WebDbUtils.createRecordItem(umbItem.getPriceForm()));
		/** 用途参照 */
		regDataJson.put(MitsubishiConst.USAGE_REF, WebDbUtils.createRecordItem(umbItem.getUsageRef()));
		/** 納品予定日時 */
		regDataJson.put(MitsubishiConst.SCHEDULED_DELIVERY_DATE,
				WebDbUtils.createRecordItem(umbItem.getDeliveryDate()));
		/** 品名分類CD1 */
		regDataJson.put(MitsubishiConst.PRODUCT_NAME_CLASS_CD1,
				WebDbUtils.createRecordItem(umbItem.getProductNameClassCD1()));
		/** 受注日 */
		regDataJson.put(MitsubishiConst.ORDER_DATE, WebDbUtils.createRecordItem(umbItem.getOrderDate()));
		/** 売上担当者CD */
		regDataJson.put(MitsubishiConst.SALESPERSON_CD, WebDbUtils.createRecordItem(umbItem.getSalespersonCD()));
		/** 売上担当者名 */
		regDataJson.put(MitsubishiConst.SALESPERSON_NAME, WebDbUtils.createRecordItem(umbItem.getSalespersonName()));
		/** 価格伺いマスタ申請(新規) */
		StringBuilder url = new StringBuilder();
		url.append(WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_1.getValue())).append(MessageFormat
				.format(messages.get(MitsubishiConst.URL_PROPERTIES), umbItem.getDataNo(), MitsubishiConst.MODE_NEW));
		regDataJson.put(MitsubishiConst.NEW_PRICE_INQUIRY_MASTER_APPLICATION,
				WebDbUtils.createRecordURL(url.toString()));

		// log: end data no
		LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), getLog
				.append(umbItem.getDataNo()).append(MitsubishiConst.HYPHEN).append(MitsubishiConst.END_LOG).toString());

		return regDataJson;
	}

	/**
	 * set value for each other record in UmitsubishiTemp from content CSV
	 * 
	 * @param content
	 * @return
	 * @throws ParseException
	 */
	private UMB01TempDto createUmitsubishiTemp(String[] content) throws ParseException {

		int j = 0;
		UMB01TempDto umitsubishiTempDto = new UMB01TempDto();

		/** データ行NO */
		umitsubishiTempDto.setDataLineNo(content[j++]);
		/** データNO */
		umitsubishiTempDto.setDataNo(content[j++]);
		/** 管理NO */
		umitsubishiTempDto.setManagerNo(content[j++]);
		/** 送信元レコード作成日時 */
		umitsubishiTempDto.setSrcCreateDate(content[j++]);
		/** 会社CD */
		umitsubishiTempDto.setCompanyCD(content[j++]);
		/** 取引CD */
		umitsubishiTempDto.setTransactionCD(content[j++]);
		/** 売上部門CD */
		umitsubishiTempDto.setSalesDepartmentCD(content[j++]);
		/** 上位部門CD */
		umitsubishiTempDto.setUpperCategoryCD(content[j++]);
		/** 会計部門CD */
		umitsubishiTempDto.setAccountDepartmentCD(content[j++]);
		/** 受注NO */
		umitsubishiTempDto.setOrderNo(content[j++]);
		/** 受注明細NO */
		umitsubishiTempDto.setSalesOrderNo(content[j++]);
		/** 得意先CD */
		umitsubishiTempDto.setCustomerCD(content[j++]);
		/** 得意先名 */
		umitsubishiTempDto.setCustomerName(content[j++]);
		/** 仕向先CD1 */
		umitsubishiTempDto.setDestinationCD1(content[j++]);
		/** 仕向先名1 */
		umitsubishiTempDto.setDestinationName1(content[j++]);
		/** 仕向先CD2 */
		umitsubishiTempDto.setDestinationCD2(content[j++]);
		/** 仕向先名2 */
		umitsubishiTempDto.setDestinationName2(content[j++]);
		/** 納品先CD */
		umitsubishiTempDto.setDestinationCD(content[j++]);
		/** 納品先名 */
		umitsubishiTempDto.setDeliveryDestinationName(content[j++]);
		/** 品名略号 */
		umitsubishiTempDto.setProductNameAbbreviation(content[j++]);
		/** カラーNO */
		umitsubishiTempDto.setColorNo(content[j++]);
		/** グレード1 */
		umitsubishiTempDto.setGrade1(content[j++]);
		/** ユーザー品目 */
		umitsubishiTempDto.setUserItem(content[j++]);
		/** 通貨CD */
		umitsubishiTempDto.setCurrencyCD(content[j++]);
		/** 取引単位CD */
		umitsubishiTempDto.setTransactionUnitCD(content[j++]);
		/** 荷姿 */
		umitsubishiTempDto.setPacking(content[j++]);
		/** 取引先枝番 */
		umitsubishiTempDto.setClientBranchNumber(content[j++]);
		/** 価格形態 */
		umitsubishiTempDto.setPriceForm(content[j++]);
		/** 用途参照 */
		umitsubishiTempDto.setUsageRef(content[j++]);
		/** 納品予定日時 */
		umitsubishiTempDto.setDeliveryDate(content[j++]);
		/** 品名分類CD1 */
		umitsubishiTempDto.setProductNameClassCD1(content[j++]);
		/** 受注日 */
		umitsubishiTempDto.setOrderDate(content[j++]);
		/** 登録担当者 */
		umitsubishiTempDto.setRegistrar(content[j++]);
		/** 売上担当者CD */
		umitsubishiTempDto.setSalespersonCD(content[j++]);
		/** 売上担当者名 */
		umitsubishiTempDto.setSalespersonName(content[j++]);

		return umitsubishiTempDto;

	}

	/**
	 * 出力パラメタを作成する
	 *
	 * @return 出力パラメタ
	 */
	private String[] getBatchParam() {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> reqMap = context.getExternalContext().getRequestParameterMap();

		String[] args = new String[1];

		args[0] = reqMap.get("csvFilePath");

		return args;
	}

	/**
	 * @throws TransformerException 
	 *
	 *
	 *
	 */
	public void calculateValue() throws TransformerException {
		// 末端価格
		BigDecimal retailPrice = umb01Dto.getPriceRefDto().getRetailPrice();
		// 小口配送単価
		BigDecimal unitPriceSmallParcel = umb01Dto.getPriceRefDto().getUnitPriceSmallParcel();
		// 小口着色単価
		BigDecimal unitPriceForeheadColor = umb01Dto.getPriceRefDto().getUnitPriceForeheadColor();
		// 一次店口銭率
		BigDecimal primaryStoreOpenRate = umb01Dto.getPriceRefDto().getPrimaryStoreOpenRate();
		// 一次店口銭金額
		BigDecimal primaryStoreCommissionAmount = umb01Dto.getPriceRefDto().getPrimaryStoreCommissionAmount();
		// 二次店口銭率
		BigDecimal secondStoreOpenRate = umb01Dto.getSecondStoreOpenRate();
		// 二次店口銭額
		BigDecimal secondStoreOpenAmount = umb01Dto.getSecondStoreOpenAmount();
		// ロット数量
		BigDecimal lotQuantity = umb01Dto.getPriceRefDto().getLotQuantity();
		BigDecimal tempValue = new BigDecimal("0");
		BigDecimal valueLotSmall = new BigDecimal("100");
		BigDecimal valueLotLarge = new BigDecimal("300");

		// pattern 1
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			tempValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
					.subtract(secondStoreOpenRate.multiply(retailPrice));

			// set data
			umb01Dto.getPriceCalParam().setPattern("1");
			umb01Dto.getPriceCalParam().setNoPreRetailPrice1(retailPrice.toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice1(retailPrice.toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice1(tempValue.toString());

		}

		// pattern 2
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			tempValue = retailPrice.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);

			// set data
			umb01Dto.getPriceCalParam().setNoPreRetailPrice2(retailPrice.toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice2(retailPrice.toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice2(tempValue.toString());
		}

		// pattern 3
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				tempValue = retailPrice.add(unitPriceSmallParcel)
						.subtract(primaryStoreOpenRate.multiply(retailPrice.add(unitPriceSmallParcel)))
						.subtract(secondStoreOpenRate.multiply(retailPrice.add(unitPriceSmallParcel)));
			} else {
				tempValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}

			// set data
			umb01Dto.getPriceCalParam().setDeliRetailPrice1(retailPrice.toString());
			umb01Dto.getPriceCalParam().setDeliTotalRetailPrice1(retailPrice.add(unitPriceSmallParcel).toString());
			umb01Dto.getPriceCalParam().setDeliPartitionUnitPrice1(tempValue.toString());
		}

		// pattern 4
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				tempValue = retailPrice.add(unitPriceSmallParcel).subtract(primaryStoreCommissionAmount)
						.subtract(secondStoreOpenAmount);
			} else {
				tempValue = retailPrice.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
			}
		}

		// pattern 5
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotLarge.compareTo(lotQuantity) < 0) {
				tempValue = retailPrice.add(unitPriceForeheadColor)
						.subtract(primaryStoreOpenRate.multiply(retailPrice.add(unitPriceForeheadColor)))
						.subtract(secondStoreOpenRate.multiply(retailPrice.add(unitPriceForeheadColor)));
			} else {
				tempValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

		// pattern 6
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotLarge.compareTo(lotQuantity) < 0) {
				tempValue = retailPrice.add(unitPriceForeheadColor).subtract(primaryStoreCommissionAmount)
						.subtract(secondStoreOpenAmount);
			} else {
				tempValue = retailPrice.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
			}
		}

		// pattern 7
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				tempValue = retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor)
						.subtract(primaryStoreOpenRate
								.multiply(retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor)))
						.subtract(secondStoreOpenRate
								.multiply(retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor)));
			} else if (valueLotSmall.compareTo(lotQuantity) >= 0 && valueLotLarge.compareTo(lotQuantity) < 0) {
				tempValue = retailPrice.add(unitPriceSmallParcel)
						.subtract(primaryStoreOpenRate.multiply(retailPrice.add(unitPriceSmallParcel)))
						.subtract(secondStoreOpenRate.multiply(retailPrice.add(unitPriceSmallParcel)));
			} else {
				tempValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

		// pattern 8
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				tempValue = retailPrice.add(unitPriceForeheadColor).add(unitPriceSmallParcel)
						.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
			} else if (valueLotSmall.compareTo(lotQuantity) >= 0 && valueLotLarge.compareTo(lotQuantity) < 0) {
				tempValue = retailPrice.add(unitPriceForeheadColor).subtract(primaryStoreCommissionAmount)
						.subtract(secondStoreOpenAmount);
			} else {
				tempValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

	}
	
	private boolean checkStatusCdApplyed(String statusCd) {
		return ConstStatus.STATUS_UNDER_DELIBERATION.equals(umb01Dto.getStatusCD())
				|| ConstStatus.STATUS_APPROVED.equals(umb01Dto.getStatusCD())
				|| ConstStatus.STATUS_COMPLETED.equals(umb01Dto.getStatusCD());
	}

	public String getCsvFilePath() {
		return this.csvFilePath;
	}

	public void setCsvFilePath(String csvFilePath) {
		this.csvFilePath = csvFilePath;
	}

	public Integer getReturnCode() {
		return this.returnCode;
	}

	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the umb01Dto
	 */
	public Umb01Dto getUmb01Dto() {
		return umb01Dto;
	}

	/**
	 * @param umb01Dto the umb01Dto to set
	 */
	public void setUmb01Dto(Umb01Dto umb01Dto) {
		this.umb01Dto = umb01Dto;
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
	 * @return the selectEmp
	 */
	public String getSelectEmp() {
		return selectEmp;
	}

	/**
	 * @param selectEmp the selectEmp to set
	 */
	public void setSelectEmp(String selectEmp) {
		this.selectEmp = selectEmp;
	}

	/**
	 * @return the emp
	 */
	public Employee getEmp() {
		return emp;
	}

	/**
	 * @param emp the emp to set
	 */
	public void setEmp(Employee emp) {
		this.emp = emp;
	}

	/**
	 * @return the applyDate
	 */
	public Date getApplyDate() {
		return applyDate;
	}

	/**
	 * @param applyDate the applyDate to set
	 */
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	/**
	 * @return the titleApply
	 */
	public String getTitleApply() {
		return titleApply;
	}

	/**
	 * @param titleApply the titleApply to set
	 */
	public void setTitleApply(String titleApply) {
		this.titleApply = titleApply;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * @return the paperDocument
	 */
	public String getPaperDocument() {
		return paperDocument;
	}

	/**
	 * @param paperDocument the paperDocument to set
	 */
	public void setPaperDocument(String paperDocument) {
		this.paperDocument = paperDocument;
	}

	/**
	 * @return the attachFileList
	 */
	public List<AttachFile> getAttachFileList() {
		return attachFileList;
	}

	/**
	 * @param attachFileList the attachFileList to set
	 */
	public void setAttachFileList(List<AttachFile> attachFileList) {
		this.attachFileList = attachFileList;
	}

	public String getFileUrlPath() {
		return fileUrlPath;
	}

	public void setFileUrlPath(String fileUrlPath) {
		this.fileUrlPath = fileUrlPath;
	}

	/**
	 * @return the unitPriceDataRef
	 */
	public String getUnitPriceDataRef() {
		return unitPriceDataRef;
	}

	/**
	 * @param unitPriceDataRef the unitPriceDataRef to set
	 */
	public void setUnitPriceDataRef(String unitPriceDataRef) {
		this.unitPriceDataRef = unitPriceDataRef;
	}

	/**
	 * @return the priceDataRef
	 */
	public String getPriceDataRef() {
		return priceDataRef;
	}

	/**
	 * @param priceDataRef the priceDataRef to set
	 */
	public void setPriceDataRef(String priceDataRef) {
		this.priceDataRef = priceDataRef;
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
	 * @return the outputHtml
	 */
	public String getOutputHtml() {
		return outputHtml;
	}

	/**
	 * @param outputHtml the outputHtml to set
	 */
	public void setOutputHtml(String outputHtml) {
		this.outputHtml = outputHtml;
	}

	/**
	 * @return the transactionList
	 */
	public List<SelectItem> getTransactionList() {
		return transactionList;
	}

	/**
	 * @param transactionList the transactionList to set
	 */
	public void setTransactionList(List<SelectItem> transactionList) {
		this.transactionList = transactionList;
	}

	/**
	 * @return the dataUpdateCategoryList
	 */
	public List<SelectItem> getDataUpdateCategoryList() {
		return dataUpdateCategoryList;
	}

	/**
	 * @param dataUpdateCategoryList the dataUpdateCategoryList to set
	 */
	public void setDataUpdateCategoryList(List<SelectItem> dataUpdateCategoryList) {
		this.dataUpdateCategoryList = dataUpdateCategoryList;
	}

	/**
	 * @return the reasonInquiryList
	 */
	public List<SelectItem> getReasonInquiryList() {
		return reasonInquiryList;
	}

	/**
	 * @param reasonInquiryList the reasonInquiryList to set
	 */
	public void setReasonInquiryList(List<SelectItem> reasonInquiryList) {
		this.reasonInquiryList = reasonInquiryList;
	}

	/**
	 * @return the retroactiveClassificationList
	 */
	public List<SelectItem> getRetroactiveClassificationList() {
		return retroactiveClassificationList;
	}

	/**
	 * @param retroactiveClassificationList the retroactiveClassificationList to set
	 */
	public void setRetroactiveClassificationList(List<SelectItem> retroactiveClassificationList) {
		this.retroactiveClassificationList = retroactiveClassificationList;
	}

	/**
	 * @return the customerReqConfirmList
	 */
	public List<SelectItem> getCustomerReqConfirmList() {
		return customerReqConfirmList;
	}

	/**
	 * @param customerReqConfirmList the customerReqConfirmList to set
	 */
	public void setCustomerReqConfirmList(List<SelectItem> customerReqConfirmList) {
		this.customerReqConfirmList = customerReqConfirmList;
	}

	/**
	 * @return the dateRange
	 */
	public DateRange getDateRange() {
		return dateRange;
	}

	/**
	 * @param dateRange the dateRange to set
	 */
	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}

	/**
	 * @return the currentMode
	 */
	public String getCurrentMode() {
		return currentMode;
	}

	/**
	 * @param currentMode the currentMode to set
	 */
	public void setCurrentMode(String currentMode) {
		this.currentMode = currentMode;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
}
