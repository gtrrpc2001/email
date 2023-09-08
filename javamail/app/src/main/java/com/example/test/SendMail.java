package com.example.test;

import android.content.Context;
import android.widget.Toast;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail {

    String getEmail;
    public SendMail(String getEmail){
        this.getEmail = getEmail;
    }
    GMailSender gMailSender = new GMailSender();

    public String sendSecurityCode(Context context, String tile, String content) {
        boolean checked = false;
        String emailCode = "";
        try {
            emailCode = gMailSender.getEmailCode();
            gMailSender.sendMail(tile, content + "\n" + "인증번호 : " + emailCode, getEmail);
            Toast.makeText(context, "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
            checked = true;
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {

            Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checked){
            return emailCode;
        }else{
            return "";
        }
    }

}
