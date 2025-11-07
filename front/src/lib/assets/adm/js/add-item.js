const addVariationBtn = document.getElementById("add-variation-btn");
const variationsList = document.getElementById("variations");
const variationTmpl = document.getElementById("variation-template");

const itemTmpl = document.getElementById("item");
const addItemBtn = document.getElementById("add-it-btn");

function inputDescription(e) {
    var vid = parseInt(e.target.parentNode.parentNode.id.split("=")[1]);
    var did = parseInt(e.target.id.split("=")[3]);
    variations[vid][did] = e.target.value;

    variationContainers = document.querySelectorAll(".adm-it-variation-selector");

    let checkboxes = document.querySelectorAll('.adm-it-variation-remove');
    checkboxes.forEach(function(checkbox) {
        checkbox.remove();
    });

    for (var i = 0; i < variationContainers.length; i++) {
        var toClone = variationContainers[i].querySelector(".adm-it-variation-checkbox");
        for (let x = 0; x < variations.length; x++) {
            for (let y = 0; y < variations[x].length; y++) {
                clone = toClone.cloneNode(true);
                clone.style.display = "flex";
                clone.querySelector(".adm-it-variation-txt").innerHTML = variations[x][y];
                clone.id = "x-" + x + "-y-" + y;
                clone.querySelector(".adm-text").addEventListener("change", itemCheckBox);
                clone.classList.add("adm-it-variation-remove");

                toClone.parentNode.appendChild(clone);
            }
            let div = document.createElement("div");
            div.className = "adm-divider adm-it-variation-remove";
            toClone.parentNode.appendChild(div);
        }
    }
}

function itemCheckBox(e) {
    var current = e.target.checked;

    id = e.target.parentNode.id.split("-")[1];
    let checkboxes = e.target.parentNode.parentNode.querySelectorAll('input[type="checkbox"].adm-text');

    for (let i = 0; i < checkboxes.length; i++) {
        if(checkboxes[i].parentNode.id.split("-")[1] == id) {
            checkboxes[i].checked = false;
        }
    }

    e.target.checked = current;
}

addVariationBtn.addEventListener("click", function(e) {
    clone = variationTmpl.cloneNode(true);

    clone.querySelector(".adm-secundary-btn").addEventListener("click", function(e) {
        c = e.target.parentNode.querySelector(".adm-input-description").cloneNode(true);
        e.target.parentNode.insertBefore(c, e.target.parentNode.querySelector(".adm-input-description").nextSibling);
        
        var vid = parseInt(e.target.parentNode.id.split("=")[1]);
        c.querySelector(".adm-text").value = "";
        c.querySelector(".adm-text").id = "v=" + vid + "=d=" + variations[vid].length;
        variations[vid].push("");
        c.querySelector(".adm-text").addEventListener("input", inputDescription);
    })

    clone.querySelector(".adm-input-description").querySelector(".adm-text").addEventListener("input", inputDescription)
    clone.querySelector(".adm-input-description").querySelector(".adm-text").id = "v=" + variations.length + "=d=" + 0;

    clone.style.display = "block";
    clone.id = "v=" + variations.length;
    variationsList.appendChild(clone);

    variations.push([""]);
})

addItemBtn.addEventListener("click", function() {
    clone = itemTmpl.cloneNode(true);
    id = document.querySelectorAll(".adm-item-form").length - 1;
    clone.id = "ITID=" + id;
    clone.querySelector(".adm-input-text").querySelector('input[type="number"].adm-text').value = id;

    clone.querySelector(".adm-add-icon-btn").addEventListener("click", async function(e) {
        let files = await selectFiles(EXT.IMAGE);

        var imagesContainer = e.target.parentNode.querySelector(".adm-item-icon").querySelector(".adm-item-image-container");

        imagesContainer.innerHTML = "";

        if (files.length < 1) {
            return;
        }

        var img = document.createElement("img");
        img.src = "/assets" + files[0];
        img.classList.add("adm-item-image");
        imagesContainer.appendChild(img);
    })

    clone.querySelector(".adm-add-image-btn").addEventListener("click", async function(e) {
        let files = await selectFiles(EXT.IMAGE);
        var imagesContainer = e.target.parentNode.querySelector(".adm-item-images").querySelector(".adm-item-image-container");

        imagesContainer.innerHTML = "";

        for (let i = 0; i < files.length; i++) {
            var img = document.createElement("img");
            img.src = "/assets" + files[i];
            img.classList.add("adm-item-image");
            imagesContainer.appendChild(img);
        }
    })

    clone.style.display = "flex"
    itemTmpl.parentNode.appendChild(clone);
})

var variations = [[]];