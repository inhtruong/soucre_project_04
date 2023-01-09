<#-- 0. 本文タイトル-->
${data.reqApprovalDto.bodyTitle}

<#-- 1. 申請者-->
${messages.dataFlow_mail_l_applicant}${data.reqApprovalDto.applierInfo}
<#-- 2. 様式-->
${messages.dataFlow_mail_l_baseForm}${data.reqApprovalDto.appStyle}
<#-- 3. 種別-->
${messages.dataFlow_mail_l_detailForm}${data.reqApprovalDto.appType}
<#-- 4. 件名-->
${messages.dataFlow_mail_l_title}${data.reqApprovalDto.subject}
<#-- 5. 権限-->
${messages.dataFlow_mail_l_authority}${data.reqApprovalDto.applyAuthority}
<#-- 6. 申請番号-->
${messages.dataFlow_mail_l_applyNo}${data.reqApprovalDto.applyNo}
<#-- 7. 前の処理者/代行処理者, 8. 前の代行処理者-->
<#if data.reqApprovalDto.preRouteUser?has_content>
${data.reqApprovalDto.preApproverInfo}
</#if>
<#-- 9. 次の処理者-->
<#if data.reqApprovalDto.nextRouteUser?has_content>
${messages.dataFlow_mail_l_nextApprover}${data.reqApprovalDto.nextRouteName}
</#if>