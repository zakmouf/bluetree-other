

drop table t_holding;
drop table t_portfolio;
drop table t_stock_relation;
drop table t_price;
drop table t_stock;

-- t_stock

create table t_stock (
  f_id           number (12,0) not null,
  f_name         varchar2 (50) not null,
  f_description  varchar2 (255)
);

create unique index ux_stock_1 on t_stock (f_id);
create unique index ix_stock_2 on t_stock (f_name);
alter table t_stock add constraint pk_stock primary key (f_id) using index;

-- t_stock_relation

create table t_stock_relation (
  parent_id  number (12,0) not null,
  child_id   number (12,0) not null
);

create unique index ux_stock_relation_1 on t_stock_relation (parent_id, child_id);
create unique index ux_stock_relation_2 on t_stock_relation (child_id, parent_id);
alter table t_stock_relation add constraint pk_stock_relation primary key (parent_id, child_id) using index;
create index ix_stock_relation_1 on t_stock_relation (parent_id);
create index ix_stock_relation_2 on t_stock_relation (child_id);
alter table t_stock_relation add constraint fk_stock_relation_1 foreign key (parent_id) references t_stock (f_id) on delete cascade;
alter table t_stock_relation add constraint fk_stock_relation_2 foreign key (child_id) references t_stock (f_id) on delete cascade;

-- t_price

create table t_price (
  stock_id  number (12,0) not null,
  f_date    date not null,
  f_value   number not null
);

create unique index ux_price_1 on t_price (stock_id, f_date);
create unique index ux_price_2 on t_price (f_date, stock_id);
alter table t_price add constraint pk_price primary key (stock_id, f_date) using index;
create index ix_price_1 on t_price (stock_id);
alter table t_price add constraint fk_price_1 foreign key (stock_id) references t_stock (f_id) on delete cascade;

-- t_portfolio

create table t_portfolio (
  f_id           number (12,0) not null,
  f_name         varchar(50) not null,
  f_start_date   date not null,
  indice_id      number (12,0) not null
);

create unique index ux_portfolio_1 on t_portfolio (f_id);
alter table t_portfolio add constraint pk_portfolio primary key (f_id) using index;
create index ix_portfolio_1 on t_portfolio (indice_id);
alter table t_portfolio add constraint fk_portfolio_1 foreign key (indice_id) references t_stock (f_id);

-- t_holding

create table t_holding (
  portfolio_id  number (12,0) not null,
  f_quantity    number not null,
  stock_id      number (12,0) not null
);

create unique index ux_holding_1 on t_holding (portfolio_id, stock_id);
create unique index ux_holding_2 on t_holding (stock_id, portfolio_id);
alter table t_holding add constraint pk_holding primary key (portfolio_id, stock_id) using index;
create index ix_holding_1 on t_holding (portfolio_id);
create index ix_holding_2 on t_holding (stock_id);
alter table t_holding add constraint fk_holding_1 foreign key (portfolio_id) references t_portfolio (f_id) on delete cascade;
alter table t_holding add constraint fk_holding_2 foreign key (stock_id) references t_stock (f_id);
