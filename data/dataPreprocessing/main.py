import pandas as pd

# input, output 설정
filePath = "../datasets/"
fileName = "전국가로수길정보표준데이터"
fileExtension = ".csv"
df = pd.read_csv(filePath+fileName+fileExtension, encoding='cp949')


# 방법1
def filter_by_number_of_trees():
    # n개 이하면 자른다!
    n = 50
    df2.drop(df2[(df2.가로수수량 < n)].index, inplace=True)
    return filePath + fileName + "(방법1-" + str(n) + "개 이하는 제거)"


# 방법2
def filter_by_garosu_jisu():
    # (그루수 / 거리수)가 n 이하면 자른다!
    # n = 5m당 1개(200개/1km)
    n = 200
    df2.drop(df2[(df2.가로수수량 / df2.가로수길길이 < n)].index, inplace=True)
    return filePath + fileName + "(방법2-1km당 " + str(n) + "개 이하는 제거)"


# 쓸 column만 뽑아서
df2 = df.loc[:,
      ['가로수길명', '가로수길시작위도', '가로수길시작경도', '가로수길종료위도', '가로수길종료경도', '가로수종류', '가로수수량', '가로수길길이', '도로명', '도로구간', '제공기관명']]
# 하나라도 없으면 제거
df2.dropna(inplace=True)
# {가로수길명, 도로명, 도로구간} == '도로명없음' 제거
df2.drop(df2[(df2.가로수길명 == '도로명없음') | (df2.도로명 == '도로명없음') | (df2.도로구간 == '도로명없음')].index, inplace=True)
# {가로수종류} == '없음' 제거
df2.drop(df2[(df2.가로수종류 == '없음')].index, inplace=True)
# {가로수수량} == 0 제거
df2.drop(df2[(df2.가로수수량 == 0)].index, inplace=True)
# 길이가 100 이상이면 m로 적은걸로 판별하고 km단위로 수정.
df2.loc[100 < df2['가로수길길이'], '가로수길길이'] = df2['가로수길길이'].apply(lambda x: x * 0.001)

# 나무의 그루 수가 적으면?
filterBy = 2
if filterBy == 0:
    finalFileName = filter_by_number_of_trees()
elif filterBy == 1:
    finalFileName = filter_by_garosu_jisu()
else:
    finalFileName = filePath+fileName+"(쓰레기 값 제거)"+fileExtension
# 전처리 데이터 출력
df2.to_csv(finalFileName, encoding='euc-kr', index=False)
