let emailContainer = document.querySelector('#email');
let emailSpace = document.querySelector('.space');
let pwdContainer = document.querySelector('#password');
let pwdSpace = Array.from(document.querySelectorAll('.space'))[1];
let nextStepBtn = document.querySelector('#nextStep');
let animation = document.querySelector('#animation');
let animationIn = document.querySelector('#animationIn');
let reigisterBtn = document.querySelector('#reigisterBtn');
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
  console.log('test alimation');
  animation.classList.add('animate__fadeOutLeft');
  animation.style.display = 'none';
  animationIn.style.display = 'block';
  animationIn.classList.add('animate__fadeInRight');
  this.style.display = 'none';
  reigisterBtn.classList.remove('d-none');

})

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


function emailAPI(email) {
  return new Promise((resolve, reject) => {
    axios({
      url: 'http://localhost:8080/carbon/member/api/checkEmail',
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