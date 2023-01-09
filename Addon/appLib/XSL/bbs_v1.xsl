<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" omit-xml-declaration="yes" encoding="utf-8" />
<!-- ** ルート要素（掲示板・フォーラム・通達示達）のテンプレート ** -->
<xsl:template match="/">
<div class="contents margin">
<table class="detail">
<tr>
	<td class="label" >No.</td>
	<td class="item">
		<xsl:value-of select="xmlDocument/CONTRIBUTENO"/>
	</td>
</tr>
<tr>
	<td class="label">件名</td>
	<td class="item">
		<xsl:value-of select="xmlDocument/SUBJECT"/>
	</td>
</tr>
<tr>
	<td class="label" >
		<xsl:value-of select="xmlDocument/DATENAME"/>
	</td>
	<td class="item">
		<xsl:value-of select="xmlDocument/CONTRIBUTEDATE"/><xsl:text> </xsl:text>
		<xsl:value-of select="xmlDocument/CONTRIBUTETIME"/>
	</td>
</tr>
<tr>
	<td class="label">掲載者</td>
	<td class="item">
		<xsl:choose>
			<!-- 掲載者IDエレメントが空でないとき(V1.53以降) -->
			<xsl:when test="xmlDocument/CONTRIBUTORID[text()!='']">
				<xsl:element name="a">
					<xsl:attribute name="href">
						<xsl:text>javascript:fncDisplayEmpData(</xsl:text>
						<xsl:value-of select="xmlDocument/CONTRIBUTORID"/>
						<xsl:text>)</xsl:text>	
						</xsl:attribute>
					<xsl:element name="img">
						<xsl:attribute name="src">
							<xsl:text>../image/boy.gif</xsl:text>
						</xsl:attribute>
						<xsl:attribute name="border">
							<xsl:text>0</xsl:text>
						</xsl:attribute>
						<xsl:attribute name="vspace">
							<xsl:text>0</xsl:text>
						</xsl:attribute>
						<xsl:attribute name="hspace">
							<xsl:text>0</xsl:text>
						</xsl:attribute>
						
					</xsl:element>
				</xsl:element>
				<xsl:choose>
					<!-- メールアドレスが空でなければリンクを生成 -->
					<xsl:when test="xmlDocument/CONTRIBUTORMAIL[text()!='']">
						<xsl:element name="a">
							<xsl:attribute name="href">
								<xsl:text>mailto:</xsl:text>
								<xsl:value-of select="xmlDocument/CONTRIBUTORMAIL"/>
							</xsl:attribute>
							<xsl:value-of select="xmlDocument/CONTRIBUTORNAME"/>
						</xsl:element>
					</xsl:when>
					<!-- メールアドレスが空ならリンクなし -->
					<xsl:otherwise>
						<xsl:value-of select="xmlDocument/CONTRIBUTORNAME"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<!-- 掲載者IDエレメントが空のとき(V1.5)、名前のみ -->
			<xsl:otherwise>
				<xsl:value-of select="xmlDocument/CONTRIBUTORNAME"/>
			</xsl:otherwise>
		</xsl:choose>
	</td>
</tr>
<!-- 掲載責任者IDエレメントが空でないとき(V1.53以降) -->
<xsl:if test="xmlDocument/CHARGERID[text()!='']">
	<tr>
		<td class="label"><xsl:value-of select="xmlDocument/CHARGEREMPNAME"/></td>
		<td class="item">
			<xsl:element name="a">
				<xsl:attribute name="href">
					<xsl:text>javascript:fncDisplayEmpData(</xsl:text>
					<xsl:value-of select="xmlDocument/CHARGERID"/>
					<xsl:text>)</xsl:text>	
				</xsl:attribute>
				<xsl:element name="img">
					<xsl:attribute name="src">
						<xsl:text>../image/boy.gif</xsl:text>
					</xsl:attribute>
					<xsl:attribute name="border">
						<xsl:text>0</xsl:text>
					</xsl:attribute>
					<xsl:attribute name="vspace">
						<xsl:text>0</xsl:text>
					</xsl:attribute>
					<xsl:attribute name="hspace">
						<xsl:text>0</xsl:text>
					</xsl:attribute>
				</xsl:element>
			</xsl:element>
			<xsl:choose>
				<xsl:when test="xmlDocument/CHARGERMAIL[text()!='']">
					<xsl:element name="a">
						<xsl:attribute name="href">
							<xsl:text>mailto:</xsl:text>
							<xsl:value-of select="xmlDocument/CHARGERMAIL"/>
						</xsl:attribute>
						<xsl:value-of select="xmlDocument/CHARGERNAME2"/>
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="xmlDocument/CHARGERNAME2"/>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</tr>
</xsl:if>
<xsl:if test="xmlDocument/CHARGERNAME[text()!='']">
	<tr>
		<td class="label"><xsl:value-of select="xmlDocument/CHARGEREMPNAME"/></td>
		<td class="item"><xsl:value-of select="xmlDocument/CHARGERNAME"/></td>
	</tr>
