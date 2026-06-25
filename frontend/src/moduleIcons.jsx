const ICON_PATHS = {
  LEAD: "M12 12a4 4 0 1 0-4-4 4 4 0 0 0 4 4Zm0 2c-3.31 0-6 1.57-6 3.5V19h12v-1.5c0-1.93-2.69-3.5-6-3.5Z",
  KHACH_HANG: "M4 20V8l8-4 8 4v12h-5v-6H9v6H4Zm3-2h2v-6h6v6h2V9.24l-5-2.5-5 2.5V18Z",
  CO_HOI: "M12 21a9 9 0 1 1 9-9h-2a7 7 0 1 0-7 7v2Zm0-4a5 5 0 1 1 5-5h-2a3 3 0 1 0-3 3v2Zm8.71-2.29-3-3L20 9.42l3 3-2.29 2.29ZM13 13l4-4 1.41 1.41-4 4H13V13Z",
  HOAT_DONG: "M6 3h12v18H6V3Zm2 2v14h8V5H8Zm2 3h4v2h-4V8Zm0 4h4v2h-4v-2Z",
  BAO_GIA: "M7 3h8l4 4v14H7V3Zm2 2v14h8V8h-3V5H9Zm2 6h4v2h-4v-2Zm0 4h4v2h-4v-2Z",
  HOP_DONG: "M6 2h9l5 5v15H6V2Zm2 2v16h10V8h-4V4H8Zm2 8h6v2h-6v-2Zm0 4h6v2h-6v-2Z",
  HOA_DON: "M6 3h12v18l-3-2-3 2-3-2-3 2V3Zm2 3v11.26l1-.66 3 2 3-2 1 .66V6H8Zm2 3h4v2h-4V9Zm0 4h6v2h-6v-2Z",
  TAI_CHINH: "M5 6h14v12H5V6Zm2 2v8h10V8H7Zm5 7a3 3 0 1 1 0-6 3 3 0 0 1 0 6Z",
  BAO_CAO: "M5 19h14v2H5v-2Zm2-2V9h3v8H7Zm5 0V4h3v13h-3Zm5 0v-6h3v6h-3Z",
  SAN_PHAM: "M12 2 3 6.5v11L12 22l9-4.5v-11L12 2Zm0 2.24 4.53 2.26L12 8.76 7.47 6.5 12 4.24ZM5 8.12l6 3v7.64l-6-3V8.12Zm8 10.64v-7.64l6-3v7.64l-6 3Z",
  TICKET: "M4 6a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v3a3 3 0 0 0 0 6v3a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2v-3a3 3 0 0 0 0-6V6Zm8 1v10h2V7h-2Z",
  NHAN_VIEN: "M16 11a3 3 0 1 0-2.83-4H10a4 4 0 1 0-3.46 5.98C4.44 13.63 3 15.05 3 17v2h10v-2c0-1.23-.58-2.25-1.52-3A5.86 5.86 0 0 1 16 12.5c1.55 0 2.96.49 4 1.32V13c0-1.1-1.79-2-4-2Zm-8 1a2 2 0 1 1 0-4 2 2 0 0 1 0 4Zm8 2c-2.21 0-4 1.12-4 2.5V19h8v-2.5c0-1.38-1.79-2.5-4-2.5Z",
};

export function ModuleIcon({ moduleKey, size = 18, color = "currentColor", style }) {
  const path = ICON_PATHS[moduleKey] || ICON_PATHS.HOAT_DONG;

  return (
    <svg
      aria-hidden="true"
      viewBox="0 0 24 24"
      width={size}
      height={size}
      fill={color}
      style={{ display: "block", flexShrink: 0, ...style }}
    >
      <path d={path} />
    </svg>
  );
}

export const MODULE_ICON_KEYS = Object.keys(ICON_PATHS);

const ACTION_PATHS = {
  add: "M11 5h2v6h6v2h-6v6h-2v-6H5v-2h6V5Z",
  edit: "M4 17.25V20h2.75L17.81 8.94l-2.75-2.75L4 17.25ZM19.71 7.04a1 1 0 0 0 0-1.41l-1.34-1.34a1 1 0 0 0-1.41 0l-1.06 1.06 2.75 2.75 1.06-1.06Z",
  delete: "M7 21a2 2 0 0 1-2-2V7h14v12a2 2 0 0 1-2 2H7ZM8 9v10h8V9H8Zm2-6h4l1 1h4v2H5V4h4l1-1Z",
  search: "M10.5 4a6.5 6.5 0 0 1 5.18 10.43l4.45 4.44-1.42 1.42-4.44-4.45A6.5 6.5 0 1 1 10.5 4Zm0 2a4.5 4.5 0 1 0 0 9 4.5 4.5 0 0 0 0-9Z",
  refresh: "M17.65 6.35A7.95 7.95 0 0 0 12 4a8 8 0 1 0 7.75 10h-2.1A6 6 0 1 1 12 6c1.66 0 3.14.69 4.22 1.78L13 11h8V3l-3.35 3.35Z",
  save: "M5 3h12l2 2v16H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2Zm0 2v14h14V6.24L16.76 5H16v5H7V5H5Zm4 0v3h5V5H9Zm-1 8h8v4H8v-4Z",
  close: "M6.41 5 12 10.59 17.59 5 19 6.41 13.41 12 19 17.59 17.59 19 12 13.41 6.41 19 5 17.59 10.59 12 5 6.41 6.41 5Z",
  view: "M12 5c5 0 9 4.5 10 7-1 2.5-5 7-10 7S3 14.5 2 12c1-2.5 5-7 10-7Zm0 2c-3.43 0-6.35 2.74-7.75 5C5.65 14.26 8.57 17 12 17s6.35-2.74 7.75-5C18.35 9.74 15.43 7 12 7Zm0 2.5a2.5 2.5 0 1 1 0 5 2.5 2.5 0 0 1 0-5Z",
  image: "M5 5h14v14H5V5Zm2 2v10h10V7H7Zm1 8 2.8-3.5 2 2.4 1.2-1.5 2 2.6H8Zm1.5-4.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3Z",
  reply: "M10 9V5l-7 7 7 7v-4h4c3.31 0 6 2.69 6 6v-2c0-5.52-4.48-10-10-10Z",
};

export function ActionIcon({ name, size = 16, color = "currentColor", style }) {
  const path = ACTION_PATHS[name] || ACTION_PATHS.view;

  return (
    <svg
      aria-hidden="true"
      viewBox="0 0 24 24"
      width={size}
      height={size}
      fill={color}
      style={{ display: "block", flexShrink: 0, ...style }}
    >
      <path d={path} />
    </svg>
  );
}
