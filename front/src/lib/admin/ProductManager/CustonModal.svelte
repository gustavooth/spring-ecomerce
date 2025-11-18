<script lang="ts">
    import { Icon } from "svelte-icons-pack";
    import { BsXLg } from "svelte-icons-pack/bs";

  let {children, openModalFunc = $bindable(), onBtnClose = $bindable(), custonClass = $bindable()} = $props();

  let modal:HTMLElement|undefined = $state();
  let oppened = $state(false);

  let canClose = false;

  openModalFunc = async () => {
    oppened = true;
    canClose = false;

    while(!canClose) {
      await new Promise((data) => {setTimeout(data, 10)})
      if (modal!= undefined) {
        modal.style.marginTop = `${window.scrollY + 50}px`;
      }
    }

    oppened = false;
  }

  function onBtnCloseModal() {
    onBtnClose(closeModalFunc);
  }

  function closeModalFunc() {
    canClose = true;
  }
</script>

<div class="absolute w-full flex justify-center z-10" bind:this={modal}>
{#if oppened}
  <div class="w-full h-full flex justify-center">
    <div class={`bg-gray-50 border border-gray-500 rounded-lg shadow-lg ${custonClass}`}>
      <div class="w-full flex justify-end">
        <button onclick={onBtnCloseModal} class="absolute p-1 cursor-pointer">
          <Icon src={BsXLg}></Icon>
        </button>
      </div>
      {@render children?.()}
    </div>
  </div>
{/if}
</div>