${data.bodyTitle}

<#-- 依頼者 -->
${messages.ktd01_l_mailRegEmp}${data.todoDto.regEmp.empName} ${data.todoDto.regEmp.mailAddress!}
<#-- 件名 -->
${messages.ktd01_l_mailTodoTitle}${data.todoDto.subject}
<#-- 顧客-->
<#if data.todoDto.sapLicense>
${messages.ktd01_l_todoCustomer_mail}${data.todoDto.customerName!}
</#if>
<#-- 内容 -->
${messages.ktd01_l_mailTodoContent}
${data.todoContent}

<#-- 以下のURLをクリックすると、スマートフォン環境に接続できます。-->
<#if data.accessLink?has_content>

${commonMsg.common_l_url_accessLink}
${data.accessLink}
</#if>