const userlogin = document.getElementById('memberId');

let userid = 0;
let point = 0;
if (userlogin == null) {
    userid = -9999;
} else {
    userid = userlogin.innerText;
}
console.log("userinfo:" + userid);

// =========================== BonusShopPage ======	=====================  
/*----------------------------------生成avatar page--------------------------------------------------------*/
$(function () {
    loadAvatarPage();
    loadFramePage();
    loadBgPage();
    selectclick();
})
function loadAvatarPage() {

    axios.get('/carbon/bonus-shop/avatar-page')
        .then(response => {
            loadAvatars(response.data);
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}
function turnPageAvatar(pageid) {
    axios.get('/carbon/bonus-shop/avatar-page', {
        params: {
            p: pageid
        }
    })
        .then(response => {
            loadAvatars(response.data);
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}

function loadFramePage() {

    axios.get('/carbon/bonus-shop/frame-page')
        .then(response => {
            loadFrames(response.data);
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}
function turnPageFrame(pageid) {
    axios.get('/carbon/bonus-shop/frame-page', {
        params: {
            p: pageid
        }
    })
        .then(response => {
            loadFrames(response.data);
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}

function loadBgPage() {

    axios.get('/carbon/bonus-shop/bg-page')
        .then(response => {
            loadBackgrounds(response.data);
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}
function turnPageBackground(pageid) {
    axios.get('/carbon/bonus-shop/bg-page', {
        params: {
            p: pageid
        }
    })
        .then(response => {
            loadBackgrounds(response.data);
        })
        .catch(err => {
            console.log('err: ' + err);
        })
}

function loadAvatars(data) {
    /*---------------------------------------------AVATAR BODY--------------------------------------------------------- */
    let avatarshtml = ``;
    data.content.forEach(e => {

        avatarshtml += `<div class="sc_item_container">
            <div class="sc_item_box" data-id="${e.bonusId}">

                <div class="item_bonusitem">

                    <img class="item_reward"
                        src="/carbon/downloadImage/${e.bonusId}">
                </div>


                <div class="item_box_footer">
                    <div class="item_box_footer_desc">

                        <div class="bonusitem_name nk-post-title h5 text-center">${e.bonusName}</div>
                        <div class="bonusitem_type text-center">
                            ${e.bonusDes}</div>
                    </div>

                    <div class="item_box_footer_Costrow">
                        <div class="item_cost">
                            <div class="item_cost_layout">
                                <div class="bonusitem_price">${e.bonusPrice}<span style="font-size:0.75rem">Coins</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>`;


    })
    $('#avatar_body').html(avatarshtml);
    /*------------------------------------AVATAR  LRButton-------------------------------------------------------- */
    let buttonhtml = ``;
    let totalPages = data.totalPages;

    let thisPage = data.pageable.pageNumber;
    buttonhtml += `
        <a href="#" class="pg-btn nk-pagination-prev" id="avatar-prearrow" data-pageid="${thisPage}" >
            <i class="fa-solid fa-chevron-left"></i>
        </a>
        <a href="#" class="pg-btn nk-pagination-next" id="avatar-nextarrow" data-pageid="${thisPage + 2}">
            <i class="fa-solid fa-chevron-right"></i>
        </a>`;
    $('#avatar-LRbutton').html(buttonhtml);

    let lbtn = document.getElementById('avatar-prearrow');
    let rbtn = document.getElementById('avatar-nextarrow');

    lbtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (thisPage == '0') {
            this.setAttribute('disabled', '');
        } else {
            let page = this.getAttribute("data-pageid");
            turnPageAvatar(page);
        }
    })
    rbtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (thisPage == totalPages - 1) {
            this.setAttribute('disabled', '');
        } else {
            let page = this.getAttribute("data-pageid");
            turnPageAvatar(page);
        }
    })

    selectclick();
}

/*------------------------------------------FRAME BODY----------------------------------------------------------- */
function loadFrames(data) {

    let avatarshtml = ``;
    data.content.forEach(e => {

        avatarshtml += `<div class="sc_item_container">
            <div class="sc_item_box" data-id="${e.bonusId}">

                <div class="item_bonusitem">

                    <img class="item_reward"
                        src="/carbon/downloadImage/${e.bonusId}">
                </div>


                <div class="item_box_footer">
                    <div class="item_box_footer_desc">

                        <div class="bonusitem_name nk-post-title h5 text-center">${e.bonusName}</div>
                        <div class="bonusitem_type text-center">
                            ${e.bonusDes}</div>
                    </div>

                    <div class="item_box_footer_Costrow">
                        <div class="item_cost">
                            <div class="item_cost_layout">
                                <div class="bonusitem_price">${e.bonusPrice}<span style="font-size:0.75rem">Coins</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>`;


    })
    $('#frame_body').html(avatarshtml);

    let buttonhtml = ``;
    let totalPages = data.totalPages;
    let thisPage = data.pageable.pageNumber;

    buttonhtml += `
        <a href="#" class="pg-btn nk-pagination-prev" id="frame-prearrow" data-pageid="${thisPage}" >
            <i class="fa-solid fa-chevron-left"></i>
        </a>
        <a href="#" class="pg-btn nk-pagination-next" id="frame-nextarrow" data-pageid="${thisPage + 2}">
            <i class="fa-solid fa-chevron-right"></i>
        </a>`;

    $('#frame-LRbutton').html(buttonhtml);

    let lbtn = document.getElementById('frame-prearrow');
    let rbtn = document.getElementById('frame-nextarrow');

    lbtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (thisPage == '0') {
            this.setAttribute('disabled', '');
        } else {
            let page = this.getAttribute("data-pageid");
            turnPageFrame(page);
        }
    })
    rbtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (thisPage == totalPages - 1) {
            this.setAttribute('disabled', '');
        } else {
            let page = this.getAttribute("data-pageid");
            turnPageFrame(page);
        }
    })


    selectclick();
}

/*------------------------------------------BG BODY----------------------------------------------------------- */
function loadBackgrounds(data) {

    let avatarshtml = ``;
    data.content.forEach(e => {

        avatarshtml += `<div class="sc_item_container">
            <div class="sc_item_box" data-id="${e.bonusId}">

                <div class="item_bonusitem">

                    <img class="item_reward"
                        src="/carbon/downloadImage/${e.bonusId}">
                </div>


                <div class="item_box_footer">
                    <div class="item_box_footer_desc">

                        <div class="bonusitem_name nk-post-title h5 text-center">${e.bonusName}</div>
                        <div class="bonusitem_type text-center">
                            ${e.bonusDes}</div>
                    </div>

                    <div class="item_box_footer_Costrow">
                        <div class="item_cost">
                            <div class="item_cost_layout">
                                <div class="bonusitem_price">${e.bonusPrice}<span style="font-size:0.75rem">Coins</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>`;


    })
    $('#bg_body').html(avatarshtml);

    let buttonhtml = ``;
    let totalPages = data.totalPages;
    let thisPage = data.pageable.pageNumber;

    buttonhtml += `
        <a href="#" class="pg-btn nk-pagination-prev" id="bg-prearrow" data-pageid="${thisPage}">
            <i class="fa-solid fa-chevron-left"></i>
        </a>
        <a href="#" class="pg-btn nk-pagination-next" id="bg-nextarrow" data-pageid="${thisPage + 2}">
            <i class="fa-solid fa-chevron-right"></i>
        </a>`;

    $('#bg-LRbutton').html(buttonhtml);

    let lbtn = document.getElementById('bg-prearrow');
    let rbtn = document.getElementById('bg-nextarrow');

    lbtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (thisPage == '0') {
            this.setAttribute('disabled', '');
        } else {
            let page = this.getAttribute("data-pageid");
            turnPageBackground(page);
        }
    })
    rbtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (thisPage == totalPages - 1) {
            this.setAttribute('disabled', '');
        } else {
            let page = this.getAttribute("data-pageid");
            turnPageBackground(page);
        }
    })

    selectclick();
}




