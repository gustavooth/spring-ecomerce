<script lang="ts">
  import { DOWNLOAD_FILE_ROUTE, requestMovePath, requestRemovePath, VIEW_IMAGE_ROUTE } from "$lib/fileServer";
  import { Icon } from "svelte-icons-pack";
  import { BsArrowDownCircle, BsCheckSquare, BsFileEarmark, BsFileEarmarkBinary, BsFileEarmarkCode, BsFileEarmarkImage, BsFileEarmarkMusic, BsFileEarmarkPlay, BsFileEarmarkRichtext, BsFileEarmarkText, BsFileEarmarkZip, BsFileImage, BsFolder, BsPencilSquare, BsSquare, BsTrashFill } from "svelte-icons-pack/bs";
  import { formatViewImagePath, ItemType } from "./utils";
    import ItemsList from "./ItemsList.svelte";

  let {selected = $bindable(false), pendingAction = $bindable(false), type = $bindable(ItemType.OTHER), imageView = $bindable(true), item = $bindable(), actions = $bindable(true), onChange = $bindable((item:ItemsList, type:number) => {}), onClick =  $bindable((item:ItemsList, type:number) => {}), onSelect =  $bindable((item:ItemsList, type:number) => {})} = $props();

  function handleCheckBoxBtn() {
    if (pendingAction) {
      return;
    }

    onSelect(item, type);
  }

  async function handleDeleteBtn() {
    if (!window.confirm("Remover o caminho: " +item.path)) {
      return
    }
    const response = await requestRemovePath({paths:[item.path]});

    if (response != undefined) {
      onChange(item);
    }
  }

  async function handleRenameBtn() {
    let newPath = prompt("Novo caminho:", item.path);
    if (newPath == undefined || newPath == item.path) {
      return;
    }

    const response = await requestMovePath({paths: [item.path, newPath]});

    if (response != undefined) {
      onChange(item.path, type);
    }
  }

  function handleDownloadBtn() {
    window.open(DOWNLOAD_FILE_ROUTE+item.path);
  }

  function handleClickItem() {
    onClick(item, type)
  }
</script> 

<div class="w-full text-3xl p-2 {selected ? 'bg-gray-400':''} {pendingAction ? 'bg-blue-300': ''} {!selected && !pendingAction ? "hover:bg-gray-200": ""}">
  <div class="flex w-full">
    <button onclick={handleCheckBoxBtn} class="text-base cursor-pointer">
      {#if selected || pendingAction}
      <Icon src={BsCheckSquare}></Icon>
      {:else}
      <Icon src={BsSquare}></Icon>
      {/if}
    </button>
    <div class="flex w-full ml-2 text-lg justify-between">
      <button onclick={handleClickItem} class="flex text-3xl cursor-pointer hover:underline">
        {#if type == ItemType.IMAGE}
        {#if imageView}
        <img class="w-10 h-10" src={formatViewImagePath(item.path)} alt={item.name}>
        {:else}
        <Icon src={BsFileEarmarkImage}></Icon>
        {/if}
        {:else if type == ItemType.FOLDER}
        <Icon src={BsFolder}></Icon>
        {:else if type == ItemType.VIDEO}
        <Icon src={BsFileEarmarkPlay}></Icon>
        {:else if type == ItemType.MUSIC}
        <Icon src={BsFileEarmarkMusic}></Icon>
        {:else if type == ItemType.COMPRESS}
        <Icon src={BsFileEarmarkZip}></Icon>
        {:else if type == ItemType.DOCUMENT}
        <Icon src={BsFileEarmarkRichtext}></Icon>
        {:else if type == ItemType.TEXT}
        <Icon src={BsFileEarmarkText}></Icon>
        {:else if type == ItemType.CODE}
        <Icon src={BsFileEarmarkCode}></Icon>
        {:else if type == ItemType.BINARY}
        <Icon src={BsFileEarmarkBinary}></Icon>
        {:else}
        <Icon src={BsFileEarmark}></Icon>
        {/if}
        <span class="ml-2 text-lg">{item.name}</span>
      </button>
      <div>
        {#if actions}
        <button onclick={handleDeleteBtn} class="cursor-pointer">
          <Icon src={BsTrashFill}></Icon>
        </button>
        <button onclick={handleRenameBtn} class="cursor-pointer">
          <Icon src={BsPencilSquare}></Icon>
        </button>
        {#if type != ItemType.FOLDER}
        <button onclick={handleDownloadBtn} class="cursor-pointer">
          <Icon src={BsArrowDownCircle}></Icon>
        </button>
        {/if}
        {/if}
      </div>
    </div>
  </div>
</div>