package com.kwery.tests.services.job.schedule.dependents;

import com.kwery.models.JobExecutionModel;
import com.kwery.models.SqlQueryExecutionModel;
import org.junit.Test;

import java.util.List;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.waitAtMost;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class JobServiceScheduleJobWithDependentsSqlQueryKilledTest extends JobServiceScheduleJobWithDependentsJobKilledTest {
    @Test
    public void test() {
        jobService.schedule(jobModel.getId());

        waitAtMost(130, SECONDS).until(() ->
                getJobExecutionModels(JobExecutionModel.Status.ONGOING).size() >= 2 && (getSqlQueryExecutionModels(SqlQueryExecutionModel.Status.ONGOING).size() >= 4));

        List<SqlQueryExecutionModel> queryExecutions = getSqlQueryExecutionModels(sqlQueryId0, SqlQueryExecutionModel.Status.ONGOING);

        for (SqlQueryExecutionModel queryExecution : queryExecutions) {
            jobService.stopExecution(queryExecution.getExecutionId());
        }

        waitAtMost(1, MINUTES).until(() -> getSqlQueryExecutionModels(sqlQueryId0, SqlQueryExecutionModel.Status.ONGOING).isEmpty());

        assertThat("Other SQL query is still executing", getSqlQueryExecutionModels(sqlQueryId1, SqlQueryExecutionModel.Status.ONGOING), hasSize(greaterThanOrEqualTo(2)));
        assertThat("Job is still executing", getJobExecutionModels(JobExecutionModel.Status.ONGOING), hasSize(greaterThanOrEqualTo(2)));

        queryExecutions = getSqlQueryExecutionModels(sqlQueryId1, SqlQueryExecutionModel.Status.ONGOING);

        for (SqlQueryExecutionModel queryExecution : queryExecutions) {
            jobService.stopExecution(queryExecution.getExecutionId());
        }

        waitAtMost(1, MINUTES).until(() ->
                getSqlQueryExecutionModels(sqlQueryId1, SqlQueryExecutionModel.Status.KILLED).size() >= 2 && getJobExecutionModels(JobExecutionModel.Status.FAILURE).size() >= 2
        );

        assertThat(getJobExecutionModels(dependentJobModel.getId()), hasSize(0));

        assertEmailDoesNotExists();
    }
}
