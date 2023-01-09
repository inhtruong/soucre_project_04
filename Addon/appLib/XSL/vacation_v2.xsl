<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
	<!-- ********************************************************************** -->
	<!--		勤怠：休暇申請　スタイルシート	    						-->
	<!-- ********************************************************************** -->
	<xsl:decimal-format name="myFormat" NaN="0" zero-digit="0"/>
	<!-- ***** ボディ ***** -->
	<xsl:template match="DOCUMENT">
		<span class="iap-default">申請内容</span>
		<div class="contents">
			<table class="detail">
				<tr>
					<td class="label">休暇種別</td>
					<td class="item"><xsl:value-of select="./VACATIONTYPE" /></td>
				</tr>
				<tr>
					<td class="label">開始日</td>
					<td class="item"><xsl:value-of select="./STARTDATETIME" /></td>
				</tr>
				<tr>
					<td class="label">終了日</td>
					<td class="item"><xsl:value-of select="./ENDDATETIME" /></td>
				</tr>
				<tr>
					<td class="label">日数</td>
					<td class="item"><xsl:value-of select="./NOOFDAYS" /></td>
				</tr>
				<tr>
					<td class="label">連絡先</td>
					<td class="item"><xsl:value-of select="./CONTACT" /></td>
				</tr>
				<tr>
					<td class="label">連絡先TEL</td>
					<td class="item"><xsl:value-of select="./CONTACTTEL" /></td>
				</tr>
				<tr>
					<td class="label">理由</td>
					<td class="item">
						<xsl:value-of disable-output-escaping="yes" select="./REASON" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
</xsl:stylesheet>
