-- 1. TEMP1 la dung de lay ten forum path 
WITH TEMP1 AS 
(
   WITH RECURSIVE FORUM AS 
   (
      SELECT
         FM.ID,
         FM.NODETYPE,
         FM.FORUMNAME,
         ARRAY[ROW_NUMBER() OVER( 
      ORDER BY
         FM.SORTKEY)] AS SORTARRAY,
         ARRAY[CAST(FORUMNAME AS TEXT)] AS PATH_KEY,
         '' AS PATHLV 
      FROM
         KT2_FORUM FM 
      WHERE
         FM.UPPERFORUMID IS NULL 
      UNION
      SELECT
         FRM.ID,
         FRM.NODETYPE,
         FRM.FORUMNAME,
         ARRAY_APPEND(UPPERFRM.SORTARRAY, ROW_NUMBER() OVER(
      ORDER BY
         FRM.SORTKEY)),
         ARRAY_APPEND(UPPERFRM.PATH_KEY, CAST(FRM.FORUMNAME AS TEXT)) PATH_KEY,
         ARRAY_TO_STRING(UPPERFRM.PATH_KEY, '/') AS PATHLV 
      FROM
         KT2_FORUM FRM,
         FORUM UPPERFRM 
      WHERE
         FRM.UPPERFORUMID = UPPERFRM.ID 
   )
   SELECT
      FRUM.* 
   FROM
      FORUM FRUM 
)
-- 2. TEMP2 LA DUNG DE LAY DATA KT2_FORUM_MSG	
,
TEMP2 AS 
(
   SELECT      
      MSG.ID, MSG.FORUMID
   FROM
      TEMP1 FR1 
      INNER JOIN
         KT2_FORUM_MSG MSG 
         ON FR1.ID = MSG.FORUMID 
   WHERE
      MSG.FORUMID IS NOT NULL 
	  <#if param.forumId?has_content > 
      AND MSG.FORUMID IN 
      (
         WITH RECURSIVE TMP AS 
         (
            SELECT
               KF.ID 
            FROM
               KT2_FORUM KF 
            WHERE
               KF.ID = ${param.forumId} 
            UNION
            SELECT
               KF.ID 
            FROM
               KT2_FORUM KF,
               TMP UPPERKF 
            WHERE
               KF.UPPERFORUMID = UPPERKF.ID 
         )
         SELECT
            * 
         FROM
            TMP 
      )
      </#if> 
      AND MSG.LASTUPDATE <= TO_TIMESTAMP('${param.date} 23:59:59.999', 'YYYYMMDD HH24:MI:SSXXX') 
      AND NOT EXISTS 
      (
         SELECT
            FMS.id 
         FROM
            KT2_FORUM_MSG FMS 
         WHERE
            FMS.LASTUPDATE > to_timestamp('${param.date} 23:59:59.999', 'YYYYMMDD HH24:MI:SSXXX') 
            AND 
            (
               FMS.TOPMESSAGEID = MSG.TOPMESSAGEID 
               OR FMS.TOPMESSAGEID = MSG.ID
            )
      )
)
-- 3. THUC HIEN TINH SUM ,VA COUNT DU LIEU TREN THONG TIN
SELECT
   T.FORUMID,
   TMP1.FORUMNAME,
   COALESCE(CEIL(SUM(BDATA.FILESIZE) * ${param.attachFactor} ), 0) as FILESIZE,
   COUNT(DISTINCT T.ID),
   TMP1.PATHLV,
   TMP1.SORTARRAY 
FROM
   TEMP2 T    
   INNER JOIN
      TEMP1 TMP1 
      ON T.FORUMID = TMP1.ID 
   LEFT JOIN
      KT2_FORUM_MSG_DOC_CONN CON 
      ON CON.MESSAGEID = T.ID 
   LEFT JOIN
      QT2_BINARY_DATA BDATA 
      ON BDATA.DOCID = CON.DOCID 
WHERE
   TMP1.NODETYPE = '0' 
GROUP BY
   FORUMID,
   FORUMNAME,
   TMP1.PATHLV,
   TMP1.SORTARRAY 