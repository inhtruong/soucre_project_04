<#-- 0. 本文タイトル-->
${data.noticeApprovalDto.bodyTitle}

<#-- 1. 決裁状況-->
${messages.dataFlow_mail_l_result}${data.noticeApprovalDto.approvalStatus}
<#-- 2. 申請者, 3. 代理申請者-->
${messages.dataFlow_mail_l_applicant}${data.noticeApprovalDto.applier}
<#-- 5. 様式-->
${messages.dataFlow_mail_l_baseForm}${data.noticeApprovalDto.appStyle}
<#-- 6. 種別-->
${messages.dataFlow_mail_l_detailForm}${data.noticeApprovalDto.appType}
<#-- 7. 件名-->
${messages.dataFlow_mail_l_title}${data.noticeApprovalDto.subject}
<#-- 8. 申請番号-->
${messages.dataFlow_mail_l_applyNo}${data.noticeApprovalDto.applyNo}
<#-- 9. 処理者, 10. 代行処理者-->
<#if data.noticeApprovalDto.notBackAway>
${messages.dataFlow_mail_l_approver}${data.noticeApprovalDto.approver}
<#if data.noticeApprovalDto.approvalProxyName?has_content>
${messages.dataFlow_mail_l_proxyApprover}${data.noticeApprovalDto.approvalProxy}
</#if>
</#if>
