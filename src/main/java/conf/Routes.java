package conf;

import controllers.IndexController;
import controllers.apis.DatasourceApiController;
import controllers.apis.SqlQueryApiController;
import controllers.apis.UserApiController;
import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;

public class Routes implements ApplicationRoutes {
    public static final String INDEX = "/";

    public static final String ADD_ADMIN_USER_API = "/api/user/add-admin-user";
    public static final String ADD_DATASOURCE_API = "/api/datasource/add-datasource";
    public static final String MYSQL_DATASOURCE_CONNECTION_TEST_API = "/api/datasource/test-connection";
    public static final String LOGIN_API = "/api/user/login";
    public static final String USER = "/api/user";
    public static final String ADD_SQL_QUERY_API = "/api/sql-query/add";
    public static final String ALL_DATASOURCES_API = "/api/datasource/all";
    public static final String CURRENTLY_EXECUTING_SQL_QUERY_API = "/api/sql-query/currently-executing";

    @Override
    public void init(Router router) {  
        router.GET().route(INDEX).with(IndexController.class, "index");

        //Api - Start
        router.POST().route(ADD_ADMIN_USER_API).with(UserApiController.class, "addAdminUser");
        router.POST().route(LOGIN_API).with(UserApiController.class, "login");
        router.POST().route(ADD_DATASOURCE_API).with(DatasourceApiController.class, "addDatasource");
        router.POST().route(MYSQL_DATASOURCE_CONNECTION_TEST_API).with(DatasourceApiController.class, "testConnection");
        router.GET().route(USER).with(UserApiController.class, "user");
        router.POST().route(ADD_SQL_QUERY_API).with(SqlQueryApiController.class, "addSqlQuery");
        router.GET().route(ALL_DATASOURCES_API).with(DatasourceApiController.class, "allDatasources");
        router.GET().route(CURRENTLY_EXECUTING_SQL_QUERY_API).with(SqlQueryApiController.class, "currentlyExecutingSqlQueries");
        //Api - End

        //Static asset
        router.GET().route("/assets/{fileName: .*}").with(AssetsController.class, "serveStatic");
    }
}
