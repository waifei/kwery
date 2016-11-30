package com.kwery.tests.services;

import com.google.inject.Provider;
import com.kwery.dao.SqlQueryDao;
import com.kwery.dao.SqlQueryExecutionDao;
import com.kwery.models.Datasource;
import com.kwery.models.SqlQueryModel;
import com.kwery.models.SqlQueryExecution;
import com.kwery.services.mail.KweryMail;
import com.kwery.services.mail.KweryMailImpl;
import com.kwery.services.mail.MailService;
import com.kwery.services.scheduler.*;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.kwery.models.SqlQueryExecution.Status.ONGOING;
import static com.kwery.tests.util.TestUtil.datasource;
import static com.kwery.tests.util.TestUtil.queryRun;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlQueryTaskSchedulerTest {
    @Mock
    private Scheduler scheduler;
    @Mock
    private SqlQueryExecutionDao sqlQueryExecutionDao;
    @Mock
    private SqlQueryTaskFactory sqlQueryTaskFactory;
    @Mock
    private Provider<SqlQueryExecution> provider;
    @Mock
    private SqlQueryTaskExecutorListener sqlQueryTaskExecutorListener;
    @Mock
    private TaskExecutor taskExecutor;
    @Mock
    private SqlQueryTask sqlQueryTask;
    @Mock
    private SqlQueryTaskSchedulerHolder sqlQueryTaskSchedulerHolder;
    @Mock
    private OneOffSqlQueryTaskSchedulerReaper oneOffSqlQueryTaskSchedulerReaper;
    @Mock
    private SchedulerService schedulerService;
    @Mock
    private SqlQueryDao sqlQueryDao;
    @Mock
    private MailService mailService;

    private SqlQueryModel sqlQuery;
    private Datasource datasource;
    private SqlQueryExecution sqlQueryExecution;
    private String taskExecutorGuid = "foo";
    private long executorStartTime = 1l;

    @Before
    public void setUpQueryTaskSchedulerTest() {
        sqlQuery = queryRun();
        sqlQuery.setDependentQueries(new LinkedList<>());

        datasource = datasource();
        sqlQuery.setDatasource(datasource);
        sqlQueryExecution = new SqlQueryExecution();

        doNothing().when(scheduler).addSchedulerListener(any());
        when(scheduler.schedule(anyString(), any(Task.class))).thenReturn("");
        doNothing().when(scheduler).start();
        when(taskExecutor.getTask()).thenReturn(sqlQueryTask);
        when(sqlQueryTask.getSqlQuery()).thenReturn(sqlQuery);
        when(provider.get()).thenReturn(sqlQueryExecution);
        when(taskExecutor.getGuid()).thenReturn(taskExecutorGuid);
        when(taskExecutor.getStartTime()).thenReturn(executorStartTime);
        when(sqlQueryExecutionDao.getByExecutionId(taskExecutorGuid)).thenReturn(sqlQueryExecution);
    }

    @Test
    public void testTaskLaunching() {
        List<TaskExecutor> ongoingExecutions = new LinkedList<>();
        SqlQueryTaskScheduler sqlQueryTaskScheduler = new SqlQueryTaskScheduler(
                scheduler,
                sqlQueryExecutionDao,
                sqlQueryTaskFactory,
                provider,
                sqlQueryTaskExecutorListener,
                sqlQueryTaskSchedulerHolder,
                oneOffSqlQueryTaskSchedulerReaper,
                schedulerService,
                sqlQueryDao,
                new JsonToHtmlProvider(),
                mailService,
                new KweryMailProvider(),
                ongoingExecutions,
                sqlQuery
        );

        sqlQueryTaskScheduler.taskLaunching(taskExecutor);

        assertThat(sqlQueryExecution.getExecutionId(), is(taskExecutorGuid));
        assertThat(sqlQueryExecution.getExecutionStart(), is(executorStartTime));
        assertThat(sqlQueryExecution.getSqlQuery(), is(sqlQuery));
        assertThat(sqlQueryExecution.getStatus(), is(ONGOING));
        assertThat(ongoingExecutions, hasSize(1));
    }

    @Test
    public void testTaskSucceeded() {
        List<TaskExecutor> ongoingExecutions = newArrayList(taskExecutor);

        sqlQuery.setDependentQueries(new LinkedList<>());
        sqlQuery.setRecipientEmails(new HashSet<>());

        when(sqlQueryDao.getById(anyInt())).thenReturn(sqlQuery);

        SqlQueryTaskScheduler sqlQueryTaskScheduler = new SqlQueryTaskScheduler(
                scheduler,
                sqlQueryExecutionDao,
                sqlQueryTaskFactory,
                provider,
                sqlQueryTaskExecutorListener,
                sqlQueryTaskSchedulerHolder,
                oneOffSqlQueryTaskSchedulerReaper,
                schedulerService,
                sqlQueryDao,
                new JsonToHtmlProvider(),
                mailService,
                new KweryMailProvider(),
                ongoingExecutions,
                sqlQuery
        );

        sqlQueryTaskScheduler.taskSucceeded(taskExecutor);

        assertThat(ongoingExecutions, hasSize(0));
    }

    @Test
    public void testTaskFailed() {
        List<TaskExecutor> ongoingExecutions = newArrayList(taskExecutor);

        sqlQuery.setDependentQueries(new LinkedList<>());
        when(sqlQueryDao.getById(anyInt())).thenReturn(sqlQuery);

        SqlQueryTaskScheduler sqlQueryTaskScheduler = new SqlQueryTaskScheduler(
                scheduler,
                sqlQueryExecutionDao,
                sqlQueryTaskFactory,
                provider,
                sqlQueryTaskExecutorListener,
                sqlQueryTaskSchedulerHolder,
                oneOffSqlQueryTaskSchedulerReaper,
                schedulerService,
                sqlQueryDao,
                new JsonToHtmlProvider(),
                mailService,
                new KweryMailProvider(),
                ongoingExecutions,
                sqlQuery
        );

        sqlQueryTaskScheduler.taskFailed(taskExecutor, new RuntimeException());
        assertThat(ongoingExecutions, hasSize(0));
    }

    @Test
    public void testOngoingQueryTasks() {
        List<TaskExecutor> ongoingExecutions = newArrayList(taskExecutor);
        SqlQueryTaskScheduler sqlQueryTaskScheduler = new SqlQueryTaskScheduler(
                scheduler,
                sqlQueryExecutionDao,
                sqlQueryTaskFactory,
                provider,
                sqlQueryTaskExecutorListener,
                sqlQueryTaskSchedulerHolder,
                oneOffSqlQueryTaskSchedulerReaper,
                schedulerService,
                sqlQueryDao,
                new JsonToHtmlProvider(),
                mailService,
                new KweryMailProvider(),
                ongoingExecutions,
                sqlQuery
        );

        List<OngoingSqlQueryTask> tasks = sqlQueryTaskScheduler.ongoingQueryTasks();

        assertThat(tasks, hasSize(1));

        OngoingSqlQueryTask ongoingSqlQueryTask = tasks.get(0);

        assertThat(ongoingSqlQueryTask.getExecutionId(), is(taskExecutorGuid));
        assertThat(ongoingSqlQueryTask.getSqlQuery(), is(sqlQuery));
        assertThat(ongoingSqlQueryTask.getStartTime(), is(executorStartTime));
    }

    @Test
    public void testStopExecution() throws SqlQueryExecutionNotFoundException {
        TaskExecutor another = mock(TaskExecutor.class);
        when(another.getGuid()).thenReturn("");

        List<TaskExecutor> ongoingExecutions = newArrayList(taskExecutor, another);

        SqlQueryTaskScheduler sqlQueryTaskScheduler = new SqlQueryTaskScheduler(
                scheduler,
                sqlQueryExecutionDao,
                sqlQueryTaskFactory,
                provider,
                sqlQueryTaskExecutorListener,
                sqlQueryTaskSchedulerHolder,
                oneOffSqlQueryTaskSchedulerReaper,
                schedulerService,
                sqlQueryDao,
                new JsonToHtmlProvider(),
                mailService,
                new KweryMailProvider(),
                ongoingExecutions,
                sqlQuery
        );

        sqlQueryTaskScheduler.stopExecution(taskExecutorGuid);

        verify(taskExecutor, times(1)).stop();
        verify(another, times(0)).stop();

        assertThat(ongoingExecutions, hasSize(2));
    }

    private static class JsonToHtmlProvider implements Provider<JsonToHtmlTable> {
        @Override
        public JsonToHtmlTable get() {
            return new JsonToHtmlTable();
        }
    }

    private static class KweryMailProvider implements Provider<KweryMail> {
        @Override
        public KweryMail get() {
            return new KweryMailImpl();
        }
    }
}
