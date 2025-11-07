import { error, redirect, type Cookies } from "@sveltejs/kit";
import { checkAdmin } from "./request";

export const ADMIN_COOKIE_NAME = "admin_token";

export async function authAdmin(cookies: Cookies, request: Request, ): Promise<Boolean> {
  const user_agent = request.headers.get("User-Agent");
  const token = cookies.get(ADMIN_COOKIE_NAME);

  if (user_agent != undefined && token != undefined) {
    const resp = await checkAdmin({token: token, agent: user_agent})

    if (resp != undefined) {
      if (resp.isAdmin) {
        return true;
      }
    }
  }

  return false;
}