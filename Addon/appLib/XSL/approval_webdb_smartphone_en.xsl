<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

	<xsl:template match="DOCUMENT">
		<xsl:if test="XDB_TITLE">
			<div class="xdb_layout_title">
				<xsl:value-of select="./XDB_TITLE" disable-output-escaping="yes"/>
			</div>
		</xsl:if>
		<div class="xdb_layout_contents">
			<xsl:apply-templates select="./XDB_LAYOUT" />
		</div>
		<xsl:apply-templates select="./RRDFREEFORM" />
		<xsl:apply-templates select="./RRDOFFICE" />
		<xsl:apply-templates select="./externalDocument" />
		<xsl:apply-templates select="./RRDATTACHMENTCOMMENT" />
		<xsl:apply-templates select="./RRDBBS" />
	</xsl:template>


	<xsl:template match="XDB_LAYOUT">
		<xsl:choose>
			<!-- v2.11c SP2 #23548  -->
			<xsl:when test="@type='detail'">
				<xsl:choose>
					<!-- Old version ( < v2.11) -->
				    <xsl:when test="not(@rows)">
				    	<table style="border:1px solid #C4C0C9;" cellspacing="0" cellpadding="0" border-collapse="collapse">
							<xsl:apply-templates select="./XDB_ROW" />
						</table>
					</xsl:when>
					<!-- New version (v2.11) -->
				    <xsl:otherwise>
				    	<table width="100%" style="border:1px solid #C4C0C9; border-bottom: none;" cellspacing="0" cellpadding="0" border-collapse="collapse">
							<thead>
								<xsl:apply-templates select="./XDB_ROW[@type='header']" />
							</thead>
							<tbody>
								<xsl:apply-templates select="./XDB_ROW[@type='data']" />
							</tbody>
						</table>
				    </xsl:otherwise>
				</xsl:choose>
				<br/>
			</xsl:when>
			<xsl:when test="@type='custom'">
				<table width="100%" cellspacing="0" cellpadding="0" border-collapse="collapse">
					<xsl:if test="string-length(@border)>0">
						<xsl:attribute name="style">
							<xsl:text>border-right:1px solid #C4C0C9;border-bottom:1px solid #C4C0C9;</xsl:text>
						</xsl:attribute>
					</xsl:if>
					<xsl:apply-templates select="./XDB_ROW" />
				</table>
				<br/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="./XDB_ROW" />
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<xsl:template match="XDB_ROW">
		<xsl:variable name="rowCount">
			<xsl:number count="XDB_ROW" />
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="../@type='detail'">
				<tr style="height:19px">
					<xsl:apply-templates select="./XDB_CELL">
						<xsl:with-param name="rowCount" select="$rowCount" />
					</xsl:apply-templates>
				</tr>
			</xsl:when>
			<xsl:when test="../@type='custom'">
				<tr style="height:19px">
					<xsl:apply-templates select="./XDB_CELL" />
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="./XDB_CELL" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="XDB_CELL">
		<xsl:param name="rowCount" />
		<xsl:variable name="cellCount">
			<xsl:number count="XDB_CELL" />
		</xsl:variable>
		<xsl:if test="../../@type='standard'">
			<xsl:if test="string-length(@header)>0">
				<div><b><xsl:value-of select="@header" disable-output-escaping="yes" /></b></div>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="./XDB_ITEM">
					<xsl:apply-templates select="./XDB_ITEM" />
					<xsl:if test="../../@type!='custom'">
						<br/>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="string-length(@header)>0">
						<br/>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<!-- v2.11c SP2 #23548  -->
		<xsl:choose>
			<!-- Old version ( < v2.11) -->
		    <xsl:when test="not(@rows)">
				<xsl:if test="../../@type='detail'">
					<td>
						<xsl:attribute name="style">
								<xsl:if test="$rowCount>1">
									<xsl:text>border-top:1px solid #C4C0C9;</xsl:text>
								</xsl:if>
								<xsl:if test="$cellCount>1">
									<xsl:text>border-left:1px solid #C4C0C9;</xsl:text>
								</xsl:if>
								<xsl:if test="../@type='header'">
									<xsl:text>background-color: #CCCCCC;color: #FFFFFF;font-weight:700;</xsl:text>
								</xsl:if>
								<xsl:if test="@text-align">
									<xsl:value-of select="@text-align" />
								</xsl:if>
						</xsl:attribute>
						<xsl:apply-templates select="./XDB_LABEL" />
					</td>
				</xsl:if>		    
			</xsl:when>
			<!-- New version (v2.11) -->
		    <xsl:otherwise>
		    	<xsl:if test="../../@type='detail'">
					<xsl:choose>
						<xsl:when test="../@type='header'">
							<th>
								<xsl:attribute name="style">
									<xsl:text>background-color: #CCCCCC;color: #FFFFFF;font-weight:700;</xsl:text>
									
									<xsl:if test="@isLast='false'">
										<xsl:text>border-right:1px solid #C4C0C9;</xsl:text>
									</xsl:if>
									<xsl:if test="@isHeadBorderBottom='true'">
										<xsl:text>border-bottom:1px solid #C4C0C9;</xsl:text>
									</xsl:if>
									<xsl:if test="@text-align">
											<xsl:value-of select="@text-align" />
										</xsl:if>
									<xsl:if test="@hnowarp!='0'">
										<xsl:text>;white-space:nowrap</xsl:text>
									</xsl:if>
								</xsl:attribute>
				  				<xsl:attribute name="rowspan">
					    			<xsl:value-of select="@rows" />
					  			</xsl:attribute>
								<xsl:attribute name="colspan">
					    			<xsl:value-of select="@cols" />
					  			</xsl:attribute>
					  			<xsl:apply-templates select="./XDB_LABEL" />
							</th>
						</xsl:when>
						
						<xsl:when test="../@type='data'">
							<td>
								<xsl:attribute name="style">
									<xsl:if test="@islast='false'">
										<xsl:text>border-right:1px solid #C4C0C9;</xsl:text>
									</xsl:if>
									<xsl:text>border-bottom:1px solid #C4C0C9;</xsl:text>
									<xsl:if test="@text-align">
											<xsl:value-of select="@text-align" />
										</xsl:if>
									<xsl:if test="@hnowarp!='0'">
										<xsl:text>;white-space:nowrap</xsl:text>
									</xsl:if>
								</xsl:attribute>
				  				<xsl:attribute name="rowspan">
					    			<xsl:value-of select="@rows" />
					  			</xsl:attribute>
								<xsl:attribute name="colspan">
					    			<xsl:value-of select="@cols" />
					  			</xsl:attribute>
								<xsl:apply-templates select="./XDB_LABEL" />
							</td>
						</xsl:when>
					</xsl:choose>
				</xsl:if>
		    </xsl:otherwise>
		</xsl:choose>
		<xsl:if test="../../@type='custom'">
			<td>
				<xsl:if test="string-length(../../@border)>0">
					<xsl:attribute name="style">
						<xsl:text>border-top:1px solid #C4C0C9;</xsl:text>
						<xsl:text>border-left:1px solid #C4C0C9;</xsl:text>
					</xsl:attribute>
				</xsl:if>
				<xsl:attribute name="rowspan">
	    			<xsl:value-of select="@rows" />
	  			</xsl:attribute>
				<xsl:attribute name="colspan">
	    			<xsl:value-of select="@cols" />
	  			</xsl:attribute>
	  			<xsl:apply-templates select="./XDB_ITEM" />
			</td>
		</xsl:if>
	</xsl:template>

	<xsl:template match="XDB_LABEL">
		<xsl:choose>
			<xsl:when test="@type='55'"><!-- attach file -->
				<xsl:variable name="fileUrl">
					<xsl:text  disable-output-escaping="yes">/pe4x/Download/</xsl:text>
					<xsl:value-of select="@fileName" disable-output-escaping="yes" />
					<xsl:text  disable-output-escaping="yes">?id=</xsl:text>
					<xsl:value-of select="@docId" disable-output-escaping="yes" />
					<xsl:if test="string-length(@docId)=0">
						<xsl:text disable-output-escaping="yes">&amp;temporary=</xsl:text>
						<xsl:value-of select="@temporary" disable-output-escaping="yes" />
					</xsl:if>
				</xsl:variable>
				<div>
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="$fileUrl"></xsl:value-of>
						</xsl:attribute>
						<xsl:attribute name="download"/>													
						<xsl:attribute name="target">
							<xsl:text  disable-output-escaping="yes">_blank</xsl:text>
						</xsl:attribute>
						<img src="/pe4x/img/b_attachment.gif" />
						<xsl:value-of select="." disable-output-escaping="yes" />
					</a>
					<xsl:value-of select="@fileSize" disable-output-escaping="yes" />
				</div>
			</xsl:when>
			<xsl:when test="@type='56'"><!-- image -->
				<div>
					<xsl:value-of select="." disable-output-escaping="yes" />
				</div>
			</xsl:when>
			<xsl:when test="@type='57'"><!-- url -->
				<xsl:choose>
					<xsl:when test="string-length(@href)>0">
						<a>
							<xsl:attribute name="href">
								<xsl:value-of select="@href"></xsl:value-of>
							</xsl:attribute>
							<xsl:attribute name="target">
								<xsl:text  disable-output-escaping="yes">_blank</xsl:text>
							</xsl:attribute>
							<xsl:choose>
								<xsl:when test="string-length(.)>0">
									<xsl:value-of select="." disable-output-escaping="yes" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="@href"></xsl:value-of>
								</xsl:otherwise>
							</xsl:choose>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="." disable-output-escaping="yes" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type='53'"><!-- customer -->
				<xsl:if test="string-length(@ent)>0">
					<div><xsl:value-of select="@ent" disable-output-escaping="yes"/></div>
				</xsl:if>
				<xsl:if test="string-length(@per)>0">
					<div><xsl:value-of select="@per" disable-output-escaping="yes"/></div>
				</xsl:if>
				<xsl:if test="string-length(@pos)>0">
					<div><xsl:value-of select="@pos" disable-output-escaping="yes"/></div>
				</xsl:if>
			</xsl:when>
			<xsl:when test="@type='62'">
				<xsl:value-of select="@empName" disable-output-escaping="yes" />
				<xsl:choose>
					<xsl:when test="@countOtherEmployee>0">
							<xsl:value-of select="@otherEmployee" disable-output-escaping="yes" />
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<div><xsl:value-of select="." disable-output-escaping="yes" /></div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="XDB_ITEM">
		<xsl:choose>
			<xsl:when test="@type='81'">
				<xsl:value-of select="./XDB_DATA/XDB_NAME" disable-output-escaping="yes" />
			</xsl:when>
			<xsl:when test="@type='83'">
				<br/>
			</xsl:when>
			<xsl:when test="@type='82'">
				<hr width="100%"/>
			</xsl:when>
			<xsl:when test="@type='84'">
				<div>(Image)</div>
			</xsl:when>
			<xsl:otherwise>
				<!-- <xsl:if test="string-length(@spLabel)>0"> -->
				<xsl:if test="string-length(@itemLabel)>0">
					<div><b><xsl:value-of select="@itemLabel" disable-output-escaping="yes" /></b></div>
				</xsl:if>
				<div><xsl:apply-templates select="./XDB_DATA" /></div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="XDB_DATA">
		<xsl:choose>
			<xsl:when test="../@type='14'">
				<xsl:apply-templates select="./XDB_CODE" />
			</xsl:when>
			<xsl:when test="../@type='55'"><!-- attach file -->
				<xsl:variable name="fileUrl">
					<xsl:text  disable-output-escaping="yes">/pe4x/Download/</xsl:text>
					<xsl:value-of select="@fileName" disable-output-escaping="yes" />
					<xsl:text  disable-output-escaping="yes">?id=</xsl:text>
					<xsl:value-of select="@docId" disable-output-escaping="yes" />
					<xsl:if test="string-length(@docId)=0">
						<xsl:text disable-output-escaping="yes">&amp;temporary=</xsl:text>
						<xsl:value-of select="@temporary" disable-output-escaping="yes" />
					</xsl:if>
				</xsl:variable>
				<div>
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="$fileUrl"></xsl:value-of>
						</xsl:attribute>
						<xsl:attribute name="download"/>													
						<xsl:attribute name="target">
							<xsl:text  disable-output-escaping="yes">_blank</xsl:text>
						</xsl:attribute>
						<img src="/pe4x/img/b_attachment.gif" />
						<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
					</a>
					<xsl:value-of select="@fileSize" disable-output-escaping="yes" />
				</div>
			</xsl:when>
			<xsl:when test="../@type='56'"><!-- image -->
				<div>
					<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
				</div>
			</xsl:when>
			<xsl:when test="../@type='57'"><!-- url -->
				<xsl:choose>
					<xsl:when test="string-length(./XDB_CODE)>0">
						<a>
							<xsl:attribute name="href">
								<xsl:value-of select="./XDB_CODE"></xsl:value-of>
							</xsl:attribute>
							<xsl:attribute name="target">
								<xsl:text  disable-output-escaping="yes">_blank</xsl:text>
							</xsl:attribute>
							<xsl:choose>
								<xsl:when test="string-length(./XDB_NAME)>0">
									<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="./XDB_CODE"></xsl:value-of>
								</xsl:otherwise>
							</xsl:choose>
						</a>
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="../@type='53'"><!-- customer -->
				<div><xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" /></div>
			</xsl:when>
			<xsl:when test="../@type='62'"><!-- Employees -->
				<xsl:value-of select="./XDB_NAME" /><br/>
			</xsl:when>
			<xsl:otherwise>
				<div><xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" /></div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="XDB_CODE">
		<xsl:apply-templates select="./RRDFREEFORM" />
	</xsl:template>


