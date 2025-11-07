<script lang="ts">
  import logo from "$lib/assets/rextech-with-logo-white.png"
    import { setAuthToken } from "$lib/request";
    import { loadToken, reload_state } from "$lib/session";
    import { onMount } from "svelte";

  onMount(() => {
    setAuthToken(loadToken());
    reload_state();
  })
</script>

<header class="w-full flex justify-center select-none">
  <a href="/" class="h-20 cursor-pointer mt-4">
    <img src="{logo}" class="h-full">
  </a>
</header>