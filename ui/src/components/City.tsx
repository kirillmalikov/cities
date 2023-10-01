import React, {useEffect, useState} from 'react';
import '../css/City.css'
import '../css/Edit.css'
import {updateCity} from "../api/cities-api";
import {UserResponse} from "../api/api-types";

interface CityProps {
    city: {
        id: number;
        name: string;
        photo: string;
    };
    onEdit?: (id: number, newName: string, newPhoto: string) => void;
}

const City: React.FC<CityProps> = ({city, onEdit}) => {
    const [name, setName] = useState(city.name);
    const [photo, setPhoto] = useState(city.photo);
    const [isEditing, setIsEditing] = useState(false);
    const [newName, setNewName] = useState(city.name);
    const [newPhoto, setNewPhoto] = useState(city.photo);
    const [randomSize, setRandomSize] = useState({value: 0});
    const user = localStorage.getItem('user');

    useEffect(() => {
        const screenWidth = window.innerWidth;
        const maxSize = 300;

        const randomWidth = Math.floor(Math.random() * maxSize) + 200;

        setRandomSize({
            value: randomWidth > screenWidth ? screenWidth : randomWidth
        });
    }, []);

    const cityBackgroundStyle: React.CSSProperties = {
        backgroundImage: `url(${photo})`,
        backgroundSize: 'cover',
        borderRadius: '50%',
        width: randomSize.value + 'px',
        height: randomSize.value + 'px',
        position: 'relative',
        margin: '20px',
        outline: '1px solid #FFE58DFF',
        outlineOffset: '-20px',
        boxShadow: '5px 5px 10px rgba(0, 0, 0, 0.5)',
    };

    const cityNameStyle: React.CSSProperties = {
        position: 'absolute',
        top: '50%',
        left: '50%',
        transform: 'translate(-50%, -50%)',
        zIndex: 1,
        color: 'white',
        fontSize: randomSize.value * 0.15 + 'px',
        textShadow: '2px 2px 4px rgba(0, 0, 0, 0.5)',
    };

    const toggleEdit = () => {
        if (user) {
            const userResponse: UserResponse = JSON.parse(user);
            if (userResponse.roles.map(role => role.name).includes('ROLE_ALLOW_EDIT')) {
                setIsEditing(!isEditing);
            }
        }
    };

    const handleSave = async () => {
        await updateCity(city.id, newName, newPhoto)
        setName(newName);
        setPhoto(newPhoto);
        setIsEditing(false);
    };

    return (
        <div className="city-container" onDoubleClick={toggleEdit}>
            <div className="city-photo" style={cityBackgroundStyle}>
                <div style={cityNameStyle}>{name}</div>
            </div>
            {isEditing ? (
                <div className="edit">
                    <div className="edit-dropdown active">
                        <p>
                            <input
                                type="text"
                                value={newName}
                                onChange={(e) => setNewName(e.target.value)}
                            />
                        </p>
                        <p>
                            <input
                                type="text"
                                value={newPhoto}
                                onChange={(e) => setNewPhoto(e.target.value)}
                            />
                        </p>
                        <button onClick={handleSave}>Save</button>
                    </div>
                </div>
            ) : <></>}
        </div>
    );
};

export default City;
