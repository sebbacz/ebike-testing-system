package com.example.ebike_testing_system.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("smtp.gmail.com")
    private String host;

  @Value("${spring.mail.port}")
    private Integer port;

    @Value("ebiketeam123test@gmail.com")
    private String username;

    @Value("lhrt fcwq jnmw qbln")
    private String password;

    @Bean
    public JavaMailSenderImpl javaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        props.put("mail.mime.address.strict", "false");

        return mailSender;
    }
}
