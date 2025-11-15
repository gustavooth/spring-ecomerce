<script lang="ts">
  import { onMount } from "svelte";
  import Product from "./product.svelte";
  import { requestIndex, type ProductPageResponse } from "$lib/request";

  let pages:ProductPageResponse[] = $state([]);

  onMount(async () => {
    const _pages = await requestIndex();
    if (_pages != undefined) {
      pages = _pages.data;
    }
  })
</script>

<div class="w-9/10 mt-5 mb-5 flex items-center flex-col">
  <h2 class="text-2xl">Todos os produtos</h2>
  <div class="mt-5 w-full justify-center flex flex-wrap">

    {#each pages as page}
    <Product page={page}></Product>
    {/each}
    

  </div>
</div>