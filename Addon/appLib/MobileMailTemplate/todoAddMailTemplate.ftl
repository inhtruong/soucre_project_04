${data.bodyTitle}

<#-- 依頼者 -->
${messages.ktd01_l_mailRegEmp}${data.todoDto.regEmp.empName} ${data.todoDto.regEmp.mailAddress!}
<#-- 件名 -->
${messages.ktd01_l_mailTodoTitle}${data.todoDto.subject}
<#-- 顧客-->
<#if data.todoDto.sapLicense>
${messages.ktd01_l_todoCustomer_mail}${data.todoDto.customerName!}
</#if>
<#-- 優先度-->
<#if data.priorityName?has_content>
${messages.ktd01_l_mailPriority}${data.priorityName}
</#if>
<#-- 期限 -->
<#if data.todoDeadline?has_content>
${messages.ktd01_l_mailDeadline}${data.todoDeadline}
</#if>
<#-- 着手日 -->
<#if data.todoFromDate?has_content>
${messages.ktd01_l_mailFromDate}${data.todoFromDate}
</#if>
<#-- 内容 -->
${messages.ktd01_l_mailTodoContent}
${data.todoContent}

<#-- 非公開 -->
<#if data.nonePublic?has_content>
${messages.ktd01_l_mailPublic}
</#if>
<#-- 依頼先 -->
${messages.ktd01_l_mailTragetr}
<#list data.todoDto.toLst as item>
	<#assign idx = item?index>
	<#if idx gte data.maxEmps >
		<#break>
	</#if>
${item.empName}
</#list>
<#if data.remainOthers?has_content>
${data.remainOthers}
</#if>

<#-- 関係者 -->
${messages.ktd01_l_mailTragetrCC}
<#list data.todoDto.ccLst as item>
	<#assign idx = item?index>
	<#if idx gte data.maxEmps >
		<#break>
	</#if>
${item.empName}
</#list>
<#if data.remainCCOthers?has_content>
${data.remainCCOthers}
</#if>

<#-- 以下のURLをクリックすると、スマートフォン環境に接続できます。-->
<#if data.accessLink?has_content>

${commonMsg.common_l_url_accessLink}
${data.accessLink}
</#if>