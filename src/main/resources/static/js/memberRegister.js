/////////////////////// step 1 /////////////////////
const emailContainer = document.querySelector('#email');
const emailSpace = document.querySelector('.space');
const pwdContainer = document.querySelector('#password');
const pwdSpace = Array.from(document.querySelectorAll('.space'))[1];
const animation = document.querySelector('#animation');
const animationIn = document.querySelector('#animationIn');
const animationIn2 = document.querySelector('#animationIn2')
const nextStepBtn = document.querySelector('#nextStep');
const reigisterBtn = document.querySelector('#reigisterBtn');
let emailStatus = '';
let pwdStatus = '';

emailContainer.addEventListener('input', function () {
  let email = emailContainer.value;
  emailStatus = 'isExist';
  checkEmail(email);
})

pwdContainer.addEventListener('input', function () {
  let pwd = pwdContainer.value;
  checkPwd(pwd);
})
pwdContainer.addEventListener('change', function () {
  let pwd = pwdContainer.value;
  checkPwd(pwd);
})

emailContainer.addEventListener('blur', function () {
  let email = emailContainer.value;

  if (checkEmail(email) == true) {
    emailAPI(email)
      .then(status => {
        emailStatus = status;
        buttonStatus(emailStatus, pwdStatus)
      })
      .catch(err => {
        console.log('err: ' + err);
      });
  }
  if (email == '') {
    classMaker(emailContainer, emailSpace, 'null', 'null');
  }
});

pwdContainer.addEventListener('blur', function () {
  let pwd = pwdContainer.value;
  if (pwd == '') {
    classMaker(pwdContainer, pwdSpace, 'null', 'null');
  }
});

nextStepBtn.addEventListener('click', function () {
  let autoInput2 = document.querySelector('#autoInput2');
  let autoInput1 = document.querySelector('#autoInput1');
  animation.classList.add('animate__fadeOutLeft');
  animation.style.display = 'none';
  animationIn.style.display = 'block';
  animationIn.classList.add('animate__fadeInRight');
  this.style.display = 'none';
  autoInput1.style.display = 'none';
  reigisterBtn.classList.remove('d-none');
  autoInput2.classList.remove('d-none');
  autoInput2.classList.add('animate__fadeInRight');
})


/////////////////////// step 2 ///////////////////////

const userIdContainer = document.querySelector('#userId');
const userIdSpace = document.querySelector('#userIdSpace');
const memberNameContainer = document.querySelector('#memberName');
const memberNameSpace = document.querySelector('#memberNameSpace');
const birthdayContainer = document.querySelector('#birthday');
const birthdaySpace = document.querySelector('#birthdaySpace');
const phoneContainer = document.querySelector('#phone');
const phoneSpace = document.querySelector('#phoneSpace');
const genderContainer = document.querySelectorAll('input[name=gender]');
const genderSpace = document.querySelector('#genderSpace');
const form = document.querySelector('#form');
let userId = '';
let memberName = '';
let phoneStatus = '';
let gender = '';


//////////////////////userId//////////////////
userIdContainer.addEventListener('input', function () {
  checkUserId(userIdContainer.value);
})
userIdContainer.addEventListener('change', function () {
  checkUserId(userIdContainer.value);
  userId = userIdContainer.value;
})

userIdContainer.addEventListener('blur', function () {
  if (userIdContainer.value == '') {
    classMaker(userIdContainer, userIdSpace, 'null', 'null');
  }
  userId = userIdContainer.value;
})

//////////////////memberName//////////////////
memberNameContainer.addEventListener('input', function () {
  checkMemberName(memberNameContainer.value);
})

memberNameContainer.addEventListener('change', function () {
  checkMemberName(memberNameContainer.value);
  memberName = memberNameContainer.value;
})

memberNameContainer.addEventListener('blur', function () {
  if (memberNameContainer.value == '') {
    classMaker(memberNameContainer, memberNameSpace, 'null', 'null');
  }
  memberName = memberNameContainer.value;
})

////////////////////birthday//////////////////
birthdayContainer.addEventListener('change', function () {
  checkBirthday(birthdayContainer.value);
})

////////////////////phone//////////////////
phoneContainer.addEventListener('input', function () {
  checkPhone(phoneContainer.value);
});


phoneContainer.addEventListener('blur', function () {
  let phone = phoneContainer.value;
  if (checkPhone(phone) == true) {
    phoneAPI(phone)
      .then(status => {
        phoneStatus = status;
        registerBtnStatus(phoneStatus);
      })
      .catch(err => {
        console.log('err: ' + err);
      });
  }
  if (phone == '') {
    classMaker(phoneContainer, phoneSpace, 'null', 'null');
  }
});

////////////////////gender//////////////////


