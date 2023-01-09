<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
	<!-- ********************************************************************** -->
	<!--		精算処理系：出張申請　スタイルシート							-->
	<!-- ********************************************************************** -->
	<xsl:decimal-format name="myFormat" NaN="0" zero-digit="0"/>
	<!-- ***** ボディ ***** -->
	<xsl:template match="DOCUMENT">
		<span style="color:#FF0000">[申請内容]</span>
		<div class="contents">
			<span style="color:#FF0000">[出張種別]</span>
				<span>
					<xsl:value-of select="./BUSINESSTRIPCLASSNAME" />
					<xsl:value-of select="./BUSINESSTRIPBREAKDOWNCLASSNAME" />
				</span>
			<br/>
			<span style="color:#FF0000">[出発日時]</span>
				<span><xsl:value-of select="./DEPARTDATETIME" /></span><br/>
			<span style="color:#FF0000">[交通機関]</span>
				<span><xsl:value-of select="./DEPARTTRANSPORT" /></span><br/>
			<span style="color:#FF0000">[便名]</span>
				<span><xsl:value-of select="./DEPARTFLIGHTNAME" disable-output-escaping="yes"/></span><br/>
			<span style="color:#FF0000">[出発日時]</span>
				<span><xsl:value-of select="./ARRIVEDATETIME" /></span><br/>
			<span style="color:#FF0000">[交通機関]</span>
				<span><xsl:value-of select="./ARRIVETRANSPORT" /></span><br/>
			<span style="color:#FF0000">[便名]</span>
				<span><xsl:value-of select="./ARRIVEFLIGHTNAME" disable-output-escaping="yes"/></span><br/>
			<span style="color:#FF0000">[訪問先]</span>
				<span><xsl:value-of select="./VISITEDNAME" disable-output-escaping="yes"/></span>
				<br/>
			<span style="color:#FF0000">[理由]</span>
				<span>
					<xsl:value-of disable-output-escaping="yes" select="./PURPOSE" />
				</span>
				<br/>
			<xsl:if test="./REPORTMATTER != ''">
				<span style="color:#FF0000">[報告事項]</span>
				<span>
					<xsl:value-of disable-output-escaping="yes" select="./REPORTMATTER" />
				</span>
				<br/>
			</xsl:if>
			<!-- for 出張申請 -->
			<xsl:if test="./CONTENTS != ''">
			<span style="color:#FF0000">[備考]※連絡先、宿泊先</span>
				<span>
					<xsl:value-of disable-output-escaping="yes" select="./CONTENTS" />
				</span>
				<br/>
			</xsl:if>
			<!-- for 出張精算 -->
			<xsl:if test="./REPORTPOINTS != ''">
			<span style="color:#FF0000">[報告事項]</span>
				<span>
					<xsl:value-of disable-output-escaping="yes" select="./REPORTPOINTS" />
				</span>
				<br/>
			</xsl:if>
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
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
		<span style="color:#FF0000">[日当]</span>
			<span>
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
			</span>
			<br/>
		<span style="color:#FF0000">[宿泊代]</span>
			<span>
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

			</span>
			<br/>
		<span style="color:#FF0000">[宿泊時領収書]</span>
			<span>
			<xsl:choose>
			      <xsl:when test="@overnight = '0001'">
			        <span>無</span>
			      </xsl:when>
			      <xsl:when test="@overnight = '0002'">
			        <span>有</span>
			      </xsl:when>
			</xsl:choose>
			</span>
			<br/>
		<span style="color:#FF0000">[食事代]</span>
			<span>
				<xsl:value-of select="format-number($foodAmount,'###,###,###,###','myFormat')" />
			</span>
			<br/>
	</xsl:template>

	<xsl:template match="TRAVELDETAILS">
		<xsl:variable name="totalMoneyAmount"
						select="sum(.//MONEYAMOUNT)" />
		<div class="contents margin">
			<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
			<xsl:apply-templates />
			<span style="color:#FF0000">[交通費合計]</span>
			<span>
				<xsl:value-of select="format-number($totalMoneyAmount,'###,###,###,###','myFormat')" />
			</span><br/>
		</div>
	</xsl:template>
	<xsl:template match="TRAVELDETAIL">
		<xsl:variable name="moneyAmount"
						select="number(./MONEYAMOUNT)" />
		<span>交通費明細(<xsl:value-of select="position()"/>)</span><br/>
		<span style="color:#FF0000">[使用日]</span>
			<span><xsl:value-of select="./PAIDDATE"/></span><br/>
		<span style="color:#FF0000">[交通機関]</span>
			<span><xsl:value-of select="./TRANSPORTMODE"/></span><br/>
		<span style="color:#FF0000">[出発地]</span>
			<span><xsl:value-of select="./DEPARTFROM" disable-output-escaping="yes"/></span><br/>
		<span style="color:#FF0000">[到着地]</span>
			<span><xsl:value-of select="./ARRIVETO" disable-output-escaping="yes"/></span><br/>
		<span style="color:#FF0000">[金額]</span>
			<span>
				<xsl:value-of select="format-number($moneyAmount,'###,###,###,###','myFormat')" />
			</span><br/>
			<span style="color:#FF0000">[領収書]</span>
			<span>
				<xsl:choose>
					<xsl:when test="@class = '0001'">無</xsl:when>
					<xsl:when test="@class = '0002'">有</xsl:when>
					<xsl:otherwise />
				</xsl:choose>
			</span>
			<br/><br/>
	</xsl:template>

	<xsl:template match="ADDITIONPAYMENT">
		<xsl:variable name="totalAmount" select="sum(.//MONEYAMOUNT)" />
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
		<div class="contents margin">

			<xsl:apply-templates />

			<span style="color:#FF0000">[その他合計]</span>
			<span>
				<xsl:value-of select="format-number($totalAmount,'###,###,###,###','myFormat')" />
			</span>
		</div>
	</xsl:template>
	<xsl:template match="ADDITIONPAYMENTDETAIL">
		<xsl:variable name="money" select="number(./MONEYAMOUNT)" />
			<span>その他(<xsl:value-of select="position()"/>)</span><br/>
			<span style="color:#FF0000">[出金内容]</span>
			<span><xsl:value-of select="./CONTENTS" disable-output-escaping="yes"/></span>
			<span><xsl:value-of select="./CONTENTSNOTE" disable-output-escaping="yes"/></span>
			<br/>
			<span style="color:#FF0000">[金額]</span>
			<span>
				<xsl:value-of select="format-number($money,'###,###,###,###','myFormat')" />
			</span>
			<br/><br/>
	</xsl:template>

	<xsl:template match="TEMPPAYMENT_APPLY">
		<xsl:if test="./@class = 'input'">
			<xsl:variable name="tempayAmount" select="number(./TEMPPAYMONEYAMOUNT)" />
			<xsl:if test="$tempayAmount != 0">
				<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
				<div class="contents margin">
					<span>仮払い</span>
					<br/>
					<span style="color:#FF0000">[現金仮払金額]</span>
						<span>
							<xsl:value-of
								select="format-number($tempayAmount,'###,###,###,###','myFormat')" />
						</span>
					<br/>
					<span style="color:#FF0000">[口座振込]</span>
					<span>
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
				</div>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template match="TEMPPAYMENT">
		<xsl:if test="./@class = 'pay'">
			<xsl:variable name="tempayAmount" select="number(./TEMPPAYMONEYAMOUNT)" />
			<xsl:variable name="totalAmount" select="sum(./OTHERTEMPPAY//MONEYAMOUNT)" />
			<xsl:if test="$tempayAmount != 0 or $totalAmount != 0 ">
				<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
				<div class="contents margin">
					<span>仮払い</span>
					<br/>
					<span style="color:#FF0000">[現金仮払金額]</span>
						<span>
							<xsl:value-of
								select="format-number($tempayAmount,'###,###,###,###','myFormat')" />
						</span>
					<br/><br/>
					<xsl:apply-templates select="./OTHERTEMPPAY" />
				</div>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template match="OTHERTEMPPAY">
		<xsl:variable name="totalAmount" select="sum(.//MONEYAMOUNT)" />
		<xsl:if test="$totalAmount != 0">
			<div class="margin">
				<xsl:apply-templates />
				<span style="color:#FF0000">[その他仮払合計]</span>
				<span>
					<xsl:value-of select="format-number($totalAmount,'###,###,###,###','myFormat')" />
				</span>
			</div>
		</xsl:if>
	</xsl:template>
	<xsl:template match="OTHERTEMPPAYDETAIL">
		<span>その他仮払い(<xsl:value-of select="position()"/>)</span>
		<br/>
		<span style="color:#FF0000">[仮払種別]</span>
		<span>
			<xsl:value-of select="./TEMPPAYMENTITEMCLASSNAME" />
		</span><br/>
		<span style="color:#FF0000">[摘要]</span>
		<span>
			<xsl:value-of select="./OUTLINE" disable-output-escaping="yes"/>
		</span><br/>
		<span style="color:#FF0000">[金額]</span>
		<span>
			<xsl:variable name="preMoneyAmount" select="./MONEYAMOUNT" />
			<xsl:value-of select="format-number($preMoneyAmount,'###,###,###,###','myFormat')" />
		</span><br/><br/>
	</xsl:template>
	<!-- total -->
	<xsl:template match="TOTAL">
		<xsl:variable name="totalAmount" select="number(./TOTALMONEY)"/>
		<xsl:variable name="totalTemppay" select="number(./TOTALTEMPPAY)"/>
		<xsl:variable name="travelMoneyAmount" select="number(./TRAVELMONEYAMOUNT)"/>
		<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
		<span style="color:#FF0000">[合計金額]</span>
		<span>
			<xsl:value-of select="format-number($totalAmount,'###,###,###,###','myFormat')" />
		</span><br/>
		<span style="color:#FF0000">[仮払合計金額]</span>
		<span>
			<xsl:value-of select="format-number($totalTemppay,'###,###,###,###','myFormat')" />
		</span><br/>
		<span style="color:#FF0000">[精算金額]</span>
		<span>
			<xsl:value-of select="format-number($travelMoneyAmount,'###,###,###,###','myFormat')" />
		</span><br/>
	</xsl:template>

	<xsl:template match="BILLABLEDETAILS">
		<xsl:variable name="billCount" select="count(./BILLABLEDETAIL)" />
		<xsl:if test="$billCount &gt; 0">
			<hr color="#ADD8E6" size="1" style="border-style:solid;border-color:#ADD8E6;background-color:#ADD8E6;height:1;" />
			<div class="contents margin">
				<xsl:variable name="totalbillableamount" select="sum(.//BILLABLEAMOUNT)" />
				<xsl:for-each select="BILLABLEDETAIL">
					<xsl:variable name="billableamount"
							select="number(./BILLABLEAMOUNT)" />
					<span>費用の負担(<xsl:value-of select="position()"/>)</span>
					<br/>
					<span style="color:#FF0000">[負担部門またはプロジェクト]</span>
						<span>
							<xsl:value-of select="./BILLABLENAME" />
							<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;]]></xsl:text>
							<xsl:value-of select="./BILLABLEDETAILNAME" />
						</span>
					<br/>
					<span style="color:#FF0000">[負担金額]</span>
					<span>
						<xsl:value-of select="format-number($billableamount,'###,###,###,###','myFormat')" />
					</span>
					<br/><br/>
				</xsl:for-each>
				<span style="color:#FF0000">[負担合計]</span>
				<span>
					<xsl:value-of select="format-number($totalbillableamount,'###,###,###,###','myFormat')" />
				</span>
			</div>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
