package net.poweregg.mitsubishi.action;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.json.JSONObject;

import net.poweregg.annotations.Login;
import net.poweregg.annotations.PEIntercepter;
import net.poweregg.annotations.RequestParameter;
import net.poweregg.common.ClassificationService;
import net.poweregg.common.entity.ClassInfo;
import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.mitsubishi.constant.MitsubishiConst.COMMON_NO;
import net.poweregg.mitsubishi.dto.Umb01Dto;
import net.poweregg.mitsubishi.dto.UmitsubishiTempDto;
import net.poweregg.mitsubishi.service.MitsubishiService;
import net.poweregg.mitsubishi.webdb.utils.CSVUtils;
import net.poweregg.mitsubishi.webdb.utils.WebDbConstant;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;
import net.poweregg.organization.entity.Employee;
import net.poweregg.ui.param.AttachFile;
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
	private static final String PRIORITY_URGENT = "0002";

	@EJB
	private ClassificationService classificationService;
	
	@EJB
	private MitsubishiService mitsubishiService;

	private Integer returnCode;

	private String csvFilePath;
	
	private Umb01Dto umb01Dto;
	
	@RequestParameter(value="datano")
	private String dataNo;
	
	@Inject
	@Login
	private LoginUser loginUser;
	
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
	
	private List<String> transactionList;
	private List<String> dataUpdateCategoryList;
	
	private String unitPriceDataRef;
	private String priceDataRef;
	private String usageRef;

	public String initUMB0102e() throws Exception {
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
		unitPriceDataRef= "";
		priceDataRef= "";
		usageRef= "";

		// TODO Instance transactionList, dataUpdateCategoryList

		umb01Dto = mitsubishiService.getDataMitsubishi(dataNo);
		return StringUtils.EMPTY;
	}

	public void executeBatch() throws Exception {
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
			List<UmitsubishiTempDto> dataList = new ArrayList<>();

			if (ArrayCollectionUtil.isCollectionNotNullOrEmpty(contentFile)) {
				for (int i = 0; i < contentFile.size(); i++) {
					String[] content = contentFile.get(i);
					UmitsubishiTempDto umitsubishiTemp = createUmitsubishiTemp(content);
					dataList.add(umitsubishiTemp);
				}
			}

			for (int i = 0; i < dataList.size(); i++) {
				UmitsubishiTempDto recordData = dataList.get(i);
				// b.1. Insert 前払勧奨情報 get classInfo by commonNo: UMB01
				List<ClassInfo> webDBClassInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
						COMMON_NO.COMMON_NO_UMB01.getValue());
				WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0);
				JSONObject regDataJson = new JSONObject();

				/** データ行NO */
				regDataJson.put(MitsubishiConst.DATA_LINE_NO,
						WebDbUtils.createRecordItemNumber(NumberUtils.toLong(recordData.getDataLineNo())));
				/** データNO */
				regDataJson.put(MitsubishiConst.DATA_NO,
						WebDbUtils.createRecordItemNumber(NumberUtils.toLong(recordData.getDataNo())));
				/** 送信元レコード作成日時 */
				regDataJson.put(MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME,
						WebDbUtils.createRecordItem(recordData.getSrcCreateDate()));
				/** 会社CD */
				regDataJson.put(MitsubishiConst.COMPANY_CD, WebDbUtils.createRecordItem(recordData.getCompanyCD()));
				/** 取引CD */
				regDataJson.put(MitsubishiConst.COMPANY_CD, WebDbUtils.createRecordItem(recordData.getTransactionCD()));
				/** 売上部門CD */
				regDataJson.put(MitsubishiConst.COMPANY_CD,
						WebDbUtils.createRecordItem(recordData.getSalesDepartmentCD()));
				/** 上位部門CD */
				regDataJson.put(MitsubishiConst.COMPANY_CD,
						WebDbUtils.createRecordItem(recordData.getUpperCategoryCD()));
				/** 会計部門CD */
				regDataJson.put(MitsubishiConst.COMPANY_CD,
						WebDbUtils.createRecordItem(recordData.getAccountDepartmentCD()));
				/** 受注NO */
				regDataJson.put(MitsubishiConst.SALES_ORDER_NO, WebDbUtils.createRecordItem(recordData.getOrderNo()));
				/** 受注明細NO */
				regDataJson.put(MitsubishiConst.SALES_ORDER_NO,
						WebDbUtils.createRecordItem(recordData.getSalesOrderNo()));
				/** 得意先CD */
				regDataJson.put(MitsubishiConst.TRANSACTION_CD,
						WebDbUtils.createRecordItem(recordData.getCustomerCD()));
				/** 得意先名 */
				regDataJson.put(MitsubishiConst.CUSTOMER_NAME,
						WebDbUtils.createRecordItem(recordData.getCustomerName()));
				/** 仕向先CD1 */
				regDataJson.put(MitsubishiConst.DESTINATION_CD1,
						WebDbUtils.createRecordItem(recordData.getDestinationCD1()));
				/** 仕向先名1 */
				regDataJson.put(MitsubishiConst.DESTINATION_NAME1,
						WebDbUtils.createRecordItem(recordData.getDestinationName1()));
				/** 仕向先CD2 */
				regDataJson.put(MitsubishiConst.DESTINATION_CD2,
						WebDbUtils.createRecordItem(recordData.getDestinationCD2()));
				/** 仕向先名2 */
				regDataJson.put(MitsubishiConst.DESTINATION_NAME2,
						WebDbUtils.createRecordItem(recordData.getDestinationName2()));
				/** 納品先CD */
				regDataJson.put(MitsubishiConst.DESTINATION_CD,
						WebDbUtils.createRecordItem(recordData.getDestinationCD()));
				/** 納品先名 */
				regDataJson.put(MitsubishiConst.DELIVERY_DESTINATION_NAME,
						WebDbUtils.createRecordItem(recordData.getDeliveryDestinationName()));
				/** 品名略号 */
				regDataJson.put(MitsubishiConst.PRODUCT_NAME_ABBREVIATION,
						WebDbUtils.createRecordItem(recordData.getProductNameAbbreviation()));
				/** カラーNO */
				regDataJson.put(MitsubishiConst.COLOR_NO, WebDbUtils.createRecordItem(recordData.getColorNo()));
				/** グレード1 */
				regDataJson.put(MitsubishiConst.GRADE_1, WebDbUtils.createRecordItem(recordData.getGrade1()));
				/** ユーザー品目 */
				regDataJson.put(MitsubishiConst.USER_ITEM, WebDbUtils.createRecordItem(recordData.getUserItem()));
				/** 通貨CD */
				regDataJson.put(MitsubishiConst.CURRENCY_CD, WebDbUtils.createRecordItem(recordData.getCurrencyCD()));
				/** 取引単位CD */
				regDataJson.put(MitsubishiConst.TRANSACTION_UNIT_CD,
						WebDbUtils.createRecordItem(recordData.getTransactionUnitCD()));
				/** 荷姿 */
				regDataJson.put(MitsubishiConst.PACKING, WebDbUtils.createRecordItem(recordData.getPacking()));
				/** 取引先枝番 */
				regDataJson.put(MitsubishiConst.CLIENT_BRANCH_NUMBER,
						WebDbUtils.createRecordItem(recordData.getClientBranchNumber()));
				/** 価格形態 */
				regDataJson.put(MitsubishiConst.PRICE_FORM, WebDbUtils.createRecordItem(recordData.getPriceForm()));
				/** 用途参照 */
				regDataJson.put(MitsubishiConst.USAGE_REF, WebDbUtils.createRecordItem(recordData.getUsageRef()));
				/** 納品予定日時 */
				regDataJson.put(MitsubishiConst.SCHEDULED_DELIVERY_DATE,
						WebDbUtils.createRecordItem(recordData.getDeliveryDate()));
				/** 品名分類CD1 */
				regDataJson.put(MitsubishiConst.PRODUCT_NAME_CLASS_CD1,
						WebDbUtils.createRecordItem(recordData.getProductNameClassCD1()));
				/** 受注日 */
				regDataJson.put(MitsubishiConst.ORDER_DATE, WebDbUtils.createRecordItem(recordData.getOrderDate()));
				/** 売上担当者CD */
				regDataJson.put(MitsubishiConst.SALESPERSON_CD,
						WebDbUtils.createRecordItem(recordData.getSalespersonCD()));
				/** 売上担当者名 */
				regDataJson.put(MitsubishiConst.SALESPERSON_NAME,
						WebDbUtils.createRecordItem(recordData.getSalespersonName()));

				webdbUtils.registJsonObject(regDataJson, true);
			}

			setReturnCode(Integer.valueOf(0));
		} catch (Exception e) {
			e.printStackTrace();
			setReturnCode(Integer.valueOf(1));
			throw e;
		}
	}

	/**
	 * set value for each other record in UmitsubishiTemp from content CSV
	 * 
	 * @param content
	 * @return
	 * @throws ParseException
	 */
	private UmitsubishiTempDto createUmitsubishiTemp(String[] content) throws ParseException {

		int j = 0;
		UmitsubishiTempDto umitsubishiTempDto = new UmitsubishiTempDto();

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
	 *
	 *
	 *
	 */
	public void calculateValue() {
		BigDecimal retailPrice = umb01Dto.getPriceRefDto().getRetailPrice();
		BigDecimal unitPriceSmallParcel = umb01Dto.getPriceRefDto().getUnitPriceSmallParcel();
		BigDecimal unitPriceForeheadColor = umb01Dto.getPriceRefDto().getUnitPriceForeheadColor();
		BigDecimal primaryStoreOpenRate = umb01Dto.getPriceRefDto().getPrimaryStoreOpenRate();
		BigDecimal primaryStoreCommissionAmount = umb01Dto.getPriceRefDto().getPrimaryStoreCommissionAmount();
		BigDecimal secondStoreOpenRate = umb01Dto.getSecondStoreOpenRate();
		BigDecimal secondStoreOpenAmount = umb01Dto.getSecondStoreOpenAmount();
		BigDecimal lotQuantity = umb01Dto.getPriceRefDto().getLotQuantity();
		BigDecimal calValue = new BigDecimal("0");
		BigDecimal valueLotSmall = new BigDecimal("100");
		BigDecimal valueLotLarge = new BigDecimal("100");

		// pattern 1
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
					.subtract(secondStoreOpenRate.multiply(retailPrice));
		}

		// pattern 2
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			calValue = retailPrice.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
		}

		// pattern 3
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceSmallParcel)
						.subtract(primaryStoreOpenRate.multiply(retailPrice.add(unitPriceSmallParcel)))
						.subtract(secondStoreOpenRate.multiply(retailPrice.add(unitPriceSmallParcel)));
			} else {
				calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

		// pattern 4
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceSmallParcel).subtract(primaryStoreCommissionAmount)
						.subtract(secondStoreOpenAmount);
			} else {
				calValue = retailPrice.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
			}
		}

		// pattern 5
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotLarge.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceForeheadColor)
						.subtract(primaryStoreOpenRate.multiply(retailPrice.add(unitPriceForeheadColor)))
						.subtract(secondStoreOpenRate.multiply(retailPrice.add(unitPriceForeheadColor)));
			} else {
				calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

		// pattern 6
		if (!BigDecimal.ZERO.equals(retailPrice) && BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotLarge.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceForeheadColor).subtract(primaryStoreCommissionAmount)
						.subtract(secondStoreOpenAmount);
			} else {
				calValue = retailPrice.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
			}
		}

		// pattern 7
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && !BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && !BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) >= 0 && valueLotLarge.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor)
						.subtract(primaryStoreOpenRate
								.multiply(retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor)))
						.subtract(secondStoreOpenRate
								.multiply(retailPrice.add(unitPriceSmallParcel).add(unitPriceForeheadColor)));
			} else {
				calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
		}

		// pattern 8
		if (!BigDecimal.ZERO.equals(retailPrice) && !BigDecimal.ZERO.equals(unitPriceSmallParcel)
				&& !BigDecimal.ZERO.equals(unitPriceForeheadColor) && BigDecimal.ZERO.equals(primaryStoreOpenRate)
				&& !BigDecimal.ZERO.equals(primaryStoreCommissionAmount) && BigDecimal.ZERO.equals(secondStoreOpenRate)
				&& !BigDecimal.ZERO.equals(secondStoreOpenAmount)) {
			if (valueLotSmall.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceForeheadColor).add(unitPriceSmallParcel)
						.subtract(primaryStoreCommissionAmount).subtract(secondStoreOpenAmount);
			} else if (valueLotSmall.compareTo(lotQuantity) >= 0 && valueLotLarge.compareTo(lotQuantity) < 0) {
				calValue = retailPrice.add(unitPriceForeheadColor).subtract(primaryStoreCommissionAmount)
						.subtract(secondStoreOpenAmount);
			} else {
				calValue = retailPrice.subtract(primaryStoreOpenRate.multiply(retailPrice))
						.subtract(secondStoreOpenRate.multiply(retailPrice));
			}
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

	/**
	 * @return the transactionList
	 */
	public List<String> getTransactionList() {
		return transactionList;
	}

	/**
	 * @param transactionList the transactionList to set
	 */
	public void setTransactionList(List<String> transactionList) {
		this.transactionList = transactionList;
	}

	/**
	 * @return the dataUpdateCategoryList
	 */
	public List<String> getDataUpdateCategoryList() {
		return dataUpdateCategoryList;
	}

	/**
	 * @param dataUpdateCategoryList the dataUpdateCategoryList to set
	 */
	public void setDataUpdateCategoryList(List<String> dataUpdateCategoryList) {
		this.dataUpdateCategoryList = dataUpdateCategoryList;
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
}
