<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

	<xsl:template match="DOCUMENT">
		<xsl:apply-templates select="./RFM_USERFORMAT" />
		<xsl:apply-templates select="./RRDFREEFORM" />
		<xsl:apply-templates select="./RRDOFFICE" />
		<xsl:apply-templates select="./externalDocument" />
		<xsl:apply-templates select="./RRDATTACHMENTCOMMENT" />
		<xsl:apply-templates select="./RRDBBS" />
	</xsl:template>

	<!-- フリー域のテンプレート -->
	<xsl:template match="RRDFREEFORM">
		<xsl:if test="string-length(./text())>0">
		<xsl:if test="@title">
			<div><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="../RFM_USERFORMAT">
				<div class="contents margin">
					<xsl:value-of select="." disable-output-escaping="yes" />
					<div style="clear:both;height:1px"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></div>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="contents">
					<xsl:value-of select="." disable-output-escaping="yes" />
					<div style="clear:both;height:1px"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></div>
				</div>
			</xsl:otherwise>
		</xsl:choose>
		</xsl:if>
	</xsl:template>

<!-- attachment, paperDocument -->
	<xsl:template match="RRDATTACHMENTCOMMENT">
		<xsl:if test="string-length(./text())>0">
		<xsl:if test="@title">
			<div><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="../RFM_USERFORMAT">
				<div class="contents margin">
					<table class="detail">
						<tr>
							<td class="label">Paper attachement</td>
							<td class="item">
								<xsl:value-of select="." disable-output-escaping="yes" />
							</td>
						</tr>
					</table>
				</div>
			</xsl:when>
			<xsl:when test="../RRDFREEFORM">
				<div class="contents margin">
					<table class="detail">
						<tr>
							<td class="label">Paper attachement</td>
							<td class="item">
								<xsl:value-of select="." disable-output-escaping="yes" />
							</td>
						</tr>
					</table>
				</div>
			</xsl:when>
			<xsl:when test="../RRDOFFICE">
				<div class="contents margin">
					<table class="detail">
						<tr>
							<td class="label">Paper attachement</td>
							<td class="item">
								<xsl:value-of select="." disable-output-escaping="yes" />
							</td>
						</tr>
					</table>
				</div>
			</xsl:when>
			<xsl:when test="../externalDocument">
				<div class="contents margin">
					<table class="detail">
						<tr>
							<td class="label">Paper attachement</td>
							<td class="item">
								<xsl:value-of select="." disable-output-escaping="yes" />
							</td>
						</tr>
					</table>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="contents">
					<table class="detail">
						<tr>
							<td class="label">Paper attachement</td>
							<td class="item">
								<xsl:value-of select="." disable-output-escaping="yes" />
							</td>
						</tr>
					</table>
				</div>
			</xsl:otherwise>
		</xsl:choose>
		</xsl:if>
	</xsl:template>


	<!-- BBS -->
	<xsl:template match="RRDBBS">
		<xsl:if test="string-length(./text())>0">
		<xsl:if test="@title">
			<div><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="../RRDBBS">
				<div class="contents margin">
					<table class="detail">
						<!--
						<tr>
							<td class="label">Employee responsible for posting</td>
							<td class="item">
								<xsl:value-of select="./CHARGEEMPID" disable-output-escaping="yes" />
							</td>
						</tr>
						 -->
						<tr>
							<td class="label">Employee responsible for posting</td>
							<td class="item">
								<xsl:value-of select="./CHARGEEMPNAME" disable-output-escaping="yes" />
							</td>
						</tr>
						<tr>
							<td class="label">Posting period</td>
							<td class="item">
								<xsl:value-of select="./DISPSTARTDATE" disable-output-escaping="yes" />
								-
								<xsl:value-of select="./DISPENDDATE" disable-output-escaping="yes" />
							</td>
						</tr>
						<tr>
							<td class="label">NaviView notice period</td>
							<td class="item">
								<xsl:choose>
									<xsl:when test="./NAVIVIEWDISPOFF">
										<xsl:value-of select="./NAVIVIEWDISPOFF" disable-output-escaping="yes" />
									</xsl:when>
									<xsl:when test="./NAVIVIEWDISPON">
										<table class="detail">
											<tr>
												<td colspan="2"><xsl:value-of select="./NAVIVIEWDISPON" disable-output-escaping="yes" /></td>
											</tr>
											<tr>
												<td colspan="2"><xsl:value-of select="./NAVIBBSDISP" disable-output-escaping="yes" /></td>
											</tr>
											<xsl:choose>
												<xsl:when test="./NAVIBBSDISPSTARTDATE">
													<tr>
														<td class="label">Notice period</td>
														<td class="item">
															<xsl:value-of select="./NAVIBBSDISPSTARTDATE" disable-output-escaping="yes" />
															-
															<xsl:value-of select="./NAVIBBSDISPENDDATE" disable-output-escaping="yes" />
														</td>
													</tr>
												</xsl:when>
											</xsl:choose>
											<tr>
												<td colspan="2"><xsl:value-of select="./SCHEDULEBBSDISP" disable-output-escaping="yes" /></td>
											</tr>
											<xsl:choose>
												<xsl:when test="./NAVIEVENTDISPSTARTDATE">
													<tr>
														<td class="label">Notice period</td>
														<td class="item">
															<xsl:value-of select="./NAVIEVENTDISPSTARTDATE" disable-output-escaping="yes" />
															-
															<xsl:value-of select="./NAVIEVENTDISPENDDATE" disable-output-escaping="yes" />
														</td>
													</tr>
													<tr>
														<td class="label">Event period</td>
														<td class="item">
															<xsl:choose>
																<xsl:when test="./HOLDINGFROMDATE">
																	<xsl:value-of select="./HOLDINGFROMDATE" disable-output-escaping="yes" />
																	-
																	<xsl:value-of select="./HOLDINGTODATE" disable-output-escaping="yes" />
																</xsl:when>
															</xsl:choose>
														</td>
													</tr>
													<tr>
														<td class="label">Event location</td>
														<td class="item">
															<xsl:value-of select="./HOLDINGPLACE" disable-output-escaping="yes" />
														</td>
													</tr>
												</xsl:when>
											</xsl:choose>
										</table>
									</xsl:when>
								</xsl:choose>
							</td>
						</tr>
						<tr>
							<td class="label">Post board</td>
							<td class="item">
								<xsl:value-of select="./BBSID" disable-output-escaping="yes" />
							</td>
						</tr>
					</table>
				</div>
			</xsl:when>
		</xsl:choose>
		</xsl:if>
	</xsl:template>

	<!-- ///// ルート要素（フォーマット情報）のテンプレート ///// -->
	<xsl:template match="RFM_USERFORMAT">
	<xsl:if test="@title">
			<div><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
		</xsl:if>
		<div class="contents">
			<table class="detail">
