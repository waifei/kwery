package com.kwery.tests.controllers.apis.mock;

import com.kwery.controllers.apis.UserApiController;
import com.kwery.dao.UserDao;
import ninja.validation.Validation;
import org.junit.Before;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

public abstract class AbstractUserApiControllerMockTest extends ControllerMockTest {
    @Mock
    protected Validation validation;
    @Mock
    protected UserDao userDao;
    protected UserApiController userApiController;

    @Before
    public void before() {
        userApiController = new UserApiController();
        userApiController.setMessages(messages);
        userApiController.setUserDao(userDao);
        when(validation.hasViolations()).thenReturn(false);
    }
}