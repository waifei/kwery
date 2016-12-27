package com.kwery.tests.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwery.dtos.JobDto;
import com.kwery.dtos.SqlQueryDto;
import com.kwery.models.*;
import com.kwery.models.SqlQueryExecutionModel.Status;
import com.kwery.tests.fluentlenium.utils.DbUtil;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import org.apache.commons.lang3.RandomStringUtils;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.HashSet;

import static com.kwery.models.EmailConfiguration.*;
import static com.kwery.models.SmtpConfiguration.*;
import static com.kwery.models.SqlQueryExecutionModel.Status.SUCCESS;
import static com.kwery.tests.fluentlenium.utils.DbUtil.getDatasource;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;

public class TestUtil {
    public static final int TIMEOUT_SECONDS = 30;

    //Corresponds to the starting id set in *sql file
    public static final int DB_START_ID = 100;

    public static User user() {
        User user = new User();
        user.setUsername("purvi");
        user.setPassword("password");
        return user;
    }

    public static Datasource datasource() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        podamFactory.getStrategy().addOrReplaceTypeManufacturer(Integer.class, new CustomIdManufacturer());
        return podamFactory.manufacturePojo(Datasource.class);
    }

    public static JobExecutionModel jobExecutionModel() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        podamFactory.getStrategy().addOrReplaceTypeManufacturer(Integer.class, new CustomIdManufacturer());
        JobExecutionModel jobExecutionModel = podamFactory.manufacturePojo(JobExecutionModel.class);
        jobExecutionModel.setSqlQueryExecutionModels(new HashSet<>());
        jobExecutionModel.setJobModel(null);

        return jobExecutionModel;
    }

    public static JobExecutionModel jobExecutionModelWithoutId() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        JobExecutionModel jobExecutionModel = podamFactory.manufacturePojo(JobExecutionModel.class);
        jobExecutionModel.setId(null);
        jobExecutionModel.setSqlQueryExecutionModels(new HashSet<>());
        jobExecutionModel.setJobModel(null);
        return jobExecutionModel;
    }

    public static SqlQueryModel queryRun() {
        SqlQueryModel q = new SqlQueryModel();
        q.setQuery("select * from foo");
        q.setLabel("test query run");
        return q;
    }

    public static SqlQueryModel sleepSqlQuery(Datasource datasource) {
        SqlQueryModel sqlQuery = new SqlQueryModel();
        sqlQuery.setDatasource(datasource);
        sqlQuery.setLabel("test");
        sqlQuery.setQuery("select sleep(86440)");
        return sqlQuery;
    }

    public static SqlQueryDto queryRunDto() {
        SqlQueryDto dto = new SqlQueryDto();
        dto.setQuery("select * from foo");
        dto.setLabel("test");
        return dto;
    }

    public static SqlQueryExecutionModel queryRunExecution() {
        return queryRunExecution(SUCCESS);
    }

    public static SqlQueryExecutionModel queryRunExecution(Status status) {
        SqlQueryExecutionModel e = new SqlQueryExecutionModel();
        e.setExecutionStart(1l);
        e.setExecutionEnd(2l);
        e.setResult("result");
        e.setStatus(status);
        e.setExecutionId("ksjdfjld");
        return e;
    }

    public static SmtpConfiguration smtpConfigurationWithoutId() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        SmtpConfiguration config = podamFactory.manufacturePojo(SmtpConfiguration.class);
        config.setId(null);
        return config;
    }

    public static SmtpConfiguration smtpConfiguration() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        SmtpConfiguration config = podamFactory.manufacturePojo(SmtpConfiguration.class);
        config.setId(1);
        return config;
    }

    public static EmailConfiguration emailConfigurationWithoutId() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        EmailConfiguration emailConfiguration = podamFactory.manufacturePojo(EmailConfiguration.class);
        emailConfiguration.setId(null);
        return emailConfiguration;
    }

    public static EmailConfiguration emailConfiguration() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        EmailConfiguration emailConfiguration = podamFactory.manufacturePojo(EmailConfiguration.class);
        emailConfiguration.setId(1);
        return emailConfiguration;
    }

    public static Datasource datasourceWithoutId() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        Datasource datasource = podamFactory.manufacturePojo(Datasource.class);
        datasource.setId(null);
        return datasource;
    }

    public static SqlQueryModel sqlQueryModelWithoutId() {
        SqlQueryModel sqlQueryModel = sqlQueryModel();
        sqlQueryModel.setId(null);
        return sqlQueryModel;
    }

    public static SqlQueryModel sqlQueryModelWithoutId(Datasource datasource) {
        SqlQueryModel sqlQueryModel = sqlQueryModelWithoutId();
        sqlQueryModel.setDatasource(datasource);
        return sqlQueryModel;
    }

    public static SqlQueryModel sqlQueryModel() {
        SqlQueryModel sqlQueryModel = new SqlQueryModel();
        sqlQueryModel.setId(DbUtil.dbId());
        sqlQueryModel.setQuery(RandomStringUtils.randomAlphanumeric(1, 1025));
        sqlQueryModel.setLabel(RandomStringUtils.randomAlphanumeric(1, 256));
        sqlQueryModel.setTitle(RandomStringUtils.randomAlphanumeric(1, 1025));
        return sqlQueryModel;
    }

    public static SqlQueryModel sqlQueryModel(Datasource datasource) {
        SqlQueryModel sqlQueryModel = sqlQueryModel();
        sqlQueryModel.setDatasource(datasource);
        return sqlQueryModel;
    }

    public static JobModel jobModelWithoutDependents() {
        return jobModel();
    }

    public static JobModel jobModelWithoutIdWithoutDependents() {
        JobModel jobModel = jobModel();
        jobModel.setId(null);
        return jobModel;
    }

    private static JobModel jobModel() {
        JobModel jobModel = new JobModel();
        jobModel.setId(DbUtil.dbId());
        jobModel.setCronExpression("* * * * *");
        jobModel.setLabel(RandomStringUtils.randomAlphanumeric(1, 256));
        jobModel.setTitle(RandomStringUtils.randomAlphanumeric(1, 1024));
        jobModel.setDependentJobs(new HashSet<>());
        jobModel.setEmails(new HashSet<>());
        jobModel.setSqlQueries(new HashSet<>());
        jobModel.setDependsOnJob(null);
        return jobModel;
    }

    public static SqlQueryExecutionModel sqlQueryExecutionModelWithoutId() {
        SqlQueryExecutionModel model = new PodamFactoryImpl().manufacturePojo(SqlQueryExecutionModel.class);
        model.setId(null);
        model.setSqlQuery(null);
        model.setJobExecutionModel(null);
        return model;
    }

    public static SqlQueryExecutionModel sqlQueryExecutionModel() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        podamFactory.getStrategy().addOrReplaceTypeManufacturer(Integer.class, new CustomIdManufacturer());
        SqlQueryExecutionModel model = podamFactory.manufacturePojo(SqlQueryExecutionModel.class);
        model.setSqlQuery(null);
        model.setJobExecutionModel(null);
        return model;
    }

    public static JobDto jobDtoWithoutId() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        JobDto jobDto = podamFactory.manufacturePojo(JobDto.class);
        jobDto.setId(0);
        jobDto.setEmails(new HashSet<>());
        jobDto.setSqlQueries(new ArrayList<>());
        return jobDto;
    }

    public static JobDto jobDto() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        podamFactory.getStrategy().addOrReplaceTypeManufacturer(Integer.class, new CustomIdManufacturer());
        JobDto jobDto = podamFactory.manufacturePojo(JobDto.class);
        jobDto.setEmails(new HashSet<>());
        jobDto.setSqlQueries(new ArrayList<>());
        return jobDto;
    }

    public static SqlQueryDto sqlQueryDtoWithoutId() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        SqlQueryDto sqlQueryDto = podamFactory.manufacturePojo(SqlQueryDto.class);
        sqlQueryDto.setId(0);
        return sqlQueryDto;
    }

    public static SqlQueryDto sqlQueryDto() {
        PodamFactory podamFactory = new PodamFactoryImpl();
        podamFactory.getStrategy().addOrReplaceTypeManufacturer(Integer.class, new CustomIdManufacturer());
        return podamFactory.manufacturePojo(SqlQueryDto.class);
    }

    public static EmailConfiguration emailConfigurationDbSetUp() {
        EmailConfiguration emailConfiguration = emailConfiguration();

        new DbSetup(
                new DataSourceDestination(getDatasource()),
                sequenceOf(
                        insertInto(TABLE_EMAIL_CONFIGURATION)
                                .row()
                                .column(EmailConfiguration.COLUMN_ID, emailConfiguration.getId())
                                .column(COLUMN_FROM_EMAIL, emailConfiguration.getFrom())
                                .column(COLUMN_BCC, emailConfiguration.getBcc())
                                .column(COLUMN_REPLY_TO, emailConfiguration.getReplyTo())
                                .end()
                                .build()
                )
        ).launch();

        return emailConfiguration;
    }

    public static SmtpConfiguration smtpConfigurationDbSetUp() {
        SmtpConfiguration smtpConfiguration = smtpConfiguration();

        new DbSetup(
                new DataSourceDestination(getDatasource()),
                sequenceOf(
                        insertInto(TABLE_SMTP_CONFIGURATION)
                                .row()
                                .column(SmtpConfiguration.COLUMN_ID, smtpConfiguration.getId())
                                .column(COLUMN_HOST, smtpConfiguration.getHost())
                                .column(COLUMN_PORT, smtpConfiguration.getPort())
                                .column(COLUMN_SSL, smtpConfiguration.isSsl())
                                .column(COLUMN_USERNAME, smtpConfiguration.getUsername())
                                .column(COLUMN_PASSWORD, smtpConfiguration.getPassword())
                                .end()
                                .build()
                )
        ).launch();

        return smtpConfiguration;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; ++i) {
            System.out.println(jobExecutionModel());
        }
    }

    public static String toJson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
        }
        return "";
    }
}
