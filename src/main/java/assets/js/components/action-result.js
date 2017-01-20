define(["knockout", "jquery", "text!components/action-result.html"], function (ko, $, template) {
    function viewModel(params) {
        var self = this;
        self.status = params.status;
        self.messages = params.messages;

        self.showSuccess = ko.computed(function(){
            return self.status() === "success";
        }, self);

        self.showFailure = ko.computed(function(){
            return self.status() === "failure";
        }, self);

        return self;
    }
    return { viewModel: viewModel, template: template };
});
