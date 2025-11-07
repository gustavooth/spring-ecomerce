<script lang="ts">
	import '../app.css';
	import favicon from '$lib/assets/icon.png';
	
	let { children } = $props();
</script>

<svelte:head>
	<link rel="icon" href={favicon} />
	<title>Rextech</title>
</svelte:head>

<div class="w-full min-h-screen bg-slate-50">
	{@render children?.()}
</div>

