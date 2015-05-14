package com.goodvideotutorials.spring.controllers;

import java.io.Reader;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.goodvideotutorials.spring.dto.SignupForm;
import com.goodvideotutorials.spring.services.UserService;
import com.goodvideotutorials.spring.util.MyUtil;
import com.goodvideotutorials.spring.validators.SignupFormValidator;


//import com.goodvideotutorials.spring.mail.MockMailSender;

//@RestController
@Controller
public class RootController {
	
	//@Resource//(name="mailSender")//Injecting Depdency
	private MailSender mailSender; // = new MockMailSender();
	
	private UserService userService;
	
	private SignupFormValidator signupFormValidator;
	
		
	private static final Logger logger = LoggerFactory.getLogger(RootController.class);
	//private MailSender mockMailSender;
	
	/*@Resource
	public void setMockMailSender(MailSender mockMailSender){
		this.mockMailSender = mockMailSender;
	}*/
	
	@Autowired
	public RootController(MailSender mailSender, UserService userService, 
			SignupFormValidator signupFormValidagtor) {
		this.mailSender = mailSender;
		this.userService = userService;
		this.signupFormValidator = signupFormValidagtor;
	}

	@InitBinder("signupForm")
	protected void initSignupBinder(WebDataBinder binder){
		binder.setValidator(signupFormValidator);
	}
	
	
//	@RequestMapping("/")
//	//@ResponseBody
//	public String home()throws MessagingException{
//		//mailSender.send("artu_jose@yahoo.com.mx","Hello","Hello from Spring");
//		return "home";
//	}
	
	@RequestMapping(value="/signup", method = RequestMethod.GET)
	public String signup(Model model){
		model.addAttribute(new SignupForm());
		return "signup";
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public String signup(@ModelAttribute("signupForm") @Valid SignupForm signupForm,
				BindingResult result, RedirectAttributes redirectAttributes){
	
		if (result.hasErrors())
				return "signup";
		
		//logger.info(signupForm.toString());
		userService.signup(signupForm);
		
		MyUtil.flash(redirectAttributes, "sucess", "signupSucess");
		
		
		return "redirect:/";
	
	}
	
	
	
//	//@ResponseBody
//	public String home()throws MessagingException{
//		//mailSender.send("artu_jose@yahoo.com.mx","Hello","Hello from Spring");
//		return "home";
//	}
	
	
}