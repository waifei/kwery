package controllers.apis.integration.sqlqueryapicontroller;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import controllers.apis.SqlQueryApiController;
import controllers.apis.integration.userapicontroller.AbstractPostLoginApiTest;
import fluentlenium.utils.DbUtil;
import models.Datasource;
import models.SqlQuery;
import ninja.Router;
import org.junit.Before;
import org.junit.Test;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.operation.CompositeOperation.sequenceOf;
import static models.Datasource.COLUMN_ID;
import static models.Datasource.COLUMN_LABEL;
import static models.Datasource.COLUMN_PASSWORD;
import static models.Datasource.COLUMN_PORT;
import static models.Datasource.COLUMN_TYPE;
import static models.Datasource.COLUMN_URL;
import static models.Datasource.COLUMN_USERNAME;
import static models.Datasource.Type.MYSQL;
import static models.SqlQuery.COLUMN_CRON_EXPRESSION;
import static models.SqlQuery.COLUMN_DATASOURCE_ID_FK;
import static models.SqlQuery.COLUMN_QUERY;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ListSqlQueriesTest extends AbstractPostLoginApiTest {
    @Before
    public void setUpListSqlQueriesTest() {
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(DbUtil.getDatasource()),
                sequenceOf(
                        insertInto(Datasource.TABLE)
                                .columns(COLUMN_ID, COLUMN_LABEL, COLUMN_PASSWORD, COLUMN_PORT, COLUMN_TYPE, COLUMN_URL, COLUMN_USERNAME)
                                .values(1, "testDatasource", "password", 3306, MYSQL.name(), "foo.com", "foo").build(),
                        insertInto(SqlQuery.TABLE)
                                .columns(SqlQuery.COLUMN_ID, COLUMN_CRON_EXPRESSION, SqlQuery.COLUMN_LABEL, COLUMN_QUERY, COLUMN_DATASOURCE_ID_FK)
                                .values(1, "*", "testQuery0", "select * from foo", 1)
                                .values(2, "* *", "testQuery1", "select * from foo", 1)
                                .build()
                )
        );
        dbSetup.launch();
    }

    @Test
    public void test() {
        String url = getInjector().getInstance(Router.class).getReverseRoute(SqlQueryApiController.class, "listSqlQueries");

        String jsonResponse = ninjaTestBrowser.makeJsonRequest(getUrl(url));
        Object json = Configuration.defaultConfiguration().jsonProvider().parse(jsonResponse);

        assertThat(json, isJson());
        assertThat(JsonPath.read(json, "$.length()"), is(2));

        assertThat(json, hasJsonPath("$[0].id", is(1)));
        assertThat(json, hasJsonPath("$[1].id", is(2)));

        assertThat(json, hasJsonPath("$[0].cronExpression", is("*")));
        assertThat(json, hasJsonPath("$[1].cronExpression", is("* *")));

        assertThat(json, hasJsonPath("$[0].label", is("testQuery0")));
        assertThat(json, hasJsonPath("$[1].label", is("testQuery1")));

        assertThat(json, hasJsonPath("$[0].datasource.label", is("testDatasource")));
        assertThat(json, hasJsonPath("$[1].datasource.label", is("testDatasource")));
    }
}