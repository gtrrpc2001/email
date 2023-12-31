package com.example.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMailSender extends javax.mail.Authenticator{
    private String host = "smtp.gmail.com";
    private String sendEmail = "email";
    private String pwd = "pwd";
    private Session session;
    private String emailCode;



    public GMailSender(){
        emailCode = createEmailCode();
        int key = 465;
        Properties prop = new Properties();
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.put("mail.host",host);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.socketFactory.port", "587");
        prop.put("mail.smtp.socketFactory.fallback","false");
        prop.put("mail.smtp.starttls.enable","true");
        prop.setProperty("mail.smtp.quitwait", "false");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                //해당 메서드에서 사용자의 계정(id & password)을 받아 인증받으며 인증 실패시 기본값으로 반환됨.
                return new PasswordAuthentication(sendEmail, pwd);
            }
        });
    }

    public String getEmailCode() {
        return emailCode;
    } //생성된 이메일 인증코드 반환

    private String createEmailCode() { //이메일 인증코드 생성
        String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String newCode = new String();

        for (int x = 0; x < 8; x++) {
            int random = (int) (Math.random() * str.length);
            newCode += str[random];
        }

        return newCode;
    }


    public synchronized void sendMail(String subject, String body, String recipients) throws Exception {
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            //DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain")); //본문 내용을 byte단위로 쪼개어 전달
            message.setSender(new InternetAddress(sendEmail));  //본인 이메일 설정
            message.setSubject(subject); //해당 이메일의 본문 설정
            message.setContent(body, "text/plain");
            message.setText(body);
            //message.setDataHandler(handler);
//        if (recipients.indexOf(',') > 0)
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
//        else
//            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            //Transport transport = session.getTransport("smtp");
            //transport.connect("smtp.gmail.com",sendEmail,pwd);
            Transport.send(message, message.getAllRecipients()); //메시지 전달
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}
