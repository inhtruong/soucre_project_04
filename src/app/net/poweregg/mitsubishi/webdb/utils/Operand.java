package net.poweregg.mitsubishi.webdb.utils;

/**
 * WebDBアクセス共通関数
 * 条件文 定数class
 */
public enum Operand {

	//同じ
	EQUALS("="),

	//異なる
	NOT_EQUALS("!="),

	//始まる
	BEGIN("^"),

	//含む
	INCLUDE("~"),

	//含まない
	NOT_INCLUDE("!~"),

	//より大きい
	MORE_THAN(">"),

	//より小さい
	LESS_THAN("<"),

	//以上
	MORE(">="),

	//以下
	LESS("<="),

	//未入力
	BLANK("n"),

	//入力済
	NOT_BLANK("!n"),

	//配下（部門選択で利用）
	SUBORDINATE("~="),

	//企業が同じ（お客様選択で利用）
	SAME_AS_ENTERPRISE("c="),

	//部門が同じ（お客様選択で利用）
	SAME_AS_POST("b="),

	//担当者が同じ（お客様選択で利用）
	SAME_AS_PERSON("e="),

	//チェック済み（フラグで利用）
	CHECK_ON("true"),

	//未チェック（フラグで利用）
	CHECK_OFF("false"),
	
	//IN
	IN("IN")
	;

	private String operand;

	Operand(String operand) {
		this.operand = operand;
	}

	public String toString() {
		return this.operand;
	}
}
