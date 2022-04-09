import React, {useState} from "react";
import styled from "styled-components";
import ShortButton from "./ShortButton";
import axios from "axios";
import { useAuth } from "../context/auth";

const List = styled.div`
    display: flex;
    flex-direction: row;
    width: 800px;
`;


const LeftColumn = styled.div`
    display: flex;
    flex: 1;
    margin-left: 5px;
`;

const RightColumn = styled.div`
    display: flex;
    flex: 1;
    margin-left: 5px;
`;

const Edit = styled.input`
    display: flex;
    flex-direction: row;
    width: 60px;
`;


function PokemonBlock(props) {
    const p = props.data
    const [ivList, setIvList] = useState(p.iv.attack + "/" + p.iv.defense + "/" + p.iv.hitPoints)
    const [changed, setChanged] = useState(false)
    const [newAttack, setNewAttack] = useState(p.iv.attack)
    const [newDefense, setNewDefense] = useState(p.iv.defense)
    const [newHitPoints, setNewHitPoints] = useState(p.iv.hitPoints)
    const { authTokens } = useAuth();

    function updatePokemon() {
        if (changed) {
            const data = {
                id: p.id,
                stats: {
                    attack: newAttack,
                    defense: newDefense,
                    hitPoints: newHitPoints
                }
            }
            axios("/api/pokemon", {
                method: "put",
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'Authorization': 'Bearer ' + authTokens.token
                },
                data: data
            }).then(_ => {
                setChanged(false)
            }).catch(_ => {
                props.setLastError("Problem updating pokemon, please try again");
            });
        }

    }
    function updateIv(event) {
        setIvList(event.target.value)
        const regexpIv = /([0-9]{1,2})\D([0-9]{1,2})\D([0-9]{1,2})/;
        const match = event.target.value.match(regexpIv)
        if (match && match[3] !== undefined) {
            setNewAttack(parseInt(match[1]))
            setNewDefense(parseInt(match[2]))
            setNewHitPoints(parseInt(match[3]))
            setChanged(true)
        } else {
            setChanged(false)
        }
    }

    return (
        <List>
            <LeftColumn>
                <div>{p.name}</div>
            </LeftColumn>
            <RightColumn>
                <Edit onChange={(e) => updateIv(e)} width="10" type="text" value={ivList}/>
            </RightColumn>
            <RightColumn>
                <div>{p.level / 2}</div>
            </RightColumn>
            <LeftColumn>
                <div>{p.fastMove.name}</div>
            </LeftColumn>
            <LeftColumn>
                <div>{p.chargedMoves.map(m => m.name).join(",")}</div>
            </LeftColumn>
            <LeftColumn>
                {changed && <ShortButton onClick={updatePokemon}>update</ShortButton>}
            </LeftColumn>
            <LeftColumn>
                <ShortButton>delete</ShortButton>
            </LeftColumn>
        </List>
    );
}

export default PokemonBlock;
