<script lang="ts">
    import ItemsList from "$lib/admin/FS/ItemsList.svelte";
  import UploadModal from "$lib/admin/FS/UploadModal.svelte";
    import { formatViewImagePath } from "$lib/admin/FS/utils";
  import CustonButton from "$lib/admin/ProductManager/CustonButton.svelte";
  import CustonModal from "$lib/admin/ProductManager/CustonModal.svelte";
  import CustonPanel from "$lib/admin/ProductManager/CustonPanel.svelte";
  import TextArea from "$lib/admin/ProductManager/TextArea.svelte";
  import TextInput from "$lib/admin/ProductManager/TextInput.svelte";
    import { requestNewPage, type NewAttributeRequest, type NewAttributeValueRequest, type NewPageImageRequest, type NewPageRequest, type NewProductRequest } from "$lib/admin/request";
  import AdminHeader from "$lib/components/admin_header.svelte";
    import Product from "$lib/components/product.svelte";
  import type { ItemList } from "$lib/fileServer";
    import { Icon } from "svelte-icons-pack";
    import { BsCheckSquare, BsSquare, BsTrash, BsTrashFill, BsX, BsXLg } from "svelte-icons-pack/bs";

  let {pageId = $bindable(undefined)} = $props();

  interface Product {
    price:string
    startStock:string
    imagePath:string
    values:string[]
  }

  interface Attribute {
    name:string
    showImage:boolean
    values:string[]
  }

  let openImageModal = $state(async (selected:ItemList[]):Promise<ItemList[]> => {return selected;});
  let openCustonModal = $state(() => {});
  let closeCustonModal = $state(() => {});

  let titleInputValue:string = $state("");
  let slugInputValue:string = $state("");
  let shortDescInputValue:string = $state("");
  let descInputValue:string = $state("");
  let imagesInputValues:ItemList[] = $state([]);
  let priceInputValue:string = $state("");
  let startStockInputValue:string = $state("");

  let products:Product[] = $state([]);
  let attributes:Attribute[] = $state([]);

  function newProductBlank():Product {
    const newProduct:Product = {price:"", startStock:"", imagePath:"", values:[]};
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
    const selected:ItemList[] = await openImageModal([]);
    for (let i = 0; i < selected.length; i++) {
      imagesInputValues.push(selected[i]);
    }
  }

  function handleBtnRemoveImage(path:string) {
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

    attributes.push({name:name, values:[], showImage:false});
  }

  function handleBtnAddAttributeValue(attributeIndex:number) {
    const value = prompt("Valor do atributo: (Ex: Verde, Pequeno)");
    if (value == undefined || value == "") {
      return;
    }

    if (attributes[attributeIndex].values.find(item => item == value)) {
      return;
    }

    attributes[attributeIndex].values.push(value);
  }

  function handleBtnAttributeImageToogle(attributeIndex:number) {
    attributes[attributeIndex].showImage = !attributes[attributeIndex].showImage;
  }

  function handleBtnRemoveAttribute(name:string) {
    attributes = attributes.filter(item => item.name != name);

    if (attributes.length == 0) {
      products = [];
    }
  }

  function handleBtnRemoveAttributeValue(value:string, attributeIndex:number) {
    attributes[attributeIndex].values = attributes[attributeIndex].values.filter(item => item != value);
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
    if (products.length == 1) {
      return;
    }

    products = products.filter((item, i) => i != index);
  }

  async function handleBtnFinalAdd() {
    if (titleInputValue == "" || slugInputValue == "" || imagesInputValues.length < 1) {
      alert("Preencha os campos (title e slug) e selecione pelo menos 1 imagem!");
      return;
    }

    let request:NewPageRequest = {title: titleInputValue, slug: slugInputValue, shortDescription: shortDescInputValue, description: descInputValue, products: [], attributes: [], images: []};
    
    let newProducts:NewProductRequest[] = [];
    if (products.length > 0) {
      for (let i = 0; i < products.length; i++) {
        if (products[i].imagePath == "" || products[i].price == "" || products[i].startStock == "" || products[i].values.length != attributes.length) {
          alert("Para cada produto preencha todos os campos e selecione um valor para cada atributo!")
          return;
        }

        newProducts.push({price: products[i].price, stock: products[i].startStock, imagePath: products[i].imagePath, values: products[i].values})
      }
    }else {
      newProducts.push({price: priceInputValue, stock: startStockInputValue, imagePath: "", values: []})
    }
    request.products = newProducts;

    let newAttributes:NewAttributeRequest[] = [];
    for (let i = 0; i < attributes.length; i++) {
      let newValues:NewAttributeValueRequest[] = [];
      for (let j = 0; j < attributes[i].values.length; j++) {
        newValues.push({value:attributes[i].values[j]});
      }

      newAttributes.push({name:attributes[i].name, showImage:attributes[i].showImage, values:newValues});
    }
    request.attributes = newAttributes;

    let images:NewPageImageRequest[] = [];
    for (let i = 0; i < imagesInputValues.length; i++) {
      images.push({index:i, path:imagesInputValues[i].path});
    }
    request.images = images;

    const resp = await requestNewPage(request);
    if (resp != undefined && resp.success) {
      alert(`Produto ${titleInputValue} - id:${resp.data.id} - Adicionado !`);
    }
  }
