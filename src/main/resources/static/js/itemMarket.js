const buyBtn = document.getElementsByClassName('buyBtn');
const show = document.getElementById('showPage1');
const salePage = document.getElementById('showSalePage1');
const buyPageInfo = document.getElementById('buyPageInfo');
const inventoryList = document.getElementById('inventoryList1');
const userId = document.getElementById('user1').textContent;
console.log('userId: ' + userId);
let removeHtml;


// =========================== itemMarketPage ===========================  
// --------------------------- show page ---------------------------
$(function(){
	let itemId = document.getElementById('itemId1').textContent;
	axios({
        url: '/carbon/market/medianPrice',
        method: 'get',
        params: {
            itemId: itemId
        }
    })
        .then(response => {
			if(response.data != ''){
	            medianPriceChart(response.data);
			} else {
				let noHistory = document.getElementById('noHistory1');
				noHistory.innerHTML = `<h4 class="m-10 text-center">此物品尚無歷史價格</h4>`;
			}
        })
        .catch(err => {
            console.log('err: ' + err);
        })
	
})
// --------------------------- buy an item page ---------------------------
for (i = 0; i < buyBtn.length; i++) {
    buyBtn[i].addEventListener('click', function (e) {
        e.preventDefault();
        removeHtml = $(this).closest('li');
        console.log(removeHtml);
        let buyId = this.getAttribute('data-id');
        buyAnItem(buyId);
    })
}


