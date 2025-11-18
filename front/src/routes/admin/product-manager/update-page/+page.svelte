<script lang="ts">
    import { page } from "$app/stores";
  import UploadModal from "$lib/admin/FS/UploadModal.svelte";
  import { formatViewImagePath } from "$lib/admin/FS/utils";
  import CustonButton from "$lib/admin/ProductManager/CustonButton.svelte";
  import CustonModal from "$lib/admin/ProductManager/CustonModal.svelte";
  import CustonPanel from "$lib/admin/ProductManager/CustonPanel.svelte";
  import TextArea from "$lib/admin/ProductManager/TextArea.svelte";
  import TextInput from "$lib/admin/ProductManager/TextInput.svelte";
  import { requestNewPage, requestSelectPage, type NewAttributeRequest, type NewAttributeValueRequest, type NewPageImageRequest, type NewPageRequest, type NewProductRequest, type ProductPageResponse, type UpdateProductPageRequest } from "$lib/admin/request";
  import AdminHeader from "$lib/components/admin_header.svelte";
  import type { ItemList } from "$lib/fileServer";
  import { onMount } from "svelte";
  import { Icon } from "svelte-icons-pack";
  import { BsArrowClockwise, BsCheckSquare, BsSquare, BsTrashFill, BsXLg } from "svelte-icons-pack/bs";

  interface Product {
    id:number
    price:string
    stock:string
    imagePath:string
    active:boolean
    values:string[]
  }

  interface AttributeValue {
    id:number;
    value:string;
    active:boolean
  }

  interface Attribute {
    id:number
    name:string
    showImage:boolean
    values:AttributeValue[]
    active:boolean
  }

  let currentPage:ProductPageResponse|undefined = $state();

  let openImageModal = $state(async (selected:ItemList[]):Promise<ItemList[]> => {return selected;});
  let openCustonModal = $state(() => {});

  let titleInputValue:string = $state("");
  let slugInputValue:string = $state("");
  let shortDescInputValue:string = $state("");
  let descInputValue:string = $state("");
  let imagesInputValues:ItemList[] = $state([]);
  let priceInputValue:string = $state("");
  let startStockInputValue:string = $state("");

  let products:Product[] = $state([]);
  let attributes:Attribute[] = $state([]);

  let updateImages = false;

  onMount(async () => {
    const param = $page.url.searchParams.get("pageId");
    let pageId:number = 0;
    if (param != undefined) {
      pageId = parseInt(param);
    }
    
    const resp = await requestSelectPage({id:pageId});
    if (resp == undefined || !resp.success) {
      return;
    }

    titleInputValue = resp.data.title;
    slugInputValue = resp.data.slug;
    for (let i = 0; i < resp.data.images.length; i++) {
      const image = resp.data.images[i];
      imagesInputValues.push({isDir:false, path:image.path, name:""});
    }
    descInputValue = resp.data.description;
    shortDescInputValue = resp.data.shortDescription;

    for (let i = 0; i < resp.data.attributes.length; i++) {
      const attribute = resp.data.attributes[i];
      let values:AttributeValue[] = [];
      for (let j = 0; j < attribute.values.length; j++) {
        const value = attribute.values[j];
        values.push({id:value.id, value:value.value, active:value.active});
      }
      attributes.push({id:attribute.id, name:attribute.name, showImage:attribute.showImage, values:values, active:attribute.active});
    }

    for (let i = 0; i < resp.data.products.length; i++) {
      const product = resp.data.products[i];
      let values:string[] = [""].fill("", attributes.length);
      for (let j = 0; j < product.values.length; j++) {
        const value = product.values[j];
        
        loop_attributes:
        for (let j = 0; j < attributes.length; j++) {
          const attribute = attributes[j];
          for (let k = 0; k < attribute.values.length; k++) {
            const attributeValue = attribute.values[k];
            if (value.value == attributeValue.value) {
              values[j] = value.value;
              break loop_attributes;
            }
          }
        }
      }
      products.push({id:product.id, values:values, price:String(product.price), stock:String(product.stock), imagePath:product.imagePath, active:product.active})
    }
  })

  function newProductBlank():Product {
    const newProduct:Product = {id: -1, price:"", stock:"", imagePath:"", values:[], active:true};
    newProduct.values.fill("", attributes.length);
    return newProduct;
  }

  function updateState() {
    if (attributes.length > 0) {
      if (products.length == 0) {
        products.push(newProductBlank());
      }else {
        const newProduct = newProductBlank();
        for (let i = 0; i < products.length; i++) {
          products[i].values = [];
          products[i].values.fill("", attributes.length);
        }
      }
    }
  }

  async function handleBtnSelectImages() {
    updateImages = true;
    const selected:ItemList[] = await openImageModal([]);
    for (let i = 0; i < selected.length; i++) {
      imagesInputValues.push(selected[i]);
    }
  }

  function handleBtnRemoveImage(path:string) {
    updateImages = true;
    imagesInputValues = imagesInputValues.filter(item => item.path != path);
  }

  function handleBtnAddAttribute() {
    const name = prompt("Nome do atributo: (Ex: Cor, Tamanho)");
    if (name == undefined || name == "") {
      return;
    }

    if (attributes.find(item => item.name == name) != undefined) {
      return;
    }

    attributes.push({id: -1, name:name, values:[], showImage:false, active:true});
  }

  function handleBtnAddAttributeValue(attributeIndex:number) {
    const value = prompt("Valor do atributo: (Ex: Verde, Pequeno)");
    if (value == undefined || value == "") {
      return;
    }

    if (attributes[attributeIndex].values.find(item => item.value == value)) {
      return;
    }

    attributes[attributeIndex].values.push({id:-1, value:value, active:true});
  }

  function handleBtnAttributeImageToogle(attributeIndex:number) {
    attributes[attributeIndex].showImage = !attributes[attributeIndex].showImage;
  }

  function handleBtnRemoveAttribute(name:string) {
    if (!confirm("Desativar atributo: "+name)) {
      return;
    }

    for (let i = 0; i < attributes.length; i++) {
      if (attributes[i].name == name) {
        if (attributes[i].id == -1) {
          attributes = attributes.filter(item => item.name != name);
        }else {
          attributes[i].active = false;
        }
      }
    }
  }

  function handleBtnRestoreAttribute(name:string) {
    if (!confirm("Ativar atributo: "+name)) {
      return;
    }

    for (let i = 0; i < attributes.length; i++) {
      if (attributes[i].name == name) {
        attributes[i].active = true;
      }
    }
  }

  function handleBtnRemoveAttributeValue(value:string, attributeIndex:number) {
    if (!confirm("Desativar valor de atributo: "+value)) {
      return;
    }

    for (let i = 0; i < attributes[attributeIndex].values.length; i++) {
      if (attributes[attributeIndex].values[i].value == value) {
        if (attributes[attributeIndex].values[i].id == -1) {
          attributes[attributeIndex].values = attributes[attributeIndex].values.filter(item => item.value != value);
        }else {
          attributes[attributeIndex].values[i].active = false;
        }
      }
    }
  }

  function handleBtnRestoreAttributeValue(value:string, attributeIndex:number) {
    if (!confirm("Ativar valor de atributo: "+value)) {
      return;
    }

    for (let i = 0; i < attributes[attributeIndex].values.length; i++) {
      if (attributes[attributeIndex].values[i].value == value) {
        attributes[attributeIndex].values[i].active = true;
      }
    }
  }

  async function handleBtnAddProductImage(productIndex:number) {
    const selected = await openImageModal([]);
    if (selected == undefined || selected.length == 0) {
      return;
    }

    products[productIndex].imagePath = selected[0].path;
  }

  function handleBtnAddProduct() {
    products.push(newProductBlank());
  }

  function onCloseAttributeModal(closeFunc:any) {
    for (let i = 0; i < attributes.length; i++) {
      if (attributes[i].values.length == 0) {
        alert("Cada atributo deve conter pelo menos 1 valor!");
        return;
      }
    }
    closeFunc();
    updateState();
  }

  function handleBtnRemoveProduct(index:number) {
    if(!confirm("Desativar produto ?")) {
      return;
    }

    if (products[index].id == -1) {
      products = products.filter((item, i) => i != index);
    }else {
      products[index].active = false;
    }
  }

  function handleBtnRestoreProduct(index:number) {
    if(!confirm("Ativar produto ?")) {
      return;
    }

    products[index].active = true;
  }

  function handleBtnFinalUpdate() {
    if (currentPage == undefined) {
      return;
    }

    let request:UpdateProductPageRequest = {id:currentPage.id, title:titleInputValue, slug:slugInputValue, 
      shortDescription: shortDescInputValue, description: descInputValue, 
      products:[], attributes:[], images:[], updateProducts: [], updateAttribute: [], 
      updateAttributeValues: []}

    let deactivateProducts = [];
    for (let i = 0; i < products.length; i++) {
      const product = products[i];
      if (product.id != -1 && !product.active && currentPage.products[i].active) {
        deactivateProducts.push(product.id);
      }
    }

    let deactivateAttributes = [];
    let deactivateAttributeValues = [];
    for (let i = 0; i < attributes.length; i++) {
      const attribute = attributes[i];
      if (attribute.id != -1 && !attribute.active && currentPage.attributes[i].active) {
        deactivateAttributes.push(attribute.id);
      } else {
        for (let j = 0; j < attribute.values.length; j++) {
          const value = attribute.values[j];
          if (value.id != -1 && !value.active && currentPage.attributes[i].values[j].active) {
            deactivateAttributeValues.push(value.id);
          }
        }
      }
    }

  }
