-- 레시피-재료 매핑 시드 데이터
-- quantity는 VARCHAR(50)이므로 "2큰술", "0.5모" 등 단위 포함 문자열로 저장

-- =====================
-- 된장찌개
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('된장',     '2큰술',  true),
    ('두부',     '0.5모',  true),
    ('애호박',   '0.3개',  false),
    ('감자',     '1개',    false),
    ('양파',     '0.5개',  false),
    ('대파',     '0.3개',  false),
    ('마늘',     '3개',    false),
    ('청양고추', '1개',    false),
    ('멸치',     '10g',    false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '된장찌개'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 김치찌개
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential,
    CASE m.iname
        WHEN '삼겹살' THEN ARRAY(SELECT id FROM ingredient_master WHERE name = '돼지고기')
        ELSE ARRAY[]::BIGINT[]
    END
FROM recipes r
JOIN (VALUES
    ('삼겹살',   '150g',   true),
    ('두부',     '0.5모',  false),
    ('대파',     '0.5개',  false),
    ('마늘',     '3개',    false),
    ('고추장',   '1작은술', false),
    ('고춧가루', '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '김치찌개'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 부대찌개
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('두부',     '0.5모',  false),
    ('라면',     '1개',    false),
    ('치즈',     '1장',    false),
    ('양파',     '0.5개',  false),
    ('대파',     '0.5개',  false),
    ('고추장',   '1큰술',  true),
    ('고춧가루', '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '부대찌개'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 순두부찌개
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential,
    CASE m.iname
        WHEN '조개' THEN ARRAY(SELECT id FROM ingredient_master WHERE name = '새우')
        ELSE ARRAY[]::BIGINT[]
    END
FROM recipes r
JOIN (VALUES
    ('순두부',   '1봉',    true),
    ('조개',     '100g',   false),
    ('달걀',     '1개',    false),
    ('대파',     '0.3개',  false),
    ('마늘',     '3개',    false),
    ('고춧가루', '1큰술',  true),
    ('참기름',   '1작은술', false),
    ('간장',     '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '순두부찌개'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 소고기미역국
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('소고기',   '150g',   true),
    ('참기름',   '1큰술',  false),
    ('간장',     '1큰술',  false),
    ('마늘',     '3개',    false),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '소고기미역국'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 된장국
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('된장',     '1.5큰술', true),
    ('두부',     '0.5모',   false),
    ('애호박',   '0.3개',   false),
    ('대파',     '0.3개',   false),
    ('마늘',     '2개',     false),
    ('청양고추', '1개',     false),
    ('멸치',     '10g',     false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '된장국'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 콩나물국
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('콩나물',   '1봉',    true),
    ('대파',     '0.3개',  false),
    ('마늘',     '2개',    false),
    ('간장',     '1큰술',  false),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '콩나물국'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 닭볶음탕
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('닭고기',   '600g',   true),
    ('감자',     '2개',    false),
    ('양파',     '1개',    false),
    ('당근',     '1개',    false),
    ('대파',     '1개',    false),
    ('마늘',     '5개',    true),
    ('고추장',   '2큰술',  true),
    ('고춧가루', '2큰술',  true),
    ('간장',     '3큰술',  true),
    ('설탕',     '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '닭볶음탕'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 갈비탕
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('소갈비',   '600g',   true),
    ('대파',     '1개',    false),
    ('마늘',     '5개',    false),
    ('소금',     '약간',   false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '갈비탕'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 어묵탕
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('무',       '200g',   false),
    ('대파',     '1개',    false),
    ('멸치',     '20g',    false),
    ('간장',     '1큰술',  false),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '어묵탕'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 불고기
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('소고기',   '300g',   true),
    ('양파',     '0.5개',  false),
    ('대파',     '0.5개',  false),
    ('마늘',     '4개',    false),
    ('간장',     '3큰술',  true),
    ('설탕',     '1큰술',  false),
    ('참기름',   '1큰술',  false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '불고기'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 제육볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('돼지목살', '300g',   true),
    ('양파',     '0.5개',  false),
    ('대파',     '0.5개',  false),
    ('마늘',     '4개',    true),
    ('고추장',   '2큰술',  true),
    ('고춧가루', '1큰술',  true),
    ('간장',     '1큰술',  false),
    ('설탕',     '1큰술',  false),
    ('참기름',   '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '제육볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 삼겹살구이
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('삼겹살',   '300g',   true),
    ('상추',     '1봉',    false),
    ('깻잎',     '1봉',    false),
    ('마늘',     '10개',   false),
    ('된장',     '2큰술',  false),
    ('소금',     '약간',   false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '삼겹살구이'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 닭갈비
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('닭고기',   '500g',   true),
    ('고구마',   '1개',    false),
    ('양배추',   '0.25개', false),
    ('양파',     '1개',    false),
    ('떡',       '100g',   false),
    ('마늘',     '4개',    false),
    ('고추장',   '2큰술',  true),
    ('고춧가루', '1큰술',  true),
    ('간장',     '2큰술',  true),
    ('설탕',     '1큰술',  false),
    ('미림',     '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '닭갈비'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 멸치볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('멸치',     '100g',   true),
    ('간장',     '1큰술',  false),
    ('설탕',     '0.5큰술', false),
    ('미림',     '1큰술',  false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '멸치볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 어묵볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('대파',     '0.5개',  false),
    ('간장',     '1큰술',  false),
    ('설탕',     '0.5큰술', false),
    ('고춧가루', '0.5큰술', false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '어묵볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 고등어구이
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential,
    CASE m.iname
        WHEN '고등어' THEN ARRAY(SELECT id FROM ingredient_master WHERE name = '삼치')
        ELSE ARRAY[]::BIGINT[]
    END
FROM recipes r
JOIN (VALUES
    ('고등어',   '1마리',  true),
    ('소금',     '약간',   false),
    ('레몬',     '0.5개',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '고등어구이'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 새우볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('새우',     '200g',   true),
    ('버터',     '1큰술',  false),
    ('마늘',     '4개',    true),
    ('대파',     '0.3개',  false),
    ('간장',     '1큰술',  false),
    ('굴소스',   '1큰술',  false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '새우볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 김치볶음밥
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('쌀',       '1공기',  true),
    ('달걀',     '1개',    false),
    ('대파',     '0.3개',  false),
    ('간장',     '1큰술',  false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '김치볶음밥'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 비빔밥
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('쌀',       '1공기',  true),
    ('소고기',   '100g',   false),
    ('시금치',   '0.5봉',  false),
    ('콩나물',   '0.5봉',  false),
    ('당근',     '0.5개',  false),
    ('달걀',     '1개',    false),
    ('표고버섯', '100g',   false),
    ('고추장',   '2큰술',  true),
    ('참기름',   '1큰술',  false),
    ('간장',     '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '비빔밥'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 카레라이스
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential,
    CASE m.iname
        WHEN '돼지고기' THEN ARRAY(SELECT id FROM ingredient_master WHERE name = '닭고기')
        ELSE ARRAY[]::BIGINT[]
    END
FROM recipes r
JOIN (VALUES
    ('쌀',       '1공기',  true),
    ('돼지고기', '200g',   false),
    ('감자',     '2개',    false),
    ('당근',     '1개',    false),
    ('양파',     '1개',    true)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '카레라이스'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 잡채
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('소고기',   '150g',   false),
    ('시금치',   '0.5봉',  false),
    ('당근',     '0.5개',  false),
    ('표고버섯', '100g',   false),
    ('양파',     '0.5개',  false),
    ('간장',     '3큰술',  true),
    ('설탕',     '1큰술',  false),
    ('참기름',   '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '잡채'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 떡볶이
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('떡',       '300g',   true),
    ('대파',     '0.5개',  false),
    ('고추장',   '2큰술',  true),
    ('고춧가루', '1큰술',  false),
    ('설탕',     '1큰술',  false),
    ('간장',     '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '떡볶이'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 비빔국수
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('국수',     '200g',   true),
    ('오이',     '0.5개',  false),
    ('달걀',     '1개',    false),
    ('고추장',   '2큰술',  true),
    ('식초',     '1큰술',  false),
    ('설탕',     '1큰술',  false),
    ('간장',     '0.5큰술', false),
    ('참기름',   '1작은술', false),
    ('마늘',     '2개',    false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '비빔국수'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 시금치나물
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('시금치',   '1봉',    true),
    ('간장',     '1큰술',  false),
    ('마늘',     '2개',    false),
    ('참기름',   '1큰술',  false),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '시금치나물'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 오이무침
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('오이',     '2개',    true),
    ('고추장',   '0.5큰술', false),
    ('식초',     '1큰술',  false),
    ('설탕',     '0.5큰술', false),
    ('마늘',     '2개',    false),
    ('참기름',   '1작은술', false),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '오이무침'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 감자조림
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('감자',     '3개',    true),
    ('간장',     '2큰술',  true),
    ('설탕',     '1큰술',  false),
    ('미림',     '1큰술',  false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '감자조림'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 계란찜
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('달걀',     '3개',    true),
    ('대파',     '0.2개',  false),
    ('멸치',     '10g',    false),
    ('참기름',   '0.5작은술', false),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '계란찜'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 계란말이
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('달걀',     '3개',    true),
    ('당근',     '0.3개',  false),
    ('대파',     '0.3개',  false),
    ('파프리카', '0.3개',  false),
    ('소금',     '약간',   false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '계란말이'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 깻잎무침
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('깻잎',     '1봉',    true),
    ('간장',     '2큰술',  true),
    ('고춧가루', '1큰술',  false),
    ('설탕',     '0.5큰술', false),
    ('마늘',     '2개',    false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '깻잎무침'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 김치전
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('밀가루',   '1컵',    true),
    ('달걀',     '1개',    false),
    ('대파',     '0.5개',  false),
    ('고춧가루', '0.5큰술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '김치전'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 감자전
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('감자',     '3개',    true),
    ('소금',     '약간',   false),
    ('간장',     '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '감자전'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 해물파전
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('대파',     '3개',    true),
    ('새우',     '100g',   false),
    ('오징어',   '0.5마리', false),
    ('달걀',     '1개',    false),
    ('밀가루',   '1컵',    true),
    ('간장',     '1큰술',  false),
    ('식초',     '0.5큰술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '해물파전'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 두부전
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('두부',     '1모',    true),
    ('달걀',     '1개',    true),
    ('밀가루',   '3큰술',  false),
    ('소금',     '약간',   false),
    ('간장',     '1큰술',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '두부전'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 크림파스타
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential,
    CASE m.iname
        WHEN '닭가슴살' THEN ARRAY(SELECT id FROM ingredient_master WHERE name = '삼겹살')
        ELSE ARRAY[]::BIGINT[]
    END
FROM recipes r
JOIN (VALUES
    ('스파게티', '160g',   true),
    ('생크림',   '200ml',  true),
    ('치즈',     '2장',    false),
    ('버터',     '1큰술',  false),
    ('마늘',     '3개',    false),
    ('닭가슴살', '150g',   false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '크림파스타'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 토마토파스타
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('스파게티', '160g',   true),
    ('토마토',   '3개',    true),
    ('양파',     '0.5개',  false),
    ('마늘',     '3개',    false),
    ('설탕',     '1작은술', false),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '토마토파스타'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 스테이크
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('소등심',   '200g',   true),
    ('버터',     '1큰술',  false),
    ('마늘',     '3개',    false),
    ('소금',     '약간',   true),
    ('후추',     '약간',   true)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '스테이크'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 시저샐러드
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('상추',     '1봉',    true),
    ('식빵',     '2장',    false),
    ('치즈',     '2장',    false),
    ('마요네즈', '3큰술',  true),
    ('레몬',     '0.5개',  false),
    ('마늘',     '1개',    false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '시저샐러드'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 치킨샌드위치
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('식빵',     '2장',    true),
    ('닭가슴살', '150g',   true),
    ('상추',     '3장',    false),
    ('토마토',   '0.5개',  false),
    ('양파',     '0.25개', false),
    ('치즈',     '1장',    false),
    ('마요네즈', '2큰술',  false),
    ('소금',     '약간',   false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '치킨샌드위치'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 오므라이스
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('쌀',       '1공기',  true),
    ('달걀',     '2개',    true),
    ('닭고기',   '100g',   false),
    ('양파',     '0.5개',  false),
    ('버터',     '1큰술',  false),
    ('케첩',     '3큰술',  true),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '오므라이스'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 볶음밥
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('쌀',       '1공기',  true),
    ('달걀',     '1개',    false),
    ('양파',     '0.3개',  false),
    ('당근',     '0.3개',  false),
    ('파프리카', '0.3개',  false),
    ('대파',     '0.3개',  false),
    ('간장',     '1큰술',  false),
    ('참기름',   '1작은술', false),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '볶음밥'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 야채볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('브로콜리', '0.5개',  false),
    ('파프리카', '1개',    false),
    ('당근',     '0.5개',  false),
    ('양파',     '0.5개',  false),
    ('마늘',     '3개',    false),
    ('굴소스',   '1큰술',  true),
    ('간장',     '0.5큰술', false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '야채볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 된장삼겹살볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('삼겹살',   '200g',   true),
    ('된장',     '1큰술',  true),
    ('고추장',   '0.5큰술', false),
    ('양파',     '0.5개',  false),
    ('애호박',   '0.5개',  false),
    ('대파',     '0.5개',  false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '된장삼겹살볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 연어구이
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('연어',     '200g',   true),
    ('간장',     '2큰술',  true),
    ('미림',     '1큰술',  false),
    ('설탕',     '0.5큰술', false),
    ('레몬',     '0.5개',  false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '연어구이'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 달걀볶음밥
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('쌀',       '1공기',  true),
    ('달걀',     '2개',    true),
    ('대파',     '0.3개',  false),
    ('간장',     '1큰술',  false),
    ('참기름',   '1작은술', false),
    ('소금',     '약간',   false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '달걀볶음밥'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 두부조림
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('두부',     '1모',    true),
    ('대파',     '0.3개',  false),
    ('마늘',     '3개',    false),
    ('간장',     '2큰술',  true),
    ('고춧가루', '1큰술',  false),
    ('설탕',     '0.5큰술', false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '두부조림'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 감자볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('감자',     '2개',    true),
    ('파프리카', '0.5개',  false),
    ('당근',     '0.3개',  false),
    ('간장',     '1큰술',  false),
    ('소금',     '약간',   false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '감자볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 마파두부
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('두부',     '1모',    true),
    ('돼지고기', '100g',   false),
    ('대파',     '0.3개',  false),
    ('마늘',     '3개',    true),
    ('고추장',   '0.5큰술', false),
    ('간장',     '1큰술',  false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '마파두부'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 볶음면
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('국수',     '200g',   true),
    ('양파',     '0.5개',  false),
    ('당근',     '0.3개',  false),
    ('파프리카', '0.5개',  false),
    ('양배추',   '0.2개',  false),
    ('마늘',     '3개',    false),
    ('굴소스',   '2큰술',  true),
    ('간장',     '1큰술',  false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '볶음면'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 토마토달걀볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('토마토',   '3개',    true),
    ('달걀',     '3개',    true),
    ('마늘',     '2개',    false),
    ('설탕',     '0.5큰술', false),
    ('소금',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '토마토달걀볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 프렌치토스트
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('식빵',     '2장',    true),
    ('달걀',     '2개',    true),
    ('우유',     '3큰술',  true),
    ('버터',     '1큰술',  false),
    ('설탕',     '1큰술',  false),
    ('딸기',     '5개',    false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '프렌치토스트'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 브로콜리달걀볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('브로콜리', '1개',    true),
    ('달걀',     '2개',    true),
    ('마늘',     '3개',    false),
    ('간장',     '1큰술',  false),
    ('굴소스',   '0.5큰술', false),
    ('참기름',   '1작은술', false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '브로콜리달걀볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 참치마요덮밥
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('쌀',       '1공기',  true),
    ('참치',     '1캔',    true),
    ('오이',     '0.3개',  false),
    ('마요네즈', '2큰술',  true),
    ('간장',     '0.5큰술', false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '참치마요덮밥'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;

-- =====================
-- 버섯볶음
-- =====================
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential, substitute_ids)
SELECT r.id, i.id, m.qty, m.essential, ARRAY[]::BIGINT[]
FROM recipes r
JOIN (VALUES
    ('표고버섯', '200g',   true),
    ('버터',     '1큰술',  false),
    ('마늘',     '3개',    false),
    ('간장',     '1큰술',  false),
    ('굴소스',   '0.5큰술', false),
    ('후추',     '약간',   false)
) AS m(iname, qty, essential) ON true
JOIN ingredient_master i ON i.name = m.iname
WHERE r.title = '버섯볶음'
ON CONFLICT (recipe_id, ingredient_id) DO NOTHING;
