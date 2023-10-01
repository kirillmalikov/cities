import React, {useEffect, useState} from 'react';
import {useSearchContext} from "./SearchContext";
import {getAllCities, searchCities} from '../api/cities-api';
import City from './City';
import PageSizeSelector from './PageSizeSelector';
import '../css/CityList.css'

const CityList: React.FC = () => {
    const [cities, setCities] = useState([]);
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const user = localStorage.getItem('user');
    const searchQuery = useSearchContext().searchQuery

    useEffect(() => {
        async function fetchData() {
            if (user) {
                try {
                    let data;
                    if (searchQuery) {
                        setCurrentPage(0);
                        data = await searchCities(searchQuery, currentPage, pageSize)
                    } else {
                        data = await getAllCities(currentPage, pageSize);
                    }
                    setCities(data);
                } catch (error) {
                    // Handle errors here
                }
            }
        }

        fetchData();
    }, [currentPage, pageSize, user, searchQuery]);

    return (
        <div>
            <div className="city-list">
                {cities.map((city) => (
                    <City key={city["id"]} city={city}/>
                ))}
            </div>
            {cities.length > 0 ? (
                <div className="pagination">
                    <button
                        onClick={() => setCurrentPage((prevPage) => prevPage - 1)}
                        disabled={currentPage === 0}
                    >
                        Previous
                    </button>
                    <span> Page {currentPage + 1} </span>
                    <button
                        onClick={() => setCurrentPage((prevPage) => prevPage + 1)}
                        disabled={cities.length < pageSize}
                    >
                        Next
                    </button>
                    <PageSizeSelector pageSize={pageSize} onPageChange={setPageSize}/>
                </div>
            ) : <></>}
        </div>
    );
};

export default CityList;
