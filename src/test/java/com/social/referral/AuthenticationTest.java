package com.social.referral;

import com.social.referral.dto.AuthenticationRequest;
import com.social.referral.services.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.verify;

@SpringBootTest
class AuthenticationTest {

	@Autowired
	AuthenticationService authenticationService;

	@Test
	@Order(1)
	void checkAuthenticationTrue() {


		AuthenticationRequest authenticationRequest=new AuthenticationRequest().toBuilder().email("tomjohn1997@gmail.com").password("94319431").build();

		Assertions.assertNotNull(authenticationService.authenticate(authenticationRequest).getAccessToken());
	}



}
