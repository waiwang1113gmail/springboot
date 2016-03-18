angular
	.module("appServices",[])
	.constants("AUTH_EVENTS",{
		loginSuccess: 'auth-login-success',
		loginFailed: 'auth-login-failed',
		logoutSuccess: 'auth-logout-success',
		sessionTimeout: 'auth-session-timeout',
		notAuthenticated: 'auth-not-authenticated',
		notAuthorized: 'auth-not-authorized'
	})
	.constants("USER_ROLES",{
		user: 'user',
		admin: 'admin'
	})
	.factory("authService",['$http',function($http){
		var services={};
		services.authenticate = function(credentials, callback) {

			var headers = credentials ? {
				authorization : "Basic "
						+ btoa(credentials.username + ":"
								+ credentials.password)
			} : {};

			$http.get('user', {
				headers : headers
			}).success(function(data) {
				if (data.name) {
					$rootScope.authenticated = true;
				} else {
					$rootScope.authenticated = false;
				}
				callback && callback();
			}).error(function() {
				$rootScope.authenticated = false;
				callback && callback();
			});

		}
		services.
		return services;
	}]);
