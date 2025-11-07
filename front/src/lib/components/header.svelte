<script lang="ts">
  import logo from "$lib/assets/rextech-with-logo.png"
  import { auth_loggout, setAuthToken, type SessionData } from "$lib/request";
  import { loadToken, reload_state, ss_state } from "$lib/session";
  import { onMount } from "svelte";
  import { Icon } from "svelte-icons-pack";
  import { BsSearch, BsPerson, BsPersonFill, BsCart3, BsCart2, BsCaretDownFill, BsCaretDown, BsTools } from "svelte-icons-pack/bs";
  import { get } from "svelte/store";

  onMount(() => {
    setAuthToken(loadToken());
    reload_state();
  })

  let {userMenuOpened = $bindable(false)} = $props()
  let login = $state(false)

  let session_data: SessionData | undefined = $state(get(ss_state));

  ss_state.subscribe((new_data) => {
    session_data = new_data;
  })

  async function handleLogout() {
    let resp = await auth_loggout()

    if (resp != undefined) {
      reload_state()
    }
  }
</script>

<header>
  <div class="w-full h-18 bg-sky-800 flex items-center justify-between p-2 select-none">
    <div class="w-80 hover:outline-2 outline-sky-950 p-1">
      <a href="/" class="w-full h-full">
        <img src="{logo}" alt="Logo">
      </a>
    </div>
    <div class="w-100 flex">
      <input type="text" class="basis-10/12 h-9 bg-sky-50 text-gray-950 rounded-l-lg p-1 focus:outline-2 focus:outline-sky-950">
      <button class="basis-2/12 h-9 bg-sky-500 cursor-pointer rounded-r-lg flex items-center justify-center text-lg hover:bg-sky-300">
        <Icon src={BsSearch}></Icon>
      </button>
    </div>
    <div class="flex h-11 items-center">
      <button onmouseenter={() => {userMenuOpened = true}} onmouseleave={() => {userMenuOpened = false}} class="flex justify-center p-1 cursor-pointer hover:outline-1 outline-sky-950">
        <div class="text-4xl">
          <Icon color="oklch(97.7% 0.013 236.62)" src={BsPersonFill}></Icon>
        </div>
        <div class="ml-1 flex flex-col text-base/4">
          {#if session_data?.logged}
          <span class="text-gray-200 text-sm">Ola {session_data.user?.display_name}</span>
          <span class="text-gray-200 font-bold">Contas e lista</span>
          {:else}
          <span class="text-gray-200 text-sm">Entre ou Registre-se</span>
          <span class="text-gray-200 font-bold text-sm">Contas e lista</span>
          {/if}
        </div>
        <div class="ml-1 text-xl flex items-end">
          {#if userMenuOpened}
          <Icon color="oklch(97.7% 0.013 236.62)" src={BsCaretDownFill}></Icon>
          {:else}
          <Icon color="oklch(97.7% 0.013 236.62)" src={BsCaretDown}></Icon>
          {/if}
        </div>
        {#if userMenuOpened}
        <div class="w-50 bg-gray-100 absolute top-14 p-2 flex flex-col items-center rounded-sm cursor-auto">
          {#if !session_data?.logged}
          <a href="/auth" class="w-8/10 h-8 bg-sky-700 hover:bg-sky-600 text-white rounded-sm flex items-center justify-center cursor-pointer">
            <span class="text-sm">Entre ou Registre-se</span>
          </a>
          <div class="w-full h-1 bg-gray-500 mt-4 mb-4"></div>
          {:else}
          <a href="/" onclick={() => {handleLogout()}} class="text-blue-600 cursor-pointer">Sair</a>
          {#if session_data?.user?.role == "admin"}
          <div class="flex p-1 w-full h-fit">
            <a href="/admin" class="flex items-center text-blue-500">
              <Icon src={BsTools}></Icon>
              <span class="pl-1">Admin</span>  
            </a>
          </div>
          {/if}
          {/if}
        </div>
        {/if}
      </button>
      <button class="flex h-full items-center justify-center p-1 cursor-pointer hover:outline-1 outline-sky-950">
        <div class="text-4xl">
          <Icon color="oklch(97.7% 0.013 236.62)" src={BsCart2}></Icon>
        </div>
        <div class="ml-1 p-1 h-full flex flex-col leading-4">
          <span class="text-gray-200 w-fit bg-sky-900 text-semibold rounded-lg pl-0.5 pr-0.5">0</span>
          <span class="text-gray-200 font-semibold">Carrinho</span>
        </div>
      </button>
    </div>
  </div>
</header>