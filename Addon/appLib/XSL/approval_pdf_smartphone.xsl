<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes"
		encoding="utf-8" />
		
	<!-- PDF申請 -->
	<xsl:template match="DOCUMENT">
	
		<xsl:text disable-output-escaping="yes">
			&lt;iframe height="500" width="100%" src="assets/pdfjs/web/viewer.html?file=/pe4x/Download/</xsl:text>
		<xsl:value-of select="./PDFFILENAME" disable-output-escaping="yes" />
		<xsl:text disable-output-escaping="yes">?id%3D</xsl:text>
		<xsl:value-of select="./PDFFILEDOCID" />
     	<xsl:text disable-output-escaping="yes">&amp;beforePrint=true&amp;afterPrint=true&amp;locale=ja#&amp;pagesLoaded=true&amp;pageChange=true&amp;openFile=true&amp;download=true&amp;viewBookmark=true&amp;print=true&amp;fullScreen=true&amp;find=true#&amp;pagemode=none&amp;errorMessage=undefined&amp;errorAppend=true"  &gt;&lt;/iframe&gt;
	    </xsl:text>
	    
	    <xsl:text disable-output-escaping="yes">&lt;div id="downloadFilePdfId" &gt;</xsl:text>
		<xsl:text disable-output-escaping="yes">&lt;a class="pe-content no-line" download="" target="_blank" href="/pe4x/Download/?id=</xsl:text>
		<xsl:value-of select="./PDFFILEDOCID" />
		<xsl:text disable-output-escaping="yes">" &gt;本文ダウンロード&lt;/a&gt; &lt;/div&gt;</xsl:text>
		
	  </xsl:template>
	  
</xsl:stylesheet>