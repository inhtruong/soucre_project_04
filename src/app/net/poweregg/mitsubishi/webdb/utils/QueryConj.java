package net.poweregg.mitsubishi.webdb.utils;

/**
 * WebDBアクセス共通関数
 * 条件文 定数class
 */
public enum QueryConj {

	AND("and"),
	OR("or");

	private String queryConj;

	QueryConj(String queryConj) {
		this.queryConj = queryConj;
	}

	public String toString() {
		return this.queryConj;
	}
}