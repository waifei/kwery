package controllers.util;

import models.Datasource;

/**
 * Holds messages present in messages.properties file, this is done to ease testing. All messages present here
 * should have an equivalent entry in messages.properties file.
 *
 * Convention for key name is to convert the corresponding key in messages.properties to upper case, replace dots
 * with hyphens and append _M.
 */
public class Messages {
    public static final String TITLE_M = "RepoDash - Create reports, dashboards and visualizations from datasources in a jiffy";
    public static final String INSTALLATION_WELCOME_M = "Welcome to RepoDash, reporting, dashboarding and visualistion made easy. As the first step, let us create an administrative user to manage the application.";
    public static final String CREATE_ADMIN_USER_M = "Create Admin User";
    public static final String USER_NAME_M = "User Name";
    public static final String PASSWORD_M = "Password";
    public static final String CREATE_M = "create";
    public static final String ADMIN_USER_ADDITION_SUCCESS_M = "Admin user with user name {0} created successfully. Please login with the newly created user name and password to complete onboarding.";
    public static final String ADMIN_USER_ADDITION_NEXT_STEP_M = "Login";
    public static final String ADMIN_USER_ADDITION_FAILURE_M = "An admin user with user name {0} already exists, please choose a different username";
    public static final String DATASOURCE_ADDITION_SUCCESS_M = "{0} datasource with label {1} created successfully";
    public static final String DATASOURCE_ADDITION_FAILURE_M = "A {0} datasource with label {1} already exists, please choose a different label";
    public static final String USER_NOT_LOGGED_IN_M = "You need to login to perform this action";
    public static final String LOGIN_SUCCESS_M = "{0} logged in successfully";
    public static final String LOGIN_FAILURE_M = "Please check your username and/or password";
    public static final String LOGIN_M = "Login";
    public static final String USERNAME_VALIDATION_M = "Username should be at least of one character";
    public static final String PASSWORD_VALIDATION_M = "Password should be at least of one character";
    public static final String URL_VALIDATION_M = "url should be at least of one character";
    public static final String LABEL_VALIDATION_M = "label should be at least of one character";

}