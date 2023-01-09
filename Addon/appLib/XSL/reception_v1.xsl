<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" encoding="utf-8" />
<!-- ********************************************************************** -->
<!--		精算処理：接待精算　スタイルシート								-->
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
		<td class="label">接待日</td>
		<td class="item"><xsl:value-of select="./ENTERTAININGDATE" /></td>
	</tr>
	<tr>
		<td class="label">接待種別</td>
		<td class="item"><xsl:value-of select="./ENTERTAININGTYPE" /></td>
	</tr>
	<xsl:apply-templates select="./CUSTOMERS" />
	<xsl:apply-templates select="./OTHERCUST" />
	<tr>
		<td class="label">出席人数</td>
		<td class="item"><xsl:value-of select="./ATTENDERCOUNT" /></td>
	</tr>
	<xsl:apply-templates select="./NECESSITY" />
	</table>
	</div>

	<div class="contents margin">
	<table class="detail">
	<xsl:apply-templates select="./ATTENDEMPNAME" />
	<xsl:apply-templates select="./OTHERCUSTATTEND" />
	</table>
	</div>
	
	<div class="contents margin">
	<table class="detail">
	<tr>
		<td class="label">接待合計税込金額</td>
		<td class="item"><xsl:value-of select="./SUMTAXINCLUDEDPRICE" /></td>
	</tr>
	<tr>
		<td class="label">支払済税込金額</td>
		<td class="item"><xsl:value-of select="./FINISHPAYTAXINCLUDEDPRICE" /></td>
	</tr>
	<xsl:apply-templates select="./TEMPPAYMONEYAMOUNT" />
	<tr>
		<td class="label">精算金額</td>
		<td class="item"><xsl:value-of select="./CALCULATEDPRICE" /></td>
	</tr>
	</table>
	</div>
	<xsl:apply-templates select="./DETAILS" />
</xsl:template>


<xsl:template match="CUSTOMERS">		<!-- 接待先 -->
	<tr>
		<td class="label">接待先</td>
		<td class="item"><xsl:value-of select="." /></td>
	</tr>
</xsl:template>

<xsl:template match="CUSTCORPNAME">
	<xsl:value-of select="." />
</xsl:template>

<xsl:template match="CUSTDEPTNAME">
	<xsl:value-of select="." />
</xsl:template>

<xsl:template match="CUSTEMPNAME">
	<xsl:value-of select="." />
</xsl:template>

<xsl:template match="OTHERCUST">		<!-- その他接待先 -->
	<tr>
		<td class="label">接待先</td>
		<td class="item"><xsl:value-of select="." /></td>
	</tr>
</xsl:template>

<xsl:template match="NECESSITY">		<!-- 理　由 -->
	<tr>
		<td class="label">理由</td>
		<td class="item">
			<xsl:apply-templates />
		</td>
	</tr>
</xsl:template>
<xsl:template match="BR">
	<xsl:element name="br" />
</xsl:template>
<xsl:template match="br">
	<xsl:element name="br" />
</xsl:template>

<xsl:template match="ATTENDEMPNAME">		<!-- 当社出席者 -->
	<tr>
		<td class="label">当社出席者</td>
		<td class="item"><xsl:value-of select="." /></td>
	</tr>
</xsl:template>

<xsl:template match="OTHERCUSTATTEND">		<!-- 他出席者 -->
	<tr>
		<td class="label">他出席者</td>
		<td class="item"><xsl:value-of select="." /></td>
	</tr>
</xsl:template>

<xsl:template match="TEMPPAYMONEYAMOUNT">	<!-- 仮払金額 -->
	<tr>
		<td class="label">仮払金額</td>
		<td class="item"><xsl:value-of select="." /></td>
	</tr>
</xsl:template>


<!-- 明細表 -->
<xsl:template match="DETAILS">
	<div class="contents margin">
	<table class="dr-table rich-table">
	<tr class="rich-table-header">
		<td class="headBorderRight">接待場所</td>
		<td class="headBorderRight">品名</td>
		<td class="headBorderRight">税込金額・支払</td>
		<td class="headBorderRight">負担金額</td>
		<td class="headBorderRight">負担部門　プロジェクト</td>
		<td class="noBorderRight">摘要</td>
	</tr>
	<xsl:for-each select="TR">
		<xsl:choose>
			<xsl:when test="position() mod 2 = 1">
				<tr class="oddRow">
					<td class="borderRight"><xsl:value-of select="./ENTERTAININGPLACE" /></td>
					<td class="borderRight"><xsl:value-of select="./ITEMNAME" /></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./AMOUNTPAYMENT" /></div></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./BILLABLEPAYMENT" /></div></td>
					<td class="borderRight"><xsl:value-of select="./DEPTPROJECT" /></td>
					<td class="noBorderRight"><xsl:value-of select="./OUTLINE" /></td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr class="evenRow">
					<td class="borderRight"><xsl:value-of select="./ENTERTAININGPLACE" /></td>
					<td class="borderRight"><xsl:value-of select="./ITEMNAME" /></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./AMOUNTPAYMENT" /></div></td>
					<td class="borderRight"><div class="right"><xsl:value-of select="./BILLABLEPAYMENT" /></div></td>
					<td class="borderRight"><xsl:value-of select="./DEPTPROJECT" /></td>
					<td class="noBorderRight"><xsl:value-of select="./OUTLINE" /></td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:for-each>
	</table>
	</div>
</xsl:template>

</xsl:stylesheet>
