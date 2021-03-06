define(["knockout", "jquery", "text!components/datasource/add.html", "ajaxutil", "waitingmodal", "jstorage", "validator"], function (ko, $, template, ajaxUtil, waitingModal) {
    function ViewModel(params) {
        var self = this;

        self.status = ko.observable("");
        self.messages = ko.observableArray([]);

        var isUpdate = false;

        if (params !== undefined) {
            //Is this onboarding flow?
            if (params["?q"] !== undefined) {
                self.onboarding = params["?q"].onboarding;
                if (self.onboarding !== undefined) {
                    self.status("info");
                    self.messages([ko.i18n("onboarding.datasource.add")]);
                }
            }

            isUpdate = params.datasourceId !== undefined;
        }

        self.username = ko.observable();
        self.password = ko.observable("");
        self.url = ko.observable();
        self.database = ko.observable();
        self.port = ko.observable();
        self.label = ko.observable();
        self.datasourceType = ko.observable("");

        self.showDatabase = ko.observable(false);

        self.datasourceType.subscribe(function(val){
            if (val === 'POSTGRESQL' || val === 'REDSHIFT' || val === 'SQLSERVER') {
                self.showDatabase(true);
                $("#database").attr("data-validate", true);
            } else {
                self.showDatabase(false);
                $("#database").attr("data-validate", false);
            }
            self.refreshValidation();
        });

        var DatasourceType = function(label, value) {
            this.label = label;
            this.value = value;
        };

        //TODO - Should be picked from the backend instead of being hardcoded here
        self.datasourceTypes = ko.observableArray([
            new DatasourceType(ko.i18n("datasource.add.type.select"), ""),
            new DatasourceType("MYSQL", "MYSQL"),
            new DatasourceType("POSTGRESQL", "POSTGRESQL"),
            new DatasourceType("REDSHIFT", "REDSHIFT"),
            new DatasourceType("SQLSERVER", "SQLSERVER")
        ]);


        $("#addDatasourceForm").validator({
            disable: false
        }).on("submit", function(e){
            if (!e.isDefaultPrevented()) {
                var datasource = {
                    url: self.url(),
                    port: Number(self.port()),
                    username: self.username(),
                    password: self.password(),
                    label: self.label(),
                    type: self.datasourceType(),
                    database: self.database()
                };

                if (isUpdate) {
                    datasource.id = params.datasourceId;
                }

                ajaxUtil.waitingAjax({
                    url: "/api/datasource/add-datasource",
                    data: ko.toJSON(datasource),
                    type: "POST",
                    contentType: "application/json",
                    success: function(response){
                      self.addDatasourceSuccessCb(response, self);
                    }
                }, "addDatasource");
            }

            return false;
        });

        self.refreshValidation = function() {
            $("#addDatasourceForm").validator("update");
        };

        if (isUpdate) {
            self.populateForm(params.datasourceId);
        }

        return self;
    }

    ViewModel.prototype.populateForm = function(datasourceId) {
        var self = this;
        ajaxUtil.waitingAjax({
            url: "/api/datasource/" + datasourceId,
            type: "GET",
            contentType: "application/json",
            success: function(datasource) {
                self.username(datasource.username);
                self.password(datasource.password);
                self.url(datasource.url);
                self.port(datasource.port);
                self.label(datasource.label);
                self.datasourceType(datasource.type);
                self.database(datasource.database);
            }
        }, "getDatasource");
    };

    ViewModel.prototype.addDatasourceSuccessCb = function(result, ref) {
        if (result.status === "success") {
            $.ajax({
                before: function(){
                    waitingModal.show(undefined, "addDatasource");
                },
                url: "/api/onboarding/next-action",
                type: "GET",
                contentType: "application/json",
                success: function(response){
                  ref.nextActionCb(response, result);
                }
            });
        } else {
            ref.status(result.status);
            ref.messages(result.messages);
        }
    };

    ViewModel.prototype.nextActionCb = function(response, addResponse) {
        waitingModal.hide("addDatasource");
        switch (response.action) {
            case "ADD_JOB":
                window.location.href = "/#report/add?onboarding=true&fromDatasource=true";
                break;
            case "SHOW_HOME_SCREEN":
                if ($.jStorage.storageAvailable()) {
                    $.jStorage.set("ds:status", addResponse.status, {TTL: (10 * 60 * 1000)});
                    $.jStorage.set("ds:messages", addResponse.messages, {TTL: (10 * 60 * 1000)});
                    window.location.href = "#datasource/list";
                } else {
                    throw new Error("Not enough space available to store result in browser");
                }
        }
    };

    return { viewModel: ViewModel, template: template };
});
