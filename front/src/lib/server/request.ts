import { api_request } from "$lib/request";

export const API_ENDPOINTS = {
  CHECK_ADMIN: "/admin/check_admin",
};

export interface PostCheckAdmin {
  token: String
  agent: String
}

export interface CheckAdminResult {
  isAdmin: Boolean
}

export async function checkAdmin(data: PostCheckAdmin): Promise<CheckAdminResult | undefined> {
  const response = await api_request<CheckAdminResult>(API_ENDPOINTS.CHECK_ADMIN, {
    method: 'POST',
    body: JSON.stringify(data)
  });

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}