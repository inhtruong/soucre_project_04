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
						&#160;
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" class="label">
						<span class="iap-default">件名</span>
					</td>
					<td class="item">
						<span class="iap-default">
							<xsl:apply-templates select="./SUBJECT" />
						</span>
						&#160;
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
						&#160;
					</td>
				</tr>
			</table>
		</div>
		&#160;
		<div class="contents">
			<span class="iap-default">購入明細</span>
			<xsl:apply-templates select="./PURCHASEDETAILS" /><!-- 購入明細入力 -->
		</div>
		&#160;
		<xsl:variable name="tempayAmount"
					select="number(./TEMPPAYMONEYAMOUNT)">
		</xsl:variable>
		<xsl:if test="$tempayAmount != 0">
			<div class="contents">
				<span class="iap-default">仮払い</span>
				<table class="detail">
					<tr>
						<td class="label">現金仮払金額</td>
						<td class="item">
							<span class="iap-default">
								<xsl:value-of
									select="format-number($tempayAmount,'###,###,###,###')" />
							</span>
							&#160;
						</td>
					</tr>

					<tr>
						<td class="label">口座振込</td>
						<td class="item">
							<span class="iap-default">
								<xsl:value-of select="./TRANSFERNECESSITYCLASS" />
							</span>
							&#160;
						</td>
					</tr>
				</table>
			</div>
		</xsl:if>
		<xsl:apply-templates select="./BILLABLEDETAILS" />
	</xsl:template>

	<xsl:template match="SUBJECT"><!-- 件名 -->
		<xsl:value-of disable-output-escaping="yes" select="." />
	</xsl:template>

	<xsl:template match="NECESSITY"><!-- 理由 -->
		<xsl:value-of disable-output-escaping="yes" select="." />
	</xsl:template>

	<xsl:template match="PURCHASEDETAILS"><!-- 購入明細 -->
		<table class="dr-table rich-table listHighlight">
		<thead>
			<tr class="rich-table-header">
				<td width="auto" class="headBorderRight">購入種類</td>
				<td width="auto" class="headBorderRight">購入先</td>
				<td width="auto" class="headBorderRight">購入品名</td>
				<td width="auto" class="rich-table-headercell headBorderRight">購入金額</td>
				<td width="auto" class="rich-table-headercell noBorderRight">課税区分</td>
			</tr>
		</thead>
			<xsl:variable name="totalamount" select="sum(.//PRICE)" />
			<xsl:for-each select="PURCHASEDETAIL">
				<xsl:variable name="price" select="number(./PRICE)" />
				<tr>
					<xsl:attribute name="class">
						<xsl:choose>
							<xsl:when test="position() mod 2 = 1">oddRow</xsl:when>
							<xsl:otherwise>evenRow</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
					<td class="borderRight tdHighlight">
						<span class="iap-default">
							<xsl:value-of select="./PURCHASETYPENAME" />
						</span>
						&#160;
					</td>
					<td class="borderRight tdHighlight">
						<span class="iap-default">
							<xsl:value-of select="./RETAILERNAME" />
						</span>
						&#160;
					</td>
					<td class="borderRight tdHighlight">
						<span class="iap-default">
							<xsl:value-of select="./ITEMNAME" />
						</span>
						&#160;
					</td>
					<td style="text-align: right" class="borderRight tdHighlight">
							<xsl:value-of select="format-number($price,'###,###,###,###')" />
					</td>
					<td class="noBorderRight tdHighlight">
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
						&#160;
					</td>
				</tr>
			</xsl:for-each>
			<tr class="dr-table-footer rich-table-footer">
				<td class="rich-table-footercell headerBg borderRight" colspan="3">購入合計</td>
				<td style="text-align:right;background-color:white" class="rich-table-footercell borderRight">
						<xsl:value-of select="format-number($totalamount,'###,###,###,###')" />
				</td>
				<td style="background-color:white" >
					&#160;
				</td>
			</tr>
		</table>

	</xsl:template>
	<xsl:template match="BILLABLEDETAILS">
		<xsl:variable name="billCount" select="count(./BILLABLEDETAIL)" />
		<xsl:if test="$billCount &gt; 0">
			<div class="contents margin">
				<span class="iap-default">費用の負担部門・プロジェクト</span>
				<table class="dr-table rich-table listHighlight">
					<tr class="rich-table-header">
						<td colspan="2" width="auto"
							class="headBorderRight">
							負担部門またはプロジェクト
						</td>
						<td width="auto" class="noBorderRight">負担金額</td>
					</tr>
					<xsl:variable name="totalbillableamount"
						select="sum(.//BILLABLEAMOUNT)" />
					<xsl:for-each select="BILLABLEDETAIL">
						<xsl:variable name="billableamount"
							select="number(./BILLABLEAMOUNT)" />
						<tr>
							<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="position() mod 2 = 1">oddRow</xsl:when>
								<xsl:otherwise>evenRow</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
							<td class="borderRight tdHighlight">
								<span class="iap-default">
									<xsl:value-of
										select="./BILLABLENAME" />
								</span>
								&#160;
							</td>
							<td class="borderRight tdHighlight">
								<span class="iap-default">
									<xsl:value-of
										select="./BILLABLEDETAILNAME" />
								</span>
								&#160;
							</td>
							<td style="text-align: right"
								class="noBorderRight tdHighlight">
								<xsl:value-of
									select="format-number($billableamount,'###,###,###,###')" />
							</td>
						</tr>
					</xsl:for-each>
					<tr class="dr-table-footer rich-table-footer">
				<td class="headerBg borderRight" colspan="2">負担合計</td>
				<td style="text-align: right; background-color: white;" class="noBorderRight">
						<xsl:value-of select="format-number($totalbillableamount,'###,###,###,###')" />
				</td>
			</tr>
				</table>
			</div>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>