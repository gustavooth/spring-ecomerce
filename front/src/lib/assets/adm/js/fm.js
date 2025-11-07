var folder_icon = `<i class="fa-solid fa-folder adm-fm-li-icon"></i>`
var file_icon = `<i class="fa-solid fa-file adm-fm-li-icon"></i>`
var img_icon = `<i class="fa-solid fa-file-image adm-fm-li-icon"></i>`
var video_icon = `<i class="fa-solid fa-file-video adm-fm-li-icon"></i>`
var audio_icon = `<i class="fa-solid fa-file-audio adm-fm-li-icon"></i>`
var code_icon = `<i class="fa-solid fa-file-code adm-fm-li-icon"></i>`
var txt_icon = `<i class="fa-solid fa-file-lines adm-fm-li-icon"></i>`
var zip_icon = `<i class="fa-solid fa-file-zipper adm-fm-li-icon"></i>`

var download_icon = `<i class="fa-regular fa-circle-down adm-fm-li-icon"></i>`
var trash_icon = `<i class="fa-regular fa-trash-can adm-fm-li-icon"></i>`
var link_icon = `<i class="fa-regular fa-clipboard adm-fm-li-icon"></i>`

var div_icon_class = "adm-fm-li-icon-box"

var fm_list = document.getElementById("fm-list");
var folder_back = document.getElementById("folder-back");

function getIcon(filename) {
    var arrayString = filename.split(".");
    var extension = arrayString[arrayString.length - 1];

    switch (extension) {
        case "html":
            return code_icon;
        case "css":
            return code_icon;
        case "js":
            return code_icon;
        case "txt":
            return txt_icon;
        case "png":
            return img_icon;
        case "svg":
            return img_icon;
        case "mp4":
            return video_icon;
        default: 
            return file_icon;
    }
}

function addFile(fileName) {
    icon = getIcon(fileName);
    clone = folder_back.cloneNode(true);
    clone.id = "";
    clone.classList.add("adm-fm-asset")
    clone.querySelector(".adm-fm-li-left").querySelector(".adm-fm-li-icon-box").innerHTML = icon;
    clone.querySelector(".adm-fm-li-midle").querySelector(".adm-fm-li-name-box").querySelector(".adm-fm-li-name").innerHTML = fileName;
    
    icon1 = document.createElement('a');
    icon1.classList.add(div_icon_class);
    icon1.innerHTML = download_icon;
    
    icon2 = document.createElement('div');
    icon2.classList.add(div_icon_class);
    icon2.innerHTML = trash_icon;

    icon3 = document.createElement('div');
    icon3.classList.add(div_icon_class);
    icon3.innerHTML = link_icon;

    clone.querySelector(".adm-fm-li-right").appendChild(icon3);
    clone.querySelector(".adm-fm-li-right").appendChild(icon1);
    clone.querySelector(".adm-fm-li-right").appendChild(icon2);

    const url = "/assets" + currentDir + fileName;

    icon1.href = url;
    icon1.download = fileName;
    
    icon1.addEventListener("click", function(event) {
        event.stopPropagation();
    });

    icon3.addEventListener("click", function(event) {
        event.stopPropagation();
        navigator.clipboard.writeText(url);
    });

    icon2.addEventListener("click", function(event) {
        event.stopPropagation();

        fetch(apiDeleteFile + currentDir + fileName)
        .then(response => {
            // Verifica se a resposta foi bem-sucedida (código de status 200)
            if (!response.ok) {
                throw new Error('Resposta da rede não foi OK');
            }
            // Analisa a resposta JSON
            return response.json();
        })
        .then(data => {
            if(data.status == "success") {
                event.target.parentNode.parentNode.parentNode.remove();
            }
        })
        .catch(error => {
            // Trata quaisquer erros que ocorram durante a operação fetch
            updateDir(currentDir);
            console.error('Houve um problema com a operação fetch:', error);
        });
    });

    clone.addEventListener("click", function() {
        window.open(url, '_blank');
    });

    fm_list.appendChild(clone);
}

