<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" encoding="utf-8" />
<!-- ********************************************************************** -->
<!--		営業支援：業務報告書　スタイルシート							-->
<!-- ********************************************************************** -->

<!-- ***** ルート ***** -->
<xsl:template match="/">
	<xsl:apply-templates select="DOCUMENT" />
</xsl:template>



<!-- ***** ボディ ***** -->
<xsl:template match="DOCUMENT">
	<h2><xsl:value-of select="./HEADLINE" /></h2>
	<div class="contents">
	<table class="detail">
	<tr>
		<td class="label">日付</td>
		<td class="item"><xsl:value-of select="./ACTIVEDATE" /></td>
	</tr>
	<tr>
		<td class="label">時刻</td>
		<td class="item"><xsl:value-of select="./ACTIVETIME" /></td>
	</tr>
	<tr>
		<td class="label">予定区分</td>
		<td class="item"><xsl:value-of select="./PLANCLASS" /></td>
	</tr>
	<tr>
		<td class="label">場所</td>
		<td class="item"><xsl:value-of select="./PLACE" /></td>
	</tr>
	<tr>
		<td class="label">場所詳細</td>
		<td class="item"><xsl:value-of select="./PLACEDETAILS" /></td>
	</tr>
	<tr>
		<td class="label">活動内容</td>
		<td class="item"><xsl:apply-templates select="./TASKTITLE" /></td>
	</tr>
	<tr>
		<td class="label">件名</td>
		<td class="item"><xsl:value-of select="./TITLE" /></td>
	</tr>
	<tr>
		<td class="label">顧客名</td>
		<td class="item">
			<xsl:apply-templates select="./PARTNERNAME" />
			<xsl:text disable-output-escaping="yes"> </xsl:text>
			<xsl:apply-templates select="./PARTNERORGNAME" />
		</td>
	</tr>
	<tr>
		<td class="label">顧客ご担当者</td>
		<td class="item"><xsl:apply-templates select="./PARTNERRESPNAME" /></td>
	</tr>
    <xsl:choose>
		<xsl:when test="./FLAGHIDNAME ='0'"></xsl:when>
		<xsl:otherwise>
			<tr>
				<td class="label">対象商品</td>
				<td class="item"><xsl:value-of select="./COMMODNAME"/></td>
			</tr>
		</xsl:otherwise>
	</xsl:choose>
	<tr>
		<td class="label">報告事項</td>
		<td class="item"><xsl:apply-templates select="./POINTS" /></td>
	</tr>
	<tr>
		<td class="label">紙添付資料</td>
		<td class="item"><xsl:apply-templates select="./PAPERATTACHMENT" /></td>
	</tr>
	<tr>
		<td class="label">同報配信先</td>
		<td class="item"><xsl:value-of select="./SHARELIST" /></td>
	</tr>
	<xsl:apply-templates select="./CONSULT" />
	<xsl:apply-templates select="./PROJECT" />
	<xsl:apply-templates select="./CLAIM" />
	</table>
	<h2>次の予定</h2>
	<table class="detail">
	<tr>
		<td class="label">日付</td>
		<td class="item"><xsl:value-of select="./SCHDATE" /></td>
	</tr>
	<tr>
		<td class="label">時刻</td>
		<td class="item"><xsl:value-of select="./SCHTIME" /></td>
	</tr>
	<tr>
		<td class="label">予定区分</td>
		<td class="item"><xsl:value-of select="./SCHCLASS" /></td>
	</tr>
	<tr>
		<td class="label">件名</td>
		<td class="item"><xsl:value-of select="./SCHTITLE" /></td>
	</tr>
	<tr>
		<td class="label">目的／内容</td>
		<td class="item"><xsl:value-of select="./SCHCONTENT" /></td>
	</tr>
	</table>
	</div>
	<xsl:apply-templates select="./RFM_USERFORMAT" />

</xsl:template>


<xsl:template match="POINTS">					<!-- 報告事項 -->
<xsl:apply-templates />
</xsl:template>

<xsl:template match="PAPERATTACHMENT">			<!-- 紙添付資料 -->
<xsl:apply-templates />
</xsl:template>

