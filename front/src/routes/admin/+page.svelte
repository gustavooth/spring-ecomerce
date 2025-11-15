<script lang="ts">
  import MinimalHeader from "$lib/components/minimal_header.svelte";
  import { Icon } from "svelte-icons-pack";
  import { BsBagDash, BsBagFill, BsBagPlusFill, BsFolderFill } from "svelte-icons-pack/bs";

</script>

<MinimalHeader></MinimalHeader>
<div class="flex w-full mt-5 justify-center">
  <a class="flex items-center justify-center m-5" href="/admin/product-manager">
    <div class="w-60 h-60 rounded-lg text-gray-800 text-8xl flex flex-col items-center border-2 border-gray-800 cursor-pointer">
      <Icon src={BsBagFill} className="mt-4"></Icon>
      <span class="text-lg mt-10">Gerenciar produtos</span>
    </div>
    </a>
    <a class="flex items-center justify-center m-5" href="/admin/file-manager">
    <div class="w-60 h-60 rounded-lg text-gray-800 text-8xl flex flex-col items-center border-2 border-gray-800 cursor-pointer">
      <Icon src={BsFolderFill} className="mt-4"></Icon>
      <span class="text-lg mt-10">Gerenciar arquivos</span>
    </div>
  </a>
</div>