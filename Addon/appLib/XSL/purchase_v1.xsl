<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" encoding="UTF-8" />
<!-- ********************************************************************** -->
<!--		精算処理系：購入申請　スタイルシート							-->
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
			<td class="label"><span class="iap-default">購入日</span></td>
			<td class="item"><span class="iap-default"><xsl:value-of select="./PURCHASEDATE" /></span></td>
		</tr>
		<tr>
			<td class="label"><span class="iap-default">購入種類</span></td>
			<td class="item"><span class="iap-default"><xsl:value-of select="./PURCHASETYPENAME" /></span></td>
		</tr>
		<tr>
			<td class="label"><span class="iap-default">申請区分</span></td>
			<td class="item"><span class="iap-default"><xsl:value-of select="./PURCHASEAPPCLASSNAME" /></span></td>
		</tr>
		<xsl:apply-templates select="./NECESSITY" />
		<tr>
			<td class="label"><span class="iap-default">購入合計税込金額</span></td>
			<td class="item"><span class="iap-default"><xsl:value-of select="./SUMTAXINCLUDEDPRICE" /></span></td>
		</tr>
		<tr>
			<td class="label"><span class="iap-default">支払済税込金額</span></td>
			<td class="item"><span class="iap-default"><xsl:value-of select="./FINISHPAYTAXINCLUDEDPRICE" /></span></td>
		</tr>
		<tr>
			<td class="label"><span class="iap-default">仮払金額</span></td>
			<td class="item"><span class="iap-default"><xsl:value-of select="./TEMPPAYMONEYAMOUNT" /></span></td>
		</tr>
		<tr>
			<td class="label"><span class="iap-default">精算金額</span></td>
			<td class="item"><span class="iap-default"><xsl:value-of select="./CALMONEYAMOUNT" /></span></td>
		</tr>
	</table>
	</div>
	<xsl:apply-templates select="./BILLABLE" />		<!-- 負担金額 -->
</xsl:template>

<xsl:template match="NECESSITY">				<!-- 理由 -->
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

<xsl:template match="BILLABLE">					<!-- 負担金額 -->
	<div class="contents margin">
	<table class="dr-table rich-table">
	<tr class="rich-table-header">
		<td class="headBorderRight">購入先</td>
		<td class="headBorderRight">品名</td>
		<td class="headBorderRight">金額・支払</td>
		<td class="headBorderRight">負担金額</td>
		<td class="headBorderRight">負担部門　プロジェクト</td>
		<td class="noBorderRight">摘要</td>
	</tr>
	<xsl:for-each select="BILLABLEDETAILS">
		<xsl:choose>
			<xsl:when test="position() mod 2 = 1">
				<tr class="oddRow">
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./RETAILERNAME" /></span></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./ITEMNAME" /></span></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./MONAYAMOUNTPAYMENT" /></div></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./BILLABLEMONEYAMOUNT" /></div></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./BILLABLEDEPTPROJECT" /></span></td>
					<td class="noBorderRight"><span class="iap-default"><xsl:value-of select="./OUTLINE" /></span></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr class="evenRow">
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./RETAILERNAME" /></span></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./ITEMNAME" /></span></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./MONAYAMOUNTPAYMENT" /></div></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./BILLABLEMONEYAMOUNT" /></div></td>
					<td class="borderRight"><span class="iap-default"><xsl:value-of select="./BILLABLEDEPTPROJECT" /></span></td>
					<td class="noBorderRight"><span class="iap-default"><xsl:value-of select="./OUTLINE" /></span></td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:for-each>
	</table>
	</div>
</xsl:template>

</xsl:stylesheet>
