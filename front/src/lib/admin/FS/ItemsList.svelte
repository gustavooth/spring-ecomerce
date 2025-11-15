<script lang="ts">
  import AdminHeader from "$lib/components/admin_header.svelte";
  import { BsArrowLeft, BsArrowRight, BsHouse, BsSearch, BsArrowClockwise, BsFolder, BsFileEarmark, BsFileEarmarkImage, BsArrowUpCircleFill, BsArrowUpCircle, BsFolderPlus, BsArrowUp, BsFileEarmarkArrowUp, BsTrashFill, BsSquare, BsCheckSquare, BsPencilSquare, BsCloudArrowDown, BsArrowDown, BsArrowDownCircle, BsScissors, BsCopy, BsXCircleFill, BsXCircle, BsClipboard } from "svelte-icons-pack/bs";
  import { Icon } from "svelte-icons-pack";
  import { DOWNLOAD_FILE_ROUTE, requestCopyPath, requestListPath, requestMkDir, requestMovePath, requestNewUpToken, requestRemovePath, requestUploadItems, VIEW_IMAGE_ROUTE, type ItemList, type ListItemsResponse, type NewTokenFile } from "$lib/fileServer";
  import { onMount } from "svelte";
  import Item from "./item.svelte";
  import { getItemTypeByPath, ItemType } from "./utils";

  const StartPath:string = "/";

  let currentPath = $state(StartPath);

  let {searchPath = $bindable(StartPath), uploadInput = $bindable(), selectedItems = $bindable([]), viewImage = $bindable(true), filterType = $bindable(ItemType.OTHER), selectMultiple = $bindable(true)} = $props();

  let items: ListItemsResponse|undefined = $state(undefined);

  let history: string[] = ["/"];
  let historyIndex = 0;

  let pendingItems:ItemList[] = $state([]);
  let pendingAction:"scissor"|"copy"|"" = $state("");

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
    history.push(path);
    historyIndex++;
  }

  function historyToLeft():string|undefined {
    if (historyIndex == 0) {
      return undefined;
    }

    historyIndex--;
    return history[historyIndex];
  }

  function historyToRight():string|undefined {
    if (history.length == historyIndex+1) {
      return undefined;
    }

    historyIndex++;
    return history[historyIndex];
  }

  async function goPath(path:string, updateHistory:boolean = true) {
    let newItems = await requestListPath({paths: [path]});
    searchPath = path;
    currentPath = path;
    items = newItems;

    if (updateHistory) {
      historyAdd(path);
    }
  }

  async function reload_items() {
    goPath(currentPath, false);
    selectedItems = [];
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
    console.log(items);
  }

  function handleHomeBtn() {
    goPath("/");
  }

  function handleSerachBtn() {
    goPath(searchPath);
  }

  async function handleNewFolderBtn() {
    const folderName = prompt("Nome da nova pasta: ");
    if (folderName == undefined) {
      return;
    }

    const response = await requestMkDir({paths:[`${currentPath}/${folderName}`]});

    if (response != null) {
      reload_items();
    }
  }

  function handleUploadBtn() {
    uploadInput.click();
  }

  function onItemChange(item:ItemList, type:number) {
    reload_items();
  }

  function onItemClick(item:ItemList, type:number) {
    if (type == ItemType.FOLDER) {
      goPath(item.path);
    }else if(type == ItemType.IMAGE) {
      window.open(VIEW_IMAGE_ROUTE+item.path, "_blank");
    }
  }

  function isSelected(item:ItemList):boolean {
    for (let i = 0; i < selectedItems.length; i++) {
      if (selectedItems[i].path == item.path) {
        return true;
      }
    }

    return false;
  }

  function isPending(item:ItemList):boolean {
    for (let i = 0; i < pendingItems.length; i++) {
      if (pendingItems[i].path == item.path) {
        return true;
      }
    }

    return false;
  }

  function onSelectItem(item:ItemList, type:number) {
    console.log(item);

    if (pendingAction != "") {
      return;
    }

    if (isSelected(item)) {
      selectedItems = selectedItems.filter(_item => _item.path != item.path);
      return;
    }

    selectedItems.push(item);
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

    let tokenResp = await requestNewUpToken({files: files});

    if (tokenResp != undefined) {
      const filesArray: File[] = Array.from(selectedFiles);
      let uploadResp = await requestUploadItems(filesArray, tokenResp.token)
      if (uploadResp != undefined) {
        reload_items();
        alert("Upload concluido!");
      }
    }
  }

  function getPreviousPath(path: string): string {
    const lastSlashIndex = path.lastIndexOf('/');
    if (lastSlashIndex <= 0) {
      return '/';
    }
    return path.substring(0, lastSlashIndex);
  }

  function handleSelectBtnCancel() {
    selectedItems = [];
    pendingItems = [];
    pendingAction = "";
  }

  function handleSelectBtn(action:"scissor"|"copy") {
    if (selectedItems.length == 0) {
      return;
    }

    pendingItems = selectedItems;
    selectedItems = [];
    pendingAction = action;
  }

  function getPathsArray(items:ItemList[]) {
    let arr = [];

    for (let i = 0; i < items.length; i++) {
      arr.push(items[i].path);  
    }

    return arr;
  }

  async function handleSelectBtnDel() {
    let paths = getPathsArray(selectedItems);
    if (!window.confirm("Deletar os items: " + paths.join(", "))) {
      return
    }

    const response = await requestRemovePath({paths:paths});

    if (response != null) {
      reload_items();
    }
  }

  async function handleSelectBtnAction() {
    let paths = getPathsArray(pendingItems);
    if (!window.confirm("Itens pendente: " + paths.join(", "))) {
      return
    }

    let newPaths = []
    for (let i = 0; i < pendingItems.length; i++) {
      newPaths.push(pendingItems[i].path);
      newPaths.push(`${currentPath}/${pendingItems[i].name}`);
    }

    let response;

    if (pendingAction == "scissor") {
      response = await requestMovePath({paths:newPaths})
    }else if (pendingAction == "copy") {
      response = await requestCopyPath({paths:newPaths})
    }

    if (response != undefined) {
      reload_items();
    }

    handleSelectBtnCancel();
  }