function addFolder(folderName, top) {
    clone = folder_back.cloneNode(true);
    clone.id = "";
    clone.classList.add("adm-fm-asset")
    clone.querySelector(".adm-fm-li-midle").querySelector(".adm-fm-li-name-box").querySelector(".adm-fm-li-name").innerHTML = folderName;
    icon1 = document.createElement('a');
    icon1.classList.add(div_icon_class);
    icon1.innerHTML = trash_icon;
    clone.querySelector(".adm-fm-li-right").appendChild(icon1);

    icon1.addEventListener("click", function(event) {
        event.stopPropagation();

        fetch(apiDeleteDir + currentDir + folderName)
        .then(response => {
            // Verifica se a resposta foi bem-sucedida (código de status 200)
            if (!response.ok) {
            throw new Error('Resposta da rede não foi OK');
            }
            // Analisa a resposta JSON
            return response.json();
        })
        .then(data => {
            if(data.status == "success") {
                event.target.parentNode.parentNode.parentNode.remove();
            }
        })
        .catch(error => {
            // Trata quaisquer erros que ocorram durante a operação fetch
            console.error('Houve um problema com a operação fetch:', error);
            updateDir(currentDir);
        });
    });

    clone.addEventListener("click", function() {
        newDir(currentDir + folderName + "/");
    });

    if(top == true) {
        fm_list.insertBefore(clone, folder_back.nextSibling);
        return;
    }

    fm_list.appendChild(clone);
}

const apiUrl = "/api/v1/adm/fm/list?path=";
const apiDeleteFile = "/api/v1/adm/fm/rmfile?path=";
const apiDeleteDir = "/api/v1/adm/fm/rmdir?path=";

const searchBar = document.getElementById("search-bar")
const searchBtn = document.getElementById("search-btn")
const homeBtn = document.getElementById("home-btn");
const updateBtn = document.getElementById("update-btn");
const backBtn = document.getElementById("back-btn");

var currentDir = "/";
var oldDir = [];
var list = true;

function listDirectory() {
    fetch(apiUrl + currentDir)
    .then(response => {
        list = true;
        // Seleciona todos os elementos com a classe específica
        var elements = fm_list.getElementsByClassName("adm-fm-asset");

        // Itera sobre os elementos e os remove
        while (elements.length > 0) {
            elements[0].remove();
        }
        // Verifica se a resposta foi bem-sucedida (código de status 200)
        if (!response.ok) {
        throw new Error('Resposta da rede não foi OK');
        }
        // Analisa a resposta JSON
        return response.json();
    })
    .then(data => {
        if(data.folders != null) {
            for (var i = 0; i < data.folders.length; i++) {
                addFolder(data.folders[i]);
            }
        }
        
        if(data.files != null) {
            for (var i = 0; i < data.files.length; i++) {
                addFile(data.files[i]);
            }
        }
        
    })
    .catch(error => {
        // Trata quaisquer erros que ocorram durante a operação fetch
        console.error('Houve um problema com a operação fetch:', error);
    });
}

function updateDir(dir) {
    list = false;

    if(dir[dir.length -1] != '/') {
        dir = dir + '/';
    }

    currentDir = dir;
    searchBar.value = dir;

    listDirectory();
}

function newDir(dir) {
    if(dir != currentDir) {
        oldDir.push(currentDir);
    }
    
    updateDir(dir);
}

function search() {
    if (searchBar.value == "") {
        searchBar.value = "/";
    }

    newDir(searchBar.value);
}

searchBtn.addEventListener("click", function() {
    search();
});

searchBar.addEventListener("keydown", function(event) {
    // Verifica se a tecla pressionada é a tecla Enter (código 13)
    if (event.key === "Enter") {
        search();
    }
});

homeBtn.addEventListener("click", function() {
    newDir("/")
});

updateBtn.addEventListener("click", function() {
    updateDir(currentDir);
});

backBtn.addEventListener("click", function() {
    if(oldDir.length < 0) {
        return;
    }

    updateDir(oldDir[oldDir.length -1]);
    oldDir.pop()
});

function getBackDir(dir) {
    // Remover barras no final do caminho, se houver
    dir = dir.replace(/\/+$/, "");

    // Extrair o diretório anterior usando uma expressão regular
    const match = dir.match(/^(.*\/)[^/]+\/?$/);

    // Verificar se houve correspondência e retornar o diretório anterior se encontrado
    if (match) {
        return match[1];
    } else {
        return null; // Retorna null se não for possível extrair o diretório anterior
    }
}

folder_back.addEventListener("click", function() {
    var backDir = getBackDir(currentDir);
    if (backDir == null) {
        return;
    }
    newDir(backDir);
});

updateDir("/");