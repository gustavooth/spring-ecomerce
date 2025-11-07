<script lang="ts">
  import MinimalHeader from "$lib/components/minimal_header.svelte";
  import { auth_email, auth_login, auth_register, session_state } from "$lib/request";
  import { reload_state, update_state } from "$lib/session";
  import { Icon } from "svelte-icons-pack";
  import { LuLoaderCircle } from "svelte-icons-pack/lu";

  let {email = $bindable(), password = $bindable(), repassword = $bindable(), username = $bindable()} = $props()

  let register_email = "";
  let invalid_password = $state(false);

  let step = $state("email")
  let loginToken: String;

  let buttonDisable = $state(false)

  async function btnNext() {
    buttonDisable = true;

    if (step == "email") {
      let resp = await auth_email({email: email});
      if (resp != undefined) {
        loginToken = resp.loginToken;

        await new Promise((data) => {
          setTimeout(data, 1000)
        })


        if (resp.nextStep == "login") {
          step = "login";
        }else {
          register_email = email;
          step = "register"
        }
      }else {
        await new Promise((data) => {
          setTimeout(data, 2000)
        })
      }
    }else if (step == "login") {
      invalid_password = false;
      let resp = await auth_login({token: loginToken, password: password})
      await new Promise((data) => {
        setTimeout(data, 1000)
      })
      if (resp != undefined) {
        if (resp.sessionState != undefined) {
          update_state(resp.sessionState);
          window.location.href = "/"
        }else {
          if (resp.message == "invalid password") {
            invalid_password = true;
          }
        }
      }
    }else {
      await new Promise((data) => {
        setTimeout(data, 2000)
      })

      if (password != repassword) {
        alert("As senhas devem ser iguais!");
        return;
      }

      let resp = await auth_register({token: loginToken, name: username, password: password});
      await new Promise((data) => {
        setTimeout(data, 1000)
      })
      
      if (resp != undefined) {
        if (resp.sessionState != undefined) {
          update_state(resp.sessionState);
          window.location.href = "/";
        }
      }
    }

    buttonDisable = false;
  }
</script>


<div class="w-full min-h-screen">
  <MinimalHeader></MinimalHeader>
  <main class="mt-4 w-full flex justify-center">
    <div class="w-1/3 bg-gray-100 rounded-sm border-1 border-gray-500 p-4">
      <div>
        {#if step == "email"}
        <h1 class="text-lg">Entre ou crie um conta</h1>
        {:else if step == "login"}
        <h1 class="text-lg">Entre</h1>
        {:else}
        <h1 class="text-lg">Crie um conta</h1>
        {/if}
      </div>
      {#if step != "register"}
      <form class="mt-4 flex flex-col">
        {#if step == "login"}
        <span>Email: {email}<button onclick={() => {step = "email"}} class="ml-1 cursor-pointer text-blue-400">Alterar</button></span>
        {#if invalid_password}
        <label class="mt-2" for="password">Senha:</label>
        <input bind:value={password} type="password" name="" id="password" class="w-2/3 bg-red-100 p-1 border-1 border-red-500 rounded-sm outline-0 outline-gray-500 focus:outline-2 hover:bg-gray-100 focus:bg-gray-100">
        {:else}
        <label class="mt-2" for="password">Senha:</label>
        <input bind:value={password} type="password" name="" id="password" class="w-2/3 bg-gray-200 p-1 border-1 border-gray-500 rounded-sm outline-0 outline-gray-500 focus:outline-2 hover:bg-gray-100 focus:bg-gray-100">
        {/if}
        {:else}
        <label for="email">Email:</label>
        <input bind:value={email} type="text" name="" id="email" class="w-2/3 bg-gray-200 p-1 border-1 border-gray-500 rounded-sm outline-0 outline-gray-500 focus:outline-2 hover:bg-gray-100 focus:bg-gray-100">
        {/if}
        
      </form>
      {:else}
      <form class="mt-4 flex flex-col">
        <span>Email: {register_email}</span>
        <label class="mt-2" for="username">Seu nome</label>
        <input bind:value={username} type="text" name="" id="username" class="w-2/3 bg-gray-200 p-1 border-1 border-gray-500 rounded-sm outline-0 outline-gray-500 focus:outline-2 hover:bg-gray-100 focus:bg-gray-100">
        <label class="mt-2" for="password">Senha:</label>
        <input bind:value={password} type="password" name="" id="password" class="w-2/3 bg-gray-200 p-1 border-1 border-gray-500 rounded-sm outline-0 outline-gray-500 focus:outline-2 hover:bg-gray-100 focus:bg-gray-100">
        <label class="mt-2" for="repassword">Confirme a senha:</label>
        <input bind:value={repassword} type="password" name="" id="repassword" class="w-2/3 bg-gray-200 p-1 border-1 border-gray-500 rounded-sm outline-0 outline-gray-500 focus:outline-2 hover:bg-gray-100 focus:bg-gray-100">
      </form>
      {/if}
      {#if buttonDisable}
      <button disabled class="p-1 mt-2 flex items-center bg-sky-500 rounded-sm border-1 select-none border-gray-900 text-gray-900">
        <Icon src={LuLoaderCircle} className="animate-spin mr-1"></Icon>
        Continuar
      </button>
      {:else}
      <button onclick={() => {btnNext()}} class="p-1 mt-2 flex items-center bg-sky-400 rounded-sm cursor-pointer border-1 select-none border-gray-900 text-gray-900 hover:bg-sky-300">Continuar</button>
      {/if}
    </div>
  </main>
</div>