# MiniClinic 社區診所掛號系統

一個以 Spring Boot 實作的社區診所掛號系統，支援醫師登入、病患掛號、
掛號狀態管理等功能。

## 線上 Demo

https://miniclinic-你的帳號.onrender.com

## 技術棧

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Thymeleaf
- SQLite（開發）/ PostgreSQL（部署）
- BCrypt / Spring Security Crypto（密碼雜湊）

## 功能清單

- 醫師登入 / 登出
- 醫師個人 Dashboard：檢視今日掛號，即時更新掛號狀態（看診完成、取消）
- 醫師帳號管理：支援線上修改密碼（包含舊密碼驗證與強度檢查）
- 病患資料管理（CRUD）
- 線上掛號功能
- 掛號狀態變更（booked / completed / cancelled）
- 系統統計摘要 API：公開的資料彙整端點

## 本機執行

```bash
git clone https://github.com/Jane-917/miniclinic.git
cd miniclinic
./mvnw spring-boot:run
```

開啟瀏覽器訪問 http://localhost:8080

預設醫師帳密：

- D001 / pass1234
- D002 / pass1234
- （其他醫師密碼均為 pass1234）

## 重要 API 端點

### 1. 系統統計摘要 (公開)
- **路徑**: `GET /api/stats`
- **說明**: 回傳醫師、病患總數及各狀態掛號統計。
- **格式**: JSON

### 2. 掛號狀態更新 (需登入)
- **路徑**: `PUT /api/appointments/{id}/status`
- **說明**: 變更掛號狀態為 `COMPLETED` 或 `CANCELLED`。
- **參數**: `{"status": "..."}`

### 3. 修改密碼 (需登入)
- **路徑**: `POST /password`
- **驗證**: 
  - 必須輸入正確的舊密碼。
  - 新密碼長度需至少 8 碼。
  - 兩次新密碼輸入必須一致。

## 資料初始化

第一次啟動時，`data.sql` 會自動插入：
- 5 位虛構醫師
- 3 位虛構病患（TEST00001, TEST00002, TEST00003）
- 3 筆示範掛號

## 專案結構

```
src/
├── main/
│   ├── java/tw/edu/fju/miniclinic/
│   │   ├── controller/     # HTTP 請求處理
│   │   ├── model/          # Entity 與 Repository
│   │   ├── interceptor/    # 登入驗證
│   │   └── config/         # Spring MVC 與攔截器配置
│   └── resources/
│       ├── templates/      # Thymeleaf 模板
│       ├── static/         # CSS、JS
│       └── application.properties
```

## 作者

2026 年 Java 程式設計課程作業

## 聲明

所有病患資料均為虛構，僅供教學使用。