genderContainer[0].addEventListener('change', function () {
  gender = 'isClicked';
  genderContainer[1].removeAttribute('checked');
  genderContainer[0].setAttribute('checked', 'true');
  checkGender();
})
genderContainer[1].addEventListener('change', function () {
  gender = 'isClicked';
  genderContainer[0].removeAttribute('checked');
  genderContainer[1].setAttribute('checked', 'true');
  checkGender();
})


/////////////////////// registerbutton event ///////////////////////
reigisterBtn.addEventListener('click', function (event) {
  event.preventDefault();
  if (userId == '') {
    classMaker(userIdContainer, userIdSpace, '此為必填欄位，請輸入符合資格的稱呼', 'false');
  } else if (memberName == '') {
    classMaker(memberNameContainer, memberNameSpace, '此為必填欄位，請輸入您的姓名', 'false');
  } else if (birthdayContainer.value == '1911-01-01') {
    classMaker(birthdayContainer, birthdaySpace, '此為必填欄位，請輸入您的生日', 'false');
  } else if (gender == '') {
    classMaker(genderContainer[0], genderSpace, '請選擇一個選項', 'false');
    classMaker(genderContainer[1], genderSpace, '請選擇一個選項', 'false');
  } else {


    for (let i = 0; genderContainer.length; i++) {
      if (genderContainer[i].checked) {
        gender = genderContainer[i].value;
        break;

      }
    }
    axios({
      url: 'http://localhost:8080/carbon/main/api/register',
      method: 'post',
      data: {
        email: emailContainer.value,
        pwd: pwdContainer.value,
        id: userIdContainer.value,
        name: memberNameContainer.value,
        birthday: birthdayContainer.value,
        phone: phoneContainer.value,
        gender: gender
      }
    })
      .then(response => {
        // autoInput2.classList.remove('animate__fadeInRight');
        // autoInput2.classList.add('animate__fadeOutLeft');
        // autoInput2.style.display = 'none';
        // reigisterBtn.classList.remove('animate__fadeInRight');
        // reigisterBtn.classList.add('animate__fadeOutLeft');
        // reigisterBtn.style.display = 'none';
        // animationIn.classList.remove('animate__fadeInRight');
        animationIn.classList.add('animate__fadeOutLeft');
        animationIn.addEventListener('animationend', function () {

          animationIn.style.display = 'none';
          animationIn2.style.display = 'flex';
          animationIn2.classList.add('animate__fadeInRight');
          console.log(response.data);

        })

      })
  }
})


/////////////////////// function step1 ///////////////////////

function checkEmail(email) {
  let regex = /^[^@\s]+@[^@\s]+\.[^@\s]+$/;
  if (regex.test(email)) {

    buttonStatus(emailStatus, pwdStatus);
    classMaker(emailContainer, emailSpace, '格式正確，輸入完成後將為您進行驗證', 'true');
    return true;
  } else {
    buttonStatus(emailStatus, pwdStatus);
    classMaker(emailContainer, emailSpace, '請輸入正確的電子郵件格式xxxx@xxxxx.xxx', 'false');
  }
}

function checkPwd(pwd) {
  let regex = /[a-zA-Z0-9]{6,}/;
  if (regex.test(pwd)) {
    pwdStatus = 'ok';
    buttonStatus(emailStatus, pwdStatus);
  } else {
    classMaker(pwdContainer, pwdSpace, '請輸入6位數以上英數字', 'false');
    pwdStatus = '';
    buttonStatus(emailStatus, pwdStatus);
  }

}

function emailAPI(email) {
  return new Promise((resolve, reject) => {
    axios({
      url: 'http://localhost:8080/carbon/main/api/checkEmail',
      method: 'get',
      params: {
        'e': email
      },
      headers: { 'Content-Type': 'application/x-www-form-urlencode' }
    })
      .then(response => {
        if (response.data == 'isExist') {
          classMaker(emailContainer, emailSpace, '此帳號已被註冊', 'false');
          resolve('isExist');
        } else {
          classMaker(emailContainer, emailSpace, '恭喜您！您可以使用此Email', 'true');
          resolve('isNotExist');
        }
      })
      .catch(err => {
        reject(err);
      });
  });
}

function buttonStatus(emailStatus, pwdStatus) {
  if (emailStatus == 'isNotExist' && pwdStatus == 'ok') {
    classMaker(pwdContainer, pwdSpace, '請點選下一步', 'true');
    nextStepBtn.removeAttribute('disabled');
  } else if (emailStatus == 'isExist' && pwdStatus == 'ok') {
    classMaker(pwdContainer, pwdSpace, '密碼符合要求，請完成Email欄位', 'true');
    nextStepBtn.setAttribute('disabled', 'true');
  } else {
    nextStepBtn.setAttribute('disabled', 'true');
  }

}

