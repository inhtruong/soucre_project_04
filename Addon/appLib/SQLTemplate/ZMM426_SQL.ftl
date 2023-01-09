WITH SELECT_MAIL AS (
	SELECT bmn.empid, bmn.mailid, bmn.id 
	FROM bt2_mail_node bmn 
		LEFT JOIN bt2_mail bm ON bmn.mailid = bm.id  
	WHERE bm.sendDateTime <= to_timestamp('${param.date} 23:59:59.999', 'YYYYMMDD HH24:MI:SSXXX')
		AND bmn.nodetype = '0' 
    	<#if param.empIds?has_content>
		AND bmn.empid IN ( ${param.empIds} )  
		</#if> 
	)
		
, SELECT_DOCID AS ( 
	 SELECT bmc.mailid, bmcdc.docid 
	 FROM bt2_mail_comment bmc 
		INNER JOIN bt2_mail_com_doc_con bmcdc ON bmc.id = bmcdc.commentid 
		
	 UNION ALL 
	 SELECT bmdc.mailid, bmdc.docid FROM bt2_mail_doc_conn bmdc)
	 
SELECT he.userid, he.empname, he.empnamefurigana, he.retiredate, 
	CEIL(COALESCE(SUM(qbd.filesize), 0) * ${param.attachFactor} ) AS filesize, COUNT(distinct sm.id) AS countgroupby

FROM select_mail sm 
	INNER JOIN ht2_employee he ON sm.empid = he.empid
	LEFT JOIN select_docid sd ON sm.mailid = sd.mailid
	LEFT JOIN qt2_binary_data qbd ON sd.docid = qbd.docid

<#if param.empIds?has_content>
WHERE he.empid IN ( ${param.empIds} )  
</#if>

GROUP BY he.userid, he.empnamefurigana, he.empname, he.retiredate
