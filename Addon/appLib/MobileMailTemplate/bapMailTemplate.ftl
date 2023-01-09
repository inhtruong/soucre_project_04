${data.bodyTitle}

<#-- 受信日時 -->
${messages.bap01_l_mail_receiveDateTime}${data.sendMailDateTime}
<#-- 件名 -->
${messages.bap01_l_mail_title}${data.mailDto.subject}
<#-- 本文 -->
${messages.bap01_l_mail_content}
${data.content}

<#-- 送信者 -->
${messages.bap01_l_mail_sender}${data.sender}
<#-- 宛先(To) -->
${messages.bap01_l_mail_toTarget}${data.toListCnt}
<#-- 宛先(Cc) -->
${messages.bap01_l_mail_ccTarget}${data.ccListCnt}

<#-- 以下のURLをクリックすると、スマートフォン環境に接続できます。-->
<#if data.accessLink?has_content>
${commonMsg.common_l_url_accessLink}
${data.accessLink}
</#if>