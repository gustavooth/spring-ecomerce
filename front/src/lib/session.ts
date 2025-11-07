import { get, writable, type Writable } from "svelte/store";
import { type SessionData, api_request, session_state, setAuthToken } from "./request";
import { browser } from "$app/environment";

export let ss_state:Writable<SessionData | undefined> = writable(undefined)
let initizlized = false;

async function load_state():Promise<SessionData | undefined> {
  return await session_state();
}

export async function reload_state() {
  initizlized = true;
  const data = await load_state();
  if (data != undefined) {
    ss_state.set(data);
    saveToken(data.token);
  }
}

export async function update_state(data: SessionData) {
  ss_state.set(data);
}

export async function saveToken(token: String) {
  localStorage.setItem("Authorization", token.toString());
}

export function loadToken(): String {
  const token = localStorage.getItem("Authorization");
  if (token != undefined) {
    return token;
  }
  return "";
}