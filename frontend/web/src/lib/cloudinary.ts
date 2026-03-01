const CLOUD_NAME = process.env.NEXT_PUBLIC_CLOUDINARY_CLOUD_NAME || '';

type Folder = 'scan' | 'recipe' | 'profile' | 'step';

interface UploadResult {
  url: string;
  publicId: string;
  width: number;
  height: number;
}

interface OptimizeOptions {
  width?: number;
  height?: number;
  quality?: string;
  format?: string;
  crop?: string;
}

export async function uploadImage(file: File | string, folder: Folder): Promise<UploadResult> {
  const formData = new FormData();

  if (typeof file === 'string') {
    formData.append('file', file);
  } else {
    formData.append('file', file);
  }
  formData.append('folder', folder);

  const res = await fetch('/api/v1/cloudinary/upload', {
    method: 'POST',
    body: formData,
  });

  if (!res.ok) {
    throw new Error('Image upload failed');
  }

  const data = await res.json();
  return data;
}

export function getOptimizedUrl(publicId: string, options: OptimizeOptions = {}): string {
  const { width, height, quality = 'auto', format = 'auto', crop = 'fill' } = options;

  const transforms: string[] = [];
  if (width) transforms.push(`w_${width}`);
  if (height) transforms.push(`h_${height}`);
  if (crop && (width || height)) transforms.push(`c_${crop}`);
  transforms.push(`q_${quality}`, `f_${format}`);

  const transformStr = transforms.join(',');
  return `https://res.cloudinary.com/${CLOUD_NAME}/image/upload/${transformStr}/${publicId}`;
}

export async function deleteImage(publicId: string): Promise<boolean> {
  const res = await fetch('/api/v1/cloudinary/upload', {
    method: 'DELETE',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ publicId }),
  });
  return res.ok;
}
