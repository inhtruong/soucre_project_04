<#-- データベース名  -->
${messages.xdb02_l_notice_mail_database_name}${data.webDbDto.databaseName}

<#-- 件名 -->
${messages.xdb02_l_notice_mail_title}${data.webDbDto.subject}
<#-- 本文 -->
${messages.xdb02_l_notice_mail_body}
${data.webDbDto.bodyNotice}

<#-- 通知日時 -->
${messages.xdb02_l_notice_mail_dateTime}${data.webDbDto.noticeDateTime}