# Online Supermarket — Progress Notes

Course project for תב"א - תכנות בסביבת אינטרנט (assignment PDF: `C:\Users\User\Desktop\8200 Speedrun\Work that isnt code\תבא - java.pdf` — **not found there anymore as of 2026-10-21**, location unknown). Plain Servlets + JSP on Tomcat, MySQL for storage. No frameworks, intentionally simple UI (not fancy).

## ▶ START HERE NEXT SESSION

Last session: 2026-10-21. Phases 1–7 done. **Next: Phase 8 — Cart**, starting with the session "wristband": in `AuthServlet.doPost`, right after `if (rs.next()){`, add `request.getSession().setAttribute("username", username);` (explained once already on 2026-10-21 — quick recap, then continue). Then: `cart_items` table → "Add to cart" button on `products.html` → `cart.html` → "Buy" lowers stock + empties the cart.

Test users: `uriah` / `1234` (shopper), `admin` / `admin123` (admin), plus any sign-ups. Remember to start MySQL first (`Start-Service MySQL80`, admin PowerShell).

## How to work with me (read this first, new session)

- **I (the user) write all the code myself.** Claude teaches/guides/reviews, does not write the project's code for me unless I explicitly ask.
- **I'm new to HTML/CSS/JS/HTTP/SQL** (know Java only). Concepts are taught **just-in-time**, one small piece at a time — not big multi-concept dumps. Wait for me to confirm each small step before introducing the next.
- **Pace (2026-10-21):** big chunks were too much. **Small steps, slightly faster:** a couple of related lines per step with a short explanation — never a long paragraph/multi-part change that leaves me lost.
- I batch a few small edits before rebuilding/redeploying (rebuilding after every single line is too slow), but still want one concept explained at a time.
- **Every time a rebuild is needed, print ALL the deploy commands in order** (full list from the "Deploy cycle" section below), ready to copy — don't just say "do the usual deploy cycle".

## Site structure (decided 2026-10-21)

Only these pages, no general nav bar:
1. `index.html` — login (first page you see)
2. `products.html` — storefront (will get one "Cart" link)
3. `cart.html` — the cart (Phase 8; will get a "Back to store" link)
4. `admin.html` — only for users whose `is_admin` column is true; `AuthServlet` sends admins here, everyone else to `products.html`

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
- [x] **Phase 5 — render products on `index.html` + minimal CSS.** *Done 2026-10-21.*
  - [x] JS part: the `<script>` at the bottom of `index.html` already fetches `/products` and renders them as a `<ul>` list into `#products`. Went through it line by line (session 2026-10-02) — see "JavaScript walkthrough" below.
  - [x] CSS started: created `src/main/webapp/style.css`, linked it from `index.html` `<head>` with `<link rel="stylesheet" href="style.css">`. Current rules: `body` (Arial font, lightyellow background), `h1` (darkgreen text), `#products` (white text, black background, 10px padding). Verified in browser.
  - [x] Fixed the "No suitable driver" bug in `Db.java` (session 2026-10-21) — see "Known bug" below. Verified: products still show after a Tomcat restart.
  - [x] Nav links styled (2026-10-21): `a { color: darkgreen; text-decoration: none; margin-right: 15px; }`. Verified in browser (needed Ctrl+F5 — browser had cached the old `style.css`). `login_area` styling left for Phase 6, once it has content.
