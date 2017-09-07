// Configuring the module

angular.module('core').run(['$scope', '$log',
	function($scope, $log) {		
		$scope.notImplemented = function(msg){
			msg = msg || 'Not Implemented';
			$log.warn(msg);
		};
	}
]);