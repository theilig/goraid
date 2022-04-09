import React, {useEffect, useState} from "react";
import { useAuth } from "../context/auth";
import axios from 'axios';
import {Navigate, useParams} from "react-router";
import PokemonBlock from "../components/PokemonBlock"
import styled from "styled-components";
import Button from "../components/Button";
import {Error, Success} from "../components/AuthForm";

function Inspect() {
    const { authTokens } = useAuth();
    const [ lastError, setLastError ] = useState("");
    const [ cancelling, setCancelling ] = useState(false)
    const [ lastMessage, setLastMessage ] = useState("")

    useEffect(() => {
        const fetchData = async () => {
            await axios("/api/collection", {
                method: "get",
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'Authorization': 'Bearer ' + authTokens.token
                }
            }).then(result => {
            }).catch(() => {
                setLastError("Could not retrieve pokemon");
            });
        }
        fetchData()
    }, [authTokens.token])

    function cancel() {
        setCancelling(true)
    }

    function submit() {

    }
    if (cancelling) {
        return <Navigate to="/" />
    }
    return (
        <div>
            <Button onClick={submit}>Add Pokemon</Button>
            <Button onClick={cancel}>Cancel</Button>
            <div>
                Adding a pokemon
            </div>
            { lastMessage && <Success>{lastMessage}</Success>}
            { lastError && <Error>{lastError}</Error> }
        </div>
    )
}
export default Inspect;
