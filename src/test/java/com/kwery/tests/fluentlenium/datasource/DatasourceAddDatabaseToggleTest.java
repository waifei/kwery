package com.kwery.tests.fluentlenium.datasource;

import com.kwery.models.Datasource;
import com.kwery.tests.util.ChromeFluentTest;
import com.kwery.tests.util.LoginRule;
import com.kwery.tests.util.NinjaServerRule;
import junit.framework.TestCase;
import org.fluentlenium.core.annotation.Page;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DatasourceAddDatabaseToggleTest extends ChromeFluentTest {
    NinjaServerRule ninjaServerRule = new NinjaServerRule();

    @Page
    DatasourceAddPage page;

    @Rule
    public RuleChain ruleChain = RuleChain.outerRule(ninjaServerRule).around(new LoginRule(ninjaServerRule, this));

    @Before
    public void setUpDatasourceAddDatabaseToggleTest() {
        page.go();

        if (!page.isRendered()) {
            TestCase.fail("Could not render add mySqlDatasource page");
        }
    }

    @Test
    public void test() {
        assertThat(page.isDatabaseFormFieldVisible(), is(false));
        page.selectDatasourceType(Datasource.Type.POSTGRESQL);
        page.waitForDatabaseFormFieldToBeVisible();
        page.selectDatasourceType(Datasource.Type.MYSQL);
        page.waitForDatabaseFormFieldToBeInvisible();
    }

    @Override
    public String getBaseUrl() {
        return ninjaServerRule.getServerUrl();
    }
}
