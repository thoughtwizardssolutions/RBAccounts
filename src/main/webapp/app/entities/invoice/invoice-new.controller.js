(function() {
	'use strict';

	angular.module('rbaccountsApp').controller('InvoiceNewController', InvoiceNewController);

	InvoiceNewController.$inject = [ '$scope', '$state', 'entity', '$uibModal', 'Invoice', 'Dealer',
			'AlertService', 'Product', 'Tax', '$http', 'UserSequence', 'Principal'];

	function InvoiceNewController($scope, $state, entity, $uibModal, Invoice, Dealer,
			AlertService, Product, Tax, $http, UserSequence, Principal) {
		var vm = this;

		vm.error = null;
		vm.invoiceType = null;
		vm.userSequence = null;
		vm.selectInvoiceType = selectInvoiceType;
		vm.errorUserExists = null;
		vm.success = null;
		vm.saveInvoice = saveInvoice;
		vm.loadDealers = loadDealers;
		vm.loadProducts = loadProducts;
		vm.loadTaxes = loadTaxes;
		vm.selectContact = selectContact;
		vm.addInvoiceitem = addInvoiceitem;
		vm.showInvoice = showInvoice;
		vm.addImeiToInvoiceItem = addImeisToInvoiceItem;
		vm.removeImeiToInvoiceItem = removeImeiToInvoiceItem;
		vm.removeInvoiceItem = removeInvoiceItem;
		vm.selectInvoiceItemProduct = selectInvoiceItemProduct
		vm.selectInvoiceItemTax = selectInvoiceItemTax
		vm.calculateItemAmount = calculateItemAmount;
		vm.calculateInvoiceTotal = calculateInvoiceTotal;
		vm.updateAmounts = updateAmounts;
		vm.showImeiSection = showImeiSection;
		vm.addImeiOnEnter = addImeiOnEnter;
		vm.editing = false;
		
		setupInvoice();
		loadDealers();
		loadProducts();
		loadTaxes();
		
		function setupInvoice() {
			vm.error = null;
			vm.success = null;
			if(entity) {
				vm.editing = true;
				// show items
				for(var i = 0 ; i < entity.invoiceItems.length ;i ++) {
					entity.invoiceItems[i].index = i;
				}
				// show imeis
				for(var i = 0; i < entity.invoiceItems.length ; i ++) {
					if(!entity.invoiceItems[i].imeis) {
						continue;
					}
					if(entity.invoiceItems[i].imeis.length === 0) {
						entity.invoiceItems[i].imeis = null;
					} else {
						for(var j = 0; j < entity.invoiceItems[i].imeis.length ; j++) {
							entity.invoiceItems[i].imeis[j].index = j;
						}
					}
                }
				vm.invoice = entity;
				// load dealer
				Dealer.get({id : vm.invoice.dealerId}, onSuccess, onError);
				function onSuccess(data, headers) {
					vm.selectedContact = data;
					vm.tmpSelectedContact = vm.selectedContact.firmName;
				}
			} else {
				vm.invoice = {};
				vm.invoice.taxes = 0;
				vm.invoice.subtotal = 0;
				vm.invoice.totalAmount = 0;
				vm.invoice.adjustments = 0;
				vm.invoice.shippingCharges = 0;
				vm.invoice.creationDate = new Date();
				vm.invoice.invoiceItems = [];
				vm.invoice.invoiceType = 'TAX_INVOICE';
				vm.dealers = [];
				vm.products = [];
				vm.selectedContact = {};
				vm.invoiceType = 'Tax Invoice';
				
		        Principal.identity().then(function(account) {
		            vm.currentAccount = account;
					UserSequence.get({user : account.login}, onSuccess, onError);
					function onSuccess(data, headers) {
						vm.userSequence = data;
						// select tax by default
						vm.invoice.invoiceNumber = data.prefixTax + '-' +data.taxSequence;
						}
		        });
			}
		}
		
		function selectInvoiceType(invoiceType) {
			vm.invoiceType = invoiceType;
			if(invoiceType === 'Tax Invoice') {
				vm.invoice.invoiceNumber = vm.userSequence.prefixTax + '-' +vm.userSequence.taxSequence;
				vm.invoice.invoiceType = 'TAX_INVOICE';
			} else if(invoiceType === 'Sales Invoice') {
				vm.invoice.invoiceNumber = vm.userSequence.prefixSales + '-' +vm.userSequence.salesSequence;
				vm.invoice.invoiceType = 'SALES_INVOICE';
			} else {
				vm.invoice.invoiceNumber = vm.userSequence.prefixSample + '-' +vm.userSequence.sampleInvoiceSequence;
				vm.invoice.invoiceType = 'SAMPLE_INVOICE';
			}
		}

		function saveInvoice() {
			vm.error = null;
			if (vm.invoice.totalAmount == 0) {
				return;
			}
			if(vm.invoice.id) {
				Invoice.update(vm.invoice,onSaveSuccess, onSaveError);
			} else {
				Invoice.save(vm.invoice, onSaveSuccess, onSaveError);
			}
			function onSaveSuccess(data) {
				vm.success = true;
				$state.go('invoice', null, { reload: true });
			}
			function onSaveError(error) {
				AlertService.error(error.headers('X-rbaccountsApp-error'));
				vm.error = true;
			}
		}
		
		function showInvoice() {
			if (vm.invoice.totalAmount == 0) {
				return;
			}
			$http.post("api/pdf/", vm.invoice, {responseType: 'arraybuffer'})
			.success(function(data, status) {
                var file = new Blob([data], {type: 'application/pdf'});
                var fileURL = URL.createObjectURL(file);
                window.open(fileURL);
            }).error(function(data,status,headers,config) {
            	AlertService.error(headers('X-rbaccountsApp-error'));
            });
		}
		function addInvoiceitem() {
			var invoiceItem = {};
			invoiceItem.index = vm.invoice.invoiceItems.length +1;
			vm.invoice.invoiceItems.push(invoiceItem);
		}
		
		function removeInvoiceItem(invoiceItem) {
			// remove item
			var index = vm.invoice.invoiceItems.indexOf(invoiceItem);
			vm.invoice.invoiceItems.splice(index, 1);
			updateAmounts();
			
		}
		
		function loadDealers() {
			Dealer.query({}, onSuccess, onError);
			function onSuccess(data, headers) {
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
				vm.products = data;
				if(vm.editing){
					// update max quantities for each item
					for(var i = 0; i < vm.invoice.invoiceItems.length; i ++) {
						for(var j = 0 ; j < vm.products.length ; j++) {
							if(vm.invoice.invoiceItems[i].productName === vm.products[j].name) {
								entity.invoiceItems[i].maxQuantity = vm.products[j].quantity;
							}
						}
					}
				}
				var product = {};
				product.name = 'Add new Product+';
				product.id = -1;
				vm.products.push(product);
			}
		}
		
		function loadTaxes() {
			Tax.query({}, onSuccess, onError);

			function onSuccess(data, headers) {
				vm.taxes = data;
				var tax = {};
				tax.name = 'Add new Tax';
				tax.id = -1;
				vm.taxes.push(tax);
			}
		}

		function onError(error) {
			AlertService.error(error.data.message);
			vm.error = true;
		}
		
		function showImeiSection(invoiceItem) {
			if(invoiceItem && invoiceItem.imeis && invoiceItem.imeis.length > 0) {
				return true;
			}
			return false;
		}
		
		function selectContact(dealer) {
			if(dealer.firmName === 'Add new contact+') {
				vm.selectedContact = null;
				vm.invoice.dealerId = null;
				vm.tmpSelectedContact = null;
                $uibModal.open({
                    templateUrl: 'app/entities/dealer/dealer-dialog.html',
                    controller: 'DealerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return null;
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
				vm.tmpSelectedContact = null;
			}
		}
		
		function selectInvoiceItemProduct(invoiceItem, product) {
			if(product.name === 'Add new Product+') {
				vm.tmpSelectedProduct = null;
				invoiceItem.productName = null;
				invoiceItem.mrp = null;
				invoiceItem.color = null;
				invoiceItem.maxQuantity = null;
				invoiceItem.product = null;
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-dialog.html',
                    controller: 'ProductDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return null;
                        }
                    }
                }).result.then(function() {
                	loadProducts();
                }, function() {
                	loadProducts();
                });
			} else {
				vm.tmpSelectedProduct = null;
				invoiceItem.productName = product.name;
				invoiceItem.mrp = product.mrp;
				invoiceItem.color = product.color;
				invoiceItem.maxQuantity = product.quantity;
				invoiceItem.product = product;
				calculateItemAmount(invoiceItem)
			}
		}
		
		function selectInvoiceItemTax(invoiceItem , tax) {
			if(tax.name === 'Add new Tax') {
                $uibModal.open({
                    templateUrl: 'app/entities/tax/tax-dialog.html',
                    controller: 'TaxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return null;
                        }
                    }
                }).result.then(function() {
                   loadTaxes();
                }, function() {
                   loadTaxes();
                });
			} else {
				invoiceItem.taxRate = tax.rate;
				invoiceItem.taxType = tax.name;
				updateAmounts();
			}
		}
		
	    function addImeisToInvoiceItem(invoiceItem) {
	    	// this is done only first time when creating imei section for invoiceItem
	    	if(!invoiceItem.imeis) {
	    		invoiceItem.imeis = [];
	    		var imei = {};
	    		imei.index = invoiceItem.imeis.length + 1;
	    		invoiceItem.imeis.push(imei);
	    	}
	      }
	    
	    function removeImeiToInvoiceItem(invoiceItem, imei) { 
    	  var index = invoiceItem.imeis.indexOf(imei);
    	  invoiceItem.imeis.splice(index, 1); 
    	  if (invoiceItem.imeis.length == 0) {
    		  invoiceItem.imeis = null;
    	  }
    	}
	    
	     function addImeiOnEnter(invoiceItem, imei1, imei2){
	    	 var imeis = invoiceItem.imeis;
	    	
	    	 if(imeis.length === 1 && !imeis[0].imei1 && !imeis[0].imei2) {
	    		 imeis[0].imei1 = imei1;
	    		 imeis[0].imei2 = imei2;
	    	 } else  if(imeis.length === invoiceItem.quantity) {
	    		 AlertService.error('Can not add more IMEI. Please increase quantity of this product.');
		    	 vm.imei1 = "";
	    		 vm.imei2 = "";
	    		 return;
	    	 } else {
	    		 var imei = {};
	    		 imei.imei1 = imei1;
	    		 imei.imei2 = imei2;
	    		 imei.index = imeis.length + 1;
	    		 imeis.push(imei);
	    	 }
	    	 vm.imei1 = "";
    		 vm.imei2 = "";
	    	 
	     } 
	     
	      
		 function calculateItemAmount(invoiceItem) {
			 if(invoiceItem.quantity > invoiceItem.maxQuantity) {
				 AlertService.error("Available quantity for the selected product is "+invoiceItem.maxQuantity);
				 vm.invalid = true;
				 return;
			 } else {
				 vm.invalid = false;
			 }
			 if(!invoiceItem.discount) invoiceItem.discount = 0;
			 if(!invoiceItem.quantity) invoiceItem.quantity = 0;
			 if(!invoiceItem.mrp) invoiceItem.mrp = 0;
			if (invoiceItem) {
				invoiceItem.amount = (invoiceItem.quantity * invoiceItem.mrp) - invoiceItem.discount;
				updateAmounts();
			 }
		 }
		 function updateAmounts(){
			vm.invoice.subtotal = 0;
			vm.invoice.taxes = 0;
			 for(var i = 0; i < vm.invoice.invoiceItems.length; i++) {
				 vm.invoice.subtotal = vm.invoice.subtotal + vm.invoice.invoiceItems[i].amount;
				 if(vm.invoice.invoiceItems[i].taxRate) {
					 vm.invoice.taxes = vm.invoice.taxes +  (vm.invoice.invoiceItems[i].amount * vm.invoice.invoiceItems[i].taxRate)/100;
				 }
			 }
			 vm.invoice.totalAmount = vm.invoice.subtotal + vm.invoice.taxes + vm.invoice.shippingCharges - vm.invoice.adjustments;
		 }
		 
		 function calculateInvoiceTotal() {
				vm.invoice.totalAmount =  vm.invoice.subtotal - vm.invoice.adjustments + vm.invoice.taxes + vm.invoice.shippingCharges;
		 }
		
	}
})();