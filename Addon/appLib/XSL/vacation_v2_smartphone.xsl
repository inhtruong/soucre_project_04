<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
	<!-- ********************************************************************** -->
	<!--		休暇申請　スタイルシート							-->
	<!-- ********************************************************************** -->
	<xsl:decimal-format name="myFormat" NaN="0" zero-digit="0"/>
	<!-- ***** ボディ ***** -->
	<xsl:template match="DOCUMENT">
		<div class="contents">
			<span style="color:#FF0000">[休暇種別]</span>
				<span>
					<xsl:value-of select="./VACATIONTYPE" />
				</span>
			<br/>
			<span style="color:#FF0000">[開始日]</span>
				<span><xsl:value-of select="./STARTDATETIME" /></span><br/>
			<span style="color:#FF0000">[終了日]</span>
				<span><xsl:value-of select="./ENDDATETIME" /></span><br/>
			<span style="color:#FF0000">[日数]</span>
				<span><xsl:value-of select="./NOOFDAYS" /></span><br/>
			<span style="color:#FF0000">[連絡先]</span>
				<span><xsl:value-of select="./CONTACT" /></span><br/>
			<span style="color:#FF0000">[連絡先TEL]</span>
				<xsl:if test="./CONTACTTEL != ''">
					<xsl:element name="a">
						<xsl:attribute name="href">tel:<xsl:value-of select="./CONTACTTEL" /></xsl:attribute>
						<span><xsl:value-of select="./CONTACTTEL" /></span>
					</xsl:element>
				</xsl:if>
				<br/>
			<span style="color:#FF0000">[理由]</span><br/>
				<span class="iap-default">
					<xsl:value-of disable-output-escaping="yes" select="./REASON" />
				</span>
		</div>
	</xsl:template>

</xsl:stylesheet>
