<script lang="ts">
    import AdminHeader from "$lib/components/admin_header.svelte";
    import ItemsList from "$lib/admin/FS/ItemsList.svelte";
</script>

<AdminHeader></AdminHeader>
<div class="w-full flex items-center justify-center">
  <div class="w-3/5">
    <ItemsList></ItemsList>
  </div>
</div>