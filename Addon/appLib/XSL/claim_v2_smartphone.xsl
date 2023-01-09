<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
	<!-- ********************************************************************** -->
	<!--		コンタクト管理：クレーム　スタイルシート							-->
	<!-- ********************************************************************** -->
	<xsl:decimal-format name="myFormat" NaN="0" zero-digit="0"/>
	<!-- ***** ボディ ***** -->
	<xsl:template match="DOCUMENT">
		<xsl:apply-templates select="./CLAIM_DETAIL" />
		<xsl:apply-templates select="./RFM_USERFORMAT" />
		<xsl:apply-templates select="./MIGRATE_DATA" />
		<xsl:apply-templates select="./CLAIM_EMPLOYEES" />
	</xsl:template>

	<!-- クレーム内容 -->
	<xsl:template match="CLAIM_DETAIL">
		<xsl:variable name="firstOrderAmount" select="number(./FIRST_ORDER_AMOUNT)" />
		<xsl:variable name="firstOrderProfit" select="number(./FIRST_ORDER_PROFIT)" />
		<div class="contents">
			<span style="color:#FF0000">[発生日時]</span>
				<span><xsl:value-of select="./OCCURRENCE_DATE" /></span><br/>

			<span style="color:#FF0000">[顧客名]</span>
			<br/>
			<span class="iap-default">
				<xsl:if test="string-length(./ENTERPRISE_ID/text())>0">
					<xsl:value-of select="./ENTERPRISE_NAME" disable-output-escaping="yes"/>
					<br/>
				</xsl:if>
				<xsl:if test="string-length(./POST_ID/text())>0">
					<xsl:value-of select="./POST_NAME" disable-output-escaping="yes"/>
					<br/>
				</xsl:if>
				<xsl:if test="string-length(./PERSON_ID/text())>0">
					<xsl:value-of select="./PERSON_NAME" disable-output-escaping="yes"/>
					<br/>
				</xsl:if>
			</span>

			<span style="color:#FF0000">[発信元手段]</span>
				<span><xsl:value-of select="./OBTAINMEASURE" /></span><br/>

			<span style="color:#FF0000">[内容]</span><br/>
				<span class="iap-default">
					<xsl:value-of disable-output-escaping="yes" select="./CONTENT" />
				</span>
				<br/>

			<xsl:if test="./COMMODITY_NAME">
				<span style="color:#FF0000">[商品]</span>
					<span><xsl:value-of select="./COMMODITY_NAME" disable-output-escaping="yes"/></span><br/>
			</xsl:if>

			<span style="color:#FF0000">[原因]</span>
				<span><xsl:value-of select="./CAUSE" /></span><br/>
			<span style="color:#FF0000">[対応状況]</span>
				<span><xsl:value-of select="./STATUS" /></span><br/>
			<span style="color:#FF0000">[対応期限]</span>
				<span><xsl:value-of select="./CORRESPOND_DATE" /></span><br/>
		</div>
	</xsl:template>



	<!-- ///// ルート要素（フォーマット情報）のテンプレート ///// -->
	<xsl:template match="RFM_USERFORMAT">
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
		<xsl:apply-templates select="./RFM_ITEM" />
	</xsl:template>
	<xsl:template match="MIGRATE_DATA">
			<xsl:if test="string-length(./text())>0">
				<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
				<span style="color:#FF0000">[<xsl:value-of select="./LABEL" disable-output-escaping="yes"/>]</span><br/>
				<span class="iap-default"><xsl:value-of select="./CONTENT" disable-output-escaping="yes"/></span><br/>
			</xsl:if>		
	</xsl:template>

	<!-- ///// 項目情報のテンプレート ///// -->
	<xsl:template match="RFM_ITEM">
		<!-- ///// 装飾項目以外の場合 ///// -->
		<xsl:if test="@type != '2000'">
			<xsl:apply-templates select="./RFM_FL" />
			<br/>
			<xsl:apply-templates select="./RFM_DATA">
				<xsl:with-param name="dtype" select="@type"></xsl:with-param>
			</xsl:apply-templates>
			<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
			<xsl:apply-templates select="./RFM_BL" />
			<br/>
		</xsl:if>
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
			<xsl:when test="$dtype='24'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='25'">
				<xsl:apply-templates select="./RFM_CODE"/>
				<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
				<xsl:apply-templates select="./RFM_NAME" />
			</xsl:when>
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

	<!-- 前リテラルのテンプレート -->
	<xsl:template match="RFM_FL">
		<xsl:choose>
			<xsl:when test="not(string-length(./text())=0)">
				<span style="color:#FF0000">[<xsl:value-of select="." disable-output-escaping="yes" />]</span> 
			</xsl:when>
			<xsl:otherwise>
				<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ///// 後リテラルのテンプレート ///// -->
	<xsl:template match="RFM_BL">
		<xsl:choose>
			<xsl:when test="not(string-length(./text())=0)">
				<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;]]></xsl:text>
				<xsl:value-of select="." disable-output-escaping="yes" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ///// コード値のテンプレート ///// -->
	<xsl:template match="RFM_CODE">
		<xsl:value-of select="." disable-output-escaping="yes" />
	</xsl:template>

	<!-- ///// 名称値のテンプレート ///// -->
	<xsl:template match="RFM_NAME">
		<xsl:apply-templates />
	</xsl:template>


	<!-- ///// 報告先 ///// -->
	<xsl:template match="CLAIM_EMPLOYEES">
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
		<span class="iap-default">報告先</span><br/>
		<xsl:if test="count(./CHARGE_EMP)>0">
			<span style="color:#FF0000">[同報配信先　顧客担当社員]</span><br/>
			<xsl:for-each select="./CHARGE_EMP">
				<xsl:value-of select="." disable-output-escaping="yes"/>
					<xsl:if test="position() != last()">
						<xsl:text>、</xsl:text>
					</xsl:if>
			</xsl:for-each>
			<br/>
		</xsl:if>
		<xsl:if test="count(./SHARE_EMP)>0">
			<span style="color:#FF0000">[同報配信先　その他通知先]</span><br/>
			<xsl:for-each select="./SHARE_EMP">
				<xsl:value-of select="." disable-output-escaping="yes"/>
					<xsl:if test="position() != last()">
						<xsl:text>、</xsl:text>
					</xsl:if>
			</xsl:for-each>
			<br/>
		</xsl:if>
	</xsl:template>


</xsl:stylesheet>