</xsl:if>
<tr>
	<td class="label">内容</td>
	<td class="item">
		<xsl:apply-templates select="xmlDocument/CONTENT"/>
	</td>
</tr>

<!-- V1.6 2003.08.14 開催期間・開催場所対応 Start -->
<xsl:apply-templates select="xmlDocument/HOLDINGFROMDATE"/>
<xsl:apply-templates select="xmlDocument/HOLDINGPLACE"/>
<!-- V1.6 2003.08.14 開催期間・開催場所対応 End -->

<xsl:apply-templates select="xmlDocument/ATTACHMENT|xmlDocument/NOATTACHMENT"/>

<xsl:apply-templates select="xmlDocument/RELATION|xmlDocument/NORELATION"/>
<xsl:apply-templates select="xmlDocument/NOTICE-TERM"/>

</table>
</div>
</xsl:template>

<!-- ** 内容のテンプレート ** -->
<xsl:template match="CONTENT">
<pre>
<xsl:value-of select="."/>
</pre>
</xsl:template>
<xsl:template match="BR|text()">
<xsl:copy />
</xsl:template>

<!-- ** 添付ファイルのテンプレート ** -->
<xsl:template match="NOATTACHMENT">
	<tr>
		<td class="label" >添付ファイル</td>
		<td class="item">
			<xsl:value-of select="."/>
		</td>
	</tr>
</xsl:template>

<xsl:template match="ATTACHMENT">
	<xsl:for-each select="TABLECONTENT">
		<tr>
			<xsl:if test="position() = 1">
				<td class="label" >添付ファイル</td>
			</xsl:if>
			<xsl:if test="not(position() = 1)">
				<td class="label"><br/></td>
			</xsl:if>
			<td class="item">
				<xsl:apply-templates select="FILENAME"/>
			</td>
		</tr>
	</xsl:for-each>
</xsl:template>

<xsl:template match="FILENAME">
	<xsl:element name="a">
		<xsl:choose>
		<xsl:when test="../DOWNLOADNAME != ''">
		<xsl:attribute name="href">../servlet/Download/<xsl:value-of select="../DOWNLOADNAME"/>?DocID=<xsl:value-of select="../DOCID"/></xsl:attribute>
		</xsl:when>
		<xsl:otherwise>
			<xsl:attribute name="href">../servlet/Download/<xsl:value-of select="."/>?DocID=<xsl:value-of select="../DOCID"/>
		</xsl:attribute>
		</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="."/>(<xsl:value-of select="../BYTES"/>)
	</xsl:element>
</xsl:template>

<xsl:template match="RELATION">
	<xsl:for-each select="RELATIONURL">
		<tr>
			<xsl:if test="position() = 1">
				<td class="label" >関連ＵＲＬ</td>
			</xsl:if>
			<xsl:if test="not(position() = 1)">
				<td class="label"><br/></td>
			</xsl:if>
			<td class="item">
				<xsl:element name="a">
					<xsl:attribute name="href">
						<xsl:value-of select="URL"/>
					</xsl:attribute>
					<xsl:attribute name="target">
						<xsl:text>_blank</xsl:text>
					</xsl:attribute>
					<xsl:value-of select="URL"/>
				</xsl:element>
			</td>
		</tr>
	</xsl:for-each>	
</xsl:template>
<xsl:template match="NORELATION">
	<tr>
		<td class="label" >関連ＵＲＬ</td>
		<td class="item">
			<xsl:value-of select="."/>
		</td>
	</tr>
</xsl:template>

<!-- V1.6 2003.08.14 開催期間・開催場所対応 Start -->
<xsl:template match="HOLDINGFROMDATE">
	<tr>
		<td class="label" >開催期間</td>
		<td class="item">
			<xsl:variable name="fromdate" select="."/>
			<xsl:variable name="todate" select="../HOLDINGTODATE"/>
			<xsl:value-of select="substring($fromdate,1,4)"/>
			<xsl:text>/</xsl:text>
			<xsl:value-of select="substring($fromdate,5,2)"/>
			<xsl:text>/</xsl:text>
			<xsl:value-of select="substring($fromdate,7,2)"/>
			<xsl:text>～</xsl:text>
			<xsl:value-of select="substring($todate,1,4)"/>
			<xsl:text>/</xsl:text>
			<xsl:value-of select="substring($todate,5,2)"/>
			<xsl:text>/</xsl:text>
			<xsl:value-of select="substring($todate,7,2)"/>
		</td>
	</tr>
</xsl:template>

<xsl:template match="HOLDINGPLACE">
	<tr>
		<td class="label" >開催場所</td>
		<td class="item">
			<xsl:value-of select="."/>
		</td>
	</tr>
</xsl:template>
<!-- V1.6 2003.08.14 開催期間・開催場所対応 End -->
<xsl:template match="NOTICE-TERM">
	<tr>
		<td class="label" >掲示期間</td>
		<td class="item">
			<xsl:value-of select="FROM"/> ～ <xsl:value-of select="TO"/>
		</td>
	</tr>
</xsl:template>

</xsl:stylesheet>
