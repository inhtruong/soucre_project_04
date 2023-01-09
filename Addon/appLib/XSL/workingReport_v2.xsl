<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>
	<xsl:template match="DOCUMENT">
		<script type="text/javascript" src="../script/CommonFunction-utf8.js">//</script>
		<div class="margin">
			<span>申請内容</span>
		</div>
		<div id="FIRST_DIV_ON_P_OUTPUT_EMP_popup" class="popupMenu">
			<ul id="FIRST_DIV_ON_P_OUTPUT_EMP_ul"></ul>
		</div>
		<xsl:apply-templates select="./WORKINGREPORT_CONTENT" />
		<xsl:apply-templates select="./CONSULT_CONTENT" />
		<xsl:apply-templates select="./CLAIM_CONTENT" />
		<xsl:apply-templates select="./RFM_USERFORMAT" />
		<xsl:apply-templates select="./MIGRATE_DATA" />
		<xsl:apply-templates select="./REPORT_PLACE" />
	</xsl:template>

	<!-- 商談内容  -->
	<xsl:template match="WORKINGREPORT_CONTENT">
		<div class="contents">
			<span>業務報告内容</span>
			<table class="detail">
				<tr>
					<td class="label">日付</td>
					<td class="item">
						<xsl:value-of select="./WR_DATE" />
					</td>
				</tr>
				<tr>
					<td class="label">時刻</td>
					<td class="item">
						<xsl:value-of select="./WR_TIME" />
					</td>
				</tr>
				<tr>
					<td class="label">場所</td>
					<td class="item">
						<xsl:value-of select="./WR_PLACE" disable-output-escaping="yes"/>
					</td>
				</tr>
				<tr>
					<td class="label">場所詳細</td>
					<td class="item">
						<xsl:value-of select="./WR_PLACE_DETAIL" disable-output-escaping="yes"/>
					</td>
				</tr>
				<tr>
					<td class="label">業務種別</td>
					<td class="item">
						<xsl:value-of select="./WR_TASK_TYPE" disable-output-escaping="yes"/>
					</td>
				</tr>
				<xsl:if test="./SEL_OBJ_NAME and string(./SEL_OBJ_NAME)">
					<tr>
						<td class="label">業務内容</td>
						<td class="item">
							<xsl:variable name="funcCode" select="./SEL_OBJ_NAME/@code" />
							<xsl:variable name="type" select="./SEL_OBJ_NAME/@taskType" />
							<xsl:variable name="corpId" select="./SEL_OBJ_NAME/@corpId" />
							<xsl:variable name="id" select="./SEL_OBJ_NAME/@id" />
							<xsl:element name="a">
								<xsl:attribute name="class">anchor noline</xsl:attribute>
								<xsl:if test="$type = 1">
									<xsl:attribute name="href">javascript:fncDisplayConsult('<xsl:value-of select="$funcCode" />', '<xsl:value-of select="$id" />','<xsl:value-of select="$corpId" />');</xsl:attribute>
								</xsl:if>
								<xsl:if test="$type = 3">
									<xsl:attribute name="href">javascript:fncDisplayClaim('<xsl:value-of select="$funcCode" />', '<xsl:value-of select="$id" />','<xsl:value-of select="$corpId" />');</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="./SEL_OBJ_NAME" disable-output-escaping="yes"/>
							</xsl:element>
						</td>
					</tr>
				</xsl:if>
				<tr>
					<td class="label">件名</td>
					<td class="item">
						<xsl:value-of select="./WR_TITLE" disable-output-escaping="yes"/>
					</td>
				</tr>
				<xsl:apply-templates select="./ENTERPRISE" />
				<xsl:if test="./WR_COMMODITY_NAME">
					<tr>
						<td class="label">対象商品</td>
						<td class="item">
							<xsl:value-of select="./WR_COMMODITY_NAME" disable-output-escaping="yes"/>
						</td>
					</tr>
				</xsl:if>
				<tr>
					<td class="label">報告事項</td>
					<td class="item">
						<xsl:value-of select="./WR_REPORT_POINTS" disable-output-escaping="yes" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="CONSULT_CONTENT">
		<div class="contents margin">
			<table class="detail">
				<tr>
					<td class="label">商談状況</td>
					<td class="item">
						<xsl:value-of select="./NR_STATUS" />
					</td>
				</tr>
				<tr>
					<td class="label">商談ランク</td>
					<td class="item">
						<xsl:value-of select="./NR_RANK_NAME" />
					</td>
				</tr>
				<tr>
					<td class="label">受注見込額</td>
					<td class="item">
						<xsl:value-of select="./NR_ORDER_EXP_AMOUNT" />
					</td>
				</tr>
				<tr>
					<td class="label">利益見込額</td>
					<td class="item">
						<xsl:value-of select="./NR_EXP_PROF_AMOUNT" />
					</td>
				</tr>
				<tr>
					<td class="label">受注予定日</td>
					<td class="item">
						<xsl:value-of select="./NR_ORDER_DATE" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<xsl:template match="CLAIM_CONTENT">
		<div class="contents margin">
			<table class="detail">
				<tr>
					<td class="label">対応状況</td>
					<td class="item">
						<xsl:value-of select="./CR_STATUS_NAME" />
					</td>
				</tr>
				<tr>
					<td class="label">原因</td>
					<td class="item">
						<xsl:value-of select="./CR_CAUSE" />
					</td>
				</tr>
				<tr>
					<td class="label">今回発生費用</td>
					<td class="item">
						<xsl:value-of select="./CR_DAMAGES_INC" />
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>

	<xsl:template match="ENTERPRISE">
		<tr>
			<td class="label">顧客名</td>
			<td class="item">
				<xsl:if test="string-length(./ENTERPRISE_ID/text())>0">
					<div>
						<xsl:element name="a">
							<xsl:attribute name="class">anchor noline</xsl:attribute>
							<xsl:attribute name="href">javascript:fncDisplayEnterprise('<xsl:value-of select="ENTERPRISE_ID" />');</xsl:attribute>
							<xsl:value-of select="ENTERPRISE_NAME" disable-output-escaping="yes"/>
						</xsl:element>
					</div>
				</xsl:if>
				<xsl:if test="string-length(./ENTERPRISE_POST_ID/text())>0">
					<div>
						<xsl:element name="a">
							<xsl:attribute name="class">anchor noline</xsl:attribute>
							<xsl:attribute name="href">javascript:fncDisplayEnterprisePost('<xsl:value-of select="ENTERPRISE_ID" />','<xsl:value-of select="ENTERPRISE_POST_ID" />')</xsl:attribute>
							<xsl:value-of select="ENTERPRISE_POST_NAME" disable-output-escaping="yes"/>
						</xsl:element>
					</div>
				</xsl:if>
				<xsl:if test="string-length(./ENTERPRISE_PERSON_ID/text())>0">
					<div>
						<xsl:element name="a">
							<xsl:attribute name="class">anchor noline</xsl:attribute>
							<xsl:attribute name="href">javascript:fncDisplayEnterprisePerson('<xsl:value-of select="ENTERPRISE_ID" />','<xsl:value-of select="ENTERPRISE_POST_ID" />','<xsl:value-of select="ENTERPRISE_PERSON_ID" />')</xsl:attribute>
							<xsl:value-of select="ENTERPRISE_PERSON_NAME" disable-output-escaping="yes"/>
						</xsl:element>
					</div>
				</xsl:if>

				<xsl:if test="string-length(./WR_OTHER_RESP_NAME/text())>0">
					<div>
						<xsl:element name="a">
							<xsl:attribute name="class">item</xsl:attribute>
						</xsl:element>
						<xsl:value-of select="./WR_OTHER_RESP_NAME" disable-output-escaping="yes"/>
					</div>
				</xsl:if>
			</td>
		</tr>
	</xsl:template>

	<xsl:template match="REPORT_PLACE">
		<xsl:variable name="contextPath" select="./CONTEXTPATH"/>
		<div class="contents margin">
			<span>報告先</span>
			<table class="detail">
				<tr>
					<td class="label">同報配信先</td>
					<td class="item">

					<xsl:for-each select="./SHARE_EMP">
						<xsl:variable name="empId" select="./@id"/>
						<xsl:variable name="empName" select="./@name"/>
						<xsl:element name="span">
							<xsl:attribute name="class" >anchor noline</xsl:attribute>
							<xsl:attribute name="onclick">javascript:fncLoadEmployeeMenu(event, 'FIRST_DIV_ON_P_OUTPUT_EMP', '<xsl:value-of select="$empId"/>', '<xsl:value-of select="$contextPath"/>', 'right')</xsl:attribute>
							<xsl:element name="img">
								<xsl:attribute name="src">../img/b_emp.png</xsl:attribute>
								<xsl:attribute name="alt">
									<xsl:choose>
										<xsl:when test="string-length(./SHARE_EMP_NAME/text())=0">
											<xsl:value-of select="." disable-output-escaping="yes"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$empName" disable-output-escaping="yes"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
							</xsl:element>
							<xsl:value-of select="." disable-output-escaping="yes"/>
							<xsl:if test="position() != last()">
								<br />
							</xsl:if>
						</xsl:element>
					</xsl:for-each>&#160;
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>


	<xsl:template match="RFM_USERFORMAT">
		<div class="contents margin">
			<table class="detail">
				<tr>
				<xsl:apply-templates />
				<xsl:if test="string-length(./text())>0">
					<xsl:text disable-output-escaping="yes">&lt;/td&gt;</xsl:text>
				</xsl:if>
				</tr>
			</table>
		</div>
	</xsl:template>

	<!-- ///// 項目情報のテンプレート ///// -->
	<xsl:template match="RFM_ITEM">
		<!-- ///// 先頭項目「以外」の場合 ///// -->
		<xsl:if test="current() != ../RFM_ITEM[1]">
			<!-- 改行フラグが yes or テキストエリア の場合 -->
			<xsl:if test="@nl='yes' or @type='11'">
				<xsl:text disable-output-escaping="yes">
					&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;
				</xsl:text>
			</xsl:if>
		</xsl:if>
		<xsl:if test="current() = ../RFM_ITEM[1]">
			<xsl:if test="@nl='no' and @type != '11'">
				<xsl:text disable-output-escaping="yes">
					&lt;td&gt;
				</xsl:text>
			</xsl:if>
		</xsl:if>

		<!-- ///// 主要タグ生成 ///// -->
		<xsl:choose>
			<!-- ///// テキストエリアの場合（TABLEタグのネストで構成） ///// -->
			<xsl:when test="@type='11'">
				<xsl:choose>
					<!-- ///// 改行フラグがyes、前リテラル後改行がyesの場合 ///// -->
					<xsl:when test="@nl='yes' and @flnl='yes'">
						<xsl:text disable-output-escaping="yes">
								&lt;td class="label"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_FL" /><br/>
						<xsl:text disable-output-escaping="yes">
								&lt;/td&gt;
							&lt;/tr&gt;
							&lt;tr&gt;
								&lt;td class="label"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
								&lt;td class="item"&gt;
									&lt;table border="0" cellspacing="0"&gt;
										&lt;tr&gt;
											&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
											&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_BL" /><br/>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
										&lt;/tr&gt;
									&lt;/table&gt;
						</xsl:text>
					</xsl:when>
					<!-- ///// 改行フラグがyes、前リテラル後改行がnoの場合 ///// -->
					<xsl:when test="@nl='yes' and @flnl='no'">
						<xsl:text disable-output-escaping="yes">
								&lt;td class="label"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_FL" /><br/>
						<xsl:text disable-output-escaping="yes">
								&lt;/td&gt;
								&lt;td class="item"&gt;
									&lt;table border="0" cellspacing="0"&gt;
										&lt;tr&gt;
											&lt;td&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
											&lt;td&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_BL" /><br/>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
										&lt;/tr&gt;
									&lt;/table&gt;
						</xsl:text>
					</xsl:when>
					<!-- ///// 改行フラグがnoの場合, 前リテラル後改行がnoの場合 ///// -->
					<xsl:when test="@nl='no' and @flnl='no'">
						<xsl:text disable-output-escaping="yes">
							&lt;td&gt;<![CDATA[&#160;]]>&lt;span
						</xsl:text>
						<xsl:if test="string-length(./RFM_FL/text())=0">
							<xsl:text disable-output-escaping="yes">&gt;<![CDATA[&#160;]]></xsl:text>
						</xsl:if>
						<xsl:if test="not(string-length(./RFM_FL/text())=0)">
						<xsl:text disable-output-escaping="yes"> class="ufLiteral headerBg"&gt;</xsl:text>
							<xsl:apply-templates select="./RFM_FL" />
						</xsl:if>
						<xsl:text disable-output-escaping="yes">
							&lt;/span&gt;<![CDATA[&#160;]]>
								&lt;table border="0" cellspacing="0"&gt;
									&lt;tr&gt;
										&lt;td&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes">
										&lt;/td&gt;&lt;td&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_BL" /><br/>
						<xsl:text disable-output-escaping="yes">
											&lt;/td&gt;
										&lt;/tr&gt;
									&lt;/table&gt;
						</xsl:text>
					</xsl:when>
					<!-- ///// 改行フラグがnoの場合, 前リテラル後改行がyesの場合 ///// -->
					<xsl:otherwise>
						<xsl:text disable-output-escaping="yes">
							&lt;td&gt;<![CDATA[&#160;]]>&lt;span
						</xsl:text>
						<xsl:if test="string-length(./RFM_FL/text())=0">
							<xsl:text disable-output-escaping="yes">&gt;<![CDATA[&#160;]]></xsl:text>
						</xsl:if>
						<xsl:if test="not(string-length(./RFM_FL/text())=0)">
						<xsl:text disable-output-escaping="yes"> class="ufLiteral headerBg"&gt;</xsl:text>
							<xsl:apply-templates select="./RFM_FL" />
						</xsl:if>
						<xsl:text disable-output-escaping="yes">
							&lt;/span&gt;&lt;/td&gt;
							&lt;/tr&gt;&lt;tr&gt;&lt;td class="label"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
							&lt;td class="item"&gt;
								&lt;table border="0" cellspacing="0"&gt;
									&lt;tr&gt;
										&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes">
										&lt;/td&gt;&lt;td class="item"&gt;
						</xsl:text>
						<xsl:if test="string-length(./RFM_BL/text())=0">
							<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						</xsl:if>
						<xsl:apply-templates select="./RFM_BL" />
						<xsl:text disable-output-escaping="yes">
										&lt;/td&gt;
									&lt;/tr&gt;
								&lt;/table&gt;
						</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- ///// テキストエリア以外の場合 ///// -->
			<xsl:when test="@type = '2000'">
				<xsl:value-of select="./RFM_DISPLAY" disable-output-escaping="yes" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<!-- ///// 改行フラグがyes、前リテラル後改行がyesの場合 ///// -->
					<xsl:when test="@nl='yes' and @flnl='yes'">
						<xsl:text disable-output-escaping="yes">
							&lt;td class="label"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_FL" /><br/>
						<xsl:text disable-output-escaping="yes">
							&lt;/td&gt;&lt;td class="item"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
							&lt;/tr&gt;&lt;tr&gt;&lt;td class="label"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
							&lt;td class="item" &gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_BL" />
					</xsl:when>
					<!-- ///// 改行フラグがyes、前リテラル後改行がnoの場合 ///// -->
					<xsl:when test="@nl='yes' and @flnl='no'">
						<xsl:text disable-output-escaping="yes">
							&lt;td class="label"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_FL" /><br/>
						<xsl:text disable-output-escaping="yes">
							&lt;/td&gt;&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_BL" />
					</xsl:when>
					<!-- ///// 改行フラグがnoの場合 , 前リテラル後改行がnoの場合///// -->
					<xsl:when test="@nl='no' and @flnl='no'">
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;&#160;&#160;&#160;]]>&lt;span </xsl:text>
						<xsl:if test="string-length(./RFM_FL/text())=0">
							<xsl:text disable-output-escaping="yes">&gt;<![CDATA[&#160;]]></xsl:text>
						</xsl:if>
						<xsl:if test="not(string-length(./RFM_FL/text())=0)">
						<xsl:text disable-output-escaping="yes"> class="ufLiteral headerBg"&gt;</xsl:text>
							<xsl:apply-templates select="./RFM_FL" />
						</xsl:if>
						<xsl:text disable-output-escaping="yes">&lt;/span&gt;<![CDATA[&#160;&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_BL" />
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;&#160;&#160;&#160;]]>&lt;span </xsl:text>
						<xsl:if test="string-length(./RFM_FL/text())=0">
							<xsl:text disable-output-escaping="yes">&gt;<![CDATA[&#160;]]></xsl:text>
						</xsl:if>
						<xsl:if test="not(string-length(./RFM_FL/text())=0)">
						<xsl:text disable-output-escaping="yes"> class="ufLiteral headerBg"&gt;</xsl:text>
							<xsl:apply-templates select="./RFM_FL" />
						</xsl:if>
						<xsl:text disable-output-escaping="yes">
							&lt;/span&gt;&lt;/td&gt;
							&lt;/tr&gt;&lt;tr&gt;&lt;td class="label"&gt;<![CDATA[&#160;]]>&lt;/td&gt;
							&lt;td class="item"&gt;
						</xsl:text>
						<xsl:apply-templates select="./RFM_DATA">
							<xsl:with-param name="dtype" select="@type"></xsl:with-param>
						</xsl:apply-templates>
						<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
						<xsl:apply-templates select="./RFM_BL" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

<!-- ///// 項目値のテンプレート ///// -->
	<xsl:template match="RFM_DATA">
		<!-- ///// RFM_ITEMのtype属性値を、パラメタで受け取る ///// -->
		<xsl:param name="dtype" />
		<!-- ///// 各項目の最初以外のデータは改行してから表示する ///// -->
		<xsl:choose>
			<xsl:when test="@cnt='0'"></xsl:when>
			<xsl:otherwise><br/></xsl:otherwise>
		</xsl:choose>
		<!-- ///// コード出力と名称出力を、明細形式で振分 ///// -->
		<xsl:choose>
			<xsl:when test="$dtype='21'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='22'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='23'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='24'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='25'">
				<xsl:apply-templates select="./RFM_CODE"/>
				<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
				<xsl:apply-templates select="./RFM_NAME" />
			</xsl:when>
			<xsl:when test="$dtype='31'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='51'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='52'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='53'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:when test="$dtype='54'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="./RFM_CODE">
					<xsl:with-param name="dtype2" select="$dtype"></xsl:with-param>
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ///// コード値のテンプレート ///// -->
	<xsl:template match="RFM_CODE">
		<!-- ///// RFM_ITEMのtype属性値を、パラメタで受け取る ///// -->
		<xsl:param name="dtype2" />
		<xsl:choose>
			<xsl:when test="@edit='yes' and not(string-length(./text())=0)">
				<!-- ///// コード値の編集 ///// -->
				<xsl:choose>
					<!-- ///// 文字テキストボックス ＵＲＬ編集 ///// -->
					<xsl:when test="$dtype2='1'">
						<xsl:element name="a">
							<xsl:attribute name="href">javascript:void(0)</xsl:attribute>
							<xsl:attribute name="onclick">window.open('<xsl:apply-templates />','','status=yes,toolbar=yes,menubar=yes,location=yes,scrollbars=yes,resizable=yes')</xsl:attribute>
							<xsl:value-of select="." disable-output-escaping="yes" />
						</xsl:element>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$dtype2='1' or $dtype2='11'">
						<xsl:value-of select="." disable-output-escaping="yes" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- 前リテラルのテンプレート -->
	<xsl:template match="RFM_FL">
		<xsl:choose>
			<xsl:when test="not(string-length(./text())=0)">
				<xsl:value-of select="." disable-output-escaping="yes" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ///// 後リテラルのテンプレート ///// -->
	<xsl:template match="RFM_BL">
		<xsl:choose>
			<xsl:when test="not(string-length(./text())=0)">
				<xsl:text disable-output-escaping="yes"><![CDATA[&#160;&#160;]]></xsl:text>
				<xsl:value-of select="." disable-output-escaping="yes" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:text disable-output-escaping="yes"><![CDATA[&#160;]]></xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ///// 名称値のテンプレート ///// -->
	<xsl:template match="RFM_NAME">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="MIGRATE_DATA">
		<xsl:if test="string-length(./text())>0">
			<div class="contents margin">
				<table class="detail">
					<tr>
						<td class="label"><xsl:value-of select="./LABEL" disable-output-escaping="yes" /></td>
						<td class="item">
							<xsl:value-of select="./CONTENT" disable-output-escaping="yes" />
						</td>
					</tr>
				</table>
			</div>
		</xsl:if>	
	</xsl:template>

</xsl:stylesheet>