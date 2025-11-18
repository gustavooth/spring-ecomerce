<script lang="ts">
  let {children, custonClass = $bindable("")} = $props()
</script>

<div class={`flex flex-col border border-gray-500 p-2 rounded-sm shadow-lg ${custonClass}`}>
  {@render children?.()}
</div>