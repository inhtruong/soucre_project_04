package net.poweregg.mitsubishi.webdb.utils;

/**
 * WebDBアクセス共通関数 定数class
 */
public interface WebDbConstant {

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"; // フォーマット:yyyy-MM-dd HH24:MI:SS

	public static final String PROCESS_CREATE = "C";
	public static final String PROCESS_UPDATE = "U";
	public static final String PROCESS_DELETE = "D";

	public static final String EVENT_UPDATE = "U";
	public static final String EVENT_CHECK = "C";

	public static final String RESPONSE_JSON_MESSAGES = "messages";
	public static final String RESPONSE_UTF8_ENCODING = "UTF-8";
	public static final String RESPONSE_CONTENT_TYPE = "application/json; charset=UTF-8";
	public static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";

	public static final String RESPONSE_JSON_RESULT = "result";
	public static final String RESPONSE_RESULT_SUCCESS = "S";
	public static final String RESPONSE_RESULT_WARN = "W";
	public static final String RESPONSE_RESULT_ERROR = "E";

	// JSON Item Keys

	// system
	public static final String JSON_SYSTEM = "system";

	// database
	public static final String JSON_DATABASE = "database";

	// screen
	public static final String JSON_SCREEN = "screen";

	// event
	public static final String JSON_EVENT = "event";

	// process
	public static final String JSON_PROCESS = "process";

	// stamp
	public static final String JSON_STAMP = "stamp";

	// loginUser
	public static final String JSON_LOGINUSER = "loginUser";

	// record
	public static final String JSON_RECORD = "record";
	public static final String JSON_RECORD_VALUE = "value";
	public static final String JSON_RECORD_TYPE = "type";

	public static final String JSON_RECORDS = "records";
	public static final String JSON_DETAILS = "details";

	public static final String JSON_COUNT = "count";

	public static final String JSON_CODE = "code";
	public static final String JSON_MESSAGE = "message";
	public static final String JSON_NO = "No";
	public static final String JSON_NOTIFICATION = "notification";
	public static final String JSON_EXTERNAL = "external";

	// 入力形式：会社コンボボックス用
	public static final String JSON_RECORD_COMPANY_CORPCODE = "corpCode";
	public static final String JSON_RECORD_COMPANY_NAME = "name";

	// 入力形式：部門選択用
	public static final String JSON_RECORD_DEPARTMENT_CORPCODE = "corpCode";
	public static final String JSON_RECORD_DEPARTMENT_DEPTCODE = "deptCode";
	public static final String JSON_RECORD_DEPARTMENT_NAME = "name";

	// 入力形式：社員選択用
	public static final String JSON_RECORD_EMPLOYEE_CORPCODE = "corpCode";
	public static final String JSON_RECORD_EMPLOYEE_DEPTCODE = "deptCode";
	public static final String JSON_RECORD_EMPLOYEE_EMPCODE = "empCode";
	public static final String JSON_RECORD_EMPLOYEE_NAME = "name";

	// 入力形式：お客様選択用
	public static final String JSON_RECORD_CUSTOMER_ENTERPRISEID = "enterpriseID";
	public static final String JSON_RECORD_CUSTOMER_POSTID = "postID";
	public static final String JSON_RECORD_CUSTOMER_PERSONID = "personID";
	public static final String JSON_RECORD_CUSTOMER_ENTERPRISE_NAME = "enterpriseName";
	public static final String JSON_RECORD_CUSTOMER_POST_NAME = "postName";
	public static final String JSON_RECORD_CUSTOMER_PERSON_NAME = "personName";

	// 入力形式：商品選択用
	public static final String JSON_RECORD_COMMODITY_CODE = "code";
	public static final String JSON_RECORD_COMMODITY_NAME = "name";

	// 入力形式：URL
	public static final String JSON_RECORD_URL_CODE = "url";
	public static final String JSON_RECORD_URL_NAME = "name";

