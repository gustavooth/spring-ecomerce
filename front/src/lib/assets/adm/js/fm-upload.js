const newDirBtn = document.getElementById("new-dir");
const uploadFilesBtn = document.getElementById("up-files");
const fileInput = document.createElement('input');
const progressBar = document.getElementById('progressBar');
const progressContainer = document.getElementById('progress-container');
fileInput.type = 'file';
fileInput.style.display = 'none';
fileInput.multiple = true;
uploadFilesBtn.appendChild(fileInput);

const apiUpFile = "/api/v1/adm/fm/upfile?path=";
const apiNewDir = "/api/v1/adm/fm/mkdir?path=";

uploadFilesBtn.addEventListener("click", function() {
    fileInput.click();
});

newDirBtn.addEventListener("click", function() {
    var folderName = prompt("Digite o nome da pasta");
    var dir = currentDir;

    fetch(apiNewDir + dir + folderName)
    .then(response => {
        if (data.status != "success") {
            throw new Error('Resposta da rede não foi OK');
        }
        // Analisa a resposta JSON
        return response.json();
    })
    .then(data => {
        if(data.ok) {
            if(currentDir == dir) {
                addFolder(folderName, true);
            }
        }
    })
    .catch(error => {
        // Trata quaisquer erros que ocorram durante a operação fetch
        console.error('Houve um problema com a operação fetch:', error);
    });
});

fileInput.addEventListener('change', function(event) {
    var files = event.target.files;
    
    // Iterar sobre cada arquivo na lista
    for (var i = 0; i < files.length; i++) {
        uploadFile(files[i])
    }
});

function uploadFile(file) {
    var formData = new FormData();
    formData.append('file', file);

    var xhr = new XMLHttpRequest();

    var dir = currentDir;

    var progress = progressBar.cloneNode(true);
    progress.style.display = 'flex';
    progress.querySelector(".adm-fm-upload-left").querySelector(".adm-text").innerHTML = dir + file.name;
    progressContainer.appendChild(progress);

    // Evento para acompanhar o progresso de upload
    xhr.upload.addEventListener('progress', function(event) {
        if (event.lengthComputable) {
            var percentComplete = (event.loaded / event.total) * 100;
            // Atualizar a barra de progresso com o percentual completo
            progress.querySelector(".adm-fm-upload-right").querySelector(".adm-fm-upload-progress").value = percentComplete;
        }
    });

    // Evento que ocorre quando o upload é concluído
    xhr.addEventListener('load', function(event) {
        if (xhr.status === 200) {
            progress.querySelector(".adm-fm-upload-right").querySelector(".adm-fm-upload-progress").value = 100;
            if(currentDir == dir) {
                addFile(file.name);
            }
        } else {
            alert('Erro ao fazer o upload do arquivo');
        }

        setTimeout(function() {
            progress.remove();
        }, 10000);
    });

    // Evento para tratar erros
    xhr.addEventListener('error', function(event) {
        console.error('Erro:', xhr.statusText);
        alert('Erro ao fazer o upload do arquivo');
    });

    xhr.open('POST', apiUpFile + dir, true);
    xhr.send(formData);
}

updateDir("/");