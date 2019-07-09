package devy.spring.security.controller;

import devy.spring.security.service.UserInfoService;
import devy.spring.security.service.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.logging.Logger;

@Controller
public class TestController {

	private final Logger logger = Logger.getLogger(TestController.class.getSimpleName());

	@Autowired
	private UserInfoService userInfoService;

	@GetMapping("/test")
	public String test() {
		return "test";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/user")
	public String join() {
		return "join";
	}

	@PostMapping("/user")
	public String joinProc(UserInfo userInfo) {

		logger.info(userInfo.toString());

		userInfoService.addUser(userInfo);
		return "test";
	}

}
