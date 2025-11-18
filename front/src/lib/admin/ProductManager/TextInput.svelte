<script lang="ts">
  let {title = $bindable(), value = $bindable(), onchage = $bindable(), custonClass = $bindable()} = $props()
</script>

<div class={custonClass}>
  <span>{title}:</span>
  <input onchange={onchage} bind:value={value} type="text" name="title" class="border border-gray-500 w-full p-1 rounded-sm shadow-md">
</div>