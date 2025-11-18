<script lang="ts">
  import { requestSelectPages, type NewPageRequest, type ProductPageResponse, type UpdateProductPageRequest } from "$lib/admin/request";
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

  function handleEditPage(id:number) {
    window.location.href = "/admin/product-manager/update-page?pageId="+id;
  }

</script>

<AdminHeader></AdminHeader>
<div class="flex w-full justify-center mt-4">
  <div class="w-2/3">
    <div class="flex flex-col">
       <div>
          <span class="text-lg">Produtos:</span>
          <button onclick={() => {window.location.href = "/admin/product-manager/new-page"}} class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
            <Icon src={BsPlusLg}></Icon>
          </button>
        </div>
      {#each pages as page, i}
        <div class="mt-2 flex flex-col p-1 border border-gray-500">
          <div class="flex justify-between w-full">
            <button onclick={() => {handleBtnOpenItem(i)}} class="flex items-center cursor-pointer">
              {#if page.products.length != 1}
                {#if itemsOppened[i]}
                  <Icon src={BsChevronUp}></Icon>
                {:else}
                  <Icon src={BsChevronDown}></Icon>
                {/if}
              {/if}
              <span class="ml-2">{page.title}</span>
              {#if page.products.length == 1}
              <span class="text-emerald-700 ml-2">R${page.products[0].price}</span>
              {/if}
            </button>
            <div>
              <button onclick={() => {handleEditPage(page.id)}} class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
                <Icon src={BsPencilFill}></Icon>
              </button>
              <button class="cursor-pointer border p-1 border-gray-500 rounded-sm text-xs">
                <Icon src={BsXLg}></Icon>
              </button>
            </div>
          </div>
          {#if itemsOppened[i] && page.products.length != 1}
          <div class="flex justify-between w-full">
            <div class="mt-2 basis-lg">
              <div>
                <span>Produtos:</span>
              </div>
              {#each page.products as product}
                {#if product.active}
                  <div class="mt-2">
                    <span class="text-emerald-800">R$ {product.price}</span>
                    <span class="m-1">-</span>
                    {#each product.values as value}
                      {#if value.active}
                        <span>{value.value}</span>
                        <span class="m-1">-</span>
                      {/if}
                    {/each}
                  </div>
                {/if}
              {/each}
            </div>
            <div class="mt-2 basis-lg">
              <div>
                <span>Atributos:</span>
              </div>
              <div class="mt-2 flex-col">
                {#each page.attributes as attribute}
                  {#if attribute.active}
                    <div class="mt-2">
                      <span>{attribute.name}:</span>
                      {#each attribute.values as value}
                        {#if value.active}
                          <span>{value.value}</span>
                          <span class="m-1">-</span>
                        {/if}
                      {/each}
                    </div>
                  {/if}
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