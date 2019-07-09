package devy.spring.security.service;

import devy.spring.security.service.domain.UserInfo;
import devy.spring.security.service.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserInfoService implements UserDetailsService {

	private final Logger logger = Logger.getLogger(UserInfoService.class.getSimpleName());

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		return userInfoMapper.getUser(userId).user(UserInfo.ROLE_USER);
	}

	public UserInfo addUser(UserInfo userInfo) {
		String password = passwordEncoder.encode(userInfo.getPassword());
		userInfo.setPassword(password);
		int result = userInfoMapper.addUser(userInfo);
		logger.info(userInfo.toString());
		return userInfo;
	}

}
