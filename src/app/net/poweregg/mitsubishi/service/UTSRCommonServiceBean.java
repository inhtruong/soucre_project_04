package net.poweregg.mitsubishi.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.JSONArray;
import org.json.JSONObject;

import net.poweregg.common.ClassificationService;
import net.poweregg.common.entity.ClassInfo;
import net.poweregg.mitsubishi.constant.MitsubishiConst.CLASS_NO;
import net.poweregg.mitsubishi.constant.MitsubishiConst.COMMON_NO;
import net.poweregg.mitsubishi.dto.UTSR01SearchDto;
import net.poweregg.mitsubishi.dto.UTSR01SearchDto.SORTITEM;
import net.poweregg.mitsubishi.dto.UTSR01SearchDto.SORTORDER;
import net.poweregg.mitsubishi.webdb.utils.Operand;
import net.poweregg.mitsubishi.webdb.utils.QueryConj;
import net.poweregg.mitsubishi.webdb.utils.WebDbConstant;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;
import net.poweregg.security.CertificationService;
import net.poweregg.util.StringUtils;

@Stateless
public class UTSRCommonServiceBean implements UTSRCommonService {

	final String NUM_ALPHA_PATTERN = "[^a-zA-Z0-9]";

	@EJB
	ClassificationService classificationService;

	@EJB
	CertificationService certificationService;

	@PersistenceContext(unitName = "pe4jPU")
	private EntityManager em;

