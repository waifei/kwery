package com.kwery.tests.fluentlenium.urlconfiguration;

import com.kwery.models.UrlConfiguration;
import com.kwery.models.UrlConfiguration.Scheme;
import com.kwery.tests.fluentlenium.KweryFluentPage;
import org.fluentlenium.core.annotation.PageUrl;
import org.fluentlenium.core.hook.wait.Wait;

import static com.kwery.tests.util.Messages.URL_CONFIGURATION_SAVE_SUCCESS_M;
import static com.kwery.tests.util.TestUtil.TIMEOUT_SECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fluentlenium.assertj.FluentLeniumAssertions.assertThat;

@Wait(timeUnit = SECONDS, timeout = TIMEOUT_SECONDS)
@PageUrl("/#url-configuration/save")
public class UrlConfigurationSavePage extends KweryFluentPage {
    public static final String INPUT_VALIDATION_ERROR_MESSAGE = "Please fill in this field.";
    public static final String INPUT_LENGTH_VALIDATION_MIN_LENGTH_ERROR_MESSAGE = "Please lengthen this text to 4 characters or more (you are currently using %d characters)";
    public static final String INPUT_MINIMUM_VALUE_VALIDATION_MESSAGE = String.format("Value must be greater than or equal to %d.", UrlConfiguration.PORT_MIN);
    public static final String INPUT_MAXIMUM_VALUE_VALIDATION_MESSAGE = String.format("Value must be less than or equal to %d.", UrlConfiguration.PORT_MAX);

    public void waitForDefaultValues() {
        await().until($(".scheme-f")).value(Scheme.http.name());
        await().until($(".port-f")).value(getPort());
        await().until($(".domain-f")).value("localhost");
    }

    public void waitForFormValues(UrlConfiguration setting) {
        await().until($(".scheme-f")).value(setting.getScheme().name());
        await().until($(".port-f")).value(String.valueOf(setting.getPort()));
        await().until($(".domain-f")).value(setting.getDomain());
    }

    public void submitForm(UrlConfiguration urlConfiguration) {
        $(".scheme-f").fillSelect().withText(urlConfiguration.getScheme().name());
        $(".port-f").fill().with(String.valueOf(urlConfiguration.getPort()));
        $(".domain-f").fill().with(urlConfiguration.getDomain());
        clickSubmit();
    }

    public void submitEmptyForm() {
        $(".port-f").fill().with("");
        $(".domain-f").fill().with("");
        clickSubmit();
    }

    public void clickSubmit() {
        $(".url-configuration-submit-f").click();
    }

    public void waitForSaveSuccessMessage() {
        super.waitForSuccessMessage(URL_CONFIGURATION_SAVE_SUCCESS_M);
    }

    public String getPort() {
        String[] urlParts = getBaseUrl().split(":");
        return urlParts[urlParts.length - 1];
    }

    public void assertEmptyFieldValidationMessages() {
        assertThat($(".domain-error-f")).hasText(INPUT_VALIDATION_ERROR_MESSAGE);
        assertThat($(".port-error-f")).hasText(INPUT_VALIDATION_ERROR_MESSAGE);
    }

    public void assertMinLengthFieldValidationMessage(String domain) {
        assertThat($(".domain-error-f")).hasText(String.format(INPUT_LENGTH_VALIDATION_MIN_LENGTH_ERROR_MESSAGE, domain.length()));
    }

    public void assertMinimumValueFieldValidationMessage() {
        assertThat($(".port-error-f")).hasText(String.format(INPUT_MINIMUM_VALUE_VALIDATION_MESSAGE));
    }

    public void assertMaximumValueFieldValidationMessage() {
        assertThat($(".port-error-f")).hasText(INPUT_MAXIMUM_VALUE_VALIDATION_MESSAGE);
    }
}
