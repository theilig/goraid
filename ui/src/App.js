import React, { useState } from 'react';
import { BrowserRouter as Router, Route } from "react-router-dom"
import Login from './pages/Login';
import Signup from "./pages/Signup";
import Pokemon from "./pages/Pokemon"
import { AuthContext } from "./context/auth";
import PrivateRoutes from "./PrivateRoutes";
import axios from "axios";
import {Routes} from "react-router";
import Logout from "./pages/Logout";
import AddPokemon from "./pages/AddPokemon";
import Inspect from "./pages/Inspect";

function App(props) {
    let existingTokens = localStorage.getItem("tokens");
    try {
        existingTokens = JSON.parse(existingTokens);
    } catch (e) {
        existingTokens = {};
    }
    const [authTokens, setAuthTokens] = useState(existingTokens);
    const [isLoggedIn, setIsLoggedIn] = useState(existingTokens && existingTokens.tokens != null)

    const setTokens = (data) => {
        localStorage.setItem("tokens", JSON.stringify(data));
        setAuthTokens(data);
    }

    const logout = () => {
        setTokens({})
        setIsLoggedIn(false)
        return ""
    }

    const confirm = (email, token) => {
        axios("/api/confirm", {
            data: {
                email,
                token
            },
            method: "post",
            headers: {'X-Requested-With': 'XMLHttpRequest'},
            withCredentials: true
        }).then(result => {
            setTokens(result.data);
            setIsLoggedIn(true)
            return null
        }).catch(error => {
            return error
        });
    }

    const NotFound = () => (
        <div>
            <h1>404 - Not Found!</h1>
        </div>
    )

    return (
        <AuthContext.Provider value={{ authTokens, confirm: confirm, logout: logout, isLoggedIn: isLoggedIn }}>
            <Router>
                <Routes>
                    <Route path="/" element={<PrivateRoutes />}>
                        <Route path="/" element={<Pokemon />} />
                        <Route path="/add-pokemon" element={<AddPokemon />} />
                        <Route path="/inspect/:pokemonId" element={<Inspect />} />
                    </Route>
                    <Route path="/login" element={<Login />} />
                    <Route path="/signup" element={<Signup />} />
                    <Route path="/confirm/:email/:confirmationToken" element={<Pokemon />} />
                    <Route path="/logout" element={<Logout />} />
                    <Route element={NotFound} />
                </Routes>
            </Router>
        </AuthContext.Provider>
    );
}
export default App;
