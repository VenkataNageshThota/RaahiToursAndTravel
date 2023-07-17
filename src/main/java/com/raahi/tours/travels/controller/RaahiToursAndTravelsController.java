package com.raahi.tours.travels.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raahi.tours.travels.domain.Mail;
import com.raahi.tours.travels.domain.RaahiToursAndTravels;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RestController
public class RaahiToursAndTravelsController {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	@Qualifier("emailConfigBean")
	private Configuration emailConfig;

	@PostMapping("/sendMail")
	public String sendMail(@RequestBody RaahiToursAndTravels raahiToursAndTravels)
			throws MessagingException, IOException, TemplateException {
		Mail mail = new Mail();
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		Map<String, String> model1 = new HashMap<>();
		model1.put("name", raahiToursAndTravels.getFirstName() + " " + raahiToursAndTravels.getLastName());
		model1.put("email", raahiToursAndTravels.getEmail());
		model1.put("mobile", raahiToursAndTravels.getMobile());
		model1.put("message", raahiToursAndTravels.getMessage());
		mail.setModel(model1);
		Template template = emailConfig.getTemplate("UserDetails.html");
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, mail.getModel());
		mimeMessageHelper.setTo("");
		mimeMessageHelper.setText(html, true);
		mimeMessageHelper.setSubject("RaahiToursAndTravels");
		mimeMessageHelper.setFrom("");
		mailSender.send(message);
		return "mail sent";
	}
}
