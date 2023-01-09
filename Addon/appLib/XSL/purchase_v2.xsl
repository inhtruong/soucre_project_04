<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
	<!-- ********************************************************************** -->
	<!--		精算処理系：購入申請　スタイルシート							-->
	<!-- ********************************************************************** -->

	<!-- ***** ボディ ***** -->
	<xsl:template match="DOCUMENT">
		<span class="iap-default">申請内容</span>
		<div class="contents">
			<table border="0" class="detail">
				<tr>
					<td nowrap="nowrap" class="label">
						<span class="iap-default">購入日</span>
					</td>
					<td class="item">
						<span class="iap-default">
							<xsl:value-of select="./PURCHASEDATE" />
						</span>
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="label">
						<span class="iap-default">理由</span>
					</td>
					<td class="item">
						<span class="iap-default">
							<xsl:apply-templates select="./NECESSITY" />
						</span>
					</td>
				</tr>
			</table>
		</div>
		<div class="contents margin">
			<span class="iap-default">購入明細</span>
			<xsl:apply-templates select="./PURCHASEDETAILS" /><!-- 購入明細入力 -->
		</div>
		<div class="contents margin">
			<table class="detail">
				<xsl:variable name="totalPurchaseAmount"
					select="number(./TOTALPURCHASEAMOUNT)">
				</xsl:variable>
				<tr>
					<td class="label">立替金額</td>
					<td class="item">
						<span class="iap-default">
							<xsl:value-of
								select="format-number($totalPurchaseAmount,'###,###,###,###')" />
						</span>
						<br />
					</td>
				</tr>

				<xsl:variable name="tempayAmount"
					select="number(./TEMPPAYMONEYAMOUNT)">
				</xsl:variable>
				<tr>
					<td class="label">仮払金額</td>
					<td class="item">
						<span class="iap-default">
							<xsl:value-of
								select="format-number($tempayAmount,'###,###,###,###')" />
						</span>
						<br />
					</td>
				</tr>

				<xsl:variable name="purchaseMoneyAmount"
					select="number(./PURCHASEMONEYAMOUNT)">
				</xsl:variable>
				<tr>
					<td class="label">精算金額</td>
					<td class="item">
						<span class="iap-default">
							<xsl:value-of
								select="format-number($purchaseMoneyAmount,'###,###,###,###')" />
						</span>
						<br />
					</td>
				</tr>
			</table>
		</div>
		<xsl:apply-templates select="./BILLABLEDETAILS" />
	</xsl:template>

	<xsl:template match="NECESSITY"><!-- 理由 -->
			<xsl:value-of disable-output-escaping="yes" select="." />
	</xsl:template>

	<xsl:template match="PURCHASEDETAILS"><!-- 購入明細 -->
		<table class="dr-table rich-table listHighlight">
		<thead>
			<tr class="rich-table-header">
				<td width="auto" class="headBorderRight" rowspan="2">購入種類</td>
				<td width="auto" class="headBorderRight headBorderBottom">購入先</td>
				<td width="auto" class="headBorderRight" rowspan="2">購入金額</td>
				<td width="auto" class="headBorderRight" rowspan="2">課税区分</td>
				<td width="auto" class="headBorderRight" rowspan="2">支払</td>
				<td width="auto" class="noBorderRight" rowspan="2">連絡事項</td>
			</tr>
			<tr class="rich-table-header">
				<td width="auto" class="headBorderRight">購入品名</td>
			</tr>
		</thead>
			<xsl:variable name="totalamount" select="sum(.//PRICE)" />
			<xsl:for-each select="PURCHASEDETAIL">
			<tbody class="rf-cst">
				<xsl:variable name="price" select="number(./PRICE)" />
				<tr>
					<xsl:attribute name="class">
						<xsl:choose>
							<xsl:when test="position() mod 2 = 1">oddRow</xsl:when>
							<xsl:otherwise>evenRow</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
					<td class="borderRight" rowspan="2">
						<span class="iap-default">
							<xsl:value-of select="./PURCHASETYPENAME" />
						</span>
						<br />
					</td>
					<td class="borderRight borderBottom" >
						<span class="iap-default">
							<xsl:value-of disable-output-escaping="yes" select="./RETAILERNAME" />
						</span>
						<br />
					</td>
					<td style="text-align: right" class="borderRight" rowspan="2">
							<xsl:value-of select="format-number($price,'###,###,###,###')" />
						<br />
					</td>
					<td class="borderRight" rowspan="2">
						<span class="iap-default">
							<xsl:choose>
								<xsl:when test="@class='0002'">
									課税（
									<xsl:value-of select="./CONSUMPTIONTAXTAXATION" />
									）
								</xsl:when>
								<xsl:when test="@class='0003'">
									非課税
								</xsl:when>
							</xsl:choose>
						</span>
						<br />
					</td>
					<td style="text-align: center" class="borderRight" rowspan="2">
						<span class="iap-default">
							<xsl:value-of select="./PAYMENTSTATUS" />
						</span>
						<br />
					</td>
					<td style="text-align: left" class="noBorderRight" rowspan="2">
						<span class="iap-default">
							<xsl:value-of disable-output-escaping="yes" select="./NOTE" />
						</span>
					</td>
				</tr>
				<tr>
					<xsl:attribute name="class">
						<xsl:choose>
							<xsl:when test="position() mod 2 = 1">oddRow</xsl:when>
							<xsl:otherwise>evenRow</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
					<td class="borderRight">
						<span class="iap-default">
							<xsl:value-of disable-output-escaping="yes" select="./ITEMNAME" />
						</span>
						<br />
					</td>
				</tr>
				</tbody>
			</xsl:for-each>
			<tr>
				<td class="headerBg borderRight" colspan="2" style="text-align: left">購入合計</td>
				<td style="text-align: right;background-color:white" class="borderRight" >
						<xsl:value-of select="format-number($totalamount,'###,###,###,###')" />
				</td>
				<td colspan="3" class="noBorderRight" style="background-color:white">
					<br />
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="BILLABLEDETAILS">
		<xsl:variable name="billCount" select="count(./BILLABLEDETAIL)"/>
		<xsl:if test="$billCount &gt; 0">
		<div class="margin contents">
				<span class="iap-default">費用の負担部門・プロジェクト</span>
				<table class="dr-table rich-table listHighlight">
					<tr class="rich-table-header">
						<td colspan="2"  class="rich-table-headercell headBorderRight">負担部門またはプロジェクト</td>
						<td class="rich-table-headercell noBorderRight">負担金額</td>
					</tr>
				<xsl:variable name="totalbillableamount" select="sum(.//BILLABLEAMOUNT)" />
				<xsl:for-each select="BILLABLEDETAIL">
					<xsl:variable name="billableamount" select="number(./BILLABLEAMOUNT)" />
					<tr>
						<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="position() mod 2 = 1">oddRow</xsl:when>
								<xsl:otherwise>evenRow</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
						<td class="borderRight tdHighlight">
							<span class="iap-default">
								<xsl:value-of select="./BILLABLENAME" />
							</span>
							<br />
						</td>
						<td class="borderRight tdHighlight">
							<span class="iap-default">
								<xsl:value-of select="./BILLABLEDETAILNAME" />
							</span>
							<br />
						</td>
						<td style="text-align: right" class="noBorderRight tdHighlight">
								<xsl:value-of
									select="format-number($billableamount,'###,###,###,###')" />
								<br />
						</td>
					</tr>
				</xsl:for-each>
				<tr >
					<td colspan="2" class="headerBg borderRight" style="text-align: left">負担合計</td>
					<td style="text-align: right; background-color: white;" class="noBorderRight">
						<xsl:value-of select="format-number($totalbillableamount, '###,###,###,###')" />
						<br />
					</td>
				</tr>

			</table>
			</div>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>