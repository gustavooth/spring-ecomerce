import { api_request, authToken, type ApiResponse, type AuthResponse } from "$lib/request"

export const ADMIN_ENDPOINTS = {
  PRODUCT: {
    NEW: {
      PAGE: "/admin/product/new-page",
      PRODUCT: "/admin/product/new-product",
      ATTRIBUTE: "/admin/product/new-attribute",
      ATTRIBUTE_VALUE: "/admin/product/new-attribute-value",
      PRODUCT_VALUE: "/admin/product/new-product-value",
      PAGE_IMAGE: "/admin/product/new-page-image",
    },
    SELECT_PAGE: "/admin/product/select-page",
    SELECT_PAGES: "/admin/product/select-pages",
    REMOVE: {
      PAGE: "/admin/product/remove-page",
      PRODUCT: "/admin/product/remove-product",
      ATTRIBUTE: "/admin/product/remove-attribute",
      ATTRIBUTE_VALUE: "/admin/product/remove-attribute-value",
      PRODUCT_VALUE: "/admin/product/remove-product-value",
      PAGE_IMAGE: "/admin/product/remove-page-image",
    }
  }
};

export interface NewPageRequest {
  title:string;
  slug:string;
  shortDescription:string;
  description:string
}

export interface NewProductRequest {
  pageId:number;
  price:string;
  imagePath:string;
}

export interface NewAttributeRequest {
  name:string;
  pageId:number;
}

export interface NewAttributeValueRequest {
  attributeId:number;
  value:string;
}

export interface NewProductValueRequest {
  attributeValueId:number;
  productId:number;
} 

export interface NewPageImageRequest {
  pageId:number;
  path:string;
  index:number;
}

export interface SelectRequest {
  id:number;
}

export interface RemoveRequest {
  id:number
}

export interface ListResponse<T> {
  data:T[];
}

export interface ProductValueResponse {
  id:number;
  valueId:number;
  value:string;
  productId:number;
}

export interface ProductResponse {
  id:number;
  pageId:number;
  price:number;
  imagePath:string;
  values:ProductValueResponse[];
}

export interface AttributeValueResponse{
  id:number;
  attributeId:number;
  value:string;
}

export interface AttributeResponse {
  id:number;
  name:string;
  pageId:string;
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
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.NEW.PAGE);
}

export async function requestNewProduct(data:NewProductRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.NEW.PRODUCT);
}

export async function requestNewAttribute(data:NewAttributeRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.NEW.ATTRIBUTE);
}

export async function requestNewAttributeValue(data:NewAttributeValueRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.NEW.ATTRIBUTE_VALUE);
}

export async function requestNewProductValue(data:NewProductValueRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.NEW.PRODUCT_VALUE);
}

export async function requestNewPageImage(data:NewPageImageRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.NEW.PAGE_IMAGE);
}

export async function requestRemovePage(data:RemoveRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.REMOVE.PAGE);
}

export async function requestRemoveProduct(data:RemoveRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.REMOVE.PRODUCT);
}

export async function requestRemoveAttribute(data:RemoveRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.REMOVE.ATTRIBUTE);
}

export async function requestRemoveAttributeValue(data:RemoveRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.REMOVE.ATTRIBUTE_VALUE);
}

export async function requestRemoveProductValue(data:RemoveRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.REMOVE.PRODUCT_VALUE);
}

export async function requestRemovePageImage(data:RemoveRequest):Promise<ApiResponse<GeneralResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.REMOVE.PAGE_IMAGE);
}

export async function requestSelectPage(data:SelectRequest):Promise<ApiResponse<ProductPageResponse> | undefined> {
  return admin_request(data, ADMIN_ENDPOINTS.PRODUCT.SELECT_PAGE);
}

export async function requestSelectPages():Promise<ApiResponse<ListResponse<ProductPageResponse>> | undefined> {
  return admin_request_no_data(ADMIN_ENDPOINTS.PRODUCT.SELECT_PAGES);
}