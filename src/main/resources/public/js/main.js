angular
		.module('hello', [ 'ngRoute','appServices'])
		.config(
				function($routeProvider, $httpProvider) {

					$routeProvider.when('/', {
						templateUrl : 'home.html',
						controller : 'home',
						controllerAs : 'controller'
					}).when('/login', {
						templateUrl : 'login.html',
						controller : 'navigation',
						controllerAs : 'controller'
					}).when('/register', {
						templateUrl : 'registerForm.html',
						controller : 'register',
						controllerAs : 'controller'
					}).otherwise('/');

					$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

				}).controller('home', function($http) {
			var self = this;
			$http.get('/resource/').success(function(data) {
				self.greeting = data;
			})
		}).controller('ApplicationController', function($scope,USER_ROLES,AuthService,Session){
		
			$scope.currentUser =null;
			$scope.userRoles = USER_ROLES; 
			$scope.authenticated=false;
			$scope.setCurrentUser = function (user) {
			    $scope.currentUser = user;
			};
			AuthService.authenticate(null,function(){
				$scope.authenticated=AuthService.isAuthenticated();
				$scope.isAuthorized = AuthService.isAuthorized;
			});
		})
		.controller('register', function($rootScope,$http,$location) {
			var self = this;
			self.user = {}; 
			var register = function(user,callback){ 
				$http.post("register", user).success(function(data, status) {
		            callback && callback(data,status);
		        }).error(function(data, status) {
		            callback && callback(data,status);
		        });
			}
			self.error=false;
			self.errorMsg="ERROR";
			self.submit=function(){
				register(self.user,function(data,status){
					console.log(status);
					if(status !==200){
						console.log("Ddsadsa");
						self.error=true;
						self.errorMsg=data;
					}else{
						$rootScope.authenticated = true;
						$location.path("/");
					}
				});
				
				
			}
		}).controller('navigation',
				function(Session, $http, $location,AuthService,$scope) {
					var self = this;
					self.credentials = {};
					self.login = function() {
						AuthService.authenticate(self.credentials, function() {
							if (Session.isAuthenticated()) { 
								$scope.authenticated=AuthService.isAuthenticated();
								$location.path("/");
								self.error = false;
							} else {
								$location.path("/j_spring_security_logout");
								self.error = true;
							}
						});
					}; 
					  
					self.logout = function() {
						  $http.post('logout', {}).finally(function() {
							Session.destroy();
						    $location.path("/");
						  });
						}
				});