- [x] **Phase 6 — login form + `AuthServlet.doPost` + sign up.** *Done 2026-10-21* (session postponed to Phase 8).
  - [x] Login form in `index.html` `#login_area`: `<form action="/app-1.0-SNAPSHOT/auth" method="post">` with `username` text input, `password` input (`type="password"`), submit button. Learned: `action` = where to send (address on the envelope), `method="post"` = hidden in the request (envelope) vs GET = in the URL (postcard).
  - [x] `AuthServlet.doPost` reads both with `request.getParameter(...)`; temporary test line `response.getWriter().write("Got username: " + username);` — verified in browser (2026-10-21). Yellow underline = warning (unused variable), not an error.
  - [x] Users inserted (2026-10-21): `uriah` / `1234` (is_admin 0) and `admin` / `admin123` (is_admin 1). MySQL shows booleans as 0/1.
  - [x] `AuthServlet.doPost` now: `try (Connection conn = Db.getConnection())` → `PreparedStatement` with `SELECT is_admin FROM users WHERE username = ? AND password = ?` → `setString(1, username)` / `setString(2, password)` → `ResultSet rs = stmt.executeQuery()`. Learned: `WHERE`, `?` blanks (protect against SQL injection), blanks count from 1, `AND` checks both on the same row, `rs.next()` = "finger" moves down one row, true if it landed on one. One combined "Wrong username or password" message on purpose (don't reveal which usernames exist).
  - [x] `if (rs.next()) { write "logged in" } else { write "wrong username or password." }` — tested 2026-10-21: right password, wrong password, unknown user all behave correctly. (Always use `{ }` on `else` even for one line.)
  - [x] Redirects (tested 2026-10-21): on match, `if (rs.getBoolean("is_admin"))` → `response.sendRedirect("admin.html")`, else → `products.html`; wrong login still writes the message. Gotchas hit: stray `;` after `if (...)` breaks the if/else; every `.java` change needs the full rebuild cycle.
  - [x] **Sign up** (done + tested 2026-10-21): second form in `index.html` `#login_area` → `/app-1.0-SNAPSHOT/signup` (POST). New `SignupServlet.java` (`@WebServlet("/signup")`, only `doPost`): `INSERT INTO users (username, password, is_admin) VALUES (?, ?, false)` via `PreparedStatement` + `executeUpdate()` (for changes; `executeQuery()` is for SELECT), then `sendRedirect("index.html")`. Learned: missing `@WebServlet` = no red but 404. Possible tweak: redirect to `products.html` to skip the second login. Maybe later: stop duplicate usernames.
  - [ ] Session (`request.getSession().setAttribute("username", username)` on login) — postponed to Phase 8 (Cart), which needs it to know whose cart is whose.
  - [ ] Then: store user in `HttpSession` on success.
  - [x] **Page split (2026-10-21):** `index.html` is now the LOGIN page (nav + `#login_area` form only, no script). New `products.html` = the shop page (nav + `#products` div + the fetch script). No `web.xml` change needed — `index.html` is already the welcome page.
  - [~] ~~Login gate~~ — DROPPED (my decision 2026-10-21): no need to block typing `products.html` directly.
- [x] **Phase 7 — admin add/delete products.** *Done 2026-10-21.*
  - [x] **Add product** (done + tested 2026-10-21): `admin.html` form → `/app-1.0-SNAPSHOT/admin` POST, placeholders, `step="0.01"` on price (number boxes are whole-numbers-only by default). `AdminServlet.doPost`: `Double.parseDouble` / `Integer.parseInt` (getParameter always returns String), `INSERT INTO products (name, price, stock) VALUES (?, ?, ?)` with `setString`/`setDouble`/`setInt`, `executeUpdate()`, redirect to `admin.html`. Tested: added "kidney beans", showed on storefront.
  - [x] **Delete product** (done + tested 2026-10-21): `ProductsServlet` now selects `id` and puts `"id":...` first in each JSON object. `admin.html` has the products script (at the bottom, before `</body>`, targeting `#product_list`); each `<li>` has its own small form → `/app-1.0-SNAPSHOT/delete` with `<input type="hidden" name="id" value="${product.id}">` + Delete button (hidden = sent but not shown). New `DeleteServlet` (`@WebServlet("/delete")`, in `com/name/app/`): `Integer.parseInt(getParameter("id"))`, `DELETE FROM products WHERE id = ?` + `setInt`, redirect to `admin.html`. Never run DELETE without WHERE. Tested: deleted "kidney beans".
- [ ] **Phase 8 — Cart** ← NEXT (added 2026-10-21 at my request — not in the original plan). Logged-in users get an "Add to cart" button per product; each user has their own cart; a "Buy" button inside the cart lowers `stock` for each item and empties the cart. Planned design: new MySQL table `cart_items` (user id, product id, quantity) so carts survive logout; reuses forms/POST (Phase 6–7) + session to know which user.
- [ ] Phase 9 — wire up remaining nav links, final polish, full click-through test.
- [ ] **Phase 10 — "Explain it back" review** (my request 2026-10-21). We go through **every file** in the project one by one, and **I explain it to Claude** in my own words. Claude checks my understanding and fills any gaps — no new code, just making sure I understand everything.

## Known bug: "No suitable driver found" (FIXED 2026-10-21)

**Symptom:** products box on `index.html` is empty (with `#products` styled it shows as a thin black stripe). Browser console (F12 → Console) says `SyntaxError: Unexpected token '<', "<!doctype "... is not valid JSON`. Tomcat log (`C:\apache-tomcat-9.0.118\logs\localhost.<date>.log`) says `java.sql.SQLException: No suitable driver found for jdbc:mysql://localhost:3306/supermarket`.

**Why:** `DriverManager` builds its list of known drivers the first time it's used. Tomcat uses it early while starting up — *before* our app (and the MySQL driver jar inside our `.war`) is loaded — so the MySQL driver never gets on the list. (`DbTest.java` worked because it ran as a standalone program where the driver was visible from the start.) It sometimes starts working by itself later, by luck — don't rely on that; it tends to come back after each Tomcat restart. Ctrl+F5 does NOT fix it (that only refreshes cached files like `style.css`; this error is on the server).

**Analogy:** the receptionist (`DriverManager`) wrote the guest list before we arrived, so we have to walk up and introduce ourselves.

**Fix (done):** in `Db.java`, load the driver class explicitly before connecting:
```java
public static Connection getConnection() throws SQLException {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        throw new SQLException(e);
    }
    return DriverManager.getConnection("jdbc:mysql://localhost:3306/supermarket", "root", "1234");
}
```
`Class.forName(...)` loads the driver class, which registers itself with `DriverManager`. The try/catch is because `ClassNotFoundException` is a checked exception — wrapped in `SQLException`, which the method already declares.

## Debugging checklist (learned 2026-10-02)

1. Is MySQL running? `Get-Service MySQL80` (start it via Services app → MySQL80 → Start, or `Start-Service MySQL80` in an admin PowerShell). Tip: set Startup type to Automatic to stop having to do this.
2. Is Tomcat running? Open `http://localhost:8080/app-1.0-SNAPSHOT/products` — should show JSON.
3. Browser console: F12 → Console tab → Ctrl+F5. Red text = JS errors.
4. Tomcat logs: `C:\apache-tomcat-9.0.118\logs\` — `localhost_access_log.<date>.txt` shows each request + status code (200 = OK, 500 = server error), `localhost.<date>.log` shows Java exceptions.

## JavaScript walkthrough (index.html `<script>`)

- `<script>` at the **bottom** of `<body>` so the `#products` div already exists when it runs (browser reads top to bottom).
- `fetch('/app-1.0-SNAPSHOT/products')` — browser asks the server (our `ProductsServlet`) for the data.
- `.then(...)` — "wait until the answer arrives, *then* do this."
- `response => response.json()` — arrow function (like a Java/Python lambda, `=>` instead of `->`); `.json()` turns the response text into real data (like Python `json.loads`).
- `.then(data => { ... })` — `data` is now the list of products.
- `let html = '<ul>';` — variable holding HTML text; `<ul>` = bullet list.
- `data.forEach(product => { ... })` — loop, like Python `for product in data:`.
- `` html += `<li>${product.name} ...</li>` `` — `<li>` = one list item; backticks + `${}` = template string, like a Python f-string / Java `String.format`.
- `html += '</ul>';` — close the list.
- `document.getElementById('products').innerHTML = html;` — find the tag with id `products`, put our list inside it.
- Whole flow: **MySQL → ProductsServlet (Java, on the server) → JSON text → fetch (JS, in the browser) → list on the page.**

## CSS basics learned

- HTML = what's on the page, JS = doing things, CSS = how it looks. HTML/CSS/JS run in the browser; Java runs on the server.
- CSS lives in its own file, connected via `<link rel="stylesheet" href="style.css">` inside `<head>` (browser sees it and fetches that file too).
- Rule shape: `selector { property: value; }` — several properties allowed per rule.
- Selectors: tag name (`body`, `h1`) styles every such tag; `#id` (`#products`) styles just the one tag with that id.
- `a` selector = every link. `text-decoration: none` removes the underline. `margin` = space *outside* a box (vs `padding` = inside); `margin-right` = only on the right side.
- After a CSS-only change, always **Ctrl+F5** — plain reload may keep the old cached `style.css` even though the server has the new one.
- Properties used: `font-family`, `background-color` (color behind), `color` (text color — default is black, so black background needs a light `color`!), `padding` (space *inside* a box), units in `px`.

## Concepts already covered (don't re-teach from scratch, just remind if needed)

Client/server model, what Tomcat does, what a `.war` is, HTML document structure + `div`/`a`/`form`/`input`/`button`, `id` vs `name` attributes, sibling vs nested elements, SQL `CREATE DATABASE`/`USE`/`CREATE TABLE`/`ALTER TABLE`/`INSERT`/`SELECT`, primary keys & auto-increment, JDBC `Connection`/`DriverManager`/`Statement`/`ResultSet`, Java checked exceptions (`throws`).
