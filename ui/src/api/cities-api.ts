const citiesApiUrl = 'http://localhost:8080/v1/cities';
const errorMessage = `Network response was not OK: `

export async function getAllCities(page: number, size: number) {
    return fetchCities(page, size);
}

export async function searchCities(name: string, page: number, size: number) {
    return fetchCities(page, size, name);
}

async function fetchCities(page: number, size: number, name?: string) {
    try {
        const url = (name ? `${citiesApiUrl}/search?name=${name}&` : `${citiesApiUrl}?`) + `page=${page}&size=${size}`;
        const response = await fetch(url, { credentials: 'include' });

        if (!response.ok) {
            throw new Error(`${errorMessage}${response.status}`);
        }
        const data = await response.json();

        return data.content;
    } catch (error) {
        console.error('Error searching cities:', error);
        throw error;
    }
}

export async function updateCity(id: number, newName: string, newPhoto: string) {
    try {
        const response = await fetch(`${citiesApiUrl}/${id}`, {
            method: 'PUT',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({name: newName, photo: newPhoto}),
        });
        if (!response.ok) {
            throw new Error(`${errorMessage}${response.status}`);
        }
    } catch (error) {
        console.error('Error updating city:', error);
        throw error;
    }
}