/*-----------------------------------------------------------click itembox--------------------------------------------*/
function selectclick() {
    let selectItem = document.getElementsByClassName('sc_item_box');

    for (i = 0; i < selectItem.length; i++) {
        selectItem[i].addEventListener('click', function (e) {
            e.preventDefault();
            let selectId = this.getAttribute('data-id');
            if (userid == -9999) {
                itemboxNotLogin(selectId);
            } else {
                itemboxDoneLogin(selectId);
            }
        })
    }
}
/*-----------------------------------------------------------------click itembox(Not login)-----------------------------*/
function itemboxNotLogin(selectId) {
    axios({
        url: '/carbon/bonus-shop/api/getbonusitem',
        method: 'get',
        params: {
            id: selectId
        }
    })
        .then(reponse => {
            // console.log("res:" + reponse.data.bonusId);
            let titletext = "尚未登入\r\n";
            let warningtext = "請登入後再購買";
            let ButtonText = "前往登入";
            Swal.fire({
                title: titletext,
                text: warningtext,
                icon: 'warning',
                imageUrl: '/carbon/downloadImage/' + reponse.data.bonusId,
                imageHeight: 200,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: ButtonText
            }).then((result) => {
                /*goto login page */
                if (result.isConfirmed) {
                    window.location.href = '/carbon/main/loginPage';
                }
            })
        })
        .catch(err => {
            console.log("err:" + err);
        })

}
/*--------------------------------------------------------------click itembox(login)-----------------------------*/

