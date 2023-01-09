<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" encoding="utf-8" />
<!-- ********************************************************************** -->
<!--		精算処理系：出張申請　スタイルシート							-->
<!-- ********************************************************************** -->

<!-- ***** ルート ***** -->
<xsl:template match="/">
	<xsl:apply-templates select="DOCUMENT" />
</xsl:template>

<!-- ***** ボディ ***** -->
<xsl:template match="DOCUMENT">
	<div class="contents margin">
	<table class="detail">
	<tr>
		<td class="label">出張種別</td>
		<td class="item"><xsl:value-of select="./BUSINESSTRIPCLASSNAME" />　<xsl:value-of select="./BUSINESSTRIPBREAKDOWNCLASSNAME" /></td>
	</tr>
	</table>
	<span class="iap-default">出発</span>
	<table class="detail">
	<tr>
		<td class="label">出発日時</td>
		<td class="item"><xsl:value-of select="./DEPARTDATE" />&#160;<xsl:value-of select="./DEPARTTIME" /></td>
	</tr>
	<tr>
		<td class="label">交通機関</td>
		<td class="item"><xsl:value-of select="./DEPARTTRANSPORT" /></td>
	</tr>
	<tr>
		<td class="label">便名</td>
		<td class="item"><xsl:value-of select="./DEPARTFLIGHTNAME" /></td>
	</tr>
	</table>
	<span class="iap-default">帰着</span>
	<table class="detail">
	<tr>
		<td class="label">到着日時</td>
		<td class="item"><xsl:value-of select="./ARRIVEDATE" />&#160;<xsl:value-of select="./ARRIVETIME" /></td>
	</tr>
	<tr>
		<td class="label">交通機関</td>
		<td class="item"><xsl:value-of select="./ARRIVETRANSPORT" /></td>
	</tr>
	<tr>
		<td class="label">便名</td>
		<td class="item"><xsl:value-of select="./ARRIVEFLIGHTNAME" /></td>
	</tr>
	</table>
	<table class="detail margin">
	<tr>
		<td class="label">訪問先</td>
		<td class="item"><xsl:value-of select="./VISITEDNAME" /></td>
	</tr>
	<xsl:apply-templates select="./PURPOSE" />
	<xsl:apply-templates select="./CONTENTS" />
	<tr>
		<td class="label">仮払金額</td>
		<td class="item"><xsl:value-of select="./TEMPPAYMONEYAMOUNT" /></td>
	</tr>
	</table>
	</div>
	<xsl:apply-templates select="./BILLABLE" />		<!-- 負担金額 -->
</xsl:template>

<xsl:template match="PURPOSE">				<!-- 理由 -->
		<tr>
			<td class="label"><span class="iap-default">理由</span></td>
			<td class="item"><span class="iap-default"><xsl:apply-templates /></span></td>
		</tr>
</xsl:template>
<xsl:template match="BR">
	<xsl:element name="br" />
</xsl:template>
<xsl:template match="br">
	<xsl:element name="br" />
</xsl:template>

<xsl:template match="CONTENTS">						<!-- 備考 -->
	<tr>
		<td class="label" nowrap="nowrap">備考<br />
		※連絡先、宿泊先</td>
		<td class="item"><xsl:apply-templates /></td>
	</tr>
</xsl:template>

<xsl:template match="BILLABLE">						<!-- 負担金額 -->
	<div class="contents margin">
	<table class="dr-table rich-table">
	<tr class="rich-table-header">
		<td class="headBorderRight">負担部門</td>
		<td class="noBorderRight">プロジェクト</td>
	</tr>
	<xsl:for-each select="BILLABLEDETAILS">
		<xsl:choose>
			<xsl:when test="position() mod 2 = 1">
				<tr class="oddRow">
					<td class="borderRight"><xsl:value-of select="./BILLABLEDEPT" /></td>
					<td class="noBorderRight"><xsl:value-of select="./BILLABLEPROJECT" /></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr class="evenRow">
					<td class="borderRight"><xsl:value-of select="./BILLABLEDEPT" /></td>
					<td class="noBorderRight"><xsl:value-of select="./BILLABLEPROJECT" /></td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:for-each>
	</table></div>
</xsl:template>

</xsl:stylesheet>
