package com.kwery.controllers;

/**
 * Holds keys present in messages.properties file.
 *
 * Convention for key name is to convert the corresponding key in messages.properties file to upper case and replace
 * the dot with hyphen.
 */
public class MessageKeys {
    public static final String TITLE = "title";
    public static final String ONBOARDING_WELCOME = "onboarding.welcome";
    public static final String CREATE_ADMIN_USER = "create.admin.user";
    public static final String USER_NAME = "user.name";
    public static final String PASSWORD = "password";
    public static final String CREATE = "create";
    public static final String URL = "url";
    public static final String LABEL = "label";
    public static final String ADMIN_USER_ADDITION_SUCCESS = "admin.user.addition.success";
    public static final String USER_UPDATE_SUCCESS = "user.update.success";
    public static final String DATASOURCE_UPDATE_SUCCESS = "datasource.update.success";
    public static final String ADMIN_USER_ADDITION_FAILURE = "admin.user.addition.failure";
    public static final String USER_UPDATE_FAILURE = "user.update.failure";
    public static final String DATASOURCE_ADDITION_SUCCESS = "datasource.addition.success";
    public static final String DATASOURCE_ADDITION_FAILURE = "datasource.addition.failure";
    public static final String DATASOURCE_UPDATE_FAILURE = "datasource.update.failure";
    public static final String ADMIN_USER_ADDITION_NEXT_ACTION = "admin.user.addition.next.action";
    public static final String USER_NOT_LOGGED_IN = "user.not.logged.in";
    public static final String LOGIN_SUCCESS = "login.success";
    public static final String LOGIN_FAILURE = "login.failure";
    public static final String LOGIN = "login";
    public static final String USERNAME_VALIDATION = "username.validation";
    public static final String PASSWORD_VALIDATION = "password.validation";
    public static final String DATASOURCE_CONNECTION_SUCCESS = "datasource.connection.success";
    public static final String DATASOURCE_CONNECTION_FAILURE = "datasource.connection.failure";
    public static final String PORT_VALIDATION = "port.validation";
    public static final String PORT = "Port";
    public static final String QUERY_VALIDATION = "query.validation";
    public static final String CRON_EXPRESSION_VALIDATION = "cron.expression.validation";
    public static final String DATASOURCE_VALIDATION = "datasource.validation";
    public static final String QUERY_RUN_WITH_CRON_ADDITION_SUCCESS = "query.run.with.cron.addition.success";
    public static final String QUERY_RUN_WITHOUT_CRON_ADDITION_SUCCESS = "query.run.without.cron.addition.success";
    public static final String QUERY_RUN_UPDATE_SUCCESS = "query.run.update.success";
    public static final String QUERY_RUN_ADDITION_FAILURE = "query.run.addition.failure";
    public static final String KILL_QUERY = "kill.query";
    public static final String KILL = "kill";
    public static final String KILLING = "killing";
    public static final String KILLED = "killed";
    public static final String KILL_QUERY_NOT_FOUND = "kill.query.not.found";
    public static final String END = "end";
    public static final String STATUS = "status";
    public static final String RESULT = "result";
    public static final String USER_DELETE_SUCCESS = "user.delete.success";
    public static final String USER_DELETE_YOURSELF = "user.delete.yourself";
    public static final String DATASOURCE_DELETE_SQL_QUERIES_PRESENT = "datasource.delete.sql.queries.present";
    public static final String DATASOURCE_DELETE_SUCCESS = "datasource.delete.success";
    public static final String SQL_QUERY_DELETE_SUCCESS = "sql.query.delete.success";
    public static final String ONBOARDING_ROOT_USER_CREATED = "onboarding.root.user.created";
    public static final String ONE_OFF_EXECUTION_SUCCESS_MESSAGE = "one.off.execution.success.message";
    public static final String SMTP_CONFIGURATION_ALREADY_PRESENT = "smtp.configuration.already.present";
    public static final String SMTP_MULTIPLE_CONFIGURATION = "smtp.multiple.configuration";
    public static final String SMTP_CONFIGURATION_ADDED = "smtp.configuration.added";
    public static final String SMTP_CONFIGURATION_UPDATED = "smtp.configuration.updated";
    public static final String EMAIL_CONFIGURATION_SAVED = "email.configuration.saved";
    public static final String EMAIL_TEST_SUBJECT = "email.test.subject";
    public static final String EMAIL_TEST_BODY = "email.test.body";
    public static final String EMAIL_TEST_FAILURE = "email.test.failure";
    public static final String EMAIL_TEST_SUCCESS = "email.test.success";

    public static final String JOBAPICONTROLLER_REPORT_NOT_FOUND = "jobapicontroller.report.not.found";
    public static final String JOBAPICONTROLLER_REPORT_NAME_EXISTS = "jobapicontroller.report.name.exists";
    public static final String JOBAPICONTROLLER_SQL_QUERY_LABEL_EXISTS = "jobapicontroller.sql.query.label.exists";
    public static final String JOBLABELAPICONTROLLER_INVALID_CRON_EXPRESSION = "joblabelapicontroller.invalid.cron.expression";

    public static final String DATASOURCEAPICONTROLLER_CONNECTION_ERROR_SQL_STATE = "datasourceapicontroller.connection.error.sql.state";
    public static final String DATASOURCEAPICONTROLLER_CONNECTION_ERROR_ERROR_CODE = "datasourceapicontroller.connection.error.error.code";

    public static final String JOBAPICONTROLLER_FILTER_DATE_PARSE_ERROR = "jobapicontroller.filter.date.parse.error";
    public static final String JOBAPICONTROLLER_DELETE_JOB_HAS_CHILDREN = "jobapicontroller.delete.job.has.children";

    public static final String JOBLABELAPICONTROLLER_DELETE_HAS_CHILDREN = "joblabelapicontroller.delete.has.children";
    public static final String JOBLABELAPICONTROLLER_DELETE_JOB_HAS_LABEL = "joblabelapicontroller.delete.job.has.label";
    public static final String JOBLABELAPICONTROLLER_DELETE_SUCCESS = "joblabelapicontroller.delete.success";

    public static final String REPORT_GENERATION_FAILURE_ALERT_EMAIL_SUBJECT = "report.generation.failure.alert.email.subject";
    public static final String REPORT_GENERATION_FAILURE_ALERT_EMAIL_BODY = "report.generation.failure.alert.email.body";

    public static final String JOBAPICONTROLLER_REPORT_CONTENT_LARGE_WARNING = "jobapicontroller.report.content.large.warning";
    public static final String REPORTEMAILSENDER_ATTACHMENT_SKIPPED = "reportemailsender.attachment.skipped";

    public static final String USERAPICONTROLLER_SIGN_UP_EMAIL_EXISTS = "userapicontroller.sign.up.email.exists";

    public static final String EMAIL_CONFIGURATION_EMPTY_FROM_EMAIL = "email.configuration.empty.from.email";
    public static final String EMAIL_CONFIGURATION_INVALID_EMAIL = "email.configuration.invalid.email";
}
