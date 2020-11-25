from urllib.request import urlopen # 인터넷 url를 열어주는 패키지
from urllib.parse import quote_plus # 한글을 유니코드 형식으로 변환해줌
from selenium import webdriver
from bs4 import BeautifulSoup
import time
import re 
import unicodedata
import pandas as pd
from pandas import DataFrame
import urllib.request
import os
import argparse
from datetime import datetime

#함수 작성
def insta_searching(word):  #word라는 매개변수를 받는 insta_searching 이라는 함수 생성
    url = 'https://www.instagram.com/explore/tags/' + word
    return url

def select_first(driver):
    first = driver.find_element_by_xpath('//*[@id="react-root"]/section/main/article/div[2]/div/div[1]/div[1]/a/div/div[2]')
    #find_element_by_css_selector 함수를 사용해 요소 찾기
    first.click()
    time.sleep(3) #로딩을 위해 3초 대기

def get_content(driver):
    # 1. 현재 페이지의 HTML 정보 가져오기
    html = driver.page_source
    soup = BeautifulSoup(html, 'html5lib')    

    try:
        # # 2. 본문 내용 가져오기 
        try:        
            content = soup.select('div.C4VMK > span')[0].text 
                        #첫 게시글 본문 내용이 <div class="C4VMK"> 임을 알 수 있다.
                                    #태그명이 div, class명이 C4VMK인 태그 아래에 있는 span 태그를 모두 선택.
            content = unicodedata.normalize('NFC', content)

        except:
            content = ' ' 
        
        content_tags = re.findall(r'#[^\s#,\\]+', content)

        # 3. 해시태그 가져오기(정규표현식 활용) 

        if(len(soup.select('div.C4VMK > span')) == 0): #게시글,댓글 없이 사진만 있는 경우
            comment = ''
        
        try: # 숨겨진 댓글에서 태그 찾기 
            xpath3 = '/html/body/div[5]/div[2]/div/article/div[3]/div[1]/ul/ul[1]/li/ul/li/div/button'

            driver.find_element_by_xpath(xpath3).click()
            
            xpath4 = '/html/body/div[5]/div[2]/div/article/div[3]/div[1]/ul/ul[1]/li/ul/div/li/div/div[1]/div[2]/span' 

            comment = driver.find_element_by_xpath(xpath4).text
            comment = unicodedata.normalize('NFC', comment)
            time.sleep(1) 
            
        except: #숨겨진 댓글이 없는 경우, 댓글 하나하나 확인하고 태그 찾기 
            try:    
                for i  in range(len(soup.select('div.C4VMK > span'))):
                    comment = soup.select('div.C4VMK > span')[i].text
                    comment = unicodedata.normalize('NFC', comment) #엑셀 데이터 깨짐 방지

                    if '#' in comment:
                        break
            except: 
                comment = '' 

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
    
        # 7. img_description 저장하기
        try:
            img_des_xpath = '/html/body/div[5]/div[2]/div/article/div[2]/div/div/div[1]/img'
            img_des_tmp = driver.find_element_by_xpath(img_des_xpath)
            img_des = img_des_tmp.get_attribute("alt")

            # print(img_des)

        except:
            try:
                img_des_xpath = '/html/body/div[5]/div[2]/div/article/div[2]/div/div[1]/div[2]/div/div/div/ul/li[2]/div/div/div/div[1]/img'
                img_des_tmp = driver.find_element_by_xpath(img_des_xpath)
                img_des = img_des_tmp.get_attribute("alt")
                
                # print(img_des) 
            except:
                img_des = '' 

        # 8. img src 저장하기
        try:
            img_list = []
            xpath5 = '/html/body/div[5]/div[2]/div/article/div[2]/div/div/div[1]/img'
            imgs_tmp = driver.find_element_by_xpath(xpath5)
            imgs = imgs_tmp.get_attribute("src")
        
        except:
            try:
                xpath5 = '/html/body/div[5]/div[2]/div/article/div[2]/div/div/div[1]/div[1]/img'
                imgs_tmp = driver.find_element_by_xpath(xpath5)
                imgs = imgs_tmp.get_attribute("src")
                
            except:
                try:
                    xpath5 = '/html/body/div[5]/div[2]/div/article/div[2]/div/div[1]/div[2]/div/div/div/ul/li[2]/div/div/div/div[1]/img'
                    imgs_tmp = driver.find_element_by_xpath(xpath5)
                    imgs = imgs_tmp.get_attribute("src")
                except:
                    imgs = ''
       
        # 9. 수집한 정보 저장하기
        data = [content, date, like, place, content_tags, comment_tags, imgs]
        return data 
    
    except:
        data = ''
        return data

