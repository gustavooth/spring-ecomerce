import { api_request, authToken, type ApiResponse, type AuthResponse } from "$lib/request"

export const ADMIN_ENDPOINTS = {
  PRODUCT: {
    NEW_PAGE: "/admin/product/new-page",
    REMOVE_PAGE: "/admin/product/remove-page",
    UPDATE_PAGE: "/admin/product/update-page",
    SELECT_PAGE: "/admin/product/select-page",
    SELECT_PAGES: "/admin/product/select-pages",
  }
};

export interface NewPageRequest {
  title:string;
  slug:string;
  shortDescription:string;
  description:string;
  products:NewProductRequest[];
  attributes:NewAttributeRequest[];
  images:NewPageImageRequest[];
}

export interface NewProductRequest {
  price:string;
  stock:string;
  imagePath:string;
  values:string[];
}

export interface NewAttributeRequest {
  name:string;
  showImage:boolean;
  values:NewAttributeValueRequest[];
}

export interface NewAttributeValueRequest {
  value:string;
}

export interface NewPageImageRequest {
  path:string;
  index:number;
}

export interface SelectRequest {
  id:number;
}

export interface RemoveRequest {
  id:number
}

export interface UpdateProductRequest {
  id:number;
  price:string;
  stock:string;
  imagePath:string;
  values:string[];
  active:boolean;
}

export interface UpdateAttributeRequest {
  id:number;
  name:string;
  showImage:boolean;
  newValues:NewAttributeValueRequest[];
  active:boolean;
}

export interface UpdateAttributeValueRequest {
  id:number;
  value:string;
  active:boolean;
}

export interface UpdateProductPageRequest {
  id:number;
  title:string;
  slug:string;
  shortDescription:string;
  description:string;
  products:NewProductRequest[];
  attributes:NewAttributeRequest[];
  images:NewPageImageRequest[];
  updateProducts:UpdateProductRequest[];
  updateAttributes:UpdateAttributeRequest[];
  updateAttributeValues:UpdateAttributeValueRequest[];
}

export interface ListResponse<T> {
  data:T[];
}

export interface ProductValueResponse {
  id:number;
  valueId:number;
  value:string;
  productId:number;
  active:boolean
}

export interface ProductResponse {
  id:number;
  pageId:number;
  price:number;
  stock:number;
  imagePath:string;
  active:boolean;
  values:ProductValueResponse[];
}

export interface AttributeValueResponse{
  id:number;
  attributeId:number;
  value:string;
  active:boolean;
}

export interface AttributeResponse {
  id:number;
  name:string;
  pageId:string;
  showImage:boolean;
  active:boolean;
  values:AttributeValueResponse[];
}

export interface PageImageResponse {
  id:number;
  pageId:number;
  path:string;
  index:number;
}

export interface ProductPageResponse {
  id:number;
  title:string;
  slug:string;
  shortDescription:string;
  description:string;
  active:boolean;
  products:ProductResponse[];
  attributes:AttributeResponse[];
  images:PageImageResponse[];
}

export interface GeneralResponse {
  id:number;
}

async function admin_request<T, E>(data:E, endpoint:string):Promise<ApiResponse<T> | undefined> {
  const response = await api_request<T>(endpoint, {
    method: 'POST',
    headers: {"Authorization": authToken},
    body: JSON.stringify(data)
  });

  return response;
}

async function admin_request_no_data<T, E>(endpoint:string):Promise<ApiResponse<T> | undefined> {
  const response = await api_request<T>(endpoint, {
    method: 'POST',
    headers: {"Authorization": authToken},
  });

  return response;
}

export async function requestNewPage(data:NewPageRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.NEW_PAGE);
}

export async function requestUpdatePage(data:SelectRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.UPDATE_PAGE);
}

export async function requestRemovePage(data:SelectRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.REMOVE_PAGE);
}

export async function requestSelectPage(data:SelectRequest):Promise<ApiResponse<ProductPageResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.SELECT_PAGE);
}

export async function requestSelectPages():Promise<ApiResponse<ListResponse<ProductPageResponse>> | undefined> {
  return admin_request_no_data(ADMIN_ENDPOINTS.PRODUCT.SELECT_PAGES);
}