	// 入力形式：添付ファイル
	public static final String JSON_RECORD_ATTACH_NAME = "name";

	// 入力形式：イメージ
	public static final String JSON_RECORD_IMAGE_NAME = "name";

	// 入力形式：DB選択項目、DB参照コード入力
	public static final String JSON_RECORD_DB_CODE = "code";
	public static final String JSON_RECORD_DB_NAME = "name";

	// 入力形式：WebDB選択項目、WebDB参照コード入力
	public static final String JSON_RECORD_WEBDB_CODE = "code";
	public static final String JSON_RECORD_WEBDB_NAME = "name";

	// ログインユーザー用
	public static final String JSON_RECORD_LOGINUSER = "loginUser";
	public static final String JSON_RECORD_LOGINUSER_CODE = "code";
	public static final String JSON_RECORD_LOGINUSER_NAME = "name";

	// フラグ
	public static final String FLAG_TRUE = "true";
	public static final String FLAG_FALSE = "false";

	// 入力形式：スタンプ
	public static final String JSON_RECORD_STAMP_RESULT = "result";
	public static final String JSON_RECORD_STAMP_CORPCODE = "corpCode";
	public static final String JSON_RECORD_STAMP_DEPTCODE = "deptCode";
	public static final String JSON_RECORD_STAMP_EMPCODE = "empCode";
	public static final String JSON_RECORD_STAMP_NAME = "name";
	public static final String JSON_RECORD_STAMP_DATE = "date";

	// システム標準項目
	public static final String JSON_RECORD_SYSTEM_COLUMN_NO = "No";
	public static final String JSON_RECORD_SYSTEM_COLUMN_REGEMP = "登録者";
	public static final String JSON_RECORD_SYSTEM_COLUMN_REGDATE = "登録日時";
	public static final String JSON_RECORD_SYSTEM_COLUMN_UPDEMP = "最終更新者";
	public static final String JSON_RECORD_SYSTEM_COLUMN_UPDDATE = "最終更新日時";

	// 会計部門 管理マスタ
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_BRANCH_CD = "店所コード";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_BRANCH = "店所";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_ACCOUTING_UPPER_CD = "会計上位部門コード";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_ACCOUTING_UPPER_NAME = "会計上位部門名";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_ACCOUTING_LOWER_CD = "会計下位部門コード";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_ACCOUTING_LOWER_NAME = "会計下位部門名";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_JOURNAL_CD = "会計部門連携コード";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_COST_CLASS_SALE = "原価区分(販管費)";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_COST_CLASS_OPERATING = "原価区分(業務費)";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_COST_CLASS_INDIVIDUAL_CONSTRUCTION = "原価区分(個別工事原価)";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_COST_CLASS_CONSTRUCTION_DIFF = "原価区分(工事原価差額)";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_COST_CLASS_OTHER = "原価区分(その他)";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_DETAIL_DEPT_IDENTIFICATION_GROUP = "明細部門識別グループ";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_DETAIL_DEPT_SUMMIT_CORP_CODE = "SUMMIT連携用店所コード";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_EXPIRE_DATE_START = "有効期限開始";
	public static final String ACCOUNTING_DEPT_MASTER_COLUMN_EXPIRE_DATE_END = "有効期限終了";
	
	//発注者名称
	public static final String JSON_RECORD_ORDER_PARTY_NAME = "name";

	// フィールド種類
	// 月項目
	public static final int ITEM_TYPE_MONTH = 7;
	// 日項目
	public static final int ITEM_TYPE_DAY = 8;

