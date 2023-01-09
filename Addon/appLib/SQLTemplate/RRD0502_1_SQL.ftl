-- TMP dung de lay ra folderId
WITH RECURSIVE TMP AS ( 
  	SELECT
		F1.ID,
	    F1.PARENTFOLDERID,
	    F1.FOLDERNAME,
	    F1.AUTHORITYID,
	    F1.DISPORDER,
	    F1.CORPID,
	    ROW_NUMBER() OVER (ORDER BY DISPORDER) AS RN
  	FROM
    	RT2_APPLICATION_FOLDER F1
  	WHERE
    	1=1
    <#if param.corpId?has_content> 
        AND F1.CORPID = '${param.corpId}'
    </#if>  
    <#if param.displayFlag?has_content> 
	  	AND F1.DISPLAYFLAG = '1'
	</#if>
)
,
--TMPFOLDER dung de lay tat ca folder con cua folderId
TMPFOLDER AS (
  SELECT
    F2.ID,
    F2.PARENTFOLDERID,
    F2.FOLDERNAME,
    F2.AUTHORITYID,
    F2.DISPORDER,
    F2.CORPID,
    1 AS LEVEL,
    ARRAY [RN] AS SORTARRAY
  FROM
    TMP F2
  WHERE
    <#if param.folderId?has_content> 
    F2.ID = ${param.folderId}
    <#else>
    F2.PARENTFOLDERID IS NULL
    </#if>
  UNION
  SELECT
    F2.ID,
    F2.PARENTFOLDERID,
    F2.FOLDERNAME,
    F2.AUTHORITYID,
    F2.DISPORDER,
    F2.CORPID,
    P.LEVEL + 1 AS LEVEL,
    ARRAY_APPEND(P.SORTARRAY, F2.RN) AS SORTARRAY 
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
  F.CORPID,
  SORTARRAY 
FROM
  TMPFOLDER F
  INNER JOIN ZT2_AUTHORITY A
    ON A.ID = AUTHORITYID
ORDER BY
  F.CORPID,
  SORTARRAY
