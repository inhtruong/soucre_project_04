<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="1.0">

<xsl:output method="xml" encoding="utf-8" />

<xsl:template match="/">
    <xsl:apply-templates select="U_MITSUBISHI" />
    <style type="text/css">
		.detailUbm {
			width: 100%;
			border: 1px solid #999999;
		}
		.detailUbm td.labelUbm {
			height:20px;
			border-bottom: 1px solid #aaaaaa;
			border-top: 1px solid #aaaaaa;
			border-right: 1px solid #aaaaaa;
			border-left: 1px solid #aaaaaa;
		}
		.detailUbm td.tdUbm {
			height:20px;
			border-bottom: 1px solid #aaaaaa;
			border-top: 1px solid #aaaaaa;
			border-right: 1px solid #aaaaaa;
			border-left: 1px solid #aaaaaa;
		}
		.detailUbm td.labelHr {
			height:10px;
			border-bottom: 1px solid #aaaaaa;
			border-top: 1px solid #aaaaaa;
			border-right: 1px solid #aaaaaa;
			border-left: 1px solid #aaaaaa;
		}
		.ri {
			text-align		:right;
		}
    </style>
</xsl:template>

<xsl:template match="U_MITSUBISHI">
	<div class="contents">
		<xsl:apply-templates select="./TB_DEFAULT" />
	</div>
	<div>　</div>
	
</xsl:template>

<xsl:template match="TB_DEFAULT">
    <table class="detail">
		<TR> 
				<TH class="label"></TH>
				<TH class="label">直販</TH>
			</TR>
			<TR>
				<TD class="label">末端単価</TD>
				<TD class="item">
					<xsl:value-of select="./TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label">小口配送単価</TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label">小口着色単価</TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label">末端単価　合計</TD>
				<TD class="item">
					<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label">仕切単価(計算値)</TD>
				<TD class="item">
					<xsl:value-of select="./PARTITIONUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label">仕切単価(決定値)</TD>
				<TD class="item">
					<xsl:value-of select="./PARTITIONUNITPRICE_D"/>
				</TD>
			</TR>
	</table>
</xsl:template>

</xsl:stylesheet>