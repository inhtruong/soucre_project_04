/**
 * 
 */
package net.poweregg.mitsubishi.csv.utils;

import java.util.ResourceBundle;

import org.json.JSONException;
import org.json.JSONObject;

import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.mitsubishi.webdb.utils.WebDbUtils;
import net.poweregg.util.StringUtils;

/**
 * @author diennv
 *
 */
public class UMBCsvDto {

	/**
	 * get header csv from resource
	 * 
	 * @return
	 */
	public static String getHeader() {
		ResourceBundle bundle = ResourceBundle.getBundle("umb-resource");
		return bundle.getString("umb_l_csvHeader");
	}

	public static void addRowData(StringBuilder builder, JSONObject data) throws JSONException {
		 String lotNum = WebDbUtils.getValue(data, MitsubishiConst.LOT_QUANTITY);
		 String[] lotNumArray = new String[]{};
		 
		 if (lotNum != null && !"".equals(lotNum)) {
			 lotNumArray = lotNum.split(",");
		 }
		 if (lotNumArray.length > 0) {
			// 小口配送単価
			 double priceSmallParcel = 0;
			 if (WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL) != null && !"".equals(WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL))) {
				 priceSmallParcel = Double.parseDouble(WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
			 }
			 // 小口着色単価
			 double priceForheadColor = 0;
			 if (WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR) != null && !"".equals(WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR))) {
				 priceForheadColor = Double.parseDouble(WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
			 }

			 for (int i = 0; i < lotNumArray.length; i++) {
				double priceTemp = 0;
				// データ行NO
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DATA_LINE_NO));
				// データNO
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DATA_NO));
				// 送信元レコード作成日時
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME));
				// データ更新区分
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DATE_UPDATE_CATEGORY));
				// 得意先CD
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CUSTOMER_CD));
				// 仕向先CD1
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DESTINATION_CD1));
				// 仕向先CD2
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DESTINATION_CD2));
				// 品名略号
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PRODUCT_NAME_ABBREVIATION));
				// カラーNo
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.COLOR_NO));
				// グレード1
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.GRADE_1));
				// 適用開始日
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.APPLICATION_START_DATE));
				if(priceSmallParcel == 0 && priceForheadColor == 0)	{
					// ロット数量
					addColumnData(builder, lotNumArray[i]);
					// 通貨CD
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CURRENCY_CD));
					// 取引先枝番
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CLIENT_BRANCH_NUMBER));
					// 価格形態
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PRICE_FORM));
					// 改定前単価
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
					// 仕切単価(決定値)
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PARTITION_UNIT_PRICE));
					// 小口配送単価
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
					// 小口着色単価
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
					// 末端価格、エンドユーザー単価
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.TOTAL_RETAIL_PRICE));
				}
				if(priceSmallParcel != 0 && priceForheadColor == 0)	{
					// ロット数量
					addColumnData(builder, lotNumArray[i]);
					// 通貨CD
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CURRENCY_CD));
					// 取引先枝番
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CLIENT_BRANCH_NUMBER));
					// 価格形態
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PRICE_FORM));
					if ("100".equals(lotNumArray[i])) {
						// 仕切単価(決定値)
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PARTITION_UNIT_PRICE));
						// 改定前単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
						// 小口配送単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
						// 小口着色単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
						// 末端価格、エンドユーザー単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.RETAIL_PRICE));
					}
					if ("0".equals(lotNumArray[i])) {
						// 仕切単価(決定値)
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PARTITION_UNIT_PRICE));
						// 改定前単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
						// 小口配送単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
						// 小口着色単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
						// 末端価格、エンドユーザー単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.TOTAL_RETAIL_PRICE));
					}
				}
				if(priceSmallParcel == 0 && priceForheadColor != 0)	{
					// ロット数量
					addColumnData(builder, lotNumArray[i]);
					// 通貨CD
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CURRENCY_CD));
					// 取引先枝番
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CLIENT_BRANCH_NUMBER));
					// 価格形態
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PRICE_FORM));
					if ("0".equals(lotNumArray[i])) {
						// 仕切単価(決定値)
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PARTITION_UNIT_PRICE));
						// 改定前単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
						// 小口配送単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
						// 小口着色単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
						// 末端価格、エンドユーザー単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.RETAIL_PRICE));
					}
					if ("100".equals(lotNumArray[i])) {
						// 仕切単価(決定値)
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PARTITION_UNIT_PRICE));
						// 改定前単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
						// 小口配送単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
						// 小口着色単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
						// 末端価格、エンドユーザー単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.TOTAL_RETAIL_PRICE));
					}
				}
				if(priceSmallParcel != 0 && priceForheadColor != 0)	{
					// ロット数量
					addColumnData(builder, lotNumArray[i]);
					// 通貨CD
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CURRENCY_CD));
					// 取引先枝番
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CLIENT_BRANCH_NUMBER));
					// 価格形態
					addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PRICE_FORM));
					if ("300".equals(lotNumArray[i])) {
						// 仕切単価(決定値)
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PARTITION_UNIT_PRICE));
						// 改定前単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
						// 小口配送単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
						// 小口着色単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
						// 末端価格、エンドユーザー単価
						priceTemp = Double.parseDouble(WebDbUtils.getValue(data, MitsubishiConst.TOTAL_RETAIL_PRICE)) - Double.parseDouble(WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
						addColumnData(builder, String.valueOf(priceTemp));
					}
					if ("100".equals(lotNumArray[i])) {
						// 仕切単価(決定値)
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PARTITION_UNIT_PRICE));
						// 改定前単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
						// 小口配送単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
						// 小口着色単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
						// 末端価格、エンドユーザー単価
						priceTemp = Double.parseDouble(WebDbUtils.getValue(data, MitsubishiConst.TOTAL_RETAIL_PRICE)) - Double.parseDouble(WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
						addColumnData(builder, String.valueOf(priceTemp));
					}
					if ("0".equals(lotNumArray[i])) {
						// 仕切単価(決定値)
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PARTITION_UNIT_PRICE));
						// 改定前単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
						// 小口配送単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
						// 小口着色単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
						// 末端価格、エンドユーザー単価
						addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.TOTAL_RETAIL_PRICE));
					}
				}
				// 契約番号
				addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CONTRACT_NUMBER));
				// 用途CD
				addLastColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.USAGE_REF));
			 }
		 }else {
			// データ行NO
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DATA_LINE_NO));
			// データNO
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DATA_NO));
			// 送信元レコード作成日時
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.SOURCE_RECORD_CREATION_DATETIME));
			// データ更新区分
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DATE_UPDATE_CATEGORY));
			// 得意先CD
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CUSTOMER_CD));
			// 仕向先CD1
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DESTINATION_CD1));
			// 仕向先CD2
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.DESTINATION_CD2));
			// 品名略号
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PRODUCT_NAME_ABBREVIATION));
			// カラーNo
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.COLOR_NO));
			// グレード1
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.GRADE_1));
			// 適用開始日
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.APPLICATION_START_DATE));
			// ロット数量
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.LOT_QUANTITY));
			// 通貨CD
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CURRENCY_CD));
			// 取引先枝番
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CLIENT_BRANCH_NUMBER));
			// 価格形態
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PRICE_FORM));
			// 仕切単価(決定値)
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.PARTITION_UNIT_PRICE));
			// 改定前単価
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_BEFORE_REVISION));
			// 小口配送単価
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_SMALL_PARCEL));
			// 小口着色単価
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.UNIT_PRICE_FOREHEAD_COLOR));
			// 末端価格、エンドユーザー単価
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.TOTAL_RETAIL_PRICE));
			// 契約番号
			addColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.CONTRACT_NUMBER));
			// 用途CD
			addLastColumnData(builder, WebDbUtils.getValue(data, MitsubishiConst.USAGE_REF));
		 }
	}

	private static void addColumnData(StringBuilder builder, String data) {
		addColumnData(builder, data, false);
	}

	private static void addLastColumnData(StringBuilder builder, String data) {
		addColumnData(builder, data, true);
	}

	private static void addColumnData(StringBuilder builder, String data, boolean isLastColumn) {
		builder.append(MitsubishiConst.DATA_QUOTE);
		builder.append(StringUtils.addEmpty(data));
		builder.append(MitsubishiConst.DATA_QUOTE);
		if (!isLastColumn) {
			builder.append(MitsubishiConst.COMMA);
		}else {
			builder.append(MitsubishiConst.CR_LF);
		}
	}
}