	// 文字列(1行)
	public static final int ITEM_TYPE_TEXT = 1;
	// 数値
	public static final int ITEM_TYPE_NUMBER = 2;
	// 日付項目
	public static final int ITEM_TYPE_DATE = 5;
	// 年月項目
	public static final int ITEM_TYPE_YM = 6;
	// 時刻項目
	public static final int ITEM_TYPE_TIME = 9;
	// 文字列(複数行)
	public static final int ITEM_TYPE_TEXTAREA = 11;
	// 自動計算項目
	public static final int ITEM_TYPE_AUTOCALC = 12;
	// 自動採番
	public static final int ITEM_TYPE_SLIPNO = 13;
	// リッチテキスト
	public static final int ITEM_TYPE_RICHTEXT = 14;
	// フラグ
	public static final int ITEM_TYPE_FLAG = 20;
	// チェックボックス
	public static final int ITEM_TYPE_CHECK = 21;
	// ラジオボタン
	public static final int ITEM_TYPE_RADIO = 22;
	// ドロップダウンリスト
	public static final int ITEM_TYPE_DROPDOWN = 23;

	// ＤＢ参照項目
	public static final int ITEM_TYPE_DB = 24;
	// ＤＢ参照コード入力
	public static final int ITEM_TYPE_DBCODE = 25;
	// WebDB参照項目
	public static final int ITEM_TYPE_WEBDB = 26;
	// WebDB参照コード入力
	public static final int ITEM_TYPE_WEBDBCODE = 27;
	// 会社コンボボックス
	public static final int ITEM_TYPE_CORP = 31;
	// 部門選択項目
	public static final int ITEM_TYPE_DEPT = 51;
	// 社員選択項目
	public static final int ITEM_TYPE_EMP = 52;
	// お客様選択項目
	public static final int ITEM_TYPE_CUSTOMER = 53;
	// 商品選択項目
	public static final int ITEM_TYPE_COMMODITY = 54;

	// 添付ファイル
	public static final int ITEM_TYPE_ATTACH = 55;
	// イメージ
	public static final int ITEM_TYPE_IMAGE = 56;
	// ＵＲＬ
	public static final int ITEM_TYPE_URL = 57;

	// 日付時刻項目
	public static final int ITEM_TYPE_DATETIME = 58;
	// 時間量項目
	public static final int ITEM_TYPE_TIME_AMOUNT = 59;
	// 時間量計算項目
	public static final int ITEM_TYPE_CALC_TIME_AMOUNT = 60;
	// 日付時刻計算項目
	public static final int ITEM_TYPE_CALC_DATETIME = 61;
	// 複数社員選択
	public static final int ITEM_TYPE_ANY_EMP = 62;
	// スタンプ項目
	public static final int ITEM_TYPE_STAMP = 63;
	// WebDB間連携項目
	public static final int ITEM_TYPE_WEBDB_CONN = 64;

	// WebDB No
	public static final int ITEM_TYPE_NO = 85;

	/** デリミタ **/
	public static final String DELIMITER = ",";

	/** 空白 **/
	public static final String BLANK = "";

	/** 半角スペース **/
	public static final String SPACE = " ";

	/** 全角スペース **/
	public static final String EM_SPACE = "　";

	/** 改行コード **/
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	/** 時間量フォーマット 1日23時45分 **/
	public static final String TIME_AMOUNT_FORMAT1 = "{0}日{1}時{2}分";

	/** 時間量フォーマット 1)23:45 **/
	public static final String TIME_AMOUNT_FORMAT2 = "{0}){1}:{2}";

	/** 時間量フォーマット 1d23h45m **/
	public static final String TIME_AMOUNT_FORMAT3 = "{0}d{1}h{2}m";

	/** corpId */
	public static final String ALL_CORP = "0000000000";

	public enum COMMON_NO_WEBDB {
		/** 消費税区分パターン */
		PWA0006("PWA0006"),
		/** 消費税区分 */
		PWA0007("PWA0007"),
		/** 管理項目パターン */
		PWA0008("PWA0008"),
		/** 管理項目 */
		PWA0009("PWA0009"),
		/** 管理項目チェック */
		PWA0010("PWA0010"),
		/** RestInfo&WebDBInfo */
		PWA0011("PWA0011"),
		/** 取引先 */
		PWA0017("PWA0017"),
		/* 銀休日カレンダー */
		PWA0020("PWA0020"),
		/** 申請メニュー */
		PWA0021("PWA0021"),
		/** 会計部門 */
		PWA0022("PWA0022"),
		/** 日当・宿泊費規定確認用リンク */
		PWA0023("PWA0023");

