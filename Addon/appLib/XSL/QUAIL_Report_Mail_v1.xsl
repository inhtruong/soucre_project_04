<?xml version="1.0" encoding="SHIFT_JIS" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="text" encoding="Shift_JIS" />
<!-- 
	【レポート機能メール本文用スタイル】
	1)TEXT変換用のため、空白はそのまま出力されます。
	  コードを整形した場合、出力結果のレイアウトも
	  変化する可能性があります。
	2)改行<BR/>は
	    <xsl:text>
	    </xsl:text>
	  に変換します。
-->
<!-- ルート -->
<xsl:template match="/"><xsl:apply-templates select="BAPDOCUMENT" /></xsl:template>
<!-- ボディー -->
<xsl:template match="BAPDOCUMENT">件名:
  <xsl:value-of select="./TITLE" />
<xsl:text>
</xsl:text>
報告日:
  <xsl:value-of select="./REPORTDATE" />
<xsl:if test="./CUSTOMERSETFLAG[text()!=0]">
<xsl:if test="./CUSTUSAGEFLAG[text()!=0]">
<xsl:text>
</xsl:text>
顧客名:
　<xsl:value-of select="./ENTERPRISENAME" />　<xsl:value-of select="./POSTNAME" />
</xsl:if>
<xsl:if test="./PERSONUSAGEFLAG[text()!=0]">
<xsl:text>
</xsl:text>
ご担当者:
　<xsl:value-of select="./PERSONNAME" />
</xsl:if>
<xsl:if test="./OTHERPERSONUSAGEFLAG[text()!=0]">
<xsl:text>
</xsl:text>
その他のご担当者:
　<xsl:value-of select="./OTHERPERSONNAME" />
</xsl:if>
</xsl:if>
<xsl:if test="./SFASETFLAG[text()!=0]">
<xsl:if test="./COMMODITYUSAGEFLAG[text()!=0]">
<xsl:text>
</xsl:text>
対象商品:
　<xsl:value-of select="./COMMODITYNAME" />
</xsl:if>
</xsl:if>

<xsl:if test="./INPUTDISPLAYFORMATID[text()!=0]">
<xsl:apply-templates select="./RFM_USERFORMAT" />
</xsl:if>

公開範囲：
　<xsl:value-of select="./AUTHORITYNAME" />
</xsl:template>


<!-- ───────────────
     以下「入力フォーマット用定義」
──────────────── -->
<xsl:template match="RFM_USERFORMAT">
<xsl:apply-templates />

</xsl:template>
<!--  項目情報のテンプレート  -->
<xsl:template match="RFM_ITEM">
    <!--  先頭項目以外で、改行フラグがyesの場合  -->
    <xsl:if test="current() != ../RFM_ITEM[1] and @nl = 'yes'">
         
    </xsl:if>
    <!--  主要タグ生成  -->
    <xsl:choose>
        <!--  テキストエリアの場合（TABLEタグのネストで構成）  -->
        <xsl:when test="@type='11'">
            <xsl:choose>
                <!--  前リテラル後改行がyesの場合  -->
                <xsl:when test="@flnl='yes'">
                    <xsl:apply-templates select="./RFM_FL" />
                    <xsl:apply-templates select="./RFM_DATA">
                        <xsl:with-param name="dtype" select="@type"></xsl:with-param>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./RFM_BL" />
                </xsl:when>
                <!--  前リテラル後改行がnoの場合  -->
                <xsl:otherwise>
                    <xsl:apply-templates select="./RFM_FL" />
                    <xsl:apply-templates select="./RFM_DATA">
                        <xsl:with-param name="dtype" select="@type"></xsl:with-param>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./RFM_BL" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:when>

        <!--  テキストエリア以外の場合  -->
        <xsl:otherwise>
            <xsl:choose>

                <!--  改行フラグが"Yes"、前リテラル後改行が"Yes"の場合  -->
                <xsl:when test="@nl='yes' and @flnl='yes'">
                    <xsl:apply-templates select="./RFM_FL" />
                     <xsl:apply-templates select="./RFM_DATA">
                        <xsl:with-param name="dtype" select="@type"></xsl:with-param>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./RFM_BL" />
                </xsl:when>

                <!--  改行フラグが"Yes"、前リテラル後改行が"No"の場合  -->
                <xsl:when test="@nl='yes' and @flnl='no'">
                    <xsl:apply-templates select="./RFM_FL" />
                    <xsl:apply-templates select="./RFM_DATA">
                        <xsl:with-param name="dtype" select="@type"></xsl:with-param>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./RFM_BL" />
                </xsl:when>

                <!--  改行フラグが"No"の場合  -->
                <xsl:otherwise>
                    <xsl:apply-templates select="./RFM_FL" /><xsl:apply-templates select="./RFM_DATA">
                        <xsl:with-param name="dtype" select="@type"></xsl:with-param>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./RFM_BL" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<!--  項目値のテンプレート  -->
<xsl:template match="RFM_DATA">

    <!--  RFM_ITEMのtype属性値を、パラメタで受け取る  -->
    <xsl:param name="dtype" />

    <!--  コード出力と名称出力を、明細形式で振分  -->
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

<!--  コード値のテンプレート  -->
<xsl:template match="RFM_CODE">
    <!--  RFM_ITEMのtype属性値を、パラメタで受け取る  -->
    <xsl:param name="dtype2" />

    <xsl:choose>
        <!-- テキストエリア場合 改行して表示-->
        <xsl:when test="$dtype2='11' and not(string-length(./text())=0)">
<xsl:text>
</xsl:text>
        </xsl:when>
        <!-- テキストエリア以外の場合 改行後、先頭１文字を桁下して表示 -->
        <xsl:when test="$dtype2!='11' and not(string-length(./text())=0)">
<xsl:text>
　</xsl:text>
        </xsl:when>
    </xsl:choose>

    <xsl:choose>
        <xsl:when test="@edit='yes' and not(string-length(./text())=0)">
            <!--  コード値の編集  -->
            <xsl:choose>
                <!--  文字テキストボックス ＵＲＬ編集  -->
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

<!--  前リテラルのテンプレート  -->
<xsl:template match="RFM_FL"><xsl:value-of select="." disable-output-escaping="yes" />:</xsl:template>

<!--  後リテラルのテンプレート  -->
<xsl:template match="RFM_BL">
    <xsl:choose>
        <xsl:when test="not(string-length(./text())=0)">
            <xsl:value-of select="." disable-output-escaping="yes" />
        </xsl:when>
        <xsl:otherwise>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<!-- 選択肢の名称値テンプレート -->
<!-- 選択肢の名称値は「改行＋桁下げ」で表示 -->
<xsl:template match="RFM_NAME">
    <xsl:choose>
        <xsl:when test="not(string-length(./text())=0)">
　<xsl:apply-templates />
        </xsl:when>
    </xsl:choose>
</xsl:template>

<!-- BRタグ対応 -->
<xsl:template match="BR">
<xsl:text>
</xsl:text>
</xsl:template>


</xsl:stylesheet>
