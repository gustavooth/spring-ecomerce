import { VIEW_IMAGE_ROUTE } from "$lib/fileServer";

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

export const fileExtensionMap: { [key: string]: number } = {
  // Imagens
  png: ItemType.IMAGE,
  jpeg: ItemType.IMAGE,
  jpg: ItemType.IMAGE,
  webp: ItemType.IMAGE,
  gif: ItemType.IMAGE,
  svg: ItemType.IMAGE,
  bmp: ItemType.IMAGE,
  tiff: ItemType.IMAGE,
  ico: ItemType.IMAGE,

  // Vídeos
  mp4: ItemType.VIDEO,
  mov: ItemType.VIDEO,
  avi: ItemType.VIDEO,
  wmv: ItemType.VIDEO,
  avchd: ItemType.VIDEO,
  webm: ItemType.VIDEO,
  flv: ItemType.VIDEO,
  mkv: ItemType.VIDEO,
  mpg: ItemType.VIDEO,
  mpeg: ItemType.VIDEO,
  "3gp": ItemType.VIDEO,

  // Músicas
  mp3: ItemType.MUSIC,
  wav: ItemType.MUSIC,
  aac: ItemType.MUSIC,
  ogg: ItemType.MUSIC,
  wma: ItemType.MUSIC,
  flac: ItemType.MUSIC,
  m4a: ItemType.MUSIC,
  aiff: ItemType.MUSIC,

  // Arquivos Compactados
  zip: ItemType.COMPRESS,
  rar: ItemType.COMPRESS,
  "7z": ItemType.COMPRESS,
  tar: ItemType.COMPRESS,
  gz: ItemType.COMPRESS,
  bz2: ItemType.COMPRESS,
  iso: ItemType.COMPRESS,
  dmg: ItemType.COMPRESS, // Imagens de disco, frequentemente compactadas

  // Documentos
  pdf: ItemType.DOCUMENT,
  doc: ItemType.DOCUMENT,
  docx: ItemType.DOCUMENT,
  ppt: ItemType.DOCUMENT,
  pptx: ItemType.DOCUMENT,
  xls: ItemType.DOCUMENT,
  xlsx: ItemType.DOCUMENT,
  odt: ItemType.DOCUMENT,
  ods: ItemType.DOCUMENT,
  odp: ItemType.DOCUMENT,
  rtf: ItemType.DOCUMENT,

  // Texto
  txt: ItemType.TEXT,
  md: ItemType.TEXT,
  log: ItemType.TEXT,
  csv: ItemType.TEXT,
  json: ItemType.TEXT,
  xml: ItemType.TEXT,

  // Código
  ts: ItemType.CODE,
  js: ItemType.CODE,
  tsx: ItemType.CODE,
  jsx: ItemType.CODE,
  html: ItemType.CODE,
  css: ItemType.CODE,
  scss: ItemType.CODE,
  java: ItemType.CODE,
  py: ItemType.CODE,
  c: ItemType.CODE,
  cpp: ItemType.CODE,
  cs: ItemType.CODE,
  php: ItemType.CODE,
  swift: ItemType.CODE,
  go: ItemType.CODE,
  rb: ItemType.CODE,
  sh: ItemType.CODE,
  sql: ItemType.CODE,
  yml: ItemType.CODE,
  yaml: ItemType.CODE,

  // Binários e Executáveis
  exe: ItemType.BINARY,
  dll: ItemType.BINARY,
  so: ItemType.BINARY,
  class: ItemType.BINARY,
  o: ItemType.BINARY,
  a: ItemType.BINARY,
  lib: ItemType.BINARY,
  dat: ItemType.BINARY,
  bin: ItemType.BINARY,
};

/**
 * Obtém o tipo de item com base no caminho completo do arquivo.
 * @param path O caminho completo do arquivo (ex: "/pasta/arquivo.png").
 * @returns O ItemType correspondente ou ItemType.OTHER se não for encontrado.
 */
export function getItemTypeByPath(path: string): number {
  // Extrai a extensão do arquivo a partir do último '.' na string.
  const extension = path.split('.').pop()?.toLowerCase();

  if (!extension) {
    return ItemType.OTHER;
  }

  return fileExtensionMap[extension] || ItemType.OTHER;
}

export function formatViewImagePath(path:string):string {
  return `${VIEW_IMAGE_ROUTE}?path=${path}`
}