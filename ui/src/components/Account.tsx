import Authentication from './Authentication'; // Import the Authentication component
import '../css/Account.css';
import ThemeToggle from "./ThemeToggle";
import React from "react";

interface AccountProps {
    darkMode: boolean;
    onToggleTheme: () => void;
}

const Account: React.FC<AccountProps> = ({darkMode, onToggleTheme}) => {
    return (
        <div className="header-right">
            <div className={`account ${darkMode ? 'dark' : ''}`}>
                <div className="account-dropdown active">
                    <Authentication/>
                    <p>Theme</p>
                    <ThemeToggle darkMode={darkMode} onToggle={onToggleTheme}/>
                </div>
            </div>
        </div>
    );
};

export default Account;
