WITH
	<#if param.bbsId?has_content>
	RECURSIVE_BBS AS (WITH RECURSIVE TMP AS
			(SELECT KBS.ID, KBS.LEVELFLAG
			FROM KT2_BBS KBS 
			WHERE KBS.ID = ${param.bbsId}
			
			UNION 
			
			SELECT KBS.ID, KBS.LEVELFLAG 
			FROM KT2_BBS KBS, TMP UPPERBBS 
			WHERE KBS.UPPERBBSID = UPPERBBS.ID ) 
		
		SELECT TMP.* FROM TMP WHERE TMP.LEVELFLAG = '0' 
	),			
	</#if>	
	 
	TEMP1 AS ( 
		SELECT 
			KBS.ID, KBS.UPPERBBSID, KBS.BBSNAME,
			COUNT(DISTINCT KBS.CONTENTID) AS COUNTGROUPBY,
			CEIL(COALESCE(SUM(KBS.FILESIZE), 0)* ${param.attachFactor} ) AS FILESIZE 
		FROM (
	
			SELECT KBS.ID, KBS.BBSNAME, KBS.UPPERBBSID, QBD.FILESIZE, KBC.ID AS CONTENTID 
				FROM KT2_BBS KBS
					INNER JOIN KT2_BBS_CONTENT KBC ON KBS.ID = KBC.BBSID
					LEFT JOIN KT2_BBS_DOC_CONN KBD ON KBC.ID = KBD.BBSCONTENTID
					LEFT JOIN QT2_BINARY_DATA QBD ON KBD.DOCID = QBD.DOCID
					<#if param.bbsId?has_content>
					INNER JOIN RECURSIVE_BBS RBBS ON RBBS.ID = KBS.ID
					</#if>	
				WHERE KBC.DISPENDDATE <= to_timestamp('${param.date} 23:59:59.999', 'YYYYMMDD HH24:MI:SSXXX')
				AND KBS.LEVELFLAG = '0'
								
		) AS KBS
		GROUP BY KBS.ID, KBS.UPPERBBSID, KBS.BBSNAME),
	
	TEMP2 AS (WITH RECURSIVE TMP1 AS 
			(SELECT ID, UPPERBBSID, ARRAY[ROW_NUMBER() OVER(ORDER BY SORTKEY)] AS SORTARRAY, BBSNAME,
				(CASE WHEN LEVELFLAG = '0' THEN ''
				ELSE BBSNAME END ) AS PATH, SORTKEY, LEVELFLAG
			FROM KT2_BBS 
			WHERE UPPERBBSID = 0 
			
			UNION 
			
			SELECT P.ID, P.UPPERBBSID,
				ARRAY_APPEND(C.SORTARRAY, ROW_NUMBER() OVER(ORDER BY P.SORTKEY)), P.BBSNAME, 
				(CASE WHEN P.LEVELFLAG = '0' THEN C.PATH 
					ELSE CONCAT(C.PATH, '/', P.BBSNAME) END 
				) AS PATH, P.SORTKEY, P.LEVELFLAG
			FROM KT2_BBS P
				INNER JOIN TMP1 C ON  P.UPPERBBSID = C.ID )
		SELECT ID, PATH, SORTARRAY FROM TMP1 WHERE LEVELFLAG = '0' )
		
SELECT TEMP1.*, TEMP2.PATH, TEMP2.SORTARRAY FROM TEMP1, TEMP2
WHERE TEMP1.ID = TEMP2.ID