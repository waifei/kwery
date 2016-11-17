package com.kwery.tests.fluentlenium.user.admin;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.kwery.tests.fluentlenium.RepoDashFluentLeniumTest;
import com.kwery.tests.fluentlenium.user.login.LoginPage;
import com.kwery.tests.fluentlenium.utils.UserTableUtil;
import com.kwery.models.User;
import org.junit.Before;

import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;
import static com.kwery.tests.fluentlenium.utils.DbUtil.getDatasource;
import static org.junit.Assert.fail;

public abstract class AddAdminUserTest extends RepoDashFluentLeniumTest {
    protected AddAdminUserPage page;
    protected User user;

    @Before
    public void setUpAddAdminUserTest() {
        UserTableUtil userTableUtil = new UserTableUtil(1);

        new DbSetup(
                new DataSourceDestination(getDatasource()),
                sequenceOf(
                        userTableUtil.insertOperation()
                )
        ).launch();

        user = userTableUtil.firstRow();

        LoginPage loginPage = createPage(LoginPage.class);
        loginPage.withDefaultUrl(getServerAddress());
        goTo(loginPage);
        if (!loginPage.isRendered()) {
            fail("Login page is not rendered");
        }
        loginPage.submitForm(user.getUsername(), user.getPassword());
        loginPage.waitForSuccessMessage(user);

        page = createPage(AddAdminUserPage.class);
        page.withDefaultUrl(getServerAddress());
        goTo(page);
        page.isRendered();
    }
}