</script>

<div class="w-full h-full flex flex-col items-center p-5">
  <div class="flex w-full justify-center">
    <button onclick={handleLeftBtn} class="w-1/20 ml-2 border border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
      <Icon src={BsArrowLeft}></Icon>
    </button>
    <button onclick={handleRightBtn} class="w-1/20 ml-2 border border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
      <Icon src={BsArrowRight}></Icon>
    </button>
    <button onclick={handleReloadBtn} class="w-1/20 ml-2 border border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
      <Icon src={BsArrowClockwise}></Icon>
    </button>
    <button onclick={handleHomeBtn} class="w-1/20 ml-2 border border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
      <Icon src={BsHouse}></Icon>
    </button>
    <input bind:value={searchPath} type="text" name="" id="" class="w-6/10 ml-2 p-1 border border-gray-800 rounded-lg">
    <button onclick={handleSerachBtn}  class="w-1/10 ml-2 border border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
      <Icon src={BsSearch}></Icon>
    </button>
    <button onclick={handleNewFolderBtn} class="w-1/20 ml-2 border border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
      <Icon src={BsFolderPlus}></Icon>
    </button>
    <button onclick={handleUploadBtn} class="w-1/20 ml-2 border border-gray-800 cursor-pointer rounded-lg flex items-center justify-center">
      <Icon src={BsFileEarmarkArrowUp}></Icon>
    </button>
    <input bind:this={uploadInput} onchange={handleUpload} type="file" multiple class="hidden">
  </div>
  <div class="w-full h-full overflow-auto rounded-lgpt-1 pt-1 pb-1 mt-2 border border-gray-800 rounded-sm">
    <div class="w-full p-1 {selectedItems.length > 0 && pendingAction == "" ? 'bg-gray-300':''} {pendingAction != "" ? 'bg-blue-500 text-white':''} flex justify-between items-center text-xs">
      <div>
        {#if pendingAction == ""}
        <span>{selectedItems.length}</span>
        {:else}
        <span>{pendingItems.length}</span>
        {/if}
      </div>
      <div class="flex">
        <button onclick={handleSelectBtnCancel} class="flex items-center justify-center p-1 border rounded-sm cursor-pointer mr-2">
          <Icon src={BsXCircle}></Icon>
          <span class="ml-1">Cancelar</span>
        </button>
        {#if pendingItems.length == 0}
        <button onclick={() => {handleSelectBtn("scissor")}} class="flex items-center justify-center p-1 border rounded-sm cursor-pointer mr-2">
          <Icon src={BsScissors}></Icon>
          <span>Cortar</span>
        </button>
        <button onclick={() => {handleSelectBtn("copy")}}  class="flex items-center justify-center p-1 border rounded-sm cursor-pointer mr-2">
          <Icon src={BsCopy}></Icon>
          <span>Copiar</span>
        </button>
        <button onclick={() => {handleSelectBtnDel()}}  class="flex items-center justify-center p-1 border rounded-sm cursor-pointer mr-2">
          <Icon src={BsTrashFill}></Icon>
          <span>Excluir</span>
        </button>
        {:else}
        <button onclick={() => {handleSelectBtnAction()}}  class="flex items-center justify-center p-1 border rounded-sm cursor-pointer mr-2">
          <Icon src={BsClipboard}></Icon>
          <span>Colar</span>
        </button>
        {/if}
      </div>
    </div>

    {#if currentPath != "/"}
    <Item item={{path:getPreviousPath(currentPath), name:".."}} actions={false} type={ItemType.FOLDER} onChange={onItemChange} onClick={onItemClick}></Item>
    {/if}

    {#each items?.items as item}
    {#if item.isDir}
    <Item item={item} type={ItemType.FOLDER} onChange={onItemChange} onClick={onItemClick} selected={isSelected(item)} pendingAction={isPending(item)} onSelect={onSelectItem}></Item>
    {/if}
    {/each}

    {#each items?.items as item}
      {#if !item.isDir}
        {#if filterType != ItemType.OTHER}
          {#if getItemTypeByPath(item.path) == filterType}
            <Item item={item} type={filterType} onChange={onItemChange} onClick={onItemClick} imageView={viewImage} selected={false} pendingAction={isPending(item)}  onSelect={onSelectItem}></Item>
          {/if}
        {:else}
          <Item item={item} type={getItemTypeByPath(item.path)} onChange={onItemChange} onClick={onItemClick} imageView={viewImage} selected={isSelected(item)} pendingAction={isPending(item)}  onSelect={onSelectItem}></Item>
        {/if}
      {/if}
    {/each}
  </div>
</div>