import { NextRequest, NextResponse } from 'next/server';
import crypto from 'crypto';

const CLOUD_NAME = process.env.NEXT_PUBLIC_CLOUDINARY_CLOUD_NAME;
const API_KEY = process.env.CLOUDINARY_API_KEY;
const API_SECRET = process.env.CLOUDINARY_API_SECRET;

export async function POST(request: NextRequest) {
  if (!CLOUD_NAME || !API_KEY || !API_SECRET) {
    return NextResponse.json(
      { error: 'Cloudinary not configured' },
      { status: 500 },
    );
  }

  const formData = await request.formData();
  const file = formData.get('file');
  const folder = (formData.get('folder') as string) || 'general';

  if (!file) {
    return NextResponse.json({ error: 'No file provided' }, { status: 400 });
  }

  const timestamp = Math.round(Date.now() / 1000);
  const fullFolder = `fridge-recipe/${folder}`;

  const paramsToSign = `folder=${fullFolder}&timestamp=${timestamp}${API_SECRET}`;
  const signature = crypto.createHash('sha1').update(paramsToSign).digest('hex');

  const uploadData = new FormData();

  if (typeof file === 'string') {
    // Base64 data URL
    uploadData.append('file', file);
  } else {
    // File object
    uploadData.append('file', file);
  }

  uploadData.append('api_key', API_KEY);
  uploadData.append('timestamp', String(timestamp));
  uploadData.append('signature', signature);
  uploadData.append('folder', fullFolder);

  const res = await fetch(
    `https://api.cloudinary.com/v1_1/${CLOUD_NAME}/image/upload`,
    { method: 'POST', body: uploadData },
  );

  if (!res.ok) {
    const err = await res.text();
    return NextResponse.json(
      { error: 'Upload failed', detail: err },
      { status: 500 },
    );
  }

  const result = await res.json();

  return NextResponse.json({
    url: result.secure_url,
    publicId: result.public_id,
    width: result.width,
    height: result.height,
  });
}

export async function DELETE(request: NextRequest) {
  if (!CLOUD_NAME || !API_KEY || !API_SECRET) {
    return NextResponse.json(
      { error: 'Cloudinary not configured' },
      { status: 500 },
    );
  }

  const { publicId } = await request.json();
  if (!publicId) {
    return NextResponse.json({ error: 'No publicId' }, { status: 400 });
  }

  const timestamp = Math.round(Date.now() / 1000);
  const paramsToSign = `public_id=${publicId}&timestamp=${timestamp}${API_SECRET}`;
  const signature = crypto.createHash('sha1').update(paramsToSign).digest('hex');

  const formData = new FormData();
  formData.append('public_id', publicId);
  formData.append('api_key', API_KEY);
  formData.append('timestamp', String(timestamp));
  formData.append('signature', signature);

  const res = await fetch(
    `https://api.cloudinary.com/v1_1/${CLOUD_NAME}/image/destroy`,
    { method: 'POST', body: formData },
  );

  if (!res.ok) {
    return NextResponse.json({ error: 'Delete failed' }, { status: 500 });
  }

  return NextResponse.json({ success: true });
}
