<script lang="ts">
  let {title = $bindable(), custonClass = $bindable(), onclick = $bindable()} = $props();
</script>

<div class={custonClass}>
  <button onclick={onclick} class="cursor-pointer border border-gray-500 p-1 rounded-sm shadow-sm">{title}</button>
</div>