function validateEmail(email) {
    var regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return regex.test(email);
}

function validatePassword(password) {
    // Verificar se a senha não é vazia
    if (password === "") {
        return false;
    }

    var regex = /^[a-zA-Z0-9!@#$%^&*()-_+=~|}{[\]:;<>,.?/]+$/;
    return regex.test(password);
}

var inEmail = document.getElementById("inEmail");
var inPassword = document.getElementById("inPassword");
var divMessages = document.getElementById("auth-message");

function addMessage(message) {
    divMessages.innerHTML += `<div class="auth-message"><i class="fa-solid fa-triangle-exclamation auth-message-color" aria-hidden="true"></i><span class="auth-message-text auth-message-color">  *</span></div>`.replace("*", message);
}

function clearMessages() {
    divMessages.innerHTML = "";
}

function selectInput(input) {
    input.style.borderColor = "rgb(175, 36, 36)";
}

function resetInput(input) {
    input.style.borderColor = "";
}

document.getElementById("loginForm").addEventListener("htmx:confirm", function (event) {
    resetInput(inEmail);
    resetInput(inPassword);
    clearMessages();

    var err = false;

    if(!validateEmail(inEmail.value)) {
        addMessage("Formato de email invalido.");
        selectInput(inEmail);
        err = true;
    }

    if(!validatePassword(inPassword.value)) {
        addMessage("Formato de senha invalido.");
        selectInput(inPassword);
        err = true;
    }

    if (err) {
        event.preventDefault();
    }
});

document.getElementById("loginForm").addEventListener("htmx:beforeOnLoad", function (event) {
    resetInput(inPassword);
    clearMessages();

    if (event.detail.xhr.status != 200) {
        addMessage("Senha incorreta!");
        selectInput(inPassword);
        event.preventDefault();
        return;
    }

    window.location.href = "/";
});