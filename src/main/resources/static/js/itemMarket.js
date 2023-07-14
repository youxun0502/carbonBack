const buyBtn = document.getElementsByClassName('buyBtn');
const show = document.getElementById('showPage1');
const salePage = document.getElementById('showSalePage1');
const buyPageInfo = document.getElementById('buyPageInfo');
const inventoryList = document.getElementById('inventoryList1');
const userId = document.getElementById('user1').textContent;
console.log('userId: ' + userId);
let myWallet ;
let removeHtml;

// =========================== itemMarketPage ===========================  
// --------------------------- wallet ---------------------------
$(document).ready(function(){
	if(userId != ''){
		axios.get('/carbon/profile/myWallet', {
			params: { memberId: userId}
		})
		.then(response => {
			console.log(response.data.balance)
			if(response.data != ''){
				$('.myWallet2').html(`<span class="myWallet3" data-balance="${response.data.balance}">錢包餘額 : NT$ ${response.data.balance}</span>`);
				$('.myWallet2').attr('data-balance', response.data.balance);
			} else {
				$('.myWallet2').html(`<span class="myWallet3" data-balance="${response.data.balance}">錢包餘額 : NT$ 0</span>`);
				$('.myWallet2').attr('data-balance', 0);
			}
		})
		.catch(err => {
			console.log('err: ' + err);
		})
	}
	
})

// --------------------------- load medianPrice ---------------------------
$(function(){
	if(document.getElementById('itemId1') != null){
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
	}
})
// --------------------------- buy an item page ---------------------------
for (i = 0; i < buyBtn.length; i++) {
    buyBtn[i].addEventListener('click', function (e) {
        e.preventDefault();
        removeHtml = $(this).closest('li');
        console.log(removeHtml);
        let buyId = this.getAttribute('data-id');
        if (userId != '') {
        	buyAnItem(buyId);
        } else {
            loginPage();
        }
    })
}


