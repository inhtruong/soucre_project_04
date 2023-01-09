<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" encoding="UTF-8" />
<xsl:template match="PROPOSE_CONTENT">
	<xsl:apply-templates/>
</xsl:template>
<xsl:template match="APPLICATIONTYPE">
	<b> <xsl:value-of select="."/> </b>
</xsl:template>
<xsl:template match="LOOP">
	<div class="contents margin">
	<table class="detail"> <xsl:apply-templates/> </table>
	</div>
</xsl:template>
<xsl:template match="APPLICATION">
	<tr>
		<td class="label">申請者 </td>
		<td class="item"><xsl:value-of select="."/></td>
	</tr>
</xsl:template>
<xsl:template match="DATE">
	<tr>
		<td class="label">日付</td>
		<td class="item"><xsl:value-of select="DAY"/><xsl:value-of select="DAYOFWEEK"/></td>
	</tr>
</xsl:template>
<xsl:template match="TIME">
	<tr>
		<td class="label">時刻</td>
		<td class="item"><xsl:value-of select="STARTTIME"/>～<xsl:value-of select="ENDTIME"/></td>
	</tr>
</xsl:template>
<xsl:template match="TYPE">
	<tr>
		<td class="label">種別</td>
		<td class="item"><xsl:value-of select="."/></td>
	</tr>
</xsl:template>
<xsl:template match="WORKHOUR">
	<tr>
		<td class="label">工数</td>
		<td class="item"><xsl:value-of select="."/></td>
	</tr>
</xsl:template>
<xsl:template match="REASON">
	<tr>
		<td class="label">理由</td>
		<td class="item"><xsl:value-of select="."/></td>
	</tr>
</xsl:template>
</xsl:stylesheet>

