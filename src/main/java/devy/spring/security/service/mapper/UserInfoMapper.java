package devy.spring.security.service.mapper;

import devy.spring.security.service.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {

	UserInfo getUser(String userId);

	int addUser(UserInfo userInfo);
}
