const sendBtn = document.getElementById("send-btn");
const pageId = document.getElementById("page-id");
const pageTitle = document.getElementById("page-title");
const pageUrl = document.getElementById("page-url");
const pageFirstItem = document.getElementById("page-fitem");
const sendPageApi = "/api/v1/adm/db/insert/item_page?id=[0]&title=[1]&url=[2]&first_item=[3]&variations=[4]&v_items=[5]";
const sendItemApi = "/api/v1/adm/db/insert/item?page_id=[0]&id=[1]&name=[2]&price=[3]&promotions=[4]&stack=[5]&max_stack=[6]&icon_path=[7]&img_paths=[8]";

sendBtn.addEventListener("click", function(e) {
    if(pageId.value == "" || pageTitle.value == "" || pageUrl.value == "" || pageFirstItem.value == "") {
        alert("Preencha todos os inputs!");
        return;
    }

    const urlRegex = /^[a-zA-Z0-9\s]+$/;
    const isValid = urlRegex.test(pageUrl.value);

    if(!isValid) {
        alert("É permitido somente letras, numeros ou '-' no input URL");
        return;
    }

    let items = [];
    itemsElement = document.getElementById("items-container").querySelectorAll(".adm-item-form");
    v_description = [];
 
    for(var i = 1; i < itemsElement.length; i++) {
        inputs = itemsElement[i].querySelectorAll(".adm-input-text");
        id = inputs[0].querySelector("input").value;
        nome = inputs[1].querySelector("input").value;
        price = inputs[2].querySelector("input").value;
        promotions = inputs[3].querySelector("input").value;
        stack = inputs[4].querySelector("input").value;
        stackMax = inputs[5].querySelector("input").value;
        elements = inputs[7].querySelector(".adm-item-image-container").querySelectorAll("img");
        var icon = "";
        if(elements.length > 0) {
            icon = elements[0].src.replace(/^.*\/\/[^\/]+/, '');
        }
        
        images = [];
        elements = inputs[8].querySelector(".adm-item-image-container").children;
        for (let i = 0; i < elements.length; i++) {
            images.push(elements[i].src.replace(/^.*\/\/[^\/]+/, ''));
        }

        items.push({
            id: id,
            nome: nome,
            price: price,
            promotions: promotions,
            stack: stack,
            stackMax: stackMax,
            icon: icon,
            images: images,
        })

        let description = inputs[6].querySelector(".adm-it-variation-selector").children;

        if(description.length < 2) {
            return;
        }

        var desc = "";
        for (let x = 2; x < description.length; x++) {
            if(description[x].classList.contains("adm-it-variation-checkbox")) {
                if(description[x].querySelector("input").checked) {
                    desc += description[x].id.split("-")[3];
                }
            }else {
                desc += "|";
            }
        }

        v_description.push(desc);
    }

    let variacoes = [];
    
    v = document.querySelectorAll(".adm-page-variation");
    for (let i = 0; i < v.length; i++) {
        nome = v[i].querySelector(".adm-input-text").querySelector("input").value;
        variacoes.push(nome + "|");

        for (let x = 0; x < variations[i].length; x++) {
            variacoes[i] += variations[i][x] + "|";
        }
    }

    console.log(items);
    console.log(v_description);
    console.log(variacoes);
    
    for (let i = 0; i < items.length; i++) {
        let nome = items[i].nome
        if (nome.includes(" ")) {
            nome = nome.split(" ").join("_");
        }

        let promotions = items[i].promotions
        if (promotions.includes(" ")) {
            promotions = promotions.split(" ").join("_");
        }

        sendItemToDb(pageId.value, items[i].id, nome, items[i].price.replace(",", "."), promotions.replace(" ", "_"), items[i].stack, items[i].stackMax, items[i].icon, items[i].images.join("_"));
    }

    let title = pageTitle.value
    if (title.includes(" ")) {
        title = title.split(" ").join("_");
    }

    let url = pageUrl.value.toLowerCase()
    if (url.includes(" ")) {
        url = url.split(" ").join("-");
    }

    variacoes.splice(0, 1);

    for (var i = 0; i < variacoes.length; i++) {
        variacoes[i] = removerUltimoPipe(variacoes[i]);
    }

    for (var i = 0; i < v_description.length; i++) {
        v_description[i] = removerUltimoPipe(v_description[i]);
    }

    sendPageToDb(pageId.value, title, url, pageFirstItem.value, variacoes.join("_"), v_description.join("_"));
})

function sendPageToDb(id, title, url, first_item, variations, v_items) {
    var link = sendPageApi.replace("[0]", id).replace("[1]", title).replace("[2]", url).replace("[3]", first_item).replace("[4]", variations).replace("[5]", v_items);
    console.log(link);
    fetch(link)
    .then(response => {
        // Analisa a resposta JSON
        return response.json();
    })
    .then(data => {
        if(data.status != "success") {
            console.error(data.message);
        }else {
            alert("Pagina " + pageId.value + " adicionada!");
        }
    })
    .catch(error => {
        // Trata quaisquer erros que ocorram durante a operação fetch
        console.error('Houve um problema com a operação fetch:', error);
    });
}

function sendItemToDb(page_id, id, name, price, promotions, stack, stackMax, icon, images) {
    var link = sendItemApi.replace("[0]", page_id).replace("[1]", id).replace("[2]", name).replace("[3]", price).replace("[4]", promotions).replace("[5]", stack).replace("[6]", stackMax).replace("[7]", icon).replace("[8]", images);
    console.log(link);
    fetch(link)
    .then(response => {
        // Analisa a resposta JSON
        return response.json();
    })
    .then(data => {
        if(data.status != "success") {
            console.error(data.message);
        }else {
            alert("Item adicionado!");
        }
    })
    .catch(error => {
        // Trata quaisquer erros que ocorram durante a operação fetch
        console.error('Houve um problema com a operação fetch:', error);
    });
}

function removerUltimoPipe(string) {
    // Verifica se a string não está vazia e se o último caractere é "|"
    if (string.length > 0 && string.charAt(string.length - 1) === "|") {
        // Remove o último caractere "|"
        return string.slice(0, -1);
    } else {
        // Retorna a string sem modificação
        return string;
    }
}