-- V3: Seed 20 popular Korean home-cooking recipes
-- Uses subqueries to reference ingredient_master by name for portability

-- ============================================================
-- 1. 김치볶음밥 (Kimchi Fried Rice)
-- ============================================================
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '김치볶음밥',
    '잘 익은 김치와 밥을 볶아 만드는 한국의 대표 볶음밥. 간단하면서도 깊은 맛이 일품입니다.',
    '한식', 'easy', 15, 1, 450,
    '[
        {"step": 1, "text": "김치를 잘게 썰고, 대파는 송송 썰어 준비합니다.", "image_url": null},
        {"step": 2, "text": "달군 팬에 식용유를 두르고 김치를 넣어 중불에서 2-3분간 볶습니다.", "image_url": null},
        {"step": 3, "text": "찬밥을 넣고 고추장 1큰술, 참기름을 넣어 고루 섞으며 볶습니다.", "image_url": null},
        {"step": 4, "text": "접시에 담고 계란 프라이와 김가루를 올려 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 65, "protein": 12, "fat": 14}'::jsonb,
    ARRAY['볶음밥', '김치', '간단요리', '혼밥', '자취요리'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 2. 된장찌개 (Soybean Paste Stew)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '된장찌개',
    '구수한 된장과 각종 채소를 넣어 끓인 한국의 대표 찌개. 밥 한 공기가 뚝딱입니다.',
    '한식', 'easy', 25, 2, 180,
    '[
        {"step": 1, "text": "두부는 깍둑썰기, 감자와 양파는 한입 크기로, 애호박은 반달썰기 합니다.", "image_url": null},
        {"step": 2, "text": "멸치 육수 400ml를 끓이고 된장 2큰술을 풀어줍니다.", "image_url": null},
        {"step": 3, "text": "감자, 양파를 먼저 넣고 5분간 끓인 후 두부, 애호박을 넣습니다.", "image_url": null},
        {"step": 4, "text": "청양고추, 대파를 넣고 다진 마늘 1작은술을 추가해 3분 더 끓입니다.", "image_url": null},
        {"step": 5, "text": "간을 보고 부족하면 된장을 조금 더 넣어 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 15, "protein": 12, "fat": 6}'::jsonb,
    ARRAY['찌개', '된장', '집밥', '건강식', '기본반찬'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 3. 제육볶음 (Spicy Stir-fried Pork)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '제육볶음',
    '매콤달콤한 양념에 볶은 돼지고기 요리. 밥도둑 반찬의 대명사입니다.',
    '한식', 'medium', 25, 2, 520,
    '[
        {"step": 1, "text": "돼지 앞다리살 300g을 한입 크기로 썰어 준비합니다.", "image_url": null},
        {"step": 2, "text": "고추장 2큰술, 고춧가루 1큰술, 간장 1큰술, 설탕 1큰술, 다진 마늘 1큰술, 생강즙, 참기름을 섞어 양념장을 만듭니다.", "image_url": null},
        {"step": 3, "text": "돼지고기에 양념장을 넣고 20분 이상 재워둡니다.", "image_url": null},
        {"step": 4, "text": "달군 팬에 양파, 대파와 함께 센 불에서 빠르게 볶습니다.", "image_url": null},
        {"step": 5, "text": "고기가 익으면 깻잎을 찢어 넣고 한 번 더 볶아 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 20, "protein": 35, "fat": 28}'::jsonb,
    ARRAY['볶음', '돼지고기', '매운요리', '밥반찬', '도시락'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 4. 계란말이 (Korean Rolled Omelette)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '계란말이',
    '부드럽고 촉촉한 한국식 계란말이. 도시락 반찬으로도 최고입니다.',
    '한식', 'medium', 10, 2, 220,
    '[
        {"step": 1, "text": "계란 4개를 잘 풀고, 당근과 대파를 잘게 다져 넣습니다.", "image_url": null},
        {"step": 2, "text": "소금 약간, 설탕 한 꼬집을 넣고 고루 섞어줍니다.", "image_url": null},
        {"step": 3, "text": "약불로 달군 팬에 기름을 두르고 계란물을 1/3만 얇게 깔아줍니다.", "image_url": null},
        {"step": 4, "text": "반쯤 익으면 한쪽에서 돌돌 말아주고, 나머지 계란물을 부어 같은 방식으로 반복합니다.", "image_url": null},
        {"step": 5, "text": "김밥 말이로 모양을 잡고 한 김 식힌 후 먹기 좋게 잘라 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 3, "protein": 18, "fat": 15}'::jsonb,
    ARRAY['계란', '반찬', '도시락', '간단요리', '아이반찬'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 5. 김치찌개 (Kimchi Stew)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '김치찌개',
    '잘 익은 김치와 돼지고기로 끓인 얼큰한 찌개. 한국인의 소울푸드입니다.',
    '한식', 'easy', 30, 2, 350,
    '[
        {"step": 1, "text": "묵은지 1포기(200g)를 큼직하게 썰고, 돼지고기 앞다리살 150g을 한입 크기로 준비합니다.", "image_url": null},
        {"step": 2, "text": "냄비에 참기름을 두르고 돼지고기와 김치를 함께 3분간 볶습니다.", "image_url": null},
        {"step": 3, "text": "물 400ml를 붓고 고춧가루 1큰술, 다진 마늘 1큰술을 넣어 센 불에서 끓입니다.", "image_url": null},
        {"step": 4, "text": "끓어오르면 두부를 넣고 중불로 줄여 15분간 더 끓입니다.", "image_url": null},
        {"step": 5, "text": "대파를 송송 썰어 올리고 한소끔 끓여 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 12, "protein": 25, "fat": 18}'::jsonb,
    ARRAY['찌개', '김치', '돼지고기', '집밥', '매운요리'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 6. 불고기 (Bulgogi)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '불고기',
    '달콤한 간장 양념에 재운 소고기 불고기. 남녀노소 누구나 좋아하는 한식 대표 메뉴입니다.',
    '한식', 'medium', 35, 3, 480,
    '[
        {"step": 1, "text": "소고기 불고기용 400g을 준비하고, 배 1/2개를 갈아 즙을 냅니다.", "image_url": null},
        {"step": 2, "text": "간장 4큰술, 설탕 2큰술, 배즙, 다진 마늘 1큰술, 참기름 1큰술, 후추를 섞어 양념장을 만듭니다.", "image_url": null},
        {"step": 3, "text": "고기에 양념장을 넣고 잘 주물러 냉장고에서 1시간 이상 재웁니다.", "image_url": null},
        {"step": 4, "text": "양파, 버섯, 당근을 채 썰어 함께 센 불에서 볶습니다.", "image_url": null},
        {"step": 5, "text": "대파를 넣고 마지막에 참기름을 둘러 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 18, "protein": 38, "fat": 22}'::jsonb,
    ARRAY['소고기', '불고기', '양념육', '손님상', '명절'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 7. 잡채 (Japchae)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '잡채',
    '당면과 각종 채소, 고기를 볶아 만든 명절 대표 요리. 잔치에 빠질 수 없는 메뉴입니다.',
    '한식', 'medium', 40, 4, 380,
    '[
        {"step": 1, "text": "당면 200g을 끓는 물에 6분간 삶아 찬물에 헹군 후 참기름을 살짝 넣어 비벼둡니다.", "image_url": null},
        {"step": 2, "text": "시금치는 데쳐서 양념하고, 당근, 양파, 파프리카, 버섯은 채 썰어 각각 볶습니다.", "image_url": null},
        {"step": 3, "text": "소고기 채끝 150g을 채 썰어 간장, 설탕, 다진 마늘로 밑간한 후 볶습니다.", "image_url": null},
        {"step": 4, "text": "큰 볼에 당면과 모든 재료를 넣고 간장 3큰술, 설탕 1.5큰술, 참기름 2큰술을 넣어 고루 버무립니다.", "image_url": null},
        {"step": 5, "text": "접시에 담고 통깨를 뿌려 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 55, "protein": 15, "fat": 12}'::jsonb,
    ARRAY['당면', '명절요리', '잔치음식', '반찬', '채소'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 8. 비빔밥 (Bibimbap)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '비빔밥',
    '갖은 나물과 고추장을 넣어 비벼 먹는 한국의 대표 음식. 영양 균형이 뛰어납니다.',
    '한식', 'medium', 35, 1, 550,
    '[
        {"step": 1, "text": "시금치, 콩나물, 당근, 애호박을 각각 데치거나 볶아 나물을 준비합니다.", "image_url": null},
        {"step": 2, "text": "각 나물에 소금, 참기름, 다진 마늘로 양념합니다.", "image_url": null},
        {"step": 3, "text": "소고기 100g을 채 썰어 간장, 설탕, 참기름으로 밑간해 볶습니다.", "image_url": null},
        {"step": 4, "text": "따뜻한 밥 위에 나물과 고기를 색깔별로 예쁘게 올립니다.", "image_url": null},
        {"step": 5, "text": "가운데 계란 노른자를 올리고 고추장을 곁들여 쓱쓱 비벼 먹습니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 70, "protein": 22, "fat": 16}'::jsonb,
    ARRAY['비빔밥', '나물', '건강식', '한그릇', '대표한식'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 9. 떡볶이 (Tteokbokki)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '떡볶이',
    '매콤달콤한 고추장 양념의 국민 간식. 어묵과 함께 끓이면 더욱 맛있습니다.',
    '한식', 'easy', 20, 2, 420,
    '[
        {"step": 1, "text": "떡볶이떡 300g을 찬물에 10분간 불려 준비합니다.", "image_url": null},
        {"step": 2, "text": "멸치 육수 500ml에 고추장 2큰술, 고춧가루 1큰술, 간장 1큰술, 설탕 2큰술, 다진 마늘을 넣어 양념장을 풀어줍니다.", "image_url": null},
        {"step": 3, "text": "양념 육수가 끓으면 떡과 어묵을 넣고 중불에서 10분간 끓입니다.", "image_url": null},
        {"step": 4, "text": "대파를 넣고 국물이 자작하게 졸아들면 삶은 계란을 올려 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 72, "protein": 10, "fat": 5}'::jsonb,
    ARRAY['떡볶이', '간식', '매운요리', '분식', '야식'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 10. 순두부찌개 (Soft Tofu Stew)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '순두부찌개',
    '부드러운 순두부와 해물을 넣어 얼큰하게 끓인 찌개. 뚝배기에 보글보글 끓여야 제맛입니다.',
    '한식', 'easy', 20, 1, 280,
    '[
        {"step": 1, "text": "뚝배기에 참기름을 두르고 다진 마늘, 고춧가루 1큰술을 넣어 볶습니다.", "image_url": null},
        {"step": 2, "text": "물 300ml 또는 조개 육수를 붓고 끓입니다.", "image_url": null},
        {"step": 3, "text": "순두부 1봉을 넣고 새우, 바지락 등 해물을 추가합니다.", "image_url": null},
        {"step": 4, "text": "간장, 소금으로 간하고 대파를 넣어 5분간 더 끓입니다.", "image_url": null},
        {"step": 5, "text": "불을 끄기 직전 계란을 톡 깨넣어 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 10, "protein": 20, "fat": 14}'::jsonb,
    ARRAY['찌개', '순두부', '해물', '매운요리', '혼밥'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 11. 감자조림 (Braised Potatoes)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '감자조림',
    '간장 양념에 조린 감자는 밥반찬으로 최고. 윤기 나게 조려야 맛있습니다.',
    '한식', 'easy', 25, 3, 250,
    '[
        {"step": 1, "text": "감자 3개를 깍둑썰기하고 물에 담가 전분기를 빼줍니다.", "image_url": null},
        {"step": 2, "text": "냄비에 감자를 넣고 물 200ml, 간장 3큰술, 설탕 1.5큰술, 올리고당 1큰술을 넣습니다.", "image_url": null},
        {"step": 3, "text": "센 불에서 끓이다가 끓기 시작하면 중불로 줄여 10분간 조립니다.", "image_url": null},
        {"step": 4, "text": "국물이 자작해지면 참기름과 통깨를 넣어 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 45, "protein": 4, "fat": 3}'::jsonb,
    ARRAY['조림', '감자', '반찬', '밑반찬', '간단요리'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 12. 미역국 (Seaweed Soup)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '미역국',
    '생일에 꼭 먹는 한국의 전통 국. 소고기와 미역의 조합이 정말 좋습니다.',
    '한식', 'easy', 30, 2, 180,
    '[
        {"step": 1, "text": "건미역 20g을 물에 30분간 불린 후 적당한 크기로 잘라줍니다.", "image_url": null},
        {"step": 2, "text": "소고기 국거리 100g에 참기름, 다진 마늘을 넣고 볶다가 미역을 넣어 함께 볶습니다.", "image_url": null},
        {"step": 3, "text": "물 800ml를 붓고 센 불에서 끓입니다.", "image_url": null},
        {"step": 4, "text": "끓어오르면 중불로 줄이고 국간장으로 간하며 20분간 더 끓여 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 5, "protein": 15, "fat": 8}'::jsonb,
    ARRAY['국', '미역', '생일', '소고기', '건강식'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 13. 소고기 미역국과 차별화 - 닭볶음탕 (Braised Spicy Chicken)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '닭볶음탕',
    '매콤한 양념에 감자, 당근과 함께 조린 닭 요리. 온 가족이 즐기는 든든한 한 끼입니다.',
    '한식', 'medium', 45, 3, 520,
    '[
        {"step": 1, "text": "닭 한 마리를 토막 내고 찬물에 30분간 담가 핏물을 빼줍니다.", "image_url": null},
        {"step": 2, "text": "감자 2개, 당근 1개, 양파 1개를 큼직하게 썰어 준비합니다.", "image_url": null},
        {"step": 3, "text": "고추장 2큰술, 고춧가루 2큰술, 간장 3큰술, 설탕 1큰술, 다진 마늘 1큰술, 생강즙으로 양념장을 만듭니다.", "image_url": null},
        {"step": 4, "text": "냄비에 닭과 채소를 넣고 양념장과 물 500ml를 부어 센 불에서 끓입니다.", "image_url": null},
        {"step": 5, "text": "끓어오르면 중불로 줄이고 뚜껑을 덮어 25분간 끓입니다.", "image_url": null},
        {"step": 6, "text": "대파와 청양고추를 넣고 5분 더 끓여 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 25, "protein": 40, "fat": 20}'::jsonb,
    ARRAY['닭', '조림', '매운요리', '가족식사', '든든한'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 14. 두부조림 (Braised Tofu)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '두부조림',
    '양념장에 조린 두부는 밥 반찬으로 최고. 고소하면서도 매콤한 맛이 일품입니다.',
    '한식', 'easy', 15, 2, 200,
    '[
        {"step": 1, "text": "두부 1모를 1cm 두께로 썰어 키친타월로 물기를 제거합니다.", "image_url": null},
        {"step": 2, "text": "팬에 식용유를 두르고 두부를 앞뒤로 노릇하게 구워줍니다.", "image_url": null},
        {"step": 3, "text": "간장 2큰술, 고춧가루 1큰술, 설탕 0.5큰술, 다진 마늘, 대파, 물 3큰술을 섞어 양념장을 만듭니다.", "image_url": null},
        {"step": 4, "text": "구운 두부 위에 양념장을 골고루 끼얹고 중불에서 3분간 조려 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 8, "protein": 16, "fat": 12}'::jsonb,
    ARRAY['두부', '조림', '반찬', '간단요리', '밑반찬'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 15. 콩나물국 (Bean Sprout Soup)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '콩나물국',
    '시원하고 깔끔한 콩나물국. 해장에도 좋고, 밥과 함께 먹으면 든든합니다.',
    '한식', 'easy', 15, 2, 80,
    '[
        {"step": 1, "text": "콩나물 200g을 깨끗이 씻어 준비합니다.", "image_url": null},
        {"step": 2, "text": "냄비에 물 600ml를 붓고 콩나물을 넣어 뚜껑을 꼭 덮고 센 불에서 끓입니다. 중간에 뚜껑을 열면 비린내가 날 수 있으니 주의합니다.", "image_url": null},
        {"step": 3, "text": "끓어오르면 국간장 1큰술, 소금, 다진 마늘 0.5큰술을 넣고 5분 더 끓입니다.", "image_url": null},
        {"step": 4, "text": "대파를 송송 썰어 넣고 한소끔 끓여 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 6, "protein": 5, "fat": 1}'::jsonb,
    ARRAY['국', '콩나물', '해장', '간단요리', '건강식'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 16. 오징어볶음 (Stir-fried Squid)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '오징어볶음',
    '매콤한 양념에 볶은 오징어와 채소. 쫄깃한 식감에 매콤달콤한 맛이 중독성 있습니다.',
    '한식', 'medium', 20, 2, 280,
    '[
        {"step": 1, "text": "오징어 1마리를 손질하여 몸통은 링 모양으로, 다리는 적당한 길이로 자릅니다.", "image_url": null},
        {"step": 2, "text": "양파 1개, 당근 1/2개, 양배추 2장을 한입 크기로 썰고, 대파는 어슷 썰어 준비합니다.", "image_url": null},
        {"step": 3, "text": "고추장 1.5큰술, 고춧가루 1큰술, 간장 1큰술, 설탕 1큰술, 다진 마늘 1큰술, 참기름을 섞어 양념장을 만듭니다.", "image_url": null},
        {"step": 4, "text": "센 불에서 채소를 먼저 볶다가 오징어와 양념장을 넣고 재빨리 볶습니다.", "image_url": null},
        {"step": 5, "text": "오징어가 익으면 통깨를 뿌려 완성합니다. 너무 오래 볶으면 질겨지니 주의합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 18, "protein": 25, "fat": 8}'::jsonb,
    ARRAY['볶음', '오징어', '해물', '매운요리', '밥반찬'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 17. 부대찌개 (Army Base Stew)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '부대찌개',
    '햄, 소시지, 김치, 두부 등 다양한 재료를 넣어 끓인 얼큰한 찌개. 라면 사리를 넣으면 더 맛있습니다.',
    '한식', 'easy', 25, 2, 550,
    '[
        {"step": 1, "text": "햄, 소시지를 한입 크기로 썰고, 김치, 두부, 양파, 대파를 준비합니다.", "image_url": null},
        {"step": 2, "text": "넓은 냄비에 김치, 햄, 소시지, 두부, 양파를 가지런히 담습니다.", "image_url": null},
        {"step": 3, "text": "고추장 1큰술, 고춧가루 1큰술, 다진 마늘 1큰술, 간장 1큰술을 물 600ml에 풀어 부어줍니다.", "image_url": null},
        {"step": 4, "text": "센 불에서 끓이다가 끓어오르면 중불로 줄여 10분간 끓입니다.", "image_url": null},
        {"step": 5, "text": "라면 사리를 넣고 3분 더 끓인 후 대파를 올려 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 40, "protein": 22, "fat": 30}'::jsonb,
    ARRAY['찌개', '부대찌개', '라면', '매운요리', '야식'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 18. 멸치볶음 (Stir-fried Anchovies)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '멸치볶음',
    '바삭하고 달콤짭짤한 멸치볶음. 도시락 반찬, 밑반찬의 기본입니다.',
    '한식', 'easy', 10, 4, 160,
    '[
        {"step": 1, "text": "마른 멸치 100g을 준비하고 머리와 내장을 떼어냅니다.", "image_url": null},
        {"step": 2, "text": "마른 팬에 멸치를 넣고 약불에서 3분간 바삭하게 볶아 비린내를 날려줍니다.", "image_url": null},
        {"step": 3, "text": "간장 1큰술, 올리고당 2큰술, 설탕 0.5큰술, 다진 마늘을 넣고 약불에서 고루 볶습니다.", "image_url": null},
        {"step": 4, "text": "참기름과 통깨를 넣어 마무리하고 완전히 식혀 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 12, "protein": 18, "fat": 5}'::jsonb,
    ARRAY['멸치', '밑반찬', '볶음', '도시락', '간단요리'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 19. 동태찌개 (Pollack Stew)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '동태찌개',
    '시원한 국물이 일품인 동태찌개. 추운 겨울에 먹으면 속이 확 풀립니다.',
    '한식', 'medium', 30, 2, 250,
    '[
        {"step": 1, "text": "동태 1마리를 토막 내고 소금, 후추로 밑간합니다.", "image_url": null},
        {"step": 2, "text": "무를 나박썰기하고, 두부, 애호박, 대파, 청양고추를 썰어 준비합니다.", "image_url": null},
        {"step": 3, "text": "냄비에 물 800ml와 무를 넣고 끓입니다.", "image_url": null},
        {"step": 4, "text": "무가 반투명해지면 고춧가루 1.5큰술, 다진 마늘 1큰술, 국간장 1큰술을 넣고 동태를 넣습니다.", "image_url": null},
        {"step": 5, "text": "두부, 애호박을 넣고 10분간 끓인 후 대파, 청양고추를 올려 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 10, "protein": 30, "fat": 8}'::jsonb,
    ARRAY['찌개', '생선', '시원한국물', '겨울요리', '해물'],
    'manual'
) ON CONFLICT DO NOTHING;

-- 20. 갈비찜 (Braised Short Ribs)
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags, source_type)
VALUES (
    '갈비찜',
    '간장 양념에 푹 조린 소갈비와 채소. 명절과 특별한 날에 빠지지 않는 한식 대표 요리입니다.',
    '한식', 'hard', 90, 4, 650,
    '[
        {"step": 1, "text": "소갈비 1kg을 찬물에 2시간 이상 담가 핏물을 빼줍니다.", "image_url": null},
        {"step": 2, "text": "끓는 물에 갈비를 넣어 5분간 데친 후 찬물에 헹궈 기름기를 제거합니다.", "image_url": null},
        {"step": 3, "text": "간장 6큰술, 설탕 3큰술, 배즙 4큰술, 다진 마늘 2큰술, 참기름 1큰술, 후추를 섞어 양념장을 만듭니다.", "image_url": null},
        {"step": 4, "text": "갈비와 양념장을 냄비에 넣고 물 500ml를 부어 센 불에서 끓입니다.", "image_url": null},
        {"step": 5, "text": "끓어오르면 중약불로 줄여 40분간 조린 후, 감자, 당근, 밤, 은행을 넣고 20분 더 조립니다.", "image_url": null},
        {"step": 6, "text": "국물이 자작해지면 대추와 잣을 올려 완성합니다.", "image_url": null}
    ]'::jsonb,
    '{"carbs": 25, "protein": 45, "fat": 35}'::jsonb,
    ARRAY['갈비', '소고기', '명절요리', '특별한날', '조림'],
    'manual'
) ON CONFLICT DO NOTHING;


-- ============================================================
-- recipe_ingredients: Reference ingredient_master by name subquery
-- Uses (SELECT id FROM recipes WHERE title = '...') for recipe_id
-- Uses (SELECT id FROM ingredient_master WHERE name = '...') for ingredient_id
-- ============================================================

-- ---- 1. 김치볶음밥 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '김치볶음밥'), (SELECT id FROM ingredient_master WHERE name = '김치'), '200g', TRUE),
    ((SELECT id FROM recipes WHERE title = '김치볶음밥'), (SELECT id FROM ingredient_master WHERE name = '밥'), '1공기', TRUE),
    ((SELECT id FROM recipes WHERE title = '김치볶음밥'), (SELECT id FROM ingredient_master WHERE name = '계란'), '1개', FALSE),
    ((SELECT id FROM recipes WHERE title = '김치볶음밥'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1/2대', FALSE),
    ((SELECT id FROM recipes WHERE title = '김치볶음밥'), (SELECT id FROM ingredient_master WHERE name = '고추장'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '김치볶음밥'), (SELECT id FROM ingredient_master WHERE name = '참기름'), '1큰술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 2. 된장찌개 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '된장찌개'), (SELECT id FROM ingredient_master WHERE name = '된장'), '2큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '된장찌개'), (SELECT id FROM ingredient_master WHERE name = '두부'), '1/2모', TRUE),
    ((SELECT id FROM recipes WHERE title = '된장찌개'), (SELECT id FROM ingredient_master WHERE name = '감자'), '1개', FALSE),
    ((SELECT id FROM recipes WHERE title = '된장찌개'), (SELECT id FROM ingredient_master WHERE name = '양파'), '1/2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '된장찌개'), (SELECT id FROM ingredient_master WHERE name = '애호박'), '1/3개', FALSE),
    ((SELECT id FROM recipes WHERE title = '된장찌개'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1/2대', FALSE),
    ((SELECT id FROM recipes WHERE title = '된장찌개'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1작은술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 3. 제육볶음 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '제육볶음'), (SELECT id FROM ingredient_master WHERE name = '돼지고기'), '300g', TRUE),
    ((SELECT id FROM recipes WHERE title = '제육볶음'), (SELECT id FROM ingredient_master WHERE name = '고추장'), '2큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '제육볶음'), (SELECT id FROM ingredient_master WHERE name = '고춧가루'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '제육볶음'), (SELECT id FROM ingredient_master WHERE name = '간장'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '제육볶음'), (SELECT id FROM ingredient_master WHERE name = '양파'), '1개', FALSE),
    ((SELECT id FROM recipes WHERE title = '제육볶음'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1대', FALSE),
    ((SELECT id FROM recipes WHERE title = '제육볶음'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '제육볶음'), (SELECT id FROM ingredient_master WHERE name = '깻잎'), '5장', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 4. 계란말이 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '계란말이'), (SELECT id FROM ingredient_master WHERE name = '계란'), '4개', TRUE),
    ((SELECT id FROM recipes WHERE title = '계란말이'), (SELECT id FROM ingredient_master WHERE name = '당근'), '1/4개', FALSE),
    ((SELECT id FROM recipes WHERE title = '계란말이'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1/2대', FALSE),
    ((SELECT id FROM recipes WHERE title = '계란말이'), (SELECT id FROM ingredient_master WHERE name = '소금'), '약간', TRUE)
ON CONFLICT DO NOTHING;

-- ---- 5. 김치찌개 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '김치찌개'), (SELECT id FROM ingredient_master WHERE name = '김치'), '200g', TRUE),
    ((SELECT id FROM recipes WHERE title = '김치찌개'), (SELECT id FROM ingredient_master WHERE name = '돼지고기'), '150g', TRUE),
    ((SELECT id FROM recipes WHERE title = '김치찌개'), (SELECT id FROM ingredient_master WHERE name = '두부'), '1/2모', FALSE),
    ((SELECT id FROM recipes WHERE title = '김치찌개'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1대', FALSE),
    ((SELECT id FROM recipes WHERE title = '김치찌개'), (SELECT id FROM ingredient_master WHERE name = '고춧가루'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '김치찌개'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '김치찌개'), (SELECT id FROM ingredient_master WHERE name = '참기름'), '1큰술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 6. 불고기 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '소고기'), '400g', TRUE),
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '간장'), '4큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '설탕'), '2큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '배'), '1/2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '양파'), '1개', FALSE),
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '버섯'), '100g', FALSE),
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '당근'), '1/2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1대', FALSE),
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '불고기'), (SELECT id FROM ingredient_master WHERE name = '참기름'), '1큰술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 7. 잡채 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '잡채'), (SELECT id FROM ingredient_master WHERE name = '당면'), '200g', TRUE),
    ((SELECT id FROM recipes WHERE title = '잡채'), (SELECT id FROM ingredient_master WHERE name = '소고기'), '150g', FALSE),
    ((SELECT id FROM recipes WHERE title = '잡채'), (SELECT id FROM ingredient_master WHERE name = '시금치'), '100g', FALSE),
    ((SELECT id FROM recipes WHERE title = '잡채'), (SELECT id FROM ingredient_master WHERE name = '당근'), '1/2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '잡채'), (SELECT id FROM ingredient_master WHERE name = '양파'), '1/2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '잡채'), (SELECT id FROM ingredient_master WHERE name = '버섯'), '100g', FALSE),
    ((SELECT id FROM recipes WHERE title = '잡채'), (SELECT id FROM ingredient_master WHERE name = '간장'), '3큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '잡채'), (SELECT id FROM ingredient_master WHERE name = '설탕'), '1.5큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '잡채'), (SELECT id FROM ingredient_master WHERE name = '참기름'), '2큰술', TRUE)
ON CONFLICT DO NOTHING;

-- ---- 8. 비빔밥 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '비빔밥'), (SELECT id FROM ingredient_master WHERE name = '밥'), '1공기', TRUE),
    ((SELECT id FROM recipes WHERE title = '비빔밥'), (SELECT id FROM ingredient_master WHERE name = '고추장'), '2큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '비빔밥'), (SELECT id FROM ingredient_master WHERE name = '시금치'), '100g', FALSE),
    ((SELECT id FROM recipes WHERE title = '비빔밥'), (SELECT id FROM ingredient_master WHERE name = '콩나물'), '100g', FALSE),
    ((SELECT id FROM recipes WHERE title = '비빔밥'), (SELECT id FROM ingredient_master WHERE name = '당근'), '1/2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '비빔밥'), (SELECT id FROM ingredient_master WHERE name = '애호박'), '1/3개', FALSE),
    ((SELECT id FROM recipes WHERE title = '비빔밥'), (SELECT id FROM ingredient_master WHERE name = '소고기'), '100g', FALSE),
    ((SELECT id FROM recipes WHERE title = '비빔밥'), (SELECT id FROM ingredient_master WHERE name = '계란'), '1개', FALSE),
    ((SELECT id FROM recipes WHERE title = '비빔밥'), (SELECT id FROM ingredient_master WHERE name = '참기름'), '1큰술', TRUE)
ON CONFLICT DO NOTHING;

-- ---- 9. 떡볶이 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '떡볶이'), (SELECT id FROM ingredient_master WHERE name = '떡'), '300g', TRUE),
    ((SELECT id FROM recipes WHERE title = '떡볶이'), (SELECT id FROM ingredient_master WHERE name = '어묵'), '200g', FALSE),
    ((SELECT id FROM recipes WHERE title = '떡볶이'), (SELECT id FROM ingredient_master WHERE name = '고추장'), '2큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '떡볶이'), (SELECT id FROM ingredient_master WHERE name = '고춧가루'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '떡볶이'), (SELECT id FROM ingredient_master WHERE name = '간장'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '떡볶이'), (SELECT id FROM ingredient_master WHERE name = '설탕'), '2큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '떡볶이'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1대', FALSE),
    ((SELECT id FROM recipes WHERE title = '떡볶이'), (SELECT id FROM ingredient_master WHERE name = '계란'), '2개', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 10. 순두부찌개 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '순두부찌개'), (SELECT id FROM ingredient_master WHERE name = '순두부'), '1봉', TRUE),
    ((SELECT id FROM recipes WHERE title = '순두부찌개'), (SELECT id FROM ingredient_master WHERE name = '계란'), '1개', FALSE),
    ((SELECT id FROM recipes WHERE title = '순두부찌개'), (SELECT id FROM ingredient_master WHERE name = '새우'), '50g', FALSE),
    ((SELECT id FROM recipes WHERE title = '순두부찌개'), (SELECT id FROM ingredient_master WHERE name = '바지락'), '100g', FALSE),
    ((SELECT id FROM recipes WHERE title = '순두부찌개'), (SELECT id FROM ingredient_master WHERE name = '고춧가루'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '순두부찌개'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1/2대', FALSE),
    ((SELECT id FROM recipes WHERE title = '순두부찌개'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1작은술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 11. 감자조림 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '감자조림'), (SELECT id FROM ingredient_master WHERE name = '감자'), '3개', TRUE),
    ((SELECT id FROM recipes WHERE title = '감자조림'), (SELECT id FROM ingredient_master WHERE name = '간장'), '3큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '감자조림'), (SELECT id FROM ingredient_master WHERE name = '설탕'), '1.5큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '감자조림'), (SELECT id FROM ingredient_master WHERE name = '참기름'), '1큰술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 12. 미역국 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '미역국'), (SELECT id FROM ingredient_master WHERE name = '미역'), '20g(건)', TRUE),
    ((SELECT id FROM recipes WHERE title = '미역국'), (SELECT id FROM ingredient_master WHERE name = '소고기'), '100g', TRUE),
    ((SELECT id FROM recipes WHERE title = '미역국'), (SELECT id FROM ingredient_master WHERE name = '참기름'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '미역국'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '미역국'), (SELECT id FROM ingredient_master WHERE name = '간장'), '1큰술', TRUE)
ON CONFLICT DO NOTHING;

-- ---- 13. 닭볶음탕 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '닭볶음탕'), (SELECT id FROM ingredient_master WHERE name = '닭'), '1마리', TRUE),
    ((SELECT id FROM recipes WHERE title = '닭볶음탕'), (SELECT id FROM ingredient_master WHERE name = '감자'), '2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '닭볶음탕'), (SELECT id FROM ingredient_master WHERE name = '당근'), '1개', FALSE),
    ((SELECT id FROM recipes WHERE title = '닭볶음탕'), (SELECT id FROM ingredient_master WHERE name = '양파'), '1개', FALSE),
    ((SELECT id FROM recipes WHERE title = '닭볶음탕'), (SELECT id FROM ingredient_master WHERE name = '고추장'), '2큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '닭볶음탕'), (SELECT id FROM ingredient_master WHERE name = '고춧가루'), '2큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '닭볶음탕'), (SELECT id FROM ingredient_master WHERE name = '간장'), '3큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '닭볶음탕'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '닭볶음탕'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1대', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 14. 두부조림 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '두부조림'), (SELECT id FROM ingredient_master WHERE name = '두부'), '1모', TRUE),
    ((SELECT id FROM recipes WHERE title = '두부조림'), (SELECT id FROM ingredient_master WHERE name = '간장'), '2큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '두부조림'), (SELECT id FROM ingredient_master WHERE name = '고춧가루'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '두부조림'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1/2대', FALSE),
    ((SELECT id FROM recipes WHERE title = '두부조림'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1작은술', FALSE),
    ((SELECT id FROM recipes WHERE title = '두부조림'), (SELECT id FROM ingredient_master WHERE name = '설탕'), '0.5큰술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 15. 콩나물국 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '콩나물국'), (SELECT id FROM ingredient_master WHERE name = '콩나물'), '200g', TRUE),
    ((SELECT id FROM recipes WHERE title = '콩나물국'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1/2대', FALSE),
    ((SELECT id FROM recipes WHERE title = '콩나물국'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '0.5큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '콩나물국'), (SELECT id FROM ingredient_master WHERE name = '간장'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '콩나물국'), (SELECT id FROM ingredient_master WHERE name = '소금'), '약간', TRUE)
ON CONFLICT DO NOTHING;

-- ---- 16. 오징어볶음 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '오징어볶음'), (SELECT id FROM ingredient_master WHERE name = '오징어'), '1마리', TRUE),
    ((SELECT id FROM recipes WHERE title = '오징어볶음'), (SELECT id FROM ingredient_master WHERE name = '양파'), '1개', FALSE),
    ((SELECT id FROM recipes WHERE title = '오징어볶음'), (SELECT id FROM ingredient_master WHERE name = '당근'), '1/2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '오징어볶음'), (SELECT id FROM ingredient_master WHERE name = '양배추'), '2장', FALSE),
    ((SELECT id FROM recipes WHERE title = '오징어볶음'), (SELECT id FROM ingredient_master WHERE name = '고추장'), '1.5큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '오징어볶음'), (SELECT id FROM ingredient_master WHERE name = '고춧가루'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '오징어볶음'), (SELECT id FROM ingredient_master WHERE name = '간장'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '오징어볶음'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1큰술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 17. 부대찌개 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '부대찌개'), (SELECT id FROM ingredient_master WHERE name = '김치'), '150g', TRUE),
    ((SELECT id FROM recipes WHERE title = '부대찌개'), (SELECT id FROM ingredient_master WHERE name = '두부'), '1/2모', FALSE),
    ((SELECT id FROM recipes WHERE title = '부대찌개'), (SELECT id FROM ingredient_master WHERE name = '양파'), '1/2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '부대찌개'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1대', FALSE),
    ((SELECT id FROM recipes WHERE title = '부대찌개'), (SELECT id FROM ingredient_master WHERE name = '라면'), '1봉', FALSE),
    ((SELECT id FROM recipes WHERE title = '부대찌개'), (SELECT id FROM ingredient_master WHERE name = '고추장'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '부대찌개'), (SELECT id FROM ingredient_master WHERE name = '고춧가루'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '부대찌개'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1큰술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 18. 멸치볶음 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '멸치볶음'), (SELECT id FROM ingredient_master WHERE name = '멸치'), '100g', TRUE),
    ((SELECT id FROM recipes WHERE title = '멸치볶음'), (SELECT id FROM ingredient_master WHERE name = '간장'), '1큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '멸치볶음'), (SELECT id FROM ingredient_master WHERE name = '설탕'), '0.5큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '멸치볶음'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '약간', FALSE),
    ((SELECT id FROM recipes WHERE title = '멸치볶음'), (SELECT id FROM ingredient_master WHERE name = '참기름'), '1큰술', FALSE)
ON CONFLICT DO NOTHING;

-- ---- 19. 동태찌개 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '동태찌개'), (SELECT id FROM ingredient_master WHERE name = '동태'), '1마리', TRUE),
    ((SELECT id FROM recipes WHERE title = '동태찌개'), (SELECT id FROM ingredient_master WHERE name = '무'), '1/4개', TRUE),
    ((SELECT id FROM recipes WHERE title = '동태찌개'), (SELECT id FROM ingredient_master WHERE name = '두부'), '1/2모', FALSE),
    ((SELECT id FROM recipes WHERE title = '동태찌개'), (SELECT id FROM ingredient_master WHERE name = '애호박'), '1/3개', FALSE),
    ((SELECT id FROM recipes WHERE title = '동태찌개'), (SELECT id FROM ingredient_master WHERE name = '대파'), '1대', FALSE),
    ((SELECT id FROM recipes WHERE title = '동태찌개'), (SELECT id FROM ingredient_master WHERE name = '고춧가루'), '1.5큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '동태찌개'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '동태찌개'), (SELECT id FROM ingredient_master WHERE name = '간장'), '1큰술', TRUE)
ON CONFLICT DO NOTHING;

-- ---- 20. 갈비찜 ----
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential)
VALUES
    ((SELECT id FROM recipes WHERE title = '갈비찜'), (SELECT id FROM ingredient_master WHERE name = '소갈비'), '1kg', TRUE),
    ((SELECT id FROM recipes WHERE title = '갈비찜'), (SELECT id FROM ingredient_master WHERE name = '간장'), '6큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '갈비찜'), (SELECT id FROM ingredient_master WHERE name = '설탕'), '3큰술', TRUE),
    ((SELECT id FROM recipes WHERE title = '갈비찜'), (SELECT id FROM ingredient_master WHERE name = '배'), '1/2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '갈비찜'), (SELECT id FROM ingredient_master WHERE name = '마늘'), '2큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '갈비찜'), (SELECT id FROM ingredient_master WHERE name = '참기름'), '1큰술', FALSE),
    ((SELECT id FROM recipes WHERE title = '갈비찜'), (SELECT id FROM ingredient_master WHERE name = '감자'), '2개', FALSE),
    ((SELECT id FROM recipes WHERE title = '갈비찜'), (SELECT id FROM ingredient_master WHERE name = '당근'), '1개', FALSE)
ON CONFLICT DO NOTHING;
