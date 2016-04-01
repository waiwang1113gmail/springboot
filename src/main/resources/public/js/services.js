angular.module('appServices',[])
	.constant('AUTH_EVENTS', {
		loginSuccess: 'auth-login-success',
		loginFailed: 'auth-login-failed',
		logoutSuccess: 'auth-logout-success',
		sessionTimeout: 'auth-session-timeout',
		notAuthenticated: 'auth-not-authenticated',
		notAuthorized: 'auth-not-authorized'
	}).constant('USER_ROLES', {
		user:'ROLE_USER',
		admin:'ROLE_ADMIN'
	}).factory("AuthService",["$http","Session",function($http,Session){
		var authService={};
		authService.authenticate=function(credentials,callback){
			var headers = credentials ? {
				authorization : "Basic "
						+ btoa(credentials.username + ":"
								+ credentials.password)
			} : {};

			$http.get('user', {
				headers : headers
			}).then(function(res){ 
				console.log(res);
				if(res.data.name){
					Session.create(res.data.details.sessionId, res.data.name,
		                       res.data.principal.authorities.map(function(v){return v.authority;}));
					
				}
				callback&&callback(res.data.name);
				return res.data.name;
			});
		}
		authService.isAuthenticated=function(){ 
			return !!Session.userId;
		}
		authService.isAuthorized=function(authorizedRoles){
			if(!angular.isArray(authorizedRoles)){
				authorizedRoles=[authorizedRoles]
			}
			return (authService.isAuthenticated() && 
					authorizedRoles.indexOf(Session.userRole) !== -1);
		}
		authService.logout=function(callback){
			console.log("logout");
			$http.post('logout', {}).finally(function() {
				console.log("logout callback");
				Session.destroy();
				callback&&callback();
			  });
		}
		return authService;		
	}]).service('Session',function(){
		this.create = function(sessionId,userId,userRole) {
			this.id=sessionId;
			this.userId=userId;
			this.userRole=userRole;
		};
		this.destroy=function(){
			this.id=null;
			this.userId=null;
			this.userRole=null;
		};
	})