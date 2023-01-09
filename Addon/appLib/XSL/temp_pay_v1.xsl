<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" encoding="utf-8" />
<!-- ********************************************************************** -->
<!--		精算処理系：仮払い申請　スタイルシート							-->
<!-- ********************************************************************** -->

<!-- ***** ルート ***** -->
<xsl:template match="/">
	<xsl:apply-templates select="DOCUMENT" />
</xsl:template>

<!-- ***** ボディ ***** -->
<xsl:template match="DOCUMENT">
	<h2>申請内容</h2>
	<div class="contents">
	<table class="detail">
		<tr>
			<td class="label">理由</td>
			<td class="item"><xsl:apply-templates select="./OUTLINE" /></td>
		</tr>
	</table>
	</div>
	<div class="contents margin">
	<h2>仮払明細</h2>
	<table class="dr-table rich-table">
		<tr class="rich-table-header">
			<td class="headBorderRight">仮払い種別</td>
			<td class="headBorderRight">摘要</td>
			<td class="headBorderRight">金額</td>
			<td class="noBorderRight">振込</td>
		</tr>
		<tr>
			<td class="borderRight"><xsl:value-of select="./TEMPPAYMENTITEMCLASSNAME" /></td>
			<td class="borderRight"><xsl:value-of select="./MEMO" /></td>
			<td class="borderRight"><div class="right"><xsl:value-of select="./TEMPPAYMONEYAMOUNT" /></div></td>
			<td class="noBorderRight"><xsl:value-of select="./TRANSFERNECESSITYCLASSNAME" /></td>
		</tr>
		<tr>
			<td class="headerBg borderRight" colspan="2">合計金額</td>
			<td class="noBorderRight"><div class="right"><xsl:value-of select="./TEMPPAYMONEYAMOUNTSUM" /></div></td>
			<td class="headerBg noBorderRight"></td>
		</tr>
	</table>
	</div>
</xsl:template>

<xsl:template match="OUTLINE">					<!-- 理由 -->
<xsl:apply-templates />
</xsl:template>
<xsl:template match="BR">
	<xsl:element name="br" />
</xsl:template>
<xsl:template match="br">
	<xsl:element name="br" />
</xsl:template>

</xsl:stylesheet>
