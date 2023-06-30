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
            if (response != null) {
                orderUpdate(order);
                newItemLog(response.data);
            }
            return response.data;
        })
        .then(result => {
            if (result != null) {
                showSuccessPage(result);
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


// --------------------------- insert new itemLog ---------------------------  		
function newItemLog(result) {
    axios({
        url: '/carbon/market/newItemLog',
        method: 'post',
        data: {
            ordId: result.ordId,
            itemId: result.itemId,
            memberId: result.buyer ? result.buyer : result.seller,
            quantity: result.buyer ? result.quantity : '-'+result.quantity
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
			//console.log('response: '+ JSON.stringify(response))
			if(response.data != '' && response.data[0].total >= 1){
	            inventoryPage(response.data);
			} else{
				$('#noItem1').removeClass('d-none');
				show.classList.add('d-flex');
			    closePage();
			}
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
        axios({
            url: '/carbon/market/itemPrices',
            method: 'get',
            params: {
                itemId: itemId
            }
        })
            .then(response => {
                showSaleInfo(response.data);
            })
            .catch(err => {
                console.log('err: ' + err);
            })
    })
}


// --------------------------- sale info --------------------------- 
function showSaleInfo(order) {
    let saleItem = document.getElementById('saleItem1');
    let saleHtmlString = `
        <div class="d-flex">
            <div class="imgBox overflow-hidden col-4 d-flex justify-content-center">
                <img src="/carbon/market/downloadImage/${order[0].itemId}" alt="${order[0].gameItem.itemImgName}"
                    class="img-fluid" style="max-width: 120px">
            </div>
            <div class="col-8">
                <h3>${order[0].gameItem.itemName}</h3>
                <span>${order[0].gameItem.game.gameName}</span>
            </div>
        </div>
        <hr>`;
    order.forEach(data => {
        saleHtmlString += `
            <div>${data.price} ${data.createTime}</div>`;
    })
    saleHtmlString += `
        <hr>
        <form class="row g-3 needs-validation" novalidate>
            <div class="my-3 row">
                <label class="col-sm-3 col-form-label">You receive:</label>
                <div class="col-3">
                	<input type="text" id="salePrice" class="form-control col-4" value="NT$" required>
                </div>
                <label class="col-sm-3 col-form-label">Buyer pays:</label>
                <div class="col-3">
	                <input type="text" id="buyPrice" class="form-control" value="NT$" readonly><span>(includes fees)</span>
                </div>
                <div class="invalid-feedback">
                        You must agree before submitting.
                </div>
            </div>
            <div>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" id="invalidCheck" required>
                    <label class="form-check-label" for="invalidCheck">
                        I agree to the terms of the Steam Subscriber Agreement
                    </label>
                    <div class="invalid-feedback">
                        You must agree before submitting.
                    </div>
                </div>
            </div>
            <div class="mt-3 d-flex justify-content-end">
                <button class="btn btn-info sellSubmitBtn" id="sellSubmitBtn1">OK, put it up for sale</button>
            </div>
        </form>
    `;
    saleItem.innerHTML = saleHtmlString;
    salePage.classList.add('d-flex');
    let salePrice;
    
    $('#salePrice').on('keyup change', function () {
        salePrice = $(this).val();
        console.log(salePrice);
        $('#buyPrice').val(salePrice);
    })
    $('.closeBtn-sale').click(function () {
        $('#showSalePage1').removeClass('d-flex');
    })
    
    // =========================== Validation ===========================  
  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  const forms = document.querySelectorAll('.needs-validation')

  // Loop over them and prevent submission
  Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }

      form.classList.add('was-validated')
      
	  let price=salePrice.replace('NT$', '');
	  console.log(price)
      axios({
        url: '/carbon/market/newOrder',
        method: 'post',
        data: {
            itemId: order[0].itemId,
            seller: userId,
            quantity: 1,
            price: price,
            status: 1,
        }
    })
        .then(response => {
            if (response.data != '') {
                newItemLog(response.data);
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
    }, false)
  })

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


// =========================== Validation ===========================  
(() => {
  'use strict'

  // Fetch all the forms we want to apply custom Bootstrap validation styles to
  const forms = document.querySelectorAll('.needs-validation')

  // Loop over them and prevent submission
  Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }

      form.classList.add('was-validated')
    }, false)
  })
})()