	@Override
	public List<UTSR01SearchDto> searchData(UTSR01SearchDto utsrSearchCond, long start, long max) throws Exception {

		// get classInfo by commonNo: UTSR01
		List<ClassInfo> webDBClassInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
				COMMON_NO.COMMON_NO_UTSR01.getValue());
		WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0);
		// get classInfo by commonNo: UTSR02
		List<ClassInfo> classInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
				COMMON_NO.COMMON_NO_UTSR02.getValue());
		List<UTSR01SearchDto> tsrResults = new ArrayList<>();

		// call API
		JSONObject jsonObj = webdbUtils.getDataFormAPI(getConditionQuery(utsrSearchCond, classInfos),
				getOjectSortField(utsrSearchCond, classInfos), start, max);
		// get list json records
		JSONArray resultJson = webdbUtils.getJsonObjectWebDB(jsonObj, start, max);
	
		// Khong tim duoc thi record no return luon
		if (resultJson == null || resultJson.length() == 0) {
			return new ArrayList<>();
		}

		String urlPe4j = WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_1.getValue());
		String urlPattern = WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_5.getValue());
		String databaseId = WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_6.getValue());
		String screenId = WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_7.getValue());

		// parse json to dto
		for (int i = 0; i < resultJson.length(); i++) {
			UTSR01SearchDto tsrTemp = new UTSR01SearchDto();
			JSONObject recordObj = resultJson.getJSONObject(i);
			for (int j = 0; j < recordObj.length(); j++) {
				// id
				tsrTemp.setId(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_1.getValue())));
				// TSR企業コード
				tsrTemp.setTsrCompanyCode(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_2.getValue())));
				// 商号
				tsrTemp.setCompanyNameKanji(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_3.getValue())));
				// 電話番号（編集済）
				tsrTemp.setTelNumberText(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_5.getValue())));
				// 評点最新
				tsrTemp.setGrade(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_9.getValue())));
				// 創業年月
				tsrTemp.setFounded(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_10.getValue())));
				// 設立年月
				tsrTemp.setDateEstablishment(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_11.getValue())));
				// 営業種目
				tsrTemp.setClassification(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_12.getValue())));
				// 売上高
				tsrTemp.setAmountSales(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_13.getValue())));
				// 資本金
				tsrTemp.setCapital(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_14.getValue())));
				// 従業員数
				tsrTemp.setNumberEmp(WebDbUtils.getValue(recordObj,
						WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_15.getValue())));
				// 詳細表示
				StringBuilder urlString = new StringBuilder();
				urlString.append(urlPe4j)
						.append(MessageFormat.format(urlPattern, databaseId,
								WebDbUtils.getValue(recordObj,
										WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_1.getValue())),
								screenId));
				tsrTemp.setDetailView(urlString.toString());

			}
			tsrResults.add(tsrTemp);
		}

		return tsrResults;
	}

	/**
	 * get query blocks contain condition search
	 * 
	 * @param utsrSearchCond
	 * @param classInfos
	 * @return
	 * @throws Exception
	 */
	private String getConditionQuery(UTSR01SearchDto utsrSearchCond, List<ClassInfo> classInfos) throws Exception {
		JSONArray condOr = new JSONArray();
		JSONArray queryBlocks = new JSONArray();

		if (!StringUtils.nullOrBlank(utsrSearchCond.getCompanyNameKanji())) {
			condOr.put(WebDbUtils.createConditionQuery(
					WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_3.getValue()), Operand.INCLUDE,
					utsrSearchCond.getCompanyNameKanji()));
		}
		if (!StringUtils.nullOrBlank(utsrSearchCond.getCompanyNameKana())) {
			condOr.put(WebDbUtils.createConditionQuery(
					WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_4.getValue()), Operand.INCLUDE,
					utsrSearchCond.getCompanyNameKana()));
		}
		if (!StringUtils.nullOrBlank(utsrSearchCond.getLocationText())) {
			condOr.put(WebDbUtils.createConditionQuery(
					WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_8.getValue()), Operand.INCLUDE,
					utsrSearchCond.getLocationText()));
		}
		if (!StringUtils.nullOrBlank(utsrSearchCond.getTelNumberText())) {
			condOr.put(WebDbUtils.createConditionQuery(
					WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_5.getValue()), Operand.INCLUDE,
					utsrSearchCond.getTelNumberText().replaceAll(NUM_ALPHA_PATTERN, StringUtils.EMPTY)));
		}
		if (!StringUtils.nullOrBlank(utsrSearchCond.getRepresentativeKanji())) {
			condOr.put(WebDbUtils.createConditionQuery(
					WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_6.getValue()), Operand.INCLUDE,
					utsrSearchCond.getRepresentativeKanji()));
		}
		if (!StringUtils.nullOrBlank(utsrSearchCond.getRepresentativeKana())) {
			condOr.put(WebDbUtils.createConditionQuery(
					WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_7.getValue()), Operand.INCLUDE,
					utsrSearchCond.getRepresentativeKana()));
		}
		if (!StringUtils.nullOrBlank(utsrSearchCond.getTsrCompanyCode())) {
			condOr.put(WebDbUtils.createConditionQuery(
					WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_2.getValue()), Operand.EQUALS,
					utsrSearchCond.getTsrCompanyCode()));
		}

		if (condOr == null || condOr.length() == 0) {
			return null;
		}

		queryBlocks.put(WebDbUtils.createConditionBlock(QueryConj.AND, QueryConj.AND, condOr));

		return queryBlocks.toString();
	}

	/**
	 * get object sort field
	 * 
	 * @param utsrSearch
	 * @param classInfos
	 * @return OjectSortField.toString()
	 * @throws Exception
	 */
	private String getOjectSortField(UTSR01SearchDto utsrSearch, List<ClassInfo> classInfos) throws Exception {
		Boolean isDesc = Boolean.TRUE;
		if (SORTORDER.ASC.equals(utsrSearch.getSortOrder())) {
			isDesc = Boolean.FALSE;
		}
		JSONArray OjectSortField = new JSONArray();
		if (SORTITEM.COMPANYCODE.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_2.getValue()), isDesc);
		}
		if (SORTITEM.COMPANYNAMEKANJI.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_3.getValue()), isDesc);
		}
		if (SORTITEM.PHONENUMBER.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_5.getValue()), isDesc);
		}
		if (SORTITEM.LATESTGRADE.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_9.getValue()), isDesc);
		}
		if (SORTITEM.FOUNDED.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_10.getValue()), isDesc);
		}
		if (SORTITEM.DATEESTABLISH.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_11.getValue()), isDesc);
		}
		if (SORTITEM.CLASSIFICATION.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_12.getValue()), isDesc);
		}
		if (SORTITEM.SALESAMOUNT.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_13.getValue()), isDesc);
		}
		if (SORTITEM.CAPITAL.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_14.getValue()), isDesc);
		}
		if (SORTITEM.EMPLOYEESCOUNT.equals(utsrSearch.getSortItem())) {
			OjectSortField = WebDbUtils
					.createOrderQuery(WebDbUtils.getColNameWebDb(classInfos, CLASS_NO.CLASSNO_15.getValue()), isDesc);
		}
		return OjectSortField.toString();
	}

	@Override
	public Long countTotalData(UTSR01SearchDto utsrSearchCond, long start, long max) throws Exception {
		// get classInfo by commonNo: UTSR01
		List<ClassInfo> webDBClassInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
				COMMON_NO.COMMON_NO_UTSR01.getValue());
		WebDbUtils webdbUtils = new WebDbUtils(webDBClassInfos, 0);
		// get classInfo by commonNo: UTSR02
		List<ClassInfo> classInfos = classificationService.getClassInfoList(WebDbConstant.ALL_CORP,
				COMMON_NO.COMMON_NO_UTSR02.getValue());
		// call API
		JSONObject jsonObj = webdbUtils.getDataFormAPI(getConditionQuery(utsrSearchCond, classInfos),
				getOjectSortField(utsrSearchCond, classInfos), start, max);
		return webdbUtils.countTotalDataWebDB(jsonObj);
	}

}
