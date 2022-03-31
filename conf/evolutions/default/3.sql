-- !Ups
ALTER TABLE UserPokemon ADD COLUMN fast_move VARCHAR(300) NOT NULL, ADD COLUMN charged_moves VARCHAR(1024) NOT NULL;

-- !Downs
ALTER TABLE UserPokemon DROP COLUMN fast_move, DROP COLUMN charged_moves;