<xsl:template match="CONSULT">						<!-- 商談 -->
	<tr>
		<td class="label">状況</td>
		<td class="item"><xsl:value-of select="./STATUS" /></td>
	</tr>
	<tr>
		<td class="label">商談レベル</td>
		<td class="item"><xsl:value-of select="./LEVEL" /></td>
	</tr>
	<tr>
		<td class="label">受注見込額</td>
		<td class="item"><xsl:value-of select="./ORDERAMOUNT" />千円</td>
	</tr>
	<tr>
		<td class="label">利益見込額</td>
		<td class="item"><xsl:value-of select="./PROFITAMOUNT" />千円</td>
	</tr>
	<tr>
		<td class="label">受注予定日</td>
		<td class="item"><xsl:value-of select="./ORDERDATE" /></td>
	</tr>
</xsl:template>

<xsl:template match="PROJECT">						<!-- プロジェクト -->
	<tr>
		<td class="label">状況</td>
		<td class="item"><xsl:value-of select="./STATUS" /></td>
	</tr>
	<tr>
		<td class="label">進度</td>
		<td class="item"><xsl:value-of select="./LEVEL" />％</td>
	</tr>
	<tr>
		<td class="label">売上予定額</td>
		<td class="item"><xsl:value-of select="./SALESAMOUNT" />千円</td>
	</tr>
	<tr>
		<td class="label">売上予定日</td>
		<td class="item"><xsl:value-of select="./SALESDATE" /></td>
	</tr>
</xsl:template>

<xsl:template match="CLAIM">						<!-- クレーム -->
	<tr>
		<td class="label">状況</td>
		<td class="item"><xsl:value-of select="./STATUS" /></td>
	</tr>
	<tr>
		<td class="label">原因</td>
		<td class="item"><xsl:value-of select="./CAUSE" /></td>
	</tr>
	<tr>
		<td class="label">今回発生費用</td>
		<td class="item"><xsl:value-of select="./DAMAGESINCURRED" />千円</td>
	</tr>
</xsl:template>

<xsl:template match="TASKTITLE">
	<xsl:value-of select="." />
</xsl:template>

<xsl:template match="PARTNERNAME">
	<xsl:value-of select="." />
</xsl:template>

<xsl:template match="PARTNERORGNAME">
	<xsl:value-of select="." />
</xsl:template>

<xsl:template match="PARTNERRESPNAME">
	<xsl:value-of select="." />
</xsl:template>

<!--*********************************************************************** -->
<!--		ここから「入力フォーマット用」									-->
<!--*********************************************************************** -->
<!-- ///// ルート要素（フォーマット情報）のテンプレート ///// -->
<!-- ///// TABLEタグ有り、TABLE罫線無し、幅100% 		 ///// -->
<!-- ///// TABLEの列数=2								///// -->
	<xsl:template match="RFM_USERFORMAT">
		<div class="contents margin">
			<table class="detail">
