# Post Composer — Experiment 1.1

A React implementation of **Experiment 1.1: Post Composer with Platform Validation**
(Unit 1). Covers controlled components, dynamic per-platform character limits,
real-time validation, and the Strategy Design Pattern.

## Features

- Platform selection dropdown (Twitter/X, LinkedIn, Instagram)
- Dynamic character limits per platform
- Real-time validation with clear error messages (blocks posting until valid)
- Strategy Pattern for validation logic (`src/utils/validationStrategies.js`) —
  adding a new platform only means adding a new entry to that file
- Toast notifications for success/error feedback

## Project structure

```
post-composer/
├── index.html
├── package.json
├── vite.config.js
└── src/
    ├── main.jsx
    ├── App.jsx
    ├── styles.css
    ├── components/
    │   └── PostComposer.jsx
    └── utils/
        └── validationStrategies.js
```

## Running it

Requires Node.js 18+.

```bash
npm install
npm run dev
```

Then open the URL Vite prints (usually http://localhost:5173).

To build a production bundle:

```bash
npm run build
npm run preview
```

## Adding a new platform

Open `src/utils/validationStrategies.js` and add a new key to
`platformStrategies`, e.g.:

```js
facebook: {
  label: "Facebook",
  limit: 63206,
  validate(content) {
    if (!content.trim()) return { valid: false, message: "Post content cannot be empty." };
    if (content.length > 63206) return { valid: false, message: "Exceeds Facebook limit." };
    return { valid: true, message: "" };
  },
},
```

It will automatically appear in the platform dropdown with its own limit and
validation rules — no other file needs to change.