</script>
<UploadModal bind:openModal={openImageModal}></UploadModal>
<CustonModal bind:openModalFunc={openCustonModal} onBtnClose={onCloseAttributeModal} custonClass="w-100 p-2 flex flex-col">
  <span>Adicionar atributos</span>
  {#each attributes as attribute, i}
    <CustonPanel custonClass="mt-2">
      <div class="w-full flex justify-end">
        <button onclick={() => {handleBtnRemoveAttribute(attribute.name)}} class="text-red-500 absolute cursor-pointer">
          <Icon src={BsTrashFill}></Icon>
        </button>
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
        {#each attribute.values as value}
          <div class="w-full flex justify-end">
            <button onclick={() => {handleBtnRemoveAttributeValue(value, i)}} class="text-red-500 absolute cursor-pointer mr-4">
              <Icon src={BsTrashFill}></Icon>
            </button>
          </div>
          <span>{value}</span>
        {/each}
        <CustonButton onclick={() => {handleBtnAddAttributeValue(i)}} custonClass="mt-2 text-emerald-700" title="Adicionar valor"></CustonButton>
      </div>
    </CustonPanel>
  {/each}
  <CustonButton onclick={handleBtnAddAttribute} custonClass="mt-6 text-emerald-700" title="Adicionar atributo"></CustonButton>
</CustonModal>
<AdminHeader link={"/admin/product-manager"}></AdminHeader>
<div class="w-full flex justify-center mt-6">
  <CustonPanel custonClass="mb-2 w-2/3">
    {#if pageId == undefined}
    <span class="text-lg mb-4">Adicionar produto</span>
    {:else}
    <span class="text-lg mb-4">Atualizar produto</span>
    {/if}
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
    <TextInput bind:value={startStockInputValue} custonClass="mt-2 w-1/5" title="Estoque inicial"></TextInput>
    {:else}
      {#each products as product, i}
        <CustonPanel custonClass="mt-2">
          <div class="w-full flex justify-end">
            <button onclick={() => {handleBtnRemoveProduct(i)}} class="absolute text-red-600 cursor-pointer">
              <Icon src={BsTrashFill}></Icon>
            </button>
          </div>
          <TextInput bind:value={product.price} custonClass="mt-2 w-1/5" title="Preco"></TextInput>
          <TextInput bind:value={product.startStock} custonClass="mt-2 w-1/4" title="Estoque inicial"></TextInput>
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
                  <div class="mr-2">
                    {#if product.values[i] == value}
                      <CustonButton onclick={() => {product.values[i] = value}} custonClass="text-emerald-700" title={value}></CustonButton>
                    {:else}
                      <CustonButton onclick={() => {product.values[i] = value}} custonClass="text-red-700" title={value}></CustonButton>
                    {/if}
                  </div>
                {/each}
              </CustonPanel>
            {/each}
          </CustonPanel>
        </CustonPanel>
      {/each}
      <CustonButton onclick={handleBtnAddProduct} custonClass="mt-2" title="Adicionar variacao"></CustonButton>
    {/if}
    <div class="w-full flex justify-end mt-2">
      <CustonButton onclick={handleBtnFinalAdd} title="Adicionar" custonClass={"text-emerald-700"}></CustonButton>
    </div>
  </CustonPanel>
</div>