// --------------------------- get order info ---------------------------        
function buyAnItem(buyId) {
    axios.get('/carbon/market/buyAnItem',
        { params: { ordId: buyId,
        			id: userId			
         } }
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
        <div class="d-flex justify-content-between align-items-center">
            <div class="d-flex overflow-hidden col-6 align-items-center">
                <img src="/carbon/market/downloadImage/${order.itemId}"
                    alt="${order.gameItem.itemImgName}" class="img-fluid" style="max-width: 80px">
                <div class="ms-1">${order.gameItem.itemName}</div>
            </div>
            <div class="col-3">${order.sell.userId}</div>
            <div class="col-2">$ ${order.price}</div>
        </div>`;

    buyPageInfo.innerHTML = infoHtmlString;
    console.log()
    if(order.needFund != 1){
		document.getElementById('changeAddFund1').innerHTML=`
			<a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-white float-right" href="/carbon/profile/wallet" id="addFunds">儲值</a>
		`;
	}
	
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
					params: {ordId: result.ordId,
							 id: userId}
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
            <div class="col-2">${order.sell.userId}</div>
            <div class="col-2">$ ${order.price}</div>
        </div>
        <div class="gap-2 col d-flex justify-content-end" id="btnGroup">
            <a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-main-1 submitBtn col-3" href="/carbon/profile/${userId}/inventory">查看物品庫</a>
            <a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-dark-3 closeBtn2 col-md-2" href="#" class="close" data-dismiss="modal" aria-label="Close">關閉</a>
        </div>`;
    buyPageInfo.innerHTML = infoHtmlString;
    $('#pageMsg').removeClass('d-none');
    $('#userInfo1').addClass('d-none');
}


// --------------------------- buy order ---------------------------  
$('#buyOrder1').on('click', function () {
	let buyId = $(this).attr('data-buyId');
    if (userId == '') {
        loginPage();
    } 
    var buyPrice;
    var buyQuantity;
    var maxPrice;
    if(0 >= $('.myWallet2').attr('data-balance')){
		document.getElementById('changeAddFund2').innerHTML=`
			<a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-white" href="/carbon/profile/wallet" id="addFunds">儲值</a>
		`;
	}
    
    $('#buyPrice1,#buyQuantity1').on('keyup', function () {
        buyPrice = Number($('#buyPrice1').val().replace('NT$', ''));
        buyQuantity = Number($('#buyQuantity1').val());
        maxPrice = $('#maxPrice1');
        if($('#buyPrice1').val().indexOf('NT$') < 0 ){
	        $('#buyPrice1').val('NT$ 0');
		}
        if(buyPrice * buyQuantity == 0 || isNaN(buyPrice * buyQuantity)){
			maxPrice.val('NT$  --');
		} else{
        	maxPrice.val('NT$ ' + buyPrice * buyQuantity);
		}
		
	    if(maxPrice.val().replace('NT$', '') > $('.myWallet2').attr('data-balance')){
			document.getElementById('changeAddFund2').innerHTML=`
				<a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-white" href="/carbon/profile/wallet" id="addFunds">儲值</a>
			`;
		}
    })
    
	
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
	                        checkSales(buyId, buyPrice, orders);
	                    }
	                    return response.data;
	                })
	                .then(result => {
	                    if (result != null) {
	                        let bohtml = `
	                        	<h3 class="text-center">新增購買訂單成功</h3>
	                        	<div class="mt-3 d-flex justify-content-end">
						            <a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-dark-3 closeBtn2 col-md-2" href="#" class="close" data-dismiss="modal" aria-label="Close">關閉</a>
						        </div>
	                        `;
							$('#buyOrderSuccess').html(bohtml);
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


// --------------------------- 確認賣單價錢 --------------------------- 
function checkSales(buyId, buyPrice, orders){
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
	    .catch(err => {
			console.log('err: ' + err)
		})
}


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
                        [[${order.sell.userId}]]
                    </div>
                </div>
                <div class="nk-forum-activity my-auto d-flex justify-content-center">
                    <div class="nk-forum-activity-title">
                        NT$ [[${order.price}]]
                    </div>
                </div>
                <div class="nk-forum-activity text-center my-auto d-flex justify-content-center">
                	<a href="#" class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-main-1 buyBtn" data-toggle="modal" data-target="#modalBuyPage" th:data-id="${order.ordId}">
                        立即購買
                    </a>
                </div>
            </li>`;
	})
		sellListHtml += `</ul>`;
}


// =========================== itemMarketList ===========================
// --------------------------- list page ajax ---------------------------
function loadItemList(){
	axios.get('/carbon/market/page')
		.then(response => {
	        loadListPage (response.data);
	    })
	    .catch(err => {
			console.log('err: ' + err);
		})
}
		
function loadListPage (data){
	let itemListHtml = ``;
	data.content.forEach(order => {
		itemListHtml += `
		    <div class="col-md-4 col-6">
		        <div class="nk-blog-post border border-secondary px-3 gameItem">
		            <a href="/carbon/market/${order.gameItem.gameId}/${order.itemId}/${order.gameItem.itemName}" class="nk-post-img cb-a">
		                <img src="/carbon/market/downloadImage/${order.itemId}" alt="${order.gameItem.itemImgName}">
		                <h2 class="nk-post-title h5" style="height: 45px">
		                	${order.gameItem.itemName}
		                </h2>
		                <div class="text-main-1" style="height: 58px">起跳價格: `;
		if(order.minPrice != null){
				itemListHtml += `
		                    <h3 class="nk-post-title h5 text-end">NT$ ${order.minPrice}</h3>`;
		} else {
				itemListHtml += `
		                    <h3 class="nk-post-title h5 text-end">NT$ --</h3>`;
		}
		itemListHtml += `
		               	</div>
		            </a>
		            <div class="nk-post-by">
		                <img src="/carbon/gameFront/getImg/${order.gameItem.game.gamePhotoLists[0].photoId}" alt="" class="rounded-circle" width="35">
		                <a href="/carbon/gameFront/${order.gameItem.game.gameName}">${order.gameItem.game.gameName}</a>
		            </div>
		            <div class="nk-gap"></div>
		        </div>
		    </div>`;
	})
	
	$('#listPage1').html(itemListHtml);
	
	let listPageHtml = ``;
	let totalPages = data.totalPages;
	let thisPage = data.pageable.pageNumber;
	console.log(totalPages)
	console.log(thisPage)
	listPageHtml +=`
				<div class="nk-gap-2"></div>
		        <div class="nk-pagination nk-pagination-center">
		            <a href="#" class="pageBtn nk-pagination-prev" data-pageid="${thisPage}">
		                <span class="ion-ios-arrow-back"></span>
		            </a>
		            <nav>`;
		    for(i = 1; i <= totalPages ;i++){
				if(i == thisPage + 1){
					listPageHtml +=`
		                <a class="pageBtn nk-pagination-current" href="#" data-pageid="${i}">${i}</a>`;
				} else {
					listPageHtml +=`
		                <a class="pageBtn" href="#" data-pageid="${i}">${i}</a>`;
				}
			}
			listPageHtml +=`
		            </nav>
		            <a href="#" class="pageBtn nk-pagination-next" data-pageid="${thisPage + 2}">
		                <span class="ion-ios-arrow-forward"></span>
		            </a>
		        </div>`;
		        
			$('#itemListPage1').html(listPageHtml);
			
			let pageBtns = document.getElementsByClassName('pageBtn');
		    for (i = 0; i < pageBtns.length; i++) {
		        pageBtns[i].addEventListener('click', function (e) {
					e.preventDefault();
		            let pageId = this.getAttribute('data-pageid');
		            console.log('pageId: ' + pageId);
		            if (pageId == 0 || pageId == totalPages + 1) {
		                //this.removeAttribute('href');
		            } else {
		                loadPage(pageId);
		            }
		        });
		    }
	
}


function loadPage(thatPage) {
	axios.get('/carbon/market/page',{
			params: { p: thatPage }
		})
		.then(response => {
            loadListPage(response.data);
        })
        .catch(err => {
			console.log('err: ' + err);
		})
}

let itemName
let gameId 
// --------------------------- search by itemName or gameId ---------------------------
function findByNameAndGame(){
	itemName = $('input[name="itemName"]').val() ? $('input[name="itemName"]').val() : '';
	gameId = $('select[name="gameId"]').val() ? $('select[name="gameId"]').val() : 0;
	console.log('itemName: ' + itemName)
	console.log('gameId: '+ gameId)
	axios.get('/carbon/market/page/find', {
		params: {
			gameId: gameId,
			itemName: itemName
		}
	})
	.then(response => {
		if(response.data.content != ''){
			loadSearchPage(response.data);
		} else {
			$('#listPage1').html('<h3 class="text-center">查無資料</h3>');
			$('#itemListPage1').html('')
		}
	})
	.catch(err => {
		console.log('err: ' + err);
	})
}

function loadSearchPage (data){
	let itemListHtml = ``;
	data.content.forEach(order => {
		itemListHtml += `
		    <div class="col-md-4 col-6">
		        <div class="nk-blog-post border border-secondary px-3 gameItem">
		            <a href="/carbon/market/${order.gameItem.gameId}/${order.itemId}/${order.gameItem.itemName}" class="nk-post-img cb-a">
		                <img src="/carbon/market/downloadImage/${order.itemId}" alt="${order.gameItem.itemImgName}">
		                <h2 class="nk-post-title h5" style="height: 45px">
		                	${order.gameItem.itemName}
		                </h2>
		                <div class="text-main-1" style="height: 58px">起跳價格: `;
		if(order.minPrice != null){
				itemListHtml += `
		                    <h3 class="nk-post-title h5 text-end">NT$ ${order.minPrice}</h3>`;
		} else {
				itemListHtml += `
		                    <h3 class="nk-post-title h5 text-end">NT$ --</h3>`;
		}
		itemListHtml += `
		               	</div>
		            </a>
		            <div class="nk-post-by">
		                <img src="/carbon/gameFront/getImg/${order.gameItem.game.gamePhotoLists[0].photoId}" alt="" class="rounded-circle" width="35">
		                <a href="/carbon/gameFront/${order.gameItem.game.gameName}">${order.gameItem.game.gameName}</a>
		            </div>
		            <div class="nk-gap"></div>
		        </div>
		    </div>`;
	})
	
	$('#listPage1').html(itemListHtml);
	
	let listPageHtml = ``;
	let totalPages = data.totalPages;
	let thisPage = data.pageable.pageNumber;
	console.log(totalPages)
	console.log(thisPage)
	listPageHtml +=`
				<div class="nk-gap-2"></div>
		        <div class="nk-pagination nk-pagination-center">
		            <a href="#" class="pageBtn nk-pagination-prev" data-pageid="${thisPage}">
		                <span class="ion-ios-arrow-back"></span>
		            </a>
		            <nav>`;
		    for(i = 1; i <= totalPages ;i++){
				if(i == thisPage + 1){
					listPageHtml +=`
		                <a class="pageBtn nk-pagination-current" href="#" data-pageid="${i}">${i}</a>`;
				} else {
					listPageHtml +=`
		                <a class="pageBtn" href="#" data-pageid="${i}">${i}</a>`;
				}
			}
			listPageHtml +=`
		            </nav>
		            <a href="#" class="pageBtn nk-pagination-next" data-pageid="${thisPage + 2}">
		                <span class="ion-ios-arrow-forward"></span>
		            </a>
		        </div>`;
		        
			$('#itemListPage1').html(listPageHtml);
			
			let pageBtns = document.getElementsByClassName('pageBtn');
		    for (i = 0; i < pageBtns.length; i++) {
		        pageBtns[i].addEventListener('click', function (e) {
					e.preventDefault();
		            let pageId = this.getAttribute('data-pageid');
		            console.log('pageId: ' + pageId);
		            if (pageId == 0 || pageId == totalPages + 1) {
		                //this.removeAttribute('href');
		            } else {
		                changeSearchPage(pageId);
		            }
		        });
		    }
	
}


function changeSearchPage(thatPage) {
	axios.get('/carbon/market/page/find', {
		params: {
			gameId: gameId,
			itemName: itemName,
			p: thatPage
		}
	})
	.then(response => {
        loadListPage(response.data);
    })
    .catch(err => {
		console.log('err: ' + err);
	})
}


// --------------------------- active listing ajax ---------------------------
function saleList(){
	if(userId != ''){
		axios.get('/carbon/market/saleList',{
			params: {memberId: userId}
		})
		.then(response =>{
			if(response.data.content != ''){
				activeList(response.data, 'saleList');
			} 
		})
		.catch(err => {
			console.log('err: ' + err);
		})
	} else {
		$('.loginToView').html('<h3>請先登入以查看上架訂單</h3>');
	}
}

function buyOrder(){
	if(userId != ''){
		axios.get('/carbon/market/buyOrder',{
			params: {memberId: userId}
		})
		.then(response =>{
			if(response.data.content != ''){
				activeList(response.data, 'buyOrder');
			} 
		})
		.catch(err => {
			console.log('err: ' + err);
		})
	} else {
		$('.loginToView').html('<h3>請先登入以查看上架訂單</h3>');
	}
}


// --------------------------- order history ajax ---------------------------
function history(){
	if(userId != ''){
		axios.get('/carbon/market/history',{
			params: {memberId: userId}
		})
		.then(response => {
			if(response.data.content != ''){
				orderHistory(response.data);
			} else {
				
			}
		})
		.catch(err => {
			console.log('err: ' + err);
		})
	} else {
		$('.loginToViewH').html('<h3>請先登入以查看歷史訂單</h3>');
	}
}


// --------------------------- show active listing ---------------------------
function activeList(data, elm){
	let sellListings = document.getElementById('sellListings1');
	let buyOrders = document.getElementById('buyOrders1');
	let activeListHtml =``;
	data.content.forEach((order) => {
		let createTime = new Date(order.createTime).toISOString().split('T')[0];
			activeListHtml +=`
			<ul class="nk-forum text-white">
				<li class="p-10">
					<div class="nk-forum-icon me-3">
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
		            </div>`;
		if(order.sell != null){
			activeListHtml +=`
		            <div class="nk-forum-activity my-auto d-flex justify-content-center">
		                <div class="nk-forum-activity-title">
		                    NT$ ${order.price}
		                </div>
		            </div>
		            <div class="nk-forum-activity text-center my-auto d-flex justify-content-center">
		            	<a href="#" class="btn btn-danger deleteBtn" data-toggle="modal" data-target="#modalDelete" data-id="${order.ordId}" data-type="sell">
		                    移除
		                </a>
		            </div>
		            `;
		} else if(order.buy != null){
			activeListHtml +=`
				<div class="nk-forum-activity my-auto">
		                <div class="nk-forum-activity-title text-center">
		                    ${order.quantity}
		                </div>
		            </div>
		            <div class="nk-forum-activity my-auto d-flex justify-content-center">
		                <div class="nk-forum-activity-title">
		                    NT$ ${order.price}
		                </div>
		            </div>
		            <div class="nk-forum-activity text-center my-auto d-flex justify-content-center">
		            	<a href="#" class="btn btn-danger deleteBtn" data-toggle="modal" data-target="#modalDelete" data-id="${order.ordId}" data-type="buy">
		                    移除
		                </a>
		            </div>
		            `;
		}
			activeListHtml +=`
		        </li>
		    </ul>
			`;
	})
	switch(elm){
		case 'saleList' :
			sellListings.innerHTML = activeListHtml;
			break;
		case 'buyOrder' :
			buyOrders.innerHTML = activeListHtml;
			break;
	}
	
	let deleteBtn = document.getElementsByClassName('deleteBtn');
	for(i = 0; i < deleteBtn.length; i++){
		deleteBtn[i].addEventListener('click', function(){
			let ordId =  this.getAttribute('data-id');
			let ordType =  this.getAttribute('data-type');
			console.log(ordType);
			
			Swal.fire({
				title: '確定要移除此筆訂單?',
				text: "注意! 此操作不可復原!",
				icon: 'warning',
				iconColor: '#ef9e2b',
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: '確定移除',
				cancelButtonText: '取消'
			}).then((result) => {
				if (result.isConfirmed) {
					axios({
					    url: '/carbon/market/orderUpdate',
					    method: 'put',
					    data: {
					        ordId: ordId,
					        status: 0
					    }
					})
					.then(response => {
						if(response.data != null){
							if(ordType == 'sell'){
								axios({
							        url: '/carbon/market/newItemLog',
							        method: 'post',
							        data: {
							            ordId: response.data.ordId,
							            itemId: response.data.itemId,
							            memberId: response.data.seller,
							            quantity: response.data.quantity,
							        }
							    })
							        .then(res => {
									})
							        .catch(err => {
							            console.log('err: ' + err);
							        })
							}
							saleList();
							buyOrder();
						}
					})
					.then(function(){
						Swal.fire(
							'移除成功!',
							'此筆訂單已被移除',
							'success'
						)
					})
					.catch(err => {
						console.log('err: ' + err);
						Swal.fire({
							icon: 'error',
							title: '哦不...',
							text: '出了些狀況，請稍後再嘗試!',
						})
					})
				}
			})
		})
	}
}


// --------------------------- show order history ---------------------------
function orderHistory(data){
	let orderHistory = document.getElementById('orderHistory1');
	let historyListHtml =``;
	let totalPages = data.totalPages;
    let thisPage = data.pageable.pageNumber;
	data.content.forEach((order) => {
		let createTime = new Date(order.createTime).toISOString().split('T')[0];
		let orderTime = new Date(order.itemOrder.createTime).toISOString().split('T')[0];
		historyListHtml +=`
		<ul class="nk-forum text-white">
			<li class="p-10">
				<div class="nk-forum-icon me-3">
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
	                    NT$ ${order.itemOrder.price}
	                </div>
	            </div>
	            <div class="nk-forum-activity my-auto">
		            <div class="nk-forum-activity-title text-center">`;
	    if(order.itemOrder.buyer != null){
		    historyListHtml +=`${order.itemOrder.buy.userId}`;
		} else{
		    historyListHtml +=`${order.itemOrder.sell.userId}`;
		}
	     historyListHtml +=`
		            </div>
	            </div>
	        </li>
	    </ul>
		`;
	})
	
	if(myWallet != ''){
		orderHistory.innerHTML = historyListHtml;
	} else {
		historyListHtml +=`
			<div class="nk-gap-2"></div>
	        <div class="nk-pagination nk-pagination-center">
	            <a href="#" class="pageBtn nk-pagination-prev" data-pageid="${thisPage}">
	                <span class="ion-ios-arrow-back"></span>
	            </a>
	            <nav>`;
	    for(i = 1; i <= totalPages ;i++){
			if(i == thisPage + 1){
				historyListHtml +=`
	                <a class="pageBtn nk-pagination-current" href="#" data-pageid="${i}">${i}</a>`;
			} else {
				historyListHtml +=`
	                <a class="pageBtn" href="#" data-pageid="${i}">${i}</a>`;
			}
		}
		historyListHtml +=`
	            </nav>
	            <a href="#" class="pageBtn nk-pagination-next" data-pageid="${thisPage + 2}">
	                <span class="ion-ios-arrow-forward"></span>
	            </a>
	        </div>`;
		orderHistory.innerHTML = historyListHtml;
		
		let pageBtns = document.getElementsByClassName('pageBtn');
	    for (i = 0; i < pageBtns.length; i++) {
	        pageBtns[i].addEventListener('click', function (e) {
				e.preventDefault();
	            let pageId = this.getAttribute('data-pageid');
	            console.log('pageId: ' + pageId);
	            if (pageId == 0 || pageId == totalPages + 1) {
	                this.setAttribute('disabled', '');
	            } else {
	                loadThatPage(pageId);
	            }
	        });
	    }
		
	} 
}
function loadThatPage(thatPage) {
	axios.get('/carbon/market/history',{
			params: {
				memberId: userId,
				p: thatPage
			}
		})
		.then(response => {
            orderHistory(response.data);
        })
        .catch(err => {
			console.log('err: ' + err);
		})
}


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
    axios.get('/carbon/profile/inventory/' + userId)
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
				<a href="#" class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-main-1 sellBtn" data-toggle="modal" data-target="#modalSalesPage" id="sellBtn1" data-itemId="${data[0].itemId}">
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
        itemCards[i].addEventListener('click', function (e) {
			e.preventDefault();
			console.log('click')
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
						<a href="#" class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-main-1 sellBtn" data-toggle="modal" data-target="#modalSalesPage" id="sellBtn1" data-itemId="${data[id].itemId}">
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
                <button class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-main-1 sellSubmitBtn" id="sellSubmitBtn1">確定上架</button>
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
    $('#salePrice').on('keyup change', function () {
        salePrice = parseFloat($('#salePrice').val().replace('NT$', ''));
        if($('#salePrice').val().indexOf('NT$') < 0){
	        $('#salePrice').val('NT$ 0');
		}
        if(salePrice == 0 || isNaN(salePrice)){
			$('#salePrice').val('NT$  --');
		} else{
        	$('#buyPrice').val('NT$ ' + (salePrice * 1.05).toFixed(2));
		}
    })
    $('#buyPrice').on('keyup change', function () {
        buyPrice = parseFloat($('#buyPrice').val().replace('NT$', ''));
        if($('#buyPrice').val().indexOf('NT$') < 0){
	        $('#buyPrice').val('NT$ 0');
		}
        if(buyPrice == 0 || isNaN(buyPrice)){
			$('#buyPrice').val('NT$  --');
		} else{
        	$('#salePrice').val('NT$ ' + (buyPrice / 1.05).toFixed(2));
		}
    })
    console.log('buy: '+buyPrice)
    console.log('sell: '+salePrice)
    
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
	            let price = $('#buyPrice').val().replace('NT$', '');
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
	                        checkBuys(newSales, price);
	                    }
	                    return response.data;
	                })
	                .then(result => {
	                    if (result != null) {
	                        let bohtml = `
	                    	    <div class="nk-gap"></div>
	                        	<h3 class="text-center">物品成功上架販售</h3>
	                        	<div class="mt-3 d-flex justify-content-end">
						            <a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-dark-3 closeBtn2 col-md-2" href="#" class="close" data-dismiss="modal" aria-label="Close">關閉</a>
						        </div>
	                        `;
							$('#saleItem1').html(bohtml);
							loadInventoryAjax()
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


// --------------------------- 確認買單價錢 --------------------------- 
function checkBuys(newSales, price){
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
        .catch(err => {
			console.log('err: ' + err);
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
		  console.log(tooltipItem)
		  const date = new Date(tooltipItem.label);
		  const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString()}`;
		  data.forEach(order=>{
			  const orderTime = order.time.split(':')[0];
			  if(formattedDate == orderTime && tooltipItem.raw == order.medianPrice){
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
			            <a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-main-1 submitBtn col-md-3" href="/carbon/main/loginPage">登入</a>
			            <a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-white nk-btn-hover-main-1 col-md-3" href="/carbon/main/registerPage">註冊帳號</a>
			            <a class="nk-btn nk-btn-lg nk-btn-rounded nk-btn-color-dark-4 closeBtn2 col-md-2" href="#" class="close" data-dismiss="modal" aria-label="Close">取消</a>
			        </div>
			    </div>
			</div>
        </div>`;
    for (i = 0; i < LoginMsg.length; i++) {
    	LoginMsg[i].innerHTML = loginHtmlString;
    }
}





