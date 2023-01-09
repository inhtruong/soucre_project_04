<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
	<!-- ********************************************************************** -->
	<!--		精算処理系：出張申請　スタイルシート							-->
	<!-- ********************************************************************** -->
	<xsl:decimal-format name="myFormat" NaN="0" zero-digit="0"/>
	<!-- ***** ボディ ***** -->
	<xsl:template match="DOCUMENT">
		<span class="iap-default">申請内容</span>
		<div class="contents">
			<table class="detail">
				<tr>
					<td class="label">出張種別</td>
					<td class="item">
						<xsl:value-of select="./BUSINESSTRIPCLASSNAME" />
						　
						<xsl:value-of
							select="./BUSINESSTRIPBREAKDOWNCLASSNAME" />
					</td>
				</tr>
			</table>
			</div>
			<div class="contents margin">
			<span class="iap-default">出発</span>
			<table class="detail">
				<tr>
					<td class="label">日時</td>
					<td class="item"><xsl:value-of select="./DEPARTDATETIME" /></td>
				</tr>
				<tr>
					<td class="label">交通機関</td>
					<td class="item"><xsl:value-of select="./DEPARTTRANSPORT" /></td>
				</tr>
				<tr>
					<td class="label">便名</td>
					<td class="item"><xsl:value-of disable-output-escaping="yes" select="./DEPARTFLIGHTNAME" /></td>
				</tr>
			</table>
			</div>
			<div class="contents margin">
			<span class="iap-default">帰着</span>
			<table class="detail">
				<tr>
					<td class="label">日時</td>
					<td class="item"><xsl:value-of select="./ARRIVEDATETIME" /></td>
				</tr>
				<tr>
					<td class="label">交通機関</td>
					<td class="item"><xsl:value-of select="./ARRIVETRANSPORT" /></td>
				</tr>
				<tr>
					<td class="label">便名</td>
					<td class="item"><xsl:value-of disable-output-escaping="yes" select="./ARRIVEFLIGHTNAME" /></td>
				</tr>
			</table>
			</div>
			<div class="contents margin">
			<table class="detail margin">
				<tr>
					<td class="label">訪問先</td>
					<td class="item"><xsl:value-of disable-output-escaping="yes" select="./VISITEDNAME" /></td>
				</tr>
				<tr>
					<td class="label">理由</td>
					<td class="item">
						<xsl:value-of disable-output-escaping="yes" select="./PURPOSE" />
					</td>
				</tr>
				<xsl:if test="./REPORTMATTER != ''">
					<tr>
						<td class="label">報告事項</td>
						<td class="item">
							<xsl:value-of disable-output-escaping="yes" select="./REPORTMATTER" />
						</td>
					</tr>
				</xsl:if>
				<!-- for 出張申請 -->
				<xsl:if test="./CONTENTS != ''">
					<tr>
						<td class="label">
							<div>備考</div>
							<div>※連絡先、宿泊先</div>
						</td>
						<td class="item">
							<xsl:value-of disable-output-escaping="yes" select="./CONTENTS" />
						</td>
					</tr>
				</xsl:if>
				<!-- for 出張精算 -->
				<xsl:if test="./REPORTPOINTS != ''">
					<tr>
						<td class="label">報告事項</td>
						<td class="item">
							<xsl:value-of disable-output-escaping="yes" select="./REPORTPOINTS" />
						</td>
					</tr>
				</xsl:if>
			</table>
			<!-- for 出張精算 -->
			<xsl:if test="./ALLOWANCE != ''">
				<xsl:apply-templates select="./ALLOWANCE" />
			</xsl:if>
		</div>
		<xsl:apply-templates select="./TRAVELDETAILS" />
		<xsl:apply-templates select="./ADDITIONPAYMENT" />
		<xsl:apply-templates select="./TEMPPAYMENT_APPLY" />
		<xsl:apply-templates select="./TEMPPAYMENT" />
		<xsl:apply-templates select="./TOTAL" />
		<xsl:apply-templates select="./BILLABLEDETAILS" /><!-- 負担金額 -->
	</xsl:template>
	<xsl:template match="ALLOWANCE">

		<xsl:variable name="dailyAllowance">
			<xsl:choose>
				<xsl:when test="string-length(./DAILYALLOWANCE) >0 ">
						<xsl:value-of select="format-number(number(./DAILYALLOWANCE),'###,###,###,###','myFormat')" />
				</xsl:when>
				<xsl:otherwise>
						<xsl:text></xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="noOfdays">
			<xsl:choose>
				<xsl:when test="string-length(./NOOFDAYS) >0 ">
						<xsl:value-of select="format-number(number(./NOOFDAYS),'###,###,###,##0.0','myFormat')" />
				</xsl:when>
				<xsl:otherwise>
						<xsl:text></xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="dailyAmount" select="number(./DAILYAMOUNT)" />

		<xsl:variable name="overNightAllowance">
			<xsl:choose>
				<xsl:when test="string-length(./OVERNIGHTALLOWANCE) >0 ">
						<xsl:value-of select="format-number(number(./OVERNIGHTALLOWANCE),'###,###,###,###','myFormat')" />
				</xsl:when>
				<xsl:otherwise>
						<xsl:text></xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="noOfNight">
			<xsl:choose>
				<xsl:when test="string-length(./NOOFNIGHT) >0 ">
						<xsl:value-of select="format-number(number(./NOOFNIGHT),'###,###,###,##0.0','myFormat')" />
				</xsl:when>
				<xsl:otherwise>
						<xsl:text></xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="overnightAmount" select="number(./OVERNIGHTAMOUNT)" />
		<xsl:variable name="foodAmount" select="number(./FOODAMOUNT)" />
		<table class="detail margin">
			<tr>
				<td class="label">日当</td>
				<td class="item">

					<!-- We must display A × B = C-->
					<!--In case : (A and B) is empty . We just display C-->
					<!--Otherwise : A × B = C-->
					<xsl:choose>
						<xsl:when test="string-length(./DAILYALLOWANCE) = 0 and string-length(./NOOFDAYS) =0">
							<xsl:value-of select="format-number($dailyAmount,'###,###,###,###','myFormat')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$dailyAllowance" />
							<xsl:text>×</xsl:text>
							<xsl:value-of select="$noOfdays" />
							<xsl:text>　=　</xsl:text>
							<xsl:value-of select="format-number($dailyAmount,'###,###,###,###','myFormat')" />
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="label">宿泊代</td>
				<td class="item">

					<xsl:choose>
						<xsl:when test="string-length(./OVERNIGHTALLOWANCE) = 0 and string-length(./NOOFNIGHT) =0">
							<xsl:value-of select="format-number($overnightAmount,'###,###,###,###','myFormat')" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$overNightAllowance" />
							<xsl:text>×</xsl:text>
							<xsl:value-of select="$noOfNight" />
							<xsl:text>　=　</xsl:text>
							<xsl:value-of select="format-number($overnightAmount,'###,###,###,###','myFormat')" />
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="label">宿泊時領収書</td>
				<td class="item">
					<xsl:choose>
				      <xsl:when test="@overnight = '0001'">
				        <span class="iap-default">無</span>
				      </xsl:when>
				      <xsl:when test="@overnight = '0002'">
				        <span class="iap-default">有</span>
				      </xsl:when>
				    </xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="label">食事代 </td>
				<td class="item">
					<xsl:value-of
								select="format-number($foodAmount,'###,###,###,###','myFormat')" />
				</td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="TRAVELDETAILS">
		<xsl:variable name="totalMoneyAmount"
						select="sum(.//MONEYAMOUNT)" />
		<div class="contents margin">
			<span class="iap-default">交通費明細</span>
			<table class="rich-table listHighlightRich">
				<tr class="rich-table-header">
					<td width="auto" class="headBorderRight" style="font-weight:bold;">使用日</td>
					<td width="auto" class="headBorderRight" style="font-weight:bold;">交通機関</td>
					<td width="auto" class="headBorderRight" style="font-weight:bold;">出発地</td>
					<td width="auto" class="headBorderRight" style="font-weight:bold;">到着地</td>
					<td width="auto" class="headBorderRight" style="font-weight:bold;">金額</td>
					<td width="auto" class="noBorderRight" style="font-weight:bold;">領収書</td>
				</tr>
				<xsl:apply-templates />
				<tr>
					<td class="headerBg borderRight" colspan="4">交通費合計</td>
					<td style="text-align: right" class="borderRight">
							<xsl:value-of select="format-number($totalMoneyAmount,'###,###,###,###','myFormat')" />
					</td>
					<td class="noBorderRight">&#160;</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="TRAVELDETAIL">
		<xsl:variable name="moneyAmount"
						select="number(./MONEYAMOUNT)" />
		<tr>
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="position() mod 2 = 1">oddRow</xsl:when>
					<xsl:otherwise>evenRow</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<td class="borderRight"><xsl:value-of select="./PAIDDATE"/></td>
			<td class="borderRight"><xsl:value-of select="./TRANSPORTMODE"/></td>
			<td class="borderRight"><xsl:value-of disable-output-escaping="yes" select="./DEPARTFROM"/></td>
			<td class="borderRight"><xsl:value-of disable-output-escaping="yes" select="./ARRIVETO"/></td>
			<td class="borderRight" style="text-align: right">
				<xsl:value-of select="format-number($moneyAmount,'###,###,###,###','myFormat')" />
			</td>
			<td class="noBorderRight">
				<span class="iap-default">
					<xsl:choose>
						<xsl:when test="@class = '0001'">無</xsl:when>
						<xsl:when test="@class = '0002'">有</xsl:when>
						<xsl:otherwise />
					</xsl:choose>
				</span>
			</td>
		</tr>
	</xsl:template>

	<xsl:template match="ADDITIONPAYMENT">
		<xsl:variable name="totalAmount" select="sum(.//MONEYAMOUNT)" />
		<div class="contents margin">
		<span class="iap-default">その他出金内容</span>
		<table class="rich-table listHighlightRich">
			<tr class="rich-table-header">
				<td class="headBorderRight" width="auto" colspan="2" style="font-weight:bold;">出金内容</td>
				<td class="noBorderRight" width="auto" style="font-weight:bold;">金額</td>
			</tr>
			<xsl:apply-templates />
			<tr>
				<td class="headerBg borderRight" colspan="2">その他合計</td>
				<td style="text-align: right" class="noBorderRight">
						<xsl:value-of select="format-number($totalAmount,'###,###,###,###','myFormat')" />
				</td>
			</tr>
		</table>
		</div>
	</xsl:template>
	<xsl:template match="ADDITIONPAYMENTDETAIL">
		<xsl:variable name="money" select="number(./MONEYAMOUNT)" />
		<tr>
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="position() mod 2 = 1">oddRow</xsl:when>
					<xsl:otherwise>evenRow</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<td class="borderRight"><xsl:value-of select="./CONTENTS" /></td>
			<td class="borderRight"><xsl:value-of disable-output-escaping="yes" select="./CONTENTSNOTE" /></td>
			<td class="noBorderRight" style="text-align: right">
				<xsl:value-of select="format-number($money,'###,###,###,###','myFormat')" />
			</td>
		</tr>
	</xsl:template>

	<xsl:template match="TEMPPAYMENT_APPLY">
		<xsl:if test="./@class = 'input'">
			<xsl:variable name="tempayAmount" select="number(./TEMPPAYMONEYAMOUNT)" />
			<xsl:if test="$tempayAmount != 0">
				<div class="contents margin">
					<span class="iap-default">仮払い</span>
					<table class="detail">
						<tr>
							<td class="label">現金仮払金額</td>
							<td class="item">
								<span class="iap-default">
									<xsl:value-of
										select="format-number($tempayAmount,'###,###,###,###','myFormat')" />
								</span>
								&#160;
							</td>
						</tr>
						<tr>
							<td class="label">口座振込</td>
							<td class="item">
								<span class="iap-default">
									<xsl:choose>
										<xsl:when
											test="./TRANSFERNECESSITYCLASS = '0002'">
											希望する
										</xsl:when>
										<xsl:when
											test="./TRANSFERNECESSITYCLASS = '0001'">
											希望なし
										</xsl:when>
									</xsl:choose>
								</span>
								&#160;
							</td>
						</tr>
					</table>
				</div>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template match="TEMPPAYMENT">
		<xsl:if test="./@class = 'pay'">
			<xsl:variable name="tempayAmount" select="number(./TEMPPAYMONEYAMOUNT)" />
			<xsl:variable name="totalAmount" select="sum(./OTHERTEMPPAY//MONEYAMOUNT)" />
			<xsl:if test="$tempayAmount != 0 or $totalAmount != 0 ">
				<div class="contents margin">
					<span class="iap-default">仮払い</span>
					<table class="detail">
						<tr>
							<td class="label">現金仮払金額</td>
							<td class="item">
								<span class="iap-default">
									<xsl:value-of
										select="format-number($tempayAmount,'###,###,###,###','myFormat')" />
								</span>
								&#160;
							</td>
						</tr>
					</table>
					<xsl:apply-templates select="./OTHERTEMPPAY" />
				</div>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template match="OTHERTEMPPAY">
	<xsl:variable name="totalAmount"
						select="sum(.//MONEYAMOUNT)" />
		<xsl:if test="$totalAmount != 0">
			<div class="margin">
				<span class="iap-default">その他仮払い</span>
				<table class="rich-table listHighlightRich">
					<tr class="rich-table-header">
						<td class="headBorderRight" width="auto" style="font-weight:bold;">仮払種別</td>
						<td class="headBorderRight" width="auto" style="font-weight:bold;">摘要</td>
						<td class="noBorderRight" width="auto" style="font-weight:bold;">金額</td>
					</tr>
					<xsl:apply-templates />
					<tr>
						<td class="headerBg borderRight" colspan="2">その他仮払合計</td>
						<td style="text-align: right" class="noBorderRight">
							<xsl:value-of select="format-number($totalAmount,'###,###,###,###','myFormat')" />
						</td>
					</tr>
				</table>
			</div>
		</xsl:if>
	</xsl:template>
	<xsl:template match="OTHERTEMPPAYDETAIL">
		<tr>
			<xsl:attribute name="class">
				<xsl:choose>
					<xsl:when test="position() mod 2 = 1">oddRow</xsl:when>
					<xsl:otherwise>evenRow</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<td class="borderRight">
				<xsl:value-of select="./TEMPPAYMENTITEMCLASSNAME" />
			</td>
			<td class="borderRight">
				<xsl:value-of disable-output-escaping="yes" select="./OUTLINE" />
			</td>
			<td class="noBorderRight" style="text-align: right">
				<xsl:variable name="preMoneyAmount" select="./MONEYAMOUNT" />
				<xsl:value-of select="format-number($preMoneyAmount,'###,###,###,###','myFormat')" />
			</td>
		</tr>
	</xsl:template>
	<!-- total -->
	<xsl:template match="TOTAL">
		<xsl:variable name="totalAmount" select="number(./TOTALMONEY)"/>
		<xsl:variable name="totalTemppay" select="number(./TOTALTEMPPAY)"/>
		<xsl:variable name="travelMoneyAmount" select="number(./TRAVELMONEYAMOUNT)"/>
		<div class="contents margin">
			<table class="detail">
				<tr>
					<td class="label">合計金額</td>
					<td class="item">
						<xsl:value-of
								select="format-number($totalAmount,'###,###,###,###','myFormat')" />
					</td>
				</tr>
				<tr>
					<td class="label">仮払合計金額</td>
					<td class="item">
						<xsl:value-of
								select="format-number($totalTemppay,'###,###,###,###','myFormat')" />
					</td>
				</tr>
				<tr>
					<td class="label">精算金額</td>
					<td class="item">
						<xsl:value-of
								select="format-number($travelMoneyAmount,'###,###,###,###','myFormat')" />
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
				<table class="dr-table rich-table listHighlightRich">
					<tr class="rich-table-header">
						<td colspan="2" width="auto" style="font-weight:bold;"
							class="headBorderRight">
							負担部門またはプロジェクト
						</td>
						<td width="auto" class="noBorderRight" style="font-weight:bold;">負担金額</td>
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
							<td class="borderRight">
								<span class="iap-default">
									<xsl:value-of select="./BILLABLENAME" />
								</span>
								&#160;
							</td>
							<td class="borderRight">
								<span class="iap-default">
									<xsl:value-of
										select="./BILLABLEDETAILNAME" />
								</span>
								&#160;
							</td>
							<td style="text-align: right"
								class="noBorderRight">
								<xsl:value-of
									select="format-number($billableamount,'###,###,###,###','myFormat')" />
							</td>
						</tr>
					</xsl:for-each>
					<tr >
						<td class="headerBg borderRight" colspan="2">
							負担合計
						</td>
						<td style="text-align: right"
							class="noBorderRight">
							<xsl:value-of
								select="format-number($totalbillableamount,'###,###,###,###','myFormat')" />
						</td>
					</tr>
				</table>
			</div>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
