<#-- 見出し-->
${data.bodyTitle}

<#-- 予約者 -->
${messages.ksc01_l_mobileDim03_body_person_book}
${data.scheDto.regEmp.empName}
${data.scheDto.regEmp.mailAddress!}

<#-- 追加登録者 -->
<#if data.scheDto.addTarget>
${messages.ksc01_l_mobileDim03_body_newParticipant}
${data.scheDto.loginEmp.empName}
${data.scheDto.loginEmp.mailAddress!}

</#if>
<#-- 日付時刻 -->
${data.scheDto.regDateTimeLbl1}${data.scheDto.regDateTime1!}
${data.scheDto.regDateTimeLbl2}${data.scheDto.regDateTime2!}
<#-- 件名 -->
${messages.ksc01_l_mobileDim03_body_title}${data.scheDto.subject!}
<#-- 顧客-->
<#if data.scheDto.sapLicense>
${messages.ksc01_l_mail_customer}${data.scheDto.customerName!}
</#if>
<#-- 内容 -->
${messages.ksc01_l_mobileDim03_body_content}
${(data.scheDto.detail!)}

<#-- 繰り返し-->
<#if data.scheDto.repeatDateInfo?has_content>
${messages.ksc01_l_body_repeat_Date}
${data.scheDto.repeatDateInfo}

</#if>
<#-- 登録先-->
${messages.ksc01_l_mobileDim03_body_emp_list}
<#list data.scheDto.toLst as item>
${item.empName}
</#list>
<#if data.remainOthers?has_content>
${data.remainOthers}
</#if>

<#-- 場所 -->
<#if data.scheDto.place?has_content>
${messages.ksc01_l_mailContent_place}${data.scheDto.place}
</#if>
<#-- 施設-->
<#if data.scheDto.resourceLst?has_content>
${messages.ksc01_l_facilityTitleMobile}
<#list data.scheDto.resourceLst as item>
${item}
</#list>

</#if>

<#-- 以下のURLをクリックすると、スマートフォン環境に接続できます。-->
<#if data.accessLink?has_content>
${commonMsg.common_l_url_accessLink}
${data.accessLink}
</#if>