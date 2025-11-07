<script lang="ts">
  import Imagem1 from "$lib/assets/produtos/1/Branco.png"
  import Imagem2 from "$lib/assets/produtos/1/Preto.png"
  import { Icon } from "svelte-icons-pack";
  import {BsTruck} from "svelte-icons-pack/bs";
</script>

<a class="w-60 bg-gray-200 flex flex-col items-center p-2 rounded-md m-2 cursor-pointer" href="/produto/test">
  <div class="w-49/50 h-50">
    <img src="{Imagem2}" alt="Imagem do produto" class="w-full h-full rounded-sm">
  </div>
  <div class="flex w-full">
    <span class="mt-1 m-1 leading-4 text-base">
      <span>Frete Gratis</span>
      North edge APACHE-46 relógio digi...</span>
  </div>
  <div class="flex w-full">
    <div class="flex justify-between text-sky-950 font-bold text-2xl m-1">
      <span class="text-xs mt-1 mr-0.5">R$</span>
      <span>149</span>
      <span class="text-xs mt-1 ml-0.5">00</span>
    </div>
    <div class="w-full flex items-end justify-end mb-1 mr-2">
      <span class="text-sm">1000 vendidos</span>
    </div>
  </div>
</a>