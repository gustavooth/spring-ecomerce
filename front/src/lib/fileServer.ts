import { api_request } from "$lib/request";

//TODO: Remover isso daqui urgente!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
export const FS_TOKEN = "HjGRgi427aUez9oxsamgzJV5Vx5Sc3g8bKEW7Y9wFBANszvH6ulY1XZskegv5nVq";

export const FS_BASE_ROUTE = "http://127.0.0.1:8081"
export const VIEW_IMAGE_ROUTE:string = "http://127.0.0.1:8081/public";
export const DOWNLOAD_FILE_ROUTE:string = "http://127.0.0.1:8081/public/download";

export const FS_ENDPOINTS = {
  NEW_UPLOAD_TOKEN: "/admin/new_upload_token",
  UPLOAD_FILES: "/upload_file",
  FS: {
    LIST: "/fs/ls",
    NEW_FOLDER: "/fs/mkdir",
    REMOVE: "/fs/rm",
    MOVE: "/fs/mv",
    COPY: "/fs/cp"
  }
};

export const ItemType = {
  OTHER: 0,
  FOLDER: 1,
  IMAGE: 2,
  VIDEO: 3,
  MUSIC: 4,
  COMPRESS: 5,
  DOCUMENT: 6,
  TEXT: 7,
  CODE: 8,
  BINARY: 9,
}

export interface FileManagerRequest {
  paths: string[];
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

export interface ListItemsResponse {
  items: ItemList[];
}

export interface PathResponse {
  path: string;
}

export interface ListPathsResponse {
  paths:string[];
}

export interface FSResponse<T = any> {
  success: boolean;
  message: string;
  data: T;
}

export async function FSRequest<T, E>(endpoint:string, data:E): Promise<T| undefined> {
  const response = await api_request<T>(endpoint, {
    method: 'POST',
    body: JSON.stringify(data),
    headers: {
      "Authorization": FS_TOKEN,
    }
  }, FS_BASE_ROUTE);

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}

export async function requestRemovePath(data: FileManagerRequest): Promise<ListPathsResponse| undefined> {
  return FSRequest(FS_ENDPOINTS.FS.REMOVE, data);
}

export async function requestMkDir(data: FileManagerRequest): Promise<PathResponse| undefined> {
  return FSRequest(FS_ENDPOINTS.FS.NEW_FOLDER, data);
}

export async function requestMovePath(data:FileManagerRequest): Promise<ListPathsResponse | undefined> {
  return FSRequest(FS_ENDPOINTS.FS.MOVE, data);
}

export async function requestCopyPath(data:FileManagerRequest): Promise<ListPathsResponse | undefined> {
  return FSRequest(FS_ENDPOINTS.FS.COPY, data);
}

export async function requestListPath(data:FileManagerRequest): Promise<ListItemsResponse | undefined> {
  return FSRequest(FS_ENDPOINTS.FS.LIST, data);
}

export async function requestNewUpToken(data: PostNewToken): Promise<NewTokenResponse | undefined> {
  return FSRequest(FS_ENDPOINTS.NEW_UPLOAD_TOKEN, data);
}

export async function requestUploadItems(files: File[], token: String) {
  const formData = new FormData();

  for (const file of files) {
    formData.append("files", file);
  }

  let route = `${FS_BASE_ROUTE}${FS_ENDPOINTS.UPLOAD_FILES}/${token}`

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
