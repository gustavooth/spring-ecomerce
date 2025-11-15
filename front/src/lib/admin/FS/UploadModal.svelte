<script lang="ts">
  import { Icon } from "svelte-icons-pack";
  import { BsArrowRight, BsXLg } from "svelte-icons-pack/bs";
  import ItemsList from "./ItemsList.svelte";
  import { ItemType } from "$lib/fileServer";
  import { onMount } from "svelte";

  //TODO: open modal deve receber uma array com items ja selecionados anteriormente para evitar que items repitidos sejam adicionados!

  let {openModal = $bindable(), filterType = $bindable(ItemType.OTHER), selectMultiple = $bindable(true)} = $props();

  let opened = $state(false);
  let selectedItems:string[] = $state([]);

  let canClose = false;

  let modal:HTMLElement|undefined = $state()

  openModal = async (_selectedItems:string[]) => {
    selectedItems = _selectedItems;
    canClose = false;
    opened = true;

    if (modal!= undefined) {
      modal.style.marginTop = `${window.scrollY + 50}px`;
    }

    while(!canClose) {
      await new Promise((data) => {setTimeout(data, 100)})
    }

    opened = false;
    return selectedItems;
  }

  function handleSelectButton() {
    canClose = true;
  }
</script>

<div bind:this={modal} class="absolute w-full">
  {#if opened}
  <div class="h-100 flex justify-center">
    <div class="w-3/5 h-100 border rounded-lg bg-gray-50 border-gray-500 p-2">
      <div class="w-full flex justify-between text-xl">
        <span>Upload de arquivos</span>
        <button onclick={handleSelectButton} class="cursor-pointer">
          <Icon src={BsXLg}></Icon>
        </button>
      </div>
      <div class="flex relative h-4/5 items-center justify-center mt-2">
        <ItemsList bind:selectedItems={selectedItems} filterType={filterType} selectMultiple={selectMultiple}></ItemsList>
      </div>
      <div class="w-full flex justify-end text-xl mt-1">
        <button onclick={handleSelectButton} class="cursor-pointer">
          <span class="mr-2">Selecionar</span>
        </button>
      </div>
    </div>
  </div>
  {/if}
</div>