import React from "react";
import { Outlet, Navigate, useLocation } from "react-router";
import {useAuth} from "./context/auth";

function PrivateRoutes() {
    const { authTokens } = useAuth();
    const location = useLocation();
    return (authTokens && authTokens.token) ? <Outlet /> : <Navigate to="/login" replace state={{from: location}} />
}

export default PrivateRoutes;
