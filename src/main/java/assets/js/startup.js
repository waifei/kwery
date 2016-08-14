define(["jquery", "knockout", "./router", "knockout-projections"], function ($, ko, router) {
    ko.components.register("greeter", {
        require: "components/greeter/greeting"
    });
    ko.components.register("onboardingWelcome", {
        template: { require: "text!/onboarding/welcome" }
    });
    ko.components.register("settings", {
        template: { require: "text!components/settings/settings.html" }
    });
    ko.components.register("onboardingCreateAdminUser", {
        require: "/onboarding/create-admin-user"
    });
    ko.applyBindings({ route: router.currentRoute });
});
