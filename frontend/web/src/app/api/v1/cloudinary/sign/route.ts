import { NextRequest, NextResponse } from 'next/server';
import crypto from 'crypto';

export async function POST(request: NextRequest) {
  const apiSecret = process.env.CLOUDINARY_API_SECRET;
  const apiKey = process.env.CLOUDINARY_API_KEY;

  if (!apiSecret || !apiKey) {
    return NextResponse.json(
      { error: 'Cloudinary not configured' },
      { status: 500 },
    );
  }

  const { folder } = await request.json();
  const timestamp = Math.round(Date.now() / 1000);

  const params: Record<string, string | number> = {
    folder: `fridge-recipe/${folder || 'general'}`,
    timestamp,
  };

  const sortedParams = Object.keys(params)
    .sort()
    .map((key) => `${key}=${params[key]}`)
    .join('&');

  const signature = crypto
    .createHash('sha1')
    .update(sortedParams + apiSecret)
    .digest('hex');

  return NextResponse.json({
    signature,
    timestamp,
    apiKey,
    cloudName: process.env.NEXT_PUBLIC_CLOUDINARY_CLOUD_NAME,
    folder: params.folder,
  });
}
