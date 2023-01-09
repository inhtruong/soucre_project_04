<?xml version="1.0" encoding="SHIFT_JIS" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="text" encoding="Shift_JIS" />
<!-- 
	�y���|�[�g�@�\���[���{���p�X�^�C���z
	1)TEXT�ϊ��p�̂��߁A�󔒂͂��̂܂܏o�͂���܂��B
	  �R�[�h�𐮌`�����ꍇ�A�o�͌��ʂ̃��C�A�E�g��
	  �ω�����\��������܂��B
	2)���s<BR/>��
	    <xsl:text>
	    </xsl:text>
	  �ɕϊ����܂��B
-->
<!-- ���[�g -->
<xsl:template match="/"><xsl:apply-templates select="BAPDOCUMENT" /></xsl:template>
<!-- �{�f�B�[ -->
<xsl:template match="BAPDOCUMENT">����:
  <xsl:value-of select="./TITLE" />
<xsl:text>
</xsl:text>
�񍐓�:
  <xsl:value-of select="./REPORTDATE" />
<xsl:if test="./CUSTOMERSETFLAG[text()!=0]">
<xsl:if test="./CUSTUSAGEFLAG[text()!=0]">
<xsl:text>
</xsl:text>
�ڋq��:
�@<xsl:value-of select="./ENTERPRISENAME" />�@<xsl:value-of select="./POSTNAME" />
</xsl:if>
<xsl:if test="./PERSONUSAGEFLAG[text()!=0]">
<xsl:text>
</xsl:text>
���S����:
�@<xsl:value-of select="./PERSONNAME" />
</xsl:if>
<xsl:if test="./OTHERPERSONUSAGEFLAG[text()!=0]">
<xsl:text>
</xsl:text>
���̑��̂��S����:
�@<xsl:value-of select="./OTHERPERSONNAME" />
</xsl:if>
</xsl:if>
<xsl:if test="./SFASETFLAG[text()!=0]">
<xsl:if test="./COMMODITYUSAGEFLAG[text()!=0]">
<xsl:text>
</xsl:text>
�Ώۏ��i:
�@<xsl:value-of select="./COMMODITYNAME" />
</xsl:if>
</xsl:if>

<xsl:if test="./INPUTDISPLAYFORMATID[text()!=0]">
<xsl:apply-templates select="./RFM_USERFORMAT" />
</xsl:if>

���J�͈́F
�@<xsl:value-of select="./AUTHORITYNAME" />
</xsl:template>


<!-- ������������������������������
     �ȉ��u���̓t�H�[�}�b�g�p��`�v
�������������������������������� -->
<xsl:template match="RFM_USERFORMAT">
<xsl:apply-templates />

</xsl:template>
<!--  ���ڏ��̃e���v���[�g  -->
<xsl:template match="RFM_ITEM">
    <!--  �擪���ڈȊO�ŁA���s�t���O��yes�̏ꍇ  -->
    <xsl:if test="current() != ../RFM_ITEM[1] and @nl = 'yes'">
         
    </xsl:if>
    <!--  ��v�^�O����  -->
    <xsl:choose>
        <!--  �e�L�X�g�G���A�̏ꍇ�iTABLE�^�O�̃l�X�g�ō\���j  -->
        <xsl:when test="@type='11'">
            <xsl:choose>
                <!--  �O���e��������s��yes�̏ꍇ  -->
                <xsl:when test="@flnl='yes'">
                    <xsl:apply-templates select="./RFM_FL" />
                    <xsl:apply-templates select="./RFM_DATA">
                        <xsl:with-param name="dtype" select="@type"></xsl:with-param>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./RFM_BL" />
                </xsl:when>
                <!--  �O���e��������s��no�̏ꍇ  -->
                <xsl:otherwise>
                    <xsl:apply-templates select="./RFM_FL" />
                    <xsl:apply-templates select="./RFM_DATA">
                        <xsl:with-param name="dtype" select="@type"></xsl:with-param>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./RFM_BL" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:when>

        <!--  �e�L�X�g�G���A�ȊO�̏ꍇ  -->
        <xsl:otherwise>
            <xsl:choose>

                <!--  ���s�t���O��"Yes"�A�O���e��������s��"Yes"�̏ꍇ  -->
                <xsl:when test="@nl='yes' and @flnl='yes'">
                    <xsl:apply-templates select="./RFM_FL" />
                     <xsl:apply-templates select="./RFM_DATA">
                        <xsl:with-param name="dtype" select="@type"></xsl:with-param>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./RFM_BL" />
                </xsl:when>

                <!--  ���s�t���O��"Yes"�A�O���e��������s��"No"�̏ꍇ  -->
                <xsl:when test="@nl='yes' and @flnl='no'">
                    <xsl:apply-templates select="./RFM_FL" />
                    <xsl:apply-templates select="./RFM_DATA">
                        <xsl:with-param name="dtype" select="@type"></xsl:with-param>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="./RFM_BL" />
                </xsl:when>

                <!--  ���s�t���O��"No"�̏ꍇ  -->
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

<!--  ���ڒl�̃e���v���[�g  -->
<xsl:template match="RFM_DATA">

    <!--  RFM_ITEM��type�����l���A�p�����^�Ŏ󂯎��  -->
    <xsl:param name="dtype" />

    <!--  �R�[�h�o�͂Ɩ��̏o�͂��A���׌`���ŐU��  -->
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

<!--  �R�[�h�l�̃e���v���[�g  -->
<xsl:template match="RFM_CODE">
    <!--  RFM_ITEM��type�����l���A�p�����^�Ŏ󂯎��  -->
    <xsl:param name="dtype2" />

    <xsl:choose>
        <!-- �e�L�X�g�G���A�ꍇ ���s���ĕ\��-->
        <xsl:when test="$dtype2='11' and not(string-length(./text())=0)">
<xsl:text>
</xsl:text>
        </xsl:when>
        <!-- �e�L�X�g�G���A�ȊO�̏ꍇ ���s��A�擪�P�������������ĕ\�� -->
        <xsl:when test="$dtype2!='11' and not(string-length(./text())=0)">
<xsl:text>
�@</xsl:text>
        </xsl:when>
    </xsl:choose>

    <xsl:choose>
        <xsl:when test="@edit='yes' and not(string-length(./text())=0)">
            <!--  �R�[�h�l�̕ҏW  -->
            <xsl:choose>
                <!--  �����e�L�X�g�{�b�N�X �t�q�k�ҏW  -->
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

<!--  �O���e�����̃e���v���[�g  -->
<xsl:template match="RFM_FL"><xsl:value-of select="." disable-output-escaping="yes" />:</xsl:template>

<!--  �ナ�e�����̃e���v���[�g  -->
<xsl:template match="RFM_BL">
    <xsl:choose>
        <xsl:when test="not(string-length(./text())=0)">
            <xsl:value-of select="." disable-output-escaping="yes" />
        </xsl:when>
        <xsl:otherwise>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<!-- �I�����̖��̒l�e���v���[�g -->
<!-- �I�����̖��̒l�́u���s�{�������v�ŕ\�� -->
<xsl:template match="RFM_NAME">
    <xsl:choose>
        <xsl:when test="not(string-length(./text())=0)">
�@<xsl:apply-templates />
        </xsl:when>
    </xsl:choose>
</xsl:template>

<!-- BR�^�O�Ή� -->
<xsl:template match="BR">
<xsl:text>
</xsl:text>
</xsl:template>


</xsl:stylesheet>
