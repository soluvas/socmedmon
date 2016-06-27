/// <reference path="../typings/index.d.ts"/>
/// <reference path="services.ts"/>
//import {Page, NavController, NavParams, IonicApp, Content, Alert, Modal, ViewController} from 'ionic-angular';
//import {Http, Headers, URLSearchParams} from 'angular2/http';
//import 'rxjs/Rx';
angular.module('starter.controllers', [])
    .controller('DashboardCtrl', function ($scope, $log, $window, $timeout, $resource) {
    $log.info('Hello!');
    var SiteSummary = $resource('http://localhost:8080/api/siteSummaries/:id');
    SiteSummary.get({ sort: ['watchedSite.name', 'watchedSite.siteScreenName'], size: 100 }, function (resp) {
        var siteSummaries = resp._embedded.siteSummaries;
        $scope.siteSummaries = siteSummaries;
        $log.info('Site summaries:', siteSummaries);
        $scope.updateTime = Date.create(siteSummaries[0].modificationTime);
    });
});

//# sourceMappingURL=controllers.js.map
