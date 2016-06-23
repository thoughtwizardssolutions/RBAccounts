(function() {
    'use strict';

    angular
        .module('rbaccountsApp')
        .controller('ReportController', ReportController);

    ReportController.$inject = ['$scope', '$state', 'Report', 'AlertService', 'FileSaver', 'Blob', 'Dealer'];

    function ReportController ($scope, $state, Report, AlertService, FileSaver, Blob, Dealer) {
        var vm = this;
        
        vm.invoices = [];
        
        vm.exportData = exportData;
        vm.getData = getData;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.datePickerOpenStatus.creationTime = false;
        vm.datePickerOpenStatus.modificationTime = false;
        vm.selectContact = selectContact;
        vm.search = {};
        
        loadDealers();
        
        function selectContact(dealer) {
        	vm.selectedDealer = dealer;
			vm.search.dealerId = dealer.id;
		}
        
		function loadDealers() {
			Dealer.query({}, onSuccess, onError);
			function onSuccess(data, headers) {
				console.log(data);
				vm.dealers = data;
			}
		}

        function getData () {
            Report.query({
            	fromDate : new Date(vm.search.fromDate).valueOf(),
            	toDate : new Date(vm.search.toDate).valueOf(),
            	dealerId : vm.search.dealerId                
            }, onSuccess, onError);
            function onSuccess(data, headers) {
            	vm.invoices = data;
                vm.invoices? vm.totalItems = vm.invoices.length: vm.totalItems = 0;
                vm.queryCount = vm.totalItems;
                vm.invoiceTotalAmount = calculateTotalAmount(vm.invoices);
                
                
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
