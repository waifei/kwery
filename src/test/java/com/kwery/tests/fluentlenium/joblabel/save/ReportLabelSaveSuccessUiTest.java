package com.kwery.tests.fluentlenium.joblabel.save;

import org.junit.Test;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class ReportLabelSaveSuccessUiTest extends AbstractReportLabelSaveUiTest {
    @Test
    public void test() {
        String label = "test";
        page.fillAndSubmitForm(label, null);
        page.waitForJobLabelListPage();
        page.waitForLabelSaveSuccessMessage(label);

        assertThat(jobLabelDao.getAllJobLabelModels(), hasSize(1));
        assertThat(jobLabelDao.getJobLabelModelByLabel(label), is(notNullValue()));
    }
}
