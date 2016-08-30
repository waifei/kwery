package controllers;

import controllers.util.ControllerTestUtil;
import ninja.NinjaDocTester;
import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ControllerTest extends NinjaDocTester {
    protected String url;
    protected Map<String, String> postParams;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void assertGetHtml() {
        Response response = makeRequest(
                Request.GET().url(testServerUrl().path(this.url))
        );
        assertThat(ControllerTestUtil.isHtmlResponse(response), is(true));
        assertThat(response.httpStatus, is(200));
    }
}