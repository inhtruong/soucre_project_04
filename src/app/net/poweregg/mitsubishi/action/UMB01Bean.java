package net.poweregg.mitsubishi.action;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.json.JSONArray;
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
import net.poweregg.mitsubishi.dto.PriceUnitRefDto;
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
import net.poweregg.ui.param.WDBRefParam;
import net.poweregg.util.DateUtils;
import net.poweregg.util.FacesContextUtils;
import net.poweregg.util.NumberUtils;
import net.poweregg.util.StringUtils;
import net.poweregg.web.engine.navigation.LoginUser;
import net.poweregg.webdb.XDBRemoteLocal;
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
	
	@EJB
    private XDBRemoteLocal xdbRemote;

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
	private List<String> reasonInquiryList = new ArrayList<>();
	private List<SelectItem> retroactiveClassificationList;
	private List<SelectItem> customerReqConfirmList;

	private String unitPriceDataRef;
	private String priceDataRef;
	private String usageRef;

	private String outputHtml;
	private DateRange dateRange;
	private String currentMode;
	private String currentDataNo;
	private String currentStatus;
	private BigDecimal tempPartitionPrice;
	private boolean showBtnStatus;
	private String contactSrceen;

	private int selectRateOrAmount;

	public String initUMB0102e() throws Exception {

		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);
		String backMode = (String) FacesContextUtils.getFromFlash("currentModeBack");
		String backDataNo = (String) FacesContextUtils.getFromFlash("dataNoBack");
		
		if (!StringUtils.nullOrBlank(backMode) && !StringUtils.nullOrBlank(backDataNo)) {
			currentMode = backMode;
			currentDataNo = backDataNo;
		} else {
			currentMode = mode;
			currentDataNo = dataNo;
		}

		if (loginUser == null) {
			return "login";
		}
		

		if (StringUtils.EMPTY.equals(mode) || StringUtils.EMPTY.equals(dataNo)) {
			LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "Error: モードまたはデータ番号が空です");
			throw new Exception("Error: モードまたはデータ番号が空です");
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
		selectRateOrAmount = 1;

		if (MitsubishiConst.MODE_NEW.equals(currentMode)) {
			initApplyScreenByDbType(1);
			return StringUtils.EMPTY;
		}
		if (MitsubishiConst.MODE_EDIT.equals(currentMode) || MitsubishiConst.MODE_CANCEL.equals(currentMode)) {
			initApplyScreenByDbType(2);
			return StringUtils.EMPTY;
		}

		LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "This mode doesn't exist");
		throw new Exception("This mode doesn't exist");
	}

	private void initApplyScreenByDbType(int dbType) throws Exception {
		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);
		
		// get value from WebDB
		umb01Dto = mitsubishiService.getDataMitsubishi(currentDataNo, dbType);
		if (umb01Dto == null) {
			LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "Error:" + currentDataNo + "は存在しません");
			throw new Exception("Error: " + MitsubishiConst.DATA_NO + " " + currentDataNo + " は存在しません");
		}
		// set status apply
		if (1 == dbType) {
			setCurrentStatus(umb01Dto.getPriceUnitRefDto().getStatusCD());
			// show/hide button update
			showBtnStatus = showBtnUpdateStatus();
		}
		if (2 == dbType) {
			if (umb01Dto.getPriceRefDto().getStatusCD() == null) {
				LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "Error: ステータスが空です");
				throw new Exception("Error: ステータスが空です");
			}
			setCurrentStatus(umb01Dto.getPriceRefDto().getStatusCD());
		}
		// initialization item
		initItems(webDBClassInfos);
		
		// make XML table price
		outputHtml = new DataFlowUtil().transformXML2HTML(mitsubishiService.createXMLTablePrice(umb01Dto), FILE_XML);
	}

	/**
	 * ワークフロー アプリケーション用のデータを準備します。
	 * 
	 * @return "confirm"
	 */
	public String toConfirm() {
		
		// 末端単価 check
		if (isBlank(umb01Dto.getPriceRefDto().getRetailPrice())) {
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "末端価格が未入力です。", "");
			
			boolean validtarget = true;
			UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
			UIComponent i_target = root.findComponent("frm:i_tableData10:tab1_retailPrice");
			if (i_target != null) {
				((UIInput) i_target).setValid(false);
			}
			validtarget = false;
			return StringUtils.EMPTY;
		}
		
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
	 * リクエストの申請に使用
	 * @param numericCode 
	 * 
	 * @return "apply"
	 * @throws Exception
	 **/
	public String apply() throws Exception {
		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);

		try {
			// 
			if (StringUtils.nullOrBlank(umb01Dto.getPriceRefDto().getAppRecepNoCancel())
					&& !StringUtils.nullOrBlank(umb01Dto.getPriceRefDto().getAppRecepNo())) {
				currentStatus = "1001";
			}
			
			// check apply
			if (!checkStatusCdApplyed(currentStatus)) {
				LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), " Error: 申請しました。");
				facesMessages.add(FacesMessage.SEVERITY_ERROR, "申請しました。", "");
				return StringUtils.EMPTY;
			}
			
			umb01Dto.getPriceRefDto().setUnitPriceBefRevision(tempPartitionPrice);
			
			// 計算値
			switch (umb01Dto.getPriceCalParam().getPattern()) {
				case "3":
				case "4":
					umb01Dto.getPriceRefDto().setLotQuantity("100,0");
					break;
				case "5":
				case "6":
					umb01Dto.getPriceRefDto().setLotQuantity("0,100");
					break;
				case "7":
				case "8":
					umb01Dto.getPriceRefDto().setLotQuantity("300,100,0");
					break;
				default:
					umb01Dto.getPriceRefDto().setLotQuantity("0");
					break;
			}

			// 申請確定
			dataflowHelper.apply();
			// 自分のデータを保存する.
			ApplicationForm appForm = flowService.findApplicationFormByRecpNo(appRecepNo);

			if (MitsubishiConst.MODE_NEW.equals(currentMode)) {
				umb01Dto.getPriceUnitRefDto().setAppRecepNo(appRecepNo.toString());
				umb01Dto.getPriceUnitRefDto().setStatusCD(appForm.getStatus());
				mitsubishiService.updateRecordDbPrice(logFileFullPath, umb01Dto, 1, currentMode);
			}
			if (MitsubishiConst.MODE_EDIT.equals(currentMode)) {
				umb01Dto.getPriceRefDto().setAppRecepNo(appRecepNo.toString());
				umb01Dto.getPriceRefDto().setStatusCD(appForm.getStatus());
				mitsubishiService.updateRecordDbPrice(logFileFullPath, umb01Dto, 2, currentMode);
			}
			if (MitsubishiConst.MODE_CANCEL.equals(currentMode)) {
				umb01Dto.getPriceRefDto().setAppRecepNoCancel(appRecepNo.toString());
				umb01Dto.getPriceRefDto().setStatusCD(appForm.getStatus());
				mitsubishiService.updateRecordDbPrice(logFileFullPath, umb01Dto, 2, currentMode);
			}

			facesMessages.add(FacesMessage.SEVERITY_INFO, "申請しました。", "");
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
		FacesContextUtils.putToFlash("currentModeBack", currentMode);
		FacesContextUtils.putToFlash("dataNoBack", umb01Dto.getPriceUnitRefDto().getDataNo().toString());
		return "return";

	}
	
	/**
	 * すべてのフィールドが等しい場合、「ステータスの更新」ボタンを表示する必要があることを示します。
	 * 
	 * @return
	 * @throws Exception 
	 */
	public boolean showBtnUpdateStatus() throws Exception {
		// get value from WebDB Master
		Umb01Dto umb01Master = mitsubishiService.getDataMitsubishi(currentDataNo, 2);
		if (umb01Master == null) {
			return false;
		}

		PriceUnitRefDto dto = umb01Dto.getPriceUnitRefDto();
		PriceUnitRefDto dtoMaster = umb01Master.getPriceUnitRefDto();
		
		return pricesMatch(dto, dtoMaster);
	}	
	
	private boolean pricesMatch(PriceUnitRefDto current, PriceUnitRefDto master) {
		return current.getCustomerCD().equals(master.getCustomerCD())
				&& current.getDestinationCD1().equals(master.getDestinationCD1())
				&& current.getDestinationCD2().equals(master.getDestinationCD2())
				&& current.getProductNameAbbreviation().equals(master.getProductNameAbbreviation())
				&& current.getColorNo().equals(master.getColorNo())
				&& current.getCurrencyCD().equals(master.getCurrencyCD())
				&& current.getClientBranchNumber().equals(master.getClientBranchNumber())
				&& current.getPriceForm().equals(master.getPriceForm());
	}
	
	public String processUpdateStatus() throws Exception {
		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);
		
		PriceUnitRefDto dto = umb01Dto.getPriceUnitRefDto();
		// get value from WebDB Master
		Umb01Dto umb01Master = mitsubishiService.getDataUpdateStatus(2, dto.getCustomerCD(),
				dto.getDestinationCD1(), dto.getDestinationCD2(), dto.getProductNameAbbreviation(), dto.getColorNo(),
				dto.getCurrencyCD(), dto.getClientBranchNumber(), dto.getPriceForm(), null);
		
		String statusCdMaster = umb01Master.getPriceRefDto().getStatusCD();
		String appRecepNoCancel = umb01Master.getPriceRefDto().getAppRecepNoCancel();
		int latestManagerNo = Integer.parseInt(umb01Master.getManagerNo());
		
		// check record applying
		if (ConstStatus.STATUS_UNDER_DELIBERATION.equals(statusCdMaster)) {
			LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(),
					" Error: 参照元データは承認処理中です。承認前データで登録します。");
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "参照元データは承認処理中です。承認前データで登録します。", "");
			return StringUtils.EMPTY;
		}
		
		if (ConstStatus.STATUS_APPROVED.equals(statusCdMaster) 
				&& !StringUtils.nullOrBlank(appRecepNoCancel)) {
			// find data in history DB
			int managerNo = latestManagerNo - 1;
			Umb01Dto umb01History = mitsubishiService.getDataUpdateStatus(3, dto.getCustomerCD(),
					dto.getDestinationCD1(), dto.getDestinationCD2(), dto.getProductNameAbbreviation(), dto.getColorNo(),
					dto.getCurrencyCD(), dto.getClientBranchNumber(), dto.getPriceForm(), StringUtils.toEmpty(managerNo));
			
			mitsubishiService.updateRecordDbPrice(logFileFullPath, umb01Dto, 1, currentMode);
			mitsubishiService.updateStatusRecord(logFileFullPath, umb01History, 3);
		} else {
			mitsubishiService.updateRecordDbPrice(logFileFullPath, umb01Dto, 1, currentMode);
			mitsubishiService.updateStatusRecord(logFileFullPath, umb01Master, 2);
		}
		
		return "apply";
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
		int recordSkip = 0;
		int recordImport =0;
		int recordTotal = 0;
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
				recordTotal = dataList.size();
				for (int i = 0; i < dataList.size(); i++) {
					UMB01TempDto recordData = dataList.get(i);
					// b.1. Insert 前払勧奨情報 get classInfo by commonNo: UMB01
					WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0, 1);
					
					// find record data at webDB price
					JSONArray rsJson = mitsubishiService.findDataUmbByCondition(webdbUtils,MitsubishiConst.DATA_NO, StringUtils.toEmpty(recordData.getDataNo()));
					// Khong tim duoc du lieu tương ung
					if (rsJson == null || rsJson.length() == 0) {
						String result = webdbUtils.registJsonObject(
								putDataUmitsubishiTemp(webDBClassInfos, recordData, logFileFullPath), true);
						if (!ConvertUtils.isNullOrEmpty(result)) {
							result = "データNO:"+ recordData.getDataNo() + "。" +result  ;
							System.out.println(result);
							LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), result);
						}else {
							recordImport++;
						}
					}else {
						JSONObject recordObj = rsJson.getJSONObject(0);
						//※T/H tồn tại toàn bộ dữ liệu có 全得意先CD、仕向先CD1、仕向先CD2、品名略号、カラーNo、グレード1、															
						//通貨CD、取引先枝番、価格形態、用途CD khớp nhau	
						if(checkDataAlreadyExists( recordData, recordObj,logFileFullPath)) {
							String result = webdbUtils.registJsonObject(
									putDataUmitsubishiTemp(webDBClassInfos, recordData, logFileFullPath), true);
							if (!ConvertUtils.isNullOrEmpty(result)) {
								LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), result);
							}else {
								recordImport++;
							}
						}else {
							recordSkip++;
						}
					}
				}
				utx.commit();
				setReturnCode(MitsubishiConst.RESULT_OK);
				LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
						"登録されたレコード数: " + String.valueOf(recordImport));
				LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
						"スキップされたレコード数: " + String.valueOf(recordSkip));
				LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
						"総レコード数: " + String.valueOf(recordTotal));
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

		//StringBuilder getLog = new StringBuilder();
		//getLog.append(MitsubishiConst.DATA_NO).append(MitsubishiConst.COLON);

		// log: start data no
		//LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
		//		getLog.append(umbItem.getDataNo()).append(MitsubishiConst.HYPHEN).append(MitsubishiConst.START_LOG).toString());

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
		regDataJson.put(MitsubishiConst.CUSTOMER_CD, WebDbUtils.createRecordItem(umbItem.getCustomerCD()));
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
		/** 登録担当者 */
		regDataJson.put(MitsubishiConst.REGISTRAR, WebDbUtils.createRecordItem(umbItem.getRegistrar()));
		/** 売上担当者CD */
		regDataJson.put(MitsubishiConst.SALESPERSON_CD, WebDbUtils.createRecordItem(umbItem.getSalespersonCD()));
		/** 売上担当者名 */
		regDataJson.put(MitsubishiConst.SALESPERSON_NAME, WebDbUtils.createRecordItem(umbItem.getSalespersonName()));
		/** 状態 */
		regDataJson.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(umbItem.getStateCD()));
		/** 価格伺いマスタ申請(新規) */
		StringBuilder url = new StringBuilder();
		url.append(WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_1.getValue())).append(MessageFormat
				.format(messages.get(MitsubishiConst.URL_PROPERTIES), umbItem.getDataNo(), MitsubishiConst.MODE_NEW));
		regDataJson.put(MitsubishiConst.NEW_PRICE_INQUIRY_MASTER_APPLICATION,
				WebDbUtils.createRecordURL(url.toString()));

		// log: end data no
		//getLog = new StringBuilder();
		//getLog.append(MitsubishiConst.DATA_NO).append(MitsubishiConst.COLON);
		//LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), getLog
		//		.append(umbItem.getDataNo()).append(MitsubishiConst.HYPHEN).append(MitsubishiConst.END_LOG).toString());

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
		/** 状態 */
		umitsubishiTempDto.setStateCD("1001");		
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
	 * @throws Exception
	 *
	 *
	 *
	 */
	public void calculateValue() throws Exception {
		// 末端価格
		BigDecimal retailPrice = toBigDecimal(umb01Dto.getPriceRefDto().getRetailPrice());
		// 小口配送単価
		BigDecimal unitPriceSmallParcel = toBigDecimal(umb01Dto.getPriceRefDto().getUnitPriceSmallParcel());
		// 小口着色単価
		BigDecimal unitPriceForeheadColor = toBigDecimal(umb01Dto.getPriceRefDto().getUnitPriceForeheadColor());
		// 一次店口銭率
		BigDecimal primaryStoreOpenRate = BigDecimal.valueOf(0);
		// 一次店口銭(金額)
		BigDecimal primaryStoreOpenAmount = BigDecimal.valueOf(0);
		// 二次店口銭率
		BigDecimal secondStoreOpenRate = BigDecimal.valueOf(0);
		// 二次店口銭額
		BigDecimal secondStoreOpenAmount = BigDecimal.valueOf(0);
		if (selectRateOrAmount == 1) { 
			primaryStoreOpenRate = toBigDecimal(umb01Dto.getPriceRefDto().getPrimaryStoreOpenRate());
			secondStoreOpenRate = toBigDecimal(umb01Dto.getSecondStoreOpenRate());
		} else {
			primaryStoreOpenAmount = toBigDecimal(umb01Dto.getPriceRefDto().getPrimaryStoreOpenAmount());
			secondStoreOpenAmount = toBigDecimal(umb01Dto.getSecondStoreOpenAmount());
		}

		BigDecimal tempValue = BigDecimal.valueOf(0);
		BigDecimal tempValueOfNoPre = BigDecimal.valueOf(0);
		BigDecimal totalRetailPrice = BigDecimal.valueOf(0);
		BigDecimal primaryDiscount = BigDecimal.valueOf(0);
		BigDecimal secondaryDiscount = BigDecimal.valueOf(0);
		BigDecimal primaryPercent = BigDecimal.valueOf(0);
		BigDecimal secondaryPercent = BigDecimal.valueOf(0);
		BigDecimal valueLotSmall = BigDecimal.valueOf(100);
		BigDecimal valueLotLarge = BigDecimal.valueOf(300);

		// pattern 1
		if (BigDecimal.ZERO.compareTo(retailPrice)!=0 && BigDecimal.ZERO.compareTo(unitPriceSmallParcel)==0
				&& BigDecimal.ZERO.compareTo(unitPriceForeheadColor)==0 && BigDecimal.ZERO.compareTo(primaryStoreOpenRate)!=0
				&& BigDecimal.ZERO.compareTo(primaryStoreOpenAmount)==0 && BigDecimal.ZERO.compareTo(secondStoreOpenRate)!=0
				&& BigDecimal.ZERO.compareTo(secondStoreOpenAmount)==0) {
			
			totalRetailPrice = retailPrice;
			tempValue = totalRetailPrice;
			umb01Dto.getPriceCalParam().setDeliRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliPartitionUnitPrice1(toBigDecimal(tempValue).toString());

			primaryDiscount = totalRetailPrice.multiply(primaryStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			secondaryDiscount = totalRetailPrice.multiply(secondStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			tempValue = retailPrice.subtract(primaryDiscount).subtract(secondaryDiscount);
			tempValueOfNoPre = tempValue;

			umb01Dto.getPriceCalParam().setNoPreRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenRate(toBigDecimal(primaryStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenAmount(toBigDecimal(primaryDiscount).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenRate(toBigDecimal(secondStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenAmount(toBigDecimal(secondaryDiscount).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenRate(toBigDecimal(primaryStoreOpenRate).add(secondStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenAmount(toBigDecimal(primaryDiscount.add(secondaryDiscount)).toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice1(toBigDecimal(tempValue).toString());
			
			umb01Dto.getPriceCalParam().setPattern("1");
		}

		// pattern 2
		if (BigDecimal.ZERO.compareTo(retailPrice)!=0 && BigDecimal.ZERO.compareTo(unitPriceSmallParcel)==0
				&& BigDecimal.ZERO.compareTo(unitPriceForeheadColor)==0 && BigDecimal.ZERO.compareTo(primaryStoreOpenRate)==0
				&& BigDecimal.ZERO.compareTo(primaryStoreOpenAmount)!=0 && BigDecimal.ZERO.compareTo(secondStoreOpenRate)==0
				&& BigDecimal.ZERO.compareTo(secondStoreOpenAmount)!=0) {

			totalRetailPrice = retailPrice;
			tempValue = totalRetailPrice;
			umb01Dto.getPriceCalParam().setDeliRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliPartitionUnitPrice1(toBigDecimal(tempValue).toString());

			primaryPercent = primaryStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			secondaryPercent = secondStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			tempValue = retailPrice.subtract(primaryStoreOpenAmount).subtract(secondStoreOpenAmount);
			tempValueOfNoPre = tempValue;

			umb01Dto.getPriceCalParam().setNoPreRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenRate(toBigDecimal(primaryPercent).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenAmount(toBigDecimal(primaryStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenRate(toBigDecimal(secondaryPercent).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenAmount(toBigDecimal(secondStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenRate(toBigDecimal(primaryPercent.add(secondaryPercent)).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenAmount(toBigDecimal(primaryStoreOpenAmount).add(secondStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice1(toBigDecimal(tempValue).toString());
			
			umb01Dto.getPriceCalParam().setPattern("2");
		}

		// pattern 3
		if (BigDecimal.ZERO.compareTo(retailPrice)!=0 && BigDecimal.ZERO.compareTo(unitPriceSmallParcel)!=0
				&& BigDecimal.ZERO.compareTo(unitPriceForeheadColor)==0 && BigDecimal.ZERO.compareTo(primaryStoreOpenRate)!=0
				&& BigDecimal.ZERO.compareTo(primaryStoreOpenAmount)==0 && BigDecimal.ZERO.compareTo(secondStoreOpenRate)!=0
				&& BigDecimal.ZERO.compareTo(secondStoreOpenAmount)==0) {
			totalRetailPrice = retailPrice;
			tempValue = totalRetailPrice;
			umb01Dto.getPriceCalParam().setDeliRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliPartitionUnitPrice1(toBigDecimal(tempValue).toString());

			primaryDiscount = totalRetailPrice.multiply(primaryStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			secondaryDiscount = totalRetailPrice.multiply(secondStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			tempValue = retailPrice.subtract(primaryDiscount).subtract(secondaryDiscount);
			tempValueOfNoPre = tempValue;

			umb01Dto.getPriceCalParam().setNoPreRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenRate(toBigDecimal(primaryStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenAmount(toBigDecimal(primaryDiscount).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenRate(toBigDecimal(secondStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenAmount(toBigDecimal(secondaryDiscount).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenRate(toBigDecimal(primaryStoreOpenRate.add(secondStoreOpenRate)).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenAmount(toBigDecimal(primaryDiscount.add(secondaryDiscount)).toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice1(toBigDecimal(tempValue).toString());

			totalRetailPrice = retailPrice.add(unitPriceSmallParcel);
			primaryDiscount = totalRetailPrice.multiply(primaryStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			secondaryDiscount = totalRetailPrice.multiply(secondStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			tempValue = totalRetailPrice.subtract(primaryDiscount).subtract(secondaryDiscount);

			umb01Dto.getPriceCalParam().setSmallRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setSmallUnitPriceParcel1(toBigDecimal(unitPriceSmallParcel).toString());
			umb01Dto.getPriceCalParam().setSmallTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setSmallPrimaryOpenRate(toBigDecimal(primaryStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setSmallPrimaryOpenAmount(toBigDecimal(primaryDiscount).toString());
			umb01Dto.getPriceCalParam().setSmallSecondaryOpenRate(toBigDecimal(secondStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setSmallSecondaryOpenAmount(toBigDecimal(secondaryDiscount).toString());
			umb01Dto.getPriceCalParam().setSmallTotalOpenRate(toBigDecimal(primaryStoreOpenRate.add(secondStoreOpenRate)).toString());
			umb01Dto.getPriceCalParam().setSmallTotalOpenAmount(toBigDecimal(primaryDiscount.add(secondaryDiscount)).toString());
			umb01Dto.getPriceCalParam().setSmallPartitionUnitPrice1(toBigDecimal(tempValue).toString());
			umb01Dto.getPriceCalParam().setCalSmallUnitPriceParcel(toBigDecimal(tempValue.add(tempValueOfNoPre)).toString());
			
			umb01Dto.getPriceCalParam().setPattern("3");
		}

		// pattern 4
		if (BigDecimal.ZERO.compareTo(retailPrice)!=0 && BigDecimal.ZERO.compareTo(unitPriceSmallParcel)!=0
				&& BigDecimal.ZERO.compareTo(unitPriceForeheadColor)==0 && BigDecimal.ZERO.compareTo(primaryStoreOpenRate)==0
				&& BigDecimal.ZERO.compareTo(primaryStoreOpenAmount)!=0 && BigDecimal.ZERO.compareTo(secondStoreOpenRate)==0
				&& BigDecimal.ZERO.compareTo(secondStoreOpenAmount)!=0) {

			totalRetailPrice = retailPrice;
			tempValue = totalRetailPrice;
			umb01Dto.getPriceCalParam().setDeliRetailPrice1(retailPrice.toString());
			umb01Dto.getPriceCalParam().setDeliTotalRetailPrice1(totalRetailPrice.toString());
			umb01Dto.getPriceCalParam().setDeliPartitionUnitPrice1(tempValue.toString());

			primaryPercent = primaryStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			secondaryPercent = secondStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			tempValue = retailPrice.subtract(primaryStoreOpenAmount).subtract(secondStoreOpenAmount);
			tempValueOfNoPre = tempValue;

			umb01Dto.getPriceCalParam().setNoPreRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenRate(toBigDecimal(primaryPercent).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenAmount(toBigDecimal(primaryStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenRate(toBigDecimal(secondaryPercent).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenAmount(toBigDecimal(secondStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenRate(toBigDecimal(primaryPercent.add(secondaryPercent)).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenAmount(toBigDecimal(primaryStoreOpenAmount.add(secondStoreOpenAmount)).toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice1(toBigDecimal(tempValue).toString());

			totalRetailPrice = retailPrice.add(unitPriceSmallParcel);
			primaryPercent = primaryStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			secondaryPercent = secondStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			tempValue = totalRetailPrice.subtract(primaryStoreOpenAmount).subtract(secondStoreOpenAmount);

			umb01Dto.getPriceCalParam().setSmallRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setSmallUnitPriceParcel1(toBigDecimal(unitPriceSmallParcel).toString());
			umb01Dto.getPriceCalParam().setSmallTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setSmallPrimaryOpenRate(toBigDecimal(primaryPercent).toString());
			umb01Dto.getPriceCalParam().setSmallPrimaryOpenAmount(toBigDecimal(primaryStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setSmallSecondaryOpenRate(toBigDecimal(secondaryPercent).toString());
			umb01Dto.getPriceCalParam().setSmallSecondaryOpenAmount(toBigDecimal(secondStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setSmallTotalOpenRate(toBigDecimal(primaryPercent.add(secondaryPercent)).toString());
			umb01Dto.getPriceCalParam()
					.setSmallTotalOpenAmount(toBigDecimal(primaryStoreOpenAmount.add(secondStoreOpenAmount)).toString());
			umb01Dto.getPriceCalParam().setSmallPartitionUnitPrice1(toBigDecimal(tempValue).toString());
			umb01Dto.getPriceCalParam().setCalSmallUnitPriceParcel(toBigDecimal(tempValue.add(tempValueOfNoPre)).toString());
			
			umb01Dto.getPriceRefDto().setPrimaryStoreOpenRate(primaryPercent);
			umb01Dto.setSecondStoreOpenRate(secondaryPercent);
			
			umb01Dto.getPriceCalParam().setPattern("4");
		}

		// pattern 5
		if (BigDecimal.ZERO.compareTo(retailPrice)!=0 && BigDecimal.ZERO.compareTo(unitPriceSmallParcel)==0
				&& BigDecimal.ZERO.compareTo(unitPriceForeheadColor)!=0 && BigDecimal.ZERO.compareTo(primaryStoreOpenRate)!=0
				&& BigDecimal.ZERO.compareTo(primaryStoreOpenAmount)==0 && BigDecimal.ZERO.compareTo(secondStoreOpenRate)!=0
				&& BigDecimal.ZERO.compareTo(secondStoreOpenAmount)==0) {
			totalRetailPrice = retailPrice;
			tempValue = totalRetailPrice;
			umb01Dto.getPriceCalParam().setDeliRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliPartitionUnitPrice1(toBigDecimal(tempValue).toString());

			primaryDiscount = totalRetailPrice.multiply(primaryStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			secondaryDiscount = totalRetailPrice.multiply(secondStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			tempValue = retailPrice.subtract(primaryDiscount).subtract(secondaryDiscount);
			tempValueOfNoPre = tempValue;

			umb01Dto.getPriceCalParam().setNoPreRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenRate(toBigDecimal(primaryStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenAmount(toBigDecimal(primaryDiscount).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenRate(toBigDecimal(secondStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenAmount(toBigDecimal(secondaryDiscount).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenRate(toBigDecimal(primaryStoreOpenRate.add(secondStoreOpenRate)).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenAmount(toBigDecimal(primaryDiscount.add(secondaryDiscount)).toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice1(toBigDecimal(tempValue).toString());

			totalRetailPrice = retailPrice.add(unitPriceForeheadColor);
			primaryDiscount = totalRetailPrice.multiply(primaryStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			secondaryDiscount = totalRetailPrice.multiply(secondStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			tempValue = totalRetailPrice.subtract(primaryDiscount).subtract(secondaryDiscount);

			umb01Dto.getPriceCalParam().setLargeRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setLargeUnitPriceForehead1(toBigDecimal(unitPriceForeheadColor).toString());
			umb01Dto.getPriceCalParam().setLargeTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setLargePrimaryOpenRate(toBigDecimal(primaryStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setLargePrimaryOpenAmount(toBigDecimal(primaryDiscount).toString());
			umb01Dto.getPriceCalParam().setLargeSecondaryOpenRate(toBigDecimal(secondStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setLargeSecondaryOpenAmount(toBigDecimal(secondaryDiscount).toString());
			umb01Dto.getPriceCalParam().setLargeTotalOpenRate(toBigDecimal(primaryStoreOpenRate.add(secondStoreOpenRate)).toString());
			umb01Dto.getPriceCalParam().setLargeTotalOpenAmount(toBigDecimal(primaryDiscount.add(secondaryDiscount)).toString());
			umb01Dto.getPriceCalParam().setLargePartitionUnitPrice1(toBigDecimal(tempValue).toString());
			umb01Dto.getPriceCalParam().setCalLargeUnitPriceForehead(toBigDecimal(tempValue.add(tempValueOfNoPre)).toString());
			
			umb01Dto.getPriceRefDto().setPrimaryStoreOpenAmount(primaryDiscount);
			umb01Dto.setSecondStoreOpenAmount(secondaryDiscount);
			
			umb01Dto.getPriceCalParam().setPattern("5");
		}

		// pattern 6
		if (BigDecimal.ZERO.compareTo(retailPrice)!=0 && BigDecimal.ZERO.compareTo(unitPriceSmallParcel)==0
				&& BigDecimal.ZERO.compareTo(unitPriceForeheadColor)!=0 && BigDecimal.ZERO.compareTo(primaryStoreOpenRate)==0
				&& BigDecimal.ZERO.compareTo(primaryStoreOpenAmount)!=0 && BigDecimal.ZERO.compareTo(secondStoreOpenRate)==0
				&& BigDecimal.ZERO.compareTo(secondStoreOpenAmount)!=0) {
			totalRetailPrice = retailPrice;
			tempValue = totalRetailPrice;
			umb01Dto.getPriceCalParam().setDeliRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliPartitionUnitPrice1(toBigDecimal(tempValue).toString());

			primaryPercent = primaryStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			secondaryPercent = secondStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			tempValue = retailPrice.subtract(primaryStoreOpenAmount).subtract(secondStoreOpenAmount);
			tempValueOfNoPre = tempValue;

			umb01Dto.getPriceCalParam().setNoPreRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenRate(toBigDecimal(primaryPercent).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenAmount(toBigDecimal(primaryStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenRate(toBigDecimal(secondaryPercent).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenAmount(toBigDecimal(secondStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenRate(toBigDecimal(primaryPercent.add(secondaryPercent)).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenAmount(toBigDecimal(primaryStoreOpenAmount.add(secondStoreOpenAmount)).toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice1(toBigDecimal(tempValue).toString());

			totalRetailPrice = retailPrice.add(unitPriceForeheadColor);
			primaryPercent = primaryStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			secondaryPercent = secondStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			tempValue = retailPrice.add(unitPriceForeheadColor).subtract(primaryStoreOpenAmount)
					.subtract(secondStoreOpenAmount);

			umb01Dto.getPriceCalParam().setLargeRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setLargeUnitPriceForehead1(toBigDecimal(unitPriceForeheadColor).toString());
			umb01Dto.getPriceCalParam().setLargeTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setLargePrimaryOpenRate(toBigDecimal(primaryPercent).toString());
			umb01Dto.getPriceCalParam().setLargePrimaryOpenAmount(toBigDecimal(primaryStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setLargeSecondaryOpenRate(toBigDecimal(primaryPercent).toString());
			umb01Dto.getPriceCalParam().setLargeSecondaryOpenAmount(toBigDecimal(secondStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setLargeTotalOpenRate(toBigDecimal(primaryPercent.add(secondaryPercent)).toString());
			umb01Dto.getPriceCalParam()
					.setLargeTotalOpenAmount(toBigDecimal(primaryStoreOpenAmount.add(secondStoreOpenAmount)).toString());
			umb01Dto.getPriceCalParam().setLargePartitionUnitPrice1(toBigDecimal(tempValue).toString());
			umb01Dto.getPriceCalParam().setCalLargeUnitPriceForehead(toBigDecimal(tempValue.add(tempValueOfNoPre)).toString());
			
			umb01Dto.getPriceRefDto().setPrimaryStoreOpenRate(primaryPercent);
			umb01Dto.setSecondStoreOpenRate(secondaryPercent);
			
			umb01Dto.getPriceCalParam().setPattern("6");
		}

		// pattern 7
		if (BigDecimal.ZERO.compareTo(retailPrice)!=0 && BigDecimal.ZERO.compareTo(unitPriceSmallParcel)!=0
				&& BigDecimal.ZERO.compareTo(unitPriceForeheadColor)!=0 && BigDecimal.ZERO.compareTo(primaryStoreOpenRate)!=0
				&& BigDecimal.ZERO.compareTo(primaryStoreOpenAmount)==0 && BigDecimal.ZERO.compareTo(secondStoreOpenRate)!=0
				&& BigDecimal.ZERO.compareTo(secondStoreOpenAmount)==0) {

			totalRetailPrice = retailPrice;
			tempValue = totalRetailPrice;
			umb01Dto.getPriceCalParam().setDeliRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliPartitionUnitPrice1(toBigDecimal(tempValue).toString());

			primaryDiscount = totalRetailPrice.multiply(primaryStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			secondaryDiscount = totalRetailPrice.multiply(secondStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			tempValue = retailPrice.subtract(primaryDiscount).subtract(secondaryDiscount);
			tempValueOfNoPre = tempValue;

			umb01Dto.getPriceCalParam().setNoPreRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenRate(toBigDecimal(primaryStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenAmount(toBigDecimal(primaryDiscount).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenRate(toBigDecimal(secondStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenAmount(toBigDecimal(secondaryDiscount).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenRate(toBigDecimal(primaryStoreOpenRate.add(secondStoreOpenRate)).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenAmount(toBigDecimal(primaryDiscount.add(secondaryDiscount)).toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice1(toBigDecimal(tempValue).toString());

			totalRetailPrice = retailPrice.add(unitPriceForeheadColor);
			primaryDiscount = totalRetailPrice.multiply(primaryStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			secondaryDiscount = totalRetailPrice.multiply(secondStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			tempValue = totalRetailPrice.subtract(primaryDiscount).subtract(secondaryDiscount);

			umb01Dto.getPriceCalParam().setLargeRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setLargeUnitPriceForehead1(toBigDecimal(unitPriceForeheadColor).toString());
			umb01Dto.getPriceCalParam().setLargeTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setLargePrimaryOpenRate(toBigDecimal(primaryStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setLargePrimaryOpenAmount(toBigDecimal(primaryDiscount).toString());
			umb01Dto.getPriceCalParam().setLargeSecondaryOpenRate(toBigDecimal(secondStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setLargeSecondaryOpenAmount(toBigDecimal(secondaryDiscount).toString());
			umb01Dto.getPriceCalParam().setLargeTotalOpenRate(toBigDecimal(primaryStoreOpenRate.add(secondStoreOpenRate)).toString());
			umb01Dto.getPriceCalParam().setLargeTotalOpenAmount(toBigDecimal(primaryDiscount.add(secondaryDiscount)).toString());
			umb01Dto.getPriceCalParam().setLargePartitionUnitPrice1(toBigDecimal(tempValue).toString());
			umb01Dto.getPriceCalParam().setCalLargeUnitPriceForehead(toBigDecimal(tempValue.add(tempValueOfNoPre)).toString());


			totalRetailPrice = retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor);
			primaryDiscount = totalRetailPrice.multiply(primaryStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			secondaryDiscount = totalRetailPrice.multiply(secondStoreOpenRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
			tempValue = totalRetailPrice.subtract(primaryDiscount).subtract(secondaryDiscount);

			umb01Dto.getPriceCalParam().setSmallRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setSmallUnitPriceParcel1(toBigDecimal(unitPriceSmallParcel).toString());
			umb01Dto.getPriceCalParam().setSmallUnitPriceForehead1(toBigDecimal(unitPriceForeheadColor).toString());
			umb01Dto.getPriceCalParam().setSmallTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setSmallPrimaryOpenRate(toBigDecimal(primaryStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setSmallPrimaryOpenAmount(toBigDecimal(primaryDiscount).toString());
			umb01Dto.getPriceCalParam().setSmallSecondaryOpenRate(toBigDecimal(secondStoreOpenRate).toString());
			umb01Dto.getPriceCalParam().setSmallSecondaryOpenAmount(toBigDecimal(secondaryDiscount).toString());
			umb01Dto.getPriceCalParam().setSmallTotalOpenRate(toBigDecimal(primaryStoreOpenRate.add(secondStoreOpenRate)).toString());
			umb01Dto.getPriceCalParam().setSmallTotalOpenAmount(toBigDecimal(primaryDiscount.add(secondaryDiscount)).toString());
			umb01Dto.getPriceCalParam().setSmallPartitionUnitPrice1(toBigDecimal(tempValue).toString());
			BigDecimal value = totalRetailPrice
					.multiply((BigDecimal.valueOf(1)).subtract((primaryStoreOpenRate.add(secondStoreOpenRate))
							.divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING)));
			umb01Dto.getPriceCalParam().setCalSmallUnitPriceParcel(toBigDecimal(value).toString());
			umb01Dto.getPriceCalParam().setCalSmallUnitPriceForehead(toBigDecimal(tempValue.add(tempValueOfNoPre)).toString());

			umb01Dto.getPriceCalParam().setPattern("7");
		}

		// pattern 8
		if (BigDecimal.ZERO.compareTo(retailPrice)!=0 && BigDecimal.ZERO.compareTo(unitPriceSmallParcel)!=0
				&& BigDecimal.ZERO.compareTo(unitPriceForeheadColor)!=0 && BigDecimal.ZERO.compareTo(primaryStoreOpenRate)==0
				&& BigDecimal.ZERO.compareTo(primaryStoreOpenAmount)!=0 && BigDecimal.ZERO.compareTo(secondStoreOpenRate)==0
				&& BigDecimal.ZERO.compareTo(secondStoreOpenAmount)!=0) {

			totalRetailPrice = retailPrice;
			tempValue = totalRetailPrice;
			umb01Dto.getPriceCalParam().setDeliRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setDeliPartitionUnitPrice1(toBigDecimal(tempValue).toString());

			primaryPercent = primaryStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			secondaryPercent = secondStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			tempValue = retailPrice.subtract(primaryStoreOpenAmount).subtract(secondStoreOpenAmount);
			tempValueOfNoPre = tempValue;

			umb01Dto.getPriceCalParam().setNoPreRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenRate(toBigDecimal(primaryPercent).toString());
			umb01Dto.getPriceCalParam().setNoPrePrimaryOpenAmount(toBigDecimal(primaryStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenRate(toBigDecimal(secondaryPercent).toString());
			umb01Dto.getPriceCalParam().setNoPreSecondaryOpenAmount(toBigDecimal(secondStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenRate(toBigDecimal(primaryPercent.add(secondaryPercent)).toString());
			umb01Dto.getPriceCalParam().setNoPreTotalOpenAmount(toBigDecimal(primaryStoreOpenAmount.add(secondStoreOpenAmount)).toString());
			umb01Dto.getPriceCalParam().setNoPrePartitionUnitPrice1(toBigDecimal(tempValue).toString());

			totalRetailPrice = retailPrice.add(unitPriceForeheadColor);
			primaryPercent = primaryStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			secondaryPercent = secondStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			tempValue = retailPrice.add(unitPriceForeheadColor).subtract(primaryStoreOpenAmount)
					.subtract(secondStoreOpenAmount);

			umb01Dto.getPriceCalParam().setLargeRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setLargeUnitPriceForehead1(toBigDecimal(unitPriceForeheadColor).toString());
			umb01Dto.getPriceCalParam().setLargeTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setLargePrimaryOpenRate(toBigDecimal(primaryPercent).toString());
			umb01Dto.getPriceCalParam().setLargePrimaryOpenAmount(toBigDecimal(primaryStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setLargeSecondaryOpenRate(toBigDecimal(secondaryPercent).toString());
			umb01Dto.getPriceCalParam().setLargeSecondaryOpenAmount(toBigDecimal(secondStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setLargeTotalOpenRate(toBigDecimal(primaryPercent.add(secondaryPercent)).toString());
			umb01Dto.getPriceCalParam()
					.setLargeTotalOpenAmount(toBigDecimal(primaryStoreOpenAmount.add(secondStoreOpenAmount)).toString());
			umb01Dto.getPriceCalParam().setLargePartitionUnitPrice1(toBigDecimal(tempValue).toString());
			umb01Dto.getPriceCalParam().setCalLargeUnitPriceForehead(toBigDecimal(tempValue.add(tempValueOfNoPre)).toString());

			totalRetailPrice = retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor);
			primaryPercent = primaryStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			secondaryPercent = secondStoreOpenAmount.divide(totalRetailPrice, 2, RoundingMode.CEILING)
					.multiply(BigDecimal.valueOf(100));
			tempValue = retailPrice.add(unitPriceForeheadColor).subtract(primaryStoreOpenAmount)
					.subtract(secondStoreOpenAmount);

			umb01Dto.getPriceCalParam().setSmallRetailPrice1(toBigDecimal(retailPrice).toString());
			umb01Dto.getPriceCalParam().setSmallUnitPriceParcel1(toBigDecimal(unitPriceSmallParcel).toString());
			umb01Dto.getPriceCalParam().setSmallUnitPriceForehead1(toBigDecimal(unitPriceForeheadColor).toString());
			umb01Dto.getPriceCalParam().setSmallTotalRetailPrice1(toBigDecimal(totalRetailPrice).toString());
			umb01Dto.getPriceCalParam().setSmallPrimaryOpenRate(toBigDecimal(primaryPercent).toString());
			umb01Dto.getPriceCalParam().setSmallPrimaryOpenAmount(toBigDecimal(primaryStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setSmallSecondaryOpenRate(toBigDecimal(secondaryPercent).toString());
			umb01Dto.getPriceCalParam().setSmallSecondaryOpenAmount(toBigDecimal(secondStoreOpenAmount).toString());
			umb01Dto.getPriceCalParam().setSmallTotalOpenRate(toBigDecimal(primaryPercent.add(secondaryPercent)).toString());
			umb01Dto.getPriceCalParam()
					.setSmallTotalOpenAmount(toBigDecimal(primaryStoreOpenAmount.add(secondStoreOpenAmount)).toString());
			umb01Dto.getPriceCalParam().setSmallPartitionUnitPrice1(toBigDecimal(tempValue).toString());
			BigDecimal value = totalRetailPrice
					.multiply((BigDecimal.valueOf(1)).subtract((primaryPercent.add(secondaryPercent))
							.divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING)));
			umb01Dto.getPriceCalParam().setCalSmallUnitPriceParcel(toBigDecimal(value).toString());
			umb01Dto.getPriceCalParam().setCalSmallUnitPriceForehead(toBigDecimal(tempValue.add(tempValueOfNoPre)).toString());

			umb01Dto.getPriceCalParam().setPattern("8");
		}

		// make XML table price
		outputHtml = new DataFlowUtil().transformXML2HTML(mitsubishiService.createXMLTablePrice(umb01Dto), FILE_XML);
	}

	private boolean checkStatusCdApplyed(String statusCd) {
		return ConstStatus.STATUS_BEFORE_APPLY.equals(statusCd) || ConstStatus.STATUS_BACKED_AWAY.equals(statusCd)
				|| ConstStatus.STATUS_SENT_BACK.equals(statusCd);
	}
	
	/**
	 * check null value of BigDecimal
	 */
	private static BigDecimal toBigDecimal(BigDecimal value) {
		if (value == null) {
			return BigDecimal.ZERO;
		}
		return value.setScale(2, RoundingMode.HALF_DOWN);
	}
	
	public static boolean isBlank(BigDecimal value) {
		return (value == null || value.signum() == 0);
	}

	private void initItems(List<ClassInfo> classInfos) throws Exception {
		// 用途参照
		usageRef = WebDbUtils.getChardata1ByClassNo(classInfos, MitsubishiConst.CLASS_NO.CLASSNO_12.getValue());
		if (!StringUtils.nullOrBlank(umb01Dto.getPriceUnitRefDto().getUsageCD())) {
			Object[] results = xdbRemote.findWebDBInfoByCode(NumberUtils.toLong(usageRef), umb01Dto.getPriceUnitRefDto().getUsageCD(), usageRef);
			String name = (String) results[1];
			WDBRefParam param = new WDBRefParam();
			param.setTargetFieldID(umb01Dto.getPriceUnitRefDto().getUsageCD());
			param.setTargetFieldName(name);
			umb01Dto.getPriceUnitRefDto().setUsageRef(param);
		}

		// 伺い理由
		for (ClassInfo classInfo : classInfos) {
			if ("REASON_INQUIRY".equals(classInfo.getClassName())) {
				reasonInquiryList.add(classInfo.getChardata1());
			}
		}
		
		umb01Dto.getPriceCalParam().setPattern("0");
		calculateValue();

		// 適用開始日
		if (umb01Dto.getPriceRefDto().getApplicationStartDate() == null) {
			Date nowDate = new Date();
			umb01Dto.getPriceRefDto().setApplicationStartDate(DateUtils.addDate(nowDate, "yyyy/MM/dd", -1));
		}
		
		tempPartitionPrice = umb01Dto.getPartitionUnitPrice();
		
		// 契約番号
		if (MitsubishiConst.MODE_NEW.equals(currentMode)) {
			BigDecimal numericCode = WebDbUtils.getNumdata1ByClassNo(classInfos,
					MitsubishiConst.CLASS_NO.CLASSNO_20.getValue());
			String numericCodeString = numericCode.setScale(0, RoundingMode.DOWN).toString();
			umb01Dto.getPriceRefDto().setContractNumber(
					String.format("%1s%08d", loginUser.getCorpID(), Long.parseLong(numericCodeString)));
		}
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
	public List<String> getReasonInquiryList() {
		return reasonInquiryList;
	}

	/**
	 * @param reasonInquiryList the reasonInquiryList to set
	 */
	public void setReasonInquiryList(List<String> reasonInquiryList) {
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

	/**
	 * @return the currentStatus
	 */
	public String getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param currentStatus the currentStatus to set
	 */
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	/**
	 * @return the selectRateOrAmount
	 */
	public int getSelectRateOrAmount() {
		return selectRateOrAmount;
	}

	/**
	 * @param selectRateOrAmount the selectRateOrAmount to set
	 */
	public void setSelectRateOrAmount(int selectRateOrAmount) {
		this.selectRateOrAmount = selectRateOrAmount;
	}
	/**
	 * 既に仮単価マスタに登録済みのデータが存在する場合、取込を行わない。
	 * 全得意先CD、仕向先CD1、仕向先CD2、品名略号、カラーNo、グレード1、通貨CD、取引先枝番、価格形態、用途CDが全て一致するデータが存在する
	 * @param recordData
	 * @param recordObj
	 * @return
	 * @throws JSONException 
	 */
	public boolean checkDataAlreadyExists(UMB01TempDto recordData,JSONObject recordObj,String logFileFullPath) throws JSONException {
		//全得意先CD
		String customerCD = WebDbUtils.getValue(recordObj, MitsubishiConst.CUSTOMER_CD);
		//仕向先CD1
		 String destinationCD1 = WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD1);
		//仕向先CD2
		 String destinationCD2 = WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD2);
		//品名略号
		 String productNameAbbreviation = WebDbUtils.getValue(recordObj, MitsubishiConst.PRODUCT_NAME_ABBREVIATION);
		//カラーNo
		 String colorNo = WebDbUtils.getValue(recordObj, MitsubishiConst.COLOR_NO);
		//グレード1
		 String grade1 = WebDbUtils.getValue(recordObj, MitsubishiConst.GRADE_1);
		//通貨CD
		 String currenyCD = WebDbUtils.getValue(recordObj, MitsubishiConst.CURRENCY_CD);
		//取引先枝番
		 String branchNum = WebDbUtils.getValue(recordObj, MitsubishiConst.CLIENT_BRANCH_NUMBER);
		//価格形態
		 String priceForm = WebDbUtils.getValue(recordObj, MitsubishiConst.PRICE_FORM);
		//用途CD->用途参照
		 String usageRef = WebDbUtils.getValue(recordObj, MitsubishiConst.USAGE_REF);
		 
		 if(((customerCD == null && (recordData.getCustomerCD() == null || "".equals(recordData.getCustomerCD()))) || (customerCD != null && recordData.getCustomerCD() != null && customerCD.equals(recordData.getCustomerCD())))
			 &&	((destinationCD1 == null && (recordData.getDestinationCD1() == null || "".equals(recordData.getDestinationCD1()))) || (destinationCD1 != null && recordData.getDestinationCD1() != null && destinationCD1.equals(recordData.getDestinationCD1())))
			 &&	((destinationCD2 == null && (recordData.getDestinationCD2() == null || "".equals(recordData.getDestinationCD2()))) || (destinationCD2 != null && recordData.getDestinationCD2() != null && destinationCD2.equals(recordData.getDestinationCD2())))
			 &&	((productNameAbbreviation == null && (recordData.getProductNameAbbreviation() == null || "".equals(recordData.getProductNameAbbreviation()))) || (productNameAbbreviation != null && recordData.getProductNameAbbreviation() != null && productNameAbbreviation.equals(recordData.getProductNameAbbreviation())))
			 &&	((colorNo == null && (recordData.getColorNo() == null || "".equals(recordData.getColorNo()))) || (colorNo != null && recordData.getColorNo() != null && colorNo.equals(recordData.getColorNo())))
			 &&	((grade1 == null && (recordData.getGrade1() == null || "".equals(recordData.getGrade1()))) || (grade1 != null && recordData.getGrade1() != null && grade1.equals(recordData.getGrade1())))
			 &&	((currenyCD == null && (recordData.getCurrencyCD() == null || "".equals(recordData.getCurrencyCD()))) || (currenyCD != null && recordData.getCurrencyCD() != null && currenyCD.equals(recordData.getCurrencyCD())))
			 &&	((branchNum == null && (recordData.getClientBranchNumber() == null || "".equals(recordData.getClientBranchNumber()))) || (branchNum != null && recordData.getClientBranchNumber() != null && branchNum.equals(recordData.getClientBranchNumber())))
			 &&	((priceForm == null && (recordData.getPriceForm() == null || "".equals(recordData.getPriceForm()))) || (priceForm != null && recordData.getPriceForm() != null && priceForm.equals(recordData.getPriceForm())))
			 &&	((usageRef == null && (recordData.getUsageRef() == null || "".equals(recordData.getUsageRef()))) || (usageRef != null && recordData.getUsageRef() != null && usageRef.equals(recordData.getUsageRef())))) {
			
			 //StringBuilder getLog = new StringBuilder();
			 //getLog.append(MitsubishiConst.DATA_NO).append(MitsubishiConst.COLON);

			 //log: start data no
			 //LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),getLog.append(recordData.getDataNo()).append("はすでに存在します。").toString());
			 return false;
		 }
		return true;
	}

	/**
	 * @return the currentDataNo
	 */
	public String getCurrentDataNo() {
		return currentDataNo;
	}

	/**
	 * @param currentDataNo the currentDataNo to set
	 */
	public void setCurrentDataNo(String currentDataNo) {
		this.currentDataNo = currentDataNo;
	}

	/**
	 * @return the tempPartitionPrice
	 */
	public BigDecimal getTempPartitionPrice() {
		return tempPartitionPrice;
	}

	/**
	 * @param tempPartitionPrice the tempPartitionPrice to set
	 */
	public void setTempPartitionPrice(BigDecimal tempPartitionPrice) {
		this.tempPartitionPrice = tempPartitionPrice;
	}

	/**
	 * @return the showBtnStatus
	 */
	public boolean isShowBtnStatus() {
		return showBtnStatus;
	}

	/**
	 * @param showBtnStatus the showBtnStatus to set
	 */
	public void setShowBtnStatus(boolean showBtnStatus) {
		this.showBtnStatus = showBtnStatus;
	}

	/**
	 * @return the contactSrceen
	 */
	public String getContactSrceen() {
		return contactSrceen;
	}

	/**
	 * @param contactSrceen the contactSrceen to set
	 */
	public void setContactSrceen(String contactSrceen) {
		this.contactSrceen = contactSrceen;
	}
}
