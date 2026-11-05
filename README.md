# talection_backend
NTNU Talection Backend repo

## Google sign-in

The backend verifies Google ID tokens against the OAuth client ID in `GOOGLE_CLIENT_ID`. Set it in `.env` when running locally, or as an environment variable in production:

```properties
GOOGLE_CLIENT_ID=<client id>.apps.googleusercontent.com
```

It must be the same client ID as `VITE_GOOGLE_CLIENT_ID` in the frontend. The frontend README describes how to create it in the Google Cloud Console.

When `GOOGLE_CLIENT_ID` is not set, Google sign-in and sign-up respond with `500 Google auth is not configured`. The rest of the backend works as normal.
