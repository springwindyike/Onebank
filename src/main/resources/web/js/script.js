var app = angular.module('JGApp', ['ngDialog']);

app.controller('JGCtrl', function ($scope, ngDialog) {
    $scope.clickToOpenSparkWidget  = function (UserID) {
      ngDialog.open({
          template: '<iframe src="../sparkwidget/'+UserID+'" width="620" height="420"></iframe> ',
          plain: true,
          width: 680,
          height: 480
      });
    };
    $scope.clickToOpenJG  = function () {
      ngDialog.open({
          template: '<iframe src="http://reachme.cisco.com/call/fkf-demo1.gen@cisco.com?widget=true" width="600" height="400"></iframe>',
          plain: true,
          width: 680,
          height: 480
      });
    };
});