</script>
<UploadModal bind:openModal={openImageModal}></UploadModal>
<CustonModal bind:openModalFunc={openCustonModal} onBtnClose={onCloseAttributeModal} custonClass="w-160 h-130 overflow-auto p-2 flex flex-col">
  <span>Adicionar atributos</span>
  <div class="flex flex-wrap">
    {#each attributes as attribute, i}
    <CustonPanel custonClass="mt-2 mr-2 {!attribute.active ? "bg-red-100":""}">
      <div class="w-full flex justify-end">
        {#if attribute.active}
          <button onclick={() => {handleBtnRemoveAttribute(attribute.name)}} class="text-red-500 absolute cursor-pointer">
            <Icon src={BsTrashFill}></Icon>
          </button>
        {:else}
          <button onclick={() => {handleBtnRestoreAttribute(attribute.name)}} class="text-red-500 absolute cursor-pointer">
            <Icon src={BsArrowClockwise}></Icon>
          </button>
        {/if}
      </div>
      <span>{attribute.name}</span>
      <div class="flex items-center">
        <span>Mostrar imagem:</span>
        <button class="cursor-pointer ml-2" onclick={() => {handleBtnAttributeImageToogle(i)}}>
        {#if attribute.showImage}
          <Icon src={BsCheckSquare}></Icon>
        {:else}
          <Icon src={BsSquare}></Icon>
        {/if}
      </button>
      </div>
      <div class="mt-2 ml-2 flex flex-col">
        {#each attribute.values as value, j}
          <div class="{!value.active ? "bg-red-100":""}">
              <div class="w-full flex justify-end">
              {#if value.active}
                <button onclick={() => {handleBtnRemoveAttributeValue(value.value, i)}} class="text-red-500 absolute cursor-pointer mr-4">
                  <Icon src={BsTrashFill}></Icon>
                </button>
              {:else}
                <button onclick={() => {handleBtnRestoreAttributeValue(value.value, i)}} class="text-red-500 absolute cursor-pointer mr-4">
                  <Icon src={BsArrowClockwise}></Icon>
                </button>
              {/if}
            </div>
            <span>{value.value}</span>
          </div>
        {/each}
        <CustonButton onclick={() => {handleBtnAddAttributeValue(i)}} custonClass="mt-2 text-emerald-700" title="Adicionar valor"></CustonButton>
      </div>
    </CustonPanel>
  {/each}
  </div>
  <CustonButton onclick={handleBtnAddAttribute} custonClass="mt-6 text-emerald-700" title="Adicionar atributo"></CustonButton>
</CustonModal>
<AdminHeader link={"/admin/product-manager"}></AdminHeader>
<div class="w-full flex justify-center mt-6">
  <CustonPanel custonClass="mb-2 w-2/3">
    <span class="text-lg mb-4">Atualizar produto</span>
    <TextInput custonClass="mt-2" title="Titulo" bind:value={titleInputValue}></TextInput>
    <TextInput custonClass="mt-2" title="Slug" bind:value={slugInputValue}></TextInput>
    <CustonPanel custonClass="mt-2">
      <CustonButton onclick={handleBtnSelectImages} title="Selecionar imagens"></CustonButton>
      <div class="flex flex-row mt-2">
        {#each imagesInputValues as image, i}
          <div class="w-20 h-20 mr-2">
            <div class="flex justify-end">
              <button onclick={() => {handleBtnRemoveImage(image.path)}} class="absolute p-1 cursor-pointer text-red-500">
                <Icon src={BsXLg}></Icon>
              </button>
            </div>
            <span class="absolute p-1">{i}</span>
            <img class="w-full h-full  rounded-sm shadow-sm" src={formatViewImagePath(image.path)} alt="">
          </div>
        {/each}
      </div>
    </CustonPanel>
    <TextArea custonClass="mt-2" title="Descricao curta" bind:value={shortDescInputValue}></TextArea>
    <TextArea custonClass="mt-2" title="Descricao" bind:value={descInputValue}></TextArea>
    <div class="w-full h-1 bg-gray-500 rounded-lg mt-2 mb-2"></div>
    <CustonPanel>
      <CustonButton onclick={() => {openCustonModal()}} title="Adicionar atributos"></CustonButton>
    </CustonPanel>
    {#if products.length == 0}
    <TextInput bind:value={priceInputValue} custonClass="mt-2 w-1/5" title="Preco"></TextInput>
    <TextInput bind:value={startStockInputValue} custonClass="mt-2 w-1/5" title="Estoque"></TextInput>
    {:else}
      {#each products as product, i}
        <CustonPanel custonClass="mt-2 {!product.active ? "bg-red-100":""}">
          {#if !product.active}
            <span class="text-red-800">Produto desativado ou pendente.</span>
          {/if}
          <div class="w-full flex justify-end">
            {#if product.active}
              <button onclick={() => {handleBtnRemoveProduct(i)}} class="absolute text-red-600 cursor-pointer">
                <Icon src={BsTrashFill}></Icon>
              </button>
            {:else}
              <button onclick={() => {handleBtnRestoreProduct(i)}} class="absolute text-red-600 cursor-pointer">
                <Icon src={BsArrowClockwise}></Icon>
              </button>
            {/if}
          </div>
          <TextInput bind:value={product.price} custonClass="mt-2 w-1/5" title="Preco"></TextInput>
          <TextInput bind:value={product.stock} custonClass="mt-2 w-1/4" title="Estoque"></TextInput>
          <CustonPanel custonClass="mt-2">
            <span>Imagem:</span>
            {#if product.imagePath == ""}
            <CustonButton onclick={() => {handleBtnAddProductImage(i)}} custonClass="text-emerald-700" title="Adicionar imagem"></CustonButton>
            {:else}
            <img src={formatViewImagePath(product.imagePath)} class="w-15 h-15 rounded-sm shadow-sm" alt="">
            <CustonButton onclick={() => {handleBtnAddProductImage(i)}} custonClass="text-emerald-700 mt-2" title="Alterar imagem"></CustonButton>
            {/if}
          </CustonPanel>
          <CustonPanel custonClass="mt-2">
            <span class="mr-2">Atributos:</span>
            {#each attributes as attribute, i}
              <CustonPanel custonClass="mt-2 flex-row items-center">
                <span>{attribute.name}:</span>
                {#each attribute.values as value, j}
                  {#if value.active}
                    <div class="mr-2">
                      {#if product.values[i] == value.value}
                        <CustonButton onclick={() => {product.values[i] = value.value}} custonClass="text-emerald-700" title={value.value}></CustonButton>
                      {:else}
                        <CustonButton onclick={() => {product.values[i] = value.value}} custonClass="text-red-700" title={value.value}></CustonButton>
                      {/if}
                    </div>
                  {/if}
                {/each}
              </CustonPanel>
            {/each}
          </CustonPanel>
        </CustonPanel>
      {/each}
      <CustonButton onclick={handleBtnAddProduct} custonClass="mt-2" title="Adicionar variacao"></CustonButton>
    {/if}
    <div class="w-full flex justify-end mt-2">
      <CustonButton title="Atualizar" custonClass={"text-emerald-700"}></CustonButton>
    </div>
  </CustonPanel>
</div>
