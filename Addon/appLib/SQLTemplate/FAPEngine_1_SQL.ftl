select
	detail.*
from
	FT2_DETAIL_FORM detail,
	RT2_APPLICATION_MATTER matter,
	FT2_BASE_FORM baseform
where
	matter.appform = baseform.baseform
	and baseform.baseform = detail.baseform
	and matter.appfolderid = '${param.appFolderId}'
	and detail.corpid = '${param.corpId}'
	and baseform.displayflag = '1'
	and detail.availabledate = (
	select
		max(detail2.availabledate)
	from
		FT2_DETAIL_FORM detail2 
	where
		detail2.corpid = detail.corpid
		and detail2.baseform = detail.baseform
		and detail2.availabledate <= '${param.baseDate}')
	    <#if param.appMatterId?has_content> 
		and matter.id = '${param.appMatterId}'
		</#if>
order by
	matter.listorder,
	detail.conditiontype,
	detail.conditionfrom