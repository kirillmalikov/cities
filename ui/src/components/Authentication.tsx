import React, {useState, useEffect} from 'react';
import {signUp, logIn, getUser, logOut} from '../api/auth-api';

interface User {
    email: string;
    password: string;
    isAdmin: boolean;
}

const Authentication: React.FC = () => {
    const [user, setUser] = useState<User>({email: '', password: '', isAdmin: false});
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        if (localStorage.getItem('user')) {
            setIsLoggedIn(true);
        }
    }, []);

    const handleSignup = async () => {
        try {
            const userResponse = await signUp(user.email, user.password, user.isAdmin);
            await logIn(user.email, user.password);
            localStorage.setItem('user', JSON.stringify(userResponse));

            setIsLoggedIn(true);
            window.location.reload();
        } catch (error) {
            console.error('Error creating user or logging in:', error);
            // Handle the error as needed (e.g., show an error message to the user).
        }
    };

    const handleLogin = async () => {
        try {
            await logIn(user.email, user.password);
            const userResponse = await getUser();
            localStorage.setItem('user', JSON.stringify(userResponse));

            setIsLoggedIn(true);
            window.location.reload();
        } catch (error) {
            console.error('Error logging in:', error);
            // Handle the error as needed (e.g., show an error message to the user).
        }
    };

    const handleLogout = async () => {
        await logOut();
        localStorage.removeItem('user');
        setIsLoggedIn(false);
        window.location.reload();
    };

    return (
        <div>
            {!isLoggedIn ? (
                <>
                    <p><input
                        type="text"
                        placeholder="Email"
                        value={user.email}
                        onChange={(e) => setUser({...user, email: e.target.value})}
                    />
                    </p>
                    <p>
                        <input
                            type="password"
                            placeholder="Password"
                            value={user.password}
                            onChange={(e) => setUser({...user, password: e.target.value})}
                        />
                    </p>
                    <p>
                        <>Allow edit</>
                        <input
                            type={"checkbox"}
                            checked={user.isAdmin}
                            onChange={(e) => setUser({...user, isAdmin: e.target.checked})}
                        />
                    </p>
                    <button onClick={handleSignup}>Sign Up</button>
                    <> </>
                    <button onClick={handleLogin}>Login</button>
                </>
            ) : (
                <button onClick={handleLogout}>Logout</button>
            )}
        </div>
    );
};

export default Authentication;
