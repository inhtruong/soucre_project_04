<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

	<xsl:variable name="img_emp" select="'b_emp.png'" />
	<xsl:variable name="img_kinkyuu" select="'f_kinkyuu_b.png'" />
	<xsl:variable name="img_daikou" select="'f_daikou_b.png'" />
	<xsl:variable name="img_kouetu" select="'f_kouetu_b.png'" />
	<xsl:variable name="img_confirm" select="'b_confirm.gif'" />
	<xsl:variable name="img_current" select="'current.gif'" />
	<xsl:variable name="img_thread" select="'thread.png'" />
	<xsl:variable name="img_attachment" select="'b_attachment.png'" />
	<xsl:variable name="img_subroute" select="'b_subroute.png'" />

	<xsl:variable name="empIconSrc" select="concat(/DATAFLOW_DOC/@imgbase, $img_emp)" />
	<xsl:variable name="kinkyuuIconSrc" select="concat(/DATAFLOW_DOC/@imgbase , $img_kinkyuu)" />
	<xsl:variable name="daikouIconSrc" select="concat(/DATAFLOW_DOC/@imgbase, $img_daikou)" />
	<xsl:variable name="kouetuIconSrc" select="concat(/DATAFLOW_DOC/@imgbase, $img_kouetu)" />
	<xsl:variable name="zumiIconSrc" select="concat(/DATAFLOW_DOC/@imgbase, $img_confirm)" />
	<xsl:variable name="taisyouIconSrc" select="concat(/DATAFLOW_DOC/@imgbase, $img_current)" />
	<xsl:variable name="threadIconSrc" select="concat(/DATAFLOW_DOC/@imgbase, $img_thread)" />
	<xsl:variable name="attachmentIconSrc" select="concat(/DATAFLOW_DOC/@imgbase, $img_attachment)" />
	<xsl:variable name="subrouteSrc" select="concat(/DATAFLOW_DOC/@imgbase, $img_subroute)" />

	<xsl:variable name="deptDelimiter" select="'）'" />

	<xsl:variable name="dispCorpName" select="/DATAFLOW_DOC/@dispcorp" />
	<xsl:variable name="dispSign" select="/DATAFLOW_DOC/@dispsign" />

	<xsl:template match="DATAFLOW_DOC">
		<xsl:apply-templates select="./DATAFLOW_HEADER" />
		<xsl:apply-templates select="./DATAFLOW_CONDITION_COMMENT" />
		<xsl:apply-templates select="./DATAFLOW_COMMENT_TREE" />
		<xsl:apply-templates select="./DATAFLOW_ROUTE" />
		<xsl:apply-templates select="./DATAFLOW_HEADER/APPRECPNO" />
		<xsl:apply-templates select="./DATAFLOW_ATTACHMENTS" />
	</xsl:template>

	<!-- 案件情報ヘッダのテンプレート -->
	<xsl:template match="DATAFLOW_HEADER">
		<table width="100%">
			<tbody>
				<tr>
					<td>
						<xsl:if test="./PRIORITY = '0002'">
							<img>
								<xsl:attribute name="src">
									<xsl:value-of select="$kinkyuuIconSrc" />
								</xsl:attribute>
							</img>
						</xsl:if>
						<xsl:if test="./PROXYFLAG = '1'">
							<img>
								<xsl:attribute name="src">
									<xsl:value-of select="$daikouIconSrc" />
								</xsl:attribute>
							</img>
						</xsl:if>
						様式：<xsl:value-of select="./BASEFORMNAME" />&#160;申請種別：<xsl:value-of select="./DETAILFORMNAME" />
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<div class="contents">
				<table class="detail" style="">
					<tbody>
						<tr>
							<td class="label">申請日</td>
							<td class="item">
								<xsl:value-of select="./APPLIEDDATE" />
								<xsl:if test="./APPPROCDATE != ''">
									&#160;【処理日：<xsl:value-of select="./APPPROCDATE" />】
								</xsl:if>
							</td>
						</tr>
						<tr>
							<td class="label">申請者</td>
							<td class="item">
								<xsl:if test="/DATAFLOW_DOC/@dispcorp = '1'">
									<xsl:value-of select="./CORPNAME" />
									<xsl:value-of select="$deptDelimiter" />
								</xsl:if>
								<xsl:value-of select="./DEPTNAME" />
								<xsl:value-of select="$deptDelimiter" />
								<img>
									<xsl:attribute name="src">
										<xsl:value-of select="$empIconSrc" />
									</xsl:attribute>
								</img>
								<xsl:value-of select="./APPLICANTNAME" />
							</td>
						</tr>
						<tr>
							<td class="label">件名</td>
							<td class="item"><xsl:value-of select="./TITLE" /></td>
						</tr>
						<tr>
							<td class="label">申請番号</td>
							<td class="item"><xsl:value-of select="./APPLICATIONNO" /></td>
						</tr>
						<tr>
							<td class="label">状況</td>
							<td class="item"><xsl:value-of select="./STATUSNAME" /></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div><br/></div>
	</xsl:template>

	<!-- 決裁条件コメントのテンプレート -->
	<xsl:template match="DATAFLOW_CONDITION_COMMENT">
		<div>
			<table width="100%">
				<tbody>
					<tr><td>決裁条件</td></tr>
				</tbody>
			</table>
		</div>
		<div>
			<div class="contents printcontentsComment">
				<table class="detail">
					<thead>
						<tr>
							<th class="headerBg" colspan="3" scope="colgroup">
								<label style="font-weight:bold">内容</label>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td class="item">
								<span style="word-wrap : break-word;overflow-wrap : break-word;">
									<xsl:value-of disable-output-escaping="yes" select="./COMMENTSTR" />
								</span>
							</td>
							<td class="item borderLeft" width="25%"><xsl:value-of select="./COMMENTDATETIME" /></td>
							<td class="item borderLeft" width="20%">
								<img>
									<xsl:attribute name="src">
										<xsl:value-of select="$empIconSrc" />
									</xsl:attribute>
								</img>
								<xsl:value-of select="./EMPNAME" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div><br/></div>
	</xsl:template>

	<!-- コメントツリーのテンプレート -->
	<xsl:template match="DATAFLOW_COMMENT_TREE">
		<table width="100%">
			<tbody>
				<tr>
					<td>
						コメント
						<xsl:if test="count(./COMMENT) > 0">
							(<xsl:value-of select="count(./COMMENT)" />件あります)
						</xsl:if>
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<div class="contents">
				<xsl:if test="count(./COMMENT) > 0">
					<table class="rf-dt">
						<colgroup span="3"></colgroup>
						<thead class="rf-dt-thd">
							<tr class="rf-dt-hdr rf-dt-hdr-fst">
								<th class="rf-dt-hdr-c headcellBorder">コメント</th>
								<th class="rf-dt-hdr-c headcellBorder" width="25%">日時</th>
								<th class="rf-dt-hdr-c headcellBorder" width="20%">記入者</th>
							</tr>
						</thead>
						<tbody class="rf-dt-b">
							<xsl:for-each select="./COMMENT">
								<tr>
									<xsl:attribute name="class">
										<xsl:choose>
											<xsl:when test="position() mod 2 = 1">rf-dt-r rf-dt-fst-r oddRow</xsl:when>
											<xsl:otherwise>rf-dt-r rf-dt-fst-r evenRow</xsl:otherwise>
										</xsl:choose>
									</xsl:attribute>
									<td class="rf-dt-c cellBorder">
										<xsl:attribute name="style">
											<xsl:text>padding-left:</xsl:text>
											<xsl:value-of select="./LEVEL * 10 + 5" />
											<xsl:text>px; width:auto;</xsl:text>
										</xsl:attribute>
										<xsl:if test="./LEVEL > 0">
											<img>
												<xsl:attribute name="src">
													<xsl:value-of select="$threadIconSrc" />
												</xsl:attribute>
											</img>
										</xsl:if>
										<xsl:if test="./COMMENTTYPE = '0003'">
											<div styleClass="guidance">【確認・指示】</div>
										</xsl:if>
										<xsl:if test="./DELFLAG = '0'">
											<span style="word-wrap : break-word;overflow-wrap : break-word;">
												<xsl:value-of disable-output-escaping="yes" select="./COMMENTSTR" />
											</span>
											<br/>
											<xsl:apply-templates select="./ATTACHFILE" />
										</xsl:if>
										<xsl:if test="./DELFLAG = '1'">
											（削除されました）
										</xsl:if>
										<div style=""></div>
									</td>
									<td class="rf-dt-c cellBorder" style="width:auto;">
										<xsl:value-of select="./COMMENTDATETIME" />
									</td>
									<td class="rf-dt-c cellBorder" style="width:auto;">
										<img>
											<xsl:attribute name="src">
												<xsl:value-of select="$empIconSrc" />
											</xsl:attribute>
										</img>
										<xsl:value-of select="./EMPNAME" />
										<xsl:if test="./COMMENTTYPE = '0003'">
											<br/>→
											<img>
												<xsl:attribute name="src">
													<xsl:value-of select="$empIconSrc" />
												</xsl:attribute>
											</img>
											<xsl:value-of select="./NOTICEEMPNAME" />
										</xsl:if>
									</td>
								</tr>
							</xsl:for-each>
						</tbody>
					</table>
				</xsl:if>
				<xsl:if test="count(./COMMENT) = 0">
					（ コメントはありません ）
				</xsl:if>
			</div>
		</div>
		<div><br/></div>
	</xsl:template>

	<!-- 承認状況のテンプレート -->
	<xsl:template match="DATAFLOW_ROUTE">
		<table width="100%">
			<tbody>
				<tr><td>承認状況</td></tr>
			</tbody>
		</table>
		<div class="contents">
			<table class="rf-dt">
				<thead class="rf-dt-thd">
					<tr class="rf-dt-hdr rf-dt-hdr-fst">
						<xsl:variable name="colspan">
							<xsl:choose>
								<xsl:when test="./FLOW_SUB_ORDER_FLAG = '1'">
									<xsl:value-of select="1" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="2" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<th class="rf-dt-hdr-c headcellBorder">
							<xsl:attribute name="colspan">
								<xsl:value-of select="$colspan" />
							</xsl:attribute>
						</th>
						<xsl:if test="./FLOW_SUB_ORDER_FLAG = '1'">
							<th class="rf-dt-hdr-c headcellBorder"></th>
						</xsl:if>
						<th class="rf-dt-hdr-c headcellBorder">担当者</th>

						<xsl:if test="$dispSign != '0'">
							<th class="rf-dt-hdr-c headcellBorder" width="34px"></th>
						</xsl:if>

						<th class="rf-dt-hdr-c headcellBorder">所属</th>
						<th class="rf-dt-hdr-c headcellBorder">役職</th>
						<th class="rf-dt-hdr-c headcellBorder">権限</th>
						<th class="rf-dt-hdr-c headcellBorder">結果</th>
						<th class="rf-dt-hdr-c headcellBorder">処理日時</th>
					</tr>
				</thead>				
				<xsl:for-each select="./APPROVER">
					<tbody>
						<xsl:if test="./FLOWPARALLEL = '1' and ./PARALLELNO != ''">
							<tr>
								<td class="rf-dt-c" style="width:auto; border-top: 1px solid #AAAAAA; border-left: 1px solid #AAAAAA;text-align:center;">
									<img>
										<xsl:attribute name="src">
											<xsl:value-of select="$subrouteSrc" />
										</xsl:attribute>
									</img>
								</td>
								<td class="rf-dt-c cellBorder" style="width:auto; background-color: #e0e0e0;" colspan="8">
									<xsl:value-of select="./PARALLELNO" />
								</td>
							</tr>
						</xsl:if>
						<tr>
							<xsl:attribute name="class">
								<xsl:choose>
									<xsl:when test="position() mod 2 = 1">rf-dt-r rf-dt-fst-r oddRow</xsl:when>
									<xsl:otherwise>rf-dt-r rf-dt-fst-r evenRow</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
							<xsl:variable name="rowspan">
								<xsl:choose>
									<xsl:when test="(./PERSONTYPE = '1' and (./PROXYFLAG = '1' or ./UNPROCPROXYFLAG = '1')) or (./PERSONTYPE = '2' and string-length(./RESULTNAME) > 0) ">
										<xsl:value-of select="2" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="1" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:variable name="colspan">
								<xsl:choose>
									<xsl:when test="./FLOWPARALLEL = '1'">
										<xsl:value-of select="1" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="2" />
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<td class="rf-dt-c">
								<xsl:attribute name="style">
									<xsl:choose>
										<xsl:when test="./FLOWPARALLEL = '1'">width:auto;background-color:white;border-left: 1px solid #AAAAAA;min-width:26px;</xsl:when>
										<xsl:otherwise>width:auto;border-left: 1px solid #AAAAAA;border-top: 1px solid #AAAAAA;border-bottom: 1px solid #AAAAAA;min-width:26px;</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<xsl:attribute name="colspan">
									<xsl:value-of select="$colspan" />
								</xsl:attribute>
								<xsl:if test="./FLOWPARALLEL != '1'">
									<xsl:if test="./APPROVALTARGET = '1'">
									<img>
										<xsl:attribute name="src">
											<xsl:value-of select="$taisyouIconSrc" />
										</xsl:attribute>
									</img>
									</xsl:if>
									<xsl:if test="./APPROVALTARGET = '2'">
										<img>
											<xsl:attribute name="src">
												<xsl:value-of select="$zumiIconSrc" />
											</xsl:attribute>
										</img>
									</xsl:if>
									<xsl:if test="./SKIPPED = '1'">
										<img>
											<xsl:attribute name="src">
												<xsl:value-of select="$kouetuIconSrc" />
											</xsl:attribute>
										</img>
									</xsl:if>
								</xsl:if>
							</td>
							
							<xsl:if test="./FLOWPARALLEL = '1'">
								<td class="rf-dt-c">
									<xsl:attribute name="style">
										<xsl:choose>
											<xsl:when test="./FLOWPARALLEL = '1'">width:auto;border:1px solid #AAAAAA !important;min-width:26px;</xsl:when>
											<xsl:otherwise>width:auto;border-top: 1px solid #AAAAAA;border-bottom: 1px solid #AAAAAA;</xsl:otherwise>
										</xsl:choose>
									</xsl:attribute>
									<xsl:attribute name="rowspan">
										<xsl:value-of select="$rowspan" />
									</xsl:attribute>
									<xsl:if test="./APPROVALTARGET = '1'">
									<img>
										<xsl:attribute name="src">
											<xsl:value-of select="$taisyouIconSrc" />
										</xsl:attribute>
									</img>
									</xsl:if>
									<xsl:if test="./APPROVALTARGET = '2'">
										<img>
											<xsl:attribute name="src">
												<xsl:value-of select="$zumiIconSrc" />
											</xsl:attribute>
										</img>
									</xsl:if>
									<xsl:if test="./SKIPPED = '1'">
										<img>
											<xsl:attribute name="src">
												<xsl:value-of select="$kouetuIconSrc" />
											</xsl:attribute>
										</img>
									</xsl:if>
								</td>
							</xsl:if>
							
							<td class="cellBorder">
								<xsl:if test="./PERSONTYPE = '1'">
									<img>
										<xsl:attribute name="src">
											<xsl:value-of select="$empIconSrc" />
										</xsl:attribute>
									</img>
									<xsl:value-of select="./EMPNAME" />
								</xsl:if>
								<xsl:if test="./PERSONTYPE = '2'">[グループ1名]</xsl:if>
								<xsl:if test="./PERSONTYPE = '3'">[社員1名]</xsl:if>
							</td>

							<xsl:if test="$dispSign != '0'">
								<td class="rf-dt-c cellBorder" style="width:auto;">
									<xsl:attribute name="rowspan">
										<xsl:value-of select="$rowspan" />
									</xsl:attribute>
									<xsl:if test="./SIGNATUREURL != ''">
										<img>
											<xsl:attribute name="src">
												<xsl:value-of select="./SIGNATUREURL" />
											</xsl:attribute>
										</img>
									</xsl:if>
								</td>
							</xsl:if>

							<xsl:variable name="colspan">
								<xsl:choose>
									<xsl:when test="./PERSONTYPE = '2' or ./PERSONTYPE = '3'">
										<xsl:value-of select="2" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="1" />
									</xsl:otherwise>
									</xsl:choose>
							</xsl:variable>

							<td class="rf-dt-c cellBorder" style="width:auto;">
								<xsl:attribute name="colspan">
									<xsl:value-of select="$colspan" />
								</xsl:attribute>
								<xsl:if test="./PERSONTYPE = '1'">
									<xsl:if test="$dispCorpName = '1'">
										<xsl:value-of select="./BELONGCORPNAME" />
										<xsl:value-of select="$deptDelimiter" />
									</xsl:if>
									<xsl:value-of select="./BELONGDEPTNAME" />
								</xsl:if>
								<xsl:if test="./PERSONTYPE = '2'">
									<xsl:value-of select="./GROUPDETAILNAME" />
								</xsl:if>
								<xsl:if test="./PERSONTYPE = '3'">
									<xsl:value-of select="./GROUPDETAILNAME" />
								</xsl:if>
							</td>
							<xsl:if test="./PERSONTYPE != '2' and ./PERSONTYPE != '3'">
								<td class="rf-dt-c cellBorder">
									<xsl:if test="./PERSONTYPE = '1'">
										<xsl:value-of select="./BELONGTITLENAME" />
									</xsl:if>
								</td>
							</xsl:if>
							<td class="rf-dt-c cellBorder" style="width:auto;">
								<xsl:attribute name="rowspan">
									<xsl:value-of select="$rowspan" />
								</xsl:attribute>
								<xsl:value-of select="./AUTHORITYNAME" />
								<xsl:if test="./EMPTYPE = '4'">(並行)</xsl:if>
							</td>
							<td class="rf-dt-c cellBorder" style="width:auto;">
								<xsl:attribute name="rowspan">
									<xsl:value-of select="$rowspan" />
								</xsl:attribute>
								<xsl:value-of select="./RESULTNAME" />
							</td>
							<td class="rf-dt-c cellBorder" style="width:auto;">
								<xsl:attribute name="rowspan">
									<xsl:value-of select="$rowspan" />
								</xsl:attribute>
								<xsl:value-of select="./PROCDATETIME" />
							</td>
						</tr>

						<xsl:if test="./PERSONTYPE = '1' and (./PROXYFLAG = '1' or ./UNPROCPROXYFLAG = '1')">
							<tr>
								<xsl:attribute name="class">
									<xsl:choose>
										<xsl:when test="position() mod 2 = 1">rf-dt-r rf-dt-fst-r cellBorder oddRow</xsl:when>
										<xsl:otherwise>rf-dt-r rf-dt-fst-r cellBorder evenRow</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<td class="rf-dt-c cellBorder">
									<img>
										<xsl:attribute name="src">
											<xsl:value-of select="$daikouIconSrc" />
										</xsl:attribute>
									</img>
									<img>
										<xsl:attribute name="src">
											<xsl:value-of select="$empIconSrc" />
										</xsl:attribute>
									</img>
									<xsl:choose>
										<xsl:when test="./PROXYFLAG = '1'">
											<xsl:value-of select="./PROXYEMPNAME" />
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="./UNPROCPROXYEMPNAME" />
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td class="rf-dt-c cellBorder">
									<xsl:choose>
										<xsl:when test="./PROXYFLAG = '1'">
											<xsl:if test="$dispCorpName = '1'">
												<xsl:value-of select="./PROXYCORPNAME" />
												<xsl:value-of select="$deptDelimiter" />
											</xsl:if>
											<xsl:value-of select="./PROXYDEPTNAME" />
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="$dispCorpName = '1'">
												<xsl:value-of select="./UNPROCPROXYCORPNAME" />
												<xsl:value-of select="$deptDelimiter" />
											</xsl:if>
											<xsl:value-of select="./UNPROCPROXYDEPTNAME" />
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td class="rf-dt-c cellBorder">
									<xsl:choose>
										<xsl:when test="./PROXYFLAG = '1'">
											<xsl:value-of select="./PROXYTITLENAME" />
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="./UNPROCPROXYTITLENAME" />
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</tr>
						</xsl:if>

						<xsl:if test="./PERSONTYPE = '2' and string-length(./RESULTNAME) > 0">
							<tr>
								<xsl:attribute name="class">
									<xsl:choose>
										<xsl:when test="position() mod 2 = 1">rf-dt-r rf-dt-fst-r cellBorder oddRow</xsl:when>
										<xsl:otherwise>rf-dt-r rf-dt-fst-r cellBorder evenRow</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
								<td class="rf-dt-c cellBorder">
									<xsl:if test="./PROXYEMPNAME != ''">
										<xsl:attribute name="src">
											<xsl:value-of select="$empIconSrc" />
										</xsl:attribute>
										<xsl:value-of select="./PROXYEMPNAME" />
									</xsl:if>
								</td>
								<td class="rf-dt-c cellBorder">
									<xsl:if test="./PROXYEMPNAME != ''">
										<xsl:if test="$dispCorpName = '1'">
											<xsl:value-of select="./PROXYCORPNAME" />
											<xsl:value-of select="$deptDelimiter" />
										</xsl:if>
										<xsl:value-of select="./PROXYDEPTNAME" />
									</xsl:if>
								</td>
								<td class="rf-dt-c cellBorder">
									<xsl:if test="./PROXYEMPNAME != ''">
										<xsl:value-of select="./PROXYTITLENAME" />
									</xsl:if>
								</td>
							</tr>
						</xsl:if>
					</tbody>
				</xsl:for-each>				
			</table>
		</div>
	</xsl:template>

	<!-- 申請受付番号のテンプレート -->
	<xsl:template match="DATAFLOW_HEADER/APPRECPNO">
		<div class="message guidance right ">申請受付番号&#160;<xsl:value-of select="." /></div>
	</xsl:template>

	<!-- 申請書添付ファイルのテンプレート -->
	<xsl:template match="DATAFLOW_ATTACHMENTS">
		<xsl:if test="count(./ATTACHFILE) > 0">
			<div>
				<div class="contents margin">
					<table class="detail">
						<tbody>
							<tr>
								<td class="label">添付ファイル</td>
								<td class="item">
									<xsl:for-each select="./ATTACHFILE">
										<div style=";overflow:auto;">
											<xsl:apply-templates select="." />
										</div>
									</xsl:for-each>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</xsl:if>
	</xsl:template>

	<!-- 添付ファイルのテンプレート -->
	<xsl:template
		match="DATAFLOW_ATTACHMENTS/ATTACHFILE | DATAFLOW_COMMENT_TREE/COMMENT/ATTACHFILE">
		<img>
			<xsl:attribute name="src">
				<xsl:value-of select="$attachmentIconSrc" />
			</xsl:attribute>
		</img>
		<xsl:value-of select="./FILENAME" />(<xsl:value-of select="./FILESIZE" />KB)
		<br/>
	</xsl:template>

	<xsl:template name="replaceAll">
		<xsl:param name="value" />
		<xsl:param name="regex" />
		<xsl:param name="replacement" />
		<xsl:value-of select="translate($value , $regex, $replacement)" />
	</xsl:template>

</xsl:stylesheet>
