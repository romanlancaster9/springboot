package com.example.springboot.utils;

import com.example.springboot.config.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class EmailUtils {

    @Autowired
    private EmailProperties emailProperties;


    public void sendEmail(String toAddress, String subject, String content, boolean isHtml) {

        // 1. 配置邮件服务器属性
        Properties props = new Properties();
        props.put("mail.smtp.host", emailProperties.getHost());
        props.put("mail.smtp.port", emailProperties.getPort());
        props.put("mail.smtp.auth", "true");

        // 根据端口选择加密方式
        if ("465".equals(emailProperties.getPort())) {
            props.put("mail.smtp.socketFactory.port", emailProperties.getPort());
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        } else {
            props.put("mail.smtp.starttls.enable", "true"); // 使用TLS
        }

        // 2. 创建Session对象
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 使用邮箱和授权码认证
                return new PasswordAuthentication(emailProperties.getUsername(), emailProperties.getPassword());
            }
        });

        try {
            // 3. 创建邮件消息
            Message message = new MimeMessage(session);
            // 设置发件人
            message.setFrom(new InternetAddress(emailProperties.getUsername()));
            // 设置收件人
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
            // 设置主题
            message.setSubject(subject);

            // 设置邮件内容
            if (isHtml) {
                message.setContent(content, "text/html;charset=UTF-8");
            } else {
                message.setText(content);
            }

            // 4. 发送邮件
            Transport.send(message);
            System.out.println("邮件发送成功!");

        } catch (MessagingException e) {
            throw new RuntimeException("邮件发送失败: " + e.getMessage(), e);
        }
    }

}
