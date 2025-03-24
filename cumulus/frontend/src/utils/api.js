async function fetchWithAuth(url, options = {}) {
    let accessToken = localStorage.getItem('accessToken');
    options.headers = { ...options.headers, Authorization: `Bearer ${accessToken}` };

    let response = await fetch(url, options);

    console.log('Response:', response.status);
    console.log('Response:', response.statusText);
    console.log(localStorage.getItem('refreshToken'));
    if (response.status === 500) {
        console.log('Access token expired. Attempting refresh...');
        const newToken = await refreshAccessToken();

        if (newToken) {
            options.headers.Authorization = `Bearer ${localStorage.getItem('accessToken')}`;
            console.log('Retrying request with new token...');
            response = await fetch(url, options);
        }
    }

    return response;
}



async function refreshAccessToken() {
    try {
        const refreshToken = localStorage.getItem('refreshToken');
        const response = await fetch('http://localhost:8080/api/session/refresh-token', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json',
            },
            body: JSON.stringify({ refreshToken }),

        });

        if (!response.ok) {
            console.log('Refresh failed:', response.status);
            return null;
        }

        const data = await response.json();
        console.log('New access token:', data.accessToken);

        localStorage.setItem('accessToken', data.accessToken);
        return data.accessToken;
    } catch (error) {
        console.error('Error refreshing token:', error);
        return null;
    }
}



export { fetchWithAuth };
