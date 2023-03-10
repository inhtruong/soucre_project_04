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

	// ??????????????????
	private Long appRecepNo;

	private String fileUrlPath;

	private String selectEmp = "";

	private Employee emp;

	/** ????????? */
	private Date applyDate;
	/** ?????? */
	private String titleApply;
	/** ????????? */
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
			LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "Error: ?????????????????????????????????????????????");
			throw new Exception("Error: ?????????????????????????????????????????????");
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
		
		if ((String) FacesContextUtils.getFromFlash("dataNoBack")== null || "".equals(FacesContextUtils.getFromFlash("dataNoBack"))) {
			// get value from WebDB
			umb01Dto = mitsubishiService.getDataMitsubishi(currentDataNo, dbType);
			if (umb01Dto == null) {
				LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "Error:" + currentDataNo + "?????????????????????");
				throw new Exception("Error: " + MitsubishiConst.DATA_NO + " " + currentDataNo + " ?????????????????????");
			}
		}
		// set status apply
		if (1 == dbType) {
			setCurrentStatus(umb01Dto.getPriceUnitRefDto().getStatusCD());
			// show/hide button update
			showBtnStatus = showBtnUpdateStatus();
		}
		if (2 == dbType) {
			if (umb01Dto.getPriceRefDto().getStatusCD() == null) {
				LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), "Error: ???????????????????????????");
				throw new Exception("Error: ???????????????????????????");
			}
			setCurrentStatus(umb01Dto.getPriceRefDto().getStatusCD());
		}
		// initialization item
		initItems(webDBClassInfos);
		
		// make XML table price
		outputHtml = new DataFlowUtil().transformXML2HTML(mitsubishiService.createXMLTablePrice(umb01Dto), FILE_XML);
	}

	/**
	 * ?????????????????? ????????????????????????????????????????????????????????????
	 * 
	 * @return "confirm"
	 */
	public String toConfirm() {
		
		// ???????????? check
		if (isBlank(umb01Dto.getPriceRefDto().getRetailPrice())) {
			boolean validtarget = true;
			UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
			UIComponent i_target = root.findComponent("frm:i_tableData10:tab1_retailPrice");
			if (i_target != null) {
				((UIInput) i_target).setValid(false);
			}
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "?????????????????????????????????", "");
			validtarget = false;
			return StringUtils.EMPTY;
		}
		
		// ????????????????????????????????????
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
		dataflowHelper.setRouteEdit(true); // ??????????????????????????????
		dataflowHelper.setApplyContent(mitsubishiService.createXMLTablePrice(umb01Dto));

		try {
			appRecepNo = dataflowHelper.prepareApply();
			facesMessages.add("????????????????????????????????????");
			return "confirm";
		} catch (ApplyException e) {
			return StringUtils.EMPTY;
		}
	}

	/**
	 * ?????????????????????????????????
	 * @param numericCode 
	 * 
	 * @return "apply"
	 * @throws Exception
	 **/
	public String apply() throws Exception {
		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);

		try {
			// check apply
			if (!checkStatusCdApplyed(currentStatus)) {
				LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(), " Error: ?????????????????????????????????????????????????????????????????????????????????????????????");
				facesMessages.add(FacesMessage.SEVERITY_ERROR, "?????????????????????????????????????????????????????????????????????????????????????????????", "");
				return StringUtils.EMPTY;
			}
			// 
			if (StringUtils.nullOrBlank(umb01Dto.getPriceRefDto().getAppRecepNoCancel())
					&& !StringUtils.nullOrBlank(umb01Dto.getPriceRefDto().getAppRecepNo())) {
				currentStatus = "1001";
			}
			
			umb01Dto.getPriceRefDto().setUnitPriceBefRevision(tempPartitionPrice);
			
			// ?????????
			switch (umb01Dto.getPriceCalParam().getPattern()) {
				case "3":
				case "4":
					umb01Dto.getPriceRefDto().setLotQuantity("0");
					break;
				case "5":
				case "6":
					umb01Dto.getPriceRefDto().setLotQuantity("0,100");
					break;
				case "7":
				case "8":
					umb01Dto.getPriceRefDto().setLotQuantity("0,100,300");
					break;
				default:
					umb01Dto.getPriceRefDto().setLotQuantity("0");
					break;
			}

			// ????????????
			dataflowHelper.apply();
			// ?????????????????????????????????.
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

			facesMessages.add(FacesMessage.SEVERITY_INFO, "?????????????????????", "");
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
	 * ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	 * 
	 * @return
	 * @throws Exception 
	 */
	public boolean showBtnUpdateStatus() throws Exception {
		PriceUnitRefDto dto = umb01Dto.getPriceUnitRefDto();
		// get value from WebDB Master
		Umb01Dto umb01Master = mitsubishiService.getDataMitsubishi(currentDataNo, 2);
		Umb01Dto umb01MasterTemp = new Umb01Dto();
		if (umb01Master != null) {
			PriceUnitRefDto dtoMaster = umb01Master.getPriceUnitRefDto();
			if (pricesMatch(dto, dtoMaster)) {
				return true;
			}
		}
		return false;
	}	

	private boolean pricesMatch(PriceUnitRefDto current, PriceUnitRefDto master) {
		String currentCustomerCD = "";
		String currenDestinationCD1 = "";
		String currentDestinationCD2 = "";
		String currentProductNameAbbreviation = "";
		String currentColorNo = "";
		String currenCurrencyCD = "";
		String currentClientBranchNumber = "";
		String currentPriceForm = "";
		if (current.getCustomerCD()!=null) { 
			currentCustomerCD = current.getCustomerCD();
		}
		if (current.getDestinationCD1()!=null) { 
			currenDestinationCD1 = current.getDestinationCD1();
		}
		if (current.getDestinationCD2()!=null) { 
			currentDestinationCD2 = current.getDestinationCD2();
		}
		if (current.getProductNameAbbreviation()!=null) { 
			currentProductNameAbbreviation = current.getProductNameAbbreviation();
		}
		if (current.getColorNo()!=null) { 
			currentColorNo = current.getColorNo();
		}
		if (current.getCurrencyCD()!=null) { 
			currenCurrencyCD = current.getCurrencyCD();
		}
		if (current.getClientBranchNumber()!=null) { 
			currentClientBranchNumber = current.getClientBranchNumber();
		}
		if (current.getPriceForm()!=null) { 
			currentPriceForm = current.getPriceForm();
		}
		return (( currentCustomerCD == "" && master.getCustomerCD() ==null) || currentCustomerCD.equals(master.getCustomerCD()))
				&& (( currenDestinationCD1 == "" && master.getDestinationCD1() ==null) ||currenDestinationCD1.equals(master.getDestinationCD1()))
				&& (( currentDestinationCD2 == "" && master.getDestinationCD2() ==null) ||currentDestinationCD2.equals(master.getDestinationCD2()))
				&& (( currentProductNameAbbreviation == "" && master.getProductNameAbbreviation() ==null) ||currentProductNameAbbreviation.equals(master.getProductNameAbbreviation()))
				&& (( currentColorNo == "" && master.getColorNo() ==null) ||currentColorNo.equals(master.getColorNo()))
				&& (( currenCurrencyCD == "" && master.getCurrencyCD() ==null) ||currenCurrencyCD.equals(master.getCurrencyCD()))
				&& (( currentClientBranchNumber == "" && master.getClientBranchNumber() ==null) ||currentClientBranchNumber.equals(master.getClientBranchNumber()))
				&& (( currentPriceForm == "" && master.getPriceForm() ==null) ||currentPriceForm.equals(master.getPriceForm()));
	}

	public String processUpdateStatus() throws Exception {
		List<ClassInfo> webDBClassInfos = mitsubishiService.getInfoWebDb();
		String logFileFullPath = LogUtils.generateLogFileFullPath(webDBClassInfos);

		Umb01Dto umb01Master = mitsubishiService.getDataMitsubishi(currentDataNo, 2);
		Umb01Dto umb01MasterTemp = new Umb01Dto();
		String statusCdMaster = umb01Master.getPriceRefDto().getStatusCD();
		// check record ??????
		if (ConstStatus.STATUS_UNDER_DELIBERATION.equals(statusCdMaster)) {
			LogUtils.writeLog(logFileFullPath, COMMON_NO.COMMON_NO_UMB01.getValue(),
					" Error: ????????????????????????????????????????????????????????????????????????????????????");
			facesMessages.add(FacesMessage.SEVERITY_ERROR, "????????????????????????????????????????????????????????????????????????????????????", "");
			return StringUtils.EMPTY;
		}
		
		if(ConstStatus.STATUS_APPROVED.equals(statusCdMaster)) {
			umb01MasterTemp = mitsubishiService.getDataMitsubishi(currentDataNo, 3);
			if (umb01MasterTemp != null) {
				umb01Dto = new Umb01Dto();
				umb01Dto = umb01MasterTemp;
				umb01Dto.getPriceRefDto().setStatusCD(ConstStatus.STATUS_APPROVED);
			}
		}
		
		// insert record form WebDB backup to WebDB master
		mitsubishiService.registerRecordDbPrice(logFileFullPath, umb01Dto);
		// export CSV
		//export csv
		mitsubishiService.exportCsvBtnStatusUMB01(umb01Dto);
		
		return "apply";
	}

	/**
	 * ??????????????????
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
			if (args != null && args.length>0) {
				 csvFilePath = args[0];
			}
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
					// b.1. Insert ?????????????????? get classInfo by commonNo: UMB01
					WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0, 1);
					
					// find record data at webDB price
					JSONArray rsJson = mitsubishiService.findDataUmbByCondition(webdbUtils,MitsubishiConst.DATA_NO, StringUtils.toEmpty(recordData.getDataNo()), null);
					// Khong tim duoc du lieu t????ng ung
					if (rsJson == null || rsJson.length() == 0) {
						String result = webdbUtils.registJsonObject(
								putDataUmitsubishiTemp(webDBClassInfos, recordData, logFileFullPath), true);
						if (!ConvertUtils.isNullOrEmpty(result)) {
							result = "?????????NO:"+ recordData.getDataNo() + "???" +result  ;
							System.out.println(result);
							LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(), result);
						}else {
							recordImport++;
						}
					}else {
						JSONObject recordObj = rsJson.getJSONObject(0);
						//???T/H t???n t???i to??n b??? d??? li???u c?? ????????????CD????????????CD1????????????CD2???????????????????????????No???????????????1???															
						//??????CD??????????????????????????????????????????CD kh???p nhau	
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
						"??????????????????????????????: " + String.valueOf(recordImport));
				LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
						"????????????????????????????????????: " + String.valueOf(recordSkip));
				LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),
						"??????????????????: " + String.valueOf(recordTotal));
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

		/** ????????????NO */
		regDataJson.put(MitsubishiConst.DATA_LINE_NO,
				WebDbUtils.createRecordItemNumber(NumberUtils.toLong(umbItem.getDataLineNo())));
		/** ?????????NO */
		regDataJson.put(MitsubishiConst.DATA_NO,
				WebDbUtils.createRecordItemNumber(NumberUtils.toLong(umbItem.getDataNo())));
		/** ??????NO */
		regDataJson.put(MitsubishiConst.MANAGER_NO,
				WebDbUtils.createRecordItemNumber(NumberUtils.toLong(umbItem.getManagerNo())));
		/** ????????????????????????????????? */
		regDataJson.put(MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME,
				WebDbUtils.createRecordItem(umbItem.getSrcCreateDate()));
		/** ??????CD */
		regDataJson.put(MitsubishiConst.COMPANY_CD, WebDbUtils.createRecordItem(umbItem.getCompanyCD()));
		/** ??????CD */
		regDataJson.put(MitsubishiConst.TRANSACTION_CD, WebDbUtils.createRecordItem(umbItem.getTransactionCD()));
		/** ????????????CD */
		regDataJson.put(MitsubishiConst.SALES_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(umbItem.getSalesDepartmentCD()));
		/** ????????????CD */
		regDataJson.put(MitsubishiConst.UPPER_CATEGORY_CD, WebDbUtils.createRecordItem(umbItem.getUpperCategoryCD()));
		/** ????????????CD */
		regDataJson.put(MitsubishiConst.ACCOUNT_DEPARTMENT_CD,
				WebDbUtils.createRecordItem(umbItem.getAccountDepartmentCD()));
		/** ??????NO */
		regDataJson.put(MitsubishiConst.ORDER_NO, WebDbUtils.createRecordItem(umbItem.getOrderNo()));
		/** ????????????NO */
		regDataJson.put(MitsubishiConst.SALES_ORDER_NO, WebDbUtils.createRecordItem(umbItem.getSalesOrderNo()));
		/** ?????????CD */
		regDataJson.put(MitsubishiConst.CUSTOMER_CD, WebDbUtils.createRecordItem(umbItem.getCustomerCD()));
		/** ???????????? */
		regDataJson.put(MitsubishiConst.CUSTOMER_NAME, WebDbUtils.createRecordItem(umbItem.getCustomerName()));
		/** ?????????CD1 */
		regDataJson.put(MitsubishiConst.DESTINATION_CD1, WebDbUtils.createRecordItem(umbItem.getDestinationCD1()));
		/** ????????????1 */
		regDataJson.put(MitsubishiConst.DESTINATION_NAME1, WebDbUtils.createRecordItem(umbItem.getDestinationName1()));
		/** ?????????CD2 */
		regDataJson.put(MitsubishiConst.DESTINATION_CD2, WebDbUtils.createRecordItem(umbItem.getDestinationCD2()));
		/** ????????????2 */
		regDataJson.put(MitsubishiConst.DESTINATION_NAME2, WebDbUtils.createRecordItem(umbItem.getDestinationName2()));
		/** ?????????CD */
		regDataJson.put(MitsubishiConst.DESTINATION_CD, WebDbUtils.createRecordItem(umbItem.getDestinationCD()));
		/** ???????????? */
		regDataJson.put(MitsubishiConst.DELIVERY_DESTINATION_NAME,
				WebDbUtils.createRecordItem(umbItem.getDeliveryDestinationName()));
		/** ???????????? */
		regDataJson.put(MitsubishiConst.PRODUCT_NAME_ABBREVIATION,
				WebDbUtils.createRecordItem(umbItem.getProductNameAbbreviation()));
		/** ?????????NO */
		regDataJson.put(MitsubishiConst.COLOR_NO, WebDbUtils.createRecordItem(umbItem.getColorNo()));
		/** ????????????1 */
		regDataJson.put(MitsubishiConst.GRADE_1, WebDbUtils.createRecordItem(umbItem.getGrade1()));
		/** ?????????????????? */
		regDataJson.put(MitsubishiConst.USER_ITEM, WebDbUtils.createRecordItem(umbItem.getUserItem()));
		/** ??????CD */
		regDataJson.put(MitsubishiConst.CURRENCY_CD, WebDbUtils.createRecordItem(umbItem.getCurrencyCD()));
		/** ????????????CD */
		regDataJson.put(MitsubishiConst.TRANSACTION_UNIT_CD,
				WebDbUtils.createRecordItem(umbItem.getTransactionUnitCD()));
		/** ?????? */
		regDataJson.put(MitsubishiConst.PACKING, WebDbUtils.createRecordItem(umbItem.getPacking()));
		/** ??????????????? */
		regDataJson.put(MitsubishiConst.CLIENT_BRANCH_NUMBER,
				WebDbUtils.createRecordItem(umbItem.getClientBranchNumber()));
		/** ???????????? */
		regDataJson.put(MitsubishiConst.PRICE_FORM, WebDbUtils.createRecordItem(umbItem.getPriceForm()));
		/** ???????????? */
		regDataJson.put(MitsubishiConst.USAGE_REF, WebDbUtils.createRecordItem(umbItem.getUsageRef()));
		/** ?????????????????? */
		regDataJson.put(MitsubishiConst.SCHEDULED_DELIVERY_DATE,
				WebDbUtils.createRecordItem(umbItem.getDeliveryDate()));
		/** ????????????CD1 */
		regDataJson.put(MitsubishiConst.PRODUCT_NAME_CLASS_CD1,
				WebDbUtils.createRecordItem(umbItem.getProductNameClassCD1()));
		/** ????????? */
		regDataJson.put(MitsubishiConst.ORDER_DATE, WebDbUtils.createRecordItem(umbItem.getOrderDate()));
		/** ??????????????? */
		regDataJson.put(MitsubishiConst.REGISTRAR, WebDbUtils.createRecordItem(umbItem.getRegistrar()));
		/** ???????????????CD */
		regDataJson.put(MitsubishiConst.SALESPERSON_CD, WebDbUtils.createRecordItem(umbItem.getSalespersonCD()));
		/** ?????????????????? */
		regDataJson.put(MitsubishiConst.SALESPERSON_NAME, WebDbUtils.createRecordItem(umbItem.getSalespersonName()));
		/** ?????? */
		regDataJson.put(MitsubishiConst.STATUS_CD, WebDbUtils.createRecordItem(umbItem.getStateCD()));
		/** ???????????????????????????(??????) */
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

		/** ????????????NO */
		umitsubishiTempDto.setDataLineNo(content[j++]);
		/** ?????????NO */
		umitsubishiTempDto.setDataNo(content[j++]);
		/** ????????????????????????????????? */
		umitsubishiTempDto.setSrcCreateDate(content[j++]);
		/** ??????CD */
		umitsubishiTempDto.setCompanyCD(content[j++]);
		/** ??????CD */
		umitsubishiTempDto.setTransactionCD(content[j++]);
		/** ????????????CD */
		umitsubishiTempDto.setSalesDepartmentCD(content[j++]);
		/** ????????????CD */
		umitsubishiTempDto.setUpperCategoryCD(content[j++]);
		/** ????????????CD */
		umitsubishiTempDto.setAccountDepartmentCD(content[j++]);
		/** ??????NO */
		umitsubishiTempDto.setOrderNo(content[j++]);
		/** ????????????NO */
		umitsubishiTempDto.setSalesOrderNo(content[j++]);
		/** ?????????CD */
		umitsubishiTempDto.setCustomerCD(content[j++]);
		/** ???????????? */
		umitsubishiTempDto.setCustomerName(content[j++]);
		/** ?????????CD1 */
		umitsubishiTempDto.setDestinationCD1(content[j++]);
		/** ????????????1 */
		umitsubishiTempDto.setDestinationName1(content[j++]);
		/** ?????????CD2 */
		umitsubishiTempDto.setDestinationCD2(content[j++]);
		/** ????????????2 */
		umitsubishiTempDto.setDestinationName2(content[j++]);
		/** ?????????CD */
		umitsubishiTempDto.setDestinationCD(content[j++]);
		/** ???????????? */
		umitsubishiTempDto.setDeliveryDestinationName(content[j++]);
		/** ???????????? */
		umitsubishiTempDto.setProductNameAbbreviation(content[j++]);
		/** ?????????NO */
		umitsubishiTempDto.setColorNo(content[j++]);
		/** ????????????1 */
		umitsubishiTempDto.setGrade1(content[j++]);
		/** ?????????????????? */
		umitsubishiTempDto.setUserItem(content[j++]);
		/** ??????CD */
		umitsubishiTempDto.setCurrencyCD(content[j++]);
		/** ????????????CD */
		umitsubishiTempDto.setTransactionUnitCD(content[j++]);
		/** ?????? */
		umitsubishiTempDto.setPacking(content[j++]);
		/** ??????????????? */
		umitsubishiTempDto.setClientBranchNumber(content[j++]);
		/** ???????????? */
		umitsubishiTempDto.setPriceForm(content[j++]);
		/** ???????????? */
		umitsubishiTempDto.setUsageRef(content[j++]);
		/** ?????????????????? */
		umitsubishiTempDto.setDeliveryDate(content[j++]);
		/** ????????????CD1 */
		umitsubishiTempDto.setProductNameClassCD1(content[j++]);
		/** ????????? */
		umitsubishiTempDto.setOrderDate(content[j++]);
		/** ??????????????? */
		umitsubishiTempDto.setRegistrar(content[j++]);
		/** ???????????????CD */
		umitsubishiTempDto.setSalespersonCD(content[j++]);
		/** ?????????????????? */
		umitsubishiTempDto.setSalespersonName(content[j++]);
		/** ?????? */
		umitsubishiTempDto.setStateCD("1001");		
		return umitsubishiTempDto;

	}

	/**
	 * ?????????????????????????????????
	 *
	 * @return ??????????????????
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
		// ????????????
		BigDecimal retailPrice = toBigDecimal(umb01Dto.getPriceRefDto().getRetailPrice());
		// ??????????????????
		BigDecimal unitPriceSmallParcel = toBigDecimal(umb01Dto.getPriceRefDto().getUnitPriceSmallParcel());
		// ??????????????????
		BigDecimal unitPriceForeheadColor = toBigDecimal(umb01Dto.getPriceRefDto().getUnitPriceForeheadColor());
		// ??????????????????
		BigDecimal primaryStoreOpenRate = BigDecimal.valueOf(0);
		// ???????????????(??????)
		BigDecimal primaryStoreOpenAmount = BigDecimal.valueOf(0);
		// ??????????????????
		BigDecimal secondStoreOpenRate = BigDecimal.valueOf(0);
		// ??????????????????
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
		// ????????????
		usageRef = WebDbUtils.getChardata1ByClassNo(classInfos, MitsubishiConst.CLASS_NO.CLASSNO_12.getValue());
		if (!StringUtils.nullOrBlank(umb01Dto.getPriceUnitRefDto().getUsageCD())) {
			Object[] results = xdbRemote.findWebDBInfoByCode(NumberUtils.toLong(usageRef), umb01Dto.getPriceUnitRefDto().getUsageCD(), usageRef);
			String name ="";
			if(results != null && results.length>1) {
				name = (String) results[1];
			}
			WDBRefParam param = new WDBRefParam();
			param.setTargetFieldID(umb01Dto.getPriceUnitRefDto().getUsageCD());
			param.setTargetFieldName(name);
			umb01Dto.getPriceUnitRefDto().setUsageRef(param);
		}

		// ????????????
		for (ClassInfo classInfo : classInfos) {
			if ("REASON_INQUIRY".equals(classInfo.getClassName())) {
				reasonInquiryList.add(classInfo.getChardata1());
			}
		}
		
		umb01Dto.getPriceCalParam().setPattern("0");
		calculateValue();

		// ???????????????
		if (umb01Dto.getPriceRefDto().getApplicationStartDate() == null) {
			Date nowDate = new Date();
			umb01Dto.getPriceRefDto().setApplicationStartDate(DateUtils.addDate(nowDate, "yyyy/MM/dd", -1));
		}
		
		tempPartitionPrice = umb01Dto.getPartitionUnitPrice();
		
		// ????????????
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
	 * ???????????????????????????????????????????????????????????????????????????????????????????????????
	 * ????????????CD????????????CD1????????????CD2???????????????????????????No???????????????1?????????CD??????????????????????????????????????????CD?????????????????????????????????????????????
	 * @param recordData
	 * @param recordObj
	 * @return
	 * @throws JSONException 
	 */
	public boolean checkDataAlreadyExists(UMB01TempDto recordData,JSONObject recordObj,String logFileFullPath) throws JSONException {
		//????????????CD
		String customerCD = WebDbUtils.getValue(recordObj, MitsubishiConst.CUSTOMER_CD);
		//?????????CD1
		 String destinationCD1 = WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD1);
		//?????????CD2
		 String destinationCD2 = WebDbUtils.getValue(recordObj, MitsubishiConst.DESTINATION_CD2);
		//????????????
		 String productNameAbbreviation = WebDbUtils.getValue(recordObj, MitsubishiConst.PRODUCT_NAME_ABBREVIATION);
		//?????????No
		 String colorNo = WebDbUtils.getValue(recordObj, MitsubishiConst.COLOR_NO);
		//????????????1
		 String grade1 = WebDbUtils.getValue(recordObj, MitsubishiConst.GRADE_1);
		//??????CD
		 String currenyCD = WebDbUtils.getValue(recordObj, MitsubishiConst.CURRENCY_CD);
		//???????????????
		 String branchNum = WebDbUtils.getValue(recordObj, MitsubishiConst.CLIENT_BRANCH_NUMBER);
		//????????????
		 String priceForm = WebDbUtils.getValue(recordObj, MitsubishiConst.PRICE_FORM);
		//??????CD->????????????
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
			 //LogUtils.writeLog(logFileFullPath, MitsubishiConst.BATCH_ID.UMB01_BATCH.getValue(),getLog.append(recordData.getDataNo()).append("??????????????????????????????").toString());
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
