package controllers.apis;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dao.UserDao;
import filters.DashRepoSecureFilter;
import models.User;
import ninja.Context;
import ninja.FilterWith;
import ninja.Result;
import ninja.Results;
import ninja.i18n.Messages;
import ninja.validation.JSR303Validation;
import ninja.validation.Validation;
import views.ActionResult;

import static com.google.common.base.Optional.of;
import static controllers.ControllerUtil.fieldMessages;
import static controllers.MessageKeys.ADMIN_USER_ADDITION_FAILURE;
import static controllers.MessageKeys.ADMIN_USER_ADDITION_SUCCESS;
import static controllers.MessageKeys.LOGIN_FAILURE;
import static controllers.MessageKeys.LOGIN_SUCCESS;
import static views.ActionResult.Status.failure;
import static views.ActionResult.Status.success;

@Singleton
public class UserApiController {
    public static final String SESSION_USERNAME_KEY = "username";

    @Inject
    private UserDao userDao;

    @Inject
    private Messages messages;

    public Result addAdminUser(
            Context context,
            @JSR303Validation User user,
            Validation validation) {

        Result json = Results.json();
        ActionResult actionResult = null;

        if (validation.hasViolations()) {
            actionResult = new ActionResult(failure, fieldMessages(validation, context, messages, json));
        } else {
            User existingUser = userDao.getByUsername(user.getUsername());

            if (existingUser == null) {
                userDao.save(user);

                String message = messages.get(ADMIN_USER_ADDITION_SUCCESS, context, of(json), user.getUsername()).get();
                actionResult = new ActionResult(success, message);
            } else {
                String message = messages.get(ADMIN_USER_ADDITION_FAILURE, context, of(json), user.getUsername()).get();
                actionResult = new ActionResult(failure, message);
            }
        }

        return json.render(actionResult);
    }

    public Result login(Context context, User user) {
        User fromDb = userDao.getUser(user.getUsername(), user.getPassword());

        Result json = Results.json();
        ActionResult actionResult = null;

        if (fromDb == null) {
            String message = messages.get(LOGIN_FAILURE, context, of(json)).get();
            actionResult = new ActionResult(failure, message);
        } else {
            String message = messages.get(LOGIN_SUCCESS, context, of(json), user.getUsername()).get();
            actionResult = new ActionResult(success, message);
            context.getSession().put(SESSION_USERNAME_KEY, user.getUsername());
        }

        return json.render(actionResult);
    }

    @FilterWith(DashRepoSecureFilter.class)
    public Result user(Context context) {
        String loggedInUserName = context.getSession().get(SESSION_USERNAME_KEY);
        User user = userDao.getByUsername(loggedInUserName);
        Result json = Results.json();
        json.render(user);
        return json;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }
}