<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
	<xsl:template match="/">
		<div class="contents">
			<xsl:copy-of select="*"></xsl:copy-of>
		</div>
	</xsl:template>
</xsl:stylesheet>
