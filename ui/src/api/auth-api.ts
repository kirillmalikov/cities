import {UserResponse} from "./api-types";

const authApiUrl = 'http://localhost:8080/v1/user';
const errorMessage = `Network response was not OK: `

export async function signUp(email: string, password: string, isAdmin?: boolean): Promise<UserResponse> {
    try {
        const response = await fetch(authApiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({email: email, password: password, isAdmin: isAdmin}),
        });
        if (!response.ok) {
            throw new Error(`${errorMessage}${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('Error creating user:', error);
        throw error;
    }
}

export async function logIn(email: string, password: string) {
    try {
        const response = await fetch(`${authApiUrl}/authenticate`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({email: email, password: password}),
        });
        if (!response.ok) {
            throw new Error(`${errorMessage}${response.status}`);
        }
    } catch (error) {
        console.error('Error logging in:', error);
        throw error;
    }
}

export async function logOut() {
    try {
        const response = await fetch(`${authApiUrl}/logout`, {
            method: 'POST',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            }
        });
        if (!response.ok) {
            throw new Error(`${errorMessage}${response.status}`);
        }
    } catch (error) {
        console.error('Error logging out:', error);
        throw error;
    }
}

export async function getUser(): Promise<UserResponse> {
    try {
        const response = await fetch(authApiUrl, {
            method: 'GET',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            }
        });
        if (!response.ok) {
            throw new Error(`${errorMessage}${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error('Error fetching user:', error);
        throw error;
    }
}
