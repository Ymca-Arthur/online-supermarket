# Online Supermarket — Progress Notes

Course project for תב"א - תכנות בסביבת אינטרנט (assignment PDF: `C:\Users\User\Desktop\8200 Speedrun\Work that isnt code\תבא - java.pdf`). Plain Servlets + JSP on Tomcat, MySQL for storage. No frameworks, intentionally simple UI (not fancy).

## How to work with me (read this first, new session)

- **I (the user) write all the code myself.** Claude teaches/guides/reviews, does not write the project's code for me unless I explicitly ask.
- **I'm new to HTML/CSS/JS/HTTP/SQL** (know Java only). Concepts are taught **just-in-time**, one small piece at a time — not big multi-concept dumps. Wait for me to confirm each small step before introducing the next.
- I batch a few small edits before rebuilding/redeploying (rebuilding after every single line is too slow), but still want one concept explained at a time.

## Environment / setup decisions

- Tomcat: **`C:\apache-tomcat-9.0.118`** (ignore the other copy at `C:\Users\User\tomcat9` — not used)
- App URL once deployed: `http://localhost:8080/app-1.0-SNAPSHOT/`
- MySQL runs as Windows service **`MySQL80`** — starts *stopped* by default, must be started manually (Services app or `Start-Service MySQL80` as admin) before `mysql -u root -p` or the app will work
- MySQL root password: `1234`
- Database: `supermarket`, with tables:
  - `products`: `id INT PK AUTO_INCREMENT`, `name VARCHAR(100)`, `price DECIMAL(10,2)`, `stock INT`
  - `users`: `id INT PK AUTO_INCREMENT`, `username VARCHAR(30)`, `password VARCHAR(255)`, `is_admin BOOLEAN`
- Java class naming convention in HTML: **underscores**, not hyphens (e.g. `product_list`, `login_area`)
- Running a single Java class in VS Code: right-click the file → **"Run Java"** (more reliable for me than the small gray "Run" codelens above `main`)

## Deploy cycle (used after any change to Java or HTML)

```
mvn clean package
del "C:\apache-tomcat-9.0.118\webapps\app-1.0-SNAPSHOT.war"
rmdir /s /q "C:\apache-tomcat-9.0.118\webapps\app-1.0-SNAPSHOT"
copy target\app-1.0-SNAPSHOT.war C:\apache-tomcat-9.0.118\webapps\
C:\apache-tomcat-9.0.118\bin\shutdown.bat
```
(wait for that console window to close itself, then:)
```
C:\apache-tomcat-9.0.118\bin\startup.bat
```

A faster shortcut exists for static-HTML-only changes (copy the file directly into the exploded `webapps\app-1.0-SNAPSHOT\` folder, skipping the full rebuild) — offered once, declined for now, revisit later if wanted.

## Phase status (plan lives at `C:\Users\User\.claude\plans\wait-remeber-you-not-parsed-moth.md`)

- [x] **Phase 1 — build/deploy pipeline.** Confirmed `mvn clean package` → copy `.war` → Tomcat → browser all works end to end.
- [x] **Phase 2 — HTML structure.**
  - `index.html`: sibling `<div id="products">` and `<div id="login_area">` added under the nav.
  - `admin.html`: `<h1>`, a `<form>` with `name`/`price`/`stock` inputs + submit button, and an empty `<div id="product_list">`.
- [x] **Phase 3 — MySQL + JDBC.** Both tables created (see above). `src/main/java/com/name/app/DbTest.java` is a **throwaway/scratch** standalone class (has its own `main`) proving JDBC connectivity — connects via `DriverManager.getConnection`, ran a real `SELECT 1` through `Statement`/`ResultSet`. Can be deleted once no longer needed; not part of the real app.
- [x] **Phase 4 — ProductsServlet GET + JSON.** `ProductsServlet.doGet` connects via JDBC (same pattern as `DbTest.java`, wrapped in try/catch since `doGet`'s inherited signature can't declare `throws SQLException`), loops all rows with `while(rs.next())`, hand-builds a JSON array with `StringBuilder`, sets `response.setContentType("application/json")`. Confirmed working: `/products` returns all 3 sample products as valid JSON.
- [ ] **Phase 5 — render products on `index.html` + minimal CSS.** *Up next*: vanilla JS `fetch()` call into `#products` div, then a small `style.css` pass.
- [ ] Phase 6 — login form + `AuthServlet.doPost` + session.
- [ ] Phase 7 — admin add/delete products via `AdminServlet`.
- [ ] Phase 8 — wire up remaining nav links, final polish, full click-through test.

## Concepts already covered (don't re-teach from scratch, just remind if needed)

Client/server model, what Tomcat does, what a `.war` is, HTML document structure + `div`/`a`/`form`/`input`/`button`, `id` vs `name` attributes, sibling vs nested elements, SQL `CREATE DATABASE`/`USE`/`CREATE TABLE`/`ALTER TABLE`/`INSERT`/`SELECT`, primary keys & auto-increment, JDBC `Connection`/`DriverManager`/`Statement`/`ResultSet`, Java checked exceptions (`throws`).
