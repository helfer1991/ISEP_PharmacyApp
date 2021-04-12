begin
    for r in (select 'drop table ' || table_name || ' cascade constraints' cmd from user_tables order by table_name)
    loop
    execute immediate(r.cmd);
    end loop;
end;
/

