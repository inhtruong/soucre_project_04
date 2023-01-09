WITH RECURSIVE TMP AS ( 
  SELECT
    F1.ID,
    F1.PARENTFOLDERID,
    F1.FOLDERNAME,
    F1.AUTHORITYID,
    F1.DISPORDER,
    ROW_NUMBER() OVER (ORDER BY DISPORDER) AS RN,
    F1.HIGHRANKCODE,
    F1.APPRANKID
  FROM
    RT2_APPLICATION_FOLDER F1
  WHERE
    F1.CORPID = '${param.corpId}'
    AND F1.DISPLAYFLAG = '1'
)
,
TMPFOLDER AS (
  SELECT
    F2.ID,
    F2.PARENTFOLDERID,
    F2.FOLDERNAME,
    F2.AUTHORITYID,
    F2.DISPORDER,
    1 AS LEVEL,
    ARRAY [RN] AS SORTARRAY,
    F2.HIGHRANKCODE,
    F2.APPRANKID
  FROM
    TMP F2
  WHERE
    1=1
    <#if param.urlFlag>
        <#if param.appFolderId?has_content>
        AND F2.ID = ${param.appFolderId}
        </#if>
        <#if param.highRankCode?has_content>
        AND F2.PARENTFOLDERID IS NULL --highRankCode chi co 1 cap folder nen can PARENTFOLDERID de tranh trung lap
        AND F2.HIGHRANKCODE IN (${param.highRankCode})
        </#if>
        <#if param.appRankId?has_content>
        AND F2.APPRANKID = ${param.appRankId} --appRankId luon co folder cha
        </#if>
    <#else>
        <#if param.appFolderId?has_content>
        AND F2.ID = ${param.appFolderId}
        <#else>
        AND F2.PARENTFOLDERID IS NULL
        </#if>
    </#if>
  UNION
  SELECT
    F2.ID,
    F2.PARENTFOLDERID,
    F2.FOLDERNAME,
    F2.AUTHORITYID,
    F2.DISPORDER,
    P.LEVEL + 1 AS LEVEL,
    ARRAY_APPEND(P.SORTARRAY, F2.RN) AS SORTARRAY ,
    F2.HIGHRANKCODE,
    F2.APPRANKID
  FROM
    TMP F2,
    TMPFOLDER P
  WHERE
    P.ID = F2.PARENTFOLDERID
) 
SELECT DISTINCT
  F.ID,
  F.PARENTFOLDERID,
  F.FOLDERNAME,
  F.AUTHORITYID,
  LPAD('・', LEVEL - 1, '・') || F.FOLDERNAME AS FOLDERNAMELEVEL,
  LEVEL,
  F.DISPORDER,
  SORTARRAY,
  F.HIGHRANKCODE,
  F.APPRANKID
FROM
  TMPFOLDER F
  INNER JOIN ZT2_AUTHORITY A
    ON A.ID = AUTHORITYID
ORDER BY
  SORTARRAY