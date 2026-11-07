# FuTrack — Job Application Tracker

A responsive, front-end-only job search tracker built with HTML, CSS, and vanilla JavaScript. The interface uses a white and green palette and stores demo applications, profile preferences, and notifications in the browser's LocalStorage.

## Run locally

Open `index.html` in a browser, or start a local static server from this folder:
hosted on vercel:https://job-application-tracker-bay-chi.vercel.app/
```sh
npx serve .
```

Then open the local URL printed by the server. Analytics uses Chart.js from jsDelivr; Google Fonts are also loaded remotely. Start the Spring Boot and PostgreSQL services in `backend/` for live application data. If the API is offline, the frontend keeps using its LocalStorage cache.

## Demo behavior

- Notifications, profile details, and theme preferences persist in LocalStorage. Applications load from the Spring Boot API when it is running and use the LocalStorage cache while it is unavailable.
- Login and registration are mock flows. They do not provide secure authentication.
- The Change password control explains that account service integration is pending.
- Resume selection is available in the application form; binary uploads need a backend or file storage service.
- Clear the site's LocalStorage to start fresh, or use Settings → Reset demo data to restore sample entries.

## Backend integration

`js/api.js` contains the Spring Boot REST client and `js/storage.js` provides the app-facing cache and offline fallback. The application fields (`company`, `title`, `location`, `type`, `mode`, `date`, `status`, `followUp`, recruiter/contact details, salary, and notes) align with the backend DTO. The authentication forms should be connected to Spring Security/JWT before handling real accounts.

The Spring Boot API, PostgreSQL Compose configuration, endpoints, and run steps are documented in [`backend/README.md`](backend/README.md). The importable Postman collection and environment are in `postman/`.

## QA checks

The frontend scripts are plain browser JavaScript. Check syntax with:

```sh
node --check js/storage.js
node --check js/app.js
```
