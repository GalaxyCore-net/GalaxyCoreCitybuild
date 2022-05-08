CREATE TABLE IF NOT EXISTS galaxycity_playerdb
(
    ID        int,
    invtoggle bit default 0,
    ectoggle  bit default 0,
    tptoggle  bit default 0,
    tpatoggle bit default 0
);

CREATE TABLE IF NOT EXISTS galaxycity_warps
(
    pos int NOT NULL,
    loc varchar(100),
    name varchar(100),
    display varchar(100)
);