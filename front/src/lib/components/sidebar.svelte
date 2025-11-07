<script lang="ts">
  import { Icon } from "svelte-icons-pack";
  import { BsPersonFill, BsBoxArrowUpRight, BsBoxArrowRight } from "svelte-icons-pack/bs";
  import { reload_state, ss_state } from "$lib/session";
  import { get } from "svelte/store";
  import { auth_loggout } from "$lib/request";

  export let onClickLoginBtn = () => {}
  export let onClickRegisterBtn = () => {}

  let session_state = get(ss_state);

  ss_state.subscribe((valor) => {
    session_state = valor;
  } )

  function handleLogoutBtn() {
    auth_loggout()
    reload_state()
  }

</script>

<div class="basis-2/12 bg-slate-700 p-4 text-gray-50 rounded-md">
  <a class="text-2xl font-bold w-fit hover:underline hover:cursor-pointer" href="/">Etern.site</a>
  <div class="p-2 w-full mt-4 flex flex-row items-center justify-between bg-slate-500 rounded-sm">
    <div class="flex flex-row items-center">
      <Icon src={BsPersonFill} color="oklch(72.3% 0.219 149.579)" className="text-lg"/>
      {#if session_state?.logged}
      <span class="pl-1 text-base">{session_state?.user?.display_name}</span>
      {:else}
      <span class="pl-1 text-base">guest5938475</span>
      {/if}
    </div>
    {#if session_state?.logged}
    <button onclick={() => {handleLogoutBtn()}} class="hover:cursor-pointer">
      <Icon src={BsBoxArrowRight} color="oklch(98.5% 0.002 247.839)" className="text-lg"/>
    </button>
    {/if}
  </div>
  {#if !session_state?.logged}
  <div class="w-full flex h-9 mt-4 font-semibold text-gray-50">
    <button onclick={() => onClickLoginBtn()} 
      class="basis-4/12 mr-2 text-sm rounded-sm bg-cyan-600 hover:bg-cyan-700 hover:cursor-pointer flex flex-row justify-center items-center select-none">
      <p>Entre</p>
    </button>
    <button onclick={() => onClickRegisterBtn()} 
      class="basis-8/12 text-sm rounded-sm bg-emerald-600 hover:bg-emerald-700 hover:cursor-pointer flex flex-row justify-center items-center select-none">
      <p>Registre-se</p>
    </button>
  </div>
  {/if}
  <div class="h-1 w-full bg-gray-400 mt-4"></div>
  <a class="flex flex-row items-center mt-4 font-sans hover:cursor-pointer hover:text-blue-400" href="/api"
    target="_blank">
    <p class="font-bold mr-2 ml-1 text-base">API Docs</p>
    <Icon src={BsBoxArrowUpRight} className="text-xs+"/>
  </a>
</div>