<colgroup>
<col width="100" />
<col />
</colgroup>
				<tr>
				<xsl:apply-templates />
				<xsl:if test="string-length(./text())>0">
					<xsl:text disable-output-escaping="yes">&lt;/td&gt;</xsl:text>
				</xsl:if>
				</tr>
			</table>
		</div>
	</xsl:template>

	<!-- ///// 項目情報のテンプレート ///// -->
	<xsl:template match="RFM_ITEM">
		<!-- ///// 先頭項目「以外」の場合 ///// -->
		<xsl:if test="current() != ../RFM_ITEM[1]">
			<!-- 改行フラグが yes or テキストエリア の場合 or AccessoryItem -->
			<xsl:if test="@nl='yes' or @type='1111' or @type='2000'">
				<xsl:text disable-output-escaping="yes">
					&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;
				</xsl:text>
			</xsl:if>
		</xsl:if>
		<xsl:if test="current() = ../RFM_ITEM[1]">
			<xsl:if test="@nl='no' and @type != '1111'">
				<xsl:text disable-output-escaping="yes">
					&lt;td&gt;
				</xsl:text>
			</xsl:if>
		</xsl:if>

		<!-- ///// 主要タグ生成 ///// -->
		<xsl:choose>
			<!-- ///// テキストエリアの場合（TABLEタグのネストで構成） ///// -->
			<xsl:when test="@type='1111'">
				<xsl:choose>
					<!-- ///// 改行フラグがyes、前リテラル後改行がyesの場合 ///// -->
					<xsl:when test="@nl='yes' and @flnl='yes'">
						<xsl:text disable-output-escaping="yes">
								&lt;td class="label"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_FL" /><br/>
						<xsl:text disable-output-escaping="yes">
								&lt;/td&gt;
							&lt;/tr&gt;
							&lt;tr&gt;
								&lt;td class="label"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
								&lt;td class="item"&gt;
									&lt;table border="0" cellspacing="0"&gt;
										&lt;tr&gt;
											&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
											&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_BL" /><br/>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
										&lt;/tr&gt;
									&lt;/table&gt;
						</xsl:text>
					</xsl:when>
					<!-- ///// 改行フラグがyes、前リテラル後改行がnoの場合 ///// -->
					<xsl:when test="@nl='yes' and @flnl='no'">
						<xsl:text disable-output-escaping="yes">
								&lt;td class="label"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_FL" /><br/>
						<xsl:text disable-output-escaping="yes">
								&lt;/td&gt;
								&lt;td class="item"&gt;
									&lt;table border="0" cellspacing="0"&gt;
										&lt;tr&gt;
											&lt;td&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
											&lt;td&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_BL" /><br/>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
										&lt;/tr&gt;
									&lt;/table&gt;
						</xsl:text>
					</xsl:when>
					<!-- ///// 改行フラグがnoの場合, 前リテラル後改行がnoの場合 ///// -->
					<xsl:when test="@nl='no' and @flnl='no'">
						<xsl:text disable-output-escaping="yes">
							&lt;td&gt;<![CDATA[&#160;]]>&lt;span class="ufLiteral headerBg"&gt;
						</xsl:text>
						<xsl:if test="string-length(./RFM_FL/text())=0">
							<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						</xsl:if>
						<xsl:apply-templates select="./RFM_FL" />
						<xsl:text disable-output-escaping="yes">
							&lt;/span&gt;<![CDATA[&#160;]]>
								&lt;table border="0" cellspacing="0"&gt;
									&lt;tr&gt;
										&lt;td&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes">
										&lt;/td&gt;&lt;td&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_BL" /><br/>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
										&lt;/tr&gt;
									&lt;/table&gt;
						</xsl:text>
					</xsl:when>
					<!-- ///// 改行フラグがnoの場合, 前リテラル後改行がyesの場合 ///// -->
					<xsl:otherwise>
						<xsl:text disable-output-escaping="yes">
							&lt;td&gt;<![CDATA[&#160;]]>&lt;span class="ufLiteral headerBg"&gt;
						</xsl:text>
						<xsl:if test="string-length(./RFM_FL/text())=0">
							<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						</xsl:if>
						<xsl:apply-templates select="./RFM_FL" />
						<xsl:text disable-output-escaping="yes">
							&lt;/span&gt;&lt;/td&gt;
							&lt;/tr&gt;&lt;tr&gt;&lt;td class="label"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
							&lt;td class="item"&gt;
								&lt;table border="0" cellspacing="0"&gt;
									&lt;tr&gt;
										&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes">
										&lt;/td&gt;&lt;td class="item"&gt;
						</xsl:text>
						<xsl:if test="string-length(./RFM_BL/text())=0">
							<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						</xsl:if>
						<xsl:apply-templates select="./RFM_BL" />
						<xsl:text disable-output-escaping="yes">
										&lt;/td&gt;
									&lt;/tr&gt;
								&lt;/table&gt;
						</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- ///// テキストエリア以外の場合 ///// -->

			<xsl:when test="@type = '2000'">
				<xsl:value-of select="./RFM_DISPLAY" disable-output-escaping="yes" />
			</xsl:when>

			<xsl:otherwise>
				<xsl:choose>
					<!-- ///// 改行フラグがyes、前リテラル後改行がyesの場合 ///// -->
					<xsl:when test="@nl='yes' and @flnl='yes'">
						<xsl:text disable-output-escaping="yes">
							&lt;td class="label"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_FL" /><br/>
						<xsl:text disable-output-escaping="yes">
							&lt;/td&gt;&lt;td class="item"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
							&lt;/tr&gt;&lt;tr&gt;&lt;td class="label"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
							&lt;td class="item" &gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_BL" />
					</xsl:when>
					<!-- ///// 改行フラグがyes、前リテラル後改行がnoの場合 ///// -->
					<xsl:when test="@nl='yes' and @flnl='no'">
						<xsl:text disable-output-escaping="yes">
							&lt;td class="label"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_FL" /><br/>
						<xsl:text disable-output-escaping="yes">
							&lt;/td&gt;&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_BL" />
					</xsl:when>
					<!-- ///// 改行フラグがnoの場合 , 前リテラル後改行がnoの場合///// -->
					<xsl:when test="@nl='no' and @flnl='no'">
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;&#160;&#160;&#160;]]>&lt;span class="ufLiteral headerBg"&gt;</xsl:text>
						<xsl:apply-templates select="./RFM_FL" />
						<xsl:text disable-output-escaping="yes">&lt;/span&gt;<![CDATA[&#160;&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_BL" />
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;&#160;&#160;&#160;]]>&lt;span class="ufLiteral headerBg"&gt;</xsl:text>
						<xsl:apply-templates select="./RFM_FL" />
						<xsl:text disable-output-escaping="yes">
							&lt;/span&gt;&lt;/td&gt;
							&lt;/tr&gt;&lt;tr&gt;&lt;td class="label"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
							&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_BL" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- ///// 項目値のテンプレート ///// -->
	<xsl:template match="RFM_DATA">
		<!-- ///// RFM_ITEMのtype属性値を、パラメタで受け取る ///// -->
		<xsl:param name="dtype" />
		<!-- ///// 各項目の最初以外のデータは改行してから表示する ///// -->
		<xsl:choose>
			<xsl:when test="@cnt='0'"></xsl:when>
			<xsl:otherwise><br/></xsl:otherwise>
		</xsl:choose>
		<!-- ///// コード出力と名称出力を、明細形式で振分 ///// -->
		<xsl:choose>
			<xsl:when test="$dtype='21'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='22'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='23'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='31'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='51'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='52'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='53'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='54'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="./RFM_CODE">
					<xsl:with-param name="dtype2" select="$dtype"></xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ///// コード値のテンプレート ///// -->
	<xsl:template match="RFM_CODE">
		<!-- ///// RFM_ITEMのtype属性値を、パラメタで受け取る ///// -->
		<xsl:param name="dtype2" />
		<xsl:choose>
			<xsl:when test="@edit='yes' and not(string-length(./text())=0)">
				<!-- ///// コード値の編集 ///// -->
				<xsl:choose>
					<!-- ///// 文字テキストボックス ＵＲＬ編集 ///// -->
					<xsl:when test="$dtype2='1'">
						<xsl:element name="a">
							<xsl:attribute name="href"><xsl:apply-templates /></xsl:attribute>
							<xsl:attribute name="target">rfm_window</xsl:attribute>
							<xsl:apply-templates />
						</xsl:element>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$dtype2='11'">
						<xsl:apply-templates />
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>


<xsl:template match="RFM_FL">
	<xsl:value-of select="." disable-output-escaping="yes" />
</xsl:template>
	<!-- ///// 後リテラルのテンプレート ///// -->
	<xsl:template match="RFM_BL">
		<xsl:choose>
			<xsl:when test="not(string-length(./text())=0)">
				<xsl:text disable-output-escaping="yes"><![CDATA[&#160;&#160;]]></xsl:text>
				<xsl:value-of select="." disable-output-escaping="yes" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ///// 名称値のテンプレート ///// -->
	<xsl:template match="RFM_NAME">
		<xsl:apply-templates />
	</xsl:template>
<!-- ///// BRタグ対応 ///// -->
<xsl:template match="BR">
	<xsl:element name="br" />
</xsl:template>
<xsl:template match="br">
	<xsl:element name="br" />
</xsl:template>

</xsl:stylesheet>
