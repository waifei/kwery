package com.kwery.tests.dao.jobexecutiondao;

import com.google.common.collect.ImmutableList;
import com.kwery.dao.JobExecutionDao;
import com.kwery.models.JobExecutionModel;
import com.kwery.models.JobModel;
import com.kwery.services.job.JobExecutionSearchFilter;
import com.kwery.tests.util.RepoDashDaoTestBase;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kwery.models.JobExecutionModel.*;
import static com.kwery.models.JobExecutionModel.Status.*;
import static com.kwery.tests.fluentlenium.utils.DbUtil.getDatasource;
import static com.kwery.tests.fluentlenium.utils.DbUtil.jobDbSetUp;
import static com.kwery.tests.util.TestUtil.jobModelWithoutDependents;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;
import static org.exparity.hamcrest.BeanMatchers.theSameBeanAs;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JobExecutionDaoTestFilter extends RepoDashDaoTestBase {
    protected JobExecutionDao jobExecutionDao;
    protected JobModel job0;
    protected JobModel job1;

    @Before
    public void setUpJobExecutionDaoTestFilter() {
        job0 = jobModelWithoutDependents();
        job1 = jobModelWithoutDependents();
        jobDbSetUp(ImmutableList.of(job0, job1));

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(getDatasource()),
                sequenceOf(
                        insertInto(JobExecutionModel.TABLE)
                                .columns(JobExecutionModel.COLUMN_ID, COLUMN_EXECUTION_END, COLUMN_EXECUTION_ID, COLUMN_EXECUTION_START, COLUMN_STATUS, JOB_ID_FK_COLUMN)
                                .values(1, 1475159940797l, "executionId", 1475158740747l, SUCCESS, job0.getId()) //Thu Sep 29 19:49:00 IST 2016  - Thu Sep 29 20:09:00 IST 2016
                                .values(2, 1475159940797l, UUID.randomUUID().toString(), 1475158740747l, SUCCESS, job1.getId()) //Thu Sep 29 19:49:00 IST 2016  - Thu Sep 29 20:09:00 IST 2016
                                .values(3, 1475159940797l, UUID.randomUUID().toString(), 1475158740747l, FAILURE, job0.getId()) //Thu Sep 29 19:49:00 IST 2016  - Thu Sep 29 20:09:00 IST 2016
                                .values(4, 1475159940797l, UUID.randomUUID().toString(), 1475158740747l, FAILURE, job1.getId()) //Thu Sep 29 19:49:00 IST 2016  - Thu Sep 29 20:09:00 IST 2016
                                .values(5, 1475159940797l, UUID.randomUUID().toString(), 1475158740747l, KILLED, job0.getId()) //Thu Sep 29 19:49:00 IST 2016  - Thu Sep 29 20:09:00 IST 2016
                                .values(6, 1475159940797l, UUID.randomUUID().toString(), 1475158740747l, KILLED, job1.getId()) //Thu Sep 29 19:49:00 IST 2016  - Thu Sep 29 20:09:00 IST 2016
                                .values(7, null, UUID.randomUUID().toString(), 1475158740747l, ONGOING, job0.getId())
                                .values(8, null, UUID.randomUUID().toString(), 1475158740747l, ONGOING, job1.getId())

                                .values(9, 1475246507724l, UUID.randomUUID().toString(), 1475245307680l, SUCCESS, job0.getId()) //Fri Sep 30 19:51:47 IST 2016  - Fri Sep 30 20:11:47 IST 2016
                                .values(10, 1475246507724l, UUID.randomUUID().toString(), 1475245307680l, SUCCESS, job1.getId()) //Fri Sep 30 19:51:47 IST 2016  - Fri Sep 30 20:11:47 IST 2016

                                .values(11, 1475246507724l, UUID.randomUUID().toString(), 1475245307680l, FAILURE, job0.getId()) //Fri Sep 30 19:51:47 IST 2016  - Fri Sep 30 20:11:47 IST 2016
                                .values(12, 1475246507724l, UUID.randomUUID().toString(), 1475245307680l, FAILURE, job1.getId()) //Fri Sep 30 19:51:47 IST 2016  - Fri Sep 30 20:11:47 IST 2016
                                .values(13, 1475246507724l, UUID.randomUUID().toString(), 1475245307680l, KILLED, job0.getId()) //Fri Sep 30 19:51:47 IST 2016  - Fri Sep 30 20:11:47 IST 2016
                                .values(14, 1475246507724l, UUID.randomUUID().toString(), 1475245307680l, KILLED, job1.getId()) //Fri Sep 30 19:51:47 IST 2016  - Fri Sep 30 20:11:47 IST 2016
                                .values(15, null, UUID.randomUUID().toString(), 1475245307680l, ONGOING, job0.getId()) //Fri Sep 30 19:51:47 IST 2016
                                .values(16, null, UUID.randomUUID().toString(), 1475245307680l, ONGOING, job1.getId()) //Fri Sep 30 19:51:47 IST 2016

                                .values(17, 1475333507680l, UUID.randomUUID().toString(), 1475331707680l, SUCCESS, job0.getId()) //Sat Oct 01 19:51:47 IST 2016 - Sat Oct 01 20:21:47 IST 2016
                                .values(18, 1475333507680l, UUID.randomUUID().toString(), 1475331707680l, SUCCESS, job1.getId()) //Sat Oct 01 19:51:47 IST 2016 - Sat Oct 01 20:21:47 IST 2016
                                .values(19, 1475333507680l, UUID.randomUUID().toString(), 1475331707680l, FAILURE, job0.getId()) //Sat Oct 01 19:51:47 IST 2016 - Sat Oct 01 20:21:47 IST 2016
                                .values(20, 1475333507680l, UUID.randomUUID().toString(), 1475331707680l, FAILURE, job1.getId()) //Sat Oct 01 19:51:47 IST 2016 - Sat Oct 01 20:21:47 IST 2016
                                .values(21, 1475333507680l, UUID.randomUUID().toString(), 1475331707680l, KILLED, job0.getId()) //Sat Oct 01 19:51:47 IST 2016 - Sat Oct 01 20:21:47 IST 2016
                                .values(22, 1475333507680l, UUID.randomUUID().toString(), 1475331707680l, KILLED, job1.getId()) //Sat Oct 01 19:51:47 IST 2016 - Sat Oct 01 20:21:47 IST 2016
                                .values(23, null, UUID.randomUUID().toString(), 1475331707680l, ONGOING, job0.getId()) //Sat Oct 01 19:51:47 IST 2016
                                .values(24, null, UUID.randomUUID().toString(), 1475331707680l, ONGOING, job1.getId()) //Sat Oct 01 19:51:47 IST 2016

                                .values(25, 1475419925130l, UUID.randomUUID().toString(), 1475418725084l, SUCCESS, job0.getId()) //Sun Oct 02 20:02:05 IST 2016 - Sun Oct 02 20:22:05 IST 2016
                                .values(26, 1475419925130l, UUID.randomUUID().toString(), 1475418725084l, SUCCESS, job1.getId()) //Sun Oct 02 20:02:05 IST 2016 - Sun Oct 02 20:22:05 IST 2016
                                .values(27, 1475419925130l, UUID.randomUUID().toString(), 1475418725084l, FAILURE, job0.getId()) //Sun Oct 02 20:02:05 IST 2016 - Sun Oct 02 20:22:05 IST 2016
                                .values(28, 1475419925130l, UUID.randomUUID().toString(), 1475418725084l, FAILURE, job1.getId()) //Sun Oct 02 20:02:05 IST 2016 - Sun Oct 02 20:22:05 IST 2016
                                .values(29, 1475419925130l, UUID.randomUUID().toString(), 1475418725084l, KILLED, job0.getId()) //Sun Oct 02 20:02:05 IST 2016 - Sun Oct 02 20:22:05 IST 2016
                                .values(30, 1475419925130l, UUID.randomUUID().toString(), 1475418725084l, KILLED, job1.getId()) //Sun Oct 02 20:02:05 IST 2016 - Sun Oct 02 20:22:05 IST 2016
                                .values(31, null, UUID.randomUUID().toString(), 1475418725084l, ONGOING, job0.getId()) //Sun Oct 02 20:02:05 IST 2016
                                .values(32, null, UUID.randomUUID().toString(), 1475418725084l, ONGOING, job1.getId()) //Sun Oct 02 20:02:05 IST 2016
                                .build()
                )
        );
        dbSetup.launch();

        jobExecutionDao = getInstance(JobExecutionDao.class);
    }

    @Test
    public void testFilterByExecutionId() {
        JobExecutionSearchFilter filter = new JobExecutionSearchFilter();
        filter.setExecutionId("executionId");

        List<JobExecutionModel> jobExecutions = jobExecutionDao.filter(filter);
        assertThat(jobExecutions, hasSize(1));

        JobExecutionModel execution = new JobExecutionModel();
        execution.setJobModel(job0);
        execution.setId(1);
        execution.setExecutionId("executionId");
        execution.setStatus(SUCCESS);
        execution.setExecutionStart(1475158740747l);
        execution.setExecutionEnd(1475159940797l);
        execution.setSqlQueryExecutionModels(new HashSet<>());

        assertThat(jobExecutions, theSameBeanAs(ImmutableList.of(execution)));

        assertThat(jobExecutionDao.count(filter), is(1l));

        assertOrdering(jobExecutions);
    }

    @Test
    public void testFilterByJobId() {
        JobExecutionSearchFilter filter = new JobExecutionSearchFilter();
        filter.setJobId(job0.getId());

        List<JobExecutionModel> executions = jobExecutionDao.filter(filter);
        assertThat(executions, hasSize(16));

        assertThat(jobExecutionDao.count(filter), is(16l));
        assertOrdering(executions);
    }

    @Test
    public void testFilterByJobIdWithPagination() {
        JobExecutionSearchFilter filter = new JobExecutionSearchFilter();
        filter.setJobId(job0.getId());
        filter.setResultCount(2);
        filter.setPageNumber(0);

        List<JobExecutionModel> executions = jobExecutionDao.filter(filter);
        assertOrdering(executions);
        assertThat(executions, hasSize(2));
        assertThat(executions.get(0).getId(), is(31));
        assertThat(executions.get(1).getId(), is(29));

        filter.setPageNumber(1);

        executions = jobExecutionDao.filter(filter);
        assertOrdering(executions);

        assertThat(executions, hasSize(2));
        assertThat(executions.get(0).getId(), is(27));
        assertThat(executions.get(1).getId(), is(25));

        assertThat(jobExecutionDao.count(filter), is(16l));
    }

    @Test
    public void testFilterByStatus() {
        JobExecutionSearchFilter filter = new JobExecutionSearchFilter();
        filter.setStatuses(ImmutableList.of(Status.SUCCESS));
        List<JobExecutionModel> executions = jobExecutionDao.filter(filter);
        assertOrdering(executions);
        assertThat(executions, hasSize(8));

        List<Integer> ids = executions.stream().map(JobExecutionModel::getId).collect(Collectors.toList());
        List<Integer> expectedIds = ImmutableList.of(
                1, 2, 9, 10, 17, 18, 25, 26
        );
        assertThat(ids, containsInAnyOrder(expectedIds.toArray(new Integer[expectedIds.size()])));

        assertThat(jobExecutionDao.count(filter), is(8l));
    }

    @Test
    public void testFilterByStatusWithPagination() {
        JobExecutionSearchFilter filter = new JobExecutionSearchFilter();
        filter.setStatuses(ImmutableList.of(Status.SUCCESS));
        filter.setPageNumber(0);
        filter.setResultCount(1);
        List<JobExecutionModel> executions = jobExecutionDao.filter(filter);
        assertOrdering(executions);
        assertThat(executions, hasSize(1));

        List<Integer> ids = executions.stream().map(JobExecutionModel::getId).collect(Collectors.toList());
        List<Integer> expectedIds = ImmutableList.of(26);
        assertThat(ids, containsInAnyOrder(expectedIds.toArray(new Integer[expectedIds.size()])));

        filter.setStatuses(ImmutableList.of(Status.SUCCESS));
        filter.setPageNumber(1);
        filter.setResultCount(1);
        executions = jobExecutionDao.filter(filter);
        assertOrdering(executions);
        assertThat(executions, hasSize(1));

        ids = executions.stream().map(JobExecutionModel::getId).collect(Collectors.toList());
        expectedIds = ImmutableList.of(25);
        assertThat(ids, containsInAnyOrder(expectedIds.toArray(new Integer[expectedIds.size()])));
        assertThat(executions, hasSize(1));

        assertThat(jobExecutionDao.count(filter), is(8l));
    }

    @Test
    public void testFilterByStatuses() {
        JobExecutionSearchFilter filter = new JobExecutionSearchFilter();
        filter.setStatuses(ImmutableList.of(SUCCESS, FAILURE, KILLED, ONGOING));
        List<JobExecutionModel> executions = jobExecutionDao.filter(filter);
        assertOrdering(executions);
        assertThat(executions, hasSize(32));
        assertThat(executions.stream().map(JobExecutionModel::getId).collect(Collectors.toList()), containsInAnyOrder(IntStream.rangeClosed(1, 32).boxed().collect(Collectors.toList()).toArray(new Integer[executions.size()])));
        assertThat(jobExecutionDao.count(filter), is(32l));
    }

    @Test
    public void testFilterByExecutionStartEndTimeWithPagination() {
        JobExecutionSearchFilter filter = new JobExecutionSearchFilter();
        filter.setExecutionStartStart(1475158740746l); //Thu Sep 29 19:49:00 IST 2016
        filter.setExecutionEndEnd(1475419925131l); //Sun Oct 02 20:22:05 IST 2016
        filter.setPageNumber(0);
        filter.setResultCount(1);
        List<JobExecutionModel> executions = jobExecutionDao.filter(filter);
        assertOrdering(executions);
        assertThat(executions.stream().map(JobExecutionModel::getId).collect(Collectors.toList()), containsInAnyOrder(30));
        assertThat(jobExecutionDao.count(filter), is(24l));
    }

    private void assertOrdering(List<JobExecutionModel> models) {
        if (!models.isEmpty() && models.size() > 1) {
            for (int i = 1; i < models.size(); ++i) {
                assertThat(models.get(i - 1).getExecutionStart(), is(greaterThanOrEqualTo(models.get(i).getExecutionStart())));
            }
        }
    }
}
