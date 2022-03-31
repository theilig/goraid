-- !Ups
CREATE TABLE UserPokemon (
    user_pokemon_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    user_id INTEGER NOT NULL,
    pokemon_id INTEGER NOT NULL,
    combat_power INTEGER NOT NULL,
    iv_attack INTEGER NOT NULL,
    iv_defense INTEGER NOT NULL,
    iv_hit_points INTEGER NOT NULL,
    min_level INTEGER NOT NULL,
    max_level INTEGER NOT NULL,
    INDEX user_id (user_id)
);
-- !Downs
DROP TABLE UserPokemon;
