<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

	<xsl:template match="DOCUMENT">
		<xsl:apply-templates select="./RFM_USERFORMAT" />
		<xsl:apply-templates select="./RRDFREEFORM" />
		<xsl:apply-templates select="./RRDOFFICE" />
		<xsl:apply-templates select="./externalDocument" />
		<xsl:apply-templates select="./RRDATTACHMENTCOMMENT" />
		<xsl:apply-templates select="./RRDBBS" />
	</xsl:template>


<!-- ///// ルート要素（フォーマット情報）のテンプレート ///// -->
	<xsl:template match="RFM_USERFORMAT">
		<xsl:if test="@title">
			<div>【<xsl:value-of select="@title" disable-output-escaping="yes"/>】</div>
		</xsl:if>
		<xsl:apply-templates select="./RFM_ITEM" />
		<br/>
	</xsl:template>


<!-- ///// 項目情報のテンプレート ///// -->
	<xsl:template match="RFM_ITEM">
		<!-- ///// 装飾項目以外の場合 ///// -->
		<xsl:if test="@type != '2000'">
			<span style="color:#FF0000">[<xsl:value-of select="./RFM_FL" disable-output-escaping="yes" />]</span>
			<br/>
			<xsl:apply-templates select="./RFM_DATA">
				<xsl:with-param name="dtype" select="@type"></xsl:with-param>
			</xsl:apply-templates>
			<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
			<xsl:apply-templates select="./RFM_BL"/>
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
				<xsl:value-of select="." disable-output-escaping="yes" />
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


<!-- フリー域のテンプレート -->
	<xsl:template match="RRDFREEFORM">
		<xsl:if test="string-length(./text())>0">
			<xsl:value-of select="." disable-output-escaping="yes" />
			<br/>
		</xsl:if>
	</xsl:template>


<!--
	Original: 2004.10.12 V1.7
	Office ActiveX
-->
	<xsl:template match="RRDOFFICE" >
		<xsl:if test="OFFICEDOCID[text()] != '' " >
			<span style="color:#FF0000">* There is a Excel content.(Can not view from this device)</span>
			<br/>
		</xsl:if>
	</xsl:template>


<!-- StraForm域のテンプレート -->
	<xsl:template match="externalDocument">
		<xsl:if test="documentId[text()] != '' and formId[text()] != ''" >
			<span style="color:#FF0000">* There is a StraForm content. (Can not view from this device)</span>
			<br/>
		</xsl:if>
	</xsl:template>


<!-- attachment, paperDocument -->
	<xsl:template match="RRDATTACHMENTCOMMENT">
		<xsl:if test="string-length(./text())>0">
				<span style="color:#FF0000">[Paper attachement]</span>
				<br/>
				<xsl:value-of select="." disable-output-escaping="yes" />
				<br/>
		</xsl:if>
	</xsl:template>

	<!-- BBS -->
	<xsl:template match="RRDBBS">
		<xsl:if test="string-length(./text())>0">
			<xsl:if test="@title">
				<div><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="../RRDBBS">
					<span style="color:#FF0000">[Employee responsible for posting]</span>
					<br/>
					<xsl:value-of select="./CHARGEEMPNAME" disable-output-escaping="yes" />
					<br/>
					<span style="color:#FF0000">[Posting period]</span>
					<br/>
					<xsl:value-of select="./DISPSTARTDATE" disable-output-escaping="yes" />
					-
					<xsl:value-of select="./DISPENDDATE" disable-output-escaping="yes" />
					<br/>
					<span style="color:#FF0000">[NaviView notice period]</span>
					<br/>
					<xsl:choose>
						<xsl:when test="./NAVIVIEWDISPOFF">
							<xsl:value-of select="./NAVIVIEWDISPOFF" disable-output-escaping="yes" />
							<br/>
						</xsl:when>
						<xsl:when test="./NAVIVIEWDISPON">
							<xsl:value-of select="./NAVIVIEWDISPON" disable-output-escaping="yes" />
							<br/>
							<xsl:value-of select="./NAVIBBSDISP" disable-output-escaping="yes" />
							<br/>
							<xsl:choose>
								<xsl:when test="./NAVIBBSDISPSTARTDATE">
									<span style="color:#FF0000">[Notice period]</span>
									<br/>
									<xsl:value-of select="./NAVIBBSDISPSTARTDATE" disable-output-escaping="yes" />
									-
									<xsl:value-of select="./NAVIBBSDISPENDDATE" disable-output-escaping="yes" />
									<br/>
								</xsl:when>
							</xsl:choose>
							<xsl:value-of select="./SCHEDULEBBSDISP" disable-output-escaping="yes" />
							<br/>
							<xsl:choose>
								<xsl:when test="./NAVIEVENTDISPSTARTDATE">
									<span style="color:#FF0000">[Notice period]</span>
									<br/>
									<xsl:value-of select="./NAVIEVENTDISPSTARTDATE" disable-output-escaping="yes" />
									-
									<xsl:value-of select="./NAVIEVENTDISPENDDATE" disable-output-escaping="yes" />
									<br/>
									<span style="color:#FF0000">[Event period]</span>
									<br/>
									<xsl:choose>
										<xsl:when test="./HOLDINGFROMDATE">
											<xsl:value-of select="./HOLDINGFROMDATE" disable-output-escaping="yes" />
											-
											<xsl:value-of select="./HOLDINGTODATE" disable-output-escaping="yes" />
										</xsl:when>
									</xsl:choose>
									<br/>
									<span style="color:#FF0000">[Event location]</span>
									<br/>
									<xsl:value-of select="./HOLDINGPLACE" disable-output-escaping="yes" />
									<br/>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
					<span style="color:#FF0000">[Post board]</span>
					<br/>
					<xsl:value-of select="./BBSID" disable-output-escaping="yes" />
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
