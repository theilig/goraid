import React, {useEffect, useState} from "react";
import { useAuth } from "../context/auth";
import axios from 'axios';
import { Navigate } from "react-router";
import styled from "styled-components";
import Button from "../components/Button";
import { Error, Success } from "../components/AuthForm";
import PokemonList from "../components/PokemonList";

const PokemonPicker = styled.select`
    width: 200px;
    margin-right: 10px;
    line-height: 1.1;
    background: none;
`;

const AttackPicker = styled.select`
    width: 200px;
    margin-right: 10px;
    line-height: 1.1;
    background: none;
`;

const Form = styled.div`
    display: flex;
    flex-direction: column;
    width: 100%;
`;

const Headings = styled.div`
    display: flex;
    flex-direction: row;
    width: 100%;
`;

const Input = styled.input`
    display: flex;
    flex-direction: column;
    width: 82%
`;


function AddPokemon() {
    const { authTokens } = useAuth();
    const [ pokedex, setPokedex ] = useState({});
    const [ lastError, setLastError ] = useState("");
    const [ lastMessage, setLastMessage ] = useState("")
    const [ cancelling, setCancelling ] = useState(false)
    const [ combatPower, setCombatPower ] = useState("")
    const [ pokemon, setPokemon ] = useState(0)
    const [ chargedMoves, setChargedMoves ] = useState([])
    const [ fastMoves, setFastMoves ] = useState([])
    const [ chargedMove, setChargedMove ] = useState("None")
    const [ alternateChargedMove, setAlternateChargedMove ] = useState("None")
    const [ fastMove, setFastMove ] = useState("None")
    const [ addedPokemon, setAddedPokemon ] = useState([])

    function submit() {
        if (combatPower > 0) {
            const data = {
                id: pokemon.id,
                combatPower: parseInt(combatPower),
                fastMove: fastMove,
                chargedMove: chargedMove
            }
            if (alternateChargedMove !== "None") {
                data["alternateChargedMove"] = alternateChargedMove
            }
            axios("/api/pokemon", {
                method: "post",
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'Authorization': 'Bearer ' + authTokens.token
                },
                data: data
            }).then(result => {
                const newAddedPokemon = [...addedPokemon]
                newAddedPokemon.push(result.data)
                setAddedPokemon(newAddedPokemon)
                setCombatPower("")
            }).catch(error => {
                setLastError("Problem adding pokemon, please try again");
            });

        } else {
            setLastError("You need to enter the combat points")
        }
    }

    function cancel() {
        setCancelling(true)
    }

    function updatePokemon(event) {
        const selectedPokemon = pokedex[event.target.value]
        setPokemon(selectedPokemon)
        const fastMoves = selectedPokemon.allFastMoves.split(",")
        const chargedMoves = selectedPokemon.allChargedMoves.split(",")
        setFastMoves(fastMoves)
        setChargedMoves(chargedMoves)
        setFastMove(fastMoves[0])
        setChargedMove(chargedMoves[0])
        setAlternateChargedMove("None")
    }

    useEffect(() => {
        const fetchData = async () => {
            await axios("/api/pokedex", {
                method: "get",
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'Authorization': 'Bearer ' + authTokens.token
                }
            }).then(result => {
                setPokedex(result.data);
            }).catch(() => {
                setLastError("Could not retrieve pokedex");
            });
        }
        fetchData()
    }, [authTokens.token])

    if (cancelling) {
        return <Navigate to="/" />
    }
    return (
        <div>
            {addedPokemon && <PokemonList data={{pokemon: addedPokemon}} />}
            <Headings>
                <Form>
                    <div>Combat Points</div>
                    <Input type="text" value={combatPower}
                           onChange={e => {setCombatPower(e.target.value);}}
                           placeholder="CP"
                    />

                </Form>
                <Form>
                    <div>Pokemon</div>
                    <PokemonPicker name="pokemon" onChange={(e) => updatePokemon(e)}>
                        {Object.keys(pokedex).sort().map((key) => <option key={pokedex[key].id}>{key}</option>)}
                    </PokemonPicker>
                </Form>
                <Form>
                    <div>Fast Move</div>
                    <AttackPicker name="fast" onChange={(e) => setFastMove(e.target.value)}>
                        {fastMoves.map((move) => <option key={move}>{move}</option>)}
                    </AttackPicker>
                </Form>
                <Form>
                    <div>Charged Move</div>
                    <AttackPicker name="charged" onChange={(e) => setChargedMove(e.target.value)}>
                       {chargedMoves.map((move) => <option key={move}>{move}</option>)}
                    </AttackPicker>
                </Form>
                <Form>
                    <div>Additional CM</div>
                    <AttackPicker name="additional_charged" onChange={(e) => setAlternateChargedMove(e.target.value)}>
                        {["none"].concat(chargedMoves).map((move) => <option key={move}>{move}</option>)}
                    </AttackPicker>
                </Form>
            </Headings>
            <Button onClick={submit}>Add Pokemon</Button>
            <Button onClick={cancel}>Cancel</Button>
            { lastMessage && <Success>{lastMessage}</Success>}
            { lastError && <Error>{lastError}</Error> }
        </div>
    )
}
export default AddPokemon;
