package com.kwery.tests.dao.jobdao.save;

import com.google.common.collect.ImmutableList;
import com.kwery.dao.JobDao;
import com.kwery.models.JobModel;
import com.kwery.tests.util.RepoDashDaoTestBase;
import com.kwery.tests.util.TestUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;

import static com.kwery.models.JobModel.JOB_CHILDREN_TABLE;
import static com.kwery.models.JobModel.JOB_TABLE;
import static com.kwery.tests.fluentlenium.utils.DbUtil.*;
import static com.kwery.tests.util.TestUtil.jobModelWithoutDependents;
import static com.kwery.tests.util.TestUtil.jobModelWithoutIdWithoutDependents;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class JobDaoSaveWithDependentsTest extends RepoDashDaoTestBase {
    protected JobDao jobDao;
    protected JobModel jobModel;

    @Before
    public void setUpJobDaoSaveTest() {
        jobModel = jobModelWithoutDependents();
        jobModel.setSqlQueries(new LinkedList<>());

        jobDbSetUp(jobModel);

        jobDao = getInstance(JobDao.class);
    }

    @Test
    public void test() throws Exception {
        JobModel newJobModel = jobModelWithoutIdWithoutDependents();
        TestUtil.nullifyTimestamps(newJobModel);

        newJobModel.setChildJobs(new HashSet<>());
        newJobModel.getChildJobs().add(jobModel);

        long now = System.currentTimeMillis();

        newJobModel = jobDao.save(newJobModel);

        assertDbState(JOB_TABLE, jobTable(ImmutableList.of(jobModel, newJobModel)));
        assertDbState(JOB_CHILDREN_TABLE, jobDependentTable(newJobModel), "id");

        assertThat(newJobModel.getCreated(), greaterThanOrEqualTo(now));
        assertThat(newJobModel.getUpdated(), greaterThanOrEqualTo(now));
    }
}
