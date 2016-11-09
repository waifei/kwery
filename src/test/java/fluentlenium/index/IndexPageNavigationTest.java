package fluentlenium.index;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IndexPageNavigationTest extends IndexPageTest {
    @Test
    public void test() {
        assertThat(page.getNextActionButton().isDisplayed(), is(true));
        assertThat(page.getNextActionButton().getText(), is(page.expectedNextActionButtonText()));
        page.clickNextActionButton();
        assertThat(url(), is(page.getBaseUrl() + "/#user/add"));
    }
}
