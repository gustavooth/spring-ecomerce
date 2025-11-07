const VBtnClass = "it-variation-btn"
const VTxtClass = "it-variation-btn-txt"
const VBtnClassS = "it-variation-btn-s"
const VTxtClassS = "it-variation-btn-txt-s"

const VImgClass = "it-variation-img"
const VImgBorderClass = "it-mini-img-border"


var variations_count = variations_data[0].split("|").length;

var selected = [];
var itemSelected = first_item;

var stack = 1;

const resetEvent = new Event('resetSelected');

const price_real = document.getElementById("price_real");
const price_real2 = document.getElementById("price_real2");
const price_cents = document.getElementById("price_cents");
const price_cent2 = document.getElementById("price_cents2");
const price_total = document.getElementById("price_total");
const price_12x = document.getElementById("price_12x");

const stackElement = document.getElementById("stack");
const stackPlus = document.getElementById("stack_plus");
const stackLess = document.getElementById("stack_less");

function selectItem(id) {
    itemSelected = id;
    var v = variations_data[id].split("|");
    for (let i = 0; i < v.length; i++) {
        var btn = document.getElementById(""+i+"|"+v[i]);
        var btn_txt = btn.firstElementChild;

        if (btn_txt.tagName === "SPAN") {
            btn.classList.remove(VBtnClass);
            btn_txt.classList.remove(VTxtClass);

            btn.classList.add(VBtnClassS);
            btn_txt.classList.add(VTxtClassS);
        }else {
            btn_txt.classList.add(VImgBorderClass);
        }
    }

    price_real.innerHTML = items[id].Price_real;
    price_real2.innerHTML = items[id].Price_real;
    price_cents.innerHTML = items[id].Price_cents;
    price_cents2.innerHTML = items[id].Price_cents;
    price_total.innerHTML = "R$"+items[id].Price;
    price_12x.innerHTML = "R$"+items[id].Price_12x;
}

function first() {
    updateButtons();

    data = variations_data[first_item].split("|");
    for (let i = 0; i < data.length; i++) {
        selected.push(data[i]);
    }

    selectItem(first_item);

    stackPlus.addEventListener("click", function(e) {
        if(stack == items[itemSelected].Max_stack) {
            return;
        }
        stack++;
        stackElement.innerHTML = ""+stack;
    });

    stackLess.addEventListener("click", function(e) {
        if(stack == 1) {
            return;
        }
        stack--;
        stackElement.innerHTML = ""+stack;
    });
}

function updateSelected() {
    var data = ""
    for (let i = 0; i < selected.length; i++) {
        if(i != 0) {
            data += "|";
        }
        data += selected[i]
    }

    for (let i = 0; i < variations_data.length; i++) {
        if(variations_data[i] == data) {
            selectItem(i);
        }
    }
}

function resetSelected() {
    for (let i = 0; i < variations_count; i++) {
        const v = document.getElementById("BtnV-" + i);
        const d = v.children;
        for (let i = 0; i < d.length; i++) {
            d[i].dispatchEvent(resetEvent);
        }
    }
}

function updateButtons() {
    for (let i = 0; i < variations_count; i++) {
        const v = document.getElementById("BtnV-" + i);
        const d = v.children;
        for (let i = 0; i < d.length; i++) {
            d[i].addEventListener("click", function(e) {
                resetSelected();
                e.stopPropagation();
                var id = e.target.id.split("|");
                selected[parseInt(id[0])] = id[1];
                updateSelected();
            });

            d[i].addEventListener("resetSelected", function(e) {
                if(e.target.classList.contains(VImgClass)) {
                    if(e.target.firstElementChild.classList.contains(VImgBorderClass)) {
                        e.target.firstElementChild.classList.remove(VImgBorderClass);
                    }
                }else {
                    if(e.target.classList.contains(VBtnClassS)) {
                        e.target.classList.remove(VBtnClassS);
                        e.target.firstElementChild.classList.remove(VTxtClassS);

                        e.target.classList.add(VBtnClass);
                        e.target.firstElementChild.classList.add(VTxtClass);
                    }
                }
            });
        }
    }
}

first();

// Zoom image

const container = document.getElementById('zoomContainer');
const image = document.getElementById('zoomImage');

container.addEventListener('mousemove', function(e) {
    const boundingRect = this.getBoundingClientRect();
    const offsetX = e.clientX - boundingRect.left;
    const offsetY = e.clientY - boundingRect.top;
    const imageWidth = image.offsetWidth;
    const imageHeight = image.offsetHeight;
    const scaleX = offsetX / imageWidth * 100;
    const scaleY = offsetY / imageHeight * 100;
    image.style.transformOrigin = `${scaleX}% ${scaleY}%`;
});

container.addEventListener('mouseenter', function() {
    image.classList.add('zoomed-image');
});

container.addEventListener('mouseleave', function() {
    image.classList.remove('zoomed-image');
});