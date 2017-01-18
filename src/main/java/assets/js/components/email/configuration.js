define(["knockout", "jquery", "text!components/email/configuration.html", "ajaxutil", "waitingmodal", "validator"], function (ko, $, template, ajaxUtil, waitingModal) {
    function viewModel(params) {
        var self = this;

        self.status = ko.observable("");
        self.messages = ko.observableArray([]);

        self.smtpConfigurationId = ko.observable();
        self.host = ko.observable();
        self.port = ko.observable();
        self.ssl = ko.observable();
        self.username = ko.observable();
        self.password = ko.observable();

        self.emailConfigurationId = ko.observable();
        self.from = ko.observable();
        self.bcc = ko.observable();
        self.replyTo = ko.observable();

        self.smtpConfigurationPresent = ko.observable(false);
        self.emailConfigurationPresent = ko.observable(false);

        self.toEmail = ko.observable();

        waitingModal.show();

        $.when(
            $.ajax("/api/mail/smtp-configuration", {
                type: "GET",
                contentType: "application/json",
                success: function(conf) {
                    if (conf != null) {
                        self.smtpConfigurationId(conf.id);
                        self.host(conf.host);
                        self.port(conf.port);
                        self.ssl(conf.ssl.toString());
                        self.username(conf.username);
                        self.password(conf.password);

                        self.smtpConfigurationPresent(true);
                    }
                }
            }),
            $.ajax("/api/mail/email-configuration", {
                type: "GET",
                contentType: "application/json",
                success: function(conf) {
                    if (conf != null) {
                        self.emailConfigurationId(conf.id);
                        self.from(conf.from);
                        self.bcc(conf.bcc);
                        self.replyTo(conf.replyTo);

                        self.emailConfigurationPresent(true);
                    }
                }
            })
        ).always(function(){
            waitingModal.hide();
        });

        $("#saveEmailConfigurationForm").validator({disable: false}).on("submit", function (e) {
            if (e.isDefaultPrevented()) {
                // handle the invalid form...
            } else {
                var conf = {
                    id: self.emailConfigurationId(),
                    from: self.from(),
                    bcc: self.bcc(),
                    replyTo: self.replyTo()
                };

                ajaxUtil.waitingAjax({
                    "url": "/api/mail/save-email-configuration",
                    type: "POST",
                    data: ko.toJSON(conf),
                    contentType: "application/json",
                    beforeSend: function(){
                        waitingModal.show(ko.i18n("progress.indicator.msg.saving"));
                    },
                    success: function(result) {
                        self.status(result.status);
                        self.messages(result.messages);
                        self.emailConfigurationPresent(true);
                    }
                })
            }

            return false;
        });

        $("#saveSmtpConfigurationForm").validator({disable: false}).on("submit", function (e) {
            if (e.isDefaultPrevented()) {
                // handle the invalid form...
            } else {
                var smtpConfiguration = {
                    id: self.smtpConfigurationId(),
                    host: self.host(),
                    port: self.port(),
                    ssl: self.ssl() === "true",
                    username: self.username(),
                    password: self.password()
                };

                ajaxUtil.waitingAjax({
                    url: "/api/mail/save-smtp-configuration",
                    type: "POST",
                    data: ko.toJSON(smtpConfiguration),
                    contentType: "application/json",
                    success: function(result) {
                        self.status(result.status);
                        self.messages(result.messages);
                        self.smtpConfigurationPresent(true);
                    }
                });
            }

            return false;
        });

        $("#testEmailForm").validator({disable: false}).on("submit", function (e) {
            if (e.isDefaultPrevented()) {
                // handle the invalid form...
            } else {
                ajaxUtil.waitingAjax({
                    url: "/api/mail/" + self.toEmail() + "/email-configuration-test",
                    type: "POST",
                    contentType: "application/json",
                    success: function(result) {
                        self.status(result.status);
                        self.messages(result.messages);
                    }
                });
            }

            return false;
        });

        self.configurationsPresent = ko.computed(function(){
            return self.smtpConfigurationPresent() && self.emailConfigurationPresent();
        }, self);

        return self;
    }
    return { viewModel: viewModel, template: template };
});
