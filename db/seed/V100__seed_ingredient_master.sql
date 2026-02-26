-- 식재료 마스터 시드 데이터
-- 한국 가정에서 흔히 사용하는 식재료 80개+

INSERT INTO ingredient_master (name, category, default_unit, default_expiry_days, aliases)
VALUES
    -- =====================
    -- 채소 (20개)
    -- =====================
    ('양파',     '채소', '개',   30,  ARRAY['양파', '파', 'onion']),
    ('대파',     '채소', '개',   14,  ARRAY['대파', '파', 'green onion', 'scallion']),
    ('마늘',     '채소', '개',   30,  ARRAY['마늘', 'garlic']),
    ('감자',     '채소', '개',   30,  ARRAY['감자', 'potato']),
    ('당근',     '채소', '개',   21,  ARRAY['당근', 'carrot']),
    ('배추',     '채소', 'kg',   7,   ARRAY['배추', 'napa cabbage', 'chinese cabbage']),
    ('무',       '채소', '개',   14,  ARRAY['무', '흰무', 'radish', 'daikon']),
    ('시금치',   '채소', '봉',   5,   ARRAY['시금치', 'spinach']),
    ('청양고추', '채소', '개',   7,   ARRAY['청양고추', '고추', '홍고추', 'chili pepper']),
    ('파프리카', '채소', '개',   7,   ARRAY['파프리카', '피망', 'paprika', 'bell pepper']),
    ('표고버섯', '채소', '봉',   5,   ARRAY['표고버섯', '버섯', 'shiitake mushroom']),
    ('콩나물',   '채소', '봉',   3,   ARRAY['콩나물', 'bean sprout']),
    ('브로콜리', '채소', '개',   5,   ARRAY['브로콜리', 'broccoli']),
    ('양배추',   '채소', '개',   14,  ARRAY['양배추', 'cabbage']),
    ('오이',     '채소', '개',   5,   ARRAY['오이', 'cucumber']),
    ('애호박',   '채소', '개',   5,   ARRAY['애호박', '호박', 'zucchini']),
    ('깻잎',     '채소', '봉',   3,   ARRAY['깻잎', '참깻잎', 'perilla leaf']),
    ('상추',     '채소', '봉',   3,   ARRAY['상추', 'lettuce']),
    ('고구마',   '채소', '개',   30,  ARRAY['고구마', 'sweet potato']),
    ('토마토',   '채소', '개',   7,   ARRAY['토마토', 'tomato']),

    -- =====================
    -- 육류 (10개)
    -- =====================
    ('소고기',       '육류', 'g',   3,  ARRAY['소고기', '쇠고기', 'beef']),
    ('돼지고기',     '육류', 'g',   3,  ARRAY['돼지고기', '돼지', 'pork']),
    ('닭고기',       '육류', 'g',   2,  ARRAY['닭고기', '닭', 'chicken']),
    ('삼겹살',       '육류', 'g',   3,  ARRAY['삼겹살', 'pork belly']),
    ('돼지목살',     '육류', 'g',   3,  ARRAY['돼지목살', '목살', 'pork neck']),
    ('소등심',       '육류', 'g',   3,  ARRAY['소등심', '등심', 'sirloin']),
    ('닭가슴살',     '육류', 'g',   2,  ARRAY['닭가슴살', 'chicken breast']),
    ('닭다리',       '육류', '개',  2,  ARRAY['닭다리', '닭다리살', 'chicken leg', 'chicken thigh']),
    ('돼지앞다리살', '육류', 'g',   3,  ARRAY['돼지앞다리살', '앞다리살', 'pork shoulder']),
    ('소갈비',       '육류', 'g',   3,  ARRAY['소갈비', '갈비', 'beef ribs']),

    -- =====================
    -- 해산물 (10개)
    -- =====================
    ('새우',   '해산물', 'g',  2,  ARRAY['새우', '왕새우', 'shrimp', 'prawn']),
    ('오징어', '해산물', '마리', 2, ARRAY['오징어', 'squid']),
    ('고등어', '해산물', '마리', 2, ARRAY['고등어', 'mackerel']),
    ('삼치',   '해산물', '마리', 2, ARRAY['삼치', 'spanish mackerel']),
    ('연어',   '해산물', 'g',  3,  ARRAY['연어', '훈제연어', 'salmon']),
    ('참치',   '해산물', '캔', 730, ARRAY['참치', '참치캔', 'tuna']),
    ('조개',   '해산물', 'g',  1,  ARRAY['조개', '바지락', '홍합', 'clam', 'mussel']),
    ('멸치',   '해산물', 'g',  180, ARRAY['멸치', '국물멸치', 'anchovy']),
    ('꽃게',   '해산물', '마리', 1, ARRAY['꽃게', '게', 'crab', 'blue crab']),
    ('전복',   '해산물', '개', 3,  ARRAY['전복', 'abalone']),

    -- =====================
    -- 유제품/달걀 (6개)
    -- =====================
    ('달걀',   '유제품/달걀', '개',   21,  ARRAY['달걀', '계란', 'egg']),
    ('우유',   '유제품/달걀', 'ml',   7,   ARRAY['우유', 'milk']),
    ('치즈',   '유제품/달걀', '장',   14,  ARRAY['치즈', '슬라이스치즈', 'cheese', 'sliced cheese']),
    ('버터',   '유제품/달걀', 'g',    30,  ARRAY['버터', 'butter']),
    ('생크림', '유제품/달걀', 'ml',   7,   ARRAY['생크림', '크림', 'heavy cream', 'whipping cream']),
    ('요구르트', '유제품/달걀', '개', 14,  ARRAY['요구르트', '요거트', 'yogurt']),

    -- =====================
    -- 두부/콩 (5개)
    -- =====================
    ('두부',   '두부/콩', '모',  3,  ARRAY['두부', 'tofu']),
    ('순두부', '두부/콩', '봉',  2,  ARRAY['순두부', 'soft tofu', 'silken tofu']),
    ('콩',     '두부/콩', 'g',   365, ARRAY['콩', '대두', 'soybean']),
    ('검은콩', '두부/콩', 'g',   365, ARRAY['검은콩', '흑태', 'black bean']),
    ('된장',   '두부/콩', 'g',   365, ARRAY['된장', 'doenjang', 'fermented soybean paste']),

    -- =====================
    -- 쌀/곡물/면 (8개)
    -- =====================
    ('쌀',       '쌀/곡물/면', 'g',   365, ARRAY['쌀', '백미', 'rice']),
    ('찹쌀',     '쌀/곡물/면', 'g',   365, ARRAY['찹쌀', 'glutinous rice', 'sticky rice']),
    ('국수',     '쌀/곡물/면', 'g',   180, ARRAY['국수', '소면', 'noodles', 'wheat noodles']),
    ('라면',     '쌀/곡물/면', '개',  180, ARRAY['라면', '인스턴트라면', 'ramen', 'instant noodles']),
    ('스파게티', '쌀/곡물/면', 'g',   365, ARRAY['스파게티', '파스타', 'spaghetti', 'pasta']),
    ('밀가루',   '쌀/곡물/면', 'g',   180, ARRAY['밀가루', 'flour', 'all-purpose flour']),
    ('식빵',     '쌀/곡물/면', '봉',  5,   ARRAY['식빵', '빵', 'bread', 'sandwich bread']),
    ('떡',       '쌀/곡물/면', 'g',   3,   ARRAY['떡', '가래떡', '떡볶이떡', 'rice cake', 'tteok']),

    -- =====================
    -- 과일 (8개)
    -- =====================
    ('사과', '과일', '개',  14, ARRAY['사과', 'apple']),
    ('배',   '과일', '개',  14, ARRAY['배', '한국배', 'pear', 'korean pear']),
    ('귤',   '과일', '개',  14, ARRAY['귤', '감귤', 'tangerine', 'mandarin']),
    ('바나나', '과일', '개', 5, ARRAY['바나나', 'banana']),
    ('딸기', '과일', '개',  3,  ARRAY['딸기', 'strawberry']),
    ('포도', '과일', '송이', 7, ARRAY['포도', 'grape']),
    ('수박', '과일', '개',  5,  ARRAY['수박', 'watermelon']),
    ('레몬', '과일', '개',  14, ARRAY['레몬', 'lemon']),

    -- =====================
    -- 조미료/소스 (13개)
    -- =====================
    ('간장',     '조미료/소스', 'ml',  365, ARRAY['간장', '진간장', '국간장', 'soy sauce']),
    ('고추장',   '조미료/소스', 'g',   365, ARRAY['고추장', 'gochujang', 'red pepper paste']),
    ('소금',     '조미료/소스', 'g',   1825, ARRAY['소금', '꽃소금', 'salt']),
    ('설탕',     '조미료/소스', 'g',   1825, ARRAY['설탕', '백설탕', 'sugar']),
    ('식초',     '조미료/소스', 'ml',  365, ARRAY['식초', '사과식초', 'vinegar', 'apple cider vinegar']),
    ('참기름',   '조미료/소스', 'ml',  180, ARRAY['참기름', 'sesame oil']),
    ('들기름',   '조미료/소스', 'ml',  90,  ARRAY['들기름', 'perilla oil']),
    ('고춧가루', '조미료/소스', 'g',   365, ARRAY['고춧가루', '빨간고춧가루', 'red pepper powder', 'gochugaru']),
    ('후추',     '조미료/소스', 'g',   365, ARRAY['후추', '흑후추', 'black pepper', 'pepper']),
    ('미림',     '조미료/소스', 'ml',  365, ARRAY['미림', '맛술', 'mirin', 'rice wine']),
    ('굴소스',   '조미료/소스', 'ml',  365, ARRAY['굴소스', 'oyster sauce']),
    ('케첩',     '조미료/소스', 'ml',  180, ARRAY['케첩', '토마토케첩', 'ketchup', 'tomato ketchup']),
    ('마요네즈', '조미료/소스', 'ml',  90,  ARRAY['마요네즈', '마요', 'mayonnaise', 'mayo'])

ON CONFLICT (name) DO NOTHING;