def move_next(driver):
    right = driver.find_element_by_css_selector('a._65Bje.coreSpriteRightPaginationArrow') 
    right.click()
    time.sleep(3)


#1. 크롬으로 인스타그램 - ' 지역 ' 검색
def crwaling(tag, day):
    driver = webdriver.Chrome()
    url = insta_searching(tag)
    driver.get(url) 
    time.sleep(4) 

    #2. 로그인 하기
    id = 'hhhhhjjjjj96'
    password = 'z1x2c3'

    # 페이지 변경 아래 2코드 필요 없음
    #login_section = '//*[@id="react-root"]/section/nav/div[2]/div/div/div[3]/div/span/a[1]/button'
    #driver.find_element_by_xpath(login_section).click()

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

    # 알림설정 창 뜨면..
    try:
        alert_xpath = '/html/body/div[4]/div/div/div/div[3]/button[2]'
        driver.find_element_by_xpath(alert_xpath).click()
    except:
        print('알림설정-나중에 하기')
        

    driver.get(url)
    time.sleep(3) 

    #4. 첫번째 게시글 열기
    select_first(driver) 

    #5. 비어있는 변수(results) 만들기
    results = [] 

    now = datetime.today().date()

    df = pd.DataFrame(results ,columns = ['Content' , 'Date', 'Like', 'Place', 'Content_tags', 'Comment_tags', 'imgs'])
    df.to_csv('{}_{}.csv'.format(now, tag), index=False, encoding='utf-8-sig')

    #여러 게시물 크롤링하기
    num = 0 #이미지 넘버링
    cnt = 0
    while True:
        data = get_content(driver) #게시물 정보 가져오기

        dt = datetime.strptime(data[1], "%Y-%m-%d").date()
        if (now - dt).days > day: #날짜차이까지만 가져오기
            cnt+=1
            print(cnt)
            if cnt > 5:
                break
            continue
        else:
            cnt=0

        imgUrl = data[6] # 이미지 저장을 위한, 이미지 소스 데이터 가져오기            
        num += 1            
        if(imgUrl !=''):
            #print(imgUrl)
            path = "./{}_{}사진".format(now, tag)
            if not os.path.isdir(path):                                                           
                os.mkdir(path)   
            urllib.request.urlretrieve(imgUrl, path+'/insta_'+str(num)+'.png') 

        if((num) % 10 == 0): # 10개씩 정보 저장
            df = pd.DataFrame(results)
            df.to_csv('{}_{}.csv'.format(now, tag), index=False, encoding='utf-8-sig', mode = 'a', header = False)
            results.clear()

        results.append(data)
        move_next(driver)

    print('총 게시물 수 : {}'.format(num))
        
if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Instagram Crawler")
    parser.add_argument("-t", "--tag", help="instagram's tag name", dest = 'tag')
    parser.add_argument("-d", "--day", type=int, help="날짜 차이만큼 가져오기", dest = 'day')

    args = parser.parse_args()

    usage = "example python img_save.py -t '삼청동' -d 1"

    if args.tag is None or args.day is None:
        print(usage)
    else:
        crwaling(args.tag, args.day)
    
