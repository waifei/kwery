package com.kwery.tests.services.oneoffexecution;

import com.kwery.models.SqlQuery;
import com.kwery.models.SqlQueryExecution;
import com.kwery.services.scheduler.SqlQueryExecutionSearchFilter;
import org.dbunit.DatabaseUnitException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.google.common.base.Strings.nullToEmpty;
import static com.kwery.models.SqlQueryExecution.Status.SUCCESS;
import static com.kwery.tests.services.oneoffexecution.DependentSqlQueriesSetUp.dependentSelectQueryId;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyIterable.emptyIterable;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;


public class SchedulerServiceOneOffExecutionWithDependentsSuccessTest extends SchedulerServiceOneOffExecutionBaseTest {
    @Before
    public void setUpSchedulerServiceOneOffExecutionWithDependentsSuccessTest() {
        new DependentSqlQueriesSetUp().setUp();
    }

    @Test
    public void test() throws InterruptedException, IOException, DatabaseUnitException, SQLException {
        long start = System.currentTimeMillis();

        SqlQuery sqlQuery = sqlQueryDao.getById(successQueryId);
        schedulerService.schedule(sqlQuery);

        SECONDS.sleep(60);

        SqlQueryExecutionSearchFilter filter = new SqlQueryExecutionSearchFilter();
        filter.setSqlQueryId(successQueryId);

        List<SqlQueryExecution> executions = sqlQueryExecutionDao.filter(filter);

        assertThat(executions, hasSize(1));

        SqlQueryExecution sqlQueryExecution = executions.get(0);

        assertThat(sqlQueryExecution.getExecutionStart(), greaterThan(start));
        assertThat(sqlQueryExecution.getExecutionEnd(), greaterThan(start));
        assertThat(sqlQueryExecution.getExecutionId(), notNullValue());
        assertThat(nullToEmpty(sqlQueryExecution.getResult()), not(equalTo("")));
        assertThat(sqlQueryExecution.getStatus(), is(SUCCESS));

        assertThat(sqlQueryTaskSchedulerHolder.all(), emptyIterable());
        assertThat("Reaper holds the scheduled task as well as the dependent one",
                oneOffSqlQueryTaskSchedulerReaper.getSqlQueryTaskSchedulerExecutorPairs(), iterableWithSize(2));
        assertThat(schedulerService.ongoingQueryTasks(successQueryId), emptyIterable());

        SqlQueryExecutionSearchFilter dependentQueryFilter = new SqlQueryExecutionSearchFilter();
        dependentQueryFilter.setSqlQueryId(dependentSelectQueryId);

        List<SqlQueryExecution> dependentQueryExecutions = sqlQueryExecutionDao.filter(dependentQueryFilter);
        assertThat(dependentQueryExecutions, hasSize(1));

        SqlQueryExecution execution = dependentQueryExecutions.get(0);

        assertThat(execution.getStatus(), is(SUCCESS));
        assertThat(execution.getExecutionStart(), greaterThan(start));
        assertThat(execution.getExecutionEnd(), greaterThan(execution.getExecutionStart()));
        assertThat(execution.getExecutionId(), notNullValue());
        assertThat(execution.getResult(), notNullValue());
    }
}