		private final String value;

		COMMON_NO_WEBDB(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 管理項目パターン ClassNo
	 * 
	 * @author tuongtv
	 *
	 */
	public enum MANAGEMENT_ITEM_PATTERN_CLASSNO {
		/** 管理項目パターン */
		CLASSNO_0001("0001"),
		/** 管理項目パターンコード */
		CLASSNO_0002("0002"),
		/** 管理項目コード１ */
		CLASSNO_0003("0003"),
		/** 管理項目コード2 */
		CLASSNO_0004("0004"),
		/** 管理項目コード3 */
		CLASSNO_0005("0005"),
		/** 管理項目コード4 */
		CLASSNO_0006("0006"),
		/** 管理項目コード5 */
		CLASSNO_0007("0007"),
		/** 管理項目コード6 */
		CLASSNO_0008("0008"),
		/** 管理項目コード7 */
		CLASSNO_0009("0009"),
		/** 管理項目コード8 */
		CLASSNO_0010("0010"),
		/** 管理項目コード備考 */
		CLASSNO_0011("0011");

		private final String value;

		MANAGEMENT_ITEM_PATTERN_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 管理項目
	 * 
	 * @author tuongtv
	 *
	 */
	public enum MANAGEMENT_ITEM_CLASSNO {
		/** 管理項目 */
		CLASSNO_0001("0001"),
		/** 管理項目コード */
		CLASSNO_0002("0002"),
		/** 名称 */
		CLASSNO_0003("0003"),
		/** 入力方式 */
		CLASSNO_0004("0004"),
		/** 入力必須 */
		CLASSNO_0005("0005"),
		/** 最小桁数 */
		CLASSNO_0006("0006"),
		/** 最大桁数 */
		CLASSNO_0007("0007"),
		/** 入力属性 */
		CLASSNO_0008("0008"),
		/** 初期値 */
		CLASSNO_0009("0009"),
		/** 選択肢 */
		CLASSNO_0010("0010"),
		/** SUMMIT転送先１ */
		CLASSNO_0011("0011"),
		/** SUMMIT転送先２ */
		CLASSNO_0012("0012"),
		/** SUMMIT識別区分 */
		CLASSNO_0013("0013");

		private final String value;

		MANAGEMENT_ITEM_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 管理項目チェック
	 * 
	 * @author tuongtv
	 *
	 */
	public enum MANAGEMENT_ITEM_CHK_CLASSNO {
		/** 管理項目チェック */
		CLASSNO_0001("0001"),
		/** 管理項目コード */
		CLASSNO_0002("0002"),
		/** コード */
		CLASSNO_0003("0003"),
		/** 正式名称 */
		CLASSNO_0004("0004"),
		/** 略名称 */
		CLASSNO_0005("0005"),
		/** 備考 */
		CLASSNO_0006("0006"),
		/** 表示順 */
		CLASSNO_0007("0007"),
		/** 有効区分 */
		CLASSNO_0008("0008");

		private final String value;

		MANAGEMENT_ITEM_CHK_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 消費税区分パターン
	 * 
	 * @author nambh
	 *
	 */
	public enum CONSUMPTION_TAX_CLASS_PATTERN_CLASSNO {

		/** 消費税区分パターン */
		CLASSNO_0001("0001"),
		/** 消費税区分パターンコード */
		CLASSNO_0002("0002"),
		/** 消費税区分コード */
		CLASSNO_0003("0003"),
		/** 表示順 */
		CLASSNO_0004("0004"),
		/** 備考 */
		CLASSNO_0005("0005");

		private final String value;

		CONSUMPTION_TAX_CLASS_PATTERN_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 消費税区分
	 * 
	 * @author nambh
	 *
	 */
	public enum CONSUMPTION_TAX_CLASS_CLASSNO {
		/** 消費税区分 */
		CLASSNO_0001("0001"),
		/** 消費税区分コード */
		CLASSNO_0002("0002"),
		/** 名称 */
		CLASSNO_0003("0003"),
		/** 課税区分 */
		CLASSNO_0004("0004"),
		/** 消費税率 */
		CLASSNO_0005("0005"),
		/** 備考 */
		CLASSNO_0006("0006"),
		/** 表示順 */
		CLASSNO_0007("0007");

		private final String value;

		CONSUMPTION_TAX_CLASS_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 取引先 ClassNo
	 * 
	 * @author Cuongnc
	 *
	 */
	public enum SUPPLIER_CLASSNO {
		/** 取引先 */
		CLASSNO_0001("0001"),
		/** 会社ID */
		CLASSNO_0002("0002"),
		/** 取引先コード */
		CLASSNO_0003("0003"),
		/** 枝番 */
		CLASSNO_0004("0004"),
		/** 取引先名 */
		CLASSNO_0005("0005"),
		/** 取引先略名 */
		CLASSNO_0006("0006"),
		/** 取引先カナ名 */
		CLASSNO_0007("0007");

		private final String value;

		SUPPLIER_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 申請メニュー ClassNo
	 * 
	 * @author minhhtp
	 *
	 */
	public enum APPLICATION_MENU_CLASSNO {
		/** 申請メニュー */
		CLASSNO_0001("0001"),
		/** No */
		CLASSNO_0002("0002"),
		/** 区分 */
		CLASSNO_0003("0003"),
		/** メニュー分類１ */
		CLASSNO_0004("0004"),
		/** メニュー分類名１ */
		CLASSNO_0005("0005"),
		/** メニュー分類説明１ */
		CLASSNO_0006("0006"),
		/** メニュー分類２ */
		CLASSNO_0007("0007"),
		/** メニュー分類名２ */
		CLASSNO_0008("0008"),
		/** メニュー分類説明２ */
		CLASSNO_0009("0009"),
		/** メニュー分類３ */
		CLASSNO_0010("0010"),
		/** メニュー分類名３ */
		CLASSNO_0011("0011"),
		/** メニュー分類説明３ */
		CLASSNO_0012("0012"),
		/** メニュー分類４ */
		CLASSNO_0013("0013"),
		/** メニュー分類名４ */
		CLASSNO_0014("0014"),
		/** メニュー分類説明４ */
		CLASSNO_0015("0015"),
		/** 原価区分 */
		CLASSNO_0016("0016"),
		/** 出発日表示 */
		CLASSNO_0017("0017"),
		/** 到着日表示 */
		CLASSNO_0018("0018"),
		/** 理由等表示 */
		CLASSNO_0019("0019"),
		/** 報告事項表示 */
		CLASSNO_0020("0020"),
		/** 日当表示 */
		CLASSNO_0021("0021"),
		/** 明細種別初期値 */
		CLASSNO_0022("0022"),
		/** 表示グループ */
		CLASSNO_0023("0023"),
		/** 遷移先画面区分 */
		CLASSNO_0024("0024"),
		/** 表示画面URL(PC) */
		CLASSNO_0025("0025"),
		/** 表示画面URL(MB) */
		CLASSNO_0026("0026"),
		/** 表示順 */
		CLASSNO_0027("0027");

		private final String value;

		APPLICATION_MENU_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 会計部門
	 * 
	 * @author thidth
	 *
	 */
	public enum ACCOUNTING_DEPARTMENT {
		/** 会計部門 */
		CLASSNO_0001("0001"),
		/** 店所コード */
		CLASSNO_0002("0002"),
		/** 会計上位部門コード */
		CLASSNO_0003("0003"),
		/** 会計上位部門名 */
		CLASSNO_0004("0004"),
		/** 会計下位部門コード */
		CLASSNO_0005("0005"),
		/** 会計下位部門名 */
		CLASSNO_0006("0006"),
		/** 会計部門連携コード */
		CLASSNO_0007("0007"),
		/** 原価区分(販管費) */
		CLASSNO_0008("0008"),
		/** 原価区分(業務費) */
		CLASSNO_0009("0009"),
		/** 原価区分(個別工事原価) */
		CLASSNO_0010("0010"),
		/** 原価区分(工事原価差額) */
		CLASSNO_0011("0011"),
		/** 原価区分(その他) */
		CLASSNO_0012("0012"),
		/** 明細部門識別グループ */
		CLASSNO_0013("0013"),
		/** SUMMIT連携用店所コード */
		CLASSNO_0014("0014"),
		/** 有効期限開始 */
		CLASSNO_0015("0015"),
		/** 有効期限終了 */
		CLASSNO_0016("0016"),
		/** ID */
		CLASSNO_0017("0017"),
		/** 登録日時 */
		CLASSNO_0018("0018"),
		/** 登録者 */
		CLASSNO_0019("0019"),
		/** 最終更新日時 */
		CLASSNO_0020("0020"),
		/** 最終更新者 */
		CLASSNO_0021("0021"),
		/** 地域コード */
		CLASSNO_0022("0022"),
		/** 地域名 */
		CLASSNO_0023("0023"),
		/** 店所名 */
		CLASSNO_0024("0024");

		private final String value;

		ACCOUNTING_DEPARTMENT(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum REST_WEBDB_CLASSNO {
		/** APIキー */
		CLASSNO_0001("0001"),
		/** pe4jUrl */
		CLASSNO_0002("0002"),
		/** pe4xUrl */
		CLASSNO_0003("0003"),
		/** common_peurl */
		CLASSNO_0004("0004"),
		/** common_uri */
		CLASSNO_0005("0005");

		private final String value;

		REST_WEBDB_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * RestInfo&WebDBInfo
	 * 
	 * @author tuongtv
	 *
	 */
	public enum REST_WEBDB_INFO {
		/** WebDB APIキー */
		CLASSNO_0001("0001"),
		/** WebDB Uri */
		CLASSNO_0002("0002"),
		/** pe4jUrl */
		CLASSNO_0003("0003"),
		/** pe4xUrl */
		CLASSNO_0004("0004"),
		/** Rest APIキー */
		CLASSNO_0005("0005"),
		/** 日当規定確認用リンク */
		CLASSNO_0006("0006");

		private final String value;

		REST_WEBDB_INFO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	/**
	 * 銀休日カレンダー
	 * 
	 * @author Nambh
	 *
	 */
	public enum SILVER_HOLIDAY_CALENDAR {
		/** 銀休日のカレンダマスタの番号 */
		CLASSNO_0001("0001");

		private final String value;

		SILVER_HOLIDAY_CALENDAR(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public enum ALLOWANCE_LINK {
		/** 日当・宿泊費規定確認用リンク */
		CLASSNO_0001("0001");

		private final String value;

		ALLOWANCE_LINK(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * UBacthJob01
	 * 
	 * @author nambh
	 *
	 */
	public enum U_BATCH_JOB {
		/** jobId */
		CLASSNO_0001("0001"),
		/** startDate */
		CLASSNO_0002("0002"),
		/** endDate */
		CLASSNO_0003("0003"),
		/** registerEmpId */
		CLASSNO_0004("0004"),
		/** jobStatus */
		CLASSNO_0005("0005"),
		/** docId */
		CLASSNO_0006("0006"),
		/** processNo */
		CLASSNO_0007("0007"),
		/** exportDate */
		CLASSNO_0008("0008"),
		/** bizYear */
		CLASSNO_0009("0009"),
		/** message */
		CLASSNO_00010("0010");
		
		private final String value;

		U_BATCH_JOB(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

}
