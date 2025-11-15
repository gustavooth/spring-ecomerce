<script lang="ts">
  import logo from "$lib/assets/rextech.png"
  import { setAuthToken } from "$lib/request";
  import { loadToken, reload_state } from "$lib/session";
  import { onMount } from "svelte";

    let {link = $bindable("/admin")} = $props();

  onMount(() => {
    setAuthToken(loadToken());
    reload_state();
  })
</script>

<header class="w-full flex justify-center select-none">
  <a href="{link}" class="h-20 cursor-pointer mt-4" aria-label="index">
    <img src="{logo}" class="h-full" alt="Logo">
  </a>
</header>