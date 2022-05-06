package com.inn.counselling.dao;

import com.inn.counselling.dao.generic.IGenericDao;
import com.inn.counselling.model.Users;

public interface IUserDao extends IGenericDao<Long, Users> {

	Users loadDomainUsersByUserNameDomain(String username, String domainname);

	Users findByUsername(String username);

	Users findByUsernameOrConatctNo(String username, long contactno);

	Boolean unLock(String username);


}
