const buyBtn = document.getElementsByClassName('buyBtn');
const show = document.getElementById('showPage1');
const salePage = document.getElementById('showSalePage1');
const buyPageInfo = document.getElementById('buyPageInfo');
const inventoryList = document.getElementById('inventoryList1');
const userId = document.getElementById('user1').textContent;
console.log('userId: ' + userId);


// =========================== itemMarketPage ===========================  
// --------------------------- buy an item page ---------------------------
for (i = 0; i < buyBtn.length; i++) {
    buyBtn[i].addEventListener('click', function (e) {
        e.preventDefault();
        let buyId = this.getAttribute('data-id');
        buyAnItem(buyId);
    })
}


// --------------------------- get order info ---------------------------        
function buyAnItem(buyId) {
    axios.get('/carbon/market/buyAnItem',
        { params: { logId: buyId } }
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
        <div class="card mb-2">
            <div class="card-body">
                <div class="card-text col d-flex justify-content-between align-items-center">
                    <div class="imgBox overflow-hidden col-2">
                        <img src="/carbon/market/downloadImage/${order.itemId}"
                            alt="${order.gameItem.itemImgName}" class="img-fluid" style="max-width: 120px">
                    </div>
                    <div class="col-6">${order.gameItem.itemName}</div>
                    <div class="col-2">${order.sell.memberName}</div>
                    <div class="col-1">$ ${order.price}</div>
                </div>
            </div>
        </div>`;

    buyPageInfo.innerHTML = infoHtmlString;
    show.classList.add('d-flex');

    const submitBtn = document.getElementById('submitBtn');
    submitBtn.addEventListener('click', function (e) {
        console.log(order.itemId);
        e.preventDefault();
        newOrder(order, userId);
    })
    closePage();
}


// --------------------------- insert new order ---------------------------        
function newOrder(order, buyer) {
    axios({
        url: '/carbon/market/newOrder',
        method: 'post',
        data: {
            itemId: order.itemId,
            buyer: buyer,
            seller: order.seller,
            quantity: 1,
            price: order.price,
            status: 2,
        }
    })
        .then(response => {
            //console.log('response: ' + JSON.stringify(response.data));
            showSuccessPage(response.data);
            return response.data;
        })
        .then(result => {
            if (result != null) {
                //console.log(JSON.stringify(result));
                orderLogUpdate(order);
                newItemLog(result);
            }
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}


// --------------------------- update orderLog status ---------------------------  		
function orderLogUpdate(order) {
    axios({
        url: '/carbon/market/orderLogUpdate',
        method: 'put',
        data: {
            logId: order.logId,
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


// --------------------------- insert new itemLog ---------------------------  		
function newItemLog(result) {
    axios({
        url: '/carbon/market/newItemLog',
        method: 'post',
        data: {
            ordId: result.logId,
            itemId: result.itemId,
            memberId: result.buyer,
            quantity: result.quantity
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
        <div class="card mb-2">
            <div class="card-body">
                <div class="card-text col d-flex justify-content-between align-items-center">
                    <div class="imgBox overflow-hidden col-2">
                        <img src="/carbon/market/downloadImage/${order.itemId}"
                            alt="" class="img-fluid" style="max-width: 120px">
                    </div>
                    <div class="col-6">{order.gameItem.itemName}</div>
                    <div class="col-2">${order.seller}</div>
                    <div class="col-1">$ ${order.price}</div>
                </div>
            </div>
        </div>
        <div class="gap-2 col d-flex justify-content-end" id="btnGroup">
            <button class="btn btn-info submitBtn col-3">VIEW INVENTORY</button>
            <button class="btn btn-info closeBtn2 col-3">CLOSE</button>
        </div>`;
    buyPageInfo.innerHTML = infoHtmlString;
    $('#pageMsg').removeClass('d-none');
    $('#userInfo1').addClass('d-none');
    closePage();
}


// --------------------------- sell list ajax ---------------------------          
function listPage() {
    let itemList = `
        	
        	`;
}


// =========================== itemMarketList ===========================
// --------------------------- sell an item ---------------------------  

$('#sellPageBtn1').click(function () {
    console.log('click');
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
            inventoryPage(response.data);
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}


// --------------------------- inventory page ---------------------------  
function inventoryPage(data) {
    let itemListHtml = `<div class="card-text col-xl-7 col-12 d-flex flex-wrap">`;
    data.forEach((item, index) => {
        itemListHtml += `    
            <div class="imgBox overflow-hidden col-3">
                <a class="itemCards d-block h-auto position-relative" href="#" data-index="${index}">
                    <img src="/carbon/market/downloadImage/${item.itemId}" alt="${item.gameItem.itemImgName}"
                        class="img-fluid" style="max-width: 110px">
                </a>
            </div>`;
    });
    itemListHtml += `
        </div>
        <div class="card-text col-xl-5 col-12" id="oneItem1">
                <div class="imgBox overflow-hidden col-4">
                    <img src="/carbon/market/downloadImage/${data[0].itemId}" alt="${data[0].gameItem.itemImgName}"
                        class="img-fluid" style="max-width: 120px">
                </div>
                <hr>
                <h3>${data[0].gameItem.itemName}</h3>
                <div>${data[0].gameItem.itemDesc}</div>
            <div class="mt-3 d-flex justify-content-end">
                <button class="btn btn-info sellBtn" id="sellBtn1" data-itemId="${data[0].itemId}">SELL</button>
            </div>
        </div>
    `;

    inventoryList.innerHTML = itemListHtml;
    show.classList.add('d-flex');
    showItemInfo(data);
    showSalePage();
    closePage();
}


// --------------------------- one gameitem info ---------------------------  
function showItemInfo(data) {
    const itemCards = document.getElementsByClassName('itemCards');
    for (i = 0; i < itemCards.length; i++) {
        itemCards[i].addEventListener('click', function () {
            let id = this.getAttribute('data-index');
            let oneItem = document.getElementById('oneItem1');
            let oneItemHtml = `
                <div class="imgBox overflow-hidden col-4">
                    <img src="/carbon/market/downloadImage/${data[id].itemId}" alt="${data[id].gameItem.itemImgName}"
                        class="img-fluid" style="max-width: 120px">
                </div>
                <hr>
                <h3>${data[id].gameItem.itemName}</h3>
                <div>${data[id].gameItem.itemDesc}</div>
                <div class="mt-3 d-flex justify-content-end">
                	<button class="btn btn-info sellBtn" id="sellBtn1" data-itemId="${data[id].itemId}">SELL</button>
            	</div>
            `;
            oneItem.innerHTML = oneItemHtml;
            showSalePage()
        })
    }
}


// --------------------------- sell page ---------------------------  
function showSalePage() {
    const sellBtn = document.getElementById('sellBtn1');
    sellBtn.addEventListener('click', function () {
        let itemId = this.getAttribute('data-itemId');
        console.log('itemId: ' + itemId);
        salePrice(itemId);
    })
}


// --------------------------- get sales prices --------------------------- 
function salePrice(itemId) {
    axios.get('/carbon/market/itemPrices', {
        params: itemId
    })
        .then(response => {
            showSaleInfo(response.data);
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}


// --------------------------- sale info --------------------------- 
function showSaleInfo(data) {
    let saleItem = document.getElementById('saleItem1');
    let saleHtmlString = `
        <div class="d-flex">
            <div class="imgBox overflow-hidden col-4">
                <img src="/carbon/market/downloadImage/${data[0].itemId}" alt="${data[0].gameItem.itemImgName}"
                    class="img-fluid" style="max-width: 120px">
            </div>
            <div>
                <h3>${data[0].gameItem.itemName}</h3>
                <span>${data[0].gameItem.game.gameName}</span>
            </div>
        </div>
        <hr>
        <div>${data[0].price} ${data[0].createTime}</div>
        <hr>
        <div>
            <label>You receive:</label><input type="text" value="$">
            <label>Buyer pays:</label><input type="text" value="$"><span>(includes fees)</span>
        </div>
        <div>
            <input type="checkbox">
            <label>I agree to the terms of the Steam Subscriber Agreement (last updated 24 Feb, 2022.)</label>
        </div>
        <div class="mt-3 d-flex justify-content-end">
            <button class="btn btn-info sellSubmitBtn" id="sellSubmitBtn1">OK, put it up for sale</button>
        </div>
    `;
    saleItem.innerHTML = saleHtmlString;
    salePage.classList.add('d-flex');
}


// =========================== redirect to login page ===========================          
function loginPage() {
    let page1 = document.getElementById('myPage1');
    let loginHtmlString = `
        <div class="text-center my-5">您需要登入或建立帳戶才能進行相關動作。</div>
        <div class="gap-2 row col d-flex justify-content-center" id="btnGroup">
            <a class="btn btn-info submitBtn col-md-3" href="/carbon/main/loginPage">SIGN IN</a>
            <a class="btn btn-info col-md-3" href="/carbon/main/registerPage">CREATE AN ACCOUNT</a>
            <a class="btn btn-info closeBtn2 col-md-2">CANCLE</a>
        </div>`;
    page1.innerHTML = loginHtmlString;
    show.classList.add('d-flex');
    closePage();
}


// =========================== close btn ===========================          
function closePage() {
    $('.closeBtn1,.closeBtn2').click(function () {
        console.log('click');
        $('#showPage1').removeClass('d-flex');
    })
}