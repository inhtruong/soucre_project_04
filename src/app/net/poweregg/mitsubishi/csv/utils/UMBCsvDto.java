/**
 * 
 */
package net.poweregg.mitsubishi.csv.utils;

import java.util.ResourceBundle;

import net.poweregg.mitsubishi.constant.MitsubishiConst;
import net.poweregg.mitsubishi.dto.UMB01MasterDto;
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

	public static void addRowData(StringBuilder builder, UMB01MasterDto data) {

		// データ行NO
		addColumnData(builder, data.getDataLineNo());
		// データNO
		addColumnData(builder, data.getDataNo());
		// 送信元レコード作成日時
		addColumnData(builder, data.getSrcCreateDate());
		// データ更新区分
		addColumnData(builder, data.getUpdateCategory());
		// 得意先CD
		addColumnData(builder, data.getCustomerCD());
		// 仕向先CD1
		addColumnData(builder, data.getDestinationCD1());
		// 仕向先CD2
		addColumnData(builder, data.getDestinationCD2());
		// 品名略号
		addColumnData(builder, data.getProductNameAbbreviation());
		// カラーNo
		addColumnData(builder, data.getColorNo());
		// グレード1
		addColumnData(builder, data.getGrade1());
		// 適用開始日
		addColumnData(builder, data.getStartDateApplication());
		// ロット数量
		addColumnData(builder, data.getLotQuantity());
		// 通貨CD
		addColumnData(builder, data.getCurrencyCD());
		// 取引先枝番
		addColumnData(builder, data.getClientBranchNumber());
		// 価格形態
		addColumnData(builder, data.getPriceForm());
		// 仕切単価(決定値)
		addColumnData(builder, data.getUnitPricePartition());
		// 改定前単価
		addColumnData(builder, data.getUnitPriceBefRevision());
		// 小口配送単価
		addColumnData(builder, data.getUnitPriceSmallParcel());
		// 小口着色単価
		addColumnData(builder, data.getUnitPriceForeheadColor());
		// 末端価格、エンドユーザー単価
		addColumnData(builder, data.getRetailPrice());
		// 契約番号
		addColumnData(builder, data.getContractNumber());
		// 用途CD
		addLastColumnData(builder, data.getUsageRef());
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
		}
	}
}
