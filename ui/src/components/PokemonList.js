import React, {useState} from "react";
import PokemonBlock from "./PokemonBlock";

function PokemonList(props) {
    return (
        <div>
            {props.data.pokemon.map((p => <PokemonBlock key={p.id} data={p} setLastError={props.setLastError}/> ))}
        </div>
    )
}

export default PokemonList;