/////////////////////// function step2 ///////////////////////

function checkUserId(userId) {
  let regex = /[a-zA-Z0-9]/;
  if (regex.test(userId)) {
    classMaker(userIdContainer, userIdSpace, '此稱呼符合資格', 'true');
  } else {
    classMaker(userIdContainer, userIdSpace, '此為必填欄位，請輸入符合資格的稱呼', 'false');
  }
}

function checkMemberName(memberName) {
  let regex = /[a-zA-Z0-9]/;
  if (regex.test(memberName)) {
    classMaker(memberNameContainer, memberNameSpace, '太棒了', 'true');
  } else {
    classMaker(memberNameContainer, memberNameSpace, '此為必填欄位，請輸入您的姓名', 'false');
  }
}

function checkBirthday(birthday) {
  let regex = /^\d{4}-\d{2}-\d{2}$/;
  if (regex.test(birthday)) {
    classMaker(birthdayContainer, birthdaySpace, '太棒了!', 'true');
  } else {
    classMaker(birthdayContainer, birthdaySpace, '此為必填欄位，請輸入您的生日', 'false');
  }
}

function checkPhone(phone) {
  let regex = /^0[29]\d{8}$/;
  if (regex.test(phone)) {
    classMaker(phoneContainer, phoneSpace, '格式正確，輸入完成後將為您進行驗證', 'true');
    return true;
  } else {
    classMaker(phoneContainer, phoneSpace, '請輸入正確的電話號碼格式', 'false');
  }
}

function checkGender() {

  if (genderContainer[0].getAttribute('checked') == 'true' || genderContainer[1].getAttribute('checked') == 'true') {
    classMaker(genderContainer[0], genderSpace, '太棒了!', 'true');
    classMaker(genderContainer[1], genderSpace, '太棒了!', 'true');
  }
}


function phoneAPI(phone) {
  return new Promise((resolve, reject) => {
    axios({
      url: 'http://localhost:8080/carbon/main/api/checkPhone',
      method: 'get',
      params: {
        'p': phone
      },
      headers: { 'Content-Type': 'application/x-www-form-urlencode' }
    })
      .then(response => {
        if (response.data == 'isExist') {
          classMaker(phoneContainer, phoneSpace, '此電話號碼已被註冊', 'false');
          resolve('isExist');
        } else {
          classMaker(phoneContainer, phoneSpace, '恭喜您！您可以使用此電話號碼', 'true');
          resolve('isNotExist');
        }
      })
      .catch(err => {
        reject(err);
      });
  });
}



function registerBtnStatus(phoneStatus) {
  if (phoneStatus == 'isNotExist') {
    reigisterBtn.removeAttribute('disabled');
  } else {
    reigisterBtn.setAttribute('disabled', 'true');
  }
}


/////////////////////// function util ///////////////////////


function classMaker(container, space, text, status) {
  if (status == 'true') {
    container.classList.add('is-valid');
    container.classList.remove('is-invalid');
    space.classList.add('text-success');
    space.classList.remove('space', 'text-light', 'text-danger');
    space.innerText = text;
  } else if (status == 'false') {
    container.classList.add('is-invalid');
    container.classList.remove('is-valid');
    space.classList.add('text-danger');
    space.classList.remove('space', 'text-light', 'text-success');
    space.innerText = text;
  } else {
    container.classList.remove('is-invalid', 'is-valid');
    space.classList.add('space', 'text-light');
    space.classList.remove('text-danger', 'text-success');
    space.innerText = text;
  }
}

////////////// autoInput ////////////////////// 


const autoInput1 = document.querySelector('#autoInput1');
autoInput1.addEventListener('click', function () {
  emailContainer.value = 'xiaoming20230703@gmail.com';
  emailContainer.dispatchEvent(new Event('blur'));

  pwdContainer.value = 'Carbon123';
  pwdContainer.dispatchEvent(new Event('change'));
})



const autoInput2 = document.querySelector('#autoInput2');
autoInput2.addEventListener('click', function (event) {
  event.preventDefault();
  userIdContainer.value = 'xiaoming';
  userIdContainer.dispatchEvent(new Event('change'));

  memberNameContainer.value = 'xiaoming';
  memberNameContainer.dispatchEvent(new Event('change'));

  birthdayContainer.value = '2000-05-01';
  birthdayContainer.dispatchEvent(new Event('change'));

  phoneContainer.value = '0908666444';
  phoneContainer.dispatchEvent(new Event('blur'));

  let genderContainer = document.querySelector('input[name="gender"][value="1"]');
  genderContainer.checked = true;

  genderContainer.dispatchEvent(new Event('change'));
})


