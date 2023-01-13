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
	<div class="detail">
	
		<!-- Confirm detail -->
			<TABLE>
				<TR>
					<TD class="label">仮単価マスタデータ参照</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./UNITPRICEDATAREF"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">単価マスタ参照</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRICEDATAREF"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">データ行NO</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./DATAMIGRATIONNO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">データNO</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./DATANO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">送信元レコード作成日時</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./SRCCREATEDATE"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">データ更新区分</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./DATAUPDATECATEGORYCD"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">売上部門CD</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./SALESDEPARTMENTCD"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">受注明細NO</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./SALESORDERNO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">受注NO</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./ORDERNO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">得意先CD</TD>
					<TD class="item">
						<xsl:value-of select="./CUSTOMERCD"/>
					</TD>
					<TD class="label">得意先名</TD>
					<TD class="item">
						<xsl:value-of select="./CUSTOMERNAME"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">仕向先CD1</TD>
					<TD class="item">
						<xsl:value-of select="./DESTINATIONCD1"/>
					</TD>
					<TD class="label">仕向先名1</TD>
					<TD class="item">
						<xsl:value-of select="./DESTINATIONNAME1"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">仕向先CD2</TD>
					<TD class="item">
						<xsl:value-of select="./DESTINATIONCD2"/>
					</TD>
					<TD class="label">仕向先名2</TD>
					<TD class="item">
						<xsl:value-of select="./DESTINATIONNAME2"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">納品先CD</TD>
					<TD class="item">
						<xsl:value-of select="./DELIVERYDESTINATIONCD"/>
					</TD>
					<TD class="label">納品先名</TD>
					<TD class="item">
						<xsl:value-of select="./DELIVERYDESTINATIONNAME"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">品名略号</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PRODUCTNAMEABBREVIATION"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">カラーNo</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./COLORNO"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">グレード1</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./GRATE1"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">ユーザー品目</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./USERITEM"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">通貨CD</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./CURENCYCD"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">取引単位CD</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./TRANSACTIONUNITCD"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">荷姿</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./PACKING"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">取引先枝番</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./CLIENTBRANCHNUMBER"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">用途CD</TD>
					<TD class="item">
						<xsl:value-of select="./USAGECD"/>
					</TD>
					<TD class="label">用途参照</TD>
					<TD class="item">
						<xsl:value-of select="./PRICEUNITREFDTO/USAGEDEF"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">納品予定日時</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./DELIVERYDATE"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">品目分類CD1</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./COMMODITYCLASSIFICATIONCD1"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">受注日</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./ORDERDATE"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">登録担当者</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./REGISTRAR"/>
					</TD>
				</TR>
				<TR>
					<TD class="label">改定前単価</TD>
					<TD class="item" colspan="3">
						<xsl:value-of select="./UNITPRICEBEFREVISION"/>
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
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">小口配送単価</TH>
							<TD class="item"></TD>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item"></TD>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">小口配送単価</TH>
							<TD class="item"></TD>
							<TD class="item"></TD>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">末端単価　合計</TH>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" rowspan="2">二次口銭<BR/>(率)</TH>
							<TH class="label">率(%)</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label">金額</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" rowspan="2">一次口銭<BR/>(率)</TH>
							<TH class="label">率(%)</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label">金額</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" rowspan="2">口銭合計</TH>
							<TH class="label">率(%)</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label">金額</TH>
							<TD class="item"></TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">仕切単価(計算値)</TH>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">小口配送単価(計算値)</TH>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">小口着色単価(計算値)</TH>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
							<TD class="item">
								<xsl:value-of select="./TOTALTERMINALUNITPRICE"/>
							</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">仕切単価(決定値)</TH>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">小口配送単価(決定値)</TH>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
						</TR>
						<TR>
							<TH class="label" colspan="2">小口着色単価(決定値)</TH>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
							<TD class="item">手入力</TD>
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
	</div>
</xsl:template>

</xsl:stylesheet>