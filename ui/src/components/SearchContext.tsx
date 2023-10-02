import React, { createContext, useContext, useState, ReactNode } from 'react';

interface SearchContextProps {
    searchQuery: string;
    setSearchQuery: React.Dispatch<React.SetStateAction<string>>;
}

const SearchContext = createContext<SearchContextProps | undefined>(undefined);

export const useSearchContext = () => {
    const context = useContext(SearchContext);
    if (!context) {
        throw new Error('useSearchContext must be used within a SearchProvider');
    }
    return context;
};

interface SearchProviderProps {
    children: ReactNode;
}

export const SearchProvider: React.FC<SearchProviderProps> = ({ children }) => {
    const [searchQuery, setSearchQuery] = useState('');

    return (
        <SearchContext.Provider value={{ searchQuery, setSearchQuery }}>
            {children}
        </SearchContext.Provider>
    );
};
