<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

	<!-- ********************************************************************** -->
	<!--		精算処理：接待精算　スタイルシート								-->
	<!-- ********************************************************************** -->

	<!-- ***** ボディ ***** -->
	<xsl:template match="DOCUMENT">
		<span style="color:#FF0000">[申請内容]</span>
		<br/>
		<span style="color:#FF0000">[接待日]</span>
		<span><xsl:value-of select="./ENTERTAININGDATE" /></span><br/>
			<xsl:apply-templates select="./CUSTOMERS" />
			<xsl:apply-templates select="./NECESSITY" />
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
		<xsl:apply-templates select="./ENTERTAININGDETAILS" />

		<xsl:apply-templates select="./TEMPPAYMENT" />

		<xsl:apply-templates select="./ACCOUNTINGTOTAL"/>

		<xsl:apply-templates select="./BILLABLEDETAILS" />
		<br/>
	</xsl:template>

	<xsl:template match="CUSTOMERS"><!-- 接待先 -->
		<span style="color:#FF0000">[接待先]</span>
		<xsl:choose>
			<xsl:when
				test="(./CUSTCORPNAME/text() != '') or (./CUSTDEPTNAME/text() != '') or (./CUSTEMPNAME/text() != '')">
				<xsl:if test="string-length(./CUSTCORPNAME/text())>0">
					<xsl:value-of select="./CUSTCORPNAME" disable-output-escaping="yes"/>
					<br/>
				</xsl:if>
				<xsl:if test="string-length(./CUSTDEPTNAME/text())>0">
					<xsl:value-of select="./CUSTDEPTNAME" disable-output-escaping="yes"/>
					<br/>
				</xsl:if>
				<xsl:if test="string-length(./CUSTEMPNAME/text())>0">
					<xsl:value-of select="./CUSTEMPNAME" disable-output-escaping="yes"/>
					<br/>
				</xsl:if>	
			</xsl:when>
		</xsl:choose>
		<xsl:if test="string-length(./OTHERCUST/text())>0">
			<span><xsl:value-of select="./OTHERCUST" disable-output-escaping="yes"/></span><!-- その他接待先 -->
			<br/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="NECESSITY">
		<span style="color:#FF0000">[理由]</span>
		<span>
			<xsl:value-of disable-output-escaping="yes" select="." />
		</span>
		<br/>
	</xsl:template>

	<xsl:template match="NOTE">
		<span>
			<xsl:value-of disable-output-escaping="yes" select="." />
		</span>
		<br/>
	</xsl:template>

	<xsl:template match="ENTERTAININGDETAILS">
		<xsl:variable name="entertainType" select="./@type"/>
		<xsl:variable name="totalAmount"
			select="sum(.//ENTERTAININGAMOUNT)" />
			<xsl:for-each select="ENTERTAININGDETAIL">
				<span>
					接待明細(<xsl:value-of select="position()"/>)
				</span>
				<br/>
				<span style="color:#FF0000">[接待種類]</span>
				<span>
					<xsl:value-of select="./ENTERTAININGTYPE" />
				</span><br/>
				<span style="color:#FF0000">[接待先会社名・ご担当者]</span>
				<span>
					<xsl:value-of disable-output-escaping="yes" select="./ENTERTAININGPLACE" />
				</span><br/>
				<span style="color:#FF0000">[接待先人数]</span>
				<span>
					<xsl:value-of select="./ATTENDERCOUNT" />名
				</span><br/>
				<span style="color:#FF0000">[当社出席者]</span>
				<span>
					<xsl:apply-templates select="EMPLIST" />
				</span><br/>
				<span style="color:#FF0000">[当社出席人数]</span>
				<span>
					<xsl:value-of select="./EMPATTENDERCOUNT" />名
				</span><br/>
				<span style="color:#FF0000">[備考]</span>
				<xsl:apply-templates select="./NOTE" />

				<span style="color:#FF0000">[接待場所／品目]</span>
				<span>
					<xsl:value-of select="./ENTERTAININGITEM" disable-output-escaping="yes"/>
				</span><br/>
				<span style="color:#FF0000">[税込金額]</span>
				<span>
					<xsl:value-of select="./ENTERTAININGAMOUNTNAME" />
				</span><br/>
				<xsl:if test="$entertainType = 'account'">
					<!-- for 接待精算 -->
					<span style="color:#FF0000">[支払状況]</span>
					<span>
						<xsl:value-of select="./PAYMENTSITUATION" />
					</span>
					<br/>
					<span style="color:#FF0000">[経理担当者への連絡事項]</span>
					<span>
					        <xsl:value-of disable-output-escaping="yes" select="./CONTACTACCOUNTINGPERSON" />
					</span>
					<br/>
    				</xsl:if>
				<br/>
				<br/>
			</xsl:for-each>
			<xsl:if test="$entertainType = 'input'">
				<span style="color:#FF0000">[合計金額]</span>
				<span>
					<xsl:value-of select="format-number($totalAmount,'###,###,###,###')" />
				</span>
				<br/>
			</xsl:if>
	</xsl:template>

	<xsl:template match="EMPLIST">
		<xsl:for-each select="EMPDETAIL">
			<xsl:if test="position() > 1">,</xsl:if>
			<xsl:value-of select="EMPNAME"/>
		</xsl:for-each>
		<br/>
		<span>
			<xsl:value-of select="./ORTHERATTEND" disable-output-escaping="yes"/>
		</span>
	</xsl:template>

	<xsl:template match="TEMPPAYMENT">
		<xsl:variable name="tempayAmount_Apply" select="number(./TEMPPAYMONEYAMOUNT)"/>
		<xsl:if test="$tempayAmount_Apply > 0">
			<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
			<span>仮払い</span>
			<br/>
			<span style="color:#FF0000">[仮払金額]</span>
			<span>
				<xsl:value-of select="format-number($tempayAmount_Apply,'###,###,###,###')" />
			</span>
			<br/>
			<span style="color:#FF0000">[口座振込]</span>
			<span>
				<xsl:value-of select="./TRANSFERNECESSITYCLASS" />
			</span>
		</xsl:if>
	</xsl:template>
	<xsl:template match="ACCOUNTINGTOTAL">
		<xsl:variable name="tempayAmount"
					select="number(./TEMPPAYMONEYAMOUNT)"/>
		<xsl:variable name="totalAmount"
					select="number(./TOTALMONEYAMOUNT)"/>
		<xsl:variable name="prepaidAmount"
					select="number(./PREPAIDMONEYAMOUNT)"/>
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
		<span style="color:#FF0000">[接待金額]</span>
		<span>
			<xsl:value-of select="format-number($totalAmount,'###,###,###,###')" />
		</span>
		<br/>
		<span style="color:#FF0000">[立替金額]</span>
		<span>
			<xsl:value-of select="format-number($prepaidAmount,'###,###,###,###')" />
		</span>
		<br/>
		<span style="color:#FF0000">[仮払金額]</span>
		<span>
			<xsl:value-of select="format-number($tempayAmount,'###,###,###,###')" />
		</span>
		<br/>
		<span style="color:#FF0000">[精算金額]</span>
		<span>
			<xsl:value-of select="format-number($prepaidAmount - $tempayAmount,'###,###,###,###')" />
		</span>
		<br/>
	</xsl:template>
	<xsl:template match="BILLABLEDETAILS">
		<xsl:variable name="billCount" select="count(./BILLABLEDETAIL)" />
		<xsl:if test="$billCount &gt; 0">
			<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
			<xsl:variable name="totalbillableamount"
				select="sum(.//BILLABLEAMOUNT)" />
			<xsl:for-each select="BILLABLEDETAIL">
				<span>費用の負担(<xsl:value-of select="position()"/>)</span>
				<br/>
				<xsl:variable name="billableamount" select="number(./BILLABLEAMOUNT)" />
				<span style="color:#FF0000">[負担部門またはプロジェクト]</span>
				<span>
					<xsl:value-of select="./BILLABLENAME" />
				</span>
				<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;]]></xsl:text>
				<span>
					<xsl:value-of select="./BILLABLEDETAILNAME" />
				</span>
				<br/>
				<span style="color:#FF0000">[負担金額]</span>
				<span>
					<xsl:value-of select="format-number($billableamount,'###,###,###,###')" />
				</span>
				<br/><br/>
			</xsl:for-each>
			<span style="color:#FF0000">[負担合計]</span>
			<span>
				<xsl:value-of select="format-number($totalbillableamount,'###,###,###,###')" />
			</span>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
