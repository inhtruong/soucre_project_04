<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" encoding="utf-8" />

<!--// 交通費決裁内容表示 //-->
<xsl:template match="/">
	<xsl:apply-templates />
</xsl:template>

<!--// 交通費情報のテンプレート //-->
<xsl:template match="IAPHEADER">
	<h2>申請内容</h2>
	<div class="contents">
	<table class="detail">
		<tr>
			<td class="label">訪問先</td>
			<td class="item"><span class="iap-default"><xsl:value-of select="./IAPVISITEDNAME" /></span></td>
		</tr>
		<xsl:apply-templates select="./IAPOUTLINE" />
	</table>
	</div>
</xsl:template>

<!--// 理由表示変換用 //-->
<xsl:template match="IAPOUTLINE">
		<tr>
			<td class="label">理由</td>
			<td class="item"><span class="iap-default"><xsl:apply-templates /></span></td>
		</tr>
</xsl:template>


<!--// 交通費明細のテンプレート //-->
<xsl:template match="IAPDETAILS">
	<div class="contents margin">
	<h2>交通費明細</h2>
	<table class="dr-table rich-table">
		<tr class="rich-table-header">
			<td class="headBorderRight">使用日</td>
			<td class="headBorderRight">交通機関</td>
			<td class="headBorderRight">出発地</td>
			<td class="headBorderRight">到着地</td>
			<td class="headBorderRight">金額</td>
			<td class="noBorderRight">領収書</td>
		</tr>
		<xsl:for-each select="./IAPDETAILS-LIST/IAPDETAILS-REC">
			<xsl:variable name="moneyamount" select="number(./IAPMONEYAMOUNT)" />
			<xsl:choose>
				<xsl:when test="position() mod 2 = 1">
					<tr class="oddRow">
						<td class="borderRight"><span class="iap-default"><xsl:value-of select="./IAPPAIDDATE" /></span></td>
						<td class="borderRight"><span class="iap-default"><xsl:value-of select="./IAPTRANSPORTMODE" /></span></td>
						<td class="borderRight"><span class="iap-default"><xsl:value-of select="./IAPDEPARTFROM" /></span></td>
						<td class="borderRight"><span class="iap-default"><xsl:value-of select="./IAPARRIVETO" /></span></td>
						<td class="borderRight"><div class="right"><xsl:value-of select="format-number($moneyamount, '###,###,###,###')" /></div></td>
						<td class="noBorderRight"><span class="iap-default">
						<xsl:choose>
							<xsl:when test="@class='0001'">無し</xsl:when>
							<xsl:when test="@class='0002'">有り</xsl:when>
							<xsl:otherwise />
						</xsl:choose>
						</span>
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr class="evenRow">
						<td class="borderRight"><span class="iap-default"><xsl:value-of select="./IAPPAIDDATE" /></span></td>
						<td class="borderRight"><span class="iap-default"><xsl:value-of select="./IAPTRANSPORTMODE" /></span></td>
						<td class="borderRight"><span class="iap-default"><xsl:value-of select="./IAPDEPARTFROM" /></span></td>
						<td class="borderRight"><span class="iap-default"><xsl:value-of select="./IAPARRIVETO" /></span></td>
						<td class="borderRight"><div class="right"><xsl:value-of select="format-number($moneyamount, '###,###,###,###')" /></div></td>
						<td class="noBorderRight"><span class="iap-default">
						<xsl:choose>
							<xsl:when test="@class='0001'">無し</xsl:when>
							<xsl:when test="@class='0002'">有り</xsl:when>
							<xsl:otherwise />
						</xsl:choose>
						</span>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		<xsl:apply-templates select="IAPDETAILS-LIST" />
	</table>
	</div>
</xsl:template>

<!--// 交通費・仮払い金額表示のテンプレート //-->
<xsl:template match="IAPTAIL">
	<div class="contents margin">
	<table class="detail">
		<xsl:variable name="temppayment" select="number(./IAPTEMPPAYMENT)" />
		<xsl:variable name="adjustamount" select="number(./IAPADJUSTAMOUNT)" />
		<xsl:if test="$temppayment > 0">
			<tr>
				<td class="label">仮払金額</td>
				<td class="item"><span class="iap-default"><xsl:value-of select="format-number($temppayment, '###,###,###,###')" /></span></td>
			</tr>
		</xsl:if>
		<tr>
			<td class="label"><span class="iap-default">精算金額</span></td>
			<td class="item"><span class="iap-default"><xsl:value-of select="format-number($adjustamount, '###,###,###,###')" /></span></td>
		</tr>
	</table>
	</div>
</xsl:template>

<!--// 負担明細のテンプレート //-->
<xsl:template match="IAPBILLABLE">
	<div class="contents margin">
	<h2>費用の負担部門・プロジェクト</h2>
	<table class="dr-table rich-table">
		<tr class="rich-table-header">
			<td class="headBorderRight">負担金額</td>
			<td class="noBorderRight">部門／プロジェクト</td>
		</tr>
		<xsl:for-each select="./IAPBILLABLE-REC">
			<xsl:variable name="billableamount" select="number(./IAPBILLABLEAMOUNT)" />
			<xsl:choose>
				<xsl:when test="position() mod 2 = 1">
					<tr class="oddRow">
						<td class="borderRight"><div class="right"><xsl:value-of select="format-number($billableamount, '###,###,###,###')" /></div></td>
						<td class="noBorderRight"><span class="iap-default"><xsl:value-of select="./IAPBILLABLEPROJECT" /><xsl:value-of select="./IAPBILLABLEDEPTNAME" /></span></td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr class="evenRow">
						<td class="borderRight"><div class="right"><xsl:value-of select="format-number($billableamount, '###,###,###,###')" /></div></td>
						<td class="noBorderRight"><span class="iap-default"><xsl:value-of select="./IAPBILLABLEPROJECT" /><xsl:value-of select="./IAPBILLABLEDEPTNAME" /></span></td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</table>
	</div>
</xsl:template>

<!--// 明細一覧部 //-->
<xsl:template match="IAPDETAILS-LIST">
	<xsl:variable name="moneyamountsum" select="sum(.//IAPMONEYAMOUNT)" />
	<tr>
		<td colspan="4" class="headerBg borderRight"><span class="iap-default" nowrap="">合計金額</span></td>
		<td class="borderRight"><div class="right"><xsl:value-of select="format-number($moneyamountsum, '###,###,###,###')" /></div></td>
		<td class="headerBg noBorderRight"></td>
	</tr>
</xsl:template>
<!--// BRタグ対応 //-->
<xsl:template match="BR">
	<xsl:element name="br" />
</xsl:template>
<xsl:template match="br">
	<xsl:element name="br" />
</xsl:template>
</xsl:stylesheet>
