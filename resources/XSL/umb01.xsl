<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.w3.org/1999/xhtml" version="1.0">

<xsl:output method="xml" encoding="utf-8" />

<xsl:template match="/">
    <xsl:apply-templates select="U_MITSUBISHI" />
    <style type="text/css">
		.itemUmb, .labelUmb {
            border-collapse: collapse;
            border: 1px solid #AAAAAA;
        }

        .detailUmb {
        	width:100%;
        	border-collapse: collapse;
            text-align: center;
        }

        .labelUmb {
            background-color: #E0E0E0;
        }
        
        .unSelectTb {
        	background-color: #E0E0E0;
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
<xsl:choose>
	<xsl:when test="./PATTERN='0'">
		<TABLE class="detail umb" id="umb">
			<TR>
	            <TH style="width: 28%" class="labelUmb" colspan="2"></TH>
	            <TH style="width: 12%" class="labelUmb">直販</TH>
	            <TH style="width: 12%" class="labelUmb">割増無</TH>
	            <TH style="width: 12%" class="labelUmb">小口配送料のみ<BR />（100kg未満）</TH>
	            <TH style="width: 12%" class="labelUmb">小口着色料のみ<BR />(100-299kg)</TH>
	            <TH style="width: 12%" class="labelUmb">小口着色<BR />(100-299kg)</TH>
	            <TH style="width: 12%" class="labelUmb">小口着色+配送<BR />（0-99kg)</TH>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">末端単価</TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRERETAILPRICE1"/>
	            </TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRERETAILPRICE2"/>
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">小口配送単価</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">小口着色単価</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">末端単価　合計</TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRETOTALRETAILPRICE1"/>
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" rowspan="2">二次口銭<BR />(率)</TD>
	            <TD class="labelUmb">率(%)</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb">金額</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" rowspan="2">一次口銭<BR />(率)</TD>
	            <TD class="labelUmb">率(%)</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb">金額</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" rowspan="2">口銭合計</TD>
	            <TD class="labelUmb">率(%)</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb">金額</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">仕切単価(計算値)</TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPREPARTITIONUNITPRICE1"/>
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	            <TD class="itemUmb">
	            	
	            </TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">仕切単価(決定値)</TD>
	            <TD class="itemUmb">手入力</TD>
	            <TD class="itemUmb">手入力</TD>
	            <TD class="itemUmb">手入力</TD>
	            <TD class="itemUmb">手入力</TD>
	            <TD class="itemUmb">手入力</TD>
	            <TD class="itemUmb">手入力</TD>
	        </TR>
    	</TABLE>
	</xsl:when>
	<xsl:when test="./PATTERN='1'">
		<TABLE class="detail umb" id="umb">
			<TR>
	            <TH style="width: 28%" class="labelUmb" colspan="2"></TH>
	            <TH style="width: 12%" class="labelUmb">直販</TH>
	            <TH style="width: 12%" class="labelUmb">割増無</TH>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">末端単価</TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRERETAILPRICE1"/>
	            </TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRERETAILPRICE1"/>
	            </TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">小口配送単価</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">小口着色単価</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">末端単価　合計</TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRETOTALRETAILPRICE1"/>
	            </TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRETOTALRETAILPRICE1"/>
	            </TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" rowspan="2">二次口銭<BR />(率)</TD>
	            <TD class="labelUmb">率(%)</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb">金額</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" rowspan="2">一次口銭<BR />(率)</TD>
	            <TD class="labelUmb">率(%)</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb">金額</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" rowspan="2">口銭合計</TD>
	            <TD class="labelUmb">率(%)</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb">金額</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">仕切単価(計算値)</TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPREPARTITIONUNITPRICE1"/>
	            </TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPREPARTITIONUNITPRICE1"/>
	            </TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">仕切単価(決定値)</TD>
	            <TD class="itemUmb">手入力</TD>
	            <TD class="itemUmb">手入力</TD>
	        </TR>
    	</TABLE>
	</xsl:when>
	<xsl:when test="./PATTERN='2'">
		<TABLE class="detail umb" id="umb">
			<TR>
	            <TH style="width: 28%" class="labelUmb" colspan="2"></TH>
	            <TH style="width: 12%" class="labelUmb">直販</TH>
	            <TH style="width: 12%" class="labelUmb">直販</TH>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">末端単価</TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRERETAILPRICE1"/>
	            </TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRERETAILPRICE1"/>
	            </TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">小口配送単価</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">小口着色単価</TD>
	            <TD class="itemUmb"></TD>
	            <TD class="itemUmb"></TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">末端単価　合計</TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRETOTALRETAILPRICE1"/>
	            </TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPRETOTALRETAILPRICE1"/>
	            </TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">仕切単価(計算値)</TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPREPARTITIONUNITPRICE1"/>
	            </TD>
	            <TD class="itemUmb">
	            	<xsl:value-of select="./NOPREPARTITIONUNITPRICE1"/>
	            </TD>
	        </TR>
	        <TR>
	            <TD class="labelUmb" colspan="2">仕切単価(決定値)</TD>
	            <TD class="itemUmb">手入力</TD>
	            <TD class="itemUmb">手入力</TD>
	        </TR>
    	</TABLE>
	</xsl:when>
</xsl:choose>
	
</xsl:template>

</xsl:stylesheet>