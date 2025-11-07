<script lang="ts">
  import AdminHeader from "$lib/components/admin_header.svelte";
</script>

<AdminHeader></AdminHeader>
<div class="w-full mt-10 flex items-center justify-center">
  <form class="flex flex-col w-1/3 bg-gray-100 rounded-sm border-1 border-gray-500 p-4"> 
    <label for="title">Titulo</label>
    <input type="text" id="title">
    <label for="slug">Slug</label>
    <input type="text" id="slug">
    <label for="description">Descricao</label>
    <input type="text" id="description">
    <label for="short-description">Descricao curta</label>
    <input type="text" id="short-description">
    <label for="image-path">image-path</label>
    <input type="text" id="image-path">
  </form>
</div>