<colgroup>
<col width="100" />
<col />
</colgroup>
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
		<xsl:if test="position()!=1">
			<!-- 改行フラグが yes or テキストエリア の場合 or AccessoryItem -->
			<xsl:if test="@nl='yes' or @type='1111' or @type='2000'">
				<xsl:text disable-output-escaping="yes">
					&lt;/td&gt;&lt;/tr&gt;&lt;tr&gt;
				</xsl:text>
			</xsl:if>
		</xsl:if>
		<xsl:if test="position()=1">
			<xsl:if test="@nl='no' and @type != '1111'">
				<xsl:text disable-output-escaping="yes">
					&lt;td&gt;
				</xsl:text>
			</xsl:if>
		</xsl:if>

		<!-- ///// 主要タグ生成 ///// -->
		<xsl:choose>
			<!-- ///// テキストエリアの場合（TABLEタグのネストで構成） ///// -->
			<xsl:when test="@type='1111'">
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
							<xsl:text disable-output-escaping="yes">&lt;/span&gt;<![CDATA[&#160;&#160;]]></xsl:text>
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
							<xsl:attribute name="href"><xsl:apply-templates /></xsl:attribute> 
                            <xsl:attribute name="target">rfm_window</xsl:attribute>
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


	<!-- StraForm域のテンプレート -->
	<xsl:template match="externalDocument">
		<xsl:choose>
			<xsl:when test="../RFM_USERFORMAT">
				<div class="margin">
					<xsl:if test="@title">
						<div class="applicationNameDummyClass1"><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
					</xsl:if>
					<table class="detail">
					<tr><td colspan="2">
						<xsl:element name="iframe">
							<xsl:attribute name="name">
								<xsl:text>f_straform</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="id">
								<xsl:text>f_straform</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="src">
								<xsl:text>../servlet/net.poweregg.web.app.rrd.RRD1323?form=</xsl:text>
								<xsl:value-of select="./formId" /><xsl:text>&amp;docid=</xsl:text>
								<xsl:value-of select="./documentId" /><xsl:text>&amp;iframeid=f_straform</xsl:text>
							</xsl:attribute>
							This browser does not support inline frame.
						</xsl:element><br />
					</td></tr>
					</table>
				</div>
			</xsl:when>

			<xsl:when test="../RRDFREEFORM">
				<div class="margin">
					<xsl:if test="@title">
						<div class="applicationNameDummyClass2"><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
					</xsl:if>
					<table class="detail">
					<tr><td colspan="2">
						<xsl:element name="iframe">
							<xsl:attribute name="name">
								<xsl:text>f_straform</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="id">
								<xsl:text>f_straform</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="src">
								<xsl:text>../servlet/net.poweregg.web.app.rrd.RRD1323?form=</xsl:text>
								<xsl:value-of select="./formId" /><xsl:text>&amp;docid=</xsl:text>
								<xsl:value-of select="./documentId" /><xsl:text>&amp;iframeid=f_straform</xsl:text>
							</xsl:attribute>
							This browser does not support inline frame.
						</xsl:element><br />
					</td></tr>
					</table>
				</div>
			</xsl:when>

			<xsl:when test="../RRDOFFICE">
				<div class="margin">
					<xsl:if test="@title">
						<div class="applicationNameDummyClass3"><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
					</xsl:if>
					<table class="detail">
					<tr><td colspan="2">
						<xsl:element name="iframe">
							<xsl:attribute name="name">
								<xsl:text>f_straform</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="id">
								<xsl:text>f_straform</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="src">
								<xsl:text>../servlet/net.poweregg.web.app.rrd.RRD1323?form=</xsl:text>
								<xsl:value-of select="./formId" /><xsl:text>&amp;docid=</xsl:text>
								<xsl:value-of select="./documentId" /><xsl:text>&amp;iframeid=f_straform</xsl:text>
							</xsl:attribute>
							This browser does not support inline frame.
						</xsl:element><br />
					</td></tr>
					</table>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div>
					<xsl:if test="@title">
						<div class="applicationNameDummyClass4"><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
					</xsl:if>
					<table class="detail">
					<tr><td colspan="2">
						<xsl:element name="iframe">
							<xsl:attribute name="name">
								<xsl:text>f_straform</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="id">
								<xsl:text>f_straform</xsl:text>
							</xsl:attribute>
							<xsl:attribute name="src">
								<xsl:text>../servlet/net.poweregg.web.app.rrd.RRD1323?form=</xsl:text>
								<xsl:value-of select="./formId" /><xsl:text>&amp;docid=</xsl:text>
								<xsl:value-of select="./documentId" /><xsl:text>&amp;iframeid=f_straform</xsl:text>
							</xsl:attribute>
							This browser does not support inline frame.
						</xsl:element><br />
					</td></tr>
					</table>
				</div>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<!--
		Original: 2004.10.12 V1.7
		Office ActiveX
	-->
	<xsl:template match="RRDOFFICE" >
		<xsl:if test="OFFICEDOCID[text()] != '' " >
			<xsl:if test="@title">
				<div><xsl:value-of select="@title" disable-output-escaping="yes"/></div>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="../RFM_USERFORMAT">
					<div id="content" class="margin">	
					</div>
					<script type="text/javascript">
						var elem = document.getElementById('content');
						var link = "/pe4j/Download/excel.xls?id=<xsl:value-of select="OFFICEDOCID"/>";
						var ObjectA = "<a href='+link+'>Download application file</a>";
						function fncOnLoad() {
							elem.innerHTML = 'Office document workflow is not available.<br/>' + ObjectA;
						}
						fncOnLoad();
					</script>
				</xsl:when>
				<xsl:when test="../RRDFREEFORM">
					<div id="content" class="margin">	
					</div>
					<script type="text/javascript">
						var elem = document.getElementById('content');
						var link = "/pe4j/Download/excel.xls?id=<xsl:value-of select="OFFICEDOCID"/>";
						var ObjectA = "<a href='+link+'>Download application file</a>";
						function fncOnLoad() {
							elem.innerHTML = 'Office document workflow is not available.<br/>' + ObjectA;
						}
						fncOnLoad();
					</script>
				</xsl:when>
				<xsl:otherwise>
					<div id="content">
					</div>
					<script type="text/javascript">
						var elem = document.getElementById('content');
						var link = "/pe4j/Download/excel.xls?id=<xsl:value-of select="OFFICEDOCID"/>";
						var ObjectA = "<a href='+link+'>Download application file</a>";
						function fncOnLoad() {
							elem.innerHTML = 'Office document workflow is not available.<br/>' + ObjectA;
						}
						fncOnLoad();
					</script>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>