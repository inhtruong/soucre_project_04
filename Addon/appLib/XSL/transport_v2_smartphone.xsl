<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

	<xsl:template match="IAPHEADER">
		<span style="color:#FF0000">[申請内容]</span>
		<div>
			<span style="color:#FF0000">[訪問先]</span>
			<span><xsl:value-of select="./IAPVISITEDNAME" disable-output-escaping="yes"/><br/></span>
			<span style="color:#FF0000">[理由]</span>
			<span>
				<xsl:apply-templates select="./IAPOUTLINE" />
				<br />
			</span>
		</div>
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
	</xsl:template>

	<xsl:template match="IAPOUTLINE">
		<xsl:value-of disable-output-escaping="yes" select="." />
	</xsl:template>


	<xsl:template match="IAPDETAILS">
		<div class="contents margin">
				<xsl:for-each select="IAPDETAILS-REC">
					<span>交通費明細(<xsl:value-of select="position()"/>)</span><br/>
					<xsl:variable name="moneyamount" select="number(./IAPMONEYAMOUNT)" />
						<span style="color:#FF0000">[使用日]</span>
						<span><xsl:value-of select="./IAPPAIDDATE" /><br/></span>

						<span style="color:#FF0000">[交通機関]</span>
						<span><xsl:value-of select="./IAPTRANSPORTMODE" /><br/></span>

						<span style="color:#FF0000">[出発地]</span>
						<span><xsl:value-of select="./IAPDEPARTFROM" disable-output-escaping="yes"/><br/></span>

						<span style="color:#FF0000">[到着地]</span>
						<span><xsl:value-of select="./IAPARRIVETO" disable-output-escaping="yes"/><br/></span>

						<span style="color:#FF0000">[金額]</span>
						<span><xsl:value-of select="format-number($moneyamount, '###,###,###,###')" /><br/></span>

						<span style="color:#FF0000">[領収書]</span>
						<span>
							<xsl:choose>
								<xsl:when test="@class='0001'">無<br/></xsl:when>
								<xsl:when test="@class='0002'">有<br/></xsl:when>
								<xsl:otherwise />
							</xsl:choose>
						</span>
						<br/>
				</xsl:for-each>
				<br />
				<xsl:variable name="moneyamountsum" select="sum(.//IAPMONEYAMOUNT)" />
				<span style="color:#FF0000">[交通費合計]</span>
				<xsl:value-of select="format-number($moneyamountsum,'###,###,###,###')" />
				<br />
		</div>
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
	</xsl:template>


	<xsl:template match="IAPBILLABLE">
		<div class="contents margin">
			<xsl:for-each select="IAPBILLABLE-REC">
				<span>費用の負担(<xsl:value-of select="position()"/>)</span><br/>
				<xsl:variable name="billableamount" select="number(./IAPBILLABLEAMOUNT)" />

						<span style="color:#FF0000">[負担部門またはプロジェクト]</span>
							<span>
							<xsl:value-of select="./IAPBILLABLETITLE" />
							<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;]]></xsl:text>
							<xsl:value-of select="./IAPBILLABLENAME" /><br/></span>
						<span style="color:#FF0000">[負担金額]</span>
							<span><xsl:value-of select="format-number($billableamount, '###,###,###,###')" /><br/></span>
						<br/>
			</xsl:for-each>
					<xsl:variable name="billableamountsum" select="sum(.//IAPBILLABLEAMOUNT)" />
					<span style="color:#FF0000">[負担合計]</span>
					<xsl:value-of select="format-number($billableamountsum,'###,###,###,###')" />

		</div>
	</xsl:template>

</xsl:stylesheet>
