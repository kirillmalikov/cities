import React, {useEffect, useState} from 'react';
import CityList from './components/CityList';
import './css/App.css'
import Header from "./components/Header";
import {SearchProvider} from "./components/SearchContext";

const App: React.FC = () => {
    const [darkMode, setDarkMode] = useState(false);

    useEffect(() => {
        if (localStorage.getItem('theme') === 'dark') {
            setDarkMode(true);
        }
    }, []);

    const toggleTheme = () => {
        setDarkMode(!darkMode);
    };

    return (
        <div className={`container ${darkMode ? 'dark' : ''}`}>
            <SearchProvider>
                <div className={`App ${darkMode ? 'dark' : ''}`}>
                    <Header darkMode={darkMode} onToggleTheme={toggleTheme}/>
                    <CityList/>
                </div>
            </SearchProvider>

        </div>
    );
}

export default App;
