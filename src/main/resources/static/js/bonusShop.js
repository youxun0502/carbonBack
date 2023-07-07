const userlogin = document.getElementById('memberId');
const selectItem = document.getElementsByClassName('sc_item_box');
let userid = 0;
let point = 0;
if (userlogin == null) {
    userid = -9999;
} else {
    userid = userlogin.innerText;
}
console.log("userinfo:" + userid);

// =========================== BonusShopPage ===========================  
//----------------------------click itembox-----------------------------
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
//----------------------------click itembox(Not login)-----------------------------
function itemboxNotLogin(selectId) {
    axios({
        url: '/carbon/bonus-shop/api/getbonusitem',
        method: 'get',
        params: {
            id: selectId
        }
    })
        .then(reponse => {
            console.log("res:" + reponse.data.bonusId);
            let titletext = "尚未登入\r\n" + reponse.data.bonusName;
            let warningtext = reponse.data.bonusDes;
            Swal.fire({
                title: titletext,
                text: warningtext,
                icon: 'question',
                imageUrl: '/carbon/downloadImage/' + reponse.data.bonusId,
                imageHeight: 200,
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: reponse.data.bonusprice + ' Coins'
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
//----------------------------click itembox(login)-----------------------------
/*!!判斷是否已購買 */
function itemboxDoneLogin(selectId) {
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
                        icon: 'warning',
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