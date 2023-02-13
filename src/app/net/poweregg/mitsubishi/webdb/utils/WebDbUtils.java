package net.poweregg.mitsubishi.webdb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.poweregg.common.entity.ClassInfo;
import net.poweregg.mitsubishi.constant.MitsubishiConst.CLASS_NO;
import net.poweregg.util.DateUtils;
import net.poweregg.util.StringUtils;

/**
 * WebDBアクセス共通関数 WebDBにアクセスするためのJson形式の文字列を編集する
 */
public class WebDbUtils {

	private WebDBConnectParam webDBConnectParam;
	private final String ENCODE = "UTF-8";

	public WebDbUtils() {
	}

	/**
	 * 
	 * @param webDBClassInfos
	 * @param systemType
	 * @param dbType: default-db_temp, 1-db_master, 2-db_ref
	 */
	public WebDbUtils(List<ClassInfo> webDBClassInfos, int systemType, int dbType) {

		webDBConnectParam = new WebDBConnectParam();
		webDBConnectParam.setPe4jUrl(WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_1.getValue()));
		webDBConnectParam.setApiKey(WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_2.getValue()));
		webDBConnectParam.setUri(WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_3.getValue()));
		webDBConnectParam.setModifyUri(WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_4.getValue()));
		webDBConnectParam.setSystem(systemType);
		webDBConnectParam.setEncode(ENCODE);
		switch (dbType) {
		case 2:
			webDBConnectParam.setDatabase(WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_6.getValue()));
			break;
		case 3:
			webDBConnectParam.setDatabase(WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_7.getValue()));
			break;
		default:
			webDBConnectParam.setDatabase(WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_5.getValue()));
		}
	}

	public WebDbUtils(WebDBConnectParam webDBConnectParam) {
		this.webDBConnectParam = webDBConnectParam;
	}

	/**
	 * JsonElemen項目を作成する。
	 *
	 * @param value
	 * @return
	 */
	public static JSONObject createRecordItem(String value) throws Exception {
		JSONObject result = new JSONObject();
		result.put("value", StringUtils.toEmpty(value));
		return result;
	}

	/**
	 * JsonElemen項目(number)を作成する。
	 *
	 * @param value
	 * @return
	 */
	public static JSONObject createRecordItemNumber(Long value) throws Exception {
		JSONObject result = new JSONObject();
		result.put("value", value);
		return result;
	}

	/**
	 * JsonElemen項目(boolean)を作成する。
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static JSONObject createRecordItemBool(Boolean value) throws Exception {
		JSONObject result = new JSONObject();
		result.put("value", value);
		return result;
	}

	/**
	 * JsonElemen項目(code)を作成する。
	 *
	 * @param value
	 * @return
	 */
	public static JSONObject createRecordItemCode(String value) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject valueItem = new JSONObject();
		valueItem.put("code", StringUtils.toEmpty(value));
		result.put("value", valueItem);

		return result;
	}

	/**
	 * JsonElemen項目(url)を作成する。
	 *
	 * @param value
	 * @return
	 */
	public static JSONObject createRecordURL(String value) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject valueItem = new JSONObject();
		valueItem.put("url", StringUtils.toEmpty(value));
		valueItem.put("name", StringUtils.toEmpty(value));
		result.put("value", valueItem);

		return result;
	}

	/**
	 * JsonElemen項目(code)を作成する。
	 *
	 * @param value
	 * @return
	 */
	public static JSONObject createRecordItemDept(String corpCode, String deptCode) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject valueItem = new JSONObject();
		valueItem.put("corpCode", StringUtils.toEmpty(corpCode));
		valueItem.put("deptCode", StringUtils.toEmpty(deptCode));
		result.put("value", valueItem);

		return result;
	}

	public static JSONObject createRecordItemCIFName(String enterpriseId) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject valueItem = new JSONObject();
		// valueItem.put("personID", StringUtils.toEmpty(personId));
		valueItem.put("enterpriseID", StringUtils.toEmpty(enterpriseId));
		// valueItem.put("postID", StringUtils.toEmpty(postId));
		result.put("value", valueItem);

		return result;
	}

	public static JSONObject createRecordItemCIFName2(String personId, String enterpriseId, String postId)
			throws Exception {
		JSONObject result = new JSONObject();
		JSONObject valueItem = new JSONObject();
		valueItem.put("personID", StringUtils.toEmpty(personId));
		valueItem.put("enterpriseID", StringUtils.toEmpty(enterpriseId));
		valueItem.put("postID", StringUtils.toEmpty(postId));
		result.put("value", valueItem);

		return result;
	}

	public static JSONObject createRecordItemCIFName3(String enterpriseId, String postId) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject valueItem = new JSONObject();
		valueItem.put("enterpriseID", StringUtils.toEmpty(enterpriseId));
		valueItem.put("postID", StringUtils.toEmpty(postId));
		result.put("value", valueItem);

		return result;
	}

	public static JSONObject createRecordItemSuccessfulBidder(String enterpriseId, String... params) throws Exception {
		JSONObject result = new JSONObject();
		JSONObject valueItem = new JSONObject();
		valueItem.put("enterpriseID", StringUtils.toEmpty(enterpriseId));
		if (params != null && params.length > 0) {
			/** postID */
			if (params.length >= 1) {
				valueItem.put("postID", StringUtils.toEmpty(params[0]));
			}
		}
		result.put("value", valueItem);
		return result;
	}

	/**
	 * 検索条件のJsonを作成する。
	 *
	 * @param field
	 * @param opr
	 * @param value
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject createConditionQuery(String field, String opr, String value) throws JSONException {
		JSONObject conditionQuery = new JSONObject();
		conditionQuery.put("field", field);
		conditionQuery.put("opr", opr);
		if (!StringUtils.isEmpty(value)) {
			conditionQuery.put("value", value);
		}

		return conditionQuery;
	}

	/**
	 * 検索条件（block）のJsonを作成する。
	 *
	 * @param blockConj
	 * @param itemConj
	 * @param conditionList
	 * @return
	 */
	public static JSONObject createConditionBlock(QueryConj blockConj, QueryConj itemConj, JSONArray conditionList)
			throws JSONException {
		JSONObject conditionBlock = new JSONObject();
		conditionBlock.put("blockConj", blockConj);
		conditionBlock.put("itemConj", itemConj);
		conditionBlock.put("items", conditionList);

		return conditionBlock;
	}

	/**
	 * Convert value of employee to condition string.
	 *
	 * @param value
	 * @return
	 */
	public static String convertValueOfEmployee(JSONObject employee) throws JSONException {
		JSONObject value = new JSONObject();
		value = employee.getJSONObject("value");
		if (!StringUtils.isEmpty(value)) {
			String corpCode = value.getString("corpCode");
			String empCode = value.getString("empCode");
			return corpCode + ":" + empCode;
		}

		return "";
	}

	/**
	 * 検索条件のJsonを作成する。 {"field": 引数, "opr": 引数, "value": 引数"}
	 *
	 * @param field フィールド名
	 * @param opr   比較演算子
	 * @param value 値
	 * @return JSON文字列
	 * @throws JSONException
	 */
	public static JSONObject createConditionQuery(String field, Operand opr, String value) throws JSONException {

		JSONObject conditionQuery = new JSONObject();
		conditionQuery.put("field", field);
		conditionQuery.put("opr", opr);
		if (!StringUtils.isEmpty(value)) {
			conditionQuery.put(WebDbConstant.JSON_RECORD_VALUE, value);
		}

		return conditionQuery;
	}

	/**
	 * 検索条件のJsonを作成する。 {"field": 引数, "opr": 引数, "value": 引数"}
	 *
	 * @param field フィールド名
	 * @param opr   比較演算子
	 * @param value 値
	 * @return JSON文字列
	 * @throws JSONException
	 */
	public static JSONObject createConditionQueryForConfirmFlag(String field, Operand opr, String value)
			throws JSONException {
		JSONObject conditionQuery = new JSONObject();
		conditionQuery.put("field", field);
		conditionQuery.put("opr", opr);
		conditionQuery.put("result", true);
		if (!StringUtils.isEmpty(value)) {
			conditionQuery.put(WebDbConstant.JSON_RECORD_VALUE, value);
		}
		return conditionQuery;
	}

	/**
	 * 検索条件のJsonを作成する。 {"field": 引数, "opr": 引数, "value": 引数"}
	 *
	 * @param field フィールド名
	 * @param opr   比較演算子
	 * @param value 値
	 * @return JSON文字列
	 * @throws JSONException
	 */
	public static JSONObject createConditionQueryForItem(String field, Operand opr, String value) throws JSONException {
		JSONObject conditionQuery = new JSONObject();
		conditionQuery.put("field", field);
		conditionQuery.put("opr", opr);
		conditionQuery.put("code", true);
		if (!StringUtils.isEmpty(value)) {
			conditionQuery.put(WebDbConstant.JSON_RECORD_VALUE, value);
		}
		return conditionQuery;
	}

	/**
	 * 並び順のJsonを作成する。
	 *
	 * @param field
	 * @param opr
	 * @param value
	 * @return
	 */
	public static JSONArray createOrderQuery(String field, Boolean desc) throws JSONException {
		JSONArray orderArr = new JSONArray();
		JSONObject orderQuery = new JSONObject();
		orderQuery.put("field", field);
		orderQuery.put("desc", desc);
		orderArr.put(orderQuery);
		return orderArr;
	}

	/**
	 * レコード情報からJSONArrayの配列を取得する
	 *
	 * @param record
	 * @param fieldName
	 * @return
	 * @throws JSONException
	 */
	public static JSONArray getObjectValue(JSONObject record, String fieldName) throws JSONException {
		JSONObject json = record.getJSONObject(fieldName);
		if (json.isNull(WebDbConstant.JSON_RECORD_VALUE))
			return null;
		return json.getJSONArray(WebDbConstant.JSON_RECORD_VALUE);
	}

	/**
	 * レコード情報から指定したフィールド名の値を取得する
	 *
	 * @param record    レコード情報
	 * @param fieldName フィールド名
	 * @return レコード値
	 * @throws JSONException
	 */
	public static String getValue(JSONObject record, String fieldName) throws JSONException {
		JSONObject json = null;
		try {
			json = record.getJSONObject(fieldName);
		} catch (JSONException e) {
			return "";
		}
		return getString(json, WebDbConstant.JSON_RECORD_VALUE);
	}

	/**
	 * レコード情報から指定したフィールド名の値を取得する
	 *
	 * @param record    レコード情報
	 * @param fieldName フィールド名
	 * @return レコード値
	 * @throws JSONException
	 */
	public static JSONObject getValueJSONObject(JSONObject record, String fieldName) throws JSONException {
		JSONObject json = null;
		try {
			json = record.getJSONObject(fieldName);
		} catch (JSONException e) {
			return null;
		}
		return json;
	}

	/**
	 * レコード情報から指定したフィールド名の値を取得する（WebDB参照項目）
	 *
	 * @param record    レコード情報
	 * @param fieldName フィールド名
	 * @return レコード値
	 * @throws JSONException
	 */
	public static String getValue(JSONObject record, String fieldName, String subFieldName) throws JSONException {
		try {
			JSONObject json = record.getJSONObject(fieldName);
			JSONObject subJson = json.getJSONObject(WebDbConstant.JSON_RECORD_VALUE);
			return getString(subJson, subFieldName);
		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * レコード情報から指定したフィールド名の値を取得する
	 *
	 * @param record    レコード情報
	 * @param fieldName フィールド名
	 * @return レコード値
	 * @throws JSONException
	 */
	public static BigDecimal getBigDecimalValue(JSONObject record, String fieldName) throws JSONException {
		JSONObject json = record.getJSONObject(fieldName);
		if (json.isNull(WebDbConstant.JSON_RECORD_VALUE)) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(json.getString(WebDbConstant.JSON_RECORD_VALUE));
	}

	/**
	 * レコード情報から指定したフィールド名の値を取得する
	 *
	 * @param record    レコード情報
	 * @param fieldName フィールド名
	 * @return レコード値
	 * @throws JSONException
	 */
	public static Date getDateValue(JSONObject record, String fieldName) throws JSONException {
		JSONObject json = null;
		try {
			json = record.getJSONObject(fieldName);
		} catch (JSONException e) {
			return null;
		}
		return DateUtils.getDate(json.getString(WebDbConstant.JSON_RECORD_VALUE));
	}

	/**
	 * JSONObjectから文字列を取得する
	 *
	 * @param json JSONObject
	 * @param key  取得対象のキー
	 * @return 取得した文字列
	 * @throws JSONException
	 */
	public static String getString(JSONObject json, String key) throws JSONException {
		if (json.isNull(key))
			return null;

		return json.getString(key);
	}

	/**
	 * レコード情報から指定したフィールド名の値を取得する
	 *
	 * @param record    レコード情報
	 * @param fieldName フィールド名
	 * @return レコード値
	 * @throws JSONException
	 */
	public static JSONArray getJSONArray(JSONObject record, String fieldName) throws JSONException {
		JSONObject json = record.getJSONObject(fieldName);
		if (json.isNull(WebDbConstant.JSON_RECORD_VALUE))
			return null;

		return json.getJSONArray(WebDbConstant.JSON_RECORD_VALUE);
	}

	/**
	 * WebDBの検索APIを実行する。
	 * 
	 * @param conn
	 * @param query
	 * @param order
	 * @param databaseName
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	private JSONObject getJsonObject(String query, String order, Long offset, Long limit) throws Exception {

		HttpURLConnection conn = null;
		JSONObject jsonObj = new JSONObject();
		try {
			String url_string = webDBConnectParam.getPe4jUrl() + webDBConnectParam.getUri();
			url_string += "?";
			url_string += "database="
					+ URLEncoder.encode(webDBConnectParam.getDatabase(), webDBConnectParam.getEncode());
			if (!StringUtils.nullOrBlank(query)) {
				url_string += "&";
				url_string += "query=" + URLEncoder.encode(query, webDBConnectParam.getEncode());
			}
			if (!StringUtils.nullOrBlank(order)) {
				url_string += "&";
				url_string += "order=" + URLEncoder.encode(order, webDBConnectParam.getEncode());
			}
			if (offset != null) {
				url_string += "&offset=" + offset;
			}
			if (limit != null) {
				url_string += "&limit=" + limit;
			}
			URL url = new URL(url_string);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", WebDbConstant.APPLICATION_JSON_CONTENT_TYPE);
			conn.setRequestProperty("X-API-Key", webDBConnectParam.getApiKey());
			conn.setRequestProperty("system", webDBConnectParam.getSystem().toString());
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.connect();
			int status = conn.getResponseCode();

			if (status == HttpURLConnection.HTTP_OK) {
				String sb = IOUtils.toString(conn.getInputStream(), webDBConnectParam.getEncode());
				jsonObj = new JSONObject(sb);
			} else {
				Reader in = new BufferedReader(
						new InputStreamReader(conn.getErrorStream(), webDBConnectParam.getEncode()));
				StringBuilder sb = new StringBuilder();
				for (int c; (c = in.read()) >= 0;) {
					sb.append((char) c);
				}
				String response = sb.toString();
				throw new Exception(url_string + "|" + response);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return jsonObj;
	}
	

	/**
	 * delete record webDB
	 * 
	 * @param recordNo
	 * @return
	 * @throws Exception
	 */
	public JSONObject delJsonObject(List<ClassInfo> webDBClassInfos, String recordNo) throws Exception {

		HttpURLConnection conn = null;
		JSONObject jsonObj = new JSONObject();
		try {
			String url_string = webDBConnectParam.getPe4jUrl() + WebDbUtils.getColNameWebDb(webDBClassInfos, CLASS_NO.CLASSNO_11.getValue());
			url_string += "?";
			url_string += "database="
					+ URLEncoder.encode(webDBConnectParam.getDatabase(), webDBConnectParam.getEncode());
			if (!StringUtils.nullOrBlank(recordNo)) {
				url_string += "&";
				url_string += "No=" + URLEncoder.encode(recordNo, webDBConnectParam.getEncode());
			}
			URL url = new URL(url_string);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("Content-Type", WebDbConstant.APPLICATION_JSON_CONTENT_TYPE);
			conn.setRequestProperty("X-API-Key", webDBConnectParam.getApiKey());
			conn.setRequestProperty("system", webDBConnectParam.getSystem().toString());
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.connect();
			int status = conn.getResponseCode();

			if (status == HttpURLConnection.HTTP_OK) {
				String sb = IOUtils.toString(conn.getInputStream(), webDBConnectParam.getEncode());
				jsonObj = new JSONObject(sb);
			} else {
				Reader in = new BufferedReader(
						new InputStreamReader(conn.getErrorStream(), webDBConnectParam.getEncode()));
				StringBuilder sb = new StringBuilder();
				for (int c; (c = in.read()) >= 0;) {
					sb.append((char) c);
				}
				String response = sb.toString();
				throw new Exception(url_string + "|" + response);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return jsonObj;
	}

	public JSONObject getDataFormAPI(String query, String order, Long offset, Long limit) throws Exception {
		JSONObject jsonObj = getJsonObject(query, order, offset, limit);
		return jsonObj;
	}

	/**
	 * WebDBの検索APIを実行する。
	 *
	 * @param HttpURLConnection
	 * @param query
	 * @param order
	 * @param offset            スキップ件数
	 * @param limit             最大取得件数
	 * @param databaseName
	 * @return JSONObject 検索結果
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public JSONArray getJsonObjectWebDB(JSONObject jsonObj, Long offset, Long limit) throws Exception {
		JSONArray arrayRecords = new JSONArray();
		Long count = 0L; // 検索総件数

		boolean loop = true;
		while (loop) {
			Long totalCount = new Long(WebDbUtils.getString(jsonObj, WebDbConstant.JSON_COUNT));
			JSONArray array = jsonObj.getJSONArray(WebDbConstant.JSON_RECORDS);

			// 上記以外の場合。全件を取得するよう繰り返し実行する。
			for (int i = 0; i < array.length(); i++) {
				org.json.JSONObject obj = (org.json.JSONObject) array.get(i);
				arrayRecords.put(obj);
			}
			count = count + array.length();
			offset = offset + array.length();

			// 最大取得件数が100件以下の場合、処理を終了する
			if (limit != null && limit <= 100) {
				loop = false;
				continue;
			}

			// 合計件数≦検索総件数の場合、繰り返し処理を終了する
			if (totalCount.compareTo(count) <= 0) {
				loop = false;
				continue;
			}
		}
		return arrayRecords;
	}

	/**
	 * WebDBの更新APIを実行する。
	 * 
	 * @param conn
	 * @param databaseName
	 * @param jsonString
	 * @param jsonNo
	 * @param notification
	 * @param notifyfollow
	 * @param external
	 * @return
	 * @throws IOException
	 */
	public String putJsonObject(JSONObject jsonString, String jsonNo, boolean notification, boolean notifyfollow,
			boolean external) throws IOException {
		try {

			// JSON型でボディを作成する。
			JSONObject body = new JSONObject();
			body.put("system", webDBConnectParam.getSystem());
			body.put("database", webDBConnectParam.getDatabase());

			body.put("No", jsonNo);
			body.put("notification", notification);
			body.put("notifyfollow", notifyfollow);
			body.put("external", external);
			body.put("record", jsonString);
			System.out.println("UPDATE: " + body.toString());

			HttpPut httpPut = new HttpPut(webDBConnectParam.getPe4jUrl() + webDBConnectParam.getModifyUri());
			httpPut.addHeader(HttpHeaders.ACCEPT, "application/json");
			httpPut.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
			httpPut.addHeader("X-API-Key", webDBConnectParam.getApiKey());

			StringEntity stringEntity = new StringEntity(body.toString(), ENCODE);
			httpPut.setEntity(stringEntity);
			System.out.println("Executing request " + httpPut.getRequestLine());

			try (CloseableHttpClient httpClient = HttpClients.createDefault();
					CloseableHttpResponse response = httpClient.execute(httpPut)) {
				int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode != HttpStatus.SC_CREATED) {
					throw new RuntimeException("異常終了（HTTP エラーコード） : " + responseCode);
				}
				JSONObject result = new JSONObject(EntityUtils.toString(response.getEntity()));
				StringBuffer msg = new StringBuffer();
				if (result.has(WebDbConstant.JSON_MESSAGE)) {
					msg.append(StringUtils.addEmpty(result.get(WebDbConstant.JSON_MESSAGE)));
				}
				if (result.has(WebDbConstant.JSON_DETAILS)) {
					msg.append(StringUtils.addEmpty(result.get(WebDbConstant.JSON_DETAILS)));
				}
				if (!StringUtils.nullOrBlank(msg.toString())) {
					return msg.toString();
				}
				System.out.println(result.toString());
			}
			return "";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * regist json object
	 * 
	 * @param conn
	 * @param databaseName
	 * @param jsonString
	 * @return
	 */
	public String registJsonObject(JSONObject jsonString, boolean external) {
		try {

			// JSON型でボディを作成する。
			JSONObject body = new JSONObject();
			body.put("system", webDBConnectParam.getSystem());
			body.put("database", webDBConnectParam.getDatabase());
			// minhhtp 20210917 add Start
			body.put("notification", true);
			// minhhtp 20210917 add End
			body.put("record", jsonString);
			body.put("external", external);
			System.out.println("REGISTER: " + body.toString());

			HttpPost httpPost = new HttpPost(webDBConnectParam.getPe4jUrl() + webDBConnectParam.getModifyUri());
			httpPost.addHeader(HttpHeaders.ACCEPT, "application/json");
			httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
			httpPost.addHeader("X-API-Key", webDBConnectParam.getApiKey());

			StringEntity stringEntity = new StringEntity(body.toString(), ENCODE);
			httpPost.setEntity(stringEntity);
			System.out.println("Executing request " + httpPost.getRequestLine());

			try (CloseableHttpClient httpClient = HttpClients.createDefault();
					CloseableHttpResponse response = httpClient.execute(httpPost)) {
				int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode != HttpStatus.SC_CREATED) {
					throw new RuntimeException("異常終了（HTTP エラーコード） : " + responseCode);
				}
				JSONObject result = new JSONObject(EntityUtils.toString(response.getEntity()));
				StringBuffer msg = new StringBuffer();
				if (result.has(WebDbConstant.JSON_MESSAGE)) {
					msg.append(StringUtils.addEmpty(result.get(WebDbConstant.JSON_MESSAGE)));
				}
				if (result.has(WebDbConstant.JSON_DETAILS)) {
					msg.append(StringUtils.addEmpty(result.get(WebDbConstant.JSON_DETAILS)));
				}
				if (!StringUtils.nullOrBlank(msg.toString())) {
					return msg.toString();
				}
				System.out.println(msg.toString());
			}
			return "";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * regist json object
	 * 
	 * @param conn
	 * @param databaseName
	 * @param jsonString
	 * @return String
	 * @author minhnv
	 */
	public String registJsonObject(JSONObject jsonString, boolean notification, Boolean notifyfollow,
			boolean external) {
		try {

			// JSON型でボディを作成する。
			JSONObject body = new JSONObject();
			body.put("system", webDBConnectParam.getSystem());
			body.put("database", webDBConnectParam.getDatabase());
			body.put("notification", notification);
			body.put("notifyfollow", notifyfollow);
			body.put("record", jsonString);
			body.put("external", external);
			System.out.println("REGISTER: " + body.toString());

			HttpPost httpPost = new HttpPost(webDBConnectParam.getPe4jUrl() + webDBConnectParam.getModifyUri());
			httpPost.addHeader(HttpHeaders.ACCEPT, "application/json");
			httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
			httpPost.addHeader("X-API-Key", webDBConnectParam.getApiKey());

			try (CloseableHttpClient httpClient = HttpClients.createDefault();
					CloseableHttpResponse response = httpClient.execute(httpPost)) {
				int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode != HttpStatus.SC_OK) {
					throw new RuntimeException("異常終了（HTTP エラーコード） : " + responseCode);
				}
				JSONObject result = new JSONObject(EntityUtils.toString(response.getEntity()));
				StringBuffer msg = new StringBuffer();
				if (result.has(WebDbConstant.JSON_MESSAGE)) {
					msg.append(StringUtils.addEmpty(result.get(WebDbConstant.JSON_MESSAGE)));
				}
				if (result.has(WebDbConstant.JSON_DETAILS)) {
					msg.append(StringUtils.addEmpty(result.get(WebDbConstant.JSON_DETAILS)));
				}
				if (!StringUtils.nullOrBlank(msg.toString())) {
					return msg.toString();
				}
				System.out.println(msg.toString());
			}
			return "";

		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public JSONObject registJsonObjectReturnResult(JSONObject jsonString, boolean external) throws Exception {
		try {

			// JSON型でボディを作成する。
			JSONObject body = new JSONObject();
			body.put("system", webDBConnectParam.getSystem());
			body.put("database", webDBConnectParam.getDatabase());
			body.put("notification", true);
			body.put("record", jsonString);
			body.put("external", external);
			System.out.println("REGISTER: " + body.toString());

			HttpPost httpPost = new HttpPost(webDBConnectParam.getPe4jUrl() + webDBConnectParam.getModifyUri());
			httpPost.addHeader(HttpHeaders.ACCEPT, "application/json");
			httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
			httpPost.addHeader("X-API-Key", webDBConnectParam.getApiKey());

			try (CloseableHttpClient httpClient = HttpClients.createDefault();
					CloseableHttpResponse response = httpClient.execute(httpPost)) {
				int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode != HttpStatus.SC_CREATED) {
					throw new RuntimeException("異常終了（HTTP エラーコード） : " + responseCode);
				}
				JSONObject result = new JSONObject(EntityUtils.toString(response.getEntity()));
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject res = new JSONObject();
			res.put(WebDbConstant.JSON_MESSAGE, e.getMessage());
			return res;
		}
	}

	/**
	 * count total record WebDBの検索APIを実行する。
	 *
	 * @param jsonObj
	 * @return long total count record
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public Long countTotalDataWebDB(JSONObject jsonObj) throws Exception {
		return new Long(WebDbUtils.getString(jsonObj, WebDbConstant.JSON_COUNT));
	}

	public static String getColNameWebDb(List<ClassInfo> classInfos, String classNo) {
		return classInfos.stream().filter(classInfo -> classInfo.getClassno().equals(classNo))
				.map(ClassInfo::getChardata1).findAny().orElse("");
	}

	public static String getClassNameByChardata1(List<ClassInfo> classInfos, String charData1) {
		return classInfos.stream().filter(classInfo -> classInfo.getChardata1().equals(charData1))
				.map(ClassInfo::getClassName).findAny().orElse("");
	}

	public static String getClassNameByClassNo(List<ClassInfo> classInfos, String classNo) {
		return classInfos.stream().filter(classInfo -> classInfo.getClassno().equals(classNo))
				.map(ClassInfo::getClassName).findAny().orElse("");
	}

	public static ClassInfo getClassInfoByChardata1(List<ClassInfo> classInfos, String charData1) {
		return classInfos.stream().filter(classInfo -> classInfo.getChardata1().equals(charData1)).findFirst().get();
	}

	public static String getClassNoByClassName(List<ClassInfo> classInfos, String className) {
		return classInfos.stream().filter(classInfo -> classInfo.getClassName().equals(className))
				.map(ClassInfo::getClassno).findAny().orElse("");
	}

	public static String getChardata1ByClassName(List<ClassInfo> classInfos, String className) {
		return classInfos.stream().filter(classInfo -> classInfo.getClassName().equals(className))
				.map(ClassInfo::getChardata1).findAny().orElse("");
	}
	
	public static String getChardata1ByClassNo(List<ClassInfo> classInfos, String classNo) {
		return classInfos.stream().filter(classInfo -> classInfo.getClassno().equals(classNo))
				.map(ClassInfo::getChardata1).findAny().orElse("");
	}

	public WebDBConnectParam getWebDBConnectParam() {
		return webDBConnectParam;
	}

	public void setWebDBConnectParam(WebDBConnectParam webDBConnectParam) {
		this.webDBConnectParam = webDBConnectParam;
	}
}
