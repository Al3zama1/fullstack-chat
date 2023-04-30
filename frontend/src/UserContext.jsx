import {createContext, useEffect, useState} from "react";
import axios from "axios";
import jwtDecode from "jwt-decode";

export const UserContext = createContext({})

export function UserContextProvider({children}) {
    const [username, setUsername] = useState(null);
    const [id, setId] = useState(null);
    useEffect(() => {
        const jwtToken = localStorage.getItem("jwtToken");

        // TODO remove jwt from local storage when token validation check fails in the server
        if (jwtToken) {
            const {sub, userId, exp} =  jwtDecode(jwtToken);
            if (exp < Date.now() / 1000) localStorage.removeItem("jwtToken");
            else {
                setUsername(sub);
                setId(userId);
            }
        }
    }, [])
    return (
        <UserContext.Provider value={{username, setUsername, id, setId}}>
            {children}
        </UserContext.Provider>
    )
}

