import { authAdmin } from '$lib/server/adminSession.svelte.js';
import { error } from '@sveltejs/kit';
import type { PageServerLoad } from './$types.js';

export const load: PageServerLoad = async ({params, cookies, request}) => {
  if(!await authAdmin(cookies, request)) {
    error(404, "Not Found");
  }
}