function itemboxDoneLogin(selectId) {
    axios({
        url: '/carbon/bonus-shop/api/checkbought',
        method: 'post',
        data: {
            memberId: userid,
            bonusId: selectId
        }
    })
        .then(res => {

            // console.log("res: " + res.data.isBought);
            if (res.data.isBought) {
                /**--------------------------------買過 跳轉至個人頁------------------------------------------------------- */
                axios({
                    url: '/carbon/bonus-shop/api/getbonusitem',
                    method: 'get',
                    params: {
                        id: selectId
                    }
                })
                    .then(reponse => {
                        let titletext = "您已購買過此商品\r\n";
                        let warningtext = "是否前往裝備?";
                        let ButtonText = "立刻裝備";
                        Swal.fire({
                            title: titletext,
                            text: warningtext,
                            icon: 'success',
                            imageUrl: '/carbon/downloadImage/' + reponse.data.bonusId,
                            imageHeight: 200,
                            showCancelButton: true,
                            confirmButtonColor: '#28a745',
                            cancelButtonColor: '#d33',
                            confirmButtonText: ButtonText
                        }).then((result) => {
                            /*goto login page */
                            if (result.isConfirmed) {
                                window.location.href = '/carbon/memberFront/memberInformationPage';
                            }
                        })

                    })

            } else {
                /**----------------------------------沒買過 進入購買頁面---------------------------------------------------- */
                axios({
                    url: '/carbon/bonus-shop/api/point',
                    method: 'get',
                    params: {
                        id: userid
                    }
                })
                    .then(res => {
                        console.log("point: " + res.data.point);
                        return res.data.point;
                    })
                    .then(point => {
                        axios({
                            url: '/carbon/bonus-shop/api/getbonusitem',
                            method: 'get',
                            params: {
                                id: selectId
                            }
                        })
                            .then(reponse => {
                                console.log("res:" + reponse.data.bonusId);
                                let titletext = "餘額 " + point + " Coin 是否購買?\r\n" + reponse.data.bonusName;
                                let warningtext = reponse.data.bonusDes;
                                let bonusId = reponse.data.bonusId;
                                Swal.fire({
                                    title: titletext,
                                    text: warningtext,
                                    imageUrl: '/carbon/downloadImage/' + reponse.data.bonusId,
                                    imageHeight: 200,
                                    icon: 'question',
                                    showCancelButton: true,
                                    confirmButtonColor: '#3085d6',
                                    cancelButtonColor: '#d33',
                                    confirmButtonText: reponse.data.bonusprice + ' Coins'
                                }).then((result) => {
                                    /*1.判斷餘額 2.購買成功 */
                                    if (result.isConfirmed) {
                                        console.log("123123123: " + bonusId);
                                        BuyBonusItem(reponse.data.bonusprice, point, bonusId);
                                    }
                                })
                            })
                            .catch(err => {
                                console.log("err:" + err);
                            })
                    })
                    .catch(err => {
                        console.log("error! no point!");
                    })
            }
        })



}
//-----------------------------------------------------------------------------------
function BuyBonusItem(price, point, bonusId) {
    if (price > point) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Coin不夠啦~',
        })
    } else {
        axios({
            url: 'http://localhost:8080/carbon/bonus-shop/api/buybonusitem',
            method: 'post',
            data: {
                bonusId: bonusId,
                point: point,
                bonusprice: price,
                memberId: userid
            }
        })

        Swal.fire({
            icon: 'success',
            title: '購買成功',
        })
    }
}