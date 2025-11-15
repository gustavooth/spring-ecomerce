<script lang="ts">
  import UploadModal from "$lib/admin/FS/UploadModal.svelte";
  import { formatViewImagePath } from "$lib/admin/FS/utils";
  import { requestNewAttribute, requestNewAttributeValue, requestNewPage, requestNewPageImage, requestNewProduct, requestNewProductValue, requestSelectPage, type NewAttributeRequest, type NewAttributeValueRequest, type NewPageImageRequest, type NewPageRequest, type NewProductRequest, type NewProductValueRequest, type ProductPageResponse } from "$lib/admin/request";
  import AdminHeader from "$lib/components/admin_header.svelte";
  import type { ItemList } from "$lib/fileServer";
  import { onMount } from "svelte";
  import { Icon } from "svelte-icons-pack";
  import { BsXLg } from "svelte-icons-pack/bs";

  export const STEP = {
    PAGE: 0,
    ATTRIBUTE: 1,
    PRODUCT: 2
  }

  let currentStep = $state(0);

  let {firstStep = $bindable(STEP.ATTRIBUTE), pageId = $bindable(13)} = $props();

  onMount(() => {
    reloadState();
    currentStep = firstStep;
  })

  let openModal = $state(async (selected:ItemList[]) => {return selected;});

  let page:ProductPageResponse|undefined = $state(undefined);

  let titleInputValue:string = $state("");
  let slugInputValue:string = $state("");
  let shortDescInputValue:string = $state("");
  let descInputValue:string = $state("");
  let imagesInputValues:ItemList[] = $state([]);

  let attributeInputValue:string = $state("");
  let attributeValueInputValues:string[] = $state([]);

  let priceInputValue:string = $state("");
  let productImageInput:ItemList|undefined = $state(undefined)
  let selectedAttributeValues:number[] = $state([]);

  async function handleBtnSelectImages() {
    const selected:ItemList[] = await openModal(imagesInputValues);

    for (let i = 0; i < selected.length; i++) {
      imagesInputValues.push(selected[i]);
    }
  }

  async function handleBtnRemoveImage(imagePath:string) {
    imagesInputValues = imagesInputValues.filter(item => item.path != imagePath);
  }

  async function handleBtnSelectAttributeValue(index:number, valueId:number) {
    selectedAttributeValues[index] = valueId;
  }

  async function sendNewPage():Promise<Boolean> {
    const request:NewPageRequest = {title:titleInputValue, slug:slugInputValue, shortDescription:shortDescInputValue, description:descInputValue};

    const resp = await requestNewPage(request);
    if (resp != undefined && resp.success) {
      pageId = resp.data.id;
      for (let i = 0; i < imagesInputValues.length; i++) {
        const image_request:NewPageImageRequest = {pageId:pageId, index:i, path:imagesInputValues[i].path};

        const image_resp = await requestNewPageImage(image_request);
        if (image_resp == undefined || !image_resp.success) {
          return false;
        }
      }
      return true;
    }

    return false;
  }

  async function sendNewAttribute():Promise<Boolean> {
    if (page != undefined) {
      const request:NewAttributeRequest = {pageId:page.id, name:attributeInputValue};

      const resp = await requestNewAttribute(request);
      if (resp != undefined && resp.success) {
        return true;
      }
    }

    return false;
  }

  async function sendNewAttributeValue(attributeId:number, value:string):Promise<Boolean> {
    if (page != undefined) {
      const request:NewAttributeValueRequest = {attributeId:attributeId, value};

      const resp = await requestNewAttributeValue(request);
      if (resp != undefined && resp.success) {
        return true;
      }
    }

    return false;
  }

  async function sendNewProduct():Promise<Boolean> {
    if (page != undefined) {
      const request:NewProductRequest = {pageId:page.id, price:priceInputValue, imagePath:""};

      if (productImageInput != undefined) {
        request.imagePath = productImageInput.path;
      }

      const resp = await requestNewProduct(request);
      if (resp != undefined && resp.success) {
        for (let i = 0; i < selectedAttributeValues.length; i++) {
          const requestValue:NewProductValueRequest = {productId:resp.data.id, attributeValueId:selectedAttributeValues[i]};
          const respValues = await requestNewProductValue(requestValue);
          if (respValues == undefined || !respValues.success) {
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  async function reloadState() {
    if (pageId != undefined) {
      const response = await requestSelectPage({id:pageId})
      if (response != undefined && response.success) {
        page = response.data;

        titleInputValue = page.title;
        slugInputValue = page.slug;
        shortDescInputValue = page.shortDescription;
        descInputValue = page.description;

        imagesInputValues = [];
        for (let i = 0; i < page.images.length; i++) {
          for (let j = 0; j < page.images.length; j++) {
            if (page.images[i].index == j) {
              console.log(page.images[j].index);
              imagesInputValues.push({isDir:false,name:"",path:page.images[j].path});
              break;
            }
          }
        }

        const start_attribute = "";
        attributeValueInputValues.fill(start_attribute, page.attributes.values.length);

        const start_index = -1;
        selectedAttributeValues.fill(start_index, page.attributes.length);
      }
    }
  }

  async function handleBtnNext() {
    if (currentStep == STEP.PAGE) {
      if (titleInputValue != "" && slugInputValue != "") {
        await sendNewPage();
        await reloadState();
        currentStep = STEP.ATTRIBUTE;
      }else {
        alert("Os campos titulo e Slug sao obrigatorios!");
      }
    }else if (currentStep == STEP.ATTRIBUTE) {
      if (page == undefined) {
        return;
      }

      for (let i = 0; i < page.attributes.length; i++) {
        if (page.attributes[i].values.length < 1) {
          alert("Cada atributo deve conter pelo menos 1 valor!");
          return;
        }
      }
      currentStep = STEP.PRODUCT;
    }else {
      window.location.href = ("/admin/product-manager");
    }
  }

  async function handleBtnBack() {
    reloadState();
    if (currentStep == STEP.PRODUCT) {
      currentStep = STEP.ATTRIBUTE;
    }else if (currentStep == STEP.ATTRIBUTE) {
      currentStep = STEP.PAGE;
    }
  }

  async function handleBtnAddAttribute() {
    if (attributeInputValue != "") {
      await sendNewAttribute();
      attributeInputValue = "";
      reloadState();
    }else {
      alert("Preencha o campo atributo!");
    }
  }

  async function handleBtnAddAttributeValue(attributeId:number, input_index:number) {
    const result = await sendNewAttributeValue(attributeId, attributeValueInputValues[input_index]);
    if (!result) {
      alert("Erro!");
      return;
    }
    alert("Atributo adicionado!");
    attributeValueInputValues[input_index] = "";
    await reloadState();
  }

  const formatarValorDouble = (valor: string): string => {
    if (typeof valor !== 'string' || !valor) {
      return '';
    }

    // 1. Remove tudo que não for dígito, vírgula ou ponto.
    // Ex: "R$ 1.234,56" -> "1.234,56"
    let stringLimpa = valor.replace(/[^0-9,.]/g, '');

    // 2. Troca todas as vírgulas por pontos.
    // Ex: "1.234,56" -> "1.234.56"
    stringLimpa = stringLimpa.replace(/,/g, '.');

    // 3. Garante que apenas o último ponto permaneça, para o caso de haver
    //    separadores de milhar.
    // Ex: "1.234.56" -> o resultado deve ser "1234.56"
    const partes = stringLimpa.split('.');

    // Se houver mais de um ponto na string...
    if (partes.length > 1) {
      // Pega todas as partes antes da última e junta (removendo os pontos)
      const parteInteira = partes.slice(0, -1).join('');
      // Pega a última parte, que é a parte decimal
      const parteDecimal = partes[partes.length - 1];
      // Junta tudo novamente no formato correto
      return `${parteInteira}.${parteDecimal}`;
    }

    // Se não houver pontos (ou apenas um), retorna a string como está.
    return stringLimpa;
  };

  function handleFormatPrice(event:Event) {
    const input = event.target as HTMLInputElement;

    input.value = formatarValorDouble(input.value);
    priceInputValue = input.value;
  }

  async function handleBtnSelectProductImage() {
    const selected:ItemList[] = await openModal(imagesInputValues);
    productImageInput = selected[0];
  }

  async function handleBtnAddProduct() {
    if (priceInputValue == "") {
      alert("Campo preco e obrigatorio!");
      return;
    }

    if (page == undefined || selectedAttributeValues.length != page.attributes.length) {
      alert("Selecione os atributos!");
      return;
    }

    for (let i = 0; i < selectedAttributeValues.length; i++) {
      if (selectedAttributeValues[i] == -1) {
        alert("Selecione os atributos!");
        return;
      }
    }

    const result = await sendNewProduct();
    if (!result) {
      alert("Erro!");
      return;
    }
    console.log("Produto adicionado!");
    selectedAttributeValues = [];
    await reloadState();
  }
</script>

<UploadModal bind:openModal={openModal}></UploadModal>
<AdminHeader link={"/admin/product-manager"}></AdminHeader>
{#if currentStep == STEP.PAGE}
<div class="w-full flex justify-center mt-6">
  <div class="flex flex-col w-2/3 border border-gray-500 p-2">
    {#if pageId == undefined}
    <span class="text-lg mb-4">Adicionar pagina de produto</span>
    {:else}
    <span class="text-lg mb-4">Atualizar pagina de produto</span>
    {/if}
    <span class="mt-2">Titulo*:</span>
    <input bind:value={titleInputValue} type="text" name="title" class="border border-gray-500 w-full p-1">
    <span class="mt-2">Slug*:</span>
    <input bind:value={slugInputValue} type="text" name="slug" class="border border-gray-500 w-full p-1">
    <span class="mt-2">Descricao curta:</span>
    <textarea bind:value={shortDescInputValue} name="shortDesc" class="border border-gray-500 w-full p-1"></textarea>
    <span class="mt-2">Descricao:</span>
    <textarea bind:value={descInputValue} name="desc" class="border border-gray-500 w-full p-1"></textarea>
    <div class="w-full flex flex-col border border-gray-500 p-2 mt-2">
      <button onclick={handleBtnSelectImages} class="cursor-pointer text-blue-500">Adicionar imagens</button>
      <div class="flex flex-wrap mt-2">
        {#each imagesInputValues as imageItem, i}
        <div class="w-20 h-20 m-2">
          <div class="absolute">
            <button onclick={() => {handleBtnRemoveImage(imageItem.path)}} class="absolute ml-2 p-1 text-red-500 cursor-pointer">
              <Icon src={BsXLg}></Icon>
            </button>
            <span>{i}</span>
          </div>
          <img class="w-full h-full rounded-lg mr-2" src={formatViewImagePath(imageItem.path)} alt="imagem">
        </div>
        {/each}
      </div>
    </div>
    <div class="w-full flex justify-end mt-6">
      <button onclick={handleBtnNext} class="p-2 border border-emerald-500 text-black cursor-pointer">
        <span>Proximo</span>
      </button>
    </div>
  </div>
</div>
{:else if currentStep == STEP.ATTRIBUTE}
<div class="w-full flex justify-center mt-6">
  <div class="flex flex-col w-2/3 border border-gray-500 p-2">
    <div class="flex w-full">
      <div class="basis-lg">
        <span class="text-lg mb-4">Adicionar atributo:</span>
        <div class="w-full mt-2">
          <input bind:value={attributeInputValue} type="text" name="attribute" id="" class="border border-gray-500 p-1" placeholder="Cor">
          <button onclick={handleBtnAddAttribute} class="ml-2 border border-gray-500 p-1 cursor-pointer">Adicionar</button>
        </div>
      </div>
      <div class="basis-lg">
        <span class="text-lg mb-4">Valores do atributo:</span>
        {#each page?.attributes as attribute, i}
          <div class="w-full mt-2">
            <div class="mt-2">
              <span>{attribute.name}:</span>
              {#each attribute.values as value}
                <span>{value.value}</span>
                <span class="m-2">-</span>
              {/each}
            </div>
            <input bind:value={attributeValueInputValues[i]} type="text" name="attribute" id="" class="border border-gray-500 p-1" placeholder="Azul">
            <button onclick={() => {handleBtnAddAttributeValue(attribute.id, i)}} class="ml-2 border border-gray-500 p-1 cursor-pointer">Adicionar</button>
          </div>
        {/each}
      </div>
    </div>
    <div class="w-full flex justify-between mt-6">
      <button onclick={handleBtnBack} class="p-2 border border-emerald-500 text-black cursor-pointer">
        <span>Voltar</span>
      </button>
      <button onclick={handleBtnNext} class="p-2 border border-emerald-500 text-black cursor-pointer">
        <span>Proximo</span>
      </button>
    </div>
  </div>
</div>
{:else if currentStep == STEP.PRODUCT}
<div class="w-full flex justify-center mt-6">
  <div class="flex flex-col w-2/3 border border-gray-500 p-2">
    <div class="flex w-full">
      <div class="basis-lg">
        <span class="text-lg mb-6">Adicionar produto:</span>
        <div class="flex flex-col w-full mt-2">
          <span>Preco*:</span>
          <input onchange={handleFormatPrice} type="text" name="attribute" id="" class="border border-gray-500 p-1" placeholder="199.99">
        </div>
        <div class="border border-gray-500 pl-2 mt-2"> 
          {#each page?.attributes as attribute, i}
            <div class="mt-2">
              <span>{attribute.name}</span>
              <div class="flex flex-wrap">
                {#each attribute.values as value}
                  {#if selectedAttributeValues[i] != value.id}
                  <button onclick={() => {handleBtnSelectAttributeValue(i, value.id)}} class="border border-gray-500 p-1 cursor-pointer m-1">{value.value}</button>
                  {:else}
                  <button onclick={() => {handleBtnSelectAttributeValue(i, value.id)}} class="border-3 border-black p-1 cursor-pointer m-1">{value.value}</button>
                  {/if}
                {/each}
              </div>
            </div>
          {/each}
        </div>
        <div class="mt-2 w-full border border-gray-500 p-2">
          <button onclick={handleBtnSelectProductImage} class=" text-blue-500 cursor-pointer">Selecionar imagem</button>
          {#if productImageInput != undefined}
            <img class="w-15 h-15 rounded border border-gray-500" src={formatViewImagePath(productImageInput.path)} alt="imagem">
          {/if}
        </div>
        <button onclick={() => {handleBtnAddProduct()}} class="mt-2 p-2 border border-gray-500 text-black cursor-pointer">
        <span>Adicionar</span>
      </button>
      </div>
      <div class="basis-lg ml-2">
        <span class="text-lg mb-4">Produtos:</span>
        {#each page?.products as product}
          <div class="mt-2 flex w-full items-center">
            {#if product.imagePath != ""}
              <img class="w-10 h-10 rounded-lg mr-2" src={formatViewImagePath(product.imagePath)} alt="i">
            {:else}
              <div class="w-10 h-10 mr-2"></div>
            {/if}
            <span class="">{product.price}</span>
            {#each product.values as value}
              <span class="ml-2">{value.value}</span>
            {/each}
          </div>
        {/each}
      </div>
    </div>
    <div class="w-full flex justify-between mt-6">
      <button onclick={handleBtnBack} class="p-2 border border-emerald-500 text-black cursor-pointer">
        <span>Voltar</span>
      </button>
      <button onclick={handleBtnNext} class="p-2 border border-emerald-500 text-black cursor-pointer">
        <span>Finalizar</span>
      </button>
    </div>
  </div>
</div>
{/if}
