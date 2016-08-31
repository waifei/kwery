package controllers.apis.integration.datasourceapicontroller;

import com.xebialabs.overcast.host.CloudHost;
import com.xebialabs.overcast.host.CloudHostFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import views.ActionResult;

import java.io.IOException;
import java.text.MessageFormat;

import static controllers.util.Messages.DATASOURCE_ADDITION_SUCCESS_M;
import static controllers.util.TestUtil.waitForMysql;
import static models.Datasource.Type.MYSQL;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class AddDatasourceSuccessTest extends DatasoureApiControllerTest {
    protected CloudHost cloudHost;

    @Before
    public void addDatasourceSuccessTestSetup() {
        cloudHost = CloudHostFactory.getCloudHost("mysql");
        cloudHost.setup();

        String host = cloudHost.getHostName();
        datasource.setUrl(host);

        int port = cloudHost.getPort(datasource.getPort());
        datasource.setPort(port);

        if (!waitForMysql(host, port)) {
            fail("Could not bring up docker MySQL service");
        }
    }

    @Test
    public void test() throws IOException {
        ActionResult successResult = actionResult(ninjaTestBrowser.postJson(addDatasourceApi, datasource));
        String successMessage = MessageFormat.format(DATASOURCE_ADDITION_SUCCESS_M, MYSQL, datasource.getLabel());
        assertSuccess(successResult, successMessage);

        assertThat(datasourceDao.getByLabel(datasource.getLabel()), notNullValue());
    }

    @After
    public void addDatasourceSuccessTestTearDown() {
        cloudHost.teardown();
    }
}
