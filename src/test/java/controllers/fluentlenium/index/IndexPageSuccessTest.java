package controllers.fluentlenium.index;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class IndexPageSuccessTest extends IndexPageTest {
    @Test
    public void test() {
        initPage();
        assertThat(page.getHeroText().getText(), is(page.expectedHeroText()));
    }
}
