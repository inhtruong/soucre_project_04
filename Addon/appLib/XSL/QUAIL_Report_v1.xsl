<?xml version="1.0" encoding="Windows-31J" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" encoding="Windows-31J" />

<!-- for debug 
<?xml version="1.0" encoding="Shift_JIS" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" encoding="Shift_JIS" />
-->

<!-- ********************************************************************** -->
<!--						レポート登録起案内容							-->
<!-- ********************************************************************** -->

<!-- ***** ルート ***** -->
<xsl:template match="/">
	<HTML>
	<HEAD>
		<META http-equiv="Content-Type" CONTENT="text/html; charset=Shift_JIS"/>
		<STYLE type="text/css">
			SPAN.headline {font-size:120%; font-weight:bold;} 
			TD.bap-title {background-color:#BCDCFF;}
			TD.bap-contents {background-color:#E0E0E0;}
			SPAN.rfm-title{white-space:nowrap;padding-left:4px;padding-right:4px;color:black;background-color:#BCDCFF;}
		</STYLE>
		<TITLE>レポート内容表示</TITLE>
		<SCRIPT type="text/javascript" src="../script/CommonFunction.js">
		//dummy comment
		</SCRIPT>
		<!-- V1.9 転送元レポートの表示 -->
		<SCRIPT type="text/javascript">
			function fncOpenReport(corpid, applyno, historyno) {
				var winopt = "menubar=no,toolbar=no,location=no,status=no,scrollbars=yes,resizable=yes";
				var url ="../servlet/PowerEggServlet";
				url += "?servletName=net.poweregg.web.app.fpt.FPT0501";
				url += "&amp;CorpID=" + corpid; 
				url += "&amp;ApplyNo=" + applyno; 
				url += "&amp;HistoryNo=" + historyno;
				var subwin = open(url,"FwReport", winopt);
				subwin.focus();
			}
		</SCRIPT>
	</HEAD>
	<BODY>
		<xsl:apply-templates select="BAPDOCUMENT" />
		<iframe style="display:none" name="f_filedownload" src="about:blank" >
		このブラウザはインラインフレームに対応していません 
		</iframe>
	</BODY>
	</HTML>
</xsl:template>

<!-- ***** ボディ ***** -->
<xsl:template match="BAPDOCUMENT">
<SPAN class="headline"><xsl:value-of select="./REPORTFORMNAME" /></SPAN>
	<TABLE cellpadding="3" cellspacing="1" border="0" width="100%">
	
		<TR>
			<TD nowrap="" class="bap-title" width="15%">件名</TD>
			<TD class="bap-contents" colspan="4" width="99%"><xsl:value-of select="./TITLE" /></TD>
		</TR>
		<TR>
			<TD nowrap="" class="bap-title" width="15%">報告日</TD>
			<TD class="bap-contents" colspan="4"><xsl:value-of select="./REPORTDATE" /></TD>
		</TR>
	<xsl:if test="./SENDTO">
		<TR>
			<TD nowrap="" class="bap-title" width="15%">送信先（To）</TD>
			<TD class="bap-contents" colspan="4"><xsl:value-of select="./SENDTO" /></TD>
		</TR>
	</xsl:if>
	<xsl:if test="./SENDCC">
		<TR>
			<TD nowrap="" class="bap-title" width="15%">送信先（Cc）</TD>
			<TD class="bap-contents" colspan="4"><xsl:value-of select="./SENDCC" /></TD>
		</TR>
	</xsl:if>
	<xsl:if test="./CUSTOMERSETFLAG[text()!=0]">
		<xsl:if test="./CUSTUSAGEFLAG[text()!=0]">
		<TR>
			<TD nowrap="" class="bap-title" width="15%">顧客名</TD>
			<TD class="bap-contents" width="30%" colspan="1" nowrap=""><xsl:element name="A">
			<xsl:attribute name="href">
				<xsl:text>javascript:fncDisplayEnterprise('</xsl:text>
				<xsl:value-of select="./ENTERPRISEID" />
				<xsl:text>')</xsl:text>
			</xsl:attribute><xsl:value-of select="./ENTERPRISENAME" />
			</xsl:element>
			</TD>
			<TD class="bap-contents" width="60%" colspan="3">
			<xsl:element name="A">
			<xsl:attribute name="href">
				<xsl:text>javascript:fncDisplayEnterprisePost('</xsl:text>
				<xsl:value-of select="./ENTERPRISEID" />
				<xsl:text>','</xsl:text>
				<xsl:value-of select="./POSTID" />
				<xsl:text>')</xsl:text>
			</xsl:attribute><xsl:value-of select="./POSTNAME" />
			</xsl:element>
			</TD>
		</TR>
		</xsl:if>
		<xsl:if test="./PERSONUSAGEFLAG[text()!=0]">
		<TR>
			<xsl:if test="./OTHERPERSONUSAGEFLAG[text()=0]">
			<TD nowrap="" class="bap-title">顧客ご担当者</TD>
			<TD class="bap-contents" colspan="4">
			<xsl:element name="A">
			<xsl:attribute name="href">
				<xsl:text>javascript:fncDisplayEnterprisePerson('</xsl:text>
				<xsl:value-of select="./ENTERPRISEID" />
				<xsl:text>','</xsl:text>
				<xsl:value-of select="./POSTID" />
				<xsl:text>','</xsl:text>
				<xsl:value-of select="./PERSONID" />
				<xsl:text>')</xsl:text>
			</xsl:attribute>
			<xsl:value-of select="./PERSONNAME" />
			</xsl:element>
			</TD>
			</xsl:if>
			<xsl:if test="./OTHERPERSONUSAGEFLAG[text()!=0]">
			<TD nowrap="" class="bap-title" width="15%">顧客ご担当者</TD>
			<TD class="bap-contents" width="30%"><xsl:element name="A">
			<xsl:attribute name="href">
				<xsl:text>javascript:fncDisplayEnterprisePerson('</xsl:text>
				<xsl:value-of select="./ENTERPRISEID" />
				<xsl:text>','</xsl:text>
				<xsl:value-of select="./POSTID" />
				<xsl:text>','</xsl:text>
				<xsl:value-of select="./PERSONID" />
				<xsl:text>')</xsl:text>
			</xsl:attribute>
			<xsl:value-of select="./PERSONNAME" />
			</xsl:element></TD>
			<TD nowrap="" class="bap-title" width="1%">その他のご担当者</TD>
			<TD class="bap-contents" width="60%"><xsl:value-of select="./OTHERPERSONNAME" /></TD>
			</xsl:if>
		</TR>
		</xsl:if>
	</xsl:if>
	<xsl:if test="./SFASETFLAG[text()!=0]">
		<xsl:if test="./COMMODITYUSAGEFLAG[text()!=0]">
		<TR>
			<TD nowrap="" class="bap-title" width="15%">対象商品</TD>
			<TD class="bap-contents" colspan="4" ><xsl:value-of select="./COMMODITYNAME" /></TD>
		</TR>
		</xsl:if>
	</xsl:if>
	
	<xsl:if test="./INPUTDISPLAYFORMATID[text()!=0]">
		<xsl:apply-templates select="./RFM_USERFORMAT" />
	</xsl:if>
	<xsl:if test="./OFFICEUSAGEFLAG[text()!=0]">
	<TR>
	<TD  colspan="5">
		<xsl:apply-templates select="./OFFICE" />
	</TD>
	</TR>
	</xsl:if>
	
	<xsl:if test="./FILEUSAGEFLAG[text()!=0]">
	
	 <TR>
		<TD nowrap="" class="bap-title" width="15%">添付資料</TD><TD class="bap-contents" colspan="4">
		 
		<TABLE border="0" cellspacing="0" cellpadding="0"> 
		<TR> 
			<TD valign="top"> 
				<xsl:apply-templates select="./ATTACHMENTFILE" />
			</TD> 
			</TR> 
  		</TABLE>
		</TD>
	</TR>  
  
	</xsl:if>
   
	<!-- xsl:if test="./AUTHORITYUSAGEFLAG[text()!=0]" -->
	<TR>
		<TD nowrap="" class="bap-title" width="15%">公開範囲</TD>
		<TD class="bap-contents" colspan="4"><xsl:value-of select="./AUTHORITYNAME" /></TD>
	</TR>
	<!-- /xsl:if -->
	<!-- 転送元が案件かレポートかでタイトルを変更 -->
	<xsl:if test="string-length(./FORWARD/CORPID[text()])>0">
	<TR>
	  <TD  nowrap=""   class="bap-title" width="15%">
		転送元
<xsl:choose>
<xsl:when test="string-length(./FORWARD/FORWARDTYPE[text()])>0">案件</xsl:when>
<xsl:otherwise>レポート</xsl:otherwise>
</xsl:choose>
	</TD>
	  	<TD class="bap-contents" colspan="4"> 
				<xsl:apply-templates select="./FORWARD" />
		</TD> 
	</TR>
</xsl:if>
<!-- 顧客選択リンク用フォーム -->
<!-- 余白を残さないためにテーブルタグ内に出力する -->
<FORM name="prm" method="post"><INPUT type="hidden" name="OpenURL" value="" /></FORM>
</TABLE>
</xsl:template>
<!-- 添付ファイルのテンプレート -->
<xsl:template match="ATTACHMENTFILE">
	<xsl:element name="A">
		<xsl:attribute name="href">../servlet/Download/<xsl:value-of select="./FILENAME"/>?DocID=<xsl:value-of select="@docid"/></xsl:attribute>
		<xsl:attribute name="target">
			<xsl:text>f_filedownload</xsl:text>
		</xsl:attribute>
		<xsl:value-of select="./FILENAME" />
		<xsl:text>(</xsl:text>
		<xsl:value-of select="./FILESIZE" />
		<xsl:text>KB)</xsl:text>
	</xsl:element><BR/>
</xsl:template>

<!-- 転送元レポート -->
<xsl:template match="FORWARD">
	<xsl:element name="A">
		<xsl:attribute name="href">javascript:fncOpenReport('<xsl:value-of select="./CORPID"/>','<xsl:value-of select="./APPLYNO"/>','<xsl:value-of select="./HISTORYNO"/>')</xsl:attribute>
		<xsl:value-of select="./TITLE"/>
	</xsl:element><BR/>
</xsl:template>



<!-- 
	Office文書のテンプレート
-->
<xsl:template match="OFFICE" >
<xsl:if test="OFFICEDOCID[text()] != ''">
	<div id="officectldiv"></div>
	<script type="text/javascript" src="../ocx/makeActiveX.js">
	// dummy
	</script>
	<script type="text/javascript">
		createOfficeCtl('officectldiv', '', '', 
		'<xsl:text disable-output-escaping="yes">/pe4j/</xsl:text><xsl:value-of select="OFFICEDOCUMENT" disable-output-escaping="yes"/>'
		);
	</script>
	<SCRIPT language="JavaScript">
	<xsl:text  disable-output-escaping="yes">
	&lt;!--
		function fncInit() {
			setTimeout('this.fncFmtOfficeCtl()', 100);
		}
		function fncFmtOfficeCtl() {
			document.getElementById("officeCtl").ActiveDocument.Application.ActiveWorkbook.Protect("", true);
			var count = document.getElementById("officeCtl").ActiveDocument.Application.Sheets.Count;
			for (var i=1; i&lt;count+1; i++) {
				document.getElementById("officeCtl").ActiveDocument.Sheets(i).Protect("");
			}
		}
		fncInit();
	 --&gt;  
	</xsl:text>
	</SCRIPT>
</xsl:if>
</xsl:template>
<!--*********************************************************************** -->
<!--		ここから「入力フォーマット用」					-->
<!--*********************************************************************** -->
<!-- ///// ルート要素（フォーマット情報）のテンプレート ///// -->
<!-- ///// TABLEタグ有り、TABLE罫線無し、幅100% 	///// -->
<!-- ///// TABLEの列数=2				///// -->
<xsl:template match="RFM_USERFORMAT">
			<TR>
			<xsl:apply-templates />
			<xsl:text disable-output-escaping="yes">&lt;/TD&gt;</xsl:text>
			</TR>
			 
</xsl:template>
<!-- ///// 項目情報のテンプレート ///// -->
<xsl:template match="RFM_ITEM">
	<!-- ///// 先頭項目「以外」の場合 ///// -->
	<xsl:if test="current() != ../RFM_ITEM[1]">
		<!-- 改行フラグが yes or テキストエリア の場合 -->
		<xsl:if test="@nl='yes' or @type='11'">
			<xsl:text disable-output-escaping="yes">
				&lt;/TD&gt;&lt;/TR&gt;&lt;TR&gt;
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
							&lt;TD class="bap-title" align="left" valign="top" colspan="2" nowrap&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_FL" /><BR/>
					<xsl:text disable-output-escaping="yes">
							&lt;/TD&gt;
						&lt;/TR&gt;
						&lt;TR&gt;
							&lt;TD class="bap-title"&gt;<![CDATA[&nbsp;]]>&lt;/TD&gt;
							&lt;TD class="bap-contents" colspan="4" align="left" valign="bottom" width="99%"&gt;
								&lt;TABLE border="0" cellspacing="0"&gt;
									&lt;TR&gt;
										&lt;TD class="bap-contents" align="left" valign="top"&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_DATA">
						<xsl:with-param name="dtype" select="@type"></xsl:with-param>
					</xsl:apply-templates>
					<xsl:text disable-output-escaping="yes">
										&lt;/TD&gt;
										&lt;TD class="bap-contents" align="left" valign="bottom"&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_BL" /><BR/>
					<xsl:text disable-output-escaping="yes">
										&lt;/TD&gt;
									&lt;/TR&gt;
								&lt;/TABLE&gt;
					</xsl:text>
				</xsl:when>
				<!-- ///// 改行フラグがyes、前リテラル後改行がnoの場合 ///// -->
				<xsl:when test="@nl='yes' and @flnl='no'">
					<xsl:text disable-output-escaping="yes">
							&lt;TD class="bap-title" align="left" valign="top" nowrap&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_FL" /><BR/>
					<xsl:text disable-output-escaping="yes">
							&lt;/TD&gt;
							&lt;TD class="bap-contents" colspan="4" align="left" valign="bottom" width="99%"&gt;
								&lt;TABLE border="0" cellspacing="0"&gt;
									&lt;TR&gt;
										&lt;TD class="bap-contents" align="left" valign="top"&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_DATA">
						<xsl:with-param name="dtype" select="@type"></xsl:with-param>
					</xsl:apply-templates>
					<xsl:text disable-output-escaping="yes">
										&lt;/TD&gt;
										&lt;TD class="bap-contents" align="left" valign="bottom"&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_BL" /><BR/>
					<xsl:text disable-output-escaping="yes">
										&lt;/TD&gt;
									&lt;/TR&gt;
								&lt;/TABLE&gt;
					</xsl:text>
				</xsl:when>
				<!-- ///// 改行フラグがnoの場合 ///// -->
				<xsl:otherwise>
					<xsl:text disable-output-escaping="yes">
							&lt;TD class="bap-title" align="left" valign="top" nowrap&gt;
							&lt;/TD&gt;
							&lt;TD class="bap-contents" colspan="4" align="left" valign="bottom"&gt;
								&lt;TABLE border="0" cellspacing="0"&gt;
									&lt;TR&gt;
					</xsl:text>
					<xsl:if test="not(string-length(./RFM_FL/text())=0)">
						<xsl:text disable-output-escaping="yes">
										&lt;TD class="bap-contents" align="left" valign="top" nowrap&gt;&lt;SPAN class="rfm-title"&gt;<!-- PE V1.93  AIT-Tinhhp ADD SPAN tag -->
						</xsl:text>
						<xsl:apply-templates select="./RFM_FL" />
						<xsl:text disable-output-escaping="yes">
							&lt;/SPAN&gt;<![CDATA[&nbsp;&nbsp;]]><!-- PE V1.93 AIT-Tinhhp ADD  close SPAN tag-->
						</xsl:text>
						<xsl:text disable-output-escaping="yes">
										&lt;/TD&gt;
						</xsl:text>
					</xsl:if>
					<xsl:text disable-output-escaping="yes">
										&lt;TD class="bap-contents" align="left" valign="top"&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_DATA">
						<xsl:with-param name="dtype" select="@type"></xsl:with-param>
					</xsl:apply-templates>
					<xsl:text disable-output-escaping="yes">
										&lt;/TD&gt;
										&lt;TD class="bap-contents" align="left" valign="bottom"&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_BL" /><BR/>
					<xsl:text disable-output-escaping="yes">
										&lt;/TD&gt;
									&lt;/TR&gt;
								&lt;/TABLE&gt;
					</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:when>
		<!-- ///// テキストエリア以外の場合 ///// -->
		<xsl:otherwise>
			<xsl:choose>
				<!-- ///// 改行フラグがyes、前リテラル後改行がyesの場合 ///// -->
				<xsl:when test="@nl='yes' and @flnl='yes'">
					<xsl:text disable-output-escaping="yes">
						&lt;TD class="bap-title" align="left" valign="top" colspan="2" nowrap&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_FL" /><BR/>
					<xsl:text disable-output-escaping="yes">
						&lt;/TD&gt;&lt;/TR&gt;
						&lt;TR&gt;&lt;TD class="bap-title"&gt;<![CDATA[&nbsp;]]>&lt;/TD&gt;
						&lt;TD class="bap-contents" colspan="4" align="left" valign="bottom" width="99%"&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_DATA">
						<xsl:with-param name="dtype" select="@type"></xsl:with-param>
					</xsl:apply-templates>
					<xsl:apply-templates select="./RFM_BL" />
				</xsl:when>
				<!-- ///// 改行フラグがyes、前リテラル後改行がnoの場合 ///// -->
				<xsl:when test="@nl='yes' and @flnl='no'">
					<xsl:text disable-output-escaping="yes">
						&lt;TD class="bap-title" align="left" valign="top" nowrap&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_FL" /><BR/>
					<xsl:text disable-output-escaping="yes">
						&lt;/TD&gt;&lt;TD class="bap-contents" colspan="4" align="left" valign="bottom" width="99%"&gt;
					</xsl:text>
					<xsl:apply-templates select="./RFM_DATA">
						<xsl:with-param name="dtype" select="@type"></xsl:with-param>
					</xsl:apply-templates>
					<xsl:apply-templates select="./RFM_BL" />
				</xsl:when>
				<!-- ///// 改行フラグがnoの場合 ///// -->
				<xsl:otherwise>
					<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;&nbsp;&nbsp;]]>&lt;SPAN class="rfm-title"&gt;</xsl:text><!-- PE V1.93 AIT-Tinhhp ADD - SPAN tag-->
					<xsl:apply-templates select="./RFM_FL" />
					<xsl:text disable-output-escaping="yes">&lt;/SPAN&gt;<![CDATA[&nbsp;&nbsp;]]></xsl:text><!-- PE V1.93 AIT-Tinhhp ADD  close SPAN tag-->
					<xsl:apply-templates select="./RFM_DATA">
						<xsl:with-param name="dtype" select="@type"></xsl:with-param>
					</xsl:apply-templates>
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
		<xsl:otherwise><BR/></xsl:otherwise>
	</xsl:choose>
	<!-- ///// コード出力と名称出力を、明細形式で振分 ///// -->
	<xsl:choose>
		<xsl:when test="$dtype='21'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
		<xsl:when test="$dtype='22'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
		<xsl:when test="$dtype='23'"><xsl:apply-templates select="./RFM_NAME" /></xsl:when>
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
					<xsl:element name="A">
						<xsl:attribute name="href"><xsl:apply-templates /></xsl:attribute>
						<xsl:attribute name="target">rfm_window</xsl:attribute>
						<xsl:apply-templates />
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:when>
		<xsl:otherwise>
			<xsl:apply-templates />
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
<!-- ///// 前リテラルのテンプレート ///// -->
<xsl:template match="RFM_FL">
	<xsl:value-of select="." disable-output-escaping="yes" />
</xsl:template>
<!-- ///// 後リテラルのテンプレート ///// -->
<xsl:template match="RFM_BL">
	<xsl:choose>
		<xsl:when test="not(string-length(./text())=0)">
			<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;&nbsp;]]></xsl:text>
			<xsl:value-of select="." disable-output-escaping="yes" />
		</xsl:when>
		<xsl:otherwise>
			<xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
<!-- ///// 名称値のテンプレート ///// -->
<xsl:template match="RFM_NAME">
	<xsl:apply-templates />
</xsl:template>
<!-- ///// BRタグ対応 ///// -->
<xsl:template match="BR">
	<xsl:element name="BR" />
</xsl:template>
</xsl:stylesheet>
