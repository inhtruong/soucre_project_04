/*
 * Copyright(C) 2012 D-CIRCLE, INC. All rights reserved.
 *
 * (1)このソフトウェアは、ディサークル株式会社に帰属する機密情報 であり開示を固く禁じます。
 * (2)この情報を使用するには、ディサークル株式会社とのライセンス 契約が必要となります。
 *
 * This software is the confidential and proprietary information of
 * D-CIRCLE, INC. You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license
 * agreement you entered into with D-CIRCLE.
 */

package net.poweregg.mitsubishi.constant;

/**
 * @author : phapnv
 * @PG_ID :
 * @createDate : Aug 21, 2012
 */
public class MitsubishiConst {
	public static final String COMMON_CORP_ID = "0000000000";
	public static final String ENTER = "\n";
	public static final String NEW_LINE = "\r\n";
	public static final String TAB = "\t";
	public static final String PARAM_ADMIN = "admin";
	public static final String PARAM_ADMIN_VALUE = "1";
	public static final String LOG_PWA = "PWA";
	public static final String SEPARATOR = "/";
	public static final String EXT_TXT = ".txt";
	public static final String COMMA = ",";
	public static final String DATA_QUOTE = "\"";
	public static final String CR_LF = "\r\n";
	public static final String TENPU_DIR = "TENPU_DIR";
	public static final String UNDER_LINE = "_";
	public static final String SEMICOLON = ";";
	public static final String COLON = ":";
	public static final String PERCENT = "%";
	public static final String ESCAPE = "!";
	public static final String REPLACE_PERCENT = "!%";
	// public static final String SHIFT_JIS = "SJIS";
	public static final String MS_932 = "MS932";
	// public static final String SHIFT_JIS_ENCODING = "Shift-JIS";
	public static final String WINDOWS_31J = "windows-31j";
	public static final String CSV_EXTENSION = ".csv";
	// public static final String UTF_8 = "UTF-8";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String HHMMSS = "hhmmss";
	public static final String URL_KOUJI_LIST = "/UMK/UMK0102l.jsf";
	public static final String ASSIT_MSG_TYPE = "9100";
	public static final String BLANK = "";
	/** Wait秒数 */
	public static final Long WAITING_TIME_DEFAULT = 5L;
	public static final String DATE_TIME_ISO8601 = "yyyy-MM-dd'T'HH:mm:ssXXX";

	public final static int RESULT_OK = 0;
	public final static int RESULT_EXCEPTION = 1;
	public final static int RESULT_WARNING = 9;
	public static final String LOG_BEGIN = "Begin";
	public static final String LOG_FINISH = "Finish";
	public static final String LEFT_BRACKET = "[";
	public static final String RIGHT_BRACKET = "]";
	// 保証予定年度
	public static final String EXPECTED_WARRANTY_YEAR = "2022年度";
	
	// 保証結果
	public static final String WARRANTY_RESULT_GUARANTEED = "保証済";
	public static final String WARRANTY_RESULT_UNGUARANTEED = "未保証";
	
	// 
	public static final String ERROR_EMPTY_FILE_CSV = "連携データは0件です。";
	
	public static final String SEARCH_LIST_WEBDB = "searchListWebDB";
	public static final String BACK_TO_SEARCH = "backToSearhPage";

	public enum EVENT {
		MODIFY("U"), CONFIRM("C");

		private final String value;

		EVENT(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum PROCESS {
		CREATE("C"), UPDATE("U"), DELETE("D");

		private final String value;

		PROCESS(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum EXTERNAL_LINKAGE_RESULT {
		SUCCESS("S"), ERROR("E");

		private final String value;

		EXTERNAL_LINKAGE_RESULT(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum COMMON_NO {
		/** SEARCH WEBDB */
		COMMON_NO_UTSR01("TSR01"),
		COMMON_NO_UTSR02("TSR02");

		private final String value;

		COMMON_NO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum CLASS_NO {
		CLASSNO_1("1"), CLASSNO_2("2"), CLASSNO_3("3"),
		CLASSNO_4("4"), CLASSNO_5("5"), CLASSNO_6("6"),
		CLASSNO_7("7"), CLASSNO_8("8"), CLASSNO_9("9"),
		CLASSNO_10("10"), CLASSNO_11("11"), CLASSNO_12("12"),
		CLASSNO_13("13"), CLASSNO_14("14"), CLASSNO_15("15");

		private final String value;

		CLASS_NO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
