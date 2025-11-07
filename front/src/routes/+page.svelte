<script lang="ts">
	import Header from '$lib/components/header.svelte';
  import Banners from "$lib/components/banners.svelte";
  import Grid from "$lib/components/grid.svelte";
</script>

<Header></Header>
<main class="flex items-center flex-col w-full h-full">
  <Banners></Banners>
  <Grid></Grid>
</main>