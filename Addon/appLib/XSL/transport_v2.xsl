<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

	<!--// 交通費決裁内容表示 //-->
	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<!--// コメント表示 //-->
	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>

	<!--// 交通費情報のテンプレート //-->
	<xsl:template match="IAPHEADER">
		<div class="margin">
			<span class="iap-default">申請内容</span>
		</div>
		<div class="contents">
			<table BORDER="0" class="detail">
				<tr>
					<td class="label" nowrap="nowrap">訪問先</td>
					<td class="item"><span class="iap-default"><xsl:value-of disable-output-escaping="yes" select="./IAPVISITEDNAME" /><br /></span></td>
				</tr>
				<tr>
					<td class="label" nowrap="nowrap" valign="top">理由</td>
					<td class="item">
						<span class="iap-default">
							<xsl:apply-templates select="./IAPOUTLINE" />
							<br />
						</span>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>

	<!--// 交通費明細のテンプレート //-->
	<xsl:template match="IAPDETAILS">
		<div class="contents margin">
			<span class="iap-default">交通費明細</span>
			<table class="dr-table rich-table listHighlight" border="0" cellpadding="0" cellspacing="0">
				<tr class="dr-table-header rich-table-header">
					<th width="130px" class="dr-table-headercell rich-table-headercell headBorderRight" nowrap="">使用日</th>
					<th class="dr-table-headercell rich-table-headercell headBorderRight" nowrap="">交通機関</th>
					<th class="dr-table-headercell rich-table-headercell headBorderRight" nowrap="">出発地</th>
					<th class="dr-table-headercell rich-table-headercell headBorderRight" nowrap="">到着地</th>
					<th width="110px" class="dr-table-headercell rich-table-headercell headBorderRight" nowrap="">金額</th>
					<th width="60px" class="dr-table-headercell rich-table-headercell noBorderRight" nowrap="">領収書</th>
				</tr>
				<xsl:for-each select="IAPDETAILS-REC">
					<xsl:variable name="moneyamount" select="number(./IAPMONEYAMOUNT)" />
					<tr>
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="position() mod 2 = 1">dr-table-firstrow rich-table-firstrow oddRow</xsl:when>
								<xsl:otherwise>dr-table-firstrow rich-table-firstrow evenRow</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<td class="dr-table-cell rich-table-cell borderRight tdHighlight" width="130px">
							<span class="iap-default"><xsl:value-of select="./IAPPAIDDATE" /><br/></span>
						</td>
						<td class="dr-table-cell rich-table-cell borderRight tdHighlight">
							<span class="iap-default"><xsl:value-of select="./IAPTRANSPORTMODE" /><br/></span>
						</td>
						<td class="dr-table-cell rich-table-cell borderRight tdHighlight">
							<span class="iap-default"><xsl:value-of disable-output-escaping="yes" select="./IAPDEPARTFROM" /><br/></span>
						</td>
						<td class="dr-table-cell rich-table-cell borderRight tdHighlight">
							<span class="iap-default"><xsl:value-of disable-output-escaping="yes" select="./IAPARRIVETO" /><br/></span>
						</td>
						<td class="dr-table-cell rich-table-cell borderRight tdHighlight" style="text-align:right" width="110px">
							<span class="iap-default"><xsl:value-of select="format-number($moneyamount, '###,###,###,###')" /><br/></span>
						</td>
						<td class="dr-table-cell rich-table-cell noBorderRight tdHighlight" width="60px">
							<span class="iap-default">
								<xsl:choose>
									<xsl:when test="@class='0001'">無</xsl:when>
									<xsl:when test="@class='0002'">有</xsl:when>
									<xsl:otherwise />
								</xsl:choose>
							</span>
						</td>
					</tr>
				</xsl:for-each>
				<tr class="dr-table-footer rich-table-footer">
					<xsl:variable name="moneyamountsum" select="sum(.//IAPMONEYAMOUNT)" />
					<td class="dr-table-footercell rich-table-footercell headerBg borderRight" colspan="4">交通費合計</td>
					<td style="text-align: right; background-color:white"
							class="rich-table-footercell dr-table-footercell borderRight" width="110px">
						<xsl:value-of select="format-number($moneyamountsum,'###,###,###,###')" />
					</td>
					<td width="60px" class="dr-table-footercell rich-table-footercell noBorderRight" style="background-color:white"><br /></td>
				</tr>
			</table>
		</div>
	</xsl:template>

	<xsl:template match="IAPOUTLINE"><!-- 理由 -->
		<xsl:value-of disable-output-escaping="yes" select="." />
	</xsl:template>

	<!--// 負担明細のテンプレート //-->
	<xsl:template match="IAPBILLABLE">
		<div class="contents margin">
			<span class="iap-default">費用の負担部門・プロジェクト</span>
			<table class="dr-table rich-table listHighlight" border="0" cellpadding="0" cellspacing="0">
				<tr class="dr-table-header rich-table-header">
					<th class="dr-table-headercell rich-table-headercell headBorderRight" colspan="2" nowrap="nowrap">負担部門またはプロジェクト</th>
					<th width="181" class="dr-table-headercell rich-table-headercell noBorderRight" nowrap="nowrap">負担金額</th>
				</tr>
				<xsl:for-each select="IAPBILLABLE-REC">
					<xsl:variable name="billableamount" select="number(./IAPBILLABLEAMOUNT)" />
					<tr>
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="position() mod 2 = 1">dr-table-firstrow rich-table-firstrow oddRow</xsl:when>
								<xsl:otherwise>dr-table-firstrow rich-table-firstrow evenRow</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<td width="110px" class="dr-table-cell rich-table-cell borderRight tdHighlight">
							<span class="iap-default"><xsl:value-of select="./IAPBILLABLETITLE" /><br/></span>
						</td>
						<td class="dr-table-cell rich-table-cell borderRight tdHighlight">
							<span class="iap-default"><xsl:value-of select="./IAPBILLABLENAME" /><br/></span>
						</td>
						<td width="181px" class="dr-table-cell rich-table-cell noBorderRight tdHighlight" style="text-align:right" nowrap="nowrap">
							<span class="iap-default"><xsl:value-of select="format-number($billableamount, '###,###,###,###')" /><br/></span>
						</td>
					</tr>
				</xsl:for-each>
				<tr class="dr-table-footer rich-table-footer">
					<xsl:variable name="billableamountsum" select="sum(.//IAPBILLABLEAMOUNT)" />
					<td class="dr-table-footercell rich-table-footercell headerBg borderRight" colspan="2">負担合計</td>
					<td style="text-align: right; background-color:white"
							class="dr-table-footercell rich-table-footercell noBorderRight" width="181px">
						<xsl:value-of select="format-number($billableamountsum,'###,###,###,###')" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
</xsl:stylesheet>
