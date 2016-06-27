/// <reference path="../typings/index.d.ts"/>
// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ngResource', 'starter.controllers', 'starter.services',
    'patternfly', 'patternfly.charts'])

.run(function(/*$ionicPlatform*/) {

//  $ionicPlatform.ready(function() {
//    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
//    // for form inputs)
//    if (window.cordova && window.cordova.plugins.Keyboard) {
//      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
//    }
//    if (window.StatusBar) {
//      // org.apache.cordova.statusbar required
//      StatusBar.styleDefault();
//    }
//  });

})

.filter('trusted', ['$sce', function ($sce) {
    return function(url) {
        return $sce.trustAsResourceUrl(url);
    };
}])
.filter('humaneNumber', function () {
    return function(input) {
        return input >= 1000000 ? d3.round(input / 1000000, 2) + 'M' :
            (input >= 1000 ? d3.round(input / 1000, 1) + 'K' : input);
    };
})

.config(function(/*$stateProvider, $urlRouterProvider, $sceDelegateProvider*/) {

//  $sceDelegateProvider.resourceUrlWhitelist([
//    // Allow same origin resource loads.
//    'self',
//    // Allow loading from data URI
//    'data:**'
//  ]);
//
//  $stateProvider
//
//  .state('app', {
//    url: "/app",
//    abstract: true,
//    templateUrl: "templates/menu.html"
//  })
//
//  .state('app.home', {
//    url: "/home",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/home.html"
//      }
//    }
//  })
//
//  .state('app.avatar-remote-control', {
//      url: "/avatar/remote-control",
//      views: {
//        'menuContent': {
//          templateUrl: "templates/avatar/remote-control.html"
//        }
//      }
//    })
//  .state('app.avatar-instruments', {
//      url: "/avatar/instruments",
//      views: {
//        'menuContent': {
//          templateUrl: "templates/avatar/instruments.html"
//        }
//      }
//    })
//
//    // Visual
//  .state('app.visual-camera', {
//      url: "/visual/camera",
//      views: {
//        'menuContent': {
//          templateUrl: "templates/visual/camera.html",
//          controller: 'VisualCameraCtrl'
//        }
//      }
//    })
//
//  .state('app.face-recognition-img', {
//    url: "/face-recognition-img",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/face-recognition-img.html",
//        controller: 'FaceRecognitionImgCtrl'
//      }
//    }
//  })
//  .state('app.visual-object-recognition', {
//    url: "/visual/object-recognition",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/visual/object-recognition.html",
//        controller: 'ObjectRecognitionCtrl'
//      }
//    }
//  })
//  .state('app.face-recognition-cam', {
//    url: "/face-recognition-cam",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/face-recognition-cam.html",
//        controller: 'FaceRecognitionCamCtrl'
//      }
//    }
//  })
//
//    // Audio
//  .state('app.audio-monitor', {
//    url: "/audio/monitor",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/audio/monitor.html",
//        controller: 'AudioMonitorCtrl'
//      }
//    }
//  })
//
//  // Persistence
//  .state('app.persistence-fact', {
//    url: "/persistence/fact",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/persistence/fact.html",
//        controller: 'PersistenceFactCtrl'
//      }
//    }
//  })
//  .state('app.persistence-query-find-all', {
//    url: "/persistence/query-find-all",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/persistence/query-find-all.html",
//        controller: 'PersistenceQueryFindAllCtrl'
//      }
//    }
//  })
//  .state('app.persistence-query-cypher', {
//    url: "/persistence/query-cypher",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/persistence/query-cypher.html",
//        controller: 'PersistenceQueryCypherCtrl'
//      }
//    }
//  })
//  .state('app.persistence-journal-image', {
//    url: "/persistence/journal-image",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/persistence/journal-image.html",
//        controller: 'PersistenceJournalImageCtrl'
//      }
//    }
//  })
//  .state('app.persistence-journal-joint', {
//    url: "/persistence/journal-joint",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/persistence/journal-joint.html",
//        controller: 'PersistenceJournalJointCtrl'
//      }
//    }
//  })
//  .state('app.persistence-journal-sonar', {
//    url: "/persistence/journal-sonar",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/persistence/journal-sonar.html",
//        controller: 'PersistenceJournalSonarCtrl'
//      }
//    }
//  })
//  .state('app.persistence-journal-tactile', {
//    url: "/persistence/journal-tactile",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/persistence/journal-tactile.html",
//        controller: 'PersistenceJournalTactileCtrl'
//      }
//    }
//  })
//  .state('app.persistence-journal-battery', {
//    url: "/persistence/journal-battery",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/persistence/journal-battery.html",
//        controller: 'PersistenceJournalBatteryCtrl'
//      }
//    }
//  })
//
//
//  .state('app.social-chat', {
//    url: "/social-chat",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/social/chat.html"
//      }
//    }
//  })
//  .state('app.social-monitor', {
//    url: "/social-monitor",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/social/monitor.html",
//        controller: 'SocialMonitorCtrl'
//      }
//    }
//  })
//  .state('app.social-express', {
//    url: "/social-express",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/social/express.html",
//        controller: 'SocialExpressCtrl'
//      }
//    }
//  })
//
//  .state('app.settings', {
//    url: "/settings",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/settings.html",
//        controller: 'SettingsCtrl'
//      }
//    }
//  })
//
//  .state('app.search', {
//    url: "/search",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/search.html"
//      }
//    }
//  })
//
//  .state('app.browse', {
//    url: "/browse",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/browse.html"
//      }
//    }
//  })
//    .state('app.playlists', {
//      url: "/playlists",
//      views: {
//        'menuContent': {
//          templateUrl: "templates/playlists.html",
//          controller: 'PlaylistsCtrl'
//        }
//      }
//    })
//
//  .state('app.single', {
//    url: "/playlists/:playlistId",
//    views: {
//      'menuContent': {
//        templateUrl: "templates/playlist.html",
//        controller: 'PlaylistCtrl'
//      }
//    }
//  });
//  // if none of the above states are matched, use this as the fallback
//  $urlRouterProvider.otherwise('/app/home');

})

.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});
