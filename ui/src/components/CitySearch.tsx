import React, { useState } from 'react';

interface CitySearchProps {
    onSearch: (query: string) => void;
}

const CitySearch: React.FC<CitySearchProps> = ({ onSearch }) => {
    const [searchQuery, setSearchQuery] = useState('');

    const handleSearch = () => {
        onSearch(searchQuery);
    };

    return (
        <div style={{marginRight: 'auto'}}>
            <input
                type="text"
                placeholder="Search by city name"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
            />
            <> </>
            <button className="searchButton" onClick={handleSearch}>Search</button>
        </div>
    );
};

export default CitySearch;
