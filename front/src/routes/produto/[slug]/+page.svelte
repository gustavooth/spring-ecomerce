<script lang="ts">
	import Header from '$lib/components/header.svelte';
  import { page } from "$app/state";
  import Imagem1 from "$lib/assets/produtos/1/Preto.png"
  import Imagem2 from "$lib/assets/produtos/1/Branco.png"
  import { Icon } from "svelte-icons-pack";
  import { BsStar, BsStarFill, BsStarHalf, BsCreditCardFill, BsShieldCheck, BsTruck, BsArrowLeftRight, BsGeoAltFill, BsCaretDownFill, BsCaretDown, BsCartPlusFill } from "svelte-icons-pack/bs";

  let stackMenuOpened = $state(false)
  let maxStack = 15;
  let stacks = [];
  let stackCount = $state(1)

  for (let i = 1; i < maxStack + 1; i++) {
    stacks.push(i)
  }
</script>

<Header></Header>

<div class="w-full h-fit flex items-center justify-center text-gary-950">
  <div class="w-9/10 flex justify-center mt-6">
    <div class="w-4/10 mr-4">
      <div class="w-full h-full">
        <img src="{Imagem1}" alt="" class="w-full h-full rounded-2xl">
      </div>
    </div>
    <div class="w-6/10 flex">
      <div class="w-6/10 mr-4 p-2">
        <div>
          <p>Relógio esportivo militar eletrônico masculino, luminoso, multifunções, LED, digital, relógio de pulso para meninos, estudantes, adolescentes, crianças</p>
        </div>
        <div class="w-fit mt-1 p-2 flex items-center text-lg text-yellow-500 hover:outline-1 outline-sky-950 cursor-pointer">
          <span class="mr-1 text-gray-950">4,8</span>
          <Icon src={BsStarFill}></Icon>
          <Icon src={BsStarFill}></Icon>
          <Icon src={BsStarFill}></Icon>
          <Icon src={BsStarFill}></Icon>
          <Icon src={BsStarHalf}></Icon>
          <span class="ml-2 text-gray-900 text-xs">7430 avaliações</span>
        </div>
        <div class="w-full h-1 bg-gray-300 mt-5 mb-5 rounded-2xl"></div>
        <div class="flex items-end">
          <div class="flex w-fit justify-between text-sky-950 font-bold text-3xl m-1">
            <span class="text-xs mt-1 mr-0.5">R$</span>
            <span>149</span>
            <span class="text-xs mt-1 ml-0.5">00</span>
          </div>
          <div class="flex w-fit justify-between text-sky-950 font-semibold line-through text-base m-1">
            <span class="text-xs mt-1 mr-0.5">R$</span>
            <span>299,00</span>
          </div>
        </div>
        <div class="w-fit">
          <div class="flex items-end text-sky-950 w-full text-xl p-2 hover:outline-1 outline-sky-950 cursor-pointer">
            <Icon src={BsCreditCardFill} className="text-3xl"></Icon>
            <span class="text-gray-800 font-semibold ml-2">7x R$ 21,42</span>
            <span class="text-sm ml-2 text-blue-600">Ver Parcelas</span>
          </div>
        </div>
        <div class="mt-4">
          <span>Cor:</span>
          <div class="flex w-full mt-2 p-2">
            <div class="w-15 h-15 hover:outline-1 outline-sky-950 rounded-lg">
              <img src="{Imagem1}" alt="" class="border-1 border-sky-950 rounded-lg cursor-pointer">
            </div>
            <div class="w-15 h-15 ml-2 hover:outline-1 outline-sky-950 rounded-lg">
              <img src="{Imagem2}" alt="" class=" border-sky-950 rounded-lg cursor-pointer">
            </div>
          </div>
        </div>
      </div>
      <div class="w-4/10 h-full border-1 border-gray-200 rounded-sm p-4 flex flex-col items-center">
        <div class="w-full">
          <div class="flex w-fit justify-between text-sky-950 font-bold text-3xl m-1">
            <span class="text-xs mt-1 mr-0.5">R$</span>
            <span>149</span>
            <span class="text-xs mt-1 ml-0.5">00</span>
          </div>
        </div>
        <div class="flex items-end text-sky-950 w-full text-lg p-2 hover:outline-1 outline-sky-950 cursor-pointer">
          <Icon src={BsCreditCardFill} className="text-2xl"></Icon>
          <span class="text-gray-800 font-semibold ml-2">7x R$ 21,42</span>
          <span class="text-sm ml-2 text-blue-600">Ver Parcelas</span>
        </div>
        <div class="w-full mt-3">
          <p class="text-sm"><span class="text-emerald-800 font-semibold">Entrega GRATIS:</span> segunda-feira, dia <span class="font-bold">19</span>. Se pedir dentro de <span class="text-red-500 font-semibold">2 horas</span>.</p>
        </div>
        <div class="flex mt-3 items-center cursor-pointer hover:outline-1 outline-sky-950">
          <div>
            <Icon src={BsGeoAltFill} color="oklch(29.3% 0.066 243.157)" className="text-xl mr-2"></Icon>
          </div>
          <div>
            <span class="text-sm">Entregando em <span class="font-semibold">Sete Lagoas 35000-000</span>. <span class="text-blue-600">Atualizar local</span></span>
          </div>
        </div>
        <div class="mt-3 w-full">
          <span class="text-emerald-600 text-2xl">Em Estoque</span>
        </div>
        <div class="w-full mt-3">
          <button onclick={() => {stackMenuOpened = true}} class="w-8/10 h-8 bg-gray-200 border-1 border-gray-600 rounded-sm flex items-center justify-between select-none cursor-pointer p-1 hover:outline-1 outline-sky-950">
            <span>Quantidade: {stackCount}</span>
            <div>
              <Icon src={BsCaretDown}></Icon>
            </div>
          </button>
          {#if stackMenuOpened}
            <div class="relative w-full">
              <div class="absolute pt-1 pb-1 bg-gray-200 w-8/10 h-25 -top-8 rounded-sm border-1 border-gray-950 overflow-auto">
                {#each stacks as stack}
                <button onclick={() => {stackCount = stack; stackMenuOpened = false}} class="w-full border-1 border-gray-950 p-0.5 flex items-center cursor-pointer hover:bg-gray-300">{stack}</button>
                {/each}
              </div>
            </div>
          {/if}
        </div>
        <div class="w-8/10 h-8 bg-sky-700 hover:bg-sky-600 text-white rounded-sm flex items-center justify-center cursor-pointer mt-4">
          <span class="text-lg">Comprar Agora</span>
        </div>
        <div class="w-8/10 h-8 bg-sky-700 hover:bg-sky-600 text-white rounded-sm flex items-center justify-center cursor-pointer mt-2">
          <span class="text-normal">Adicionar ao Carrinho</span>
        </div>
        <div class="w-4/5 mt-3 flex justify-between">
          <div class="flex flex-col justify-center items-center text-xl text-emerald-500 cursor-pointer select-none">
            <div class="p-2 border-1 border-gray-200 rounded-4xl">
              <Icon src={BsShieldCheck}></Icon>
            </div>
            <span class="text-xs text-gray-950">Compra</span>
            <span class="text-xs text-gray-950">Segura</span>
          </div>
          <div class="flex flex-col justify-center items-center text-xl text-emerald-500 cursor-pointer select-none">
            <div class="p-2 border-1 border-gray-200 rounded-4xl">
              <Icon src={BsArrowLeftRight}></Icon>
            </div>
            <span class="text-xs text-gray-950">Politica de</span>
            <span class="text-xs text-gray-950">Devolucao</span>
          </div>
          <div class="flex flex-col justify-center items-center text-xl text-emerald-500 cursor-pointer select-none">
            <div class="p-2 border-1 border-gray-200 rounded-4xl">
              <Icon src={BsTruck}></Icon>
            </div>
            <span class="text-xs text-gray-950">Entrega</span>
            <span class="text-xs text-gray-950">Rapida</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>