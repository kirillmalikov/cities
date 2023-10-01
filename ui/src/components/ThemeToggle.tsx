import React, {useEffect, useRef} from 'react';
import '../css/ThemeToggle.css';

interface ThemeToggleProps {
    darkMode: boolean;
    onToggle: () => void;
}

const ThemeToggle: React.FC<ThemeToggleProps> = ({darkMode, onToggle}) => {
    const sliderRef = useRef<HTMLInputElement>(null);

    useEffect(() => {
        if (sliderRef.current) {
            sliderRef.current.style.setProperty('--rotation', darkMode ? '0deg' : '180deg');
        }

        localStorage.setItem('theme', darkMode ? 'dark' : '');
    }, [darkMode]);

    return (
        <>
            <input
                ref={sliderRef}
                type="range"
                min="0"
                max="1"
                step="1"
                value={darkMode ? '1' : '0'}
                className="theme-toggle"
                onChange={onToggle}
            />
        </>
    );
};

export default ThemeToggle;
