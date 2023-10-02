import React, {useState} from 'react';
import '../css/Header.css';
import Account from "./Account";
import CitySearch from "./CitySearch";
import {useSearchContext} from "./SearchContext";

interface HeaderProps {
    darkMode: boolean;
    onToggleTheme: () => void;
}

const Header: React.FC<HeaderProps> = ({darkMode, onToggleTheme}) => {
    const { setSearchQuery } = useSearchContext();
    const [isAccountDropdownOpen, setIsAccountDropdownOpen] = useState(false);
    const handleMouseEnter = () => {
        setIsAccountDropdownOpen(true);
    };

    const handleMouseLeave = () => {
        setIsAccountDropdownOpen(false);
    };

    const handleCitySearch = (query: string) => {
        setSearchQuery(query);
    };

    return (
        <div className={`header ${darkMode ? 'dark' : ''}`}>
            <div className={`logo ${darkMode ? 'dark' : ''}`}/>
            <h1>City List</h1>
            <CitySearch onSearch={handleCitySearch} />
            <div className="header-right">
                <div className="account-text" onMouseEnter={handleMouseEnter}>
                    Account
                    <div onMouseLeave={handleMouseLeave}>
                        {isAccountDropdownOpen && <Account darkMode={darkMode} onToggleTheme={onToggleTheme}/>}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Header;
