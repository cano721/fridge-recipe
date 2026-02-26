-- 레시피 시드 데이터 (한식 30개 + 기타 20개 = 50개)

INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, steps, nutrition, tags, source_type)
VALUES

    -- =====================
    -- 한식 찌개/국 (10개)
    -- =====================
    (
        '된장찌개',
        '구수한 된장과 신선한 채소로 끓인 한국 대표 국물요리',
        '한식', 'easy', 25, 2,
        '[{"order":1,"description":"두부를 1.5cm 크기로 깍둑썰기 합니다."},{"order":2,"description":"애호박, 양파를 1cm 두께로 썰어줍니다."},{"order":3,"description":"냄비에 멸치 육수 500ml를 붓고 된장 2큰술을 풀어줍니다."},{"order":4,"description":"육수가 끓으면 감자, 애호박, 양파를 넣고 중불에서 5분 끓입니다."},{"order":5,"description":"두부와 청양고추를 넣고 3분 더 끓인 후 대파를 올려 마무리합니다."}]'::jsonb,
        '{"calories":180,"protein":12,"fat":7,"carbs":18}'::jsonb,
        ARRAY['국물', '한식', '간단', '건강', '찌개'],
        'manual'
    ),
    (
        '김치찌개',
        '잘 익은 김치와 돼지고기로 끓인 얼큰하고 깊은 맛의 찌개',
        '한식', 'easy', 30, 2,
        '[{"order":1,"description":"돼지고기(삼겹살)를 한 입 크기로 썰어줍니다."},{"order":2,"description":"냄비에 기름을 두르고 돼지고기를 볶다가 잘 익은 김치를 넣고 함께 볶습니다."},{"order":3,"description":"물 400ml를 붓고 끓이기 시작합니다."},{"order":4,"description":"고추장 1작은술, 고춧가루 1큰술을 넣어 간을 맞춥니다."},{"order":5,"description":"두부를 넣고 10분간 중불에서 끓인 후 대파를 올려 마무리합니다."}]'::jsonb,
        '{"calories":320,"protein":20,"fat":18,"carbs":15}'::jsonb,
        ARRAY['국물', '한식', '얼큰', '찌개', '김치'],
        'manual'
    ),
    (
        '부대찌개',
        '햄, 소시지, 두부, 라면이 어우러진 푸짐한 찌개',
        '한식', 'easy', 25, 3,
        '[{"order":1,"description":"햄과 소시지를 먹기 좋게 썰고, 두부는 깍둑썰기 합니다."},{"order":2,"description":"냄비에 물 600ml를 붓고 고추장 1큰술, 고춧가루 1큰술을 넣어 육수를 만듭니다."},{"order":3,"description":"햄, 소시지, 두부, 양파, 대파를 넣고 끓입니다."},{"order":4,"description":"라면 사리를 넣고 3분간 더 끓입니다."},{"order":5,"description":"치즈를 올려 녹으면 완성입니다."}]'::jsonb,
        '{"calories":480,"protein":25,"fat":22,"carbs":45}'::jsonb,
        ARRAY['국물', '한식', '찌개', '얼큰', '라면'],
        'manual'
    ),
    (
        '순두부찌개',
        '부드러운 순두부와 해산물로 끓인 얼큰한 찌개',
        '한식', 'easy', 20, 2,
        '[{"order":1,"description":"냄비에 참기름을 두르고 다진 마늘을 볶아 향을 냅니다."},{"order":2,"description":"고춧가루 1큰술을 넣고 볶아 양념장을 만듭니다."},{"order":3,"description":"물 400ml와 조개(또는 새우)를 넣고 끓입니다."},{"order":4,"description":"순두부를 숟가락으로 크게 떠서 넣습니다."},{"order":5,"description":"국간장으로 간을 맞추고 달걀을 깨 넣은 후 대파를 올려 마무리합니다."}]'::jsonb,
        '{"calories":210,"protein":16,"fat":10,"carbs":12}'::jsonb,
        ARRAY['국물', '한식', '얼큰', '찌개', '해산물'],
        'manual'
    ),
    (
        '소고기미역국',
        '소고기와 미역으로 끓인 담백하고 영양 만점의 국',
        '한식', 'easy', 40, 3,
        '[{"order":1,"description":"건미역을 찬물에 20분 불려 먹기 좋게 자릅니다."},{"order":2,"description":"소고기(국거리)를 참기름에 볶아 색이 변하면 미역을 넣고 함께 볶습니다."},{"order":3,"description":"물 1L를 붓고 센 불에서 끓입니다."},{"order":4,"description":"끓어오르면 중불로 줄여 20분간 더 끓입니다."},{"order":5,"description":"국간장과 소금으로 간을 맞추고 마무리합니다."}]'::jsonb,
        '{"calories":150,"protein":14,"fat":6,"carbs":8}'::jsonb,
        ARRAY['국물', '한식', '국', '소고기', '건강'],
        'manual'
    ),
    (
        '된장국',
        '시원하고 구수한 한국 전통 된장국',
        '한식', 'easy', 15, 2,
        '[{"order":1,"description":"냄비에 물 500ml를 붓고 멸치 다시마 육수를 냅니다."},{"order":2,"description":"된장 1.5큰술을 풀어줍니다."},{"order":3,"description":"두부와 애호박을 넣고 중불에서 끓입니다."},{"order":4,"description":"대파와 청양고추를 넣고 2분 더 끓입니다."},{"order":5,"description":"간을 확인하고 마무리합니다."}]'::jsonb,
        '{"calories":120,"protein":9,"fat":5,"carbs":10}'::jsonb,
        ARRAY['국물', '한식', '국', '간단', '건강'],
        'manual'
    ),
    (
        '콩나물국',
        '시원하고 깔끔한 콩나물국',
        '한식', 'easy', 15, 2,
        '[{"order":1,"description":"콩나물을 깨끗이 씻어줍니다."},{"order":2,"description":"냄비에 물 500ml를 붓고 콩나물을 넣은 뒤 뚜껑을 닫고 센 불에서 끓입니다."},{"order":3,"description":"끓어오르면 뚜껑을 열지 않고 3분간 더 끓입니다."},{"order":4,"description":"다진 마늘, 국간장, 소금으로 간을 맞춥니다."},{"order":5,"description":"대파를 썰어 넣고 마무리합니다."}]'::jsonb,
        '{"calories":60,"protein":5,"fat":2,"carbs":7}'::jsonb,
        ARRAY['국물', '한식', '국', '간단', '해장'],
        'manual'
    ),
    (
        '닭볶음탕',
        '매콤달콤한 양념에 감자와 함께 조린 닭볶음탕',
        '한식', 'medium', 45, 3,
        '[{"order":1,"description":"닭을 찬물에 씻어 핏물을 제거하고 끓는 물에 데칩니다."},{"order":2,"description":"고추장 2큰술, 고춧가루 2큰술, 간장 3큰술, 설탕 1큰술, 다진 마늘로 양념장을 만듭니다."},{"order":3,"description":"냄비에 닭과 양념장, 물 200ml를 넣고 센 불에서 끓입니다."},{"order":4,"description":"끓어오르면 감자, 양파, 당근을 넣고 중불에서 20분간 조립니다."},{"order":5,"description":"대파를 넣고 5분 더 조린 후 마무리합니다."}]'::jsonb,
        '{"calories":380,"protein":30,"fat":14,"carbs":28}'::jsonb,
        ARRAY['한식', '볶음', '매콤', '닭고기'],
        'manual'
    ),
    (
        '갈비탕',
        '진하게 우려낸 소갈비 육수로 만든 고급스러운 국물요리',
        '한식', 'hard', 120, 3,
        '[{"order":1,"description":"소갈비를 찬물에 2시간 담가 핏물을 뺍니다."},{"order":2,"description":"끓는 물에 갈비를 넣고 5분간 데친 후 찬물에 씻습니다."},{"order":3,"description":"냄비에 갈비와 물 2L, 대파, 마늘, 생강을 넣고 센 불에서 끓입니다."},{"order":4,"description":"끓어오르면 약불로 줄여 1시간 30분 동안 천천히 끓입니다."},{"order":5,"description":"소금과 후추로 간을 맞추고 대파를 올려 완성합니다."}]'::jsonb,
        '{"calories":420,"protein":35,"fat":20,"carbs":15}'::jsonb,
        ARRAY['국물', '한식', '국', '소고기', '고급'],
        'manual'
    ),
    (
        '어묵탕',
        '시원한 국물과 쫄깃한 어묵이 어우러진 간식 겸 국물요리',
        '한식', 'easy', 20, 3,
        '[{"order":1,"description":"어묵을 먹기 좋게 썰고 꼬치에 꽂아줍니다."},{"order":2,"description":"냄비에 물 1L와 다시마, 멸치를 넣고 10분간 육수를 냅니다."},{"order":3,"description":"육수에 어묵을 넣고 끓입니다."},{"order":4,"description":"국간장, 소금으로 간을 맞춥니다."},{"order":5,"description":"대파와 무를 넣고 5분 더 끓여 완성합니다."}]'::jsonb,
        '{"calories":160,"protein":10,"fat":5,"carbs":20}'::jsonb,
        ARRAY['국물', '한식', '국', '간단', '간식'],
        'manual'
    ),

    -- =====================
    -- 한식 볶음/구이 (8개)
    -- =====================
    (
        '불고기',
        '간장 양념에 재운 소고기를 달콤하게 볶은 한국 대표 요리',
        '한식', 'easy', 30, 3,
        '[{"order":1,"description":"소고기(불고기감)를 간장 3큰술, 설탕 1큰술, 다진 마늘, 참기름, 후추로 30분 이상 재웁니다."},{"order":2,"description":"양파는 채썰고 대파는 어슷썰기 합니다."},{"order":3,"description":"팬에 재운 소고기를 넣고 센 불에서 볶습니다."},{"order":4,"description":"고기가 익으면 양파와 대파를 넣고 함께 볶습니다."},{"order":5,"description":"참깨를 뿌려 마무리합니다."}]'::jsonb,
        '{"calories":340,"protein":28,"fat":15,"carbs":20}'::jsonb,
        ARRAY['한식', '볶음', '소고기', '달콤'],
        'manual'
    ),
    (
        '제육볶음',
        '매콤달콤한 고추장 양념에 돼지고기를 볶은 인기 반찬',
        '한식', 'easy', 25, 2,
        '[{"order":1,"description":"돼지고기(목살)를 먹기 좋게 썰어줍니다."},{"order":2,"description":"고추장 2큰술, 고춧가루 1큰술, 간장 1큰술, 설탕 1큰술, 다진 마늘, 참기름으로 양념을 만듭니다."},{"order":3,"description":"돼지고기에 양념을 넣고 20분 재웁니다."},{"order":4,"description":"팬에 재운 돼지고기와 양파, 대파를 넣고 센 불에서 볶습니다."},{"order":5,"description":"고기가 완전히 익으면 참깨를 뿌려 완성합니다."}]'::jsonb,
        '{"calories":360,"protein":25,"fat":16,"carbs":25}'::jsonb,
        ARRAY['한식', '볶음', '돼지고기', '매콤', '반찬'],
        'manual'
    ),
    (
        '삼겹살구이',
        '불판에 구워 먹는 한국인이 사랑하는 삼겹살',
        '한식', 'easy', 20, 2,
        '[{"order":1,"description":"삼겹살을 먹기 좋은 두께(0.5~1cm)로 준비합니다."},{"order":2,"description":"불판을 달구고 삼겹살을 올립니다."},{"order":3,"description":"앞뒤로 골고루 굽습니다."},{"order":4,"description":"상추와 깻잎에 구운 삼겹살과 마늘, 된장을 올려 쌈을 싸 먹습니다."},{"order":5,"description":"소금, 참기름에 찍어 먹어도 맛있습니다."}]'::jsonb,
        '{"calories":520,"protein":22,"fat":42,"carbs":3}'::jsonb,
        ARRAY['한식', '구이', '삼겹살', '쌈'],
        'manual'
    ),
    (
        '닭갈비',
        '매콤한 고추장 양념에 닭고기와 채소를 볶은 춘천식 닭갈비',
        '한식', 'medium', 35, 3,
        '[{"order":1,"description":"닭고기를 한 입 크기로 자르고 고추장 2큰술, 고춧가루 1큰술, 간장 2큰술, 설탕 1큰술, 다진 마늘, 미림으로 20분 재웁니다."},{"order":2,"description":"고구마와 양배추, 양파를 먹기 좋게 자릅니다."},{"order":3,"description":"넓은 팬에 기름을 두르고 재운 닭고기를 넣어 볶습니다."},{"order":4,"description":"고구마, 양배추, 양파를 넣고 함께 볶습니다."},{"order":5,"description":"떡을 넣고 채소가 익을 때까지 볶아 완성합니다."}]'::jsonb,
        '{"calories":420,"protein":32,"fat":12,"carbs":45}'::jsonb,
        ARRAY['한식', '볶음', '닭고기', '매콤'],
        'manual'
    ),
    (
        '멸치볶음',
        '고소하고 짭조름한 밥도둑 멸치볶음',
        '한식', 'easy', 15, 4,
        '[{"order":1,"description":"멸치를 마른 팬에서 2분간 볶아 비린내를 제거합니다."},{"order":2,"description":"팬에 기름을 두르고 멸치를 넣어 볶습니다."},{"order":3,"description":"간장 1큰술, 설탕 0.5큰술, 미림 1큰술을 넣고 볶습니다."},{"order":4,"description":"물엿 1큰술을 넣고 윤기 나게 볶습니다."},{"order":5,"description":"참기름과 참깨를 뿌려 완성합니다."}]'::jsonb,
        '{"calories":120,"protein":12,"fat":5,"carbs":8}'::jsonb,
        ARRAY['한식', '반찬', '멸치', '볶음', '간단'],
        'manual'
    ),
    (
        '어묵볶음',
        '달콤짭짤한 양념에 볶은 간편 반찬',
        '한식', 'easy', 15, 3,
        '[{"order":1,"description":"어묵을 먹기 좋은 크기로 자릅니다."},{"order":2,"description":"팬에 기름을 두르고 어묵을 볶습니다."},{"order":3,"description":"간장 1큰술, 설탕 0.5큰술, 고춧가루 0.5큰술을 넣고 볶습니다."},{"order":4,"description":"물엿 1큰술을 넣고 윤기 나게 볶습니다."},{"order":5,"description":"대파와 참깨를 넣어 마무리합니다."}]'::jsonb,
        '{"calories":140,"protein":8,"fat":6,"carbs":15}'::jsonb,
        ARRAY['한식', '반찬', '어묵', '볶음', '간단'],
        'manual'
    ),
    (
        '고등어구이',
        '바삭하게 구워낸 고소한 고등어구이',
        '한식', 'easy', 20, 2,
        '[{"order":1,"description":"고등어에 소금을 뿌려 10분 절입니다."},{"order":2,"description":"키친타월로 물기를 제거합니다."},{"order":3,"description":"달군 팬에 기름을 두르고 고등어를 껍질 면부터 굽습니다."},{"order":4,"description":"앞뒤로 노릇하게 구워줍니다."},{"order":5,"description":"레몬즙을 뿌려 완성합니다."}]'::jsonb,
        '{"calories":290,"protein":26,"fat":18,"carbs":2}'::jsonb,
        ARRAY['한식', '구이', '생선', '고등어'],
        'manual'
    ),
    (
        '새우볶음',
        '마늘과 버터 향이 가득한 간단 새우볶음',
        '한식', 'easy', 15, 2,
        '[{"order":1,"description":"새우의 껍질을 벗기고 등에 칼집을 넣어 내장을 제거합니다."},{"order":2,"description":"팬에 버터를 녹이고 다진 마늘을 볶습니다."},{"order":3,"description":"새우를 넣고 센 불에서 볶습니다."},{"order":4,"description":"간장 1큰술, 굴소스 1큰술을 넣고 볶습니다."},{"order":5,"description":"대파를 넣고 후추로 마무리합니다."}]'::jsonb,
        '{"calories":180,"protein":20,"fat":8,"carbs":5}'::jsonb,
        ARRAY['한식', '볶음', '새우', '해산물'],
        'manual'
    ),

    -- =====================
    -- 한식 밥/면 (6개)
    -- =====================
    (
        '김치볶음밥',
        '잘 익은 김치로 만든 간단하고 맛있는 볶음밥',
        '한식', 'easy', 15, 1,
        '[{"order":1,"description":"잘 익은 김치를 잘게 썰어줍니다."},{"order":2,"description":"팬에 기름을 두르고 김치를 볶습니다."},{"order":3,"description":"밥을 넣고 센 불에서 볶습니다."},{"order":4,"description":"간장 1큰술, 참기름을 넣고 볶습니다."},{"order":5,"description":"달걀 프라이를 올려 완성합니다."}]'::jsonb,
        '{"calories":380,"protein":10,"fat":12,"carbs":58}'::jsonb,
        ARRAY['한식', '밥', '볶음밥', '간단', '김치'],
        'manual'
    ),
    (
        '비빔밥',
        '다양한 나물과 고추장을 넣어 비빈 한국 대표 밥요리',
        '한식', 'medium', 40, 2,
        '[{"order":1,"description":"시금치, 콩나물, 당근을 각각 데치거나 볶아 나물을 준비합니다."},{"order":2,"description":"소고기를 간장, 설탕, 참기름으로 양념해 볶습니다."},{"order":3,"description":"따뜻한 밥을 그릇에 담습니다."},{"order":4,"description":"나물과 소고기볶음, 달걀 프라이를 보기 좋게 올립니다."},{"order":5,"description":"고추장을 넣고 참기름을 뿌려 비벼 먹습니다."}]'::jsonb,
        '{"calories":450,"protein":18,"fat":12,"carbs":68}'::jsonb,
        ARRAY['한식', '밥', '비빔밥', '나물'],
        'manual'
    ),
    (
        '카레라이스',
        '채소와 고기가 들어간 부드러운 일본식 카레',
        '한식', 'easy', 35, 3,
        '[{"order":1,"description":"감자, 당근, 양파를 먹기 좋게 썹니다."},{"order":2,"description":"돼지고기(또는 닭고기)를 한 입 크기로 썹니다."},{"order":3,"description":"냄비에 기름을 두르고 고기를 볶다가 채소를 넣고 함께 볶습니다."},{"order":4,"description":"물 800ml를 붓고 끓이다가 카레 루를 넣고 녹입니다."},{"order":5,"description":"약불에서 15분간 끓여 걸쭉하게 만든 후 밥 위에 올려 완성합니다."}]'::jsonb,
        '{"calories":420,"protein":18,"fat":14,"carbs":58}'::jsonb,
        ARRAY['한식', '밥', '카레', '간단'],
        'manual'
    ),
    (
        '잡채',
        '당면과 다양한 채소, 고기가 어우러진 명절 대표 요리',
        '한식', 'medium', 45, 4,
        '[{"order":1,"description":"당면을 찬물에 30분 불립니다."},{"order":2,"description":"소고기, 시금치, 당근, 표고버섯, 양파를 각각 볶습니다."},{"order":3,"description":"불린 당면을 끓는 물에 5분 삶은 후 건집니다."},{"order":4,"description":"간장 3큰술, 설탕 1큰술, 참기름으로 당면 양념을 합니다."},{"order":5,"description":"모든 재료를 큰 볼에 넣고 잘 섞어 완성합니다."}]'::jsonb,
        '{"calories":380,"protein":15,"fat":10,"carbs":62}'::jsonb,
        ARRAY['한식', '면', '잡채', '명절'],
        'manual'
    ),
    (
        '떡볶이',
        '달콤하고 매콤한 고추장 소스에 조린 국민 간식',
        '한식', 'easy', 20, 2,
        '[{"order":1,"description":"떡을 찬물에 담가 불려줍니다."},{"order":2,"description":"냄비에 물 300ml를 넣고 고추장 2큰술, 고춧가루 1큰술, 설탕 1큰술, 간장 1큰술로 소스를 만듭니다."},{"order":3,"description":"소스가 끓으면 떡과 어묵을 넣습니다."},{"order":4,"description":"중불에서 10분간 끓이며 소스를 입힙니다."},{"order":5,"description":"대파를 넣고 마무리합니다."}]'::jsonb,
        '{"calories":320,"protein":8,"fat":4,"carbs":65}'::jsonb,
        ARRAY['한식', '간식', '떡볶이', '매콤', '분식'],
        'manual'
    ),
    (
        '비빔국수',
        '새콤달콤한 양념에 비벼 먹는 여름 별미 비빔국수',
        '한식', 'easy', 15, 2,
        '[{"order":1,"description":"국수를 끓는 물에 삶아 찬물에 헹굽니다."},{"order":2,"description":"고추장 2큰술, 식초 1큰술, 설탕 1큰술, 간장 0.5큰술, 참기름, 다진 마늘로 양념장을 만듭니다."},{"order":3,"description":"오이를 채썰고 달걀을 삶아 반으로 자릅니다."},{"order":4,"description":"국수에 양념장을 넣고 잘 비빕니다."},{"order":5,"description":"오이와 달걀을 올려 완성합니다."}]'::jsonb,
        '{"calories":340,"protein":12,"fat":8,"carbs":58}'::jsonb,
        ARRAY['한식', '면', '국수', '여름', '간단'],
        'manual'
    ),

    -- =====================
    -- 한식 나물/반찬 (6개)
    -- =====================
    (
        '시금치나물',
        '참기름과 마늘로 무친 담백한 시금치 나물',
        '한식', 'easy', 10, 3,
        '[{"order":1,"description":"시금치를 깨끗이 씻어줍니다."},{"order":2,"description":"끓는 물에 소금을 넣고 시금치를 20초간 데칩니다."},{"order":3,"description":"찬물에 헹궈 물기를 꼭 짭니다."},{"order":4,"description":"간장 1큰술, 다진 마늘 1작은술, 참기름 1큰술, 소금으로 무쳐줍니다."},{"order":5,"description":"깨를 뿌려 완성합니다."}]'::jsonb,
        '{"calories":60,"protein":4,"fat":3,"carbs":6}'::jsonb,
        ARRAY['한식', '나물', '반찬', '건강', '간단'],
        'manual'
    ),
    (
        '오이무침',
        '새콤달콤하게 무친 시원한 오이 반찬',
        '한식', 'easy', 10, 3,
        '[{"order":1,"description":"오이를 얇게 어슷썰기 합니다."},{"order":2,"description":"소금에 10분 절인 후 물기를 짭니다."},{"order":3,"description":"고추장 0.5큰술, 식초 1큰술, 설탕 0.5큰술, 다진 마늘, 참기름으로 양념을 만듭니다."},{"order":4,"description":"오이에 양념을 넣고 버무립니다."},{"order":5,"description":"참깨를 뿌려 완성합니다."}]'::jsonb,
        '{"calories":40,"protein":2,"fat":2,"carbs":6}'::jsonb,
        ARRAY['한식', '반찬', '무침', '오이', '간단'],
        'manual'
    ),
    (
        '감자조림',
        '달콤짭짤하게 조린 간편 반찬 감자조림',
        '한식', 'easy', 20, 3,
        '[{"order":1,"description":"감자를 한 입 크기로 썰어 전분을 제거하기 위해 물에 담급니다."},{"order":2,"description":"팬에 기름을 두르고 감자를 볶습니다."},{"order":3,"description":"간장 2큰술, 설탕 1큰술, 미림 1큰술, 물 3큰술을 넣고 조립니다."},{"order":4,"description":"뚜껑을 덮고 약불에서 10분간 조립니다."},{"order":5,"description":"물엿을 넣고 윤기 나게 마무리합니다."}]'::jsonb,
        '{"calories":160,"protein":3,"fat":4,"carbs":28}'::jsonb,
        ARRAY['한식', '반찬', '조림', '감자'],
        'manual'
    ),
    (
        '계란찜',
        '부드럽고 폭신한 한국식 달걀찜',
        '한식', 'easy', 15, 2,
        '[{"order":1,"description":"달걀 3개를 깨서 멸치 육수(또는 물) 200ml와 함께 잘 풀어줍니다."},{"order":2,"description":"소금으로 간하고 체에 걸러 부드럽게 만듭니다."},{"order":3,"description":"뚝배기나 냄비에 달걀물을 붓고 약불에서 가열합니다."},{"order":4,"description":"뚜껑을 덮고 5분간 쪄줍니다."},{"order":5,"description":"대파와 참기름을 올려 마무리합니다."}]'::jsonb,
        '{"calories":150,"protein":12,"fat":10,"carbs":3}'::jsonb,
        ARRAY['한식', '반찬', '달걀', '간단'],
        'manual'
    ),
    (
        '계란말이',
        '채소를 넣어 돌돌 만 부드러운 계란말이',
        '한식', 'easy', 15, 2,
        '[{"order":1,"description":"달걀 3개에 소금, 후추를 넣고 잘 풀어줍니다."},{"order":2,"description":"당근, 대파, 파프리카를 잘게 다집니다."},{"order":3,"description":"달걀물에 채소를 넣고 섞습니다."},{"order":4,"description":"약불에서 기름 두른 팬에 달걀물을 조금씩 부어가며 말아줍니다."},{"order":5,"description":"먹기 좋은 크기로 썰어 완성합니다."}]'::jsonb,
        '{"calories":180,"protein":14,"fat":12,"carbs":4}'::jsonb,
        ARRAY['한식', '반찬', '달걀', '도시락'],
        'manual'
    ),
    (
        '깻잎무침',
        '향긋한 깻잎을 양념에 무친 밥도둑 반찬',
        '한식', 'easy', 10, 3,
        '[{"order":1,"description":"깻잎을 깨끗이 씻어줍니다."},{"order":2,"description":"간장 2큰술, 고춧가루 1큰술, 설탕 0.5큰술, 다진 마늘, 참기름으로 양념을 만듭니다."},{"order":3,"description":"깻잎 한 장씩 양념을 발라가며 쌓습니다."},{"order":4,"description":"참깨를 뿌려줍니다."},{"order":5,"description":"냉장고에 30분 이상 두었다가 먹으면 더 맛있습니다."}]'::jsonb,
        '{"calories":50,"protein":3,"fat":3,"carbs":5}'::jsonb,
        ARRAY['한식', '반찬', '나물', '깻잎'],
        'manual'
    ),

    -- =====================
    -- 한식 전/부침 (4개)
    -- =====================
    (
        '김치전',
        '잘 익은 김치로 만든 바삭한 김치전',
        '한식', 'easy', 20, 2,
        '[{"order":1,"description":"잘 익은 김치를 잘게 썰어줍니다."},{"order":2,"description":"밀가루 1컵, 물 0.7컵, 달걀 1개를 섞어 반죽을 만듭니다."},{"order":3,"description":"반죽에 김치와 김치 국물을 넣고 섞습니다."},{"order":4,"description":"기름을 두른 팬에 반죽을 올려 중불에서 앞뒤로 노릇하게 굽습니다."},{"order":5,"description":"간장과 식초를 섞은 양념장과 함께 냅니다."}]'::jsonb,
        '{"calories":280,"protein":8,"fat":12,"carbs":38}'::jsonb,
        ARRAY['한식', '전', '부침', '김치', '분식'],
        'manual'
    ),
    (
        '감자전',
        '갈아 만든 감자반죽으로 만든 고소한 감자전',
        '한식', 'easy', 20, 2,
        '[{"order":1,"description":"감자를 강판에 갈아줍니다."},{"order":2,"description":"갈아낸 감자의 물을 짜고 녹말만 남깁니다."},{"order":3,"description":"감자와 녹말, 소금을 섞어 반죽을 만듭니다."},{"order":4,"description":"기름을 두른 팬에 반죽을 올려 중불에서 앞뒤로 노릇하게 굽습니다."},{"order":5,"description":"간장에 찍어 먹습니다."}]'::jsonb,
        '{"calories":220,"protein":5,"fat":8,"carbs":34}'::jsonb,
        ARRAY['한식', '전', '부침', '감자'],
        'manual'
    ),
    (
        '해물파전',
        '바삭한 파전에 신선한 해물이 가득한 해물파전',
        '한식', 'medium', 25, 3,
        '[{"order":1,"description":"밀가루 1컵과 부침가루 0.5컵, 물 1컵을 섞어 반죽을 만듭니다."},{"order":2,"description":"새우, 오징어를 먹기 좋게 자릅니다."},{"order":3,"description":"대파를 5~6cm 길이로 자릅니다."},{"order":4,"description":"기름을 두른 팬에 반죽을 붓고 해물과 대파를 올린 후 달걀을 풀어 뿌립니다."},{"order":5,"description":"앞뒤로 노릇하게 구워 초간장과 함께 냅니다."}]'::jsonb,
        '{"calories":320,"protein":16,"fat":12,"carbs":40}'::jsonb,
        ARRAY['한식', '전', '해산물', '파전'],
        'manual'
    ),
    (
        '두부전',
        '고소하고 담백한 두부전',
        '한식', 'easy', 15, 2,
        '[{"order":1,"description":"두부를 1cm 두께로 납작하게 썰고 키친타월로 물기를 제거합니다."},{"order":2,"description":"밀가루와 달걀물을 준비합니다."},{"order":3,"description":"두부에 밀가루를 묻히고 달걀물을 입힙니다."},{"order":4,"description":"기름을 두른 팬에 중불로 앞뒤 노릇하게 굽습니다."},{"order":5,"description":"간장 양념장과 함께 냅니다."}]'::jsonb,
        '{"calories":180,"protein":12,"fat":10,"carbs":10}'::jsonb,
        ARRAY['한식', '전', '두부', '간단'],
        'manual'
    ),

    -- =====================
    -- 양식/기타 (20개)
    -- =====================
    (
        '크림파스타',
        '생크림으로 만든 부드럽고 진한 크림파스타',
        '양식', 'medium', 25, 2,
        '[{"order":1,"description":"스파게티를 소금물에 알덴테로 삶습니다."},{"order":2,"description":"베이컨(또는 닭가슴살)을 팬에 볶습니다."},{"order":3,"description":"다진 마늘을 버터에 볶아 향을 냅니다."},{"order":4,"description":"생크림 200ml를 붓고 치즈를 녹여 소스를 만듭니다."},{"order":5,"description":"삶은 파스타와 베이컨을 넣고 섞어 후추로 마무리합니다."}]'::jsonb,
        '{"calories":580,"protein":20,"fat":28,"carbs":65}'::jsonb,
        ARRAY['양식', '파스타', '크림', '간단'],
        'manual'
    ),
    (
        '토마토파스타',
        '신선한 토마토 소스로 만든 새콤달콤한 파스타',
        '양식', 'medium', 30, 2,
        '[{"order":1,"description":"스파게티를 소금물에 알덴테로 삶습니다."},{"order":2,"description":"양파와 마늘을 올리브오일에 볶습니다."},{"order":3,"description":"토마토 캔(또는 생토마토)을 넣고 소금, 설탕, 이탈리안 시즈닝으로 소스를 만듭니다."},{"order":4,"description":"소스를 15분간 약불에서 졸입니다."},{"order":5,"description":"삶은 파스타를 넣고 섞어 파마산 치즈를 뿌려 완성합니다."}]'::jsonb,
        '{"calories":420,"protein":14,"fat":10,"carbs":72}'::jsonb,
        ARRAY['양식', '파스타', '토마토'],
        'manual'
    ),
    (
        '스테이크',
        '육즙 가득한 팬에 구운 소고기 스테이크',
        '양식', 'medium', 20, 1,
        '[{"order":1,"description":"소고기(등심 또는 채끝)를 실온에 30분 꺼내둡니다."},{"order":2,"description":"소금과 후추로 양념합니다."},{"order":3,"description":"팬을 강불로 달구고 기름을 두른 후 고기를 올립니다."},{"order":4,"description":"앞뒤로 각 2~3분씩 원하는 굽기로 굽고 버터를 올려 녹입니다."},{"order":5,"description":"5분간 레스팅 후 썰어 냅니다."}]'::jsonb,
        '{"calories":380,"protein":35,"fat":22,"carbs":2}'::jsonb,
        ARRAY['양식', '스테이크', '소고기', '구이'],
        'manual'
    ),
    (
        '시저샐러드',
        '바삭한 크루통과 파마산 치즈가 올라간 시저샐러드',
        '양식', 'easy', 15, 2,
        '[{"order":1,"description":"상추(로메인)를 먹기 좋게 자릅니다."},{"order":2,"description":"마요네즈 3큰술, 레몬즙 1큰술, 다진 마늘, 후추로 드레싱을 만듭니다."},{"order":3,"description":"식빵을 큐브로 잘라 올리브오일에 구워 크루통을 만듭니다."},{"order":4,"description":"상추에 드레싱을 버무립니다."},{"order":5,"description":"크루통과 파마산 치즈를 올려 완성합니다."}]'::jsonb,
        '{"calories":240,"protein":8,"fat":16,"carbs":18}'::jsonb,
        ARRAY['양식', '샐러드', '간단', '건강'],
        'manual'
    ),
    (
        '치킨샌드위치',
        '부드러운 닭가슴살과 신선한 채소의 든든한 샌드위치',
        '양식', 'easy', 20, 1,
        '[{"order":1,"description":"닭가슴살을 소금, 후추로 양념해 팬에 굽습니다."},{"order":2,"description":"식빵에 마요네즈와 머스타드를 바릅니다."},{"order":3,"description":"상추, 토마토, 양파를 올립니다."},{"order":4,"description":"구운 닭가슴살을 올리고 치즈를 얹습니다."},{"order":5,"description":"식빵으로 덮어 완성합니다."}]'::jsonb,
        '{"calories":380,"protein":30,"fat":14,"carbs":35}'::jsonb,
        ARRAY['양식', '샌드위치', '닭고기', '간단'],
        'manual'
    ),
    (
        '오므라이스',
        '케첩 볶음밥을 달걀로 감싼 부드러운 오므라이스',
        '양식', 'easy', 20, 1,
        '[{"order":1,"description":"팬에 버터를 녹이고 다진 양파와 닭고기를 볶습니다."},{"order":2,"description":"밥을 넣고 케첩 2큰술로 양념해 볶음밥을 만듭니다."},{"order":3,"description":"달걀 2개를 풀어 소금으로 간합니다."},{"order":4,"description":"버터 두른 팬에 달걀물을 부어 반숙으로 만듭니다."},{"order":5,"description":"볶음밥을 달걀로 감싸 케첩을 뿌려 완성합니다."}]'::jsonb,
        '{"calories":420,"protein":18,"fat":16,"carbs":55}'::jsonb,
        ARRAY['양식', '밥', '달걀', '간단'],
        'manual'
    ),
    (
        '볶음밥',
        '남은 재료로 만드는 간단하고 맛있는 볶음밥',
        '기타', 'easy', 15, 1,
        '[{"order":1,"description":"달걀을 풀어 팬에 스크램블 형태로 볶습니다."},{"order":2,"description":"다진 양파, 당근, 파프리카를 볶습니다."},{"order":3,"description":"밥을 넣고 센 불에서 볶습니다."},{"order":4,"description":"간장 1큰술, 참기름, 소금으로 간을 맞춥니다."},{"order":5,"description":"대파를 넣고 마무리합니다."}]'::jsonb,
        '{"calories":360,"protein":10,"fat":10,"carbs":58}'::jsonb,
        ARRAY['기타', '밥', '볶음밥', '간단'],
        'manual'
    ),
    (
        '야채볶음',
        '다양한 채소를 굴소스로 볶은 건강한 야채볶음',
        '기타', 'easy', 15, 2,
        '[{"order":1,"description":"브로콜리, 파프리카, 당근, 양파를 먹기 좋게 썹니다."},{"order":2,"description":"팬에 기름을 두르고 마늘을 볶아 향을 냅니다."},{"order":3,"description":"단단한 채소부터 순서대로 넣고 센 불에서 볶습니다."},{"order":4,"description":"굴소스 1큰술, 간장 0.5큰술을 넣고 볶습니다."},{"order":5,"description":"참기름을 뿌려 마무리합니다."}]'::jsonb,
        '{"calories":120,"protein":5,"fat":5,"carbs":16}'::jsonb,
        ARRAY['기타', '채소', '볶음', '건강', '간단'],
        'manual'
    ),
    (
        '된장삼겹살볶음',
        '된장과 삼겹살의 환상적인 조합',
        '한식', 'easy', 20, 2,
        '[{"order":1,"description":"삼겹살을 한 입 크기로 자릅니다."},{"order":2,"description":"팬에 삼겹살을 볶아 기름을 뺍니다."},{"order":3,"description":"된장 1큰술, 고추장 0.5큰술을 넣고 볶습니다."},{"order":4,"description":"양파와 애호박을 넣고 함께 볶습니다."},{"order":5,"description":"대파를 넣고 참기름으로 마무리합니다."}]'::jsonb,
        '{"calories":420,"protein":20,"fat":30,"carbs":10}'::jsonb,
        ARRAY['한식', '볶음', '삼겹살'],
        'manual'
    ),
    (
        '연어구이',
        '촉촉하고 고소한 간장 양념 연어구이',
        '기타', 'easy', 20, 2,
        '[{"order":1,"description":"연어에 간장 2큰술, 미림 1큰술, 설탕 0.5큰술로 양념해 15분 재웁니다."},{"order":2,"description":"팬에 기름을 두르고 연어 껍질 면부터 굽습니다."},{"order":3,"description":"뒤집어 나머지 면을 굽습니다."},{"order":4,"description":"남은 양념을 뿌려가며 조립니다."},{"order":5,"description":"레몬을 곁들여 완성합니다."}]'::jsonb,
        '{"calories":280,"protein":28,"fat":16,"carbs":5}'::jsonb,
        ARRAY['기타', '생선', '구이', '연어'],
        'manual'
    ),
    (
        '달걀볶음밥',
        '심플하지만 맛있는 달걀 볶음밥',
        '기타', 'easy', 10, 1,
        '[{"order":1,"description":"달걀 2개를 깨서 소금, 후추를 넣고 잘 풀어줍니다."},{"order":2,"description":"팬에 기름을 두르고 달걀을 반숙으로 스크램블합니다."},{"order":3,"description":"밥을 넣고 센 불에서 볶습니다."},{"order":4,"description":"간장 1큰술, 참기름을 넣고 볶습니다."},{"order":5,"description":"대파를 넣고 마무리합니다."}]'::jsonb,
        '{"calories":340,"protein":12,"fat":12,"carbs":48}'::jsonb,
        ARRAY['기타', '밥', '달걀', '간단'],
        'manual'
    ),
    (
        '두부조림',
        '매콤달콤하게 조린 쫄깃한 두부조림',
        '한식', 'easy', 20, 3,
        '[{"order":1,"description":"두부를 1cm 두께로 썰어 소금을 뿌려 밑간합니다."},{"order":2,"description":"기름을 두른 팬에 두부를 넣고 앞뒤로 노릇하게 굽습니다."},{"order":3,"description":"간장 2큰술, 고춧가루 1큰술, 설탕 0.5큰술, 다진 마늘, 물 3큰술로 양념장을 만듭니다."},{"order":4,"description":"구운 두부에 양념장을 붓고 약불에서 조립니다."},{"order":5,"description":"대파와 참깨를 뿌려 완성합니다."}]'::jsonb,
        '{"calories":160,"protein":12,"fat":8,"carbs":10}'::jsonb,
        ARRAY['한식', '반찬', '조림', '두부', '매콤'],
        'manual'
    ),
    (
        '감자볶음',
        '채 썬 감자를 간장으로 볶은 간단한 반찬',
        '한식', 'easy', 15, 3,
        '[{"order":1,"description":"감자를 가늘게 채 썰어 찬물에 담가 전분을 뺍니다."},{"order":2,"description":"팬에 기름을 두르고 감자를 넣어 볶습니다."},{"order":3,"description":"간장 1큰술, 소금으로 간을 맞춥니다."},{"order":4,"description":"파프리카(또는 당근)를 넣고 함께 볶습니다."},{"order":5,"description":"참기름과 참깨를 뿌려 완성합니다."}]'::jsonb,
        '{"calories":140,"protein":3,"fat":4,"carbs":24}'::jsonb,
        ARRAY['한식', '반찬', '볶음', '감자'],
        'manual'
    ),
    (
        '마파두부',
        '중식풍의 매콤한 마파두부',
        '중식', 'medium', 20, 2,
        '[{"order":1,"description":"두부를 1.5cm 크기로 자르고 돼지고기를 다집니다."},{"order":2,"description":"팬에 기름을 두르고 다진 마늘, 생강을 볶습니다."},{"order":3,"description":"다진 돼지고기를 넣고 볶은 후 두반장 1큰술, 고추장 0.5큰술을 넣습니다."},{"order":4,"description":"두부와 물 100ml를 넣고 약불에서 5분 끓입니다."},{"order":5,"description":"전분물로 걸쭉하게 만들고 대파를 올려 완성합니다."}]'::jsonb,
        '{"calories":280,"protein":18,"fat":14,"carbs":16}'::jsonb,
        ARRAY['중식', '두부', '매콤'],
        'manual'
    ),
    (
        '볶음면',
        '쫄깃한 면과 채소를 굴소스로 볶은 볶음면',
        '중식', 'easy', 20, 2,
        '[{"order":1,"description":"면을 삶아 찬물에 헹굽니다."},{"order":2,"description":"양파, 당근, 파프리카, 양배추를 채 썹니다."},{"order":3,"description":"팬에 기름을 두르고 채소를 볶습니다."},{"order":4,"description":"삶은 면을 넣고 굴소스 2큰술, 간장 1큰술, 참기름으로 양념합니다."},{"order":5,"description":"센 불에서 볶아 완성합니다."}]'::jsonb,
        '{"calories":380,"protein":10,"fat":10,"carbs":62}'::jsonb,
        ARRAY['중식', '면', '볶음', '간단'],
        'manual'
    ),
    (
        '토마토달걀볶음',
        '새콤달콤한 토마토와 달걀의 중식풍 볶음',
        '중식', 'easy', 15, 2,
        '[{"order":1,"description":"토마토를 6등분하고 달걀 3개를 풀어줍니다."},{"order":2,"description":"팬에 기름을 두르고 달걀을 부드럽게 볶다 꺼냅니다."},{"order":3,"description":"같은 팬에 마늘을 볶고 토마토를 넣어 볶습니다."},{"order":4,"description":"설탕 0.5큰술, 소금으로 간을 맞춥니다."},{"order":5,"description":"달걀을 다시 넣고 가볍게 섞어 완성합니다."}]'::jsonb,
        '{"calories":180,"protein":12,"fat":12,"carbs":8}'::jsonb,
        ARRAY['중식', '달걀', '볶음', '간단'],
        'manual'
    ),
    (
        '프렌치토스트',
        '달콤하고 부드러운 아침식사용 프렌치토스트',
        '양식', 'easy', 15, 1,
        '[{"order":1,"description":"달걀 2개, 우유 3큰술, 설탕 1큰술, 계피가루를 섞어 달걀물을 만듭니다."},{"order":2,"description":"식빵을 달걀물에 충분히 적십니다."},{"order":3,"description":"버터를 녹인 팬에 중약불로 앞뒤로 노릇하게 굽습니다."},{"order":4,"description":"메이플 시럽이나 꿀을 뿌립니다."},{"order":5,"description":"딸기나 바나나 등 과일을 곁들입니다."}]'::jsonb,
        '{"calories":320,"protein":12,"fat":14,"carbs":38}'::jsonb,
        ARRAY['양식', '아침', '빵', '간단', '디저트'],
        'manual'
    ),
    (
        '브로콜리달걀볶음',
        '브로콜리와 달걀로 만드는 건강한 볶음 요리',
        '기타', 'easy', 15, 2,
        '[{"order":1,"description":"브로콜리를 작은 송이로 나눠 끓는 물에 1분간 데칩니다."},{"order":2,"description":"팬에 기름을 두르고 다진 마늘을 볶습니다."},{"order":3,"description":"브로콜리를 넣고 센 불에서 볶습니다."},{"order":4,"description":"달걀 2개를 풀어 넣고 간장, 굴소스로 간을 맞춥니다."},{"order":5,"description":"참기름을 뿌려 마무리합니다."}]'::jsonb,
        '{"calories":160,"protein":12,"fat":8,"carbs":10}'::jsonb,
        ARRAY['기타', '달걀', '채소', '건강', '간단'],
        'manual'
    ),
    (
        '참치마요덮밥',
        '참치캔과 마요네즈로 만드는 간단한 덮밥',
        '기타', 'easy', 10, 1,
        '[{"order":1,"description":"참치캔의 기름을 빼줍니다."},{"order":2,"description":"참치에 마요네즈 2큰술, 간장 0.5큰술, 후추를 넣고 섞습니다."},{"order":3,"description":"따뜻한 밥 위에 참치마요를 올립니다."},{"order":4,"description":"오이를 얇게 썰어 곁들입니다."},{"order":5,"description":"김가루와 참깨를 뿌려 완성합니다."}]'::jsonb,
        '{"calories":420,"protein":22,"fat":16,"carbs":50}'::jsonb,
        ARRAY['기타', '밥', '참치', '간단'],
        'manual'
    ),
    (
        '버섯볶음',
        '다양한 버섯으로 만드는 감칠맛 가득한 버섯볶음',
        '한식', 'easy', 15, 2,
        '[{"order":1,"description":"표고버섯, 새송이버섯, 느타리버섯을 먹기 좋게 자릅니다."},{"order":2,"description":"팬에 버터를 녹이고 다진 마늘을 볶습니다."},{"order":3,"description":"버섯을 넣고 센 불에서 볶습니다."},{"order":4,"description":"간장 1큰술, 굴소스 0.5큰술로 간을 맞춥니다."},{"order":5,"description":"후추와 파슬리를 뿌려 완성합니다."}]'::jsonb,
        '{"calories":100,"protein":5,"fat":5,"carbs":10}'::jsonb,
        ARRAY['한식', '채소', '버섯', '볶음'],
        'manual'
    )

ON CONFLICT (title) DO NOTHING;
