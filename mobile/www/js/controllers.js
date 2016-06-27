/// <reference path="../typings/index.d.ts"/>
/// <reference path="services.ts"/>
//import {Page, NavController, NavParams, IonicApp, Content, Alert, Modal, ViewController} from 'ionic-angular';
//import {Http, Headers, URLSearchParams} from 'angular2/http';
//import 'rxjs/Rx';
angular.module('starter.controllers', [])
    .controller('DashboardCtrl', function ($scope, $log, $window, $timeout, $resource, $http) {
    $scope.apiUri = 'http://localhost:8080/api/';
    $scope.refreshSiteSummaries = function () {
        var SiteSummary = $resource($scope.apiUri + 'siteSummaries/:id');
        SiteSummary.get({ sort: ['watchedSite.name', 'watchedSite.siteScreenName'], size: 100 }, function (resp) {
            var siteSummaries = resp._embedded.siteSummaries;
            $scope.siteSummaries = siteSummaries;
            $log.info('Site summaries:', siteSummaries);
            $scope.updateTime = Date.create(siteSummaries[0].modificationTime);
        });
    };
    $http.get('/api/config.json', { responseType: 'json' }).then(function (resp) {
        $scope.apiUri = resp.data.apiUri;
        $log.info('apiUri =', $scope.apiUri, 'from', resp);
        $scope.refreshSiteSummaries();
    }, $scope.refreshSiteSummaries);
});

//# sourceMappingURL=controllers.js.map
