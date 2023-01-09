<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes"
		encoding="utf-8" />

	<!-- PDF申請 -->
	<xsl:template match="DOCUMENT">
		
		<xsl:text disable-output-escaping="yes">&lt;div class="contents"&gt;</xsl:text>
			<xsl:text disable-output-escaping="yes">
			&lt;iframe height="600" width="100%" src="/pe4j/pdfjs/web/viewer.html?file=/pe4j/Download/</xsl:text>
			
			<xsl:value-of select="./PDFFILENAME" disable-output-escaping="yes" />
			<xsl:text disable-output-escaping="yes">?id%3D</xsl:text>
		
			<xsl:value-of select="./PDFFILEDOCID" />
	     	<xsl:text disable-output-escaping="yes">&amp;beforePrint=true&amp;afterPrint=true&amp;pagesLoaded=true&amp;pageChange=true&amp;openFile=true&amp;download=true&amp;viewBookmark=true&amp;print=true&amp;fullScreen=true&amp;find=true#&amp;locale=zh-CN#&amp;pagemode=none&amp;errorMessage=undefined&amp;errorAppend=true"  &gt;&lt;/iframe&gt;
		    </xsl:text>
		    
			<xsl:text disable-output-escaping="yes">&lt;div id="downloadFilePdfId" &gt;&lt;/div&gt;</xsl:text>
			
		<xsl:text disable-output-escaping="yes">&lt;/div&gt;</xsl:text>

		<script type="text/javascript">
			var link ="/pe4j/Download/?id=<xsl:value-of select="./PDFFILEDOCID" />";
			fncOnLoad();
			
			function fncOnLoad() {
				var ObjectA = "<a href="+link+">下载文本</a>";
				var elem = document.getElementById('downloadFilePdfId');
				elem.innerHTML = ObjectA;
			}
			
		
		</script>
	</xsl:template>

</xsl:stylesheet>
