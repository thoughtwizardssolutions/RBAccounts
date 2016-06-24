(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['$scope', '$state', 'Report', 'AlertService', 'FileSaver', 'Blob', 'UserName'];

    function ReportController ($scope, $state, Report, AlertService, FileSaver, Blob, UserName) {
        var vm = this;
        
        vm.invoices = [];
        vm.noData = false;
        vm.exportData = exportData;
        vm.getData = getData;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.datePickerOpenStatus.creationTime = false;
        vm.datePickerOpenStatus.modificationTime = false;
        vm.selectUser = selectUser;
        vm.search = {};
        
        loadUsernames();
        
        function selectUser(user) {
        	vm.selectedUsername = user;
			vm.search.createdBy = user;
		}
        
		function loadUsernames() {
			UserName.query({}, onSuccess, onError);
			function onSuccess(data, headers) {
				console.log(data);
				vm.usernames = data;
				vm.username.push('All Users');
			}
		}

        function getData () {
        	vm.noData = false;
        	vm.invoices = [];
            Report.query({
            	fromDate : new Date(vm.search.fromDate).valueOf(),
            	toDate : new Date(vm.search.toDate).valueOf(),
            	dealerId : vm.search.dealerId,
            	createdBy : vm.search.createdBy === 'All Users' ? null : vm.search.createdBy
            }, onSuccess, onError);
            function onSuccess(data, headers) {
            	vm.invoices = data;
                vm.invoices? vm.totalItems = vm.invoices.length: vm.totalItems = 0;
                vm.queryCount = vm.totalItems;
                vm.invoiceTotalAmount = calculateTotalAmount(vm.invoices);
                if(data.length === 0) vm.noData = true;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        
        function calculateTotalAmount(invoices) {
        	if(!invoices) {
        		return 0;
        	}
        	var total = 0;
        	for(var i = 0 ; i < invoices.length ; i ++) {
        		total = total + invoices[i].totalAmount;
        	}
        	return total;
        }

         function exportData() {
            var blob = new Blob([document.getElementById('exportable').innerHTML], {
                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
            });
            FileSaver.saveAs(blob, "Report.xls");
        }
         
         function openCalendar (date) {
             vm.datePickerOpenStatus[date] = true;
         }
         
 		function onError(error) {
			AlertService.error(error.data.message);
		}
        
    }
})();