// --------------------------- get order info ---------------------------        
function buyAnItem(buyId) {
    axios.get('/carbon/market/buyAnItem',
        { params: { ordId: buyId } }
    )
        .then(response => {
            // console.log('response: ' + JSON.stringify(response.data));
            if (userId != '') {
                showBuyInfo(response.data);
            } else {
                loginPage();
            }
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}


// --------------------------- show buy order info page ---------------------------
function showBuyInfo(order) {
    let infoHtmlString = `
        <div class="col d-flex justify-content-between align-items-center">
            <div class="imgBox overflow-hidden col-2">
                <img src="/carbon/market/downloadImage/${order.itemId}"
                    alt="${order.gameItem.itemImgName}" class="img-fluid" style="max-width: 90px">
            </div>
            <div class="col-5">${order.gameItem.itemName}</div>
            <div class="col-2">${order.sell.memberName}</div>
            <div class="col-2">$ ${order.price}</div>
        </div>`;

    buyPageInfo.innerHTML = infoHtmlString;

    const submitBtn = document.getElementById('submitBtn');
    submitBtn.addEventListener('click', function () {
        console.log(order.itemId);
        newOrder(order, userId);
    })
}


// --------------------------- insert new order ---------------------------        
function newOrder(order, buyer) {
    axios({
        url: '/carbon/market/done',
        method: 'post',
        data: {
            itemId: order.itemId,
            buyer: buyer,
            seller: order.seller,
            quantity: 1,
            price: order.price
        }
    })
        .then(response => {
            if (response != null) {
                orderUpdate(order);
            }
            return response.data;
            
        })
        .then(result => {
            if (result != null) {
	            axios.get('/carbon/market/buyAnItem',{
					params: {ordId: result.ordId}
					})
					.then(res => {
		                showSuccessPage(res.data);
		                removeHtml.remove();
					})
					.catch(err => {
						console.log('err: ' + err);
					})
            }
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}


// --------------------------- update order status ---------------------------  		
function orderUpdate(order) {
    axios({
        url: '/carbon/market/orderUpdate',
        method: 'put',
        data: {
            ordId: order.ordId,
            status: 2
        }
    })
        .then(res => {
            //console.log(res.data);
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}


// --------------------------- insert new itemLog (沒用到了) ---------------------------  		
function newItemLog(result) {
    axios({
        url: '/carbon/market/newItemLog',
        method: 'post',
        data: {
            ordId: result.ordId,
            itemId: result.itemId,
            memberId: result.buyer ? result.buyer : result.seller,
            quantity: result.buyer ? result.quantity : '-' + result.quantity
        }
    })
        .then(res => {
            //console.log(JSON.stringify(res.data));
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}


// --------------------------- success page ---------------------------  
function showSuccessPage(order) {
    let infoHtmlString = `
        <div class="col d-flex justify-content-between align-items-center">
            <div class="imgBox overflow-hidden col-2">
                <img src="/carbon/market/downloadImage/${order.itemId}"
                    alt="" class="img-fluid" style="max-width: 90px">
            </div>
            <div class="col-5">${order.gameItem.itemName}</div>
            <div class="col-2">${order.sell.memberName}</div>
            <div class="col-2">$ ${order.price}</div>
        </div>
        <div class="gap-2 col d-flex justify-content-end" id="btnGroup">
            <a class="btn btn-info submitBtn col-3" href="#">查看物品庫</a>
            <a class="btn btn-info closeBtn2 col-md-2" href="#" class="close" data-dismiss="modal" aria-label="Close">關閉</a>
        </div>`;
    buyPageInfo.innerHTML = infoHtmlString;
    $('#pageMsg').removeClass('d-none');
    $('#userInfo1').addClass('d-none');
}


// --------------------------- buy order ---------------------------  
$('#buyOrder1').on('click', function () {
    if (userId == '') {
        loginPage();
    } 
    var buyPrice;
    var buyQuantity;
    $('#buyPrice1,#buyQuantity1').on('keyup change', function () {
        buyPrice = Number($('#buyPrice1').val().replace('NT$', ''));
        buyQuantity = Number($('#buyQuantity1').val());
        if($('#buyPrice1').val().indexOf('NT$') < 0 ){
	        $('#buyPrice1').val('NT$ 0');
		}
        if(buyPrice * buyQuantity == 0 || isNaN(buyPrice * buyQuantity)){
			$('#maxPrice1').val('NT$  --');
		} else{
        	$('#maxPrice1').val('NT$ ' + buyPrice * buyQuantity);
		}
    })
    let buyId = $(this).attr('data-buyId');
    console.log(buyId);

    // ------------- Validation -------------  
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    const forms = document.querySelectorAll('.needs-validation')
    // Loop over them and prevent submission
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            } else{
                event.preventDefault()
				console.log(buyPrice);
	            console.log(buyQuantity);
	            axios({
	                url: '/carbon/market/newOrder',
	                method: 'post',
	                data: {
	                    itemId: buyId,
	                    buyer: userId,
	                    quantity: buyQuantity,
	                    price: buyPrice,
	                }
	            })
	                .then(response => {
	                    if (response.data != '') {
	                        let orders = response.data;
	                        console.log('itemId: ' + orders.itemId)
	                        axios({
	                            method: 'get',
	                            url: '/carbon/market/checkSales',
	                            params: { itemId: buyId }
	                        })
	                            .then(res => {
	                                let sales = res.data;
	                                //console.log('sales: ' + JSON.stringify(sales));
	                                if (sales != '' && buyPrice >= sales[0].price) {
	                                    console.log('res: ' + JSON.stringify(sales[0].price))
	                                    console.log('price: ' + buyPrice)
	                                    axios({
	                                        url: '/carbon/market/done',
	                                        method: 'post',
	                                        data: {
	                                            itemId: sales[0].itemId,
	                                            buyer: userId,
	                                            seller: sales[0].seller,
	                                            quantity: 1,
	                                            price: sales[0].price,
	                                        }
	                                    })
	                                        .then(result => {
	                                            //console.log('result: ' + JSON.stringify(result.data))
	                                            if (result.data != '') {
	                                                orderUpdate(orders);
	                                                orderUpdate(sales[0]);
	                                            }
	                                            return result.data;
	                                        })
	                                        .catch(err => {
	                                            console.log('err: ' + err);
	                                        })
	                                }
	                            })
	                    }
	                    return response.data;
	                })
	                .then(result => {
	                    if (result != null) {
	                        // showSuccessPage(result);
	
	                    }
	                })
	                .catch(err => {
	                    console.log('err: ' + err);
	                })
			}
            form.classList.add('was-validated')
        }, false)
    })
})

// --------------------------- sell list ajax ---------------------------          
function listPage(data) {
    let sellListHtml = `
        <ul class="nk-forum text-white">`;
    data.forEach(order => {
    	sellListHtml += `
            <li class="p-10">
                <div class="nk-forum-activity me-3 d-flex">
                	<img th:src="@{'/market/downloadImage/' + ${order.itemId}}" th:alt="${order.gameItem.itemImgName}"
                            class="img-fluid" style="max-width: 120px">
                </div>
                <div class="nk-forum-title my-auto">
                	<h3>[[${order.gameItem.itemName}]]</h3>
                </div>
                <div class="nk-forum-activity my-auto">
                    <div class="nk-forum-activity-title text-center">
                        [[${order.sell.memberName}]]
                    </div>
                </div>
                <div class="nk-forum-activity my-auto d-flex justify-content-center">
                    <div class="nk-forum-activity-title">
                        NT$ [[${order.price}]]
                    </div>
                </div>
                <div class="nk-forum-activity text-center my-auto d-flex justify-content-center">
                	<a href="#" class="btn btn-info buyBtn" data-toggle="modal" data-target="#modalBuyPage" th:data-id="${order.ordId}">
                        立即購買
                    </a>
                </div>
            </li>`;
	})
		sellListHtml += `</ul>`;
}


// =========================== itemMarketList ===========================
// --------------------------- show active ---------------------------
if(userId != ''){
	
	axios.get('/carbon/market/activeList',{
		params: {memberId: userId}
	})
	.then(response =>{
		if(response.data != ''){
			//console.log(JSON.stringify(response.data))
			activeList(response.data);
		}
	})
	.catch(err => {
		console.log('err: ' + err);
	})
	
	axios.get('/carbon/market/history',{
		params: {memberId: memberId}
	})
	.then(response => {
		if(response.data != ''){
			ordrHistory(response.data);
		}
	})
	.catch(err => {
		console.log('err: ' + err);
	})
}




function activeList(data){
	let sellListings = document.getElementById('sellListings1');
	let buyOrders = document.getElementById('buyOrders1');
	let sellListHtml =``;
	let buyOrderHtml =``;
	data.forEach((order) => {
		const createTime = new Date(order.createTime).toISOString().split('T')[0];
		// 拆成2個axios
		if(order.sell != null){
			sellListHtml +=`
			<ul class="nk-forum">
				<li class="p-10">
		            <div class="nk-forum-activity me-3 d-flex">
		            	<img src="/carbon/market/downloadImage/${order.itemId}" alt="${order.gameItem.itemImgName}"
		                        class="img-fluid" style="max-width: 50px">
		            </div>
		            <div class="nk-forum-title my-auto">
		            	<h3>${order.gameItem.itemName}</h3>
		            </div>
		            <div class="nk-forum-activity my-auto">
		                <div class="nk-forum-activity-title text-center">
		                    ${createTime}
		                </div>
		            </div>
		            <div class="nk-forum-activity my-auto d-flex justify-content-center">
		                <div class="nk-forum-activity-title">
		                    NT$ ${order.price}
		                </div>
		            </div>
		            <div class="nk-forum-activity text-center my-auto d-flex justify-content-center">
		            	<a href="#" class="btn btn-danger deleteBtn" data-toggle="modal" data-target="#modalDelete" data-id="${order.ordId}">
		                    棄單
		                </a>
		            </div>
		        </li>
		    </ul>
			`;
		} else if(order.buy != null){
			buyOrderHtml +=`
			<ul class="nk-forum">
				<li class="p-10">
		            <div class="nk-forum-activity me-3 d-flex">
		            	<img src="/carbon/market/downloadImage/${order.itemId}" alt="${order.gameItem.itemImgName}"
		                        class="img-fluid" style="max-width: 50px">
		            </div>
		            <div class="nk-forum-title my-auto">
		            	<h3>${order.gameItem.itemName}</h3>
		            </div>
		            <div class="nk-forum-activity my-auto">
		                <div class="nk-forum-activity-title text-center">
		                    ${order.buy.userId}
		                </div>
		            </div>
		            <div class="nk-forum-activity my-auto d-flex justify-content-center">
		                <div class="nk-forum-activity-title">
		                    NT$ ${order.price}
		                </div>
		            </div>
		            <div class="nk-forum-activity text-center my-auto d-flex justify-content-center">
		            	<a href="#" class="btn btn-danger deleteBtn" data-toggle="modal" data-target="#modalDelete" data-id="${order.ordId}">
		                    棄單
		                </a>
		            </div>
		        </li>
		    </ul>
			`;
		}
	})
	sellListings.innerHTML = sellListHtml;
	buyOrders.innerHTML = buyOrderHtml;
}


function activeList(data){
	let orderHistory = document.getElementById('orderHistory1');
	let sellListHtml =``;
	let buyOrderHtml =``;
	data.forEach((order) => {
		const createTime = new Date(order.createTime).toISOString().split('T')[0];
		const orderTime = new Date(order.itemOrder.createTime).toISOString().split('T')[0];
		sellListHtml +=`
		<ul class="nk-forum">
			<li class="p-10">
	            <div class="nk-forum-activity me-3 d-flex">
	            	<img src="/carbon/market/downloadImage/${order.itemId}" alt="${order.gameItem.itemImgName}"
	                        class="img-fluid" style="max-width: 50px">
	            </div>
	            <div class="nk-forum-title my-auto">
	            	<h3>${order.gameItem.itemName}</h3>
	            </div>
	            <div class="nk-forum-activity my-auto">
	                <div class="nk-forum-activity-title text-center">
	                    ${orderTime}
	                </div>
	            </div>
	            <div class="nk-forum-activity my-auto">
	                <div class="nk-forum-activity-title text-center">
	                    ${createTime}
	                </div>
	            </div>
	            <div class="nk-forum-activity my-auto d-flex justify-content-center">
	                <div class="nk-forum-activity-title">
	                    NT$ ${order.price}
	                </div>
	            </div>
	            <div class="nk-forum-activity text-center my-auto d-flex justify-content-center">
	            	<a href="#" class="btn btn-danger deleteBtn" data-toggle="modal" data-target="#modalDelete" data-id="${order.ordId}">
	                    棄單
	                </a>
	            </div>
	        </li>
	    </ul>
		`;


// --------------------------- sell an item ---------------------------  

$('#sellPageBtn1').click(function () {
    if (userId != '') {
        loadInventoryAjax();
    } else {
        loginPage();
    }
})


// --------------------------- get itemLog by memberId ---------------------------  
function loadInventoryAjax() {
    axios.get('/carbon/profiles/inventory/' + userId)
        .then(response => {
            //console.log('response: '+ JSON.stringify(response))
            if (response.data != '' && response.data[0].total >= 1) {
                inventoryPage(response.data);
            } else {
                $('#noItem1').removeClass('d-none');
            }
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}


// --------------------------- inventory page ---------------------------  
function inventoryPage(data) {
    let itemListHtml = `
    	<div class="nk-box-1 bg-dark-3">
			<h5>從虛寶倉庫中選擇一項物品</h5>
			<div class="d-flex col mb-3 flex-wrap">
		    	<div class="col-xl-7 col-12 border-end border-secondary">
		    		<div class="row vertical-gap text-white">
    	`;
    data.forEach((item, index) => {
        for (i = 0; i < item.total; i++) {
            itemListHtml += `   
            <div class="col-3 px-1">
            	<div class="nk-box-5 bg-dark-4">
		        	<div class="nk-gallery-item-group">
	                	<a href="#" class="nk-gallery-item itemCards" data-index="${index}">
		                    <img src="/carbon/market/downloadImage/${item.itemId}" alt="${item.gameItem.itemImgName}">
		                </a>
	                </div> 
                </div> 
            </div>`;
        }
    });
    itemListHtml += `
	        </div>
        </div>
        <div class="col-xl-5 col-12" id="oneItem1">
            <div class="nk-popup-gallery col">
        		<div class="nk-gallery-item-box">
                	<img src="/carbon/market/downloadImage/${data[0].itemId}" alt="${data[0].gameItem.itemImgName}">
                </div>
            </div>
            <h3>${data[0].gameItem.itemName}</h3>
            <div>${data[0].gameItem.itemDesc}</div>
            <div class="nk-gap-3"></div>
        `;
    if (data[0].gameItem.status != 0) {
        itemListHtml += ` 
			<div class="position-absolute bottom-0 end-0"> 
				<a href="#" class="btn btn-info sellBtn" data-toggle="modal" data-target="#modalSalesPage" id="sellBtn1" data-itemId="${data[0].itemId}">
                    販賣
                </a>          
	        </div>`;
    } else {
        itemListHtml += ` 
			<div class="d-flex justify-content-between position-absolute bottom-0">           
		        <div class="text-main-1">此道具不可買賣</div>
	            <button class="btn btn-secondary" disabled>販賣</button>
	        </div>
	        	`;
    }
    itemListHtml += `
	        </div>
        </div>
    </div>
    `;

    inventoryList.innerHTML = itemListHtml;
    showItemInfo(data);
    showSalePage(data[0]);
}


// --------------------------- one gameitem info ---------------------------  
function showItemInfo(data) {
    const itemCards = document.getElementsByClassName('itemCards');
    for (i = 0; i < itemCards.length; i++) {
        itemCards[i].addEventListener('click', function () {
            let id = this.getAttribute('data-index');
            let oneItem = document.getElementById('oneItem1');
            let oneItemHtml = `
            	<div class="nk-popup-gallery col">
	        		<div class="nk-gallery-item-box">
	                	<img src="/carbon/market/downloadImage/${data[id].itemId}" alt="${data[id].gameItem.itemImgName}">
	                </div>
	            </div>
	            <h3>${data[id].gameItem.itemName}</h3>
	            <div>${data[id].gameItem.itemDesc}</div>
	            <div class="nk-gap-3"></div>`;
	            
            if (data[id].gameItem.status != 0) {
                oneItemHtml += `            
	               <div class="position-absolute bottom-0 end-0"> 
						<a href="#" class="btn btn-info sellBtn" data-toggle="modal" data-target="#modalSalesPage" id="sellBtn1" data-itemId="${data[id].itemId}">
		                    販賣
		                </a>          
			        </div>`;
            } else {
                oneItemHtml += `  
					 <div class="mt-3 d-flex justify-content-between">          
				         <div class="text-main-1">此道具不可買賣</div>
			             <button class="btn btn-secondary" disabled>販賣</button>
			         </div>
			        	`;
            }
            oneItem.innerHTML = oneItemHtml;
            showSalePage(data[id])
        })
    }
}


// --------------------------- sell page ---------------------------  
function showSalePage(data) {
    const sellBtn = document.getElementById('sellBtn1');
    sellBtn.addEventListener('click', function () {
        let itemId = this.getAttribute('data-itemId');
        console.log('itemId: ' + itemId);
        axios({
            url: '/carbon/market/medianPrice',
            method: 'get',
            params: {
                itemId: itemId
            }
        })
            .then(response => {
                showSaleInfo(data, response.data);
            })
            .catch(err => {
                console.log('err: ' + err);
            })
    })
}


// --------------------------- sale info --------------------------- 
var salesPriceChart;
function showSaleInfo(order, medianPrice) {
    let saleItem = document.getElementById('saleItem1');
    let saleHtmlString = `
        <div class="d-flex border-bottom border-secondary">
        	<div class="nk-popup-gallery col-4">
        		<div class="nk-gallery-item-box">
                	<img src="/carbon/market/downloadImage/${order.itemId}" alt="${order.gameItem.itemImgName}">
                </div>
            </div>
            <div class="col-8">
                <h3>${order.gameItem.itemName}</h3>
                <span>${order.gameItem.game.gameName}</span>
            </div>
        </div>
        <hr class="text-white">`;
        saleHtmlString += `
            <div id="noHistory1">
            	<canvas class="salesPriceChart1"></canvas>
            </div>`;
    saleHtmlString += `
        <hr>
        <form class="nk-form needs-validation border-top border-secondary" novalidate>
            <div class="my-3 row text-white">
                <label class="col-sm-3 col-form-label">價格: </label>
                <div class="col-3">
                	<input type="text" id="salePrice" class="form-control" value="NT$" required>
                </div>
                <label class="col-sm-3 col-form-label">買方支付: </label>
                <div class="col-3">
	                <input type="text" id="buyPrice" class="form-control" value="NT$"><span>(含稅)</span>
                </div>
                <div class="invalid-feedback">
                    必須輸入有效的價格..
                </div>
            </div>
            <div class="d-flex justify-content-between">
            	<div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" id="invalidCheck" required>
                    <label class="form-check-label" for="invalidCheck">
                         我同意協議條款
                    </label>
                    <div class="invalid-feedback">
                        必須勾選同意條款
                    </div>
                </div>
                <button class="btn btn-info sellSubmitBtn" id="sellSubmitBtn1">確定上架</button>
            </div>
        </form>
    `;
    saleItem.innerHTML = saleHtmlString;
    
    if(medianPrice != ''){
	    medianPriceChart(medianPrice);
	} else {
		let noHistory = document.getElementById('noHistory1');
		noHistory.innerHTML = `<h4 class="m-10 text-center">此物品尚無歷史價格</h4>`;
	}
    
    let salePrice;
    let buyPrice;
    $('#salePrice,#buyPrice').on('keyup change', function () {
        salePrice = Number($('#salePrice').val().replace('NT$', ''));
        if($('#salePrice').val().indexOf('NT$') < 0){
	        $('#salePrice').val('NT$ 0');
		}
        if(salePrice == 0 || isNaN(salePrice)){
			$('#salePrice').val('NT$  --');
		} else{
        	$('#buyPrice').val('NT$ ' + salePrice);
		}
    })
    $('#salePrice,#buyPrice').on('keyup change', function () {
        buyPrice = Number($('#buyPrice').val().replace('NT$', ''));
        if($('#buyPrice').val().indexOf('NT$') < 0){
	        $('#buyPrice').val('NT$ 0');
		}
        if(buyPrice == 0 || isNaN(buyPrice)){
			$('#buyPrice').val('NT$  --');
		} else{
        	$('#salePrice').val('NT$ ' + buyPrice);
		}
    })
    
    // ------------- Validation -------------   
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    const forms = document.querySelectorAll('.needs-validation')
    // Loop over them and prevent submission
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            } else {
	            event.preventDefault()
	            let price = salePrice;
	            console.log(price)
	            axios({
	                url: '/carbon/market/done',
	                method: 'post',
	                data: {
	                    itemId: order.itemId,
	                    seller: userId,
	                    quantity: 1,
	                    price: price,
	                }
	            })
	                .then(response => {
	                    if (response.data != '') {
	                        let newSales = response.data;
	                        console.log('itemId: ' + newSales.itemId)
	                        axios({
	                            method: 'get',
	                            url: '/carbon/market/checkBuys',
	                            params: { itemId: newSales.itemId }
	                        })
	                            .then(res => {
	                                let sales = res.data;
	                                //console.log(JSON.stringify(sales));
	                                if (sales != '' && sales[0].price >= price) {
	                                    console.log('res: ' + JSON.stringify(sales[0].price))
	                                    console.log('price: ' + price)
	                                    axios({
	                                        url: '/carbon/market/done',
	                                        method: 'post',
	                                        data: {
	                                            itemId: sales[0].itemId,
	                                            buyer: sales[0].buyer,
	                                            seller: userId,
	                                            quantity: 1,
	                                            price: price,
	                                        }
	                                    })
	                                        .then(result => {
	                                            //console.log('result: ' + JSON.stringify(result.data))
	                                            if (result.data != '') {
	                                                orderUpdate(sales[0]);
	                                                orderUpdate(newSales);
	                                            }
	                                            return result.data;
	                                        })
	                                        .catch(err => {
	                                            console.log('err: ' + err);
	                                        })
	                                }
	                            })
	                    }
	                    return response.data;
	                })
	                .then(result => {
	                    if (result != null) {
	                        // showSuccessPage(result);
	
	
	                    }
	                })
	                .catch(err => {
	                    console.log('err: ' + err);
	                })
			}

            form.classList.add('was-validated')

        }, false)
    })

}

// =========================== medianPrice charts.js ===========================   
function medianPriceChart(data){
	const sellPriceData = data.map(order => ({
		 time: order.time, 
		 medianPrice: order.medianPrice, 
	}));
	const decimation = {
	  enabled: false,
	  algorithm: 'min-max',
	};
	const footer = (tooltipItems) => {
	  let sold = 0;
	  tooltipItems.forEach(function(tooltipItem) {
		 // console.log(tooltipItem)
		  const date = new Date(tooltipItem.label);
		  const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}`;
		  data.forEach(order=>{
			  if(formattedDate === order.time && tooltipItem.raw == order.medianPrice){
				  sold += order.total;
			  }
		  })
	  });
	  return '賣出: ' + sold
	};
	//console.log(sellPriceData)
    salesPriceChart = new Chart(
    $('.salesPriceChart1'),
    {
      type: 'line',
      data: {
        labels: sellPriceData.map(row => row.time),
        datasets: [{
          label: '中位價格 NT$',
          data: sellPriceData.map(row => row.medianPrice),
        }]
      },
      options: {
	    animation: false,
	    //parsing: false,
	    interaction: {
	      mode: 'nearest',
	      axis: 'x',
	      intersect: false
	    },
	    plugins: {
	      decimation: decimation,
	      title: {
	        display: true,
	        text: '中位販售價格',
	        align: 'start',
	        color: '#fff',
	        font: { size: 16 }
	      },
	      legend: {
              display: true,
              align: 'end',
              labels: {
                  color: '#fff',
                  font: { size: 16 }
              }
          },
	      tooltip: {
	        callbacks: {
	          footer: footer,
	        }
	      }
	    },
	    scales: {
	      x: {
	        type: 'time',
	        time: {
                unit: 'day'
            },
	        ticks: {
	          source: 'auto',
	          maxRotation: 0,
	          autoSkip: true,
			  color: '#fff',
			  font: { size: 16 }
	        }
	      },
	      y: {
			  ticks: {
	            color: '#fff',
	            font: { size: 16 }
	          }
		  }
	    },
	  },
    }
  );
}



// =========================== redirect to login page ===========================          
function loginPage() {
    let LoginMsg = document.getElementsByClassName('LoginMsg');
    let loginHtmlString = `
	    <div class="modal-dialog modal-lg" role="document">
	        <div class="modal-content">
	            <div class="modal-body">
	                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	                    <span class="ion-android-close"></span>
	                </button>
			        <h4 class="text-center my-5">您需要登入或建立帳戶才能進行相關動作。</h4>
			        <div class="gap-2 row col d-flex justify-content-center" id="btnGroup">
			            <a class="btn btn-info submitBtn col-md-3" href="/carbon/main/loginPage">登入</a>
			            <a class="btn btn-info col-md-3" href="/carbon/main/registerPage">註冊帳號</a>
			            <a class="btn btn-info closeBtn2 col-md-2" href="#" class="close" data-dismiss="modal" aria-label="Close">取消</a>
			        </div>
			    </div>
			</div>
        </div>`;
    for (i = 0; i < LoginMsg.length; i++) {
    	LoginMsg[i].innerHTML = loginHtmlString;
    }
}


// =========================== 控制多個modal的scroll ===========================
$('#modalSalesPage').on('hidden.bs.modal', function (e) {
	$('body').addClass('modal-open');
});


