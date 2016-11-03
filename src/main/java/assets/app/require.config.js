var require = {
    baseUrl: "/assets/js",
    paths: {
        "crossroads": "crossroads",
        "jquery": "jquery",
        "jquery-migrate": "jquery-migrate",
        "knockout": "knockout",
        "knockout-projections": "knockout-projections",
        "signals": "signals",
        "hasher": "hasher",
        "text": "requirejs-text",
        "jquery-ui": "jquery-ui",
        "knockout-jqueryui": "knockout-jqueryui",
        "repo-dash": "repo-dash",
        "router": "router",
        "polyglot": "polyglot",
        "jquery-validate": "jquery.validate",
        "bootstrap": "/assets/bootstrap/js/bootstrap"
    },
    shim: {
        "jquery-migrate": {
            deps: ["jquery"],
            exports: "jquery"
        },
        "jquery-validate": {
            deps: ["jquery"]
        },
        "bootstrap": {
            deps: ["jquery"]
        }
    }
};
