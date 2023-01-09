<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" omit-xml-declaration="yes" indent="yes" encoding="utf-8"/>

	<xsl:template match="DOCUMENT">
	<xsl:if test="XDB_TITLE">
			<div>
				<xsl:value-of select="./XDB_TITLE" disable-output-escaping="yes"/>
			</div>
	</xsl:if>
	<div class="contents">
		<xsl:if test="XDB_LAYOUT">
				<xsl:apply-templates select="./XDB_LAYOUT"/>
		</xsl:if>
	</div>
	<xsl:apply-templates select="./RFM_USERFORMAT" />
	<xsl:apply-templates select="./RRDFREEFORM" />
	<xsl:apply-templates select="./RRDOFFICE" />
	<xsl:apply-templates select="./externalDocument" />
	<xsl:apply-templates select="./RRDATTACHMENTCOMMENT" />
	<xsl:apply-templates select="./RRDBBS" />
	</xsl:template>



	<!-- WebDB begin -->
	<!-- ScreenLayout -->
	<xsl:template match="XDB_LAYOUT">
		<xsl:variable name="layoutCount">
			<xsl:number count="XDB_LAYOUT" />
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="@type='standard'">
				<xsl:choose>
					<xsl:when test="@frame and @frame='1'">
						<div>
							<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="$layoutCount>1">
									<xsl:text>contents margin</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>contents</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							</xsl:attribute>
							<table>
								<xsl:attribute name="class">
									<xsl:text>xdb_stdlayout detail</xsl:text>
								</xsl:attribute>
								<xsl:apply-templates select="./XDB_ROW" />
							</table>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<table>
							<xsl:attribute name="class">
								<xsl:text>xdb_stdlayout detail </xsl:text>
								<xsl:if test="$layoutCount>1">
									<xsl:text>margin</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:apply-templates select="./XDB_ROW" />
						</table>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type='custom'">
				<xsl:choose>
					<xsl:when test="@frame and @frame='1'">
						<div>
							<xsl:attribute name="class">
							<xsl:choose>
								<xsl:when test="$layoutCount>1">
									<xsl:text>contents margin</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>contents</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							</xsl:attribute>
							<table>
								<xsl:attribute name="style">
									<xsl:text>width:99%;border-collapse: collapse;border: </xsl:text>
					    			<xsl:value-of select="@border" />
					    			<xsl:text>;border-color: </xsl:text>
					    			<xsl:value-of select="@border-color" />
					    			<xsl:text>;</xsl:text>
					  			</xsl:attribute>
								<xsl:attribute name="cellspacing">0</xsl:attribute>
								<xsl:apply-templates select="./XDB_ROW" />
							</table>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<table>
							<xsl:if test="$layoutCount>1">
								<xsl:attribute name="class">
									<xsl:text>margin</xsl:text>
								</xsl:attribute>
							</xsl:if>
							<xsl:attribute name="style">
								<xsl:text>width:99%;border-collapse: collapse;border: </xsl:text>
				    			<xsl:value-of select="@border" />
				    			<xsl:text>;border-color: </xsl:text>
				    			<xsl:value-of select="@border-color" />
				    			<xsl:text>;</xsl:text>
				  			</xsl:attribute>
							<xsl:attribute name="cellspacing">0</xsl:attribute>
							<xsl:apply-templates select="./XDB_ROW" />
						</table>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type='detail'">
				<div class="contents">
					<xsl:attribute name="class">
						<xsl:if test="$layoutCount>1">
							<xsl:text> margin</xsl:text>
						</xsl:if>
					</xsl:attribute>
					<!-- v2.11c SP2 #23548  -->
					<!-- check version -->
					<xsl:attribute name="rowspan">
		    			<xsl:value-of select="@rows" />
		  			</xsl:attribute>
					<table class="dr-table rich-table listHighlight" cellspacing="0" cellpadding="0" border="0" >
						<!-- Move @type='header' row to thead tag -->
						<thead>
							<xsl:apply-templates select="./XDB_ROW[@type='header']" />
						</thead>
						<!-- Move @type='data' row to tbody tag -->
						<xsl:for-each select="./XDB_ROW[@type='data']">
							<!-- Get counting of previous sibling node having @style = current style -->
							<xsl:variable name="prevRow" select="preceding-sibling::XDB_ROW[1]" />
							<!-- For each group, put data to tbody tag -->
							<xsl:if test="not($prevRow/@type = current()/@type) or not($prevRow/@style = current()/@style)">
								<xsl:variable name="curStyle" select="current()/@style"/>
								<xsl:variable name="diffStyleRowCnt" select="count(preceding-sibling::XDB_ROW[@type='data' and not(@style=$curStyle)])"/>
								<tbody class="rf-cst">
									<xsl:apply-templates select="." />
									<!-- Get all following sibling having @style = current style -->
									<xsl:for-each select="following-sibling::XDB_ROW[@type='data' and @style = current()/@style
											 and count(preceding-sibling::XDB_ROW[@type='data' and not(@style = $curStyle)]) = $diffStyleRowCnt]">
										<xsl:apply-templates select="." />
									</xsl:for-each>
								</tbody>
							</xsl:if>
						</xsl:for-each>
					</table>
				</div>
			</xsl:when>
		</xsl:choose>
	</xsl:template>


	<!-- Row for Layout -->
	<xsl:template match="XDB_ROW">
		<xsl:choose>
			<xsl:when test="@type">
				<xsl:choose>
					<xsl:when test="@type='header'">
						<tr class="dr-table-header rich-table-header">
							<xsl:apply-templates select="./XDB_CELL" />
						</tr>
					</xsl:when>
					<xsl:when test="@type='data'">
						<tr style="height:19px">
							<xsl:attribute name="class">
								<xsl:choose>
									<xsl:when test="@style='odd'">
										<xsl:text>dr-table-firstrow rich-table-firstrow oddRow</xsl:text>
									</xsl:when>
									<xsl:when test="@style='even'">
										<xsl:text>dr-table-firstrow rich-table-firstrow evenRow</xsl:text>
									</xsl:when>
								</xsl:choose>
							</xsl:attribute>
							<xsl:apply-templates select="./XDB_CELL" />
						</tr>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<tr style="height:19px">
					<xsl:apply-templates select="./XDB_CELL" />
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- LayoutCell -->
	<xsl:template match="XDB_CELL">
		<xsl:choose>
			<xsl:when test="./XDB_ITEM">
				<xsl:choose>
					<xsl:when test="../../@type='standard'">
						<td class="label">
							<xsl:attribute name="rowspan">
								<xsl:value-of select="@rows" disable-output-escaping="yes" />
							</xsl:attribute>
							<xsl:attribute name="style">
								<xsl:text>text-align: </xsl:text>
								<xsl:value-of select="@htext-align" />
								<xsl:text>;background-color: </xsl:text>
								<xsl:value-of select="@hbackground-color" />
								<xsl:text>;color: </xsl:text>
								<xsl:value-of select="@hcolor" />
								<xsl:text>;font-size: </xsl:text>
								<xsl:value-of select="@hfont-size" />
								<xsl:text>;text-align: </xsl:text>
								<xsl:value-of select="@htext-align" />
								<xsl:text>;vertical-align: </xsl:text>
								<xsl:value-of select="@hvertical-align" />
								<xsl:text>;</xsl:text>
								<xsl:if test="string-length(@hwidth) > 0">
									<xsl:text>width: </xsl:text>
									<xsl:value-of select="@hwidth" />
									<xsl:text>;</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:value-of select="@header" disable-output-escaping="yes" />
						</td>
					</xsl:when>
					<xsl:when test="../../@type='custom'">

					</xsl:when>
				</xsl:choose>
				<td>
					<xsl:if test="../../@type='standard'">
						<xsl:attribute name="class">
			    			item
			  			</xsl:attribute>
						<xsl:attribute name="colspan">
			    			<xsl:value-of select="(@cols*2)-1" />
			  			</xsl:attribute>
						<xsl:attribute name="rowspan">
			    			<xsl:value-of select="@rows" />
			  			</xsl:attribute>
			  			<xsl:attribute name="style">
			  				<xsl:text>text-align: </xsl:text>
							<xsl:value-of select="@text-align" />
							<xsl:text>;background-color: </xsl:text>
							<xsl:value-of select="@background-color" />
							<xsl:text>;vertical-align: </xsl:text>
							<xsl:value-of select="@vertical-align" />
							<xsl:text>;</xsl:text>
							<xsl:if test="string-length(@height) > 0">
								<xsl:text>height: </xsl:text>
								<xsl:value-of select="@height" />
								<xsl:text>;</xsl:text>
							</xsl:if>
							<xsl:if test="string-length(@width) > 0">
								<xsl:text>width: </xsl:text>
								<xsl:value-of select="@width" />
								<xsl:text>;</xsl:text>
							</xsl:if>
			  			</xsl:attribute>
					</xsl:if>
					<xsl:if test="../../@type='custom'">
						<xsl:attribute name="rowspan">
			    			<xsl:value-of select="@rows" />
			  			</xsl:attribute>
						<xsl:attribute name="colspan">
			    			<xsl:value-of select="@cols" />
			  			</xsl:attribute>
						<xsl:attribute name="style">
							<xsl:text>padding-left:5px;padding-right:5px;padding-top:2px;</xsl:text>
							<xsl:text>border: </xsl:text>
							<xsl:value-of select="../../@border" />
			    			<xsl:text>;border-color: </xsl:text>
			    			<xsl:value-of select="../../@border-color" />
			    			<xsl:text>;text-align: </xsl:text>
							<xsl:value-of select="@text-align" />
							<xsl:text>;background-color: </xsl:text>
							<xsl:value-of select="@background-color" />
							<xsl:text>;vertical-align: </xsl:text>
							<xsl:value-of select="@vertical-align" />
							<xsl:text>;</xsl:text>
							<xsl:if test="string-length(@height) > 0">
								<xsl:text>height: </xsl:text>
								<xsl:value-of select="@height" />
								<xsl:text>;</xsl:text>
							</xsl:if>
							<xsl:if test="string-length(@width) > 0">
								<xsl:text>width: </xsl:text>
								<xsl:value-of select="@width" />
								<xsl:text>;</xsl:text>
							</xsl:if>
			  			</xsl:attribute>
					</xsl:if>
					<xsl:if test="./XDB_ITEM">
						<xsl:apply-templates select="./XDB_ITEM" />
					</xsl:if>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="../../@type='standard'">
						<td class="label">
							<xsl:attribute name="rowspan">
								<xsl:value-of select="@rows" disable-output-escaping="yes" />
							</xsl:attribute>
							<xsl:attribute name="style">
								<xsl:text>text-align: </xsl:text>
								<xsl:value-of select="@htext-align" />
								<xsl:text>;background-color: </xsl:text>
								<xsl:value-of select="@hbackground-color" />
								<xsl:text>;color: </xsl:text>
								<xsl:value-of select="@hcolor" />
								<xsl:text>;font-size: </xsl:text>
								<xsl:value-of select="@hfont-size" />
								<xsl:text>;vertical-align: </xsl:text>
								<xsl:value-of select="@hvertical-align" />
								<xsl:text>;</xsl:text>
								<xsl:if test="string-length(@hwidth) > 0">
									<xsl:text>width: </xsl:text>
									<xsl:value-of select="@hwidth" />
									<xsl:text>;</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:value-of select="@header" disable-output-escaping="yes" />
							<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
						</td>
						<td class="item">
							<xsl:attribute name="colspan">
				    			<xsl:value-of select="(@cols*2)-1" />
				  			</xsl:attribute>
							<xsl:attribute name="rowspan">
				    			<xsl:value-of select="@rows" />
				  			</xsl:attribute>
				  			<xsl:attribute name="style">
				  				<xsl:text>text-align: </xsl:text>
								<xsl:value-of select="@text-align" />
								<xsl:text>;background-color: </xsl:text>
								<xsl:value-of select="@background-color" />
								<xsl:text>;vertical-align: </xsl:text>
								<xsl:value-of select="@vertical-align" />
								<xsl:text>;</xsl:text>
								<xsl:if test="string-length(@height) > 0">
									<xsl:text>height: </xsl:text>
									<xsl:value-of select="@height" />
									<xsl:text>;</xsl:text>
								</xsl:if>
								<xsl:if test="string-length(@width) > 0">
									<xsl:text>width: </xsl:text>
									<xsl:value-of select="@width" />
									<xsl:text>;</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
						</td>
					</xsl:when>
					<xsl:when test="../../@type='custom'">
						<td>
							<xsl:attribute name="rowspan">
								<xsl:value-of select="@rows" />
							</xsl:attribute>
							<xsl:attribute name="colspan">
								<xsl:value-of select="@cols" />
							</xsl:attribute>
							<xsl:attribute name="style">
								<xsl:text>padding-left:5px;padding-right:5px;padding-top:2px;</xsl:text>
								<xsl:text>border: </xsl:text>
								<xsl:value-of select="../../@border" />
								<xsl:text>;border-color: </xsl:text>
								<xsl:value-of select="../../@border-color" />
								<xsl:text>;text-align: </xsl:text>
								<xsl:value-of select="@text-align" />
								<xsl:text>;background-color: </xsl:text>
								<xsl:value-of select="@background-color" />
								<xsl:text>;vertical-align: </xsl:text>
								<xsl:value-of select="@vertical-align" />
								<xsl:text>;</xsl:text>
								<xsl:if test="string-length(@height) > 0">
									<xsl:text>height: </xsl:text>
									<xsl:value-of select="@height" />
									<xsl:text>;</xsl:text>
								</xsl:if>
								<xsl:if test="string-length(@width) > 0">
									<xsl:text>width: </xsl:text>
									<xsl:value-of select="@width" />
									<xsl:text>;</xsl:text>
								</xsl:if>
							</xsl:attribute>
							<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
						</td>
					</xsl:when>
					<xsl:when test="../../@type='detail'">
						<xsl:choose>
							<xsl:when test="../@type='header'">
								<th>
									<xsl:attribute name="class">
										<xsl:if test="@isLast='false'">
											<xsl:text>label dr-table-headercell rich-table-headercell headBorderRight</xsl:text>
											<xsl:if test="@isHeadBorderBottom='true'">
												<xsl:text>label dr-table-headercell rich-table-headercell headBorderRight headBorderBottom_XDB</xsl:text>
											</xsl:if>
										</xsl:if>
										<xsl:if test="@isLast='true'">
											<xsl:text>label dr-table-headercell rich-table-headercell</xsl:text>
											<xsl:if test="@isHeadBorderBottom='true'">
												<xsl:text>label dr-table-headercell rich-table-headercell headBorderBottom_XDB</xsl:text>
											</xsl:if>
										</xsl:if>
									</xsl:attribute>
									<xsl:attribute name="style">
						    			<xsl:text>background-color: </xsl:text>
										<xsl:value-of select="@hbackground-color" />
										<xsl:text>;font-size: </xsl:text>
										<xsl:value-of select="@hfont-size" />
										<xsl:text>;color: </xsl:text>
										<xsl:value-of select="@hcolor" />
										<xsl:text>;text-align: </xsl:text>
										<xsl:value-of select="@htext-align" />
										<xsl:text>;vertical-align: </xsl:text>
										<xsl:value-of select="@hvertical-align" />
										<xsl:if test="string-length(@hwidth) > 0">
											<xsl:text>;width: </xsl:text>
											<xsl:value-of select="@hwidth" />
											<xsl:text>em</xsl:text>
										</xsl:if>
										<xsl:if test="@hnowarp!='0'">
											<xsl:text>;white-space:nowrap</xsl:text>
										</xsl:if>
					  				</xsl:attribute>
					  				<!-- v2.11c SP2 #23548  -->
					  				<xsl:attribute name="rowspan">
						    			<xsl:value-of select="@rows" />
						  			</xsl:attribute>
									<xsl:attribute name="colspan">
						    			<xsl:value-of select="@cols" />
						  			</xsl:attribute>

					  				<xsl:choose>
										<xsl:when test="string-length(@hwidth) > 0 and @hnowarp='0'">
											<div class="wordbreakAll">
												<xsl:choose>
													<xsl:when test="string-length(XDB_LABEL/@header) > 0">
														<xsl:value-of select="XDB_LABEL/@header" disable-output-escaping="yes" />
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="XDB_LABEL" disable-output-escaping="yes" />
													</xsl:otherwise>
												</xsl:choose>
											</div>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose>
												<xsl:when test="string-length(XDB_LABEL/@header) > 0">
													<xsl:value-of select="XDB_LABEL/@header" disable-output-escaping="yes" />
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="XDB_LABEL" disable-output-escaping="yes" />
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>

								</th>
							</xsl:when>
							<xsl:when test="../@type='data'">
								<td>
									<xsl:attribute name="class">
										<xsl:value-of select="@class" disable-output-escaping="yes" />
									</xsl:attribute>
									<xsl:attribute name="style">
										<xsl:text>background-color: </xsl:text>
										<xsl:value-of select="@background-color" />
										<xsl:text>;text-align: </xsl:text>
										<xsl:value-of select="@text-align" />
										<xsl:text>;vertical-align: </xsl:text>
										<xsl:value-of select="@vertical-align" />
					  				</xsl:attribute>
					  				<!-- v2.11c SP2 #23548  -->
					  				<xsl:attribute name="rowspan">
						    			<xsl:value-of select="@rows" />
						  			</xsl:attribute>
									<xsl:attribute name="colspan">
						    			<xsl:value-of select="@cols" />
						  			</xsl:attribute>

					  				<span>
					  					<xsl:choose>
					  						<xsl:when test="XDB_LABEL/@type and (XDB_LABEL/@type = '52' or XDB_LABEL/@type = '62')">
							  					<xsl:attribute name="style">
							  						<xsl:text>white-space: </xsl:text>
													<xsl:value-of select="@nowrap" />
							  					</xsl:attribute>
						  					</xsl:when>
						  					<xsl:otherwise>
						  						<xsl:attribute name="style">
							  						<xsl:text>font-weight: </xsl:text>
													<xsl:value-of select="@font-weight" />
													<xsl:text>;font-style: </xsl:text>
													<xsl:value-of select="@font-style" />
													<xsl:text>;text-decoration: </xsl:text>
													<xsl:value-of select="@text-decoration" />
													<xsl:text>;color: </xsl:text>
													<xsl:value-of select="@color" />
													<xsl:text>;font-size: </xsl:text>
													<xsl:value-of select="@font-size" />
													<xsl:text>;white-space: </xsl:text>
													<xsl:value-of select="@nowrap" />
							  					</xsl:attribute>
						  					</xsl:otherwise>
					  					</xsl:choose>

										<xsl:apply-templates select="XDB_LABEL" />
										<xsl:if test="string-length(XDB_LABEL)=0">
											<xsl:if test="./@type != '57'">
												<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
											</xsl:if>
										</xsl:if>
									</span>
								</td>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- layoutItem -->
	<xsl:template match="XDB_ITEM">
		<xsl:variable name="itemCount">
			<xsl:number count="XDB_ITEM" />
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="@type='81'"><!-- fixed string -->
				<span>
					<xsl:attribute name="style">
						<xsl:text>display: inline-block;vertical-align: top;</xsl:text>
						<xsl:text>margin-right:10px;</xsl:text>
						<xsl:text>color: </xsl:text>
						<xsl:value-of select="@color" />
		    			<xsl:text>;font-size: </xsl:text>
		    			<xsl:value-of select="@font-size" />
		    			<xsl:text>;font-weight: </xsl:text>
		    			<xsl:value-of select="@font-weight" />
		    			<xsl:text>;font-style: </xsl:text>
		    			<xsl:value-of select="@font-style" />
		    			<xsl:text>;text-decoration: </xsl:text>
		    			<xsl:value-of select="@text-decoration" />
		    			<xsl:text>;vertical-align: </xsl:text>
		    			<xsl:value-of select="../@vertical-align" />
					</xsl:attribute>
					<xsl:choose>
						<xsl:when test="string-length(@href)>0">
							<a>
		    					<xsl:attribute name="style">
		    						<xsl:text>color: </xsl:text>
									<xsl:value-of select="@color" />
									<xsl:text>;</xsl:text>
								</xsl:attribute>
								<xsl:attribute name="href">
									<xsl:value-of select="@href" />
								</xsl:attribute>
								<xsl:attribute name="target">
									<xsl:text>blank</xsl:text>
								</xsl:attribute>
								<xsl:apply-templates select="XDB_DATA" />
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="XDB_DATA" />
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</xsl:when>
			<xsl:when test="@type='82'"><!-- horizon line -->
				<div>
					<xsl:attribute name="align">
						<xsl:value-of select="@align" />
					</xsl:attribute>
					<hr>
						<xsl:attribute name="width">
							<xsl:value-of select="@hrwidth" />%
						</xsl:attribute>
					</hr>
				</div>
			</xsl:when>
			<xsl:when test="@type='83'"><!-- new line -->
				<br/>
			</xsl:when>
			<xsl:when test="@type='84'">
				<div>
					<xsl:attribute name="align">
						<xsl:value-of select="@align" />
					</xsl:attribute>
					<a>
						<xsl:attribute name="href">
							<xsl:if test="string-length(@href)>0">
								<xsl:value-of select="@href" />
							</xsl:if>
							<xsl:if test="string-length(@href)=0">
								<xsl:text>#</xsl:text>
							</xsl:if>
						</xsl:attribute>
						<img>
							<xsl:attribute name="height">
								<xsl:value-of select="@height" />
							</xsl:attribute>
							<xsl:attribute name="width">
								<xsl:value-of select="@width" />
							</xsl:attribute>
							<xsl:if test="string-length(./XDB_DATA/XDB_NAME)>0">
								<xsl:attribute name="alt">
									<xsl:value-of select="./XDB_DATA/XDB_NAME" />
								</xsl:attribute>
							</xsl:if>
							<xsl:variable name="imgUrl">
								<xsl:value-of select="./XDB_DATA/XDB_CODE" disable-output-escaping="yes" />
							</xsl:variable>
							<xsl:attribute name="src">
								<xsl:value-of select="$imgUrl"></xsl:value-of>
							</xsl:attribute>
						</img>
					</a>
				</div>
			</xsl:when>
			<xsl:when test="@type='14'">
				<div>
					<xsl:attribute name="class">
						<xsl:text>blockDisplayPrintOuter</xsl:text>
					</xsl:attribute>
					<xsl:attribute name="style">
						<xsl:text>vertical-align:</xsl:text>
						<xsl:value-of select="../@vertical-align" />
						<xsl:text>;</xsl:text>
						<xsl:if test="@type='11'">
							<xsl:text>text-align: left;</xsl:text>
						</xsl:if>
						<xsl:text>margin-right:10px;</xsl:text>
						<xsl:if test="string-length(@width) > 0">
							<xsl:text>;width:</xsl:text>
							<xsl:value-of select="@width" />
							<xsl:text>em</xsl:text>
						</xsl:if>
		 			</xsl:attribute>
		 			<xsl:if test="string-length(@itemLabel)>0">
			 			<span>
			 				<xsl:attribute name="style">
								<xsl:text>display: inline-block;vertical-align: top;</xsl:text>
								<xsl:text>;text-decoration: </xsl:text>
						    	<xsl:value-of select="@text-decoration" />
						    	<xsl:text>;font-weight: </xsl:text>
						    	<xsl:value-of select="@font-weight" />
						    	<xsl:text>;font-style: </xsl:text>
						    	<xsl:value-of select="@font-style" />
						    	<xsl:text>;color: </xsl:text>
						    	<xsl:value-of select="@color" />
						    	<xsl:text>;font-size: </xsl:text>
						    	<xsl:value-of select="@font-size" />
		 					</xsl:attribute>
			 				<xsl:value-of select="@itemLabel" />
			 			</span>
			 			<br/>
		 			</xsl:if>
		 			<span>
						<xsl:attribute name="class">
							<xsl:text>blockDisplayPrintInner</xsl:text>
						</xsl:attribute>
		 				<xsl:attribute name="style">
							<xsl:text>text-align:left;vertical-align: top</xsl:text>
					    	<xsl:text>;text-decoration: </xsl:text>
					    	<xsl:value-of select="@text-decoration" />
					    	<xsl:text>;font-weight: </xsl:text>
					    	<xsl:value-of select="@font-weight" />
					    	<xsl:text>;font-style: </xsl:text>
					    	<xsl:value-of select="@font-style" />
					    	<xsl:text>;color: </xsl:text>
					    	<xsl:value-of select="@color" />
					    	<xsl:text>;font-size: </xsl:text>
					    	<xsl:value-of select="@font-size" />
	 					</xsl:attribute>
						<xsl:apply-templates select="./XDB_DATA" />
					</span>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div>
					<xsl:if test="@type='11'">
						<xsl:attribute name="class">
							<xsl:text>blockDisplayPrintOuter</xsl:text>
						</xsl:attribute>
					</xsl:if>
					<xsl:attribute name="style">
						<xsl:text>display: inline-block;vertical-align:</xsl:text>
						<xsl:value-of select="../@vertical-align" />
						<xsl:text>;</xsl:text>
						<xsl:if test="@type='11'">
							<xsl:text>text-align: left;</xsl:text>
						</xsl:if>
						<xsl:text>margin-right:10px;</xsl:text>
		 			</xsl:attribute>
		 			<xsl:if test="string-length(@itemLabel)>0">
			 			<span>
			 				<xsl:attribute name="style">
								<xsl:text>display: inline-block;vertical-align: top;</xsl:text>
								<xsl:text>;text-decoration: </xsl:text>
						    	<xsl:value-of select="@text-decoration" />
						    	<xsl:text>;font-weight: </xsl:text>
						    	<xsl:value-of select="@font-weight" />
						    	<xsl:text>;font-style: </xsl:text>
						    	<xsl:value-of select="@font-style" />
						    	<xsl:text>;color: </xsl:text>
						    	<xsl:value-of select="@color" />
						    	<xsl:text>;font-size: </xsl:text>
						    	<xsl:value-of select="@font-size" />
		 					</xsl:attribute>
			 				<xsl:value-of select="@itemLabel" />
			 			</span>
		 			</xsl:if>
		 			<span>
						<xsl:if test="@type='11'">
							<xsl:attribute name="class">
								<xsl:text>blockDisplayPrintInner</xsl:text>
							</xsl:attribute>
						</xsl:if>
		 				<xsl:choose>
		 					<xsl:when test="@type and (@type = '52' or @type = '62')">
		 						<xsl:attribute name="style">
		 							<xsl:text>display: inline-block;text-align:left;</xsl:text>
		 						</xsl:attribute>
		 					</xsl:when>
		 					<xsl:otherwise>
		 						<xsl:attribute name="style">
									<xsl:text>display: inline-block;vertical-align: top</xsl:text>
							    	<xsl:text>;text-decoration: </xsl:text>
							    	<xsl:value-of select="@text-decoration" />
							    	<xsl:text>;font-weight: </xsl:text>
							    	<xsl:value-of select="@font-weight" />
							    	<xsl:text>;font-style: </xsl:text>
							    	<xsl:value-of select="@font-style" />
							    	<xsl:text>;color: </xsl:text>
							    	<xsl:value-of select="@color" />
							    	<xsl:text>;font-size: </xsl:text>
							    	<xsl:value-of select="@font-size" />
			 					</xsl:attribute>
		 					</xsl:otherwise>
		 				</xsl:choose>

						<xsl:apply-templates select="./XDB_DATA" />
					</span>
				</div>
				<xsl:if test="@type != '57'">
					<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Data of Item -->
	<xsl:template match="XDB_DATA">
		<xsl:variable name="dataCount">
			<xsl:number count="XDB_DATA" />
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="../@type='13'">
				<xsl:choose>
					<xsl:when test="string-length(./XDB_CODE)>0">
						<div style="display:inline;">
							<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
						</div>
					</xsl:when>
					<xsl:otherwise>
						<div class="guidance" style="display:inline;">
							<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="../@type='21'"><!-- checkbox -->
				<div align="left">
					<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
				</div>
			</xsl:when>
			<xsl:when test="../@type='53'"><!-- customer -->
				<xsl:choose>
					<xsl:when test="string-length(./XDB_CODE)>0">
						<a>
							<xsl:attribute name="href">
								<xsl:choose>
								    <xsl:when test="@cnt='0'">
								 		<xsl:text>/pe4j/CAP/CAP2001d.jsf?enterpriseID=</xsl:text>
								    </xsl:when>
								    <xsl:when test="@cnt='1'">
								   		<xsl:text>/pe4j/CAP/CAP3001d.jsf?postID=</xsl:text>
								    </xsl:when>
								    <xsl:otherwise>
									    <xsl:text>/pe4j/CAP/CAP4011d.jsf?personID=</xsl:text>
								    </xsl:otherwise>
								</xsl:choose>
								<xsl:value-of select="./XDB_CODE"></xsl:value-of>
							</xsl:attribute>
							<xsl:attribute name="target">
								<xsl:text  disable-output-escaping="yes">_blank</xsl:text>
							</xsl:attribute>
							<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
						</a>
				    	<br/>
					</xsl:when>
					<xsl:otherwise>
							<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="../@type='55'">
				<xsl:variable name="fileUrl">
					<xsl:text  disable-output-escaping="yes">/pe4j/servlet/Download/</xsl:text>
					<xsl:value-of select="@fileName" disable-output-escaping="yes" />
					<xsl:text  disable-output-escaping="yes">?id=</xsl:text>
					<xsl:value-of select="@docId" disable-output-escaping="yes" />
					<xsl:if test="string-length(@docId)=0">
						<xsl:text disable-output-escaping="yes">&amp;temporary=</xsl:text>
						<xsl:value-of select="@temporary" disable-output-escaping="yes" />
					</xsl:if>
				</xsl:variable>
				<xsl:if test="$dataCount>1">
					<br/>
				</xsl:if>
				<a>
					<xsl:attribute name="href">
						<xsl:value-of select="$fileUrl"></xsl:value-of>
					</xsl:attribute>
					<img src="/pe4j/img/b_attachment.png" />
					<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
				</a>
				<xsl:value-of select="@fileSize" disable-output-escaping="yes" />
			</xsl:when>
			<xsl:when test="../@type='56'">
				<xsl:variable name="imgUrl">
					<xsl:text  disable-output-escaping="yes">/pe4j/servlet/Download/</xsl:text>
					<xsl:value-of select="@fileName" disable-output-escaping="yes" />
					<xsl:text  disable-output-escaping="yes">?id=</xsl:text>
					<xsl:value-of select="@docId" disable-output-escaping="yes" />
					<xsl:if test="string-length(@docId)=0">
						<xsl:text disable-output-escaping="yes">&amp;temporary=</xsl:text>
						<xsl:value-of select="@temporary" disable-output-escaping="yes" />
					</xsl:if>
				</xsl:variable>
				<xsl:if test="$dataCount>1">
					<br/>
				</xsl:if>
				<img>
					<xsl:attribute name="style">
						<xsl:text>max-height: </xsl:text>
						<xsl:value-of select="@maxHeight" />
						<xsl:text>px;max-width: </xsl:text>
						<xsl:value-of select="@maxWidth" />
						<xsl:text>px;</xsl:text>
					</xsl:attribute>

					<xsl:attribute name="src">
						<xsl:value-of select="$imgUrl"></xsl:value-of>
					</xsl:attribute>
				</img>
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
			<xsl:when test="../@type='62'"><!-- Employees -->
				<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" /><br/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="./XDB_NAME" disable-output-escaping="yes" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Detail data label -->
	<xsl:template match="XDB_LABEL">
		<xsl:variable name="labelCount">
			<xsl:number count="XDB_LABEL" />
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="@type='21'"><!-- checkbox -->
				<xsl:if test="$labelCount>1">
					<br/>
				</xsl:if>
				<xsl:value-of select="." disable-output-escaping="yes" />
			</xsl:when>
			<xsl:when test="@type='53'">
				<xsl:if test="string-length(@ent)>0">
					<xsl:value-of select="@ent" disable-output-escaping="yes"/>
					<br/>
				</xsl:if>
				<xsl:if test="string-length(@pos)>0">
					<xsl:value-of select="@pos" disable-output-escaping="yes"/>
					<br/>
				</xsl:if>
				<xsl:if test="string-length(@per)>0">
					<xsl:value-of select="@per" disable-output-escaping="yes"/>
				</xsl:if>
			</xsl:when>
			<xsl:when test="@type='55'">
				<xsl:variable name="fileUrl">
					<xsl:text  disable-output-escaping="yes">/pe4j/servlet/Download/</xsl:text>
					<xsl:value-of select="@fileName" disable-output-escaping="yes" />
					<xsl:text  disable-output-escaping="yes">?id=</xsl:text>
					<xsl:value-of select="@docId" disable-output-escaping="yes" />
					<xsl:if test="string-length(@docId)=0">
						<xsl:text disable-output-escaping="yes">&amp;temporary=</xsl:text>
						<xsl:value-of select="@temporary" disable-output-escaping="yes" />
					</xsl:if>
				</xsl:variable>
				<xsl:if test="$labelCount>1">
					<br/>
				</xsl:if>
				<a>
					<xsl:attribute name="href">
						<xsl:value-of select="$fileUrl"></xsl:value-of>
					</xsl:attribute>
					<img src="/pe4j/img/b_attachment.png" />
					<xsl:value-of select="." disable-output-escaping="yes" />
				</a>
				<xsl:value-of select="@fileSize" disable-output-escaping="yes" />
			</xsl:when>
			<xsl:when test="@type='56'">
				<xsl:variable name="imgUrl">
					<xsl:text  disable-output-escaping="yes">/pe4j/servlet/Download/</xsl:text>
					<xsl:value-of select="@fileName" disable-output-escaping="yes" />
					<xsl:text  disable-output-escaping="yes">?id=</xsl:text>
					<xsl:value-of select="@docId" disable-output-escaping="yes" />
					<xsl:if test="string-length(@docId)=0">
						<xsl:text disable-output-escaping="yes">&amp;temporary=</xsl:text>
						<xsl:value-of select="@temporary" disable-output-escaping="yes" />
					</xsl:if>
				</xsl:variable>
				<xsl:if test="$labelCount>1">
					<br/>
				</xsl:if>
				<img>
					<xsl:attribute name="style">
						<xsl:text>max-height: </xsl:text>
						<xsl:value-of select="@maxHeight" />
						<xsl:text>px;max-width: </xsl:text>
						<xsl:value-of select="@maxWidth" />
						<xsl:text>px;</xsl:text>
					</xsl:attribute>

					<xsl:attribute name="src">
						<xsl:value-of select="$imgUrl"></xsl:value-of>
					</xsl:attribute>
				</img>
			</xsl:when>
			<xsl:when test="@type='57'"> <!-- url -->
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
			<xsl:when test="@type='62'">
				<xsl:value-of select="@empName" disable-output-escaping="yes" />
				<xsl:choose>
					<xsl:when test="@countOtherEmployee>0">
							<xsl:value-of select="@otherEmployee" disable-output-escaping="yes" />
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="." disable-output-escaping="yes" />
			</xsl:otherwise>
		</xsl:choose>
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
				<div class="contents margin">
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
							<td class="label">紙添付資料</td>
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
							<td class="label">紙添付資料</td>
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
							<td class="label">紙添付資料</td>
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
							<td class="label">紙添付資料</td>
							<td class="item">
								<xsl:value-of select="." disable-output-escaping="yes" />
							</td>
						</tr>
					</table>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="contents margin">
					<table class="detail">
						<tr>
							<td class="label">紙添付資料</td>
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
							<td class="label">掲載責任者</td>
							<td class="item">
								<xsl:value-of select="./CHARGEEMPID" disable-output-escaping="yes" />
							</td>
						</tr>
						 -->
						<tr>
							<td class="label">掲載責任者</td>
							<td class="item">
								<xsl:value-of select="./CHARGEEMPNAME" disable-output-escaping="yes" />
							</td>
						</tr>
						<tr>
							<td class="label">掲示期間</td>
							<td class="item">
								<xsl:value-of select="./DISPSTARTDATE" disable-output-escaping="yes" />
								～
								<xsl:value-of select="./DISPENDDATE" disable-output-escaping="yes" />
							</td>
						</tr>
						<tr>
							<td class="label">NaviView通知期間</td>
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
														<td class="label">通知期間</td>
														<td class="item">
															<xsl:value-of select="./NAVIBBSDISPSTARTDATE" disable-output-escaping="yes" />
															～
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
														<td class="label">通知期間</td>
														<td class="item">
															<xsl:value-of select="./NAVIEVENTDISPSTARTDATE" disable-output-escaping="yes" />
															～
															<xsl:value-of select="./NAVIEVENTDISPENDDATE" disable-output-escaping="yes" />
														</td>
													</tr>
													<tr>
														<td class="label">開催期間</td>
														<td class="item">
															<xsl:choose>
																<xsl:when test="./HOLDINGFROMDATE">
																	<xsl:value-of select="./HOLDINGFROMDATE" disable-output-escaping="yes" />
																	～
																	<xsl:value-of select="./HOLDINGTODATE" disable-output-escaping="yes" />
																</xsl:when>
															</xsl:choose>
														</td>
													</tr>
													<tr>
														<td class="label">開催場所</td>
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
							<td class="label">掲載掲示板</td>
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
							このブラウザはインラインフレームに対応していません
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
							このブラウザはインラインフレームに対応していません
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
							このブラウザはインラインフレームに対応していません
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
							このブラウザはインラインフレームに対応していません
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
						var ObjectA = "<a href='+link+'>申請ファイルをダウンロード</a>";
						function fncOnLoad() {
							elem.innerHTML = 'Office文書ワークフローは利用できません。<br/>' + ObjectA;
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
						var ObjectA = "<a href='+link+'>申請ファイルをダウンロード</a>";
						function fncOnLoad() {
							elem.innerHTML = 'Office文書ワークフローは利用できません。<br/>' + ObjectA;
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
						var ObjectA = "<a href='+link+'>申請ファイルをダウンロード</a>";
						function fncOnLoad() {
							elem.innerHTML = 'Office文書ワークフローは利用できません。<br/>' + ObjectA;
						}
						fncOnLoad();
					</script>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
