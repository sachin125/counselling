package com.inn.counselling.service;


import com.inn.counselling.model.Role;
import com.inn.counselling.service.generic.IGenericService;


public interface IRoleService extends IGenericService<Long, Role> {

	Role findbyName(String rolename);

}
