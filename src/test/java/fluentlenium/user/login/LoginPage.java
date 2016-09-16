package fluentlenium.user.login;

import fluentlenium.RepoDashFluentLeniumTest;
import fluentlenium.RepoDashPage;
import models.User;
import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.annotation.AjaxElement;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.support.FindBy;

import static java.text.MessageFormat.format;
import static java.util.concurrent.TimeUnit.SECONDS;
import static util.Messages.LOGIN_FAILURE_M;
import static util.Messages.LOGIN_SUCCESS_M;

public class LoginPage extends FluentPage implements RepoDashPage {
    @AjaxElement
    @FindBy(id = "loginForm")
    protected FluentWebElement loginForm;

    @Override
    public String getUrl() {
        return "/#user/login";
    }

    public void submitForm(String... inputs) {
        fill("input").with(inputs);
        click("#login");
    }

    @Override
    public boolean isRendered() {
        return loginForm.isDisplayed();
    }

    public void waitForSuccessMessage(User user) {
        await().atMost(RepoDashFluentLeniumTest.TIMEOUT_SECONDS, SECONDS).until(".isa_info").hasText(format(LOGIN_SUCCESS_M, user.getUsername()));
    }

    public void waitForFailureMessage() {
        await().atMost(RepoDashFluentLeniumTest.TIMEOUT_SECONDS, SECONDS).until(".isa_error").hasText(LOGIN_FAILURE_M);
    }
}