define(["jquery", "knockout", "crossroads", "hasher", "repo-dash"], function ($, ko, crossroads, hasher, repoDash) {
    return new Router(repoDash.componentMapping.mapping());

    function Router(componentMapping) {
        var currentRoute = this.currentRoute = ko.observable({});

        ko.utils.arrayForEach(componentMapping, function (mapping) {
            var addedRoute = crossroads.addRoute(mapping.url, function (requestParams) {
                currentRoute(ko.utils.extend(requestParams, {page: mapping.component}));
            });

            addedRoute.rules = {
                request_ : function(request) {
                    if (mapping.auth && !repoDash.user.isAuthenticated()) {
                        return false;
                    }
                    return true;
                }
            };
        });

        crossroads.routed.add(console.log, console);

        //If a route is bypassed, it means that it did not meet the auth rules, hence redirect to auth
        //Previous is appended so that post login intended page is shown
        crossroads.bypassed.add(function(request){
            crossroads.parse("user/login?previous=" + request);
        });

        activateCrossroads();
    }

    function activateCrossroads() {
        function parseHash(newHash, oldHash){
            crossroads.parse(newHash);
        }
        crossroads.normalizeFn = crossroads.NORM_AS_OBJECT;

        hasher.initialized.add(parseHash);
        hasher.changed.add(parseHash);
        hasher.init();
    }
});
