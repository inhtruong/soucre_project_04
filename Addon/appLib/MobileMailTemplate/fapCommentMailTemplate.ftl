<#-- 0. Body title-->
${data.bodyTitle}

<#-- 1. 申請者, 2. 代理申請者-->
${messages.dataFlow_mail_comment_l_applicant}${data.cmtData.appEmpName} ${data.proxyEmpName}
<#-- 3. 様式-->
${messages.dataFlow_mail_comment_l_baseForm}${data.cmtData.appStyle}
<#-- 4. 種別-->
${messages.dataFlow_mail_comment_l_detailForm}${data.cmtData.appType}
<#-- 5. 件名-->
${messages.dataFlow_mail_comment_l_title}${data.cmtData.subject}
<#-- 6. 申請番号-->
${messages.dataFlow_mail_comment_l_applyNo}${data.cmtData.applyNo}
<#-- 7. コメント登録者-->
${messages.dataFlow_mail_comment_l_commenter}${data.cmtData.commenter}
<#-- 8. コメント登録日時-->
${messages.dataFlow_mail_comment_l_comment_time}${data.commentTime}
<#-- 9. コメント内容, 10. 添付ファイル有無-->
${messages.dataFlow_mail_comment_l_content}
${data.cmtData.commentContent}
<#if data.cmtData.hasAttachFile>${messages.dataFlow_mail_comment_l_has_attach_file}</#if>
