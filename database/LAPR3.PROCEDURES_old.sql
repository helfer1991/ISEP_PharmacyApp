create or replace procedure insertNewOrders(
 p_id_order order_entry.id_order%type,
 p_pharmacy_id order_entry.FK_PHARMACY_ID%type,
 p_client_nif order_entry.FK_CLIENT_NIF%type,
 p_shipping_address order_entry.FK_SHIPPMENT_ADDRESS_ID%type,
 p_delivery_fee order_entry.DELIVERY_FEE%type)
is
    --Cursor para consulta Reservas
    CURSOR c_reserva(p_id_reserva reserva.id%type) is
        select *
          from reserva r
         where r.id = p_id_reserva;
    r_reserva c_reserva%rowtype;
    
    v_max_id_order NUMBER;
    v_max_numero_fatura NUMBER;
    
    ex_reserva_invalida exception;
    ex_checkin_not_done exception;
begin
  
  open c_checkin(p_id_reserva);
  fetch c_checkin into r_checkin;
  if c_checkin%notfound then
    raise ex_checkin_not_done;
  end if;
  close c_checkin;
  
  
  
  INSERT INTO fatura(id, numero, data, id_reserva, id_cliente, valor_faturado_reserva) 
    VALUES(v_max_id_fatura,v_max_numero_fatura, sysdate, r_reserva.id, r_reserva.id_cliente, r_reserva.preco);
  
exception
  when ex_reserva_invalida then
    raise_application_error(-20001, 'Reserva inexistente ou invalida');
  when ex_checkin_not_done then
    raise_application_error(-20002, 'Reserva não tem check in realizado');
end;