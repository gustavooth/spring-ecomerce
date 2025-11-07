function templateName(nomeUsuario) {
    // Remove espaços consecutivos
    nomeUsuario = nomeUsuario.replace(/\s+/g, ' ');

    if (nomeUsuario[0] === " ") {
        nomeUsuario = nomeUsuario.substring(1);
    }

    // Verifica se o primeiro caractere é uma letra minúscula e transforma em maiúscula
    if (nomeUsuario[0].toLowerCase() === nomeUsuario[0]) {
        nomeUsuario = nomeUsuario[0].toUpperCase() + nomeUsuario.slice(1);
    }

    let partes = nomeUsuario.split(" ");
    for (var j = 0; j < partes.length; j++) {
        if(partes[j]) {
            partes[j] = partes[j][0].toUpperCase() + partes[j].slice(1);
        }
    }
    nomeUsuario = partes.join(' ');

    return nomeUsuario;
}

function validateUserName(nomeUsuario) {
    // Verificar se a senha não é vazia
    if (nomeUsuario === "") {
        return false;
    }

    // Regex para verificar se o nome de usuário contém apenas letras e espaços
    var regex = /^[a-zA-Z\s]+$/;
    if (!regex.test(nomeUsuario)) {
        return false;
    }

    // Verifica se os primeiros quatro caracteres contêm espaços
    if (nomeUsuario.slice(0, 4).includes(" ")) {
        return false;
    }

    // Para cada espaço, os próximos 2 caracteres devem conter somente letras (maiúsculas e minúsculas)
    var partes = nomeUsuario.split(" ");
    for (var i = 0; i < partes.length; i++) {
        var parte = partes[i];
        if (parte.length < 3) {
            return false;
        }
        if (!/^[a-zA-Z]{2}$/.test(parte.slice(1, 3))) {
            return false;
        }
    }

    return true;
}

function validateEmail(email) {
    var regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return regex.test(email);
}

function validatePassword(password) {
    // Verificar se a senha não é vazia
    if (password === "") {
        return false;
    }

    // Verificar se a senha tem pelo menos 6 caracteres
    if (password.length < 6) {
        return false;
    }

    // Verificar se a senha contém pelo menos um número
    var temNumero = false;
    for (var i = 0; i < password.length; i++) {
        if (!isNaN(parseInt(password[i]))) {
            temNumero = true;
            break;
        }
    }
    if (!temNumero) {
        return false;
    }

    // Verificar se a senha contém pelo menos uma letra maiúscula
    var temMaiuscula = false;
    for (var j = 0; j < password.length; j++) {
        if (password[j] === password[j].toUpperCase() && password[j] !== password[j].toLowerCase()) {
            temMaiuscula = true;
            break;
        }
    }
    if (!temMaiuscula) {
        return false;
    }

    var regex = /^[a-zA-Z0-9!@#$%^&*()-_+=~|}{[\]:;<>,.?/]+$/;
    return regex.test(password);
}



var inName = document.getElementById("inName");
var inEmail = document.getElementById("inEmail");
var inPassword = document.getElementById("inPassword");
var inPassword2 = document.getElementById("inPassword2");
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

document.getElementById("registerForm").addEventListener("htmx:confirm", function (event) {
    resetInput(inName);
    resetInput(inEmail);
    resetInput(inPassword);
    resetInput(inPassword2);
    clearMessages();

    inName.value = templateName(inName.value)

    var err = false;

    if(!validateUserName(inName.value)) {
        addMessage("Formato de nome invalido.");
        selectInput(inName);
        err = true;
    }

    if(!validateEmail(inEmail.value)) {
        addMessage("Formato de email invalido.");
        selectInput(inEmail);
        err = true;
    }

    if(inPassword.value != inPassword2.value) {
        addMessage("As senhas não coincidem.");
        selectInput(inPassword);
        selectInput(inPassword2);
        err = true;
    }

    if(!validatePassword(inPassword.value)) {
        addMessage("A senha deve conter no mínimo 6 caracteres contendo pelo menos um numero e uma letra maiúscula.");
        selectInput(inPassword);
        err = true;
    }

    if (err) {
        event.preventDefault();
        return;
    }
});

document.getElementById("registerForm").addEventListener("htmx:beforeOnLoad", function (event) {
    resetInput(inPassword);
    clearMessages();

    if (event.detail.xhr.status != 200) {
        addMessage("Houve um problema! Por favor, entre em contato com o suporte!");
        event.preventDefault();
        return;
    }

    window.location.href = "/";
});