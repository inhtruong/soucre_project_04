<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
	<!-- ********************************************************************** -->
	<!--		精算処理系：購入申請　スタイルシート							-->
	<!-- ********************************************************************** -->

	<!-- ***** ボディ ***** -->
	<xsl:template match="DOCUMENT">
		<span style="color:#FF0000">[申請内容]</span>
		<div class="contents">
			<span style="color:#FF0000">[購入日]</span>
			<span>
				<xsl:value-of select="./PURCHASEDATE" />
			</span>
			<br/>
			<span style="color:#FF0000">[理由]</span>
			<span>
				<xsl:apply-templates select="./NECESSITY" />
			</span>
		</div>

		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />

		<div class="contents margin">
			<span>購入明細</span>
			<xsl:apply-templates select="./PURCHASEDETAILS" /><!-- 購入明細入力 -->
		</div>

		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />

		<div class="contents margin">
			<xsl:variable name="totalPurchaseAmount"
				select="number(./TOTALPURCHASEAMOUNT)">
			</xsl:variable>

			<span style="color:#FF0000">[立替金額]</span>
			<span>
				<xsl:value-of
					select="format-number($totalPurchaseAmount,'###,###,###,###')" />
			</span>
			<br />

			<xsl:variable name="tempayAmount"
				select="number(./TEMPPAYMONEYAMOUNT)">
			</xsl:variable>
			<span style="color:#FF0000">[仮払金額]</span>
			<span>
				<xsl:value-of
					select="format-number($tempayAmount,'###,###,###,###')" />
			</span>
			<br />
			<xsl:variable name="purchaseMoneyAmount"
				select="number(./PURCHASEMONEYAMOUNT)">
			</xsl:variable>
			<span style="color:#FF0000">[精算金額]</span>
			<span>
				<xsl:value-of
					select="format-number($purchaseMoneyAmount,'###,###,###,###')" />
			</span>
			<br />
		</div>
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
		<xsl:apply-templates select="./BILLABLEDETAILS" />
	</xsl:template>

	<xsl:template match="NECESSITY"><!-- 理由 -->
		<xsl:value-of disable-output-escaping="yes" select="." />
	</xsl:template>

	<xsl:template match="PURCHASEDETAILS"><!-- 購入明細 -->
		<xsl:variable name="totalamount" select="sum(.//PRICE)" />
		<xsl:for-each select="PURCHASEDETAIL">
			<xsl:variable name="price" select="number(./PRICE)" />
			<span>購入明細(<xsl:value-of select="position()"/>)</span><br/>
			<span style="color:#FF0000">[購入種類]</span>
			<span>
				<xsl:value-of select="./PURCHASETYPENAME" />
			</span>
			<br />
			<span style="color:#FF0000">[購入先]</span>
			<span>
				<xsl:value-of select="./RETAILERNAME" disable-output-escaping="yes"/>
			</span>
			<br />
			<span style="color:#FF0000">[購入品名]</span>
			<span>
				<xsl:value-of select="./ITEMNAME" disable-output-escaping="yes"/>
			</span>
			<br />
			<span style="color:#FF0000">[購入金額]</span>
				<xsl:value-of select="format-number($price,'###,###,###,###')" />
			<br />
			<span style="color:#FF0000">[課税区分]</span>
			<span>
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
			<span style="color:#FF0000">[支払]</span>
			<span>
				<xsl:value-of select="./PAYMENTSTATUS" />
			</span>
			<br />
			<span style="color:#FF0000">[連絡事項]</span>
			<span>
				<xsl:value-of select="./NOTE" disable-output-escaping="yes"/>
			</span>
			<br /><br />
		</xsl:for-each>

		<span style="color:#FF0000">[購入合計]</span>
		<span>
			<xsl:value-of select="format-number($totalamount,'###,###,###,###')" />
		</span>
		<br />
	</xsl:template>
	<xsl:template match="BILLABLEDETAILS">
		<xsl:variable name="billCount" select="count(./BILLABLEDETAIL)"/>
		<xsl:if test="$billCount &gt; 0">
		<div class="contents">
			<xsl:variable name="totalbillableamount" select="sum(.//BILLABLEAMOUNT)" />
			<xsl:for-each select="BILLABLEDETAIL">
				<xsl:variable name="billableamount" select="number(./BILLABLEAMOUNT)" />
				<span>費用の負担(<xsl:value-of select="position()"/>)</span><br/>
					<span style="color:#FF0000">[負担部門またはプロジェクト]</span>
					<span>
						<xsl:value-of select="./BILLABLENAME" />
					</span>
					<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;]]></xsl:text>
					<span>
						<xsl:value-of select="./BILLABLEDETAILNAME" />
					</span>
					<br />
					<span style="color:#FF0000">[負担金額]</span>
					<span>
						<xsl:value-of select="format-number($billableamount,'###,###,###,###')" />
					</span>
					<br /><br />
			</xsl:for-each>
			<span style="color:#FF0000">[負担合計]</span>
			<span>
				<xsl:value-of select="format-number($totalbillableamount, '###,###,###,###')" />
			</span>
			<br />
		</div>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>