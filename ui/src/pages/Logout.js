import React, { useEffect } from "react";
import { Navigate } from 'react-router';
import { useAuth } from "../context/auth";

function Logout() {
    const {logout} = useAuth();
    useEffect(() => {
        logout()
    })
    return <Navigate to="/login"/>;
}

export default Logout;
