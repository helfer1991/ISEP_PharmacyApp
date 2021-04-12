CREATE OR REPLACE TRIGGER trginsertCourierTypeStatus
AFTER
INSERT
ON courier
FOR EACH ROW

DECLARE
status_id COURIER_STATUS.FK_COURIER_TYPE_STATUS_ID%TYPE;

BEGIN
    
    SELECT ID_COURIER_TYPE_STATUS INTO status_id
    FROM COURIER_TYPE_STATUS
    WHERE description='Free';


    INSERT INTO COURIER_STATUS (FK_PERSON_NIF, FK_COURIER_TYPE_STATUS_ID,Date_Entry) VALUES (:new.FK_PERSON_NIF, status_id, to_date(sysdate, 'YYYY-MM-DD HH24:MI:SS'));
     
END;