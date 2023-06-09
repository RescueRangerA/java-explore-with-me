create table if not exists accounts
(
    id       bigint generated by default as identity,
    email    varchar(255),
    username varchar(255),
    primary key (id),
    constraint uq_account_email
        unique (email)
);

create table if not exists event_category
(
    id   bigint generated by default as identity,
    name varchar(255),
    primary key (id),
    constraint uq_category_name
        unique (name)
);

create table if not exists event
(
    id                 bigint generated by default as identity,
    creator_id         bigint,
    status             int,

    category_id        bigint,

    title              varchar(255),
    annotation         text,
    description        text,

    created_on         timestamp without time zone,
    started_on         timestamp without time zone,
    published_on       timestamp without time zone,

    location_latitude  decimal,
    location_longitude decimal,
    paid               boolean,
    participant_limit  bigint,
    request_moderation boolean,

    primary key (id),
    constraint fk_event_creator
        foreign key (creator_id) references accounts,
    constraint fk_event_category
        foreign key (category_id) references event_category
);

create table if not exists event_participation_request
(
    id         bigint generated by default as identity,
    user_id    bigint,
    event_id   bigint,
    created_on timestamp without time zone,
    status     int,
    primary key (id),
    constraint fk_request_user
        foreign key (user_id) references accounts,
    constraint fk_request_event
        foreign key (event_id) references event
);

create table if not exists event_compilation
(
    id     bigint generated by default as identity,
    pinned boolean,
    title  varchar(255),
    primary key (id)
);

create table if not exists compilation_event
(
    id             bigint generated by default as identity,
    compilation_id bigint,
    event_id       bigint,
    primary key (id),
    constraint fk_compilation_event_compilation
        foreign key (compilation_id) references event_compilation,
    constraint fk_compilation_event_event
        foreign key (event_id) references event
);

create table if not exists event_reaction
(
    event_id bigint,
    user_id  bigint,
    is_like  boolean,
    primary key (event_id, user_id),
    constraint fk_event_reaction_event
        foreign key (event_id) references event,
    constraint fk_compilation_reaction_user
        foreign key (user_id) references accounts
);