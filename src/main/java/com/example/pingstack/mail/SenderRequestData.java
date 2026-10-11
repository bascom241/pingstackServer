package com.example.pingstack.mail;

import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;

public class SenderRequestData {
    private SendSmtpEmailSender sendSmtpEmailSender;
    private SendSmtpEmailTo sendSmtpEmailTo;
    private String subject;
    private String htmlContent;


    public SendSmtpEmailSender getSendSmtpEmailSender() {
        return sendSmtpEmailSender;
    }

    public void setSendSmtpEmailSender(SendSmtpEmailSender sendSmtpEmailSender) {
        this.sendSmtpEmailSender = sendSmtpEmailSender;
    }

    public SendSmtpEmailTo getSendSmtpEmailTo() {
        return sendSmtpEmailTo;
    }

    public void setSendSmtpEmailTo(SendSmtpEmailTo sendSmtpEmailTo) {
        this.sendSmtpEmailTo = sendSmtpEmailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public SenderRequestData(SendSmtpEmailSender sendSmtpEmailSender, SendSmtpEmailTo sendSmtpEmailTo, String subject, String htmlContent) {
        this.sendSmtpEmailSender = sendSmtpEmailSender;
        this.sendSmtpEmailTo = sendSmtpEmailTo;
        this.subject = subject;
        this.htmlContent = htmlContent;
    }
}
