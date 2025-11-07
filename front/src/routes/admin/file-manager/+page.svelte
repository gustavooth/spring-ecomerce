<script lang="ts">
  import AdminHeader from "$lib/components/admin_header.svelte";
  import { BsArrowLeft, BsArrowRight, BsHouse, BsSearch, BsArrowClockwise, BsFolder, BsFileEarmark, BsFileEarmarkImage, BsArrowUpCircleFill, BsArrowUpCircle, BsFolderPlus, BsArrowUp, BsFileEarmarkArrowUp, BsTrashFill, BsSquare, BsCheckSquare } from "svelte-icons-pack/bs";
  import { Icon } from "svelte-icons-pack";
  import { fs_upload, listFiles, newFolder, newUploadToken, removeItem, type ListFilesResponse, type NewTokenFile } from "$lib/fileServer";
  import { onMount } from "svelte";

  interface historyItem {
    path: string,
  };

  let currentPath = "/";

  let {searchPath = $bindable(currentPath), uploadInput = $bindable()} = $props();

  let items: ListFilesResponse|undefined = $state(undefined);

  let selectedItems:string[] = $state([]);

  let history: historyItem[] = [{path: currentPath}];
  let historyIndex = 0;

  onMount(async () => {
    reload_items();
  })
  
  function historyAdd(path: string) {
    // Remover todos os items depois do cursor
    let newHistory = [];
    for (let i = 0; i < historyIndex+1; i++) {
      newHistory.push(history[i]);
    }
    history = newHistory;

    // Adicionar o novo path e atualizar posicao do cursor
    history.push({path: path});
    historyIndex++;
  }

  function historyToLeft():string|undefined {
    if (historyIndex == 0) {
      return undefined;
    }

    historyIndex--;
    return history[historyIndex].path;
  }

  function historyToRight():string|undefined {
    if (history.length == historyIndex+1) {
      return undefined;
    }

    historyIndex++;
    return history[historyIndex].path;
  }

  async function goPath(path:string, updateHistory:boolean = true) {
    selectedItems = [];
    let newItems = await listFiles({path: path});
    searchPath = path;
    currentPath = path;
    items = newItems;

    if (updateHistory) {
      historyAdd(path);
    }
  }

  async function reload_items() {
    goPath(currentPath, false);
  }

  function handleLeftBtn() {
    let newPath = historyToLeft();
    if (newPath != undefined) {
      goPath(newPath, false);
    }else {
      reload_items();
    }
  }

  function handleRightBtn() {
    let newPath = historyToRight();
    if (newPath != undefined) {
      goPath(newPath, false);
    }else {
      reload_items();
    }
  }

  function handleReloadBtn() {
    reload_items();
  }

  function handleHomeBtn() {
    goPath("/");
  }

  function handleSerachBtn() {
    goPath(searchPath);
  }

  export const imgExt = ["png", "jpeg", "jpg", "webp", "gif", "svg"];
  function fileIsImage(path:string):boolean {
    for(let ext of imgExt) {
      if (path.includes(ext)) {
        return true;
      }
    }
    return false;
  }

  async function handleRemoveBtn(path: string, ask: boolean = true) {
    if (ask) {
        if (!window.confirm("Remover o caminho: " +path)) {
        return
      }
    }
    const response = await removeItem({path:path});
    console.debug(response);

    reload_items();
  }

  async function handleNewFolderBtn() {
    const folderName = prompt("Nome da nova pasta: ");
    if (folderName == undefined) {
      return;
    }

    const response = await newFolder({path:`${currentPath}/${folderName}`});

    if (response != null) {
      reload_items();
    }
  }

  function handleUploadBtn() {
    uploadInput.click();
  }

  function handleBoxBtn(path:string) {
    selectedItems.push(path);
  }

  function handleCheckBoxBtn(path:string) {
    selectedItems = selectedItems.filter(item => item != path);
  }

  function handleRemoveAllSelected() {
    for (let i = 0; i < selectedItems.length; i++) {
      handleRemoveBtn(selectedItems[i], false);
    }
  }

  async function handleUpload(event: Event) {
    const input = event.target as HTMLInputElement;
    const selectedFiles = input.files;

    if (selectedFiles == null) {
      return;
    }

    let files: NewTokenFile[] = [];

    for (let i = 0; i < selectedFiles.length; i++) {
      const file = selectedFiles[i];
      files.push({name: file.name, path: currentPath});
    }

    let tokenResp = await newUploadToken({files: files});

    if (tokenResp != undefined) {
      const filesArray: File[] = Array.from(selectedFiles);
      let uploadResp = await fs_upload(filesArray, tokenResp.token)
      if (uploadResp != undefined) {
        reload_items();
        alert("Upload concluido!");
      }
    }
  }
