<script lang="ts">
  import { requestRemoveAttribute, requestRemoveAttributeValue, requestRemovePage, requestRemovePageImage, requestRemoveProduct, requestSelectPages, type ProductPageResponse } from "$lib/admin/request";
  import AdminHeader from "$lib/components/admin_header.svelte";
  import { onMount } from "svelte";
  import { Icon } from "svelte-icons-pack";
  import { BsChevronDown, BsChevronUp, BsPencilFill, BsPlusLg, BsXLg } from "svelte-icons-pack/bs";

  let itemsOppened:boolean[] = $state([]);

  let pages:ProductPageResponse[] = $state([]);

  onMount(async () => {
    updateState();
  })

  async function updateState() {
    const resp = await requestSelectPages();
    if (resp != undefined && resp.success) {
      pages = resp.data.data;
    }

    const oppened_start = false;
    itemsOppened.fill(oppened_start, pages.length);
  }

  function handleBtnOpenItem(index:number) {
    itemsOppened[index] = !itemsOppened[index];
  }

  async function handleBtnRemovePage(id:number, name:string) {
    if (!confirm(`Remover pagina ${name}?`)) {
      return;
    }

    const resp = await requestRemovePage({id:id});
    if (resp != undefined) {
      if (resp.success) {
        alert("Pagina removida com sucesso!");
      }else {
        console.error(resp.message);
      }
    }
    updateState();
  }

  async function handleBtnRemoveProduct(id:number) {
    if (!confirm(`Remover pagina ${name}?`)) {
      return;
    }

    const resp = await requestRemoveProduct({id:id});
    if (resp != undefined) {
      if (resp.success) {
        alert("Produto removido com sucesso!");
      }else {
        console.error(resp.message);
      }
    }
    updateState();
  }

  async function handleBtnRemoveAttribute(id:number, name:string) {
    if (!confirm(`Remover atributo ${name} e todos os seus valores ?`)) {
      return;
    }

    const resp = await requestRemoveAttribute({id:id});
    if (resp != undefined) {
      if (resp.success) {
        alert("Atributo removido com sucesso!");
      }else {
        console.error(resp.message);
      }
    }
    updateState();
  }

  async function handleBtnRemovePageImage(id:number) {
    if (!confirm("Remover imagem ?")) {
      return;
    }

    const resp = await requestRemovePageImage({id:id});
    if (resp != undefined) {
      if (resp.success) {
        alert("Imagem removida com sucesso!");
      }else {
        console.error(resp.message);
      }
    }
    updateState();
  }

</script>

<AdminHeader></AdminHeader>
<div class="flex w-full justify-center mt-4">
  <div class="w-2/3">
    <div class="flex flex-col">
       <div>
          <span class="text-lg">Paginas e produtos:</span>
          <button onclick={() => {window.location.href = "/admin/product-manager/new-page"}} class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
            <Icon src={BsPlusLg}></Icon>
          </button>
        </div>
      {#each pages as page, i}
        <div class="mt-2 flex flex-col p-1 border border-gray-500">
          <div class="flex justify-between w-full">
            <button onclick={() => {handleBtnOpenItem(i)}} class="flex items-center cursor-pointer">
              {#if itemsOppened[i]}
                <Icon src={BsChevronUp}></Icon>
              {:else}
                <Icon src={BsChevronDown}></Icon>
              {/if}
              <span class="ml-2">{page.title}</span>
            </button>
            <div>
              <button class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
                <Icon src={BsPencilFill}></Icon>
              </button>
              <button onclick={async () => {handleBtnRemovePage(page.id, page.title)}} class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
                <Icon src={BsXLg}></Icon>
              </button>
            </div>
          </div>
          {#if itemsOppened[i]}
          <div class="flex justify-between w-full">
            <div class="mt-2 basis-lg">
              <div>
                <span>Produtos:</span>
                <button class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
                  <Icon src={BsPlusLg}></Icon>
                </button>
              </div>
              {#each page.products as product}
              <div class="mt-2">
                <span class="text-emerald-800">R$ {product.price}</span>
                <span class="m-1">-</span>
                {#each product.values as value}
                <span>{value.value}</span>
                <span class="m-1">-</span>
                {/each}
                <button onclick={async () => {handleBtnRemoveProduct(product.id)}} class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
                  <Icon src={BsXLg}></Icon>
                </button>
              </div>
              {/each}
            </div>
            <div class="mt-2 basis-lg">
              <div>
                <span>Attributos:</span>
                <button class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
                  <Icon src={BsPlusLg}></Icon>
                </button>
              </div>
              <div class="mt-2 flex-col">
                {#each page.attributes as attribute}
                  <div class="mt-2">
                    <span>{attribute.name}:</span>
                    {#each attribute.values as value}
                      <span>{value.value}</span>
                      <span class="m-1">-</span>
                    {/each}
                    <button onclick={async () => {handleBtnRemoveAttribute(attribute.id, attribute.name)}} class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
                      <Icon src={BsXLg}></Icon>
                    </button>
                  </div>
                {/each}
              </div>
            </div>
          </div>
          {/if}
        </div>
      {/each}
    </div>
  </div>
</div>