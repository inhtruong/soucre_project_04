<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output  method="xml" omit-xml-declaration="yes" encoding="UTF-8" />

<xsl:template match="PROPOSE_CONTENT">
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="APPLICATIONTYPE">
 <b> <xsl:value-of select="."/> </b>  
</xsl:template> 

<xsl:template match="LOOP">
<xsl:text disable-output-escaping="yes"></xsl:text> 
	<div class="contents margin">
<table class="detail"> <xsl:apply-templates/> </table> 
 	</div>
</xsl:template> 

<xsl:template match="APPLICATIONTYPE">
 <b> <xsl:value-of select="."/></b> 
</xsl:template> 

<xsl:template match="APPLICATION">
<tr>
<td class="label">申請者 </td>
<td class="item"><xsl:value-of select="."/></td>
</tr>
</xsl:template>

<xsl:template match="VACATIONDAY">
<tr>
<td class="label"><xsl:value-of select="NAME"/></td>
<td class="item"><xsl:value-of select="DAY"/></td>
</tr>
</xsl:template>

<xsl:template match="NUMOFDAY">
<tr>
<td class="label">日数</td>
<td class="item"><xsl:value-of select="."/></td>
</tr>
</xsl:template>

<xsl:template match="REASON">
<tr>
<td class="label">理由</td>
<td class="item"><xsl:value-of select="."/></td>
</tr>
</xsl:template>

<xsl:template match="CONTACT">
<tr>
<td class="label">連絡先</td>
<td class="item"><xsl:value-of select="CONTACTHOME"/><xsl:text disable-output-escaping="yes">&lt;br /&gt;</xsl:text>TEL:<xsl:value-of select="CONTACTTEL"/><xsl:value-of select="CONTACT"/></td>
</tr>
</xsl:template>

</xsl:stylesheet>
