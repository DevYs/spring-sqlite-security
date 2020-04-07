package devy.spring.security.config;

import devy.spring.security.service.UserInfoService;
import devy.spring.security.service.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

/**
 * 웹 어플리케이션이 실행된 후 작업<br>
 * Configration, Bean, Component 등이 모두 생성된 상태이다.
 * @author hanys
 */
@Configuration
public class AfterStartedApplication {

    private Logger logger = LoggerFactory.getLogger(AfterStartedApplication.class);

    @Autowired
    private UserInfoService userInfoService;

    /**
     * Spring boot가 시작되면 실행된다.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void applicationReady() {
        logger.info("Started application !!");

        String firstId = "first";
        UserDetails first = userInfoService.loadUserByUsername(firstId);
        if(first == null) {
            UserInfo defaultUser = new UserInfo();
            defaultUser.setUserId(firstId);
            defaultUser.setPassword(firstId);
            defaultUser.setUserName(firstId);
            defaultUser = userInfoService.addUser(defaultUser);

            if(0 < defaultUser.getUserNo()) {
                logger.info("defaultUser : " + defaultUser.toString());
            }
        }
    }

}
