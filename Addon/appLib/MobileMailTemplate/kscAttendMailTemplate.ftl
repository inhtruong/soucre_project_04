<#-- 見出し-->
${data.bodyTitle}

<#-- 予約者 -->
${messages.ksc01_l_mobileDim03_body_person_book}${data.scheDto.regEmp.empName}
<#-- 追加登録者 -->
<#if data.scheDto.addTarget>
${messages.ksc01_l_mobileDim03_body_newParticipant}${data.scheDto.addTargetEmp.empName}
</#if>
<#-- 承認者-->
${messages.ksc01_l_mail_approver}${data.scheDto.loginEmp.empName}
<#-- 社員-->
<#if data.scheDto.dispEmp?has_content>
${messages.ksc01_l_mail_employee}${data.scheDto.dispEmp.empName}
</#if>
<#-- 日付時刻 -->
${data.scheDto.regDateTimeLbl1}${data.scheDto.regDateTime1!}
${data.scheDto.regDateTimeLbl2}${data.scheDto.regDateTime2!}
<#-- 繰り返し-->
<#if data.scheDto.repeatDateInfo?has_content>
${messages.ksc01_l_mail_repeateDateTime}
${data.scheDto.repeatDateInfo}

</#if>
<#-- 件名 -->
${messages.ksc01_l_mobileDim03_body_title}${data.scheDto.subject}
<#-- 顧客-->
<#if data.scheDto.sapLicense>
${messages.ksc01_l_mail_customer}${data.scheDto.customerName!}
</#if>
<#-- コメント -->
${messages.ksc01_l_mail_attendComment}
${data.scheDto.replyComment!}