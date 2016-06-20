(function() {
	'use strict';

	angular.module('rbaccountsApp').controller('InvoiceNewController', InvoiceNewController);

	InvoiceNewController.$inject = [ '$scope', '$state', '$uibModal', '$timeout', 'Invoice', 'Dealer',
			'AlertService', 'Product', 'Tax', 'Pdf', '$http'];

	function InvoiceNewController($scope, $state, $uibModal, $timeout, Invoice, Dealer,
			AlertService, Product, Tax, Pdf, $http) {
		var vm = this;

		vm.doNotMatch = null;
		vm.error = null;
		vm.errorUserExists = null;
		vm.success = null;
		vm.dealers = [];
		vm.products = [];
		vm.taxes = [];
		vm.saveInvoice = saveInvoice;
		vm.invoice = {};
		vm.invoice.creationDate = new Date();
		vm.invoice.invoiceItems = [];
		vm.invoice.dealer = null;
		vm.selectedContact = {};
		vm.loadDealers = loadDealers;
		vm.loadProducts = loadProducts;
		vm.loadTaxes = loadTaxes;
		vm.selectContact = selectContact;
		vm.addInvoiceitem = addInvoiceitem;
		vm.showInvoice = showInvoice;
		vm.addImeiToInvoiceItem = addImeisToInvoiceItem;
		vm.addImeis = addImeis;
		vm.removeInvoiceItem = removeInvoiceItem;
		vm.selectInvoiceItemProduct = selectInvoiceItemProduct
		vm.selectInvoiceItemTax = selectInvoiceItemTax

		loadDealers();
		loadProducts();
		loadTaxes();

		function saveInvoice() {
			console.log('inside save method...,....');
			Invoice.save(vm.invoice);
			$state.go('invoice');
		}
		function showInvoice() {
			console.log('inside save method...,....');
			$http.post("api/pdf/", vm.invoice, {responseType: 'arraybuffer'}).success(function(data, status) {
                console.log(data);
                var file = new Blob([data], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
                display: block;
            });
		}
		function addInvoiceitem() {
			var invoiceItem = {};
			invoiceItem.index = vm.invoice.invoiceItems.length + 1;
			vm.invoice.invoiceItems.push(invoiceItem);
		}
		
		function removeInvoiceItem(invoiceItem) {
			var index = vm.invoice.invoiceItems.indexOf(invoiceItem);
			vm.invoice.invoiceItems.splice(index, 1);
		}
		
		
		function loadDealers() {
			Dealer.query({}, onSuccess, onError);
			function onSuccess(data, headers) {
				console.log(data);
				vm.dealers = data;
				var dealer = {};
				dealer.firmName = 'Add new contact+';
				dealer.id =-1;
				vm.dealers.push(dealer);
			}
		}
		
		function loadProducts() {
			Product.query({}, onSuccess, onError);

			function onSuccess(data, headers) {
				console.log(data);
				vm.products = data;
				var product = {};
				product.name = 'Add new Product+';
				product.id = -1;
				vm.products.push(product);
			}
		}
		
		function loadTaxes() {
			Tax.query({}, onSuccess, onError);

			function onSuccess(data, headers) {
				console.log(data);
				vm.taxes = data;
				var tax = {};
				tax.name = 'Add new Tax+';
				tax.id = -1;
				vm.taxes.push(tax);
			}
		}

		function onError(error) {
			AlertService.error(error.data.message);
		}
		
		function selectContact(dealer) {
			if(dealer.firmName === 'Add new contact+') {
                $uibModal.open({
                    templateUrl: 'app/entities/dealer/dealer-dialog.html',
                    controller: 'DealerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                creationDate: null,
                                modificationDate: null,
                                firmName: null,
                                ownerName: null,
                                tin: null,
                                termsAndConditions: null,
                                openingBalance: null,
                                currentBalance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                	loadDealers();
                }, function() {
                	loadDealers();
                });
			} else {
				vm.selectedContact = dealer;
				vm.invoice.dealerId = dealer.id;
				console.log('Selected contact : ');
				console.log(dealer);
				console.log('Selected firmName : ');
				console.log(vm.selectedContact.firmName);
				console.log('Selected invoice : ');
				console.dir(vm.invoice);
			}
		}
		
		function selectInvoiceItemProduct(invoiceItem, product) {
			if(product.name === 'Add new Product+') {
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-dialog.html',
                    controller: 'ProductDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                creationDate: null,
                                modificationDate: null,
                                firmName: null,
                                ownerName: null,
                                tin: null,
                                termsAndConditions: null,
                                openingBalance: null,
                                currentBalance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                	loadProducts();
                }, function() {
                	loadProducts();
                });
			} else {
				// invoiceItem.selectedProduct = product;
				invoiceItem.name = product.name;
				invoiceItem.mrp = product.mrp;
				invoiceItem.color = product.color;
				console.log('Selected product : ');
				console.log(product);
				console.log('Selected invoice : ');
				console.dir(vm.invoice);
			}
		}
		
		function selectInvoiceItemTax(invoiceItem , tax) {
			if(tax.name === 'Add new Tax+') {
                $uibModal.open({
                    templateUrl: 'app/entities/tax/tax-dialog.html',
                    controller: 'TaxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                creationDate: null,
                                modificationDate: null,
                                firmName: null,
                                ownerName: null,
                                tin: null,
                                termsAndConditions: null,
                                openingBalance: null,
                                currentBalance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                   loadTaxes();
                }, function() {
                   loadTaxes();
                });
			} else {
				invoiceItem.tax = tax;
				console.log('Selected tax : ');
				console.log(tax);
				console.log('Selected invoice : ');
				console.log(vm.invoice);
			}
		}
		
	    function addImeisToInvoiceItem(invoiceItem) {
	    	console.log('adding imei section to invoiceItmem');
	    	// this is done only first time when creating two input text for imeis
	    	if(!invoiceItem.imeis) {
	    		invoiceItem.imeis = [];
	    		var imei = {};
	    		invoiceItem.imeis.push(imei);
	    	}
	      }
	      
	      function addImeis(imeis, imei) {
	    	  console.log('imei changed');
	    	  console.log(imei);
	    	  if(imei && imei.imei1 && imei.imei1.length === 15) {
	    		  console.log('change focus to imei2....');
	    		  
	    	  }
	    	  if(imei && imei.imei1 && imei.imei2 && imei.imei1.length === 15 && imei.imei2.length === 15) {
	    		 imei.index = imeis.length + 1;
	    		 console.log('adding imei');
	    		 console.log(imei);
	    		 imeis.push(imei);
	    		 console.log('imei added .. New imeis list');
	    		 console.log(imeis);
	    		 imei.imei1 = "";
	    		 imei.imei1 = "";
	    	 }
	      }
		
	}
})();