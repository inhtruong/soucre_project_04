<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:strip-space elements=""/>
<xsl:output method="html" encoding="utf-8"/>

<!--
Author : AIT_Trinhhtd
System : UMB:GeneralAffair management system
Process : toConfirm(Price master)  
Creation Date : 2023/01/12
Modification date :
-->
<!-- ***** Route ***** -->
<xsl:template match="CONFIRMDATA">
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

<div class="contents"> <!-- Confirm Price master detail -->
	<div class="detail">
	
		<!-- Confirm detail -->
		<xsl:if test="">
			<TABLE>
				<TR>
					<TD class="label">仮単価マスタデータ参照</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/UNITPRICEDATAREF"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">単価マスタ参照</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/PRICEDATAREF"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">データ行NO</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/DATALINENO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">データNO</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/DATANO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">送信元レコード作成日時</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/SRCCREATEDATE"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">データ更新区分</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/UPDATEXATEGORY"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">売上部門CD</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/SALEDEPARTMENTCD"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">受注明細NO</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/SALEORDERNO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">受注NO</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/ORDERNO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">得意先CD</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/CUSTOMERCD"/>
					</TD>
					<TD class="label">得意先名</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/CUSTOMERNAME"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">仕向先CD1</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/DESTINATIONCD1"/>
					</TD>
					<TD class="label">仕向先名1</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/DESTINATIONNAME1"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">仕向先CD2</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/DESTINATIONCD2"/>
					</TD>
					<TD class="label">仕向先名2</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/DESTINATIONNAME2"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">納品先CD</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/DELIVERYDESTINATIONCD"/>
					</TD>
					<TD class="label">納品先名</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/DELIVERYDESTINATIONNAME"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">品名略号</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/PRODUCTNAMEABBREVIATION"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">カラーNo</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/COLORNO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">グレード1</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/GRATE1"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">ユーザー品目</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/USERITEM"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">通貨CD</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/CURENCYCD"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">取引単位CD</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/TRANSACTIONUNITCD"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">荷姿</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/PACKING"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">取引先枝番</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/CLIENTBRACHNUMBER"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">用途CD</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/USAGECD"/>
					</TD>
					<TD class="label">用途参照</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/USAGEDEF"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">納品予定日時</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/DELIVERYDATE"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">品目分類CD1</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/COMMODITYCLASSIFICATIONCD1"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">受注日</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/ORDERDATE"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">登録担当者</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEUNITREFDTO/REGISTRAR"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">改定前単価</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEREFDTO/UNITPRICEBEFREVISION"/>
					</TD>
				</TR>
				<TR>
					<TABLE>
						<TR>
							<TH colspan="2"></TH>
							<TH>直販</TH>
							<TH>割増無</TH>
							<TH>小口配送料のみ<BR/>（100kg未満）</TH>
							<TH>小口着色料のみ<BR/>(100-299kg)</TH>
							<TH>小口着色<BR/>(100-299kg)</TH>
							<TH>小口着色+配送<BR/>（0-99kg)</TH>
						</TR>
						<TR>
							<TH class="label" colspan="2">末端単価</TH>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">小口配送単価</TH>
							<TD class="item"></TD>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item"></TD>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">小口配送単価</TH>
							<TD class="item"></TD>
							<TD class="item"></TD>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">末端単価　合計</TH>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" rowspan="2">二次口銭<BR/>(率)</TH>
							<TH class="label">率(%)</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label">金額</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" rowspan="2">一次口銭<BR/>(率)</TH>
							<TH class="label">率(%)</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label">金額</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" rowspan="2">口銭合計</TH>
							<TH class="label">率(%)</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label">金額</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">仕切単価(計算値)</TH>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">改定前単価</TH>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">仕切単価(決定値)</TH>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./PRICECALPARAM/TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
					</TABLE>
				</TR>
				<TR>
					<TD class="label">伺い理由</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEREFDTO/RESONINQUIRY"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">適用日</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEREFDTO/APPLICATIONSTARTDATE"/>
					</TD>
					<TD class="label">~</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEREFDTO/APPLICATIONENDDATE"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">遡及区分</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEREFDTO/RETROACTIVECLASSIFICATION"/>
					</TD>
				</TR>
			</TABLE>
		</xsl:if>
	</div>
</div>
</xsl:stylesheet>
