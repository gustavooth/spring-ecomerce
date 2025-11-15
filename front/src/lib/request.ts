export const API_BASE = "http://127.0.0.1:8080"

export let authToken = "";
export function setAuthToken(token: String) {
  authToken = token.toString();
}

export const API_ENDPOINTS = {
  AUTH: {
    EMAIL: "/auth/email",
    LOGIN: "/auth/login",
    REGISTER: "/auth/register",
    LOGGOUT: "/auth/logout",
  },
  SESSION_STATE: "/session_state",
  INDEX: "/index",
  PRODUCT: "/p/{slug}"
};

export interface ApiResponse<T = any> {
  success: boolean;
  message: string;
  data: T;
}

export interface UserData {
  display_name: string
  role: String
}

export interface SessionData {
  token: String
  logged: boolean
  user: UserData | undefined
}

export interface AuthResponse {
  message: String
  sessionState: SessionData|undefined
}

export interface EmailResponse {
  loginToken: String
  nextStep: String
}

export interface PostAuthEmail {
  email: String
}

export interface PostAuthLogin {
  token: String
  password: String
}

export interface PostAuthRegister {
  token: String
  name: String,
  password: String
}

export interface ListResponse<T> {
  data:T[];
}

export interface ProductValueResponse {
  value:string;
}

export interface ProductResponse {
  price:number;
  imagePath:string;
  values:ProductValueResponse[];
}

export interface AttributeValueResponse{
  value:string;
}

export interface AttributeResponse {
  name:string;
  values:AttributeValueResponse[];
}

export interface PageImageResponse {
  path:string;
  index:number;
}

export interface ProductPageResponse {
  title:string;
  slug:string;
  shortDescription:string;
  description:string;
  products:ProductResponse[];
  attributes:AttributeResponse[];
  images:PageImageResponse[];
}

export async function api_request<T>(
  endpoint: string,
  options: RequestInit = {},
  custonRoute: String = API_BASE,
): Promise<ApiResponse<T> | undefined>{
  const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'Origin': 'http://127.0.0.1:5173/',
      'credentials': 'include',
      ...options.headers as Record<string, string>
    };

  try {
    const response = await fetch(`${custonRoute}${endpoint}`, {
      ...options,
      headers
    });

    const data: ApiResponse<T> = await response.json();

    if (!response.ok) {
      console.error('❌ HTTP Error:', {
        endpoint,
        status: response.status,
        statusText: response.statusText,
        response: response
      });
    }else if (!data.success) {
      console.error('❌ API Error:', {
        endpoint,
        message: data.message,
        data: data.data
      });

      return undefined;
    }
    return data;
  } catch (error) {
    console.error('❌ apiRequest: Erro na requisição:', {
      endpoint,
      error: error instanceof Error ? error.message : 'Erro desconhecido'
    });
  }
  

  return undefined;
}

export async function session_state(): Promise<SessionData|undefined> {
  const response = await api_request<SessionData>(API_ENDPOINTS.SESSION_STATE, {
    method: 'GET',
    headers: {"Authorization": authToken.toString()},
  });

  if (response != undefined) {
    return response.data;
  }
}

export async function auth_email(data: PostAuthEmail): Promise<EmailResponse | undefined> {
  const response = await api_request<EmailResponse>(API_ENDPOINTS.AUTH.EMAIL, {
    method: 'POST',
    headers: {"Authorization": authToken},
    body: JSON.stringify(data)
  });

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}

export async function auth_login(data: PostAuthLogin): Promise<AuthResponse | undefined> {
  const response = await api_request<AuthResponse>(API_ENDPOINTS.AUTH.LOGIN, {
    method: 'POST',
    headers: {"Authorization": authToken},
    body: JSON.stringify(data)
  });

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}

export async function auth_register(data: PostAuthRegister): Promise<AuthResponse | undefined> {
  const response = await api_request<AuthResponse>(API_ENDPOINTS.AUTH.REGISTER, {
    method: 'POST',
    headers: {"Authorization": authToken},
    body: JSON.stringify(data)
  });

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}

export async function auth_loggout(): Promise<AuthResponse | undefined> {
  const response = await api_request<AuthResponse>(API_ENDPOINTS.AUTH.LOGGOUT, {
    method: 'POST',
    headers: {"Authorization": authToken},
  });

  if (response != undefined) {
    return response.data;
  }

  return undefined;
}

export async function requestIndex(): Promise<ListResponse<ProductPageResponse> | undefined> {
  const response = await api_request<ListResponse<ProductPageResponse>>(API_ENDPOINTS.INDEX, {
    method: 'GET',
  });

  return response?.data;
}

export async function requestPage(slug:string): Promise<ListResponse<ProductPageResponse> | undefined> {
  const response = await api_request<ListResponse<ProductPageResponse>>(API_ENDPOINTS.PRODUCT.replace("{slug}", slug), {
    method: 'GET',
  });

  return response?.data;
}