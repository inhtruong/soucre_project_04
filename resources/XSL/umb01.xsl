<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:strip-space elements=""/>
<xsl:output method="html" encoding="utf-8"/>

<!--
Author : dattdd
System : UMB:GeneralAffair management system
Process : OutAccount Affair
Creation Date : 2023/01/06
Modification date :
-->
<!-- ***** Route ***** -->
<xsl:template match="AFFAIRDATA">
		<LINK rel="stylesheet" href="../css/default.css" type="text/css"></LINK>
		<LINK rel="stylesheet" href="../UMB/css/UMBCommon.css" type="text/css"></LINK>
		<STYLE type="text/css">
			TD.rrdtitle{font-size:10pt;color:black;background-color:#BCDCFF;}
			TD.rrddata{font-size:10pt;color:black;background-color:#E0E0E0;}
			TD.rfm-title{font-size:10pt;color:black;background-color:#BCDCFF;}
			TD.rfm-data{font-size:10pt;color:black;background-color:#E0E0E0;}
			TD.modifyItem{border-bottom: 1px solid #cccccc; border-top: 1px solid #cccccc; background-color:#FFFFCC;}
		</STYLE>
		<SCRIPT src="../script/CommonFunction.js"></SCRIPT>
<!--<BODY>-->

<div class="contents">
	<TABLE class="detail">
		<!-- Pattern 1 -->
		<xsl:if test="./PATTERN == 1">
			<TR> 
				<TH class="label"></TH>
				<TH class="label">直販</TH>
			</TR>
			<TR>
				<TD class="label">末端単価</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
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
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label">仕切単価(計算値)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label">仕切単価(決定値)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE_D"/>
				</TD>
			</TR>		
		</xsl:if>
		
		<!-- Pattern 2 -->
		<xsl:if test="./PATTERN == 2">
			<TR> 
				<TH class="label"></TH>
				<TH class="label">直販</TH>
				<TH class="label">割増無</TH>
			</TR>
			<TR>
				<TD class="label">末端単価</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label">小口配送単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label">小口着色単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label">末端単価　合計</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label">仕切単価(計算値)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label">仕切単価(決定値)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE_D"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE_D"/>
				</TD>
			</TR>		
		</xsl:if>
		
		<!-- Pattern 3 -->
		<xsl:if test="./PATTERN == 3">
			<TR> 
				<TH class="label" colspan="2"></TH>
				<TH class="label">割増無</TH>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口配送単価</TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口着色単価</TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価　合計</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)(%) -->
				<TD rowspan="2">一次口銭<BR/>(率)</TD>
				<TD class="label">率(%)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)金額 -->
				<TD class="label">金額</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計(%) -->
				<TD rowspan="2">口銭合計</TD>
				<TD class="label">率(%)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計 金額 -->
				<TD class="label">金額</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(計算値)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(決定値)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE_D"/>
				</TD>
			</TR>		
		</xsl:if>
		
		<!-- Pattern 4,5 -->
		<xsl:if test="./PATTERN == 4 || ./PATTERN == 5">
			<TR> 
				<TH class="label" colspan="2"></TH>
				<TH class="label">直送</TH>
				<TH class="label">割増無</TH>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口配送単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口着色単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価　合計</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 二次口銭(率)(%) -->
				<TD rowspan="2">二次口銭<BR/>(率)</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!--二次口銭(率)金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)(%) -->
				<TD rowspan="2">一次口銭<BR/>(率)</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計(%) -->
				<TD rowspan="2">口銭合計</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計 金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(計算値)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(決定値)</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE_D"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
			</TR>		
		</xsl:if>
		
		<!-- Pattern 6-->
		<xsl:if test="./PATTERN == 6">
			<TR> 
				<TH class="label" colspan="2"></TH>
				<TH class="label"></TH>
				<TH class="label">割増無</TH>
				<TH class="label">小口配送料のみ<BR/>（100kg未満）</TH>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口配送単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口着色単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価　合計</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 二次口銭(率)(%) -->
				<TD rowspan="2">二次口銭<BR/>(率)</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!--二次口銭(率)金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)(%) -->
				<TD rowspan="2">一次口銭<BR/>(率)</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計(%) -->
				<TD rowspan="2">口銭合計</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計 金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(計算値)</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(決定値)</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE_D"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
			</TR>		
		</xsl:if>
		
		<!-- Pattern 7-->
		<xsl:if test="./PATTERN == 7">
			<TR> 
				<TH class="label" colspan="2"></TH>
				<TH class="label"></TH>
				<TH class="label">割増無</TH>
				<TH class="label">小口配送料のみ<BR/>（100kg未満）</TH>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口配送単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口着色単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価　合計</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 二次口銭(率)(%) -->
				<TD rowspan="2">二次口銭<BR/>(率)</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!--二次口銭(率)金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)(%) -->
				<TD rowspan="2">一次口銭<BR/>(率)</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計(%) -->
				<TD rowspan="2">口銭合計</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計 金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(計算値)</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(決定値)</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE_D"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
			</TR>		
		</xsl:if>
		
				<!-- Pattern 7-->
		<xsl:if test="./PATTERN == 7">
			<TR> 
				<TH class="label" colspan="2"></TH>
				<TH class="label"></TH>
				<TH class="label">割増無</TH>
				<TH class="label">小口着色<BR/>(100-299kg)</TH>
				<TH class="label">小口着色+配送<BR/>（0-99kg)</TH>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口配送単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">小口着色単価</TD>
				<TD class="item disable"></TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUE/TERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">末端単価　合計</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 二次口銭(率)(%) -->
				<TD rowspan="2">二次口銭<BR/>(率)</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!--二次口銭(率)金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)(%) -->
				<TD rowspan="2">一次口銭<BR/>(率)</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 一次口銭(率)金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計(%) -->
				<TD rowspan="2">口銭合計</TD>
				<TD class="label">率(%)</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR> <!-- 口銭合計 金額 -->
				<TD class="label">金額</TD>
				<TD class="item disable"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(計算値)</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>
			<TR>
				<TD class="label" colspan="2">仕切単価(決定値)</TD>
				<TD class="item"></TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE_D"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/PARTITIONUNITPRICE"/>
				</TD>
				<TD class="item">
					<xsl:value-of select="./OUTVALUES/TOTALTERMINALUNITPRICE"/>
				</TD>
			</TR>		
		</xsl:if>
	</TABLE>
</div>
</xsl:stylesheet>
