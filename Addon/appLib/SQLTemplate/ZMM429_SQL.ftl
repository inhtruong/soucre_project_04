<#setting number_format="computer">

--1. TEMP1 dung de lay path 
WITH TEMP1 AS 
(
   WITH RECURSIVE TMP1 AS 
   (
      SELECT
         ID,
         PARENTFOLDERID,
         ARRAY[ROW_NUMBER() OVER(ORDER BY DISPORDER)] AS SORTARRAY,
         (
            CASE
               WHEN
                  PARENTFOLDERID IS NULL 
               THEN
                  '' 
               ELSE
                  FOLDERNAME 
            END
         )
         AS FOLDERNAME, SYSTEMCLASS , UNITID 
      FROM
         XT2_FOLDER 
      WHERE
         PARENTFOLDERID IS NULL 
         AND SYSTEMCLASS = 2 
         AND UNITID = ${param.unitId}
      UNION
      SELECT
         P.ID,
         P.PARENTFOLDERID,
         ARRAY_APPEND(C.SORTARRAY, ROW_NUMBER() OVER(ORDER BY P.DISPORDER)),
         (
            CASE
               WHEN
                  C.FOLDERNAME = '' 
               THEN
                  P.FOLDERNAME 
               ELSE
                  CONCAT(C.FOLDERNAME , '/' , P.FOLDERNAME) 
            END
         ) AS FOLDERNAME, P.SYSTEMCLASS, C.UNITID 
      FROM
         XT2_FOLDER P 
         INNER JOIN TMP1 C ON P.PARENTFOLDERID = C.ID 
      WHERE P.SYSTEMCLASS = 2
      AND P.UNITID = ${param.unitId}
   )
   SELECT
      *       
   FROM
      TMP1
   
)
--2. TEMP2 dung de lay tat ca folder con cua selectFolderId
,
TEMP2 AS
(
   WITH RECURSIVE TMP4 AS 
   (
      SELECT
         ID,
         PARENTFOLDERID,         
         SYSTEMCLASS,
         UNITID    
      FROM
           XT2_FOLDER 
      WHERE
	  SYSTEMCLASS = 2              
	  AND UNITID = ${param.unitId}                   
	  <#if param.folderId?has_content > 
	  AND ID = ${param.folderId} 
	  </#if> 
      UNION ALL
      SELECT
         P.ID,
         P.PARENTFOLDERID,
         P.SYSTEMCLASS,
         C.UNITID 
      FROM
         XT2_FOLDER P 
         INNER JOIN TMP4 C ON P.PARENTFOLDERID = C.ID 
         
      WHERE P.SYSTEMCLASS = 2
	  AND P.UNITID = ${param.unitId}    
   )
   SELECT
      TMP4.* , U.UNITNAME 
   FROM
      TMP4 INNER JOIN CT2_UNIT U 
      				ON U.UNITID = TMP4.UNITID
   
)
-- 3.TEMP3 : lay DBTABLENAME data from XT2_DATABASE_DEF, XT2_SUBTABLE_DEF ma co attachFile/image
,
TEMP3 AS
(
   SELECT
      T.ID,
      T.DBTABLENAME || '_' || F.ID AS TABLENAME,
      T.DATABASENAME,
      TREE_FD.FOLDERNAME
   FROM
      TEMP2 D 
      INNER JOIN TEMP1 TREE_FD 
      				ON TREE_FD.ID = D.ID 
      INNER JOIN XT2_DATABASE_DEF T 
  				ON D.ID = T.FOLDERID 
      INNER JOIN XT2_FIELD_DEF F 
      				ON T.ID = F.DATABASEID 
   WHERE
      D.UNITID = ${param.unitId}
      AND D.SYSTEMCLASS = 2 
      AND F.FIELDTYPE IN (55, 56)
   UNION
   SELECT
      T.ID,
      'XT2_' || TO_CHAR(SUB.ID, 'FM00000000000') || '_' || F.ID AS TABLENAME,
      T.DATABASENAME,
      TREE_FD.FOLDERNAME
   FROM
      TEMP2 D 
      INNER JOIN TEMP1 TREE_FD 
      			ON TREE_FD.ID = D.ID 
      INNER JOIN XT2_DATABASE_DEF T 
      			ON D.ID = T.FOLDERID 
      INNER JOIN XT2_SUBTABLE_DEF SUB 
      			ON T.ID = SUB.MAINDATABASEID 
      INNER JOIN XT2_FIELD_DEF F 
      			ON SUB.ID = F.DATABASEID 
   WHERE
      D.UNITID = ${param.unitId} 
      AND D.SYSTEMCLASS = 2 
      AND F.FIELDTYPE IN (55, 56)
)
SELECT DISTINCT
   R2.ID,
   R1.TABLENAME,
   R2.DATABASENAME,
   TREE_FD.FOLDERNAME,
   D.UNITNAME,
   TREE_FD.SORTARRAY,
   R2.DISPORDER 
FROM
   TEMP2 D 
   INNER JOIN XT2_DATABASE_DEF R2 
   			ON D.ID = R2.FOLDERID
   INNER JOIN TEMP1 TREE_FD 
   			ON TREE_FD.ID = D.ID 
   LEFT JOIN TEMP3 R1 ON R2.ID = R1.ID 

WHERE D.SYSTEMCLASS = 2 
      AND D.UNITID = ${param.unitId}    
     