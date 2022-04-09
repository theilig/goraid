import React, {useEffect, useState} from "react";
import { useAuth } from "../context/auth";
import axios from 'axios';
import {Navigate, useParams} from "react-router";
import PokemonBlock from "../components/PokemonBlock"
import styled from "styled-components";
import Button from "../components/Button";
import {Error} from "../components/AuthForm";
import PokemonList from "../components/PokemonList";

const H2 = styled.h2`
    
`;

function Pokemon() {
    const { authTokens, logout } = useAuth();
    const [ pokemonList, setPokemonList ] = useState({pokemon:[]});
    const [ lastError, setLastError ] = useState("");
    const [ adding, setAdding ] = useState(false)
    const [ selectedPokemon, setSelectedPokemon ] = useState(undefined)

    function logOutUser() {
        logout()
        setLastError("Logged out");
    }

    function addPokemon() {
        setAdding(true)
    }

    useEffect(() => {
        const fetchData = async () => {
            await axios("/api/collection", {
                method: "get",
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'Authorization': 'Bearer ' + authTokens.token
                }
            }).then(result => {
                setPokemonList({"pokemon": result.data});
            }).catch(() => {
                setLastError("Could not retrieve pokemon");
            });
        }
        fetchData()
    }, [authTokens.token])

    if (selectedPokemon > 0) {
        let location = "/inspect/" + selectedPokemon;
        return (<Navigate push to={location} />);
    } else if (adding) {
        return <Navigate push to={"/add-pokemon"} />
    }
    return (
        <div>
            <Button onClick={addPokemon}>Add Pokemon</Button>
            <Button onClick={logOutUser}> Log out</Button>
            <H2>Current Pokemon</H2>
            <div>
                {pokemonList && <PokemonList data={pokemonList} setLastError={setLastError} />}
            </div>
            { lastError && <Error>{lastError}</Error> }
        </div>
    )
}
export default Pokemon;
