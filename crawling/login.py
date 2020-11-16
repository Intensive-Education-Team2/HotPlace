from urllib.request import urlopen # 인터넷 url를 열어주는 패키지
from urllib.parse import quote_plus # 한글을 유니코드 형식으로 변환해줌
from selenium import webdriver
from bs4 import BeautifulSoup
import time
import re 
import pandas as pd
import unicodedata

#함수 작성
def insta_searching(word):  #word라는 매개변수를 받는 insta_searching 이라는 함수 생성
    url = 'https://www.instagram.com/explore/tags/' + word
    return url

def select_first(driver):
    first = driver.find_element_by_css_selector('div._9AhH0') 
    #find_element_by_css_selector 함수를 사용해 요소 찾기
    first.click()
    time.sleep(3) #로딩을 위해 3초 대기

def get_content(driver):
    # 1. 현재 페이지의 HTML 정보 가져오기
    html = driver.page_source
    soup = BeautifulSoup(html, 'html5lib')    
   
    # # 2. 본문 내용 가져오기 
    try:  			
        content = soup.select('div.C4VMK > span')[0].text 
        			#첫 게시글 본문 내용이 <div class="C4VMK"> 임을 알 수 있다.
                                #태그명이 div, class명이 C4VMK인 태그 아래에 있는 span 태그를 모두 선택.
        content = unicodedata.normalize('NFC', content)

    except:
        content = ' ' 
   
    # 3. 해시태그 가져오기(정규표현식 활용) 
    try:       #댓글에 있는 태그 찾기 
        for i  in range(len(soup.select('div.C4VMK > span'))):
            comment = soup.select('div.C4VMK > span')[i].text
            comment = unicodedata.normalize('NFC', comment)

            if '#' in comment:
                break
    except: 
        comment = '' 
    
    if(len(soup.select('div.C4VMK > span')) == 0):
        comment = ''
    
    try: # 숨겨진 댓글에서 태그 찾기 
        xpath3 = '/html/body/div[5]/div[2]/div/article/div[3]/div[1]/ul/ul[1]/li/ul/li/div/button'

        driver.find_element_by_xpath(xpath3).click()
        time.sleep(1)
        
        tmp = driver.find_elements_by_xpath('/html/body/div[5]/div[2]/div/article/div[3]/div[1]/ul/ul[1]/li/ul/div/li/div/div[1]/div[2]/span')

        for i  in range(len(tmp)):
            comment = tmp[i].text
            comment = unicodedata.normalize('NFC', comment)

            if '#' in comment:
                break
        time.sleep(1) 
        
        print(comment)
        
    except:
        print('asd')
    

    comment_tags = re.findall(r'#[^\s#,\\]+', comment) 
   
    # 4. 작성 일자 가져오기
    try:
        date = soup.select('time._1o9PC.Nzb55')[0]['datetime'][:10] #앞에서부터 10자리 글자

    except:
        date = ''
   
    # 5. 좋아요 수 가져오기
    try:
        like = soup.select('div.Nm9Fw > button')[0].text[4:-1] 
    except:
        like = 0
   
    # 6. 위치 정보 가져오기
    try:
        place = soup.select('div.JF9hh')[0].text
    except:
        place = ''
   
    # 7. 수집한 정보 저장하기
    data = [content, date, like, place, comment_tags]
    return data 

def move_next(driver):
    right = driver.find_element_by_css_selector('a._65Bje.coreSpriteRightPaginationArrow') 
    right.click()
    time.sleep(3)


#1. 크롬으로 인스타그램 - '사당맛집' 검색
driver = webdriver.Chrome()
word = '가로수길'
url = insta_searching(word)
driver.get(url) 
time.sleep(4) 

#2. 로그인 하기
id = 'hhhhhhjjjjjj96'
password = 'z1x2c3' 
login_section = '//*[@id="react-root"]/section/nav/div[2]/div/div/div[3]/div/span/a[1]/button'

driver.find_element_by_xpath(login_section).click()

time.sleep(3) 

elem_login = driver.find_element_by_name("username")
elem_login.clear()
elem_login.send_keys(id) 

elem_login = driver.find_element_by_name('password')
elem_login.clear()
elem_login.send_keys(password) 

time.sleep(1) 


#xpath 경우, 해당 위치소스코드에서 오른쪽 마우스 이용해 copy- copy xpath 해서 가져오기 
driver.find_element_by_xpath('//*[@id="loginForm"]/div/div[3]/button').submit() 

time.sleep(3) 

driver.find_element_by_xpath('//*[@id="react-root"]/section/main/div/div/div/section/div/button').click()

#3. 검색페이지 접속하기
driver.get(url)
time.sleep(4) 

#4. 첫번째 게시글 열기
select_first(driver) 

#5. 비어있는 변수(results) 만들기
results = [] 

#여러 게시물 크롤링하기
target = 10 #크롤링할 게시물 수
for i in range(target):
    data = get_content(driver) #게시물 정보 가져오기

    #result안에 들어있는 key 값 비교 후, 동일한 key 있으면 result 배열에 추가 하지 않기. 
    results.append(data)
    move_next(driver)    
#print(results[:2])

import pandas as pd
from pandas import DataFrame

## XlsxWriter 엔진으로 Pandas writer 객체 만들기
writer = pd.ExcelWriter('C:\\Users\\phz96\\Desktop\\tmp\\craw.xlsx', engine='xlsxwriter')
 
#list to dateframe  
df = pd.DataFrame(results ,columns = ['Content' , 'Date', 'Like', 'Place', 'Comment_tags'])

## DataFrame을 xlsx에 쓰기
df.to_excel(writer, sheet_name='Sheet1')
 
## Pandas writer 객체 닫기
writer.close()

