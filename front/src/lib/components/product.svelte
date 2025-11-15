<script lang="ts">
    import { formatViewImagePath } from "$lib/admin/FS/utils";
  import Imagem2 from "$lib/assets/produtos/1/Preto.png"

  let {page = $bindable()} = $props();
</script>

<a class="w-60 bg-gray-200 flex flex-col items-center p-2 rounded-md m-2 cursor-pointer" href="/produto/test">
  <div class="w-49/50 h-50">
    <img src="{formatViewImagePath(page.images[0].path)}" alt="Imagem do produto" class="w-full h-full rounded-sm">
  </div>
  <div class="flex w-full">
    <span class="mt-1 m-1 leading-4 text-base">
      <span>Frete Gratis</span>
      {page.title}</span>
  </div>
  <div class="flex w-full">
    <div class="flex justify-between text-sky-950 font-bold text-2xl m-1">
      <span class="text-xs mt-1 mr-0.5">R$</span>
      <span>{page.products[0].price}</span>
      <span class="text-xs mt-1 ml-0.5">00</span>
    </div>
    <div class="w-full flex items-end justify-end mb-1 mr-2">
      <span class="text-sm">{Math.floor(page.products[0].price / 3)} vendidos</span>
    </div>
  </div>
</a>