package com.kwery.tests.services.job.launch.dependets.failurealertemail;

import com.google.common.collect.ImmutableSet;
import com.kwery.tests.services.job.launch.dependets.JobServiceLaunchJobWithDependentsFailureTest;
import ninja.postoffice.Mail;
import ninja.postoffice.mock.PostofficeMockImpl;
import org.junit.Before;
import org.junit.Test;

import static com.kwery.tests.fluentlenium.utils.DbUtil.jobFailureAlertEmailDbSetUp;
import static com.kwery.tests.util.Messages.REPORT_GENERATION_FAILURE_ALERT_EMAIL_SUBJECT_M;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class JobServiceLaunchWithDependentsJobFailureAlertEmailTest extends JobServiceLaunchJobWithDependentsFailureTest {
    @Before
    public void setUp() {
        this.disableMailTest();
        jobModel.setFailureAlertEmails(ImmutableSet.of("foo@bar.com"));
        jobFailureAlertEmailDbSetUp(jobModel);
    }

    @Test
    public void test() {
        super.test();
        Mail mail = ((PostofficeMockImpl) mailService.getPostoffice()).getLastSentMail();
        assertThat(mail, notNullValue());
        assertThat(mail.getSubject(), containsString(REPORT_GENERATION_FAILURE_ALERT_EMAIL_SUBJECT_M));
    }
}
