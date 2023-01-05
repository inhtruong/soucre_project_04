package net.poweregg.mitsubishi.webdb.utils;

/**
 * Webデータベース接続情報
 *
 * @createDate : 2018.09.01
 */
public class WebDBConnectParam {

	public WebDBConnectParam() {
	}

	/**
	 * WebAPI実行用の認証キー
	 */
	private String apiKey;

	/**
	 * pe4jUrl
	 */
	private String pe4jUrl;

	/**
	 * uri
	 */
	private String uri;

	/**
	 * Mofidy uri
	 */
	private String modifyUri;

	/**
	 * WebDB種類 システム区分（0:WebDB,1:汎用申請,2:CRM）
	 */
	private Integer system;

	/**
	 * データベース名
	 */
	private String database;

	/**
	 * encode
	 */
	private String encode;
	
	private String getWithBodyUri;

	/**
	 * @return apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey
	 *            セットする apiKey
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @param database
	 *            セットする database
	 */
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * @return system
	 */
	public Integer getSystem() {
		return system;
	}

	/**
	 * @param system
	 *            セットする system
	 */
	public void setSystem(Integer system) {
		this.system = system;
	}

	/**
	 * @return the pe4jUrl
	 */
	public String getPe4jUrl() {
		return pe4jUrl;
	}

	/**
	 * @param pe4jUrl
	 *            the pe4jUrl to set
	 */
	public void setPe4jUrl(String pe4jUrl) {
		this.pe4jUrl = pe4jUrl;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the encode
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * @param encode
	 *            the encode to set
	 */
	public void setEncode(String encode) {
		this.encode = encode;
	}

	public String getModifyUri() {
		return modifyUri;
	}

	public void setModifyUri(String modifyUri) {
		this.modifyUri = modifyUri;
	}

	public String getGetWithBodyUri() {
		return getWithBodyUri;
	}

	public void setGetWithBodyUri(String getWithBodyUri) {
		this.getWithBodyUri = getWithBodyUri;
	}

}