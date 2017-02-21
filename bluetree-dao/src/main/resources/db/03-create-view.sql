
-- v_price

create or replace view v_price
as
select   p.stock_id,
         p.f_date as price_date,
         p.f_value as price_value
from     t_price p;

-- v_stock

create or replace view v_stock
as
select s.f_id as stock_id,
       s.f_name as stock_name,
       s.f_description as stock_description,
       p_.stock_date_count,
       p_.stock_first_date,
       p_.stock_last_date
from   t_stock s
       left join (select   p.stock_id,
                           count (1) as stock_date_count,
                           min (p.price_date) as stock_first_date,
                           max (p.price_date) as stock_last_date
                  from     v_price p
                  group by p.stock_id) p_
              on p_.stock_id (+)= s.f_id;

-- v_portfolio

create or replace view v_portfolio
as
select p.f_id as portfolio_id,
       p.f_name as portfolio_name,
       p.f_start_date as portfolio_start_date,
       i.stock_id as indice_id,
       i.stock_name as indice_name,
       i.stock_description as indice_description,
       i.stock_date_count as indice_date_count,
       i.stock_first_date as indice_first_date,
       i.stock_last_date as indice_last_date
from   t_portfolio p
       join v_stock i
         on i.stock_id = p.indice_id;

-- v_holding

create or replace view v_holding
as
select h.portfolio_id,
       h.f_quantity as holding_quantity,
       s.*
from   t_holding h
       join v_stock s
         on s.stock_id = h.stock_id
