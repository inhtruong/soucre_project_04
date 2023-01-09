<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" encoding="utf-8" />
<!-- ********************************************************************** -->
<!--		精算処理系：旅費精算　スタイルシート							-->
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
		<td class="label">日時</td>
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
		<td class="label">日時</td>
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
	<xsl:apply-templates select="./REPORTPOINTS" />
	<tr>
		<td class="label">日当</td>
		<td class="item"><xsl:value-of select="./DAILYALLOWANCE" />
		×
		<xsl:value-of select="./NOOFDAYS" />
		　=　
		<xsl:value-of select="./DAILYALLOWANCEAMOUNT" /></td>
	</tr>
	<tr>
		<td class="label">宿泊</td>
		<td class="item"><xsl:value-of select="./OVERNIGHTALLOWANCE" />
		×
		<xsl:value-of select="./NOOFNIGHT" />
		　=　
		<xsl:value-of select="./NIGHTALLOWANCEAMOUNT" />
		<xsl:value-of select="./HASRECEIPTCLASSNAME" /></td>
	</tr>
	<tr>
		<td class="label">食事代</td>
		<td class="item"><xsl:value-of select="./FOODALLOWANCE" /></td>
	</tr>
	</table>
	</div>
	<xsl:apply-templates select="./OTHER" />		<!-- その他 -->
	<xsl:apply-templates select="./TRANSPORT" />	<!-- 交通費 -->
	<div class="contents margin">
	<table class="detail">
		<tr>
			<td class="label">合計金額</td>
			<td class="item">　<xsl:value-of select="./TOTALMONEYAMOUNT" /></td>
		</tr>
	</table>
	</div>
	<xsl:apply-templates select="./TEMPPAY" />		<!-- 仮払い -->
	<div class="contents margin">
	<table class="detail">
		<tr>
			<td class="label">精算金額</td>
			<td class="item">　<span class="iap-default"><xsl:value-of select="./SETTLEMENTMONEYAMOUNT" /></span></td>
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


<xsl:template match="REPORTPOINTS">					<!-- 報告事項 -->
		<tr>
			<td class="label"><span class="iap-default">報告事項</span></td>
			<td class="item"><span class="iap-default"><xsl:apply-templates /></span></td>
		</tr>
</xsl:template>

<xsl:template match="OTHER">						<!-- その他 -->
	<div class="contents margin">
	<table class="dr-table rich-table">
	<tr class="rich-table-header">
		<td class="headBorderRight">その他内容</td>
		<td class="noBorderRight">金額</td>
	</tr>
	<xsl:for-each select="OTHERDETAILS">
		<xsl:choose>
			<xsl:when test="position() mod 2 = 1">
				<tr class="oddRow">
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./OTHERTRAVELEXPCALITEMCLASSNAME" />　<xsl:value-of select="./OTHERTRAVELEXPCALOTHER" /></span></td>
					<td class="noBorderRight"><div class="right"><xsl:value-of select="./OTHERMONEYAMOUNT" /></div></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr class="evenRow">
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./OTHERTRAVELEXPCALITEMCLASSNAME" />　<xsl:value-of select="./OTHERTRAVELEXPCALOTHER" /></span></td>
					<td class="noBorderRight"><div class="right"><xsl:value-of select="./OTHERMONEYAMOUNT" /></div></td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:for-each>
	</table>
	</div>
</xsl:template>

