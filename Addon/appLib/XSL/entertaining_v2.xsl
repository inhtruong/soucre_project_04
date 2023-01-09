<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

	<!-- ********************************************************************** -->
	<!--		精算処理：接待精算　スタイルシート								-->
	<!-- ********************************************************************** -->

	<!-- ***** ボディ ***** -->
	<xsl:template match="DOCUMENT">
	<script type="text/javascript" src="../script/CommonFunction-utf8.js">//</script>
		<div class="margin">
			<span class="iap-default">申請内容</span>
		</div>
		<div class="contents">
			<table class="detail">
				<tr>
					<td class="label">接待日</td>
					<td class="item">
						<xsl:value-of select="./ENTERTAININGDATE" />
					</td>
				</tr>
				<xsl:apply-templates select="./CUSTOMERS" />
				<xsl:apply-templates select="./NECESSITY" />
			</table>
		</div>
		<xsl:apply-templates select="./ENTERTAININGDETAILS" />

		<xsl:apply-templates select="./TEMPPAYMENT" />
		<xsl:apply-templates select="./ACCOUNTINGTOTAL"/>

		<xsl:apply-templates select="./BILLABLEDETAILS" />
	</xsl:template>

	<xsl:template match="ENTERTAININGDETAILS">
		<xsl:variable name="entertainType" select="./@type"/>
		<xsl:variable name="totalAmount"
			select="sum(.//ENTERTAININGAMOUNT)" />
		<div class="margin message">
		<span class="iap-default">
			接待明細（
			<xsl:value-of select="count(./ENTERTAININGDETAIL)" />
			件）
		</span>
		</div>
		<div><!-- 接待明細件数 -->
			<xsl:for-each select="ENTERTAININGDETAIL">
				<div class ="contents">
				<xsl:if test="position() > 1">
					<xsl:attribute name="class">contents margin</xsl:attribute>
				</xsl:if>
				<span class="iap-default">
					接待明細
					<xsl:value-of select="position()" />
				</span>
				<table class="detail">
					<tr>
						<td class="label">接待種類</td>
						<td class="item">
							<xsl:value-of select="./ENTERTAININGTYPE" />
						</td>
					</tr>
					<tr>
						<td class="label">接待先会社名<br/>ご担当者</td>
						<td class="item">
							<xsl:value-of disable-output-escaping="yes" select="./ENTERTAININGPLACE" />
						</td>
					</tr>
					<tr>
						<td class="label">接待先人数</td>
						<td class="item">
							<xsl:value-of select="./ATTENDERCOUNT" />
							名
						</td>
					</tr>
					<tr>
						<td class="label">当社出席者</td>
						<td class="item">
							<xsl:apply-templates select="EMPLIST" />
						</td>
					</tr>
					<tr>
						<td class="label">当社出席人数</td>
						<td class="item">
							<xsl:value-of select="./EMPATTENDERCOUNT" />
							名
						</td>
					</tr>
					<xsl:apply-templates select="./NOTE" />
					<tr>
						<td class="label">接待場所／品目</td>
						<td class="item">
							<xsl:value-of disable-output-escaping="yes" select="./ENTERTAININGITEM" />
						</td>
					</tr>
					<tr>
						<td class="label">税込金額</td>
						<td class="item">
							<xsl:value-of
								select="./ENTERTAININGAMOUNTNAME" />
						</td>
					</tr>
					<xsl:if test="$entertainType = 'account'">
						<!-- for 接待精算 -->
						<tr>
					      <td class="label">支払状況</td>
					      <td class="item">
					        <xsl:value-of
					          select="./PAYMENTSITUATION" />
					      </td>
					    </tr>
					    <tr>
					      <td class="label">経理担当者への<br/>連絡事項</td>
					      <td class="item">
					        <xsl:value-of disable-output-escaping="yes"
					          select="./CONTACTACCOUNTINGPERSON" />
					      </td>
					    </tr>
    				</xsl:if>
				</table>
				</div>
			</xsl:for-each>
			<xsl:if test="$entertainType = 'input'">
				<div class="contents margin">
				<span class="iap-default">合計</span>
				<table class="detail">
					<tr>
						<td class="label">合計金額</td>
						<td class="item">
							<xsl:value-of
								select="format-number($totalAmount,'###,###,###,###')" />
						</td>
					</tr>
				</table>
				</div>
			</xsl:if>
		</div>
	</xsl:template>

	<xsl:template match="EMPLIST">
		<xsl:for-each select="EMPDETAIL">
			<div>
				<xsl:element name="img">
					<xsl:attribute name="src">../img/b_emp.png</xsl:attribute>

					<xsl:attribute name="alt"><xsl:value-of select="EMPNAME" /></xsl:attribute>
				</xsl:element>
				<a class="noline">
					<xsl:attribute name="href">mailto:<xsl:value-of select="EMPEMAIL" /></xsl:attribute>
					<xsl:value-of select="EMPNAME" />
				</a>
			</div>
		</xsl:for-each>
		<div>
			<xsl:value-of disable-output-escaping="yes" select="./ORTHERATTEND"/>
		</div>
	</xsl:template>

	<xsl:template match="TEMPPAYMENT">
		<xsl:variable name="tempayAmount_Apply"
						select="number(./TEMPPAYMONEYAMOUNT)">
		</xsl:variable>
		<xsl:if test="$tempayAmount_Apply > 0">
				<div class="contents margin">
					<span class="iap-default">仮払い</span>
					<table class="detail">
						<tr>
							<td class="label">仮払金額</td>
							<td class="item">
								<span class="iap-default">
									<xsl:value-of
										select="format-number($tempayAmount_Apply,'###,###,###,###')" />
								</span>
								&#160;
							</td>
						</tr>
						<tr>
							<td class="label">口座振込</td>
							<td class="item">
								<span class="iap-default">
									<xsl:value-of select="./TRANSFERNECESSITYCLASS" />
								</span>
								&#160;
							</td>
						</tr>
					</table>
				</div>
		</xsl:if>
	</xsl:template>
	<xsl:template match="ACCOUNTINGTOTAL">
		<xsl:variable name="tempayAmount"
					select="number(./TEMPPAYMONEYAMOUNT)"/>
		<xsl:variable name="totalAmount"
					select="number(./TOTALMONEYAMOUNT)"/>
		<xsl:variable name="prepaidAmount"
					select="number(./PREPAIDMONEYAMOUNT)"/>
		<div class="contents margin">
			<table class="detail">
				<tr>
					<td class="label">接待金額</td>
					<td class="item">
						<xsl:value-of
								select="format-number($totalAmount,'###,###,###,###')" />
					</td>
				</tr>
				<tr>
					<td class="label">立替金額</td>
					<td class="item">
						<xsl:value-of
								select="format-number($prepaidAmount,'###,###,###,###')" />
					</td>
				</tr>
				<tr>
					<td class="label">仮払金額</td>
					<td class="item">
						<xsl:value-of
								select="format-number($tempayAmount,'###,###,###,###')" />
					</td>
				</tr>
				<tr>
					<td class="label">精算金額</td>
					<td class="item">
						<xsl:value-of
								select="format-number($prepaidAmount - $tempayAmount,'###,###,###,###')" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="BILLABLEDETAILS">
		<xsl:variable name="billCount" select="count(./BILLABLEDETAIL)" />
		<xsl:if test="$billCount &gt; 0">
			<div class="contents margin">
				<span class="iap-default">費用の負担部門・プロジェクト</span>
				<table class="dr-table rich-table listHighlight">
					<tr class="rich-table-header">
						<td colspan="2" width="auto"
							class="headBorderRight">
							負担部門またはプロジェクト
						</td>
						<td width="auto" class="noBorderRight">負担金額</td>
					</tr>
					<xsl:variable name="totalbillableamount"
						select="sum(.//BILLABLEAMOUNT)" />
					<xsl:for-each select="BILLABLEDETAIL">
						<xsl:variable name="billableamount"
							select="number(./BILLABLEAMOUNT)" />
						<tr>
							<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="position() mod 2 = 1">oddRow</xsl:when>
								<xsl:otherwise>evenRow</xsl:otherwise>
							</xsl:choose>
						</xsl:attribute>
							<td class="borderRight tdHighlight">
								<span class="iap-default">
									<xsl:value-of
										select="./BILLABLENAME" />
								</span>
								&#160;
							</td>
							<td class="borderRight tdHighlight">
								<span class="iap-default">
									<xsl:value-of
										select="./BILLABLEDETAILNAME" />
								</span>
								&#160;
							</td>
							<td style="text-align: right"
								class="noBorderRight tdHighlight">
								<xsl:value-of
									select="format-number($billableamount,'###,###,###,###')" />
							</td>
						</tr>
					</xsl:for-each>
					<tr class="dr-table-footer rich-table-footer">
						<td class="dr-table-footercell rich-table-footercell headBorderRight headerBg" colspan="2">負担合計</td>
						<td style="text-align: right; background-color: white" class="noBorderRight">
								<xsl:value-of select="format-number($totalbillableamount,'###,###,###,###')" />
						</td>
					</tr>
				</table>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="CUSTOMERS"><!-- 接待先 -->
		<tr>
			<td class="label">接待先</td>
			<td class="item">
				<div>
					<xsl:choose>
						<xsl:when test="(./CUSTCORPNAME/text() != '') or (./CUSTDEPTNAME/text() != '') or (./CUSTEMPNAME/text() != '')">
						<!-- AIT TRANHSV MODIFIED : PE VERSION 2.2 20100623 ticket 6080
							<xsl:value-of select="./CUSTCORPNAME" disable-output-escaping="yes"/>
							<xsl:if test="(./CUSTCORPNAME/text() != '') and ((./CUSTDEPTNAME/text() != '') or (./CUSTEMPNAME/text() != ''))">
								<br/>
							</xsl:if>
							<xsl:value-of select="./CUSTDEPTNAME" disable-output-escaping="yes"/>
							<xsl:if test="(./CUSTDEPTNAME/text() != '') and (./CUSTEMPNAME/text() != '')">
								<br/>
							</xsl:if>
							<xsl:value-of select="./CUSTEMPNAME" disable-output-escaping="yes"/>
							&#160;
						 -->
						 	<xsl:if test="count(CUSTCORPID) > 0">
								<xsl:element name="a">
										<xsl:attribute name="class">anchor noline</xsl:attribute>
										<xsl:attribute name="href">javascript:fncDisplayEnterprise('<xsl:value-of select="CUSTCORPID" />')</xsl:attribute>
										<xsl:value-of select="./CUSTCORPNAME" disable-output-escaping="yes"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="count(CUSTCORPID) = 0">
									<xsl:value-of select="./CUSTCORPNAME" disable-output-escaping="yes"/>
							</xsl:if>
							
							<xsl:if test="(./CUSTCORPNAME/text() != '') and ((./CUSTDEPTNAME/text() != '') or (./CUSTEMPNAME/text() != ''))">
								<br/>
							</xsl:if>
							<xsl:if test="count(CUSTDEPTID) > 0">
								<xsl:element name="a">
										<xsl:attribute name="class">anchor noline</xsl:attribute>
										<xsl:attribute name="href">javascript:fncDisplayEnterprisePost('<xsl:value-of select="CUSTCORPID" />','<xsl:value-of select="CUSTDEPTID" />')</xsl:attribute>
										<xsl:value-of select="./CUSTDEPTNAME" disable-output-escaping="yes"/>
								</xsl:element>
							</xsl:if>
							<xsl:if test="count(CUSTDEPTID) = 0">
									<xsl:value-of select="./CUSTDEPTNAME" disable-output-escaping="yes"/>
							</xsl:if>
							<xsl:if test="(./CUSTDEPTNAME/text() != '') and (./CUSTEMPNAME/text() != '')">
								<br/>
							</xsl:if>
							<xsl:if test="count(CUSTEMPID) > 0">
								<xsl:element name="a">
										<xsl:attribute name="class">anchor noline</xsl:attribute>
										<xsl:attribute name="href">javascript:fncDisplayEnterprisePerson('<xsl:value-of select="CUSTCORPID" />','<xsl:value-of select="CUSTDEPTID" />','<xsl:value-of select="CUSTEMPID" />')</xsl:attribute>
										<xsl:value-of select="./CUSTEMPNAME" disable-output-escaping="yes"/>
								</xsl:element>
								
								&#160;
							</xsl:if>
							<xsl:if test="count(CUSTEMPID) = 0">
										<xsl:value-of select="./CUSTEMPNAME" disable-output-escaping="yes"/>
								&#160;
							</xsl:if>
							
						</xsl:when>
					</xsl:choose>
					<xsl:if test="((./CUSTCORPNAME/text() != '') or (./CUSTDEPTNAME/text() != '') or (./CUSTEMPNAME/text() != ''))
									and (./OTHERCUST/text() != '')">
						<br/>
					</xsl:if>
					<xsl:value-of disable-output-escaping="yes" select="./OTHERCUST" /><!-- その他接待先 -->
				</div>
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="NECESSITY">
		<!-- 理　由 -->
		<tr>
			<td class="label">理由</td>
			<td class="item">
				<xsl:value-of disable-output-escaping="yes" select="." />
			</td>
		</tr>
	</xsl:template>
	<xsl:template match="NOTE">
	<tr>
		<td class="label">備考</td>
		<td class="item">
			<xsl:value-of disable-output-escaping="yes" select="." />
		</td>
	</tr>
	</xsl:template>

</xsl:stylesheet>
