Drop TABLE DataEntity;

Drop TABLE LINK;

Drop TABLE ExecuteStart;

CREATE TABLE Link (
uuid bigserial NOT NULL PRIMARY KEY,
link varchar(2048)
);

CREATE TABLE ExecuteStart (
uuid bigserial NOT NULL PRIMARY KEY,
dateTime timestamp
);

CREATE TABLE DataEntity (
uuid bigserial NOT NULL PRIMARY KEY,
date varchar(1280),
name varchar(1280),
win_first varchar(128),
draw varchar(128),
win_second varchar(128),
event_number varchar(128),
resource bigserial  REFERENCES Link (uuid),
startId bigserial  REFERENCES ExecuteStart (uuid)

);
