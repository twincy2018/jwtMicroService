package com.com.service.data;

import javax.persistence.PostPersist;

import org.springframework.messaging.support.MessageBuilder;




import com.com.service.model.Registration;
import com.com.service.utils.BeanUtils;

public class UserRegistrationListener {
	private Registration registration;
	
	public UserRegistrationListener(){
	}

	@PostPersist
	public void register(User user){
		getRegistration().newUserUaaCompleted().
		send(MessageBuilder.withPayload(user.getEmail()).build());	}
	
	private Registration getRegistration(){
		if(registration==null){
			this.registration =  BeanUtils.getBean(Registration.class);
		}
		return registration;
	}
}