<xsl:template match="TRANSPORT">					<!-- 交通費 -->
	<div class="contents margin">
	<table class="dr-table rich-table">
	<tr class="rich-table-header">
		<td class="headBorderRight">使用日</td>
		<td class="headBorderRight">交通機関</td>
		<td class="headBorderRight">出発地</td>
		<td class="headBorderRight">到着地</td>
		<td class="headBorderRight">金額</td>
		<td class="noBorderRight">領収書</td>
	</tr>
	<xsl:for-each select="TRANSPORTDETAILS">
		<xsl:choose>
			<xsl:when test="position() mod 2 = 1">
				<tr class="oddRow">
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTPAIDDATE" /></span></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTMODE" /></span></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTDEPART" /></span></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTARRIVE" /></span></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./TRANSPORTMONEYAMOUNT" /></div></td>
					<td class="noBorderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTHASRECEIPT" /></span></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr class="evenRow">
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTPAIDDATE" /></span></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTMODE" /></span></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTDEPART" /></span></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTARRIVE" /></span></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./TRANSPORTMONEYAMOUNT" /></div></td>
					<td class="noBorderRight"><span class="iap-default"><xsl:value-of select="./TRANSPORTHASRECEIPT" /></span></td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:for-each>
		<tr>
		<td class="headerBg borderRight" colspan="4">合計金額</td>
		<td class="borderRight"><div class="right"><xsl:value-of select="./TRANSPORTTOTALMONEYAMOUNT" /></div></td>
		<td class="headerBg noBorderRight"></td>
		</tr>
	</table>
	</div>
</xsl:template>

<xsl:template match="TEMPPAY">					<!-- 仮払い -->
	<div class="contents margin">
	<table class="dr-table rich-table">
	<tr class="rich-table-header">
		<td class="headBorderRight">仮払い種別</td>
		<td class="headBorderRight">摘要</td>
		<td class="noBorderRight">金額</td>
	</tr>
	<xsl:for-each select="TEMPPAYDETAILS">
		<xsl:choose>
			<xsl:when test="position() mod 2 = 1">
				<tr class="oddRow">
					<td class="borderRight"><xsl:value-of select="./TEMPPAYITEMCLASSNAME" /></td>
					<td class="borderRight"><xsl:value-of select="./TEMPPAYPOUTLINE" /></td>
					<td class="noBorderRight"><div class="right"><xsl:value-of select="./TEMPPAYMONEYAMOUNT" /></div></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr class="evenRow">
					<td class="borderRight"><xsl:value-of select="./TEMPPAYITEMCLASSNAME" /></td>
					<td class="borderRight"><xsl:value-of select="./TEMPPAYPOUTLINE" /></td>
					<td class="noBorderRight"><div class="right"><xsl:value-of select="./TEMPPAYMONEYAMOUNT" /></div></td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:for-each>
		<tr>
		<td class="headerBg borderRight" colspan="2">合計金額</td>
		<td class="noBorderRight"><div class="right"><xsl:value-of select="./TEMPPAYTOTALMONEYAMOUNT" /></div></td>
		</tr>
	</table>
	</div>
</xsl:template>

<xsl:template match="BILLABLE">					<!-- 負担金額 -->
	<div class="contents margin">
	<table class="dr-table rich-table">
	<tr class="rich-table-header">
		<td class="headBorderRight">負担金額</td>
		<td class="headBorderRight">負担部門</td>
		<td class="noBorderRight">プロジェクト</td>
	</tr>
	<xsl:for-each select="BILLABLEDETAILS">
		<xsl:choose>
			<xsl:when test="position() mod 2 = 1">
				<tr class="oddRow">
					<td class="borderRight"><div class="right"><xsl:value-of select="./BILLABLEAMOUNT" /></div></td>
					<td class="borderRight"><xsl:value-of select="./BILLABLEDEPT" /></td>
					<td class="noBorderRight"><xsl:value-of select="./BILLABLEPROJECT" /></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr class="evenRow">
					<td class="borderRight"><div class="right"><xsl:value-of select="./BILLABLEAMOUNT" /></div></td>
					<td class="borderRight"><xsl:value-of select="./BILLABLEDEPT" /></td>
					<td class="noBorderRight"><xsl:value-of select="./BILLABLEPROJECT" /></td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:for-each>
	</table>
	</div>
</xsl:template>

</xsl:stylesheet>
