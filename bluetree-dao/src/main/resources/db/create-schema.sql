
delete from t_price where stock_id in (select f_id from t_stock where f_name = 'VOW3.DE') and f_date < to_date ('2009-11-16','YYYY-MM-DD');

exec dbms_stats.gather_schema_stats ('BLUETREE');



