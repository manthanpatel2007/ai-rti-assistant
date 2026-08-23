const API_BASE =
  import.meta.env.VITE_API_BASE_URL ||
  'http://localhost:8080';


// =========================================================
// TOKEN HELPERS
// =========================================================

const getAccessToken = () =>
  localStorage.getItem('rti_token');

const getRefreshToken = () =>
  localStorage.getItem('rti_refresh_token');

const saveTokens = (data) => {
  if (data?.token) {
    localStorage.setItem('rti_token', data.token);
  }

  if (data?.refreshToken) {
    localStorage.setItem(
      'rti_refresh_token',
      data.refreshToken
    );
  }
};

const clearTokens = () => {
  localStorage.removeItem('rti_token');
  localStorage.removeItem('rti_refresh_token');
};


// =========================================================
// REFRESH ACCESS TOKEN
// =========================================================

let refreshPromise = null;

async function refreshAccessToken() {

  const refreshToken = getRefreshToken();

  if (!refreshToken) {
    throw new Error('No refresh token available');
  }

  // Prevent multiple refresh requests
  if (!refreshPromise) {

    refreshPromise = fetch(
      `${API_BASE}/api/auth/refresh-token?refreshToken=${encodeURIComponent(
        refreshToken
      )}`,
      {
        method: 'POST',
      }
    )
      .then(async response => {

        const contentType =
          response.headers.get('content-type') || '';

        const data =
          contentType.includes('application/json')
            ? await response.json()
            : await response.text();

        if (!response.ok) {
          throw new Error(
            typeof data === 'string'
              ? data
              : data.message || 'Session expired'
          );
        }

        return data;
      })
      .then(data => {

        saveTokens(data);

        return data.token;
      })
      .catch(error => {

        clearTokens();

        throw error;
      })
      .finally(() => {

        refreshPromise = null;
      });
  }

  return refreshPromise;
}


// =========================================================
// MAIN REQUEST FUNCTION
// =========================================================

async function request(
  path,
  options = {},
  retry = true
) {

  const token = getAccessToken();

  const headers =
    new Headers(options.headers || {});


  // JSON content type
  if (!(options.body instanceof FormData)) {

    headers.set(
      'Content-Type',
      'application/json'
    );
  }


  // Access token
  if (token) {

    headers.set(
      'Authorization',
      `Bearer ${token}`
    );
  }


  const response =
    await fetch(
      `${API_BASE}${path}`,
      {
        ...options,
        headers,
      }
    );


  // =======================================================
  // ACCESS TOKEN EXPIRED
  // =======================================================

  if (
    response.status === 401 &&
    retry
  ) {

    try {

      const newAccessToken =
        await refreshAccessToken();


      // Retry original request
      const retryHeaders =
        new Headers(options.headers || {});


      if (!(options.body instanceof FormData)) {

        retryHeaders.set(
          'Content-Type',
          'application/json'
        );
      }


      retryHeaders.set(
        'Authorization',
        `Bearer ${newAccessToken}`
      );


      return request(
        path,
        {
          ...options,
          headers: retryHeaders,
        },
        false
      );

    } catch (error) {

      clearTokens();

      window.location.href = '/login';

      throw error;
    }
  }


  // =======================================================
  // RESPONSE PARSING
  // =======================================================

  const contentType =
    response.headers.get('content-type') || '';


  const data =
    contentType.includes('application/json')
      ? await response.json()
      : await response.text();


  // =======================================================
  // ERROR
  // =======================================================

  if (!response.ok) {

    throw new Error(
      typeof data === 'string'
        ? data
        : data.message ||
          'Something went wrong'
    );
  }


  return data;
}


// =========================================================
// API
// =========================================================

export const api = {

  // =======================================================
  // AUTH
  // =======================================================

  register: (body) =>
    request(
      '/api/auth/register',
      {
        method: 'POST',
        body: JSON.stringify(body),
      }
    ),


  verifyOtp: ({ email, otp }) =>
    request(
      `/api/auth/verify-otp?email=${encodeURIComponent(
        email
      )}&otp=${encodeURIComponent(otp)}`,
      {
        method: 'POST',
      }
    ),


  resendOtp: (email) =>
    request(
      `/api/auth/resend-otp?email=${encodeURIComponent(
        email
      )}`,
      {
        method: 'POST',
      }
    ),


  login: async (body) => {

    const data =
      await request(
        '/api/auth/login',
        {
          method: 'POST',
          body: JSON.stringify(body),
        }
      );


    // Save access + refresh token
    saveTokens(data);

    return data;
  },


  forgotPassword: (email) =>
    request(
      '/api/auth/forgot-password',
      {
        method: 'POST',
        body: JSON.stringify({
          email,
        }),
      }
    ),


  resetPassword: (body) =>
    request(
      '/api/auth/reset-password',
      {
        method: 'POST',
        body: JSON.stringify(body),
      }
    ),


  refreshToken: () =>
    refreshAccessToken(),


  logout: async () => {

    const refreshToken =
      getRefreshToken();


    try {

      if (refreshToken) {

        await request(
          `/api/auth/logout?refreshToken=${encodeURIComponent(
            refreshToken
          )}`,
          {
            method: 'POST',
          },
          false
        );
      }

    } finally {

      clearTokens();

      window.location.href = '/login';
    }
  },


  // =======================================================
  // GOVERNMENT
  // =======================================================

  districts: () =>
    request(
      '/api/government/districts'
    ),


  subDistricts: (id) =>
    request(
      `/api/government/districts/${id}/sub-districts`
    ),


  departments: (id) =>
    request(
      `/api/government/sub-districts/${id}/departments`
    ),


  problems: (id) =>
    request(
      `/api/government/departments/${id}/problems`
    ),


  pios: (id) =>
    request(
      `/api/government/departments/${id}/pios`
    ),


  // =======================================================
  // RTI
  // =======================================================

  createRti: (body) =>
    request(
      '/api/rti',
      {
        method: 'POST',
        body: JSON.stringify(body),
      }
    ),


  myRtis: () =>
    request(
      '/api/rti/my'
    ),


  generatePdf: (id) =>
    request(
      `/api/rti/${id}/generate-pdf`,
      {
        method: 'POST',
      }
    ),


  sendRti: (id) =>
    request(
      `/api/rti/${id}/send`,
      {
        method: 'POST',
      }
    ),


  uploadAttachment: (id, file) => {

    const form =
      new FormData();

    form.append(
      'file',
      file
    );

    return request(
      `/api/rti/${id}/attachments`,
      {
        method: 'POST',
        body: form,
      }
    );
  },


  pdfUrl: (
    id,
    mode = 'preview'
  ) =>
    `${API_BASE}/api/rti/${id}/${mode}`,
    
};