import { api_request } from "$lib/request";

//TODO: Remover isso daqui urgente!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
export const FS_TOKEN = "HjGRgi427aUez9oxsamgzJV5Vx5Sc3g8bKEW7Y9wFBANszvH6ulY1XZskegv5nVq";

export const FS_BASE = "http://127.0.0.1:8081"

export const FS_ENDPOINTS = {
  NEW_UPLOAD_TOKEN: "/admin/new_upload_token",
  UPLOAD_FILES: "/upload_file",
  FS: {
    LIST: "/fs/ls",
    NEW_FOLDER: "/fs/mkdir",
    REMOVE: "/fs/rm"
  }
};

export interface FileManagerRequest {
  path: string;
}

export interface NewTokenFile {
  name: string;
  path: string;
}

export interface PostNewToken {
  files: NewTokenFile[];
}

export interface NewTokenResponse {
  token: string;
}

export interface UploadFilesResponse {
  ok: boolean;
}

export interface ItemList {
  isDir: boolean,
  name: string,
  path: string,
}

export interface ListFilesResponse {
  items: ItemList[];
}

export interface NewFolderResponse {
  newPath: string;
}

export interface RemoveResponse {
  removedFiles:string[];
}

export interface FSResponse<T = any> {
  success: boolean;
  message: string;
  data: T;
}

export async function removeItem(data: FileManagerRequest): Promise<RemoveResponse| undefined> {
  const response = await api_request<RemoveResponse>(FS_ENDPOINTS.FS.REMOVE, {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      "Authorization": FS_TOKEN,
    }
  }, FS_BASE);

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}

export async function newFolder(data: FileManagerRequest): Promise<NewFolderResponse| undefined> {
  const response = await api_request<NewFolderResponse>(FS_ENDPOINTS.FS.NEW_FOLDER, {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      "Authorization": FS_TOKEN,
    }
  }, FS_BASE);

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}

export async function listFiles(data:FileManagerRequest): Promise<ListFilesResponse | undefined> {
  const response = await api_request<ListFilesResponse>(FS_ENDPOINTS.FS.LIST, {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      "Authorization": FS_TOKEN,
    }
  }, FS_BASE);

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}

export async function newUploadToken(data: PostNewToken): Promise<NewTokenResponse | undefined> {
  const response = await api_request<NewTokenResponse>(FS_ENDPOINTS.NEW_UPLOAD_TOKEN, {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      "Authorization": FS_TOKEN,
    }
  }, FS_BASE);

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}

export async function fs_upload(files: File[], token: String) {
  const formData = new FormData();

  for (const file of files) {
    formData.append("files", file);
  }

  let route = `${FS_BASE}${FS_ENDPOINTS.UPLOAD_FILES}/${token}`

  try{
    const response = await fetch(route, {method: "POST", body: formData, headers: {"Authorization": FS_TOKEN}});

    const data: FSResponse<UploadFilesResponse> = await response.json();
    
    if (!response.ok) {
      console.error('❌ HTTP Error:', {
        route,
        status: response.status,
        statusText: response.statusText,
        response: response
      });
    }else if (!data.success) {
      console.error('❌ API Error:', {
        route,
        message: data.message,
        data: data.data
      });

      return undefined;
    }
    return data;
  } catch (error) {
    console.error('❌ apiRequest: Erro na requisição:', {
      route,
      error: error instanceof Error ? error.message : 'Erro desconhecido'
    });
  }
}
