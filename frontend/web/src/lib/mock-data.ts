// Mock data for when Supabase is not configured

export const mockIngredientMaster = [
  { id: 1, name: '양파', category: '채소', iconUrl: null, defaultUnit: '개' },
  { id: 2, name: '대파', category: '채소', iconUrl: null, defaultUnit: '대' },
  { id: 3, name: '마늘', category: '채소', iconUrl: null, defaultUnit: '쪽' },
  { id: 4, name: '감자', category: '채소', iconUrl: null, defaultUnit: '개' },
  { id: 5, name: '당근', category: '채소', iconUrl: null, defaultUnit: '개' },
  { id: 6, name: '토마토', category: '채소', iconUrl: null, defaultUnit: '개' },
  { id: 7, name: '시금치', category: '채소', iconUrl: null, defaultUnit: 'g' },
  { id: 8, name: '버섯', category: '채소', iconUrl: null, defaultUnit: 'g' },
  { id: 9, name: '애호박', category: '채소', iconUrl: null, defaultUnit: '개' },
  { id: 10, name: '고추', category: '채소', iconUrl: null, defaultUnit: '개' },
  { id: 11, name: '돼지고기', category: '육류', iconUrl: null, defaultUnit: 'g' },
  { id: 12, name: '소고기', category: '육류', iconUrl: null, defaultUnit: 'g' },
  { id: 13, name: '닭고기', category: '육류', iconUrl: null, defaultUnit: 'g' },
  { id: 14, name: '계란', category: '육류', iconUrl: null, defaultUnit: '개' },
  { id: 15, name: '새우', category: '해산물', iconUrl: null, defaultUnit: 'g' },
  { id: 16, name: '우유', category: '유제품', iconUrl: null, defaultUnit: 'ml' },
  { id: 17, name: '치즈', category: '유제품', iconUrl: null, defaultUnit: 'g' },
  { id: 18, name: '간장', category: '양념', iconUrl: null, defaultUnit: 'ml' },
  { id: 19, name: '된장', category: '양념', iconUrl: null, defaultUnit: 'g' },
  { id: 20, name: '고추장', category: '양념', iconUrl: null, defaultUnit: 'g' },
  { id: 21, name: '참기름', category: '양념', iconUrl: null, defaultUnit: 'ml' },
  { id: 22, name: '식용유', category: '양념', iconUrl: null, defaultUnit: 'ml' },
  { id: 23, name: '소금', category: '양념', iconUrl: null, defaultUnit: 'g' },
  { id: 24, name: '설탕', category: '양념', iconUrl: null, defaultUnit: 'g' },
  { id: 25, name: '쌀', category: '곡물', iconUrl: null, defaultUnit: 'g' },
  { id: 26, name: '파스타', category: '곡물', iconUrl: null, defaultUnit: 'g' },
  { id: 27, name: '멸치', category: '해산물', iconUrl: null, defaultUnit: 'g' },
  { id: 28, name: '레몬', category: '과일', iconUrl: null, defaultUnit: '개' },
];

export const mockUserIngredients = [
  { id: 1, ingredientId: 14, ingredientName: '계란', category: '육류', iconUrl: null, quantity: 10, unit: '개', expiryDate: getFutureDate(5), storageType: 'fridge', memo: null, registeredVia: 'manual', createdAt: new Date().toISOString() },
  { id: 2, ingredientId: 1, ingredientName: '양파', category: '채소', iconUrl: null, quantity: 3, unit: '개', expiryDate: getFutureDate(14), storageType: 'fridge', memo: null, registeredVia: 'manual', createdAt: new Date().toISOString() },
  { id: 3, ingredientId: 3, ingredientName: '마늘', category: '채소', iconUrl: null, quantity: 20, unit: '쪽', expiryDate: getFutureDate(30), storageType: 'fridge', memo: null, registeredVia: 'manual', createdAt: new Date().toISOString() },
  { id: 4, ingredientId: 16, ingredientName: '우유', category: '유제품', iconUrl: null, quantity: 1000, unit: 'ml', expiryDate: getFutureDate(2), storageType: 'fridge', memo: null, registeredVia: 'manual', createdAt: new Date().toISOString() },
  { id: 5, ingredientId: 6, ingredientName: '토마토', category: '채소', iconUrl: null, quantity: 5, unit: '개', expiryDate: getFutureDate(3), storageType: 'fridge', memo: null, registeredVia: 'manual', createdAt: new Date().toISOString() },
  { id: 6, ingredientId: 26, ingredientName: '파스타', category: '곡물', iconUrl: null, quantity: 500, unit: 'g', expiryDate: getFutureDate(180), storageType: 'pantry', memo: null, registeredVia: 'manual', createdAt: new Date().toISOString() },
];