</script>

<div class="w-full min-h-screen flex flex-col items-center">
  <AdminHeader></AdminHeader>
  <div class="w-200 mt-5 flex flex-col items-center p-5">
    <div class="flex w-full justify-center">
      <button onclick={handleLeftBtn} class="w-1/20 ml-2 border-1 border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
        <Icon src={BsArrowLeft}></Icon>
      </button>
      <button onclick={handleRightBtn} class="w-1/20 ml-2 border-1 border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
        <Icon src={BsArrowRight}></Icon>
      </button>
      <button onclick={handleReloadBtn} class="w-1/20 ml-2 border-1 border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
        <Icon src={BsArrowClockwise}></Icon>
      </button>
      <button onclick={handleHomeBtn} class="w-1/20 ml-2 border-1 border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
        <Icon src={BsHouse}></Icon>
      </button>
      <input bind:value={searchPath} type="text" name="" id="" class="w-6/10 ml-2 p-1 border-1 border-gray-800 rounded-lg">
      <button onclick={handleSerachBtn}  class="w-1/10 ml-2 border-1 border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
        <Icon src={BsSearch}></Icon>
      </button>
      <button onclick={handleNewFolderBtn} class="w-1/20 ml-2 border-1 border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
        <Icon src={BsFolderPlus}></Icon>
      </button>
      <button onclick={handleUploadBtn} class="w-1/20 ml-2 border-1 border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
        <Icon src={BsFileEarmarkArrowUp}></Icon>
      </button>
      <input bind:this={uploadInput} onchange={handleUpload} type="file" multiple class="hidden">
    </div>
    <div class="w-full rounded-lgpt-1 pt-1 pb-1 mt-2 border-1 border-gray-800 rounded-sm">
      {#if selectedItems.length > 0}
      <div class="pl-2 w-full flex">
        <span>{selectedItems.length}</span>
        <button onclick={handleRemoveAllSelected} class="pl-2 text-blue-500 cursor-pointer">Remover selecionados</button>
        <button onclick={() => {selectedItems = []}} class="pl-2 text-red-500 cursor-pointer">Cancelar</button>
      </div>
      {/if}
      {#each items?.items as item}
      {#if item.isDir}
      <div class="w-full text-3xl p-2 {!selectedItems.includes(item.path) ? 'hover:bg-gray-200':'bg-gray-400'}">
        <div class="flex w-full">
          {#if selectedItems.includes(item.path)}
          <button onclick={() => {handleCheckBoxBtn(item.path)}} class="text-lg cursor-pointer">
            <Icon src={BsCheckSquare}></Icon>
          </button>
          {:else}
          <button onclick={() => {handleBoxBtn(item.path)}} class="text-lg cursor-pointer">
            <Icon src={BsSquare}></Icon>
          </button>
          {/if}
          <div class="flex w-full ml-2 text-lg justify-between">
            <button onclick={() => {goPath(item.path)}} class="flex text-3xl cursor-pointer hover:underline">
              <Icon src={BsFolder}></Icon>
              <span class="ml-2 text-lg">{item.name}</span>
            </button>
            <button onclick={() => {handleRemoveBtn(item.path)}} class="cursor-pointer">
              <Icon src={BsTrashFill}></Icon>
            </button>
          </div>
        </div>
      </div>
      {/if}
      {/each}

      {#each items?.items as item}
      {#if !item.isDir}
      <div class="w-full text-3xl p-2 {!selectedItems.includes(item.path) ? 'hover:bg-gray-200':'bg-gray-400'}">
        <div class="flex w-full">
          {#if selectedItems.includes(item.path)}
          <button onclick={() => {handleCheckBoxBtn(item.path)}} class="text-lg cursor-pointer">
            <Icon src={BsCheckSquare}></Icon>
          </button>
          {:else}
          <button onclick={() => {handleBoxBtn(item.path)}} class="text-lg cursor-pointer">
            <Icon src={BsSquare}></Icon>
          </button>
          {/if}
          <div class="flex w-full ml-2 text-lg justify-between">
            <button class="flex text-3xl cursor-pointer hover:underline">
              {#if fileIsImage(item.path)}
              <Icon src={BsFileEarmarkImage}></Icon>
              {:else}
              <Icon src={BsFileEarmark}></Icon>
              {/if}
              <span class="ml-2 text-lg">{item.name}</span>
            </button>
            <button onclick={() => {handleRemoveBtn(item.path)}} class="cursor-pointer">
              <Icon src={BsTrashFill}></Icon>
            </button>
          </div>
        </div>
      </div>
      {/if}
      {/each}
    </div>
  </div>
</div>