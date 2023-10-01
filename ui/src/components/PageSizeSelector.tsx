import React from 'react';

interface PageSizeSelectorProps {
    pageSize: number;
    onPageChange: (pageSize: number) => void;
}

const PageSizeSelector: React.FC<PageSizeSelectorProps> = ({ pageSize, onPageChange }) => {
    const options = [10, 20, 50, 100, 200];

    const handlePageSizeChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const newSize = parseInt(event.target.value, 10);
        onPageChange(newSize);
    };

    return (
        <div className="page-size-selector">
            <label htmlFor="pageSize">Page Size: </label>
            <select id="pageSize" value={pageSize} onChange={handlePageSizeChange}>
                {options.map((option) => (
                    <option key={option} value={option}>
                        {option}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default PageSizeSelector;
