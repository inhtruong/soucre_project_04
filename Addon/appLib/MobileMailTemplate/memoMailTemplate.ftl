${data.bodyTitle}

<#-- 受付日時 -->
${messages.kme_l_mail_dateTime}${data.memoInfAcceptDateTime}
<#-- 依頼主 -->
<#if data.memoInf.customerName?has_content>
${messages.kme_l_mail_requester}${data.memoInf.customerName}
</#if>
<#-- 用件 -->
${messages.kme_l_mail_todo}${data.memoInf.title}
<#-- 内容 -->
<#if data.memoInf.message?has_content>
${messages.kme_l_mail_content}
${data.memoInf.message}

</#if>
<#-- 電話番号 -->
<#if data.memoInf.telNo?has_content>
${messages.kme_l_mail_telNo}${data.memoInf.telNo}
</#if>
<#-- 送信者 -->
${messages.kme_l_mail_sender}${data.memoInf.tranEmp.empName}
${messages.kme_l_mail_sendTo}
<#-- 送信先 -->
<#assign toSize = data.memoInf.receivers?size>
<#list data.memoInf.receivers as item>
	<#assign idx = item?index>
	<#if idx gte 10 >
		<#break>
	</#if>
${item.receiver.empName}
</#list>
<#if data.memoNumberOfReceivers?has_content>
${data.memoNumberOfReceivers}
</#if>

<#-- 以下のURLをクリックして、受信確認を行ってください。 -->
<#if data.confirmLink?has_content>

${messages.kme_l_confirmLink}
${data.confirmLink}
</#if>
<#-- 以下のURLをクリックすると、スマートフォン環境に接続できます。-->
<#if data.accessLink?has_content>

${commonMsg.common_l_url_accessLink}
${data.accessLink}
</#if>