package com.example.login_project;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, Void> {

    private Session session;
    private String email, subject, message;

    public JavaMailAPI(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Thiết lập các thuộc tính SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");  // Địa chỉ máy chủ SMTP
        props.put("mail.smtp.socketFactory.port", "465");  // Cổng cho SSL
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  // Sử dụng SSL
        props.put("mail.smtp.auth", "true");  // Bật xác thực
        props.put("mail.smtp.port", "465");  // Cổng để kết nối

        // Khởi tạo session với xác thực
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Sử dụng email và mật khẩu ứng dụng
                return new PasswordAuthentication("duyetck9@gmail.com", "wwfc jwfj oftf oehy");  // Thay bằng mật khẩu ứng dụng
            }
        });

        try {
            Message mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress("duyetck9@gmail.com"));
            mm.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mm.setSubject(subject);

            // Chuyển đổi nội dung thành HTML
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Your OTP Code</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            background-color: #f4f4f4;\n" +
                    "            margin: 0;\n" +
                    "            padding: 20px;\n" +
                    "        }\n" +
                    "        .container {\n" +
                    "            max-width: 600px;\n" +
                    "            margin: auto;\n" +
                    "            background: #ffffff;\n" +
                    "            padding: 20px;\n" +
                    "            border-radius: 8px;\n" +
                    "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                    "        }\n" +
                    "        .header {\n" +
                    "            text-align: center;\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "        .header img {\n" +
                    "            max-width: 100px;\n" +
                    "        }\n" +
                    "        .otp-code {\n" +
                    "            font-size: 24px;\n" +
                    "            font-weight: bold;\n" +
                    "            color: #4CAF50;\n" +
                    "            text-align: center;\n" +
                    "            margin: 20px 0;\n" +
                    "        }\n" +
                    "        .footer {\n" +
                    "            text-align: center;\n" +
                    "            margin-top: 20px;\n" +
                    "            font-size: 14px;\n" +
                    "            color: #666;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <div class=\"header\">\n" +
                    "            <img src=\"https://example.com/logo.png\" alt=\"Your Logo\">\n\n" +
                    "            <h1>Your OTP Code</h1>\n" +
                    "        </div>\n" +
                    "        <p>Hi,</p>\n" +
                    "        <p>Your OTP code for verification is:</p>\n" +
                    "         <div class='otp-code'>" + message + "</div>" +
                    "        <p>This code is valid for 5 minutes. Please do not share it with anyone.</p>\n" +
                    "        <div class=\"footer\">\n" +
                    "            <p>Thank you for using our service!</p>\n" +
                    "            <p>If you did not request this, please ignore this email.</p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>\n"; // Thay thế bằng nội dung HTML ở trên
            mm.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(mm);
            Log.d("EmailStatus", "Email sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("EmailStatus", "Failed to send email: " + e.getMessage());
        }
        return null;
    }
}
