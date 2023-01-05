package net.poweregg.mitsubishi.webdb.utils;

/**
 * SPAアクセス共通関数 定数class
 */
public interface SPACooperationConstant {
	/** corpId */
	public static final String ALL_CORP = "0000000000";
	public static final String LOG_SPA="SPA";
	
	public enum COMMON_NO_SPA {
		/** ログイン情報 */
		LOGIN("9990"),
		/** URL */
		URI_URL("SPA0001"),
		/** カスタムプロパティ */
		CUSTOM_PROPERTY("SPA0002"),
		/** SPA親フォルダーのフォルダーID */
		SPA_ROOT_FOLDER_ID("SPA0003"),
		/** SPA連携設定 */
		SPA_COPPORATION_SETTING("SPA0004");

		private final String value;

		COMMON_NO_SPA(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * ログイン情報 ClassNo
	 * @author anhlt
	 *
	 */
	public enum LOGIN_CLASSNO {
		/** hostname*/
		CLASSNO_HOSTNAME("1"),
		/** ユーザー */
		CLASSNO_USER("2"),
		/** domain */
		CLASSNO_DOMAIN("3"),
		/** パスワード */
		CLASSNO_PASSWORD("4");

		private final String value;

		LOGIN_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * SPAカスタムプロパティのデータの型
	 * @author anhlt
	 *
	 */
	public enum CUSTOMPROPERTY_TYPE {
		/** string */
		STRING("string"),
		/** number */
		NUMBER("number"),
		/** date */
		DATE("date"),
		/** boolean */
		BOOLEAN("boolean"),
		/** hyperLink */
		HYPERLINK("hyperLink");
	
		private final String value;

		CUSTOMPROPERTY_TYPE(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * SPAカスタムプロパティの日付のフォーマット
	 * @author anhlt
	 *
	 */
	public enum CUSTOMPROPERTY_DATE_TYPE_FORMAT {
		/** yyyyMMddHHmmss */
		yyyyMMddHHmmss("1"),
		/** yyyyMMddHHmm */
		yyyyMMddHHmm("2"),
		/** yyyyMMdd */
		yyyyMMdd("3");
	
		private final String value;

		CUSTOMPROPERTY_DATE_TYPE_FORMAT(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * SPAカスタムプロパティの数値型
	 * @author anhlt
	 *
	 */
	public enum CUSTOMPROPERTY_NUMBER_TYPE_FORMAT {
		/** 整数のみの数値 */
		NO_FRACTION("1"),
		/** 小数を含んだ数値 */
		FRACTION("2");
	
		private final String value;

		CUSTOMPROPERTY_NUMBER_TYPE_FORMAT(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * SPAカスタムプロパティ
	 * @author anhlt
	 *
	 */
	public enum CUSTOMPROPERTY_ITEM_CLASSNO {
		/** 名称 */
		NAME("0001"),
		/** 取引先 */
		SUPPLIERS("0002"),
		/** 金額 */
		AMOUNT("0003"),
		/** 申請番号 */
		APPLICATIONNO("0004"),
		/** 申請者 */
		EMPNAME("0005"),
		/** 代理申請 */
		PROXYEMPNAME("0006"),
		/** 処理年月日 */
		APPLIEDDATE("0007"),
		/** 決裁日 */
		APPROVEDDATE("0008"),
		/** 起票店所名 */
		STORENAME("0009")
		,/** 申請部門名 */
		APPLYDEPTNAME("0010");
	
		private final String value;

		CUSTOMPROPERTY_ITEM_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
    
    /**
	 * SPAのWebAPIのURI
	 * @author anhlt
	 *
	 */
	public enum SPA_WEBAPI_URI_CLASSNO {
		/** ログイン用のURL */
		LOGIN_URL("0001"),				//"/spa/service/auth/login"
		/** ログアウト用のURL */
		LOGOUT_URL("0002"),				//"/spa/service/auth/logout"
		/** アーカイブ用のURL */
		ARCHIVES_URL("0003"),			//"/spa/service/archives_v3"
		/** アーカイブ用のURL */
		CUSTOM_PROPERTIES_URL("0004"),	//"/spa/service/properties_v2"
		/** フォルダ取得のURL */
		FOLDER_LOOKUP_URL("0005"),		//"/spa/service/folders/lookup"
		/** フォルダ作成用のURL */
		FOLDER_CREATE_URL("0006");		//"/spa/service/folders_v2"
	
		private final String value;

		SPA_WEBAPI_URI_CLASSNO(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * SPA連携設定
	 * @author anhlt
	 *
	 */
	public enum SPA_COPPORATION_SETTING {
		/** ログファイル名 */
		LOG_FILE_NAME("0001"),
		/** ログフォルダパス */
		LOG_FILE_PATH("0002"),
		/** SPA添付フォルダ */
		SPA_TEMP_FOLDER("0003");
	
		private final String value;

		SPA_COPPORATION_SETTING(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * SPA親フォルダーのフォルダーID
	 * @author anhlt
	 *
	 */
	public enum SPA_ROOT_FOLDER_ID {
		/** SPA親フォルダーのフォルダーID */
		ROOT_FOLDER_ID("0001");
	
		private final String value;

		SPA_ROOT_FOLDER_ID(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	/**
	 * SPA連携結果
	 * @author anhlt
	 *
	 */
	public enum SPA_RESULT {
		
		/** 成功 */
		SUCCESS("1"),
		/** エラー */
		ERROR("9");
	
		private final String value;

		SPA_RESULT(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