export const mockRecipes = [
  {
    id: 1, title: '김치볶음밥', description: '남은 밥과 김치로 만드는 간단한 볶음밥',
    cuisineType: '한식', difficulty: 'easy', cookingTime: 15, servings: 1, calories: 450,
    thumbnailUrl: null, tags: ['간단요리', '한그릇', '볶음밥'], avgRating: 4.5, viewCount: 120,
    steps: [{ step: 1, description: '김치를 잘게 썰어주세요' }, { step: 2, description: '팬에 기름을 두르고 김치를 볶아주세요' }, { step: 3, description: '밥을 넣고 함께 볶아주세요' }, { step: 4, description: '계란후라이를 올려 완성합니다' }],
    nutrition: { protein: 15, carbs: 65, fat: 12, fiber: 3 },
    ingredients: [{ id: 25, name: '쌀', quantity: '1공기', isEssential: true }, { id: 14, name: '계란', quantity: '1개', isEssential: false }],
  },
  {
    id: 2, title: '토마토 파스타', description: '신선한 토마토로 만드는 클래식 파스타',
    cuisineType: '양식', difficulty: 'easy', cookingTime: 25, servings: 2, calories: 520,
    thumbnailUrl: null, tags: ['파스타', '토마토', '양식'], avgRating: 4.3, viewCount: 95,
    steps: [{ step: 1, description: '파스타를 삶아주세요' }, { step: 2, description: '마늘을 올리브오일에 볶아주세요' }, { step: 3, description: '토마토를 넣고 소스를 만들어주세요' }, { step: 4, description: '삶은 파스타를 소스에 버무려주세요' }],
    nutrition: { protein: 18, carbs: 72, fat: 15, fiber: 4 },
    ingredients: [{ id: 26, name: '파스타', quantity: '200g', isEssential: true }, { id: 6, name: '토마토', quantity: '3개', isEssential: true }, { id: 3, name: '마늘', quantity: '4쪽', isEssential: true }],
  },
  {
    id: 3, title: '된장찌개', description: '구수한 된장으로 끓이는 한국 전통 찌개',
    cuisineType: '한식', difficulty: 'easy', cookingTime: 30, servings: 3, calories: 180,
    thumbnailUrl: null, tags: ['찌개', '된장', '전통'], avgRating: 4.7, viewCount: 200,
    steps: [{ step: 1, description: '멸치육수를 끓여주세요' }, { step: 2, description: '된장을 풀어주세요' }, { step: 3, description: '감자, 호박, 두부를 넣어주세요' }, { step: 4, description: '대파, 고추를 넣고 마무리합니다' }],
    nutrition: { protein: 12, carbs: 15, fat: 6, fiber: 5 },
    ingredients: [{ id: 19, name: '된장', quantity: '2큰술', isEssential: true }, { id: 4, name: '감자', quantity: '1개', isEssential: false }, { id: 9, name: '애호박', quantity: '1/2개', isEssential: false }],
  },
  {
    id: 4, title: '닭가슴살 샐러드', description: '다이어트에 좋은 고단백 샐러드',
    cuisineType: '양식', difficulty: 'easy', cookingTime: 20, servings: 1, calories: 280,
    thumbnailUrl: null, tags: ['다이어트', '샐러드', '고단백'], avgRating: 4.1, viewCount: 80,
    steps: [{ step: 1, description: '닭가슴살을 삶거나 구워주세요' }, { step: 2, description: '채소를 씻고 먹기 좋게 잘라주세요' }],
    nutrition: { protein: 35, carbs: 12, fat: 8, fiber: 6 },
    ingredients: [{ id: 13, name: '닭고기', quantity: '200g', isEssential: true }, { id: 6, name: '토마토', quantity: '1개', isEssential: false }],
  },
  {
    id: 5, title: '계란말이', description: '부드러운 계란말이',
    cuisineType: '한식', difficulty: 'medium', cookingTime: 15, servings: 2, calories: 200,
    thumbnailUrl: null, tags: ['반찬', '계란', '간단요리'], avgRating: 4.4, viewCount: 150,
    steps: [{ step: 1, description: '계란을 풀고 소금, 파를 넣어주세요' }, { step: 2, description: '팬에 기름을 두르고 약불로 줄여주세요' }, { step: 3, description: '계란물을 얇게 펴고 말아주세요' }],
    nutrition: { protein: 14, carbs: 2, fat: 12, fiber: 0 },
    ingredients: [{ id: 14, name: '계란', quantity: '4개', isEssential: true }, { id: 2, name: '대파', quantity: '1/2대', isEssential: false }],
  },
  {
    id: 6, title: '소고기 미역국', description: '생일이나 특별한 날의 미역국',
    cuisineType: '한식', difficulty: 'medium', cookingTime: 40, servings: 4, calories: 220,
    thumbnailUrl: null, tags: ['국', '미역', '소고기', '생일'], avgRating: 4.8, viewCount: 180,
    steps: [{ step: 1, description: '미역을 불려주세요' }, { step: 2, description: '소고기를 참기름에 볶아주세요' }, { step: 3, description: '물을 넣고 끓여주세요' }],
    nutrition: { protein: 20, carbs: 8, fat: 10, fiber: 3 },
    ingredients: [{ id: 12, name: '소고기', quantity: '150g', isEssential: true }, { id: 21, name: '참기름', quantity: '1큰술', isEssential: true }],
  },
];

export const mockUser = {
  id: 1,
  email: 'demo@fridgerecipe.app',
  nickname: '냉장고 요리사',
  profileImage: null,
  oauthProvider: 'demo',
  dietaryPrefs: {},
};

function getFutureDate(days: number): string {
  const d = new Date();
  d.setDate(d.getDate() + days);
  return d.toISOString().split('T')[0];
}

export const mockCategories = ['채소', '육류', '해산물', '유제품', '양념', '곡물', '과일'];
