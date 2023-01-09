--TEMP1 dung de lay path cua folder
WITH TEMP1 AS ( 
  WITH RECURSIVE TMP1 AS ( 
    SELECT
      P1.ID,
      P1.PARENTFOLDERID,
      ARRAY [ROW_NUMBER() OVER(ORDER BY P1.DISPORDER)] AS SORTARRAY,
      P1.FOLDERNAME ::TEXT 
    FROM
      RT2_APPLICATION_FOLDER P1 
    WHERE
      P1.PARENTFOLDERID IS NULL 
      AND P1.CORPID = '${param.corpId}' 
    UNION ALL 
    SELECT
      P2.ID,
      P2.PARENTFOLDERID,
      ARRAY_APPEND( 
        C.SORTARRAY,
        ROW_NUMBER() OVER (ORDER BY P2.DISPORDER)
      ) AS SORTARRAY,
      ( 
        CASE 
          WHEN C.FOLDERNAME = '' 
            THEN P2.FOLDERNAME 
          ELSE CONCAT(C.FOLDERNAME, '/', P2.FOLDERNAME) 
          END
      ) AS FOLDERNAME 
    FROM
      RT2_APPLICATION_FOLDER P2 
      INNER JOIN TMP1 C 
        ON P2.PARENTFOLDERID = C.ID
  ) 
  SELECT
    * 
  FROM
    TMP1
)

--TEMP2 dung de lay tat ca folder con cua folderId
,
TEMP2 AS ( 
  WITH RECURSIVE TMP2 AS ( 
    SELECT
      P3.ID,
      P3.PARENTFOLDERID,
      P3.AUTHORITYID,
      ARRAY [ROW_NUMBER() OVER(ORDER BY P3.DISPORDER)] AS SORTARRAY 
    FROM
      RT2_APPLICATION_FOLDER P3 
    WHERE
      P3.CORPID = '${param.corpId}'
      <#if param.folderId?has_content>
      AND P3.ID = ${param.folderId}
      <#else>
      AND P3.PARENTFOLDERID IS NULL
      </#if>
    UNION ALL
    SELECT
      P4.ID,
      P4.PARENTFOLDERID,
      P4.AUTHORITYID,
      ARRAY_APPEND( 
        C2.SORTARRAY,
        ROW_NUMBER() OVER (ORDER BY P4.DISPORDER)
      ) AS SORTARRAY 
    FROM
      RT2_APPLICATION_FOLDER P4 
      INNER JOIN TMP2 C2 
        ON P4.PARENTFOLDERID = C2.ID
  ) 
  SELECT
    ID,
    PARENTFOLDERID,
    AUTHORITYID,
    SORTARRAY 
  FROM
    TMP2
) 
SELECT DISTINCT
  D.ID,
  D.PARENTFOLDERID,
  TREE_FD.FOLDERNAME ::TEXT,
  D.AUTHORITYID,
  D.SORTARRAY 
FROM
  TEMP2 D 
  INNER JOIN TEMP1 TREE_FD 
    ON TREE_FD.ID = D.ID 
ORDER BY
  D.SORTARRAY