<!-- フリー域のテンプレート -->
	<xsl:template match="RRDFREEFORM">
		<xsl:if test="string-length(./text())>0">
			<xsl:value-of select="." disable-output-escaping="yes" />
			<div style="clear:both;height:1px"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></div>
		</xsl:if>
	</xsl:template>

<!--
	Original: 2004.10.12 V1.7
	Office ActiveX
-->
	<xsl:template match="RRDOFFICE" >
		<xsl:if test="OFFICEDOCID[text()] != '' " >
			<span style="color:#FF0000">* There is a Excel content.(Can not view from this device)</span>
			<br/>
		</xsl:if>
	</xsl:template>


<!-- StraForm域のテンプレート -->
	<xsl:template match="externalDocument">
		<xsl:if test="documentId[text()] != '' and formId[text()] != ''" >
			<span style="color:#FF0000">* There is a StraForm content. (Can not view from this device)</span>
			<br/>
		</xsl:if>
	</xsl:template>


<!-- attachment, paperDocument -->
	<xsl:template match="RRDATTACHMENTCOMMENT">
		<xsl:if test="string-length(./text())>0">
				<span style="color:#FF0000">[Paper attachement]</span>
				<br/>
				<xsl:value-of select="." disable-output-escaping="yes" />
				<br/>
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
					<span style="color:#FF0000">[Employee responsible for posting]</span>
					<br/>
					<xsl:value-of select="./CHARGEEMPNAME" disable-output-escaping="yes" />
					<br/>
					<span style="color:#FF0000">[Posting period]</span>
					<br/>
					<xsl:value-of select="./DISPSTARTDATE" disable-output-escaping="yes" />
					-
					<xsl:value-of select="./DISPENDDATE" disable-output-escaping="yes" />
					<br/>
					<span style="color:#FF0000">[NaviView notice period]</span>
					<br/>
					<xsl:choose>
						<xsl:when test="./NAVIVIEWDISPOFF">
							<xsl:value-of select="./NAVIVIEWDISPOFF" disable-output-escaping="yes" />
							<br/>
						</xsl:when>
						<xsl:when test="./NAVIVIEWDISPON">
							<xsl:value-of select="./NAVIVIEWDISPON" disable-output-escaping="yes" />
							<br/>
							<xsl:value-of select="./NAVIBBSDISP" disable-output-escaping="yes" />
							<br/>
							<xsl:choose>
								<xsl:when test="./NAVIBBSDISPSTARTDATE">
									<span style="color:#FF0000">[Notice period]</span>
									<br/>
									<xsl:value-of select="./NAVIBBSDISPSTARTDATE" disable-output-escaping="yes" />
									-
									<xsl:value-of select="./NAVIBBSDISPENDDATE" disable-output-escaping="yes" />
									<br/>
								</xsl:when>
							</xsl:choose>
							<xsl:value-of select="./SCHEDULEBBSDISP" disable-output-escaping="yes" />
							<br/>
							<xsl:choose>
								<xsl:when test="./NAVIEVENTDISPSTARTDATE">
									<span style="color:#FF0000">[Notice period]</span>
									<br/>
									<xsl:value-of select="./NAVIEVENTDISPSTARTDATE" disable-output-escaping="yes" />
									-
									<xsl:value-of select="./NAVIEVENTDISPENDDATE" disable-output-escaping="yes" />
									<br/>
									<span style="color:#FF0000">[Event period]</span>
									<br/>
									<xsl:choose>
										<xsl:when test="./HOLDINGFROMDATE">
											<xsl:value-of select="./HOLDINGFROMDATE" disable-output-escaping="yes" />
											-
											<xsl:value-of select="./HOLDINGTODATE" disable-output-escaping="yes" />
										</xsl:when>
									</xsl:choose>
									<br/>
									<span style="color:#FF0000">[Event location]</span>
									<br/>
									<xsl:value-of select="./HOLDINGPLACE" disable-output-escaping="yes" />
									<br/>
								</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
					<span style="color:#FF0000">[Post board]</span>
					<br/>
					<xsl:value-of select="./BBSID" disable-output-escaping="yes" />
				</xsl:when>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
