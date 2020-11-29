from selenium import webdriver
from bs4 import BeautifulSoup

import unicodedata
import re
import os
import argparse

import time
from datetime import datetime

import pandas as pd
from pandas import DataFrame

driver = webdriver.Chrome() # 크롬 웹드라이버

instagram_id = 'hhhhhjjjjj96' # wirte your instagram id
instagram_pw = 'z1x2c3v4b5' # write your instagram password

# Tag 검색 함수
def insta_searching(word):  #word라는 매개변수를 받는 insta_searching 이라는 함수 생성
    url = 'https://www.instagram.com/explore/tags/' + word
    return url

# 최근 게시물 선택 함수 (인기 게시물 무시)
def select_first(driver):
    #find_element_by_css_selector 함수를 사용해 요소 찾기
    first = driver.find_element_by_xpath('//*[@id="react-root"]/section/main/article/div[2]/div/div[1]/div[1]/a/div/div[2]')    
    first.click()
    time.sleep(3) #로딩을 위해 3초 대기

# 다음 게시물 이동 함수
def move_next(driver):
    right = driver.find_element_by_css_selector('a._65Bje.coreSpriteRightPaginationArrow') 
    right.click()
    time.sleep(3)

# 게시물 정보 획득 함수
def get_content(driver):
    html = driver.page_source
    soup = BeautifulSoup(html, 'html5lib')    

    try:
        # 1. 게시자 아이디 가져오기
        try:
            xpath2 = '/html/body/div[5]/div[2]/div/article/header/div[2]/div[1]/div[1]/span/a'
            user_profile = driver.find_element_by_xpath(xpath2).text
        except:
            user_profile = ''

        # 2. 본문 내용 가져오기 
        try:
            #첫 게시글 본문 내용이 <div class="C4VMK"> 임을 알 수 있다.
            #태그명이 div, class명이 C4VMK인 태그 아래에 있는 span 태그를 모두 선택.    
            content = soup.select('div.C4VMK > span')[0].text             
            content = unicodedata.normalize('NFC', content)
        except:
            content = '' 
        
        # 3. 해시태그 가져오기(정규표현식 활용) 
        content_tags = re.findall(r'#[^\s#,\\]+', content)

        if(len(soup.select('div.C4VMK > span')) == 0): #게시글,댓글 없이 사진만 있는 경우
            comment = ''
              
        try: # 3.1. 숨겨진 댓글에서 태그 찾기 
            xpath3 = '/html/body/div[5]/div[2]/div/article/div[3]/div[1]/ul/ul[1]/li/ul/li/div/button'
            driver.find_element_by_xpath(xpath3).click()
            
            xpath4 = '/html/body/div[5]/div[2]/div/article/div[3]/div[1]/ul/ul[1]/li/ul/div/li/div/div[1]/div[2]/span' 
            comment = driver.find_element_by_xpath(xpath4).text
            comment = unicodedata.normalize('NFC', comment)
            time.sleep(1) 
            
        except: # 3.2. 숨겨진 댓글이 없는 경우, 댓글 하나하나 확인하고 태그 찾기 
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
            date = soup.select('time._1o9PC.Nzb55')[0]['title']
            date = date.replace("년 ","-").replace("월 ","-").replace("일","")
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

        # 7. img src 저장하기
        try:
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
       
        # 8. 수집한 정보 저장하기
        data = [user_profile, date, content, like, place, content_tags, comment_tags, imgs]
        return data 
    
    except:
        data = ''
        return data

def login(id, pw):
    driver.get('https://www.instagram.com')
    time.sleep(3) 

    elem_login = driver.find_element_by_name("username")
    elem_login.clear()
    elem_login.send_keys(id) 
    elem_login = driver.find_element_by_name('password')
    elem_login.clear()
    elem_login.send_keys(pw)
    elem_login.submit()

    time.sleep(3)
    driver.find_element_by_class_name('cmbtv').click()

    time.sleep(3)    
    try:
        alert_xpath = '/html/body/div[4]/div/div/div/div[3]/button[2]'
        driver.find_element_by_xpath(alert_xpath).click()
    except:
        print('알림설정-나중에 하기')

def is_nan(x):
    return (x != x)

# 1. 크롬으로 인스타그램 - ' 지역, 날짜 ' 검색
def crwaling(tag, day):
    now = datetime.today().date()

    # 2. 로그인
    login(instagram_id,instagram_pw)
    time.sleep(4)

    # 3. Tag 검색
    url = insta_searching(tag)
    driver.get(url)
    time.sleep(4)

    # 4. 첫번째 게시글 열기
    select_first(driver) 

    # 5. 비어있는 변수(results) 만들기
    results = []

    # 6. csv 파일 생성
    df = pd.DataFrame(results ,columns = ['User_Profile', 'Date', 'Content', 'Like', 'Place', 'Content_tags', 'Comment_tags', 'imgs'])
    df.to_csv('./last/{}_{}.csv'.format(now, tag), index=False, encoding='utf-8-sig')

    num = 0 # 총 게시물 수를 위한 변수
    day_cnt = 0 # 날짜 카운트
    user_cnt = 0 # User 카운트
    prev_user = ''
    cur_user = ''

    # 7. 게시물 크롤링하기
    while True:
        data = get_content(driver) # 게시물 정보 가져오기

        cur_user = data[0]
        if prev_user == cur_user:
            user_cnt += 1
            day_cnt -= 1
            if day_cnt == 0 :
                day_cnt = 0
        else:
            user_cnt = 0
        
        if user_cnt > 5:
            move_next(driver)
            prev_user = cur_user
            continue

        try:
            dt = datetime.strptime(data[1], "%Y-%m-%d").date()
        except: 
            dt = now

        # 지정한 날짜가 12번 반복되면 종료
        if (now - dt).days == (day + 1): 
            day_cnt += 1
            #print("이상한 날짜 횟수 {}".format(day_cnt))
            if day_cnt == 12:
                break
            else:
                move_next(driver)
                prev_user = cur_user
                continue
        else:
            day_cnt=0
        
        num += 1
        results.append(data)

        if num % 5 == 0: # 5개씩 csv파일에 저장
            df = pd.DataFrame(results)
            df.to_csv('./last/{}_{}.csv'.format(now, tag), index=False, encoding='utf-8-sig', mode = 'a', header = False)
            results.clear()

        move_next(driver)
        prev_user = cur_user

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
        start = time.time()
        crwaling(args.tag, args.day)
        print("time :", time.time() - start)
    
